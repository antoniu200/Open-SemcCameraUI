// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.capturefeedback.animation;

import java.util.Locale;
import com.sonyericsson.android.camera.util.CamLog;
import android.view.animation.PathInterpolator;
import android.view.animation.Interpolator;

public class CaptureFeedbackAnimationFactory
{
    public static final String TAG = "CaptureFeedbackAnimationFactory";
    
    public static CaptureFeedbackAnimation createDefaultAnimation() {
        return new DefaultFeedbackAnimation();
    }
    
    private static class DefaultFeedbackAnimation implements CaptureFeedbackAnimation
    {
        private static final float BLUE = 0.0f;
        private static final float CONTROL_X1 = 0.95f;
        private static final float CONTROL_X2 = 0.795f;
        private static final float CONTROL_Y1 = 0.05f;
        private static final float CONTROL_Y2 = 0.035f;
        private static final long DURATION_MILLIS = 200L;
        private static final float END_ALPHA = 0.0f;
        private static final float GREEN = 0.0f;
        private static final float RED = 0.0f;
        private static final float START_ALPHA = 1.0f;
        private final Interpolator mInterpolator;
        
        private DefaultFeedbackAnimation() {
            this.mInterpolator = (Interpolator)new PathInterpolator(0.95f, 0.05f, 0.795f, 0.035f);
        }
        
        @Override
        public boolean draw(final CaptureFeedbackAnimationCanvas captureFeedbackAnimationCanvas, final long l) {
            final float f = l / 200.0f;
            final float f2 = 1.0f + -1.0f * Math.min(1.0f, this.mInterpolator.getInterpolation(f));
            if (CamLog.VERBOSE) {
                CamLog.d(String.format(Locale.UK, "time:%d progress:%f alpha:%f", l, f, f2));
            }
            if (f > 1.0) {
                return false;
            }
            captureFeedbackAnimationCanvas.drawColor(f2, 0.0f, 0.0f, 0.0f);
            return true;
        }
    }
}
