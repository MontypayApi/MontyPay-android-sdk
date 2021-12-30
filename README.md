![](https://jitpack.io/v/MontyPay/MontyPay-android-sdk.svg) | [View SDK Wiki](https://github.com/MontypayApi/MontyPay-android-sdk/wiki) | [Report new issue](https://github.com/MontypayApi/MontyPay-android-sdk/issues/new)

# MontyPay Android SDK

Monty Pay Is a global payment gateway founded as a subsidiary of Monty Capital with a HQ in Switzerland and offices in UK, Cyprus, Lebanon, Nigeria, and UAE. It is a smart and powerful payment gateway solution that aims to help merchants grow and develop their payment methods. We allow for a better and unified online commerce experience for shoppers and merchants alike, aiming to simplify the complexities of payments to help you grow.

<p align="center">
  <a href="https://montypay.com">
      <img src="https://user-images.githubusercontent.com/85153851/122195876-66844b00-ce9f-11eb-80d3-5f622e406c98.jpg" alt="MontyPay" width="400px"/>
  </a>
</p>

MontyPay Android SDK was developed and designed with one purpose: to help the Android developers easily integrate the MontyPay API Payment Platform for a specific merchant. 

The main aspects of the MontyPay Android SDK:

- [Kotlin](https://developer.android.com/kotlin) is the main language
- [Retrofit](http://square.github.io/retrofit/) is the API machine 
- [KDoc](https://kotlinlang.org/docs/reference/kotlin-doc.html) code coverage
- API debug [logging](https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor)
- Minimum SDK 16+
- Sample Application

To properly set up the SDK, read [Wiki](https://github.com/MontypayApi/MontyPay-android-sdk/wiki) first.
To get used to the SDK, download a [sample app](https://github.com/MontypayApi/MontyPay-android-sdk/tree/main/sample).

## Setup

Add to the root build.gradle:

```groovy
allprojects {
    repositories {
        ...
        jcenter()
        maven { url 'https://jitpack.io' }
    }
}
```

Add to the package build.gradle:

```groovy
dependencies {
    implementation 'com.github.MontyPay:MontyPay-android-sdk:{latest-version}'
}
```


## Sample

| Sale | Recurring Sale | Capture |
|-|-|-|
| ![](/media/sale.gif) | ![](/media/recurring-sale.gif) | ![](/media/capture.gif) |

| Creditvoid | Get Trans Status | Get Trans Details |
|-|-|-|
| ![](/media/creditvoid.gif) | ![](/media/get-trans-status.gif) | ![](/media/get-trans-details.gif) |

## Getting help

To report a specific issue or feature request, open a [new issue](https://github.com/MontypayApi/MontyPay-android-sdk/issues).

Or write a direct letter to the [support@montypay.com](mailto:support@montypay.com).

## License

MIT License. See the [LICENSE](https://github.com/MontypayApi/MontyPay-android-sdk/blob/main/LICENSE) file for more details.

## Contacts

![](https://user-images.githubusercontent.com/85153851/122368628-2b991a80-cf66-11eb-96d2-e7123e7507ee.png)

Website: [https://www.montypay.com](https://www.montypay.com)

Phone: +961-1-748847

Email: support@montypay.com

Address: MontyPay - UK 



© 2021 MontyPay. All rights reserved

