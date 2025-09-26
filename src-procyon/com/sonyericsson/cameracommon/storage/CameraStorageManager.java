// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import com.sonyericsson.android.camera.util.PerfLog;
import java.util.concurrent.Semaphore;
import android.os.StatFs;
import android.content.UriPermission;
import android.net.Uri;
import java.util.ArrayList;
import java.util.List;
import android.os.Environment;
import java.util.Iterator;
import android.support.annotation.NonNull;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Callable;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import com.sonyericsson.cameracommon.storage.updater.StorageStateUpdater;
import java.util.Map;
import android.content.Context;

public class CameraStorageManager
{
    public static final String TAG = "CameraStorageManager";
    private static final String THREAD_NAME_CHECK_REMAIN = "SM#ChkRemain";
    private static final String THREAD_NAME_CHECK_WRITABLE = "SM#ChkWritable";
    private static final int TIMEOUT_CHECK_WRITABLE = 5000;
    public static final int TIMEOUT_GET_STATFS = 3500;
    public static final int TIMEOUT_SEMAPHORE_ACQUIRE_MS = 4000;
    private Context mContext;
    private final Map<Storage.StorageType, DcfPathBuilder> mDcfPathBuilderMap;
    private boolean mIsApplicationForeground;
    private Map<Storage.StorageType, DetailStorageState> mLastStorageStates;
    private final Object mReadyStateLock;
    private SavingTaskInquiry mSavingTaskInquiry;
    private final SlowMotionPathBuilder mSlowMotionPathBuilder;
    private final Object mStateLock;
    private StorageController mStorageController;
    private Map<Storage.StorageType, StorageStateUpdater> mStorageUpdaterMap;
    private Map<String, DetailStorageState> mWritableCheckResult;
    
    public CameraStorageManager(final Context mContext, final StorageController mStorageController) {
        this.mLastStorageStates = null;
        this.mSlowMotionPathBuilder = new SlowMotionPathBuilder(".mp4");
        this.mWritableCheckResult = new ConcurrentHashMap<String, DetailStorageState>();
        this.mIsApplicationForeground = false;
        this.mStateLock = new Object();
        this.mReadyStateLock = new Object();
        this.mContext = mContext;
        this.mStorageController = mStorageController;
        this.mDcfPathBuilderMap = new HashMap<Storage.StorageType, DcfPathBuilder>();
        this.mLastStorageStates = new HashMap<Storage.StorageType, DetailStorageState>();
        this.mStorageUpdaterMap = new HashMap<Storage.StorageType, StorageStateUpdater>();
    }
    
