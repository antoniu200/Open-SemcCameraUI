// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import kotlin.reflect.KProperty;
import kotlin.reflect.KCallable;
import kotlin.SinceKotlin;
import kotlin.reflect.KProperty1;

public abstract class PropertyReference1 extends PropertyReference implements KProperty1
{
    public PropertyReference1() {
    }
    
    @SinceKotlin(version = "1.1")
    public PropertyReference1(final Object o) {
        super(o);
    }
    
    @Override
    protected KCallable computeReflected() {
        return Reflection.property1(this);
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public Object getDelegate(final Object o) {
        return ((KProperty1)this.getReflected()).getDelegate(o);
    }
    
    @Override
    public KProperty1.Getter getGetter() {
        return (KProperty1.Getter)((KProperty1)this.getReflected()).getGetter();
    }
    
    @Override
    public Object invoke(final Object o) {
        return this.get(o);
    }
}
