package com.sonymobile.cameracommon.view;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.device.CameraParameterConverter;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RecognizedScene {
    final int mIconId;
    final CameraParameterConverter.SceneMode mSceneMode;
    final int mTextId;

    private RecognizedScene(CameraParameterConverter.SceneMode sceneMode, int i, int i2) {
        this.mSceneMode = sceneMode;
        this.mIconId = i;
        this.mTextId = i2;
    }

    public static RecognizedScene create(CameraParameterConverter.SceneMode sceneMode) {
        if (sceneMode == null) {
            return new RecognizedScene(null, -1, -1);
        }
        switch (sceneMode) {
            case AUTO:
                return new RecognizedScene(sceneMode, -1, -1);
            case PORTRAIT:
                return new RecognizedScene(sceneMode, R.drawable.cam_scene_recog_softsnap_icn, R.string.cam_strings_scenes_auto_recog_softsnap_cy_txt);
            case LANDSCAPE:
                return new RecognizedScene(sceneMode, R.drawable.cam_scene_recog_landscape_icn, R.string.cam_strings_scenes_auto_recog_landscape_txt);
            case NIGHT:
                return new RecognizedScene(sceneMode, R.drawable.cam_scene_recog_night_icn, R.string.cam_strings_scenes_auto_recog_twilight_txt);
            case NIGHT_PORTRAIT:
                return new RecognizedScene(sceneMode, R.drawable.cam_scene_recog_night_portrait_icn, R.string.cam_strings_scenes_auto_recog_twilight_portrait_cy_txt);
            case BACKLIGHT:
                return new RecognizedScene(sceneMode, R.drawable.cam_scene_recog_backlight_icn, R.string.cam_strings_scenes_auto_recog_backlight_txt);
            case BACKLIGHT_PORTRAIT:
                return new RecognizedScene(sceneMode, R.drawable.cam_scene_recog_backlight_portrait_icn, R.string.cam_strings_scenes_auto_recog_backlight_portrait_cy_txt);
            case DOCUMENT:
                return new RecognizedScene(sceneMode, R.drawable.cam_scene_recog_document_icn, R.string.cam_strings_scenes_auto_recog_text_txt);
            case DARK:
                return new RecognizedScene(sceneMode, R.drawable.cam_scene_recog_super_low_light_icn, R.string.cam_strings_scenes_auto_recog_low_light_cy_txt);
            case BABY:
                return new RecognizedScene(sceneMode, R.drawable.cam_scene_recog_infant_icn, R.string.cam_strings_scenes_auto_recog_infant_cy_txt);
            case SPOTLIGHT:
                return new RecognizedScene(sceneMode, R.drawable.cam_scene_recog_spotlight_icn, R.string.cam_strings_scenes_auto_recog_spotlight_cy_txt);
            case DISH:
                return new RecognizedScene(sceneMode, R.drawable.cam_scene_recog_gourmet_icn, R.string.cam_strings_scenes_auto_recog_gourmet_txt);
            default:
                return new RecognizedScene(sceneMode, -1, -1);
        }
    }

    public CameraParameterConverter.SceneMode getSceneMode() {
        return this.mSceneMode;
    }

    public int getIconId() {
        return this.mIconId;
    }

    public int getTextId() {
        return this.mTextId;
    }
}
