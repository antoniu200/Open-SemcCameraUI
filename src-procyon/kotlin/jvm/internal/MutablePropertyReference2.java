// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import kotlin.reflect.KMutableProperty;
import kotlin.reflect.KProperty2;
import kotlin.reflect.KProperty;
import kotlin.SinceKotlin;
import kotlin.reflect.KCallable;
import kotlin.reflect.KMutableProperty2;

public abstract class MutablePropertyReference2 extends MutablePropertyReference implements KMutableProperty2
{
    @Override
    protected KCallable computeReflected() {
        return Reflection.mutableProperty2(this);
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public Object getDelegate(final Object o, final Object o2) {
        return ((KMutableProperty2)this.getReflected()).getDelegate(o, o2);
    }
    
    @Override
    public KProperty2.Getter getGetter() {
        return (KProperty2.Getter)((KMutableProperty2)this.getReflected()).getGetter();
    }
    
    @Override
    public KMutableProperty2.Setter getSetter() {
        return (KMutableProperty2.Setter)((KMutableProperty2)this.getReflected()).getSetter();
    }
    
    @Override
    public Object invoke(final Object o, final Object o2) {
        return this.get(o, o2);
    }
}
