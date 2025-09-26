// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.sidetouch;

import com.sonyericsson.android.camera.view.baselayout.zoombar.Zoombar;
import android.widget.LinearLayout$LayoutParams;
import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonGroup;
import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonItemFactory;
import android.widget.LinearLayout;
import com.sonymobile.cameracommon.font.FontUtil;
import java.util.Locale;
import com.sonyericsson.cameracommon.viewfinder.recordingindicator.DurationParameterSet;
import android.widget.TextView;
import com.sonyericsson.cameracommon.viewfinder.recordingindicator.RecordingTimeIndicator;
import com.sonyericsson.cameracommon.settings.SelfTimerInterface;
import com.sonyericsson.android.camera.configuration.parameters.SelfTimer;
import android.widget.FrameLayout;
import android.os.Message;
import com.sonyericsson.cameracommon.contentsview.ThumbnailFactory;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import android.widget.ImageView$ScaleType;
import android.os.Handler;
import com.sonyericsson.android.camera.util.CamLog;
import android.view.View$OnClickListener;
import android.view.animation.Animation$AnimationListener;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.AnimationSet;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import android.view.ViewParent;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.view.ViewFinderImpl;
import com.sonyericsson.cameracommon.utility.CameraTimer;
import android.widget.ImageView;
import com.sonyericsson.android.camera.view.AutoReviewController;
import android.graphics.Bitmap;
import com.sonyericsson.android.camera.view.AutoReviewContent;
import android.view.ViewGroup;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import android.graphics.Point;
import android.graphics.Rect;
import android.content.Context;

public abstract class SideTouchUiIcon implements Icon
{
    protected final Context mContext;
    protected OnDetachedListener mOnDetachedListener;
    
    protected SideTouchUiIcon(final Context mContext) {
        this.mContext = mContext;
    }
    
    private void adjustSidePosition(final Rect rect, final Point point) {
        final View iconView = this.getIconView();
        if (iconView == null) {
            return;
        }
        final FrameLayout$LayoutParams layoutParams = (FrameLayout$LayoutParams)iconView.getLayoutParams();
        if (layoutParams.width > 0 && layoutParams.height > 0) {
            layoutParams.leftMargin = point.y - layoutParams.width / 2;
            final int leftMargin = layoutParams.leftMargin;
            boolean b = false;
            if (leftMargin < 0) {
                layoutParams.leftMargin = 0;
            }
            if (rect.height() < layoutParams.leftMargin + layoutParams.width) {
                layoutParams.leftMargin = rect.height() - layoutParams.width;
            }
            int topMargin = rect.width() - point.x - layoutParams.height / 2;
            if (topMargin < layoutParams.height) {
                topMargin = 0;
            }
            else if (topMargin > rect.width() - layoutParams.height) {
                topMargin = rect.width() - layoutParams.height;
            }
            layoutParams.topMargin = topMargin;
            iconView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            if (topMargin == 0) {
                b = true;
            }
            this.onSidePositionAttached(b);
        }
    }
    
    @Override
    public View attach(final ViewGroup viewGroup, final Point point) {
        this.attachView(viewGroup);
        final Rect rect = new Rect();
        viewGroup.getGlobalVisibleRect(rect);
        this.adjustSidePosition(rect, point);
        return this.getIconView();
    }
    
    protected abstract void attachView(final ViewGroup p0);
    
    @Override
    public void detach(final ViewGroup viewGroup) {
        viewGroup.removeView(this.getIconView());
        if (this.mOnDetachedListener != null) {
            this.mOnDetachedListener.onDetached((SideTouchUi.Icon)this);
            this.mOnDetachedListener = null;
        }
    }
    
    int getDimensionPixelSize(final int n) {
        return this.mContext.getResources().getDimensionPixelSize(n);
    }
    
    protected abstract View getIconView();
    
    String getStringResource(final int n) {
        return this.mContext.getResources().getString(n);
    }
    
    @Override
    public void onFocusChanged(final boolean b) {
        final View iconView = this.getIconView();
        int visibility;
        if (b) {
            visibility = 0;
        }
        else {
            visibility = 4;
        }
        iconView.setVisibility(visibility);
    }
    
