// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.tutorial;

import java.util.List;

class TutorialFactory
{
    public TutorialFactory() {
    }
    
    public TutorialContentView.TutorialContent create(final int n, final List<TutorialController.TutorialType> list, final List<TutorialContentView.TutorialContent> list2) {
        return new PagingTutorialContentView.CustomTutorialContent(n, list, list2);
    }
    
    public TutorialContentView.TutorialContent create(final TutorialController.TutorialType tutorialType, final int n) {
        TutorialContentView.TutorialContent tutorialContent = null;
        switch (TutorialFactory$1.$SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[tutorialType.ordinal()]) {
            default: {
                tutorialContent = new PagingTutorialContentView.OneShotSlowTutorialContent(n);
                break;
            }
            case 12: {
                tutorialContent = new PagingTutorialContentView.VideoFusionTutorialContent(n);
                break;
            }
            case 11: {
                tutorialContent = new PagingTutorialContentView.ManualFusionTutorialContent(n);
                break;
            }
            case 10: {
                tutorialContent = new PagingTutorialContentView.StandardSlowTutorialContent(n);
                break;
            }
            case 9: {
                tutorialContent = new PagingTutorialContentView.OneShotSlowTutorialContent(n, new Object[] { 960 });
                break;
            }
            case 8: {
                tutorialContent = new PagingTutorialContentView.SuperSlowTutorialContent(n);
                break;
            }
            case 7: {
                tutorialContent = new PagingTutorialContentView.SuperSlowMoreOptionsTutorialContent(n);
                break;
            }
            case 6: {
                tutorialContent = new PagingTutorialContentView.HandShutterTutorialContent(n);
                break;
            }
            case 5: {
                tutorialContent = new PagingTutorialContentView.EyeGuideTutorialContent(n);
                break;
            }
            case 4: {
                tutorialContent = new PagingTutorialContentView.DualCameraTutorialContent(n);
                break;
            }
            case 3: {
                tutorialContent = new PagingTutorialContentView.PredictiveLaunchTutorialContent(n);
                break;
            }
            case 2: {
                tutorialContent = new PagingTutorialContentView.SideSenseTutorialContent(n);
                break;
            }
            case 1: {
                tutorialContent = new SimpleTutorialContentView.SaveLocationTutorialContent(n);
                break;
            }
        }
        return tutorialContent;
    }
}
