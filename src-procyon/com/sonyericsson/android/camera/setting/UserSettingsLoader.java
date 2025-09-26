// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.setting;

import java.util.Map;
import com.sonyericsson.android.camera.parameter.Parameters;
import com.sonyericsson.android.camera.parameter.ModeIndependentParams;
import com.sonyericsson.android.camera.configuration.Configurations;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import android.content.Context;

public interface UserSettingsLoader
{
    void clearMasterData();
    
    SharedPreferencesAccessor getSharedPreferencesAccessor();
    
    Parameters getUserSettingParameters(final Context p0, final CapturingMode p1, final Storage p2, final Configurations p3, final boolean p4, final ModeIndependentParams p5, final boolean p6);
    
    void load();
    
    void registerLoadCompletedListener(final OnLoadCompletedListener p0);
    
    void release();
    
    void save(final Map<CapturingMode, Parameters> p0, final CapturingMode p1);
    
    void unregisterLoadCompletedListener(final OnLoadCompletedListener p0);
    
    public interface OnLoadCompletedListener
    {
        void onLoadCompleted();
    }
}
