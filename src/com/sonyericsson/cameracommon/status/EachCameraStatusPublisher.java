package com.sonyericsson.cameracommon.status;

import android.content.Context;
import android.graphics.Rect;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.cameracommon.status.eachcamera.ArtFilter;
import com.sonyericsson.cameracommon.status.eachcamera.BurstShooting;
import com.sonyericsson.cameracommon.status.eachcamera.DeviceStatus;
import com.sonyericsson.cameracommon.status.eachcamera.FaceDetection;
import com.sonyericsson.cameracommon.status.eachcamera.FaceIdentification;
import com.sonyericsson.cameracommon.status.eachcamera.Hdr;
import com.sonyericsson.cameracommon.status.eachcamera.Metadata;
import com.sonyericsson.cameracommon.status.eachcamera.ObjectTracking;
import com.sonyericsson.cameracommon.status.eachcamera.OnlineRemote;
import com.sonyericsson.cameracommon.status.eachcamera.PhotoLight;
import com.sonyericsson.cameracommon.status.eachcamera.PictureResolution;
import com.sonyericsson.cameracommon.status.eachcamera.PreviewMaxFps;
import com.sonyericsson.cameracommon.status.eachcamera.PreviewResolution;
import com.sonyericsson.cameracommon.status.eachcamera.SceneRecognition;
import com.sonyericsson.cameracommon.status.eachcamera.SlowMotion;
import com.sonyericsson.cameracommon.status.eachcamera.SoundPhoto;
import com.sonyericsson.cameracommon.status.eachcamera.TimeShift;
import com.sonyericsson.cameracommon.status.eachcamera.VideoNoiseReduction;
import com.sonyericsson.cameracommon.status.eachcamera.VideoRecordingFps;
import com.sonyericsson.cameracommon.status.eachcamera.VideoResolution;
import com.sonyericsson.cameracommon.status.eachcamera.VideoStabilizerStatus;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class EachCameraStatusPublisher extends CameraStatusPublisher<EachCameraStatusValue> {
    private final String mKeyPrefix;

    public EachCameraStatusPublisher(Context context, CameraInfo.CameraId cameraId) {
        super(context);
        if (getCameraCommonVersion() >= 10) {
            this.mKeyPrefix = "camera" + cameraId.getCameraDeviceIdApi1() + "_";
            return;
        }
        this.mKeyPrefix = null;
    }

    public EachCameraStatusPublisher putFromParameter(CameraParameters cameraParameters, CameraInfo.CameraId cameraId, boolean z) {
        Rect pictureSize;
        if (cameraParameters != null) {
            Rect previewSize = cameraParameters.getPreviewSize();
            if (previewSize != null) {
                put(new PreviewResolution(previewSize));
                put(new PreviewMaxFps(PlatformCapability.getMaxPreviewFps(cameraId)));
            }
            if (!z && (pictureSize = cameraParameters.getPictureSize()) != null) {
                put(new PictureResolution(pictureSize));
            }
        }
        return this;
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusPublisher
    public CameraStatusPublisher<EachCameraStatusValue> putDefaultAll() {
        put(new DeviceStatus(DeviceStatus.DEFAULT_VALUE));
        put(new PreviewResolution(PreviewResolution.DEFAULT_VALUE));
        put(new PictureResolution(PictureResolution.DEFAULT_VALUE));
        put(new VideoResolution(VideoResolution.DEFAULT_VALUE));
        put(new PreviewMaxFps(0));
        put(new VideoRecordingFps(0));
        put(new BurstShooting(BurstShooting.DEFAULT_VALUE));
        put(new FaceIdentification(FaceIdentification.DEFAULT_VALUE));
        put(new FaceDetection(FaceDetection.DEFAULT_VALUE));
        put(new SceneRecognition(SceneRecognition.DEFAULT_VALUE));
        put(new ObjectTracking(ObjectTracking.DEFAULT_VALUE));
        put(new VideoStabilizerStatus(VideoStabilizerStatus.DEFAULT_VALUE));
        put(new PhotoLight(PhotoLight.DEFAULT_VALUE));
        put(new VideoNoiseReduction(VideoNoiseReduction.DEFAULT_VALUE));
        put(new Metadata(Metadata.DEFAULT_VALUE));
        put(new ArtFilter(ArtFilter.DEFAULT_VALUE));
        put(new TimeShift(TimeShift.DEFAULT_VALUE));
        put(new SoundPhoto(SoundPhoto.DEFAULT_VALUE));
        put(new OnlineRemote(OnlineRemote.DEFAULT_VALUE));
        put(new SlowMotion(SlowMotion.Value.OFF));
        put(new Hdr(Hdr.Value.OFF));
        return this;
    }

    @Override // com.sonyericsson.cameracommon.status.CameraStatusPublisher
    protected String keyPrefix() {
        return this.mKeyPrefix;
    }
}
