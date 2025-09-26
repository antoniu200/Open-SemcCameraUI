package com.sonyericsson.cameracommon.storage;

import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusPhoto;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusVideo;
import com.sonyericsson.cameracommon.storage.PhotoSavingRequest;
import com.sonyericsson.cameracommon.storage.Storage;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.Iterator;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RequestFactory {
    private static final String TAG = "RequestFactory";

    public static SavingRequest createSavingRequest(RequestBuilder requestBuilder) {
        SavingRequest savingRequest;
        TakenStatusCommon takenStatusCommon = requestBuilder.mCommonStatus;
        if (requestBuilder instanceof VideoSavingRequestBuilder) {
            VideoSavingRequestBuilder videoSavingRequestBuilder = (VideoSavingRequestBuilder) requestBuilder;
            SavingRequest videoSavingRequest = new VideoSavingRequest(takenStatusCommon, videoSavingRequestBuilder.mVideoStatus);
            videoSavingRequest.setOneShot(videoSavingRequestBuilder.isOneShot());
            savingRequest = videoSavingRequest;
        } else {
            PhotoSavingRequestBuilder photoSavingRequestBuilder = (PhotoSavingRequestBuilder) requestBuilder;
            PhotoSavingRequest photoSavingRequest = new PhotoSavingRequest(takenStatusCommon, photoSavingRequestBuilder.mPhotoStatus, photoSavingRequestBuilder.mShouldUpdateOrientationBeforeStoring);
            photoSavingRequest.attachImageReader(photoSavingRequestBuilder.mImageReader, photoSavingRequestBuilder.mOnImageReaderDettachedListener);
            photoSavingRequest.setOneShot(photoSavingRequestBuilder.isOneShot());
            savingRequest = photoSavingRequest;
        }
        savingRequest.setFinalInSavingGroup(requestBuilder.isFinalInSavingGroup());
        savingRequest.setStorageType(requestBuilder.getStorageType());
        return savingRequest;
    }

    public static abstract class RequestBuilder {
        public TakenStatusCommon mCommonStatus;
        private boolean mFinalRequest = true;
        private boolean mIsOneShot = false;
        private Storage.StorageType mStorageType;

        public RequestBuilder(TakenStatusCommon takenStatusCommon) {
            this.mCommonStatus = takenStatusCommon;
        }

        public void setRequestId(int i) {
            this.mCommonStatus.mRequestId = i;
        }

        public int getRequestId() {
            return this.mCommonStatus.mRequestId;
        }

        public void setFilePath(String str) {
            this.mCommonStatus.mFilePath = str;
        }

        public String getFilePath() {
            return this.mCommonStatus.mFilePath;
        }

        public void setStorageType(Storage.StorageType storageType) {
            this.mStorageType = storageType;
        }

        public Storage.StorageType getStorageType() {
            return this.mStorageType;
        }

        public void setDateTaken(long j) {
            this.mCommonStatus.mDateTaken = j;
        }

        public long getDateTaken() {
            return this.mCommonStatus.mDateTaken;
        }

        public void setExtraOutput(Uri uri) {
            this.mCommonStatus.mExtraOutput = uri;
        }

        public Uri getExtraOutput() {
            return this.mCommonStatus.mExtraOutput;
        }

        public void setSomcType(int i) {
            this.mCommonStatus.mSomcType = i;
        }

        public int getSomcType() {
            return this.mCommonStatus.mSomcType;
        }

        public void setFinalInSavingGroup(boolean z) {
            this.mFinalRequest = z;
        }

        public boolean isFinalInSavingGroup() {
            return this.mFinalRequest;
        }

        public void setOneShot(boolean z) {
            this.mIsOneShot = z;
        }

        public boolean isOneShot() {
            return this.mIsOneShot;
        }

        public void addCallback(Storage.OnStoreCompletedListener onStoreCompletedListener) {
            Iterator<WeakReference<Storage.OnStoreCompletedListener>> it = this.mCommonStatus.mCallbacks.iterator();
            while (it.hasNext()) {
                if (it.next().get() == onStoreCompletedListener) {
                    return;
                }
            }
            this.mCommonStatus.mCallbacks.add(new WeakReference<>(onStoreCompletedListener));
        }
    }

    public static class VideoSavingRequestBuilder extends RequestBuilder {
        public TakenStatusVideo mVideoStatus;

        public VideoSavingRequestBuilder(TakenStatusCommon takenStatusCommon, TakenStatusVideo takenStatusVideo) {
            super(takenStatusCommon);
            this.mVideoStatus = takenStatusVideo;
        }

        public TakenStatusVideo getVideo() {
            return this.mVideoStatus;
        }

        public long getDuration() {
            return this.mVideoStatus.mDuration;
        }

        public void setDuration(long j) {
            if (CamLog.VERBOSE) {
                CamLog.d("setDuration: " + j);
            }
            this.mVideoStatus.mDuration = j;
        }

        public void setSlowMotion(String str, Storage storage, Storage.StorageType storageType) {
            CameraStorageManager cameraStorageManager = ((StorageImpl) storage).getCameraStorageManager();
            if (cameraStorageManager != null) {
                if (str != null) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("set path for slow motion to video request");
                    }
                    setFilePath(cameraStorageManager.getSlowMotionPath(str, storageType));
                } else {
                    if (CamLog.VERBOSE) {
                        CamLog.d("set path video request");
                    }
                    setFilePath(getOutputFile(this.mCommonStatus.fileExtension, cameraStorageManager, storageType));
                }
            }
        }

        private String getOutputFile(String str, CameraStorageManager cameraStorageManager, Storage.StorageType storageType) throws InterruptedException {
            if (cameraStorageManager == null) {
                return MediaSavingConstants.INVALID_FILE_PATH;
            }
            String videoPath = MediaSavingConstants.INVALID_FILE_PATH;
            int i = 0;
            while (true) {
                if (i >= 30) {
                    break;
                }
                videoPath = cameraStorageManager.getVideoPath(str, storageType);
                if (videoPath.equals(MediaSavingConstants.INVALID_FILE_PATH)) {
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException unused) {
                    }
                    i++;
                } else if (CamLog.VERBOSE) {
                    CamLog.d("Path is neither null nor /dev/null");
                }
            }
            if (i >= 30) {
                videoPath = MediaSavingConstants.INVALID_FILE_PATH;
            }
            if (CamLog.VERBOSE) {
                CamLog.d("getOutputFile: " + videoPath);
            }
            return videoPath;
        }
    }

    public static class PhotoSavingRequestBuilder extends RequestBuilder {
        private Image mImage;
        private ImageReader mImageReader;
        private boolean mIsOneshot;
        private PhotoSavingRequest.OnImageReaderDetachedListener mOnImageReaderDettachedListener;
        public TakenStatusPhoto mPhotoStatus;
        private boolean mShouldUpdateOrientationBeforeStoring;

        public PhotoSavingRequestBuilder(TakenStatusCommon takenStatusCommon, TakenStatusPhoto takenStatusPhoto, boolean z) {
            super(takenStatusCommon);
            this.mIsOneshot = false;
            this.mPhotoStatus = takenStatusPhoto;
            this.mShouldUpdateOrientationBeforeStoring = z;
        }

        public boolean getShouldUpdateOrientationBeforeStoring() {
            return this.mShouldUpdateOrientationBeforeStoring;
        }

        public void setSaveTimeForCaptureGroup(String str) {
            this.mCommonStatus.mSaveTimeForPredictiveCapture = str;
        }

        public String getSaveTimeForPredictiveCapture() {
            return this.mCommonStatus.mSaveTimeForPredictiveCapture;
        }

        public void setCaptureIdForCaptureGourp(int i) {
            this.mCommonStatus.mCaptureIdForPredictiveCapture = i;
        }

        public int getCaptureIdForPredictiveCapture() {
            return this.mCommonStatus.mCaptureIdForPredictiveCapture;
        }

        public void setImageData(byte[] bArr) {
            this.mPhotoStatus.mImage = bArr;
        }

        public byte[] getImageData() {
            return this.mPhotoStatus.mImage;
        }

        public void attachImageReader(ImageReader imageReader, PhotoSavingRequest.OnImageReaderDetachedListener onImageReaderDetachedListener) {
            this.mImageReader = imageReader;
            this.mOnImageReaderDettachedListener = onImageReaderDetachedListener;
        }

        public void setOneshot(boolean z) {
            this.mIsOneshot = z;
        }

        @Override // com.sonyericsson.cameracommon.storage.RequestFactory.RequestBuilder
        public boolean isOneShot() {
            return this.mIsOneshot;
        }

        public ByteBuffer getImageReaderData() {
            if (this.mImageReader == null) {
                return null;
            }
            if (this.mImage == null) {
                this.mImage = this.mImageReader.acquireNextImage();
            }
            if (this.mImage != null) {
                return this.mImage.getPlanes()[0].getBuffer();
            }
            throw new IllegalStateException("ImageReader maybe closed");
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
    }
}
