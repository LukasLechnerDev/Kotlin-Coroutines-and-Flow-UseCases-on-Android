package com.lukaslechner.coroutineusecasesonandroid.mock

data class AndroidVersion(val apiVersion: Int, val name: String)

data class VersionFeatures(val androidVersion: AndroidVersion, val features: List<String>)

val mockAndroidVersionOreo = AndroidVersion(27, "Oreo")
val mockAndroidVersionPie = AndroidVersion(28, "Pie")
val mockAndroidVersionAndroid10 = AndroidVersion(29, "Android 10")

val mockAndroidVersions = listOf(
    mockAndroidVersionOreo, mockAndroidVersionPie, mockAndroidVersionAndroid10
)

val featuresOfOreo = listOf(
    "Neural networks API.",
    "Shared memory API.",
    "WallpaperColors API.",
    "Bluetooth battery level for connected devices, accessible in Quick Settings.",
    "Android Oreo Go Edition, a lightweight distribution of Android that runs better than normal Android on devices with less than 1 GB of RAM.",
    "Autofill framework updates.",
    "Programmatic Safe Browsing actions.",
    "Navigation buttons dim when not in use.",
    "Visual changes to 'Power Off' and 'Restart', including a new screen and floating toolbar.",
    "Toast messages are now white with the same existing transparency.",
    "Automatic light and dark themes.",
    "New Easter egg in the form of an official Oreo cookie picture."
)

val featuresOfPie = listOf(
    "New user interface for the quick settings menu.",
    "The clock has moved to the left of the notification bar.",
    "The \"dock\" now has a semi-transparent background.",
    "Battery Saver no longer shows an orange overlay on the notification and status bars.",
    "A \"screenshot\" button has been added to the power options.",
    "A new \"Lockdown\" mode which disables biometric authentication once activated.",
    "Rounded corners across the UI.",
    "New transitions for switching between apps, or activities within apps.",
    "Richer messaging notifications, where a full conversation can be seen within a notification, full-scale images, and smart replies akin to Google's new app, Reply.",
    "Support for display cutouts.",
    "Redesigned volume slider.",
    "Battery percentage now shown in Always-On Display.",
    "Lock screen security changes include the possible return of an improved NFC Unlock.",
    "Experimental features (which are currently hidden within a menu called Feature Flags) such as a redesigned About Phone page in settings, and automatic Bluetooth enabling while driving.",
    "DNS over TLS.",
    "A new optional gesture-based system interface, allowing users to navigate the OS using swipes more often than the traditional UI.",
    "Redesigned multitask app switcher with the Google search bar and app drawer built-in.",
    "Android Dashboard, which tells the user how much time they are spending on their device and in apps, and allows the user to set time limits on apps.",
    "\"Shush\", an enhanced version of Do Not Disturb mode activated by placing the phone face down, which mutes standard notifications.",
    "\"Adaptive Battery\" prediction, which makes use of Doze to hibernate user apps the OS determines the user will not use.",
    "Auto-Brightness feature modifies screen brightness based on user habits.",
    "Wind Down option lets Android users set a specific bedtime that enables Do Not Disturb and turns the entire phone's interface gray to discourage further use at night.",
    "Vulkan 1.1 support."
)

val featuresOfAndroid10 = listOf(
    "New permissions to access location in background and to access photo, video and audio files",
    "Background apps can no longer jump into the foreground",
    "Limited access to non-resettable device identifiers",
    "Sharing shortcuts, which allow sharing content with a contact directly",
    "Floating settings panel, that allows changing system settings directly from apps",
    "Dynamic depth format for photos, which allow changing background blur after taking a photo",
    "Support for the AV1 video codec, the HDR10+ video format and the Opus audio codec",
    "Support for aptX Adaptive, LHDC, LLAC, CELT and AAC LATM codecs[244",
    "A native MIDI API, allowing interaction with music controllers",
    "Better support for biometric authentication in apps",
    "Support for the WPA3 Wi-Fi security protocol",
    "Support for foldable phones",
    "Support for Notification Bubbles",
    "New system-wide dark theme/mode",
    "Project Mainline, allows core OS components to be updated via the Google Play Store, without requiring a complete system update"
)

val mockVersionFeaturesOreo = VersionFeatures(
    mockAndroidVersionOreo,
    featuresOfOreo
)

val mockVersionFeaturesPie = VersionFeatures(
    mockAndroidVersionPie,
    featuresOfPie
)

val mockVersionFeaturesAndroid10 = VersionFeatures(
    mockAndroidVersionAndroid10,
    featuresOfAndroid10
)