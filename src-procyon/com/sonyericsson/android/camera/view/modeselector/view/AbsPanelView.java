// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector.view;

import android.widget.ImageView;
import android.util.AttributeSet;
import android.content.Context;
import com.sonymobile.cameracommon.font.FontUtil;
import android.graphics.Typeface;
import android.widget.FrameLayout;

public abstract class AbsPanelView extends FrameLayout
{
    public static final Typeface REGULAR_ROBOTO;
    
    static {
        REGULAR_ROBOTO = FontUtil.createTypeface(FontUtil.RobotoFontType.REGULAR);
    }
    
    public AbsPanelView(final Context context) {
        super(context);
    }
    
    public AbsPanelView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public int getAppIconHeight() {
        return 0;
    }
    
    public ImageView getAppIconView() {
        return null;
    }
    
    public int getAppIconWidth() {
        return 0;
    }
    
    public void setItem(final PanelAttributes panelAttributes) {
    }
    
    public void setUiOrientation(final int n) {
    }
    
    public interface PanelAttributes
    {
        String getIconUri();
        
        String getTitle();
    }
}
