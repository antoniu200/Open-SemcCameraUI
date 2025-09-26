// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.setting.settingitem;

import android.content.res.Resources;
import java.util.List;

public interface SettingItem
{
    boolean compareData(final SettingItem p0);
    
    boolean compareData(final Object p0);
    
    List<SettingItem> getChildren();
    
    String getContentDescription(final Resources p0);
    
    int getDialogItemType();
    
    int getIconId();
    
    Selectability getSelectability();
    
    String getSubText(final Resources p0);
    
    String getText(final Resources p0);
    
    boolean isSelectable();
    
    boolean isSelected();
    
    boolean isSoundEnabled();
    
    void select();
    
    void setSelectability(final Selectability p0);
    
    void setSelected(final boolean p0);
    
    public enum Selectability
    {
        private static final Selectability[] $VALUES;
        
        RESTRICTED, 
        SELECTABLE, 
        UNSELECTABLE;
        
        static {
            $VALUES = new Selectability[] { Selectability.SELECTABLE, Selectability.UNSELECTABLE, Selectability.RESTRICTED };
        }
    }
}
