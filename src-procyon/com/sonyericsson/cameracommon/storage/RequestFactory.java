// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusVideo;
import android.net.Uri;
import java.util.Iterator;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusPhoto;
import android.media.ImageReader;
import android.media.Image;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;

public class RequestFactory
{
    private static final String TAG = "RequestFactory";
    
    public static SavingRequest createSavingRequest(final RequestBuilder requestBuilder) {
        final TakenStatusCommon mCommonStatus = requestBuilder.mCommonStatus;
        SavingRequest savingRequest;
        if (requestBuilder instanceof VideoSavingRequestBuilder) {
            final VideoSavingRequestBuilder videoSavingRequestBuilder = (VideoSavingRequestBuilder)requestBuilder;
            savingRequest = new VideoSavingRequest(mCommonStatus, videoSavingRequestBuilder.mVideoStatus);
            savingRequest.setOneShot(((RequestBuilder)videoSavingRequestBuilder).isOneShot());
        }
        else {
            final PhotoSavingRequestBuilder photoSavingRequestBuilder = (PhotoSavingRequestBuilder)requestBuilder;
            savingRequest = new PhotoSavingRequest(mCommonStatus, photoSavingRequestBuilder.mPhotoStatus, photoSavingRequestBuilder.mShouldUpdateOrientationBeforeStoring);
            ((PhotoSavingRequest)savingRequest).attachImageReader(photoSavingRequestBuilder.mImageReader, photoSavingRequestBuilder.mOnImageReaderDettachedListener);
            savingRequest.setOneShot(photoSavingRequestBuilder.isOneShot());
        }
        savingRequest.setFinalInSavingGroup(requestBuilder.isFinalInSavingGroup());
        savingRequest.setStorageType(requestBuilder.getStorageType());
        return savingRequest;
    }
    
    public static class PhotoSavingRequestBuilder extends RequestBuilder
    {
        private Image mImage;
        private ImageReader mImageReader;
        private boolean mIsOneshot;
        private PhotoSavingRequest.OnImageReaderDetachedListener mOnImageReaderDettachedListener;
        public TakenStatusPhoto mPhotoStatus;
        private boolean mShouldUpdateOrientationBeforeStoring;
        
        public PhotoSavingRequestBuilder(final TakenStatusCommon takenStatusCommon, final TakenStatusPhoto mPhotoStatus, final boolean mShouldUpdateOrientationBeforeStoring) {
            super(takenStatusCommon);
            this.mIsOneshot = false;
            this.mPhotoStatus = mPhotoStatus;
            this.mShouldUpdateOrientationBeforeStoring = mShouldUpdateOrientationBeforeStoring;
        }
        
        public void attachImageReader(final ImageReader mImageReader, final PhotoSavingRequest.OnImageReaderDetachedListener mOnImageReaderDettachedListener) {
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
        
        public int getCaptureIdForPredictiveCapture() {
            return this.mCommonStatus.mCaptureIdForPredictiveCapture;
        }
        
        public byte[] getImageData() {
            return this.mPhotoStatus.mImage;
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
        
        public String getSaveTimeForPredictiveCapture() {
            return this.mCommonStatus.mSaveTimeForPredictiveCapture;
        }
        
        public boolean getShouldUpdateOrientationBeforeStoring() {
            return this.mShouldUpdateOrientationBeforeStoring;
        }
        
        @Override
        public boolean isOneShot() {
            return this.mIsOneshot;
        }
        
        public void setCaptureIdForCaptureGourp(final int mCaptureIdForPredictiveCapture) {
            this.mCommonStatus.mCaptureIdForPredictiveCapture = mCaptureIdForPredictiveCapture;
        }
        
        public void setImageData(final byte[] mImage) {
            this.mPhotoStatus.mImage = mImage;
        }
        
        public void setOneshot(final boolean mIsOneshot) {
            this.mIsOneshot = mIsOneshot;
        }
        
        public void setSaveTimeForCaptureGroup(final String mSaveTimeForPredictiveCapture) {
            this.mCommonStatus.mSaveTimeForPredictiveCapture = mSaveTimeForPredictiveCapture;
        }
    }
    
    public abstract static class RequestBuilder
    {
        public TakenStatusCommon mCommonStatus;
        private boolean mFinalRequest;
        private boolean mIsOneShot;
        private Storage.StorageType mStorageType;
        
        public RequestBuilder(final TakenStatusCommon mCommonStatus) {
            this.mFinalRequest = true;
            this.mIsOneShot = false;
            this.mCommonStatus = mCommonStatus;
        }
        
        public void addCallback(final Storage.OnStoreCompletedListener referent) {
            final Iterator<WeakReference<Storage.OnStoreCompletedListener>> iterator = this.mCommonStatus.mCallbacks.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().get() == referent) {
                    return;
                }
            }
            this.mCommonStatus.mCallbacks.add(new WeakReference<Storage.OnStoreCompletedListener>(referent));
        }
        
