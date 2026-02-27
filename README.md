# Awesome PDF

Android app scaffold with Kotlin + Jetpack Compose + Material 3.

## Implemented
- Navigation flow: `Splash -> Onboarding -> Auth -> Main`.
- Bottom navigation with 5 tabs: Home, Tools, Files, AI, Settings.
- Merge PDF flow end-to-end:
  - Pick 2+ PDFs from storage.
  - Merge with `WorkManager` in background.
  - Progress updates in UI.
  - Output saved to app documents folder (`/Android/data/com.awesomepdf/files/Documents/merged`).
  - Result screen showing output path.
- Firebase + account/settings sync:
  - Google sign-in (ID token flow) and anonymous skip.
  - Sign out from Settings.
  - Firestore profile doc create/update on sign-in.
  - Entitlement + settings synced to Firestore profile doc.
- Billing + premium gate:
  - Monthly/yearly/lifetime paywall plans with restore purchases.
  - `EntitlementManager` publishes entitlement state flow.
  - `FeatureGate` protects AI screen for non-premium users.
  - Remote Config key `paywall_banner_enabled` controls paywall banner visibility on Home.
- AI features with backend stub service:
  - Retrofit + OkHttp stack with Authorization token placeholder header.
  - Summarise PDF from selected file with extraction/upload placeholder.
  - Make Notes as bullet list and export notes to Markdown.
  - Ask PDF chat with citations placeholder.
  - Monthly usage meter/quota UI.
- Crashlytics:
  - Non-fatal errors recorded from merge tool flow failures.

## Run
1. Open project in Android Studio.
2. Sync Gradle.
3. Run the `app` module on emulator/device.
