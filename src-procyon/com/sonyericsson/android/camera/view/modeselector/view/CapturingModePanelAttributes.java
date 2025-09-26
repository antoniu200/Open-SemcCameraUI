// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector.view;

public class CapturingModePanelAttributes implements PanelAttributes
{
    private String mActivityName;
    private String mDescription;
    private String mIconUri;
    private String mModeName;
    private String mPackageName;
    private String mTitle;
    
    private CapturingModePanelAttributes() {
    }
    
    private CapturingModePanelAttributes(final CapturingModePanelAttributes capturingModePanelAttributes) {
        this.mPackageName = capturingModePanelAttributes.mPackageName;
        this.mActivityName = capturingModePanelAttributes.mActivityName;
        this.mModeName = capturingModePanelAttributes.mModeName;
        this.mIconUri = capturingModePanelAttributes.mIconUri;
        this.mTitle = capturingModePanelAttributes.mTitle;
        this.mDescription = capturingModePanelAttributes.mDescription;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final CapturingModePanelAttributes capturingModePanelAttributes = (CapturingModePanelAttributes)o;
        if (this.mModeName == null) {
            if (capturingModePanelAttributes.mModeName != null) {
                return false;
            }
        }
        else if (!this.mModeName.equals(capturingModePanelAttributes.mModeName)) {
            return false;
        }
        if (this.mPackageName == null) {
            if (capturingModePanelAttributes.mPackageName != null) {
                return false;
            }
        }
        else if (!this.mPackageName.equals(capturingModePanelAttributes.mPackageName)) {
            return false;
        }
        return true;
    }
    
    public String getActivityName() {
        return this.mActivityName;
    }
    
    public String getDescription() {
        return this.mDescription;
    }
    
    @Override
    public String getIconUri() {
        return this.mIconUri;
    }
    
    public String getModeName() {
        return this.mModeName;
    }
    
    public String getPackageName() {
        return this.mPackageName;
    }
    
    @Override
    public String getTitle() {
        return this.mTitle;
    }
    
    @Override
    public int hashCode() {
        final String mModeName = this.mModeName;
        int hashCode = 0;
        int hashCode2;
        if (mModeName == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = this.mModeName.hashCode();
        }
        if (this.mPackageName != null) {
            hashCode = this.mPackageName.hashCode();
        }
        return 31 * (hashCode2 + 31) + hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CapturingModePanelAttributes [mPackageName=");
        sb.append(this.mPackageName);
        sb.append(", mActivityName=");
        sb.append(this.mActivityName);
        sb.append(", mModeName=");
        sb.append(this.mModeName);
        sb.append(", mIconUri=");
        sb.append(this.mIconUri);
        sb.append(", mTitle=");
        sb.append(this.mTitle);
        sb.append(", mDescription=");
        sb.append(this.mDescription);
        sb.append("]");
        return sb.toString();
    }
    
    public static class AttributesBuilder
    {
        CapturingModePanelAttributes mAttributes;
        
        public AttributesBuilder() {
            this.mAttributes = new CapturingModePanelAttributes((CapturingModePanelAttributes$1)null);
        }
        
        public CapturingModePanelAttributes build() {
            return new CapturingModePanelAttributes(this.mAttributes, null);
        }
        
        public AttributesBuilder setActivityName(final String s) {
            this.mAttributes.mActivityName = s;
            return this;
        }
        
        public AttributesBuilder setDescription(final String s) {
            this.mAttributes.mDescription = s;
            return this;
        }
        
        public AttributesBuilder setIconUri(final String s) {
            this.mAttributes.mIconUri = s;
            return this;
        }
        
        public AttributesBuilder setModeName(final String s) {
            this.mAttributes.mModeName = s;
            return this;
        }
        
        public AttributesBuilder setPackageName(final String s) {
            this.mAttributes.mPackageName = s;
            return this;
        }
        
        public AttributesBuilder setTitle(final String s) {
            this.mAttributes.mTitle = s;
            return this;
        }
    }
}
