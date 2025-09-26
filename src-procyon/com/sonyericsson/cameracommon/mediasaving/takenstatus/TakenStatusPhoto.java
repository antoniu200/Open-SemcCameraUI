// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.mediasaving.takenstatus;

import java.io.Serializable;
import java.util.Arrays;
import com.sonyericsson.android.camera.util.CamLog;

public class TakenStatusPhoto
{
    public static final String TAG = "TakenStatusPhoto";
    protected Facing mFacing;
    public byte[] mImage;
    
    public TakenStatusPhoto() {
        this.mFacing = Facing.UNKNOWN;
    }
    
    public TakenStatusPhoto(final Facing mFacing) {
        this.mFacing = Facing.UNKNOWN;
        this.mFacing = mFacing;
    }
    
    public TakenStatusPhoto(final TakenStatusPhoto takenStatusPhoto) {
        this.mFacing = Facing.UNKNOWN;
        this.mImage = takenStatusPhoto.mImage;
        this.mFacing = takenStatusPhoto.mFacing;
    }
    
    public boolean isFront() {
        return this.mFacing == Facing.FRONT;
    }
    
    public void log() {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Image            : ");
            Serializable value;
            if (this.mImage == null) {
                value = "null";
            }
            else {
                value = Arrays.hashCode(this.mImage);
            }
            sb.append(value);
            CamLog.d(sb.toString());
        }
    }
    
    public enum Facing
    {
        private static final Facing[] $VALUES;
        
        BACK, 
        FRONT, 
        UNKNOWN;
        
        static {
            $VALUES = new Facing[] { Facing.FRONT, Facing.BACK, Facing.UNKNOWN };
        }
    }
}
