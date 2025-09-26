// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import com.sonyericsson.android.camera.view.modeselector.view.CapturingModePanelAttributes;
import com.sonyericsson.android.camera.view.modeselector.view.AbsPanelView;
import android.content.Context;

public class CapturingModeAttributes implements CapturingMode
{
    static final boolean $assertionsDisabled = false;
    public static final String TAG = "CapturingModeAttributes";
    private final String mActivityName;
    private final int mDescriptionLabelId;
    private final Long mId;
    private final InternalCaptureType mInternalCaptureType;
    private final boolean mIsVisibleNormal;
    private final boolean mIsVisibleOneshot;
    private final boolean mIsVisibleShortcut;
    private final String mModeName;
    private final String mPackageName;
    private final int mSelectorIconId;
    private final int mSelectorLabelId;
    private final int mShortcutIconId;
    private final int mShortcutLabelId;
    private final Object mTag;
    
    CapturingModeAttributes(final Long n, final String s, final String s2, final String s3, final int n2, final int n3, final int n4, final int n5, final int n6, final InternalCaptureType internalCaptureType, final boolean b, final boolean b2, final boolean b3) {
        this(n, s, s2, s3, n2, n3, n4, n5, n6, internalCaptureType, b, b2, b3, null);
    }
    
    CapturingModeAttributes(final Long mId, final String mPackageName, final String mActivityName, final String mModeName, final int mSelectorIconId, final int mSelectorLabelId, final int mDescriptionLabelId, final int mShortcutIconId, final int mShortcutLabelId, final InternalCaptureType mInternalCaptureType, final boolean mIsVisibleNormal, final boolean mIsVisibleOneshot, final boolean mIsVisibleShortcut, final Object mTag) {
        this.mId = mId;
        this.mPackageName = mPackageName;
        this.mActivityName = mActivityName;
        this.mModeName = mModeName;
        this.mSelectorIconId = mSelectorIconId;
        this.mSelectorLabelId = mSelectorLabelId;
        this.mDescriptionLabelId = mDescriptionLabelId;
        this.mShortcutIconId = mShortcutIconId;
        this.mShortcutLabelId = mShortcutLabelId;
        this.mInternalCaptureType = mInternalCaptureType;
        this.mIsVisibleNormal = mIsVisibleNormal;
        this.mIsVisibleOneshot = mIsVisibleOneshot;
        this.mIsVisibleShortcut = mIsVisibleShortcut;
        this.mTag = mTag;
    }
    
    public CapturingModeAttributes(final String s, final String s2, final String s3, final int n, final int n2, final int n3, final int n4, final int n5, final InternalCaptureType internalCaptureType, final boolean b, final boolean b2, final boolean b3, final Object o) {
        this(null, s, s2, s3, n, n2, n3, n4, n5, internalCaptureType, b, b2, b3, o);
    }
    
    private static AbsPanelView.PanelAttributes convert(final Context context, final CapturingModeAttributes capturingModeAttributes) {
        if (capturingModeAttributes == null) {
            return null;
        }
        final String resourceUri = ResourceUtil.getResourceUri(context, capturingModeAttributes.getPackageName(), capturingModeAttributes.getSelectorIconId());
        String title;
        if ((title = capturingModeAttributes.getSelectorLabel()) == null) {
            title = ResourceUtil.getString(context, capturingModeAttributes.getPackageName(), capturingModeAttributes.getSelectorLabelId(), "", 100);
        }
        final String string = ResourceUtil.getString(context, capturingModeAttributes.getPackageName(), capturingModeAttributes.getDescriptionLabelId(), "", 500);
        final CapturingModePanelAttributes.AttributesBuilder attributesBuilder = new CapturingModePanelAttributes.AttributesBuilder();
        attributesBuilder.setPackageName(capturingModeAttributes.getPackageName()).setActivityName(capturingModeAttributes.getActivityName()).setModeName(capturingModeAttributes.getModeName()).setIconUri(resourceUri).setTitle(title).setDescription(string);
        return attributesBuilder.build();
    }
    
