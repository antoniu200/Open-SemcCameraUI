package com.sonyericsson.android.camera.view.selectabledialog;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView;
import com.sonyericsson.android.camera.view.selectabledialog.AbsSelectableDialog;
import com.sonyericsson.android.camera.view.selectabledialog.ScrollContainer;
import com.sonyericsson.android.camera.view.setting.dialogitem.SettingDialogItem;
import com.sonyericsson.android.camera.view.setting.settingitem.SettingItem;
import com.sonyericsson.cameracommon.utility.LayoutOrientationResolver;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import com.sonyericsson.cameracommon.widget.CategorySwitch;
import java.util.Iterator;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SettingMenuView extends AbsDialogScrollView {
    private final boolean mIsSettingMenu;
    private final AbsSelectableDialog.SelectableDialogType mMenuDialogType;

    public SettingMenuView(@NonNull Context context, AbsSelectableDialog.SelectableDialogType selectableDialogType) {
        super(context);
        this.mMenuDialogType = selectableDialogType;
        this.mIsSettingMenu = selectableDialogType == AbsSelectableDialog.SelectableDialogType.SETTING_MENU;
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView
    public void updateDefaultScrollPosition() {
        if (this.mIsSettingMenu) {
            super.updateDefaultScrollPosition();
        }
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView
    protected void addContent(String str, ArrayAdapter arrayAdapter) {
        ListArrange listArrange = new ListArrange(str, arrayAdapter, this.mArranges.isEmpty());
        this.mArranges.add(listArrange);
        addContentView(listArrange.getView());
        listArrange.getView().getLayoutParams().width = -1;
        listArrange.getView().getLayoutParams().height = -2;
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView
    protected int getScrollableContentLength() {
        if (this.mArranges.isEmpty()) {
            return 0;
        }
        int iComputeArrangeHeight = 0;
        for (int i = 0; i < this.mArranges.size(); i++) {
            iComputeArrangeHeight += this.mArranges.get(i).computeArrangeHeight();
        }
        return iComputeArrangeHeight;
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView
    public void selectTagItem(final Object obj) {
        final SettingDialogItem settingDialogItemFindItemWith = findItemWith(obj);
        if (settingDialogItemFindItemWith != null) {
            post(new Runnable() { // from class: com.sonyericsson.android.camera.view.selectabledialog.SettingMenuView.1
                @Override // java.lang.Runnable
                public void run() {
                    Iterator<AbsDialogScrollView.ContentArrange> it = SettingMenuView.this.mArranges.iterator();
                    int iComputeHeight = 0;
                    while (it.hasNext()) {
                        iComputeHeight += it.next().computeHeight();
                    }
                    SettingMenuView.this.scrollTo(0, iComputeHeight);
                    if (obj == UserSettingKey.GEO_TAG) {
                        View childAt = ((ViewGroup) settingDialogItemFindItemWith.getView()).getChildAt(1);
                        if (childAt instanceof CategorySwitch) {
                            childAt.callOnClick();
                            return;
                        }
                        return;
                    }
                    if (obj == UserSettingKey.SIDE_SENSE) {
                        View childAt2 = ((ViewGroup) settingDialogItemFindItemWith.getView()).getChildAt(1);
                        if (childAt2 instanceof CategorySwitch) {
                            childAt2.callOnClick();
                            return;
                        }
                        return;
                    }
                    settingDialogItemFindItemWith.select(settingDialogItemFindItemWith.getItem());
                }
            });
        }
    }

    @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView
    public void updateRotatableLayout(ViewGroup viewGroup, int i, int i2) {
        if (this.mMenuDialogType == AbsSelectableDialog.SelectableDialogType.SETTING_MONOCHROME) {
            adjustMonochromeLayout(i, i2);
            return;
        }
        if (((FrameLayout.LayoutParams) this.mBackground.getLayoutParams()).gravity == 48) {
            adjustRotatableLayoutGravityTop(viewGroup, i, i2);
        } else {
            adjustRotatableLayoutGravityBottom(viewGroup, i, i2);
        }
        setPivotX(getLayoutParams().width / 2.0f);
        setPivotY(getLayoutParams().height - (getLayoutParams().width / 2.0f));
        setRotation(RotationUtil.getAngle(this.mOrientation));
        requestLayout();
    }

    private void adjustRotatableLayoutGravityBottom(ViewGroup viewGroup, int i, int i2) {
        if (this.mIsSettingMenu) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mSomcScroller.getLayoutParams();
            if (isPortrait()) {
                layoutParams.height = this.mParams.maxHeightPortrait;
            } else {
                layoutParams.height = this.mParams.maxHeightLandscape;
            }
            this.mSomcScroller.setOrientation(this.mOrientation);
        }
        int i3 = getLayoutParams().height;
        int i4 = getLayoutParams().width;
        int i5 = i3 - (isPortrait() ? this.mParams.maxHeightPortrait : this.mParams.maxHeightLandscape);
        setPadding(0, i5, 0, Math.max(0, Math.min(this.mBottomMarginHint, i3 - (i5 + i2))));
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) getLayoutParams();
        if (isPortrait()) {
            layoutParams2.setMargins(this.mParams.leftMarginPortrait, 0, this.mParams.rightMarginPortrait, this.mParams.bottomMarginPortrait);
            if (this.mIsSettingMenu && this.mSomcScroller.getCurrentStatus() != ScrollContainer.Status.FULLSCREEN) {
                this.mSomcScroller.setViewMargin(this.mParams.maxHeightPortrait - getInitialDisplayHeight());
            }
        } else {
            layoutParams2.setMargins(this.mParams.leftMarginLandscape, 0, (viewGroup.getMeasuredWidth() - viewGroup.getMeasuredHeight()) / 2, this.mParams.bottomMarginLandscape);
            if (this.mIsSettingMenu && this.mSomcScroller.getCurrentStatus() != ScrollContainer.Status.FULLSCREEN) {
                this.mSomcScroller.setViewMargin(this.mParams.maxHeightLandscape - getInitialDisplayHeight());
            }
        }
        if (AnonymousClass2.$SwitchMap$com$sonyericsson$android$camera$view$selectabledialog$AbsSelectableDialog$HorizontalGravity[this.mParams.horizontalGavity.ordinal()] != 1) {
            return;
        }
        if (isPortrait()) {
            setTranslationX(i2 - i4);
        } else {
            setTranslationX(0.0f);
        }
    }

    /* renamed from: com.sonyericsson.android.camera.view.selectabledialog.SettingMenuView$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$sonyericsson$android$camera$view$selectabledialog$AbsSelectableDialog$HorizontalGravity = new int[AbsSelectableDialog.HorizontalGravity.values().length];

        static {
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$selectabledialog$AbsSelectableDialog$HorizontalGravity[AbsSelectableDialog.HorizontalGravity.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    private void adjustRotatableLayoutGravityTop(ViewGroup viewGroup, int i, int i2) {
        int i3;
        Rect rect = new Rect();
        if (viewGroup.getGlobalVisibleRect(rect)) {
            if (LayoutOrientationResolver.getInstance().getOrientation() == LayoutOrientationResolver.LayoutOrientationType.PORTRAIT) {
                rect = new Rect(0, 0, rect.height(), rect.width());
            }
            int iDimenToPixel = dimenToPixel(R.dimen.setting_2nd_layer_margin);
            int iDimenToPixel2 = dimenToPixel(R.dimen.setting_2nd_layer_margin);
            int iDimenToPixel3 = dimenToPixel(R.dimen.setting_2nd_layer_margin);
            int iDimenToPixel4 = dimenToPixel(R.dimen.setting_menu_drop_shadow_size);
            int i4 = getLayoutParams().height;
            int i5 = getLayoutParams().width;
            int i6 = isPortrait() ? ((i5 - i) - iDimenToPixel2) + iDimenToPixel4 : i5 - i;
            int i7 = isPortrait() ? this.mParams.maxHeightPortrait : this.mParams.maxHeightLandscape;
            int i8 = isPortrait() ? rect.right : rect.bottom;
            int i9 = isPortrait() ? 0 : i4 - rect.bottom;
            if (this.mIsExpandedWhenOpened) {
                if (this.mTopMarginHint + i2 < (iDimenToPixel4 * 2) + i8) {
                    i3 = ((this.mTopMarginHint - iDimenToPixel4) - iDimenToPixel) + i9;
                } else {
                    int i10 = (-i2) + i8 + i9;
                    i3 = i2 == i7 ? i10 - iDimenToPixel : i10 + iDimenToPixel;
                }
            } else if (this.mTopMarginHint + i2 < rect.right + (iDimenToPixel4 * 2)) {
                i3 = (this.mTopMarginHint - iDimenToPixel4) - iDimenToPixel;
            } else {
                int i11 = (-i2) + rect.right;
                i3 = i2 == i7 ? i11 - iDimenToPixel : i11 + iDimenToPixel;
            }
            setPadding(i6, i3, 0, -iDimenToPixel3);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
            if (!isPortrait()) {
                layoutParams.setMargins(this.mParams.leftMarginLandscape, 0, ((viewGroup.getMeasuredWidth() - viewGroup.getMeasuredHeight()) / 2) - iDimenToPixel2, this.mParams.bottomMarginLandscape);
            } else {
                layoutParams.setMargins(this.mParams.leftMarginLandscape, 0, this.mParams.rightMarginLandscape, this.mParams.bottomMarginLandscape);
            }
        }
    }

    private void adjustMonochromeLayout(int i, int i2) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        float angle = RotationUtil.getAngle(this.mOrientation);
        int navigationBarMargin = LayoutDependencyResolver.getNavigationBarMargin(getContext());
        int i3 = getLayoutParams().height;
        int i4 = getLayoutParams().width;
        int iDimenToPixel = dimenToPixel(R.dimen.mru_button_container_size);
        int iDimenToPixel2 = dimenToPixel(R.dimen.mru_button_container_bottom_margin);
        if (isPortrait()) {
            if (angle != getRotation()) {
                float f = i4 / 2.0f;
                setPivotX(f);
                setPivotY(i3 - f);
                setRotation(angle);
                if (this.mBottomMarginHint == 0) {
                    layoutParams.leftMargin = ((((i3 - i4) - this.mTopMarginHint) - iDimenToPixel) - navigationBarMargin) - iDimenToPixel2;
                    layoutParams.bottomMargin = iDimenToPixel2;
                } else {
                    int i5 = (this.mTopMarginHint - (i4 - i2)) + iDimenToPixel2;
                    if (i2 + i5 < i4) {
                        layoutParams.leftMargin = i5;
                    } else {
                        layoutParams.leftMargin = ((i3 - i4) - iDimenToPixel2) - navigationBarMargin;
                    }
                    layoutParams.bottomMargin = (i4 - i) / 2;
                }
            }
        } else if (angle != getRotation()) {
            float f2 = i4 / 2.0f;
            setPivotX(f2);
            setPivotY(i3 - f2);
            setRotation(angle);
            if (this.mBottomMarginHint == 0) {
                layoutParams.leftMargin = ((i3 - this.mTopMarginHint) - i) - navigationBarMargin;
                layoutParams.bottomMargin = (iDimenToPixel2 * 2) + iDimenToPixel;
            } else {
                layoutParams.leftMargin = ((i3 - this.mBottomMarginHint) + ((this.mBottomMarginHint - i) / 2)) - navigationBarMargin;
                if (this.mTopMarginHint > i2 + iDimenToPixel2) {
                    layoutParams.bottomMargin = (this.mTopMarginHint - i2) - iDimenToPixel2;
                } else {
                    layoutParams.bottomMargin = iDimenToPixel2;
                }
            }
        } else if (this.mBottomMarginHint == 0) {
            layoutParams.leftMargin = ((i3 - this.mTopMarginHint) - i) - navigationBarMargin;
            layoutParams.bottomMargin = (iDimenToPixel2 * 2) + iDimenToPixel;
        } else {
            layoutParams.leftMargin = (i3 - this.mBottomMarginHint) + (((this.mBottomMarginHint - i) - navigationBarMargin) / 2);
            if (this.mTopMarginHint > i2 + iDimenToPixel2) {
                layoutParams.bottomMargin = (this.mTopMarginHint - i2) - iDimenToPixel2;
            } else {
                layoutParams.bottomMargin = iDimenToPixel2;
            }
        }
        setLayoutParams(layoutParams);
    }

    private SettingDialogItem findItemWith(Object obj) {
        View viewFindItemViewWithTag = findItemViewWithTag(obj);
        if (viewFindItemViewWithTag == null || !(viewFindItemViewWithTag.getTag() instanceof SettingDialogItem)) {
            return null;
        }
        return (SettingDialogItem) viewFindItemViewWithTag.getTag();
    }

    private class ListArrange extends AbsDialogScrollView.ContentArrange {
        private final boolean mIsTop;
        private final String mTitle;
        private final int mTitleLeftPadding;

        protected ListArrange(String str, ArrayAdapter arrayAdapter, boolean z) {
            super(arrayAdapter);
            this.mTitle = str;
            this.mIsTop = z;
            this.mTitleLeftPadding = SettingMenuView.this.dimenToPixel(R.dimen.setting_1st_layer_title_left_padding);
            setup();
            fetchItems();
        }

        @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.ContentArrange
        protected void setup() {
            this.container = new FrameLayout(SettingMenuView.this.getContext());
            if (!this.mIsTop) {
                this.container.setPadding(0, SettingMenuView.this.dimenToPixel(R.dimen.setting_more_separator_height), 0, 0);
            }
            LinearLayout linearLayout = new LinearLayout(SettingMenuView.this.getContext());
            this.container.addView(linearLayout);
            linearLayout.getLayoutParams().width = -1;
            linearLayout.getLayoutParams().height = -2;
            if (SettingMenuView.this.mParams.panelBackgroundColor != 0) {
                linearLayout.setBackgroundColor(SettingMenuView.this.mParams.panelBackgroundColor);
            }
            linearLayout.setOrientation(1);
            if (this.mTitle != null) {
                TextView textView = new TextView(SettingMenuView.this.getContext());
                linearLayout.addView(textView);
                textView.getLayoutParams().width = -1;
                textView.getLayoutParams().height = SettingMenuView.this.dimenToPixel(R.dimen.setting_common_height);
                textView.setText(this.mTitle);
                textView.setContentDescription(this.mTitle);
                textView.setTextColor(SettingMenuView.this.getResources().getColor(R.color.setting_menu_title_color));
                textView.setTextSize(1, 14.0f);
                textView.setGravity(16);
                textView.setPaddingRelative(this.mTitleLeftPadding, 0, 0, 0);
            }
            this.mRowItems = new LinearLayout(SettingMenuView.this.getContext());
            this.mRowItems.setMotionEventSplittingEnabled(false);
            linearLayout.addView(this.mRowItems);
            this.mRowItems.setOrientation(1);
            this.mRowItems.getLayoutParams().width = -1;
            this.mRowItems.getLayoutParams().height = -2;
        }

        @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.ContentArrange
        protected int computeHeight() {
            if (SettingMenuView.this.mIsSettingMenu && !SettingMenuView.this.mIsExpandedWhenOpened) {
                return SettingMenuView.this.getInitialDisplayHeight();
            }
            return computeArrangeHeight();
        }

        @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.ContentArrange
        protected int computeArrangeHeight() {
            return (this.mTitle == null ? 0 : SettingMenuView.this.dimenToPixel(R.dimen.setting_common_height)) + (SettingMenuView.this.mParams.itemHeight * this.mAdapter.getCount()) + (this.mIsTop ? 0 : SettingMenuView.this.dimenToPixel(R.dimen.setting_more_separator_height));
        }

        @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.ContentArrange
        protected View findViewWithTag(Object obj) {
            for (int i = 0; i < this.mRowItems.getChildCount(); i++) {
                View childAt = this.mRowItems.getChildAt(i);
                if ((childAt.getTag() instanceof SettingDialogItem) && ((SettingDialogItem) childAt.getTag()).getItem().compareData(obj)) {
                    return childAt;
                }
            }
            return null;
        }

        @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.ContentArrange
        protected void release() {
            this.mAdapter.unregisterDataSetObserver(this);
            for (int i = 0; i < this.mRowItems.getChildCount(); i++) {
                View childAt = this.mRowItems.getChildAt(i);
                if (childAt.getTag() instanceof SettingDialogItem) {
                    ((SettingDialogItem) childAt.getTag()).setClickable(false);
                }
            }
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            fetchItems();
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            fetchItems();
        }

        @Override // com.sonyericsson.android.camera.view.selectabledialog.AbsDialogScrollView.ContentArrange
        protected int computeScrollOffset() {
            int i = 0;
            int iDimenToPixel = this.mTitle == null ? 0 : SettingMenuView.this.dimenToPixel(R.dimen.setting_common_height);
            int i2 = SettingMenuView.this.mParams.itemHeight;
            int iDimenToPixel2 = SettingMenuView.this.dimenToPixel(R.dimen.setting_common_separator_height);
            int iDimenToPixel3 = SettingMenuView.this.dimenToPixel(R.dimen.setting_more_separator_height);
            int i3 = 0;
            while (i < this.mAdapter.getCount() && !((SettingItem) this.mAdapter.getItem(i)).isSelected()) {
                i3 += i == 0 ? iDimenToPixel + i2 : i2 + iDimenToPixel2;
                i++;
            }
            return i3 + iDimenToPixel3;
        }
    }
}
