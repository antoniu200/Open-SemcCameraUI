package com.sonyericsson.android.camera.view.setting;

import android.content.Context;
import android.content.res.Resources;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.LaunchCondition;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.UserSettingSelectability;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.TouchCapture;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.VideoCodec;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.controller.StateMachine;
import com.sonyericsson.android.camera.controller.launcher.ApplicationLauncher;
import com.sonyericsson.android.camera.device.CameraDeviceHandler;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.parameter.Parameters;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.HelpGuide;
import com.sonyericsson.android.camera.util.SettingUtil;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.view.ViewFinder;
import com.sonyericsson.android.camera.view.ViewFinderImpl;
import com.sonyericsson.android.camera.view.messagedialog.DialogId;
import com.sonyericsson.android.camera.view.modeselector.Mode;
import com.sonyericsson.android.camera.view.modeselector.ModeLoader;
import com.sonyericsson.android.camera.view.modeselector.ModeSelectorInternalMode;
import com.sonyericsson.android.camera.view.selectabledialog.ModeSelector;
import com.sonyericsson.android.camera.view.selectabledialog.SettingMenu;
import com.sonyericsson.android.camera.view.setting.ContextualSettingList;
import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;
import com.sonyericsson.android.camera.view.setting.dialog.SettingDialogFactory;
import com.sonyericsson.android.camera.view.setting.dialog.SettingDialogListener;
import com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItemFactory;
import com.sonyericsson.android.camera.view.setting.executor.SettingChangeExecutor;
import com.sonyericsson.android.camera.view.setting.executor.SettingChangerInterface;
import com.sonyericsson.android.camera.view.setting.executor.SettingExecutorInterface;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItemBuilder;
import com.sonyericsson.android.camera.view.setting.settingitem.TypedSettingItem;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import com.sonyericsson.cameracommon.utility.RegionConfig;
import com.sonymobile.cameracommon.research.ResearchUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SettingUi {
    public static final String TAG = "SettingUi";
    private final CameraActivity mActivity;
    private final CameraDeviceHandler mCameraDevice;
    private final SettingDialogItemFactory mDialogItemFactory;
    private boolean mIsDeviceInSecurityLock;
    private final UserSettings mSetting;
    private final SettingDialogStack mSettingDialogStack;
    private final Map<SettingAdapter, UserSettingKey[]> mSettingMenuAdapter = new HashMap();
    private final ContextualSettingList mSettingMenuList;
    private final StateMachine mStateMachine;
    private final ViewFinderImpl mViewFinder;

    public SettingUi(CameraActivity cameraActivity, SettingDialogStack settingDialogStack, StateMachine stateMachine, ViewFinderImpl viewFinderImpl, CameraDeviceHandler cameraDeviceHandler, boolean z) {
        this.mActivity = cameraActivity;
        this.mSettingMenuList = new ContextualSettingList(this.mActivity.getResources().getBoolean(R.bool.enable_4k_videosize));
        this.mSettingDialogStack = settingDialogStack;
        this.mSettingDialogStack.setContextualMenuListener(new SettingDialogListenerImpl());
        this.mStateMachine = stateMachine;
        this.mViewFinder = viewFinderImpl;
        this.mCameraDevice = cameraDeviceHandler;
        this.mIsDeviceInSecurityLock = z;
        this.mDialogItemFactory = new SettingDialogItemFactory();
        this.mSetting = this.mStateMachine.getUserSetting();
    }

    public void setDeviceInSecurityLock(boolean z) {
        this.mIsDeviceInSecurityLock = z;
    }

    private class SettingDialogListenerImpl implements SettingDialogListener {
        @Override // com.sonyericsson.android.camera.view.setting.dialog.SettingDialogListener
        public void onOpenSettingDialog(Object obj) {
        }

        private SettingDialogListenerImpl() {
        }

        @Override // com.sonyericsson.android.camera.view.setting.dialog.SettingDialogListener
        public void onCloseSettingDialog(Object obj) {
            if (obj == UserSettingKey.SETTING_MENU) {
                SettingUi.this.clearSettingMenuAdapter();
            }
        }
    }

    private class ParameterChanger implements SettingChangerInterface<UserSettingValue> {
        private ParameterChanger() {
        }

        @Override // com.sonyericsson.android.camera.view.setting.executor.SettingChangerInterface
        public void changeValue(TypedSettingItem<UserSettingValue> typedSettingItem) throws ExecutionException, InterruptedException, Resources.NotFoundException {
            UserSettingValue userSettingValue = SettingUi.this.mSetting.get(typedSettingItem.getData().getKey());
            UserSettingValue data = typedSettingItem.getData();
            if (userSettingValue == data) {
                return;
            }
            UserSettingKey key = data.getKey();
            if (CamLog.VERBOSE) {
                CamLog.d("ParameterChanger#changeValue() Key : " + key + " value : " + data);
            }
            if (!UserSettingKey.DESTINATION_TO_SAVE.equals(key) || userSettingValue != DestinationToSave.EMMC || data != DestinationToSave.SDCARD || Storage.StorageState.UNGRANTED != SettingUi.this.mActivity.getStorage().getCurrentState(Storage.StorageType.EXTERNAL_CARD)) {
                SettingUi.this.mSetting.set(data);
                CapturingMode capturingMode = (CapturingMode) SettingUi.this.mSetting.get(UserSettingKey.CAPTURING_MODE);
                switch (key) {
                    case VIDEO_HDR:
                        if (userSettingValue == VideoHdr.HDR_ON && data == VideoHdr.HDR_OFF) {
                            SettingUi.this.setVideoStabilizerByVideoHdrChanged();
                            break;
                        }
                        break;
                    case VIDEO_SIZE:
                        VideoSize videoSize = (VideoSize) userSettingValue;
                        if (((VideoSize) data).is4KVideo() && !videoSize.is4KVideo()) {
                            SettingUi.this.mViewFinder.showMessageDialog(DialogId.THERMAL_NOTE, new Object[0]);
                            break;
                        }
                        break;
                    case SIDE_SENSE:
                        if (!SettingUtil.isSideSenseEnabled(true)) {
                            SettingUi.this.mSettingDialogStack.closeCurrentDialog();
                            SettingUi.this.mViewFinder.showMessageDialog(DialogId.SIDE_SENSE_DISABLE_ON_CONTEXTUAL_SETTINGS, new Object[0]);
                            break;
                        }
                        break;
                    case GRID_LINE:
                        SettingUi.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_GRID_LINE, new Object[0]);
                        break;
                    case SHUTTER_SOUND:
                        if (data != ShutterSound.OFF) {
                            SettingUi.this.mCameraDevice.playShutterSound(capturingMode.getType());
                            break;
                        }
                        break;
                    case GEO_TAG:
                        if (data == Geotag.ON && RegionConfig.isChinaRegion(SettingUi.this.mActivity)) {
                            SettingUi.this.mSettingDialogStack.closeAllSettingDialogs();
                        }
                        if (!SettingUi.this.mActivity.getGeoTagManager().setGeotag((Geotag) data, SettingUi.this.mActivity, SettingUi.this.mViewFinder)) {
                            SettingUi.this.mSettingDialogStack.closeCurrentDialog();
                            SettingUi.this.mViewFinder.showMessageDialog(DialogId.LOCATION_SERVICE_DISABLE_ON_CONTEXTUAL_SETTINGS, new Object[0]);
                            break;
                        }
                        break;
                    case DESTINATION_TO_SAVE:
                        if (userSettingValue == DestinationToSave.EMMC) {
                            SettingUi.this.mViewFinder.showHiSpeedSdCardRecommendDialogOnDestinationChange();
                        }
                        Storage.StorageType type = ((DestinationToSave) data).getType();
                        SettingUi.this.mViewFinder.notifyStorageStateChanged(type, SettingUi.this.mActivity.getStorage().getCurrentState(type), false, false);
                        break;
                    case TOUCH_CAPTURE:
                        SettingUi.this.updateTouchCapture((TouchCapture) data);
                        break;
                    case VIDEO_CODEC:
                        VideoSize videoSize2 = (VideoSize) SettingUi.this.mSetting.get(UserSettingKey.VIDEO_SIZE);
                        if (data == VideoCodec.H264) {
                            if (videoSize2 == VideoSize.FOUR_K_UHD_H265) {
                                SettingUi.this.mSetting.set(VideoSize.FOUR_K_UHD_H264);
                                break;
                            }
                        } else if (data == VideoCodec.H265 && videoSize2 == VideoSize.FOUR_K_UHD_H264) {
                            SettingUi.this.mSetting.set(VideoSize.FOUR_K_UHD_H265);
                            break;
                        }
                        break;
                }
                SettingUi.this.updateSettingMenu(false);
                ResearchUtil.getInstance().sendSettingsCommon(typedSettingItem.getData());
                LocalResearchUtil.getInstance().setSettingsValue(userSettingValue, typedSettingItem.getData(), capturingMode);
                return;
            }
            SettingUi.this.mViewFinder.showMessageDialog(DialogId.REQUEST_SD_CARD_PERMISSION, new Object[0]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTouchCapture(TouchCapture touchCapture) {
        this.mViewFinder.updateTouchCapture(touchCapture);
        this.mSetting.set(touchCapture);
        this.mViewFinder.commit();
    }

    public void openSettingMenuDialog() {
        openSettingMenuDialog(false, false);
    }

    public void openUserSelectMenu(UserSettingKey userSettingKey) {
        SettingMenu settingMenuOpenSettingMenuDialog = openSettingMenuDialog(true, true);
        if (userSettingKey != null) {
            settingMenuOpenSettingMenuDialog.select(userSettingKey);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearSettingMenuAdapter() {
        if (this.mSettingMenuAdapter.size() > 0) {
            this.mSettingMenuAdapter.clear();
        }
    }

    private SettingMenu openSettingMenuDialog(boolean z, boolean z2) {
        CapturingMode currentCapturingMode = this.mStateMachine.getCurrentCapturingMode();
        ContextualSettingList.Group group = this.mSettingMenuList.get(currentCapturingMode, this.mActivity.isOneShot());
        CameraActivity cameraActivity = this.mActivity;
        updateSaveDestinationSelectability(this.mActivity.hasExtraOutputPath());
        SettingMenu settingMenuCreateSettingMenuDialog = SettingDialogFactory.createSettingMenuDialog(cameraActivity, this.mSettingDialogStack.getBackgroundWidth(), this.mSettingDialogStack.getBackgroundHeight(), z2);
        SettingAdapter settingAdapterGenerateSettingMenuItemAdapter = generateSettingMenuItemAdapter(cameraActivity, group.priorityHigh.keys);
        settingMenuCreateSettingMenuDialog.addPanel(getString(group.priorityHigh.titleResource), settingAdapterGenerateSettingMenuItemAdapter);
        this.mSettingMenuAdapter.put(settingAdapterGenerateSettingMenuItemAdapter, group.priorityHigh.keys);
        if (group.common.keys.length > 0) {
            SettingAdapter settingAdapterGenerateSettingMenuItemAdapter2 = generateSettingMenuItemAdapter(cameraActivity, group.common.keys);
            settingMenuCreateSettingMenuDialog.addPanel(getString(group.common.titleResource), settingAdapterGenerateSettingMenuItemAdapter2);
            this.mSettingMenuAdapter.put(settingAdapterGenerateSettingMenuItemAdapter2, group.common.keys);
        }
        if (!this.mSettingDialogStack.openMenuDialog(settingMenuCreateSettingMenuDialog, currentCapturingMode, z)) {
            this.mSettingDialogStack.closeAllSettingDialogs(true);
        }
        return settingMenuCreateSettingMenuDialog;
    }

    public void openModeSelectDialog(ModeLoader modeLoader, ModeSelector.OnModeSelectListener onModeSelectListener) {
        if (this.mSettingDialogStack.openModeSelectorDialog(modeLoader, onModeSelectListener)) {
            return;
        }
        this.mSettingDialogStack.closeCurrentDialog();
    }

    public void openMonochromeDialog(boolean z, int i, Mode mode) {
        this.mSettingDialogStack.openMonochromeDialog(generateMonochromeAdpter(z, mode), i);
    }

    private SettingAdapter generateMonochromeAdpter(boolean z, Mode mode) {
        SettingAdapter settingAdapter = new SettingAdapter(this.mActivity, this.mDialogItemFactory, this.mIsDeviceInSecurityLock);
        ArrayList arrayList = new ArrayList();
        arrayList.add(SettingItemBuilder.build(ApplicationLauncher.MonochromeType.MONOCHROME_PHOTO).iconId(R.drawable.cam_dual_effect_monochrome_photo_icn).textId(R.string.cam_strings_monochrome_photo_txt).dialogItemType(2).executor(new MonochromeExecutor(z, mode)).selected(false).selectability(SettingItem.Selectability.SELECTABLE).commit());
        arrayList.add(SettingItemBuilder.build(ApplicationLauncher.MonochromeType.MONOCHROME_VIDEO).iconId(R.drawable.cam_dual_effect_monochrome_video_icn).textId(R.string.cam_strings_monochrome_video_txt).dialogItemType(2).executor(new MonochromeExecutor(z, mode)).selected(false).selectability(SettingItem.Selectability.SELECTABLE).commit());
        settingAdapter.addAll(arrayList);
        return settingAdapter;
    }

    private class MonochromeExecutor<T> implements SettingExecutorInterface<T> {
        boolean mIsMostRecentlyUsed;
        Mode mMode;

        private MonochromeExecutor(boolean z, Mode mode) {
            this.mIsMostRecentlyUsed = false;
            this.mIsMostRecentlyUsed = z;
            this.mMode = mode;
        }

        @Override // com.sonyericsson.android.camera.view.setting.executor.SettingExecutorInterface
        public void onExecute(TypedSettingItem<T> typedSettingItem) throws Resources.NotFoundException {
            SettingUi.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_MRU_SHORTCUT, this.mMode);
            if (this.mIsMostRecentlyUsed) {
                LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.MRU_SHORTCUT);
                LocalResearchUtil.getInstance().setLaunchBy(LaunchCondition.LaunchTrigger.MRU_SHORTCUT);
            } else {
                LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.MODE_SELECTOR);
                LocalResearchUtil.getInstance().setLaunchBy(LaunchCondition.LaunchTrigger.MODE_SELECTOR);
            }
            CapturingMode capturingMode = (CapturingMode) SettingUi.this.mSetting.get(UserSettingKey.CAPTURING_MODE);
            switch ((ApplicationLauncher.MonochromeType) typedSettingItem.getData()) {
                case MONOCHROME_PHOTO:
                    if (SettingUi.this.mActivity.isDeviceInSecurityLock()) {
                        SettingUi.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_UNLOCK_SCREEN_DIALOG, CapturingMode.SCENE_RECOGNITION, this.mMode);
                        break;
                    } else {
                        LocalResearchUtil.getInstance().sendEventInternalModeChange(capturingMode, ModeSelectorInternalMode.DUAL_MONOCHROME);
                        ApplicationLauncher.launchExternalCamera(SettingUi.this.mActivity, 17, SettingUi.this.mStateMachine.getUserSetting(), CapturingMode.SCENE_RECOGNITION, true);
                        break;
                    }
                case MONOCHROME_VIDEO:
                    if (SettingUi.this.mActivity.isDeviceInSecurityLock()) {
                        SettingUi.this.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_UNLOCK_SCREEN_DIALOG, CapturingMode.VIDEO, this.mMode);
                        break;
                    } else {
                        LocalResearchUtil.getInstance().sendEventInternalModeChange(capturingMode, ModeSelectorInternalMode.DUAL_MONOCHROME);
                        ApplicationLauncher.launchExternalCamera(SettingUi.this.mActivity, 17, SettingUi.this.mStateMachine.getUserSetting(), CapturingMode.VIDEO, true);
                        break;
                    }
            }
            SettingUi.this.mSettingDialogStack.closeAllSettingDialogs();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void openSecondLayerDialog(SettingAdapter settingAdapter, Object obj) {
        this.mSettingDialogStack.openSecondLayerDialog(settingAdapter, obj);
    }

    public void openShortcutSettingDialog(UserSettingKey userSettingKey) {
        SettingAdapter settingAdapter = new SettingAdapter(this.mActivity, this.mDialogItemFactory, this.mIsDeviceInSecurityLock);
        Iterator<SettingItem> it = generateChildrenSettingItem(userSettingKey, this.mActivity.getStorage()).iterator();
        while (it.hasNext()) {
            settingAdapter.add(it.next());
        }
        SettingMenu settingMenuCreateShortcutDialog = SettingDialogFactory.createShortcutDialog(this.mActivity, userSettingKey, this.mSettingDialogStack.getBackgroundWidth(), this.mSettingDialogStack.getBackgroundHeight());
        if (settingMenuCreateShortcutDialog != null) {
            settingMenuCreateShortcutDialog.addPanel(this.mActivity.getResources().getString(userSettingKey.getTitleTextId()), settingAdapter);
            if (this.mSettingDialogStack.openShortcutDialog(settingMenuCreateShortcutDialog, userSettingKey)) {
                return;
            }
            this.mSettingDialogStack.closeCurrentDialog();
        }
    }

    public void setSensorOrientation(int i) {
        this.mSettingDialogStack.setUiOrientation(i);
    }

    public void updateSettingMenu(boolean z) {
        if (CamLog.VERBOSE) {
            CamLog.d("updateSettingMenu() is called");
        }
        for (SettingAdapter settingAdapter : this.mSettingMenuAdapter.keySet()) {
            settingAdapter.clear();
            for (UserSettingKey userSettingKey : this.mSettingMenuAdapter.get(settingAdapter)) {
                if (isVisible(this.mActivity, userSettingKey)) {
                    settingAdapter.add(generateParameterKeyItem(userSettingKey, this.mActivity.getStorage()));
                }
                if (z) {
                    settingAdapter.notifyDataSetInvalidated();
                } else {
                    settingAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public boolean closeDialogs() {
        if (!this.mSettingDialogStack.isDialogOpened()) {
            return false;
        }
        this.mSettingDialogStack.closeAllSettingDialogs(true);
        return true;
    }

    private String getString(int i) {
        return this.mActivity.getResources().getString(i);
    }

    private SettingAdapter generateSettingMenuItemAdapter(Context context, UserSettingKey[] userSettingKeyArr) {
        SettingAdapter settingAdapter = new SettingAdapter(this.mActivity, this.mDialogItemFactory, this.mIsDeviceInSecurityLock);
        for (UserSettingKey userSettingKey : userSettingKeyArr) {
            if (isVisible(context, userSettingKey)) {
                settingAdapter.add(generateParameterKeyItem(userSettingKey, this.mActivity.getStorage()));
            }
        }
        return settingAdapter;
    }

    private SettingItem generateParameterKeyItem(UserSettingKey userSettingKey, Storage storage) {
        List<SettingItem> listGenerateChildrenSettingItem = generateChildrenSettingItem(userSettingKey, storage);
        SettingItem selectedSettingItem = getSelectedSettingItem(listGenerateChildrenSettingItem);
        String text = selectedSettingItem != null ? selectedSettingItem.getText(this.mActivity.getResources()) : "";
        boolean zIsSelectableKey = isSelectableKey(userSettingKey);
        DialogId restrictMessageDialogId = userSettingKey.getRestrictMessageDialogId(this.mSetting);
        SettingItem.Selectability selectability = SettingItem.Selectability.SELECTABLE;
        if (!zIsSelectableKey) {
            if (restrictMessageDialogId == DialogId.DLG_INVALID) {
                selectability = SettingItem.Selectability.UNSELECTABLE;
            } else {
                selectability = SettingItem.Selectability.RESTRICTED;
            }
        }
        SettingItemBuilder settingItemBuilderExecutor = SettingItemBuilder.build(userSettingKey).textId(userSettingKey.getTitleTextId()).additionalTextForAccessibility(text).dialogItemType(getDialogItemType(userSettingKey)).selectability(selectability).executor(generateSettingItemExecutor(userSettingKey, selectability, storage));
        if (!userSettingKey.equals(UserSettingKey.HELP_GUIDE) && !userSettingKey.equals(UserSettingKey.RESET_SETTINGS)) {
            Iterator<SettingItem> it = listGenerateChildrenSettingItem.iterator();
            while (it.hasNext()) {
                settingItemBuilderExecutor.item(it.next());
            }
        }
        return settingItemBuilderExecutor.commit();
    }

    private SettingExecutorInterface<UserSettingKey> generateSettingItemExecutor(final UserSettingKey userSettingKey, SettingItem.Selectability selectability, final Storage storage) {
        final CameraActivity cameraActivity = this.mActivity;
        switch (selectability) {
            case SELECTABLE:
                return new SettingExecutorInterface<UserSettingKey>() { // from class: com.sonyericsson.android.camera.view.setting.SettingUi.1
                    @Override // com.sonyericsson.android.camera.view.setting.executor.SettingExecutorInterface
                    public void onExecute(TypedSettingItem<UserSettingKey> typedSettingItem) {
                        if (userSettingKey == UserSettingKey.HELP_GUIDE) {
                            if (SettingUi.this.mIsDeviceInSecurityLock) {
                                SettingUi.this.mViewFinder.showMessageDialog(DialogId.UNLOCK_REQUEST_FOR_OPENING_OPTION_MENU, userSettingKey.name());
                                return;
                            }
                            if (HelpGuide.isHelpAppAvailable(cameraActivity)) {
                                HelpGuide.startHelpApp(cameraActivity);
                            } else {
                                HelpGuide.startOnlineHelp(cameraActivity);
                            }
                            ResearchUtil.getInstance().sendSettingsCommon(typedSettingItem.getData().toString());
                            return;
                        }
                        if (userSettingKey == UserSettingKey.RESET_SETTINGS) {
                            if (!SettingUi.this.mIsDeviceInSecurityLock) {
                                SettingUi.this.mSettingDialogStack.closeCurrentDialog();
                                SettingUi.this.mViewFinder.showMessageDialog(DialogId.RESET_CONFIRMATION, new Object[0]);
                                return;
                            } else {
                                SettingUi.this.mViewFinder.showMessageDialog(DialogId.UNLOCK_REQUEST_FOR_OPENING_OPTION_MENU, userSettingKey.name());
                                return;
                            }
                        }
                        if (!SettingUi.this.mIsDeviceInSecurityLock || (userSettingKey != UserSettingKey.DESTINATION_TO_SAVE && userSettingKey != UserSettingKey.GEO_TAG && (SettingUtil.isSideSenseEnabled(true) || userSettingKey != UserSettingKey.SIDE_SENSE))) {
                            SettingUi.this.openSecondLayerDialog(SettingUi.this.generateParameterItemAdapter(typedSettingItem.getData(), storage), userSettingKey);
                        } else {
                            SettingUi.this.mViewFinder.showMessageDialog(DialogId.UNLOCK_REQUEST_FOR_OPENING_OPTION_MENU, userSettingKey.name());
                        }
                    }
                };
            case RESTRICTED:
                return new SettingExecutorInterface<UserSettingKey>() { // from class: com.sonyericsson.android.camera.view.setting.SettingUi.2
                    @Override // com.sonyericsson.android.camera.view.setting.executor.SettingExecutorInterface
                    public void onExecute(TypedSettingItem<UserSettingKey> typedSettingItem) {
                        SettingUi.this.mViewFinder.showMessageDialog(userSettingKey.getRestrictMessageDialogId(SettingUi.this.mSetting), new Object[0]);
                    }
                };
            default:
                return null;
        }
    }

    private boolean isSelectableKey(UserSettingKey userSettingKey) {
        boolean zIsSelectable = userSettingKey.isSelectable();
        int i = AnonymousClass3.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()];
        if (i == 3) {
            CapturingMode capturingMode = (CapturingMode) this.mSetting.get(UserSettingKey.CAPTURING_MODE);
            if (capturingMode == CapturingMode.SLOW_MOTION && (((SlowMotion) this.mSetting.get(UserSettingKey.SLOW_MOTION)) == SlowMotion.STANDARD_SLOW_MOTION || !PlatformCapability.isSuperSlowFullHdSupported(capturingMode.getCameraId()))) {
                return false;
            }
        } else if (i == 11 && ((VideoHdr) this.mSetting.get(UserSettingKey.VIDEO_HDR)) == VideoHdr.HDR_ON) {
            return false;
        }
        return zIsSelectable;
    }

    private boolean isVisible(Context context, UserSettingKey userSettingKey) {
        CapturingMode capturingMode = (CapturingMode) this.mSetting.get(UserSettingKey.CAPTURING_MODE);
        switch (userSettingKey) {
            case VIDEO_HDR:
                return false;
            case VIDEO_SIZE:
                if (this.mActivity.isOneShotVideo() && this.mSetting.get(userSettingKey) == VideoSize.MMS) {
                    return false;
                }
                break;
            case SLOW_MOTION:
            case GRID_LINE:
            case GEO_TAG:
            case TOUCH_CAPTURE:
            case VIDEO_CODEC:
            default:
                if (userSettingKey.isCommon()) {
                    return true;
                }
                break;
            case SIDE_SENSE:
                return PlatformCapability.isSideTouchSupported();
            case SHUTTER_SOUND:
                return !PlatformCapability.isForceSound(this.mStateMachine.getCurrentCameraId());
            case DESTINATION_TO_SAVE:
                if (userSettingKey.getSelectability() == UserSettingSelectability.FIXED) {
                    return false;
                }
                return !CommonUtility.shouldStorageForceInternal(context);
            case PREDICTIVE_LAUNCH:
                return PlatformCapability.isLiftTriggerSupported();
            case CAMERA_KEY:
                return PlatformCapability.isManualBurstSupported(CameraInfo.CameraId.BACK) || PlatformCapability.isManualBurstSupported(CameraInfo.CameraId.FRONT);
            case DISTORTION_CORRECTION:
                return !capturingMode.isVideo() && PlatformCapability.isDistortionCorrectionSupported(capturingMode.getCameraId());
            case FUSION_MODE:
                return PlatformCapability.isHighSensitivityFusionSupported(capturingMode.getCameraId());
        }
        if (this.mSetting.getOptions(userSettingKey).length <= 1) {
            return false;
        }
        UserSettingSelectability selectability = userSettingKey.getSelectability();
        return selectability == UserSettingSelectability.SELECTABLE || selectability == UserSettingSelectability.UNAVAILABLE;
    }

    private SettingItem getSelectedSettingItem(List<SettingItem> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("The specified list is empty.");
        }
        for (SettingItem settingItem : list) {
            if (settingItem.isSelected() && settingItem.isSelectable()) {
                return settingItem;
            }
        }
        if (!CamLog.VERBOSE) {
            return null;
        }
        CamLog.e("The specified list doesn't have a selected item.");
        return null;
    }

    private List<SettingItem> generateChildrenSettingItem(final UserSettingKey userSettingKey, final Storage storage) {
        final ArrayList list = new ArrayList();
        final UserSettingValue value = this.mSetting.get(userSettingKey);
        if (this.mStateMachine.getCurrentCapturingMode() == CapturingMode.SLOW_MOTION && userSettingKey == UserSettingKey.VIDEO_SIZE) {
            final SlowMotion slowMotion = (SlowMotion)this.mSetting.get(UserSettingKey.SLOW_MOTION);
            final boolean superSlowFullHdSupported = PlatformCapability.isSuperSlowFullHdSupported(this.mStateMachine.getCurrentCameraId());
            if (slowMotion != SlowMotion.STANDARD_SLOW_MOTION && superSlowFullHdSupported) {
                final VideoSize full_HD = VideoSize.FULL_HD;
                final String string = this.getString(2131690233);
                list.add(SettingItemBuilder.build(full_HD).iconId(full_HD.getIconId()).textId(2131690234).dialogItemType(this.getDialogItemTypeForSecondLayer(userSettingKey)).executor((SettingExecutorInterface<VideoSize>)this.createSettingChangeExecutor(userSettingKey)).selected(value == full_HD).subText(string).additionalTextForAccessibility(string).selectability(SettingItem.Selectability.SELECTABLE).commit());
            }
            final VideoSize hd = VideoSize.HD;
            final String string2 = this.getString(2131690237);
            list.add(SettingItemBuilder.build((Geotag)hd).iconId(hd.getIconId()).textId(hd.getTextId()).dialogItemType(this.getDialogItemTypeForSecondLayer(userSettingKey)).executor((SettingExecutorInterface<Geotag>)this.createSettingChangeExecutor(userSettingKey)).selected(value == hd || slowMotion == SlowMotion.STANDARD_SLOW_MOTION).subText(string2).additionalTextForAccessibility(string2).selectability(SettingItem.Selectability.SELECTABLE).commit());
            return list;
        }
        final UserSettingValue[] options = this.getOptions(userSettingKey);
        UserSettingValue off = value;
        if (userSettingKey == UserSettingKey.GEO_TAG) {
            off = value;
            if (!GeotagManager.isGeoTagEnabled((Geotag)value, (Context)this.mActivity)) {
                off = Geotag.OFF;
            }
        }
        UserSettingValue off2 = off;
        if (userSettingKey == UserSettingKey.SIDE_SENSE) {
            off2 = off;
            if (!SettingUtil.isSideSenseEnabled(true)) {
                off2 = SideSense.OFF;
            }
        }
        final VideoSize videoSize = (VideoSize)this.mSetting.get(UserSettingKey.VIDEO_SIZE);
        final VideoCodec videoCodec = (VideoCodec)this.mSetting.get(UserSettingKey.VIDEO_CODEC);
        final CapturingMode capturingMode = (CapturingMode)this.mSetting.get(UserSettingKey.CAPTURING_MODE);
        final VideoHdr videoHdr = (VideoHdr)this.mSetting.get(UserSettingKey.VIDEO_HDR);
        final int length = options.length;
        int i = 0;
        final UserSettingValue[] array = options;
        while (i < length) {
            final UserSettingValue userSettingValue = array[i];
            if (userSettingValue != null) {
                final boolean b = off2 == userSettingValue;
                int n = isVideoSelectableValues(userSettingValue, capturingMode, videoSize, videoHdr) ? 1 : 0;
                SettingItem.Selectability selectability = SettingItem.Selectability.SELECTABLE;
                if (userSettingValue != VideoSize.FOUR_K_UHD_H264 || videoCodec != VideoCodec.H265) {
                    if (userSettingValue != VideoSize.FOUR_K_UHD_H265 || videoCodec != VideoCodec.H264) {
                        if (userSettingKey == UserSettingKey.DESTINATION_TO_SAVE) {
                            final DestinationToSave destinationToSave = (DestinationToSave)userSettingValue;
                            n = (storage.getAvailableStorage().contains(destinationToSave.getType()) ? 1 : 0);
                            Label_0699: {
                                if (n == 0) {
                                    if (destinationToSave.getType() != Storage.StorageType.INTERNAL || ((DestinationToSave)off2).getType() != Storage.StorageType.INTERNAL || storage.getAvailableStorage().contains(Storage.StorageType.EXTERNAL_CARD)) {
                                        if (destinationToSave.getType() != Storage.StorageType.EXTERNAL_CARD || ((DestinationToSave)off2).getType() != Storage.StorageType.EXTERNAL_CARD || storage.getAvailableStorage().contains(Storage.StorageType.INTERNAL)) {
                                            if (destinationToSave.getType() != Storage.StorageType.EXTERNAL_CARD || Storage.StorageState.UNGRANTED != this.mActivity.getStorage().getCurrentState(Storage.StorageType.EXTERNAL_CARD)) {
                                                break Label_0699;
                                            }
                                        }
                                    }
                                    n = 1;
                                }
                            }
                        }
                        if (n == 0) {
                            selectability = SettingItem.Selectability.UNSELECTABLE;
                        }
                        final SettingItemBuilder<VideoSize> selectability2 = SettingItemBuilder.build(userSettingValue).iconId(userSettingValue.getIconId()).textId(userSettingValue.getTextId()).dialogItemType(this.getDialogItemTypeForSecondLayer(userSettingKey)).executor((SettingExecutorInterface<VideoSize>)this.createSettingChangeExecutor(userSettingKey)).selected(b).selectability(selectability);
                        if (SettingUi$3.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()] == 4) {
                            final SlowMotion slowMotion2 = (SlowMotion)userSettingValue;
                            selectability2.subText(slowMotion2.getDescriptionText((Context)this.mActivity));
                            selectability2.additionalTextForAccessibility(slowMotion2.getDescriptionText((Context)this.mActivity));
                        }
                        list.add(selectability2.commit());
                    }
                }
            }
            ++i;
        }
        return list;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SettingAdapter generateParameterItemAdapter(UserSettingKey userSettingKey, Storage storage) {
        SettingAdapter settingAdapter = new SettingAdapter(this.mActivity, this.mDialogItemFactory, this.mIsDeviceInSecurityLock);
        settingAdapter.addAll(generateChildrenSettingItem(userSettingKey, storage));
        return settingAdapter;
    }

    public static boolean isSelectableValues(UserSettingKey userSettingKey, Parameters parameters, UserSettingValue userSettingValue) {
        return userSettingKey.isSelectable() && (AnonymousClass3.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()] != 16 ? true : isVideoSelectableValues(userSettingValue, parameters.capturingMode, parameters.getVideoSize(), parameters.getVideoHdr()));
    }

    private static boolean isVideoSelectableValues(UserSettingValue userSettingValue, CapturingMode capturingMode, VideoSize videoSize, VideoHdr videoHdr) {
        if (videoSize.is4KVideo() && userSettingValue == VideoCodec.H265 && videoHdr != VideoHdr.HDR_ON) {
            return true;
        }
        UserSettingKey key = userSettingValue.getKey();
        if (UserSettingKey.VIDEO_STABILIZER == key && key.isSelectable()) {
            return ((VideoStabilizer) userSettingValue).isValueEnabled(capturingMode.getCameraId(), videoSize, videoHdr);
        }
        return key.isSelectable();
    }

    public static UserSettingValue getImageQualityControlDefaultValue(UserSettingKey userSettingKey) {
        switch (userSettingKey) {
            case WHITE_BALANCE:
                return WhiteBalance.AUTO;
            case ISO:
                return Iso.ISO_AUTO;
            case EV:
                return Ev.ZERO;
            case SHUTTER_SPEED:
                return ShutterSpeed.AUTO;
            case FOCUS_RANGE:
                return FocusRange.AF;
            default:
                throw new IllegalArgumentException("Undefined default value for " + userSettingKey);
        }
    }

    public static int getImageQualityControlTabDescription(UserSettingKey userSettingKey) {
        switch (userSettingKey) {
            case WHITE_BALANCE:
                return R.string.cam_strings_accessibility_image_quality_control_white_balance_txt;
            case ISO:
                return R.string.cam_strings_accessibility_image_quality_control_iso_txt;
            case EV:
                return R.string.cam_strings_accessibility_image_quality_control_exposure_value_txt;
            case SHUTTER_SPEED:
                return R.string.cam_strings_accessibility_image_quality_control_shutter_speed_txt;
            case FOCUS_RANGE:
                return R.string.cam_strings_accessibility_image_quality_control_focus_txt;
            default:
                throw new IllegalArgumentException("Undefined description for " + userSettingKey);
        }
    }

    private int getDialogItemType(UserSettingKey userSettingKey) {
        switch (userSettingKey) {
            case VIDEO_STABILIZER:
                if (getOptions(userSettingKey).length > 2) {
                }
                break;
        }
        return 3;
    }

    private int getDialogItemTypeForSecondLayer(UserSettingKey userSettingKey) {
        int i = AnonymousClass3.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()];
        if (i != 1 && i != 9 && i != 12) {
            if (i == 16) {
                return getOptions(userSettingKey).length > 2 ? 2 : 4;
            }
            switch (i) {
                case 3:
                    return this.mStateMachine.getCurrentCapturingMode() != CapturingMode.SLOW_MOTION ? 2 : 5;
                case 4:
                    return 5;
                default:
                    switch (i) {
                        case 22:
                        case 23:
                        case 24:
                        case 25:
                            return 4;
                        case 26:
                        case 27:
                        case 28:
                            return 1;
                        default:
                            switch (i) {
                                case 31:
                                case 32:
                                case 33:
                                    break;
                                default:
                                    return 2;
                            }
                    }
            }
        }
        return 2;
    }

    private SettingExecutorInterface<UserSettingValue> createSettingChangeExecutor(UserSettingKey userSettingKey) {
        ParameterChanger parameterChanger = new ParameterChanger();
        int i = AnonymousClass3.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()];
        if (i != 13 && i != 23 && i != 25) {
            switch (i) {
                case 4:
                    return new SlowMotionExecutor(new SettingChangeExecutor(parameterChanger));
                case 5:
                case 6:
                case 7:
                case 8:
                    break;
                default:
                    switch (i) {
                        case 17:
                        case 18:
                        case 19:
                        case 20:
                        case 21:
                            break;
                        default:
                            return new CloseExecutor(new SettingChangeExecutor(parameterChanger));
                    }
            }
        }
        return new SettingChangeExecutor(parameterChanger);
    }

    private class CloseExecutor<T> implements SettingExecutorInterface<T> {
        private final SettingExecutorInterface<T> mExecutor;

        private CloseExecutor(SettingExecutorInterface<T> settingExecutorInterface) {
            this.mExecutor = settingExecutorInterface;
        }

        @Override // com.sonyericsson.android.camera.view.setting.executor.SettingExecutorInterface
        public void onExecute(TypedSettingItem<T> typedSettingItem) {
            this.mExecutor.onExecute(typedSettingItem);
            SettingUi.this.mSettingDialogStack.closeCurrentDialog();
        }
    }

    private class SlowMotionExecutor<T> implements SettingExecutorInterface<T> {
        private final SettingExecutorInterface<T> mExecutor;

        private SlowMotionExecutor(SettingExecutorInterface<T> settingExecutorInterface) {
            this.mExecutor = settingExecutorInterface;
        }

        @Override // com.sonyericsson.android.camera.view.setting.executor.SettingExecutorInterface
        public void onExecute(TypedSettingItem<T> typedSettingItem) {
            TutorialController.DisplayTrigger displayTrigger;
            this.mExecutor.onExecute(typedSettingItem);
            switch ((SlowMotion) SettingUi.this.mSetting.get(UserSettingKey.SLOW_MOTION)) {
                case SUPER_SLOW_MOTION:
                default:
                    displayTrigger = null;
                    break;
                case SUPER_SLOW_SHOT:
                    displayTrigger = TutorialController.DisplayTrigger.CHANGE_TO_SUPER_SLOW_MOTION_SHOT;
                    break;
                case STANDARD_SLOW_MOTION:
                    displayTrigger = TutorialController.DisplayTrigger.CHANGE_TO_STANDARD_SLOW_MOTION;
                    break;
            }
            if (displayTrigger != null && SettingUi.this.mStateMachine.isTutorialNeededToBeShownForCurrentMode()) {
                SettingUi.this.mViewFinder.openTutorial(displayTrigger);
            }
            SettingUi.this.mSettingDialogStack.closeAllSettingDialogs();
        }
    }

    private UserSettingValue[] getOptions(UserSettingKey userSettingKey) {
        return this.mSetting.getOptions(userSettingKey);
    }

    private void updateSaveDestinationSelectability(boolean z) {
        if (z) {
            UserSettingKey.DESTINATION_TO_SAVE.setSelectability(UserSettingSelectability.FIXED);
        } else {
            UserSettingKey.DESTINATION_TO_SAVE.setSelectability(UserSettingSelectability.SELECTABLE);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVideoStabilizerByVideoHdrChanged() {
        VideoStabilizer videoStabilizer;
        VideoStabilizer videoStabilizer2 = (VideoStabilizer) this.mSetting.get(UserSettingKey.VIDEO_STABILIZER);
        CameraInfo.CameraId cameraId = ((CapturingMode) this.mSetting.get(UserSettingKey.CAPTURING_MODE)).getCameraId();
        VideoSize videoSize = (VideoSize) this.mSetting.get(UserSettingKey.VIDEO_SIZE);
        if (videoStabilizer2 == VideoStabilizer.OFF || !VideoStabilizer.isSteadyShotSupported(cameraId, videoSize)) {
            return;
        }
        if (VideoStabilizer.isIntelligentActiveSupported(cameraId, videoSize)) {
            videoStabilizer = VideoStabilizer.INTELLIGENT_ACTIVE;
        } else {
            videoStabilizer = VideoStabilizer.STEADY_SHOT;
        }
        CamLog.d("SteadyShot -> " + videoStabilizer);
        this.mSetting.set(videoStabilizer);
    }
}
