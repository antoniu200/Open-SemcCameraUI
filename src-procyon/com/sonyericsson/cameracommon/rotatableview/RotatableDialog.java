// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.rotatableview;

import android.content.Context;
import android.app.AlertDialog$Builder;
import android.widget.Button;
import android.content.DialogInterface$OnKeyListener;
import android.content.DialogInterface$OnDismissListener;
import android.content.DialogInterface$OnCancelListener;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.content.DialogInterface;
import android.view.ViewGroup$LayoutParams;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import android.widget.FrameLayout$LayoutParams;
import android.view.WindowManager$LayoutParams;
import android.content.res.Resources;
import android.content.res.Resources$NotFoundException;
import com.sonyericsson.android.camera.util.CamLog;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.view.Window;
import android.view.View;
import android.app.AlertDialog;
import android.graphics.Rect;
import android.view.View$OnAttachStateChangeListener;
import android.view.View$OnTouchListener;

public class RotatableDialog implements View$OnTouchListener, View$OnAttachStateChangeListener
{
    public static final String TAG = "RotatableDialog";
    private final int mAnimationsForLand;
    private final int mAnimationsForPort;
    private Rect mDefaultPaddingRect;
    private AlertDialog mDialog;
    private int mDialogWidthForLand;
    private int mDialogWidthForPort;
    private int mDisplayHeight;
    private int mDisplayWidth;
    private int mHalfDiffBetweenDisplayWidthAndHeight;
    private int mOrientation;
    private View mScrollableView;
    private Rect mTempRect;
    private Window mWindow;
    
    protected RotatableDialog(final AlertDialog mDialog) {
        this.mTempRect = new Rect();
        this.mDefaultPaddingRect = null;
        this.mOrientation = 2;
        this.mDialog = mDialog;
        (this.mWindow = mDialog.getWindow()).addFlags(128);
        this.mAnimationsForLand = 2131755416;
        this.mAnimationsForPort = 2131755417;
    }
    
    private void attachScrollableView() {
        if (this.mScrollableView == null) {
            return;
        }
        View viewById = this.mWindow.findViewById(16908299);
        if (viewById != null) {
            final FrameLayout frameLayout = new FrameLayout(this.mWindow.getContext());
            frameLayout.setPadding(viewById.getPaddingLeft(), viewById.getPaddingTop(), viewById.getPaddingRight(), viewById.getPaddingBottom());
            frameLayout.addView(this.mScrollableView, viewById.getLayoutParams().width, viewById.getLayoutParams().height);
            final ViewParent parent = viewById.getParent();
            ScrollView scrollView;
            if (parent instanceof LinearLayout) {
                scrollView = (ScrollView)parent.getParent();
                viewById = (View)parent;
            }
            else {
                scrollView = (ScrollView)parent;
            }
            scrollView.removeView(viewById);
            scrollView.addView((View)frameLayout, -1, -1);
        }
    }
    
    private int calculateOutValue(int n, final int n2, final int n3) {
        if (n < n2) {
            n -= n2;
        }
        else if (n3 < n) {
            n -= n3;
        }
        else {
            n = 0;
        }
        return n;
    }
    
