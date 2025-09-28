package com.sonyericsson.android.camera.view.overlaycontrol;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.setting.UiControlSettings;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl;
import com.sonyericsson.android.camera.view.overlaycontrol.imagequality.ImageQualityControlView;
import com.sonyericsson.android.camera.view.overlaycontrol.imagequality.ImageQualityWidgetFactory;
import com.sonyericsson.android.camera.view.overlaycontrol.imagequality.OnSlideListener;
import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;
import com.sonyericsson.android.camera.view.setting.executor.SettingExecutorInterface;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItemBuilder;
import com.sonyericsson.android.camera.view.setting.settingitem.TypedSettingItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ImageQualityControl extends OverlayControl {
    public static final List<UserSettingKey> KEYS = Collections.unmodifiableList(Arrays.asList(UserSettingKey.WHITE_BALANCE, UserSettingKey.EV, UserSettingKey.ISO, UserSettingKey.SHUTTER_SPEED, UserSettingKey.FOCUS_RANGE));
    private final EnumValueAccessor<CapturingMode> mCapturingMode;
    private boolean mIsSliderPressed;
    private final OnSlideListener mOnSlideListener;
    private int mOrientation;
    private UserSettingKey mSelectedTab;
    private final UiControlSettings mUiSettings;
    private final Map<UserSettingKey, EnumValueAccessor<? extends UserSettingValue>> mValueAccessor;
    private ImageQualityControlView mView;
    private final ViewFactory mViewFactory;

    private class ViewFactory {
        private static final String VIEW_TAG = "Imagequalitycontrol-view";
        private final Rect mContainerRect;
        private final ViewGroup mParent;
        private final LayoutDependencyResolver.ScreenAspect mScreenAspect;

        public ViewFactory(ViewGroup viewGroup, Rect rect, LayoutDependencyResolver.ScreenAspect screenAspect) {
            this.mParent = viewGroup;
            this.mContainerRect = rect;
            this.mScreenAspect = screenAspect;
        }

        public ImageQualityControlView create() {
            View viewFindViewWithTag = this.mParent.findViewWithTag(VIEW_TAG);
            if (viewFindViewWithTag != null) {
                if (ImageQualityControl.this.mIsSliderPressed) {
                    ImageQualityControl.this.mOnSlideListener.onSlideStopped();
                }
                this.mParent.removeView(viewFindViewWithTag);
            }
            ImageQualityControlView imageQualityControlViewCreate = ImageQualityControlView.create(this.mParent, this.mContainerRect, this.mScreenAspect);
            imageQualityControlViewCreate.setTag(VIEW_TAG);
            imageQualityControlViewCreate.setAdapter(ImageQualityControl.this.createAdapter(this.mParent.getContext()));
            imageQualityControlViewCreate.setOnImageQualityControlDialogTabSelectListener(new ImageQualityControlView.OnImageQualityControlDialogTabSelectListener() { // from class: com.sonyericsson.android.camera.view.overlaycontrol.ImageQualityControl.ViewFactory.1
                @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.ImageQualityControlView.OnImageQualityControlDialogTabSelectListener
                public void onSelect(UserSettingKey userSettingKey) {
                    if (ImageQualityControl.this.mIsSliderPressed) {
                        return;
                    }
                    ImageQualityControl.this.update(userSettingKey);
                }
            });
            return imageQualityControlViewCreate;
        }
    }

    public ImageQualityControl(ViewGroup viewGroup, UiControlSettings uiControlSettings, Rect rect, LayoutDependencyResolver.ScreenAspect screenAspect, OverlayControl.StateListener stateListener, EnumValueAccessor<CapturingMode> enumValueAccessor, EnumValueAccessor<FocusRange> enumValueAccessor2, EnumValueAccessor<ShutterSpeed> enumValueAccessor3, EnumValueAccessor<Iso> enumValueAccessor4, EnumValueAccessor<Ev> enumValueAccessor5, EnumValueAccessor<WhiteBalance> enumValueAccessor6) {
        super(stateListener);
        this.mValueAccessor = new HashMap();
        this.mIsSliderPressed = false;
        this.mOnSlideListener = new OnSlideListener() { // from class: com.sonyericsson.android.camera.view.overlaycontrol.ImageQualityControl.1
            @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.OnSlideListener
            public void onSlideStarted() {
                ImageQualityControl.this.mIsSliderPressed = true;
                ImageQualityControl.this.notifyValueUpdateStart();
            }

            @Override // com.sonyericsson.android.camera.view.overlaycontrol.imagequality.OnSlideListener
            public void onSlideStopped() {
                ImageQualityControl.this.mIsSliderPressed = false;
                ImageQualityControl.this.notifyValueUpdateEnd();
            }
        };
        this.mOrientation = 2;
        this.mUiSettings = uiControlSettings;
        this.mCapturingMode = enumValueAccessor;
        this.mValueAccessor.put(UserSettingKey.FOCUS_RANGE, enumValueAccessor2);
        this.mValueAccessor.put(UserSettingKey.SHUTTER_SPEED, enumValueAccessor3);
        this.mValueAccessor.put(UserSettingKey.ISO, enumValueAccessor4);
        this.mValueAccessor.put(UserSettingKey.EV, enumValueAccessor5);
        this.mValueAccessor.put(UserSettingKey.WHITE_BALANCE, enumValueAccessor6);
        this.mViewFactory = new ViewFactory(viewGroup, rect, screenAspect);
        this.mView = this.mViewFactory.create();
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl
    public void enable() {
        this.mSelectedTab = this.mUiSettings.getLastImageQualityControlTab(this.mCapturingMode.get().isFront());
        super.enable();
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl
    public void disable() {
        this.mUiSettings.save();
        super.disable();
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl
    public void refresh() {
        onVisibilityUpdated();
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl
    public void release() {
        ((ViewGroup) this.mView.getParent()).removeView(this.mView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void update(UserSettingKey userSettingKey) {
        this.mSelectedTab = userSettingKey;
        if (!this.mView.update(this.mSelectedTab, this.mValueAccessor)) {
            if (CamLog.DEBUG) {
                CamLog.d("Re-create view because setting structure is modified.");
            }
            this.mView = this.mViewFactory.create();
            if (!this.mView.update(this.mSelectedTab, this.mValueAccessor)) {
                CamLog.i("Fail to fetch setting structure.");
            }
            this.mView.setSensorOrientation(this.mOrientation);
        }
        this.mUiSettings.setLastImageQualityControlTab(this.mSelectedTab, this.mCapturingMode.get().isFront());
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl
    protected void onVisibilityUpdated() {
        if (isVisible()) {
            update(this.mSelectedTab);
            this.mView.setVisibility(0);
        } else {
            this.mView.setVisibility(8);
        }
    }

    @Override // com.sonyericsson.android.camera.view.overlaycontrol.OverlayControl
    protected void onOrientationChanged(int i) {
        this.mOrientation = i;
        this.mView.setSensorOrientation(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SettingAdapter createAdapter(Context context) {
        SettingAdapter settingAdapter = new SettingAdapter(context, new ImageQualityWidgetFactory(this.mOnSlideListener, ((CameraActivity) context).getCameraDevice().getCameraId()), false);
        for (UserSettingKey userSettingKey : KEYS) {
            UserSettingValue[] userSettingValueArr = (UserSettingValue[]) this.mValueAccessor.get(userSettingKey).values();
            boolean z = true;
            if (userSettingKey == UserSettingKey.ISO && userSettingValueArr.length <= 1) {
                z = false;
            }
            if (userSettingKey.isSelectable() && z) {
                SettingItemBuilder settingItemBuilderSelectability = SettingItemBuilder.build(userSettingKey).textId(userSettingKey.getTitleTextId()).additionalTextForAccessibility("").selectability(SettingItem.Selectability.SELECTABLE);
                Iterator<SettingItem> it = generateValueItems(userSettingKey).iterator();
                while (it.hasNext()) {
                    settingItemBuilderSelectability.item(it.next());
                }
                settingAdapter.add(settingItemBuilderSelectability.commit());
            }
        }
        return settingAdapter;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyValue(UserSettingValue userSettingValue) {
        this.mValueAccessor.get(userSettingValue.getKey()).set(userSettingValue);
    }

    private List<SettingItem> generateValueItems(UserSettingKey userSettingKey) {
        ArrayList arrayList = new ArrayList();
        if (this.mValueAccessor.get(userSettingKey) == null) {
            return arrayList;
        }
        UserSettingValue[] userSettingValueArr = (UserSettingValue[]) this.mValueAccessor.get(userSettingKey).values();
        UserSettingValue userSettingValue = this.mValueAccessor.get(userSettingKey).get();
        int length = userSettingValueArr.length;
        for (int i = 0; i < length; i++) {
            UserSettingValue userSettingValue2 = userSettingValueArr[i];
            if (userSettingValue2 != null) {
                boolean z = userSettingValue == userSettingValue2;
                SettingItem.Selectability selectability = SettingItem.Selectability.SELECTABLE;
                if (!userSettingKey.isSelectable()) {
                    selectability = SettingItem.Selectability.UNSELECTABLE;
                }
                arrayList.add(SettingItemBuilder.build(userSettingValue2).iconId(userSettingValue2.getIconId()).textId(userSettingValue2.getTextId()).executor(new SettingExecutorInterface<UserSettingValue>() { // from class: com.sonyericsson.android.camera.view.overlaycontrol.ImageQualityControl.2
                    @Override // com.sonyericsson.android.camera.view.setting.executor.SettingExecutorInterface
                    public void onExecute(TypedSettingItem<UserSettingValue> typedSettingItem) {
                        ImageQualityControl.this.applyValue(typedSettingItem.getData());
                        ImageQualityControl.this.update(ImageQualityControl.this.mSelectedTab);
                    }
                }).selected(z).selectability(selectability).commit());
            }
        }
        return arrayList;
    }
}
