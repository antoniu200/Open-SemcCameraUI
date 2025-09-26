// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector;

import java.util.Collection;
import java.lang.ref.WeakReference;
import android.os.AsyncTask;
import java.util.concurrent.Executor;
import java.util.Iterator;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import com.sonyericsson.android.camera.util.CamLog;
import android.os.Handler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import android.content.Context;
import android.content.ContentResolver;
import android.database.ContentObserver;

public class CapturingModeListLoader
{
    private static final String TAG = "CapturingModeListLoader";
    private final CapturingModeAttributes.InternalCaptureType[] mCaptureTypeList;
    private final CapturingModeCollection mCapturingModeCollection;
    private final ContentObserver mContentObserver;
    private final ContentResolver mContentResolver;
    private final Context mContext;
    private final ExecutorService mExecutor;
    private List<CapturingModeAttributes> mLocalModeAttrsList;
    private List<CapturingModeAttributes> mModeAttrsList;
    private OnCapturingModeListChangedListener mOnModeListChangedListener;
    private final CapturingModeAttributes.VisibilityType mVisibilityType;
    
    public CapturingModeListLoader(final Context mContext, final CapturingModeAttributes.InternalCaptureType[] array, final CapturingModeAttributes.VisibilityType mVisibilityType, final OnCapturingModeListChangedListener mOnModeListChangedListener, final ExecutorService mExecutor) {
        this.mContext = mContext;
        this.mCaptureTypeList = array.clone();
        this.mVisibilityType = mVisibilityType;
        this.mOnModeListChangedListener = mOnModeListChangedListener;
        this.mModeAttrsList = new ArrayList<CapturingModeAttributes>();
        this.mExecutor = mExecutor;
        this.mContentObserver = new ContentObserver(this, null) {
            final CapturingModeListLoader this$0;
            
            public void onChange(final boolean b) {
                if (CamLog.VERBOSE) {
                    CamLog.d("CAPTURINGMODE_CONTENT_URI has been changed.");
                }
                this.this$0.startLoadTask();
            }
        };
        (this.mContentResolver = mContext.getContentResolver()).registerContentObserver(CameraCommonProviderConstants.CAPTURINGMODE_CONTENT_URI, true, this.mContentObserver);
        this.mCapturingModeCollection = new CapturingModeCollection(this.mContentResolver);
    }
    
    private List<CapturingModeAttributes> filter(List<CapturingModeAttributes> iterator) {
        final ArrayList list = new ArrayList();
        final PackageManager packageManager = this.mContext.getPackageManager();
        iterator = ((List<CapturingModeAttributes>)iterator).iterator();
        while (iterator.hasNext()) {
            final CapturingModeAttributes capturingModeAttributes = iterator.next();
            if (this.mContext.getPackageName().equals(capturingModeAttributes.getPackageName())) {
                final int selectorIconId = capturingModeAttributes.getSelectorIconId();
                if (selectorIconId == 2131230854 || selectorIconId == 2131230877) {
                    continue;
                }
            }
            int enabled = 0;
            try {
                enabled = (packageManager.getApplicationInfo(capturingModeAttributes.getPackageName(), 128).enabled ? 1 : 0);
            }
            catch (final PackageManager$NameNotFoundException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Failed to check whether mode is enabled. Message : ");
                sb.append(ex.getMessage());
                CamLog.e(sb.toString());
            }
            if (enabled != 0) {
                list.add(capturingModeAttributes);
            }
        }
        if (this.mVisibilityType == CapturingModeAttributes.VisibilityType.Oneshot) {
            return CapturingModeUtil.sortOneshotCapturingMode((List<CapturingModeAttributes>)list);
        }
        return list;
    }
    
    public CapturingModeAttributes getCapturingMode(final String s, final String s2) {
        for (final CapturingModeAttributes capturingModeAttributes : this.mModeAttrsList) {
            if (capturingModeAttributes == null) {
                continue;
            }
            if (s.equals(capturingModeAttributes.getPackageName()) && s2.equals(capturingModeAttributes.getModeName())) {
                return capturingModeAttributes;
            }
        }
        return null;
    }
    
    public void release() {
        this.mContentResolver.unregisterContentObserver(this.mContentObserver);
        this.mCapturingModeCollection.release();
        this.mExecutor.shutdown();
    }
    
    public void setLocalCapturingMode(final List<CapturingModeAttributes> mLocalModeAttrsList) {
        this.mLocalModeAttrsList = mLocalModeAttrsList;
    }
    
    public void startLoadTask() {
        if (CamLog.VERBOSE) {
            CamLog.d("start GetTask.");
        }
        new GetCapturingModeListTask(this).executeOnExecutor((Executor)this.mExecutor, (Object[])new Void[0]);
    }
    
