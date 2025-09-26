// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector;

import android.os.AsyncTask;
import com.sonyericsson.android.camera.view.modeselector.internalmode.googlelens.GoogleLensCapturingModeAttributes;
import com.sonyericsson.android.camera.view.modeselector.internalmode.googlelens.GoogleLensMode;
import android.app.Activity;
import java.util.Iterator;
import android.support.annotation.NonNull;
import java.util.concurrent.Executor;
import java.util.ArrayList;
import com.sonyericsson.android.camera.util.ThreadUtil;
import java.util.List;
import android.content.Context;
import java.util.concurrent.ExecutorService;

public class ModeLoader implements OnCapturingModeListChangedListener
{
    private static final String THREAD_NAME = "AddonAppsLoader";
    private static ExecutorService mExecutor;
    private CapturingModeListLoader mCapturingModeListLoader;
    private final Context mContext;
    private final List<OnModeListChangeListener> mListeners;
    private final List<Mode> mModes;
    
    static {
        ModeLoader.mExecutor = ThreadUtil.buildExecutor("AddonAppsLoader");
    }
    
    public ModeLoader(final Context mContext) {
        this.mModes = new ArrayList<Mode>();
        this.mListeners = new ArrayList<OnModeListChangeListener>();
        this.mContext = mContext;
    }
    
    public static void updatePluginsDatabase(final Context context) {
        new UpdatePluginsDBTask(context).executeOnExecutor((Executor)ModeLoader.mExecutor, (Object[])new Void[0]);
    }
    
    public void addModeChangeListener(final OnModeListChangeListener onModeListChangeListener) {
        if (!this.mListeners.contains(onModeListChangeListener)) {
            this.mListeners.add(onModeListChangeListener);
        }
        this.mModes.clear();
        this.load();
    }
    
    public Mode findById(@NonNull final String s) {
        for (final Mode mode : this.mModes) {
            if (mode == null) {
                continue;
            }
            if (s.equals(mode.getId())) {
                return mode;
            }
        }
        return null;
    }
    
    public void load() {
        this.mCapturingModeListLoader = new CapturingModeListLoader(this.mContext, CapturingModeAttributes.InternalCaptureType.values(), CapturingModeAttributes.VisibilityType.Normal, (CapturingModeListLoader.OnCapturingModeListChangedListener)this, ModeLoader.mExecutor);
        final ArrayList localCapturingMode = new ArrayList();
        for (final ModeSelectorInternalMode modeSelectorInternalMode : ModeSelectorInternalMode.values()) {
            if (modeSelectorInternalMode.isSupported(this.mContext)) {
                localCapturingMode.add(new CapturingModeAttributes(this.mContext.getPackageName(), ((Activity)this.mContext).getLocalClassName(), modeSelectorInternalMode.name(), modeSelectorInternalMode.iconId, modeSelectorInternalMode.textId, -1, -1, -1, CapturingModeAttributes.InternalCaptureType.Photo, true, false, false, modeSelectorInternalMode));
            }
        }
        this.mCapturingModeListLoader.setLocalCapturingMode(localCapturingMode);
        this.mCapturingModeListLoader.startLoadTask();
        CapturingModeUtil.requestRegisterMode(this.mContext);
    }
    
    @Override
    public void onCapturingModeListChanged(final List<CapturingModeAttributes> list) {
        this.mModes.clear();
        final ArrayList list2 = new ArrayList();
        for (CapturingModeAttributes capturingModeAttributes : list) {
            Mode mode;
            if (capturingModeAttributes.getTag() instanceof ModeSelectorInternalMode) {
                mode = new InternalMode(this.mContext, (ModeSelectorInternalMode)capturingModeAttributes.getTag());
            }
            else if (GoogleLensMode.isLensMode(this.mContext, capturingModeAttributes)) {
                capturingModeAttributes = new GoogleLensCapturingModeAttributes(this.mContext, capturingModeAttributes);
                mode = new GoogleLensMode(this.mContext, capturingModeAttributes);
            }
            else {
                mode = new AddonMode(this.mContext, capturingModeAttributes);
            }
            if (mode != null) {
                this.mModes.add(mode);
                list2.add(capturingModeAttributes);
            }
        }
        if (!this.mListeners.isEmpty()) {
            final Iterator<OnModeListChangeListener> iterator2 = this.mListeners.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().onModeListChanged(this.mModes, list2);
            }
        }
    }
    
    public void removeModeChangeListener(final OnModeListChangeListener onModeListChangeListener) {
        this.mListeners.remove(onModeListChangeListener);
    }
    
    public interface OnModeListChangeListener
    {
        void onModeListChanged(final List<Mode> p0, final List<CapturingModeAttributes> p1);
    }
    
    private static class UpdatePluginsDBTask extends AsyncTask<Void, Void, Void>
    {
        private static final String THREAD_NAME = "PluginsDBTask";
        private Context mContext;
        
        public UpdatePluginsDBTask(final Context mContext) {
            this.mContext = mContext;
        }
        
        protected Void doInBackground(final Void... array) {
            Thread.currentThread().setName("PluginsDBTask");
            new CapturingModePluginsPMLoader(this.mContext).updatePluginsInDB();
            return null;
        }
        
        protected void onPostExecute(final Void void1) {
        }
    }
}
