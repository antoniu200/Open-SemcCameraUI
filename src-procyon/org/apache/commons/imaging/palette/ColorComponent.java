// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.palette;

enum ColorComponent
{
    private static final ColorComponent[] $VALUES;
    
    ALPHA(24), 
    BLUE(0), 
    GREEN(8), 
    RED(16);
    
    private final int shift;
    
    static {
        $VALUES = new ColorComponent[] { ColorComponent.ALPHA, ColorComponent.RED, ColorComponent.GREEN, ColorComponent.BLUE };
    }
    
    private ColorComponent(final int shift) {
        this.shift = shift;
    }
    
    public int argbComponent(final int n) {
        return n >> this.shift & 0xFF;
    }
}
