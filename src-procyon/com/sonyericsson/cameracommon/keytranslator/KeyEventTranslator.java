// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.keytranslator;

import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.VolumeKey;
import com.sonyericsson.android.camera.setting.UserSettings;

public class KeyEventTranslator
{
    public static final String TAG = "KeyEventTranslator";
    private KeyType mCurrentKeyType;
    private final UserSettings mSetting;
    
    public KeyEventTranslator(final UserSettings mSetting) {
        this.mCurrentKeyType = KeyType.NON;
        this.mSetting = mSetting;
    }
    
    private boolean isAvailableNow(final TranslatedKeyCode translatedKeyCode, final KeyAction keyAction) {
        boolean b = false;
        switch (KeyEventTranslator$1.$SwitchMap$com$sonyericsson$cameracommon$keytranslator$KeyEventTranslator$TranslatedKeyCode[translatedKeyCode.ordinal()]) {
            default: {
                b = true;
                break;
            }
            case 4: {
                b = this.isExpectedKeyType(keyAction, KeyType.NON, KeyType.VOLUME_DOWN_KEY, KeyType.VOLUME_DOWN_KEY, KeyType.NON);
                break;
            }
            case 3: {
                b = this.isExpectedKeyType(keyAction, KeyType.NON, KeyType.VOLUME_UP_KEY, KeyType.VOLUME_UP_KEY, KeyType.NON);
                break;
            }
            case 2: {
                b = this.isExpectedKeyType(keyAction, KeyType.CAMERA_KEY, KeyType.CAMERA_KEY, KeyType.CAMERA_KEY, KeyType.CAMERA_KEY);
                break;
            }
            case 1: {
                b = this.isExpectedKeyType(keyAction, KeyType.NON, KeyType.CAMERA_KEY, KeyType.CAMERA_KEY, KeyType.NON);
                break;
            }
        }
        return b;
    }
    
    private boolean isExpectedKeyType(final KeyAction keyAction, final KeyType keyType, final KeyType mCurrentKeyType, final KeyType keyType2, final KeyType mCurrentKeyType2) {
        switch (KeyEventTranslator$1.$SwitchMap$com$sonyericsson$cameracommon$keytranslator$KeyEventTranslator$KeyAction[keyAction.ordinal()]) {
            default: {
                return false;
            }
            case 3: {
                return this.mCurrentKeyType == keyType2;
            }
            case 2: {
                if (this.mCurrentKeyType == keyType2) {
                    this.mCurrentKeyType = mCurrentKeyType2;
                    return true;
                }
                return false;
            }
            case 1: {
                if (this.mCurrentKeyType == keyType) {
                    this.mCurrentKeyType = mCurrentKeyType;
                    return true;
                }
                return false;
            }
        }
    }
    
    public void reset() {
        this.mCurrentKeyType = KeyType.NON;
    }
    
    public TranslatedKeyCode translateKeyCode(final int n) {
        TranslatedKeyCode translatedKeyCode;
        if (n != 4) {
            if (n != 27) {
                if (n != 66) {
                    if (n == 80) {
                        translatedKeyCode = TranslatedKeyCode.FOCUS;
                        return translatedKeyCode;
                    }
                    if (n == 82) {
                        translatedKeyCode = TranslatedKeyCode.MENU;
                        return translatedKeyCode;
                    }
                    switch (n) {
                        default: {
                            translatedKeyCode = TranslatedKeyCode.NON;
                            return translatedKeyCode;
                        }
                        case 24:
                        case 25: {
                            final VolumeKey volumeKey = (VolumeKey)this.mSetting.get(UserSettingKey.VOLUME_KEY);
                            if (volumeKey == null) {
                                translatedKeyCode = TranslatedKeyCode.ZOOM;
                                return translatedKeyCode;
                            }
                            switch (KeyEventTranslator$1.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$VolumeKey[volumeKey.ordinal()]) {
                                default: {
                                    CamLog.e("Volume key parameter is invalid state.");
                                    translatedKeyCode = TranslatedKeyCode.ZOOM;
                                    return translatedKeyCode;
                                }
                                case 3: {
                                    if (n == 24) {
                                        translatedKeyCode = TranslatedKeyCode.FOCUS_AND_SHUTTER_UP_KEY;
                                        return translatedKeyCode;
                                    }
                                    translatedKeyCode = TranslatedKeyCode.FOCUS_AND_SHUTTER_DOWN_KEY;
                                    return translatedKeyCode;
                                }
                                case 2: {
                                    translatedKeyCode = TranslatedKeyCode.VOLUME;
                                    return translatedKeyCode;
                                }
                                case 1: {
                                    translatedKeyCode = TranslatedKeyCode.ZOOM;
                                    return translatedKeyCode;
                                }
                            }
                            break;
                        }
                        case 23: {
                            break;
                        }
                    }
                }
                translatedKeyCode = TranslatedKeyCode.FOCUS_AND_SHUTTER_UP_KEY;
            }
            else {
                translatedKeyCode = TranslatedKeyCode.SHUTTER;
            }
        }
        else {
            translatedKeyCode = TranslatedKeyCode.BACK;
        }
        return translatedKeyCode;
    }
    
