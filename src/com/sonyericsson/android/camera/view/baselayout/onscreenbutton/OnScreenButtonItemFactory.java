package com.sonyericsson.android.camera.view.baselayout.onscreenbutton;

import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.view.UserEventHandler;
import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButton;
import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonGroup;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class OnScreenButtonItemFactory {

    public enum ButtonType implements UserEventHandler.TouchEventSource {
        CAPTURE_LARGE,
        CAPTURE_SMALL,
        CAPTURE_WITH_SELFTIMER_LARGE,
        CAPTURE_WITH_SELFTIMER_LONG,
        CAPTURE_WITH_SELFTIMER_SHORT,
        CANCEL_SELFTIMER_LARGE,
        CANCEL_SELFTIMER_SIDE,
        START_RECORDING_LARGE,
        STOP_RECORDING_IN_PAUSE_LARGE,
        STOP_RECORDING_LARGE,
        STOP_RECORDING_SMALL,
        RESUME_RECORDING_SMALL,
        PAUSE_RECORDING_SMALL,
        TRIGGER_SUPER_SLOW_MOTION,
        TRIGGER_SUPER_SLOW_MOTION_PRESSED,
        TRIGGER_SUPER_SLOW_MOTION_DISABLED,
        TOUCH_CAPTURE,
        TOUCH_CAPTURE_WITH_SELFTIMER,
        TOUCH_CAPTURE_WITH_SELFTIMER_LONG,
        TOUCH_CAPTURE_WITH_SELFTIMER_SHORT,
        TOUCH_RECORDING_START,
        SIDE_TOUCH_STOP_RECORDING,
        SIDE_TOUCH_PAUSE_RECORDING,
        SIDE_TOUCH_START_RECORDING,
        SIDE_TOUCH_SNAPSHOT_RECORDING,
        SIDE_TOUCH_STOP_RECORDING_IN_PAUSE,
        SIDE_TOUCH_RESUME_RECORDING
    }

    public static OnScreenButtonGroup.MutableButtonItem createMutableButton(OnScreenButtonListener onScreenButtonListener) {
        return new OnScreenButtonGroup.MutableButtonItem(onScreenButtonListener, true);
    }

    public static OnScreenButtonGroup.Item createButton(ButtonType buttonType, OnScreenButtonListener onScreenButtonListener) {
        switch (buttonType) {
            case CAPTURE_LARGE:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.capture_button_photo, -1, -1, R.string.cam_strings_accessibility_shutter_button_txt, null), onScreenButtonListener, false);
            case TOUCH_CAPTURE:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.cam_touch_capture_button_icn, R.drawable.cam_touch_capture_button_port_icn, -1, -1, null), onScreenButtonListener, false);
            case TOUCH_RECORDING_START:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.cam_touch_capture_button_icn, R.drawable.cam_touch_capture_button_port_icn, -1, -1, null), onScreenButtonListener, false);
            case TOUCH_CAPTURE_WITH_SELFTIMER:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.cam_touch_capture_button_selftimer_icn, R.drawable.cam_touch_capture_button_selftimer_port_icn, -1, -1, null), onScreenButtonListener, false);
            case TOUCH_CAPTURE_WITH_SELFTIMER_LONG:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.cam_touch_capture_button_selftimer_10sec_icn, R.drawable.cam_touch_capture_button_selftimer_10sec_port_icn, -1, -1, null), onScreenButtonListener, false);
            case TOUCH_CAPTURE_WITH_SELFTIMER_SHORT:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.cam_touch_capture_button_selftimer_3sec_icn, R.drawable.cam_touch_capture_button_selftimer_3sec_port_icn, -1, -1, null), onScreenButtonListener, false);
            case CAPTURE_SMALL:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.capture_button_snapshot, R.drawable.capture_button_snapshot_portrait, -1, R.string.cam_strings_accessibility_shutter_button_txt, null), onScreenButtonListener, false);
            case START_RECORDING_LARGE:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.capture_button_video_rec_start, -1, -1, R.string.cam_strings_accessibility_recording_button_txt, null), onScreenButtonListener, false);
            case STOP_RECORDING_IN_PAUSE_LARGE:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.capture_button_video_rec_stop, -1, -1, R.string.cam_strings_accessibility_recording_stop_button_txt, null), onScreenButtonListener, false);
            case STOP_RECORDING_LARGE:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.capture_button_video_stop_rec, -1, -1, R.string.cam_strings_accessibility_recording_stop_button_txt, null), onScreenButtonListener, false);
            case RESUME_RECORDING_SMALL:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.capture_button_video_rec, -1, -1, R.string.cam_strings_accessibility_recording_button_txt, null), onScreenButtonListener, false);
            case PAUSE_RECORDING_SMALL:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.capture_button_video_pause, -1, -1, R.string.cam_strings_accessibility_pause_button_txt, null), onScreenButtonListener, false);
            case TRIGGER_SUPER_SLOW_MOTION:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.capture_button_video_super_slow_motion, -1, -1, R.string.cam_strings_accessibility_recording_slow_motion_button_txt, null), onScreenButtonListener, false);
            case TRIGGER_SUPER_SLOW_MOTION_PRESSED:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.cam_core_capture_button_super_slow_motion_pressed_icn, -1, -1, R.string.cam_strings_accessibility_recording_slow_motion_button_txt, null), onScreenButtonListener, false);
            case TRIGGER_SUPER_SLOW_MOTION_DISABLED:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.cam_core_capture_button_super_slow_motion_disable_icn, -1, -1, R.string.cam_strings_accessibility_recording_slow_motion_button_txt, null), onScreenButtonListener, false);
            case CAPTURE_WITH_SELFTIMER_LARGE:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.capture_button_selftimer, -1, -1, R.string.cam_strings_accessibility_shutter_button_txt, null), onScreenButtonListener, false);
            case CAPTURE_WITH_SELFTIMER_LONG:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.selftimer_shutter_button_10sec, -1, -1, R.string.cam_strings_accessibility_shutter_button_txt, null), onScreenButtonListener, false);
            case CAPTURE_WITH_SELFTIMER_SHORT:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.selftimer_shutter_button_3sec, -1, -1, R.string.cam_strings_accessibility_shutter_button_txt, null), onScreenButtonListener, false);
            case CANCEL_SELFTIMER_LARGE:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.selftimer_cancel_button, R.drawable.selftimer_cancel_button_port, -1, R.string.cam_strings_accessibility_self_timer_stop_button_txt, null), onScreenButtonListener, false);
            case CANCEL_SELFTIMER_SIDE:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.slow_motion_cancel, R.drawable.slow_motion_cancel, -1, R.string.cam_strings_accessibility_self_timer_stop_button_txt, null), onScreenButtonListener, false);
            case STOP_RECORDING_SMALL:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.capture_button_video_rec_stop_small, -1, -1, R.string.cam_strings_accessibility_recording_stop_button_txt, null), onScreenButtonListener, false);
            case SIDE_TOUCH_STOP_RECORDING:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.side_touch_capture_button_video_stop, -1, -1, R.string.cam_strings_accessibility_recording_stop_button_txt, null), onScreenButtonListener, false);
            case SIDE_TOUCH_PAUSE_RECORDING:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.side_touch_capture_button_video_pause, -1, -1, R.string.cam_strings_accessibility_pause_button_txt, null), onScreenButtonListener, false);
            case SIDE_TOUCH_START_RECORDING:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.side_touch_capture_button_video_rec, -1, -1, R.string.cam_strings_accessibility_recording_button_txt, null), onScreenButtonListener, false);
            case SIDE_TOUCH_SNAPSHOT_RECORDING:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.side_touch_capture_button_video_snapshot, -1, -1, R.string.cam_strings_accessibility_shutter_button_txt, null), onScreenButtonListener, false);
            case SIDE_TOUCH_STOP_RECORDING_IN_PAUSE:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.side_touch_capture_button_video_stop_paused, -1, -1, R.string.cam_strings_accessibility_recording_stop_button_txt, null), onScreenButtonListener, false);
            case SIDE_TOUCH_RESUME_RECORDING:
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(R.drawable.side_touch_capture_button_video_rec, -1, -1, R.string.cam_strings_accessibility_recording_button_txt, null), onScreenButtonListener, false);
            default:
                throw new IllegalArgumentException("This type is not supported. type:" + buttonType.name());
        }
    }
}
