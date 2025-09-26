// 
// Decompiled by Procyon v0.6.0
// 

package android.arch.lifecycle;

import java.util.HashMap;
import java.util.Map;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class MethodCallsLogger
{
    private Map<String, Integer> mCalledMethods;
    
    public MethodCallsLogger() {
        this.mCalledMethods = new HashMap<String, Integer>();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public boolean approveCall(final String s, final int n) {
        final Integer n2 = this.mCalledMethods.get(s);
        boolean b = false;
        int intValue;
        if (n2 != null) {
            intValue = n2;
        }
        else {
            intValue = 0;
        }
        if ((intValue & n) != 0x0) {
            b = true;
        }
        this.mCalledMethods.put(s, n | intValue);
        return b ^ true;
    }
}
