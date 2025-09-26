package com.sonyericsson.android.camera.view.hint;

import android.content.res.Resources;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public abstract class HintTextContent {
    public static final long INFINITE_TIMEOUT_MILLIS = -1;
    public static final int NO_FADE = -1;
    protected boolean mTransparentBackground = true;

    public enum HintPriority {
        HIGH,
        MIDDLE,
        LOW
    }

    public int getButtonDescriptionResourceId() {
        return -1;
    }

    public abstract int getButtonMessageResourceId();

    public int getFadeDuration() {
        return -1;
    }

    public int getMessageDescriptionResourceId() {
        return -1;
    }

    public abstract int getMessageResourceId();

    public HintTextContent getNext() {
        return null;
    }

    public String getSubMessage() {
        return null;
    }

    public long getTimedOutDuration() {
        return -1L;
    }

    public boolean isToast() {
        return false;
    }

    public HintPriority getPriority() {
        return HintPriority.LOW;
    }

    public void attach(HintTextView hintTextView) throws Resources.NotFoundException {
        if (hintTextView == null) {
            return;
        }
        hintTextView.setContent(this);
        if (hintTextView.getVisibility() != 0) {
            hintTextView.setVisibility(0);
        }
    }

    public void detach(HintTextView hintTextView) {
        if (hintTextView == null || hintTextView.getVisibility() == 4) {
            return;
        }
        hintTextView.setVisibility(4);
    }

    public String getTag() {
        return getClass().getSimpleName();
    }

    public boolean isTransparentBackground() {
        return this.mTransparentBackground;
    }

    public boolean equals(Object obj) {
        if (obj instanceof HintTextContent) {
            return getTag().equals(((HintTextContent) obj).getTag());
        }
        return false;
    }

    public int hashCode() {
        return getTag().hashCode();
    }
}
