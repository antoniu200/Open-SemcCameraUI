// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\f\n\u0002\b\u0006\b\u00c0\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\n" }, d2 = { "Lkotlin/jvm/internal/CharCompanionObject;", "", "()V", "MAX_HIGH_SURROGATE", "", "MAX_LOW_SURROGATE", "MAX_SURROGATE", "MIN_HIGH_SURROGATE", "MIN_LOW_SURROGATE", "MIN_SURROGATE", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
public final class CharCompanionObject
{
    public static final CharCompanionObject INSTANCE;
    public static final char MAX_HIGH_SURROGATE = '\udbff';
    public static final char MAX_LOW_SURROGATE = '\udfff';
    public static final char MAX_SURROGATE = '\udfff';
    public static final char MIN_HIGH_SURROGATE = '\ud800';
    public static final char MIN_LOW_SURROGATE = '\udc00';
    public static final char MIN_SURROGATE = '\ud800';
    
    static {
        INSTANCE = new CharCompanionObject();
    }
    
    private CharCompanionObject() {
    }
}
