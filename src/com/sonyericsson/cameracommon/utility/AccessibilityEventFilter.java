package com.sonyericsson.cameracommon.utility;

import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class AccessibilityEventFilter extends View.AccessibilityDelegate {
    public static final String TAG = "AccessibilityEventFilter";
    private String mAllowedClassName;

    public AccessibilityEventFilter() {
        this.mAllowedClassName = "";
    }

    public AccessibilityEventFilter(Class<?> cls) {
        this.mAllowedClassName = "";
        this.mAllowedClassName = String.copyValueOf(cls.getName().toCharArray());
    }

    @Override // android.view.View.AccessibilityDelegate
    public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
        int eventType = accessibilityEvent.getEventType();
        boolean z = eventType == 8 || eventType == 65536 || eventType == 32768;
        boolean z2 = (accessibilityEvent.getRecordCount() <= 0 && accessibilityEvent.getText().isEmpty() && accessibilityEvent.getContentDescription() == null) ? false : true;
        if ((z || z2) && !this.mAllowedClassName.equals(accessibilityEvent.getClassName())) {
            if (CamLog.VERBOSE) {
                CamLog.d("onRequestSendAccessibilityEvent: This event should be ignored. Ignored event = " + accessibilityEvent.toString());
            }
            return false;
        }
        return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
    }
}
