// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png;

enum FilterType
{
    private static final FilterType[] $VALUES;
    
    AVERAGE, 
    NONE, 
    PAETH, 
    SUB, 
    UP;
    
    static {
        $VALUES = new FilterType[] { FilterType.NONE, FilterType.SUB, FilterType.UP, FilterType.AVERAGE, FilterType.PAETH };
    }
}
