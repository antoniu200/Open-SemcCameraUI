// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.iptc;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

public class PhotoshopApp13Data
{
    private final List<IptcBlock> rawBlocks;
    private final List<IptcRecord> records;
    
    public PhotoshopApp13Data(final List<IptcRecord> records, final List<IptcBlock> rawBlocks) {
        this.rawBlocks = rawBlocks;
        this.records = records;
    }
    
    public List<IptcBlock> getNonIptcBlocks() {
        final ArrayList list = new ArrayList();
        for (final IptcBlock iptcBlock : this.rawBlocks) {
            if (!iptcBlock.isIPTCBlock()) {
                list.add(iptcBlock);
            }
        }
        return list;
    }
    
    public List<IptcBlock> getRawBlocks() {
        return new ArrayList<IptcBlock>(this.rawBlocks);
    }
    
    public List<IptcRecord> getRecords() {
        return new ArrayList<IptcRecord>(this.records);
    }
}
