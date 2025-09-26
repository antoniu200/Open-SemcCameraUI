// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.selectabledialog;

import com.sonyericsson.android.camera.view.modeselector.AddonMode;
import com.sonyericsson.android.camera.view.modeselector.InternalMode;
import com.sonyericsson.android.camera.view.modeselector.view.CapturingModePanelAttributes;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import com.sonyericsson.android.camera.view.modeselector.view.CapturingModePanelView;
import com.sonyericsson.android.camera.view.modeselector.view.AbsPanelView;
import com.sonyericsson.android.camera.view.modeselector.view.AbsAppsUiSelectorAdapter;
import android.widget.FrameLayout;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.CameraApplication;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import com.sonyericsson.android.camera.view.modeselector.CapturingModeAttributes;
import com.sonyericsson.android.camera.view.modeselector.Mode;
import java.util.List;
import android.view.View;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.view.modeselector.ModeSelectorInternalMode;
import android.graphics.Rect;
import android.view.View$OnClickListener;
import android.content.Context;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.modeselector.ModeLoader;

public class ModeSelector extends AbsSelectableDialog implements OnModeListChangeListener
{
    protected static final boolean DEBUG;
    protected static final String TAG = "ModeSelector";
    private ModeLoader mModeLoader;
    private OnModeSelectListener mOnModeSelectListener;
    
    static {
        DEBUG = CamLog.VERBOSE;
    }
    
    public ModeSelector(final Context context, final Params params, final int n, final boolean b) {
        super(context, params, b);
        final ModeSelectorView mDialogScrollView = new ModeSelectorView(context);
        mDialogScrollView.setOnItemClickListener((View$OnClickListener)new OnItemClickListener());
        mDialogScrollView.setup(true, params, this, -1, n, b);
        if (b) {
            mDialogScrollView.setScrollStatus(Status.FULLSCREEN);
        }
        this.mDialogScrollView = mDialogScrollView;
    }
    
    private void adjustAttachedContainer() {
        if (this.isPortrait()) {
            this.mSettingDialogStack.addBottomView();
            this.mSettingDialogStack.adjustContainer(false);
        }
        else {
            this.mSettingDialogStack.removeBottomView();
            this.mSettingDialogStack.adjustContainer(true);
        }
    }
    
    @Override
    public void closeImmediate() {
        this.mModeLoader.removeModeChangeListener((ModeLoader.OnModeListChangeListener)this);
        this.mSettingDialogStack.removeBottomView();
        this.mSettingDialogStack.adjustContainer(false);
        super.closeImmediate();
    }
    
    public Rect getMonochromeGlobalVisibleItemRect(final boolean b) {
        final View itemViewWithTag = this.mDialogScrollView.findItemViewWithTag(ModeSelectorInternalMode.DUAL_MONOCHROME.name());
        if (itemViewWithTag != null) {
            final Rect rect = new Rect();
            itemViewWithTag.getGlobalVisibleRect(rect);
            final int measuredWidth = this.mDialogScrollView.getContentLayout().getMeasuredWidth();
            final int measuredHeight = this.mDialogScrollView.getContentLayout().getMeasuredHeight();
            Rect rect2;
            if (b) {
                if (this.mOrientation == 2) {
                    rect2 = new Rect(0, 0, rect.left, measuredWidth);
                }
                else {
                    rect2 = new Rect(0, 0, rect.bottom, measuredWidth);
                }
            }
            else {
                final Rect viewFinderSize = LayoutDependencyResolver.getViewFinderSize(this.mContext);
                if (this.mOrientation == 2) {
                    rect2 = new Rect(0, 0, rect.left - (measuredHeight - viewFinderSize.height()), measuredWidth);
                }
                else {
                    rect2 = new Rect(0, 0, rect.bottom - (measuredHeight - viewFinderSize.height()), measuredWidth);
                }
            }
            return rect2;
        }
        return null;
    }
    
    @Override
    public void onModeListChanged(final List<Mode> list, final List<CapturingModeAttributes> list2) {
        final ArrayList list3 = new ArrayList();
        for (int i = 0; i < list.size(); ++i) {
            if (((Mode)list.get(i)).isAvailable()) {
                list3.add(list2.get(i));
            }
        }
        final CapturingModeSelectorAdapter capturingModeSelectorAdapter = new CapturingModeSelectorAdapter(this.mContext);
        capturingModeSelectorAdapter.updateItems(CapturingModeAttributes.toAttributesList(this.mContext, list3));
        if (this.mDialogScrollView != null) {
            this.addPanel(capturingModeSelectorAdapter);
            this.adjustLayout();
            this.startOpenAnimation();
        }
    }
    
