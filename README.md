# Awesome PDF

Android app built with Kotlin + Jetpack Compose + Material 3, following MVVM + Clean Architecture.

## Implemented
- Navigation flow: Splash -> Onboarding -> Auth -> Main.
- Main uses Bottom Navigation: Home / Tools / Files / AI / Settings.
- Tool stubs with file picker: Merge, Split, Compress, Images to PDF.
- Firebase initialization (Auth, Firestore, Analytics, Crashlytics, Remote Config).
- Billing scaffold + paywall UI + debug fake entitlement toggle.
- AI screen with stubbed backend-proxy responses and quota counter.
- WorkManager stub for long running PDF processing.
- Unit tests for entitlement manager and summarize use case.

## Package structure
- `domain/` models, repositories, use cases
- `data/` repository implementations
- `presentation/` Compose screens + view models
- `di/` Hilt modules
- `worker/` WorkManager workers

## Build
1. Use JDK 17.
2. Open in Android Studio Iguana+.
3. Sync Gradle and run app.

> Firebase/Google services files are not committed in this scaffold. Add your own `google-services.json` if needed for production setup.
