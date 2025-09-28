package com.sonyericsson.cameracommon.storage;

import android.content.Context;
import android.content.UriPermission;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.support.v4.media.session.PlaybackStateCompat;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.android.camera.util.ThreadUtil;
import com.sonyericsson.android.camera.util.capability.SharedPrefsTranslator;
import com.sonyericsson.cameracommon.constants.CommonConstants;
import com.sonyericsson.cameracommon.mediasaving.MediaSavingConstants;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.storage.StorageUtil;
import com.sonyericsson.cameracommon.storage.updater.StorageStateUpdater;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.commons.imaging.formats.jpeg.iptc.IptcConstants;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CameraStorageManager {
    public static final String TAG = "CameraStorageManager";
    private static final String THREAD_NAME_CHECK_REMAIN = "SM#ChkRemain";
    private static final String THREAD_NAME_CHECK_WRITABLE = "SM#ChkWritable";
    private static final int TIMEOUT_CHECK_WRITABLE = 5000;
    public static final int TIMEOUT_GET_STATFS = 3500;
    public static final int TIMEOUT_SEMAPHORE_ACQUIRE_MS = 4000;
    private Context mContext;
    private Map<Storage.StorageType, DetailStorageState> mLastStorageStates;
    private SavingTaskInquiry mSavingTaskInquiry;
    private StorageController mStorageController;
    private final SlowMotionPathBuilder mSlowMotionPathBuilder = new SlowMotionPathBuilder(MediaSavingConstants.MEDIA_TYPE_MPEG4_EXT);
    private Map<String, DetailStorageState> mWritableCheckResult = new ConcurrentHashMap();
    private boolean mIsApplicationForeground = false;
    private final Object mStateLock = new Object();
    private final Object mReadyStateLock = new Object();
    private final Map<Storage.StorageType, DcfPathBuilder> mDcfPathBuilderMap = new HashMap();
    private Map<Storage.StorageType, StorageStateUpdater> mStorageUpdaterMap = new HashMap();

    public enum DetailStorageState {
        MEMORY_READY,
        MEMORY_READY_LOW,
        MEMORY_ERR_READ_ONLY,
        MEMORY_ERR_SHARED,
        MEMORY_ERR_FORMAT,
        MEMORY_ERR_NO_MEMORY_CARD,
        MEMORY_ERR_ACCESS,
        MEMORY_ERR_FULL,
        MEMORY_ERR_TIMED_OUT,
        MEMORY_ERR_FULL_COUNT,
        MEMORY_NO_DCIM,
        MEMORY_CHECKING,
        MEMORY_UNGRANTED
    }

    public enum GrantCheckResult {
        GRANTED,
        UNGRANTED,
        READ_ONLY
    }

    public enum UpdateRequestReason {
        APP_LAUNCH,
        APP_LAUNCH_WITH_UNTRUSTED,
        APP_CLOSE,
        PERIODIC_UPDATE,
        PHOTO_STORING_COMPLETED,
        VIDEO_STORING_COMPLETED,
        STORING_FAILED,
        RECEIVE_STORAGE_MOUNTED,
        RECEIVE_STORAGE_EJECTED,
        RECEIVE_OTHER_ACTION
    }

    public enum UpdateInterval {
        STOP(-1),
        IMMEDIATE(0),
        LOW_MEMORY(1000),
        NORMAL(IptcConstants.IMAGE_RESOURCE_BLOCK_PRINT_FLAGS_INFO);

        private static final long THRESHOLD_LOW_MEMORY = 307200;
        private final int intervalSec;

        UpdateInterval(int i) {
            this.intervalSec = i;
        }

        @NonNull
        public static UpdateInterval decide(long j) {
            if (j <= CommonConstants.STORAGE_REMAIN_MIN) {
                return STOP;
            }
            if (j < THRESHOLD_LOW_MEMORY) {
                return LOW_MEMORY;
            }
            return NORMAL;
        }

        public int getIntervalMillis() {
            return this.intervalSec;
        }
    }

    public CameraStorageManager(Context context, StorageController storageController) {
        this.mLastStorageStates = null;
        this.mContext = context;
        this.mStorageController = storageController;
        this.mLastStorageStates = new HashMap();
    }

    void initialize(@NonNull SavingTaskInquiry savingTaskInquiry, @NonNull Map<Storage.StorageType, Semaphore> map) {
        if (CamLog.VERBOSE) {
            CamLog.d("CameraStorageManager initialize");
        }
        PerfLog.STORAGE_MANAGER_SETUP.begin();
        this.mStorageController.setAvailableStorageSize(Storage.StorageType.EXTERNAL_CARD, 0L);
        this.mStorageController.setAvailableStorageSize(Storage.StorageType.INTERNAL, 0L);
        this.mLastStorageStates = new HashMap();
        this.mSavingTaskInquiry = savingTaskInquiry;
        for (Storage.StorageType storageType : StorageUtil.getMountableStorageTypes()) {
            this.mStorageUpdaterMap.put(storageType, new StorageStateUpdater(storageType, this, savingTaskInquiry, map.get(storageType)));
            synchronized (this.mReadyStateLock) {
                changeReadyStateTo(storageType, Storage.StorageReadyState.PREPARING, UpdateRequestReason.APP_LAUNCH);
            }
        }
        PerfLog.STORAGE_MANAGER_SETUP.end();
    }

    void updateStorageStateByAction(String str, Storage.StorageType storageType) throws InterruptedException {
        UpdateRequestReason updateRequestReason;
        Semaphore accessSemaphore = this.mStorageUpdaterMap.get(storageType).getAccessSemaphore();
        try {
            accessSemaphore.acquire();
            if (str.equals("android.intent.action.MEDIA_MOUNTED")) {
                updateRequestReason = UpdateRequestReason.RECEIVE_STORAGE_MOUNTED;
            } else if (str.equals("android.intent.action.MEDIA_EJECT")) {
                updateRequestReason = UpdateRequestReason.RECEIVE_STORAGE_EJECTED;
            } else {
                updateRequestReason = UpdateRequestReason.RECEIVE_OTHER_ACTION;
            }
            updateStorageState(storageType, updateRequestReason);
            this.mStorageController.checkAndNotifyStateChanged(storageType, false);
            accessSemaphore.release();
        } catch (InterruptedException unused) {
            CamLog.e("Failed to acquire of storage access permit.");
        }
    }

    public void requestVolumeCheck(Storage.StorageType storageType, UpdateInterval updateInterval, UpdateRequestReason updateRequestReason) {
        this.mStorageUpdaterMap.get(storageType).requestVolumeCheck(updateInterval, updateRequestReason);
    }

    public void requestWriteCheck(Storage.StorageType storageType, UpdateRequestReason updateRequestReason) {
        this.mStorageUpdaterMap.get(storageType).requestWriteCheck(updateRequestReason);
    }

    public void updateStorageState(Storage.StorageType storageType, UpdateRequestReason updateRequestReason) {
        if (updateRequestReason == UpdateRequestReason.RECEIVE_STORAGE_MOUNTED) {
            synchronized (this.mReadyStateLock) {
                changeReadyStateTo(storageType, Storage.StorageReadyState.PREPARING, updateRequestReason);
            }
        }
        long reservedSize = 0;
        if (this.mSavingTaskInquiry != null) {
            reservedSize = this.mSavingTaskInquiry.getReservedSize(storageType);
        } else {
            CamLog.w("called before initializing.");
        }
        updateStateByVolumeInfo(storageType, reservedSize, updateRequestReason);
        updateStorageStateByWriting(storageType, updateRequestReason);
    }

    public synchronized void updateStorageStateByWriting(Storage.StorageType storageType, UpdateRequestReason updateRequestReason) {
        String pathFromType;
        if (CamLog.DEBUG) {
            CamLog.d("updateStorageStateByWriting StorageType = " + storageType + ", reason = " + updateRequestReason);
        }
        synchronized (this.mStateLock) {
            DetailStorageState lastStorageState = getLastStorageState(storageType);
            boolean zIsWritableCheckNeeded = isWritableCheckNeeded(storageType);
            if (CamLog.DEBUG) {
                CamLog.d("isWritable: " + zIsWritableCheckNeeded);
            }
            if (zIsWritableCheckNeeded) {
                if (decideForceFsWritingCheck(updateRequestReason) && (pathFromType = StorageUtil.getPathFromType(storageType, this.mContext)) != null) {
                    this.mWritableCheckResult.remove(pathFromType);
                }
                DetailStorageState nextStateFromWritable = getNextStateFromWritable(storageType, decideForceSdCardGrantedCheck(storageType, updateRequestReason));
                if (nextStateFromWritable == DetailStorageState.MEMORY_READY) {
                    nextStateFromWritable = lastStorageState;
                }
                setLastStorageState(storageType, nextStateFromWritable);
            }
        }
        synchronized (this.mReadyStateLock) {
            if (this.mIsApplicationForeground) {
                if (changeReadyStateTo(storageType, Storage.StorageReadyState.COMPLETED, updateRequestReason)) {
                    this.mStorageController.checkAndNotifyStateChanged(storageType, true);
                }
            } else {
                changeReadyStateTo(storageType, Storage.StorageReadyState.SUSPENDED, updateRequestReason);
            }
        }
        Storage.StorageState storageState = this.mStorageController.getStorageState(storageType);
        if (storageState != Storage.StorageState.REMOVED && storageState != Storage.StorageState.CORRUPT && storageState != Storage.StorageState.UNAVAILABLE) {
            updateDcfPath(storageType);
        }
    }

    public synchronized DetailStorageState updateStateByVolumeInfo(Storage.StorageType storageType, long j, UpdateRequestReason updateRequestReason) {
        DetailStorageState nextStateFromVolume;
        long jUpdateAvailableStorageSize = 0;
        nextStateFromVolume = null;
        if (CamLog.DEBUG) {
            CamLog.d("invoke: type = " + storageType + ", reservedSize = " + j + ", reason = " + updateRequestReason);
        }
        synchronized (this.mStateLock) {
            DetailStorageState lastStorageState = getLastStorageState(storageType);
            if (CamLog.DEBUG) {
                CamLog.d("lastState = " + lastStorageState);
            }
            if (lastStorageState != null && lastStorageState != DetailStorageState.MEMORY_READY && lastStorageState != DetailStorageState.MEMORY_READY_LOW && lastStorageState != DetailStorageState.MEMORY_ERR_FULL) {
                switch (updateRequestReason) {
                    case APP_LAUNCH:
                        if (lastStorageState == DetailStorageState.MEMORY_NO_DCIM || lastStorageState == DetailStorageState.MEMORY_ERR_READ_ONLY || lastStorageState == DetailStorageState.MEMORY_ERR_TIMED_OUT) {
                            updateRequestReason = UpdateRequestReason.APP_LAUNCH_WITH_UNTRUSTED;
                            break;
                        }
                        break;
                    case APP_LAUNCH_WITH_UNTRUSTED:
                    case RECEIVE_STORAGE_MOUNTED:
                    case RECEIVE_STORAGE_EJECTED:
                        break;
                    default:
                        nextStateFromVolume = lastStorageState;
                        break;
                }
            }
            if (storageType == Storage.StorageType.EXTERNAL_CARD && !StorageUtil.isExistRemovableStorage(this.mContext)) {
                nextStateFromVolume = DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD;
            }
            if (nextStateFromVolume == null) {
                nextStateFromVolume = getNextStateFromVolume(storageType);
                if (updateRequestReason == UpdateRequestReason.RECEIVE_STORAGE_EJECTED) {
                    if (nextStateFromVolume.equals(DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD)) {
                        nextStateFromVolume = DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD;
                    } else {
                        nextStateFromVolume = DetailStorageState.MEMORY_ERR_SHARED;
                    }
                }
                if (nextStateFromVolume == DetailStorageState.MEMORY_READY || nextStateFromVolume == DetailStorageState.MEMORY_READY_LOW) {
                    jUpdateAvailableStorageSize = updateAvailableStorageSize(storageType, j);
                    nextStateFromVolume = getNextStateFromRemain(jUpdateAvailableStorageSize);
                }
                this.mStorageController.setAvailableStorageSize(storageType, jUpdateAvailableStorageSize);
            }
            setLastStorageState(storageType, nextStateFromVolume);
            synchronized (this.mReadyStateLock) {
                if (this.mIsApplicationForeground) {
                    changeReadyStateTo(storageType, Storage.StorageReadyState.ACCESSIBLE, updateRequestReason);
                } else {
                    changeReadyStateTo(storageType, Storage.StorageReadyState.SUSPENDED, updateRequestReason);
                }
            }
            if (updateRequestReason == UpdateRequestReason.RECEIVE_STORAGE_MOUNTED) {
                requestVolumeCheck(storageType, calculateNextPollingInterval(storageType), UpdateRequestReason.PERIODIC_UPDATE);
            }
        }
        return nextStateFromVolume;
    }

    private DetailStorageState getNextStateFromVolume(Storage.StorageType storageType) {
        String volumeState = StorageUtil.getVolumeState(storageType, this.mContext);
        if (CamLog.VERBOSE) {
            CamLog.d("getNextStateFromVolume() storage = " + storageType + " , volume state = " + volumeState);
        }
        if ("bad_removal".equals(volumeState)) {
            return DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD;
        }
        if ("mounted_ro".equals(volumeState)) {
            return DetailStorageState.MEMORY_ERR_READ_ONLY;
        }
        if ("removed".equals(volumeState)) {
            return DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD;
        }
        if ("shared".equals(volumeState)) {
            return DetailStorageState.MEMORY_ERR_SHARED;
        }
        if ("unmountable".equals(volumeState)) {
            return DetailStorageState.MEMORY_ERR_FORMAT;
        }
        if ("unmounted".equals(volumeState)) {
            return DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD;
        }
        if ("checking".equals(volumeState)) {
            return DetailStorageState.MEMORY_CHECKING;
        }
        if ("mounted".equals(volumeState)) {
            return DetailStorageState.MEMORY_READY;
        }
        if ("ejecting".equals(volumeState)) {
            return DetailStorageState.MEMORY_ERR_NO_MEMORY_CARD;
        }
        return DetailStorageState.MEMORY_ERR_ACCESS;
    }

    private boolean decideForceFsWritingCheck(UpdateRequestReason updateRequestReason) {
        int i = AnonymousClass1.$SwitchMap$com$sonyericsson$cameracommon$storage$CameraStorageManager$UpdateRequestReason[updateRequestReason.ordinal()];
        if (i != 5) {
            switch (i) {
                case 2:
                case 3:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    private boolean decideForceSdCardGrantedCheck(Storage.StorageType storageType, UpdateRequestReason updateRequestReason) {
        if (storageType == Storage.StorageType.EXTERNAL_CARD) {
            switch (updateRequestReason) {
                case PHOTO_STORING_COMPLETED:
                case VIDEO_STORING_COMPLETED:
                    return false;
            }
        }
        return true;
    }

    @NonNull
    private DetailStorageState getNextStateFromWritable(Storage.StorageType storageType, boolean z) {
        DetailStorageState detailStorageState;
        ExecutorService executorServiceBuildExecutor = ThreadUtil.buildExecutor(THREAD_NAME_CHECK_WRITABLE);
        Future futureSubmit = executorServiceBuildExecutor.submit(new CheckFsDirectoryTask(this.mContext, storageType, z));
        try {
            try {
                if (storageType == Storage.StorageType.INTERNAL) {
                    detailStorageState = (DetailStorageState) futureSubmit.get();
                } else {
                    detailStorageState = (DetailStorageState) futureSubmit.get(5000L, TimeUnit.MILLISECONDS);
                }
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                CamLog.e("getNextStateFromWritable: timed out or fatal error, type = " + storageType, e);
                detailStorageState = DetailStorageState.MEMORY_ERR_TIMED_OUT;
            }
            return detailStorageState;
        } finally {
            futureSubmit.cancel(true);
            executorServiceBuildExecutor.shutdown();
        }
    }

    public Uri getSdGrantedUri(Context context) {
        if (this.mStorageController.getStorageState(Storage.StorageType.EXTERNAL_CARD) == Storage.StorageState.REMOVED) {
            return null;
        }
        Uri sdCardGrantedUri = StorageUtil.getSdCardGrantedUri(context);
        List<UriPermission> persistedUriPermissions = context.getContentResolver().getPersistedUriPermissions();
        boolean z = false;
        if (persistedUriPermissions.size() == 1) {
            return persistedUriPermissions.get(0).getUri();
        }
        if (persistedUriPermissions.size() == 0) {
            return null;
        }
        Iterator<UriPermission> it = persistedUriPermissions.iterator();
        while (it.hasNext()) {
            Uri uri = it.next().getUri();
            if (sdCardGrantedUri == null || !uri.toString().equals(sdCardGrantedUri.toString())) {
                context.getContentResolver().releasePersistableUriPermission(uri, 3);
            } else {
                z = true;
            }
        }
        if (z) {
            return sdCardGrantedUri;
        }
        return null;
    }

    private class CheckFsDirectoryTask implements Callable<DetailStorageState> {
        private final Context context;
        private final boolean forceSdCardGrantedCheck;
        private final Storage.StorageType type;

        public CheckFsDirectoryTask(Context context, Storage.StorageType storageType, boolean z) {
            this.type = storageType;
            this.context = context;
            this.forceSdCardGrantedCheck = z;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.concurrent.Callable
        public DetailStorageState call() throws Exception {
            DetailStorageState detailStorageState = DetailStorageState.MEMORY_READY;
            String pathFromType = StorageUtil.getPathFromType(this.type, this.context);
            if (this.type != Storage.StorageType.EXTERNAL_CARD) {
                if (CameraStorageManager.this.mWritableCheckResult.containsKey(pathFromType)) {
                    detailStorageState = (DetailStorageState) CameraStorageManager.this.mWritableCheckResult.get(pathFromType);
                } else {
                    CamLog.i("CheckFsDirectoryTask [" + this.type + "] write check : E");
                    if (pathFromType == null) {
                        CamLog.e("storage path is null");
                        detailStorageState = DetailStorageState.MEMORY_NO_DCIM;
                    } else if (!DcfPathBuilder.checkAndCreateDirectory(pathFromType)) {
                        detailStorageState = DetailStorageState.MEMORY_NO_DCIM;
                    } else if (!DcfPathBuilder.checkWritable(pathFromType)) {
                        detailStorageState = DetailStorageState.MEMORY_ERR_READ_ONLY;
                    }
                    CameraStorageManager.this.mWritableCheckResult.put(pathFromType, detailStorageState);
                    CamLog.i("CheckFsDirectoryTask [" + this.type + "] write check : X");
                }
            } else if (this.forceSdCardGrantedCheck) {
                CamLog.i("CheckFsDirectoryTask [" + this.type + "] write check : E");
                Uri sdGrantedUri = CameraStorageManager.this.getSdGrantedUri(this.context);
                if (sdGrantedUri != null) {
                    GrantCheckResult grantCheckResultCheckSdCardGranted = StorageUtil.checkSdCardGranted(this.context, sdGrantedUri);
                    if (grantCheckResultCheckSdCardGranted == GrantCheckResult.READ_ONLY) {
                        detailStorageState = DetailStorageState.MEMORY_ERR_READ_ONLY;
                    } else if (grantCheckResultCheckSdCardGranted == GrantCheckResult.GRANTED) {
                        StorageUtil.setSdCardGranted(this.context, sdGrantedUri);
                    } else {
                        detailStorageState = DetailStorageState.MEMORY_UNGRANTED;
                        try {
                            this.context.getContentResolver().releasePersistableUriPermission(sdGrantedUri, 3);
                        } catch (SecurityException unused) {
                            CamLog.d("can not releasePersistableUriPermission");
                        }
                    }
                } else {
                    detailStorageState = DetailStorageState.MEMORY_UNGRANTED;
                }
                CameraStorageManager.this.mWritableCheckResult.put(pathFromType, detailStorageState);
                CamLog.i("CheckFsDirectoryTask [" + this.type + "] write check : X");
            }
            return (detailStorageState == DetailStorageState.MEMORY_READY && DcfPathBuilder.isAlreadyLastFileExist(pathFromType)) ? DetailStorageState.MEMORY_ERR_FULL_COUNT : detailStorageState;
        }
    }

    private DetailStorageState getNextStateFromRemain(long j) {
        DetailStorageState detailStorageState;
        if (j > CommonConstants.STORAGE_REMAIN_ENOUGH) {
            detailStorageState = DetailStorageState.MEMORY_READY;
        } else if (j > CommonConstants.STORAGE_REMAIN_MIN) {
            detailStorageState = DetailStorageState.MEMORY_READY_LOW;
        } else {
            detailStorageState = DetailStorageState.MEMORY_ERR_FULL;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getNextStateFromRemain() newState = " + detailStorageState);
        }
        return detailStorageState;
    }

    private void setLastStorageState(Storage.StorageType storageType, DetailStorageState detailStorageState) {
        if (CamLog.DEBUG) {
            CamLog.d("updateLastStorageState storage: " + storageType + " state: " + detailStorageState);
        }
        this.mLastStorageStates.put(storageType, detailStorageState);
        if (CamLog.VERBOSE) {
            logStorageState();
        }
        this.mStorageController.setStorageState(storageType, detailStorageState);
    }

    private void logStorageState() {
        if (CamLog.VERBOSE) {
            StringBuilder sb = new StringBuilder();
            Iterator<Storage.StorageType> it = this.mLastStorageStates.keySet().iterator();
            int i = 0;
            while (it.hasNext()) {
                sb.append(i + ":" + this.mLastStorageStates.get(it.next()) + " ");
                i++;
            }
            CamLog.v("logStorageState " + ((Object) sb));
        }
    }

    public long checkRemain(boolean z, Storage.StorageType storageType) {
        if (CamLog.VERBOSE) {
            CamLog.d("checkRemain: storage: " + storageType);
        }
        this.mStorageController.checkAndNotifyStateChanged(storageType, z);
        return this.mStorageController.getAvailableStorageSize(storageType);
    }

    public void checkAndNotifyStateChanged(Storage.StorageType storageType) {
        if (CamLog.VERBOSE) {
            CamLog.d("checkAndNotifyStateChanged : " + storageType);
        }
        this.mStorageController.checkAndNotifyStateChanged(storageType, false);
    }

    private DetailStorageState getLastStorageState(Storage.StorageType storageType) {
        return this.mLastStorageStates.get(storageType);
    }

    private void updateDcfPath(Storage.StorageType storageType) {
        if (CamLog.VERBOSE) {
            CamLog.d("updateDcfPath : targetStorage = " + storageType);
        }
        String rootDirectory = getRootDirectory(storageType);
        if (rootDirectory != null) {
            if (!this.mDcfPathBuilderMap.containsKey(storageType)) {
                this.mDcfPathBuilderMap.put(storageType, new DcfPathBuilder(rootDirectory));
            } else if (!rootDirectory.equals(this.mDcfPathBuilderMap.get(storageType).getRootPath())) {
                if (CamLog.VERBOSE) {
                    CamLog.d("Root path is changed");
                }
                this.mDcfPathBuilderMap.put(storageType, new DcfPathBuilder(rootDirectory));
            }
            this.mDcfPathBuilderMap.get(storageType).startScan();
        }
    }

    public StatFs getStatFs(String str) {
        if (CamLog.VERBOSE) {
            CamLog.d("getStatFs: " + str);
        }
        ExecutorService executorServiceBuildExecutor = ThreadUtil.buildExecutor(THREAD_NAME_CHECK_REMAIN);
        Future futureSubmit = executorServiceBuildExecutor.submit(new StorageUtil.GetStatFsTask(str));
        try {
            try {
                try {
                    try {
                        return (StatFs) futureSubmit.get(3500L, TimeUnit.MILLISECONDS);
                    } catch (TimeoutException e) {
                        CamLog.e("GetStatFsTask failed.", e);
                        return null;
                    }
                } catch (InterruptedException e2) {
                    CamLog.e("GetStatFsTask has been interrupted.", e2);
                    return null;
                }
            } catch (ExecutionException e3) {
                CamLog.e("GetStatFsTask failed.", e3);
                return null;
            }
        } finally {
            futureSubmit.cancel(true);
            executorServiceBuildExecutor.shutdown();
        }
    }

    public long updateAvailableStorageSize(Storage.StorageType storageType, long j) {
        String pathFromType = StorageUtil.getPathFromType(storageType, this.mContext);
        if (pathFromType == null) {
            CamLog.e("Storage is not mounted. : " + storageType);
            return 0L;
        }
        StatFs statFs = getStatFs(pathFromType);
        if (statFs == null) {
            CamLog.e("Failed to get StatFs: " + pathFromType);
            return 0L;
        }
        if (!StorageUtil.getVolumeState(storageType, this.mContext).equals("mounted")) {
            return 0L;
        }
        long blockSizeLong = statFs.getBlockSizeLong();
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        if (CamLog.VERBOSE) {
            CamLog.d("getAvailableSize size: " + blockSizeLong);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("getAvailableSize num: " + availableBlocksLong);
        }
        long jMax = Math.max(0L, ((blockSizeLong * availableBlocksLong) - j) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID);
        if (CamLog.VERBOSE) {
            CamLog.d("getAvailableSize total[KB]: " + jMax);
        }
        return jMax;
    }

    private boolean isReadable(Storage.StorageType storageType) {
        if (storageType == null) {
            return false;
        }
        switch (this.mLastStorageStates.get(storageType)) {
        }
        return false;
    }

    private boolean isWritableCheckNeeded(Storage.StorageType storageType) {
        if (storageType == null) {
            return false;
        }
        switch (this.mLastStorageStates.get(storageType)) {
        }
        return false;
    }

    public List<String> getReadableStoragePaths() {
        String pathFromType;
        ArrayList arrayList = new ArrayList();
        for (Storage.StorageType storageType : this.mLastStorageStates.keySet()) {
            if (isReadable(storageType) && (pathFromType = StorageUtil.getPathFromType(storageType, this.mContext)) != null) {
                arrayList.add(pathFromType);
            }
        }
        return arrayList;
    }

    public void release() {
        Iterator<Storage.StorageType> it = StorageUtil.getMountableStorageTypes().iterator();
        while (it.hasNext()) {
            this.mStorageUpdaterMap.get(it.next()).release();
        }
        this.mStorageController.release();
    }

    public String getRootDirectory(Storage.StorageType storageType) {
        return StorageUtil.getPathFromType(storageType, this.mContext);
    }

    public String getDcimDirectory(Storage.StorageType storageType) {
        return getRootDirectory(storageType) + SharedPrefsTranslator.CONNECTOR_SLASH + Environment.DIRECTORY_DCIM;
    }

    public String getPhotoPath(Storage.StorageType storageType) {
        DcfPathBuilder dcfPathBuilder = this.mDcfPathBuilderMap.get(storageType);
        if (dcfPathBuilder != null) {
            if (CamLog.VERBOSE) {
                CamLog.d("DcfPathbuilder is not null!!");
            }
            return dcfPathBuilder.getPhotoPath(storageType);
        }
        if (!CamLog.VERBOSE) {
            return null;
        }
        CamLog.d("DcfPathbuilder is null!!");
        return null;
    }

    public String getBurstPhotoPath(SavingRequest savingRequest) {
        return ManualBurstPathBuilder.getPhotoPath(StorageUtil.getPathFromType(savingRequest.getStorageType(), this.mContext) + SharedPrefsTranslator.CONNECTOR_SLASH + Environment.DIRECTORY_DCIM, savingRequest);
    }

    public String getPredictiveCapturePhotoPath(SavingRequest savingRequest) {
        return PredictiveCapturePathBuilder.getPhotoPath(getDcimDirectory(savingRequest.getStorageType()), savingRequest);
    }

    public String getVideoPath(String str, Storage.StorageType storageType) {
        return this.mDcfPathBuilderMap.get(storageType) != null ? this.mDcfPathBuilderMap.get(storageType).getVideoPath(str, storageType) : MediaSavingConstants.INVALID_FILE_PATH;
    }

    public String getSlowMotionPath(String str, Storage.StorageType storageType) {
        return this.mSlowMotionPathBuilder.get(getDcimDirectory(storageType), str, System.currentTimeMillis(), storageType);
    }

    public UpdateInterval calculateNextPollingInterval(Storage.StorageType storageType) {
        return UpdateInterval.decide(this.mStorageController.getAvailableStorageSize(storageType));
    }

    public synchronized void doResume() {
        synchronized (this.mReadyStateLock) {
            this.mIsApplicationForeground = true;
        }
        for (Storage.StorageType storageType : StorageUtil.getMountableStorageTypes()) {
            synchronized (this.mReadyStateLock) {
                if (this.mStorageController.getStorageReadyState(storageType) == Storage.StorageReadyState.SUSPENDED) {
                    changeReadyStateTo(storageType, Storage.StorageReadyState.PREPARING, UpdateRequestReason.APP_LAUNCH);
                }
            }
            this.mStorageUpdaterMap.get(storageType).setAutoUpdateEnabled(true);
        }
    }

    public synchronized void doPause() {
        synchronized (this.mReadyStateLock) {
            this.mIsApplicationForeground = false;
        }
        for (Storage.StorageType storageType : StorageUtil.getMountableStorageTypes()) {
            synchronized (this.mReadyStateLock) {
                if (this.mStorageController.getStorageReadyState(storageType) == Storage.StorageReadyState.COMPLETED) {
                    changeReadyStateTo(storageType, Storage.StorageReadyState.SUSPENDED, UpdateRequestReason.APP_CLOSE);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x00e4  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00e7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean changeReadyStateTo(com.sonyericsson.cameracommon.storage.Storage.StorageType r7, com.sonyericsson.cameracommon.storage.Storage.StorageReadyState r8, com.sonyericsson.cameracommon.storage.CameraStorageManager.UpdateRequestReason r9) {
        /*
            Method dump skipped, instructions count: 256
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sonyericsson.cameracommon.storage.CameraStorageManager.changeReadyStateTo(com.sonyericsson.cameracommon.storage.Storage$StorageType, com.sonyericsson.cameracommon.storage.Storage$StorageReadyState, com.sonyericsson.cameracommon.storage.CameraStorageManager$UpdateRequestReason):boolean");
    }
}
