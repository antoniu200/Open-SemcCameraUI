package com.sonyericsson.android.camera.parameter.dependency;

import com.sonyericsson.android.camera.configuration.parameters.AspectRatio;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Resolution;
import com.sonyericsson.android.camera.parameter.CapturingModeParams;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class AspectRatioApplier extends DependencyApplier {
    public static final String TAG = "AspectRatioApplier";
    private final AspectRatio mValue;

    public AspectRatioApplier(AspectRatio aspectRatio) {
        this.mValue = aspectRatio;
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void apply(CapturingModeParams capturingModeParams) {
        Resolution resolution = (Resolution) capturingModeParams.mResolution.get();
        int iWidth = resolution.getPictureRect().width();
        int iHeight = resolution.getPictureRect().height();
        if (AspectRatio.getAspectRatio(iWidth, iHeight) == this.mValue) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        AspectRatio aspectRatio = AspectRatio.getAspectRatio(iWidth, iHeight);
        for (Resolution resolution2 : Resolution.getOptions((CapturingMode) capturingModeParams.mCapturingMode.get())) {
            int iWidth2 = resolution2.getPictureRect().width();
            int iHeight2 = resolution2.getPictureRect().height();
            if (AspectRatio.getAspectRatio(iWidth2, iHeight2) == aspectRatio) {
                arrayList.add(resolution2);
            } else if (AspectRatio.getAspectRatio(iWidth2, iHeight2) == this.mValue) {
                arrayList2.add(resolution2);
            }
        }
        sortResolutionList(arrayList);
        sortResolutionList(arrayList2);
        if (arrayList2.size() > 0) {
            int iIndexOf = arrayList.indexOf(resolution);
            if (arrayList2.size() > iIndexOf) {
                capturingModeParams.mResolution.applyRecommendedValue(arrayList2.get(iIndexOf));
            } else {
                capturingModeParams.mResolution.applyRecommendedValue(arrayList2.get(arrayList2.size() - 1));
            }
        }
    }

    private void sortResolutionList(List<Resolution> list) {
        Collections.sort(list, new Comparator<Resolution>() { // from class: com.sonyericsson.android.camera.parameter.dependency.AspectRatioApplier.1
            @Override // java.util.Comparator
            public int compare(Resolution resolution, Resolution resolution2) {
                return -(resolution.getPictureRect().width() - resolution2.getPictureRect().width());
            }
        });
    }

    @Override // com.sonyericsson.android.camera.parameter.dependency.DependencyApplier
    public void reset(CapturingModeParams capturingModeParams) {
        capturingModeParams.mResolution.reset();
    }
}
