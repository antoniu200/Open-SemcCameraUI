// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.animation;

import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;

public class AnimationRequest
{
    public final AnimationDegree mDegree;
    public final CapturingMode mFrom;
    public final CapturingMode mTarget;
    public final AnimationType mType;
    
    public AnimationRequest(final AnimationType mType, final AnimationDegree mDegree, final CapturingMode mFrom, final CapturingMode mTarget) {
        this.mType = mType;
        this.mDegree = mDegree;
        this.mFrom = mFrom;
        this.mTarget = mTarget;
    }
    
    public enum AnimationDegree
    {
        private static final AnimationDegree[] $VALUES;
        
        CANCEL, 
        EXEC, 
        FINISH, 
        START;
        
        static {
            $VALUES = new AnimationDegree[] { AnimationDegree.START, AnimationDegree.CANCEL, AnimationDegree.EXEC, AnimationDegree.FINISH };
        }
    }
    
    public enum AnimationType
    {
        private static final AnimationType[] $VALUES;
        
        MODE_ICON, 
        MODE_SELECTOR, 
        MODE_TOUCH, 
        MRU_SHORTCUT, 
        NONE, 
        SWITCH_TOUCH;
        
        static {
            $VALUES = new AnimationType[] { AnimationType.NONE, AnimationType.MODE_TOUCH, AnimationType.MODE_ICON, AnimationType.MODE_SELECTOR, AnimationType.MRU_SHORTCUT, AnimationType.SWITCH_TOUCH };
        }
    }
}
