// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.vanilla.wearablebridge.common;

public interface AbstractCapturableState
{
    public enum AbstractPhotoState
    {
        private static final AbstractPhotoState[] $VALUES;
        
        BLOCKED, 
        IDLE;
        
        static {
            $VALUES = new AbstractPhotoState[] { AbstractPhotoState.IDLE, AbstractPhotoState.BLOCKED };
        }
    }
    
    public enum AbstractVideoState
    {
        private static final AbstractVideoState[] $VALUES;
        
        BLOCKED, 
        IDLE, 
        RECORDING, 
        STARTING_REC;
        
        static {
            $VALUES = new AbstractVideoState[] { AbstractVideoState.IDLE, AbstractVideoState.STARTING_REC, AbstractVideoState.RECORDING, AbstractVideoState.BLOCKED };
        }
    }
}
