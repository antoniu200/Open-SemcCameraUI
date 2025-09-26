// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.debug;

import org.jetbrains.annotations.Nullable;
import android.content.res.Resources;
import android.view.ViewGroup$LayoutParams;
import android.content.Context;
import android.widget.FrameLayout$LayoutParams;
import kotlin.TypeCastException;
import android.graphics.Color;
import com.sonyericsson.android.camera.util.CamLog;
import kotlin.jvm.internal.Intrinsics;
import android.view.View;
import com.sonyericsson.android.camera.SideTouchEventDetector;
import org.jetbrains.annotations.NotNull;
import android.view.ViewGroup;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u000b" }, d2 = { "Lcom/sonyericsson/android/camera/debug/SideTouchEmulateViewFactory;", "", "()V", "TAG", "", "create", "Landroid/view/View;", "parent", "Landroid/view/ViewGroup;", "area", "Lcom/sonyericsson/android/camera/SideTouchEventDetector$SideTouchArea;", "SemcCameraUI_release" }, k = 1, mv = { 1, 1, 11 })
public final class SideTouchEmulateViewFactory
{
    public static final SideTouchEmulateViewFactory INSTANCE;
    private static final String TAG = "SideTouchEmulateView";
    
    static {
        INSTANCE = new SideTouchEmulateViewFactory();
    }
    
    private SideTouchEmulateViewFactory() {
    }
    
    @Nullable
    public final View create(@NotNull final ViewGroup viewGroup, @NotNull final SideTouchEventDetector.SideTouchArea sideTouchArea) {
        Intrinsics.checkParameterIsNotNull(viewGroup, "parent");
        Intrinsics.checkParameterIsNotNull(sideTouchArea, "area");
        final StringBuilder sb = new StringBuilder();
        sb.append("SideTouchEmulateView-");
        sb.append(sideTouchArea.name());
        final String string = sb.toString();
        for (int childCount = viewGroup.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = viewGroup.getChildAt(i);
            Intrinsics.checkExpressionValueIsNotNull(child, "parent.getChildAt(index)");
            if (Intrinsics.areEqual(string, child.getTag())) {
                CamLog.d("Already added");
                return null;
            }
        }
        final Context context = viewGroup.getContext();
        Intrinsics.checkExpressionValueIsNotNull(context, "parent.context");
        final SideTouchEmulateView sideTouchEmulateView = new SideTouchEmulateView(context, sideTouchArea);
        sideTouchEmulateView.setTag((Object)string);
        sideTouchEmulateView.setBackgroundColor(Color.argb(102, 255, 0, 0));
        final View view = (View)sideTouchEmulateView;
        viewGroup.addView(view);
        final ViewGroup$LayoutParams layoutParams = sideTouchEmulateView.getLayoutParams();
        if (layoutParams == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
        }
        final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)layoutParams;
        frameLayout$LayoutParams.width = -1;
        final float n = 20;
        final Resources resources = viewGroup.getResources();
        Intrinsics.checkExpressionValueIsNotNull(resources, "parent.resources");
        frameLayout$LayoutParams.height = (int)(n * resources.getDisplayMetrics().density);
        final int n2 = SideTouchEmulateViewFactory$WhenMappings.$EnumSwitchMapping$0[sideTouchArea.ordinal()];
        int gravity = 48;
        while (true) {
            switch (n2) {
                default: {
                    gravity = gravity;
                }
                case 1: {
                    frameLayout$LayoutParams.gravity = gravity;
                    sideTouchEmulateView.requestLayout();
                    return view;
                }
                case 4: {
                    gravity = 5;
                    continue;
                }
                case 3: {
                    gravity = 3;
                    continue;
                }
                case 2: {
                    gravity = 80;
                    continue;
                }
            }
            break;
        }
    }
}
