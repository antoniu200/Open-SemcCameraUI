// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import kotlin.DeprecationLevel;
import kotlin.jvm.functions.Function22;
import kotlin.jvm.functions.Function21;
import kotlin.jvm.functions.Function20;
import kotlin.jvm.functions.Function19;
import kotlin.jvm.functions.Function18;
import kotlin.jvm.functions.Function17;
import kotlin.jvm.functions.Function16;
import kotlin.jvm.functions.Function15;
import kotlin.jvm.functions.Function14;
import kotlin.jvm.functions.Function13;
import kotlin.jvm.functions.Function12;
import kotlin.jvm.functions.Function11;
import kotlin.jvm.functions.Function10;
import kotlin.jvm.functions.Function9;
import kotlin.jvm.functions.Function8;
import kotlin.jvm.functions.Function7;
import kotlin.jvm.functions.Function6;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function0;
import java.io.Serializable;
import kotlin.Function;

@Deprecated
@kotlin.Deprecated(level = DeprecationLevel.ERROR, message = "This class is no longer supported, do not use it.")
public abstract class FunctionImpl implements Function, Serializable, Function0, Function1, Function2, Function3, Function4, Function5, Function6, Function7, Function8, Function9, Function10, Function11, Function12, Function13, Function14, Function15, Function16, Function17, Function18, Function19, Function20, Function21, Function22
{
    private void checkArity(final int n) {
        if (this.getArity() != n) {
            this.throwWrongArity(n);
        }
    }
    
    private void throwWrongArity(final int i) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Wrong function arity, expected: ");
        sb.append(i);
        sb.append(", actual: ");
        sb.append(this.getArity());
        throw new IllegalStateException(sb.toString());
    }
    
    public abstract int getArity();
    
    @Override
    public Object invoke() {
        this.checkArity(0);
        return this.invokeVararg(new Object[0]);
    }
    
    @Override
    public Object invoke(final Object o) {
        this.checkArity(1);
        return this.invokeVararg(o);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2) {
        this.checkArity(2);
        return this.invokeVararg(o, o2);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3) {
        this.checkArity(3);
        return this.invokeVararg(o, o2, o3);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3, final Object o4) {
        this.checkArity(4);
        return this.invokeVararg(o, o2, o3, o4);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3, final Object o4, final Object o5) {
        this.checkArity(5);
        return this.invokeVararg(o, o2, o3, o4, o5);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6) {
        this.checkArity(6);
        return this.invokeVararg(o, o2, o3, o4, o5, o6);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7) {
        this.checkArity(7);
        return this.invokeVararg(o, o2, o3, o4, o5, o6, o7);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Object o8) {
        this.checkArity(8);
        return this.invokeVararg(o, o2, o3, o4, o5, o6, o7, o8);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Object o8, final Object o9) {
        this.checkArity(9);
        return this.invokeVararg(o, o2, o3, o4, o5, o6, o7, o8, o9);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Object o8, final Object o9, final Object o10) {
        this.checkArity(10);
        return this.invokeVararg(o, o2, o3, o4, o5, o6, o7, o8, o9, o10);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Object o8, final Object o9, final Object o10, final Object o11) {
        this.checkArity(11);
        return this.invokeVararg(o, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Object o8, final Object o9, final Object o10, final Object o11, final Object o12) {
        this.checkArity(12);
        return this.invokeVararg(o, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Object o8, final Object o9, final Object o10, final Object o11, final Object o12, final Object o13) {
        this.checkArity(13);
        return this.invokeVararg(o, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Object o8, final Object o9, final Object o10, final Object o11, final Object o12, final Object o13, final Object o14) {
        this.checkArity(14);
        return this.invokeVararg(o, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Object o8, final Object o9, final Object o10, final Object o11, final Object o12, final Object o13, final Object o14, final Object o15) {
        this.checkArity(15);
        return this.invokeVararg(o, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Object o8, final Object o9, final Object o10, final Object o11, final Object o12, final Object o13, final Object o14, final Object o15, final Object o16) {
        this.checkArity(16);
        return this.invokeVararg(o, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Object o8, final Object o9, final Object o10, final Object o11, final Object o12, final Object o13, final Object o14, final Object o15, final Object o16, final Object o17) {
        this.checkArity(17);
        return this.invokeVararg(o, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Object o8, final Object o9, final Object o10, final Object o11, final Object o12, final Object o13, final Object o14, final Object o15, final Object o16, final Object o17, final Object o18) {
        this.checkArity(18);
        return this.invokeVararg(o, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Object o8, final Object o9, final Object o10, final Object o11, final Object o12, final Object o13, final Object o14, final Object o15, final Object o16, final Object o17, final Object o18, final Object o19) {
        this.checkArity(19);
        return this.invokeVararg(o, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Object o8, final Object o9, final Object o10, final Object o11, final Object o12, final Object o13, final Object o14, final Object o15, final Object o16, final Object o17, final Object o18, final Object o19, final Object o20) {
        this.checkArity(20);
        return this.invokeVararg(o, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Object o8, final Object o9, final Object o10, final Object o11, final Object o12, final Object o13, final Object o14, final Object o15, final Object o16, final Object o17, final Object o18, final Object o19, final Object o20, final Object o21) {
        this.checkArity(21);
        return this.invokeVararg(o, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21);
    }
    
    @Override
    public Object invoke(final Object o, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Object o8, final Object o9, final Object o10, final Object o11, final Object o12, final Object o13, final Object o14, final Object o15, final Object o16, final Object o17, final Object o18, final Object o19, final Object o20, final Object o21, final Object o22) {
        this.checkArity(22);
        return this.invokeVararg(o, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22);
    }
    
    public Object invokeVararg(final Object... array) {
        throw new UnsupportedOperationException();
    }
}
