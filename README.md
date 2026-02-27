# Awesome PDF

Android app scaffold with Kotlin + Jetpack Compose + Material 3.

## Implemented
- Navigation flow: `Splash -> Onboarding -> Main`.
- Bottom navigation with 5 tabs: Home, Tools, Files, AI, Settings.
- Merge PDF flow end-to-end:
  - Pick 2+ PDFs from storage.
  - Merge with `WorkManager` in background.
  - Progress updates in UI.
  - Output saved to app documents folder (`/Android/data/com.awesomepdf/files/Documents/merged`).
  - Result screen showing output path.
- Google Play Billing integration scaffold:
  - Paywall offers monthly/yearly/lifetime plans.
  - Restore purchases action.
  - `EntitlementManager` publishes `Flow<EntitlementState>`.
  - AI tab protected by `FeatureGate` when user is not premium.
  - Debug fake entitlement toggle in Settings for debug builds.

## Run
1. Open project in Android Studio.
2. Sync Gradle.
3. Run the `app` module on emulator/device.
