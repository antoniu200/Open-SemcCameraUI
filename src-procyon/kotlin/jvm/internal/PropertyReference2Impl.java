// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import kotlin.reflect.KDeclarationContainer;

public class PropertyReference2Impl extends PropertyReference2
{
    private final String name;
    private final KDeclarationContainer owner;
    private final String signature;
    
    public PropertyReference2Impl(final KDeclarationContainer owner, final String name, final String signature) {
        this.owner = owner;
        this.name = name;
        this.signature = signature;
    }
    
    @Override
    public Object get(final Object o, final Object o2) {
        return this.getGetter().call(o, o2);
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public KDeclarationContainer getOwner() {
        return this.owner;
    }
    
    @Override
    public String getSignature() {
        return this.signature;
    }
}
