# Back Gesture Height — Android 16 / LineageOS

Vector/LSPosed legacy module. It hooks `com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler.isWithinTouchRegion(int,int)` and rejects Back gesture starts in the top N% of the display.

The value is stored in `Settings.Secure.gesture_back_exclude_top` (0..50). The included app has a 0–50% slider and applies the setting through `su`, then restarts SystemUI.

Install APK, enable it for System UI in Vector, reboot, then choose a value in the app. Disable the module in Vector if SystemUI becomes unstable.
