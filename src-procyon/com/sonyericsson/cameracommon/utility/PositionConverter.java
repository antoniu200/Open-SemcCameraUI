// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import android.graphics.RectF;
import com.sonyericsson.android.camera.util.CamLog;
import android.graphics.Matrix;
import android.graphics.Rect;

public class PositionConverter
{
    public static final String TAG = "PositionConverter";
    private static PositionConverter sInstance;
    private int mActiveArrayHeight;
    private Rect mActiveArrayRect;
    private int mActiveArrayWidth;
    private Rect mCropRegion;
    private Matrix mMatrixFromActiveArrayToSurface;
    private Matrix mMatrixFromSurfaceToActiveArray;
    private boolean mMirror;
    private boolean mPrepared;
    private int mPreviewHeight;
    private int mPreviewWidth;
    private int mSurfaceHeight;
    private int mSurfaceWidth;
    
    static {
        PositionConverter.sInstance = new PositionConverter();
    }
    
    private PositionConverter() {
    }
    
    private Rect convert(final Rect rect, final Matrix matrix) {
        if (matrix == null) {
            CamLog.w("Matrix to convert rect is null. Surface has not been created.");
            return new Rect();
        }
        final RectF rectF = new RectF(rect);
        matrix.mapRect(rectF);
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }
    
    public static PositionConverter getInstance() {
        return PositionConverter.sInstance;
    }
    
    private void updateMatrix() {
        final LayoutOrientationResolver instance = LayoutOrientationResolver.getInstance();
        final float n = this.mCropRegion.width() / (float)this.mCropRegion.height();
        this.mMatrixFromActiveArrayToSurface = new Matrix();
        final LayoutOrientationResolver.LayoutOrientationType orientation = instance.getOrientation();
        final LayoutOrientationResolver.LayoutOrientationType portrait = LayoutOrientationResolver.LayoutOrientationType.PORTRAIT;
        final float n2 = 0.0f;
        if (orientation == portrait) {
            this.mMatrixFromActiveArrayToSurface.setRotate(90.0f);
            this.mMatrixFromActiveArrayToSurface.postTranslate((float)this.mActiveArrayHeight, 0.0f);
            if (this.mMirror) {
                this.mMatrixFromActiveArrayToSurface.postScale(1.0f, -1.0f);
                this.mMatrixFromActiveArrayToSurface.postTranslate(0.0f, (float)this.mActiveArrayWidth);
            }
            float n3;
            if (n < this.mSurfaceHeight / (float)this.mSurfaceWidth) {
                n3 = this.mSurfaceHeight / (float)this.mCropRegion.width();
            }
            else {
                n3 = this.mSurfaceWidth / (float)this.mCropRegion.height();
            }
            this.mMatrixFromActiveArrayToSurface.postScale(n3, n3);
            this.mMatrixFromActiveArrayToSurface.postTranslate(this.mSurfaceWidth / 2.0f - this.mCropRegion.centerY() * n3, this.mSurfaceHeight / 2.0f - this.mCropRegion.centerX() * n3);
        }
        else {
            this.mMatrixFromActiveArrayToSurface.setRotate(0.0f);
            if (this.mMirror) {
                this.mMatrixFromActiveArrayToSurface.postScale(-1.0f, 1.0f);
                this.mMatrixFromActiveArrayToSurface.postTranslate((float)this.mActiveArrayWidth, 0.0f);
            }
            float n4;
            if (n < this.mSurfaceWidth / (float)this.mSurfaceHeight) {
                n4 = this.mSurfaceWidth / (float)this.mCropRegion.width();
            }
            else {
                n4 = this.mSurfaceHeight / (float)this.mCropRegion.height();
            }
            this.mMatrixFromActiveArrayToSurface.postScale(n4, n4);
            this.mMatrixFromActiveArrayToSurface.postTranslate(this.mSurfaceWidth / 2.0f - this.mCropRegion.centerX() * n4, this.mSurfaceHeight / 2.0f - this.mCropRegion.centerY() * n4);
        }
        this.mMatrixFromSurfaceToActiveArray = new Matrix();
        if (instance.getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            this.mMatrixFromSurfaceToActiveArray.setRotate(-90.0f);
            if (this.mMirror) {
                this.mMatrixFromSurfaceToActiveArray.postScale(-1.0f, 1.0f);
            }
            final Matrix mMatrixFromSurfaceToActiveArray = this.mMatrixFromSurfaceToActiveArray;
            float n5 = n2;
            if (this.mMirror) {
                n5 = (float)this.mSurfaceHeight;
            }
            mMatrixFromSurfaceToActiveArray.postTranslate(n5, (float)this.mSurfaceWidth);
            float n6;
            if (n < this.mSurfaceHeight / (float)this.mSurfaceWidth) {
                n6 = this.mCropRegion.width() / (float)this.mSurfaceHeight;
            }
            else {
                n6 = this.mCropRegion.height() / (float)this.mSurfaceWidth;
            }
            this.mMatrixFromSurfaceToActiveArray.postScale(n6, n6);
            this.mMatrixFromSurfaceToActiveArray.postTranslate(this.mCropRegion.centerX() - this.mSurfaceHeight * n6 / 2.0f, this.mCropRegion.centerY() - this.mSurfaceWidth * n6 / 2.0f);
        }
        else {
            this.mMatrixFromSurfaceToActiveArray.setRotate(0.0f);
            if (this.mMirror) {
                this.mMatrixFromSurfaceToActiveArray.postScale(-1.0f, 1.0f);
                this.mMatrixFromSurfaceToActiveArray.postTranslate((float)this.mSurfaceWidth, 0.0f);
            }
            float n7;
            if (n < this.mSurfaceWidth / (float)this.mSurfaceHeight) {
                n7 = this.mCropRegion.width() / (float)this.mSurfaceWidth;
            }
            else {
                n7 = this.mCropRegion.height() / (float)this.mSurfaceHeight;
            }
            this.mMatrixFromSurfaceToActiveArray.postScale(n7, n7);
            this.mMatrixFromSurfaceToActiveArray.postTranslate(this.mCropRegion.centerX() - this.mSurfaceWidth * n7 / 2.0f, this.mCropRegion.centerY() - this.mSurfaceHeight * n7 / 2.0f);
        }
    }
    
