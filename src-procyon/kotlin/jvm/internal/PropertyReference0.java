// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import kotlin.reflect.KProperty;
import kotlin.reflect.KCallable;
import kotlin.SinceKotlin;
import kotlin.reflect.KProperty0;

public abstract class PropertyReference0 extends PropertyReference implements KProperty0
{
    public PropertyReference0() {
    }
    
    @SinceKotlin(version = "1.1")
    public PropertyReference0(final Object o) {
        super(o);
    }
    
    @Override
    protected KCallable computeReflected() {
        return Reflection.property0(this);
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public Object getDelegate() {
        return ((KProperty0)this.getReflected()).getDelegate();
    }
    
    @Override
    public KProperty0.Getter getGetter() {
        return (KProperty0.Getter)((KProperty0)this.getReflected()).getGetter();
    }
    
    @Override
    public Object invoke() {
        return this.get();
    }
}
