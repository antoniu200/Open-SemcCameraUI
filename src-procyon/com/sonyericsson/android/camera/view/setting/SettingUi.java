// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.setting;

import com.sonyericsson.android.camera.view.tutorial.TutorialController;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSound;
import com.sonyericsson.cameracommon.utility.RegionConfig;
import com.sonyericsson.android.camera.view.modeselector.ModeSelectorInternalMode;
import com.sonyericsson.android.camera.LaunchCondition;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.view.ViewFinder;
import com.sonyericsson.android.camera.view.selectabledialog.ModeSelector;
import com.sonyericsson.android.camera.view.modeselector.ModeLoader;
import android.widget.ArrayAdapter;
import com.sonyericsson.android.camera.view.setting.dialog.SettingDialogFactory;
import com.sonyericsson.android.camera.view.selectabledialog.SettingMenu;
import com.sonyericsson.cameracommon.utility.CommonUtility;
import com.sonyericsson.android.camera.configuration.UserSettingSelectability;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.configuration.parameters.VideoStabilizer;
import com.sonyericsson.android.camera.parameter.Parameters;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonymobile.cameracommon.research.ResearchUtil;
import com.sonyericsson.android.camera.util.HelpGuide;
import com.sonyericsson.android.camera.view.setting.settingitem.TypedSettingItem;
import java.util.Iterator;
import com.sonyericsson.android.camera.view.messagedialog.DialogId;
import java.util.Collection;
import com.sonyericsson.android.camera.controller.launcher.ApplicationLauncher;
import com.sonyericsson.android.camera.view.modeselector.Mode;
import com.sonyericsson.android.camera.configuration.parameters.DestinationToSave;
import com.sonyericsson.android.camera.configuration.parameters.VideoHdr;
import com.sonyericsson.android.camera.configuration.parameters.VideoCodec;
import com.sonyericsson.android.camera.configuration.parameters.SideSense;
import com.sonyericsson.android.camera.util.SettingUtil;
import android.content.Context;
import com.sonyericsson.cameracommon.mediasaving.location.GeotagManager;
import com.sonyericsson.android.camera.configuration.parameters.Geotag;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItemBuilder;
import com.sonyericsson.android.camera.configuration.parameters.VideoSize;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import java.util.ArrayList;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import java.util.List;
import com.sonyericsson.android.camera.view.setting.executor.SettingChangerInterface;
import com.sonyericsson.android.camera.view.setting.executor.SettingChangeExecutor;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.view.setting.executor.SettingExecutorInterface;
import com.sonyericsson.android.camera.configuration.parameters.TouchCapture;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.android.camera.view.setting.dialog.SettingDialogListener;
import java.util.HashMap;
import com.sonyericsson.android.camera.view.ViewFinderImpl;
import com.sonyericsson.android.camera.controller.StateMachine;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;
import java.util.Map;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItemFactory;
import com.sonyericsson.android.camera.device.CameraDeviceHandler;
import com.sonyericsson.android.camera.CameraActivity;

public class SettingUi
{
    public static final String TAG = "SettingUi";
    private final CameraActivity mActivity;
    private final CameraDeviceHandler mCameraDevice;
    private final SettingDialogItemFactory mDialogItemFactory;
    private boolean mIsDeviceInSecurityLock;
    private final UserSettings mSetting;
    private final SettingDialogStack mSettingDialogStack;
    private final Map<SettingAdapter, UserSettingKey[]> mSettingMenuAdapter;
    private final ContextualSettingList mSettingMenuList;
    private final StateMachine mStateMachine;
    private final ViewFinderImpl mViewFinder;
    
    public SettingUi(final CameraActivity mActivity, final SettingDialogStack mSettingDialogStack, final StateMachine mStateMachine, final ViewFinderImpl mViewFinder, final CameraDeviceHandler mCameraDevice, final boolean mIsDeviceInSecurityLock) {
        this.mSettingMenuAdapter = new HashMap<SettingAdapter, UserSettingKey[]>();
        this.mActivity = mActivity;
        this.mSettingMenuList = new ContextualSettingList(this.mActivity.getResources().getBoolean(2131034117));
        (this.mSettingDialogStack = mSettingDialogStack).setContextualMenuListener(new SettingDialogListenerImpl());
        this.mStateMachine = mStateMachine;
        this.mViewFinder = mViewFinder;
        this.mCameraDevice = mCameraDevice;
        this.mIsDeviceInSecurityLock = mIsDeviceInSecurityLock;
        this.mDialogItemFactory = new SettingDialogItemFactory();
        this.mSetting = this.mStateMachine.getUserSetting();
    }
    
    private void clearSettingMenuAdapter() {
        if (this.mSettingMenuAdapter.size() > 0) {
            this.mSettingMenuAdapter.clear();
        }
    }
    
