// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.sidetouchgesturedetector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import android.view.Display;
import android.util.Log;
import android.util.DisplayMetrics;
import android.graphics.Point;
import android.provider.Settings$System;
import android.view.WindowManager;
import android.database.ContentObserver;
import android.view.MotionEvent;
import android.os.Handler;
import android.view.MotionEvent$PointerCoords;
import android.content.Context;
import android.net.Uri;

public class DynamicAreaFilter
{
    private static final float DEFAULT_HEIGHT_PX = 864.0f;
    private static final int DEFAULT_VALUE_INT_DYNAMIC_FILTER_LIMIT_SCALE = 70;
    private static final int DEFAULT_VALUE_INT_DYNAMIC_FILTER_TOP_LIMIT_SCALE = 10;
    private static final int DEFAULT_VALUE_INT_SMOOTH_FACTOR = 90;
    private static final float DEFAULT_Y_DPI = 537.882f;
    private static final Uri DYNAMIC_FILTER_LIMIT_SCALE_URI;
    private static final Uri DYNAMIC_FILTER_TOP_LIMIT_SCALE_URI;
    private static final Uri DYNAMIC_FILTER_TYPE_URI;
    private static final int HEIGHT_MARGIN_TOP = 400;
    private static final int INT_DYNAMIC_FILTER_TYPE_HIGH = 2;
    private static final int INT_DYNAMIC_FILTER_TYPE_LOW = 0;
    private static final int INT_DYNAMIC_FILTER_TYPE_MID = 1;
    public static final String KEY_INT_DYNAMIC_FILTER_LIMIT_SCALE = "somc.side_sense_dynamic_filer_limit_scale";
    public static final String KEY_INT_DYNAMIC_FILTER_TOP_LIMIT_SCALE = "somc.side_sense_dynamic_filer_top_limit_scale";
    private static final String KEY_INT_DYNAMIC_FILTER_TYPE = "somc.side_sense_dynamic_filter_type";
    private static final String KEY_INT_SMOOTH_FACTOR = "somc.side_sense_smooth_factor";
    private static final int POLLING_DURATION = 50;
    private static final float SMOOTH_FACTOR = 0.95f;
    private static final float SMOOTH_FACTOR_NOW = 0.050000012f;
    private static final Uri SMOOTH_FACTOR_TYPE_URI;
    private static final String TAG = "DynamicAreaFilter";
    private static float sSmoothFactor = 0.0f;
    private static float sSmoothFactorNow = 0.0f;
    private static float sSuperSmoothFactor = 0.995f;
    private static float sSuperSmoothFactorNow;
    private Context mContext;
    private MotionEvent$PointerCoords mCoords;
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
    private Runnable mPollingRunnable;
    private int mRightMinHeight;
    private int mRightMinHeightLPF;
    private int mRightMinHeightSuperLPF;
    private int mScreenHeight;
    private ContentObserver mSettingsObserver;
    private int mTopLimitScale;
    private int mValidScreenHeightMax;
    private int mValidScreenHeightMin;
    private WindowManager mWindowManager;
    
    static {
        DYNAMIC_FILTER_TOP_LIMIT_SCALE_URI = Settings$System.getUriFor("somc.side_sense_dynamic_filer_top_limit_scale");
        DYNAMIC_FILTER_LIMIT_SCALE_URI = Settings$System.getUriFor("somc.side_sense_dynamic_filer_limit_scale");
        DYNAMIC_FILTER_TYPE_URI = Settings$System.getUriFor("somc.side_sense_dynamic_filter_type");
        SMOOTH_FACTOR_TYPE_URI = Settings$System.getUriFor("somc.side_sense_smooth_factor");
        DynamicAreaFilter.sSuperSmoothFactorNow = 1.0f - DynamicAreaFilter.sSuperSmoothFactor;
    }
    
    public DynamicAreaFilter(final Context context) {
        this.mCoords = new MotionEvent$PointerCoords();
        this.mPollingRunnable = new Runnable() {
            final DynamicAreaFilter this$0;
            
            @Override
            public void run() {
                this.this$0.calculateValidHeight(this.this$0.mMotionEvent);
            }
        };
        this.init(context);
    }
    
