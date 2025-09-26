package com.sonymobile.sidetouchgesturedetector;

import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Point;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class DynamicAreaFilter {
    private static final float DEFAULT_HEIGHT_PX = 864.0f;
    private static final int DEFAULT_VALUE_INT_DYNAMIC_FILTER_LIMIT_SCALE = 70;
    private static final int DEFAULT_VALUE_INT_DYNAMIC_FILTER_TOP_LIMIT_SCALE = 10;
    private static final int DEFAULT_VALUE_INT_SMOOTH_FACTOR = 90;
    private static final float DEFAULT_Y_DPI = 537.882f;
    private static final int HEIGHT_MARGIN_TOP = 400;
    private static final int INT_DYNAMIC_FILTER_TYPE_HIGH = 2;
    private static final int INT_DYNAMIC_FILTER_TYPE_LOW = 0;
    private static final int INT_DYNAMIC_FILTER_TYPE_MID = 1;
    private static final int POLLING_DURATION = 50;
    private static final float SMOOTH_FACTOR = 0.95f;
    private static final float SMOOTH_FACTOR_NOW = 0.050000012f;
    private static final String TAG = "DynamicAreaFilter";
    private static float sSmoothFactor = 0.0f;
    private static float sSmoothFactorNow = 0.0f;
    private Context mContext;
    private boolean mCurDown;
    private float mDeviceYDpi;
    private int mDynamicFilterType;
    private int mDynamicLimitScale;
    private int mDynamicScale;
    private Handler mHandler;
    private int mLeftMinHeight;
    private int mLeftMinHeightLPF;
    private int mLeftMinHeightSuperLPF;
    private int mLimitScale;
    private MotionEvent mMotionEvent;
    private OnDynamicAreaListener mOnDynamicAreaListener;
    private int mRightMinHeight;
    private int mRightMinHeightLPF;
    private int mRightMinHeightSuperLPF;
    private int mScreenHeight;
    private ContentObserver mSettingsObserver;
    private int mTopLimitScale;
    private int mValidScreenHeightMax;
    private int mValidScreenHeightMin;
    private WindowManager mWindowManager;
    public static final String KEY_INT_DYNAMIC_FILTER_TOP_LIMIT_SCALE = "somc.side_sense_dynamic_filer_top_limit_scale";
    private static final Uri DYNAMIC_FILTER_TOP_LIMIT_SCALE_URI = Settings.System.getUriFor(KEY_INT_DYNAMIC_FILTER_TOP_LIMIT_SCALE);
    public static final String KEY_INT_DYNAMIC_FILTER_LIMIT_SCALE = "somc.side_sense_dynamic_filer_limit_scale";
    private static final Uri DYNAMIC_FILTER_LIMIT_SCALE_URI = Settings.System.getUriFor(KEY_INT_DYNAMIC_FILTER_LIMIT_SCALE);
    private static final String KEY_INT_DYNAMIC_FILTER_TYPE = "somc.side_sense_dynamic_filter_type";
    private static final Uri DYNAMIC_FILTER_TYPE_URI = Settings.System.getUriFor(KEY_INT_DYNAMIC_FILTER_TYPE);
    private static final String KEY_INT_SMOOTH_FACTOR = "somc.side_sense_smooth_factor";
    private static final Uri SMOOTH_FACTOR_TYPE_URI = Settings.System.getUriFor(KEY_INT_SMOOTH_FACTOR);
    private static float sSuperSmoothFactor = 0.995f;
    private static float sSuperSmoothFactorNow = 1.0f - sSuperSmoothFactor;
    private MotionEvent.PointerCoords mCoords = new MotionEvent.PointerCoords();
    private Runnable mPollingRunnable = new Runnable() { // from class: com.sonymobile.sidetouchgesturedetector.DynamicAreaFilter.1
        @Override // java.lang.Runnable
        public void run() {
            DynamicAreaFilter.this.calculateValidHeight(DynamicAreaFilter.this.mMotionEvent);
        }
    };

    public interface OnDynamicAreaListener {
        void onDynamicLimitScaleChanged(int i);

        void onDynamicScaleChanged(int i);

        void onLeftMinHeightChanged(int i);

        void onRightMinHeightChanged(int i);

        void onTopLimitScaleChanged(int i);
    }

    public DynamicAreaFilter(Context context) {
        init(context);
    }

    public DynamicAreaFilter(Context context, OnDynamicAreaListener onDynamicAreaListener) {
        this.mOnDynamicAreaListener = onDynamicAreaListener;
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mHandler = new Handler();
        this.mWindowManager = (WindowManager) context.getSystemService("window");
        Display defaultDisplay = this.mWindowManager.getDefaultDisplay();
        Point point = new Point(0, 0);
        defaultDisplay.getRealSize(point);
        this.mScreenHeight = point.y > point.x ? point.y : point.x;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        this.mDeviceYDpi = displayMetrics.ydpi;
        this.mTopLimitScale = getDynamicFilterTopLimitScale();
        this.mValidScreenHeightMin = getValidScreenHeightMin(this.mTopLimitScale);
        this.mLimitScale = getDynamicFilterLimitScale();
        this.mValidScreenHeightMax = getValidScreenHeightMax(this.mLimitScale);
        this.mDynamicFilterType = getDynamicFilterType();
        registerSettingsObserver();
        setSmoothFactor();
        Log.i(TAG, ".init sSmoothFactor=" + sSmoothFactor + " screenSize.x=" + point.x + " screenSize.y=" + point.y + " mScreenHeight=" + this.mScreenHeight + " mLimitScale=" + this.mLimitScale + " mDynamicFilterType=" + this.mDynamicFilterType + " mValidScreenHeightMin=" + this.mValidScreenHeightMin + " mValidScreenHeightMax=" + this.mValidScreenHeightMax);
        this.mLeftMinHeightLPF = this.mValidScreenHeightMax;
        this.mRightMinHeightLPF = this.mValidScreenHeightMax;
        this.mLeftMinHeightSuperLPF = this.mValidScreenHeightMax;
        this.mRightMinHeightSuperLPF = this.mValidScreenHeightMax;
    }

    public void onSideTouchEvent(MotionEvent motionEvent) {
        this.mMotionEvent = motionEvent;
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mCurDown = true;
            this.mLeftMinHeight = this.mValidScreenHeightMax;
            this.mRightMinHeight = this.mValidScreenHeightMax;
        }
        if (isPortrait()) {
            startPolling();
        } else {
            stopPolling();
        }
        if (action == 1 || action == 3) {
            this.mCurDown = false;
            this.mHandler.removeCallbacksAndMessages(null);
        }
    }

    private void startPolling() {
        if (HandlerHelper.hasCallbacks(this.mHandler, this.mPollingRunnable)) {
            return;
        }
        this.mHandler.postDelayed(this.mPollingRunnable, 50L);
    }

    private void stopPolling() {
        this.mHandler.removeCallbacksAndMessages(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void calculateValidHeight(MotionEvent motionEvent) {
        if (this.mCurDown) {
            int pointerCount = motionEvent.getPointerCount();
            for (int i = 0; i < pointerCount; i++) {
                motionEvent.getPointerCoords(i, this.mCoords);
                if (this.mCoords.x == 0.0f) {
                    if (this.mCoords.y < this.mLeftMinHeight && this.mCoords.y < this.mValidScreenHeightMax) {
                        this.mLeftMinHeight = (int) this.mCoords.y;
                    }
                } else if (this.mCoords.y < this.mRightMinHeight && this.mCoords.y < this.mValidScreenHeightMax) {
                    this.mRightMinHeight = (int) this.mCoords.y;
                }
                if (this.mLeftMinHeight != this.mScreenHeight) {
                    this.mLeftMinHeightLPF = getHeightLPF(this.mLeftMinHeightLPF, this.mLeftMinHeight);
                    this.mLeftMinHeightSuperLPF = getHeightSuperLPF(this.mLeftMinHeightSuperLPF, this.mLeftMinHeight);
                    if (this.mOnDynamicAreaListener != null) {
                        this.mOnDynamicAreaListener.onLeftMinHeightChanged(this.mLeftMinHeightLPF);
                    }
                }
                if (this.mRightMinHeight != this.mScreenHeight) {
                    this.mRightMinHeightLPF = getHeightLPF(this.mRightMinHeightLPF, this.mRightMinHeight);
                    this.mRightMinHeightSuperLPF = getHeightSuperLPF(this.mRightMinHeightSuperLPF, this.mRightMinHeight);
                    if (this.mOnDynamicAreaListener != null) {
                        this.mOnDynamicAreaListener.onRightMinHeightChanged(this.mRightMinHeightLPF);
                    }
                }
                int i2 = (int) (((((this.mLeftMinHeightSuperLPF < this.mRightMinHeightSuperLPF ? this.mLeftMinHeightSuperLPF : this.mRightMinHeightSuperLPF) + 400.0f) + 100.0f) * 100.0f) / this.mScreenHeight);
                if (this.mLimitScale <= i2) {
                    i2 = this.mLimitScale;
                }
                this.mDynamicLimitScale = i2;
                if (this.mOnDynamicAreaListener != null) {
                    this.mOnDynamicAreaListener.onDynamicLimitScaleChanged(this.mDynamicLimitScale);
                }
                int i3 = (int) ((100.0f * ((this.mLeftMinHeightLPF < this.mRightMinHeightLPF ? this.mLeftMinHeightLPF : this.mRightMinHeightLPF) + 400.0f)) / this.mScreenHeight);
                int i4 = isDynamicLimitUsed() ? this.mDynamicLimitScale : this.mLimitScale;
                if (i4 <= i3) {
                    i3 = i4;
                }
                this.mDynamicScale = i3;
                if (this.mOnDynamicAreaListener != null) {
                    this.mOnDynamicAreaListener.onDynamicScaleChanged(this.mDynamicScale);
                }
            }
            startPolling();
        }
    }

    private static int getHeightLPF(int i, int i2) {
        return (int) ((i * sSmoothFactor) + (i2 * sSmoothFactorNow));
    }

    private static int getHeightSuperLPF(int i, int i2) {
        return (int) ((i * sSuperSmoothFactor) + (i2 * sSuperSmoothFactorNow));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDynamicFilterLimitScale() {
        int i = Settings.System.getInt(this.mContext.getContentResolver(), KEY_INT_DYNAMIC_FILTER_LIMIT_SCALE, 70);
        return 70 == i ? changeLimitScaleForDevice() : i;
    }

    private int changeLimitScaleForDevice() {
        return Math.round(100.0f - ((((int) ((DEFAULT_HEIGHT_PX * this.mDeviceYDpi) / DEFAULT_Y_DPI)) * 100.0f) / this.mScreenHeight));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDynamicFilterTopLimitScale() {
        return Settings.System.getInt(this.mContext.getContentResolver(), KEY_INT_DYNAMIC_FILTER_TOP_LIMIT_SCALE, 10);
    }

    private int getSmoothFactor() {
        return Settings.System.getInt(this.mContext.getContentResolver(), KEY_INT_SMOOTH_FACTOR, 90);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSmoothFactor() {
        sSmoothFactor = getSmoothFactor() / 100.0f;
        if (0.99f < sSmoothFactor) {
            sSmoothFactor = 0.99f;
        }
        sSmoothFactorNow = 1.0f - sSmoothFactor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getDynamicFilterType() {
        return Settings.System.getInt(this.mContext.getContentResolver(), KEY_INT_DYNAMIC_FILTER_TYPE, 2);
    }

    private boolean isDynamicLimitUsed() {
        return this.mDynamicFilterType == 1 || this.mDynamicFilterType == 2;
    }

    private boolean isFingerSideDisabled() {
        return this.mDynamicFilterType == 2;
    }

    private void registerSettingsObserver() {
        if (this.mSettingsObserver == null) {
            this.mSettingsObserver = new ContentObserver(new Handler()) { // from class: com.sonymobile.sidetouchgesturedetector.DynamicAreaFilter.2
                @Override // android.database.ContentObserver
                public void onChange(boolean z, Uri uri) {
                    if (!DynamicAreaFilter.DYNAMIC_FILTER_LIMIT_SCALE_URI.equals(uri)) {
                        if (!DynamicAreaFilter.DYNAMIC_FILTER_TOP_LIMIT_SCALE_URI.equals(uri)) {
                            if (!DynamicAreaFilter.DYNAMIC_FILTER_TYPE_URI.equals(uri)) {
                                if (DynamicAreaFilter.SMOOTH_FACTOR_TYPE_URI.equals(uri)) {
                                    DynamicAreaFilter.this.setSmoothFactor();
                                    return;
                                }
                                return;
                            } else {
                                DynamicAreaFilter.this.mDynamicFilterType = DynamicAreaFilter.this.getDynamicFilterType();
                                return;
                            }
                        }
                        DynamicAreaFilter.this.mTopLimitScale = DynamicAreaFilter.this.getDynamicFilterTopLimitScale();
                        DynamicAreaFilter.this.mValidScreenHeightMin = DynamicAreaFilter.this.getValidScreenHeightMin(DynamicAreaFilter.this.mTopLimitScale);
                        if (DynamicAreaFilter.this.mOnDynamicAreaListener != null) {
                            DynamicAreaFilter.this.mOnDynamicAreaListener.onTopLimitScaleChanged(DynamicAreaFilter.this.mTopLimitScale);
                            return;
                        }
                        return;
                    }
                    DynamicAreaFilter.this.mLimitScale = DynamicAreaFilter.this.getDynamicFilterLimitScale();
                    DynamicAreaFilter.this.mValidScreenHeightMax = DynamicAreaFilter.this.getValidScreenHeightMax(DynamicAreaFilter.this.mLimitScale);
                }
            };
            this.mContext.getContentResolver().registerContentObserver(DYNAMIC_FILTER_LIMIT_SCALE_URI, false, this.mSettingsObserver);
            this.mContext.getContentResolver().registerContentObserver(DYNAMIC_FILTER_TOP_LIMIT_SCALE_URI, false, this.mSettingsObserver);
            this.mContext.getContentResolver().registerContentObserver(DYNAMIC_FILTER_TYPE_URI, false, this.mSettingsObserver);
            this.mContext.getContentResolver().registerContentObserver(SMOOTH_FACTOR_TYPE_URI, false, this.mSettingsObserver);
        }
    }

    public void unregisterSettingsObserver() {
        if (this.mSettingsObserver != null) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mSettingsObserver);
            this.mSettingsObserver = null;
        }
        stopPolling();
    }

    public int getValidScreenHeight(MotionEvent motionEvent) {
        if (isFingerSideDisabled()) {
            if (motionEvent.getX(motionEvent.getActionIndex()) == 0.0f) {
                if (this.mLeftMinHeightLPF > this.mRightMinHeightLPF) {
                    return 0;
                }
            } else if (this.mLeftMinHeightLPF < this.mRightMinHeightLPF) {
                return 0;
            }
        }
        if (motionEvent.getY(motionEvent.getActionIndex()) < this.mValidScreenHeightMin) {
            return 0;
        }
        return (int) (this.mScreenHeight * (this.mDynamicScale / 100.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getValidScreenHeightMax(int i) {
        return (int) (this.mScreenHeight * (i / 100.0f));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getValidScreenHeightMin(int i) {
        return (int) (this.mScreenHeight * (i / 100.0f));
    }

    private boolean isPortrait() {
        int rotation = this.mWindowManager.getDefaultDisplay().getRotation();
        return rotation == 0 || rotation == 2;
    }

    private static class HandlerHelper {
        private static final String METHOD_HAS_CALLBACKS = "hasCallbacks";
        private static Method sMethod;

        private HandlerHelper() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean hasCallbacks(Handler handler, Runnable runnable) {
            try {
                if (sMethod == null) {
                    sMethod = Handler.class.getDeclaredMethod(METHOD_HAS_CALLBACKS, Runnable.class);
                }
                return ((Boolean) sMethod.invoke(handler, runnable)).booleanValue();
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException unused) {
                return false;
            }
        }
    }
}