    private SettingExecutorInterface<UserSettingValue> createSettingChangeExecutor(final UserSettingKey userSettingKey) {
        final ParameterChanger parameterChanger = new ParameterChanger();
        final int n = SettingUi$3.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()];
        Label_0144: {
            if (n != 13 && n != 23 && n != 25) {
                switch (n) {
                    default: {
                        switch (n) {
                            default: {
                                return new CloseExecutor<UserSettingValue>((SettingExecutorInterface)new SettingChangeExecutor((SettingChangerInterface<Object>)parameterChanger));
                            }
                            case 17:
                            case 18:
                            case 19:
                            case 20:
                            case 21: {
                                break Label_0144;
                            }
                        }
                        break;
                    }
                    case 4: {
                        return new SlowMotionExecutor<UserSettingValue>((SettingExecutorInterface)new SettingChangeExecutor(parameterChanger));
                    }
                    case 5:
                    case 6:
                    case 7:
                    case 8: {
                        break;
                    }
                }
            }
        }
        return new SettingChangeExecutor<UserSettingValue>((SettingChangerInterface<Object>)parameterChanger);
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
    
    private SettingAdapter generateMonochromeAdpter(final boolean b, final Mode mode) {
        final SettingAdapter settingAdapter = new SettingAdapter((Context)this.mActivity, this.mDialogItemFactory, this.mIsDeviceInSecurityLock);
        final ArrayList list = new ArrayList();
        list.add(SettingItemBuilder.build(ApplicationLauncher.MonochromeType.MONOCHROME_PHOTO).iconId(2131231216).textId(2131689950).dialogItemType(2).executor(new MonochromeExecutor<ApplicationLauncher.MonochromeType>(b, mode)).selected(false).selectability(SettingItem.Selectability.SELECTABLE).commit());
        list.add(SettingItemBuilder.build(ApplicationLauncher.MonochromeType.MONOCHROME_VIDEO).iconId(2131231217).textId(2131689952).dialogItemType(2).executor(new MonochromeExecutor<ApplicationLauncher.MonochromeType>(b, mode)).selected(false).selectability(SettingItem.Selectability.SELECTABLE).commit());
        settingAdapter.addAll((Collection)list);
        return settingAdapter;
    }
    
    private SettingAdapter generateParameterItemAdapter(final UserSettingKey userSettingKey, final Storage storage) {
        final SettingAdapter settingAdapter = new SettingAdapter((Context)this.mActivity, this.mDialogItemFactory, this.mIsDeviceInSecurityLock);
        settingAdapter.addAll((Collection)this.generateChildrenSettingItem(userSettingKey, storage));
        return settingAdapter;
    }
    
    private SettingItem generateParameterKeyItem(final UserSettingKey userSettingKey, final Storage storage) {
        final List<SettingItem> generateChildrenSettingItem = this.generateChildrenSettingItem(userSettingKey, storage);
        final SettingItem selectedSettingItem = this.getSelectedSettingItem(generateChildrenSettingItem);
        String text = "";
        if (selectedSettingItem != null) {
            text = selectedSettingItem.getText(this.mActivity.getResources());
        }
        final boolean selectableKey = this.isSelectableKey(userSettingKey);
        final DialogId restrictMessageDialogId = userSettingKey.getRestrictMessageDialogId(this.mSetting);
        SettingItem.Selectability selectability = SettingItem.Selectability.SELECTABLE;
        if (!selectableKey) {
            if (restrictMessageDialogId == DialogId.DLG_INVALID) {
                selectability = SettingItem.Selectability.UNSELECTABLE;
            }
            else {
                selectability = SettingItem.Selectability.RESTRICTED;
            }
        }
        final SettingItemBuilder<UserSettingKey> executor = SettingItemBuilder.build(userSettingKey).textId(userSettingKey.getTitleTextId()).additionalTextForAccessibility(text).dialogItemType(this.getDialogItemType(userSettingKey)).selectability(selectability).executor(this.generateSettingItemExecutor(userSettingKey, selectability, storage));
        if (!userSettingKey.equals(UserSettingKey.HELP_GUIDE)) {
            if (!userSettingKey.equals(UserSettingKey.RESET_SETTINGS)) {
                final Iterator<SettingItem> iterator = generateChildrenSettingItem.iterator();
                while (iterator.hasNext()) {
                    executor.item(iterator.next());
                }
            }
        }
        return executor.commit();
    }
    
