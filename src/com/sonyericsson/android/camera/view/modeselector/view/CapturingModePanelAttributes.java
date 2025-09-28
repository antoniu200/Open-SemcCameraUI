package com.sonyericsson.android.camera.view.modeselector.view;

import com.sonyericsson.android.camera.view.modeselector.view.AbsPanelView;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CapturingModePanelAttributes implements AbsPanelView.PanelAttributes {
    private String mActivityName;
    private String mDescription;
    private String mIconUri;
    private String mModeName;
    private String mPackageName;
    private String mTitle;

    private CapturingModePanelAttributes() {
    }

    private CapturingModePanelAttributes(CapturingModePanelAttributes capturingModePanelAttributes) {
        this.mPackageName = capturingModePanelAttributes.mPackageName;
        this.mActivityName = capturingModePanelAttributes.mActivityName;
        this.mModeName = capturingModePanelAttributes.mModeName;
        this.mIconUri = capturingModePanelAttributes.mIconUri;
        this.mTitle = capturingModePanelAttributes.mTitle;
        this.mDescription = capturingModePanelAttributes.mDescription;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public String getActivityName() {
        return this.mActivityName;
    }

    public String getModeName() {
        return this.mModeName;
    }

    @Override // com.sonyericsson.android.camera.view.modeselector.view.AbsPanelView.PanelAttributes
    public String getIconUri() {
        return this.mIconUri;
    }

    @Override // com.sonyericsson.android.camera.view.modeselector.view.AbsPanelView.PanelAttributes
    public String getTitle() {
        return this.mTitle;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public String toString() {
        return "CapturingModePanelAttributes [mPackageName=" + this.mPackageName + ", mActivityName=" + this.mActivityName + ", mModeName=" + this.mModeName + ", mIconUri=" + this.mIconUri + ", mTitle=" + this.mTitle + ", mDescription=" + this.mDescription + "]";
    }

    public int hashCode() {
        return (31 * ((this.mModeName == null ? 0 : this.mModeName.hashCode()) + 31)) + (this.mPackageName != null ? this.mPackageName.hashCode() : 0);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CapturingModePanelAttributes capturingModePanelAttributes = (CapturingModePanelAttributes) obj;
        if (this.mModeName == null) {
            if (capturingModePanelAttributes.mModeName != null) {
                return false;
            }
        } else if (!this.mModeName.equals(capturingModePanelAttributes.mModeName)) {
            return false;
        }
        if (this.mPackageName == null) {
            if (capturingModePanelAttributes.mPackageName != null) {
                return false;
            }
        } else if (!this.mPackageName.equals(capturingModePanelAttributes.mPackageName)) {
            return false;
        }
        return true;
    }

    public static class AttributesBuilder {
        CapturingModePanelAttributes mAttributes = new CapturingModePanelAttributes();

        public AttributesBuilder setPackageName(String str) {
            this.mAttributes.mPackageName = str;
            return this;
        }

        public AttributesBuilder setActivityName(String str) {
            this.mAttributes.mActivityName = str;
            return this;
        }

        public AttributesBuilder setModeName(String str) {
            this.mAttributes.mModeName = str;
            return this;
        }

        public AttributesBuilder setIconUri(String str) {
            this.mAttributes.mIconUri = str;
            return this;
        }

        public AttributesBuilder setTitle(String str) {
            this.mAttributes.mTitle = str;
            return this;
        }

        public AttributesBuilder setDescription(String str) {
            this.mAttributes.mDescription = str;
            return this;
        }

        public CapturingModePanelAttributes build() {
            return new CapturingModePanelAttributes();
        }
    }
}
