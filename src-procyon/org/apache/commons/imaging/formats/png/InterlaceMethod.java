// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.png;

public enum InterlaceMethod
{
    private static final InterlaceMethod[] $VALUES;
    
    ADAM7(true), 
    NONE(false);
    
    private final boolean progressive;
    
    static {
        $VALUES = new InterlaceMethod[] { InterlaceMethod.NONE, InterlaceMethod.ADAM7 };
    }
    
    private InterlaceMethod(final boolean progressive) {
        this.progressive = progressive;
    }
    
    public boolean isProgressive() {
        return this.progressive;
    }
}