    private SettingExecutorInterface<UserSettingKey> generateSettingItemExecutor(final UserSettingKey userSettingKey, final SettingItem.Selectability selectability, final Storage storage) {
        final CameraActivity mActivity = this.mActivity;
        switch (SettingUi$3.$SwitchMap$com$sonyericsson$android$camera$view$setting$settingitem$SettingItem$Selectability[selectability.ordinal()]) {
            default: {
                return null;
            }
            case 2: {
                return new SettingExecutorInterface<UserSettingKey>(this, userSettingKey) {
                    final SettingUi this$0;
                    final UserSettingKey val$key;
                    
                    @Override
                    public void onExecute(final TypedSettingItem<UserSettingKey> typedSettingItem) {
                        this.this$0.mViewFinder.showMessageDialog(this.val$key.getRestrictMessageDialogId(this.this$0.mSetting), new Object[0]);
                    }
                };
            }
            case 1: {
                return new SettingExecutorInterface<UserSettingKey>(this, userSettingKey, mActivity, storage) {
                    final SettingUi this$0;
                    final Context val$context;
                    final UserSettingKey val$key;
                    final Storage val$storage;
                    
                    @Override
                    public void onExecute(final TypedSettingItem<UserSettingKey> typedSettingItem) {
                        if (this.val$key == UserSettingKey.HELP_GUIDE) {
                            if (this.this$0.mIsDeviceInSecurityLock) {
                                this.this$0.mViewFinder.showMessageDialog(DialogId.UNLOCK_REQUEST_FOR_OPENING_OPTION_MENU, this.val$key.name());
                                return;
                            }
                            if (HelpGuide.isHelpAppAvailable(this.val$context)) {
                                HelpGuide.startHelpApp(this.val$context);
                            }
                            else {
                                HelpGuide.startOnlineHelp(this.val$context);
                            }
                            ResearchUtil.getInstance().sendSettingsCommon(typedSettingItem.getData().toString());
                        }
                        else if (this.val$key == UserSettingKey.RESET_SETTINGS) {
                            if (this.this$0.mIsDeviceInSecurityLock) {
                                this.this$0.mViewFinder.showMessageDialog(DialogId.UNLOCK_REQUEST_FOR_OPENING_OPTION_MENU, this.val$key.name());
                                return;
                            }
                            this.this$0.mSettingDialogStack.closeCurrentDialog();
                            this.this$0.mViewFinder.showMessageDialog(DialogId.RESET_CONFIRMATION, new Object[0]);
                        }
                        else if (this.this$0.mIsDeviceInSecurityLock && (this.val$key == UserSettingKey.DESTINATION_TO_SAVE || this.val$key == UserSettingKey.GEO_TAG || (!SettingUtil.isSideSenseEnabled(true) && this.val$key == UserSettingKey.SIDE_SENSE))) {
                            this.this$0.mViewFinder.showMessageDialog(DialogId.UNLOCK_REQUEST_FOR_OPENING_OPTION_MENU, this.val$key.name());
                        }
                        else {
                            this.this$0.openSecondLayerDialog(this.this$0.generateParameterItemAdapter(typedSettingItem.getData(), this.val$storage), this.val$key);
                        }
                    }
                };
            }
        }
    }
    
    private SettingAdapter generateSettingMenuItemAdapter(final Context context, final UserSettingKey[] array) {
        final SettingAdapter settingAdapter = new SettingAdapter((Context)this.mActivity, this.mDialogItemFactory, this.mIsDeviceInSecurityLock);
        for (final UserSettingKey userSettingKey : array) {
            if (this.isVisible(context, userSettingKey)) {
                settingAdapter.add((Object)this.generateParameterKeyItem(userSettingKey, this.mActivity.getStorage()));
            }
        }
        return settingAdapter;
    }
    