    protected void onSidePositionAttached(final boolean b) {
    }
    
    @Override
    public void setOnDetachedListener(final OnDetachedListener mOnDetachedListener) {
        this.mOnDetachedListener = mOnDetachedListener;
    }
    
    protected static final class AutoReviewIcon extends SideTouchUiIcon implements ContentReceiver
    {
        private static final long ANIMATION_DURATION = 200L;
        private static final float ANIMATION_HIDE_SCALE = 0.8f;
        private static final float ANIMATION_SHOW_SCALE = 1.2f;
        private static final String TAG = "AutoReviewIcon";
        private final Aspect mAspect;
        private Bitmap mBitmap;
        private AutoReviewController.OnAutoReviewEventListener mCloseListener;
        private ImageView mImageView;
        private int mOrientation;
        private CameraTimer mTimer;
        
        public AutoReviewIcon(final Context context, final ViewFinderImpl.AutoReviewContentReceiverProxy autoReviewContentReceiverProxy) {
            super(context);
            autoReviewContentReceiverProxy.bindReceiver(this);
            if (LayoutDependencyResolver.isTablet(context)) {
                if (this.isPreviewAspectRatio(1, 1)) {
                    this.mAspect = Aspect.TABLET_1_1;
                }
                else if (this.isPreviewAspectRatio(4, 3)) {
                    this.mAspect = Aspect.TABLET_4_3;
                }
                else {
                    this.mAspect = Aspect.TABLET_16_9;
                }
            }
            else if (this.isPreviewAspectRatio(1, 1)) {
                this.mAspect = Aspect.PHONE_1_1;
            }
            else if (this.isPreviewAspectRatio(4, 3)) {
                this.mAspect = Aspect.PHONE_4_3;
            }
            else {
                this.mAspect = Aspect.PHONE_16_9;
            }
        }
        
        private void detachFromParent() {
            final ViewParent parent = this.getIconView().getParent();
            if (parent != null) {
                this.detach((ViewGroup)parent);
            }
        }
        
        private boolean isPreviewAspectRatio(final int n, final int n2) {
            final Rect previewSize = PositionConverter.getInstance().getPreviewSize();
            final LayoutOrientationResolver.LayoutOrientationType orientation = LayoutOrientationResolver.getInstance().getOrientation();
            final LayoutOrientationResolver.LayoutOrientationType portrait = LayoutOrientationResolver.LayoutOrientationType.PORTRAIT;
            boolean b = false;
            if (orientation == portrait) {
                if (previewSize.width() * n != previewSize.height() * n2) {
                    return b;
                }
            }
            else if (previewSize.width() * n2 != previewSize.height() * n) {
                return b;
            }
            b = true;
            return b;
        }
        
