// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.focusview;

import java.util.Collection;
import java.util.Iterator;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.ArrayList;
import java.util.List;

public class FaceInformationList
{
    public static final String TAG = "FaceInformationList";
    private List<NamedFace> mNamedFaceList;
    private boolean mUseSmileGuage;
    private String mUserTouchUuid;
    
    public FaceInformationList() {
        this.mNamedFaceList = new ArrayList<NamedFace>();
        this.mUseSmileGuage = false;
        this.mUserTouchUuid = null;
    }
    
    public static void dumpFaceInformationList(final FaceInformationList list) {
        if (list == null) {
            CamLog.v("dumpFaceInformationList() argument is null");
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("dumpFaceInformationList use smile guage = ");
        sb.append(list.isUseSmileGuage());
        CamLog.v(sb.toString());
        CamLog.v("################");
        for (final NamedFace namedFace : list.getNamedFaceList()) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(" name = ");
            sb2.append(namedFace.mName);
            sb2.append(" UUID = ");
            sb2.append(namedFace.mUuid);
            sb2.append(" position = ");
            sb2.append(namedFace.mFacePosition);
            sb2.append(" smileScore = ");
            sb2.append(namedFace.mSmileScore);
            CamLog.v(sb2.toString());
        }
        CamLog.v("################");
    }
    
    public void addNamedFace(final NamedFace namedFace) {
        this.mNamedFaceList.add(namedFace);
    }
    
    public void addNamedFaceList(final List<NamedFace> list) {
        this.mNamedFaceList.addAll(list);
    }
    
    public void clearNamedFaceList() {
        this.mNamedFaceList.clear();
    }
    
    public NamedFace getNamedFace(final int i) {
        if (this.mNamedFaceList.size() <= i) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getNamedFace index overflow index = ");
            sb.append(i);
            CamLog.e(sb.toString());
            return null;
        }
        return this.mNamedFaceList.get(i);
    }
    
    public NamedFace getNamedFaceByUuid(final String anObject) {
        for (final NamedFace namedFace : this.mNamedFaceList) {
            if (namedFace.mUuid.equals(anObject)) {
                return namedFace;
            }
        }
        return null;
    }
    
    public List<NamedFace> getNamedFaceList() {
        return this.mNamedFaceList;
    }
    
    public String getUserSelectedUuid() {
        return this.mUserTouchUuid;
    }
    
    public boolean isUseSmileGuage() {
        return this.mUseSmileGuage;
    }
    
    public void setNamedFaceList(final List<NamedFace> mNamedFaceList) {
        this.mNamedFaceList = mNamedFaceList;
    }
    
    public void setUseSmileGuage(final boolean mUseSmileGuage) {
        this.mUseSmileGuage = mUseSmileGuage;
    }
    
    public void setUserTouchUuid(final String mUserTouchUuid) {
        this.mUserTouchUuid = mUserTouchUuid;
    }
}
