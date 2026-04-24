package com.example.hw_1;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

public class AppBlockService extends AccessibilityService {

    private static final String TELEGRAM_PACKAGE = "org.telegram.messenger";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getPackageName() == null) return;

        String packageName = event.getPackageName().toString();

        if (packageName.equals(TELEGRAM_PACKAGE)) {
            openCalculator();
        }
    }

    private void openCalculator() {
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.android.calculator2");

        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onInterrupt() {
    }
}