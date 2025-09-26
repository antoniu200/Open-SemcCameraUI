// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import java.io.ObjectStreamException;
import kotlin.reflect.KVisibility;
import kotlin.reflect.KTypeParameter;
import kotlin.reflect.KType;
import kotlin.jvm.KotlinReflectionNotSupportedError;
import kotlin.reflect.KDeclarationContainer;
import java.lang.annotation.Annotation;
import java.util.List;
import kotlin.reflect.KParameter;
import java.util.Map;
import kotlin.SinceKotlin;
import java.io.Serializable;
import kotlin.reflect.KCallable;

public abstract class CallableReference implements KCallable, Serializable
{
    @SinceKotlin(version = "1.1")
    public static final Object NO_RECEIVER;
    @SinceKotlin(version = "1.1")
    protected final Object receiver;
    private transient KCallable reflected;
    
    static {
        NO_RECEIVER = NoReceiver.INSTANCE;
    }
    
    public CallableReference() {
        this(CallableReference.NO_RECEIVER);
    }
    
    @SinceKotlin(version = "1.1")
    protected CallableReference(final Object receiver) {
        this.receiver = receiver;
    }
    
    @Override
    public Object call(final Object... array) {
        return this.getReflected().call(array);
    }
    
    @Override
    public Object callBy(final Map map) {
        return this.getReflected().callBy(map);
    }
    
    @SinceKotlin(version = "1.1")
    public KCallable compute() {
        KCallable reflected;
        if ((reflected = this.reflected) == null) {
            reflected = this.computeReflected();
            this.reflected = reflected;
        }
        return reflected;
    }
    
    protected abstract KCallable computeReflected();
    
    @Override
    public List<Annotation> getAnnotations() {
        return this.getReflected().getAnnotations();
    }
    
    @SinceKotlin(version = "1.1")
    public Object getBoundReceiver() {
        return this.receiver;
    }
    
    @Override
    public String getName() {
        throw new AbstractMethodError();
    }
    
    public KDeclarationContainer getOwner() {
        throw new AbstractMethodError();
    }
    
    @Override
    public List<KParameter> getParameters() {
        return this.getReflected().getParameters();
    }
    
    @SinceKotlin(version = "1.1")
    protected KCallable getReflected() {
        final KCallable compute = this.compute();
        if (compute == this) {
            throw new KotlinReflectionNotSupportedError();
        }
        return compute;
    }
    
    @Override
    public KType getReturnType() {
        return this.getReflected().getReturnType();
    }
    
    public String getSignature() {
        throw new AbstractMethodError();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public List<KTypeParameter> getTypeParameters() {
        return this.getReflected().getTypeParameters();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public KVisibility getVisibility() {
        return this.getReflected().getVisibility();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public boolean isAbstract() {
        return this.getReflected().isAbstract();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public boolean isFinal() {
        return this.getReflected().isFinal();
    }
    
    @SinceKotlin(version = "1.1")
    @Override
    public boolean isOpen() {
        return this.getReflected().isOpen();
    }
    
    @SinceKotlin(version = "1.2")
    private static class NoReceiver implements Serializable
    {
        private static final NoReceiver INSTANCE;
        
        static {
            INSTANCE = new NoReceiver();
        }
        
        private Object readResolve() throws ObjectStreamException {
            return NoReceiver.INSTANCE;
        }
    }
}
