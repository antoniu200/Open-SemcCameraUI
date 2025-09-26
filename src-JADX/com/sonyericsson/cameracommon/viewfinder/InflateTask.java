package com.sonyericsson.cameracommon.viewfinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.cameracommon.utility.MeasurePerformance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class InflateTask implements Callable<Map<InflateItem, List<View>>> {
    public static final String TAG = "InflateTask";
    private final List<InflateItem> mInflateItemList;
    private Map<InflateItem, List<View>> mInflatedItemMap = new HashMap();
    private LayoutInflater mLayoutInflater;

    public InflateTask(LayoutInflater layoutInflater, List<InflateItem> list) {
        this.mLayoutInflater = layoutInflater;
        this.mInflateItemList = list;
    }

    @Override // java.util.concurrent.Callable
    public Map<InflateItem, List<View>> call() {
        if (CamLog.VERBOSE) {
            CamLog.d("InflateTask.call in");
        }
        if (CamLog.VERBOSE) {
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.INFLATE_VIEWS, true);
        }
        PerfLog.TASK_INFLATE.begin();
        Iterator<InflateItem> it = this.mInflateItemList.iterator();
        while (it.hasNext()) {
            register(it.next());
        }
        PerfLog.TASK_INFLATE.end();
        if (CamLog.VERBOSE) {
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.INFLATE_VIEWS, false);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("InflateTask.call out");
        }
        return this.mInflatedItemMap;
    }

    private void register(InflateItem inflateItem) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < inflateItem.getViewCount(); i++) {
            arrayList.add(this.mLayoutInflater.inflate(inflateItem.getLayoutId(), (ViewGroup) null));
        }
        this.mInflatedItemMap.put(inflateItem, arrayList);
    }
}
