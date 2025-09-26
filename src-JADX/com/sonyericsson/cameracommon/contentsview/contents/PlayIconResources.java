package com.sonyericsson.cameracommon.contentsview.contents;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.cameracommon.contentsview.contents.Content;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class PlayIconResources {
    PlayIconResources() {
    }

    public static int get(Content.ContentsType contentsType) {
        switch (contentsType) {
            case VIDEO:
            case TIME_SHIFT_VIDEO:
            case TIME_SHIFT_VIDEO_120F:
            case VIDEO_4K:
            case SUPER_SLOW_MOTION_VIDEO:
            case SUPER_SLOW_SHOT_VIDEO:
            case STANDARD_SLOW_MOTION_VIDEO:
            case HIGH_FRAME_RATE_VIDEO:
            case HDR_VIDEO:
            case HDR_VIDEO_4K:
                return R.drawable.cam_core_photo_stack_video_icn;
            default:
                return -1;
        }
    }
}
