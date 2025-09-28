package com.sonyericsson.android.camera.setting;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public interface MessageSettings {
    void clearSavedMessageSettings();

    int getDisplayCount(MessageType messageType);

    boolean isNeverShow(MessageType messageType);

    void save();

    void setDisplayCount(MessageType messageType, int i);

    void setNeverShow(MessageType messageType, boolean z);
}
