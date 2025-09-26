// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import java.util.ArrayList;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import java.util.Collections;
import java.util.Comparator;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import java.util.List;
import com.sonyericsson.android.camera.configuration.parameters.AspectRatio;

public class AspectRatioApplier extends DependencyApplier
{
    public static final String TAG = "AspectRatioApplier";
    private final AspectRatio mValue;
    
    public AspectRatioApplier(final AspectRatio mValue) {
        this.mValue = mValue;
    }
    
    private void sortResolutionList(final List<Resolution> list) {
        Collections.sort((List<Object>)list, (Comparator<? super Object>)new Comparator<Resolution>(this) {
            final AspectRatioApplier this$0;
            
            @Override
            public int compare(final Resolution resolution, final Resolution resolution2) {
                return -(resolution.getPictureRect().width() - resolution2.getPictureRect().width());
            }
        });
    }
    
    @Override
    public void apply(final CapturingModeParams capturingModeParams) {
        final Resolution resolution = capturingModeParams.mResolution.get();
        final int width = resolution.getPictureRect().width();
        final int height = resolution.getPictureRect().height();
        if (AspectRatio.getAspectRatio(width, height) == this.mValue) {
            return;
        }
        final ArrayList list = new ArrayList();
        final ArrayList list2 = new ArrayList();
        final AspectRatio aspectRatio = AspectRatio.getAspectRatio(width, height);
        for (final Resolution resolution2 : Resolution.getOptions(capturingModeParams.mCapturingMode.get())) {
            final int width2 = resolution2.getPictureRect().width();
            final int height2 = resolution2.getPictureRect().height();
            if (AspectRatio.getAspectRatio(width2, height2) == aspectRatio) {
                list.add(resolution2);
            }
            else if (AspectRatio.getAspectRatio(width2, height2) == this.mValue) {
                list2.add(resolution2);
            }
        }
        this.sortResolutionList(list);
        this.sortResolutionList(list2);
        if (list2.size() > 0) {
            final int index = list.indexOf(resolution);
            if (list2.size() > index) {
                capturingModeParams.mResolution.applyRecommendedValue((Resolution)list2.get(index));
            }
            else {
                capturingModeParams.mResolution.applyRecommendedValue((Resolution)list2.get(list2.size() - 1));
            }
        }
    }
    
    @Override
    public void reset(final CapturingModeParams capturingModeParams) {
        capturingModeParams.mResolution.reset();
    }
}
