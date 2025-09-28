package com.sonyericsson.cameracommon.mediasaving.takenstatus;

import com.sonyericsson.android.camera.util.CamLog;
import java.util.Arrays;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TakenStatusPhoto {
    public static final String TAG = "TakenStatusPhoto";
    protected Facing mFacing;
    public byte[] mImage;

    public enum Facing {
        FRONT,
        BACK,
        UNKNOWN
    }

    public TakenStatusPhoto() {
        this.mFacing = Facing.UNKNOWN;
    }

    public TakenStatusPhoto(TakenStatusPhoto takenStatusPhoto) {
        this.mFacing = Facing.UNKNOWN;
        this.mImage = takenStatusPhoto.mImage;
        this.mFacing = takenStatusPhoto.mFacing;
    }

    public TakenStatusPhoto(Facing facing) {
        this.mFacing = Facing.UNKNOWN;
        this.mFacing = facing;
    }

    public boolean isFront() {
        return this.mFacing == Facing.FRONT;
    }

    public void log() {
        if (CamLog.VERBOSE) {
            String[] strArr = new String[1];
            StringBuilder sb = new StringBuilder();
            sb.append("Image            : ");
            sb.append(this.mImage == null ? "null" : Integer.valueOf(Arrays.hashCode(this.mImage)));
            strArr[0] = sb.toString();
            CamLog.d(strArr);
        }
    }
}
