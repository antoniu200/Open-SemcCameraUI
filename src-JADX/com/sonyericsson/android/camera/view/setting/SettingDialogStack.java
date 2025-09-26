package com.sonyericsson.android.camera.view.setting;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.view.modeselector.ModeLoader;
import com.sonyericsson.android.camera.view.selectabledialog.ModeSelector;
import com.sonyericsson.android.camera.view.selectabledialog.SettingMenu;
import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;
import com.sonyericsson.android.camera.view.setting.dialog.SettingDialogFactory;
import com.sonyericsson.android.camera.view.setting.dialog.SettingDialogInterface;
import com.sonyericsson.android.camera.view.setting.dialog.SettingDialogListener;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SettingDialogStack {
    private static final String TAG = "SettingDialogStack";
    private final ViewGroup mBackground;
    private LinearLayout mBottomContainer;
    private CapturingMode mCapturingMode;
    private final FrameLayout mContainer;
    private final Context mContext;
    private SettingMenu mContextualMenu;
    private SettingMenu mContextualMenu2ndLayer;
    private SettingAdapter mContextualMenu2ndLayerAdapter;
    private final HashMap<SettingDialogInterface, Object> mDialogTags;
    private ExclusiveViewListener mExclusiveViewListener;
    private ModeSelector mModeSelectorDialog;
    private SettingMenu mMonochromeDialog;
    private int mOrientation;
    private final Rect mScreenRect;
    private final List<SettingDialogListener> mSettingDialogListenerList;
    private SettingMenu mShortcutDialog;
    private SettingDialogListener mContextualMenuListener = null;
    private boolean mIsCanceledOnTouchOutside = true;

    public interface ExclusiveViewListener {
        boolean isExclusiveView(View view, MotionEvent motionEvent);
    }

    public SettingDialogStack(Context context, ViewGroup viewGroup, Rect rect) {
        this.mContext = context;
        this.mScreenRect = rect;
        this.mBackground = new Background(this.mContext);
        viewGroup.addView(this.mBackground);
        this.mBackground.getLayoutParams().width = -1;
        this.mBackground.getLayoutParams().height = -1;
        this.mBackground.setClickable(false);
        this.mBackground.setFocusable(false);
        this.mContainer = new FrameLayout(this.mContext);
        this.mBackground.addView(this.mContainer);
        adjustContainer(false);
        this.mShortcutDialog = null;
        this.mContextualMenu = null;
        this.mContextualMenu2ndLayer = null;
        this.mMonochromeDialog = null;
        this.mSettingDialogListenerList = new ArrayList();
        this.mDialogTags = new HashMap<>();
    }

    public void setContextualMenuListener(SettingDialogListener settingDialogListener) {
        this.mContextualMenuListener = settingDialogListener;
    }

    private void notifyCloseContextualMenu(Object obj) {
        if (this.mContextualMenuListener != null) {
            this.mContextualMenuListener.onCloseSettingDialog(obj);
        }
    }

    public void addDialogListener(SettingDialogListener settingDialogListener) {
        this.mSettingDialogListenerList.add(settingDialogListener);
    }

    public void removeDialogListener(SettingDialogListener settingDialogListener) {
        this.mSettingDialogListenerList.remove(settingDialogListener);
    }

    private void notifyOpenSettingDialog(Object obj) {
        Iterator<SettingDialogListener> it = this.mSettingDialogListenerList.iterator();
        while (it.hasNext()) {
            it.next().onOpenSettingDialog(obj);
        }
    }

    private void notifyCloseSettingDialog(Object obj) {
        Iterator<SettingDialogListener> it = this.mSettingDialogListenerList.iterator();
        while (it.hasNext()) {
            it.next().onCloseSettingDialog(obj);
        }
    }

    public boolean isDialogOpened() {
        return (this.mContextualMenu == null && this.mShortcutDialog == null && this.mContextualMenu2ndLayer == null && this.mModeSelectorDialog == null && this.mMonochromeDialog == null) ? false : true;
    }

    public boolean isMenuDialogOpened() {
        return this.mContextualMenu != null;
    }

    public boolean isShortcutDialogOpened() {
        return this.mShortcutDialog != null;
    }

    public boolean isSecondLayerDialogOpened() {
        return this.mContextualMenu2ndLayer != null;
    }

    public SettingMenu getContextualMenu() {
        return this.mContextualMenu;
    }

    public boolean isOpened(Object obj) {
        if (CamLog.VERBOSE) {
            CamLog.d("isOpened:" + this.mDialogTags.values());
        }
        for (Object obj2 : this.mDialogTags.values()) {
            if (obj2 != null && obj2.equals(obj)) {
                return true;
            }
        }
        return false;
    }

    public int getBackgroundWidth() {
        return this.mContainer.getMeasuredWidth();
    }

    public int getBackgroundHeight() {
        return this.mContainer.getMeasuredHeight();
    }

    public void addBottomView() {
        removeBottomView();
        this.mBottomContainer = new LinearLayout(this.mContext);
        this.mBackground.addView(this.mBottomContainer);
        this.mBottomContainer.setOrientation(1);
        ImageView imageView = new ImageView(this.mContext);
        imageView.setBackgroundResource(R.drawable.cam_setting_separator_shadow_icn);
        this.mBottomContainer.addView(imageView);
        imageView.getLayoutParams().width = -1;
        imageView.getLayoutParams().height = -2;
        View view = new View(this.mContext);
        this.mBottomContainer.addView(view);
        view.setBackgroundColor(this.mContext.getResources().getColor(R.color.setting_1st_layer_background_color));
        view.getLayoutParams().width = -1;
        view.getLayoutParams().height = LayoutDependencyResolver.getNavigationBarMargin(this.mContext);
        this.mBottomContainer.setPivotX((new Rect(LayoutDependencyResolver.getViewFinderSize(this.mContext)).width() - LayoutDependencyResolver.getNavigationBarMargin(this.mContext)) - this.mContext.getResources().getDimensionPixelSize(R.dimen.setting_1st_layer_value_bottom_shadow_height));
        this.mBottomContainer.setPivotY(0.0f);
        this.mBottomContainer.setRotation(-90.0f);
    }

    public void removeBottomView() {
        if (this.mBottomContainer != null) {
            this.mBackground.removeView(this.mBottomContainer);
        }
    }

    public boolean openModeSelectorDialog(ModeLoader modeLoader, ModeSelector.OnModeSelectListener onModeSelectListener) {
        if (this.mModeSelectorDialog != null) {
            return false;
        }
        closeMenuDialog(false);
        closeShortcutDialog(false);
        closeSecondLayerDialog(false);
        closeModeSelectDialog(false);
        closemMonochromeDialog(false);
        adjustContainer(this.mOrientation == 2);
        this.mModeSelectorDialog = SettingDialogFactory.createModeSelector(this.mContext, getBackgroundWidth(), getBackgroundHeight(), false);
        this.mModeSelectorDialog.setSettingDialogStack(this);
        this.mModeSelectorDialog.setSensorOrientation(this.mOrientation);
        this.mModeSelectorDialog.setOnModeSelectListener(onModeSelectListener);
        this.mModeSelectorDialog.setModeLoader(modeLoader);
        this.mModeSelectorDialog.open(this.mContainer);
        this.mDialogTags.put(this.mModeSelectorDialog, null);
        resetEnabledOfDialogs();
        this.mContainer.requestFocus();
        notifyOpenSettingDialog(null);
        return true;
    }

    public boolean openMonochromeDialog(SettingAdapter settingAdapter, int i) {
        Rect rect;
        if (CamLog.DEBUG) {
            CamLog.d(TAG, "openMonochromeDialog");
        }
        boolean z = false;
        closemMonochromeDialog(false);
        if (this.mModeSelectorDialog != null) {
            rect = this.mModeSelectorDialog.getMonochromeGlobalVisibleItemRect(true);
            if (rect == null) {
                return false;
            }
        } else {
            rect = new Rect(0, 0, i, 0);
            z = true;
        }
        this.mMonochromeDialog = SettingDialogFactory.createMonochromeDialog(this.mContext, z, this.mContainer.getMeasuredWidth(), this.mContainer.getMeasuredHeight());
        this.mMonochromeDialog.setSettingDialogStack(this);
        this.mMonochromeDialog.addPanel(settingAdapter);
        this.mMonochromeDialog.setTopMarginHint(rect.right);
        this.mMonochromeDialog.setBottomMarginHint(rect.bottom);
        this.mMonochromeDialog.setSensorOrientation(this.mOrientation);
        this.mMonochromeDialog.open(this.mContainer);
        this.mDialogTags.put(this.mMonochromeDialog, null);
        resetEnabledOfDialogs();
        this.mContainer.requestFocus();
        notifyOpenSettingDialog(null);
        if (CamLog.DEBUG) {
            CamLog.d(TAG, "openMonochromeDialog:NOTIFY OPEN CALLBACK");
        }
        return true;
    }

    public boolean openMenuDialog(SettingMenu settingMenu, Object obj, boolean z) {
        if (CamLog.VERBOSE) {
            CamLog.d("openFirstLayerMenuDialog");
        }
        if (this.mContextualMenu != null) {
            return false;
        }
        closeMenuDialog(false);
        closeShortcutDialog(false);
        closeSecondLayerDialog(false);
        closeModeSelectDialog(false);
        closemMonochromeDialog(false);
        this.mContextualMenu = settingMenu;
        this.mContextualMenu.setSettingDialogStack(this);
        this.mContextualMenu.setSensorOrientation(this.mOrientation);
        if (z) {
            this.mContextualMenu.openImmediate(this.mContainer);
        } else {
            this.mContextualMenu.open(this.mContainer);
        }
        this.mDialogTags.put(this.mContextualMenu, obj);
        if (CamLog.VERBOSE) {
            CamLog.d("mMenuDialog put:" + obj);
        }
        resetEnabledOfDialogs();
        this.mContainer.requestFocus();
        notifyOpenSettingDialog(UserSettingKey.SETTING_MENU);
        if (CamLog.VERBOSE) {
            CamLog.d("openMenuDialog:NOTIFY OPEN CALLBACK");
        }
        return true;
    }

    public View getBackground() {
        return this.mBackground;
    }

    public boolean openShortcutDialog(SettingMenu settingMenu, Object obj) {
        if (CamLog.VERBOSE) {
            CamLog.d("openShortcutDialog");
        }
        if (isOpened(obj)) {
            return false;
        }
        closeMenuDialog(false);
        closeShortcutDialog(false);
        closeSecondLayerDialog(false);
        closeModeSelectDialog(false);
        closemMonochromeDialog(false);
        this.mShortcutDialog = settingMenu;
        this.mShortcutDialog.setSensorOrientation(this.mOrientation);
        this.mShortcutDialog.open(this.mContainer);
        this.mDialogTags.put(this.mShortcutDialog, obj);
        if (CamLog.VERBOSE) {
            CamLog.d("mShortcutDialog put:" + obj);
        }
        resetEnabledOfDialogs();
        this.mContainer.requestFocus();
        notifyOpenSettingDialog(obj);
        if (CamLog.VERBOSE) {
            CamLog.d("openShortcutDialog OPEN CALLBACK");
        }
        return true;
    }

    public boolean openSecondLayerDialog(SettingAdapter settingAdapter, Object obj) {
        if (CamLog.VERBOSE) {
            CamLog.d("openSecondLayerDialog: " + obj);
        }
        closeSecondLayerDialog(false);
        if (this.mContextualMenu == null) {
            return false;
        }
        Rect rect = new Rect();
        if (!this.mContainer.getGlobalVisibleRect(rect)) {
            return false;
        }
        Rect rect2 = new Rect();
        if (!this.mContextualMenu.getGlobalVisibleItemRect(rect2, obj)) {
            return false;
        }
        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
            if (this.mOrientation == 2) {
                rect2 = new Rect(rect2.top, rect.width() - rect2.right, rect2.bottom, rect.width() - rect2.left);
            }
            rect = new Rect(0, 0, rect.height(), rect.width());
        } else if (this.mOrientation == 1) {
            rect2 = new Rect(rect2.bottom, rect2.left, rect2.top, rect2.right);
        }
        if (obj == UserSettingKey.SLOW_MOTION || (obj == UserSettingKey.VIDEO_SIZE && this.mCapturingMode == CapturingMode.SLOW_MOTION)) {
            this.mContextualMenu2ndLayer = SettingDialogFactory.createSecondLayerDialogDetails(this.mContext, this.mContainer.getMeasuredWidth(), this.mContainer.getMeasuredHeight(), this.mContextualMenu.isExpanded());
        } else {
            this.mContextualMenu2ndLayer = SettingDialogFactory.createSecondLayerDialog(this.mContext, this.mContainer.getMeasuredWidth(), this.mContainer.getMeasuredHeight(), this.mContextualMenu.isExpanded());
        }
        this.mContextualMenu2ndLayer.setSettingDialogStack(this);
        this.mContextualMenu2ndLayer.addPanel(settingAdapter);
        int iWidth = rect.width() - rect.height();
        if (this.mOrientation == 2) {
            if (this.mContextualMenu.isExpanded()) {
                this.mContextualMenu2ndLayer.setTopMarginHint(rect2.bottom);
            } else {
                this.mContextualMenu2ndLayer.setTopMarginHint(rect2.bottom + iWidth);
            }
        } else {
            this.mContextualMenu2ndLayer.setTopMarginHint(rect2.bottom + this.mContainer.getPaddingRight());
        }
        this.mContextualMenu2ndLayer.setSensorOrientation(this.mOrientation);
        this.mContextualMenu2ndLayer.open(this.mContainer);
        this.mDialogTags.put(this.mContextualMenu2ndLayer, obj);
        this.mContextualMenu2ndLayerAdapter = settingAdapter;
        if (CamLog.VERBOSE) {
            CamLog.d("mSecondLayerDialog put: " + obj);
        }
        resetEnabledOfDialogs();
        this.mContainer.requestFocus();
        notifyOpenSettingDialog(obj);
        if (CamLog.VERBOSE) {
            CamLog.d("openPopupDialog:NOTIFY OPEN CALLBACK");
        }
        return true;
    }

    public void reopenSecondLayerDialog() {
        if (this.mContextualMenu2ndLayerAdapter == null || this.mDialogTags.get(this.mContextualMenu2ndLayer) == null) {
            return;
        }
        openSecondLayerDialog(this.mContextualMenu2ndLayerAdapter, this.mDialogTags.get(this.mContextualMenu2ndLayer));
    }

    public boolean closeCurrentDialog() {
        if (CamLog.VERBOSE) {
            CamLog.d("closeCurrentDialogs.");
        }
        Object obj = this.mContextualMenu2ndLayer != null ? this.mDialogTags.get(this.mContextualMenu2ndLayer) : null;
        boolean zCloseSecondLayerDialog = closeSecondLayerDialog(true);
        if (!zCloseSecondLayerDialog) {
            if (this.mMonochromeDialog != null) {
                obj = this.mDialogTags.get(this.mMonochromeDialog);
            }
            zCloseSecondLayerDialog = closemMonochromeDialog(true);
        }
        if (!zCloseSecondLayerDialog) {
            if (this.mShortcutDialog != null) {
                obj = this.mDialogTags.get(this.mShortcutDialog);
            }
            zCloseSecondLayerDialog = closeShortcutDialog(true);
        }
        if (!zCloseSecondLayerDialog) {
            if (this.mContextualMenu != null) {
                obj = UserSettingKey.SETTING_MENU;
            }
            zCloseSecondLayerDialog = closeMenuDialog(true);
        }
        if (!zCloseSecondLayerDialog) {
            if (this.mModeSelectorDialog != null) {
                obj = this.mDialogTags.get(this.mModeSelectorDialog);
            }
            zCloseSecondLayerDialog = closeModeSelectDialog(true);
        }
        resetEnabledOfDialogs();
        if (zCloseSecondLayerDialog) {
            if (!isDialogOpened()) {
                this.mContainer.clearFocus();
                notifyCloseSettingDialog(obj);
                if (CamLog.VERBOSE) {
                    CamLog.d("closeCurrentDialogs:NOTIFY CLOSE CALLBACK");
                }
            } else {
                notifyCloseSettingDialog(obj);
                if (CamLog.VERBOSE) {
                    CamLog.d("closeCurrentDialogs:NOTIFY CLOSE CALLBACK NOT ALL DIALOGS");
                }
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.d("closeCurrentDialogs: consumed: " + zCloseSecondLayerDialog);
        }
        return zCloseSecondLayerDialog;
    }

    public void closeAllSettingDialogs() {
        closeAllSettingDialogs(false);
    }

    public void closeAllSettingDialogs(boolean z) {
        Object obj = this.mContextualMenu2ndLayer != null ? this.mDialogTags.get(this.mContextualMenu2ndLayer) : null;
        boolean zCloseSecondLayerDialog = closeSecondLayerDialog(z) | false;
        if (this.mMonochromeDialog != null) {
            obj = this.mDialogTags.get(this.mMonochromeDialog);
        }
        boolean zClosemMonochromeDialog = zCloseSecondLayerDialog | closemMonochromeDialog(z);
        if (this.mShortcutDialog != null) {
            obj = this.mDialogTags.get(this.mShortcutDialog);
        }
        boolean zCloseShortcutDialog = zClosemMonochromeDialog | closeShortcutDialog(z);
        if (this.mContextualMenu != null) {
            obj = UserSettingKey.SETTING_MENU;
        }
        boolean zCloseMenuDialog = zCloseShortcutDialog | closeMenuDialog(z);
        if (this.mModeSelectorDialog != null) {
            obj = this.mDialogTags.get(this.mModeSelectorDialog);
        }
        boolean zCloseModeSelectDialog = closeModeSelectDialog(z) | zCloseMenuDialog;
        resetEnabledOfDialogs();
        if (CamLog.VERBOSE) {
            CamLog.d("closeAllSettingDialogs");
        }
        if (!zCloseModeSelectDialog || isDialogOpened()) {
            return;
        }
        this.mContainer.clearFocus();
        notifyCloseSettingDialog(obj);
        if (CamLog.VERBOSE) {
            CamLog.d("closeAllSettingDialogs:NOTIFY CLOSE CALLBACK");
        }
    }

    public void setUiOrientation(int i) {
        this.mOrientation = i;
        for (SettingDialogInterface settingDialogInterface : getDialogList()) {
            if (settingDialogInterface != null && settingDialogInterface != this.mMonochromeDialog) {
                settingDialogInterface.setSensorOrientation(this.mOrientation);
            }
        }
        if (this.mMonochromeDialog != null) {
            if (this.mModeSelectorDialog != null) {
                Rect monochromeGlobalVisibleItemRect = this.mModeSelectorDialog.getMonochromeGlobalVisibleItemRect(false);
                this.mMonochromeDialog.setTopMarginHint(monochromeGlobalVisibleItemRect.right);
                this.mMonochromeDialog.setBottomMarginHint(monochromeGlobalVisibleItemRect.bottom);
            }
            this.mMonochromeDialog.setSensorOrientation(this.mOrientation);
        }
    }

    public void setCapturingMode(CapturingMode capturingMode) {
        this.mCapturingMode = capturingMode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SettingDialogInterface getCurrentDialog() {
        if (CamLog.VERBOSE) {
            CamLog.d("getCurrentDialog");
        }
        for (SettingDialogInterface settingDialogInterface : getDialogList()) {
            if (settingDialogInterface != null) {
                return settingDialogInterface;
            }
        }
        return null;
    }

    private void resetEnabledOfDialogs() {
        if (CamLog.VERBOSE) {
            CamLog.d("resetEnabledOfDialogs");
        }
        SettingDialogInterface currentDialog = getCurrentDialog();
        SettingDialogInterface[] dialogList = getDialogList();
        int length = dialogList.length;
        for (int i = 0; i < length; i++) {
            SettingDialogInterface settingDialogInterface = dialogList[i];
            if (settingDialogInterface != null) {
                settingDialogInterface.setEnabled(settingDialogInterface == currentDialog);
            }
        }
    }

    private boolean closeMenuDialog(boolean z) {
        if (this.mContextualMenu == null) {
            return false;
        }
        this.mDialogTags.remove(this.mContextualMenu);
        if (CamLog.VERBOSE) {
            CamLog.d("mMenuDialog remove:");
        }
        if (z) {
            this.mContextualMenu.close();
        } else {
            this.mContextualMenu.closeImmediate();
        }
        this.mContextualMenu = null;
        notifyCloseContextualMenu(UserSettingKey.SETTING_MENU);
        return true;
    }

    private boolean closeShortcutDialog(boolean z) {
        if (this.mShortcutDialog == null) {
            return false;
        }
        this.mDialogTags.remove(this.mShortcutDialog);
        if (CamLog.VERBOSE) {
            CamLog.d("mShortcutDialog remove:");
        }
        if (z) {
            this.mShortcutDialog.close();
        } else {
            this.mShortcutDialog.closeImmediate();
        }
        this.mShortcutDialog = null;
        return true;
    }

    private boolean closeModeSelectDialog(boolean z) {
        if (this.mModeSelectorDialog == null) {
            return false;
        }
        this.mDialogTags.remove(this.mModeSelectorDialog);
        if (CamLog.VERBOSE) {
            CamLog.d("mControlDialog remove:");
        }
        if (z) {
            this.mModeSelectorDialog.close();
        } else {
            this.mModeSelectorDialog.closeImmediate();
        }
        this.mModeSelectorDialog = null;
        return true;
    }

    private boolean closemMonochromeDialog(boolean z) {
        if (this.mMonochromeDialog == null) {
            return false;
        }
        this.mDialogTags.remove(this.mMonochromeDialog);
        if (CamLog.DEBUG) {
            CamLog.d(TAG, "mMonochromeDialog remove:");
        }
        if (z) {
            this.mMonochromeDialog.close();
        } else {
            this.mMonochromeDialog.closeImmediate();
        }
        this.mMonochromeDialog = null;
        resetEnabledOfDialogs();
        return true;
    }

    public boolean closeSecondLayerDialog(boolean z) {
        if (this.mContextualMenu2ndLayer == null) {
            return false;
        }
        this.mDialogTags.remove(this.mContextualMenu2ndLayer);
        if (CamLog.VERBOSE) {
            CamLog.d("mSecondLayerDialog remove:");
        }
        if (z) {
            this.mContextualMenu2ndLayer.close();
        } else {
            this.mContextualMenu2ndLayer.closeImmediate();
        }
        this.mContextualMenu2ndLayer = null;
        resetEnabledOfDialogs();
        return true;
    }

    private SettingDialogInterface[] getDialogList() {
        return new SettingDialogInterface[]{this.mShortcutDialog, this.mContextualMenu2ndLayer, this.mContextualMenu, this.mMonochromeDialog, this.mModeSelectorDialog};
    }

    public void setCanceledOnTouchOutside(boolean z) {
        this.mIsCanceledOnTouchOutside = z;
    }

    private class Background extends FrameLayout {
        private final Rect mBackgroundRect;
        private final Rect mItemRect;

        public Background(Context context) {
            super(context);
            this.mBackgroundRect = new Rect();
            this.mItemRect = new Rect();
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            int i3;
            super.onMeasure(i, i2);
            if (CamLog.VERBOSE) {
                CamLog.d("onMeasure() E");
            }
            if (SettingDialogStack.this.mContextualMenu2ndLayer != null) {
                Object obj = SettingDialogStack.this.mDialogTags.get(SettingDialogStack.this.mContextualMenu2ndLayer);
                Rect rect = new Rect();
                if (SettingDialogStack.this.mContainer.getGlobalVisibleRect(rect)) {
                    if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
                        this.mBackgroundRect.left = rect.top;
                        this.mBackgroundRect.top = rect.left;
                        this.mBackgroundRect.right = rect.bottom;
                        this.mBackgroundRect.bottom = rect.right;
                    } else {
                        this.mBackgroundRect.left = rect.left;
                        this.mBackgroundRect.top = rect.top;
                        this.mBackgroundRect.right = rect.right;
                        this.mBackgroundRect.bottom = rect.bottom;
                    }
                    if (CamLog.VERBOSE) {
                        CamLog.d("  backgroundRect: (" + this.mBackgroundRect.left + ", " + this.mBackgroundRect.top + ", " + this.mBackgroundRect.right + ", " + this.mBackgroundRect.bottom + ")");
                    }
                    Rect rect2 = new Rect();
                    if (SettingDialogStack.this.mContextualMenu.getGlobalVisibleItemRect(rect2, obj)) {
                        if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
                            this.mItemRect.left = rect2.top;
                            this.mItemRect.top = rect2.left;
                            this.mItemRect.right = rect2.bottom;
                            this.mItemRect.bottom = rect2.right;
                        } else {
                            this.mItemRect.left = rect2.left;
                            this.mItemRect.top = rect2.top;
                            this.mItemRect.right = rect2.right;
                            this.mItemRect.bottom = rect2.bottom;
                        }
                        if (CamLog.VERBOSE) {
                            CamLog.d("  itemRect: (" + this.mItemRect.left + ", " + this.mItemRect.top + ", " + this.mItemRect.right + ", " + this.mItemRect.bottom + ")");
                        }
                        if (SettingDialogStack.this.mOrientation == 2) {
                            i3 = this.mBackgroundRect.bottom - this.mItemRect.bottom;
                        } else {
                            i3 = this.mBackgroundRect.right - this.mItemRect.right;
                        }
                        SettingDialogStack.this.mContextualMenu2ndLayer.setBottomMarginHint(i3);
                        if (CamLog.VERBOSE) {
                            CamLog.d("  margin-button:" + i3);
                        }
                    }
                }
            }
            if (CamLog.VERBOSE) {
                CamLog.d("onMeasure() X");
            }
        }

        @Override // android.view.View
        public boolean onTouchEvent(MotionEvent motionEvent) {
            if (CamLog.VERBOSE) {
                CamLog.d("onTouchEvent: " + motionEvent.getAction());
            }
            SettingDialogInterface currentDialog = SettingDialogStack.this.getCurrentDialog();
            if (currentDialog != null) {
                switch (motionEvent.getAction()) {
                    case 0:
                        if (SettingDialogStack.this.mExclusiveViewListener == null || !SettingDialogStack.this.mExclusiveViewListener.isExclusiveView(SettingDialogStack.this.mBackground, motionEvent)) {
                        }
                        break;
                    case 1:
                        if (SettingDialogStack.this.mIsCanceledOnTouchOutside) {
                            if (SettingDialogStack.this.mContextualMenu == null || !SettingDialogStack.this.mContextualMenu.isOperationAcceptable()) {
                                Rect rect = new Rect();
                                SettingDialogStack.this.mBackground.getGlobalVisibleRect(rect);
                                if (!currentDialog.hitTest(rect.right - ((int) motionEvent.getY()), rect.top + ((int) motionEvent.getX()))) {
                                    SettingDialogStack.this.closeCurrentDialog();
                                    break;
                                }
                            }
                        }
                        break;
                }
                return true;
            }
            return false;
        }
    }

    public void setExclusiveViewListener(ExclusiveViewListener exclusiveViewListener) {
        this.mExclusiveViewListener = exclusiveViewListener;
    }

    public void adjustContainer(boolean z) {
        if (!z) {
            int navigationBarMargin = !LayoutDependencyResolver.isTablet(this.mContext) ? LayoutDependencyResolver.getNavigationBarMargin(this.mContext) : 0;
            this.mContainer.getLayoutParams().width = this.mScreenRect.width();
            this.mContainer.getLayoutParams().height = this.mScreenRect.height();
            this.mContainer.setPadding(0, 0, navigationBarMargin, 0);
        } else {
            this.mContainer.getLayoutParams().width = this.mScreenRect.width();
            this.mContainer.getLayoutParams().height = this.mScreenRect.height();
            this.mContainer.setPadding(0, 0, 0, 0);
        }
        ((FrameLayout.LayoutParams) this.mContainer.getLayoutParams()).gravity = 51;
        ((FrameLayout.LayoutParams) this.mContainer.getLayoutParams()).topMargin = this.mScreenRect.top;
        ((FrameLayout.LayoutParams) this.mContainer.getLayoutParams()).leftMargin = this.mScreenRect.left;
        this.mContainer.requestLayout();
    }
}
