package com.sonyericsson.cameracommon.utility;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PositionConverter {
    public static final String TAG = "PositionConverter";
    private static PositionConverter sInstance = new PositionConverter();
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

    public static PositionConverter getInstance() {
        return sInstance;
    }

    private PositionConverter() {
    }

    public void init(boolean z, Rect rect, Rect rect2, Rect rect3) {
        if (CamLog.VERBOSE) {
            CamLog.d("prepare: mirror: " + z + ", surface: " + rect + ", preview: " + rect2);
        }
        if (this.mMirror != z || this.mSurfaceWidth != rect.width() || this.mSurfaceHeight != rect.height()) {
            this.mPrepared = false;
        }
        this.mMirror = z;
        this.mPreviewWidth = rect2.width();
        this.mPreviewHeight = rect2.height();
        this.mActiveArrayRect = new Rect(rect3);
        this.mActiveArrayWidth = this.mActiveArrayRect.centerX() * 2;
        this.mActiveArrayHeight = this.mActiveArrayRect.centerY() * 2;
        setSurfaceSize(rect.width(), rect.height());
        this.mPrepared = true;
    }

    public void setSurfaceSize(int i, int i2) {
        if (CamLog.VERBOSE) {
            CamLog.d("setSurfaceSize (" + this.mSurfaceWidth + " x " + this.mSurfaceHeight + ") to (" + i + " x " + i2 + ")");
        }
        if (this.mPrepared && this.mSurfaceWidth == i && this.mSurfaceHeight == i2) {
            return;
        }
        this.mSurfaceWidth = i;
        this.mSurfaceHeight = i2;
        this.mCropRegion = this.mActiveArrayRect;
        updateMatrix();
    }

    public void setCropRegion(Rect rect) {
        if (rect.equals(this.mCropRegion)) {
            return;
        }
        this.mCropRegion = rect;
        if (this.mSurfaceWidth == 0 || this.mSurfaceHeight == 0) {
            return;
        }
        updateMatrix();
    }

    private void updateMatrix() {
        float fHeight;
        float fHeight2;
        float fHeight3;
        float fHeight4;
        LayoutOrientationResolver layoutOrientationResolver = LayoutOrientationResolver.getInstance();
        float fWidth = this.mCropRegion.width() / this.mCropRegion.height();
        this.mMatrixFromActiveArrayToSurface = new Matrix();
        if (layoutOrientationResolver.getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            this.mMatrixFromActiveArrayToSurface.setRotate(90.0f);
            this.mMatrixFromActiveArrayToSurface.postTranslate(this.mActiveArrayHeight, 0.0f);
            if (this.mMirror) {
                this.mMatrixFromActiveArrayToSurface.postScale(1.0f, -1.0f);
                this.mMatrixFromActiveArrayToSurface.postTranslate(0.0f, this.mActiveArrayWidth);
            }
            if (fWidth < this.mSurfaceHeight / this.mSurfaceWidth) {
                fHeight4 = this.mSurfaceHeight / this.mCropRegion.width();
            } else {
                fHeight4 = this.mSurfaceWidth / this.mCropRegion.height();
            }
            this.mMatrixFromActiveArrayToSurface.postScale(fHeight4, fHeight4);
            this.mMatrixFromActiveArrayToSurface.postTranslate((this.mSurfaceWidth / 2.0f) - (this.mCropRegion.centerY() * fHeight4), (this.mSurfaceHeight / 2.0f) - (this.mCropRegion.centerX() * fHeight4));
        } else {
            this.mMatrixFromActiveArrayToSurface.setRotate(0.0f);
            if (this.mMirror) {
                this.mMatrixFromActiveArrayToSurface.postScale(-1.0f, 1.0f);
                this.mMatrixFromActiveArrayToSurface.postTranslate(this.mActiveArrayWidth, 0.0f);
            }
            if (fWidth < this.mSurfaceWidth / this.mSurfaceHeight) {
                fHeight = this.mSurfaceWidth / this.mCropRegion.width();
            } else {
                fHeight = this.mSurfaceHeight / this.mCropRegion.height();
            }
            this.mMatrixFromActiveArrayToSurface.postScale(fHeight, fHeight);
            this.mMatrixFromActiveArrayToSurface.postTranslate((this.mSurfaceWidth / 2.0f) - (this.mCropRegion.centerX() * fHeight), (this.mSurfaceHeight / 2.0f) - (this.mCropRegion.centerY() * fHeight));
        }
        this.mMatrixFromSurfaceToActiveArray = new Matrix();
        if (layoutOrientationResolver.getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            this.mMatrixFromSurfaceToActiveArray.setRotate(-90.0f);
            if (this.mMirror) {
                this.mMatrixFromSurfaceToActiveArray.postScale(-1.0f, 1.0f);
            }
            this.mMatrixFromSurfaceToActiveArray.postTranslate(this.mMirror ? this.mSurfaceHeight : 0.0f, this.mSurfaceWidth);
            if (fWidth < this.mSurfaceHeight / this.mSurfaceWidth) {
                fHeight3 = this.mCropRegion.width() / this.mSurfaceHeight;
            } else {
                fHeight3 = this.mCropRegion.height() / this.mSurfaceWidth;
            }
            this.mMatrixFromSurfaceToActiveArray.postScale(fHeight3, fHeight3);
            this.mMatrixFromSurfaceToActiveArray.postTranslate(this.mCropRegion.centerX() - ((this.mSurfaceHeight * fHeight3) / 2.0f), this.mCropRegion.centerY() - ((this.mSurfaceWidth * fHeight3) / 2.0f));
            return;
        }
        this.mMatrixFromSurfaceToActiveArray.setRotate(0.0f);
        if (this.mMirror) {
            this.mMatrixFromSurfaceToActiveArray.postScale(-1.0f, 1.0f);
            this.mMatrixFromSurfaceToActiveArray.postTranslate(this.mSurfaceWidth, 0.0f);
        }
        if (fWidth < this.mSurfaceWidth / this.mSurfaceHeight) {
            fHeight2 = this.mCropRegion.width() / this.mSurfaceWidth;
        } else {
            fHeight2 = this.mCropRegion.height() / this.mSurfaceHeight;
        }
        this.mMatrixFromSurfaceToActiveArray.postScale(fHeight2, fHeight2);
        this.mMatrixFromSurfaceToActiveArray.postTranslate(this.mCropRegion.centerX() - ((this.mSurfaceWidth * fHeight2) / 2.0f), this.mCropRegion.centerY() - ((this.mSurfaceHeight * fHeight2) / 2.0f));
    }

    public void setPreviewSize(int i, int i2) {
        if (CamLog.VERBOSE) {
            CamLog.d("setPreviewSize (" + this.mPreviewWidth + " x " + this.mPreviewHeight + ") to (" + i + " x " + i2 + ")");
        }
        this.mPreviewWidth = i;
        this.mPreviewHeight = i2;
    }

    public Rect convertFromActiveArrayToView(Rect rect) {
        Rect rectConvert = convert(rect, this.mMatrixFromActiveArrayToSurface);
        if (CamLog.VERBOSE) {
            CamLog.d("convertFromActiveArrayToView (" + rect + ") to (" + rectConvert + ")");
        }
        return rectConvert;
    }

    public Rect convertFromViewToActiveArray(Rect rect) {
        Rect rectConvert = convert(rect, this.mMatrixFromSurfaceToActiveArray);
        if (!this.mActiveArrayRect.contains(rectConvert)) {
            rectConvert.intersect(this.mActiveArrayRect);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("convertFromViewToActiveArray (" + rect + ") to (" + rectConvert + ")");
        }
        return rectConvert;
    }

    private Rect convert(Rect rect, Matrix matrix) {
        if (matrix == null) {
            CamLog.w("Matrix to convert rect is null. Surface has not been created.");
            return new Rect();
        }
        RectF rectF = new RectF(rect);
        matrix.mapRect(rectF);
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    public Rect getPreviewSize() {
        return new Rect(0, 0, this.mPreviewWidth, this.mPreviewHeight);
    }
}
