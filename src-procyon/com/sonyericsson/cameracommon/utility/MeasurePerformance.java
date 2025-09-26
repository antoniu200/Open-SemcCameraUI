// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.utility;

import android.annotation.SuppressLint;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import android.os.Environment;
import java.util.Collection;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class MeasurePerformance
{
    private static final String DEBUG_PERFORM_FILE = "camera_perform.csv";
    private static final boolean DEBUG_PERFORM_MEM = false;
    private static final String DEBUG_PERFORM_TIME_TAG = "VERBOSE_PERFORM_TIME_TAG";
    private static final String FILE = "camera_perform.csv";
    public static final String TAG = "MeasurePerformance";
    public static final String TAG_DEVICE = "[DEVICE]";
    public static final String TAG_SEQ = "[SEQ]";
    public static final String TAG_SHOT = "[SHOT]";
    public static final String TAG_STARTUP = "[START UP]";
    public static final String TAG_SURFACE = "[SURFACE]";
    public static final String TAG_TASK = "[TASK]";
    private static List<MeasureResource> mResourceList;
    private static List<MeasureTime> mTimeList;
    private static MeasurePerformance sInstance;
    private static boolean sMemoryFlag = false;
    private static boolean sTimerFlag = false;
    private long originalTime;
    
    static {
        MeasurePerformance.mTimeList = Collections.synchronizedList(new ArrayList<MeasureTime>());
        MeasurePerformance.mResourceList = Collections.synchronizedList(new ArrayList<MeasureResource>());
        MeasurePerformance.sInstance = new MeasurePerformance();
    }
    
    protected MeasurePerformance() {
        if (CamLog.VERBOSE) {
            CamLog.d("MeasurePerformance() is called.");
        }
        init();
        this.originalTime = System.currentTimeMillis();
    }
    
    private static void init() {
        MeasurePerformance.mTimeList.clear();
        MeasurePerformance.mResourceList.clear();
    }
    
    private static final boolean isLastMeasuredTime(final List<MeasureTime> list, int i) {
        final MeasureTime measureTime = list.get(i);
        ++i;
        while (i < list.size()) {
            final MeasureTime measureTime2 = list.get(i);
            if (measureTime2.kind == measureTime.kind && measureTime2.id == measureTime.id) {
                return false;
            }
            ++i;
        }
        return true;
    }
    
    public static final void measureResource(final int n) {
        if (!MeasurePerformance.sMemoryFlag) {
            return;
        }
        final MeasureResource measureResource = new MeasureResource();
        final Runtime runtime = Runtime.getRuntime();
        final long totalMemory = runtime.totalMemory();
        final long freeMemory = runtime.freeMemory();
        measureResource.mTotalMemory = totalMemory;
        measureResource.mFreeMemory = freeMemory;
        MeasurePerformance.mResourceList.add(measureResource);
    }
    
    public static final void measureResource(final String mTag) {
        if (!MeasurePerformance.sMemoryFlag) {
            return;
        }
        final MeasureResource measureResource = new MeasureResource();
        final Runtime runtime = Runtime.getRuntime();
        final long totalMemory = runtime.totalMemory();
        final long freeMemory = runtime.freeMemory();
        measureResource.mTag = mTag;
        measureResource.mTotalMemory = totalMemory;
        measureResource.mFreeMemory = freeMemory;
        MeasurePerformance.mResourceList.add(measureResource);
    }
    
    private static final void measureTime(final PerformanceIds id, final MeasureKind kind, final String additionalInfo) {
        if (!MeasurePerformance.sTimerFlag) {
            return;
        }
        final long currentTimeMillis = System.currentTimeMillis();
        final MeasureTime measureTime = new MeasureTime();
        measureTime.id = id;
        measureTime.additionalInfo = additionalInfo;
        measureTime.time = currentTimeMillis;
        measureTime.kind = kind;
        MeasurePerformance.mTimeList.add(measureTime);
    }
    
    public static final void measureTime(final PerformanceIds performanceIds, final boolean b) {
        MeasureKind measureKind;
        if (b) {
            measureKind = MeasureKind.MEASURE_START;
        }
        else {
            measureKind = MeasureKind.MEASURE_END;
        }
        measureTime(performanceIds, measureKind, "");
    }
    
    public static final void measureTime(final PerformanceIds performanceIds, final boolean b, final String s) {
        MeasureKind measureKind;
        if (b) {
            measureKind = MeasureKind.MEASURE_START;
        }
        else {
            measureKind = MeasureKind.MEASURE_END;
        }
        measureTime(performanceIds, measureKind, s);
    }
    
    public static final void measureTimeOverwrite(final PerformanceIds performanceIds, final boolean b) {
        MeasureKind measureKind;
        if (b) {
            measureKind = MeasureKind.MEASURE_START_OVERWRITE;
        }
        else {
            measureKind = MeasureKind.MEASURE_END;
        }
        measureTime(performanceIds, measureKind, "");
    }
    
    public static final void outResult() {
        if (!MeasurePerformance.sTimerFlag && !MeasurePerformance.sMemoryFlag) {
            return;
        }
        final ArrayList resultTime = new ArrayList((Collection<? extends E>)MeasurePerformance.mTimeList);
        final ArrayList resultResource = new ArrayList((Collection<? extends E>)MeasurePerformance.mResourceList);
        String string = "";
        init();
        if (MeasurePerformance.sTimerFlag) {
            final StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(setResultTime(resultTime));
            string = sb.toString();
        }
        String string2 = string;
        if (MeasurePerformance.sMemoryFlag) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string);
            sb2.append(setResultResource(resultResource));
            string2 = sb2.toString();
        }
        writeFile(string2);
    }
    
    public static final void outResultDelay(final int n) {
    }
    
    public static final void setMemoryFlag(final boolean sMemoryFlag) {
        MeasurePerformance.sMemoryFlag = sMemoryFlag;
    }
    
    private static String setResultResource(final List<MeasureResource> list) {
        final StringBuilder sb = new StringBuilder();
        sb.append("---Measure Resource Start---\n");
        sb.append("ID,Total,Used,free\n");
        for (int n = 0; list.size() > n; ++n) {
            final String mTag = list.get(n).mTag;
            final long mTotalMemory = list.get(n).mTotalMemory;
            final long mFreeMemory = list.get(n).mFreeMemory;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(mTag);
            sb2.append(",");
            sb2.append(mTotalMemory);
            sb2.append(",");
            sb2.append(mTotalMemory - mFreeMemory);
            sb2.append(",");
            sb2.append(mFreeMemory);
            sb2.append("\n");
            sb.append(sb2.toString());
        }
        sb.append("---Measure Resource End---\n");
        return sb.toString();
    }
    
    private static String setResultTime(final List<MeasureTime> list) {
        final MeasureTime[] array = new MeasureTime[PerformanceIds.values().length];
        final MeasureAmountTime[] array2 = new MeasureAmountTime[PerformanceIds.values().length];
        final int n = 0;
        for (int i = 0; i < PerformanceIds.values().length; ++i) {
            array2[i] = new MeasureAmountTime();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("---Measure Time Start---\n");
        sb.append("VERBOSE_PERFORM_TIME_TAGID,Time[ms],Comment\n");
        for (int j = 0; j < list.size(); ++j) {
            final MeasureTime measureTime = list.get(j);
            if (measureTime.kind == MeasureKind.MEASURE_START) {
                if (array[measureTime.id.ordinal()] == null) {
                    array[measureTime.id.ordinal()] = measureTime;
                }
            }
            else if (measureTime.kind == MeasureKind.MEASURE_START_OVERWRITE) {
                array[measureTime.id.ordinal()] = measureTime;
            }
            else if (array[measureTime.id.ordinal()] != null) {
                array2[measureTime.id.ordinal()].id = measureTime.id;
                final MeasureAmountTime measureAmountTime = array2[measureTime.id.ordinal()];
                measureAmountTime.total += measureTime.time - array[measureTime.id.ordinal()].time;
                final MeasureAmountTime measureAmountTime2 = array2[measureTime.id.ordinal()];
                ++measureAmountTime2.count;
                if (isLastMeasuredTime(list, j)) {
                    sb.append("VERBOSE_PERFORM_TIME_TAG");
                    sb.append(measureTime.id.tag);
                    sb.append(measureTime.id.name());
                    sb.append(", ");
                    sb.append(Long.toString(measureTime.time - array[measureTime.id.ordinal()].time));
                    sb.append(", ");
                    sb.append(measureTime.additionalInfo);
                    sb.append(array[measureTime.id.ordinal()].additionalInfo);
                    sb.append("\n");
                    array[measureTime.id.ordinal()] = null;
                }
            }
        }
        sb.append("VERBOSE_PERFORM_TIME_TAG---Measure Time End---\n\n");
        sb.append("---Measure Time Dump Start---\n");
        sb.append("ID,Type(1:Start/2:End),SytemTime,RelativeTime,Comment\n");
        int n2 = 0;
        int k;
        while (true) {
            k = n;
            if (n2 >= list.size()) {
                break;
            }
            final MeasureTime measureTime2 = list.get(n2);
            sb.append(measureTime2.id.name());
            sb.append(", ");
            sb.append(measureTime2.kind);
            sb.append(", ");
            sb.append(Long.toString(measureTime2.time));
            sb.append(", ");
            sb.append(Long.toString(measureTime2.time - MeasurePerformance.sInstance.originalTime));
            sb.append(", ");
            sb.append(measureTime2.additionalInfo);
            sb.append("\n");
            ++n2;
        }
        while (k < PerformanceIds.values().length) {
            final MeasureAmountTime measureAmountTime3 = array2[k];
            if (measureAmountTime3.id != null) {
                sb.append("AmountTime: ");
                sb.append(measureAmountTime3.id.name());
                sb.append(" total: ");
                sb.append(Long.toString(measureAmountTime3.total));
                sb.append(", count: ");
                sb.append(Long.toString(measureAmountTime3.count));
                sb.append(", avalage: ");
                sb.append(Long.toString(measureAmountTime3.total / measureAmountTime3.count));
                sb.append("\n");
            }
            ++k;
        }
        sb.append("---Measure Time Dump End---\n");
        return sb.toString();
    }
    
    public static final void setTimerFlag(final boolean sTimerFlag) {
        MeasurePerformance.sTimerFlag = sTimerFlag;
    }
    
    @SuppressLint({ "NewApi" })
    private static void writeFile(final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory());
        sb.append("/");
        sb.append("camera_perform.csv");
        final File file = new File(sb.toString());
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (final IOException ex) {
                CamLog.e("Create output file failed");
                return;
            }
        }
        try {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(Environment.getExternalStorageDirectory());
            sb2.append("/");
            sb2.append("camera_perform.csv");
            final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(sb2.toString()));
            final Object o = null;
            try {
                try {
                    outputStreamWriter.write(str);
                    if (outputStreamWriter != null) {
                        outputStreamWriter.close();
                    }
                    return;
                }
                finally {
                    if (outputStreamWriter != null) {
                        if (o != null) {
                            final OutputStreamWriter outputStreamWriter2 = outputStreamWriter;
                            outputStreamWriter2.close();
                        }
                        else {
                            outputStreamWriter.close();
                        }
                    }
                }
            }
            catch (final Throwable t) {}
            try {
                final OutputStreamWriter outputStreamWriter2 = outputStreamWriter;
                outputStreamWriter2.close();
            }
            catch (final Throwable t2) {}
        }
        catch (final IOException obj) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("[MeasurePerformance::writeFile]:Error = ");
            sb3.append(obj);
            CamLog.e(sb3.toString());
        }
        catch (final FileNotFoundException obj2) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("[MeasurePerformance::writeFile]:Error = ");
            sb4.append(obj2);
            CamLog.e(sb4.toString());
        }
    }
    
    private static class MeasureAmountTime
    {
        public int count;
        public PerformanceIds id;
        public long total;
    }
    
    enum MeasureKind
    {
        private static final MeasureKind[] $VALUES;
        
        MEASURE_END, 
        MEASURE_START, 
        MEASURE_START_OVERWRITE;
        
        static {
            $VALUES = new MeasureKind[] { MeasureKind.MEASURE_START, MeasureKind.MEASURE_START_OVERWRITE, MeasureKind.MEASURE_END };
        }
    }
    
    private static class MeasureResource
    {
        public long mFreeMemory;
        public String mTag;
        public long mTotalMemory;
    }
    
    private static class MeasureTime
    {
        public String additionalInfo;
        public PerformanceIds id;
        public MeasureKind kind;
        public long time;
    }
    
    private static class OutResultDelayTask implements Runnable
    {
        @Override
        public void run() {
            MeasurePerformance.outResult();
        }
    }
    
    public enum PerformanceIds
    {
        private static final PerformanceIds[] $VALUES;
        
        CREATE_EFFECT_RENDERER_PACK("[TASK]"), 
        HANDLE_EVENT(""), 
        INFLATE_VIEWS("[TASK]"), 
        LAUNCH("[SEQ]"), 
        LAUNCH_TO_DISPATCH_DRAW("[SEQ]"), 
        LAZY_INITIALIZATION_TASK("[TASK]"), 
        MSG_ON_STORE_CALLBACK_END(""), 
        MSG_ON_STORE_CALLBACK_START(""), 
        NOTIFY_STORE_COMPLETE(""), 
        ON_CREATE("[SEQ][START UP]"), 
        ON_DESTROY("[START UP]"), 
        ON_PAUSE("[START UP]"), 
        ON_RESTART("[START UP]"), 
        ON_RESUME("[SEQ][START UP]"), 
        ON_RESUME_TO_SURFACE_CHANGED("[SURFACE]"), 
        ON_START("[START UP]"), 
        ON_STOP("[START UP]"), 
        OPEN_CAMERA_DEVICE_TASK("[TASK][DEVICE]"), 
        RECORDING_START("[SHOT]"), 
        RECORDING_STOP("[SHOT]"), 
        RESUME_TO_LAUNCH("[SEQ]"), 
        SETTUP_RELATED_TO_SURFACE_SIZE("[TASK]"), 
        SETUP_CAMERA_DEVICE_TASK("[TASK]"), 
        SET_CONTENT_VIEWS(""), 
        STARTUP_TIME("[SEQ]"), 
        STORE_DATA_INTO_SD_CARD(""), 
        STOT_TO_ON_PICT_TAKEN("[SHOT]"), 
        STOT_TO_SHOT("[SHOT]"), 
        SURFACE_CHANGED("[SURFACE]"), 
        SURFACE_CHANGED_TO_LAUNCH("[SURFACE]"), 
        SWITCH_CAMERA_DEVICE("[DEVICE]"), 
        UPDATE_REMAIN("");
        
        String tag;
        
        static {
            $VALUES = new PerformanceIds[] { PerformanceIds.ON_CREATE, PerformanceIds.ON_START, PerformanceIds.ON_RESTART, PerformanceIds.ON_RESUME, PerformanceIds.ON_PAUSE, PerformanceIds.ON_STOP, PerformanceIds.ON_DESTROY, PerformanceIds.NOTIFY_STORE_COMPLETE, PerformanceIds.MSG_ON_STORE_CALLBACK_START, PerformanceIds.MSG_ON_STORE_CALLBACK_END, PerformanceIds.HANDLE_EVENT, PerformanceIds.SET_CONTENT_VIEWS, PerformanceIds.INFLATE_VIEWS, PerformanceIds.STARTUP_TIME, PerformanceIds.ON_RESUME_TO_SURFACE_CHANGED, PerformanceIds.SURFACE_CHANGED, PerformanceIds.SURFACE_CHANGED_TO_LAUNCH, PerformanceIds.RESUME_TO_LAUNCH, PerformanceIds.LAUNCH, PerformanceIds.LAUNCH_TO_DISPATCH_DRAW, PerformanceIds.OPEN_CAMERA_DEVICE_TASK, PerformanceIds.SWITCH_CAMERA_DEVICE, PerformanceIds.SETUP_CAMERA_DEVICE_TASK, PerformanceIds.CREATE_EFFECT_RENDERER_PACK, PerformanceIds.LAZY_INITIALIZATION_TASK, PerformanceIds.SETTUP_RELATED_TO_SURFACE_SIZE, PerformanceIds.STOT_TO_SHOT, PerformanceIds.STOT_TO_ON_PICT_TAKEN, PerformanceIds.RECORDING_START, PerformanceIds.RECORDING_STOP, PerformanceIds.STORE_DATA_INTO_SD_CARD, PerformanceIds.UPDATE_REMAIN };
        }
        
        private PerformanceIds(final String tag) {
            this.tag = tag;
        }
    }
}
