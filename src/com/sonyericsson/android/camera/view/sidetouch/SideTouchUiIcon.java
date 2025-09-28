package com.sonyericsson.android.camera.view.sidetouch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.AutoReviewContent;
import com.sonyericsson.android.camera.view.AutoReviewController;
import com.sonyericsson.android.camera.view.ViewFinderImpl;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonGroup;
import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonItemFactory;
import com.sonyericsson.android.camera.view.baselayout.zoombar.Zoombar;
import com.sonyericsson.android.camera.view.sidetouch.SideTouchUi;
import com.sonyericsson.cameracommon.contentsview.ThumbnailFactory;
import com.sonyericsson.cameracommon.utility.CameraTimer;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonyericsson.cameracommon.viewfinder.recordingindicator.DurationParameterSet;
import com.sonyericsson.cameracommon.viewfinder.recordingindicator.RecordingTimeIndicator;
import com.sonymobile.cameracommon.font.FontUtil;
import java.util.Locale;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class SideTouchUiIcon implements SideTouchUi.Icon {
    protected final Context mContext;
    protected SideTouchUi.Icon.OnDetachedListener mOnDetachedListener;

    protected abstract void attachView(ViewGroup viewGroup);

    protected abstract View getIconView();

    protected void onSidePositionAttached(boolean z) {
    }

    protected SideTouchUiIcon(Context context) {
        this.mContext = context;
    }

    @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUi.Icon
    public View attach(ViewGroup viewGroup, Point point) {
        attachView(viewGroup);
        Rect rect = new Rect();
        viewGroup.getGlobalVisibleRect(rect);
        adjustSidePosition(rect, point);
        return getIconView();
    }

    @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUi.Icon
    public void detach(ViewGroup viewGroup) {
        viewGroup.removeView(getIconView());
        if (this.mOnDetachedListener != null) {
            this.mOnDetachedListener.onDetached(this);
            this.mOnDetachedListener = null;
        }
    }

    @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUi.Icon
    public void setOnDetachedListener(SideTouchUi.Icon.OnDetachedListener onDetachedListener) {
        this.mOnDetachedListener = onDetachedListener;
    }

    @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUi.Icon
    public void onFocusChanged(boolean z) {
        getIconView().setVisibility(z ? 0 : 4);
    }

    private void adjustSidePosition(Rect rect, Point point) {
        int iWidth;
        View iconView = getIconView();
        if (iconView == null) {
            return;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) iconView.getLayoutParams();
        if (layoutParams.width <= 0 || layoutParams.height <= 0) {
            return;
        }
        layoutParams.leftMargin = point.y - (layoutParams.width / 2);
        if (layoutParams.leftMargin < 0) {
            layoutParams.leftMargin = 0;
        }
        if (rect.height() < layoutParams.leftMargin + layoutParams.width) {
            layoutParams.leftMargin = rect.height() - layoutParams.width;
        }
        int iWidth2 = (rect.width() - point.x) - (layoutParams.height / 2);
        if (iWidth2 < layoutParams.height) {
            iWidth = 0;
        } else {
            iWidth = iWidth2 > rect.width() - layoutParams.height ? rect.width() - layoutParams.height : iWidth2;
        }
        layoutParams.topMargin = iWidth;
        iconView.setLayoutParams(layoutParams);
        onSidePositionAttached(iWidth == 0);
    }

    int getDimensionPixelSize(int i) {
        return this.mContext.getResources().getDimensionPixelSize(i);
    }

    String getStringResource(int i) {
        return this.mContext.getResources().getString(i);
    }

    protected static final class TransparentCoverIcon extends SideTouchUiIcon {
        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon
        protected void attachView(ViewGroup viewGroup) {
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon
        protected View getIconView() {
            return null;
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon, com.sonyericsson.android.camera.view.sidetouch.SideTouchUi.Icon
        public void onFocusChanged(boolean z) {
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUi.Icon
        public void setUiOrientation(int i) {
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUi.Icon
        public void show() {
        }

        protected TransparentCoverIcon(Context context) {
            super(context);
        }
    }

    protected static final class CountDownIcon extends SideTouchUiIcon {
        private static final long PUT_IN_ANIMATION_DURATION = 120;
        private SideTouchCountDownCircleView mSideTouchCountDownCircleView;
        private FrameLayout mSideTouchCountDownView;

        public CountDownIcon(Context context) {
            super(context);
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon
        protected void attachView(ViewGroup viewGroup) {
            View.inflate(this.mContext, R.layout.side_touch_countdown, viewGroup);
            this.mSideTouchCountDownView = (FrameLayout) viewGroup.findViewById(R.id.side_touch_countdown);
            this.mSideTouchCountDownCircleView = (SideTouchCountDownCircleView) this.mSideTouchCountDownView.findViewById(R.id.side_touch_countdown_circle);
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon
        protected View getIconView() {
            return this.mSideTouchCountDownView;
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUi.Icon
        public void show() {
            this.mSideTouchCountDownView.setVisibility(0);
            this.mSideTouchCountDownCircleView.setSelfTimer(SelfTimer.SIDE_COUNT_DOWN);
            this.mSideTouchCountDownCircleView.startAnimation();
            TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, ((FrameLayout.LayoutParams) this.mSideTouchCountDownView.getLayoutParams()).topMargin == 0 ? -r0.width : r0.width, 0.0f);
            translateAnimation.setDuration(PUT_IN_ANIMATION_DURATION);
            this.mSideTouchCountDownView.startAnimation(translateAnimation);
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUi.Icon
        public void setUiOrientation(int i) {
            if (i == 1) {
                this.mSideTouchCountDownCircleView.setRotation(270.0f);
            } else {
                this.mSideTouchCountDownCircleView.setRotation(0.0f);
            }
        }
    }

    protected static final class AutoReviewIcon extends SideTouchUiIcon implements AutoReviewContent.ContentReceiver {
        private static final long ANIMATION_DURATION = 200;
        private static final float ANIMATION_HIDE_SCALE = 0.8f;
        private static final float ANIMATION_SHOW_SCALE = 1.2f;
        private static final String TAG = "AutoReviewIcon";
        private final Aspect mAspect;
        private Bitmap mBitmap;
        private AutoReviewController.OnAutoReviewEventListener mCloseListener;
        private ImageView mImageView;
        private int mOrientation;
        private CameraTimer mTimer;

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUi.Icon
        public void show() {
        }

        public enum Aspect {
            TABLET_1_1(R.dimen.autopreview_1_1_width_tablet, R.dimen.autopreview_1_1_height_tablet),
            TABLET_4_3(R.dimen.autopreview_4_3_width_tablet, R.dimen.autopreview_4_3_height_tablet),
            TABLET_16_9(R.dimen.autopreview_16_9_width_tablet, R.dimen.autopreview_16_9_height_tablet),
            PHONE_1_1(R.dimen.autopreview_1_1_width, R.dimen.autopreview_1_1_height),
            PHONE_4_3(R.dimen.autopreview_4_3_width, R.dimen.autopreview_4_3_height),
            PHONE_16_9(R.dimen.autopreview_16_9_width, R.dimen.autopreview_16_9_height);

            final int resource_height;
            final int resource_width;

            Aspect(int i, int i2) {
                this.resource_width = i;
                this.resource_height = i2;
            }
        }

        public AutoReviewIcon(Context context, ViewFinderImpl.AutoReviewContentReceiverProxy autoReviewContentReceiverProxy) {
            super(context);
            autoReviewContentReceiverProxy.bindReceiver(this);
            if (LayoutDependencyResolver.isTablet(context)) {
                if (isPreviewAspectRatio(1, 1)) {
                    this.mAspect = Aspect.TABLET_1_1;
                    return;
                } else if (isPreviewAspectRatio(4, 3)) {
                    this.mAspect = Aspect.TABLET_4_3;
                    return;
                } else {
                    this.mAspect = Aspect.TABLET_16_9;
                    return;
                }
            }
            if (isPreviewAspectRatio(1, 1)) {
                this.mAspect = Aspect.PHONE_1_1;
            } else if (isPreviewAspectRatio(4, 3)) {
                this.mAspect = Aspect.PHONE_4_3;
            } else {
                this.mAspect = Aspect.PHONE_16_9;
            }
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon
        protected void attachView(ViewGroup viewGroup) {
            String stringResource = getStringResource(R.string.cam_strings_accessibility_auto_review_txt);
            this.mImageView = new AutoReviewImageView(this.mContext);
            this.mImageView.setVisibility(8);
            this.mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            this.mImageView.setContentDescription(stringResource);
            this.mImageView.setBackgroundResource(R.drawable.cam_core_autoreview_bg_icn);
            viewGroup.addView(this.mImageView);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mImageView.getLayoutParams();
            layoutParams.width = getDimensionPixelSize(this.mAspect.resource_width);
            layoutParams.height = getDimensionPixelSize(this.mAspect.resource_height);
            int dimensionPixelSize = getDimensionPixelSize(R.dimen.autopreview_shadow_padding) * 2;
            layoutParams.width += dimensionPixelSize;
            layoutParams.height += dimensionPixelSize;
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon
        protected View getIconView() {
            return this.mImageView;
        }

        private void startShowAnimation() {
            AnimationSet animationSet = new AnimationSet(false);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mImageView.getLayoutParams();
            TranslateAnimation translateAnimation = new TranslateAnimation((-(((int) (layoutParams.width * ANIMATION_SHOW_SCALE)) - layoutParams.width)) / 2.0f, 0.0f, (-(((int) (layoutParams.height * ANIMATION_SHOW_SCALE)) - layoutParams.height)) / 2.0f, 0.0f);
            ScaleAnimation scaleAnimation = new ScaleAnimation(ANIMATION_SHOW_SCALE, 1.0f, ANIMATION_SHOW_SCALE, 1.0f);
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            animationSet.addAnimation(scaleAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.addAnimation(translateAnimation);
            animationSet.setDuration(ANIMATION_DURATION);
            this.mImageView.setVisibility(0);
            this.mImageView.startAnimation(animationSet);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void startHideAnimation() {
            AnimationSet animationSet = new AnimationSet(false);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mImageView.getLayoutParams();
            TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, (layoutParams.width * 0.19999999f) / 2.0f, 0.0f, (layoutParams.height * 0.19999999f) / 2.0f);
            ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, ANIMATION_HIDE_SCALE, 1.0f, ANIMATION_HIDE_SCALE);
            AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(alphaAnimation);
            animationSet.addAnimation(scaleAnimation);
            animationSet.setAnimationListener(new Animation.AnimationListener() { // from class: com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon.AutoReviewIcon.1
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation) {
                    AutoReviewIcon.this.detachFromParent();
                }
            });
            animationSet.setDuration(ANIMATION_DURATION);
            this.mImageView.startAnimation(animationSet);
            this.mImageView.setOnClickListener(null);
            this.mImageView.setClickable(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void detachFromParent() {
            ViewParent parent = getIconView().getParent();
            if (parent != null) {
                detach((ViewGroup) parent);
            }
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUi.Icon
        public void setUiOrientation(int i) {
            this.mOrientation = i;
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon, com.sonyericsson.android.camera.view.sidetouch.SideTouchUi.Icon
        public void onFocusChanged(boolean z) {
            if (this.mBitmap == null) {
                return;
            }
            super.onFocusChanged(z);
        }

        @Override // com.sonyericsson.android.camera.view.AutoReviewContent.ContentReceiver
        public void onReceive(AutoReviewContent autoReviewContent) {
            if (autoReviewContent.mIsPhoto) {
                this.mBitmap = AutoReviewController.convertBitmap(this.mContext, autoReviewContent.mUri, autoReviewContent.mData, autoReviewContent.mIsReverse);
            } else {
                this.mBitmap = ThumbnailFactory.createVideoThumbnail(this.mContext, autoReviewContent.mUri, (int) RotationUtil.getAngle(this.mOrientation));
            }
            if (this.mBitmap == null || this.mBitmap.isRecycled()) {
                detachFromParent();
                return;
            }
            this.mCloseListener = autoReviewContent.mEventListener;
            this.mImageView.setImageBitmap(this.mBitmap);
            this.mImageView.setOnClickListener(autoReviewContent.mClickListener);
            startShowAnimation();
            startTimer(autoReviewContent.mDuration);
        }

        private void startTimer(long j) {
            if (CamLog.VERBOSE) {
                CamLog.d("startTimer: " + j);
            }
            stopTimer();
            if (j > 0) {
                this.mTimer = new CameraTimer(j, j, new PreviewTimerHandler(), TAG, 0L);
                this.mTimer.start();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void stopTimer() {
            if (CamLog.VERBOSE) {
                CamLog.d("stopTimer: " + this.mTimer);
            }
            if (this.mTimer != null) {
                this.mTimer.cancel();
                this.mTimer = null;
            }
        }

        private class AutoReviewImageView extends ImageView {
            public AutoReviewImageView(Context context) {
                super(context);
            }

            @Override // android.widget.ImageView, android.view.View
            public void onDetachedFromWindow() {
                super.onDetachedFromWindow();
                AutoReviewIcon.this.stopTimer();
                if (AutoReviewIcon.this.mBitmap != null && !AutoReviewIcon.this.mBitmap.isRecycled()) {
                    AutoReviewIcon.this.mBitmap.recycle();
                }
                if (AutoReviewIcon.this.mCloseListener != null) {
                    AutoReviewIcon.this.mCloseListener.onAutoReviewClosed();
                    AutoReviewIcon.this.mCloseListener = null;
                }
            }
        }

        private class PreviewTimerHandler extends Handler {
            private PreviewTimerHandler() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 1:
                        AutoReviewIcon.this.startHideAnimation();
                        break;
                }
            }
        }

        private boolean isPreviewAspectRatio(int i, int i2) {
            Rect previewSize = PositionConverter.getInstance().getPreviewSize();
            if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
                if (previewSize.width() * i != previewSize.height() * i2) {
                    return false;
                }
            } else if (previewSize.width() * i2 != previewSize.height() * i) {
                return false;
            }
            return true;
        }
    }

    protected static final class ZoomIcon extends SideTouchUiIcon {
        private int mOrientation;
        private FrameLayout mZoomBarLayout;
        private final ViewFinderImpl.ZoomBarUpdateProxy mZoomBarUpdateProxy;
        private Zoombar mZoombar;

        public ZoomIcon(Context context, ViewFinderImpl.ZoomBarUpdateProxy zoomBarUpdateProxy) {
            super(context);
            this.mOrientation = 0;
            this.mZoomBarUpdateProxy = zoomBarUpdateProxy;
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon
        protected void attachView(ViewGroup viewGroup) {
            View.inflate(this.mContext, R.layout.zoombar, viewGroup);
            this.mZoomBarLayout = (FrameLayout) viewGroup.findViewById(R.id.zoombar_base);
            this.mZoombar = (Zoombar) this.mZoomBarLayout.findViewById(R.id.zoombar);
            this.mZoomBarUpdateProxy.bindZoomBar(this.mZoombar);
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon, com.sonyericsson.android.camera.view.sidetouch.SideTouchUi.Icon
        public void detach(final ViewGroup viewGroup) {
            this.mZoombar.setZoombarDisplayChangedListener(new Zoombar.ZoombarDisplayChangedListener() { // from class: com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon.ZoomIcon.1
                @Override // com.sonyericsson.android.camera.view.baselayout.zoombar.Zoombar.ZoombarDisplayChangedListener
                public void onShowZoombar() {
                }

                @Override // com.sonyericsson.android.camera.view.baselayout.zoombar.Zoombar.ZoombarDisplayChangedListener
                public void onZoombarHidden() {
                    ZoomIcon.super.detach(viewGroup);
                }
            });
            this.mZoombar.hideDelayed();
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon
        protected View getIconView() {
            return this.mZoomBarLayout;
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUi.Icon
        public void show() {
            this.mZoombar.show();
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUi.Icon
        public void setUiOrientation(int i) {
            if (this.mOrientation == 0) {
                this.mZoombar.setSensorOrientation(i);
                this.mOrientation = i;
            }
        }
    }

    protected static abstract class ScreenButtonIcon extends SideTouchUiIcon {
        private final ViewFinderImpl.SideTouchUiButtonListenerFactory mListenerFactory;
        protected OnScreenButtonGroup mOnScreenButtonGroup;
        protected final int mOrientation;

        protected abstract void setupButtons();

        public ScreenButtonIcon(Context context, int i, ViewFinderImpl.SideTouchUiButtonListenerFactory sideTouchUiButtonListenerFactory) {
            super(context);
            this.mOrientation = i;
            this.mListenerFactory = sideTouchUiButtonListenerFactory;
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon
        protected void attachView(ViewGroup viewGroup) {
            View.inflate(this.mContext, R.layout.side_touch_on_screen_button_group, viewGroup);
            this.mOnScreenButtonGroup = (OnScreenButtonGroup) viewGroup.findViewById(R.id.side_screen_button_group);
            setupButtons();
            updateIconSize();
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon
        protected View getIconView() {
            return this.mOnScreenButtonGroup;
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUi.Icon
        public void show() {
            this.mOnScreenButtonGroup.show();
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUi.Icon
        public void setUiOrientation(int i) {
            this.mOnScreenButtonGroup.setUiOrientation(i);
        }

        protected void setupButtonMain(OnScreenButtonItemFactory.ButtonType buttonType, int i, boolean z) {
            this.mOnScreenButtonGroup.setMain(OnScreenButtonItemFactory.createButton(buttonType, this.mListenerFactory.create(buttonType)), i, z);
        }

        protected void setupButtonOption1(OnScreenButtonItemFactory.ButtonType buttonType, int i, boolean z) {
            this.mOnScreenButtonGroup.setOption1(OnScreenButtonItemFactory.createButton(buttonType, this.mListenerFactory.create(buttonType)), i, z);
        }

        protected void setupButtonOption2(OnScreenButtonItemFactory.ButtonType buttonType, int i, boolean z) {
            this.mOnScreenButtonGroup.setOption2(OnScreenButtonItemFactory.createButton(buttonType, this.mListenerFactory.create(buttonType)), i, z);
        }

        protected void updateIconSize() {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mOnScreenButtonGroup.getLayoutParams();
            LinearLayout linearLayout = (LinearLayout) this.mOnScreenButtonGroup.findViewById(R.id.capture_button_group_layout);
            int i = 0;
            for (int i2 = 0; i2 < linearLayout.getChildCount(); i2++) {
                View childAt = linearLayout.getChildAt(i2);
                if (childAt.getVisibility() == 0) {
                    LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) childAt.getLayoutParams();
                    if (i > 0) {
                        layoutParams2.leftMargin = getDimensionPixelSize(R.dimen.side_icon_space);
                        i += layoutParams2.leftMargin;
                    }
                    i += layoutParams2.width;
                }
            }
            layoutParams.width = i;
        }
    }

    protected static final class SelfTimerCancelIcon extends ScreenButtonIcon {
        public SelfTimerCancelIcon(Context context, int i, ViewFinderImpl.SideTouchUiButtonListenerFactory sideTouchUiButtonListenerFactory) {
            super(context, i, sideTouchUiButtonListenerFactory);
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon.ScreenButtonIcon
        protected void setupButtons() {
            setupButtonMain(OnScreenButtonItemFactory.ButtonType.CANCEL_SELFTIMER_SIDE, this.mOrientation, true);
        }
    }

    protected static class RecordingIcon extends ScreenButtonIcon implements RecordingTimeIndicator {
        private TextView mRecTimeView;
        private DurationParameterSet mRecordingTime;

        protected RecordingIcon(Context context, int i, ViewFinderImpl.SideTouchUiButtonListenerFactory sideTouchUiButtonListenerFactory, ViewFinderImpl.RecordingTimeReceiverProxy recordingTimeReceiverProxy) {
            super(context, i, sideTouchUiButtonListenerFactory);
            recordingTimeReceiverProxy.bindReceiver(this);
            this.mRecordingTime = new DurationParameterSet();
            this.mRecordingTime.update(recordingTimeReceiverProxy.getCurrentTime());
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon.ScreenButtonIcon, com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon
        protected void attachView(ViewGroup viewGroup) {
            super.attachView(viewGroup);
            this.mRecTimeView = (TextView) this.mOnScreenButtonGroup.findViewById(R.id.recording_time_on_side_touch);
            this.mRecTimeView.setRotation(RotationUtil.getAngle(this.mOrientation));
            this.mRecTimeView.setVisibility(0);
            this.mOnScreenButtonGroup.findViewById(R.id.capture_button_group_layout).setBackgroundResource(R.drawable.cam_side_touch_bg_recording_icn);
            FontUtil.setRobotoFont(this.mRecTimeView, FontUtil.RobotoFontType.BOLD);
            int i = this.mRecTimeView.getLayoutParams().height;
            if (this.mOrientation == 1) {
                int i2 = this.mRecTimeView.getLayoutParams().width;
                this.mOnScreenButtonGroup.getLayoutParams().width += i;
                this.mOnScreenButtonGroup.getLayoutParams().width += getDimensionPixelSize(R.dimen.side_icon_space);
                this.mRecTimeView.setX(this.mRecTimeView.getX() - ((i2 / 2.0f) - (i / 2.0f)));
            } else {
                this.mOnScreenButtonGroup.getLayoutParams().height += i;
                this.mOnScreenButtonGroup.getLayoutParams().height += getDimensionPixelSize(R.dimen.side_icon_space);
            }
            updateRecordingTimeView();
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon
        protected void onSidePositionAttached(boolean z) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ((LinearLayout) this.mOnScreenButtonGroup.findViewById(R.id.capture_button_group_layout)).getLayoutParams();
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.mRecTimeView.getLayoutParams();
            if (this.mOrientation == 1) {
                layoutParams.gravity = 5;
                layoutParams2.gravity = 19;
                return;
            }
            layoutParams2.gravity = 1;
            if (z) {
                layoutParams2.gravity = 80 | layoutParams2.gravity;
            } else {
                layoutParams2.gravity |= 48;
                layoutParams.gravity = 80;
            }
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon.ScreenButtonIcon
        protected void setupButtons() {
            setupButtonMain(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_STOP_RECORDING, this.mOrientation, false);
            setupButtonOption1(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_PAUSE_RECORDING, this.mOrientation, false);
            setupButtonOption2(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_SNAPSHOT_RECORDING, this.mOrientation, true);
        }

        @Override // com.sonyericsson.cameracommon.viewfinder.recordingindicator.RecordingTimeIndicator
        public void onTimeTicked(int i) {
            if (this.mRecordingTime == null || getIconView() == null || !getIconView().isAttachedToWindow()) {
                return;
            }
            this.mRecordingTime.update(i);
            updateRecordingTimeView();
        }

        private void updateRecordingTimeView() {
            String str;
            if (this.mRecordingTime.hour < 1) {
                str = String.format(Locale.US, this.mContext.getString(R.string.cam_status_recoding_time_format_txt), Integer.valueOf(this.mRecordingTime.min), Integer.valueOf(this.mRecordingTime.sec));
            } else {
                str = String.format(Locale.US, this.mContext.getString(R.string.cam_status_recoding_hours_time_format_txt), Integer.valueOf(this.mRecordingTime.hour), Integer.valueOf(this.mRecordingTime.min), Integer.valueOf(this.mRecordingTime.sec));
            }
            this.mRecTimeView.setText(str);
        }
    }

    protected static final class RecordingPauseIcon extends RecordingIcon {
        protected RecordingPauseIcon(Context context, int i, ViewFinderImpl.SideTouchUiButtonListenerFactory sideTouchUiButtonListenerFactory, ViewFinderImpl.RecordingTimeReceiverProxy recordingTimeReceiverProxy) {
            super(context, i, sideTouchUiButtonListenerFactory, recordingTimeReceiverProxy);
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon.RecordingIcon, com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon.ScreenButtonIcon
        protected void setupButtons() {
            setupButtonMain(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_STOP_RECORDING_IN_PAUSE, this.mOrientation, false);
            setupButtonOption1(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_RESUME_RECORDING, this.mOrientation, false);
            setupButtonOption2(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_SNAPSHOT_RECORDING, this.mOrientation, true);
        }
    }

    protected static class RestrictedRecordingIcon extends RecordingIcon {
        protected RestrictedRecordingIcon(Context context, int i, ViewFinderImpl.SideTouchUiButtonListenerFactory sideTouchUiButtonListenerFactory, ViewFinderImpl.RecordingTimeReceiverProxy recordingTimeReceiverProxy) {
            super(context, i, sideTouchUiButtonListenerFactory, recordingTimeReceiverProxy);
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon.RecordingIcon, com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon.ScreenButtonIcon
        protected void setupButtons() {
            setupButtonMain(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_STOP_RECORDING, this.mOrientation, false);
            setupButtonOption1(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_PAUSE_RECORDING, this.mOrientation, false);
        }
    }

    protected static final class RestrictedRecordingPauseIcon extends RestrictedRecordingIcon {
        protected RestrictedRecordingPauseIcon(Context context, int i, ViewFinderImpl.SideTouchUiButtonListenerFactory sideTouchUiButtonListenerFactory, ViewFinderImpl.RecordingTimeReceiverProxy recordingTimeReceiverProxy) {
            super(context, i, sideTouchUiButtonListenerFactory, recordingTimeReceiverProxy);
        }

        @Override // com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon.RestrictedRecordingIcon, com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon.RecordingIcon, com.sonyericsson.android.camera.view.sidetouch.SideTouchUiIcon.ScreenButtonIcon
        protected void setupButtons() {
            setupButtonMain(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_STOP_RECORDING_IN_PAUSE, this.mOrientation, false);
            setupButtonOption1(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_RESUME_RECORDING, this.mOrientation, false);
        }
    }
}
