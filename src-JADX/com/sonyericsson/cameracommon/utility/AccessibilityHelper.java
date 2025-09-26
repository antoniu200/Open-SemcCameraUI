package com.sonyericsson.cameracommon.utility;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class AccessibilityHelper {
    public static final String TAG = "AccessibilityHelper";
    private static final Rect sRectForHit = new Rect();

    public static View requestAccessibilityFocus(ViewGroup viewGroup, MotionEvent motionEvent) {
        View viewSearchContentDescribedView = searchContentDescribedView(viewGroup, motionEvent);
        if (viewSearchContentDescribedView != null && viewSearchContentDescribedView.getContentDescription() != null) {
            if (CamLog.VERBOSE) {
                CamLog.i("Description: " + ((Object) viewSearchContentDescribedView.getContentDescription()));
            }
            viewSearchContentDescribedView.performAccessibilityAction(64, null);
        } else {
            if (CamLog.VERBOSE) {
                CamLog.i("TalkingView is not found.");
            }
            viewGroup.performAccessibilityAction(64, null);
        }
        return viewSearchContentDescribedView;
    }

    private static View searchContentDescribedView(ViewGroup viewGroup, MotionEvent motionEvent) {
        View viewSearchContentDescribedView;
        if (CamLog.VERBOSE) {
            CamLog.d("searchContentDescribedView() is called.");
        }
        if (viewGroup == null || motionEvent == null) {
            CamLog.e("searchContentDescribedView : Arg is null.[viewGroup = " + viewGroup + ", event = " + motionEvent + "]");
            return null;
        }
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = viewGroup.getChildAt(childCount);
            if (checkToTouch(childAt, x, y)) {
                if (childAt.getContentDescription() != null) {
                    viewSearchContentDescribedView = childAt;
                } else {
                    viewSearchContentDescribedView = childAt instanceof ViewGroup ? searchContentDescribedView((ViewGroup) childAt, motionEvent) : null;
                }
                if (viewSearchContentDescribedView != null) {
                    return viewSearchContentDescribedView;
                }
                if (childAt.getVisibility() == 0 && childAt.getAlpha() > 0.0f && childAt.isClickable()) {
                    return childAt;
                }
            }
        }
        return null;
    }

    private static boolean checkToTouch(View view, int i, int i2) {
        if (view == null || view.getVisibility() != 0) {
            return false;
        }
        if (!view.getGlobalVisibleRect(sRectForHit)) {
            if (CamLog.VERBOSE) {
                CamLog.d("checkToTouch: This view exists out of the parent view.");
            }
            return false;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("checkToTouch: GlobalVisibleRect = " + sRectForHit);
            CamLog.d("checkToTouch: Point = (" + i + "," + i2 + ")");
        }
        return sRectForHit.contains(i, i2);
    }

    public static class HoverEventInterceptView extends FrameLayout {
        public HoverEventInterceptView(Context context) {
            super(context);
        }

        @Override // android.view.ViewGroup
        public boolean onInterceptHoverEvent(MotionEvent motionEvent) {
            super.onInterceptHoverEvent(motionEvent);
            AccessibilityHelper.requestAccessibilityFocus(this, motionEvent);
            return true;
        }
    }
}
