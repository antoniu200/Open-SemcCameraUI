package com.sonyericsson.cameracommon.keytranslator;

import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.VolumeKey;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.util.CamLog;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class KeyEventTranslator {
    public static final String TAG = "KeyEventTranslator";
    private KeyType mCurrentKeyType = KeyType.NON;
    private final UserSettings mSetting;

    private enum KeyAction {
        DOWN,
        UP,
        LONG_PRESS
    }

    private enum KeyType {
        NON,
        CAMERA_KEY,
        VOLUME_UP_KEY,
        VOLUME_DOWN_KEY
    }

    public enum TranslatedKeyCode {
        NON,
        ZOOM,
        VOLUME,
        FOCUS,
        SHUTTER,
        FOCUS_AND_SHUTTER_UP_KEY,
        FOCUS_AND_SHUTTER_DOWN_KEY,
        BACK,
        MENU,
        IGNORED,
        ENTER
    }

    public KeyEventTranslator(UserSettings userSettings) {
        this.mSetting = userSettings;
    }

    public void reset() {
        this.mCurrentKeyType = KeyType.NON;
    }

    public TranslatedKeyCode translateKeyCode(int i) {
        if (i == 4) {
            return TranslatedKeyCode.BACK;
        }
        if (i == 27) {
            return TranslatedKeyCode.SHUTTER;
        }
        if (i != 66) {
            if (i == 80) {
                return TranslatedKeyCode.FOCUS;
            }
            if (i != 82) {
                switch (i) {
                    case 23:
                        break;
                    case 24:
                    case 25:
                        VolumeKey volumeKey = (VolumeKey) this.mSetting.get(UserSettingKey.VOLUME_KEY);
                        if (volumeKey == null) {
                            return TranslatedKeyCode.ZOOM;
                        }
                        switch (volumeKey) {
                            case ZOOM:
                                return TranslatedKeyCode.ZOOM;
                            case VOLUME:
                                return TranslatedKeyCode.VOLUME;
                            case HW_CAMERA_KEY:
                                if (i == 24) {
                                    return TranslatedKeyCode.FOCUS_AND_SHUTTER_UP_KEY;
                                }
                                return TranslatedKeyCode.FOCUS_AND_SHUTTER_DOWN_KEY;
                            default:
                                CamLog.e("Volume key parameter is invalid state.");
                                return TranslatedKeyCode.ZOOM;
                        }
                    default:
                        return TranslatedKeyCode.NON;
                }
            } else {
                return TranslatedKeyCode.MENU;
            }
        }
        return TranslatedKeyCode.FOCUS_AND_SHUTTER_UP_KEY;
    }

    public TranslatedKeyCode translateKeyCodeOnDown(int i) {
        TranslatedKeyCode translatedKeyCodeTranslateKeyCode = translateKeyCode(i);
        return !isAvailableNow(translatedKeyCodeTranslateKeyCode, KeyAction.DOWN) ? TranslatedKeyCode.IGNORED : translatedKeyCodeTranslateKeyCode;
    }

    public TranslatedKeyCode translateKeyCodeOnUp(int i) {
        TranslatedKeyCode translatedKeyCodeTranslateKeyCode = translateKeyCode(i);
        return !isAvailableNow(translatedKeyCodeTranslateKeyCode, KeyAction.UP) ? TranslatedKeyCode.IGNORED : translatedKeyCodeTranslateKeyCode;
    }

    public TranslatedKeyCode translateKeyCodeOnLongPress(int i) {
        TranslatedKeyCode translatedKeyCodeTranslateKeyCode = translateKeyCode(i);
        return !isAvailableNow(translatedKeyCodeTranslateKeyCode, KeyAction.LONG_PRESS) ? TranslatedKeyCode.IGNORED : translatedKeyCodeTranslateKeyCode;
    }

    private boolean isAvailableNow(TranslatedKeyCode translatedKeyCode, KeyAction keyAction) {
        switch (translatedKeyCode) {
            case FOCUS:
                return isExpectedKeyType(keyAction, KeyType.NON, KeyType.CAMERA_KEY, KeyType.CAMERA_KEY, KeyType.NON);
            case SHUTTER:
                return isExpectedKeyType(keyAction, KeyType.CAMERA_KEY, KeyType.CAMERA_KEY, KeyType.CAMERA_KEY, KeyType.CAMERA_KEY);
            case FOCUS_AND_SHUTTER_UP_KEY:
                return isExpectedKeyType(keyAction, KeyType.NON, KeyType.VOLUME_UP_KEY, KeyType.VOLUME_UP_KEY, KeyType.NON);
            case FOCUS_AND_SHUTTER_DOWN_KEY:
                return isExpectedKeyType(keyAction, KeyType.NON, KeyType.VOLUME_DOWN_KEY, KeyType.VOLUME_DOWN_KEY, KeyType.NON);
            default:
                return true;
        }
    }

    private boolean isExpectedKeyType(KeyAction keyAction, KeyType keyType, KeyType keyType2, KeyType keyType3, KeyType keyType4) {
        switch (keyAction) {
            case DOWN:
                if (this.mCurrentKeyType != keyType) {
                    return false;
                }
                this.mCurrentKeyType = keyType2;
                return true;
            case UP:
                if (this.mCurrentKeyType != keyType3) {
                    return false;
                }
                this.mCurrentKeyType = keyType4;
                return true;
            case LONG_PRESS:
                return this.mCurrentKeyType == keyType3;
            default:
                return false;
        }
    }
}
