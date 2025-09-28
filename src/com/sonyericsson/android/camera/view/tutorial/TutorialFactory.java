package com.sonyericsson.android.camera.view.tutorial;

import com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView;
import com.sonyericsson.android.camera.view.tutorial.SimpleTutorialContentView;
import com.sonyericsson.android.camera.view.tutorial.TutorialContentView;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class TutorialFactory {
    public TutorialContentView.TutorialContent create(int i, List<TutorialController.TutorialType> list, List<TutorialContentView.TutorialContent> list2) {
        return new PagingTutorialContentView.CustomTutorialContent(i, list, list2);
    }

    public TutorialContentView.TutorialContent create(TutorialController.TutorialType tutorialType, int i) {
        switch (tutorialType) {
            case SAVE_LOCATION:
                return new SimpleTutorialContentView.SaveLocationTutorialContent(i);
            case SIDE_SENSE:
                return new PagingTutorialContentView.SideSenseTutorialContent(i);
            case PREDICTIVE_LAUNCH:
                return new PagingTutorialContentView.PredictiveLaunchTutorialContent(i);
            case DUAL_CAMERA:
                return new PagingTutorialContentView.DualCameraTutorialContent(i);
            case EYE_GUIDE:
                return new PagingTutorialContentView.EyeGuideTutorialContent(i);
            case HAND_SHUTTER:
                return new PagingTutorialContentView.HandShutterTutorialContent(i);
            case SUPER_SLOW_MOTION_MORE_OPTIONS:
                return new PagingTutorialContentView.SuperSlowMoreOptionsTutorialContent(i);
            case SUPER_SLOW_MOTION:
                return new PagingTutorialContentView.SuperSlowTutorialContent(i);
            case SUPER_SLOW_MOTION_SHOT:
                return new PagingTutorialContentView.OneShotSlowTutorialContent(i, 960);
            case STANDARD_SLOW_MOTION:
                return new PagingTutorialContentView.StandardSlowTutorialContent(i);
            case MANUAL_FUSION:
                return new PagingTutorialContentView.ManualFusionTutorialContent(i);
            case VIDEO_FUSION:
                return new PagingTutorialContentView.VideoFusionTutorialContent(i);
            default:
                return new PagingTutorialContentView.OneShotSlowTutorialContent(i);
        }
    }
}
