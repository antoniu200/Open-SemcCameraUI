// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.view;

import com.sonyericsson.android.camera.device.CameraParameterConverter;

public class RecognizedScene
{
    final int mIconId;
    final CameraParameterConverter.SceneMode mSceneMode;
    final int mTextId;
    
    private RecognizedScene(final CameraParameterConverter.SceneMode mSceneMode, final int mIconId, final int mTextId) {
        this.mSceneMode = mSceneMode;
        this.mIconId = mIconId;
        this.mTextId = mTextId;
    }
    
    public static RecognizedScene create(final CameraParameterConverter.SceneMode sceneMode) {
        if (sceneMode == null) {
            return new RecognizedScene(null, -1, -1);
        }
        switch (RecognizedScene$1.$SwitchMap$com$sonyericsson$android$camera$device$CameraParameterConverter$SceneMode[sceneMode.ordinal()]) {
            default: {
                return new RecognizedScene(sceneMode, -1, -1);
            }
            case 12: {
                return new RecognizedScene(sceneMode, 2131231270, 2131690065);
            }
            case 11: {
                return new RecognizedScene(sceneMode, 2131231277, 2131690070);
            }
            case 10: {
                return new RecognizedScene(sceneMode, 2131231271, 2131690066);
            }
            case 9: {
                return new RecognizedScene(sceneMode, 2131231278, 2131690068);
            }
            case 8: {
                return new RecognizedScene(sceneMode, 2131231269, 2131690071);
            }
            case 7: {
                return new RecognizedScene(sceneMode, 2131231268, 2131690063);
            }
            case 6: {
                return new RecognizedScene(sceneMode, 2131231267, 2131690064);
            }
            case 5: {
                return new RecognizedScene(sceneMode, 2131231275, 2131690072);
            }
            case 4: {
                return new RecognizedScene(sceneMode, 2131231274, 2131690073);
            }
            case 3: {
                return new RecognizedScene(sceneMode, 2131231272, 2131690067);
            }
            case 2: {
                return new RecognizedScene(sceneMode, 2131231276, 2131690069);
            }
            case 1: {
                return new RecognizedScene(sceneMode, -1, -1);
            }
        }
    }
    
    public int getIconId() {
        return this.mIconId;
    }
    
    public CameraParameterConverter.SceneMode getSceneMode() {
        return this.mSceneMode;
    }
    
    public int getTextId() {
        return this.mTextId;
    }
}
