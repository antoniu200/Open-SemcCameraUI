// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.media;

import android.support.annotation.NonNull;
import android.os.Bundle;
import androidx.versionedparcelable.VersionedParcelable;

interface AudioAttributesImpl extends VersionedParcelable
{
    Object getAudioAttributes();
    
    int getContentType();
    
    int getFlags();
    
    int getLegacyStreamType();
    
    int getRawLegacyStreamType();
    
    int getUsage();
    
    int getVolumeControlStream();
    
    @NonNull
    Bundle toBundle();
}
