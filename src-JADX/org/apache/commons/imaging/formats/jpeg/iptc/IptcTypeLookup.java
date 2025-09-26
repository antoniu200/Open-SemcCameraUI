package org.apache.commons.imaging.formats.jpeg.iptc;

import java.util.HashMap;
import java.util.Map;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class IptcTypeLookup {
    private static final Map<Integer, IptcType> IPTC_TYPE_MAP = new HashMap();

    static {
        for (IptcTypes iptcTypes : IptcTypes.values()) {
            IPTC_TYPE_MAP.put(Integer.valueOf(iptcTypes.getType()), iptcTypes);
        }
    }

    private IptcTypeLookup() {
    }

    public static IptcType getIptcType(int i) {
        if (!IPTC_TYPE_MAP.containsKey(Integer.valueOf(i))) {
            return IptcTypes.getUnknown(i);
        }
        return IPTC_TYPE_MAP.get(Integer.valueOf(i));
    }
}
