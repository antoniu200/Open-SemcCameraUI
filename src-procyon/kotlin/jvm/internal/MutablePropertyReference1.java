// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import kotlin.reflect.KMutableProperty;
import kotlin.reflect.KProperty1;
import kotlin.reflect.KProperty;
import kotlin.reflect.KCallable;
import kotlin.SinceKotlin;
import kotlin.reflect.KMutableProperty1;

public abstract class MutablePropertyReference1 extends MutablePropertyReference implements KMutableProperty1
{
    public MutablePropertyReference1() {
    }
    
    @SinceKotlin(version = "1.1")
    public MutablePropertyReference1(final Object o) {
        super(o);
    }
    
    @Override
    protected KCallable computeReflected() {
        return Reflection.mutableProperty1(this);
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public Object getDelegate(final Object o) {
        return ((KMutableProperty1)this.getReflected()).getDelegate(o);
    }
    
    @Override
    public KProperty1.Getter getGetter() {
        return (KProperty1.Getter)((KMutableProperty1)this.getReflected()).getGetter();
    }
    
    @Override
    public KMutableProperty1.Setter getSetter() {
        return (KMutableProperty1.Setter)((KMutableProperty1)this.getReflected()).getSetter();
    }
    
    @Override
    public Object invoke(final Object o) {
        return this.get(o);
    }
}
