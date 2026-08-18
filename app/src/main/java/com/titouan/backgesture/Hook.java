package com.titouan.backgesture;

import android.graphics.Point;
import android.provider.Settings;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public final class Hook implements IXposedHookLoadPackage {
    private static final String TAG = "BackGestureHeight";
    private static final String KEY = "gesture_back_exclude_top";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lp) throws Throwable {
        if (!"com.android.systemui".equals(lp.packageName)) {
            return;
        }

        try {
            Class<?> clazz = XposedHelpers.findClass(
                    "com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler",
                    lp.classLoader);

            XposedHelpers.findAndHookMethod(
                    clazz,
                    "isWithinTouchRegion",
                    int.class, int.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            try {
                                int percent = Settings.Secure.getInt(
                                        ((android.content.Context) XposedHelpers.getObjectField(
                                                param.thisObject, "mContext")).getContentResolver(),
                                        KEY,
                                        0);

                                percent = Math.max(0, Math.min(50, percent));
                                if (percent == 0) {
                                    return;
                                }

                                Point displaySize = (Point) XposedHelpers.getObjectField(
                                        param.thisObject, "mDisplaySize");

                                if (displaySize == null || displaySize.y <= 0) {
                                    return;
                                }

                                int y = (int) param.args[1];
                                int excludedHeight = displaySize.y * percent / 100;

                                if (y < excludedHeight) {
                                    param.setResult(false);
                                }
                            } catch (Throwable t) {
                                XposedBridge.log(TAG + ": hook error: " + t);
                            }
                        }
                    });

            XposedBridge.log(TAG + ": isWithinTouchRegion(int,int) hook installed");
        } catch (Throwable t) {
            XposedBridge.log(TAG + ": hook installation failed: " + t);
        }
    }
}