    public Rect convertFromActiveArrayToView(final Rect obj) {
        final Rect convert = this.convert(obj, this.mMatrixFromActiveArrayToSurface);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("convertFromActiveArrayToView (");
            sb.append(obj);
            sb.append(") to (");
            sb.append(convert);
            sb.append(")");
            CamLog.d(sb.toString());
        }
        return convert;
    }
    
    public Rect convertFromViewToActiveArray(final Rect obj) {
        final Rect convert = this.convert(obj, this.mMatrixFromSurfaceToActiveArray);
        if (!this.mActiveArrayRect.contains(convert)) {
            convert.intersect(this.mActiveArrayRect);
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("convertFromViewToActiveArray (");
            sb.append(obj);
            sb.append(") to (");
            sb.append(convert);
            sb.append(")");
            CamLog.d(sb.toString());
        }
        return convert;
    }
    
    public Rect getPreviewSize() {
        return new Rect(0, 0, this.mPreviewWidth, this.mPreviewHeight);
    }
    
    public void init(final boolean b, final Rect obj, final Rect obj2, final Rect rect) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("prepare: mirror: ");
            sb.append(b);
            sb.append(", surface: ");
            sb.append(obj);
            sb.append(", preview: ");
            sb.append(obj2);
            CamLog.d(sb.toString());
        }
        if (this.mMirror != b || this.mSurfaceWidth != obj.width() || this.mSurfaceHeight != obj.height()) {
            this.mPrepared = false;
        }
        this.mMirror = b;
        this.mPreviewWidth = obj2.width();
        this.mPreviewHeight = obj2.height();
        this.mActiveArrayRect = new Rect(rect);
        this.mActiveArrayWidth = this.mActiveArrayRect.centerX() * 2;
        this.mActiveArrayHeight = this.mActiveArrayRect.centerY() * 2;
        this.setSurfaceSize(obj.width(), obj.height());
        this.mPrepared = true;
    }
    
    public void setCropRegion(final Rect mCropRegion) {
        if (!mCropRegion.equals((Object)this.mCropRegion)) {
            this.mCropRegion = mCropRegion;
            if (this.mSurfaceWidth != 0 && this.mSurfaceHeight != 0) {
                this.updateMatrix();
            }
        }
    }
    
    public void setPreviewSize(final int n, final int n2) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setPreviewSize (");
            sb.append(this.mPreviewWidth);
            sb.append(" x ");
            sb.append(this.mPreviewHeight);
            sb.append(") to (");
            sb.append(n);
            sb.append(" x ");
            sb.append(n2);
            sb.append(")");
            CamLog.d(sb.toString());
        }
        this.mPreviewWidth = n;
        this.mPreviewHeight = n2;
    }
    
    public void setSurfaceSize(final int n, final int n2) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setSurfaceSize (");
            sb.append(this.mSurfaceWidth);
            sb.append(" x ");
            sb.append(this.mSurfaceHeight);
            sb.append(") to (");
            sb.append(n);
            sb.append(" x ");
            sb.append(n2);
            sb.append(")");
            CamLog.d(sb.toString());
        }
        if (this.mPrepared && this.mSurfaceWidth == n && this.mSurfaceHeight == n2) {
            return;
        }
        this.mSurfaceWidth = n;
        this.mSurfaceHeight = n2;
        this.mCropRegion = this.mActiveArrayRect;
        this.updateMatrix();
    }
}
