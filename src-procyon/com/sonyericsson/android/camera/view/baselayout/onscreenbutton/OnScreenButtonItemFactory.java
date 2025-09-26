// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout.onscreenbutton;

import com.sonyericsson.android.camera.view.UserEventHandler;

public class OnScreenButtonItemFactory
{
    public static OnScreenButtonGroup.Item createButton(final ButtonType buttonType, final OnScreenButtonListener onScreenButtonListener) {
        switch (OnScreenButtonItemFactory$1.$SwitchMap$com$sonyericsson$android$camera$view$baselayout$onscreenbutton$OnScreenButtonItemFactory$ButtonType[buttonType.ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("This type is not supported. type:");
                sb.append(buttonType.name());
                throw new IllegalArgumentException(sb.toString());
            }
            case 27: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231561, -1, -1, 2131689599, null), onScreenButtonListener, false);
            }
            case 26: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231564, -1, -1, 2131689602, null), onScreenButtonListener, false);
            }
            case 25: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231562, -1, -1, 2131689604, null), onScreenButtonListener, false);
            }
            case 24: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231561, -1, -1, 2131689599, null), onScreenButtonListener, false);
            }
            case 23: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231560, -1, -1, 2131689595, null), onScreenButtonListener, false);
            }
            case 22: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231563, -1, -1, 2131689602, null), onScreenButtonListener, false);
            }
            case 21: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231446, -1, -1, 2131689602, null), onScreenButtonListener, false);
            }
            case 20: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231565, 2131231565, -1, 2131689603, null), onScreenButtonListener, false);
            }
            case 19: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231529, 2131231530, -1, 2131689603, null), onScreenButtonListener, false);
            }
            case 18: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231534, -1, -1, 2131689604, null), onScreenButtonListener, false);
            }
            case 17: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231532, -1, -1, 2131689604, null), onScreenButtonListener, false);
            }
            case 16: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231432, -1, -1, 2131689604, null), onScreenButtonListener, false);
            }
            case 15: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131230965, -1, -1, 2131689600, null), onScreenButtonListener, false);
            }
            case 14: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131230970, -1, -1, 2131689600, null), onScreenButtonListener, false);
            }
            case 13: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231451, -1, -1, 2131689600, null), onScreenButtonListener, false);
            }
            case 12: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231441, -1, -1, 2131689595, null), onScreenButtonListener, false);
            }
            case 11: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231442, -1, -1, 2131689599, null), onScreenButtonListener, false);
            }
            case 10: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231450, -1, -1, 2131689602, null), onScreenButtonListener, false);
            }
            case 9: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231444, -1, -1, 2131689602, null), onScreenButtonListener, false);
            }
            case 8: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231443, -1, -1, 2131689599, null), onScreenButtonListener, false);
            }
            case 7: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231433, 2131231434, -1, 2131689604, null), onScreenButtonListener, false);
            }
            case 6: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231400, 2131231401, -1, -1, null), onScreenButtonListener, false);
            }
            case 5: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231398, 2131231399, -1, -1, null), onScreenButtonListener, false);
            }
            case 4: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231402, 2131231403, -1, -1, null), onScreenButtonListener, false);
            }
            case 3: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231396, 2131231397, -1, -1, null), onScreenButtonListener, false);
            }
            case 2: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231396, 2131231397, -1, -1, null), onScreenButtonListener, false);
            }
            case 1: {
                return new OnScreenButtonGroup.ImmutableButtonItem(buttonType, new OnScreenButton.Resource(2131231424, -1, -1, 2131689604, null), onScreenButtonListener, false);
            }
        }
    }
    
    public static OnScreenButtonGroup.MutableButtonItem createMutableButton(final OnScreenButtonListener onScreenButtonListener) {
        return new OnScreenButtonGroup.MutableButtonItem(onScreenButtonListener, true);
    }
    
    public enum ButtonType implements TouchEventSource
    {
        private static final ButtonType[] $VALUES;
        
        CANCEL_SELFTIMER_LARGE, 
        CANCEL_SELFTIMER_SIDE, 
        CAPTURE_LARGE, 
        CAPTURE_SMALL, 
        CAPTURE_WITH_SELFTIMER_LARGE, 
        CAPTURE_WITH_SELFTIMER_LONG, 
        CAPTURE_WITH_SELFTIMER_SHORT, 
        PAUSE_RECORDING_SMALL, 
        RESUME_RECORDING_SMALL, 
        SIDE_TOUCH_PAUSE_RECORDING, 
        SIDE_TOUCH_RESUME_RECORDING, 
        SIDE_TOUCH_SNAPSHOT_RECORDING, 
        SIDE_TOUCH_START_RECORDING, 
        SIDE_TOUCH_STOP_RECORDING, 
        SIDE_TOUCH_STOP_RECORDING_IN_PAUSE, 
        START_RECORDING_LARGE, 
        STOP_RECORDING_IN_PAUSE_LARGE, 
        STOP_RECORDING_LARGE, 
        STOP_RECORDING_SMALL, 
        TOUCH_CAPTURE, 
        TOUCH_CAPTURE_WITH_SELFTIMER, 
        TOUCH_CAPTURE_WITH_SELFTIMER_LONG, 
        TOUCH_CAPTURE_WITH_SELFTIMER_SHORT, 
        TOUCH_RECORDING_START, 
        TRIGGER_SUPER_SLOW_MOTION, 
        TRIGGER_SUPER_SLOW_MOTION_DISABLED, 
        TRIGGER_SUPER_SLOW_MOTION_PRESSED;
        
        static {
            $VALUES = new ButtonType[] { ButtonType.CAPTURE_LARGE, ButtonType.CAPTURE_SMALL, ButtonType.CAPTURE_WITH_SELFTIMER_LARGE, ButtonType.CAPTURE_WITH_SELFTIMER_LONG, ButtonType.CAPTURE_WITH_SELFTIMER_SHORT, ButtonType.CANCEL_SELFTIMER_LARGE, ButtonType.CANCEL_SELFTIMER_SIDE, ButtonType.START_RECORDING_LARGE, ButtonType.STOP_RECORDING_IN_PAUSE_LARGE, ButtonType.STOP_RECORDING_LARGE, ButtonType.STOP_RECORDING_SMALL, ButtonType.RESUME_RECORDING_SMALL, ButtonType.PAUSE_RECORDING_SMALL, ButtonType.TRIGGER_SUPER_SLOW_MOTION, ButtonType.TRIGGER_SUPER_SLOW_MOTION_PRESSED, ButtonType.TRIGGER_SUPER_SLOW_MOTION_DISABLED, ButtonType.TOUCH_CAPTURE, ButtonType.TOUCH_CAPTURE_WITH_SELFTIMER, ButtonType.TOUCH_CAPTURE_WITH_SELFTIMER_LONG, ButtonType.TOUCH_CAPTURE_WITH_SELFTIMER_SHORT, ButtonType.TOUCH_RECORDING_START, ButtonType.SIDE_TOUCH_STOP_RECORDING, ButtonType.SIDE_TOUCH_PAUSE_RECORDING, ButtonType.SIDE_TOUCH_START_RECORDING, ButtonType.SIDE_TOUCH_SNAPSHOT_RECORDING, ButtonType.SIDE_TOUCH_STOP_RECORDING_IN_PAUSE, ButtonType.SIDE_TOUCH_RESUME_RECORDING };
        }
    }
}
