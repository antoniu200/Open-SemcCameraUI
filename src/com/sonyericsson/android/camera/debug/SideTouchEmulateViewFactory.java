package com.sonyericsson.android.camera.debug;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.sonyericsson.android.camera.SideTouchEventDetector;
import com.sonyericsson.android.camera.util.CamLog;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SideTouchEmulateViewFactory.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u000b"}, d2 = {"Lcom/sonyericsson/android/camera/debug/SideTouchEmulateViewFactory;", "", "()V", "TAG", "", "create", "Landroid/view/View;", "parent", "Landroid/view/ViewGroup;", "area", "Lcom/sonyericsson/android/camera/SideTouchEventDetector$SideTouchArea;", "SemcCameraUI_release"}, k = 1, mv = {1, 1, 11})
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class SideTouchEmulateViewFactory {
    public static final SideTouchEmulateViewFactory INSTANCE = new SideTouchEmulateViewFactory();
    private static final String TAG = "SideTouchEmulateView";

    @Metadata(bv = {1, 0, 2}, k = 3, mv = {1, 1, 11})
    public final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0 = new int[SideTouchEventDetector.SideTouchArea.values().length];

        static {
            $EnumSwitchMapping$0[SideTouchEventDetector.SideTouchArea.RIGHT.ordinal()] = 1;
            $EnumSwitchMapping$0[SideTouchEventDetector.SideTouchArea.LEFT.ordinal()] = 2;
            $EnumSwitchMapping$0[SideTouchEventDetector.SideTouchArea.TOP.ordinal()] = 3;
            $EnumSwitchMapping$0[SideTouchEventDetector.SideTouchArea.BOTTOM.ordinal()] = 4;
        }
    }

    private SideTouchEmulateViewFactory() {
    }

    @Nullable
    public final View create(@NotNull ViewGroup parent, @NotNull SideTouchEventDetector.SideTouchArea area) {
        Intrinsics.checkParameterIsNotNull(parent, "parent");
        Intrinsics.checkParameterIsNotNull(area, "area");
        String str = "SideTouchEmulateView-" + area.name();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = parent.getChildAt(i);
            Intrinsics.checkExpressionValueIsNotNull(childAt, "parent.getChildAt(index)");
            if (Intrinsics.areEqual(str, childAt.getTag())) {
                CamLog.d("Already added");
                return null;
            }
        }
        Context context = parent.getContext();
        Intrinsics.checkExpressionValueIsNotNull(context, "parent.context");
        SideTouchEmulateView sideTouchEmulateView = new SideTouchEmulateView(context, area);
        sideTouchEmulateView.setTag(str);
        sideTouchEmulateView.setBackgroundColor(Color.argb(102, 255, 0, 0));
        SideTouchEmulateView sideTouchEmulateView2 = sideTouchEmulateView;
        parent.addView(sideTouchEmulateView2);
        ViewGroup.LayoutParams layoutParams = sideTouchEmulateView.getLayoutParams();
        if (layoutParams == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
        }
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) layoutParams;
        layoutParams2.width = -1;
        Resources resources = parent.getResources();
        Intrinsics.checkExpressionValueIsNotNull(resources, "parent.resources");
        layoutParams2.height = (int) (20 * resources.getDisplayMetrics().density);
        int i2 = WhenMappings.$EnumSwitchMapping$0[area.ordinal()];
        int i3 = 48;
        switch (i2) {
            case 2:
                i3 = 80;
                break;
            case 3:
                i3 = 3;
                break;
            case 4:
                i3 = 5;
                break;
        }
        layoutParams2.gravity = i3;
        sideTouchEmulateView.requestLayout();
        return sideTouchEmulateView2;
    }
}
