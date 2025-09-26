// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.imaging.formats.jpeg.iptc;

import java.util.HashMap;
import java.util.Map;

public final class IptcTypeLookup
{
    private static final Map<Integer, IptcType> IPTC_TYPE_MAP;
    
    static {
        IPTC_TYPE_MAP = new HashMap<Integer, IptcType>();
        for (final IptcTypes iptcTypes : IptcTypes.values()) {
            IptcTypeLookup.IPTC_TYPE_MAP.put(iptcTypes.getType(), iptcTypes);
        }
    }
    
    private IptcTypeLookup() {
    }
    
    public static IptcType getIptcType(final int n) {
        if (!IptcTypeLookup.IPTC_TYPE_MAP.containsKey(n)) {
            return IptcTypes.getUnknown(n);
        }
        return IptcTypeLookup.IPTC_TYPE_MAP.get(n);
    }
}
