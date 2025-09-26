// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view;

import com.sonyericsson.android.camera.util.CamLog;
import java.util.Iterator;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import android.widget.FrameLayout$LayoutParams;
import android.view.WindowManager;
import android.util.DisplayMetrics;
import android.view.View$OnClickListener;
import android.widget.ImageView$ScaleType;
import android.view.ViewStub;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import java.util.ArrayList;
import android.widget.LinearLayout$LayoutParams;
import com.sonyericsson.android.camera.NavigatorContents;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Rect;
import android.widget.TextView;
import android.widget.FrameLayout;
import java.util.List;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ApplicationNavigator extends LinearLayout
{
    public static final int MODE_INDEX_UNSPECIFIED = -1;
    private static final String TAG = "ApplicationNavigator";
    private int mCaptureButtonAreaHeight;
    private ImageView mCurrentModeIndicatorView;
    private int mDisplayHeight;
    private int mIconSize;
    private List<ImageView> mImageList;
    private FrameLayout mModeIconsView;
    private FrameLayout mModeSwitchAnimationContainer;
    private ImageView mModeSwitchImageView;
    private TextView mModeSwitchNameView;
    private FrameLayout mModeSwitchViewContainer;
    private boolean mNavigationEnabled;
    private int mOrientation;
    private Rect mRect;
    private int mViewIndex;
    
    public ApplicationNavigator(final Context context) {
        super(context);
        this.mNavigationEnabled = true;
        this.setImportantForAccessibility(1);
    }
    
    public ApplicationNavigator(final Context context, final AttributeSet set) {
        super(context, set);
        this.mNavigationEnabled = true;
        this.setImportantForAccessibility(1);
    }
    
    private int calculateDraggingPosition(final float n, final int n2) {
        return (int)(this.calculateModeIndicatorPosition(n2) + n * this.mIconSize);
    }
    
    private int calculateIconPosition(final int n) {
        return (int)((n - 1) * this.mIconSize + this.mDisplayHeight / 2.0f);
    }
    
    private static int computeReverseIndex(final int n) {
        return NavigatorContents.values().length - n - 1;
    }
    
    private static int computeViewIndex(final NavigatorContents navigatorContents) {
        return computeReverseIndex(NavigatorContents.indexOf(navigatorContents));
    }
    
    private void createCurrentModeIndicator() {
        this.mCurrentModeIndicatorView = (ImageView)this.findViewById(2131296374);
    }
    
    private void createIcons() {
        final LinearLayout$LayoutParams layoutParams = new LinearLayout$LayoutParams(-2, -2);
        layoutParams.gravity = 1;
        this.mImageList = new ArrayList<ImageView>();
        this.mModeIconsView = (FrameLayout)this.findViewById(2131296468);
        for (int i = 0; i < NavigatorContents.values().length; ++i) {
            final NavigatorContents navigatorContents = NavigatorContents.values()[NavigatorContents.values().length - i - 1];
            final ImageView imageView = new ImageView(this.getContext());
            imageView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            imageView.setFocusable(true);
            imageView.setClickable(true);
            imageView.setBackgroundResource(2131230837);
            imageView.setTag((Object)i);
            imageView.setImageResource(navigatorContents.getIconId());
            imageView.setContentDescription((CharSequence)navigatorContents.getText(this.getContext()));
            imageView.setSoundEffectsEnabled(false);
            this.mImageList.add(imageView);
            this.mModeIconsView.addView((View)imageView);
        }
        this.mIconSize = this.getContext().getResources().getDimensionPixelSize(2131165456);
        this.findViewById(2131296294).setMinimumHeight(this.mImageList.size() * this.mIconSize);
    }
    
    private void createModeSwitchContainer() {
        this.mModeSwitchAnimationContainer = (FrameLayout)((ViewStub)this.findViewById(2131296631)).inflate();
        this.mModeSwitchViewContainer = (FrameLayout)this.mModeSwitchAnimationContainer.findViewById(2131296437);
        (this.mModeSwitchNameView = (TextView)this.mModeSwitchAnimationContainer.findViewById(2131296471)).setAlpha(0.0f);
        (this.mModeSwitchImageView = (ImageView)this.mModeSwitchAnimationContainer.findViewById(2131296470)).setAlpha(0.0f);
        this.mModeSwitchImageView.setScaleType(ImageView$ScaleType.CENTER);
    }
    
    private NavigatorContents getSelectedContents() {
        final NavigatorContents superior_AUTO = NavigatorContents.SUPERIOR_AUTO;
        final float translationY = this.mCurrentModeIndicatorView.getTranslationY();
        final float n = (float)this.mCurrentModeIndicatorView.getHeight();
        int n2 = 0;
        NavigatorContents navigatorContents;
        while (true) {
            navigatorContents = superior_AUTO;
            if (n2 >= this.mImageList.size()) {
                break;
            }
            final float n3 = this.mImageList.get(n2).getTranslationY() + this.mImageList.get(n2).getHeight() / 2.0f;
            if (translationY <= n3 && n3 <= n + translationY) {
                navigatorContents = NavigatorContents.values()[computeReverseIndex(n2)];
                break;
            }
            ++n2;
        }
        return navigatorContents;
    }
    
    private void sendAccessibilityEventForModeName() {
        if (this.mModeSwitchNameView.getVisibility() == 0) {
            this.mModeSwitchNameView.setContentDescription((CharSequence)this.getResources().getString(this.getSelectedContents().getTextId()));
            this.mModeSwitchNameView.sendAccessibilityEvent(32);
        }
    }
    
    private void setIconClickListener(final View$OnClickListener onClickListener) {
        for (int i = 0; i < this.mImageList.size(); ++i) {
            this.mImageList.get(i).setOnClickListener(onClickListener);
        }
    }
    
    private void setIconPositions() {
        for (int i = 0; i < this.mImageList.size(); ++i) {
            this.mImageList.get(i).setTranslationY((float)this.calculateIconPosition(i));
        }
    }
    
    private void setModeIndicatorPosition(final int n) {
        this.mCurrentModeIndicatorView.setTranslationY((float)this.calculateModeIndicatorPosition(n));
    }
    
    private void setupModeSwitchContainer() {
        this.createModeSwitchContainer();
        this.updateModeSwitchAnimationContainer();
    }
    
    private void updateModeSwitchAnimationContainer() {
        if (this.mModeSwitchAnimationContainer == null) {
            return;
        }
        final LinearLayout$LayoutParams layoutParams = (LinearLayout$LayoutParams)this.mModeSwitchAnimationContainer.getLayoutParams();
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager)this.getContext().getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        int n;
        if (displayMetrics.heightPixels > displayMetrics.widthPixels) {
            n = displayMetrics.widthPixels;
        }
        else {
            n = displayMetrics.heightPixels;
        }
        int n2;
        if (this.mOrientation == 1) {
            n2 = this.mModeSwitchAnimationContainer.getWidth();
        }
        else {
            n2 = this.mModeSwitchAnimationContainer.getHeight();
        }
        if (n2 != n) {
            layoutParams.width = n;
            layoutParams.height = n;
            this.mModeSwitchAnimationContainer.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        }
        int n3;
        if (displayMetrics.heightPixels > displayMetrics.widthPixels) {
            n3 = displayMetrics.heightPixels;
        }
        else {
            n3 = displayMetrics.widthPixels;
        }
        final int dimensionPixelSize = this.getContext().getResources().getDimensionPixelSize(2131165428);
        final int dimensionPixelSize2 = this.getContext().getResources().getDimensionPixelSize(2131165456);
        final int dimensionPixelSize3 = this.getContext().getResources().getDimensionPixelSize(2131165449);
        final FrameLayout$LayoutParams layoutParams2 = (FrameLayout$LayoutParams)this.mModeSwitchViewContainer.getLayoutParams();
        final int n4 = (n3 - dimensionPixelSize - dimensionPixelSize2 - this.mCaptureButtonAreaHeight - dimensionPixelSize3) / 2;
        if (this.mOrientation == 2) {
            layoutParams2.setMargins(0, 0, n4, 0);
            layoutParams2.gravity = 8388629;
        }
        else {
            layoutParams2.setMargins(0, 0, 0, n4);
            layoutParams2.gravity = 81;
        }
        this.mModeSwitchViewContainer.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
        this.mModeSwitchAnimationContainer.setRotation(RotationUtil.getAngle(this.mOrientation));
    }
    
    private void updateNavigatorIcons() {
        final float angle = RotationUtil.getAngle(this.mOrientation);
        for (final ImageView imageView : this.mImageList) {
            if (this.mOrientation == 1) {
                imageView.getLayoutParams().width = this.mIconSize;
                imageView.getLayoutParams().height = imageView.getDrawable().getIntrinsicWidth();
            }
            else {
                imageView.getLayoutParams().height = this.mIconSize;
                imageView.getLayoutParams().width = imageView.getDrawable().getIntrinsicWidth();
            }
            imageView.setPivotX(this.mIconSize / 2.0f);
            imageView.setPivotY(this.mIconSize / 2.0f);
            imageView.requestLayout();
            imageView.setRotation(angle);
        }
    }
    
    public int calculateModeIndicatorPosition(final int n) {
        return (int)(this.calculateIconPosition(n) + this.mIconSize / 2.0f - this.mCurrentModeIndicatorView.getBackground().getIntrinsicHeight() / 2.0f);
    }
    
    public ImageView getCurrentModeIndicatorView() {
        return this.mCurrentModeIndicatorView;
    }
    
    public int getModeIndexUnder(final int n, final int n2) {
        if (this.mModeIconsView != null && this.mNavigationEnabled) {
            if (this.mRect == null) {
                this.mRect = new Rect();
            }
            for (int i = 0; i < this.mModeIconsView.getChildCount(); ++i) {
                if (this.mModeIconsView.getChildAt(i).getGlobalVisibleRect(this.mRect) && this.mRect.contains(n, n2)) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }
    
    public ImageView getModeSwitchImageView() {
        if (this.mModeSwitchAnimationContainer == null) {
            this.setupModeSwitchContainer();
        }
        return this.mModeSwitchImageView;
    }
    
    public TextView getModeSwitchNameView() {
        if (this.mModeSwitchAnimationContainer == null) {
            this.setupModeSwitchContainer();
        }
        return this.mModeSwitchNameView;
    }
    
    public void hide() {
        this.setVisibility(4);
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.createIcons();
        this.createCurrentModeIndicator();
        this.updateModeSwitchAnimationContainer();
    }
    
    public void resetContentDescriptionForModeName() {
        this.getModeSwitchNameView().setContentDescription((CharSequence)null);
    }
    
    public void resume(final NavigatorContents navigatorContents) {
        this.mViewIndex = computeViewIndex(navigatorContents);
        this.setIconPositions();
        this.setModeIndicatorPosition(this.mViewIndex);
        this.setModeIconClickable(false);
    }
    
    public void setDraggingPosition(final NavigatorContents navigatorContents, final float f) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setDraggingPosition() draggingRate = ");
            sb.append(f);
            CamLog.d(sb.toString());
        }
        if (!this.mNavigationEnabled) {
            return;
        }
        this.mCurrentModeIndicatorView.setTranslationY((float)this.calculateDraggingPosition(f, computeViewIndex(navigatorContents)));
    }
    
    public void setModeIconClickable(final boolean enabled) {
        for (int i = 0; i < this.mImageList.size(); ++i) {
            this.mImageList.get(i).setEnabled(enabled);
        }
    }
    
    public void setNavigationEnabled(final boolean mNavigationEnabled) {
        this.mNavigationEnabled = mNavigationEnabled;
        int visibility;
        if (this.mNavigationEnabled) {
            visibility = 0;
        }
        else {
            visibility = 4;
        }
        this.setVisibility(visibility);
    }
    
    public void setOrientation(final int n) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setOrientation() orientation = ");
            sb.append(n);
            CamLog.d(sb.toString());
        }
        final int mOrientation = this.mOrientation;
        this.mOrientation = n;
        if (!this.isAttachedToWindow()) {
            return;
        }
        if (mOrientation != n) {
            this.updateNavigatorIcons();
            this.updateModeSwitchAnimationContainer();
        }
    }
    
    public void setVisibility(int visibility) {
        if (!this.mNavigationEnabled) {
            visibility = 4;
        }
        if (this.getVisibility() != visibility) {
            super.setVisibility(visibility);
        }
        this.setModeIconClickable(visibility == 0);
    }
    
    public void setup(final NavigatorContents navigatorContents, final Rect rect, final int mCaptureButtonAreaHeight, final View$OnClickListener iconClickListener) {
        this.mDisplayHeight = rect.height();
        this.mCaptureButtonAreaHeight = mCaptureButtonAreaHeight;
        this.mViewIndex = computeViewIndex(navigatorContents);
        this.setIconPositions();
        this.setIconClickListener(iconClickListener);
        this.setModeIndicatorPosition(this.mViewIndex);
    }
    
    public void show() {
        this.setVisibility(0);
    }
    
    public void updateModeSwitchViews() {
        this.mModeSwitchImageView.setImageResource(this.getSelectedContents().getLargeIconId());
        this.mModeSwitchNameView.setText((CharSequence)this.getSelectedContents().getText(this.getContext()));
        this.sendAccessibilityEventForModeName();
    }
}
