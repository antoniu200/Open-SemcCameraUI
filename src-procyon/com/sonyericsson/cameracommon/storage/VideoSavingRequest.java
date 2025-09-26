// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import com.sonyericsson.cameracommon.utility.CommonUtility;
import java.io.File;
import android.content.ContentValues;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusCommon;
import com.sonyericsson.cameracommon.mediasaving.takenstatus.TakenStatusVideo;

public class VideoSavingRequest extends SavingRequest
{
    public static final String TAG = "VideoSavingRequest";
    public final TakenStatusVideo video;
    
    public VideoSavingRequest(final TakenStatusCommon takenStatusCommon, final TakenStatusVideo video) {
        super(takenStatusCommon);
        this.video = video;
        if (CamLog.VERBOSE) {
            CamLog.d("VideoSavingRequest: at created.");
        }
        this.log();
    }
    
    public VideoSavingRequest(final VideoSavingRequest videoSavingRequest) {
        super(videoSavingRequest);
        this.video = new TakenStatusVideo(videoSavingRequest.video.maxDurationMills, videoSavingRequest.video.maxFileSizeBytes);
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
        final File file = new File(this.getFilePath());
        contentValues.put("title", CommonUtility.removeFileExtension(file.getName()));
        contentValues.put("_display_name", file.getName());
        if (s.length() > 0) {
            contentValues.put("description", s);
        }
        contentValues.put("datetaken", Long.valueOf(this.getDateTaken()));
        contentValues.put("mime_type", this.common.mimeType);
        contentValues.put("_size", Long.valueOf(file.length()).toString());
        contentValues.put("date_modified", Long.valueOf(file.lastModified() / 1000L));
        contentValues.put("artist", "<unknown>");
        contentValues.put("album", "<unknown>");
        contentValues.put("duration", Long.valueOf(this.getDuration()).toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.common.width);
        sb2.append("x");
        sb2.append(this.common.height);
        contentValues.put("resolution", sb2.toString());
        contentValues.put("width", Integer.valueOf(this.common.width));
        contentValues.put("height", Integer.valueOf(this.common.height));
        contentValues.put("_data", this.getFilePath());
        return contentValues;
    }
    
    public long getDuration() {
        return this.video.mDuration;
    }
    
    @Override
    public void log() {
        super.log();
        this.video.log();
    }
    
    public void setDuration(final long n) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setDuration: ");
            sb.append(n);
            CamLog.d(sb.toString());
        }
        this.video.mDuration = n;
    }
}