    public DynamicAreaFilter(final Context context, final OnDynamicAreaListener mOnDynamicAreaListener) {
        this.mCoords = new MotionEvent$PointerCoords();
        this.mPollingRunnable = new Runnable() {
            final DynamicAreaFilter this$0;
            
            @Override
            public void run() {
                this.this$0.calculateValidHeight(this.this$0.mMotionEvent);
            }
        };
        this.mOnDynamicAreaListener = mOnDynamicAreaListener;
        this.init(context);
    }
    
    private void calculateValidHeight(final MotionEvent motionEvent) {
        if (this.mCurDown) {
            for (int pointerCount = motionEvent.getPointerCount(), i = 0; i < pointerCount; ++i) {
                motionEvent.getPointerCoords(i, this.mCoords);
                if (this.mCoords.x == 0.0f) {
                    if (this.mCoords.y < this.mLeftMinHeight && this.mCoords.y < this.mValidScreenHeightMax) {
                        this.mLeftMinHeight = (int)this.mCoords.y;
                    }
                }
                else if (this.mCoords.y < this.mRightMinHeight && this.mCoords.y < this.mValidScreenHeightMax) {
                    this.mRightMinHeight = (int)this.mCoords.y;
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
                int n;
                if (this.mLeftMinHeightSuperLPF < this.mRightMinHeightSuperLPF) {
                    n = this.mLeftMinHeightSuperLPF;
                }
                else {
                    n = this.mRightMinHeightSuperLPF;
                }
                int mLimitScale = (int)((n + 400.0f + 100.0f) * 100.0f / this.mScreenHeight);
                if (this.mLimitScale <= mLimitScale) {
                    mLimitScale = this.mLimitScale;
                }
                this.mDynamicLimitScale = mLimitScale;
                if (this.mOnDynamicAreaListener != null) {
                    this.mOnDynamicAreaListener.onDynamicLimitScaleChanged(this.mDynamicLimitScale);
                }
                int n2;
                if (this.mLeftMinHeightLPF < this.mRightMinHeightLPF) {
                    n2 = this.mLeftMinHeightLPF;
                }
                else {
                    n2 = this.mRightMinHeightLPF;
                }
                final int n3 = (int)(100.0f * (n2 + 400.0f) / this.mScreenHeight);
                int mDynamicScale;
                if (this.isDynamicLimitUsed()) {
                    mDynamicScale = this.mDynamicLimitScale;
                }
                else {
                    mDynamicScale = this.mLimitScale;
                }
                if (mDynamicScale > n3) {
                    mDynamicScale = n3;
                }
                this.mDynamicScale = mDynamicScale;
                if (this.mOnDynamicAreaListener != null) {
                    this.mOnDynamicAreaListener.onDynamicScaleChanged(this.mDynamicScale);
                }
            }
            this.startPolling();
        }
    }
    
    private int changeLimitScaleForDevice() {
        return Math.round(100.0f - (int)(864.0f * this.mDeviceYDpi / 537.882f) * 100.0f / this.mScreenHeight);
    }
    
    private int getDynamicFilterLimitScale() {
        int n;
        if (70 == (n = Settings$System.getInt(this.mContext.getContentResolver(), "somc.side_sense_dynamic_filer_limit_scale", 70))) {
            n = this.changeLimitScaleForDevice();
        }
        return n;
    }
    
    private int getDynamicFilterTopLimitScale() {
        return Settings$System.getInt(this.mContext.getContentResolver(), "somc.side_sense_dynamic_filer_top_limit_scale", 10);
    }
    
    private int getDynamicFilterType() {
        return Settings$System.getInt(this.mContext.getContentResolver(), "somc.side_sense_dynamic_filter_type", 2);
    }
    
    private static int getHeightLPF(final int n, final int n2) {
        return (int)(n * DynamicAreaFilter.sSmoothFactor + n2 * DynamicAreaFilter.sSmoothFactorNow);
    }
    
    private static int getHeightSuperLPF(final int n, final int n2) {
        return (int)(n * DynamicAreaFilter.sSuperSmoothFactor + n2 * DynamicAreaFilter.sSuperSmoothFactorNow);
    }
    
    private int getSmoothFactor() {
        return Settings$System.getInt(this.mContext.getContentResolver(), "somc.side_sense_smooth_factor", 90);
    }
    
    private int getValidScreenHeightMax(final int n) {
        return (int)(this.mScreenHeight * (n / 100.0f));
    }
    
    private int getValidScreenHeightMin(final int n) {
        return (int)(this.mScreenHeight * (n / 100.0f));
    }
    
    private void init(final Context mContext) {
        this.mContext = mContext;
        this.mHandler = new Handler();
        this.mWindowManager = (WindowManager)mContext.getSystemService("window");
        final Display defaultDisplay = this.mWindowManager.getDefaultDisplay();
        final Point point = new Point(0, 0);
        defaultDisplay.getRealSize(point);
        int mScreenHeight;
        if (point.y > point.x) {
            mScreenHeight = point.y;
        }
        else {
            mScreenHeight = point.x;
        }
        this.mScreenHeight = mScreenHeight;
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        this.mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        this.mDeviceYDpi = displayMetrics.ydpi;
        this.mTopLimitScale = this.getDynamicFilterTopLimitScale();
        this.mValidScreenHeightMin = this.getValidScreenHeightMin(this.mTopLimitScale);
        this.mLimitScale = this.getDynamicFilterLimitScale();
        this.mValidScreenHeightMax = this.getValidScreenHeightMax(this.mLimitScale);
        this.mDynamicFilterType = this.getDynamicFilterType();
        this.registerSettingsObserver();
        this.setSmoothFactor();
        final String tag = DynamicAreaFilter.TAG;
        final StringBuilder sb = new StringBuilder();
        sb.append(".init sSmoothFactor=");
        sb.append(DynamicAreaFilter.sSmoothFactor);
        sb.append(" screenSize.x=");
        sb.append(point.x);
        sb.append(" screenSize.y=");
        sb.append(point.y);
        sb.append(" mScreenHeight=");
        sb.append(this.mScreenHeight);
        sb.append(" mLimitScale=");
        sb.append(this.mLimitScale);
        sb.append(" mDynamicFilterType=");
        sb.append(this.mDynamicFilterType);
        sb.append(" mValidScreenHeightMin=");
        sb.append(this.mValidScreenHeightMin);
        sb.append(" mValidScreenHeightMax=");
        sb.append(this.mValidScreenHeightMax);
        Log.i(tag, sb.toString());
        this.mLeftMinHeightLPF = this.mValidScreenHeightMax;
        this.mRightMinHeightLPF = this.mValidScreenHeightMax;
        this.mLeftMinHeightSuperLPF = this.mValidScreenHeightMax;
        this.mRightMinHeightSuperLPF = this.mValidScreenHeightMax;
    }
    
    private boolean isDynamicLimitUsed() {
        final int mDynamicFilterType = this.mDynamicFilterType;
        boolean b = true;
        if (mDynamicFilterType != 1) {
            b = (this.mDynamicFilterType == 2 && b);
        }
        return b;
    }
    
    private boolean isFingerSideDisabled() {
        return this.mDynamicFilterType == 2;
    }
    
    private boolean isPortrait() {
        final int rotation = this.mWindowManager.getDefaultDisplay().getRotation();
        return rotation == 0 || rotation == 2;
    }
    
    private void registerSettingsObserver() {
        if (this.mSettingsObserver == null) {
            this.mSettingsObserver = new ContentObserver(this, new Handler()) {
                final DynamicAreaFilter this$0;
                
                public void onChange(final boolean b, final Uri uri) {
                    if (DynamicAreaFilter.DYNAMIC_FILTER_LIMIT_SCALE_URI.equals((Object)uri)) {
                        this.this$0.mLimitScale = this.this$0.getDynamicFilterLimitScale();
                        this.this$0.mValidScreenHeightMax = this.this$0.getValidScreenHeightMax(this.this$0.mLimitScale);
                    }
                    else if (DynamicAreaFilter.DYNAMIC_FILTER_TOP_LIMIT_SCALE_URI.equals((Object)uri)) {
                        this.this$0.mTopLimitScale = this.this$0.getDynamicFilterTopLimitScale();
                        this.this$0.mValidScreenHeightMin = this.this$0.getValidScreenHeightMin(this.this$0.mTopLimitScale);
                        if (this.this$0.mOnDynamicAreaListener != null) {
                            this.this$0.mOnDynamicAreaListener.onTopLimitScaleChanged(this.this$0.mTopLimitScale);
                        }
                    }
                    else if (DynamicAreaFilter.DYNAMIC_FILTER_TYPE_URI.equals((Object)uri)) {
                        this.this$0.mDynamicFilterType = this.this$0.getDynamicFilterType();
                    }
                    else if (DynamicAreaFilter.SMOOTH_FACTOR_TYPE_URI.equals((Object)uri)) {
                        this.this$0.setSmoothFactor();
                    }
                }
            };
            this.mContext.getContentResolver().registerContentObserver(DynamicAreaFilter.DYNAMIC_FILTER_LIMIT_SCALE_URI, false, this.mSettingsObserver);
            this.mContext.getContentResolver().registerContentObserver(DynamicAreaFilter.DYNAMIC_FILTER_TOP_LIMIT_SCALE_URI, false, this.mSettingsObserver);
            this.mContext.getContentResolver().registerContentObserver(DynamicAreaFilter.DYNAMIC_FILTER_TYPE_URI, false, this.mSettingsObserver);
            this.mContext.getContentResolver().registerContentObserver(DynamicAreaFilter.SMOOTH_FACTOR_TYPE_URI, false, this.mSettingsObserver);
        }
    }
    
    private void setSmoothFactor() {
        DynamicAreaFilter.sSmoothFactor = this.getSmoothFactor() / 100.0f;
        if (0.99f < DynamicAreaFilter.sSmoothFactor) {
            DynamicAreaFilter.sSmoothFactor = 0.99f;
        }
        DynamicAreaFilter.sSmoothFactorNow = 1.0f - DynamicAreaFilter.sSmoothFactor;
    }
    
    private void startPolling() {
        if (!hasCallbacks(this.mHandler, this.mPollingRunnable)) {
            this.mHandler.postDelayed(this.mPollingRunnable, 50L);
        }
    }
    
    private void stopPolling() {
        this.mHandler.removeCallbacksAndMessages((Object)null);
    }
    
    public int getValidScreenHeight(final MotionEvent motionEvent) {
        if (this.isFingerSideDisabled()) {
            if (motionEvent.getX(motionEvent.getActionIndex()) == 0.0f) {
                if (this.mLeftMinHeightLPF > this.mRightMinHeightLPF) {
                    return 0;
                }
            }
            else if (this.mLeftMinHeightLPF < this.mRightMinHeightLPF) {
                return 0;
            }
        }
        if (motionEvent.getY(motionEvent.getActionIndex()) < this.mValidScreenHeightMin) {
            return 0;
        }
        return (int)(this.mScreenHeight * (this.mDynamicScale / 100.0f));
    }
    
    public void onSideTouchEvent(final MotionEvent mMotionEvent) {
        this.mMotionEvent = mMotionEvent;
        final int action = mMotionEvent.getAction();
        if (action == 0) {
            this.mCurDown = true;
            this.mLeftMinHeight = this.mValidScreenHeightMax;
            this.mRightMinHeight = this.mValidScreenHeightMax;
        }
        if (this.isPortrait()) {
            this.startPolling();
        }
        else {
            this.stopPolling();
        }
        if (action == 1 || action == 3) {
            this.mCurDown = false;
            this.mHandler.removeCallbacksAndMessages((Object)null);
        }
    }
    
    public void unregisterSettingsObserver() {
        if (this.mSettingsObserver != null) {
            this.mContext.getContentResolver().unregisterContentObserver(this.mSettingsObserver);
            this.mSettingsObserver = null;
        }
        this.stopPolling();
    }
    
    private static class HandlerHelper
    {
        private static final String METHOD_HAS_CALLBACKS = "hasCallbacks";
        private static Method sMethod;
        
        private static boolean hasCallbacks(final Handler obj, final Runnable runnable) {
            try {
                if (HandlerHelper.sMethod == null) {
                    HandlerHelper.sMethod = Handler.class.getDeclaredMethod("hasCallbacks", Runnable.class);
                }
                return (boolean)HandlerHelper.sMethod.invoke(obj, runnable);
            }
            catch (final IllegalArgumentException | NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
                return false;
            }
        }
    }
    
    public interface OnDynamicAreaListener
    {
        void onDynamicLimitScaleChanged(final int p0);
        
        void onDynamicScaleChanged(final int p0);
        
        void onLeftMinHeightChanged(final int p0);
        
        void onRightMinHeightChanged(final int p0);
        
        void onTopLimitScaleChanged(final int p0);
    }
}
