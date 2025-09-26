package com.sonyericsson.android.camera.util;

import android.util.Log;
import com.sonyericsson.cameracommon.storage.RequestFactory;
import com.sonyericsson.cameracommon.storage.SavingRequest;
import com.sonyericsson.cameracommon.storage.SavingTaskManager;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CapturePerformanceLogger {
    private static final String TAG = "CapturePerformance";
    private static int sNumOfBurstTaken;
    private static final Map<Long, TimeLog> sCaptureTimeMap = new HashMap();
    private static List<TimeLog> sBurstTimeList = new ArrayList();

    private CapturePerformanceLogger() {
    }

    public static TimeLog create(RequestFactory.RequestBuilder requestBuilder) {
        TimeLog timeLog = new TimeLog(requestBuilder.mCommonStatus.savedFileType);
        synchronized (sCaptureTimeMap) {
            sCaptureTimeMap.put(Long.valueOf(requestBuilder.getDateTaken()), timeLog);
        }
        return timeLog;
    }

    public static TimeLog create(SavingRequest savingRequest) {
        TimeLog timeLog = new TimeLog();
        synchronized (sCaptureTimeMap) {
            sCaptureTimeMap.put(Long.valueOf(savingRequest.getDateTaken()), timeLog);
        }
        return timeLog;
    }

    public static TimeLog get(RequestFactory.RequestBuilder requestBuilder) {
        return sCaptureTimeMap.get(Long.valueOf(requestBuilder.getDateTaken()));
    }

    public static TimeLog get(SavingRequest savingRequest) {
        return sCaptureTimeMap.get(Long.valueOf(savingRequest.getDateTaken()));
    }

    public static void complete(RequestFactory.RequestBuilder requestBuilder) {
        sCaptureTimeMap.get(Long.valueOf(requestBuilder.getDateTaken())).log();
        sCaptureTimeMap.remove(Long.valueOf(requestBuilder.getDateTaken()));
    }

    public static void complete(SavingRequest savingRequest) {
        TimeLog timeLog = sCaptureTimeMap.get(Long.valueOf(savingRequest.getDateTaken()));
        timeLog.burstNum = savingRequest.getCaptureIdForPredictiveCapture();
        timeLog.log();
        if (SavingTaskManager.SavedFileType.BURST == savingRequest.common.savedFileType) {
            sBurstTimeList.add(timeLog);
        }
        sCaptureTimeMap.remove(Long.valueOf(savingRequest.getDateTaken()));
        if (sNumOfBurstTaken <= 0 || sNumOfBurstTaken != sBurstTimeList.size()) {
            return;
        }
        completeBurst();
        sNumOfBurstTaken = 0;
    }

    public static void setNumOfBurstTaken(int i) {
        sNumOfBurstTaken = i;
    }

    public static class TimeLog {
        public int burstNum;
        public int fileSize;
        private final SavingTaskManager.SavedFileType savingFileType;
        public long scanFileDone;
        public long shutterDone;
        public long snapshotDone;
        public long startSave;
        public long startScan;
        public long startSnapshot;
        public long writeFileDone;

        public TimeLog() {
            this.savingFileType = SavingTaskManager.SavedFileType.PHOTO;
        }

        public TimeLog(SavingTaskManager.SavedFileType savedFileType) {
            this.savingFileType = savedFileType;
        }

        void log() {
            StringBuilder sb = new StringBuilder();
            DecimalFormat decimalFormat = new DecimalFormat("#.0");
            if (AnonymousClass1.$SwitchMap$com$sonyericsson$cameracommon$storage$SavingTaskManager$SavedFileType[this.savingFileType.ordinal()] == 1) {
                sb.append("Burst: ");
                sb.append("Num = ");
                sb.append(this.burstNum);
                sb.append("\tSize = ");
                sb.append(decimalFormat.format(this.fileSize / 1048576.0d));
                sb.append(" MB");
                sb.append("\tCapture = ");
                sb.append(this.snapshotDone - this.startSnapshot);
                sb.append("\tSave = ");
                sb.append(this.writeFileDone - this.startSave);
                sb.append("\tScan = ");
                sb.append(this.scanFileDone - this.startScan);
            } else {
                sb.append("[[[");
                sb.append(this.startSnapshot);
                sb.append(',');
                sb.append(this.shutterDone - this.startSnapshot);
                sb.append(',');
                sb.append(this.snapshotDone - this.startSnapshot);
                sb.append(',');
                sb.append(this.startSave - this.startSnapshot);
                sb.append(',');
                sb.append(this.writeFileDone - this.startSnapshot);
                sb.append(',');
                sb.append(this.startScan - this.startSnapshot);
                sb.append(',');
                sb.append(this.scanFileDone - this.startSnapshot);
                sb.append("]]]");
            }
            CamLog.d(sb.toString());
        }
    }

    /* renamed from: com.sonyericsson.android.camera.util.CapturePerformanceLogger$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$sonyericsson$cameracommon$storage$SavingTaskManager$SavedFileType = new int[SavingTaskManager.SavedFileType.values().length];

        static {
            try {
                $SwitchMap$com$sonyericsson$cameracommon$storage$SavingTaskManager$SavedFileType[SavingTaskManager.SavedFileType.BURST.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    private static void completeBurst() {
        long j = 0;
        long j2 = 0;
        long j3 = 0;
        long j4 = 0;
        for (TimeLog timeLog : sBurstTimeList) {
            j += timeLog.writeFileDone - timeLog.startSave;
            j2 += timeLog.scanFileDone - timeLog.startScan;
            if (j3 != 0) {
                j4 += timeLog.snapshotDone - j3;
            }
            j3 = timeLog.snapshotDone;
        }
        long j5 = sBurstTimeList.get(0).startSnapshot;
        Log.e(TAG, "===============================================\nTime spent to capture " + sBurstTimeList.size() + " images = " + (sBurstTimeList.get(sBurstTimeList.size() - 1).scanFileDone - j5) + " [ms]\nAverage Capture Duration = " + (j4 / (sBurstTimeList.size() - 1)) + " [ms]\nAverage Save Duration = " + (j / sBurstTimeList.size()) + " [ms]\nAverage ScanFile Duration = " + (j2 / sBurstTimeList.size()) + " [ms]\n===============================================");
        sBurstTimeList.clear();
    }
}
