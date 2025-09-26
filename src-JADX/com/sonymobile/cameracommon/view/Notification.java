package com.sonymobile.cameracommon.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.device.CameraParameterConverter;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.utility.RotationUtil;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class Notification extends RelativeLayout {
    public static final String TAG = "Notification";
    private boolean mAnimating;
    private ImageView mConditionIcon;
    private TextView mConditionText;
    private CameraParameterConverter.SceneMode mScene;
    private ImageView mSceneIcon;
    private TextView mSceneText;
    private SceneTextAnimation mSceneTextAnimation;
    private int mSensorOrientation;

    interface SceneTextAnimation {
        void cancel();

        void create();

        void release();

        void start();
    }

    public Notification(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSensorOrientation = 2;
        this.mAnimating = false;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        if (CamLog.VERBOSE) {
            CamLog.d("onFinishInflate: " + getVisibility());
        }
        super.onFinishInflate();
        this.mSceneIcon = (ImageView) findViewById(R.id.scene_icon);
        this.mSceneText = (TextView) findViewById(R.id.scene_text);
        this.mConditionIcon = (ImageView) findViewById(R.id.condition_icon);
        this.mConditionText = (TextView) findViewById(R.id.condition_text);
        createSceneTextAnimation();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseSceneTextAnimation();
    }

    public void onMacroStatusChanged(boolean z, CameraParameterConverter.SceneMode sceneMode) {
        if (CamLog.VERBOSE) {
            CamLog.d("Macro: " + getVisibility());
        }
        if (z) {
            findViewById(R.id.recognised_scene).setVisibility(0);
            findViewById(R.id.recognised_condition).setVisibility(0);
            this.mSceneIcon.setImageResource(R.drawable.cam_scene_recog_macro_icn);
            this.mSceneText.setText(R.string.cam_strings_focus_mode_macro_txt);
            if (this.mSensorOrientation == 2) {
                startSceneTextAnimation();
            }
        } else if (sceneMode == CameraParameterConverter.SceneMode.AUTO) {
            findViewById(R.id.recognised_scene).setVisibility(4);
        }
        invalidate();
    }

    public void onRecognisedSceneChanged(RecognizedScene recognizedScene) {
        if (CamLog.VERBOSE) {
            CamLog.d("onRecognisedSceneChanged: scene: " + recognizedScene);
        }
        this.mScene = CameraParameterConverter.SceneMode.AUTO;
        int iconId = recognizedScene.getIconId();
        int textId = recognizedScene.getTextId();
        if (iconId > 0 && textId > 0) {
            findViewById(R.id.recognised_scene).setVisibility(0);
            this.mSceneIcon.setImageResource(iconId);
            this.mSceneText.setText(textId);
            if (this.mSensorOrientation == 2) {
                startSceneTextAnimation();
            } else {
                this.mScene = recognizedScene.getSceneMode();
            }
        } else {
            findViewById(R.id.recognised_scene).setVisibility(4);
        }
        invalidate();
    }

    public void onRecognisedConditionChanged(RecognizedCondition recognizedCondition) {
        if (CamLog.VERBOSE) {
            CamLog.d("onRecognisedConditionChanged: condition: " + recognizedCondition + ", visibility: " + findViewById(R.id.recognised_condition).getVisibility());
        }
        int iconId = recognizedCondition.getIconId();
        int textId = recognizedCondition.getTextId();
        findViewById(R.id.recognised_condition).setVisibility(0);
        if (iconId > 0) {
            this.mConditionIcon.setVisibility(0);
            this.mConditionIcon.setImageResource(iconId);
        } else {
            this.mConditionIcon.setVisibility(4);
        }
        if (textId > 0) {
            this.mConditionText.setVisibility(0);
            this.mConditionText.setText(textId);
            if (this.mSensorOrientation == 2) {
                startSceneTextAnimation();
            }
        } else {
            this.mConditionText.setVisibility(4);
        }
        invalidate();
    }

    public void updateLayout() {
        if (CamLog.VERBOSE) {
            CamLog.d("updateLayout: entry: visibility: " + getVisibility());
        }
        requestLayout();
        invalidate();
    }

    public void onModeChanged(int i, boolean z) {
        if (CamLog.VERBOSE) {
            CamLog.d("onModeChanged: " + i);
        }
        if (z) {
            findViewById(R.id.scene_indicator).setVisibility(4);
            findViewById(R.id.condition_indicator).setVisibility(4);
        } else if (i == 1) {
            findViewById(R.id.scene_indicator).setVisibility(0);
            findViewById(R.id.condition_indicator).setVisibility(0);
        } else {
            this.mSensorOrientation = 2;
            findViewById(R.id.scene_indicator).setVisibility(4);
            findViewById(R.id.condition_indicator).setVisibility(4);
        }
    }

    protected void setAnimationStatus(boolean z) {
        if (CamLog.VERBOSE) {
            CamLog.d("setAnimationStatus: from " + this.mAnimating + " to " + z);
        }
        if (this.mAnimating == z) {
            return;
        }
        this.mAnimating = z;
    }

    protected void createSceneTextAnimation() {
        this.mSceneTextAnimation = new NoFadeoutAnimtion();
        this.mSceneTextAnimation.create();
    }

    protected void releaseSceneTextAnimation() {
        this.mSceneTextAnimation.release();
    }

    protected void startSceneTextAnimation() {
        if (CamLog.VERBOSE) {
            CamLog.d("startSceneTextAnimation: " + this.mAnimating);
        }
        this.mSceneTextAnimation.start();
    }

    protected void cancelSceneTextAnimation() {
        if (CamLog.VERBOSE) {
            CamLog.d("cancelSceneTextAnimation: " + this.mAnimating);
        }
        this.mSceneTextAnimation.cancel();
    }

    class NoFadeoutAnimtion implements SceneTextAnimation {
        private Runnable mSceneTextRunnable;

        NoFadeoutAnimtion() {
        }

        @Override // com.sonymobile.cameracommon.view.Notification.SceneTextAnimation
        public void create() {
            this.mSceneTextRunnable = new Runnable() { // from class: com.sonymobile.cameracommon.view.Notification.NoFadeoutAnimtion.1
                @Override // java.lang.Runnable
                public void run() {
                    Notification.this.mSceneText.setVisibility(4);
                    Notification.this.mConditionText.setVisibility(4);
                    Notification.this.setAnimationStatus(false);
                }
            };
        }

        @Override // com.sonymobile.cameracommon.view.Notification.SceneTextAnimation
        public void release() {
            Notification.this.removeCallbacks(this.mSceneTextRunnable);
        }

        @Override // com.sonymobile.cameracommon.view.Notification.SceneTextAnimation
        public void start() {
            if (Notification.this.mAnimating) {
                cancel();
            }
            Notification.this.postDelayed(this.mSceneTextRunnable, Notification.this.getResources().getInteger(R.integer.scene_fade_out_delay) + Notification.this.getResources().getInteger(R.integer.scene_fade_out_duration));
            Notification.this.setAnimationStatus(true);
            Notification.this.mSceneText.setVisibility(0);
            Notification.this.mConditionText.setVisibility(0);
        }

        @Override // com.sonymobile.cameracommon.view.Notification.SceneTextAnimation
        public void cancel() {
            Notification.this.removeCallbacks(this.mSceneTextRunnable);
        }
    }

    public void setSensorOrientation(int i) {
        this.mSensorOrientation = i;
        float angle = RotationUtil.getAngle(i);
        this.mSceneIcon.setRotation(angle);
        setOrientationSceneText();
        this.mConditionIcon.setRotation(angle);
        setOrientationConditionText();
    }

    private void setOrientationSceneText() {
        if (this.mSensorOrientation == 2) {
            if (this.mScene != CameraParameterConverter.SceneMode.AUTO) {
                this.mSceneText.setVisibility(0);
                startSceneTextAnimation();
                this.mScene = CameraParameterConverter.SceneMode.AUTO;
                return;
            }
            return;
        }
        cancelSceneTextAnimation();
        this.mSceneText.setVisibility(4);
    }

    private void setOrientationConditionText() {
        if (this.mSensorOrientation == 2) {
            this.mConditionText.setVisibility(0);
            startSceneTextAnimation();
        } else {
            cancelSceneTextAnimation();
            this.mConditionText.setVisibility(4);
        }
    }
}
