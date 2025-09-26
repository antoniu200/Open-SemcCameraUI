// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.gif;

class GraphicControlExtension extends GifBlock
{
    public final int delay;
    public final int dispose;
    public final int packed;
    public final boolean transparency;
    public final int transparentColorIndex;
    
    public GraphicControlExtension(final int n, final int packed, final int dispose, final boolean transparency, final int delay, final int transparentColorIndex) {
        super(n);
        this.packed = packed;
        this.dispose = dispose;
        this.transparency = transparency;
        this.delay = delay;
        this.transparentColorIndex = transparentColorIndex;
    }
}
