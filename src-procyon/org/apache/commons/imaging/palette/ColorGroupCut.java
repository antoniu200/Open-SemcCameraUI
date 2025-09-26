// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.palette;

class ColorGroupCut
{
    public final ColorGroup less;
    public final int limit;
    public final ColorComponent mode;
    public final ColorGroup more;
    
    public ColorGroupCut(final ColorGroup less, final ColorGroup more, final ColorComponent mode, final int limit) {
        this.less = less;
        this.more = more;
        this.mode = mode;
        this.limit = limit;
    }
    
    public ColorGroup getColorGroup(final int n) {
        if (this.mode.argbComponent(n) <= this.limit) {
            return this.less;
        }
        return this.more;
    }
}
