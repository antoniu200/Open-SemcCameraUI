// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.text;

import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.JvmField;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u00c2\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005" }, d2 = { "Lkotlin/text/SystemProperties;", "", "()V", "LINE_SEPARATOR", "", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
final class SystemProperties
{
    public static final SystemProperties INSTANCE;
    @JvmField
    @NotNull
    public static final String LINE_SEPARATOR;
    
    static {
        INSTANCE = new SystemProperties();
        final String property = System.getProperty("line.separator");
        if (property == null) {
            Intrinsics.throwNpe();
        }
        LINE_SEPARATOR = property;
    }
    
    private SystemProperties() {
    }
}
