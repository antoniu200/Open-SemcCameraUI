// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.util;

import java.text.DecimalFormat;
import java.util.Iterator;
import android.util.Log;
import com.sonyericsson.cameracommon.storage.SavingTaskManager;
import com.sonyericsson.cameracommon.storage.SavingRequest;
import com.sonyericsson.cameracommon.storage.RequestFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class CapturePerformanceLogger
{
    private static final String TAG = "CapturePerformance";
    private static List<TimeLog> sBurstTimeList;
    private static final Map<Long, TimeLog> sCaptureTimeMap;
    private static int sNumOfBurstTaken;
    
    static {
        sCaptureTimeMap = new HashMap<Long, TimeLog>();
        CapturePerformanceLogger.sBurstTimeList = new ArrayList<TimeLog>();
    }
    
    private CapturePerformanceLogger() {
    }
    
    public static void complete(final RequestFactory.RequestBuilder requestBuilder) {
        CapturePerformanceLogger.sCaptureTimeMap.get(requestBuilder.getDateTaken()).log();
        CapturePerformanceLogger.sCaptureTimeMap.remove(requestBuilder.getDateTaken());
    }
    
    public static void complete(final SavingRequest savingRequest) {
        final TimeLog timeLog = CapturePerformanceLogger.sCaptureTimeMap.get(savingRequest.getDateTaken());
        timeLog.burstNum = savingRequest.getCaptureIdForPredictiveCapture();
        timeLog.log();
        if (SavingTaskManager.SavedFileType.BURST == savingRequest.common.savedFileType) {
            CapturePerformanceLogger.sBurstTimeList.add(timeLog);
        }
        CapturePerformanceLogger.sCaptureTimeMap.remove(savingRequest.getDateTaken());
        if (CapturePerformanceLogger.sNumOfBurstTaken > 0 && CapturePerformanceLogger.sNumOfBurstTaken == CapturePerformanceLogger.sBurstTimeList.size()) {
            completeBurst();
            CapturePerformanceLogger.sNumOfBurstTaken = 0;
        }
    }
    
    private static void completeBurst() {
        final Iterator<TimeLog> iterator = CapturePerformanceLogger.sBurstTimeList.iterator();
        final long n = 0L;
        final long n2 = 0L;
        long snapshotDone;
        long n3 = snapshotDone = n2;
        long n4 = n2;
        long n5 = n;
        while (iterator.hasNext()) {
            final TimeLog timeLog = iterator.next();
            n5 += timeLog.writeFileDone - timeLog.startSave;
            n4 += timeLog.scanFileDone - timeLog.startScan;
            long n6 = n3;
            if (snapshotDone != 0L) {
                n6 = n3 + (timeLog.snapshotDone - snapshotDone);
            }
            snapshotDone = timeLog.snapshotDone;
            n3 = n6;
        }
        final long startSnapshot = CapturePerformanceLogger.sBurstTimeList.get(0).startSnapshot;
        final long scanFileDone = CapturePerformanceLogger.sBurstTimeList.get(CapturePerformanceLogger.sBurstTimeList.size() - 1).scanFileDone;
        final StringBuilder sb = new StringBuilder();
        sb.append("===============================================");
        sb.append("\n");
        sb.append("Time spent to capture ");
        sb.append(CapturePerformanceLogger.sBurstTimeList.size());
        sb.append(" images = ");
        sb.append(scanFileDone - startSnapshot);
        sb.append(" [ms]");
        sb.append("\n");
        sb.append("Average Capture Duration = ");
        sb.append(n3 / (CapturePerformanceLogger.sBurstTimeList.size() - 1));
        sb.append(" [ms]");
        sb.append("\n");
        sb.append("Average Save Duration = ");
        sb.append(n5 / CapturePerformanceLogger.sBurstTimeList.size());
        sb.append(" [ms]");
        sb.append("\n");
        sb.append("Average ScanFile Duration = ");
        sb.append(n4 / CapturePerformanceLogger.sBurstTimeList.size());
        sb.append(" [ms]");
        sb.append("\n");
        sb.append("===============================================");
        Log.e("CapturePerformance", sb.toString());
        CapturePerformanceLogger.sBurstTimeList.clear();
    }
    
    public static TimeLog create(final RequestFactory.RequestBuilder requestBuilder) {
        final TimeLog timeLog = new TimeLog(requestBuilder.mCommonStatus.savedFileType);
        synchronized (CapturePerformanceLogger.sCaptureTimeMap) {
            CapturePerformanceLogger.sCaptureTimeMap.put(requestBuilder.getDateTaken(), timeLog);
            return timeLog;
        }
    }
    
    public static TimeLog create(final SavingRequest savingRequest) {
        final TimeLog timeLog = new TimeLog();
        synchronized (CapturePerformanceLogger.sCaptureTimeMap) {
            CapturePerformanceLogger.sCaptureTimeMap.put(savingRequest.getDateTaken(), timeLog);
            return timeLog;
        }
    }
    
    public static TimeLog get(final RequestFactory.RequestBuilder requestBuilder) {
        return CapturePerformanceLogger.sCaptureTimeMap.get(requestBuilder.getDateTaken());
    }
    
    public static TimeLog get(final SavingRequest savingRequest) {
        return CapturePerformanceLogger.sCaptureTimeMap.get(savingRequest.getDateTaken());
    }
    
    public static void setNumOfBurstTaken(final int sNumOfBurstTaken) {
        CapturePerformanceLogger.sNumOfBurstTaken = sNumOfBurstTaken;
    }
    
    public static class TimeLog
    {
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
        
        public TimeLog(final SavingTaskManager.SavedFileType savingFileType) {
            this.savingFileType = savingFileType;
        }
        
        void log() {
            final StringBuilder sb = new StringBuilder();
            final DecimalFormat decimalFormat = new DecimalFormat("#.0");
            if (CapturePerformanceLogger$1.$SwitchMap$com$sonyericsson$cameracommon$storage$SavingTaskManager$SavedFileType[this.savingFileType.ordinal()] != 1) {
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
            else {
                sb.append("Burst: ");
                sb.append("Num = ");
                sb.append(this.burstNum);
                sb.append("\tSize = ");
                sb.append(decimalFormat.format(this.fileSize / 1048576.0));
                sb.append(" MB");
                sb.append("\tCapture = ");
                sb.append(this.snapshotDone - this.startSnapshot);
                sb.append("\tSave = ");
                sb.append(this.writeFileDone - this.startSave);
                sb.append("\tScan = ");
                sb.append(this.scanFileDone - this.startScan);
            }
            CamLog.d(sb.toString());
        }
    }
}
