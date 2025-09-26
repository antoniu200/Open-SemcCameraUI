// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import kotlin.reflect.KProperty;
import kotlin.SinceKotlin;
import kotlin.reflect.KCallable;
import kotlin.reflect.KProperty2;

public abstract class PropertyReference2 extends PropertyReference implements KProperty2
{
    @Override
    protected KCallable computeReflected() {
        return Reflection.property2(this);
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public Object getDelegate(final Object o, final Object o2) {
        return ((KProperty2)this.getReflected()).getDelegate(o, o2);
    }
    
    @Override
    public KProperty2.Getter getGetter() {
        return (KProperty2.Getter)((KProperty2)this.getReflected()).getGetter();
    }
    
    @Override
    public Object invoke(final Object o, final Object o2) {
        return this.get(o, o2);
    }
}
