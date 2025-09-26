// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.view;

import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonyericsson.android.camera.util.CamLog;
import android.util.AttributeSet;
import android.content.Context;
import com.sonyericsson.android.camera.device.CameraParameterConverter;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Notification extends RelativeLayout
{
    public static final String TAG = "Notification";
    private boolean mAnimating;
    private ImageView mConditionIcon;
    private TextView mConditionText;
    private CameraParameterConverter.SceneMode mScene;
    private ImageView mSceneIcon;
    private TextView mSceneText;
    private SceneTextAnimation mSceneTextAnimation;
    private int mSensorOrientation;
    
    public Notification(final Context context, final AttributeSet set) {
        super(context, set);
        this.mSensorOrientation = 2;
        this.mAnimating = false;
    }
    
    private void setOrientationConditionText() {
        if (this.mSensorOrientation == 2) {
            this.mConditionText.setVisibility(0);
            this.startSceneTextAnimation();
        }
        else {
            this.cancelSceneTextAnimation();
            this.mConditionText.setVisibility(4);
        }
    }
    
    private void setOrientationSceneText() {
        if (this.mSensorOrientation == 2) {
            if (this.mScene != CameraParameterConverter.SceneMode.AUTO) {
                this.mSceneText.setVisibility(0);
                this.startSceneTextAnimation();
                this.mScene = CameraParameterConverter.SceneMode.AUTO;
            }
        }
        else {
            this.cancelSceneTextAnimation();
            this.mSceneText.setVisibility(4);
        }
    }
    
    protected void cancelSceneTextAnimation() {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("cancelSceneTextAnimation: ");
            sb.append(this.mAnimating);
            CamLog.d(sb.toString());
        }
        this.mSceneTextAnimation.cancel();
    }
    
    protected void createSceneTextAnimation() {
        (this.mSceneTextAnimation = (SceneTextAnimation)new NoFadeoutAnimtion()).create();
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.releaseSceneTextAnimation();
    }
    
    protected void onFinishInflate() {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onFinishInflate: ");
            sb.append(this.getVisibility());
            CamLog.d(sb.toString());
        }
        super.onFinishInflate();
        this.mSceneIcon = (ImageView)this.findViewById(2131296548);
        this.mSceneText = (TextView)this.findViewById(2131296553);
        this.mConditionIcon = (ImageView)this.findViewById(2131296358);
        this.mConditionText = (TextView)this.findViewById(2131296360);
        this.createSceneTextAnimation();
    }
    
    public void onMacroStatusChanged(final boolean b, final CameraParameterConverter.SceneMode sceneMode) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Macro: ");
            sb.append(this.getVisibility());
            CamLog.d(sb.toString());
        }
        if (b) {
            this.findViewById(2131296519).setVisibility(0);
            this.findViewById(2131296518).setVisibility(0);
            this.mSceneIcon.setImageResource(2131231273);
            this.mSceneText.setText(2131689845);
            if (this.mSensorOrientation == 2) {
                this.startSceneTextAnimation();
            }
        }
        else if (sceneMode == CameraParameterConverter.SceneMode.AUTO) {
            this.findViewById(2131296519).setVisibility(4);
        }
        this.invalidate();
    }
    
    public void onModeChanged(final int i, final boolean b) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onModeChanged: ");
            sb.append(i);
            CamLog.d(sb.toString());
        }
        if (b) {
            this.findViewById(2131296549).setVisibility(4);
            this.findViewById(2131296359).setVisibility(4);
        }
        else if (i == 1) {
            this.findViewById(2131296549).setVisibility(0);
            this.findViewById(2131296359).setVisibility(0);
        }
        else {
            this.mSensorOrientation = 2;
            this.findViewById(2131296549).setVisibility(4);
            this.findViewById(2131296359).setVisibility(4);
        }
    }
    
    public void onRecognisedConditionChanged(final RecognizedCondition obj) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onRecognisedConditionChanged: condition: ");
            sb.append(obj);
            sb.append(", visibility: ");
            sb.append(this.findViewById(2131296518).getVisibility());
            CamLog.d(sb.toString());
        }
        final int iconId = obj.getIconId();
        final int textId = obj.getTextId();
        this.findViewById(2131296518).setVisibility(0);
        if (iconId > 0) {
            this.mConditionIcon.setVisibility(0);
            this.mConditionIcon.setImageResource(iconId);
        }
        else {
            this.mConditionIcon.setVisibility(4);
        }
        if (textId > 0) {
            this.mConditionText.setVisibility(0);
            this.mConditionText.setText(textId);
            if (this.mSensorOrientation == 2) {
                this.startSceneTextAnimation();
            }
        }
        else {
            this.mConditionText.setVisibility(4);
        }
        this.invalidate();
    }
    
    public void onRecognisedSceneChanged(final RecognizedScene obj) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onRecognisedSceneChanged: scene: ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        this.mScene = CameraParameterConverter.SceneMode.AUTO;
        final int iconId = obj.getIconId();
        final int textId = obj.getTextId();
        if (iconId > 0 && textId > 0) {
            this.findViewById(2131296519).setVisibility(0);
            this.mSceneIcon.setImageResource(iconId);
            this.mSceneText.setText(textId);
            if (this.mSensorOrientation == 2) {
                this.startSceneTextAnimation();
            }
            else {
                this.mScene = obj.getSceneMode();
            }
        }
        else {
            this.findViewById(2131296519).setVisibility(4);
        }
        this.invalidate();
    }
    
    protected void releaseSceneTextAnimation() {
        this.mSceneTextAnimation.release();
    }
    
    protected void setAnimationStatus(final boolean b) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setAnimationStatus: from ");
            sb.append(this.mAnimating);
            sb.append(" to ");
            sb.append(b);
            CamLog.d(sb.toString());
        }
        if (this.mAnimating == b) {
            return;
        }
        this.mAnimating = b;
    }
    
    public void setSensorOrientation(final int mSensorOrientation) {
        this.mSensorOrientation = mSensorOrientation;
        final float angle = RotationUtil.getAngle(mSensorOrientation);
        this.mSceneIcon.setRotation(angle);
        this.setOrientationSceneText();
        this.mConditionIcon.setRotation(angle);
        this.setOrientationConditionText();
    }
    
    protected void startSceneTextAnimation() {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("startSceneTextAnimation: ");
            sb.append(this.mAnimating);
            CamLog.d(sb.toString());
        }
        this.mSceneTextAnimation.start();
    }
    
    public void updateLayout() {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("updateLayout: entry: visibility: ");
            sb.append(this.getVisibility());
            CamLog.d(sb.toString());
        }
        this.requestLayout();
        this.invalidate();
    }
    
    class NoFadeoutAnimtion implements SceneTextAnimation
    {
        private Runnable mSceneTextRunnable;
        final Notification this$0;
        
        NoFadeoutAnimtion(final Notification this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void cancel() {
            this.this$0.removeCallbacks(this.mSceneTextRunnable);
        }
        
        @Override
        public void create() {
            this.mSceneTextRunnable = new Runnable(this) {
                final NoFadeoutAnimtion this$1;
                
                @Override
                public void run() {
                    this.this$1.this$0.mSceneText.setVisibility(4);
                    this.this$1.this$0.mConditionText.setVisibility(4);
                    this.this$1.this$0.setAnimationStatus(false);
                }
            };
        }
        
        @Override
        public void release() {
            this.this$0.removeCallbacks(this.mSceneTextRunnable);
        }
        
        @Override
        public void start() {
            if (this.this$0.mAnimating) {
                this.cancel();
            }
            this.this$0.postDelayed(this.mSceneTextRunnable, (long)(this.this$0.getResources().getInteger(2131361804) + this.this$0.getResources().getInteger(2131361805)));
            this.this$0.setAnimationStatus(true);
            this.this$0.mSceneText.setVisibility(0);
            this.this$0.mConditionText.setVisibility(0);
        }
    }
    
    interface SceneTextAnimation
    {
        void cancel();
        
        void create();
        
        void release();
        
        void start();
    }
}
