// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.sound;

import java.io.File;
import android.support.annotation.NonNull;
import android.media.AudioAttributes$Builder;
import android.media.SoundPool$Builder;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import android.media.SoundPool;
import java.util.Map;
import android.media.SoundPool$OnLoadCompleteListener;
import android.content.Context;

public class SoundPlayer
{
    private static final int ID_NOT_LOADED = 0;
    private static final int ID_NOT_PLAYED = 0;
    private static final int NUM_SOUND_STREAMS = 1;
    private static final int SOUND_POOL_LOAD_PRIORITY = 1;
    private Context mApplicationContext;
    private SoundPool$OnLoadCompleteListener mLoadCompleteListener;
    private int mSoundIDPlayed;
    private int mSoundIDToPlay;
    private final Map<Type, SoundLoad> mSoundMap;
    private SoundPool mSoundPool;
    
    public SoundPlayer(final Context mApplicationContext) {
        this.mSoundIDToPlay = 0;
        this.mSoundIDPlayed = 0;
        this.mSoundMap = new ConcurrentHashMap<Type, SoundLoad>();
        this.mLoadCompleteListener = (SoundPool$OnLoadCompleteListener)new SoundPool$OnLoadCompleteListener() {
            final SoundPlayer this$0;
            
            public void onLoadComplete(final SoundPool soundPool, final int n, final int n2) {
                if (this.this$0.mSoundPool == null) {
                    return;
                }
                if (n2 != 0) {
                    for (final Type type : this.this$0.mSoundMap.keySet()) {
                        if (((SoundLoad)this.this$0.mSoundMap.get(type)).soundID == n) {
                            ((SoundLoad)this.this$0.mSoundMap.get(type)).soundID = 0;
                            break;
                        }
                    }
                    return;
                }
                for (final Type type2 : this.this$0.mSoundMap.keySet()) {
                    if (((SoundLoad)this.this$0.mSoundMap.get(type2)).soundID == n) {
                        ((SoundLoad)this.this$0.mSoundMap.get(type2)).isLoaded = true;
                        break;
                    }
                }
                if (n == this.this$0.mSoundIDToPlay) {
                    this.this$0.mSoundIDToPlay = 0;
                    this.this$0.mSoundIDPlayed = this.this$0.mSoundPool.play(n, 1.0f, 1.0f, 0, 0, 1.0f);
                }
            }
        };
        this.mApplicationContext = mApplicationContext;
        (this.mSoundPool = new SoundPool$Builder().setMaxStreams(1).setAudioAttributes(new AudioAttributes$Builder().setUsage(13).setFlags(1).setContentType(4).build()).build()).setOnLoadCompleteListener(this.mLoadCompleteListener);
        for (final Type type : Type.values()) {
            final String access$000 = type.getSoundFile();
            int n;
            if (access$000 != null) {
                n = this.mSoundPool.load(access$000, 1);
            }
            else {
                n = this.mSoundPool.load(this.mApplicationContext, type.resourceId, 1);
            }
            this.mSoundMap.put(type, new SoundLoad(n, false));
        }
    }
    
    public void play(@NonNull final Type type) {
        synchronized (this) {
            if (this.mSoundPool == null) {
                return;
            }
            if (this.mSoundMap.get(type).soundID == 0) {
                final String access$000 = type.getSoundFile();
                if (access$000 != null) {
                    this.mSoundMap.get(type).soundID = this.mSoundPool.load(access$000, 1);
                }
                else {
                    this.mSoundMap.get(type).soundID = this.mSoundPool.load(this.mApplicationContext, type.resourceId, 1);
                }
                this.mSoundIDToPlay = this.mSoundMap.get(type).soundID;
                this.mSoundIDPlayed = 0;
            }
            else if (!this.mSoundMap.get(type).isLoaded) {
                this.mSoundIDToPlay = this.mSoundMap.get(type).soundID;
                this.mSoundIDPlayed = 0;
            }
            else {
                this.mSoundIDPlayed = this.mSoundPool.play(this.mSoundMap.get(type).soundID, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        }
    }
    
    public void release() {
        synchronized (this) {
            if (this.mSoundPool != null) {
                this.mSoundMap.clear();
                this.mSoundPool.release();
                this.mSoundPool = null;
            }
        }
    }
    
    public void stop() {
        synchronized (this) {
            if (this.mSoundPool != null && this.mSoundIDPlayed != 0) {
                this.mSoundPool.stop(this.mSoundIDPlayed);
                this.mSoundIDPlayed = 0;
            }
        }
    }
    
    private static class SoundLoad
    {
        public boolean isLoaded;
        public int soundID;
        
        public SoundLoad(final int soundID, final boolean isLoaded) {
            this.soundID = soundID;
            this.isLoaded = isLoaded;
        }
    }
    
    public enum Type
    {
        private static final Type[] $VALUES;
        
        SELF_TIMER_1SEC("selftimer_1sec.m4a", 2131623956), 
        SELF_TIMER_3SEC("selftimer_3sec.m4a", 2131623957), 
        SELF_TIMER_4SEC("selftimer_4sec.m4a", 2131623958);
        
        private final String[] SOUND_DIRS;
        private final int resourceId;
        private final String soundName;
        
        static {
            $VALUES = new Type[] { Type.SELF_TIMER_1SEC, Type.SELF_TIMER_3SEC, Type.SELF_TIMER_4SEC };
        }
        
        private Type(final String soundName, final int resourceId) {
            this.SOUND_DIRS = new String[] { "/system/media/audio/ui/common/" };
            this.soundName = soundName;
            this.resourceId = resourceId;
        }
        
        private String getSoundFile() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.SOUND_DIRS[0]);
            sb.append(this.soundName);
            if (new File(sb.toString()).exists()) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(this.SOUND_DIRS[0]);
                sb2.append(this.soundName);
                return sb2.toString();
            }
            return null;
        }
    }
}
