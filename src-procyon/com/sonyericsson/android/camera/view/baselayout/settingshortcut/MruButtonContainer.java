// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.baselayout.settingshortcut;

import com.sonyericsson.android.camera.view.modeselector.AddonMode;
import com.sonyericsson.android.camera.setting.SharedPreferencesAccessor;
import android.view.View;
import android.view.View$OnClickListener;
import android.widget.ImageView;
import com.sonyericsson.android.camera.view.modeselector.InternalMode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import java.util.Iterator;
import com.sonyericsson.android.camera.view.modeselector.CapturingModeAttributes;
import java.util.List;
import android.support.annotation.NonNull;
import android.content.Context;
import com.sonyericsson.android.camera.view.selectabledialog.ModeSelector;
import com.sonyericsson.android.camera.view.modeselector.ModeLoader;
import com.sonyericsson.android.camera.view.modeselector.Mode;
import android.widget.FrameLayout;

public class MruButtonContainer extends FrameLayout
{
    private boolean mIsAvailable;
    private Mode.OnStateChangeListener mModeChangeListener;
    private ModeLoader.OnModeListChangeListener mModeListChangeListener;
    private ModeLoader mModeLoader;
    private ModeSelector.OnModeSelectListener mModeSelectListener;
    private MruButton mMruButton;
    private boolean mRequestVisible;
    
    public MruButtonContainer(@NonNull final Context context) {
        super(context);
        this.mModeChangeListener = new Mode.OnStateChangeListener() {
            final MruButtonContainer this$0;
            
            @Override
            public void onAvailabilityChanged(final Mode mode, final boolean availability) {
                if (mode != null && mode.compare(this.this$0.mMruButton.getMode())) {
                    this.this$0.setAvailability(availability);
                }
            }
        };
        this.mModeListChangeListener = new ModeLoader.OnModeListChangeListener() {
            final MruButtonContainer this$0;
            
            @Override
            public void onModeListChanged(final List<Mode> list, final List<CapturingModeAttributes> list2) {
                final Iterator<Mode> iterator = list.iterator();
                while (iterator.hasNext()) {
                    iterator.next().setOnStateChangeListener(this.this$0.mModeChangeListener);
                }
                this.this$0.mMruButton.setMode(null);
            }
        };
        this.init();
    }
    
    public MruButtonContainer(@NonNull final Context context, @Nullable final AttributeSet set) {
        super(context, set);
        this.mModeChangeListener = new Mode.OnStateChangeListener() {
            final MruButtonContainer this$0;
            
            @Override
            public void onAvailabilityChanged(final Mode mode, final boolean availability) {
                if (mode != null && mode.compare(this.this$0.mMruButton.getMode())) {
                    this.this$0.setAvailability(availability);
                }
            }
        };
        this.mModeListChangeListener = new ModeLoader.OnModeListChangeListener() {
            final MruButtonContainer this$0;
            
            @Override
            public void onModeListChanged(final List<Mode> list, final List<CapturingModeAttributes> list2) {
                final Iterator<Mode> iterator = list.iterator();
                while (iterator.hasNext()) {
                    iterator.next().setOnStateChangeListener(this.this$0.mModeChangeListener);
                }
                this.this$0.mMruButton.setMode(null);
            }
        };
        this.init();
    }
    
    public MruButtonContainer(@NonNull final Context context, @Nullable final AttributeSet set, final int n) {
        super(context, set, n);
        this.mModeChangeListener = new Mode.OnStateChangeListener() {
            final MruButtonContainer this$0;
            
            @Override
            public void onAvailabilityChanged(final Mode mode, final boolean availability) {
                if (mode != null && mode.compare(this.this$0.mMruButton.getMode())) {
                    this.this$0.setAvailability(availability);
                }
            }
        };
        this.mModeListChangeListener = new ModeLoader.OnModeListChangeListener() {
            final MruButtonContainer this$0;
            
            @Override
            public void onModeListChanged(final List<Mode> list, final List<CapturingModeAttributes> list2) {
                final Iterator<Mode> iterator = list.iterator();
                while (iterator.hasNext()) {
                    iterator.next().setOnStateChangeListener(this.this$0.mModeChangeListener);
                }
                this.this$0.mMruButton.setMode(null);
            }
        };
        this.init();
    }
    