        private void startHideAnimation() {
            final AnimationSet set = new AnimationSet(false);
            final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.mImageView.getLayoutParams();
            final TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, frameLayout$LayoutParams.width * 0.19999999f / 2.0f, 0.0f, frameLayout$LayoutParams.height * 0.19999999f / 2.0f);
            final ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 0.8f, 1.0f, 0.8f);
            final AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
            set.addAnimation((Animation)translateAnimation);
            set.addAnimation((Animation)alphaAnimation);
            set.addAnimation((Animation)scaleAnimation);
            set.setAnimationListener((Animation$AnimationListener)new Animation$AnimationListener(this) {
                final AutoReviewIcon this$0;
                
                public void onAnimationEnd(final Animation animation) {
                    this.this$0.detachFromParent();
                }
                
                public void onAnimationRepeat(final Animation animation) {
                }
                
                public void onAnimationStart(final Animation animation) {
                }
            });
            set.setDuration(200L);
            this.mImageView.startAnimation((Animation)set);
            this.mImageView.setOnClickListener((View$OnClickListener)null);
            this.mImageView.setClickable(false);
        }
        
        private void startShowAnimation() {
            final AnimationSet set = new AnimationSet(false);
            final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.mImageView.getLayoutParams();
            final TranslateAnimation translateAnimation = new TranslateAnimation(-(float)((int)(frameLayout$LayoutParams.width * 1.2f) - frameLayout$LayoutParams.width) / 2.0f, 0.0f, -(float)((int)(frameLayout$LayoutParams.height * 1.2f) - frameLayout$LayoutParams.height) / 2.0f, 0.0f);
            final ScaleAnimation scaleAnimation = new ScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f);
            final AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            set.addAnimation((Animation)scaleAnimation);
            set.addAnimation((Animation)alphaAnimation);
            set.addAnimation((Animation)translateAnimation);
            set.setDuration(200L);
            this.mImageView.setVisibility(0);
            this.mImageView.startAnimation((Animation)set);
        }
        
        private void startTimer(final long lng) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("startTimer: ");
                sb.append(lng);
                CamLog.d(sb.toString());
            }
            this.stopTimer();
            if (lng > 0L) {
                (this.mTimer = new CameraTimer(lng, lng, new PreviewTimerHandler(), "AutoReviewIcon", 0L)).start();
            }
        }
        
        private void stopTimer() {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("stopTimer: ");
                sb.append(this.mTimer);
                CamLog.d(sb.toString());
            }
            if (this.mTimer != null) {
                this.mTimer.cancel();
                this.mTimer = null;
            }
        }
        
        @Override
        protected void attachView(final ViewGroup viewGroup) {
            final String stringResource = this.getStringResource(2131689562);
            (this.mImageView = new AutoReviewImageView(this.mContext)).setVisibility(8);
            this.mImageView.setScaleType(ImageView$ScaleType.FIT_XY);
            this.mImageView.setContentDescription((CharSequence)stringResource);
            this.mImageView.setBackgroundResource(2131230950);
            viewGroup.addView((View)this.mImageView);
            final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.mImageView.getLayoutParams();
            frameLayout$LayoutParams.width = this.getDimensionPixelSize(this.mAspect.resource_width);
            frameLayout$LayoutParams.height = this.getDimensionPixelSize(this.mAspect.resource_height);
            final int dimensionPixelSize = this.getDimensionPixelSize(2131165278);
            final int width = frameLayout$LayoutParams.width;
            final int n = dimensionPixelSize * 2;
            frameLayout$LayoutParams.width = width + n;
            frameLayout$LayoutParams.height += n;
        }
        
        @Override
        protected View getIconView() {
            return (View)this.mImageView;
        }
        
        @Override
        public void onFocusChanged(final boolean b) {
            if (this.mBitmap == null) {
                return;
            }
            super.onFocusChanged(b);
        }
        
        @Override
        public void onReceive(final AutoReviewContent autoReviewContent) {
            if (autoReviewContent.mIsPhoto) {
                this.mBitmap = AutoReviewController.convertBitmap(this.mContext, autoReviewContent.mUri, autoReviewContent.mData, autoReviewContent.mIsReverse);
            }
            else {
                this.mBitmap = ThumbnailFactory.createVideoThumbnail(this.mContext, autoReviewContent.mUri, (int)RotationUtil.getAngle(this.mOrientation));
            }
            if (this.mBitmap != null && !this.mBitmap.isRecycled()) {
                this.mCloseListener = autoReviewContent.mEventListener;
                this.mImageView.setImageBitmap(this.mBitmap);
                this.mImageView.setOnClickListener(autoReviewContent.mClickListener);
                this.startShowAnimation();
                this.startTimer(autoReviewContent.mDuration);
                return;
            }
            this.detachFromParent();
        }
        
        @Override
        public void setUiOrientation(final int mOrientation) {
            this.mOrientation = mOrientation;
        }
        
        @Override
        public void show() {
        }
        
        public enum Aspect
        {
            private static final Aspect[] $VALUES;
            
            PHONE_16_9(2131165267, 2131165265), 
            PHONE_1_1(2131165271, 2131165269), 
            PHONE_4_3(2131165275, 2131165273), 
            TABLET_16_9(2131165268, 2131165266), 
            TABLET_1_1(2131165272, 2131165270), 
            TABLET_4_3(2131165276, 2131165274);
            
            final int resource_height;
            final int resource_width;
            
            static {
                $VALUES = new Aspect[] { Aspect.TABLET_1_1, Aspect.TABLET_4_3, Aspect.TABLET_16_9, Aspect.PHONE_1_1, Aspect.PHONE_4_3, Aspect.PHONE_16_9 };
            }
            
            private Aspect(final int resource_width, final int resource_height) {
                this.resource_width = resource_width;
                this.resource_height = resource_height;
            }
        }
        
        private class AutoReviewImageView extends ImageView
        {
            final AutoReviewIcon this$0;
            
            public AutoReviewImageView(final AutoReviewIcon this$0, final Context context) {
                this.this$0 = this$0;
                super(context);
            }
            
            public void onDetachedFromWindow() {
                super.onDetachedFromWindow();
                this.this$0.stopTimer();
                if (this.this$0.mBitmap != null && !this.this$0.mBitmap.isRecycled()) {
                    this.this$0.mBitmap.recycle();
                }
                if (this.this$0.mCloseListener != null) {
                    this.this$0.mCloseListener.onAutoReviewClosed();
                    this.this$0.mCloseListener = null;
                }
            }
        }
        
        private class PreviewTimerHandler extends Handler
        {
            final AutoReviewIcon this$0;
            
            private PreviewTimerHandler(final AutoReviewIcon this$0) {
                this.this$0 = this$0;
            }
            
            public void handleMessage(final Message message) {
                switch (message.what) {
                    default: {
                        return;
                    }
                    case 1: {
                        this.this$0.startHideAnimation();
                    }
                    case 0:
                    case 2:
                    case 3: {}
                }
            }
        }
    }
    
    protected static final class CountDownIcon extends SideTouchUiIcon
    {
        private static final long PUT_IN_ANIMATION_DURATION = 120L;
        private SideTouchCountDownCircleView mSideTouchCountDownCircleView;
        private FrameLayout mSideTouchCountDownView;
        
        public CountDownIcon(final Context context) {
            super(context);
        }
        
        @Override
        protected void attachView(final ViewGroup viewGroup) {
            View.inflate(this.mContext, 2131493011, viewGroup);
            this.mSideTouchCountDownView = (FrameLayout)viewGroup.findViewById(2131296609);
            this.mSideTouchCountDownCircleView = (SideTouchCountDownCircleView)this.mSideTouchCountDownView.findViewById(2131296610);
        }
        
        @Override
        protected View getIconView() {
            return (View)this.mSideTouchCountDownView;
        }
        
        @Override
        public void setUiOrientation(final int n) {
            if (n == 1) {
                this.mSideTouchCountDownCircleView.setRotation(270.0f);
            }
            else {
                this.mSideTouchCountDownCircleView.setRotation(0.0f);
            }
        }
        
        @Override
        public void show() {
            this.mSideTouchCountDownView.setVisibility(0);
            this.mSideTouchCountDownCircleView.setSelfTimer(SelfTimer.SIDE_COUNT_DOWN);
            this.mSideTouchCountDownCircleView.startAnimation();
            final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.mSideTouchCountDownView.getLayoutParams();
            int width;
            if (frameLayout$LayoutParams.topMargin == 0) {
                width = -frameLayout$LayoutParams.width;
            }
            else {
                width = frameLayout$LayoutParams.width;
            }
            final TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float)width, 0.0f);
            translateAnimation.setDuration(120L);
            this.mSideTouchCountDownView.startAnimation((Animation)translateAnimation);
        }
    }
    
    protected static class RecordingIcon extends ScreenButtonIcon implements RecordingTimeIndicator
    {
        private TextView mRecTimeView;
        private DurationParameterSet mRecordingTime;
        
        protected RecordingIcon(final Context context, final int n, final ViewFinderImpl.SideTouchUiButtonListenerFactory sideTouchUiButtonListenerFactory, final ViewFinderImpl.RecordingTimeReceiverProxy recordingTimeReceiverProxy) {
            super(context, n, sideTouchUiButtonListenerFactory);
            recordingTimeReceiverProxy.bindReceiver(this);
            (this.mRecordingTime = new DurationParameterSet()).update(recordingTimeReceiverProxy.getCurrentTime());
        }
        
        private void updateRecordingTimeView() {
            String text;
            if (this.mRecordingTime.hour < 1) {
                text = String.format(Locale.US, this.mContext.getString(2131689547), this.mRecordingTime.min, this.mRecordingTime.sec);
            }
            else {
                text = String.format(Locale.US, this.mContext.getString(2131689546), this.mRecordingTime.hour, this.mRecordingTime.min, this.mRecordingTime.sec);
            }
            this.mRecTimeView.setText((CharSequence)text);
        }
        
        @Override
        protected void attachView(final ViewGroup viewGroup) {
            super.attachView(viewGroup);
            (this.mRecTimeView = (TextView)this.mOnScreenButtonGroup.findViewById(2131296526)).setRotation(RotationUtil.getAngle(this.mOrientation));
            this.mRecTimeView.setVisibility(0);
            this.mOnScreenButtonGroup.findViewById(2131296343).setBackgroundResource(2131231321);
            FontUtil.setRobotoFont(this.mRecTimeView, FontUtil.RobotoFontType.BOLD);
            final int height = this.mRecTimeView.getLayoutParams().height;
            if (this.mOrientation == 1) {
                final int width = this.mRecTimeView.getLayoutParams().width;
                final ViewGroup$LayoutParams layoutParams = this.mOnScreenButtonGroup.getLayoutParams();
                layoutParams.width += height;
                final ViewGroup$LayoutParams layoutParams2 = this.mOnScreenButtonGroup.getLayoutParams();
                layoutParams2.width += this.getDimensionPixelSize(2131165625);
                this.mRecTimeView.setX(this.mRecTimeView.getX() - (width / 2.0f - height / 2.0f));
            }
            else {
                final ViewGroup$LayoutParams layoutParams3 = this.mOnScreenButtonGroup.getLayoutParams();
                layoutParams3.height += height;
                final ViewGroup$LayoutParams layoutParams4 = this.mOnScreenButtonGroup.getLayoutParams();
                layoutParams4.height += this.getDimensionPixelSize(2131165625);
            }
            this.updateRecordingTimeView();
        }
        
        @Override
        protected void onSidePositionAttached(final boolean b) {
            final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)((LinearLayout)this.mOnScreenButtonGroup.findViewById(2131296343)).getLayoutParams();
            final FrameLayout$LayoutParams frameLayout$LayoutParams2 = (FrameLayout$LayoutParams)this.mRecTimeView.getLayoutParams();
            if (this.mOrientation == 1) {
                frameLayout$LayoutParams.gravity = 5;
                frameLayout$LayoutParams2.gravity = 19;
            }
            else {
                frameLayout$LayoutParams2.gravity = 1;
                if (b) {
                    frameLayout$LayoutParams2.gravity |= 0x50;
                }
                else {
                    frameLayout$LayoutParams2.gravity |= 0x30;
                    frameLayout$LayoutParams.gravity = 80;
                }
            }
        }
        
        @Override
        public void onTimeTicked(final int n) {
            if (this.mRecordingTime != null && ((ScreenButtonIcon)this).getIconView() != null && ((ScreenButtonIcon)this).getIconView().isAttachedToWindow()) {
                this.mRecordingTime.update(n);
                this.updateRecordingTimeView();
            }
        }
        
        @Override
        protected void setupButtons() {
            ((ScreenButtonIcon)this).setupButtonMain(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_STOP_RECORDING, this.mOrientation, false);
            ((ScreenButtonIcon)this).setupButtonOption1(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_PAUSE_RECORDING, this.mOrientation, false);
            ((ScreenButtonIcon)this).setupButtonOption2(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_SNAPSHOT_RECORDING, this.mOrientation, true);
        }
    }
    
    protected abstract static class ScreenButtonIcon extends SideTouchUiIcon
    {
        private final ViewFinderImpl.SideTouchUiButtonListenerFactory mListenerFactory;
        protected OnScreenButtonGroup mOnScreenButtonGroup;
        protected final int mOrientation;
        
        public ScreenButtonIcon(final Context context, final int mOrientation, final ViewFinderImpl.SideTouchUiButtonListenerFactory mListenerFactory) {
            super(context);
            this.mOrientation = mOrientation;
            this.mListenerFactory = mListenerFactory;
        }
        
        @Override
        protected void attachView(final ViewGroup viewGroup) {
            View.inflate(this.mContext, 2131493012, viewGroup);
            this.mOnScreenButtonGroup = (OnScreenButtonGroup)viewGroup.findViewById(2131296608);
            this.setupButtons();
            this.updateIconSize();
        }
        
        @Override
        protected View getIconView() {
            return (View)this.mOnScreenButtonGroup;
        }
        
        @Override
        public void setUiOrientation(final int uiOrientation) {
            this.mOnScreenButtonGroup.setUiOrientation(uiOrientation);
        }
        
        protected void setupButtonMain(final OnScreenButtonItemFactory.ButtonType buttonType, final int n, final boolean b) {
            this.mOnScreenButtonGroup.setMain(OnScreenButtonItemFactory.createButton(buttonType, this.mListenerFactory.create(buttonType)), n, b);
        }
        
        protected void setupButtonOption1(final OnScreenButtonItemFactory.ButtonType buttonType, final int n, final boolean b) {
            this.mOnScreenButtonGroup.setOption1(OnScreenButtonItemFactory.createButton(buttonType, this.mListenerFactory.create(buttonType)), n, b);
        }
        
        protected void setupButtonOption2(final OnScreenButtonItemFactory.ButtonType buttonType, final int n, final boolean b) {
            this.mOnScreenButtonGroup.setOption2(OnScreenButtonItemFactory.createButton(buttonType, this.mListenerFactory.create(buttonType)), n, b);
        }
        
        protected abstract void setupButtons();
        
        @Override
        public void show() {
            this.mOnScreenButtonGroup.show();
        }
        
        protected void updateIconSize() {
            final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.mOnScreenButtonGroup.getLayoutParams();
            final LinearLayout linearLayout = (LinearLayout)this.mOnScreenButtonGroup.findViewById(2131296343);
            int i = 0;
            int width = 0;
            while (i < linearLayout.getChildCount()) {
                final View child = linearLayout.getChildAt(i);
                int n = width;
                if (child.getVisibility() == 0) {
                    final LinearLayout$LayoutParams linearLayout$LayoutParams = (LinearLayout$LayoutParams)child.getLayoutParams();
                    int n2;
                    if ((n2 = width) > 0) {
                        linearLayout$LayoutParams.leftMargin = this.getDimensionPixelSize(2131165625);
                        n2 = width + linearLayout$LayoutParams.leftMargin;
                    }
                    n = n2 + linearLayout$LayoutParams.width;
                }
                ++i;
                width = n;
            }
            frameLayout$LayoutParams.width = width;
        }
    }
    
    protected static final class RecordingPauseIcon extends RecordingIcon
    {
        protected RecordingPauseIcon(final Context context, final int n, final ViewFinderImpl.SideTouchUiButtonListenerFactory sideTouchUiButtonListenerFactory, final ViewFinderImpl.RecordingTimeReceiverProxy recordingTimeReceiverProxy) {
            super(context, n, sideTouchUiButtonListenerFactory, recordingTimeReceiverProxy);
        }
        
        @Override
        protected void setupButtons() {
            ((ScreenButtonIcon)this).setupButtonMain(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_STOP_RECORDING_IN_PAUSE, this.mOrientation, false);
            ((ScreenButtonIcon)this).setupButtonOption1(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_RESUME_RECORDING, this.mOrientation, false);
            ((ScreenButtonIcon)this).setupButtonOption2(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_SNAPSHOT_RECORDING, this.mOrientation, true);
        }
    }
    
    protected static class RestrictedRecordingIcon extends RecordingIcon
    {
        protected RestrictedRecordingIcon(final Context context, final int n, final ViewFinderImpl.SideTouchUiButtonListenerFactory sideTouchUiButtonListenerFactory, final ViewFinderImpl.RecordingTimeReceiverProxy recordingTimeReceiverProxy) {
            super(context, n, sideTouchUiButtonListenerFactory, recordingTimeReceiverProxy);
        }
        
        @Override
        protected void setupButtons() {
            ((ScreenButtonIcon)this).setupButtonMain(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_STOP_RECORDING, this.mOrientation, false);
            ((ScreenButtonIcon)this).setupButtonOption1(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_PAUSE_RECORDING, this.mOrientation, false);
        }
    }
    
    protected static final class RestrictedRecordingPauseIcon extends RestrictedRecordingIcon
    {
        protected RestrictedRecordingPauseIcon(final Context context, final int n, final ViewFinderImpl.SideTouchUiButtonListenerFactory sideTouchUiButtonListenerFactory, final ViewFinderImpl.RecordingTimeReceiverProxy recordingTimeReceiverProxy) {
            super(context, n, sideTouchUiButtonListenerFactory, recordingTimeReceiverProxy);
        }
        
        @Override
        protected void setupButtons() {
            ((ScreenButtonIcon)this).setupButtonMain(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_STOP_RECORDING_IN_PAUSE, this.mOrientation, false);
            ((ScreenButtonIcon)this).setupButtonOption1(OnScreenButtonItemFactory.ButtonType.SIDE_TOUCH_RESUME_RECORDING, this.mOrientation, false);
        }
    }
    
    protected static final class SelfTimerCancelIcon extends ScreenButtonIcon
    {
        public SelfTimerCancelIcon(final Context context, final int n, final ViewFinderImpl.SideTouchUiButtonListenerFactory sideTouchUiButtonListenerFactory) {
            super(context, n, sideTouchUiButtonListenerFactory);
        }
        
        @Override
        protected void setupButtons() {
            ((ScreenButtonIcon)this).setupButtonMain(OnScreenButtonItemFactory.ButtonType.CANCEL_SELFTIMER_SIDE, this.mOrientation, true);
        }
    }
    
    protected static final class TransparentCoverIcon extends SideTouchUiIcon
    {
        protected TransparentCoverIcon(final Context context) {
            super(context);
        }
        
        @Override
        protected void attachView(final ViewGroup viewGroup) {
        }
        
        @Override
        protected View getIconView() {
            return null;
        }
        
        @Override
        public void onFocusChanged(final boolean b) {
        }
        
        @Override
        public void setUiOrientation(final int n) {
        }
        
        @Override
        public void show() {
        }
    }
    
    protected static final class ZoomIcon extends SideTouchUiIcon
    {
        private int mOrientation;
        private FrameLayout mZoomBarLayout;
        private final ViewFinderImpl.ZoomBarUpdateProxy mZoomBarUpdateProxy;
        private Zoombar mZoombar;
        
        public ZoomIcon(final Context context, final ViewFinderImpl.ZoomBarUpdateProxy mZoomBarUpdateProxy) {
            super(context);
            this.mOrientation = 0;
            this.mZoomBarUpdateProxy = mZoomBarUpdateProxy;
        }
        
        @Override
        protected void attachView(final ViewGroup viewGroup) {
            View.inflate(this.mContext, 2131493030, viewGroup);
            this.mZoomBarLayout = (FrameLayout)viewGroup.findViewById(2131296701);
            this.mZoombar = (Zoombar)this.mZoomBarLayout.findViewById(2131296700);
            this.mZoomBarUpdateProxy.bindZoomBar(this.mZoombar);
        }
        
        @Override
        public void detach(final ViewGroup viewGroup) {
            this.mZoombar.setZoombarDisplayChangedListener((Zoombar.ZoombarDisplayChangedListener)new Zoombar.ZoombarDisplayChangedListener(this, viewGroup) {
                final ZoomIcon this$0;
                final ViewGroup val$from;
                
                @Override
                public void onShowZoombar() {
                }
                
                @Override
                public void onZoombarHidden() {
                    this.this$0.detach(this.val$from);
                }
            });
            this.mZoombar.hideDelayed();
        }
        
        @Override
        protected View getIconView() {
            return (View)this.mZoomBarLayout;
        }
        
        @Override
        public void setUiOrientation(final int n) {
            if (this.mOrientation == 0) {
                this.mZoombar.setSensorOrientation(n);
                this.mOrientation = n;
            }
        }
        
        @Override
        public void show() {
            this.mZoombar.show();
        }
    }
}
