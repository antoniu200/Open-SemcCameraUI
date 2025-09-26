// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import android.widget.FrameLayout;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.util.CamLog;
import android.view.View;
import android.graphics.Rect;

public class AccessibilityHelper
{
    public static final String TAG = "AccessibilityHelper";
    private static final Rect sRectForHit;
    
    static {
        sRectForHit = new Rect();
    }
    
    private static boolean checkToTouch(final View view, final int i, final int j) {
        if (view == null) {
            return false;
        }
        if (view.getVisibility() != 0) {
            return false;
        }
        if (!view.getGlobalVisibleRect(AccessibilityHelper.sRectForHit)) {
            if (CamLog.VERBOSE) {
                CamLog.d("checkToTouch: This view exists out of the parent view.");
            }
            return false;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("checkToTouch: GlobalVisibleRect = ");
            sb.append(AccessibilityHelper.sRectForHit);
            CamLog.d(sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("checkToTouch: Point = (");
            sb2.append(i);
            sb2.append(",");
            sb2.append(j);
            sb2.append(")");
            CamLog.d(sb2.toString());
        }
        return AccessibilityHelper.sRectForHit.contains(i, j);
    }
    
    public static View requestAccessibilityFocus(final ViewGroup viewGroup, final MotionEvent motionEvent) {
        final View searchContentDescribedView = searchContentDescribedView(viewGroup, motionEvent);
        if (searchContentDescribedView != null && searchContentDescribedView.getContentDescription() != null) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Description: ");
                sb.append((Object)searchContentDescribedView.getContentDescription());
                CamLog.i(sb.toString());
            }
            searchContentDescribedView.performAccessibilityAction(64, (Bundle)null);
        }
        else {
            if (CamLog.VERBOSE) {
                CamLog.i("TalkingView is not found.");
            }
            viewGroup.performAccessibilityAction(64, (Bundle)null);
        }
        return searchContentDescribedView;
    }
    
    private static View searchContentDescribedView(final ViewGroup obj, final MotionEvent obj2) {
        if (CamLog.VERBOSE) {
            CamLog.d("searchContentDescribedView() is called.");
        }
        if (obj != null && obj2 != null) {
            final int n = (int)obj2.getX();
            final int n2 = (int)obj2.getY();
            for (int i = obj.getChildCount() - 1; i >= 0; --i) {
                final View child = obj.getChildAt(i);
                if (checkToTouch(child, n, n2)) {
                    View searchContentDescribedView;
                    if (child.getContentDescription() != null) {
                        searchContentDescribedView = child;
                    }
                    else if (child instanceof ViewGroup) {
                        searchContentDescribedView = searchContentDescribedView((ViewGroup)child, obj2);
                    }
                    else {
                        searchContentDescribedView = null;
                    }
                    if (searchContentDescribedView != null) {
                        return searchContentDescribedView;
                    }
                    if (child.getVisibility() == 0 && child.getAlpha() > 0.0f && child.isClickable()) {
                        return child;
                    }
                }
            }
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("searchContentDescribedView : Arg is null.[viewGroup = ");
        sb.append(obj);
        sb.append(", event = ");
        sb.append(obj2);
        sb.append("]");
        CamLog.e(sb.toString());
        return null;
    }
    
    public static class HoverEventInterceptView extends FrameLayout
    {
        public HoverEventInterceptView(final Context context) {
            super(context);
        }
        
        public boolean onInterceptHoverEvent(final MotionEvent motionEvent) {
            super.onInterceptHoverEvent(motionEvent);
            AccessibilityHelper.requestAccessibilityFocus((ViewGroup)this, motionEvent);
            return true;
        }
    }
}
