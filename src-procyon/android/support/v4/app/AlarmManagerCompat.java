// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.app;

import android.app.AlarmManager$AlarmClockInfo;
import android.os.Build$VERSION;
import android.app.PendingIntent;
import android.support.annotation.NonNull;
import android.app.AlarmManager;

public final class AlarmManagerCompat
{
    private AlarmManagerCompat() {
    }
    
    public static void setAlarmClock(@NonNull final AlarmManager alarmManager, final long n, @NonNull final PendingIntent pendingIntent, @NonNull final PendingIntent pendingIntent2) {
        if (Build$VERSION.SDK_INT >= 21) {
            alarmManager.setAlarmClock(new AlarmManager$AlarmClockInfo(n, pendingIntent), pendingIntent2);
        }
        else {
            setExact(alarmManager, 0, n, pendingIntent2);
        }
    }
    
    public static void setAndAllowWhileIdle(@NonNull final AlarmManager alarmManager, final int n, final long n2, @NonNull final PendingIntent pendingIntent) {
        if (Build$VERSION.SDK_INT >= 23) {
            alarmManager.setAndAllowWhileIdle(n, n2, pendingIntent);
        }
        else {
            alarmManager.set(n, n2, pendingIntent);
        }
    }
    
    public static void setExact(@NonNull final AlarmManager alarmManager, final int n, final long n2, @NonNull final PendingIntent pendingIntent) {
        if (Build$VERSION.SDK_INT >= 19) {
            alarmManager.setExact(n, n2, pendingIntent);
        }
        else {
            alarmManager.set(n, n2, pendingIntent);
        }
    }
    
    public static void setExactAndAllowWhileIdle(@NonNull final AlarmManager alarmManager, final int n, final long n2, @NonNull final PendingIntent pendingIntent) {
        if (Build$VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(n, n2, pendingIntent);
        }
        else {
            setExact(alarmManager, n, n2, pendingIntent);
        }
    }
}