        public long getDateTaken() {
            return this.mCommonStatus.mDateTaken;
        }
        
        public Uri getExtraOutput() {
            return this.mCommonStatus.mExtraOutput;
        }
        
        public String getFilePath() {
            return this.mCommonStatus.mFilePath;
        }
        
        public int getRequestId() {
            return this.mCommonStatus.mRequestId;
        }
        
        public int getSomcType() {
            return this.mCommonStatus.mSomcType;
        }
        
        public Storage.StorageType getStorageType() {
            return this.mStorageType;
        }
        
        public boolean isFinalInSavingGroup() {
            return this.mFinalRequest;
        }
        
        public boolean isOneShot() {
            return this.mIsOneShot;
        }
        
        public void setDateTaken(final long mDateTaken) {
            this.mCommonStatus.mDateTaken = mDateTaken;
        }
        
        public void setExtraOutput(final Uri mExtraOutput) {
            this.mCommonStatus.mExtraOutput = mExtraOutput;
        }
        
        public void setFilePath(final String mFilePath) {
            this.mCommonStatus.mFilePath = mFilePath;
        }
        
        public void setFinalInSavingGroup(final boolean mFinalRequest) {
            this.mFinalRequest = mFinalRequest;
        }
        
        public void setOneShot(final boolean mIsOneShot) {
            this.mIsOneShot = mIsOneShot;
        }
        
        public void setRequestId(final int mRequestId) {
            this.mCommonStatus.mRequestId = mRequestId;
        }
        
        public void setSomcType(final int mSomcType) {
            this.mCommonStatus.mSomcType = mSomcType;
        }
        
        public void setStorageType(final Storage.StorageType mStorageType) {
            this.mStorageType = mStorageType;
        }
    }
    
    public static class VideoSavingRequestBuilder extends RequestBuilder
    {
        public TakenStatusVideo mVideoStatus;
        
        public VideoSavingRequestBuilder(final TakenStatusCommon takenStatusCommon, final TakenStatusVideo mVideoStatus) {
            super(takenStatusCommon);
            this.mVideoStatus = mVideoStatus;
        }
        
        private String getOutputFile(String s, final CameraStorageManager cameraStorageManager, final Storage.StorageType storageType) {
            if (cameraStorageManager == null) {
                return "/dev/null";
            }
            String str = "/dev/null";
            int n = 0;
        Label_0045_Outer:
            while (true) {
                Label_0081: {
                    if (n >= 30) {
                        break Label_0081;
                    }
                    final String videoPath = cameraStorageManager.getVideoPath(s, storageType);
                    Label_0055: {
                        if (!videoPath.equals("/dev/null")) {
                            break Label_0055;
                        }
                        while (true) {
                            try {
                                Thread.sleep(100L);
                                ++n;
                                str = videoPath;
                                continue Label_0045_Outer;
                            Block_6_Outer:
                                while (true) {
                                    CamLog.d("Path is neither null nor /dev/null");
                                    str = videoPath;
                                    break Label_0081;
                                    Label_0134: {
                                        return str;
                                    }
                                    while (true) {
                                        str = "/dev/null";
                                        Label_0092:
                                        iftrue(Label_0134:)(!CamLog.VERBOSE);
                                        s = (String)new StringBuilder();
                                        ((StringBuilder)s).append("getOutputFile: ");
                                        ((StringBuilder)s).append(str);
                                        CamLog.d(((StringBuilder)s).toString());
                                        return str;
                                        iftrue(Label_0092:)(n < 30);
                                        continue;
                                    }
                                    str = videoPath;
                                    iftrue(Label_0081:)(!CamLog.VERBOSE);
                                    continue Block_6_Outer;
                                }
                            }
                            catch (final InterruptedException ex) {
                                continue;
                            }
                            break;
                        }
                    }
                }
            }
        }
        
        public long getDuration() {
            return this.mVideoStatus.mDuration;
        }
        
        public TakenStatusVideo getVideo() {
            return this.mVideoStatus;
        }
        
        public void setDuration(final long n) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("setDuration: ");
                sb.append(n);
                CamLog.d(sb.toString());
            }
            this.mVideoStatus.mDuration = n;
        }
        
        public void setSlowMotion(final String s, final Storage storage, final Storage.StorageType storageType) {
            final CameraStorageManager cameraStorageManager = ((StorageImpl)storage).getCameraStorageManager();
            if (cameraStorageManager != null) {
                if (s != null) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("set path for slow motion to video request");
                    }
                    ((RequestBuilder)this).setFilePath(cameraStorageManager.getSlowMotionPath(s, storageType));
                }
                else {
                    if (CamLog.VERBOSE) {
                        CamLog.d("set path video request");
                    }
                    ((RequestBuilder)this).setFilePath(this.getOutputFile(this.mCommonStatus.fileExtension, cameraStorageManager, storageType));
                }
            }
        }
    }
}