    private void initialize() {
        final ViewGroup viewGroup = (ViewGroup)this.mWindow.getDecorView();
        viewGroup.addOnAttachStateChangeListener((View$OnAttachStateChangeListener)this);
        viewGroup.setOnTouchListener((View$OnTouchListener)this);
        this.setWindowAnimations(this.mOrientation);
        final Rect rect = new Rect();
        this.mWindow.getWindowManager().getDefaultDisplay().getRectSize(rect);
        if (rect.width() > rect.height()) {
            this.mDisplayWidth = rect.width();
            this.mDisplayHeight = rect.height();
        }
        else {
            this.mDisplayWidth = rect.height();
            this.mDisplayHeight = rect.width();
        }
        this.mHalfDiffBetweenDisplayWidthAndHeight = (this.mDisplayWidth - this.mDisplayHeight) / 2;
        try {
            final Resources resources = this.mDialog.getContext().getResources();
            final String string = resources.getString(17104899);
            final String string2 = resources.getString(17104900);
            final String replace = string.replace("%", "");
            final String replace2 = string2.replace("%", "");
            final float n = Float.parseFloat(replace) / 100.0f;
            final float n2 = Float.parseFloat(replace2) / 100.0f;
            this.mDialogWidthForLand = (int)(this.mDisplayWidth * n);
            this.mDialogWidthForPort = (int)(this.mDisplayHeight * n2);
        }
        catch (final NumberFormatException ex) {
            CamLog.e("Fail to get width of dialog for each orientation.", ex);
        }
        catch (final NullPointerException ex2) {
            CamLog.e("Fail to get width of dialog for each orientation.", ex2);
        }
        catch (final Resources$NotFoundException ex3) {
            CamLog.e("Fail to get width of dialog for each orientation.", (Throwable)ex3);
        }
        this.attachScrollableView();
    }
    
    private void release() {
        final ViewGroup viewGroup = (ViewGroup)this.mWindow.getDecorView();
        viewGroup.removeOnAttachStateChangeListener((View$OnAttachStateChangeListener)this);
        viewGroup.setOnTouchListener((View$OnTouchListener)null);
        this.mDialog = null;
        this.mWindow = null;
        this.mScrollableView = null;
    }
    
    private void setWindowAnimations(final int n) {
        if (n == 1) {
            this.mWindow.setWindowAnimations(this.mAnimationsForPort);
        }
        else {
            this.mWindow.setWindowAnimations(this.mAnimationsForLand);
        }
    }
    
