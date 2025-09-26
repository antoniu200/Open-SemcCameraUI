// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.hint;

public class HintTextTimedOutMessage extends HintTextContent
{
    private static final int FADE_OUT_DURATION = 1000;
    private static final int SHOW_DURATION = 5000;
    private final boolean mIsToast;
    private final MessageType mType;
    
    public HintTextTimedOutMessage(final MessageType mType) {
        this.mType = mType;
        this.mIsToast = this.mType.mIsToast;
        this.mTransparentBackground = false;
    }
    
    public static String createTag(final MessageType obj) {
        final StringBuilder sb = new StringBuilder();
        sb.append(HintTextTimedOutMessage.class.getSimpleName());
        sb.append(":");
        sb.append(obj);
        return sb.toString();
    }
    
    @Override
    public int getButtonMessageResourceId() {
        return -1;
    }
    
    @Override
    public int getFadeDuration() {
        return 1000;
    }
    
    @Override
    public int getMessageResourceId() {
        return this.mType.mMessageResourceId;
    }
    
    @Override
    public HintPriority getPriority() {
        return this.mType.mPriority;
    }
    
    @Override
    public String getTag() {
        return createTag(this.mType);
    }
    
    @Override
    public long getTimedOutDuration() {
        return 5000L;
    }
    
    @Override
    public boolean isToast() {
        return this.mIsToast;
    }
    
    public enum MessageType
    {
        private static final MessageType[] $VALUES;
        
        BURST_CHANGE_CAMERA_KEY_SETTING(2131689653, HintPriority.HIGH, true), 
        BURST_IMAGES_ARE_SAVED_TO_INTERNAL_STORAGE(2131689657, HintPriority.HIGH, true), 
        CANNOT_BURST_DUE_TO_FUSION_MODE(2131689656, HintPriority.HIGH, true), 
        CANNOT_BURST_DUE_TO_LOW_BATTERY(2131689651, HintPriority.HIGH, true), 
        CANNOT_BURST_IN_DARK_CONDITION(2131689654, HintPriority.HIGH, true), 
        CANNOT_BURST_USING_FRONT_CAMERA(2131689655, HintPriority.HIGH, true), 
        ISO_CHANGED_BY_FUSION(2131689926, HintPriority.HIGH, true), 
        ZOOM_NOT_AVAILABLE(2131690263, HintPriority.HIGH, true);
        
        private final boolean mIsToast;
        private final int mMessageResourceId;
        private final HintPriority mPriority;
        
        static {
            $VALUES = new MessageType[] { MessageType.CANNOT_BURST_DUE_TO_LOW_BATTERY, MessageType.CANNOT_BURST_IN_DARK_CONDITION, MessageType.BURST_CHANGE_CAMERA_KEY_SETTING, MessageType.CANNOT_BURST_USING_FRONT_CAMERA, MessageType.CANNOT_BURST_DUE_TO_FUSION_MODE, MessageType.BURST_IMAGES_ARE_SAVED_TO_INTERNAL_STORAGE, MessageType.ZOOM_NOT_AVAILABLE, MessageType.ISO_CHANGED_BY_FUSION };
        }
        
        private MessageType(final int mMessageResourceId, final HintPriority mPriority, final boolean mIsToast) {
            this.mMessageResourceId = mMessageResourceId;
            this.mPriority = mPriority;
            this.mIsToast = mIsToast;
        }
    }
}