    private void init() {
        this.mIsAvailable = false;
        this.mRequestVisible = false;
        this.update();
    }
    
    public boolean hasInternalMode() {
        return this.mMruButton.getMode() != null && InternalMode.class.isAssignableFrom(this.mMruButton.getMode().getClass());
    }
    
    public void hide() {
        this.mRequestVisible = false;
        this.update();
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mMruButton = (MruButton)new MruSmallModeButton();
    }
    
    public void setAvailability(final boolean mIsAvailable) {
        this.mIsAvailable = mIsAvailable;
        this.update();
    }
    
    public void setMode(final Mode mode) {
        this.mMruButton.setMode(mode);
    }
    
    public void setOnModeSelectListener(final ModeSelector.OnModeSelectListener mModeSelectListener) {
        this.mModeSelectListener = mModeSelectListener;
    }
    
    public void setup(final ModeLoader mModeLoader) {
        (this.mModeLoader = mModeLoader).addModeChangeListener(this.mModeListChangeListener);
    }
    
    public void show() {
        this.mRequestVisible = true;
        this.update();
    }
    
    public void update() {
        if (this.mIsAvailable && this.mRequestVisible) {
            if (this.mMruButton != null) {
                this.mMruButton.setClickable(true);
            }
            this.setVisibility(0);
        }
        else {
            if (this.mMruButton != null) {
                this.mMruButton.setClickable(false);
            }
            this.setVisibility(8);
        }
    }
    
    private class MruButton
    {
        private ImageView mButton;
        private View$OnClickListener mClickListener;
        protected Mode mMode;
        final MruButtonContainer this$0;
        
        MruButton(final MruButtonContainer this$0) {
            this.this$0 = this$0;
            this.mClickListener = (View$OnClickListener)new View$OnClickListener() {
                final MruButton this$1;
                
                public void onClick(final View view) {
                    if (this.this$1.mMode == null) {
                        return;
                    }
                    if (this.this$1.this$0.mModeSelectListener != null) {
                        this.this$1.this$0.mModeSelectListener.onModeSelected(this.this$1.mMode, true);
                    }
                }
            };
            (this.mButton = (ImageView)this$0.findViewById(2131296473)).setOnClickListener(this.mClickListener);
        }
        
        Mode getMode() {
            return this.mMode;
        }
        
        void setClickable(final boolean clickable) {
            this.mButton.setClickable(clickable);
        }
        
        void setMode(final Mode mMode) {
            this.mMode = mMode;
            if (mMode == null) {
                this.this$0.setAvailability(false);
                return;
            }
            mMode.loadSmallIcon(this.mButton);
            this.this$0.setAvailability(mMode.isAvailable());
        }
    }
    
    private class MruSmallModeButton extends MruButton
    {
        private SharedPreferencesAccessor mPreferenceAccessor;
        final MruButtonContainer this$0;
        
        private MruSmallModeButton(final MruButtonContainer this$0) {
            this.this$0 = this$0.super();
        }
        
        @Override
        void setMode(final Mode mode) {
            if (this.mPreferenceAccessor == null) {
                this.mPreferenceAccessor = new SharedPreferencesAccessor(this.this$0.getContext(), "mode-shortcut");
            }
            Mode byId;
            if ((byId = mode) == null) {
                String s;
                if ((s = this.mPreferenceAccessor.readString("MODE_SHORTCUT_ID", null)) == null) {
                    s = AddonMode.generateId(this.this$0.getContext().getPackageName(), "GOOGLE_LENS");
                }
                byId = this.this$0.mModeLoader.findById(s);
            }
            super.setMode(byId);
            if (byId != null) {
                this.mPreferenceAccessor.writeString("MODE_SHORTCUT_ID", byId.getId(), false);
                this.mPreferenceAccessor.apply();
            }
        }
    }
}