    private void updateLayout(int mHalfDiffBetweenDisplayWidthAndHeight) {
        if (!this.isShowing()) {
            return;
        }
        this.setWindowAnimations(mHalfDiffBetweenDisplayWidthAndHeight);
        final ViewGroup viewGroup = (ViewGroup)this.mWindow.getDecorView();
        final WindowManager$LayoutParams windowManager$LayoutParams = (WindowManager$LayoutParams)viewGroup.getLayoutParams();
        final View child = viewGroup.getChildAt(0);
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)child.getLayoutParams();
        if (this.mDefaultPaddingRect == null) {
            this.mDefaultPaddingRect = new Rect();
            this.mDefaultPaddingRect.left = viewGroup.getPaddingLeft();
            this.mDefaultPaddingRect.top = viewGroup.getPaddingTop();
            this.mDefaultPaddingRect.right = viewGroup.getPaddingRight();
            this.mDefaultPaddingRect.bottom = viewGroup.getPaddingBottom();
        }
        layoutParams.height = -2;
        layoutParams.gravity = 17;
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            windowManager$LayoutParams.height = this.mDisplayWidth;
            if (mHalfDiffBetweenDisplayWidthAndHeight == 1) {
                viewGroup.setPadding(this.mDefaultPaddingRect.left, this.mDefaultPaddingRect.top, this.mDefaultPaddingRect.right, this.mDefaultPaddingRect.bottom);
                child.setRotation(0.0f);
                child.setTranslationX(0.0f);
                layoutParams.width = this.mDialogWidthForPort;
                windowManager$LayoutParams.width = this.mDisplayHeight;
            }
            else {
                child.setRotation(90.0f);
                child.setTranslationX((float)(-this.mHalfDiffBetweenDisplayWidthAndHeight));
                if (this.mDialogWidthForLand > this.mDisplayHeight) {
                    layoutParams.width = this.mDisplayHeight;
                }
                else {
                    layoutParams.width = this.mDialogWidthForLand;
                }
                mHalfDiffBetweenDisplayWidthAndHeight = this.mHalfDiffBetweenDisplayWidthAndHeight;
                viewGroup.setPadding(0, mHalfDiffBetweenDisplayWidthAndHeight, 0, mHalfDiffBetweenDisplayWidthAndHeight);
                windowManager$LayoutParams.width = this.mDisplayWidth;
            }
        }
        else {
            windowManager$LayoutParams.width = this.mDisplayWidth;
            if (mHalfDiffBetweenDisplayWidthAndHeight == 1) {
                viewGroup.setPadding(this.mDefaultPaddingRect.left, this.mDefaultPaddingRect.top, this.mDefaultPaddingRect.right, this.mDefaultPaddingRect.bottom);
                child.setRotation(270.0f);
                child.setTranslationY((float)(-this.mHalfDiffBetweenDisplayWidthAndHeight));
                layoutParams.width = this.mDialogWidthForPort;
                windowManager$LayoutParams.height = this.mDisplayWidth;
            }
            else {
                viewGroup.setPadding(0, 0, 0, 0);
                child.setRotation(0.0f);
                child.setTranslationY(0.0f);
                if (this.mDialogWidthForLand > this.mDisplayHeight) {
                    layoutParams.width = this.mDisplayHeight;
                }
                else {
                    layoutParams.width = this.mDialogWidthForLand;
                }
                windowManager$LayoutParams.height = this.mDisplayHeight;
            }
        }
        this.mWindow.getWindowManager().updateViewLayout((View)viewGroup, (ViewGroup$LayoutParams)windowManager$LayoutParams);
        child.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
    }
    
    public void cancel() {
        if (this.mDialog != null) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Cancel Dialog: ");
                sb.append(this.mDialog);
                CamLog.d(sb.toString());
            }
            this.mDialog.cancel();
        }
    }
    
    public void dismiss() {
        if (this.mDialog != null) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dismiss Dialog: ");
                sb.append(this.mDialog);
                CamLog.d(sb.toString());
            }
            this.mDialog.dismiss();
        }
    }
    
    public void hide() {
        if (this.mDialog != null) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Hide Dialog: ");
                sb.append(this.mDialog);
                CamLog.d(sb.toString());
            }
            this.mDialog.hide();
        }
    }
    
    public boolean isShowing() {
        return this.mDialog != null && this.mDialog.isShowing();
    }
    
    public boolean isShown(final DialogInterface dialogInterface) {
        return this.mDialog == dialogInterface;
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final ViewGroup viewGroup = (ViewGroup)view;
        viewGroup.getChildAt(0).getGlobalVisibleRect(this.mTempRect);
        final int calculateOutValue = this.calculateOutValue((int)motionEvent.getRawX(), this.mTempRect.left, this.mTempRect.right);
        final int calculateOutValue2 = this.calculateOutValue((int)motionEvent.getRawY(), this.mTempRect.top, this.mTempRect.bottom);
        if (calculateOutValue != 0 || calculateOutValue2 != 0) {
            int n;
            if ((n = calculateOutValue) > 0) {
                n = calculateOutValue + viewGroup.getWidth();
            }
            int n2;
            if ((n2 = calculateOutValue2) > 0) {
                n2 = calculateOutValue2 + viewGroup.getHeight();
            }
            final MotionEvent obtain = MotionEvent.obtain(motionEvent);
            obtain.setLocation((float)n, (float)n2);
            if (this.isShowing()) {
                this.mDialog.onTouchEvent(obtain);
            }
            obtain.recycle();
        }
        return false;
    }
    
    public void onViewAttachedToWindow(final View view) {
        final ViewGroup viewGroup = (ViewGroup)this.mWindow.getDecorView();
        viewGroup.findViewById(16908290).setBackground(viewGroup.getBackground());
        viewGroup.setBackground((Drawable)null);
        this.updateLayout(this.mOrientation);
        this.mDialog.getWindow().clearFlags(131072);
    }
    
    public void onViewDetachedFromWindow(final View view) {
        this.release();
    }
    
    public void setCancelable(final boolean cancelable) {
        if (this.mDialog != null) {
            this.mDialog.setCancelable(cancelable);
        }
    }
    
    public void setCanceledOnTouchOutside(final boolean canceledOnTouchOutside) {
        if (this.mDialog != null) {
            this.mDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        }
    }
    
    public void setOnCancelListener(final DialogInterface$OnCancelListener onCancelListener) {
        if (this.mDialog != null) {
            this.mDialog.setOnCancelListener(onCancelListener);
        }
    }
    
    public void setOnDismissListener(final DialogInterface$OnDismissListener onDismissListener) {
        if (this.mDialog != null) {
            this.mDialog.setOnDismissListener(onDismissListener);
        }
    }
    
    public void setOnKeyListener(final DialogInterface$OnKeyListener onKeyListener) {
        if (this.mDialog != null) {
            this.mDialog.setOnKeyListener(onKeyListener);
        }
    }
    
    public void setOrientation(final int mOrientation) {
        this.updateLayout(this.mOrientation = mOrientation);
    }
    
    public void setPositiveButtonEnabled(final boolean enabled) {
        if (this.mDialog != null) {
            final Button button = this.mDialog.getButton(-1);
            if (button != null) {
                button.setEnabled(enabled);
            }
        }
    }
    
    public void setViewAsScrollable(final View mScrollableView) {
        this.mScrollableView = mScrollableView;
        if (this.mScrollableView != null) {
            this.mDialog.setMessage((CharSequence)"");
        }
    }
    
    public void show() {
        if (this.mDialog != null) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Show Dialog: ");
                sb.append(this.mDialog);
                CamLog.d(sb.toString());
            }
            this.mDialog.show();
            this.initialize();
        }
    }
    
    public static class Builder extends AlertDialog$Builder
    {
        private Cancelable mIsCancelable;
        private Cancelable mIsCancelableOnTouchOutside;
        protected View mScrollableView;
        private int mSensorOrientation;
        
        public Builder(final Context context) {
            super(context);
            this.mScrollableView = null;
        }
        
        public RotatableDialog createRotatableDialog() {
            final RotatableDialog rotatableDialog = new RotatableDialog(super.create());
            rotatableDialog.setViewAsScrollable(this.mScrollableView);
            final Cancelable mIsCancelable = this.mIsCancelable;
            final Cancelable use_DEFAULT = Cancelable.USE_DEFAULT;
            final boolean b = false;
            if (mIsCancelable != use_DEFAULT) {
                rotatableDialog.setCancelable(this.mIsCancelable == Cancelable.TRUE);
            }
            if (this.mIsCancelableOnTouchOutside != Cancelable.USE_DEFAULT) {
                boolean canceledOnTouchOutside = b;
                if (this.mIsCancelableOnTouchOutside == Cancelable.TRUE) {
                    canceledOnTouchOutside = true;
                }
                rotatableDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            }
            rotatableDialog.setOrientation(this.mSensorOrientation);
            return rotatableDialog;
        }
        
        public Builder setAlertIcon() {
            super.setIcon(17301543);
            return this;
        }
        
        public Builder setCancelable(final Cancelable mIsCancelable, final Cancelable mIsCancelableOnTouchOutside) {
            this.mIsCancelable = mIsCancelable;
            this.mIsCancelableOnTouchOutside = mIsCancelableOnTouchOutside;
            return this;
        }
        
        public Builder setOrientation(final int mSensorOrientation) {
            this.mSensorOrientation = mSensorOrientation;
            return this;
        }
        
        public Builder setViewAsScrollable(final View mScrollableView) {
            this.mScrollableView = mScrollableView;
            return this;
        }
    }
    
    public enum Cancelable
    {
        private static final Cancelable[] $VALUES;
        
        FALSE, 
        TRUE, 
        USE_DEFAULT;
        
        static {
            $VALUES = new Cancelable[] { Cancelable.TRUE, Cancelable.FALSE, Cancelable.USE_DEFAULT };
        }
    }
}
