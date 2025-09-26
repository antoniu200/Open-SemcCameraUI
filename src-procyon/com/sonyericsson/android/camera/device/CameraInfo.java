// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.device;

public class CameraInfo
{
    public boolean canDisableShutterSound;
    public CameraId facing;
    public int orientation;
    
    public enum CameraId
    {
        private static final CameraId[] $VALUES;
        
        BACK("0", 0), 
        FRONT("1", 1);
        
        private final String mCameraDeviceId;
        private final int mCameraDeviceIdApi1;
        
        static {
            $VALUES = new CameraId[] { CameraId.BACK, CameraId.FRONT };
        }
        
        private CameraId(final String mCameraDeviceId, final int mCameraDeviceIdApi1) {
            this.mCameraDeviceId = mCameraDeviceId;
            this.mCameraDeviceIdApi1 = mCameraDeviceIdApi1;
        }
        
        public String getCameraDeviceId() {
            return this.mCameraDeviceId;
        }
        
        public int getCameraDeviceIdApi1() {
            return this.mCameraDeviceIdApi1;
        }
    }
}
