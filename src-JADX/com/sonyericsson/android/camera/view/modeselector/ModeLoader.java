package com.sonyericsson.android.camera.view.modeselector;

import android.app.Activity;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.android.camera.view.modeselector.CapturingModeAttributes;
import com.sonyericsson.android.camera.view.modeselector.CapturingModeListLoader;
import com.sonyericsson.android.camera.view.modeselector.internalmode.googlelens.GoogleLensCapturingModeAttributes;
import com.sonyericsson.android.camera.view.modeselector.internalmode.googlelens.GoogleLensMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ModeLoader implements CapturingModeListLoader.OnCapturingModeListChangedListener {
    private static final String THREAD_NAME = "AddonAppsLoader";
    private static ExecutorService mExecutor = ThreadUtil.buildExecutor(THREAD_NAME);
    private CapturingModeListLoader mCapturingModeListLoader;
    private final Context mContext;
    private final List<Mode> mModes = new ArrayList();
    private final List<OnModeListChangeListener> mListeners = new ArrayList();

    public interface OnModeListChangeListener {
        void onModeListChanged(List<Mode> list, List<CapturingModeAttributes> list2);
    }

    public ModeLoader(Context context) {
        this.mContext = context;
    }

    public void addModeChangeListener(OnModeListChangeListener onModeListChangeListener) {
        if (!this.mListeners.contains(onModeListChangeListener)) {
            this.mListeners.add(onModeListChangeListener);
        }
        this.mModes.clear();
        load();
    }

    public void removeModeChangeListener(OnModeListChangeListener onModeListChangeListener) {
        this.mListeners.remove(onModeListChangeListener);
    }

    @Override // com.sonyericsson.android.camera.view.modeselector.CapturingModeListLoader.OnCapturingModeListChangedListener
    public void onCapturingModeListChanged(List<CapturingModeAttributes> list) {
        Mode addonMode;
        this.mModes.clear();
        ArrayList arrayList = new ArrayList();
        for (CapturingModeAttributes capturingModeAttributes : list) {
            if (capturingModeAttributes.getTag() instanceof ModeSelectorInternalMode) {
                addonMode = new InternalMode(this.mContext, (ModeSelectorInternalMode) capturingModeAttributes.getTag());
            } else if (GoogleLensMode.isLensMode(this.mContext, capturingModeAttributes)) {
                GoogleLensCapturingModeAttributes googleLensCapturingModeAttributes = new GoogleLensCapturingModeAttributes(this.mContext, capturingModeAttributes);
                addonMode = new GoogleLensMode(this.mContext, googleLensCapturingModeAttributes);
                capturingModeAttributes = googleLensCapturingModeAttributes;
            } else {
                addonMode = new AddonMode(this.mContext, capturingModeAttributes);
            }
            if (addonMode != null) {
                this.mModes.add(addonMode);
                arrayList.add(capturingModeAttributes);
            }
        }
        if (this.mListeners.isEmpty()) {
            return;
        }
        Iterator<OnModeListChangeListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onModeListChanged(this.mModes, arrayList);
        }
    }

    public void load() {
        this.mCapturingModeListLoader = new CapturingModeListLoader(this.mContext, CapturingModeAttributes.InternalCaptureType.values(), CapturingModeAttributes.VisibilityType.Normal, this, mExecutor);
        ArrayList arrayList = new ArrayList();
        for (ModeSelectorInternalMode modeSelectorInternalMode : ModeSelectorInternalMode.values()) {
            if (modeSelectorInternalMode.isSupported(this.mContext)) {
                arrayList.add(new CapturingModeAttributes(this.mContext.getPackageName(), ((Activity) this.mContext).getLocalClassName(), modeSelectorInternalMode.name(), modeSelectorInternalMode.iconId, modeSelectorInternalMode.textId, -1, -1, -1, CapturingModeAttributes.InternalCaptureType.Photo, true, false, false, (Object) modeSelectorInternalMode));
            }
        }
        this.mCapturingModeListLoader.setLocalCapturingMode(arrayList);
        this.mCapturingModeListLoader.startLoadTask();
        CapturingModeUtil.requestRegisterMode(this.mContext);
    }

    public Mode findById(@NonNull String str) {
        for (Mode mode : this.mModes) {
            if (mode != null && str.equals(mode.getId())) {
                return mode;
            }
        }
        return null;
    }

    public static void updatePluginsDatabase(Context context) {
        new UpdatePluginsDBTask(context).executeOnExecutor(mExecutor, new Void[0]);
    }

    private static class UpdatePluginsDBTask extends AsyncTask<Void, Void, Void> {
        private static final String THREAD_NAME = "PluginsDBTask";
        private Context mContext;

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Void r1) {
        }

        public UpdatePluginsDBTask(Context context) {
            this.mContext = context;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... voidArr) throws Resources.NotFoundException, PackageManager.NameNotFoundException, RemoteException, OperationApplicationException {
            Thread.currentThread().setName(THREAD_NAME);
            new CapturingModePluginsPMLoader(this.mContext).updatePluginsInDB();
            return null;
        }
    }
}