    public static List<AbsPanelView.PanelAttributes> toAttributesList(final Context context, final List<CapturingModeAttributes> list) {
        final ArrayList list2 = new ArrayList();
        final Iterator<CapturingModeAttributes> iterator = list.iterator();
        while (iterator.hasNext()) {
            list2.add(convert(context, iterator.next()));
        }
        return list2;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        if (o == null) {
            return false;
        }
        if (o instanceof CapturingModeAttributes) {
            final CapturingModeAttributes capturingModeAttributes = (CapturingModeAttributes)o;
            boolean b2 = b;
            if (capturingModeAttributes.getPackageName().equals(this.mPackageName)) {
                b2 = b;
                if (capturingModeAttributes.getModeName().equals(this.mModeName)) {
                    b2 = true;
                }
            }
            return b2;
        }
        return false;
    }
    
    public String getActivityName() {
        return this.mActivityName;
    }
    
    String getAttributes() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PackageName=");
        sb.append(this.mPackageName);
        sb.append(", ActivityName=");
        sb.append(this.mActivityName);
        sb.append(", ModeName=");
        sb.append(this.mModeName);
        sb.append(", SelectorIconId=");
        sb.append(this.mSelectorIconId);
        sb.append(", SelectorLabelId=");
        sb.append(this.mSelectorLabelId);
        sb.append(", DescriptionLabelId=");
        sb.append(this.mDescriptionLabelId);
        sb.append(", ShortcutIconId=");
        sb.append(this.mShortcutIconId);
        sb.append(", ShortcutLabelId=");
        sb.append(this.mShortcutLabelId);
        return sb.toString();
    }
    
    public int getDescriptionLabelId() {
        return this.mDescriptionLabelId;
    }
    
    public Long getId() {
        return this.mId;
    }
    
    public InternalCaptureType getInternalCaptureType() {
        return this.mInternalCaptureType;
    }
    
    public String getModeName() {
        return this.mModeName;
    }
    
    public String getPackageName() {
        return this.mPackageName;
    }
    
    public int getSelectorIconId() {
        return this.mSelectorIconId;
    }
    
    public String getSelectorLabel() {
        return null;
    }
    
    public int getSelectorLabelId() {
        return this.mSelectorLabelId;
    }
    
    public int getShortcutIconId() {
        return this.mShortcutIconId;
    }
    
    public int getShortcutLabelId() {
        return this.mShortcutLabelId;
    }
    
    public Object getTag() {
        return this.mTag;
    }
    
    @Override
    public int hashCode() {
        return -1;
    }
    
    @Override
    public boolean is(final String s, final String s2) {
        return s.equals(this.mPackageName) && s2.equals(this.mModeName);
    }
    
    public boolean isVisibleNormal() {
        return this.mIsVisibleNormal;
    }
    
    public boolean isVisibleOneshot() {
        return this.mIsVisibleOneshot;
    }
    
    public boolean isVisibleShortcut() {
        return this.mIsVisibleShortcut;
    }
    
    public enum InternalCaptureType
    {
        private static final InternalCaptureType[] $VALUES;
        
        Photo, 
        Video;
        
        static {
            $VALUES = new InternalCaptureType[] { InternalCaptureType.Photo, InternalCaptureType.Video };
        }
    }
    
    public enum VisibilityType
    {
        private static final VisibilityType[] $VALUES;
        
        Normal("visibility_normal"), 
        Oneshot("visibility_oneshot"), 
        Shortcut("visibility_shortcut");
        
        final String mColumn;
        
        static {
            $VALUES = new VisibilityType[] { VisibilityType.Normal, VisibilityType.Oneshot, VisibilityType.Shortcut };
        }
        
        private VisibilityType(final String mColumn) {
            this.mColumn = mColumn;
        }
    }
}
