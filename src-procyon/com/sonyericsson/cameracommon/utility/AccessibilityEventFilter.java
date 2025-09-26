// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import com.sonyericsson.android.camera.util.CamLog;
import android.view.accessibility.AccessibilityEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View$AccessibilityDelegate;

public class AccessibilityEventFilter extends View$AccessibilityDelegate
{
    public static final String TAG = "AccessibilityEventFilter";
    private String mAllowedClassName;
    
    public AccessibilityEventFilter() {
        this.mAllowedClassName = "";
    }
    
    public AccessibilityEventFilter(final Class<?> clazz) {
        this.mAllowedClassName = "";
        this.mAllowedClassName = String.copyValueOf(clazz.getName().toCharArray());
    }
    
    public boolean onRequestSendAccessibilityEvent(final ViewGroup viewGroup, final View view, final AccessibilityEvent accessibilityEvent) {
        final int eventType = accessibilityEvent.getEventType();
        final boolean b = eventType == 8 || eventType == 65536 || eventType == 32768;
        final boolean b2 = accessibilityEvent.getRecordCount() > 0 || !accessibilityEvent.getText().isEmpty() || accessibilityEvent.getContentDescription() != null;
        if ((b || b2) && !this.mAllowedClassName.equals(accessibilityEvent.getClassName())) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onRequestSendAccessibilityEvent: This event should be ignored. Ignored event = ");
                sb.append(accessibilityEvent.toString());
                CamLog.d(sb.toString());
            }
            return false;
        }
        return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
    }
}
