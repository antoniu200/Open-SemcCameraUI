package com.sonyericsson.cameracommon.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.support.annotation.NonNull;
import com.sonyericsson.android.camera.R;
import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SoundPlayer {
    private static final int ID_NOT_LOADED = 0;
    private static final int ID_NOT_PLAYED = 0;
    private static final int NUM_SOUND_STREAMS = 1;
    private static final int SOUND_POOL_LOAD_PRIORITY = 1;
    private Context mApplicationContext;
    private int mSoundIDToPlay = 0;
    private int mSoundIDPlayed = 0;
    private final Map<Type, SoundLoad> mSoundMap = new ConcurrentHashMap();
    private SoundPool.OnLoadCompleteListener mLoadCompleteListener = new SoundPool.OnLoadCompleteListener() { // from class: com.sonyericsson.cameracommon.sound.SoundPlayer.1
        @Override // android.media.SoundPool.OnLoadCompleteListener
        public void onLoadComplete(SoundPool soundPool, int i, int i2) {
            if (SoundPlayer.this.mSoundPool == null) {
                return;
            }
            if (i2 != 0) {
                for (Type type : SoundPlayer.this.mSoundMap.keySet()) {
                    if (((SoundLoad) SoundPlayer.this.mSoundMap.get(type)).soundID == i) {
                        ((SoundLoad) SoundPlayer.this.mSoundMap.get(type)).soundID = 0;
                        return;
                    }
                }
                return;
            }
            Iterator it = SoundPlayer.this.mSoundMap.keySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Type type2 = (Type) it.next();
                if (((SoundLoad) SoundPlayer.this.mSoundMap.get(type2)).soundID == i) {
                    ((SoundLoad) SoundPlayer.this.mSoundMap.get(type2)).isLoaded = true;
                    break;
                }
            }
            if (i == SoundPlayer.this.mSoundIDToPlay) {
                SoundPlayer.this.mSoundIDToPlay = 0;
                SoundPlayer.this.mSoundIDPlayed = SoundPlayer.this.mSoundPool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        }
    };
    private SoundPool mSoundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(new AudioAttributes.Builder().setUsage(13).setFlags(1).setContentType(4).build()).build();

    public SoundPlayer(Context context) {
        int iLoad;
        this.mApplicationContext = context;
        this.mSoundPool.setOnLoadCompleteListener(this.mLoadCompleteListener);
        for (Type type : Type.values()) {
            String soundFile = type.getSoundFile();
            if (soundFile != null) {
                iLoad = this.mSoundPool.load(soundFile, 1);
            } else {
                iLoad = this.mSoundPool.load(this.mApplicationContext, type.resourceId, 1);
            }
            this.mSoundMap.put(type, new SoundLoad(iLoad, false));
        }
    }

    public synchronized void play(@NonNull Type type) {
        if (this.mSoundPool == null) {
            return;
        }
        if (this.mSoundMap.get(type).soundID != 0) {
            if (!this.mSoundMap.get(type).isLoaded) {
                this.mSoundIDToPlay = this.mSoundMap.get(type).soundID;
                this.mSoundIDPlayed = 0;
            } else {
                this.mSoundIDPlayed = this.mSoundPool.play(this.mSoundMap.get(type).soundID, 1.0f, 1.0f, 0, 0, 1.0f);
            }
        } else {
            String soundFile = type.getSoundFile();
            if (soundFile != null) {
                this.mSoundMap.get(type).soundID = this.mSoundPool.load(soundFile, 1);
            } else {
                this.mSoundMap.get(type).soundID = this.mSoundPool.load(this.mApplicationContext, type.resourceId, 1);
            }
            this.mSoundIDToPlay = this.mSoundMap.get(type).soundID;
            this.mSoundIDPlayed = 0;
        }
    }

    public synchronized void stop() {
        if (this.mSoundPool != null && this.mSoundIDPlayed != 0) {
            this.mSoundPool.stop(this.mSoundIDPlayed);
            this.mSoundIDPlayed = 0;
        }
    }

    public synchronized void release() {
        if (this.mSoundPool != null) {
            this.mSoundMap.clear();
            this.mSoundPool.release();
            this.mSoundPool = null;
        }
    }

    private static class SoundLoad {
        public boolean isLoaded;
        public int soundID;

        public SoundLoad(int i, boolean z) {
            this.soundID = i;
            this.isLoaded = z;
        }
    }

    public enum Type {
        SELF_TIMER_1SEC("selftimer_1sec.m4a", R.raw.selftimer_1sec),
        SELF_TIMER_3SEC("selftimer_3sec.m4a", R.raw.selftimer_3sec),
        SELF_TIMER_4SEC("selftimer_4sec.m4a", R.raw.selftimer_4sec);

        private final String[] SOUND_DIRS = {"/system/media/audio/ui/common/"};
        private final int resourceId;
        private final String soundName;

        Type(String str, int i) {
            this.soundName = str;
            this.resourceId = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String getSoundFile() {
            if (!new File(this.SOUND_DIRS[0] + this.soundName).exists()) {
                return null;
            }
            return this.SOUND_DIRS[0] + this.soundName;
        }
    }
}
