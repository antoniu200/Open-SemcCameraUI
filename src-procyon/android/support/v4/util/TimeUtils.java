// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.util;

import java.io.PrintWriter;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public final class TimeUtils
{
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static final int HUNDRED_DAY_FIELD_LEN = 19;
    private static final int SECONDS_PER_DAY = 86400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;
    private static char[] sFormatStr;
    private static final Object sFormatSync;
    
    static {
        sFormatSync = new Object();
        TimeUtils.sFormatStr = new char[24];
    }
    
    private TimeUtils() {
    }
    
    private static int accumField(final int n, final int n2, final boolean b, final int n3) {
        if (n > 99 || (b && n3 >= 3)) {
            return 3 + n2;
        }
        if (n > 9 || (b && n3 >= 2)) {
            return 2 + n2;
        }
        if (!b && n <= 0) {
            return 0;
        }
        return 1 + n2;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static void formatDuration(final long n, final long n2, final PrintWriter printWriter) {
        if (n == 0L) {
            printWriter.print("--");
            return;
        }
        formatDuration(n - n2, printWriter, 0);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static void formatDuration(final long n, final PrintWriter printWriter) {
        formatDuration(n, printWriter, 0);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static void formatDuration(final long n, final PrintWriter printWriter, int formatDurationLocked) {
        synchronized (TimeUtils.sFormatSync) {
            formatDurationLocked = formatDurationLocked(n, formatDurationLocked);
            printWriter.print(new String(TimeUtils.sFormatStr, 0, formatDurationLocked));
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static void formatDuration(final long n, final StringBuilder sb) {
        synchronized (TimeUtils.sFormatSync) {
            sb.append(TimeUtils.sFormatStr, 0, formatDurationLocked(n, 0));
        }
    }
    
    private static int formatDurationLocked(long n, int printField) {
        if (TimeUtils.sFormatStr.length < printField) {
            TimeUtils.sFormatStr = new char[printField];
        }
        final char[] sFormatStr = TimeUtils.sFormatStr;
        final long n2 = lcmp(n, 0L);
        if (n2 == 0) {
            while (printField - 1 > 0) {
                sFormatStr[0] = ' ';
            }
            sFormatStr[0] = '0';
            return 1;
        }
        char c;
        if (n2 > 0) {
            c = '+';
        }
        else {
            c = '-';
            n = -n;
        }
        final int n3 = (int)(n % 1000L);
        int n4 = (int)Math.floor((double)(n / 1000L));
        int n5;
        if (n4 > 86400) {
            n5 = n4 / 86400;
            n4 -= 86400 * n5;
        }
        else {
            n5 = 0;
        }
        int n6;
        if (n4 > 3600) {
            n6 = n4 / 3600;
            n4 -= n6 * 3600;
        }
        else {
            n6 = 0;
        }
        int n7;
        int n8;
        if (n4 > 60) {
            n7 = n4 / 60;
            n8 = n4 - n7 * 60;
        }
        else {
            n7 = 0;
            n8 = n4;
        }
        int n15;
        if (printField != 0) {
            final int accumField = accumField(n5, 1, false, 0);
            final int n9 = accumField + accumField(n6, 1, accumField > 0, 2);
            final int n10 = n9 + accumField(n7, 1, n9 > 0, 2);
            final int n11 = n10 + accumField(n8, 1, n10 > 0, 2);
            int n12;
            if (n11 > 0) {
                n12 = 3;
            }
            else {
                n12 = 0;
            }
            int n13 = n11 + (accumField(n3, 2, true, n12) + 1);
            int n14 = 0;
            while (true) {
                n15 = n14;
                if (n13 >= printField) {
                    break;
                }
                sFormatStr[n14] = ' ';
                ++n14;
                ++n13;
            }
        }
        else {
            n15 = 0;
        }
        sFormatStr[n15] = c;
        final int n16 = n15 + 1;
        if (printField != 0) {
            printField = 1;
        }
        else {
            printField = 0;
        }
        final int printField2 = printField(sFormatStr, n5, 'd', n16, false, 0);
        final boolean b = printField2 != n16;
        int n17;
        if (printField != 0) {
            n17 = 2;
        }
        else {
            n17 = 0;
        }
        final int printField3 = printField(sFormatStr, n6, 'h', printField2, b, n17);
        final boolean b2 = printField3 != n16;
        int n18;
        if (printField != 0) {
            n18 = 2;
        }
        else {
            n18 = 0;
        }
        final int printField4 = printField(sFormatStr, n7, 'm', printField3, b2, n18);
        final boolean b3 = printField4 != n16;
        int n19;
        if (printField != 0) {
            n19 = 2;
        }
        else {
            n19 = 0;
        }
        final int printField5 = printField(sFormatStr, n8, 's', printField4, b3, n19);
        if (printField != 0 && printField5 != n16) {
            printField = 3;
        }
        else {
            printField = 0;
        }
        printField = printField(sFormatStr, n3, 'm', printField5, true, printField);
        sFormatStr[printField] = 's';
        return printField + 1;
    }
    
    private static int printField(final char[] array, int n, final char c, int n2, final boolean b, int n3) {
        if (!b) {
            final int n4 = n2;
            if (n <= 0) {
                return n4;
            }
        }
        int n6;
        if ((b && n3 >= 3) || n > 99) {
            final int n5 = n / 100;
            array[n2] = (char)(n5 + 48);
            n6 = n2 + 1;
            n -= n5 * 100;
        }
        else {
            n6 = n2;
        }
        int n7 = 0;
        Label_0121: {
            if ((!b || n3 < 2) && n <= 9) {
                n7 = n6;
                n3 = n;
                if (n2 == n6) {
                    break Label_0121;
                }
            }
            n2 = n / 10;
            array[n6] = (char)(n2 + 48);
            n7 = n6 + 1;
            n3 = n - n2 * 10;
        }
        array[n7] = (char)(n3 + 48);
        n = n7 + 1;
        array[n] = c;
        return n + 1;
    }
}
