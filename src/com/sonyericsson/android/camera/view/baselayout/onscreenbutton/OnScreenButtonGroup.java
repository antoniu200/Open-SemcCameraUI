package com.sonyericsson.android.camera.view.baselayout.onscreenbutton;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButton;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class OnScreenButtonGroup extends FrameLayout {
    private static final String TAG = "OnScreenButtonGroup";
    private OnScreenButton mMain;
    private OnScreenButton mOption1;
    private OnScreenButton mOption2;
    private int mOrientation;

    public interface OnItemUpdatedListener {
        void onUpdated(Item item);
    }

    public OnScreenButtonGroup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mOrientation = 2;
    }

    public static abstract class Item {
        private final boolean mIsSoundEffectsEnabled;
        private final OnScreenButtonListener mOnScreenButtonListener;
        private final List<OnItemUpdatedListener> mOnUpdatedListeners = new ArrayList();
        private boolean mIsEnabled = true;

        public abstract OnScreenButton.Resource getResource();

        public Item(OnScreenButtonListener onScreenButtonListener, boolean z) {
            this.mOnScreenButtonListener = onScreenButtonListener;
            this.mIsSoundEffectsEnabled = z;
        }

        public OnScreenButtonListener getOnScreenButtonListener() {
            return this.mOnScreenButtonListener;
        }

        public boolean isSoundEffectsEnabled() {
            return this.mIsSoundEffectsEnabled;
        }

        public void setEnabled(boolean z) {
            this.mIsEnabled = z;
            notifyUpdated();
        }

        public boolean isEnabled() {
            return this.mIsEnabled;
        }

        void addOnUpdatedListener(OnItemUpdatedListener onItemUpdatedListener) {
            if (this.mOnUpdatedListeners.contains(onItemUpdatedListener)) {
                return;
            }
            this.mOnUpdatedListeners.add(onItemUpdatedListener);
        }

        void removeOnUpdatedListener(OnItemUpdatedListener onItemUpdatedListener) {
            this.mOnUpdatedListeners.remove(onItemUpdatedListener);
        }

        void notifyUpdated() {
            Iterator<OnItemUpdatedListener> it = this.mOnUpdatedListeners.iterator();
            while (it.hasNext()) {
                it.next().onUpdated(this);
            }
        }
    }

    public static class ImmutableButtonItem extends Item {
        private final OnScreenButton.Resource mResource;
        private final Object mTag;

        public ImmutableButtonItem(Object obj, OnScreenButton.Resource resource, OnScreenButtonListener onScreenButtonListener, boolean z) {
            super(onScreenButtonListener, z);
            this.mTag = obj;
            this.mResource = resource;
        }

        @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonGroup.Item
        public OnScreenButton.Resource getResource() {
            return this.mResource;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof ImmutableButtonItem)) {
                return false;
            }
            ImmutableButtonItem immutableButtonItem = (ImmutableButtonItem) obj;
            if (this.mTag != immutableButtonItem.mTag) {
                return false;
            }
            OnScreenButtonListener onScreenButtonListener = immutableButtonItem.getOnScreenButtonListener();
            OnScreenButtonListener onScreenButtonListener2 = getOnScreenButtonListener();
            return (onScreenButtonListener2 == null && onScreenButtonListener == null) || onScreenButtonListener2 == onScreenButtonListener || !(onScreenButtonListener2 == null || onScreenButtonListener == null || !onScreenButtonListener2.getClass().getName().equals(onScreenButtonListener.getClass().getName()));
        }
    }

    public static class MutableButtonItem extends Item {
        private OnScreenButton.Resource mResource;

        public MutableButtonItem(OnScreenButtonListener onScreenButtonListener, boolean z) {
            super(onScreenButtonListener, z);
            this.mResource = OnScreenButton.EMPTY_RESOURCE;
        }

        @Override // com.sonyericsson.android.camera.view.baselayout.onscreenbutton.OnScreenButtonGroup.Item
        public OnScreenButton.Resource getResource() {
            return this.mResource;
        }

        public Builder update() {
            return new Builder(this.mResource);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setResource(OnScreenButton.Resource resource) {
            this.mResource = resource;
            notifyUpdated();
        }

        public class Builder {
            private int mBackground;
            private int mDescription;
            private int mIcon;
            private int mIconPortrait;
            private String mText;

            private Builder(OnScreenButton.Resource resource) {
                this.mIcon = resource.mIcon;
                this.mIconPortrait = resource.mIconPortrait;
                this.mBackground = resource.mBackground;
                this.mDescription = resource.mDescription;
                this.mText = resource.mText;
            }

            public Builder icon(int i) {
                this.mIcon = i;
                this.mIconPortrait = -1;
                return this;
            }

            public Builder background(int i) {
                this.mBackground = i;
                return this;
            }

            public Builder description(int i) {
                this.mDescription = i;
                return this;
            }

            public Builder text(String str) {
                this.mText = str;
                return this;
            }

            public void commit() {
                MutableButtonItem.this.setResource(new OnScreenButton.Resource(this.mIcon, this.mIconPortrait, this.mBackground, this.mDescription, this.mText));
            }
        }
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mMain = (OnScreenButton) findViewById(R.id.main_button);
        this.mOption2 = (OnScreenButton) findViewById(R.id.sub_button);
        this.mOption1 = (OnScreenButton) findViewById(R.id.extra_button);
    }

    public void clearTouched() {
        this.mMain.clearTouched();
        this.mOption2.clearTouched();
        this.mOption1.clearTouched();
    }

    public boolean isMainButtonTouched() {
        return this.mMain.isTouched();
    }

    public void setMain(Item item) {
        if (CamLog.VERBOSE) {
            CamLog.d("setMain(" + item + ")");
        }
        this.mMain.setItem(item);
        this.mMain.setVisibility(0);
    }

    public void setMain(Item item, int i, boolean z) {
        if (CamLog.VERBOSE) {
            CamLog.d("setMain(" + item + ", " + i + ", " + z + ")");
        }
        setMain(item);
        this.mMain.changeRotatability(i, z);
    }

    public void setOption2(Item item) {
        if (CamLog.VERBOSE) {
            CamLog.d("setOption2(" + item + ")");
        }
        this.mOption2.setItem(item);
        this.mOption2.setVisibility(0);
    }

    public void setOption2(Item item, int i, boolean z) {
        if (CamLog.VERBOSE) {
            CamLog.d("setOption2(" + item + ", " + i + ", " + z + ")");
        }
        setOption2(item);
        this.mOption2.changeRotatability(i, z);
    }

    public void setOption1(Item item) {
        if (CamLog.VERBOSE) {
            CamLog.d("setOption1(" + item + ")");
        }
        this.mOption1.setItem(item);
        this.mOption1.setVisibility(0);
    }

    public void setOption1(Item item, int i, boolean z) {
        if (CamLog.VERBOSE) {
            CamLog.d("setOption1(" + item + ", " + i + ", " + z + ")");
        }
        setOption1(item);
        this.mOption1.changeRotatability(i, z);
    }

    public void clearMain() {
        this.mMain.setItem(null);
        this.mMain.setVisibility(4);
    }

    public void clearOption2() {
        this.mOption2.setItem(null);
        this.mOption2.setVisibility(4);
    }

    public void clearOption1() {
        this.mOption1.setItem(null);
        this.mOption1.setVisibility(4);
    }

    public void setUiOrientation(int i) {
        if (i == this.mOrientation) {
            return;
        }
        this.mMain.setUiOrientation(i);
        this.mOption2.setUiOrientation(i);
        this.mOption1.setUiOrientation(i);
        this.mOrientation = i;
    }

    public void show() {
        if (getVisibility() == 0) {
            return;
        }
        setVisibility(0);
        this.mMain.setVisibility(0);
        this.mOption2.setVisibility(0);
        this.mOption1.setVisibility(0);
    }

    public void setMainRotatability(int i, boolean z) {
        this.mMain.changeRotatability(i, z);
    }
}
