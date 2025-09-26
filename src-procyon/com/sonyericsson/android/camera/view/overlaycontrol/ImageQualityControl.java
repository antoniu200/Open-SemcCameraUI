// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.overlaycontrol;

import android.view.View;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.setting.settingitem.TypedSettingItem;
import com.sonyericsson.android.camera.view.setting.executor.SettingExecutorInterface;
import java.util.ArrayList;
import java.util.Iterator;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItemBuilder;
import com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItemFactory;
import com.sonyericsson.android.camera.view.overlaycontrol.imagequality.ImageQualityWidgetFactory;
import com.sonyericsson.android.camera.CameraActivity;
import com.sonyericsson.android.camera.view.setting.dialog.SettingAdapter;
import android.content.Context;
import java.util.HashMap;
import com.sonyericsson.android.camera.configuration.parameters.WhiteBalance;
import com.sonyericsson.android.camera.configuration.parameters.Ev;
import com.sonyericsson.android.camera.configuration.parameters.Iso;
import com.sonyericsson.android.camera.configuration.parameters.ShutterSpeed;
import com.sonyericsson.android.camera.configuration.parameters.FocusRange;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import android.graphics.Rect;
import android.view.ViewGroup;
import java.util.Collections;
import java.util.Arrays;
import com.sonyericsson.android.camera.view.overlaycontrol.imagequality.ImageQualityControlView;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import java.util.Map;
import com.sonyericsson.android.camera.setting.UiControlSettings;
import com.sonyericsson.android.camera.view.overlaycontrol.imagequality.OnSlideListener;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.List;

public class ImageQualityControl extends OverlayControl
{
    public static final List<UserSettingKey> KEYS;
    private final EnumValueAccessor<CapturingMode> mCapturingMode;
    private boolean mIsSliderPressed;
    private final OnSlideListener mOnSlideListener;
    private int mOrientation;
    private UserSettingKey mSelectedTab;
    private final UiControlSettings mUiSettings;
    private final Map<UserSettingKey, EnumValueAccessor<? extends UserSettingValue>> mValueAccessor;
    private ImageQualityControlView mView;
    private final ViewFactory mViewFactory;
    
    static {
        KEYS = Collections.unmodifiableList((List<? extends UserSettingKey>)Arrays.asList(UserSettingKey.WHITE_BALANCE, UserSettingKey.EV, UserSettingKey.ISO, UserSettingKey.SHUTTER_SPEED, UserSettingKey.FOCUS_RANGE));
    }
    
    public ImageQualityControl(final ViewGroup viewGroup, final UiControlSettings mUiSettings, final Rect rect, final LayoutDependencyResolver.ScreenAspect screenAspect, final StateListener stateListener, final EnumValueAccessor<CapturingMode> mCapturingMode, final EnumValueAccessor<FocusRange> enumValueAccessor, final EnumValueAccessor<ShutterSpeed> enumValueAccessor2, final EnumValueAccessor<Iso> enumValueAccessor3, final EnumValueAccessor<Ev> enumValueAccessor4, final EnumValueAccessor<WhiteBalance> enumValueAccessor5) {
        super(stateListener);
        this.mValueAccessor = new HashMap<UserSettingKey, EnumValueAccessor<? extends UserSettingValue>>();
        this.mIsSliderPressed = false;
        this.mOnSlideListener = new OnSlideListener() {
            final ImageQualityControl this$0;
            
            @Override
            public void onSlideStarted() {
                this.this$0.mIsSliderPressed = true;
                this.this$0.notifyValueUpdateStart();
            }
            
            @Override
            public void onSlideStopped() {
                this.this$0.mIsSliderPressed = false;
                this.this$0.notifyValueUpdateEnd();
            }
        };
        this.mOrientation = 2;
        this.mUiSettings = mUiSettings;
        this.mCapturingMode = mCapturingMode;
        this.mValueAccessor.put(UserSettingKey.FOCUS_RANGE, enumValueAccessor);
        this.mValueAccessor.put(UserSettingKey.SHUTTER_SPEED, enumValueAccessor2);
        this.mValueAccessor.put(UserSettingKey.ISO, enumValueAccessor3);
        this.mValueAccessor.put(UserSettingKey.EV, enumValueAccessor4);
        this.mValueAccessor.put(UserSettingKey.WHITE_BALANCE, enumValueAccessor5);
        this.mViewFactory = new ViewFactory(viewGroup, rect, screenAspect);
        this.mView = this.mViewFactory.create();
    }
    
    private void applyValue(final UserSettingValue userSettingValue) {
        this.mValueAccessor.get(userSettingValue.getKey()).set(userSettingValue);
    }
    