    public TranslatedKeyCode translateKeyCodeOnDown(final int n) {
        TranslatedKeyCode translatedKeyCode;
        if (!this.isAvailableNow(translatedKeyCode = this.translateKeyCode(n), KeyAction.DOWN)) {
            translatedKeyCode = TranslatedKeyCode.IGNORED;
        }
        return translatedKeyCode;
    }
    
    public TranslatedKeyCode translateKeyCodeOnLongPress(final int n) {
        TranslatedKeyCode translatedKeyCode;
        if (!this.isAvailableNow(translatedKeyCode = this.translateKeyCode(n), KeyAction.LONG_PRESS)) {
            translatedKeyCode = TranslatedKeyCode.IGNORED;
        }
        return translatedKeyCode;
    }
    
    public TranslatedKeyCode translateKeyCodeOnUp(final int n) {
        TranslatedKeyCode translatedKeyCode;
        if (!this.isAvailableNow(translatedKeyCode = this.translateKeyCode(n), KeyAction.UP)) {
            translatedKeyCode = TranslatedKeyCode.IGNORED;
        }
        return translatedKeyCode;
    }
    
    private enum KeyAction
    {
        private static final KeyAction[] $VALUES;
        
        DOWN, 
        LONG_PRESS, 
        UP;
        
        static {
            $VALUES = new KeyAction[] { KeyAction.DOWN, KeyAction.UP, KeyAction.LONG_PRESS };
        }
    }
    
    private enum KeyType
    {
        private static final KeyType[] $VALUES;
        
        CAMERA_KEY, 
        NON, 
        VOLUME_DOWN_KEY, 
        VOLUME_UP_KEY;
        
        static {
            $VALUES = new KeyType[] { KeyType.NON, KeyType.CAMERA_KEY, KeyType.VOLUME_UP_KEY, KeyType.VOLUME_DOWN_KEY };
        }
    }
    
    public enum TranslatedKeyCode
    {
        private static final TranslatedKeyCode[] $VALUES;
        
        BACK, 
        ENTER, 
        FOCUS, 
        FOCUS_AND_SHUTTER_DOWN_KEY, 
        FOCUS_AND_SHUTTER_UP_KEY, 
        IGNORED, 
        MENU, 
        NON, 
        SHUTTER, 
        VOLUME, 
        ZOOM;
        
        static {
            $VALUES = new TranslatedKeyCode[] { TranslatedKeyCode.NON, TranslatedKeyCode.ZOOM, TranslatedKeyCode.VOLUME, TranslatedKeyCode.FOCUS, TranslatedKeyCode.SHUTTER, TranslatedKeyCode.FOCUS_AND_SHUTTER_UP_KEY, TranslatedKeyCode.FOCUS_AND_SHUTTER_DOWN_KEY, TranslatedKeyCode.BACK, TranslatedKeyCode.MENU, TranslatedKeyCode.IGNORED, TranslatedKeyCode.ENTER };
        }
    }
}
