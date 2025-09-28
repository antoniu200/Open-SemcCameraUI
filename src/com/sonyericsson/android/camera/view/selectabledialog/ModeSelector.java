package com.sonyericsson.android.camera.view.selectabledialog;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.view.modeselector.AddonMode;
import com.sonyericsson.android.camera.view.modeselector.CapturingModeAttributes;
import com.sonyericsson.android.camera.view.modeselector.InternalMode;
import com.sonyericsson.android.camera.view.modeselector.Mode;
import com.sonyericsson.android.camera.view.modeselector.ModeLoader;
import com.sonyericsson.android.camera.view.modeselector.ModeSelectorInternalMode;
import com.sonyericsson.android.camera.view.modeselector.view.AbsAppsUiSelectorAdapter;
import com.sonyericsson.android.camera.view.modeselector.view.AbsPanelView;
import com.sonyericsson.android.camera.view.modeselector.view.CapturingModePanelAttributes;
import com.sonyericsson.android.camera.view.modeselector.view.CapturingModePanelView;
import com.sonyericsson.android.camera.view.selectabledialog.AbsSelectableDialog;
import com.sonyericsson.android.camera.view.selectabledialog.ScrollContainer;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ModeSelector extends AbsSelectableDialog implements ModeLoader.OnModeListChangeListener {
    protected static final boolean DEBUG = CamLog.VERBOSE;
    protected static final String TAG = "ModeSelector";
    private ModeLoader mModeLoader;
    private OnModeSelectListener mOnModeSelectListener;

    public interface OnModeSelectListener {
        void onModeSelected(Mode mode, boolean z);
    }

    private class CapturingModeSelectorAdapter extends AbsAppsUiSelectorAdapter {
        public CapturingModeSelectorAdapter(Context context) {
            super(context, -1, null);
        }

        @Override // com.sonyericsson.android.camera.view.modeselector.view.AbsAppsUiSelectorAdapter
        public View onCreateItemView(int i, ViewGroup viewGroup) {
            return getLayoutInflater().inflate(R.layout.mode_selector_grid_item_panel, (ViewGroup) null);
        }

        @Override // com.sonyericsson.android.camera.view.modeselector.view.AbsAppsUiSelectorAdapter
        public View onPrepareItemView(int i, AbsPanelView absPanelView, ViewGroup viewGroup) {
            CapturingModePanelView capturingModePanelView = (CapturingModePanelView) absPanelView;
            if (capturingModePanelView.getLayoutParams() == null) {
                capturingModePanelView.setLayoutParams(new LinearLayout.LayoutParams(ModeSelector.this.mParams.itemWidth, ModeSelector.this.mParams.itemHeight));
            }
            return super.onPrepareItemView(i, absPanelView, viewGroup);
        }
    }

    private class OnItemClickListener implements View.OnClickListener {
        private OnItemClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() == null || !CapturingModePanelAttributes.class.isAssignableFrom(view.getTag().getClass())) {
                return;
            }
            CapturingModePanelAttributes capturingModePanelAttributes = (CapturingModePanelAttributes) view.getTag();
            Mode modeFindById = null;
            ModeSelectorInternalMode[] modeSelectorInternalModeArrValues = ModeSelectorInternalMode.values();
            int length = modeSelectorInternalModeArrValues.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                ModeSelectorInternalMode modeSelectorInternalMode = modeSelectorInternalModeArrValues[i];
                if (modeSelectorInternalMode.name().equals(capturingModePanelAttributes.getModeName())) {
                    modeFindById = ModeSelector.this.mModeLoader.findById(InternalMode.generateId(ModeSelector.this.mContext, modeSelectorInternalMode));
                    if (modeSelectorInternalMode != ModeSelectorInternalMode.DUAL_MONOCHROME) {
                        ModeSelector.this.mSettingDialogStack.closeAllSettingDialogs();
                    }
                } else {
                    i++;
                }
            }
            if (modeFindById == null) {
                modeFindById = ModeSelector.this.mModeLoader.findById(AddonMode.generateId(capturingModePanelAttributes.getPackageName(), capturingModePanelAttributes.getModeName()));
            }
            if (ModeSelector.this.mOnModeSelectListener != null) {
                ModeSelector.this.mOnModeSelectListener.onModeSelected(modeFindById, false);
            }
        }
    }

    public ModeSelector(Context context, AbsSelectableDialog.Params params, int i, boolean z) {
        super(context, params, z);
        ModeSelectorView modeSelectorView = new ModeSelectorView(context);
        modeSelectorView.setOnItemClickListener(new OnItemClickListener());
        modeSelectorView.setup(true, params, this, -1, i, z);
        if (z) {
            modeSelectorView.setScrollStatus(ScrollContainer.Status.FULLSCREEN);
        }
        this.mDialogScrollView = modeSelectorView;
    }

    public void setOnModeSelectListener(OnModeSelectListener onModeSelectListener) {
        this.mOnModeSelectListener = onModeSelectListener;
    }

    @Override // com.sonyericsson.android.camera.view.setting.dialog.SettingDialogInterface
    public void open(ViewGroup viewGroup) {
        this.mParent = (FrameLayout) viewGroup;
        this.mParent.addView(this.mDialogScrollView);
        adjustAttachedContainer();
        adjustLayout();
        this.mModeLoader.addModeChangeListener(this);
    }

    public void setModeLoader(ModeLoader modeLoader) {
        this.mModeLoader = modeLoader;
    }

    @Override // com.sonyericsson.android.camera.view.modeselector.ModeLoader.OnModeListChangeListener
    public void onModeListChanged(List<Mode> list, List<CapturingModeAttributes> list2) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isAvailable()) {
                arrayList.add(list2.get(i));
            }
        }
        CapturingModeSelectorAdapter capturingModeSelectorAdapter = new CapturingModeSelectorAdapter(this.mContext);
        capturingModeSelectorAdapter.updateItems(CapturingModeAttributes.toAttributesList(this.mContext, arrayList));
        if (this.mDialogScrollView != null) {
            addPanel(capturingModeSelectorAdapter);
            adjustLayout();
            startOpenAnimation();
        }
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsSelectableDialog
    protected void onOrientationChanged(int i) {
        this.mDialogScrollView.setUiOrientation(i);
        if (this.mDialogScrollView.isAttachedToWindow()) {
            adjustAttachedContainer();
            adjustLayout();
        }
    }

    private void adjustAttachedContainer() {
        if (isPortrait()) {
            this.mSettingDialogStack.addBottomView();
            this.mSettingDialogStack.adjustContainer(false);
        } else {
            this.mSettingDialogStack.removeBottomView();
            this.mSettingDialogStack.adjustContainer(true);
        }
    }

    public Rect getMonochromeGlobalVisibleItemRect(boolean z) {
        View viewFindItemViewWithTag = this.mDialogScrollView.findItemViewWithTag(ModeSelectorInternalMode.DUAL_MONOCHROME.name());
        if (viewFindItemViewWithTag == null) {
            return null;
        }
        Rect rect = new Rect();
        viewFindItemViewWithTag.getGlobalVisibleRect(rect);
        int measuredWidth = this.mDialogScrollView.getContentLayout().getMeasuredWidth();
        int measuredHeight = this.mDialogScrollView.getContentLayout().getMeasuredHeight();
        if (z) {
            if (this.mOrientation == 2) {
                return new Rect(0, 0, rect.left, measuredWidth);
            }
            return new Rect(0, 0, rect.bottom, measuredWidth);
        }
        Rect viewFinderSize = LayoutDependencyResolver.getViewFinderSize(this.mContext);
        if (this.mOrientation == 2) {
            return new Rect(0, 0, rect.left - (measuredHeight - viewFinderSize.height()), measuredWidth);
        }
        return new Rect(0, 0, rect.bottom - (measuredHeight - viewFinderSize.height()), measuredWidth);
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsSelectableDialog
    public void closeImmediate() {
        this.mModeLoader.removeModeChangeListener(this);
        this.mSettingDialogStack.removeBottomView();
        this.mSettingDialogStack.adjustContainer(false);
        super.closeImmediate();
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsSelectableDialog, com.sonyericsson.android.camera.view.selectabledialog.ScrollContainer.OnScrollListener
    public void onScrollFinished(ScrollContainer.Status status) {
        if (status == ScrollContainer.Status.EXIT) {
            CameraApplication.getUiThreadHandler().post(new Runnable() { // from class: com.sonyericsson.android.camera.view.selectabledialog.ModeSelector.1
                @Override // java.lang.Runnable
                public void run() {
                    ModeSelector.this.mSettingDialogStack.closeAllSettingDialogs();
                }
            });
        }
    }
}
