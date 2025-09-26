// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.setting;

import android.view.MotionEvent;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import android.widget.ArrayAdapter;
import com.sonyericsson.android.camera.view.setting.dialog.SettingDialogFactory;
import com.sonyericsson.android.camera.view.modeselector.ModeLoader;
import android.widget.FrameLayout$LayoutParams;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import android.widget.ImageView;
import java.util.Iterator;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.ArrayList;
import android.view.View;
import java.util.List;
import android.graphics.Rect;
import com.sonyericsson.android.camera.view.selectabledialog.ModeSelector;
import com.sonyericsson.android.camera.view.setting.dialog.SettingDialogInterface;
import java.util.HashMap;
import com.sonyericsson.android.camera.view.setting.dialog.SettingDialogListener;
import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;
import com.sonyericsson.android.camera.view.selectabledialog.SettingMenu;
import android.content.Context;
import android.widget.FrameLayout;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import android.widget.LinearLayout;
import android.view.ViewGroup;

public class SettingDialogStack
{
    private static final String TAG = "SettingDialogStack";
    private final ViewGroup mBackground;
    private LinearLayout mBottomContainer;
    private CapturingMode mCapturingMode;
    private final FrameLayout mContainer;
    private final Context mContext;
    private SettingMenu mContextualMenu;
    private SettingMenu mContextualMenu2ndLayer;
    private SettingAdapter mContextualMenu2ndLayerAdapter;
    private SettingDialogListener mContextualMenuListener;
    private final HashMap<SettingDialogInterface, Object> mDialogTags;
    private ExclusiveViewListener mExclusiveViewListener;
    private boolean mIsCanceledOnTouchOutside;
    private ModeSelector mModeSelectorDialog;
    private SettingMenu mMonochromeDialog;
    private int mOrientation;
    private final Rect mScreenRect;
    private final List<SettingDialogListener> mSettingDialogListenerList;
    private SettingMenu mShortcutDialog;
    
    public SettingDialogStack(final Context mContext, final ViewGroup viewGroup, final Rect mScreenRect) {
        this.mContextualMenuListener = null;
        this.mIsCanceledOnTouchOutside = true;
        this.mContext = mContext;
        this.mScreenRect = mScreenRect;
        viewGroup.addView((View)(this.mBackground = (ViewGroup)new Background(this.mContext)));
        this.mBackground.getLayoutParams().width = -1;
        this.mBackground.getLayoutParams().height = -1;
        this.mBackground.setClickable(false);
        this.mBackground.setFocusable(false);
        this.mContainer = new FrameLayout(this.mContext);
        this.mBackground.addView((View)this.mContainer);
        this.adjustContainer(false);
        this.mShortcutDialog = null;
        this.mContextualMenu = null;
        this.mContextualMenu2ndLayer = null;
        this.mMonochromeDialog = null;
        this.mSettingDialogListenerList = new ArrayList<SettingDialogListener>();
        this.mDialogTags = new HashMap<SettingDialogInterface, Object>();
    }
    
    private boolean closeMenuDialog(final boolean b) {
        if (this.mContextualMenu != null) {
            this.mDialogTags.remove(this.mContextualMenu);
            if (CamLog.VERBOSE) {
                CamLog.d("mMenuDialog remove:");
            }
            if (b) {
                this.mContextualMenu.close();
            }
            else {
                this.mContextualMenu.closeImmediate();
            }
            this.mContextualMenu = null;
            this.notifyCloseContextualMenu(UserSettingKey.SETTING_MENU);
            return true;
        }
        return false;
    }
    
    private boolean closeModeSelectDialog(final boolean b) {
        if (this.mModeSelectorDialog != null) {
            this.mDialogTags.remove(this.mModeSelectorDialog);
            if (CamLog.VERBOSE) {
                CamLog.d("mControlDialog remove:");
            }
            if (b) {
                this.mModeSelectorDialog.close();
            }
            else {
                this.mModeSelectorDialog.closeImmediate();
            }
            this.mModeSelectorDialog = null;
            return true;
        }
        return false;
    }
    
