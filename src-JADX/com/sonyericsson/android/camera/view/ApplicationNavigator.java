package com.sonyericsson.android.camera.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sonyericsson.android.camera.NavigatorContents;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ApplicationNavigator extends LinearLayout {
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

    public ApplicationNavigator(Context context) {
        super(context);
        this.mNavigationEnabled = true;
        setImportantForAccessibility(1);
    }

    public ApplicationNavigator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mNavigationEnabled = true;
        setImportantForAccessibility(1);
    }

    @Override // android.view.View
    protected void onFinishInflate() throws Resources.NotFoundException {
        super.onFinishInflate();
        createIcons();
        createCurrentModeIndicator();
        updateModeSwitchAnimationContainer();
    }

    public void setup(NavigatorContents navigatorContents, Rect rect, int i, View.OnClickListener onClickListener) {
        this.mDisplayHeight = rect.height();
        this.mCaptureButtonAreaHeight = i;
        this.mViewIndex = computeViewIndex(navigatorContents);
        setIconPositions();
        setIconClickListener(onClickListener);
        setModeIndicatorPosition(this.mViewIndex);
    }

    public void resume(NavigatorContents navigatorContents) {
        this.mViewIndex = computeViewIndex(navigatorContents);
        setIconPositions();
        setModeIndicatorPosition(this.mViewIndex);
        setModeIconClickable(false);
    }

    private static int computeViewIndex(NavigatorContents navigatorContents) {
        return computeReverseIndex(NavigatorContents.indexOf(navigatorContents));
    }

    private static int computeReverseIndex(int i) {
        return (NavigatorContents.values().length - i) - 1;
    }

    public void show() {
        setVisibility(0);
    }

    public void hide() {
        setVisibility(4);
    }

    private void sendAccessibilityEventForModeName() {
        if (this.mModeSwitchNameView.getVisibility() == 0) {
            this.mModeSwitchNameView.setContentDescription(getResources().getString(getSelectedContents().getTextId()));
            this.mModeSwitchNameView.sendAccessibilityEvent(32);
        }
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        if (!this.mNavigationEnabled) {
            i = 4;
        }
        if (getVisibility() != i) {
            super.setVisibility(i);
        }
        setModeIconClickable(i == 0);
    }

    public void setNavigationEnabled(boolean z) {
        this.mNavigationEnabled = z;
        setVisibility(this.mNavigationEnabled ? 0 : 4);
    }

    private void createIcons() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
        layoutParams.gravity = 1;
        this.mImageList = new ArrayList();
        this.mModeIconsView = (FrameLayout) findViewById(R.id.mode_icons);
        for (int i = 0; i < NavigatorContents.values().length; i++) {
            NavigatorContents navigatorContents = NavigatorContents.values()[(NavigatorContents.values().length - i) - 1];
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(layoutParams);
            imageView.setFocusable(true);
            imageView.setClickable(true);
            imageView.setBackgroundResource(R.drawable.application_navigator_icon_selector);
            imageView.setTag(Integer.valueOf(i));
            imageView.setImageResource(navigatorContents.getIconId());
            imageView.setContentDescription(navigatorContents.getText(getContext()));
            imageView.setSoundEffectsEnabled(false);
            this.mImageList.add(imageView);
            this.mModeIconsView.addView(imageView);
        }
        this.mIconSize = getContext().getResources().getDimensionPixelSize(R.dimen.navigator_container_width);
        findViewById(R.id.apps_bar).setMinimumHeight(this.mImageList.size() * this.mIconSize);
    }

    private void createCurrentModeIndicator() {
        this.mCurrentModeIndicatorView = (ImageView) findViewById(R.id.current_mode_indicator);
    }

    private void createModeSwitchContainer() {
        this.mModeSwitchAnimationContainer = (FrameLayout) ((ViewStub) findViewById(R.id.stub_mode_switch_container)).inflate();
        this.mModeSwitchViewContainer = (FrameLayout) this.mModeSwitchAnimationContainer.findViewById(R.id.layout_mode_switch_container);
        this.mModeSwitchNameView = (TextView) this.mModeSwitchAnimationContainer.findViewById(R.id.mode_switch_animation_name);
        this.mModeSwitchNameView.setAlpha(0.0f);
        this.mModeSwitchImageView = (ImageView) this.mModeSwitchAnimationContainer.findViewById(R.id.mode_switch_animation_icon);
        this.mModeSwitchImageView.setAlpha(0.0f);
        this.mModeSwitchImageView.setScaleType(ImageView.ScaleType.CENTER);
    }

    private void setModeIndicatorPosition(int i) {
        this.mCurrentModeIndicatorView.setTranslationY(calculateModeIndicatorPosition(i));
    }

    private void setIconPositions() {
        for (int i = 0; i < this.mImageList.size(); i++) {
            this.mImageList.get(i).setTranslationY(calculateIconPosition(i));
        }
    }

    private void setIconClickListener(View.OnClickListener onClickListener) {
        for (int i = 0; i < this.mImageList.size(); i++) {
            this.mImageList.get(i).setOnClickListener(onClickListener);
        }
    }

    public void setModeIconClickable(boolean z) {
        for (int i = 0; i < this.mImageList.size(); i++) {
            this.mImageList.get(i).setEnabled(z);
        }
    }

    private int calculateIconPosition(int i) {
        return (int) (((i - 1) * this.mIconSize) + (this.mDisplayHeight / 2.0f));
    }

    public int calculateModeIndicatorPosition(int i) {
        return (int) ((calculateIconPosition(i) + (this.mIconSize / 2.0f)) - (this.mCurrentModeIndicatorView.getBackground().getIntrinsicHeight() / 2.0f));
    }

    public void updateModeSwitchViews() {
        this.mModeSwitchImageView.setImageResource(getSelectedContents().getLargeIconId());
        this.mModeSwitchNameView.setText(getSelectedContents().getText(getContext()));
        sendAccessibilityEventForModeName();
    }

    public void setDraggingPosition(NavigatorContents navigatorContents, float f) {
        if (CamLog.VERBOSE) {
            CamLog.d("setDraggingPosition() draggingRate = " + f);
        }
        if (this.mNavigationEnabled) {
            this.mCurrentModeIndicatorView.setTranslationY(calculateDraggingPosition(f, computeViewIndex(navigatorContents)));
        }
    }

    private int calculateDraggingPosition(float f, int i) {
        return (int) (calculateModeIndicatorPosition(i) + (f * this.mIconSize));
    }

    @Override // android.widget.LinearLayout
    public void setOrientation(int i) throws Resources.NotFoundException {
        if (CamLog.VERBOSE) {
            CamLog.d("setOrientation() orientation = " + i);
        }
        int i2 = this.mOrientation;
        this.mOrientation = i;
        if (isAttachedToWindow() && i2 != i) {
            updateNavigatorIcons();
            updateModeSwitchAnimationContainer();
        }
    }

    private void updateNavigatorIcons() {
        float angle = RotationUtil.getAngle(this.mOrientation);
        for (ImageView imageView : this.mImageList) {
            if (this.mOrientation == 1) {
                imageView.getLayoutParams().width = this.mIconSize;
                imageView.getLayoutParams().height = imageView.getDrawable().getIntrinsicWidth();
            } else {
                imageView.getLayoutParams().height = this.mIconSize;
                imageView.getLayoutParams().width = imageView.getDrawable().getIntrinsicWidth();
            }
            imageView.setPivotX(this.mIconSize / 2.0f);
            imageView.setPivotY(this.mIconSize / 2.0f);
            imageView.requestLayout();
            imageView.setRotation(angle);
        }
    }

    private void updateModeSwitchAnimationContainer() throws Resources.NotFoundException {
        int height;
        if (this.mModeSwitchAnimationContainer == null) {
            return;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mModeSwitchAnimationContainer.getLayoutParams();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        int i = displayMetrics.heightPixels > displayMetrics.widthPixels ? displayMetrics.widthPixels : displayMetrics.heightPixels;
        if (this.mOrientation == 1) {
            height = this.mModeSwitchAnimationContainer.getWidth();
        } else {
            height = this.mModeSwitchAnimationContainer.getHeight();
        }
        if (height != i) {
            layoutParams.width = i;
            layoutParams.height = i;
            this.mModeSwitchAnimationContainer.setLayoutParams(layoutParams);
        }
        int i2 = displayMetrics.heightPixels > displayMetrics.widthPixels ? displayMetrics.heightPixels : displayMetrics.widthPixels;
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R.dimen.left_icon_area_height);
        int dimensionPixelSize2 = getContext().getResources().getDimensionPixelSize(R.dimen.navigator_container_width);
        int dimensionPixelSize3 = getContext().getResources().getDimensionPixelSize(R.dimen.mode_switch_image_size);
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.mModeSwitchViewContainer.getLayoutParams();
        int i3 = ((((i2 - dimensionPixelSize) - dimensionPixelSize2) - this.mCaptureButtonAreaHeight) - dimensionPixelSize3) / 2;
        if (this.mOrientation == 2) {
            layoutParams2.setMargins(0, 0, i3, 0);
            layoutParams2.gravity = 8388629;
        } else {
            layoutParams2.setMargins(0, 0, 0, i3);
            layoutParams2.gravity = 81;
        }
        this.mModeSwitchViewContainer.setLayoutParams(layoutParams2);
        this.mModeSwitchAnimationContainer.setRotation(RotationUtil.getAngle(this.mOrientation));
    }

    public TextView getModeSwitchNameView() throws Resources.NotFoundException {
        if (this.mModeSwitchAnimationContainer == null) {
            setupModeSwitchContainer();
        }
        return this.mModeSwitchNameView;
    }

    public ImageView getCurrentModeIndicatorView() {
        return this.mCurrentModeIndicatorView;
    }

    private NavigatorContents getSelectedContents() {
        NavigatorContents navigatorContents = NavigatorContents.SUPERIOR_AUTO;
        float translationY = this.mCurrentModeIndicatorView.getTranslationY();
        float height = this.mCurrentModeIndicatorView.getHeight() + translationY;
        for (int i = 0; i < this.mImageList.size(); i++) {
            float translationY2 = this.mImageList.get(i).getTranslationY() + (this.mImageList.get(i).getHeight() / 2.0f);
            if (translationY <= translationY2 && translationY2 <= height) {
                return NavigatorContents.values()[computeReverseIndex(i)];
            }
        }
        return navigatorContents;
    }

    public void resetContentDescriptionForModeName() {
        getModeSwitchNameView().setContentDescription(null);
    }

    public int getModeIndexUnder(int i, int i2) {
        if (this.mModeIconsView == null || !this.mNavigationEnabled) {
            return -1;
        }
        if (this.mRect == null) {
            this.mRect = new Rect();
        }
        for (int i3 = 0; i3 < this.mModeIconsView.getChildCount(); i3++) {
            if (this.mModeIconsView.getChildAt(i3).getGlobalVisibleRect(this.mRect) && this.mRect.contains(i, i2)) {
                return i3;
            }
        }
        return -1;
    }

    public ImageView getModeSwitchImageView() throws Resources.NotFoundException {
        if (this.mModeSwitchAnimationContainer == null) {
            setupModeSwitchContainer();
        }
        return this.mModeSwitchImageView;
    }

    private void setupModeSwitchContainer() throws Resources.NotFoundException {
        createModeSwitchContainer();
        updateModeSwitchAnimationContainer();
    }
}
