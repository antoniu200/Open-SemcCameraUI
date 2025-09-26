// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import java.io.Serializable;
import kotlin.Function;

public interface FunctionBase extends Function, Serializable
{
    int getArity();
}
