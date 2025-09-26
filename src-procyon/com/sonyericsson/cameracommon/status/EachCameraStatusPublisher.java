// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.status;

import android.graphics.Rect;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.device.CameraParameters;
import com.sonyericsson.cameracommon.status.eachcamera.Hdr;
import com.sonyericsson.cameracommon.status.eachcamera.SlowMotion;
import com.sonyericsson.cameracommon.status.eachcamera.OnlineRemote;
import com.sonyericsson.cameracommon.status.eachcamera.SoundPhoto;
import com.sonyericsson.cameracommon.status.eachcamera.TimeShift;
import com.sonyericsson.cameracommon.status.eachcamera.ArtFilter;
import com.sonyericsson.cameracommon.status.eachcamera.Metadata;
import com.sonyericsson.cameracommon.status.eachcamera.VideoNoiseReduction;
import com.sonyericsson.cameracommon.status.eachcamera.PhotoLight;
import com.sonyericsson.cameracommon.status.eachcamera.VideoStabilizerStatus;
import com.sonyericsson.cameracommon.status.eachcamera.ObjectTracking;
import com.sonyericsson.cameracommon.status.eachcamera.SceneRecognition;
import com.sonyericsson.cameracommon.status.eachcamera.FaceDetection;
import com.sonyericsson.cameracommon.status.eachcamera.FaceIdentification;
import com.sonyericsson.cameracommon.status.eachcamera.BurstShooting;
import com.sonyericsson.cameracommon.status.eachcamera.VideoRecordingFps;
import com.sonyericsson.cameracommon.status.eachcamera.PreviewMaxFps;
import com.sonyericsson.cameracommon.status.eachcamera.VideoResolution;
import com.sonyericsson.cameracommon.status.eachcamera.PictureResolution;
import com.sonyericsson.cameracommon.status.eachcamera.PreviewResolution;
import com.sonyericsson.cameracommon.status.eachcamera.DeviceStatus;
import com.sonyericsson.android.camera.device.CameraInfo;
import android.content.Context;

public class EachCameraStatusPublisher extends CameraStatusPublisher<EachCameraStatusValue>
{
    private final String mKeyPrefix;
    
    public EachCameraStatusPublisher(final Context context, final CameraInfo.CameraId cameraId) {
        super(context);
        if (this.getCameraCommonVersion() >= 10) {
            final StringBuilder sb = new StringBuilder();
            sb.append("camera");
            sb.append(cameraId.getCameraDeviceIdApi1());
            sb.append("_");
            this.mKeyPrefix = sb.toString();
        }
        else {
            this.mKeyPrefix = null;
        }
    }
    
    @Override
    protected String keyPrefix() {
        return this.mKeyPrefix;
    }
    
    @Override
    public CameraStatusPublisher<EachCameraStatusValue> putDefaultAll() {
        ((CameraStatusPublisher<DeviceStatus>)this).put(new DeviceStatus(DeviceStatus.DEFAULT_VALUE));
        ((CameraStatusPublisher<PreviewResolution>)this).put(new PreviewResolution(PreviewResolution.DEFAULT_VALUE));
        ((CameraStatusPublisher<PictureResolution>)this).put(new PictureResolution(PictureResolution.DEFAULT_VALUE));
        ((CameraStatusPublisher<VideoResolution>)this).put(new VideoResolution(VideoResolution.DEFAULT_VALUE));
        ((CameraStatusPublisher<PreviewMaxFps>)this).put(new PreviewMaxFps(0));
        ((CameraStatusPublisher<VideoRecordingFps>)this).put(new VideoRecordingFps(0));
        ((CameraStatusPublisher<BurstShooting>)this).put(new BurstShooting(BurstShooting.DEFAULT_VALUE));
        ((CameraStatusPublisher<FaceIdentification>)this).put(new FaceIdentification(FaceIdentification.DEFAULT_VALUE));
        ((CameraStatusPublisher<FaceDetection>)this).put(new FaceDetection(FaceDetection.DEFAULT_VALUE));
        ((CameraStatusPublisher<SceneRecognition>)this).put(new SceneRecognition(SceneRecognition.DEFAULT_VALUE));
        ((CameraStatusPublisher<ObjectTracking>)this).put(new ObjectTracking(ObjectTracking.DEFAULT_VALUE));
        ((CameraStatusPublisher<VideoStabilizerStatus>)this).put(new VideoStabilizerStatus(VideoStabilizerStatus.DEFAULT_VALUE));
        ((CameraStatusPublisher<PhotoLight>)this).put(new PhotoLight(PhotoLight.DEFAULT_VALUE));
        ((CameraStatusPublisher<VideoNoiseReduction>)this).put(new VideoNoiseReduction(VideoNoiseReduction.DEFAULT_VALUE));
        ((CameraStatusPublisher<Metadata>)this).put(new Metadata(Metadata.DEFAULT_VALUE));
        ((CameraStatusPublisher<ArtFilter>)this).put(new ArtFilter(ArtFilter.DEFAULT_VALUE));
        ((CameraStatusPublisher<TimeShift>)this).put(new TimeShift(TimeShift.DEFAULT_VALUE));
        ((CameraStatusPublisher<SoundPhoto>)this).put(new SoundPhoto(SoundPhoto.DEFAULT_VALUE));
        ((CameraStatusPublisher<OnlineRemote>)this).put(new OnlineRemote(OnlineRemote.DEFAULT_VALUE));
        ((CameraStatusPublisher<SlowMotion>)this).put(new SlowMotion(SlowMotion.Value.OFF));
        ((CameraStatusPublisher<Hdr>)this).put(new Hdr(Hdr.Value.OFF));
        return this;
    }
    
    public EachCameraStatusPublisher putFromParameter(final CameraParameters cameraParameters, final CameraInfo.CameraId cameraId, final boolean b) {
        if (cameraParameters != null) {
            final Rect previewSize = cameraParameters.getPreviewSize();
            if (previewSize != null) {
                ((CameraStatusPublisher<PreviewResolution>)this).put(new PreviewResolution(previewSize));
                ((CameraStatusPublisher<PreviewMaxFps>)this).put(new PreviewMaxFps(PlatformCapability.getMaxPreviewFps(cameraId)));
            }
            if (!b) {
                final Rect pictureSize = cameraParameters.getPictureSize();
                if (pictureSize != null) {
                    ((CameraStatusPublisher<PictureResolution>)this).put(new PictureResolution(pictureSize));
                }
            }
        }
        return this;
    }
}
