/*
 * Property of Akurateco (https://akurateco.com).
 */

package com.akurateco.sample.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.get
import com.akurateco.sample.R
import com.akurateco.sample.app.AkuratecoTransactionStorage
import com.akurateco.sample.app.preattyPrint
import com.akurateco.sample.databinding.ActivityRecurringSaleBinding
import com.akurateco.sample.databinding.ActivitySaleBinding
import com.akurateco.sdk.core.AkuratecoSdk
import com.akurateco.sdk.model.request.card.AkuratecoTestCard
import com.akurateco.sdk.model.request.options.AkuratecoRecurringOptions
import com.akurateco.sdk.model.request.options.AkuratecoSaleOptions
import com.akurateco.sdk.model.request.order.AkuratecoOrder
import com.akurateco.sdk.model.request.order.AkuratecoSaleOrder
import com.akurateco.sdk.model.request.payer.AkuratecoPayer
import com.akurateco.sdk.model.request.payer.AkuratecoPayerOptions
import com.akurateco.sdk.model.response.base.error.AkuratecoError
import com.akurateco.sdk.model.response.sale.AkuratecoSaleCallback
import com.akurateco.sdk.model.response.sale.AkuratecoSaleResponse
import com.akurateco.sdk.model.response.sale.AkuratecoSaleResult
import io.kimo.lib.faker.Faker
import java.text.DecimalFormat
import java.util.*

class AkuratecoRecurringSaleActivity : AppCompatActivity(R.layout.activity_recurring_sale) {

    private lateinit var binding: ActivityRecurringSaleBinding
    private lateinit var akuratecoTransactionStorage: AkuratecoTransactionStorage

    private var selectedTransaction: AkuratecoTransactionStorage.Transaction? = null
    private var transactions: List<AkuratecoTransactionStorage.Transaction>? = null

    private val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        akuratecoTransactionStorage = AkuratecoTransactionStorage(this)
        binding = ActivityRecurringSaleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureView()
    }

    private fun configureView() {
        binding.btnLoadRecurringSale.setOnClickListener {
            transactions = akuratecoTransactionStorage.getRecurringSaleTransactions()
            invalidateSpinner()
        }
        binding.btnLoadAll.setOnClickListener {
            transactions = akuratecoTransactionStorage.getAllTransactions()
            invalidateSpinner()
        }
        binding.btnRandomize.setOnClickListener {
            randomize()
        }
        binding.btnAuth.setOnClickListener {
            executeRequest(true)
        }
        binding.btnSale.setOnClickListener {
            executeRequest(false)
        }

        invalidateSpinner()
    }

    private fun invalidateSpinner() {
        binding.spinnerTransactions.apply {
            val prettyTransactions = transactions
                .orEmpty()
                .map { it.toString() }
                .toMutableList()
                .apply {
                    add(0, "Select Transaction")
                }

            adapter = object : ArrayAdapter<String>(
                this@AkuratecoRecurringSaleActivity,
                android.R.layout.simple_spinner_dropdown_item,
                prettyTransactions
            ) {
                override fun isEnabled(position: Int): Boolean {
                    return position != 0
                }

                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                    return super.getDropDownView(position, convertView, parent).apply {
                        alpha = if (position == 0) 0.5F else 1.0F
                    }
                }
            }

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    parent?.get(0)?.alpha = if (position == 0) 0.5F else 1.0F

                    if (transactions.isNullOrEmpty()) {
                        invalidateSelectedTransaction()
                        return
                    }

                    selectedTransaction = if (position == 0) {
                        null
                    } else {
                        transactions?.get((position - 1).coerceAtLeast(0))
                    }

                    invalidateSelectedTransaction()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selectedTransaction = null
                    invalidateSelectedTransaction()
                }
            }

            invalidateSelectedTransaction()
        }

    }

    private fun invalidateSelectedTransaction() {
        binding.txtSelectedTransaction.text = selectedTransaction?.preattyPrint()

        binding.btnAuth.isEnabled = selectedTransaction != null
        binding.btnSale.isEnabled = selectedTransaction != null

        binding.etxtRecurringFirstTransId.setText(selectedTransaction?.id)
        binding.etxtRecurringToken.setText(selectedTransaction?.recurringToken)
    }

    private fun randomize() {
        binding.etxtOrderId.setText(UUID.randomUUID().toString())
        binding.etxtOrderAmount.setText(DecimalFormat("#.##").format(random.nextDouble() * 10_000))
        binding.etxtOrderDescription.setText(Faker.Lorem.sentences())

        binding.txtResponse.text = ""
    }

    private fun onRequestStart() {
        binding.progressBar.show()
        binding.txtResponse.text = ""
    }

    private fun onRequestFinish() {
        binding.progressBar.hide()
    }

    private fun executeRequest(isAuth: Boolean) {
        selectedTransaction?.let { selectedTransaction ->
            val amount = try {
                binding.etxtOrderAmount.text.toString().toDouble()
            } catch (e: Exception) {
                0.0
            }

            val order = AkuratecoOrder(
                id = binding.etxtOrderId.text.toString(),
                amount = amount,
                description = binding.etxtOrderDescription.text.toString()
            )

            val recurringOptions = AkuratecoRecurringOptions(
                firstTransactionId = binding.etxtRecurringFirstTransId.text.toString(),
                token = binding.etxtRecurringToken.text.toString()
            )

            val transaction = AkuratecoTransactionStorage.Transaction(
                payerEmail = selectedTransaction.payerEmail,
                cardNumber = selectedTransaction.cardNumber
            )

            onRequestStart()
            AkuratecoSdk.Adapter.RECURRING_SALE.execute(
                order = order,
                options = recurringOptions,
                payerEmail = selectedTransaction.payerEmail,
                cardNumber = selectedTransaction.cardNumber,
                auth = isAuth,
                callback = object : AkuratecoSaleCallback {
                    override fun onResponse(response: AkuratecoSaleResponse) {
                        super.onResponse(response)
                        onRequestFinish()
                        binding.txtResponse.text = response.preattyPrint()
                    }

                    override fun onResult(result: AkuratecoSaleResult) {
                        transaction.fill(result.result)
                        transaction.isAuth = isAuth
                        if (result is AkuratecoSaleResult.Recurring) {
                            transaction.recurringToken = result.result.recurringToken
                        }

                        akuratecoTransactionStorage.addTransaction(transaction)
                    }

                    override fun onError(error: AkuratecoError) = Unit

                    override fun onFailure(throwable: Throwable) {
                        super.onFailure(throwable)
                        onRequestFinish()
                        binding.txtResponse.text = throwable.preattyPrint()
                    }
                }
            )
        } ?: invalidateSelectedTransaction()
    }
}
