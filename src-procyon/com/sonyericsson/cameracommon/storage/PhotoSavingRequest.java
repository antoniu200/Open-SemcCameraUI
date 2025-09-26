// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import java.nio.ByteBuffer;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import java.io.File;
import android.content.ContentValues;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusPhoto;
import android.media.ImageReader;
import android.media.Image;

public class PhotoSavingRequest extends SavingRequest
{
    public static final String TAG = "PhotoSavingRequest";
    private Image mImage;
    private ImageReader mImageReader;
    private OnImageReaderDetachedListener mOnImageReaderDettachedListener;
    public final TakenStatusPhoto photo;
    public final boolean shouldUpdateOrientationBeforeStoring;
    
    public PhotoSavingRequest(final TakenStatusCommon takenStatusCommon, final TakenStatusPhoto photo, final boolean shouldUpdateOrientationBeforeStoring) {
        super(takenStatusCommon);
        this.photo = photo;
        this.shouldUpdateOrientationBeforeStoring = shouldUpdateOrientationBeforeStoring;
        if (CamLog.VERBOSE) {
            CamLog.d("PhotoSavingRequest: at created.");
        }
        this.log();
    }
    
    public PhotoSavingRequest(final PhotoSavingRequest photoSavingRequest) {
        super(photoSavingRequest);
        this.photo = new TakenStatusPhoto(photoSavingRequest.photo);
        this.shouldUpdateOrientationBeforeStoring = false;
    }
    
    public PhotoSavingRequest(final PhotoSavingRequest photoSavingRequest, final int n) {
        super(photoSavingRequest, n);
        this.photo = new TakenStatusPhoto(photoSavingRequest.photo);
        this.shouldUpdateOrientationBeforeStoring = false;
    }
    
    public void attachImageReader(final ImageReader mImageReader, final OnImageReaderDetachedListener mOnImageReaderDettachedListener) {
        this.mImageReader = mImageReader;
        this.mOnImageReaderDettachedListener = mOnImageReaderDettachedListener;
    }
    
    public void close() {
        if (this.mImage != null) {
            this.mImage.close();
            this.mImage = null;
        }
        if (this.mOnImageReaderDettachedListener != null) {
            this.mOnImageReaderDettachedListener.onDetached(this.mImageReader);
        }
        this.mImageReader = null;
    }
    
    @Override
    public ContentValues createContentValues(final String s) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("createContentValues savedFileType: ");
            sb.append(this.common.savedFileType);
            CamLog.d(sb.toString());
        }
        final ContentValues contentValues = new ContentValues();
        if (this.common.mSomcType != 0) {
            contentValues.put("somctype", Integer.valueOf(this.getSomcType()));
        }
        final File file = new File(this.getFilePath());
        contentValues.put("title", CommonUtility.removeFileExtension(file.getName()));
        contentValues.put("_display_name", file.getName());
        if (s.length() > 0) {
            contentValues.put("description", s);
        }
        contentValues.put("datetaken", Long.valueOf(this.getDateTaken()));
        contentValues.put("mime_type", this.common.mimeType);
        contentValues.put("orientation", Integer.valueOf(this.common.orientation));
        contentValues.put("_size", Long.valueOf(file.length()).toString());
        contentValues.put("date_modified", Long.valueOf(file.lastModified() / 1000L));
        contentValues.put("_data", this.getFilePath());
        contentValues.put("width", Integer.valueOf(this.common.width));
        contentValues.put("height", Integer.valueOf(this.common.height));
        return contentValues;
    }
    
    public byte[] getImageData() {
        return this.photo.mImage;
    }
    
    public ByteBuffer getImageReaderData() {
        ByteBuffer buffer;
        if (this.mImageReader != null) {
            if (this.mImage == null) {
                this.mImage = this.mImageReader.acquireNextImage();
            }
            if (this.mImage == null) {
                throw new IllegalStateException("ImageReader maybe closed");
            }
            buffer = this.mImage.getPlanes()[0].getBuffer();
        }
        else {
            buffer = null;
        }
        return buffer;
    }
    
    public boolean isImageReaderUsing() {
        return this.mImageReader != null;
    }
    
    public boolean isPredictiveCaptureCoverImage() {
        return PredictiveCapturePathBuilder.isPredictiveCaptureCoverImage(this.getFilePath());
    }
    
    public boolean isPredictiveCaptureImage() {
        return PredictiveCapturePathBuilder.isPredictiveCaptureImage(this.getFilePath());
    }
    
    public boolean isPredictiveCaptureLastImage() {
        return PredictiveCapturePathBuilder.isPredictiveCaptureLastImage(this.getFilePath());
    }
    
    @Override
    public void log() {
        super.log();
        this.photo.log();
    }
    
    public void setImageData(final byte[] mImage) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setImageData size: ");
            sb.append(mImage.length);
            CamLog.d(sb.toString());
        }
        this.photo.mImage = mImage;
    }
    
    public interface OnImageReaderDetachedListener
    {
        void onDetached(final ImageReader p0);
    }
}
