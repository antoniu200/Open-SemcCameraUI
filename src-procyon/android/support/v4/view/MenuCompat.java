// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.view;

import android.view.MenuItem;
import android.annotation.SuppressLint;
import android.os.Build$VERSION;
import android.support.v4.internal.view.SupportMenu;
import android.view.Menu;

public final class MenuCompat
{
    private MenuCompat() {
    }
    
    @SuppressLint({ "NewApi" })
    public static void setGroupDividerEnabled(final Menu menu, final boolean b) {
        if (menu instanceof SupportMenu) {
            ((SupportMenu)menu).setGroupDividerEnabled(b);
        }
        else if (Build$VERSION.SDK_INT >= 28) {
            menu.setGroupDividerEnabled(b);
        }
    }
    
    @Deprecated
    public static void setShowAsAction(final MenuItem menuItem, final int showAsAction) {
        menuItem.setShowAsAction(showAsAction);
    }
}
