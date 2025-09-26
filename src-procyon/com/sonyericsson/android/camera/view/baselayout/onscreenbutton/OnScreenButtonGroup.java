// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout.onscreenbutton;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import com.sonyericsson.android.camera.util.CamLog;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.FrameLayout;

public class OnScreenButtonGroup extends FrameLayout
{
    private static final String TAG = "OnScreenButtonGroup";
    private OnScreenButton mMain;
    private OnScreenButton mOption1;
    private OnScreenButton mOption2;
    private int mOrientation;
    
    public OnScreenButtonGroup(final Context context, final AttributeSet set) {
        super(context, set);
        this.mOrientation = 2;
    }
    
    public void clearMain() {
        this.mMain.setItem(null);
        this.mMain.setVisibility(4);
    }
    
    public void clearOption1() {
        this.mOption1.setItem(null);
        this.mOption1.setVisibility(4);
    }
    
    public void clearOption2() {
        this.mOption2.setItem(null);
        this.mOption2.setVisibility(4);
    }
    
    public void clearTouched() {
        this.mMain.clearTouched();
        this.mOption2.clearTouched();
        this.mOption1.clearTouched();
    }
    
    public boolean isMainButtonTouched() {
        return this.mMain.isTouched();
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mMain = (OnScreenButton)this.findViewById(2131296460);
        this.mOption2 = (OnScreenButton)this.findViewById(2131296632);
        this.mOption1 = (OnScreenButton)this.findViewById(2131296396);
    }
    
