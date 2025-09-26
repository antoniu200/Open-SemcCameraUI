// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.debug;

import android.view.InputDevice$MotionRange;
import android.app.Activity;
import kotlin.TypeCastException;
import com.sonyericsson.android.camera.util.CamLog;
import android.view.MotionEvent;
import kotlin.jvm.internal.Intrinsics;
import android.content.Context;
import org.jetbrains.annotations.NotNull;
import com.sonyericsson.android.camera.SideTouchEventDetector;
import kotlin.Metadata;
import android.widget.FrameLayout;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\r" }, d2 = { "Lcom/sonyericsson/android/camera/debug/SideTouchEmulateView;", "Landroid/widget/FrameLayout;", "context", "Landroid/content/Context;", "area", "Lcom/sonyericsson/android/camera/SideTouchEventDetector$SideTouchArea;", "(Landroid/content/Context;Lcom/sonyericsson/android/camera/SideTouchEventDetector$SideTouchArea;)V", "getArea", "()Lcom/sonyericsson/android/camera/SideTouchEventDetector$SideTouchArea;", "onTouchEvent", "", "event", "Landroid/view/MotionEvent;", "SemcCameraUI_release" }, k = 1, mv = { 1, 1, 11 })
final class SideTouchEmulateView extends FrameLayout
{
    @NotNull
    private final SideTouchEventDetector.SideTouchArea area;
    
    public SideTouchEmulateView(@NotNull final Context context, @NotNull final SideTouchEventDetector.SideTouchArea area) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(area, "area");
        super(context);
        this.area = area;
    }
    
    @NotNull
    public final SideTouchEventDetector.SideTouchArea getArea() {
        return this.area;
    }
    
    public boolean onTouchEvent(@NotNull final MotionEvent motionEvent) {
        Intrinsics.checkParameterIsNotNull(motionEvent, "event");
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("SideTouchView x:");
            sb.append(motionEvent.getX());
            sb.append(" y:");
            sb.append(motionEvent.getY());
            CamLog.d(sb.toString());
        }
        final MotionEvent obtain = MotionEvent.obtain(motionEvent);
        float n = 0.0f;
        switch (SideTouchEmulateView$WhenMappings.$EnumSwitchMapping$0[this.area.ordinal()]) {
            default: {
                n = obtain.getY();
                break;
            }
            case 2: {
                final InputDevice$MotionRange motionRange = motionEvent.getDevice().getMotionRange(0);
                Intrinsics.checkExpressionValueIsNotNull(motionRange, "event.device.getMotionRange(MotionEvent.AXIS_X)");
                n = motionRange.getMin();
                break;
            }
            case 1: {
                final InputDevice$MotionRange motionRange2 = motionEvent.getDevice().getMotionRange(0);
                Intrinsics.checkExpressionValueIsNotNull(motionRange2, "event.device.getMotionRange(MotionEvent.AXIS_X)");
                n = motionRange2.getMax();
                break;
            }
        }
        obtain.setLocation(n, obtain.getX());
        obtain.setSource(536870912);
        final Context context = this.getContext();
        if (context == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.app.Activity");
        }
        final boolean onGenericMotionEvent = ((Activity)context).onGenericMotionEvent(obtain);
        obtain.recycle();
        return onGenericMotionEvent;
    }
}
