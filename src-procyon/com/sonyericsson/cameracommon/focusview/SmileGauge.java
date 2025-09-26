// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.focusview;

import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import android.widget.ImageView;
import android.view.ViewGroup$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import com.sonyericsson.android.camera.util.CamLog;
import android.content.res.TypedArray;
import com.sonyericsson.android.camera.R;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.RelativeLayout;

public class SmileGauge extends RelativeLayout
{
    public static final int SMILE_LEVEL = 5;
    public static final int SMILE_MAX = 100;
    public static final int SMILE_MIN = 0;
    public static final String TAG = "SmileGauge";
    private boolean mIsForLandscape;
    private int mMargin;
    private int mSmileScore;
    
    public SmileGauge(final Context context) {
        this(context, null);
    }
    
    public SmileGauge(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public SmileGauge(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mIsForLandscape = true;
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.SmileGauge);
        this.mIsForLandscape = obtainStyledAttributes.getBoolean(0, true);
        obtainStyledAttributes.recycle();
    }
    
    protected void alignToDirection(final int i) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("align direction = ");
            sb.append(i);
            CamLog.d(sb.toString());
        }
        final RelativeLayout$LayoutParams layoutParams = (RelativeLayout$LayoutParams)this.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        layoutParams.addRule(i, 2131296527);
        this.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
    }
    
    protected void clearLayoutParams() {
        if (CamLog.VERBOSE) {
            CamLog.v("clearLayoutParams() is called.");
        }
        final RelativeLayout$LayoutParams layoutParams = (RelativeLayout$LayoutParams)this.getLayoutParams();
        if (layoutParams == null) {
            return;
        }
        layoutParams.addRule(6, 0);
        layoutParams.addRule(5, 0);
        layoutParams.addRule(7, 0);
        layoutParams.topMargin = 0;
        layoutParams.leftMargin = 0;
        this.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
    }
    
    protected void drawThreshold() {
        if (CamLog.VERBOSE) {
            CamLog.v("drawThreshold() is called.");
        }
        final ImageView imageView = (ImageView)this.findViewById(2131296617);
        final RelativeLayout$LayoutParams layoutParams = new RelativeLayout$LayoutParams(-2, -2);
        if (this.isForLandscape()) {
            layoutParams.topMargin = this.mMargin;
            layoutParams.addRule(9);
        }
        else {
            final RelativeLayout$LayoutParams layoutParams2 = new RelativeLayout$LayoutParams(-2, -2);
            final ImageView imageView2 = (ImageView)this.findViewById(2131296614);
            if (this.isLayoutOrientationLandscape()) {
                layoutParams2.addRule(10);
                layoutParams.leftMargin = this.mMargin;
                layoutParams.addRule(12);
            }
            else {
                layoutParams2.addRule(12);
                layoutParams.rightMargin = this.mMargin;
                layoutParams.addRule(11);
            }
            imageView2.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
        }
        imageView.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
    }
    
    public int getSmileScore() {
        return this.mSmileScore;
    }
    
    public boolean isForLandscape() {
        return this.mIsForLandscape;
    }
    
    public boolean isLayoutOrientationLandscape() {
        return LayoutOrientationResolver.getInstance().getOrientation() != LayoutOrientationResolver.LayoutOrientationType.PORTRAIT;
    }
    
    protected void moveToId(final int n) {
        if (this.getId() != n) {
            this.setVisibility(8);
        }
        else {
            this.setVisibility(0);
        }
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
    
    public void onFinishInflate() {
        if (CamLog.VERBOSE) {
            CamLog.v("onFinishInflate() is called.");
        }
        super.onFinishInflate();
        this.mSmileScore = 0;
    }
    
    public void setPosition(final int i, final int j, final int k, final int l, final int m) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setPosition( ");
            sb.append(i);
            sb.append(", ");
            sb.append(j);
            sb.append(", ");
            sb.append(k);
            sb.append(", ");
            sb.append(l);
            sb.append(", ");
            sb.append(m);
            sb.append(" )");
            CamLog.d(sb.toString());
        }
        if (i != k && j != l) {
            this.update(m);
        }
        else {
            this.setVisibility(4);
        }
    }
    
    public void setSmileLevel(final int n) {
        this.mMargin = this.getResources().getDimensionPixelSize(n);
    }
    
    public void setSmileScore(final int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("setSmileScore() is called.");
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setSmileScore: ");
            sb.append(i);
            CamLog.d(sb.toString());
        }
        int n;
        if (i < 0) {
            n = 0;
        }
        else if ((n = i) > 100) {
            n = 100;
        }
        this.mSmileScore = n;
        final SmileScore smileScore = (SmileScore)this.findViewById(2131296616);
        smileScore.setSmileScore(n);
        if (!this.isForLandscape()) {
            final RelativeLayout$LayoutParams layoutParams = (RelativeLayout$LayoutParams)smileScore.getLayoutParams();
            if (this.isLayoutOrientationLandscape()) {
                layoutParams.removeRule(12);
                layoutParams.addRule(10);
            }
            else {
                layoutParams.removeRule(10);
                layoutParams.addRule(12);
            }
            smileScore.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        }
        smileScore.invalidate();
        if (CamLog.VERBOSE) {
            CamLog.d("setSmileScore: invalidate");
        }
    }
    
    public void setVisibility(final int n) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setVisibility( ");
            sb.append(n);
            sb.append(" ): ");
            sb.append(this.getId());
            CamLog.d(sb.toString());
        }
        super.setVisibility(n);
        if (n == 0) {
            this.drawThreshold();
        }
    }
    
    protected void update(final int n) {
        if (CamLog.VERBOSE) {
            CamLog.d("update() orientation", RotationUtil.orientationToString(n));
        }
        if (this.isLayoutOrientationLandscape()) {
            if ((this.isForLandscape() && n != 2) || (!this.isForLandscape() && n != 1)) {
                this.clearLayoutParams();
                this.setVisibility(8);
                return;
            }
        }
        else if ((!this.isForLandscape() && n != 2) || (this.isForLandscape() && n != 1)) {
            this.clearLayoutParams();
            this.setVisibility(8);
            return;
        }
        this.clearLayoutParams();
        if (n == 2) {
            if (this.isLayoutOrientationLandscape()) {
                this.moveToId(2131296615);
                this.alignToDirection(6);
            }
            else {
                this.moveToId(2131296618);
                this.alignToDirection(7);
            }
        }
        else if (this.isLayoutOrientationLandscape()) {
            this.moveToId(2131296613);
            this.alignToDirection(5);
        }
        else {
            this.moveToId(2131296615);
            this.alignToDirection(6);
        }
        this.postInvalidate();
    }
}
