package com.sonyericsson.android.camera.view.baselayout.settingshortcut;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.SharedPreferencesConstants;
import com.sonyericsson.android.camera.setting.SharedPreferencesAccessor;
import com.sonyericsson.android.camera.view.modeselector.CapturingModeAttributes;
import com.sonyericsson.android.camera.view.modeselector.InternalMode;
import com.sonyericsson.android.camera.view.modeselector.Mode;
import com.sonyericsson.android.camera.view.modeselector.ModeLoader;
import com.sonyericsson.android.camera.view.modeselector.internalmode.googlelens.GoogleLensMode;
import com.sonyericsson.android.camera.view.selectabledialog.ModeSelector;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class MruButtonContainer extends FrameLayout {
    private boolean mIsAvailable;
    private Mode.OnStateChangeListener mModeChangeListener;
    private ModeLoader.OnModeListChangeListener mModeListChangeListener;
    private ModeLoader mModeLoader;
    private ModeSelector.OnModeSelectListener mModeSelectListener;
    private MruButton mMruButton;
    private boolean mRequestVisible;

    public MruButtonContainer(@NonNull Context context) {
        super(context);
        this.mModeChangeListener = new Mode.OnStateChangeListener() { // from class: com.sonyericsson.android.camera.view.baselayout.settingshortcut.MruButtonContainer.1
            @Override // com.sonyericsson.android.camera.view.modeselector.Mode.OnStateChangeListener
            public void onAvailabilityChanged(Mode mode, boolean z) {
                if (mode == null || !mode.compare(MruButtonContainer.this.mMruButton.getMode())) {
                    return;
                }
                MruButtonContainer.this.setAvailability(z);
            }
        };
        this.mModeListChangeListener = new ModeLoader.OnModeListChangeListener() { // from class: com.sonyericsson.android.camera.view.baselayout.settingshortcut.MruButtonContainer.2
            @Override // com.sonyericsson.android.camera.view.modeselector.ModeLoader.OnModeListChangeListener
            public void onModeListChanged(List<Mode> list, List<CapturingModeAttributes> list2) throws Resources.NotFoundException {
                Iterator<Mode> it = list.iterator();
                while (it.hasNext()) {
                    it.next().setOnStateChangeListener(MruButtonContainer.this.mModeChangeListener);
                }
                MruButtonContainer.this.mMruButton.setMode(null);
            }
        };
        init();
    }

    public MruButtonContainer(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mModeChangeListener = new Mode.OnStateChangeListener() { // from class: com.sonyericsson.android.camera.view.baselayout.settingshortcut.MruButtonContainer.1
            @Override // com.sonyericsson.android.camera.view.modeselector.Mode.OnStateChangeListener
            public void onAvailabilityChanged(Mode mode, boolean z) {
                if (mode == null || !mode.compare(MruButtonContainer.this.mMruButton.getMode())) {
                    return;
                }
                MruButtonContainer.this.setAvailability(z);
            }
        };
        this.mModeListChangeListener = new ModeLoader.OnModeListChangeListener() { // from class: com.sonyericsson.android.camera.view.baselayout.settingshortcut.MruButtonContainer.2
            @Override // com.sonyericsson.android.camera.view.modeselector.ModeLoader.OnModeListChangeListener
            public void onModeListChanged(List<Mode> list, List<CapturingModeAttributes> list2) throws Resources.NotFoundException {
                Iterator<Mode> it = list.iterator();
                while (it.hasNext()) {
                    it.next().setOnStateChangeListener(MruButtonContainer.this.mModeChangeListener);
                }
                MruButtonContainer.this.mMruButton.setMode(null);
            }
        };
        init();
    }

    public MruButtonContainer(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mModeChangeListener = new Mode.OnStateChangeListener() { // from class: com.sonyericsson.android.camera.view.baselayout.settingshortcut.MruButtonContainer.1
            @Override // com.sonyericsson.android.camera.view.modeselector.Mode.OnStateChangeListener
            public void onAvailabilityChanged(Mode mode, boolean z) {
                if (mode == null || !mode.compare(MruButtonContainer.this.mMruButton.getMode())) {
                    return;
                }
                MruButtonContainer.this.setAvailability(z);
            }
        };
        this.mModeListChangeListener = new ModeLoader.OnModeListChangeListener() { // from class: com.sonyericsson.android.camera.view.baselayout.settingshortcut.MruButtonContainer.2
            @Override // com.sonyericsson.android.camera.view.modeselector.ModeLoader.OnModeListChangeListener
            public void onModeListChanged(List<Mode> list, List<CapturingModeAttributes> list2) throws Resources.NotFoundException {
                Iterator<Mode> it = list.iterator();
                while (it.hasNext()) {
                    it.next().setOnStateChangeListener(MruButtonContainer.this.mModeChangeListener);
                }
                MruButtonContainer.this.mMruButton.setMode(null);
            }
        };
        init();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mMruButton = new MruSmallModeButton();
    }

    private void init() {
        this.mIsAvailable = false;
        this.mRequestVisible = false;
        update();
    }

    public void setup(ModeLoader modeLoader) {
        this.mModeLoader = modeLoader;
        this.mModeLoader.addModeChangeListener(this.mModeListChangeListener);
    }

    public void show() {
        this.mRequestVisible = true;
        update();
    }

    public void hide() {
        this.mRequestVisible = false;
        update();
    }

    public void setAvailability(boolean z) {
        this.mIsAvailable = z;
        update();
    }

    public void update() {
        if (this.mIsAvailable && this.mRequestVisible) {
            if (this.mMruButton != null) {
                this.mMruButton.setClickable(true);
            }
            setVisibility(0);
        } else {
            if (this.mMruButton != null) {
                this.mMruButton.setClickable(false);
            }
            setVisibility(8);
        }
    }

    public void setOnModeSelectListener(ModeSelector.OnModeSelectListener onModeSelectListener) {
        this.mModeSelectListener = onModeSelectListener;
    }

    public void setMode(Mode mode) {
        this.mMruButton.setMode(mode);
    }

    public boolean hasInternalMode() {
        if (this.mMruButton.getMode() == null) {
            return false;
        }
        return InternalMode.class.isAssignableFrom(this.mMruButton.getMode().getClass());
    }

    private class MruButton {
        private ImageView mButton;
        private View.OnClickListener mClickListener = new View.OnClickListener() { // from class: com.sonyericsson.android.camera.view.baselayout.settingshortcut.MruButtonContainer.MruButton.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (MruButton.this.mMode == null || MruButtonContainer.this.mModeSelectListener == null) {
                    return;
                }
                MruButtonContainer.this.mModeSelectListener.onModeSelected(MruButton.this.mMode, true);
            }
        };
        protected Mode mMode;

        MruButton() {
            this.mButton = (ImageView) MruButtonContainer.this.findViewById(R.id.mru_button);
            this.mButton.setOnClickListener(this.mClickListener);
        }

        void setClickable(boolean z) {
            this.mButton.setClickable(z);
        }

        void setMode(Mode mode) throws Resources.NotFoundException {
            this.mMode = mode;
            if (mode == null) {
                MruButtonContainer.this.setAvailability(false);
            } else {
                mode.loadSmallIcon(this.mButton);
                MruButtonContainer.this.setAvailability(mode.isAvailable());
            }
        }

        Mode getMode() {
            return this.mMode;
        }
    }

    private class MruSmallModeButton extends MruButton {
        private SharedPreferencesAccessor mPreferenceAccessor;

        private MruSmallModeButton() {
            super();
        }

        @Override // com.sonyericsson.android.camera.view.baselayout.settingshortcut.MruButtonContainer.MruButton
        void setMode(Mode mode) throws Resources.NotFoundException {
            if (this.mPreferenceAccessor == null) {
                this.mPreferenceAccessor = new SharedPreferencesAccessor(MruButtonContainer.this.getContext(), SharedPreferencesConstants.MODE_SHORTCUT_SHARED_PREFS_NAME);
            }
            if (mode == null) {
                String string = this.mPreferenceAccessor.readString(SharedPreferencesConstants.KEY_MODE_SHORTCUT_ID, null);
                if (string == null) {
                    string = GoogleLensMode.generateId(MruButtonContainer.this.getContext().getPackageName(), GoogleLensMode.MODE_NAME);
                }
                mode = MruButtonContainer.this.mModeLoader.findById(string);
            }
            super.setMode(mode);
            if (mode != null) {
                this.mPreferenceAccessor.writeString(SharedPreferencesConstants.KEY_MODE_SHORTCUT_ID, mode.getId(), false);
                this.mPreferenceAccessor.apply();
            }
        }
    }
}
