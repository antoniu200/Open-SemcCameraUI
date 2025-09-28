package com.sonyericsson.cameracommon.utility;

import android.annotation.SuppressLint;
import android.os.Environment;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.capability.SharedPrefsTranslator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class MeasurePerformance {
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
    private static boolean sMemoryFlag = false;
    private static boolean sTimerFlag = false;
    private long originalTime;
    private static List<MeasureTime> mTimeList = Collections.synchronizedList(new ArrayList());
    private static List<MeasureResource> mResourceList = Collections.synchronizedList(new ArrayList());
    private static MeasurePerformance sInstance = new MeasurePerformance();

    enum MeasureKind {
        MEASURE_START,
        MEASURE_START_OVERWRITE,
        MEASURE_END
    }

    public static final void outResultDelay(int i) {
    }

    public enum PerformanceIds {
        ON_CREATE("[SEQ][START UP]"),
        ON_START(MeasurePerformance.TAG_STARTUP),
        ON_RESTART(MeasurePerformance.TAG_STARTUP),
        ON_RESUME("[SEQ][START UP]"),
        ON_PAUSE(MeasurePerformance.TAG_STARTUP),
        ON_STOP(MeasurePerformance.TAG_STARTUP),
        ON_DESTROY(MeasurePerformance.TAG_STARTUP),
        NOTIFY_STORE_COMPLETE(""),
        MSG_ON_STORE_CALLBACK_START(""),
        MSG_ON_STORE_CALLBACK_END(""),
        HANDLE_EVENT(""),
        SET_CONTENT_VIEWS(""),
        INFLATE_VIEWS(MeasurePerformance.TAG_TASK),
        STARTUP_TIME(MeasurePerformance.TAG_SEQ),
        ON_RESUME_TO_SURFACE_CHANGED(MeasurePerformance.TAG_SURFACE),
        SURFACE_CHANGED(MeasurePerformance.TAG_SURFACE),
        SURFACE_CHANGED_TO_LAUNCH(MeasurePerformance.TAG_SURFACE),
        RESUME_TO_LAUNCH(MeasurePerformance.TAG_SEQ),
        LAUNCH(MeasurePerformance.TAG_SEQ),
        LAUNCH_TO_DISPATCH_DRAW(MeasurePerformance.TAG_SEQ),
        OPEN_CAMERA_DEVICE_TASK("[TASK][DEVICE]"),
        SWITCH_CAMERA_DEVICE(MeasurePerformance.TAG_DEVICE),
        SETUP_CAMERA_DEVICE_TASK(MeasurePerformance.TAG_TASK),
        CREATE_EFFECT_RENDERER_PACK(MeasurePerformance.TAG_TASK),
        LAZY_INITIALIZATION_TASK(MeasurePerformance.TAG_TASK),
        SETTUP_RELATED_TO_SURFACE_SIZE(MeasurePerformance.TAG_TASK),
        STOT_TO_SHOT(MeasurePerformance.TAG_SHOT),
        STOT_TO_ON_PICT_TAKEN(MeasurePerformance.TAG_SHOT),
        RECORDING_START(MeasurePerformance.TAG_SHOT),
        RECORDING_STOP(MeasurePerformance.TAG_SHOT),
        STORE_DATA_INTO_SD_CARD(""),
        UPDATE_REMAIN("");

        String tag;

        PerformanceIds(String str) {
            this.tag = str;
        }
    }

    private static class MeasureTime {
        public String additionalInfo;
        public PerformanceIds id;
        public MeasureKind kind;
        public long time;

        private MeasureTime() {
        }
    }

    private static class MeasureAmountTime {
        public int count;
        public PerformanceIds id;
        public long total;

        private MeasureAmountTime() {
        }
    }

    private static class MeasureResource {
        public long mFreeMemory;
        public String mTag;
        public long mTotalMemory;

        private MeasureResource() {
        }
    }

    protected MeasurePerformance() {
        if (CamLog.VERBOSE) {
            CamLog.d("MeasurePerformance() is called.");
        }
        init();
        this.originalTime = System.currentTimeMillis();
    }

    private static void init() {
        mTimeList.clear();
        mResourceList.clear();
    }

    public static final void setTimerFlag(boolean z) {
        sTimerFlag = z;
    }

    public static final void setMemoryFlag(boolean z) {
        sMemoryFlag = z;
    }

    private static final void measureTime(PerformanceIds performanceIds, MeasureKind measureKind, String str) {
        if (sTimerFlag) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            MeasureTime measureTime = new MeasureTime();
            measureTime.id = performanceIds;
            measureTime.additionalInfo = str;
            measureTime.time = jCurrentTimeMillis;
            measureTime.kind = measureKind;
            mTimeList.add(measureTime);
        }
    }

    private static final boolean isLastMeasuredTime(List<MeasureTime> list, int i) {
        MeasureTime measureTime = list.get(i);
        for (int i2 = i + 1; i2 < list.size(); i2++) {
            MeasureTime measureTime2 = list.get(i2);
            if (measureTime2.kind == measureTime.kind && measureTime2.id == measureTime.id) {
                return false;
            }
        }
        return true;
    }

    public static final void measureTime(PerformanceIds performanceIds, boolean z, String str) {
        measureTime(performanceIds, z ? MeasureKind.MEASURE_START : MeasureKind.MEASURE_END, str);
    }

    public static final void measureTime(PerformanceIds performanceIds, boolean z) {
        measureTime(performanceIds, z ? MeasureKind.MEASURE_START : MeasureKind.MEASURE_END, "");
    }

    public static final void measureTimeOverwrite(PerformanceIds performanceIds, boolean z) {
        measureTime(performanceIds, z ? MeasureKind.MEASURE_START_OVERWRITE : MeasureKind.MEASURE_END, "");
    }

    public static final void measureResource(int i) {
        if (sMemoryFlag) {
            MeasureResource measureResource = new MeasureResource();
            Runtime runtime = Runtime.getRuntime();
            long j = runtime.totalMemory();
            long jFreeMemory = runtime.freeMemory();
            measureResource.mTotalMemory = j;
            measureResource.mFreeMemory = jFreeMemory;
            mResourceList.add(measureResource);
        }
    }

    public static final void measureResource(String str) {
        if (sMemoryFlag) {
            MeasureResource measureResource = new MeasureResource();
            Runtime runtime = Runtime.getRuntime();
            long j = runtime.totalMemory();
            long jFreeMemory = runtime.freeMemory();
            measureResource.mTag = str;
            measureResource.mTotalMemory = j;
            measureResource.mFreeMemory = jFreeMemory;
            mResourceList.add(measureResource);
        }
    }

    public static final void outResult() throws IOException {
        if (sTimerFlag || sMemoryFlag) {
            ArrayList arrayList = new ArrayList(mTimeList);
            ArrayList arrayList2 = new ArrayList(mResourceList);
            String str = "";
            init();
            if (sTimerFlag) {
                str = "" + setResultTime(arrayList);
            }
            if (sMemoryFlag) {
                str = str + setResultResource(arrayList2);
            }
            writeFile(str);
        }
    }

    private static class OutResultDelayTask implements Runnable {
        private OutResultDelayTask() {
        }

        @Override // java.lang.Runnable
        public void run() throws IOException {
            MeasurePerformance.outResult();
        }
    }

    private static String setResultTime(List<MeasureTime> list) {
        MeasureTime[] measureTimeArr = new MeasureTime[PerformanceIds.values().length];
        MeasureAmountTime[] measureAmountTimeArr = new MeasureAmountTime[PerformanceIds.values().length];
        int i = 0;
        while (true) {
            if (i >= PerformanceIds.values().length) {
                break;
            }
            measureAmountTimeArr[i] = new MeasureAmountTime();
            i++;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("---Measure Time Start---\n");
        sb.append("VERBOSE_PERFORM_TIME_TAGID,Time[ms],Comment\n");
        for (int i2 = 0; i2 < list.size(); i2++) {
            MeasureTime measureTime = list.get(i2);
            if (measureTime.kind == MeasureKind.MEASURE_START) {
                if (measureTimeArr[measureTime.id.ordinal()] == null) {
                    measureTimeArr[measureTime.id.ordinal()] = measureTime;
                }
            } else if (measureTime.kind == MeasureKind.MEASURE_START_OVERWRITE) {
                measureTimeArr[measureTime.id.ordinal()] = measureTime;
            } else if (measureTimeArr[measureTime.id.ordinal()] != null) {
                measureAmountTimeArr[measureTime.id.ordinal()].id = measureTime.id;
                measureAmountTimeArr[measureTime.id.ordinal()].total += measureTime.time - measureTimeArr[measureTime.id.ordinal()].time;
                measureAmountTimeArr[measureTime.id.ordinal()].count++;
                if (isLastMeasuredTime(list, i2)) {
                    sb.append(DEBUG_PERFORM_TIME_TAG);
                    sb.append(measureTime.id.tag);
                    sb.append(measureTime.id.name());
                    sb.append(", ");
                    sb.append(Long.toString(measureTime.time - measureTimeArr[measureTime.id.ordinal()].time));
                    sb.append(", ");
                    sb.append(measureTime.additionalInfo);
                    sb.append(measureTimeArr[measureTime.id.ordinal()].additionalInfo);
                    sb.append("\n");
                    measureTimeArr[measureTime.id.ordinal()] = null;
                }
            }
        }
        sb.append("VERBOSE_PERFORM_TIME_TAG---Measure Time End---\n\n");
        sb.append("---Measure Time Dump Start---\n");
        sb.append("ID,Type(1:Start/2:End),SytemTime,RelativeTime,Comment\n");
        for (int i3 = 0; i3 < list.size(); i3++) {
            MeasureTime measureTime2 = list.get(i3);
            sb.append(measureTime2.id.name());
            sb.append(", ");
            sb.append(measureTime2.kind);
            sb.append(", ");
            sb.append(Long.toString(measureTime2.time));
            sb.append(", ");
            sb.append(Long.toString(measureTime2.time - sInstance.originalTime));
            sb.append(", ");
            sb.append(measureTime2.additionalInfo);
            sb.append("\n");
        }
        for (int i4 = 0; i4 < PerformanceIds.values().length; i4++) {
            MeasureAmountTime measureAmountTime = measureAmountTimeArr[i4];
            if (measureAmountTime.id != null) {
                sb.append("AmountTime: ");
                sb.append(measureAmountTime.id.name());
                sb.append(" total: ");
                sb.append(Long.toString(measureAmountTime.total));
                sb.append(", count: ");
                sb.append(Long.toString(measureAmountTime.count));
                sb.append(", avalage: ");
                sb.append(Long.toString(measureAmountTime.total / measureAmountTime.count));
                sb.append("\n");
            }
        }
        sb.append("---Measure Time Dump End---\n");
        return sb.toString();
    }

    private static String setResultResource(List<MeasureResource> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("---Measure Resource Start---\n");
        sb.append("ID,Total,Used,free\n");
        for (int i = 0; list.size() > i; i++) {
            String str = list.get(i).mTag;
            long j = list.get(i).mTotalMemory;
            long j2 = list.get(i).mFreeMemory;
            sb.append(str + "," + j + "," + (j - j2) + "," + j2 + "\n");
        }
        sb.append("---Measure Resource End---\n");
        return sb.toString();
    }

    @SuppressLint({"NewApi"})
    private static void writeFile(String str) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory() + SharedPrefsTranslator.CONNECTOR_SLASH + "camera_perform.csv");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException unused) {
                CamLog.e("Create output file failed");
                return;
            }
        }
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(Environment.getExternalStorageDirectory() + SharedPrefsTranslator.CONNECTOR_SLASH + "camera_perform.csv"));
            try {
                outputStreamWriter.write(str);
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
            } finally {
            }
        } catch (FileNotFoundException e) {
            CamLog.e("[MeasurePerformance::writeFile]:Error = " + e);
        } catch (IOException e2) {
            CamLog.e("[MeasurePerformance::writeFile]:Error = " + e2);
        }
    }
}