    private enum CapturingModeId
    {
        private static final CapturingModeId[] $VALUES;
        
        AR_EFFECT("AR Effect"), 
        BOKEH("BackgroundDefocus"), 
        CREATIVE_EFFECT("capturing_mode_single_effect"), 
        DUAL_BACKGROUND_DEFOCUS("DUAL_BACKGROUND_DEFOCUS"), 
        DUAL_MONOCHROME("DUAL_MONOCHROME"), 
        GOOGLE_LENS("GOOGLE_LENS"), 
        MANUAL("MANUAL"), 
        PANORAMA("capturing_mode_sweep_panorama"), 
        PORTRAIT_SELFIE("PORTRAIT_SELFIE"), 
        SLOW_MOTION("SLOW_MOTION"), 
        SOUND_PHOTO("capturing_mode_soundphoto");
        
        private final String name;
        
        static {
            $VALUES = new CapturingModeId[] { CapturingModeId.DUAL_BACKGROUND_DEFOCUS, CapturingModeId.DUAL_MONOCHROME, CapturingModeId.PORTRAIT_SELFIE, CapturingModeId.GOOGLE_LENS, CapturingModeId.BOKEH, CapturingModeId.SLOW_MOTION, CapturingModeId.AR_EFFECT, CapturingModeId.MANUAL, CapturingModeId.CREATIVE_EFFECT, CapturingModeId.PANORAMA, CapturingModeId.SOUND_PHOTO };
        }
        
        private CapturingModeId(final String name2) {
            this.name = name2;
        }
        
        public String getName() {
            return this.name;
        }
    }
    
    private static class GetCapturingModeListTask extends AsyncTask<Void, Void, List<CapturingModeAttributes>>
    {
        private static final String THREAD_NAME = "GetModeListTask";
        private final WeakReference<CapturingModeListLoader> mCapturingModeListLoaderRef;
        
        GetCapturingModeListTask(final CapturingModeListLoader referent) {
            this.mCapturingModeListLoaderRef = new WeakReference<CapturingModeListLoader>(referent);
        }
        
        private List<CapturingModeAttributes> sortCapturingModeList(final List<CapturingModeAttributes> list) {
            final ArrayList list2 = new ArrayList();
            for (final CapturingModeId capturingModeId : CapturingModeId.values()) {
                for (final CapturingModeAttributes capturingModeAttributes : list) {
                    if (capturingModeAttributes.getModeName().equals(capturingModeId.getName())) {
                        list2.add(capturingModeAttributes);
                        list.remove(capturingModeAttributes);
                        break;
                    }
                }
            }
            if (!list.isEmpty()) {
                list2.addAll(list2.size(), list);
            }
            return list2;
        }
        
        public List<CapturingModeAttributes> doInBackground(final Void... array) {
            Thread.currentThread().setName("GetModeListTask");
            final CapturingModeListLoader capturingModeListLoader = this.mCapturingModeListLoaderRef.get();
            if (capturingModeListLoader == null) {
                return null;
            }
            final long currentTimeMillis = System.currentTimeMillis();
            final List<CapturingModeAttributes> capturingModeList = capturingModeListLoader.mCapturingModeCollection.getCapturingModeList(capturingModeListLoader.mCaptureTypeList, new CapturingModeAttributes.VisibilityType[] { capturingModeListLoader.mVisibilityType });
            final long currentTimeMillis2 = System.currentTimeMillis();
            final StringBuilder sb = new StringBuilder();
            sb.append("Mode query takes ");
            sb.append(currentTimeMillis2 - currentTimeMillis);
            sb.append(" [ms]");
            CamLog.i(sb.toString());
            final List access$300 = capturingModeListLoader.filter(capturingModeList);
            access$300.addAll(capturingModeListLoader.mLocalModeAttrsList);
            if (CamLog.VERBOSE) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("onCapturingModeGroupChanged count: ");
                sb2.append(access$300.size());
                CamLog.d(sb2.toString());
            }
            return this.sortCapturingModeList(access$300);
        }
        
        public void onPostExecute(final List<CapturingModeAttributes> list) {
            final CapturingModeListLoader capturingModeListLoader = this.mCapturingModeListLoaderRef.get();
            if (capturingModeListLoader == null) {
                return;
            }
            capturingModeListLoader.mModeAttrsList.clear();
            capturingModeListLoader.mModeAttrsList.addAll(list);
            capturingModeListLoader.mOnModeListChangedListener.onCapturingModeListChanged(capturingModeListLoader.mModeAttrsList);
        }
    }
    
    public interface OnCapturingModeListChangedListener
    {
        void onCapturingModeListChanged(final List<CapturingModeAttributes> p0);
    }
}
