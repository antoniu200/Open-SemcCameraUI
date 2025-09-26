// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.widget;

import android.view.View;
import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import android.widget.ListView;

public final class ListViewCompat
{
    private ListViewCompat() {
    }
    
    public static boolean canScrollList(@NonNull final ListView listView, int n) {
        if (Build$VERSION.SDK_INT >= 19) {
            return listView.canScrollList(n);
        }
        final int childCount = listView.getChildCount();
        final boolean b = false;
        boolean b2 = false;
        if (childCount == 0) {
            return false;
        }
        final int firstVisiblePosition = listView.getFirstVisiblePosition();
        if (n > 0) {
            n = listView.getChildAt(childCount - 1).getBottom();
            if (firstVisiblePosition + childCount < listView.getCount() || n > listView.getHeight() - listView.getListPaddingBottom()) {
                b2 = true;
            }
            return b2;
        }
        n = listView.getChildAt(0).getTop();
        if (firstVisiblePosition <= 0) {
            final boolean b3 = b;
            if (n >= listView.getListPaddingTop()) {
                return b3;
            }
        }
        return true;
    }
    
    public static void scrollListBy(@NonNull final ListView listView, final int n) {
        if (Build$VERSION.SDK_INT >= 19) {
            listView.scrollListBy(n);
        }
        else {
            final int firstVisiblePosition = listView.getFirstVisiblePosition();
            if (firstVisiblePosition == -1) {
                return;
            }
            final View child = listView.getChildAt(0);
            if (child == null) {
                return;
            }
            listView.setSelectionFromTop(firstVisiblePosition, child.getTop() - n);
        }
    }
}
