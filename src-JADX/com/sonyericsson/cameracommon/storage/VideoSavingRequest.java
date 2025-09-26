package com.sonyericsson.cameracommon.storage;

import android.content.ContentValues;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.capability.SharedPrefsTranslator;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusVideo;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import java.io.File;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class VideoSavingRequest extends SavingRequest {
    public static final String TAG = "VideoSavingRequest";
    public final TakenStatusVideo video;

    public VideoSavingRequest(TakenStatusCommon takenStatusCommon, TakenStatusVideo takenStatusVideo) {
        super(takenStatusCommon);
        this.video = takenStatusVideo;
        if (CamLog.VERBOSE) {
            CamLog.d("VideoSavingRequest: at created.");
        }
        log();
    }

    public VideoSavingRequest(VideoSavingRequest videoSavingRequest) {
        super(videoSavingRequest);
        this.video = new TakenStatusVideo(videoSavingRequest.video.maxDurationMills, videoSavingRequest.video.maxFileSizeBytes);
    }

    public long getDuration() {
        return this.video.mDuration;
    }

    public void setDuration(long j) {
        if (CamLog.VERBOSE) {
            CamLog.d("setDuration: " + j);
        }
        this.video.mDuration = j;
    }

    @Override // com.sonyericsson.cameracommon.storage.SavingRequest
    public void log() {
        super.log();
        this.video.log();
    }

    @Override // com.sonyericsson.cameracommon.storage.SavingRequest
    public ContentValues createContentValues(String str) {
        if (CamLog.VERBOSE) {
            CamLog.d("createContentValues savedFileType: " + this.common.savedFileType);
        }
        ContentValues contentValues = new ContentValues();
        File file = new File(getFilePath());
        contentValues.put("title", CommonUtility.removeFileExtension(file.getName()));
        contentValues.put("_display_name", file.getName());
        if (str.length() > 0) {
            contentValues.put("description", str);
        }
        contentValues.put("datetaken", Long.valueOf(getDateTaken()));
        contentValues.put("mime_type", this.common.mimeType);
        contentValues.put("_size", Long.valueOf(file.length()).toString());
        contentValues.put("date_modified", Long.valueOf(file.lastModified() / 1000));
        contentValues.put("artist", "<unknown>");
        contentValues.put("album", "<unknown>");
        contentValues.put("duration", Long.valueOf(getDuration()).toString());
        contentValues.put("resolution", this.common.width + SharedPrefsTranslator.CONNECTOR_CROSS + this.common.height);
        contentValues.put("width", Integer.valueOf(this.common.width));
        contentValues.put("height", Integer.valueOf(this.common.height));
        contentValues.put("_data", getFilePath());
        return contentValues;
    }
}
