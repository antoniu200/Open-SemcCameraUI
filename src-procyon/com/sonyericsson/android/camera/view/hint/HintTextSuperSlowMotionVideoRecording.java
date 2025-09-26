// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.hint;

public class HintTextSuperSlowMotionVideoRecording extends HintTextSlowMotion
{
    private final boolean mIsDone;
    
    public HintTextSuperSlowMotionVideoRecording(final boolean mIsDone) {
        int n;
        if (!mIsDone) {
            n = 2131690151;
        }
        else {
            n = 2131690152;
        }
        super(n);
        this.mIsDone = mIsDone;
    }
    
    public static String createTag(final boolean b) {
        final StringBuilder sb = new StringBuilder();
        sb.append(HintTextSuperSlowMotionVideoRecording.class.getSimpleName());
        sb.append(":");
        sb.append(b);
        return sb.toString();
    }
    
    @Override
    public String getTag() {
        return createTag(this.mIsDone);
    }
}
