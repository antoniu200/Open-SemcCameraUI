// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.hint;

import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import android.graphics.Rect;
import android.view.View$OnClickListener;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import com.sonymobile.cameracommon.font.FontUtil;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.util.AttributeSet;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.FrameLayout;

public class HintTextView extends FrameLayout
{
    private Button mButton;
    private LinearLayout mLayoutContainer;
    private TextView mMessage;
    private View mMessageBackground;
    private int mOrientation;
    private Runnable mSendAccessibilityEventTask;
    private TextView mSubMessage;
    private boolean mTransparentBackground;
    
    public HintTextView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mTransparentBackground = true;
        this.mSendAccessibilityEventTask = new Runnable() {
            final HintTextView this$0;
            
            @Override
            public void run() {
                this.this$0.sendAccessibilityEvent(32);
            }
        };
    }
    
    private int getBottomMarginPixelSize(final int n) {
        if (n == 1) {
            return this.getResources().getDimensionPixelSize(2131165358);
        }
        return this.getResources().getDimensionPixelSize(2131165357);
    }
    
    public static HintTextView inflate(final Context context) {
        return (HintTextView)View.inflate(context, 2131492928, (ViewGroup)null);
    }
    
    private void postAccessibilityEvent() {
        this.removeCallbacks(this.mSendAccessibilityEventTask);
        this.post(this.mSendAccessibilityEventTask);
    }
    
    private void setButtonDescription(final int n) {
        if (n == -1) {
            this.mButton.setContentDescription((CharSequence)"");
            return;
        }
        this.mButton.setContentDescription((CharSequence)this.getResources().getString(n));
    }
    
    private void setButtonMessage(final int text) {
        final FrameLayout frameLayout = (FrameLayout)this.findViewById(2131296407);
        final View viewById = this.findViewById(2131296326);
        if (text == -1) {
            this.mButton.setVisibility(8);
            frameLayout.setVisibility(8);
            viewById.setVisibility(0);
            return;
        }
        if (this.mButton.getVisibility() != 0) {
            this.mButton.setVisibility(0);
            frameLayout.setVisibility(0);
            viewById.setVisibility(8);
        }
        this.mButton.setText(text);
    }
    
    private void setMessageContents(final int text) {
        if (text == -1) {
            this.mMessage.setVisibility(8);
            this.mMessageBackground.setVisibility(8);
            return;
        }
        if (this.mMessage.getVisibility() != 0) {
            this.mMessage.setVisibility(0);
            this.mMessageBackground.setVisibility(0);
        }
        this.mMessage.setText(text);
    }
    
    private void setMessageDescription(final int n) {
        if (n == -1) {
            return;
        }
        this.mMessage.setContentDescription((CharSequence)this.getResources().getString(n));
    }
    
    private void setSubMessage(final String text) {
        final View viewById = this.findViewById(2131296464);
        final View viewById2 = this.findViewById(2131296633);
        if (text == null) {
            this.mSubMessage.setVisibility(8);
            viewById.setVisibility(8);
            viewById2.setVisibility(8);
            return;
        }
        if (this.mSubMessage.getVisibility() != 0) {
            this.mSubMessage.setVisibility(0);
            viewById.setVisibility(0);
            viewById2.setVisibility(0);
        }
        this.mSubMessage.setText((CharSequence)text);
    }
    
    private void setTransparentBackground(final boolean mTransparentBackground) {
        this.mTransparentBackground = mTransparentBackground;
        if (this.mMessage != null && this.mTransparentBackground) {
            this.mMessageBackground.setBackground((Drawable)null);
        }
        else {
            this.mMessageBackground.setBackgroundResource(2131231213);
        }
    }
    
    private void updateRotation() {
        final int measuredWidth = this.getMeasuredWidth();
        final int measuredHeight = this.getMeasuredHeight();
        final float angle = RotationUtil.getAngle(this.mOrientation);
        if (angle != this.getRotation()) {
            this.setRotation(angle);
        }
        if (measuredWidth != 0) {
            float translationX = 0.0f;
            if (angle != 0.0f) {
                translationX = (measuredWidth - measuredHeight) / 2.0f;
            }
            if ((int)translationX != (int)this.getTranslationX()) {
                this.setTranslationX(translationX);
            }
        }
    }
    
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mLayoutContainer = (LinearLayout)this.findViewById(2131296415);
        this.mMessage = (TextView)this.findViewById(2131296416);
        this.mSubMessage = (TextView)this.findViewById(2131296418);
        this.mButton = (Button)this.findViewById(2131296325);
        this.mMessageBackground = this.findViewById(2131296417);
        FontUtil.setRobotoFont(this.mMessage, FontUtil.RobotoFontType.MEDIUM);
        FontUtil.setRobotoFont(this.mSubMessage, FontUtil.RobotoFontType.MEDIUM);
        FontUtil.setRobotoFont(this.mButton, FontUtil.RobotoFontType.MEDIUM);
    }
    
    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
        this.updateRotation();
        if (this.getHeight() < this.mLayoutContainer.getWidth()) {
            final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)this.mLayoutContainer.getLayoutParams();
            layoutParams.width = this.getHeight();
            this.mLayoutContainer.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            super.onMeasure(n, n2);
        }
    }
    
    public void setContent(final HintTextContent hintTextContent) {
        this.setMessageContents(hintTextContent.getMessageResourceId());
        this.setSubMessage(hintTextContent.getSubMessage());
        this.setButtonMessage(hintTextContent.getButtonMessageResourceId());
        this.setMessageDescription(hintTextContent.getMessageDescriptionResourceId());
        this.setButtonDescription(hintTextContent.getButtonDescriptionResourceId());
        this.setTransparentBackground(hintTextContent.isTransparentBackground());
        if (hintTextContent.getMessageDescriptionResourceId() == -1) {
            this.setImportantForAccessibility(2);
        }
        else {
            this.setImportantForAccessibility(1);
        }
        if (this.getVisibility() == 0) {
            this.postAccessibilityEvent();
        }
    }
    
    public void setOnButtonClickListener(final View$OnClickListener onClickListener) {
        this.mButton.setOnClickListener(onClickListener);
    }
    
    public void setUiOrientation(Rect surfaceViewRect, final Context context, final LayoutDependencyResolver.ScreenAspect screenAspect, final int mOrientation) {
        this.mOrientation = mOrientation;
        this.findViewById(2131296326).getLayoutParams().height = this.getBottomMarginPixelSize(mOrientation);
        this.findViewById(2131296326).requestLayout();
        final float n = Math.max(surfaceViewRect.width(), surfaceViewRect.height()) * 1.0f;
        if (Math.min(surfaceViewRect.width(), surfaceViewRect.height()) * 1.0f / n < 0.75f && this.mOrientation == 1) {
            surfaceViewRect = LayoutDependencyResolver.getSurfaceViewRect(context, 0.75f, screenAspect);
            this.setPadding(0, 0, 0, (int)n - surfaceViewRect.height());
        }
        else {
            this.setPadding(0, 0, 0, 0);
        }
        this.updateRotation();
    }
    
    public void setVisibility(final int visibility) {
        final boolean b = this.getVisibility() != 0 && visibility == 0;
        super.setVisibility(visibility);
        if (b) {
            this.postAccessibilityEvent();
        }
    }
}
