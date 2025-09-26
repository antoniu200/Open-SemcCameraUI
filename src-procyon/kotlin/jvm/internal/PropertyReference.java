// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import kotlin.reflect.KCallable;
import kotlin.SinceKotlin;
import kotlin.reflect.KProperty;

public abstract class PropertyReference extends CallableReference implements KProperty
{
    public PropertyReference() {
    }
    
    @SinceKotlin(version = "1.1")
    public PropertyReference(final Object o) {
        super(o);
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b = true;
        if (o == this) {
            return true;
        }
        if (o instanceof PropertyReference) {
            final PropertyReference propertyReference = (PropertyReference)o;
            if (!this.getOwner().equals(propertyReference.getOwner()) || !this.getName().equals(propertyReference.getName()) || !this.getSignature().equals(propertyReference.getSignature()) || !Intrinsics.areEqual(this.getBoundReceiver(), propertyReference.getBoundReceiver())) {
                b = false;
            }
            return b;
        }
        return o instanceof KProperty && o.equals(this.compute());
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    protected KProperty getReflected() {
        return (KProperty)super.getReflected();
    }
    
    @Override
    public int hashCode() {
        return (this.getOwner().hashCode() * 31 + this.getName().hashCode()) * 31 + this.getSignature().hashCode();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public boolean isConst() {
        return this.getReflected().isConst();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public boolean isLateinit() {
        return this.getReflected().isLateinit();
    }
    
    @Override
    public String toString() {
        final KCallable compute = this.compute();
        if (compute != this) {
            return compute.toString();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("property ");
        sb.append(this.getName());
        sb.append(" (Kotlin reflection is not available)");
        return sb.toString();
    }
}
