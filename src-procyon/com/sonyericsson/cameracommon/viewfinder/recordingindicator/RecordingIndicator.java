// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.viewfinder.recordingindicator;

import java.util.Locale;
import com.sonymobile.cameracommon.font.FontUtil;
import com.sonyericsson.cameracommon.contentsview.ThumbnailUtil;
import android.media.ThumbnailUtils;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory$Options;
import com.sonyericsson.cameracommon.utility.ResourceUtil;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import android.widget.LinearLayout$LayoutParams;
import android.view.ViewGroup$LayoutParams;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonymobile.cameracommon.extendedview.RoundRectImageView;
import android.graphics.Bitmap$Config;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RelativeLayout$LayoutParams;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import android.view.View;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class RecordingIndicator extends RelativeLayout implements RecordingTimeIndicator
{
    public static final String TAG = "RecordingIndicator";
    private LinearLayout mConstraintIndicator;
    private TextView mConstraintRecordingTimeText;
    private View mContainer;
    private int mDisplayOrientation;
    private int mDuration;
    private boolean mIsConstraint;
    private boolean mIsRecording;
    private boolean mIsSequence;
    private boolean mIsThumbnailReady;
    private TextView mMaxDurationText;
    private DurationParameterSet mMaxTime;
    private int mPivotForRotationConstraint;
    private int mPivotForRotationSequence;
    private int mPivotForRotationUnConstraint;
    private RecordingProgressBar mProgressBar;
    private final float mRadius;
    private DurationParameterSet mRecordingTime;
    private LayoutDependencyResolver.ScreenAspect mScreenAspect;
    private LinearLayout mSequenceIndicator;
    private TextView mSequenceRec;
    private TextView mSequenceRecordingTimeText;
    private String mStringFormatRecordingTime;
    private String mStringFormatRemainConstraintTime;
    private int mThumbnailCnt;
    private LinearLayout mThumbnailContainer;
    private final int mThumbnailMaxNum;
    private final int mThumbnailPadding;
    private final RelativeLayout$LayoutParams mThumbnailParams;
    private final int mThumbnailSize;
    private final Bitmap[] mThumbnails;
    private LinearLayout mTimeContainer;
    private LinearLayout mUnConstraintIndicator;
    private TextView mUnConstraintRecordingTimeText;
    
    public RecordingIndicator(final Context context, final AttributeSet set) {
        super(context, set);
        this.mUnConstraintIndicator = null;
        this.mUnConstraintRecordingTimeText = null;
        this.mConstraintIndicator = null;
        this.mConstraintRecordingTimeText = null;
        this.mMaxDurationText = null;
        this.mSequenceIndicator = null;
        this.mSequenceRecordingTimeText = null;
        this.mThumbnailContainer = null;
        this.mTimeContainer = null;
        this.mSequenceRec = null;
        this.mProgressBar = null;
        this.mThumbnailMaxNum = this.getResources().getInteger(2131361803);
        this.mThumbnailSize = this.getResources().getDimensionPixelSize(2131165525);
        this.mRadius = this.getResources().getDimension(2131165524);
        this.mThumbnailParams = new RelativeLayout$LayoutParams(-2, this.mThumbnailSize);
        this.mThumbnailPadding = this.getResources().getDimensionPixelSize(2131165523);
        this.mStringFormatRemainConstraintTime = null;
        this.mStringFormatRecordingTime = null;
        this.mIsConstraint = false;
        this.mIsSequence = false;
        this.mIsThumbnailReady = false;
        this.mIsRecording = false;
        this.mThumbnailCnt = 0;
        this.mThumbnails = new Bitmap[this.mThumbnailMaxNum];
        this.mDuration = 0;
        this.mMaxTime = null;
        this.mRecordingTime = null;
        this.mPivotForRotationUnConstraint = context.getResources().getDimensionPixelSize(2131165528) / 2;
        this.mPivotForRotationConstraint = context.getResources().getDimensionPixelSize(2131165510) / 2;
        this.mPivotForRotationSequence = context.getResources().getDimensionPixelSize(2131165518) / 2;
    }
    
    private void addEmptyThumbnails() {
        final Bitmap bitmap = Bitmap.createBitmap(this.mThumbnailSize, this.mThumbnailSize, Bitmap$Config.RGB_565);
        bitmap.eraseColor(-16777216);
        final RoundRectImageView roundRectImageView = this.createRoundRectImageView(bitmap);
        roundRectImageView.setRadius(this.mRadius, 0.0f, 0.0f, this.mRadius);
        this.mThumbnailContainer.addView((View)roundRectImageView);
    }
    
    private RoundRectImageView createRoundRectImageView(final Bitmap imageBitmap) {
        if (CamLog.VERBOSE) {
            CamLog.d("createRoundRectImageView");
        }
        final RoundRectImageView roundRectImageView = new RoundRectImageView(this.getContext());
        roundRectImageView.setLayoutParams((ViewGroup$LayoutParams)this.mThumbnailParams);
        roundRectImageView.setImageBitmap(imageBitmap);
        roundRectImageView.setClickable(false);
        roundRectImageView.setFocusable(false);
        roundRectImageView.setFocusableInTouchMode(false);
        return roundRectImageView;
    }
    
    private void resetThumbnails() {
        if (CamLog.VERBOSE) {
            CamLog.d("resetThumbnails");
        }
        this.mThumbnailCnt = 0;
        if (this.mThumbnailContainer != null) {
            this.mThumbnailContainer.removeAllViews();
        }
        this.addEmptyThumbnails();
    }
    
    private void setSequentialIndicator(final boolean b) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setSequentialIndicator:");
            sb.append(b);
            CamLog.d(sb.toString());
        }
        if (b) {
            if (!this.mIsThumbnailReady) {
                return;
            }
        }
        else {
            this.mIsThumbnailReady = false;
        }
        int backgroundResource;
        int visibility;
        if (b) {
            backgroundResource = 2131231302;
            visibility = 0;
        }
        else {
            backgroundResource = 2131231301;
            visibility = 8;
        }
        this.mTimeContainer.setBackgroundResource(backgroundResource);
        this.mTimeContainer.setPadding(this.getResources().getDimensionPixelSize(2131165521), 0, this.getResources().getDimensionPixelSize(2131165521), 0);
        final LinearLayout$LayoutParams layoutParams = (LinearLayout$LayoutParams)this.mTimeContainer.getLayoutParams();
        layoutParams.width = -2;
        this.mTimeContainer.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
        this.mSequenceRec.setVisibility(visibility);
    }
    
    private void setUnconstraintIndicator(final boolean b) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setUnconstraintIndicator:");
            sb.append(b);
            CamLog.d(sb.toString());
        }
        int backgroundResource;
        if (b) {
            backgroundResource = 2131231305;
        }
        else {
            backgroundResource = 2131231304;
        }
        this.mUnConstraintIndicator.setBackgroundResource(backgroundResource);
        this.mUnConstraintIndicator.setPadding(this.getResources().getDimensionPixelSize(2131165521), 0, this.getResources().getDimensionPixelSize(2131165521), 0);
    }
    
    private void updateLayout() {
        final float angle = RotationUtil.getAngle(this.mDisplayOrientation);
        final LayoutOrientationResolver.LayoutOrientationType orientation = LayoutOrientationResolver.getInstance().getOrientation();
        float rotation = angle;
        if (orientation == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            rotation = angle + 90.0f;
        }
        if (this.mScreenAspect != null && this.mScreenAspect == LayoutDependencyResolver.ScreenAspect.EIGHTEEN_NINE) {
            if (orientation == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
                this.mContainer.setPadding(ResourceUtil.getDimensionPixelSize(this.getContext(), this.getContext().getPackageName(), 2131165531), 0, 0, 0);
            }
            else {
                this.mContainer.setPadding(0, 0, 0, ResourceUtil.getDimensionPixelSize(this.getContext(), this.getContext().getPackageName(), 2131165531));
            }
        }
        if (this.mUnConstraintIndicator != null) {
            this.updateLayoutParams(this.mUnConstraintIndicator);
            this.mUnConstraintIndicator.setRotation(rotation);
            this.mUnConstraintIndicator.setPivotX((float)this.mPivotForRotationUnConstraint);
            this.mUnConstraintIndicator.setPivotY((float)this.mPivotForRotationUnConstraint);
        }
        if (this.mConstraintIndicator != null) {
            this.updateLayoutParams(this.mConstraintIndicator);
            this.mConstraintIndicator.setRotation(rotation);
            this.mConstraintIndicator.setPivotX((float)this.mPivotForRotationConstraint);
            this.mConstraintIndicator.setPivotY((float)this.mPivotForRotationConstraint);
        }
        if (this.mSequenceIndicator != null) {
            this.updateLayoutParams(this.mSequenceIndicator);
            this.mSequenceIndicator.setRotation(rotation);
            this.mSequenceIndicator.setPivotX((float)this.mPivotForRotationSequence);
            this.mSequenceIndicator.setPivotY((float)this.mPivotForRotationSequence);
        }
    }
    
    private void updateLayoutParams(final LinearLayout linearLayout) {
        final RelativeLayout$LayoutParams relativeLayout$LayoutParams = (RelativeLayout$LayoutParams)linearLayout.getLayoutParams();
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            relativeLayout$LayoutParams.removeRule(12);
            relativeLayout$LayoutParams.addRule(10, -1);
        }
        else {
            relativeLayout$LayoutParams.removeRule(10);
            relativeLayout$LayoutParams.addRule(12, -1);
        }
        linearLayout.requestLayout();
    }
    
    private void updateProgressbar(final int n) {
        if (n <= this.mDuration) {
            this.mProgressBar.setProgress(n, this.mDuration);
        }
    }
    
    private void updateThumbnails(final Bitmap bitmap) {
        this.mIsThumbnailReady = true;
        this.setIndicator(this.mIsRecording);
        final RoundRectImageView roundRectImageView = this.createRoundRectImageView(bitmap);
        if (this.mThumbnailCnt == 0) {
            this.mThumbnailContainer.removeAllViews();
            roundRectImageView.setRadius(this.mRadius, 0.0f, 0.0f, this.mRadius);
            this.mThumbnails[0] = bitmap;
        }
        else {
            roundRectImageView.setPadding(this.mThumbnailPadding, 0, 0, 0);
            if (this.mThumbnailCnt == 1) {
                this.mThumbnails[1] = bitmap;
            }
            else if (this.mThumbnailCnt >= this.mThumbnailMaxNum) {
                this.mThumbnailContainer.removeViewAt(0);
                final RoundRectImageView roundRectImageView2 = this.createRoundRectImageView(this.mThumbnails[1]);
                roundRectImageView2.setRadius(this.mRadius, 0.0f, 0.0f, this.mRadius);
                this.mThumbnailContainer.removeViewAt(0);
                this.mThumbnailContainer.addView((View)roundRectImageView2, 0);
                int n;
                for (int i = 0; i < this.mThumbnailMaxNum - 1; i = n) {
                    final Bitmap[] mThumbnails = this.mThumbnails;
                    final Bitmap[] mThumbnails2 = this.mThumbnails;
                    n = i + 1;
                    mThumbnails[i] = mThumbnails2[n];
                }
            }
            this.mThumbnails[this.mThumbnailMaxNum - 1] = bitmap;
        }
        this.mThumbnailContainer.addView((View)roundRectImageView);
        ++this.mThumbnailCnt;
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("updateThumbnails: thumnailCnt=");
            sb.append(this.mThumbnailCnt);
            CamLog.d(sb.toString());
        }
    }
    
    public void addChapter(final byte[] array, final int i) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("addChapter: orientation=");
            sb.append(i);
            CamLog.d(sb.toString());
        }
        if (!this.mIsSequence) {
            return;
        }
        final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
        bitmapFactory$Options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(array, 0, array.length, bitmapFactory$Options);
        final int outHeight = bitmapFactory$Options.outHeight;
        final int outWidth = bitmapFactory$Options.outWidth;
        if (outHeight < outWidth) {
            bitmapFactory$Options.inSampleSize = Math.round(outHeight / (float)this.mThumbnailSize);
        }
        else if (outWidth < outHeight) {
            bitmapFactory$Options.inSampleSize = Math.round(outWidth / (float)this.mThumbnailSize);
        }
        bitmapFactory$Options.inJustDecodeBounds = false;
        bitmapFactory$Options.inPreferredConfig = Bitmap$Config.RGB_565;
        bitmapFactory$Options.inPurgeable = true;
        this.updateThumbnails(ThumbnailUtil.rotateThumbnail(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeByteArray(array, 0, array.length, bitmapFactory$Options), this.mThumbnailSize, this.mThumbnailSize), i));
    }
    
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mUnConstraintIndicator = (LinearLayout)this.findViewById(2131296685);
        FontUtil.setBold(this.mUnConstraintRecordingTimeText = (TextView)this.mUnConstraintIndicator.findViewById(2131296524));
        FontUtil.setBold((TextView)this.mUnConstraintIndicator.findViewById(2131296522));
        this.mConstraintIndicator = (LinearLayout)this.findViewById(2131296362);
        FontUtil.setBold(this.mConstraintRecordingTimeText = (TextView)this.mConstraintIndicator.findViewById(2131296524));
        FontUtil.setBold((TextView)this.mConstraintIndicator.findViewById(2131296522));
        FontUtil.setBold((TextView)this.mConstraintIndicator.findViewById(2131296521));
        FontUtil.setBold(this.mMaxDurationText = (TextView)this.mConstraintIndicator.findViewById(2131296530));
        this.mProgressBar = (RecordingProgressBar)this.findViewById(2131296516);
        this.mSequenceIndicator = (LinearLayout)this.findViewById(2131296596);
        FontUtil.setBold(this.mSequenceRecordingTimeText = (TextView)this.mSequenceIndicator.findViewById(2131296524));
        FontUtil.setBold(this.mSequenceRec = (TextView)this.mSequenceIndicator.findViewById(2131296522));
        this.mThumbnailContainer = (LinearLayout)this.mSequenceIndicator.findViewById(2131296660);
        this.mTimeContainer = (LinearLayout)this.mSequenceIndicator.findViewById(2131296662);
        this.mMaxTime = new DurationParameterSet();
        this.mRecordingTime = new DurationParameterSet();
        this.mContainer = this.findViewById(2131296520);
    }
    
    public void onTimeTicked(final int n) {
        this.updateRecordingTime(n);
    }
    
    public void prepareBeforeRecording(final int mDuration) {
        this.mRecordingTime.update(0);
        this.mContainer.setVisibility(4);
        final int dimensionPixelSize = this.getContext().getResources().getDimensionPixelSize(2131165517);
        if (this.mIsConstraint) {
            this.mDuration = mDuration;
            this.mMaxTime.update(this.mDuration);
            this.mProgressBar.setProgress(0, 0);
            this.mStringFormatRemainConstraintTime = this.getContext().getString(2131689547);
            this.mMaxDurationText.setText((CharSequence)String.format(Locale.US, this.mStringFormatRemainConstraintTime, this.mMaxTime.min, this.mMaxTime.sec));
            this.mStringFormatRecordingTime = this.getContext().getString(2131689547);
            this.mConstraintRecordingTimeText.setText((CharSequence)String.format(Locale.US, this.mStringFormatRecordingTime, this.mRecordingTime.min, this.mRecordingTime.sec));
        }
        else {
            this.mStringFormatRecordingTime = this.getContext().getString(2131689547);
            if (this.mIsSequence) {
                this.resetThumbnails();
                this.setIndicator(this.mIsRecording);
                this.mSequenceRecordingTimeText.setText((CharSequence)String.format(Locale.US, this.mStringFormatRecordingTime, this.mRecordingTime.min, this.mRecordingTime.sec));
                this.mSequenceRecordingTimeText.getLayoutParams().width = dimensionPixelSize;
            }
            else {
                this.mUnConstraintRecordingTimeText.setText((CharSequence)String.format(Locale.US, this.mStringFormatRecordingTime, this.mRecordingTime.min, this.mRecordingTime.sec));
                this.mUnConstraintRecordingTimeText.getLayoutParams().width = dimensionPixelSize;
            }
        }
        if (this.mIsConstraint) {
            this.mConstraintIndicator.setVisibility(0);
            this.mUnConstraintIndicator.setVisibility(8);
            this.mSequenceIndicator.setVisibility(8);
        }
        else if (this.mIsSequence) {
            this.mConstraintIndicator.setVisibility(8);
            this.mUnConstraintIndicator.setVisibility(8);
            this.mSequenceIndicator.setVisibility(0);
        }
        else {
            this.mConstraintIndicator.setVisibility(8);
            this.mUnConstraintIndicator.setVisibility(0);
            this.mSequenceIndicator.setVisibility(8);
        }
        this.updateLayout();
    }
    
    public void release() {
    }
    
    public void setConstraint(final boolean mIsConstraint) {
        this.mIsConstraint = mIsConstraint;
    }
    
    public void setIndicator(final boolean b) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setIndicator:");
            sb.append(b);
            CamLog.d(sb.toString());
        }
        this.mIsRecording = b;
        if (this.mIsSequence) {
            this.setSequentialIndicator(b);
        }
        else {
            this.setUnconstraintIndicator(b);
        }
    }
    
    public void setOrientation(final int n) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setOrientation: ");
            sb.append(n);
            CamLog.d(sb.toString());
        }
        this.mDisplayOrientation = n;
        this.updateLayout();
    }
    
    public void setScreenAspect(final LayoutDependencyResolver.ScreenAspect mScreenAspect) {
        this.mScreenAspect = mScreenAspect;
    }
    
    public void setSequenceMode(final boolean mIsSequence) {
        this.mIsSequence = mIsSequence;
    }
    
    public void setVisible(final boolean b) {
        if (b) {
            if (this.mIsConstraint) {
                this.mConstraintIndicator.setVisibility(0);
                this.mUnConstraintIndicator.setVisibility(8);
                this.mSequenceIndicator.setVisibility(8);
            }
            else if (this.mIsSequence) {
                this.mConstraintIndicator.setVisibility(8);
                this.mUnConstraintIndicator.setVisibility(8);
                this.mSequenceIndicator.setVisibility(0);
            }
            else {
                this.mConstraintIndicator.setVisibility(8);
                this.mUnConstraintIndicator.setVisibility(0);
                this.mSequenceIndicator.setVisibility(8);
            }
        }
        else {
            this.mConstraintIndicator.setVisibility(8);
            this.mUnConstraintIndicator.setVisibility(8);
            this.mSequenceIndicator.setVisibility(8);
        }
    }
    
    public void updateRecordingTime(final int n) {
        this.mRecordingTime.update(n);
        this.mContainer.setVisibility(0);
        String text;
        int n2;
        if (this.mRecordingTime.hour < 1) {
            this.mStringFormatRecordingTime = this.getContext().getString(2131689547);
            text = String.format(Locale.US, this.mStringFormatRecordingTime, this.mRecordingTime.min, this.mRecordingTime.sec);
            n2 = this.getContext().getResources().getDimensionPixelSize(2131165517);
        }
        else {
            this.mStringFormatRecordingTime = this.getContext().getString(2131689546);
            text = String.format(Locale.US, this.mStringFormatRecordingTime, this.mRecordingTime.hour, this.mRecordingTime.min, this.mRecordingTime.sec);
            n2 = this.getContext().getResources().getDimensionPixelSize(2131165516);
        }
        if (this.mIsConstraint) {
            this.mConstraintRecordingTimeText.setText((CharSequence)text);
            this.updateProgressbar(n);
        }
        else if (this.mIsSequence) {
            this.mSequenceRecordingTimeText.setText((CharSequence)text);
            this.mSequenceRecordingTimeText.getLayoutParams().width = n2;
        }
        else {
            this.mUnConstraintRecordingTimeText.setText((CharSequence)text);
            this.mUnConstraintRecordingTimeText.getLayoutParams().width = n2;
        }
    }
}
