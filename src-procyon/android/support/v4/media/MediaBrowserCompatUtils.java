// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.media;

import android.os.Bundle;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class MediaBrowserCompatUtils
{
    private MediaBrowserCompatUtils() {
    }
    
    public static boolean areSameOptions(final Bundle bundle, final Bundle bundle2) {
        final boolean b = true;
        boolean b2 = true;
        final boolean b3 = true;
        if (bundle == bundle2) {
            return true;
        }
        if (bundle == null) {
            return bundle2.getInt("android.media.browse.extra.PAGE", -1) == -1 && bundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1) == -1 && b3;
        }
        if (bundle2 == null) {
            return bundle.getInt("android.media.browse.extra.PAGE", -1) == -1 && bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1) == -1 && b;
        }
        if (bundle.getInt("android.media.browse.extra.PAGE", -1) != bundle2.getInt("android.media.browse.extra.PAGE", -1) || bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1) != bundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1)) {
            b2 = false;
        }
        return b2;
    }
    
    public static boolean hasDuplicatedItems(final Bundle bundle, final Bundle bundle2) {
        int int1;
        if (bundle == null) {
            int1 = -1;
        }
        else {
            int1 = bundle.getInt("android.media.browse.extra.PAGE", -1);
        }
        int int2;
        if (bundle2 == null) {
            int2 = -1;
        }
        else {
            int2 = bundle2.getInt("android.media.browse.extra.PAGE", -1);
        }
        int int3;
        if (bundle == null) {
            int3 = -1;
        }
        else {
            int3 = bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1);
        }
        int int4;
        if (bundle2 == null) {
            int4 = -1;
        }
        else {
            int4 = bundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1);
        }
        final int n = Integer.MAX_VALUE;
        final boolean b = false;
        int n2;
        int n3;
        if (int1 != -1 && int3 != -1) {
            n2 = int1 * int3;
            n3 = int3 + n2 - 1;
        }
        else {
            n3 = Integer.MAX_VALUE;
            n2 = 0;
        }
        int n5;
        int n6;
        if (int2 != -1 && int4 != -1) {
            final int n4 = int4 * int2;
            n5 = int4 + n4 - 1;
            n6 = n4;
        }
        else {
            n6 = 0;
            n5 = n;
        }
        boolean b2 = b;
        if (n3 >= n6) {
            b2 = b;
            if (n5 >= n2) {
                b2 = true;
            }
        }
        return b2;
    }
}
