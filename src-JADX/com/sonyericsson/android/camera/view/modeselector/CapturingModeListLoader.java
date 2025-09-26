package com.sonyericsson.android.camera.view.modeselector;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.os.AsyncTask;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.modeselector.CapturingModeAttributes;
import com.sonyericsson.android.camera.view.modeselector.internalmode.googlelens.GoogleLensMode;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CapturingModeListLoader {
    private static final String TAG = "CapturingModeListLoader";
    private final CapturingModeAttributes.InternalCaptureType[] mCaptureTypeList;
    private final CapturingModeCollection mCapturingModeCollection;
    private final ContentResolver mContentResolver;
    private final Context mContext;
    private final ExecutorService mExecutor;
    private List<CapturingModeAttributes> mLocalModeAttrsList;
    private OnCapturingModeListChangedListener mOnModeListChangedListener;
    private final CapturingModeAttributes.VisibilityType mVisibilityType;
    private List<CapturingModeAttributes> mModeAttrsList = new ArrayList();
    private final ContentObserver mContentObserver = new ContentObserver(null) { // from class: com.sonyericsson.android.camera.view.modeselector.CapturingModeListLoader.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            if (CamLog.VERBOSE) {
                CamLog.d("CAPTURINGMODE_CONTENT_URI has been changed.");
            }
            CapturingModeListLoader.this.startLoadTask();
        }
    };

    public interface OnCapturingModeListChangedListener {
        void onCapturingModeListChanged(List<CapturingModeAttributes> list);
    }

    private enum CapturingModeId {
        DUAL_BACKGROUND_DEFOCUS("DUAL_BACKGROUND_DEFOCUS"),
        DUAL_MONOCHROME("DUAL_MONOCHROME"),
        PORTRAIT_SELFIE("PORTRAIT_SELFIE"),
        GOOGLE_LENS(GoogleLensMode.MODE_NAME),
        BOKEH("BackgroundDefocus"),
        SLOW_MOTION("SLOW_MOTION"),
        AR_EFFECT("AR Effect"),
        MANUAL("MANUAL"),
        CREATIVE_EFFECT("capturing_mode_single_effect"),
        PANORAMA("capturing_mode_sweep_panorama"),
        SOUND_PHOTO("capturing_mode_soundphoto");

        private final String name;

        CapturingModeId(String str) {
            this.name = str;
        }

        public String getName() {
            return this.name;
        }
    }

    public CapturingModeListLoader(Context context, CapturingModeAttributes.InternalCaptureType[] internalCaptureTypeArr, CapturingModeAttributes.VisibilityType visibilityType, OnCapturingModeListChangedListener onCapturingModeListChangedListener, ExecutorService executorService) {
        this.mContext = context;
        this.mCaptureTypeList = (CapturingModeAttributes.InternalCaptureType[]) internalCaptureTypeArr.clone();
        this.mVisibilityType = visibilityType;
        this.mOnModeListChangedListener = onCapturingModeListChangedListener;
        this.mExecutor = executorService;
        this.mContentResolver = context.getContentResolver();
        this.mContentResolver.registerContentObserver(CameraCommonProviderConstants.CAPTURINGMODE_CONTENT_URI, true, this.mContentObserver);
        this.mCapturingModeCollection = new CapturingModeCollection(this.mContentResolver);
    }

    public void startLoadTask() {
        if (CamLog.VERBOSE) {
            CamLog.d("start GetTask.");
        }
        new GetCapturingModeListTask(this).executeOnExecutor(this.mExecutor, new Void[0]);
    }

    public void release() {
        this.mContentResolver.unregisterContentObserver(this.mContentObserver);
        this.mCapturingModeCollection.release();
        this.mExecutor.shutdown();
    }

    public CapturingModeAttributes getCapturingMode(String str, String str2) {
        for (CapturingModeAttributes capturingModeAttributes : this.mModeAttrsList) {
            if (capturingModeAttributes != null && str.equals(capturingModeAttributes.getPackageName()) && str2.equals(capturingModeAttributes.getModeName())) {
                return capturingModeAttributes;
            }
        }
        return null;
    }

    private static class GetCapturingModeListTask extends AsyncTask<Void, Void, List<CapturingModeAttributes>> {
        private static final String THREAD_NAME = "GetModeListTask";
        private final WeakReference<CapturingModeListLoader> mCapturingModeListLoaderRef;

        GetCapturingModeListTask(CapturingModeListLoader capturingModeListLoader) {
            this.mCapturingModeListLoaderRef = new WeakReference<>(capturingModeListLoader);
        }

        @Override // android.os.AsyncTask
        public List<CapturingModeAttributes> doInBackground(Void... voidArr) {
            Thread.currentThread().setName(THREAD_NAME);
            CapturingModeListLoader capturingModeListLoader = this.mCapturingModeListLoaderRef.get();
            if (capturingModeListLoader == null) {
                return null;
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            List<CapturingModeAttributes> capturingModeList = capturingModeListLoader.mCapturingModeCollection.getCapturingModeList(capturingModeListLoader.mCaptureTypeList, new CapturingModeAttributes.VisibilityType[]{capturingModeListLoader.mVisibilityType});
            CamLog.i("Mode query takes " + (System.currentTimeMillis() - jCurrentTimeMillis) + " [ms]");
            List<CapturingModeAttributes> listFilter = capturingModeListLoader.filter(capturingModeList);
            listFilter.addAll(capturingModeListLoader.mLocalModeAttrsList);
            if (CamLog.VERBOSE) {
                CamLog.d("onCapturingModeGroupChanged count: " + listFilter.size());
            }
            return sortCapturingModeList(listFilter);
        }

        private List<CapturingModeAttributes> sortCapturingModeList(List<CapturingModeAttributes> list) {
            ArrayList arrayList = new ArrayList();
            for (CapturingModeId capturingModeId : CapturingModeId.values()) {
                Iterator<CapturingModeAttributes> it = list.iterator();
                while (true) {
                    if (it.hasNext()) {
                        CapturingModeAttributes next = it.next();
                        if (next.getModeName().equals(capturingModeId.getName())) {
                            arrayList.add(next);
                            list.remove(next);
                            break;
                        }
                    }
                }
            }
            if (!list.isEmpty()) {
                arrayList.addAll(arrayList.size(), list);
            }
            return arrayList;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(List<CapturingModeAttributes> list) {
            CapturingModeListLoader capturingModeListLoader = this.mCapturingModeListLoaderRef.get();
            if (capturingModeListLoader == null) {
                return;
            }
            capturingModeListLoader.mModeAttrsList.clear();
            capturingModeListLoader.mModeAttrsList.addAll(list);
            capturingModeListLoader.mOnModeListChangedListener.onCapturingModeListChanged(capturingModeListLoader.mModeAttrsList);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List<CapturingModeAttributes> filter(List<CapturingModeAttributes> list) {
        int selectorIconId;
        ArrayList arrayList = new ArrayList();
        PackageManager packageManager = this.mContext.getPackageManager();
        for (CapturingModeAttributes capturingModeAttributes : list) {
            if (!this.mContext.getPackageName().equals(capturingModeAttributes.getPackageName()) || ((selectorIconId = capturingModeAttributes.getSelectorIconId()) != 2131230854 && selectorIconId != 2131230877)) {
                boolean z = false;
                try {
                    z = packageManager.getApplicationInfo(capturingModeAttributes.getPackageName(), 128).enabled;
                } catch (PackageManager.NameNotFoundException e) {
                    CamLog.e("Failed to check whether mode is enabled. Message : " + e.getMessage());
                }
                if (z) {
                    arrayList.add(capturingModeAttributes);
                }
            }
        }
        return this.mVisibilityType == CapturingModeAttributes.VisibilityType.Oneshot ? CapturingModeUtil.sortOneshotCapturingMode(arrayList) : arrayList;
    }

    public void setLocalCapturingMode(List<CapturingModeAttributes> list) {
        this.mLocalModeAttrsList = list;
    }
}
