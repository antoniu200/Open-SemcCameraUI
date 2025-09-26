// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import kotlin.SinceKotlin;
import kotlin.reflect.KProperty2;
import kotlin.reflect.KProperty1;
import kotlin.reflect.KProperty0;
import kotlin.reflect.KMutableProperty2;
import kotlin.reflect.KMutableProperty1;
import kotlin.reflect.KMutableProperty0;
import kotlin.reflect.KDeclarationContainer;
import kotlin.reflect.KFunction;
import kotlin.reflect.KClass;

public class ReflectionFactory
{
    private static final String KOTLIN_JVM_FUNCTIONS = "kotlin.jvm.functions.";
    
    public KClass createKotlinClass(final Class clazz) {
        return new ClassReference(clazz);
    }
    
    public KClass createKotlinClass(final Class clazz, final String s) {
        return new ClassReference(clazz);
    }
    
    public KFunction function(final FunctionReference functionReference) {
        return functionReference;
    }
    
    public KClass getOrCreateKotlinClass(final Class clazz) {
        return new ClassReference(clazz);
    }
    
    public KClass getOrCreateKotlinClass(final Class clazz, final String s) {
        return new ClassReference(clazz);
    }
    
    public KDeclarationContainer getOrCreateKotlinPackage(final Class clazz, final String s) {
        return new PackageReference(clazz, s);
    }
    
    public KMutableProperty0 mutableProperty0(final MutablePropertyReference0 mutablePropertyReference0) {
        return mutablePropertyReference0;
    }
    
    public KMutableProperty1 mutableProperty1(final MutablePropertyReference1 mutablePropertyReference1) {
        return mutablePropertyReference1;
    }
    
    public KMutableProperty2 mutableProperty2(final MutablePropertyReference2 mutablePropertyReference2) {
        return mutablePropertyReference2;
    }
    
    public KProperty0 property0(final PropertyReference0 propertyReference0) {
        return propertyReference0;
    }
    
    public KProperty1 property1(final PropertyReference1 propertyReference1) {
        return propertyReference1;
    }
    
    public KProperty2 property2(final PropertyReference2 propertyReference2) {
        return propertyReference2;
    }
    
    @SinceKotlin(version = "1.1")
    public String renderLambdaToString(final Lambda lambda) {
        String s2;
        final String s = s2 = lambda.getClass().getGenericInterfaces()[0].toString();
        if (s.startsWith("kotlin.jvm.functions.")) {
            s2 = s.substring("kotlin.jvm.functions.".length());
        }
        return s2;
    }
}