    private int getDialogItemType(final UserSettingKey userSettingKey) {
        switch (SettingUi$3.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()]) {
            default: {
                return 3;
            }
            case 29: {
                return 3;
            }
            case 26:
            case 27:
            case 28: {
                return 1;
            }
            case 22:
            case 23:
            case 24:
            case 25: {
                return 4;
            }
            case 16: {
                if (this.getOptions(userSettingKey).length > 2) {
                    return 3;
                }
                return 4;
            }
            case 9:
            case 10:
            case 12:
            case 14:
            case 30: {
                return 3;
            }
            case 5:
            case 6:
            case 7:
            case 8:
            case 13: {
                return 4;
            }
        }
    }
    
    private int getDialogItemTypeForSecondLayer(final UserSettingKey userSettingKey) {
        final int n = SettingUi$3.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()];
        Label_0167: {
            if (n != 1 && n != 9 && n != 12) {
                if (n != 16) {
                    switch (n) {
                        default: {
                            switch (n) {
                                default: {
                                    switch (n) {
                                        default: {
                                            return 2;
                                        }
                                        case 31:
                                        case 32:
                                        case 33: {
                                            break Label_0167;
                                        }
                                    }
                                    break;
                                }
                                case 26:
                                case 27:
                                case 28: {
                                    return 1;
                                }
                                case 22:
                                case 23:
                                case 24:
                                case 25: {
                                    return 4;
                                }
                            }
                            break;
                        }
                        case 4: {
                            return 5;
                        }
                        case 3: {
                            if (this.mStateMachine.getCurrentCapturingMode() != CapturingMode.SLOW_MOTION) {
                                return 2;
                            }
                            return 5;
                        }
                    }
                }
                else {
                    if (this.getOptions(userSettingKey).length > 2) {
                        return 2;
                    }
                    return 4;
                }
            }
        }
        return 2;
    }
    
    public static UserSettingValue getImageQualityControlDefaultValue(final UserSettingKey obj) {
        switch (SettingUi$3.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[obj.ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Undefined default value for ");
                sb.append(obj);
                throw new IllegalArgumentException(sb.toString());
            }
            case 21: {
                return FocusRange.AF;
            }
            case 20: {
                return ShutterSpeed.AUTO;
            }
            case 19: {
                return Ev.ZERO;
            }
            case 18: {
                return Iso.ISO_AUTO;
            }
            case 17: {
                return WhiteBalance.AUTO;
            }
        }
    }
    
    public static int getImageQualityControlTabDescription(final UserSettingKey obj) {
        switch (SettingUi$3.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[obj.ordinal()]) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Undefined description for ");
                sb.append(obj);
                throw new IllegalArgumentException(sb.toString());
            }
            case 21: {
                return 2131689583;
            }
            case 20: {
                return 2131689588;
            }
            case 19: {
                return 2131689577;
            }
            case 18: {
                return 2131689584;
            }
            case 17: {
                return 2131689589;
            }
        }
    }
    
    private UserSettingValue[] getOptions(final UserSettingKey userSettingKey) {
        return this.mSetting.getOptions(userSettingKey);
    }
    
    private SettingItem getSelectedSettingItem(final List<SettingItem> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("The specified list is empty.");
        }
        for (final SettingItem settingItem : list) {
            if (settingItem.isSelected() && settingItem.isSelectable()) {
                return settingItem;
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.e("The specified list doesn't have a selected item.");
        }
        return null;
    }
    
    private String getString(final int n) {
        return this.mActivity.getResources().getString(n);
    }
    
    private boolean isSelectableKey(final UserSettingKey userSettingKey) {
        final boolean selectable = userSettingKey.isSelectable();
        final int n = SettingUi$3.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()];
        final boolean b = false;
        if (n != 3) {
            if (n == 11) {
                if (this.mSetting.get(UserSettingKey.VIDEO_HDR) == VideoHdr.HDR_ON) {
                    return b;
                }
            }
        }
        else {
            final CapturingMode capturingMode = (CapturingMode)this.mSetting.get(UserSettingKey.CAPTURING_MODE);
            if (capturingMode == CapturingMode.SLOW_MOTION) {
                boolean b2 = b;
                if (this.mSetting.get(UserSettingKey.SLOW_MOTION) == SlowMotion.STANDARD_SLOW_MOTION) {
                    return b2;
                }
                if (!PlatformCapability.isSuperSlowFullHdSupported(capturingMode.getCameraId())) {
                    b2 = b;
                    return b2;
                }
            }
        }
        return selectable;
    }
    
    public static boolean isSelectableValues(final UserSettingKey userSettingKey, final Parameters parameters, final UserSettingValue userSettingValue) {
        final int n = SettingUi$3.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()];
        final boolean b = true;
        final boolean b2 = n != 16 || isVideoSelectableValues(userSettingValue, parameters.capturingMode, parameters.getVideoSize(), parameters.getVideoHdr());
        return userSettingKey.isSelectable() && b2 && b;
    }
    
    private static boolean isVideoSelectableValues(final UserSettingValue userSettingValue, final CapturingMode capturingMode, final VideoSize videoSize, final VideoHdr videoHdr) {
        if (videoSize.is4KVideo() && userSettingValue == VideoCodec.H265 && videoHdr != VideoHdr.HDR_ON) {
            return true;
        }
        final UserSettingKey key = userSettingValue.getKey();
        if (UserSettingKey.VIDEO_STABILIZER == key && key.isSelectable()) {
            return ((VideoStabilizer)userSettingValue).isValueEnabled(capturingMode.getCameraId(), videoSize, videoHdr);
        }
        return key.isSelectable();
    }
    
    private boolean isVisible(final Context context, final UserSettingKey userSettingKey) {
        final CapturingMode capturingMode = (CapturingMode)this.mSetting.get(UserSettingKey.CAPTURING_MODE);
        final int n = SettingUi$3.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingKey.ordinal()];
        boolean b = false;
        switch (n) {
            default: {
                if (userSettingKey.isCommon()) {
                    return true;
                }
                break;
            }
            case 15: {
                return PlatformCapability.isHighSensitivityFusionSupported(capturingMode.getCameraId());
            }
            case 14: {
                return !capturingMode.isVideo() && PlatformCapability.isDistortionCorrectionSupported(capturingMode.getCameraId());
            }
            case 13: {
                return PlatformCapability.isManualBurstSupported(CameraInfo.CameraId.BACK) || PlatformCapability.isManualBurstSupported(CameraInfo.CameraId.FRONT);
            }
            case 12: {
                return PlatformCapability.isLiftTriggerSupported();
            }
            case 9: {
                return userSettingKey.getSelectability() != UserSettingSelectability.FIXED && (CommonUtility.shouldStorageForceInternal(context) ^ true);
            }
            case 7: {
                return PlatformCapability.isForceSound(this.mStateMachine.getCurrentCameraId()) ^ true;
            }
            case 5: {
                return PlatformCapability.isSideTouchSupported();
            }
            case 3: {
                if (this.mActivity.isOneShotVideo() && this.mSetting.get(userSettingKey) == VideoSize.MMS) {
                    return false;
                }
                break;
            }
            case 2: {
                return false;
            }
        }
        if (this.mSetting.getOptions(userSettingKey).length <= 1) {
            return false;
        }
        final UserSettingSelectability selectability = userSettingKey.getSelectability();
        if (selectability == UserSettingSelectability.SELECTABLE || selectability == UserSettingSelectability.UNAVAILABLE) {
            b = true;
        }
        return b;
    }
    
    private void openSecondLayerDialog(final SettingAdapter settingAdapter, final Object o) {
        this.mSettingDialogStack.openSecondLayerDialog(settingAdapter, o);
    }
    
    private SettingMenu openSettingMenuDialog(final boolean b, final boolean b2) {
        final CapturingMode currentCapturingMode = this.mStateMachine.getCurrentCapturingMode();
        final ContextualSettingList.Group value = this.mSettingMenuList.get(currentCapturingMode, this.mActivity.isOneShot());
        final CameraActivity mActivity = this.mActivity;
        this.updateSaveDestinationSelectability(this.mActivity.hasExtraOutputPath());
        final SettingMenu settingMenuDialog = SettingDialogFactory.createSettingMenuDialog((Context)mActivity, this.mSettingDialogStack.getBackgroundWidth(), this.mSettingDialogStack.getBackgroundHeight(), b2);
        final SettingAdapter generateSettingMenuItemAdapter = this.generateSettingMenuItemAdapter((Context)mActivity, value.priorityHigh.keys);
        settingMenuDialog.addPanel(this.getString(value.priorityHigh.titleResource), generateSettingMenuItemAdapter);
        this.mSettingMenuAdapter.put(generateSettingMenuItemAdapter, value.priorityHigh.keys);
        if (value.common.keys.length > 0) {
            final SettingAdapter generateSettingMenuItemAdapter2 = this.generateSettingMenuItemAdapter((Context)mActivity, value.common.keys);
            settingMenuDialog.addPanel(this.getString(value.common.titleResource), generateSettingMenuItemAdapter2);
            this.mSettingMenuAdapter.put(generateSettingMenuItemAdapter2, value.common.keys);
        }
        if (!this.mSettingDialogStack.openMenuDialog(settingMenuDialog, currentCapturingMode, b)) {
            this.mSettingDialogStack.closeAllSettingDialogs(true);
        }
        return settingMenuDialog;
    }
    
    private void setVideoStabilizerByVideoHdrChanged() {
        final VideoStabilizer videoStabilizer = (VideoStabilizer)this.mSetting.get(UserSettingKey.VIDEO_STABILIZER);
        final CameraInfo.CameraId cameraId = ((CapturingMode)this.mSetting.get(UserSettingKey.CAPTURING_MODE)).getCameraId();
        final VideoSize videoSize = (VideoSize)this.mSetting.get(UserSettingKey.VIDEO_SIZE);
        if (videoStabilizer != VideoStabilizer.OFF && VideoStabilizer.isSteadyShotSupported(cameraId, videoSize)) {
            VideoStabilizer obj;
            if (VideoStabilizer.isIntelligentActiveSupported(cameraId, videoSize)) {
                obj = VideoStabilizer.INTELLIGENT_ACTIVE;
            }
            else {
                obj = VideoStabilizer.STEADY_SHOT;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("SteadyShot -> ");
            sb.append(obj);
            CamLog.d(sb.toString());
            this.mSetting.set(obj);
        }
    }
    
    private void updateSaveDestinationSelectability(final boolean b) {
        if (b) {
            UserSettingKey.DESTINATION_TO_SAVE.setSelectability(UserSettingSelectability.FIXED);
        }
        else {
            UserSettingKey.DESTINATION_TO_SAVE.setSelectability(UserSettingSelectability.SELECTABLE);
        }
    }
    
    private void updateTouchCapture(final TouchCapture touchCapture) {
        this.mViewFinder.updateTouchCapture(touchCapture);
        this.mSetting.set(touchCapture);
        this.mViewFinder.commit();
    }
    
    public boolean closeDialogs() {
        if (this.mSettingDialogStack.isDialogOpened()) {
            this.mSettingDialogStack.closeAllSettingDialogs(true);
            return true;
        }
        return false;
    }
    
    public void openModeSelectDialog(final ModeLoader modeLoader, final ModeSelector.OnModeSelectListener onModeSelectListener) {
        if (!this.mSettingDialogStack.openModeSelectorDialog(modeLoader, onModeSelectListener)) {
            this.mSettingDialogStack.closeCurrentDialog();
        }
    }
    
    public void openMonochromeDialog(final boolean b, final int n, final Mode mode) {
        this.mSettingDialogStack.openMonochromeDialog(this.generateMonochromeAdpter(b, mode), n);
    }
    
    public void openSettingMenuDialog() {
        this.openSettingMenuDialog(false, false);
    }
    
    public void openShortcutSettingDialog(final UserSettingKey userSettingKey) {
        final SettingAdapter settingAdapter = new SettingAdapter((Context)this.mActivity, this.mDialogItemFactory, this.mIsDeviceInSecurityLock);
        final Iterator<SettingItem> iterator = this.generateChildrenSettingItem(userSettingKey, this.mActivity.getStorage()).iterator();
        while (iterator.hasNext()) {
            settingAdapter.add((Object)iterator.next());
        }
        final SettingMenu shortcutDialog = SettingDialogFactory.createShortcutDialog((Context)this.mActivity, userSettingKey, this.mSettingDialogStack.getBackgroundWidth(), this.mSettingDialogStack.getBackgroundHeight());
        if (shortcutDialog != null) {
            shortcutDialog.addPanel(this.mActivity.getResources().getString(userSettingKey.getTitleTextId()), settingAdapter);
            if (!this.mSettingDialogStack.openShortcutDialog(shortcutDialog, userSettingKey)) {
                this.mSettingDialogStack.closeCurrentDialog();
            }
        }
    }
    
    public void openUserSelectMenu(final UserSettingKey userSettingKey) {
        final SettingMenu openSettingMenuDialog = this.openSettingMenuDialog(true, true);
        if (userSettingKey != null) {
            openSettingMenuDialog.select(userSettingKey);
        }
    }
    
    public void setDeviceInSecurityLock(final boolean mIsDeviceInSecurityLock) {
        this.mIsDeviceInSecurityLock = mIsDeviceInSecurityLock;
    }
    
    public void setSensorOrientation(final int uiOrientation) {
        this.mSettingDialogStack.setUiOrientation(uiOrientation);
    }
    
    public void updateSettingMenu(final boolean b) {
        if (CamLog.VERBOSE) {
            CamLog.d("updateSettingMenu() is called");
        }
        for (final SettingAdapter settingAdapter : this.mSettingMenuAdapter.keySet()) {
            settingAdapter.clear();
            for (final UserSettingKey userSettingKey : this.mSettingMenuAdapter.get(settingAdapter)) {
                if (this.isVisible((Context)this.mActivity, userSettingKey)) {
                    settingAdapter.add((Object)this.generateParameterKeyItem(userSettingKey, this.mActivity.getStorage()));
                }
                if (b) {
                    settingAdapter.notifyDataSetInvalidated();
                }
                else {
                    settingAdapter.notifyDataSetChanged();
                }
            }
        }
    }
    
    private class CloseExecutor<T> implements SettingExecutorInterface<T>
    {
        private final SettingExecutorInterface<T> mExecutor;
        final SettingUi this$0;
        
        private CloseExecutor(final SettingUi this$0, final SettingExecutorInterface<T> mExecutor) {
            this.this$0 = this$0;
            this.mExecutor = mExecutor;
        }
        
        @Override
        public void onExecute(final TypedSettingItem<T> typedSettingItem) {
            this.mExecutor.onExecute(typedSettingItem);
            this.this$0.mSettingDialogStack.closeCurrentDialog();
        }
    }
    
    private class MonochromeExecutor<T> implements SettingExecutorInterface<T>
    {
        boolean mIsMostRecentlyUsed;
        Mode mMode;
        final SettingUi this$0;
        
        private MonochromeExecutor(final SettingUi this$0, final boolean mIsMostRecentlyUsed, final Mode mMode) {
            this.this$0 = this$0;
            this.mIsMostRecentlyUsed = false;
            this.mIsMostRecentlyUsed = mIsMostRecentlyUsed;
            this.mMode = mMode;
        }
        
        @Override
        public void onExecute(final TypedSettingItem<T> typedSettingItem) {
            this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_MRU_SHORTCUT, this.mMode);
            if (this.mIsMostRecentlyUsed) {
                LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.MRU_SHORTCUT);
                LocalResearchUtil.getInstance().setLaunchBy(LaunchCondition.LaunchTrigger.MRU_SHORTCUT);
            }
            else {
                LocalResearchUtil.getInstance().setModeChangeMethod(LocalResearchUtil.ModeChangeMethod.MODE_SELECTOR);
                LocalResearchUtil.getInstance().setLaunchBy(LaunchCondition.LaunchTrigger.MODE_SELECTOR);
            }
            final CapturingMode capturingMode = (CapturingMode)this.this$0.mSetting.get(UserSettingKey.CAPTURING_MODE);
            switch (SettingUi$3.$SwitchMap$com$sonyericsson$android$camera$controller$launcher$ApplicationLauncher$MonochromeType[((ApplicationLauncher.MonochromeType)typedSettingItem.getData()).ordinal()]) {
                case 2: {
                    if (this.this$0.mActivity.isDeviceInSecurityLock()) {
                        this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_UNLOCK_SCREEN_DIALOG, CapturingMode.VIDEO, this.mMode);
                        break;
                    }
                    LocalResearchUtil.getInstance().sendEventInternalModeChange(capturingMode, ModeSelectorInternalMode.DUAL_MONOCHROME);
                    ApplicationLauncher.launchExternalCamera(this.this$0.mActivity, 17, this.this$0.mStateMachine.getUserSetting(), CapturingMode.VIDEO, true);
                    break;
                }
                case 1: {
                    if (this.this$0.mActivity.isDeviceInSecurityLock()) {
                        this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_SHOW_UNLOCK_SCREEN_DIALOG, CapturingMode.SCENE_RECOGNITION, this.mMode);
                        break;
                    }
                    LocalResearchUtil.getInstance().sendEventInternalModeChange(capturingMode, ModeSelectorInternalMode.DUAL_MONOCHROME);
                    ApplicationLauncher.launchExternalCamera(this.this$0.mActivity, 17, this.this$0.mStateMachine.getUserSetting(), CapturingMode.SCENE_RECOGNITION, true);
                    break;
                }
            }
            this.this$0.mSettingDialogStack.closeAllSettingDialogs();
        }
    }
    
    private class ParameterChanger implements SettingChangerInterface<UserSettingValue>
    {
        final SettingUi this$0;
        
        private ParameterChanger(final SettingUi this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void changeValue(final TypedSettingItem<UserSettingValue> typedSettingItem) {
            final UserSettingValue value = this.this$0.mSetting.get(typedSettingItem.getData().getKey());
            final UserSettingValue obj = typedSettingItem.getData();
            if (value == obj) {
                return;
            }
            final UserSettingKey key = obj.getKey();
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("ParameterChanger#changeValue() Key : ");
                sb.append(key);
                sb.append(" value : ");
                sb.append(obj);
                CamLog.d(sb.toString());
            }
            if (UserSettingKey.DESTINATION_TO_SAVE.equals(key) && value == DestinationToSave.EMMC && obj == DestinationToSave.SDCARD && Storage.StorageState.UNGRANTED == this.this$0.mActivity.getStorage().getCurrentState(Storage.StorageType.EXTERNAL_CARD)) {
                this.this$0.mViewFinder.showMessageDialog(DialogId.REQUEST_SD_CARD_PERMISSION, new Object[0]);
                return;
            }
            this.this$0.mSetting.set(obj);
            final CapturingMode capturingMode = (CapturingMode)this.this$0.mSetting.get(UserSettingKey.CAPTURING_MODE);
            while (true) {
                switch (SettingUi$3.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[key.ordinal()]) {
                    default: {
                        break Label_0679;
                    }
                    case 11: {
                        final VideoSize videoSize = (VideoSize)this.this$0.mSetting.get(UserSettingKey.VIDEO_SIZE);
                        if (obj == VideoCodec.H264) {
                            if (videoSize == VideoSize.FOUR_K_UHD_H265) {
                                this.this$0.mSetting.set(VideoSize.FOUR_K_UHD_H264);
                            }
                            break Label_0679;
                        }
                        else {
                            if (obj == VideoCodec.H265 && videoSize == VideoSize.FOUR_K_UHD_H264) {
                                this.this$0.mSetting.set(VideoSize.FOUR_K_UHD_H265);
                            }
                            break Label_0679;
                        }
                        break;
                    }
                    case 8: {
                        if (obj == Geotag.ON && RegionConfig.isChinaRegion((Context)this.this$0.mActivity)) {
                            this.this$0.mSettingDialogStack.closeAllSettingDialogs();
                        }
                        if (!this.this$0.mActivity.getGeoTagManager().setGeotag((Geotag)obj, this.this$0.mActivity, this.this$0.mViewFinder)) {
                            this.this$0.mSettingDialogStack.closeCurrentDialog();
                            this.this$0.mViewFinder.showMessageDialog(DialogId.LOCATION_SERVICE_DISABLE_ON_CONTEXTUAL_SETTINGS, new Object[0]);
                        }
                        break Label_0679;
                    }
                    case 7: {
                        if (obj != ShutterSound.OFF) {
                            this.this$0.mCameraDevice.playShutterSound(capturingMode.getType());
                        }
                        break Label_0679;
                    }
                    case 5: {
                        if (!SettingUtil.isSideSenseEnabled(true)) {
                            this.this$0.mSettingDialogStack.closeCurrentDialog();
                            this.this$0.mViewFinder.showMessageDialog(DialogId.SIDE_SENSE_DISABLE_ON_CONTEXTUAL_SETTINGS, new Object[0]);
                        }
                        break Label_0679;
                    }
                    case 3: {
                        final VideoSize videoSize2 = (VideoSize)value;
                        if (((VideoSize)obj).is4KVideo() && !videoSize2.is4KVideo()) {
                            this.this$0.mViewFinder.showMessageDialog(DialogId.THERMAL_NOTE, new Object[0]);
                        }
                        break Label_0679;
                    }
                    case 2: {
                        if (value == VideoHdr.HDR_ON && obj == VideoHdr.HDR_OFF) {
                            this.this$0.setVideoStabilizerByVideoHdrChanged();
                        }
                        break Label_0679;
                    }
                    case 1:
                    case 4: {
                        this.this$0.updateSettingMenu(false);
                        ResearchUtil.getInstance().sendSettingsCommon(typedSettingItem.getData());
                        LocalResearchUtil.getInstance().setSettingsValue(value, typedSettingItem.getData(), capturingMode);
                        return;
                    }
                    case 10: {
                        this.this$0.updateTouchCapture((TouchCapture)obj);
                        continue;
                    }
                    case 9: {
                        if (value == DestinationToSave.EMMC) {
                            this.this$0.mViewFinder.showHiSpeedSdCardRecommendDialogOnDestinationChange();
                        }
                        final Storage.StorageType type = ((DestinationToSave)obj).getType();
                        this.this$0.mViewFinder.notifyStorageStateChanged(type, this.this$0.mActivity.getStorage().getCurrentState(type), false, false);
                        continue;
                    }
                    case 6: {
                        this.this$0.mViewFinder.sendViewUpdateEvent(ViewFinder.ViewUpdateEvent.EVENT_REQUEST_UPDATE_GRID_LINE, new Object[0]);
                        continue;
                    }
                }
                break;
            }
        }
    }
    
    private class SettingDialogListenerImpl implements SettingDialogListener
    {
        final SettingUi this$0;
        
        private SettingDialogListenerImpl(final SettingUi this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onCloseSettingDialog(final Object o) {
            if (o == UserSettingKey.SETTING_MENU) {
                this.this$0.clearSettingMenuAdapter();
            }
        }
        
        @Override
        public void onOpenSettingDialog(final Object o) {
        }
    }
    
    private class SlowMotionExecutor<T> implements SettingExecutorInterface<T>
    {
        private final SettingExecutorInterface<T> mExecutor;
        final SettingUi this$0;
        
        private SlowMotionExecutor(final SettingUi this$0, final SettingExecutorInterface<T> mExecutor) {
            this.this$0 = this$0;
            this.mExecutor = mExecutor;
        }
        
        @Override
        public void onExecute(final TypedSettingItem<T> typedSettingItem) {
            this.mExecutor.onExecute(typedSettingItem);
            Enum<TutorialController.DisplayTrigger> enum1 = null;
            switch (SettingUi$3.$SwitchMap$com$sonyericsson$android$camera$configuration$parameters$SlowMotion[((SlowMotion)this.this$0.mSetting.get(UserSettingKey.SLOW_MOTION)).ordinal()]) {
                case 3: {
                    enum1 = TutorialController.DisplayTrigger.CHANGE_TO_STANDARD_SLOW_MOTION;
                    break;
                }
                case 2: {
                    enum1 = TutorialController.DisplayTrigger.CHANGE_TO_SUPER_SLOW_MOTION_SHOT;
                    break;
                }
                default:
                case 1: {
                    enum1 = null;
                    break;
                }
            }
            if (enum1 != null && this.this$0.mStateMachine.isTutorialNeededToBeShownForCurrentMode()) {
                this.this$0.mViewFinder.openTutorial((TutorialController.DisplayTrigger)enum1);
            }
            this.this$0.mSettingDialogStack.closeAllSettingDialogs();
        }
    }
}
