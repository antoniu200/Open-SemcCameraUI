package com.sonyericsson.cameracommon.focusview;

import com.sonyericsson.android.camera.util.CamLog;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class FaceInformationList {
    public static final String TAG = "FaceInformationList";
    private List<NamedFace> mNamedFaceList = new ArrayList();
    private boolean mUseSmileGuage = false;
    private String mUserTouchUuid = null;

    public void setUserTouchUuid(String str) {
        this.mUserTouchUuid = str;
    }

    public void addNamedFace(NamedFace namedFace) {
        this.mNamedFaceList.add(namedFace);
    }

    public void addNamedFaceList(List<NamedFace> list) {
        this.mNamedFaceList.addAll(list);
    }

    public void setNamedFaceList(List<NamedFace> list) {
        this.mNamedFaceList = list;
    }

    public List<NamedFace> getNamedFaceList() {
        return this.mNamedFaceList;
    }

    public NamedFace getNamedFace(int i) {
        if (this.mNamedFaceList.size() <= i) {
            CamLog.e("getNamedFace index overflow index = " + i);
            return null;
        }
        return this.mNamedFaceList.get(i);
    }

    public void clearNamedFaceList() {
        this.mNamedFaceList.clear();
    }

    public void setUseSmileGuage(boolean z) {
        this.mUseSmileGuage = z;
    }

    public boolean isUseSmileGuage() {
        return this.mUseSmileGuage;
    }

    public String getUserSelectedUuid() {
        return this.mUserTouchUuid;
    }

    public NamedFace getNamedFaceByUuid(String str) {
        for (NamedFace namedFace : this.mNamedFaceList) {
            if (namedFace.mUuid.equals(str)) {
                return namedFace;
            }
        }
        return null;
    }

    public static void dumpFaceInformationList(FaceInformationList faceInformationList) {
        if (faceInformationList == null) {
            CamLog.v("dumpFaceInformationList() argument is null");
            return;
        }
        CamLog.v("dumpFaceInformationList use smile guage = " + faceInformationList.isUseSmileGuage());
        CamLog.v("################");
        for (NamedFace namedFace : faceInformationList.getNamedFaceList()) {
            CamLog.v(" name = " + namedFace.mName + " UUID = " + namedFace.mUuid + " position = " + namedFace.mFacePosition + " smileScore = " + namedFace.mSmileScore);
        }
        CamLog.v("################");
    }
}
