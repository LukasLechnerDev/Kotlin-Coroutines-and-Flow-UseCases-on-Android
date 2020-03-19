package com.lukaslechner.coroutineusecasesonandroid.mockdata

import com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1.AndroidVersion

fun getMockAndroidVersions() = listOf(
    AndroidVersion(27, "Oreo"),
    AndroidVersion(28, "Pie"),
    AndroidVersion(29, "Android 10")
)

fun getFeaturesOfAndroid10() = listOf(
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