package com.sonyericsson.android.camera.view.baselayout.zoombar;

import android.animation.Animator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonymobile.cameracommon.research.ResearchUtil;
import java.util.List;
import java.util.Locale;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class Zoombar extends FrameLayout {
    private static final boolean DEBUG = false;
    public static final int DELAY_ZOOMBAR_HIDE = 1000;
    private static final long IMMEDIATE_ANIMATION_DURATION_IN_MILLIS = 0;
    private static final float INVISIBLE_ALPHA = 0.0f;
    private static final long INVISIBLE_ANIMATION_DURATION_IN_MILLIS = 100;
    public static final int MIN_VALUE = 0;
    public static final String TAG = "Zoombar";
    private static final float VISIBLE_ALPHA = 1.0f;
    private static final long VISIBLE_ANIMATION_DURATION_IN_MILLIS = 100;
    private Animator.AnimatorListener mHideAnimationlistener;
    private final Runnable mHideEvent;
    private ImageView mLeftIndicator;
    private ImageView mRightIndicator;
    private TextView mValueIndicator;
    private List<Integer> mZoomRatios;
    private ZoombarDisplayChangedListener mZoombarDisplayChangedListener;

    public interface ZoombarDisplayChangedListener {
        void onShowZoombar();

        void onZoombarHidden();
    }

    public void setZoombarDisplayChangedListener(ZoombarDisplayChangedListener zoombarDisplayChangedListener) {
        this.mZoombarDisplayChangedListener = zoombarDisplayChangedListener;
    }

    public Zoombar(Context context) {
        this(context, null);
    }

    public Zoombar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public Zoombar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mZoombarDisplayChangedListener = null;
        this.mHideAnimationlistener = new Animator.AnimatorListener() { // from class: com.sonyericsson.android.camera.view.baselayout.zoombar.Zoombar.1
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                if (Zoombar.this.mZoombarDisplayChangedListener != null) {
                    Zoombar.this.mZoombarDisplayChangedListener.onZoombarHidden();
                }
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                if (Zoombar.this.mZoombarDisplayChangedListener != null) {
                    Zoombar.this.mZoombarDisplayChangedListener.onZoombarHidden();
                }
            }
        };
        this.mHideEvent = new Runnable() { // from class: com.sonyericsson.android.camera.view.baselayout.zoombar.Zoombar.2
            @Override // java.lang.Runnable
            public void run() {
                Zoombar.this.hideWithAnimation(true);
            }
        };
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mLeftIndicator = (ImageView) findViewById(R.id.left_indicator);
        this.mRightIndicator = (ImageView) findViewById(R.id.right_indicator);
        this.mValueIndicator = (TextView) findViewById(R.id.value_indicator);
    }

    public void setZoomRatios(List<Integer> list) {
        this.mZoomRatios = list;
    }

    public List<Integer> getZoomRatios() {
        return this.mZoomRatios;
    }

    public int zoom(int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("zoom() current:" + i + " maxZoom:120 zoomRatios:" + this.mZoomRatios);
        }
        if (!validateZoomParameters(i)) {
            hideImmediately();
            return i;
        }
        if (i < 0) {
            i = 0;
        }
        if (i > 120) {
            i = 120;
        }
        Integer num = this.mZoomRatios.get(i);
        int dimension = (int) getResources().getDimension(R.dimen.max_zoom_indicator_width);
        int intrinsicWidth = this.mLeftIndicator.getDrawable().getIntrinsicWidth();
        int i2 = (((dimension - intrinsicWidth) * (120 - i)) / 120) + intrinsicWidth;
        String str = String.format(Locale.getDefault(), "%.1f", Float.valueOf(num.intValue() / 100.0f));
        this.mLeftIndicator.getLayoutParams().width = i2;
        this.mRightIndicator.getLayoutParams().width = i2;
        this.mValueIndicator.setText(str);
        this.mLeftIndicator.requestLayout();
        this.mRightIndicator.requestLayout();
        if (CamLog.VERBOSE) {
            CamLog.d("zoom() position:" + i2 + " srPosition:0");
        }
        ResearchUtil.getInstance().setZoomRatio(Float.parseFloat(String.format(Locale.US, "%.1f", Float.valueOf(num.intValue() / 100.0f))));
        return i;
    }

    public void setSensorOrientation(int i) {
        this.mValueIndicator.setRotation(RotationUtil.getAngle(i));
    }

    public void show() {
        Handler handler = getHandler();
        if (handler != null) {
            handler.removeCallbacks(this.mHideEvent);
        }
        showWithAnimation(true);
    }

    public void showImmediately() {
        Handler handler = getHandler();
        if (handler != null) {
            handler.removeCallbacks(this.mHideEvent);
        }
        showWithAnimation(false);
    }

    public void hideDelayed() {
        Handler handler = getHandler();
        if (handler != null) {
            handler.postDelayed(this.mHideEvent, 1000L);
        }
    }

    public void hideImmediately() {
        Handler handler = getHandler();
        if (handler != null) {
            handler.removeCallbacks(this.mHideEvent);
        }
        hideWithAnimation(false);
    }

    private void showWithAnimation(boolean z) {
        if (this.mZoombarDisplayChangedListener != null) {
            this.mZoombarDisplayChangedListener.onShowZoombar();
        }
        animate().setListener(null).alpha(VISIBLE_ALPHA).setDuration(z ? 100L : 0L).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideWithAnimation(boolean z) {
        animate().alpha(0.0f).setDuration(z ? 100L : 0L).setListener(this.mHideAnimationlistener).start();
    }

    private boolean validateZoomParameters(int i) {
        Integer num;
        return (this.mZoomRatios == null || (num = this.mZoomRatios.get(i)) == null || num.intValue() < 0) ? false : true;
    }
}
