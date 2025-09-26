// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import kotlin.reflect.KDeclarationContainer;

public class FunctionReferenceImpl extends FunctionReference
{
    private final String name;
    private final KDeclarationContainer owner;
    private final String signature;
    
    public FunctionReferenceImpl(final int n, final KDeclarationContainer owner, final String name, final String signature) {
        super(n);
        this.owner = owner;
        this.name = name;
        this.signature = signature;
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
