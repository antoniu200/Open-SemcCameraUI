package com.sonyericsson.android.camera.debug;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.sonyericsson.android.camera.SideTouchEventDetector;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonymobile.sidetouchgesturedetector.SideTouchUtils;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* compiled from: SideTouchEmulateViewFactory.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\r"}, d2 = {"Lcom/sonyericsson/android/camera/debug/SideTouchEmulateView;", "Landroid/widget/FrameLayout;", "context", "Landroid/content/Context;", "area", "Lcom/sonyericsson/android/camera/SideTouchEventDetector$SideTouchArea;", "(Landroid/content/Context;Lcom/sonyericsson/android/camera/SideTouchEventDetector$SideTouchArea;)V", "getArea", "()Lcom/sonyericsson/android/camera/SideTouchEventDetector$SideTouchArea;", "onTouchEvent", "", NotificationCompat.CATEGORY_EVENT, "Landroid/view/MotionEvent;", "SemcCameraUI_release"}, k = 1, mv = {1, 1, 11})
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
final class SideTouchEmulateView extends FrameLayout {

    @NotNull
    private final SideTouchEventDetector.SideTouchArea area;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SideTouchEmulateView(@NotNull Context context, @NotNull SideTouchEventDetector.SideTouchArea area) {
        super(context);
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(area, "area");
        this.area = area;
    }

    @NotNull
    public final SideTouchEventDetector.SideTouchArea getArea() {
        return this.area;
    }

    @Override // android.view.View
    public boolean onTouchEvent(@NotNull MotionEvent event) {
        float max;
        Intrinsics.checkParameterIsNotNull(event, "event");
        if (CamLog.DEBUG) {
            CamLog.d("SideTouchView x:" + event.getX() + " y:" + event.getY());
        }
        MotionEvent motionEventObtain = MotionEvent.obtain(event);
        switch (this.area) {
            case RIGHT:
                InputDevice.MotionRange motionRange = event.getDevice().getMotionRange(0);
                Intrinsics.checkExpressionValueIsNotNull(motionRange, "event.device.getMotionRange(MotionEvent.AXIS_X)");
                max = motionRange.getMax();
                break;
            case LEFT:
                InputDevice.MotionRange motionRange2 = event.getDevice().getMotionRange(0);
                Intrinsics.checkExpressionValueIsNotNull(motionRange2, "event.device.getMotionRange(MotionEvent.AXIS_X)");
                max = motionRange2.getMin();
                break;
            default:
                max = motionEventObtain.getY();
                break;
        }
        motionEventObtain.setLocation(max, motionEventObtain.getX());
        motionEventObtain.setSource(SideTouchUtils.SOURCE_SIDETOUCH);
        Context context = getContext();
        if (context == null) {
            throw new TypeCastException("null cannot be cast to non-null type android.app.Activity");
        }
        boolean zOnGenericMotionEvent = ((Activity) context).onGenericMotionEvent(motionEventObtain);
        motionEventObtain.recycle();
        return zOnGenericMotionEvent;
    }
}
