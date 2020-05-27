# Customized Rocket.Chat Android native application

## Description

This repository contains all the code related to the Android native application of [Rocket.Chat.Android](https://github.com/RocketChat/Rocket.Chat.Android). 

## How to build

- Make sure that you have the latest **Gradle** and the **Android plugin** versions installed. Go to `File > Project Structure > Project` and make sure that you have the latest versions installed. Refer [this](https://developer.android.com/studio/releases/gradle-plugin.html#updating-gradle) to see the compatible versions.
- Kotlin is already configured in the project. To check, go to `Tools > Kotlin > Configure Kotlin in project`. A message saying kotlin is already configured in the project pops up. You can update kotlin to the latest version by going to `Tools > Kotlin > Configure Kotlin updates` and download the latest version of kotlin.

### SDK Instructions

- This version requires the [Kotlin SDK](https://github.com/RocketChat/Rocket.Chat.Kotlin.SDK) for Rocket.Chat. Clone the Kotlin SDK in by running `git clone https://github.com/RocketChat/Rocket.Chat.Kotlin.SDK.git`.
- First, a build is required for the SDK, so that required jar files are generated. Make sure that the Android repository and the Kotlin SDK have the same immediate parent directory. Change the current directory to `Rocket.Chat.Android/app` and run the `build-sdk.sh` which will result in creating of the required jar file `core*.jar` and `common*.jar` in `Rocket.Chat.Android/app/libs`, by the following steps in your terminal window:

```
cd Rocket.Chat.Android/app
./build-sdk.sh
```

**Note:** *You need to have Java 8 as default Java for the system (project won't build when using a Java 9+ version).*