    private SettingAdapter createAdapter(final Context context) {
        final SettingAdapter settingAdapter = new SettingAdapter(context, new ImageQualityWidgetFactory(this.mOnSlideListener, ((CameraActivity)context).getCameraDevice().getCameraId()), false);
        for (final UserSettingKey userSettingKey : ImageQualityControl.KEYS) {
            final UserSettingValue[] array = this.mValueAccessor.get(userSettingKey).values();
            final UserSettingKey iso = UserSettingKey.ISO;
            int n = 1;
            if (userSettingKey == iso) {
                if (array.length > 1) {
                    n = n;
                }
                else {
                    n = 0;
                }
            }
            if (userSettingKey.isSelectable() && n != 0) {
                final SettingItemBuilder<UserSettingKey> selectability = SettingItemBuilder.build(userSettingKey).textId(userSettingKey.getTitleTextId()).additionalTextForAccessibility("").selectability(SettingItem.Selectability.SELECTABLE);
                final Iterator<SettingItem> iterator2 = this.generateValueItems(userSettingKey).iterator();
                while (iterator2.hasNext()) {
                    selectability.item(iterator2.next());
                }
                settingAdapter.add((Object)selectability.commit());
            }
        }
        return settingAdapter;
    }
    
    private List<SettingItem> generateValueItems(final UserSettingKey userSettingKey) {
        final ArrayList list = new ArrayList();
        if (this.mValueAccessor.get(userSettingKey) == null) {
            return list;
        }
        final UserSettingValue[] array = this.mValueAccessor.get(userSettingKey).values();
        final UserSettingValue userSettingValue = this.mValueAccessor.get(userSettingKey).get();
        for (final UserSettingValue userSettingValue2 : array) {
            if (userSettingValue2 != null) {
                final boolean b = userSettingValue == userSettingValue2;
                SettingItem.Selectability selectability = SettingItem.Selectability.SELECTABLE;
                if (!userSettingKey.isSelectable()) {
                    selectability = SettingItem.Selectability.UNSELECTABLE;
                }
                list.add(SettingItemBuilder.build(userSettingValue2).iconId(userSettingValue2.getIconId()).textId(userSettingValue2.getTextId()).executor(new SettingExecutorInterface<UserSettingValue>(this) {
                    final ImageQualityControl this$0;
                    
                    @Override
                    public void onExecute(final TypedSettingItem<UserSettingValue> typedSettingItem) {
                        this.this$0.applyValue(typedSettingItem.getData());
                        this.this$0.update(this.this$0.mSelectedTab);
                    }
                }).selected(b).selectability(selectability).commit());
            }
        }
        return list;
    }
    
    private void update(final UserSettingKey mSelectedTab) {
        this.mSelectedTab = mSelectedTab;
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
    
    @Override
    public void disable() {
        this.mUiSettings.save();
        super.disable();
    }
    
    @Override
    public void enable() {
        this.mSelectedTab = this.mUiSettings.getLastImageQualityControlTab(this.mCapturingMode.get().isFront());
        super.enable();
    }
    
    @Override
    protected void onOrientationChanged(final int n) {
        this.mOrientation = n;
        this.mView.setSensorOrientation(n);
    }
    
    @Override
    protected void onVisibilityUpdated() {
        if (this.isVisible()) {
            this.update(this.mSelectedTab);
            this.mView.setVisibility(0);
        }
        else {
            this.mView.setVisibility(8);
        }
    }
    
    @Override
    public void refresh() {
        this.onVisibilityUpdated();
    }
    
    @Override
    public void release() {
        ((ViewGroup)this.mView.getParent()).removeView((View)this.mView);
    }
    
    private class ViewFactory
    {
        private static final String VIEW_TAG = "Imagequalitycontrol-view";
        private final Rect mContainerRect;
        private final ViewGroup mParent;
        private final LayoutDependencyResolver.ScreenAspect mScreenAspect;
        final ImageQualityControl this$0;
        
        public ViewFactory(final ImageQualityControl this$0, final ViewGroup mParent, final Rect mContainerRect, final LayoutDependencyResolver.ScreenAspect mScreenAspect) {
            this.this$0 = this$0;
            this.mParent = mParent;
            this.mContainerRect = mContainerRect;
            this.mScreenAspect = mScreenAspect;
        }
        
        public ImageQualityControlView create() {
            final View viewWithTag = this.mParent.findViewWithTag((Object)"Imagequalitycontrol-view");
            if (viewWithTag != null) {
                if (this.this$0.mIsSliderPressed) {
                    this.this$0.mOnSlideListener.onSlideStopped();
                }
                this.mParent.removeView(viewWithTag);
            }
            final ImageQualityControlView create = ImageQualityControlView.create(this.mParent, this.mContainerRect, this.mScreenAspect);
            create.setTag((Object)"Imagequalitycontrol-view");
            create.setAdapter(this.this$0.createAdapter(this.mParent.getContext()));
            create.setOnImageQualityControlDialogTabSelectListener((ImageQualityControlView.OnImageQualityControlDialogTabSelectListener)new ImageQualityControlView.OnImageQualityControlDialogTabSelectListener(this) {
                final ViewFactory this$1;
                
                @Override
                public void onSelect(final UserSettingKey userSettingKey) {
                    if (!this.this$1.this$0.mIsSliderPressed) {
                        this.this$1.this$0.update(userSettingKey);
                    }
                }
            });
            return create;
        }
    }
}