    @Override
    protected void onOrientationChanged(final int uiOrientation) {
        this.mDialogScrollView.setUiOrientation(uiOrientation);
        if (this.mDialogScrollView.isAttachedToWindow()) {
            this.adjustAttachedContainer();
            this.adjustLayout();
        }
    }
    
    @Override
    public void onScrollFinished(final Status status) {
        if (status == Status.EXIT) {
            CameraApplication.getUiThreadHandler().post((Runnable)new Runnable(this) {
                final ModeSelector this$0;
                
                @Override
                public void run() {
                    this.this$0.mSettingDialogStack.closeAllSettingDialogs();
                }
            });
        }
    }
    
    @Override
    public void open(final ViewGroup viewGroup) {
        (this.mParent = (FrameLayout)viewGroup).addView((View)this.mDialogScrollView);
        this.adjustAttachedContainer();
        this.adjustLayout();
        this.mModeLoader.addModeChangeListener((ModeLoader.OnModeListChangeListener)this);
    }
    
    public void setModeLoader(final ModeLoader mModeLoader) {
        this.mModeLoader = mModeLoader;
    }
    
    public void setOnModeSelectListener(final OnModeSelectListener mOnModeSelectListener) {
        this.mOnModeSelectListener = mOnModeSelectListener;
    }
    
    private class CapturingModeSelectorAdapter extends AbsAppsUiSelectorAdapter
    {
        final ModeSelector this$0;
        
        public CapturingModeSelectorAdapter(final ModeSelector this$0, final Context context) {
            this.this$0 = this$0;
            super(context, -1, null);
        }
        
        public View onCreateItemView(final int n, final ViewGroup viewGroup) {
            return this.getLayoutInflater().inflate(2131492940, (ViewGroup)null);
        }
        
        public View onPrepareItemView(final int n, final AbsPanelView absPanelView, final ViewGroup viewGroup) {
            final CapturingModePanelView capturingModePanelView = (CapturingModePanelView)absPanelView;
            if (capturingModePanelView.getLayoutParams() == null) {
                capturingModePanelView.setLayoutParams((ViewGroup$LayoutParams)new LinearLayout$LayoutParams(this.this$0.mParams.itemWidth, this.this$0.mParams.itemHeight));
            }
            return super.onPrepareItemView(n, absPanelView, viewGroup);
        }
    }
    
    private class OnItemClickListener implements View$OnClickListener
    {
        final ModeSelector this$0;
        
        private OnItemClickListener(final ModeSelector this$0) {
            this.this$0 = this$0;
        }
        
        public void onClick(final View view) {
            if (view.getTag() != null && CapturingModePanelAttributes.class.isAssignableFrom(view.getTag().getClass())) {
                final CapturingModePanelAttributes capturingModePanelAttributes = (CapturingModePanelAttributes)view.getTag();
                final Mode mode = null;
                final ModeSelectorInternalMode[] values = ModeSelectorInternalMode.values();
                final int length = values.length;
                int n = 0;
                Mode byId;
                while (true) {
                    byId = mode;
                    if (n >= length) {
                        break;
                    }
                    final ModeSelectorInternalMode modeSelectorInternalMode = values[n];
                    if (modeSelectorInternalMode.name().equals(capturingModePanelAttributes.getModeName())) {
                        byId = this.this$0.mModeLoader.findById(InternalMode.generateId(this.this$0.mContext, modeSelectorInternalMode));
                        if (modeSelectorInternalMode != ModeSelectorInternalMode.DUAL_MONOCHROME) {
                            this.this$0.mSettingDialogStack.closeAllSettingDialogs();
                            byId = byId;
                            break;
                        }
                        break;
                    }
                    else {
                        ++n;
                    }
                }
                Mode byId2;
                if ((byId2 = byId) == null) {
                    byId2 = this.this$0.mModeLoader.findById(AddonMode.generateId(capturingModePanelAttributes.getPackageName(), capturingModePanelAttributes.getModeName()));
                }
                if (this.this$0.mOnModeSelectListener != null) {
                    this.this$0.mOnModeSelectListener.onModeSelected(byId2, false);
                }
            }
        }
    }
    
    public interface OnModeSelectListener
    {
        void onModeSelected(final Mode p0, final boolean p1);
    }
}