    private boolean changeReadyStateTo(final Storage.StorageType obj, final Storage.StorageReadyState storageReadyState, final UpdateRequestReason obj2) {
        final Storage.StorageReadyState storageReadyState2 = this.mStorageController.getStorageReadyState(obj);
        final boolean debug = CamLog.DEBUG;
        boolean b = true;
        if (debug) {
            final StringBuilder sb = new StringBuilder();
            sb.append("changeReadyStateTo: type = ");
            sb.append(obj);
            sb.append(", from = ");
            sb.append(storageReadyState2);
            sb.append(", to = ");
            sb.append(storageReadyState);
            sb.append(", Reason = ");
            sb.append(obj2);
            CamLog.d(sb.toString());
        }
        Label_0410: {
            switch (CameraStorageManager$1.$SwitchMap$com$sonyericsson$cameracommon$storage$Storage$StorageReadyState[storageReadyState2.ordinal()]) {
                default: {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(storageReadyState2);
                    sb2.append(" is not supported.");
                    CamLog.e(sb2.toString());
                    break;
                }
                case 5: {
                    if (storageReadyState == Storage.StorageReadyState.SUSPENDED) {
                        this.mStorageUpdaterMap.get(obj).setAutoUpdateEnabled(false);
                        break Label_0410;
                    }
                    if (storageReadyState == Storage.StorageReadyState.PREPARING) {
                        break Label_0410;
                    }
                    break;
                }
                case 4: {
                    if (storageReadyState == Storage.StorageReadyState.SUSPENDED) {
                        this.mStorageUpdaterMap.get(obj).setAutoUpdateEnabled(false);
                        break Label_0410;
                    }
                    if (storageReadyState == Storage.StorageReadyState.COMPLETED) {
                        break Label_0410;
                    }
                    break;
                }
                case 3: {
                    if (storageReadyState == Storage.StorageReadyState.SUSPENDED) {
                        this.mStorageUpdaterMap.get(obj).setAutoUpdateEnabled(false);
                        break Label_0410;
                    }
                    if (storageReadyState == Storage.StorageReadyState.ACCESSIBLE) {
                        this.requestWriteCheck(obj, obj2);
                        break Label_0410;
                    }
                    break;
                }
                case 2: {
                    if (storageReadyState == Storage.StorageReadyState.PREPARING) {
                        this.mStorageUpdaterMap.get(obj).requestVolumeCheck(UpdateInterval.IMMEDIATE, obj2);
                        break Label_0410;
                    }
                    break;
                }
                case 1: {
                    if (storageReadyState == Storage.StorageReadyState.PREPARING) {
                        this.mStorageUpdaterMap.get(obj).requestVolumeCheck(UpdateInterval.IMMEDIATE, obj2);
                        break Label_0410;
                    }
                    if (CamLog.DEBUG) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Incorrect state : ");
                        sb3.append(storageReadyState);
                        throw new IllegalStateException(sb3.toString());
                    }
                    break;
                }
            }
            b = false;
        }
        if (b) {
            this.mStorageController.setStorageReadyState(obj, storageReadyState);
            this.mStorageController.checkAndNotifyReadyStateChanged(obj);
        }
        return b;
    }
    
    private boolean decideForceFsWritingCheck(final UpdateRequestReason updateRequestReason) {
        final int n = CameraStorageManager$1.$SwitchMap$com$sonyericsson$cameracommon$storage$CameraStorageManager$UpdateRequestReason[updateRequestReason.ordinal()];
        if (n != 5) {
            switch (n) {
                default: {
                    return false;
                }
                case 2:
                case 3: {
                    break;
                }
            }
        }
        return true;
    }
    
    private boolean decideForceSdCardGrantedCheck(final Storage.StorageType storageType, final UpdateRequestReason updateRequestReason) {
        if (storageType == Storage.StorageType.EXTERNAL_CARD) {
            switch (CameraStorageManager$1.$SwitchMap$com$sonyericsson$cameracommon$storage$CameraStorageManager$UpdateRequestReason[updateRequestReason.ordinal()]) {
                case 6:
                case 7: {
                    return false;
                }
            }
        }
        return true;
    }
    
    private DetailStorageState getLastStorageState(final Storage.StorageType storageType) {
        return this.mLastStorageStates.get(storageType);
    }
    
    private DetailStorageState getNextStateFromRemain(final long n) {
        DetailStorageState obj;
        if (n > 153600L) {
            obj = DetailStorageState.MEMORY_READY;
        }
        else if (n > 61440L) {
            obj = DetailStorageState.MEMORY_READY_LOW;
        }
        else {
            obj = DetailStorageState.MEMORY_ERR_FULL;
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getNextStateFromRemain() newState = ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return obj;
    }
    
    private DetailStorageState getNextStateFromVolume(final Storage.StorageType obj) {
        final String volumeState = StorageUtil.getVolumeState(obj, this.mContext);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getNextStateFromVolume() storage = ");
            sb.append(obj);
            sb.append(" , volume state = ");
            sb.append(volumeState);
            CamLog.d(sb.toString());
        }
        DetailStorageState detailStorageState;
        if ("bad_removal".equals(volumeState)) {
            detailStorageState = DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD;
        }
        else if ("mounted_ro".equals(volumeState)) {
            detailStorageState = DetailStorageState.MEMORY_ERR_READ_ONLY;
        }
        else if ("removed".equals(volumeState)) {
            detailStorageState = DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD;
        }
        else if ("shared".equals(volumeState)) {
            detailStorageState = DetailStorageState.MEMORY_ERR_SHARED;
        }
        else if ("unmountable".equals(volumeState)) {
            detailStorageState = DetailStorageState.MEMORY_ERR_FORMAT;
        }
        else if ("unmounted".equals(volumeState)) {
            detailStorageState = DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD;
        }
        else if ("checking".equals(volumeState)) {
            detailStorageState = DetailStorageState.MEMORY_CHECKING;
        }
        else if ("mounted".equals(volumeState)) {
            detailStorageState = DetailStorageState.MEMORY_READY;
        }
        else if ("ejecting".equals(volumeState)) {
            detailStorageState = DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD;
        }
        else {
            detailStorageState = DetailStorageState.MEMORY_ERR_ACCESS;
        }
        return detailStorageState;
    }
    
    @NonNull
    private DetailStorageState getNextStateFromWritable(final Storage.StorageType obj, final boolean b) {
        final ExecutorService buildExecutor = ThreadUtil.buildExecutor("SM#ChkWritable");
        final Future<DetailStorageState> submit = buildExecutor.submit((Callable<DetailStorageState>)new CheckFsDirectoryTask(this.mContext, obj, b));
        while (true) {
            try {
                try {
                    if (obj == Storage.StorageType.INTERNAL) {
                        final DetailStorageState detailStorageState = submit.get();
                    }
                    else {
                        final DetailStorageState detailStorageState2 = submit.get(5000L, TimeUnit.MILLISECONDS);
                    }
                    submit.cancel(true);
                    buildExecutor.shutdown();
                }
                finally {}
            }
            catch (final InterruptedException | ExecutionException | TimeoutException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("getNextStateFromWritable: timed out or fatal error, type = ");
                sb.append(obj);
                CamLog.e(sb.toString(), (Throwable)ex);
                final DetailStorageState memory_ERR_TIMED_OUT = DetailStorageState.MEMORY_ERR_TIMED_OUT;
                continue;
            }
            break;
        }
        return (DetailStorageState)obj;
        submit.cancel(true);
        buildExecutor.shutdown();
    }
    
    private boolean isReadable(final Storage.StorageType storageType) {
        if (storageType == null) {
            return false;
        }
        switch (CameraStorageManager$1.$SwitchMap$com$sonyericsson$cameracommon$storage$CameraStorageManager$DetailStorageState[this.mLastStorageStates.get(storageType).ordinal()]) {
            default: {
                return false;
            }
            case 1:
            case 2:
            case 3:
            case 4:
            case 5: {
                return true;
            }
        }
    }
    
    private boolean isWritableCheckNeeded(final Storage.StorageType storageType) {
        if (storageType == null) {
            return false;
        }
        switch (CameraStorageManager$1.$SwitchMap$com$sonyericsson$cameracommon$storage$CameraStorageManager$DetailStorageState[this.mLastStorageStates.get(storageType).ordinal()]) {
            default: {
                return false;
            }
            case 1:
            case 2:
            case 3:
            case 4: {
                return true;
            }
        }
    }
    
    private void logStorageState() {
        if (CamLog.VERBOSE) {
            final StringBuilder obj = new StringBuilder();
            final Iterator<Storage.StorageType> iterator = this.mLastStorageStates.keySet().iterator();
            int i = 0;
            while (iterator.hasNext()) {
                final Storage.StorageType storageType = iterator.next();
                final StringBuilder sb = new StringBuilder();
                sb.append(i);
                sb.append(":");
                sb.append(this.mLastStorageStates.get(storageType));
                sb.append(" ");
                obj.append(sb.toString());
                ++i;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("logStorageState ");
            sb2.append((Object)obj);
            CamLog.v(sb2.toString());
        }
    }
    
    private void setLastStorageState(final Storage.StorageType obj, final DetailStorageState obj2) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("updateLastStorageState storage: ");
            sb.append(obj);
            sb.append(" state: ");
            sb.append(obj2);
            CamLog.d(sb.toString());
        }
        this.mLastStorageStates.put(obj, obj2);
        if (CamLog.VERBOSE) {
            this.logStorageState();
        }
        this.mStorageController.setStorageState(obj, obj2);
    }
    
    private void updateDcfPath(final Storage.StorageType obj) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("updateDcfPath : targetStorage = ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        final String rootDirectory = this.getRootDirectory(obj);
        if (rootDirectory != null) {
            if (!this.mDcfPathBuilderMap.containsKey(obj)) {
                this.mDcfPathBuilderMap.put(obj, new DcfPathBuilder(rootDirectory));
            }
            else if (!rootDirectory.equals(this.mDcfPathBuilderMap.get(obj).getRootPath())) {
                if (CamLog.VERBOSE) {
                    CamLog.d("Root path is changed");
                }
                this.mDcfPathBuilderMap.put(obj, new DcfPathBuilder(rootDirectory));
            }
            this.mDcfPathBuilderMap.get(obj).startScan();
        }
    }
    
    public UpdateInterval calculateNextPollingInterval(final Storage.StorageType storageType) {
        return UpdateInterval.decide(this.mStorageController.getAvailableStorageSize(storageType));
    }
    
    public void checkAndNotifyStateChanged(final Storage.StorageType obj) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("checkAndNotifyStateChanged : ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        this.mStorageController.checkAndNotifyStateChanged(obj, false);
    }
    
    public long checkRemain(final boolean b, final Storage.StorageType obj) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("checkRemain: storage: ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        this.mStorageController.checkAndNotifyStateChanged(obj, b);
        return this.mStorageController.getAvailableStorageSize(obj);
    }
    
    public void doPause() {
        synchronized (this) {
            synchronized (this.mReadyStateLock) {
                this.mIsApplicationForeground = false;
                monitorexit(this.mReadyStateLock);
                for (Storage.StorageType storageType : StorageUtil.getMountableStorageTypes()) {
                    final Object mReadyStateLock = this.mReadyStateLock;
                    monitorenter(this.mReadyStateLock);
                    try {
                        if (this.mStorageController.getStorageReadyState(storageType) == Storage.StorageReadyState.COMPLETED) {
                            this.changeReadyStateTo(storageType, Storage.StorageReadyState.SUSPENDED, UpdateRequestReason.APP_CLOSE);
                        }
                        monitorexit(this.mReadyStateLock);
                        continue;
                    }
                    finally {}
                    break;
                }
            }
        }
    }
    
    public void doResume() {
        synchronized (this) {
            synchronized (this.mReadyStateLock) {
                this.mIsApplicationForeground = true;
                monitorexit(this.mReadyStateLock);
                for (Storage.StorageType storageType : StorageUtil.getMountableStorageTypes()) {
                    final Object mReadyStateLock = this.mReadyStateLock;
                    monitorenter(this.mReadyStateLock);
                    try {
                        if (this.mStorageController.getStorageReadyState(storageType) == Storage.StorageReadyState.SUSPENDED) {
                            this.changeReadyStateTo(storageType, Storage.StorageReadyState.PREPARING, UpdateRequestReason.APP_LAUNCH);
                        }
                        monitorexit(this.mReadyStateLock);
                        this.mStorageUpdaterMap.get(storageType).setAutoUpdateEnabled(true);
                        continue;
                    }
                    finally {}
                    break;
                }
            }
        }
    }
    
    public String getBurstPhotoPath(final SavingRequest savingRequest) {
        final StringBuilder sb = new StringBuilder();
        sb.append(StorageUtil.getPathFromType(savingRequest.getStorageType(), this.mContext));
        sb.append("/");
        sb.append(Environment.DIRECTORY_DCIM);
        return ManualBurstPathBuilder.getPhotoPath(sb.toString(), savingRequest);
    }
    
    public String getDcimDirectory(final Storage.StorageType storageType) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getRootDirectory(storageType));
        sb.append("/");
        sb.append(Environment.DIRECTORY_DCIM);
        return sb.toString();
    }
    
    public String getPhotoPath(final Storage.StorageType storageType) {
        final DcfPathBuilder dcfPathBuilder = this.mDcfPathBuilderMap.get(storageType);
        if (dcfPathBuilder != null) {
            if (CamLog.VERBOSE) {
                CamLog.d("DcfPathbuilder is not null!!");
            }
            return dcfPathBuilder.getPhotoPath(storageType);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("DcfPathbuilder is null!!");
        }
        return null;
    }
    
    public String getPredictiveCapturePhotoPath(final SavingRequest savingRequest) {
        return PredictiveCapturePathBuilder.getPhotoPath(this.getDcimDirectory(savingRequest.getStorageType()), savingRequest);
    }
    
    public List<String> getReadableStoragePaths() {
        final ArrayList list = new ArrayList();
        for (final Storage.StorageType storageType : this.mLastStorageStates.keySet()) {
            if (this.isReadable(storageType)) {
                final String pathFromType = StorageUtil.getPathFromType(storageType, this.mContext);
                if (pathFromType == null) {
                    continue;
                }
                list.add(pathFromType);
            }
        }
        return list;
    }
    
    public String getRootDirectory(final Storage.StorageType storageType) {
        return StorageUtil.getPathFromType(storageType, this.mContext);
    }
    
    public Uri getSdGrantedUri(final Context context) {
        final Storage.StorageState storageState = this.mStorageController.getStorageState(Storage.StorageType.EXTERNAL_CARD);
        final Storage.StorageState removed = Storage.StorageState.REMOVED;
        final Uri uri = null;
        if (storageState == removed) {
            return null;
        }
        final Uri sdCardGrantedUri = StorageUtil.getSdCardGrantedUri(context);
        final List persistedUriPermissions = context.getContentResolver().getPersistedUriPermissions();
        final int size = persistedUriPermissions.size();
        boolean b = false;
        Uri uri2;
        if (size == 1) {
            uri2 = persistedUriPermissions.get(0).getUri();
        }
        else {
            uri2 = uri;
            if (persistedUriPermissions.size() != 0) {
                final Iterator iterator = persistedUriPermissions.iterator();
                while (iterator.hasNext()) {
                    final Uri uri3 = ((UriPermission)iterator.next()).getUri();
                    if (sdCardGrantedUri != null && uri3.toString().equals(sdCardGrantedUri.toString())) {
                        b = true;
                    }
                    else {
                        context.getContentResolver().releasePersistableUriPermission(uri3, 3);
                    }
                }
                if (!b) {
                    uri2 = uri;
                }
                else {
                    uri2 = sdCardGrantedUri;
                }
            }
        }
        return uri2;
    }
    
    public String getSlowMotionPath(final String s, final Storage.StorageType storageType) {
        return this.mSlowMotionPathBuilder.get(this.getDcimDirectory(storageType), s, System.currentTimeMillis(), storageType);
    }
    
    public StatFs getStatFs(final String str) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getStatFs: ");
            sb.append(str);
            CamLog.d(sb.toString());
        }
        final ExecutorService buildExecutor = ThreadUtil.buildExecutor("SM#ChkRemain");
        final Future<StatFs> submit = buildExecutor.submit((Callable<StatFs>)new StorageUtil.GetStatFsTask(str));
        try {
            try {
                final StatFs statFs = submit.get(3500L, TimeUnit.MILLISECONDS);
                submit.cancel(true);
                buildExecutor.shutdown();
            }
            finally {}
        }
        catch (final TimeoutException ex) {
            CamLog.e("GetStatFsTask failed.", ex);
        }
        catch (final ExecutionException ex2) {
            CamLog.e("GetStatFsTask failed.", ex2);
        }
        catch (final InterruptedException ex3) {
            CamLog.e("GetStatFsTask has been interrupted.", ex3);
        }
        submit.cancel(true);
        buildExecutor.shutdown();
        return (StatFs)str;
        submit.cancel(true);
        buildExecutor.shutdown();
    }
    
    public String getVideoPath(final String s, final Storage.StorageType storageType) {
        if (this.mDcfPathBuilderMap.get(storageType) != null) {
            return this.mDcfPathBuilderMap.get(storageType).getVideoPath(s, storageType);
        }
        return "/dev/null";
    }
    
    void initialize(@NonNull final SavingTaskInquiry mSavingTaskInquiry, @NonNull final Map<Storage.StorageType, Semaphore> map) {
        if (CamLog.VERBOSE) {
            CamLog.d("CameraStorageManager initialize");
        }
        PerfLog.STORAGE_MANAGER_SETUP.begin();
        this.mStorageController.setAvailableStorageSize(Storage.StorageType.EXTERNAL_CARD, 0L);
        this.mStorageController.setAvailableStorageSize(Storage.StorageType.INTERNAL, 0L);
        this.mLastStorageStates = new HashMap<Storage.StorageType, DetailStorageState>();
        this.mSavingTaskInquiry = mSavingTaskInquiry;
        for (final Storage.StorageType storageType : StorageUtil.getMountableStorageTypes()) {
            this.mStorageUpdaterMap.put(storageType, new StorageStateUpdater(storageType, this, mSavingTaskInquiry, map.get(storageType)));
            synchronized (this.mReadyStateLock) {
                this.changeReadyStateTo(storageType, Storage.StorageReadyState.PREPARING, UpdateRequestReason.APP_LAUNCH);
                continue;
            }
            break;
        }
        PerfLog.STORAGE_MANAGER_SETUP.end();
    }
    
    public void release() {
        final Iterator<Storage.StorageType> iterator = StorageUtil.getMountableStorageTypes().iterator();
        while (iterator.hasNext()) {
            this.mStorageUpdaterMap.get(iterator.next()).release();
        }
        this.mStorageController.release();
    }
    
    public void requestVolumeCheck(final Storage.StorageType storageType, final UpdateInterval updateInterval, final UpdateRequestReason updateRequestReason) {
        this.mStorageUpdaterMap.get(storageType).requestVolumeCheck(updateInterval, updateRequestReason);
    }
    
    public void requestWriteCheck(final Storage.StorageType storageType, final UpdateRequestReason updateRequestReason) {
        this.mStorageUpdaterMap.get(storageType).requestWriteCheck(updateRequestReason);
    }
    
    public long updateAvailableStorageSize(final Storage.StorageType obj, long max) {
        final String pathFromType = StorageUtil.getPathFromType(obj, this.mContext);
        if (pathFromType == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Storage is not mounted. : ");
            sb.append(obj);
            CamLog.e(sb.toString());
            return 0L;
        }
        final StatFs statFs = this.getStatFs(pathFromType);
        if (statFs == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed to get StatFs: ");
            sb2.append(pathFromType);
            CamLog.e(sb2.toString());
            return 0L;
        }
        if (!StorageUtil.getVolumeState(obj, this.mContext).equals("mounted")) {
            return 0L;
        }
        final long blockSizeLong = statFs.getBlockSizeLong();
        final long availableBlocksLong = statFs.getAvailableBlocksLong();
        if (CamLog.VERBOSE) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("getAvailableSize size: ");
            sb3.append(blockSizeLong);
            CamLog.d(sb3.toString());
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("getAvailableSize num: ");
            sb4.append(availableBlocksLong);
            CamLog.d(sb4.toString());
        }
        max = Math.max(0L, (blockSizeLong * availableBlocksLong - max) / 1024L);
        if (CamLog.VERBOSE) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("getAvailableSize total[KB]: ");
            sb5.append(max);
            CamLog.d(sb5.toString());
        }
        return max;
    }
    
    public DetailStorageState updateStateByVolumeInfo(final Storage.StorageType obj, final long lng, final UpdateRequestReason obj2) {
        monitorenter(this);
        long updateAvailableStorageSize = 0L;
        final DetailStorageState detailStorageState = null;
        try {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invoke: type = ");
                sb.append(obj);
                sb.append(", reservedSize = ");
                sb.append(lng);
                sb.append(", reason = ");
                sb.append(obj2);
                CamLog.d(sb.toString());
            }
            synchronized (this.mStateLock) {
                final DetailStorageState lastStorageState = this.getLastStorageState(obj);
                if (CamLog.DEBUG) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("lastState = ");
                    sb2.append(lastStorageState);
                    CamLog.d(sb2.toString());
                }
                DetailStorageState detailStorageState2 = detailStorageState;
                UpdateRequestReason app_LAUNCH_WITH_UNTRUSTED = obj2;
                if (lastStorageState != null) {
                    detailStorageState2 = detailStorageState;
                    app_LAUNCH_WITH_UNTRUSTED = obj2;
                    if (lastStorageState != DetailStorageState.MEMORY_READY) {
                        detailStorageState2 = detailStorageState;
                        app_LAUNCH_WITH_UNTRUSTED = obj2;
                        if (lastStorageState != DetailStorageState.MEMORY_READY_LOW) {
                            detailStorageState2 = detailStorageState;
                            app_LAUNCH_WITH_UNTRUSTED = obj2;
                            if (lastStorageState != DetailStorageState.MEMORY_ERR_FULL) {
                                detailStorageState2 = detailStorageState;
                                app_LAUNCH_WITH_UNTRUSTED = obj2;
                                switch (CameraStorageManager$1.$SwitchMap$com$sonyericsson$cameracommon$storage$CameraStorageManager$UpdateRequestReason[obj2.ordinal()]) {
                                    default: {
                                        detailStorageState2 = lastStorageState;
                                        app_LAUNCH_WITH_UNTRUSTED = obj2;
                                        break;
                                    }
                                    case 1: {
                                        if (lastStorageState != DetailStorageState.MEMORY_NO_DCIM && lastStorageState != DetailStorageState.MEMORY_ERR_READ_ONLY) {
                                            detailStorageState2 = detailStorageState;
                                            app_LAUNCH_WITH_UNTRUSTED = obj2;
                                            if (lastStorageState != DetailStorageState.MEMORY_ERR_TIMED_OUT) {
                                                break;
                                            }
                                        }
                                        app_LAUNCH_WITH_UNTRUSTED = UpdateRequestReason.APP_LAUNCH_WITH_UNTRUSTED;
                                        detailStorageState2 = detailStorageState;
                                    }
                                    case 2:
                                    case 3:
                                    case 4: {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                DetailStorageState memory_ERR_NO_MEMORY_CARD = detailStorageState2;
                if (obj == Storage.StorageType.EXTERNAL_CARD) {
                    memory_ERR_NO_MEMORY_CARD = detailStorageState2;
                    if (!StorageUtil.isExistRemovableStorage(this.mContext)) {
                        memory_ERR_NO_MEMORY_CARD = DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD;
                    }
                }
                DetailStorageState nextStateFromRemain;
                if ((nextStateFromRemain = memory_ERR_NO_MEMORY_CARD) == null) {
                    DetailStorageState detailStorageState4;
                    final DetailStorageState detailStorageState3 = detailStorageState4 = this.getNextStateFromVolume(obj);
                    if (app_LAUNCH_WITH_UNTRUSTED == UpdateRequestReason.RECEIVE_STORAGE_EJECTED) {
                        if (detailStorageState3.equals(DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD)) {
                            detailStorageState4 = DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD;
                        }
                        else {
                            detailStorageState4 = DetailStorageState.MEMORY_ERR_SHARED;
                        }
                    }
                    if (detailStorageState4 == DetailStorageState.MEMORY_READY || (nextStateFromRemain = detailStorageState4) == DetailStorageState.MEMORY_READY_LOW) {
                        updateAvailableStorageSize = this.updateAvailableStorageSize(obj, lng);
                        nextStateFromRemain = this.getNextStateFromRemain(updateAvailableStorageSize);
                    }
                    this.mStorageController.setAvailableStorageSize(obj, updateAvailableStorageSize);
                }
                this.setLastStorageState(obj, nextStateFromRemain);
                synchronized (this.mReadyStateLock) {
                    if (this.mIsApplicationForeground) {
                        this.changeReadyStateTo(obj, Storage.StorageReadyState.ACCESSIBLE, app_LAUNCH_WITH_UNTRUSTED);
                    }
                    else {
                        this.changeReadyStateTo(obj, Storage.StorageReadyState.SUSPENDED, app_LAUNCH_WITH_UNTRUSTED);
                    }
                    monitorexit(this.mReadyStateLock);
                    if (app_LAUNCH_WITH_UNTRUSTED == UpdateRequestReason.RECEIVE_STORAGE_MOUNTED) {
                        this.requestVolumeCheck(obj, this.calculateNextPollingInterval(obj), UpdateRequestReason.PERIODIC_UPDATE);
                    }
                    return nextStateFromRemain;
                }
            }
        }
        finally {
            monitorexit(this);
        }
    }
    
    public void updateStorageState(final Storage.StorageType storageType, final UpdateRequestReason updateRequestReason) {
        if (updateRequestReason == UpdateRequestReason.RECEIVE_STORAGE_MOUNTED) {
            synchronized (this.mReadyStateLock) {
                this.changeReadyStateTo(storageType, Storage.StorageReadyState.PREPARING, updateRequestReason);
            }
        }
        long reservedSize = 0L;
        if (this.mSavingTaskInquiry != null) {
            reservedSize = this.mSavingTaskInquiry.getReservedSize(storageType);
        }
        else {
            CamLog.w("called before initializing.");
        }
        this.updateStateByVolumeInfo(storageType, reservedSize, updateRequestReason);
        this.updateStorageStateByWriting(storageType, updateRequestReason);
    }
    
    void updateStorageStateByAction(final String s, final Storage.StorageType storageType) {
        final Semaphore accessSemaphore = this.mStorageUpdaterMap.get(storageType).getAccessSemaphore();
        try {
            accessSemaphore.acquire();
            UpdateRequestReason updateRequestReason;
            if (s.equals("android.intent.action.MEDIA_MOUNTED")) {
                updateRequestReason = UpdateRequestReason.RECEIVE_STORAGE_MOUNTED;
            }
            else if (s.equals("android.intent.action.MEDIA_EJECT")) {
                updateRequestReason = UpdateRequestReason.RECEIVE_STORAGE_EJECTED;
            }
            else {
                updateRequestReason = UpdateRequestReason.RECEIVE_OTHER_ACTION;
            }
            this.updateStorageState(storageType, updateRequestReason);
            this.mStorageController.checkAndNotifyStateChanged(storageType, false);
            accessSemaphore.release();
        }
        catch (final InterruptedException ex) {
            CamLog.e("Failed to acquire of storage access permit.");
        }
    }
    
    public void updateStorageStateByWriting(final Storage.StorageType obj, final UpdateRequestReason obj2) {
        synchronized (this) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("updateStorageStateByWriting StorageType = ");
                sb.append(obj);
                sb.append(", reason = ");
                sb.append(obj2);
                CamLog.d(sb.toString());
            }
            synchronized (this.mStateLock) {
                DetailStorageState lastStorageState = this.getLastStorageState(obj);
                final boolean writableCheckNeeded = this.isWritableCheckNeeded(obj);
                if (CamLog.DEBUG) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("isWritable: ");
                    sb2.append(writableCheckNeeded);
                    CamLog.d(sb2.toString());
                }
                if (writableCheckNeeded) {
                    if (this.decideForceFsWritingCheck(obj2)) {
                        final String pathFromType = StorageUtil.getPathFromType(obj, this.mContext);
                        if (pathFromType != null) {
                            this.mWritableCheckResult.remove(pathFromType);
                        }
                    }
                    final DetailStorageState nextStateFromWritable = this.getNextStateFromWritable(obj, this.decideForceSdCardGrantedCheck(obj, obj2));
                    if (nextStateFromWritable != DetailStorageState.MEMORY_READY) {
                        lastStorageState = nextStateFromWritable;
                    }
                    this.setLastStorageState(obj, lastStorageState);
                }
                monitorexit(this.mStateLock);
                synchronized (this.mReadyStateLock) {
                    if (this.mIsApplicationForeground) {
                        if (this.changeReadyStateTo(obj, Storage.StorageReadyState.COMPLETED, obj2)) {
                            this.mStorageController.checkAndNotifyStateChanged(obj, true);
                        }
                    }
                    else {
                        this.changeReadyStateTo(obj, Storage.StorageReadyState.SUSPENDED, obj2);
                    }
                    monitorexit(this.mReadyStateLock);
                    final Storage.StorageState storageState = this.mStorageController.getStorageState(obj);
                    if (storageState != Storage.StorageState.REMOVED && storageState != Storage.StorageState.CORRUPT && storageState != Storage.StorageState.UNAVAILABLE) {
                        this.updateDcfPath(obj);
                    }
                }
            }
        }
    }
    
    private class CheckFsDirectoryTask implements Callable<DetailStorageState>
    {
        private final Context context;
        private final boolean forceSdCardGrantedCheck;
        final CameraStorageManager this$0;
        private final Storage.StorageType type;
        
        public CheckFsDirectoryTask(final CameraStorageManager this$0, final Context context, final Storage.StorageType type, final boolean forceSdCardGrantedCheck) {
            this.this$0 = this$0;
            this.type = type;
            this.context = context;
            this.forceSdCardGrantedCheck = forceSdCardGrantedCheck;
        }
        
        @Override
        public DetailStorageState call() throws Exception {
            final DetailStorageState memory_READY = DetailStorageState.MEMORY_READY;
            final String pathFromType = StorageUtil.getPathFromType(this.type, this.context);
            Enum<DetailStorageState> enum1;
            if (this.type == Storage.StorageType.EXTERNAL_CARD) {
                enum1 = memory_READY;
                if (this.forceSdCardGrantedCheck) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("CheckFsDirectoryTask [");
                    sb.append(this.type);
                    sb.append("] write check : E");
                    CamLog.i(sb.toString());
                    final Uri sdGrantedUri = this.this$0.getSdGrantedUri(this.context);
                    if (sdGrantedUri != null) {
                        final GrantCheckResult checkSdCardGranted = StorageUtil.checkSdCardGranted(this.context, sdGrantedUri);
                        if (checkSdCardGranted == GrantCheckResult.READ_ONLY) {
                            enum1 = DetailStorageState.MEMORY_ERR_READ_ONLY;
                        }
                        else if (checkSdCardGranted == GrantCheckResult.GRANTED) {
                            StorageUtil.setSdCardGranted(this.context, sdGrantedUri);
                            enum1 = memory_READY;
                        }
                        else {
                            enum1 = DetailStorageState.MEMORY_UNGRANTED;
                            try {
                                this.context.getContentResolver().releasePersistableUriPermission(sdGrantedUri, 3);
                            }
                            catch (final SecurityException ex) {
                                CamLog.d("can not releasePersistableUriPermission");
                            }
                        }
                    }
                    else {
                        enum1 = DetailStorageState.MEMORY_UNGRANTED;
                    }
                    this.this$0.mWritableCheckResult.put(pathFromType, enum1);
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("CheckFsDirectoryTask [");
                    sb2.append(this.type);
                    sb2.append("] write check : X");
                    CamLog.i(sb2.toString());
                }
            }
            else if (this.this$0.mWritableCheckResult.containsKey(pathFromType)) {
                enum1 = this.this$0.mWritableCheckResult.get(pathFromType);
            }
            else {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("CheckFsDirectoryTask [");
                sb3.append(this.type);
                sb3.append("] write check : E");
                CamLog.i(sb3.toString());
                if (pathFromType == null) {
                    CamLog.e("storage path is null");
                    enum1 = DetailStorageState.MEMORY_NO_DCIM;
                }
                else if (!DcfPathBuilder.checkAndCreateDirectory(pathFromType)) {
                    enum1 = DetailStorageState.MEMORY_NO_DCIM;
                }
                else {
                    enum1 = memory_READY;
                    if (!DcfPathBuilder.checkWritable(pathFromType)) {
                        enum1 = DetailStorageState.MEMORY_ERR_READ_ONLY;
                    }
                }
                this.this$0.mWritableCheckResult.put(pathFromType, enum1);
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("CheckFsDirectoryTask [");
                sb4.append(this.type);
                sb4.append("] write check : X");
                CamLog.i(sb4.toString());
            }
            Enum<DetailStorageState> memory_ERR_FULL_COUNT;
            if ((memory_ERR_FULL_COUNT = enum1) == DetailStorageState.MEMORY_READY) {
                memory_ERR_FULL_COUNT = enum1;
                if (DcfPathBuilder.isAlreadyLastFileExist(pathFromType)) {
                    memory_ERR_FULL_COUNT = DetailStorageState.MEMORY_ERR_FULL_COUNT;
                }
            }
            return (DetailStorageState)memory_ERR_FULL_COUNT;
        }
    }
    
    public enum DetailStorageState
    {
        private static final DetailStorageState[] $VALUES;
        
        MEMORY_CHECKING, 
        MEMORY_ERR_ACCESS, 
        MEMORY_ERR_FORMAT, 
        MEMORY_ERR_FULL, 
        MEMORY_ERR_FULL_COUNT, 
        MEMORY_ERR_NO_MEMORY_CARD, 
        MEMORY_ERR_READ_ONLY, 
        MEMORY_ERR_SHARED, 
        MEMORY_ERR_TIMED_OUT, 
        MEMORY_NO_DCIM, 
        MEMORY_READY, 
        MEMORY_READY_LOW, 
        MEMORY_UNGRANTED;
        
        static {
            $VALUES = new DetailStorageState[] { DetailStorageState.MEMORY_READY, DetailStorageState.MEMORY_READY_LOW, DetailStorageState.MEMORY_ERR_READ_ONLY, DetailStorageState.MEMORY_ERR_SHARED, DetailStorageState.MEMORY_ERR_FORMAT, DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD, DetailStorageState.MEMORY_ERR_ACCESS, DetailStorageState.MEMORY_ERR_FULL, DetailStorageState.MEMORY_ERR_TIMED_OUT, DetailStorageState.MEMORY_ERR_FULL_COUNT, DetailStorageState.MEMORY_NO_DCIM, DetailStorageState.MEMORY_CHECKING, DetailStorageState.MEMORY_UNGRANTED };
        }
    }
    
    public enum GrantCheckResult
    {
        private static final GrantCheckResult[] $VALUES;
        
        GRANTED, 
        READ_ONLY, 
        UNGRANTED;
        
        static {
            $VALUES = new GrantCheckResult[] { GrantCheckResult.GRANTED, GrantCheckResult.UNGRANTED, GrantCheckResult.READ_ONLY };
        }
    }
    
    public enum UpdateInterval
    {
        private static final UpdateInterval[] $VALUES;
        
        IMMEDIATE(0), 
        LOW_MEMORY(1000), 
        NORMAL(10000), 
        STOP(-1);
        
        private static final long THRESHOLD_LOW_MEMORY = 307200L;
        private final int intervalSec;
        
        static {
            $VALUES = new UpdateInterval[] { UpdateInterval.STOP, UpdateInterval.IMMEDIATE, UpdateInterval.LOW_MEMORY, UpdateInterval.NORMAL };
        }
        
        private UpdateInterval(final int intervalSec) {
            this.intervalSec = intervalSec;
        }
        
        @NonNull
        public static UpdateInterval decide(final long n) {
            if (n <= 61440L) {
                return UpdateInterval.STOP;
            }
            if (n < 307200L) {
                return UpdateInterval.LOW_MEMORY;
            }
            return UpdateInterval.NORMAL;
        }
        
        public int getIntervalMillis() {
            return this.intervalSec;
        }
    }
    
    public enum UpdateRequestReason
    {
        private static final UpdateRequestReason[] $VALUES;
        
        APP_CLOSE, 
        APP_LAUNCH, 
        APP_LAUNCH_WITH_UNTRUSTED, 
        PERIODIC_UPDATE, 
        PHOTO_STORING_COMPLETED, 
        RECEIVE_OTHER_ACTION, 
        RECEIVE_STORAGE_EJECTED, 
        RECEIVE_STORAGE_MOUNTED, 
        STORING_FAILED, 
        VIDEO_STORING_COMPLETED;
        
        static {
            $VALUES = new UpdateRequestReason[] { UpdateRequestReason.APP_LAUNCH, UpdateRequestReason.APP_LAUNCH_WITH_UNTRUSTED, UpdateRequestReason.APP_CLOSE, UpdateRequestReason.PERIODIC_UPDATE, UpdateRequestReason.PHOTO_STORING_COMPLETED, UpdateRequestReason.VIDEO_STORING_COMPLETED, UpdateRequestReason.STORING_FAILED, UpdateRequestReason.RECEIVE_STORAGE_MOUNTED, UpdateRequestReason.RECEIVE_STORAGE_EJECTED, UpdateRequestReason.RECEIVE_OTHER_ACTION };
        }
    }
}
