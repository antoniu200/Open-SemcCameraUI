// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.viewfinder;

import java.util.Iterator;
import com.sonyericsson.android.camera.util.PerfLog;
import com.sonyericsson.cameracommon.utility.MeasurePerformance;
import com.sonyericsson.android.camera.util.CamLog;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.HashMap;
import android.view.LayoutInflater;
import android.view.View;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class InflateTask implements Callable<Map<InflateItem, List<View>>>
{
    public static final String TAG = "InflateTask";
    private final List<InflateItem> mInflateItemList;
    private Map<InflateItem, List<View>> mInflatedItemMap;
    private LayoutInflater mLayoutInflater;
    
    public InflateTask(final LayoutInflater mLayoutInflater, final List<InflateItem> mInflateItemList) {
        this.mLayoutInflater = mLayoutInflater;
        this.mInflateItemList = mInflateItemList;
        this.mInflatedItemMap = new HashMap<InflateItem, List<View>>();
    }
    
    private void register(final InflateItem inflateItem) {
        final ArrayList list = new ArrayList();
        for (int i = 0; i < inflateItem.getViewCount(); ++i) {
            list.add(this.mLayoutInflater.inflate(inflateItem.getLayoutId(), (ViewGroup)null));
        }
        this.mInflatedItemMap.put(inflateItem, list);
    }
    
    @Override
    public Map<InflateItem, List<View>> call() {
        if (CamLog.VERBOSE) {
            CamLog.d("InflateTask.call in");
        }
        if (CamLog.VERBOSE) {
            MeasurePerformance.measureTime(MeasurePerformance.PerformanceIds.INFLATE_VIEWS, true);
        }
        PerfLog.TASK_INFLATE.begin();
        final Iterator<InflateItem> iterator = this.mInflateItemList.iterator();
        while (iterator.hasNext()) {
            this.register(iterator.next());
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
}
