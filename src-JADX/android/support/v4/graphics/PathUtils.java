package android.support.v4.graphics;

import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.Collection;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class PathUtils {
    @NonNull
    @RequiresApi(26)
    public static Collection<PathSegment> flatten(@NonNull Path path) {
        return flatten(path, 0.5f);
    }

    @NonNull
    @RequiresApi(26)
    public static Collection<PathSegment> flatten(@NonNull Path path, @FloatRange(from = 0.0d) float f) {
        float[] fArrApproximate = path.approximate(f);
        int length = fArrApproximate.length / 3;
        ArrayList arrayList = new ArrayList(length);
        for (int i = 1; i < length; i++) {
            int i2 = i * 3;
            int i3 = (i - 1) * 3;
            float f2 = fArrApproximate[i2];
            float f3 = fArrApproximate[i2 + 1];
            float f4 = fArrApproximate[i2 + 2];
            float f5 = fArrApproximate[i3];
            float f6 = fArrApproximate[i3 + 1];
            float f7 = fArrApproximate[i3 + 2];
            if (f2 != f5 && (f3 != f6 || f4 != f7)) {
                arrayList.add(new PathSegment(new PointF(f6, f7), f5, new PointF(f3, f4), f2));
            }
        }
        return arrayList;
    }

    private PathUtils() {
    }
}