    private boolean closeShortcutDialog(final boolean b) {
        if (this.mShortcutDialog != null) {
            this.mDialogTags.remove(this.mShortcutDialog);
            if (CamLog.VERBOSE) {
                CamLog.d("mShortcutDialog remove:");
            }
            if (b) {
                this.mShortcutDialog.close();
            }
            else {
                this.mShortcutDialog.closeImmediate();
            }
            this.mShortcutDialog = null;
            return true;
        }
        return false;
    }
    
    private boolean closemMonochromeDialog(final boolean b) {
        if (this.mMonochromeDialog != null) {
            this.mDialogTags.remove(this.mMonochromeDialog);
            if (CamLog.DEBUG) {
                CamLog.d("SettingDialogStack", "mMonochromeDialog remove:");
            }
            if (b) {
                this.mMonochromeDialog.close();
            }
            else {
                this.mMonochromeDialog.closeImmediate();
            }
            this.mMonochromeDialog = null;
            this.resetEnabledOfDialogs();
            return true;
        }
        return false;
    }
    
    private SettingDialogInterface getCurrentDialog() {
        if (CamLog.VERBOSE) {
            CamLog.d("getCurrentDialog");
        }
        for (final SettingDialogInterface settingDialogInterface : this.getDialogList()) {
            if (settingDialogInterface != null) {
                return settingDialogInterface;
            }
        }
        return null;
    }
    
    private SettingDialogInterface[] getDialogList() {
        return new SettingDialogInterface[] { this.mShortcutDialog, this.mContextualMenu2ndLayer, this.mContextualMenu, this.mMonochromeDialog, this.mModeSelectorDialog };
    }
    
    private void notifyCloseContextualMenu(final Object o) {
        if (this.mContextualMenuListener != null) {
            this.mContextualMenuListener.onCloseSettingDialog(o);
        }
    }
    