    public void setMain(final Item item) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setMain(");
            sb.append(item);
            sb.append(")");
            CamLog.d(sb.toString());
        }
        this.mMain.setItem(item);
        this.mMain.setVisibility(0);
    }
    
    public void setMain(final Item item, final int i, final boolean b) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setMain(");
            sb.append(item);
            sb.append(", ");
            sb.append(i);
            sb.append(", ");
            sb.append(b);
            sb.append(")");
            CamLog.d(sb.toString());
        }
        this.setMain(item);
        this.mMain.changeRotatability(i, b);
    }
    
    public void setMainRotatability(final int n, final boolean b) {
        this.mMain.changeRotatability(n, b);
    }
    
    public void setOption1(final Item item) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setOption1(");
            sb.append(item);
            sb.append(")");
            CamLog.d(sb.toString());
        }
        this.mOption1.setItem(item);
        this.mOption1.setVisibility(0);
    }
    
    public void setOption1(final Item item, final int i, final boolean b) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setOption1(");
            sb.append(item);
            sb.append(", ");
            sb.append(i);
            sb.append(", ");
            sb.append(b);
            sb.append(")");
            CamLog.d(sb.toString());
        }
        this.setOption1(item);
        this.mOption1.changeRotatability(i, b);
    }
    
    public void setOption2(final Item item) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setOption2(");
            sb.append(item);
            sb.append(")");
            CamLog.d(sb.toString());
        }
        this.mOption2.setItem(item);
        this.mOption2.setVisibility(0);
    }
    
    public void setOption2(final Item item, final int i, final boolean b) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setOption2(");
            sb.append(item);
            sb.append(", ");
            sb.append(i);
            sb.append(", ");
            sb.append(b);
            sb.append(")");
            CamLog.d(sb.toString());
        }
        this.setOption2(item);
        this.mOption2.changeRotatability(i, b);
    }
    
    public void setUiOrientation(final int n) {
        if (n == this.mOrientation) {
            return;
        }
        this.mMain.setUiOrientation(n);
        this.mOption2.setUiOrientation(n);
        this.mOption1.setUiOrientation(n);
        this.mOrientation = n;
    }
    
    public void show() {
        if (this.getVisibility() == 0) {
            return;
        }
        this.setVisibility(0);
        this.mMain.setVisibility(0);
        this.mOption2.setVisibility(0);
        this.mOption1.setVisibility(0);
    }
    
    public static class ImmutableButtonItem extends Item
    {
        private final OnScreenButton.Resource mResource;
        private final Object mTag;
        
        public ImmutableButtonItem(final Object mTag, final OnScreenButton.Resource mResource, final OnScreenButtonListener onScreenButtonListener, final boolean b) {
            super(onScreenButtonListener, b);
            this.mTag = mTag;
            this.mResource = mResource;
        }
        
        @Override
        public boolean equals(final Object o) {
            final boolean b = o instanceof ImmutableButtonItem;
            final boolean b2 = false;
            if (!b) {
                return false;
            }
            final ImmutableButtonItem immutableButtonItem = (ImmutableButtonItem)o;
            if (this.mTag != immutableButtonItem.mTag) {
                return false;
            }
            final OnScreenButtonListener onScreenButtonListener = ((Item)immutableButtonItem).getOnScreenButtonListener();
            final OnScreenButtonListener onScreenButtonListener2 = ((Item)this).getOnScreenButtonListener();
            if ((onScreenButtonListener2 != null || onScreenButtonListener != null) && onScreenButtonListener2 != onScreenButtonListener) {
                boolean b3 = b2;
                if (onScreenButtonListener2 == null) {
                    return b3;
                }
                b3 = b2;
                if (onScreenButtonListener == null) {
                    return b3;
                }
                b3 = b2;
                if (!onScreenButtonListener2.getClass().getName().equals(onScreenButtonListener.getClass().getName())) {
                    return b3;
                }
            }
            return true;
        }
        
        @Override
        public OnScreenButton.Resource getResource() {
            return this.mResource;
        }
    }
    
    public abstract static class Item
    {
        private boolean mIsEnabled;
        private final boolean mIsSoundEffectsEnabled;
        private final OnScreenButtonListener mOnScreenButtonListener;
        private final List<OnItemUpdatedListener> mOnUpdatedListeners;
        
        public Item(final OnScreenButtonListener mOnScreenButtonListener, final boolean mIsSoundEffectsEnabled) {
            this.mOnScreenButtonListener = mOnScreenButtonListener;
            this.mIsSoundEffectsEnabled = mIsSoundEffectsEnabled;
            this.mOnUpdatedListeners = new ArrayList<OnItemUpdatedListener>();
            this.mIsEnabled = true;
        }
        
        void addOnUpdatedListener(final OnItemUpdatedListener onItemUpdatedListener) {
            if (!this.mOnUpdatedListeners.contains(onItemUpdatedListener)) {
                this.mOnUpdatedListeners.add(onItemUpdatedListener);
            }
        }
        
        public OnScreenButtonListener getOnScreenButtonListener() {
            return this.mOnScreenButtonListener;
        }
        
        public abstract OnScreenButton.Resource getResource();
        
        public boolean isEnabled() {
            return this.mIsEnabled;
        }
        
        public boolean isSoundEffectsEnabled() {
            return this.mIsSoundEffectsEnabled;
        }
        
        void notifyUpdated() {
            final Iterator<OnItemUpdatedListener> iterator = this.mOnUpdatedListeners.iterator();
            while (iterator.hasNext()) {
                iterator.next().onUpdated(this);
            }
        }
        
        void removeOnUpdatedListener(final OnItemUpdatedListener onItemUpdatedListener) {
            this.mOnUpdatedListeners.remove(onItemUpdatedListener);
        }
        
        public void setEnabled(final boolean mIsEnabled) {
            this.mIsEnabled = mIsEnabled;
            this.notifyUpdated();
        }
    }
    
    public static class MutableButtonItem extends Item
    {
        private OnScreenButton.Resource mResource;
        
        public MutableButtonItem(final OnScreenButtonListener onScreenButtonListener, final boolean b) {
            super(onScreenButtonListener, b);
            this.mResource = OnScreenButton.EMPTY_RESOURCE;
        }
        
        private void setResource(final OnScreenButton.Resource mResource) {
            this.mResource = mResource;
            ((Item)this).notifyUpdated();
        }
        
        @Override
        public OnScreenButton.Resource getResource() {
            return this.mResource;
        }
        
        public Builder update() {
            return new Builder(this.mResource);
        }
        
        public class Builder
        {
            private int mBackground;
            private int mDescription;
            private int mIcon;
            private int mIconPortrait;
            private String mText;
            final MutableButtonItem this$0;
            
            private Builder(final MutableButtonItem this$0, final OnScreenButton.Resource resource) {
                this.this$0 = this$0;
                this.mIcon = resource.mIcon;
                this.mIconPortrait = resource.mIconPortrait;
                this.mBackground = resource.mBackground;
                this.mDescription = resource.mDescription;
                this.mText = resource.mText;
            }
            
            public Builder background(final int mBackground) {
                this.mBackground = mBackground;
                return this;
            }
            
            public void commit() {
                this.this$0.setResource(new OnScreenButton.Resource(this.mIcon, this.mIconPortrait, this.mBackground, this.mDescription, this.mText));
            }
            
            public Builder description(final int mDescription) {
                this.mDescription = mDescription;
                return this;
            }
            
            public Builder icon(final int mIcon) {
                this.mIcon = mIcon;
                this.mIconPortrait = -1;
                return this;
            }
            
            public Builder text(final String mText) {
                this.mText = mText;
                return this;
            }
        }
    }
    
    public interface OnItemUpdatedListener
    {
        void onUpdated(final Item p0);
    }
}
