[![CircleCI](https://circleci.com/gh/mercadolibre/fury_device-sdk-android/tree/release%2F1.0.0.svg?style=svg)](https://circleci.com/gh/mercadolibre/fury_device-sdk-android/tree/release%2F1.0.0)

# MercadoPago's Device SDK for Android

## What is this for?

The MercadoPago's Device SDK for Android makes it easy to obtain the device fingerprinting information.
You should send this information when processing a payment with MercadoPago so we can use it to help you avoid rejected payments or chargebacks.
For more details about this, visit the [developers guide](https://developers.mercadopago.com). 

## Requirements

⚠️ This module is supported with minimum API level 16 ⚠️

## Installation

### Android Studio

Add this line to your app's `build.gradle` inside the `dependencies` section:
```java
implementation 'com.mercadolibre.android.device:sdk:2.0.0'
```

### Local deployment

With this command you can generate a local version for testing:

    ./gradlew publishLocal

## 🐒 How to use?

Only **4** steps needed to obtain the device fingerprint information:

1) Add the following repository to your proyect `build.gradle`
```
repositories {
        maven {
            url "https://artifacts.mercadolibre.com/repository/android-releases"
        }
    }
```

2) Import into your project
```java
import com.mercadolibre.android.device.sdk.DeviceSDK;
```

3) Initialize the sdk in your `MainApplication` doing the following:
```java
DeviceSDK.getInstance().execute(this);
```

4) In the place of your checkout, you will be able to obtain the device fingerprint information using **one** of the following options:
```java
Device device = DeviceSDK.getInstance().getInfo(); // Returns a Device object with the info, this class is a Serializable class.
Map deviceMap = DeviceSDK.getInstance().getInfoAsMap(); //Returns a Map<String, Object> object
String jsonString = DeviceSDK.getInstance().getInfoAsJsonString(); // Returns a JSON string object.
```


### 🔮 Example
This project includes a test application using Device SDK.

## Author

Device Team, device@mercadolibre.com

## License

Mercado Pago's Device SDK is available under the MIT license. See the LICENSE file for more info.