    private void notifyCloseSettingDialog(final Object o) {
        final Iterator<SettingDialogListener> iterator = this.mSettingDialogListenerList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onCloseSettingDialog(o);
        }
    }
    
    private void notifyOpenSettingDialog(final Object o) {
        final Iterator<SettingDialogListener> iterator = this.mSettingDialogListenerList.iterator();
        while (iterator.hasNext()) {
            iterator.next().onOpenSettingDialog(o);
        }
    }
    
    private void resetEnabledOfDialogs() {
        if (CamLog.VERBOSE) {
            CamLog.d("resetEnabledOfDialogs");
        }
        final SettingDialogInterface currentDialog = this.getCurrentDialog();
        for (final SettingDialogInterface settingDialogInterface : this.getDialogList()) {
            if (settingDialogInterface != null) {
                settingDialogInterface.setEnabled(settingDialogInterface == currentDialog);
            }
        }
    }
    
    public void addBottomView() {
        this.removeBottomView();
        this.mBottomContainer = new LinearLayout(this.mContext);
        this.mBackground.addView((View)this.mBottomContainer);
        this.mBottomContainer.setOrientation(1);
        final ImageView imageView = new ImageView(this.mContext);
        imageView.setBackgroundResource(2131231306);
        this.mBottomContainer.addView((View)imageView);
        imageView.getLayoutParams().width = -1;
        imageView.getLayoutParams().height = -2;
        final View view = new View(this.mContext);
        this.mBottomContainer.addView(view);
        view.setBackgroundColor(this.mContext.getResources().getColor(2131099756));
        view.getLayoutParams().width = -1;
        view.getLayoutParams().height = LayoutDependencyResolver.getNavigationBarMargin(this.mContext);
        this.mBottomContainer.setPivotX((float)(new Rect(LayoutDependencyResolver.getViewFinderSize(this.mContext)).width() - LayoutDependencyResolver.getNavigationBarMargin(this.mContext) - this.mContext.getResources().getDimensionPixelSize(2131165580)));
        this.mBottomContainer.setPivotY(0.0f);
        this.mBottomContainer.setRotation(-90.0f);
    }
    
    public void addDialogListener(final SettingDialogListener settingDialogListener) {
        this.mSettingDialogListenerList.add(settingDialogListener);
    }
    
    public void adjustContainer(final boolean b) {
        if (!b) {
            int navigationBarMargin;
            if (!LayoutDependencyResolver.isTablet(this.mContext)) {
                navigationBarMargin = LayoutDependencyResolver.getNavigationBarMargin(this.mContext);
            }
            else {
                navigationBarMargin = 0;
            }
            this.mContainer.getLayoutParams().width = this.mScreenRect.width();
            this.mContainer.getLayoutParams().height = this.mScreenRect.height();
            this.mContainer.setPadding(0, 0, navigationBarMargin, 0);
        }
        else {
            this.mContainer.getLayoutParams().width = this.mScreenRect.width();
            this.mContainer.getLayoutParams().height = this.mScreenRect.height();
            this.mContainer.setPadding(0, 0, 0, 0);
        }
        ((FrameLayout$LayoutParams)this.mContainer.getLayoutParams()).gravity = 51;
        ((FrameLayout$LayoutParams)this.mContainer.getLayoutParams()).topMargin = this.mScreenRect.top;
        ((FrameLayout$LayoutParams)this.mContainer.getLayoutParams()).leftMargin = this.mScreenRect.left;
        this.mContainer.requestLayout();
    }
    
    public void closeAllSettingDialogs() {
        this.closeAllSettingDialogs(false);
    }
    
    public void closeAllSettingDialogs(final boolean b) {
        Object o;
        if (this.mContextualMenu2ndLayer != null) {
            o = this.mDialogTags.get(this.mContextualMenu2ndLayer);
        }
        else {
            o = null;
        }
        final boolean closeSecondLayerDialog = this.closeSecondLayerDialog(b);
        if (this.mMonochromeDialog != null) {
            o = this.mDialogTags.get(this.mMonochromeDialog);
        }
        final boolean closemMonochromeDialog = this.closemMonochromeDialog(b);
        if (this.mShortcutDialog != null) {
            o = this.mDialogTags.get(this.mShortcutDialog);
        }
        final boolean closeShortcutDialog = this.closeShortcutDialog(b);
        if (this.mContextualMenu != null) {
            o = UserSettingKey.SETTING_MENU;
        }
        final boolean closeMenuDialog = this.closeMenuDialog(b);
        if (this.mModeSelectorDialog != null) {
            o = this.mDialogTags.get(this.mModeSelectorDialog);
        }
        final boolean closeModeSelectDialog = this.closeModeSelectDialog(b);
        this.resetEnabledOfDialogs();
        if (CamLog.VERBOSE) {
            CamLog.d("closeAllSettingDialogs");
        }
        if ((closeModeSelectDialog | (closeSecondLayerDialog | false | closemMonochromeDialog | closeShortcutDialog | closeMenuDialog)) && !this.isDialogOpened()) {
            this.mContainer.clearFocus();
            this.notifyCloseSettingDialog(o);
            if (CamLog.VERBOSE) {
                CamLog.d("closeAllSettingDialogs:NOTIFY CLOSE CALLBACK");
            }
        }
    }
    
    public boolean closeCurrentDialog() {
        if (CamLog.VERBOSE) {
            CamLog.d("closeCurrentDialogs.");
        }
        Object value = null;
        if (this.mContextualMenu2ndLayer != null) {
            value = this.mDialogTags.get(this.mContextualMenu2ndLayer);
        }
        final boolean closeSecondLayerDialog = this.closeSecondLayerDialog(true);
        Object o = value;
        boolean closemMonochromeDialog = closeSecondLayerDialog;
        if (!closeSecondLayerDialog) {
            o = value;
            if (this.mMonochromeDialog != null) {
                o = this.mDialogTags.get(this.mMonochromeDialog);
            }
            closemMonochromeDialog = this.closemMonochromeDialog(true);
        }
        Object o2 = o;
        boolean closeShortcutDialog = closemMonochromeDialog;
        if (!closemMonochromeDialog) {
            if (this.mShortcutDialog != null) {
                o = this.mDialogTags.get(this.mShortcutDialog);
            }
            closeShortcutDialog = this.closeShortcutDialog(true);
            o2 = o;
        }
        Object o3 = o2;
        boolean closeMenuDialog = closeShortcutDialog;
        if (!closeShortcutDialog) {
            o3 = o2;
            if (this.mContextualMenu != null) {
                o3 = UserSettingKey.SETTING_MENU;
            }
            closeMenuDialog = this.closeMenuDialog(true);
        }
        Object o4 = o3;
        boolean closeModeSelectDialog = closeMenuDialog;
        if (!closeMenuDialog) {
            if (this.mModeSelectorDialog != null) {
                o3 = this.mDialogTags.get(this.mModeSelectorDialog);
            }
            closeModeSelectDialog = this.closeModeSelectDialog(true);
            o4 = o3;
        }
        this.resetEnabledOfDialogs();
        if (closeModeSelectDialog) {
            if (!this.isDialogOpened()) {
                this.mContainer.clearFocus();
                this.notifyCloseSettingDialog(o4);
                if (CamLog.VERBOSE) {
                    CamLog.d("closeCurrentDialogs:NOTIFY CLOSE CALLBACK");
                }
            }
            else {
                this.notifyCloseSettingDialog(o4);
                if (CamLog.VERBOSE) {
                    CamLog.d("closeCurrentDialogs:NOTIFY CLOSE CALLBACK NOT ALL DIALOGS");
                }
            }
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("closeCurrentDialogs: consumed: ");
            sb.append(closeModeSelectDialog);
            CamLog.d(sb.toString());
        }
        return closeModeSelectDialog;
    }
    
    public boolean closeSecondLayerDialog(final boolean b) {
        if (this.mContextualMenu2ndLayer != null) {
            this.mDialogTags.remove(this.mContextualMenu2ndLayer);
            if (CamLog.VERBOSE) {
                CamLog.d("mSecondLayerDialog remove:");
            }
            if (b) {
                this.mContextualMenu2ndLayer.close();
            }
            else {
                this.mContextualMenu2ndLayer.closeImmediate();
            }
            this.mContextualMenu2ndLayer = null;
            this.resetEnabledOfDialogs();
            return true;
        }
        return false;
    }
    
    public View getBackground() {
        return (View)this.mBackground;
    }
    
    public int getBackgroundHeight() {
        return this.mContainer.getMeasuredHeight();
    }
    
    public int getBackgroundWidth() {
        return this.mContainer.getMeasuredWidth();
    }
    
    public SettingMenu getContextualMenu() {
        return this.mContextualMenu;
    }
    
    public boolean isDialogOpened() {
        return this.mContextualMenu != null || this.mShortcutDialog != null || this.mContextualMenu2ndLayer != null || this.mModeSelectorDialog != null || this.mMonochromeDialog != null;
    }
    
    public boolean isMenuDialogOpened() {
        return this.mContextualMenu != null;
    }
    
    public boolean isOpened(final Object obj) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("isOpened:");
            sb.append(this.mDialogTags.values());
            CamLog.d(sb.toString());
        }
        for (final Object next : this.mDialogTags.values()) {
            if (next != null && next.equals(obj)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isSecondLayerDialogOpened() {
        return this.mContextualMenu2ndLayer != null;
    }
    
    public boolean isShortcutDialogOpened() {
        return this.mShortcutDialog != null;
    }
    
    public boolean openMenuDialog(final SettingMenu mContextualMenu, final Object o, final boolean b) {
        if (CamLog.VERBOSE) {
            CamLog.d("openFirstLayerMenuDialog");
        }
        if (this.mContextualMenu != null) {
            return false;
        }
        this.closeMenuDialog(false);
        this.closeShortcutDialog(false);
        this.closeSecondLayerDialog(false);
        this.closeModeSelectDialog(false);
        this.closemMonochromeDialog(false);
        (this.mContextualMenu = mContextualMenu).setSettingDialogStack(this);
        this.mContextualMenu.setSensorOrientation(this.mOrientation);
        if (b) {
            this.mContextualMenu.openImmediate((ViewGroup)this.mContainer);
        }
        else {
            this.mContextualMenu.open((ViewGroup)this.mContainer);
        }
        this.mDialogTags.put(this.mContextualMenu, o);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("mMenuDialog put:");
            sb.append(o);
            CamLog.d(sb.toString());
        }
        this.resetEnabledOfDialogs();
        this.mContainer.requestFocus();
        this.notifyOpenSettingDialog(UserSettingKey.SETTING_MENU);
        if (CamLog.VERBOSE) {
            CamLog.d("openMenuDialog:NOTIFY OPEN CALLBACK");
        }
        return true;
    }
    
    public boolean openModeSelectorDialog(final ModeLoader modeLoader, final ModeSelector.OnModeSelectListener onModeSelectListener) {
        if (this.mModeSelectorDialog != null) {
            return false;
        }
        this.closeMenuDialog(false);
        this.closeShortcutDialog(false);
        this.closeSecondLayerDialog(false);
        this.closeModeSelectDialog(false);
        this.closemMonochromeDialog(false);
        this.adjustContainer(this.mOrientation == 2);
        (this.mModeSelectorDialog = SettingDialogFactory.createModeSelector(this.mContext, this.getBackgroundWidth(), this.getBackgroundHeight(), false)).setSettingDialogStack(this);
        this.mModeSelectorDialog.setSensorOrientation(this.mOrientation);
        this.mModeSelectorDialog.setOnModeSelectListener(onModeSelectListener);
        this.mModeSelectorDialog.setModeLoader(modeLoader);
        this.mModeSelectorDialog.open((ViewGroup)this.mContainer);
        this.mDialogTags.put(this.mModeSelectorDialog, null);
        this.resetEnabledOfDialogs();
        this.mContainer.requestFocus();
        this.notifyOpenSettingDialog(null);
        return true;
    }
    
    public boolean openMonochromeDialog(final SettingAdapter settingAdapter, final int n) {
        if (CamLog.DEBUG) {
            CamLog.d("SettingDialogStack", "openMonochromeDialog");
        }
        boolean b = false;
        this.closemMonochromeDialog(false);
        Rect monochromeGlobalVisibleItemRect;
        if (this.mModeSelectorDialog != null) {
            if ((monochromeGlobalVisibleItemRect = this.mModeSelectorDialog.getMonochromeGlobalVisibleItemRect(true)) == null) {
                return false;
            }
        }
        else {
            monochromeGlobalVisibleItemRect = new Rect(0, 0, n, 0);
            b = true;
        }
        (this.mMonochromeDialog = SettingDialogFactory.createMonochromeDialog(this.mContext, b, this.mContainer.getMeasuredWidth(), this.mContainer.getMeasuredHeight())).setSettingDialogStack(this);
        this.mMonochromeDialog.addPanel(settingAdapter);
        this.mMonochromeDialog.setTopMarginHint(monochromeGlobalVisibleItemRect.right);
        this.mMonochromeDialog.setBottomMarginHint(monochromeGlobalVisibleItemRect.bottom);
        this.mMonochromeDialog.setSensorOrientation(this.mOrientation);
        this.mMonochromeDialog.open((ViewGroup)this.mContainer);
        this.mDialogTags.put(this.mMonochromeDialog, null);
        this.resetEnabledOfDialogs();
        this.mContainer.requestFocus();
        this.notifyOpenSettingDialog(null);
        if (CamLog.DEBUG) {
            CamLog.d("SettingDialogStack", "openMonochromeDialog:NOTIFY OPEN CALLBACK");
        }
        return true;
    }
    
    public boolean openSecondLayerDialog(final SettingAdapter mContextualMenu2ndLayerAdapter, final Object obj) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("openSecondLayerDialog: ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        this.closeSecondLayerDialog(false);
        if (this.mContextualMenu == null) {
            return false;
        }
        final Rect rect = new Rect();
        if (!this.mContainer.getGlobalVisibleRect(rect)) {
            return false;
        }
        final Rect rect2 = new Rect();
        if (!this.mContextualMenu.getGlobalVisibleItemRect(rect2, obj)) {
            return false;
        }
        Rect rect3;
        Rect rect4;
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            rect3 = rect2;
            if (this.mOrientation == 2) {
                rect3 = new Rect(rect2.top, rect.width() - rect2.right, rect2.bottom, rect.width() - rect2.left);
            }
            rect4 = new Rect(0, 0, rect.height(), rect.width());
        }
        else {
            rect4 = rect;
            rect3 = rect2;
            if (this.mOrientation == 1) {
                rect3 = new Rect(rect2.bottom, rect2.left, rect2.top, rect2.right);
                rect4 = rect;
            }
        }
        if (obj != UserSettingKey.SLOW_MOTION && (obj != UserSettingKey.VIDEO_SIZE || this.mCapturingMode != CapturingMode.SLOW_MOTION)) {
            this.mContextualMenu2ndLayer = SettingDialogFactory.createSecondLayerDialog(this.mContext, this.mContainer.getMeasuredWidth(), this.mContainer.getMeasuredHeight(), this.mContextualMenu.isExpanded());
        }
        else {
            this.mContextualMenu2ndLayer = SettingDialogFactory.createSecondLayerDialogDetails(this.mContext, this.mContainer.getMeasuredWidth(), this.mContainer.getMeasuredHeight(), this.mContextualMenu.isExpanded());
        }
        this.mContextualMenu2ndLayer.setSettingDialogStack(this);
        this.mContextualMenu2ndLayer.addPanel(mContextualMenu2ndLayerAdapter);
        final int width = rect4.width();
        final int height = rect4.height();
        if (this.mOrientation == 2) {
            if (this.mContextualMenu.isExpanded()) {
                this.mContextualMenu2ndLayer.setTopMarginHint(rect3.bottom);
            }
            else {
                this.mContextualMenu2ndLayer.setTopMarginHint(rect3.bottom + (width - height));
            }
        }
        else {
            this.mContextualMenu2ndLayer.setTopMarginHint(rect3.bottom + this.mContainer.getPaddingRight());
        }
        this.mContextualMenu2ndLayer.setSensorOrientation(this.mOrientation);
        this.mContextualMenu2ndLayer.open((ViewGroup)this.mContainer);
        this.mDialogTags.put(this.mContextualMenu2ndLayer, obj);
        this.mContextualMenu2ndLayerAdapter = mContextualMenu2ndLayerAdapter;
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("mSecondLayerDialog put: ");
            sb2.append(obj);
            CamLog.d(sb2.toString());
        }
        this.resetEnabledOfDialogs();
        this.mContainer.requestFocus();
        this.notifyOpenSettingDialog(obj);
        if (CamLog.VERBOSE) {
            CamLog.d("openPopupDialog:NOTIFY OPEN CALLBACK");
        }
        return true;
    }
    
    public boolean openShortcutDialog(final SettingMenu mShortcutDialog, final Object o) {
        if (CamLog.VERBOSE) {
            CamLog.d("openShortcutDialog");
        }
        if (this.isOpened(o)) {
            return false;
        }
        this.closeMenuDialog(false);
        this.closeShortcutDialog(false);
        this.closeSecondLayerDialog(false);
        this.closeModeSelectDialog(false);
        this.closemMonochromeDialog(false);
        (this.mShortcutDialog = mShortcutDialog).setSensorOrientation(this.mOrientation);
        this.mShortcutDialog.open((ViewGroup)this.mContainer);
        this.mDialogTags.put(this.mShortcutDialog, o);
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("mShortcutDialog put:");
            sb.append(o);
            CamLog.d(sb.toString());
        }
        this.resetEnabledOfDialogs();
        this.mContainer.requestFocus();
        this.notifyOpenSettingDialog(o);
        if (CamLog.VERBOSE) {
            CamLog.d("openShortcutDialog OPEN CALLBACK");
        }
        return true;
    }
    
    public void removeBottomView() {
        if (this.mBottomContainer != null) {
            this.mBackground.removeView((View)this.mBottomContainer);
        }
    }
    
    public void removeDialogListener(final SettingDialogListener settingDialogListener) {
        this.mSettingDialogListenerList.remove(settingDialogListener);
    }
    
    public void reopenSecondLayerDialog() {
        if (this.mContextualMenu2ndLayerAdapter != null && this.mDialogTags.get(this.mContextualMenu2ndLayer) != null) {
            this.openSecondLayerDialog(this.mContextualMenu2ndLayerAdapter, this.mDialogTags.get(this.mContextualMenu2ndLayer));
        }
    }
    
    public void setCanceledOnTouchOutside(final boolean mIsCanceledOnTouchOutside) {
        this.mIsCanceledOnTouchOutside = mIsCanceledOnTouchOutside;
    }
    
    public void setCapturingMode(final CapturingMode mCapturingMode) {
        this.mCapturingMode = mCapturingMode;
    }
    
    public void setContextualMenuListener(final SettingDialogListener mContextualMenuListener) {
        this.mContextualMenuListener = mContextualMenuListener;
    }
    
    public void setExclusiveViewListener(final ExclusiveViewListener mExclusiveViewListener) {
        this.mExclusiveViewListener = mExclusiveViewListener;
    }
    
    public void setUiOrientation(int i) {
        this.mOrientation = i;
        final SettingDialogInterface[] dialogList = this.getDialogList();
        int length;
        SettingDialogInterface settingDialogInterface;
        for (length = dialogList.length, i = 0; i < length; ++i) {
            settingDialogInterface = dialogList[i];
            if (settingDialogInterface != null && settingDialogInterface != this.mMonochromeDialog) {
                settingDialogInterface.setSensorOrientation(this.mOrientation);
            }
        }
        if (this.mMonochromeDialog != null) {
            if (this.mModeSelectorDialog != null) {
                final Rect monochromeGlobalVisibleItemRect = this.mModeSelectorDialog.getMonochromeGlobalVisibleItemRect(false);
                this.mMonochromeDialog.setTopMarginHint(monochromeGlobalVisibleItemRect.right);
                this.mMonochromeDialog.setBottomMarginHint(monochromeGlobalVisibleItemRect.bottom);
            }
            this.mMonochromeDialog.setSensorOrientation(this.mOrientation);
        }
    }
    
    private class Background extends FrameLayout
    {
        private final Rect mBackgroundRect;
        private final Rect mItemRect;
        final SettingDialogStack this$0;
        
        public Background(final SettingDialogStack this$0, final Context context) {
            this.this$0 = this$0;
            super(context);
            this.mBackgroundRect = new Rect();
            this.mItemRect = new Rect();
        }
        
        protected void onMeasure(int n, final int n2) {
            super.onMeasure(n, n2);
            if (CamLog.VERBOSE) {
                CamLog.d("onMeasure() E");
            }
            if (this.this$0.mContextualMenu2ndLayer != null) {
                final Object value = this.this$0.mDialogTags.get(this.this$0.mContextualMenu2ndLayer);
                final Rect rect = new Rect();
                if (this.this$0.mContainer.getGlobalVisibleRect(rect)) {
                    if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
                        this.mBackgroundRect.left = rect.top;
                        this.mBackgroundRect.top = rect.left;
                        this.mBackgroundRect.right = rect.bottom;
                        this.mBackgroundRect.bottom = rect.right;
                    }
                    else {
                        this.mBackgroundRect.left = rect.left;
                        this.mBackgroundRect.top = rect.top;
                        this.mBackgroundRect.right = rect.right;
                        this.mBackgroundRect.bottom = rect.bottom;
                    }
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("  backgroundRect: (");
                        sb.append(this.mBackgroundRect.left);
                        sb.append(", ");
                        sb.append(this.mBackgroundRect.top);
                        sb.append(", ");
                        sb.append(this.mBackgroundRect.right);
                        sb.append(", ");
                        sb.append(this.mBackgroundRect.bottom);
                        sb.append(")");
                        CamLog.d(sb.toString());
                    }
                    final Rect rect2 = new Rect();
                    if (this.this$0.mContextualMenu.getGlobalVisibleItemRect(rect2, value)) {
                        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
                            this.mItemRect.left = rect2.top;
                            this.mItemRect.top = rect2.left;
                            this.mItemRect.right = rect2.bottom;
                            this.mItemRect.bottom = rect2.right;
                        }
                        else {
                            this.mItemRect.left = rect2.left;
                            this.mItemRect.top = rect2.top;
                            this.mItemRect.right = rect2.right;
                            this.mItemRect.bottom = rect2.bottom;
                        }
                        if (CamLog.VERBOSE) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("  itemRect: (");
                            sb2.append(this.mItemRect.left);
                            sb2.append(", ");
                            sb2.append(this.mItemRect.top);
                            sb2.append(", ");
                            sb2.append(this.mItemRect.right);
                            sb2.append(", ");
                            sb2.append(this.mItemRect.bottom);
                            sb2.append(")");
                            CamLog.d(sb2.toString());
                        }
                        if (this.this$0.mOrientation == 2) {
                            n = this.mBackgroundRect.bottom - this.mItemRect.bottom;
                        }
                        else {
                            n = this.mBackgroundRect.right - this.mItemRect.right;
                        }
                        this.this$0.mContextualMenu2ndLayer.setBottomMarginHint(n);
                        if (CamLog.VERBOSE) {
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("  margin-button:");
                            sb3.append(n);
                            CamLog.d(sb3.toString());
                        }
                    }
                }
            }
            if (CamLog.VERBOSE) {
                CamLog.d("onMeasure() X");
            }
        }
        
        public boolean onTouchEvent(final MotionEvent motionEvent) {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onTouchEvent: ");
                sb.append(motionEvent.getAction());
                CamLog.d(sb.toString());
            }
            final SettingDialogInterface access$500 = this.this$0.getCurrentDialog();
            if (access$500 != null) {
                switch (motionEvent.getAction()) {
                    case 1: {
                        if (!this.this$0.mIsCanceledOnTouchOutside) {
                            return true;
                        }
                        if (this.this$0.mContextualMenu != null && this.this$0.mContextualMenu.isOperationAcceptable()) {
                            return true;
                        }
                        final Rect rect = new Rect();
                        this.this$0.mBackground.getGlobalVisibleRect(rect);
                        if (!access$500.hitTest(rect.right - (int)motionEvent.getY(), rect.top + (int)motionEvent.getX())) {
                            this.this$0.closeCurrentDialog();
                        }
                        return true;
                    }
                    case 0: {
                        return this.this$0.mExclusiveViewListener == null || !this.this$0.mExclusiveViewListener.isExclusiveView((View)this.this$0.mBackground, motionEvent);
                    }
                }
            }
            return false;
        }
    }
    
    public interface ExclusiveViewListener
    {
        boolean isExclusiveView(final View p0, final MotionEvent p1);
    }
}
