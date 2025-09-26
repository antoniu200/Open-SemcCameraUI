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

public class Reflection
{
    private static final KClass[] EMPTY_K_CLASS_ARRAY;
    static final String REFLECTION_NOT_AVAILABLE = " (Kotlin reflection is not available)";
    private static final ReflectionFactory factory;
    
    static {
        ReflectionFactory factory2 = null;
        while (true) {
            try {
                factory2 = (ReflectionFactory)Class.forName("kotlin.reflect.jvm.internal.ReflectionFactoryImpl").newInstance();
                if (factory2 == null) {
                    factory2 = new ReflectionFactory();
                }
                factory = factory2;
                EMPTY_K_CLASS_ARRAY = new KClass[0];
            }
            catch (final ClassCastException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                continue;
            }
            break;
        }
    }
    
    public static KClass createKotlinClass(final Class clazz) {
        return Reflection.factory.createKotlinClass(clazz);
    }
    
    public static KClass createKotlinClass(final Class clazz, final String s) {
        return Reflection.factory.createKotlinClass(clazz, s);
    }
    
    public static KFunction function(final FunctionReference functionReference) {
        return Reflection.factory.function(functionReference);
    }
    
    public static KClass getOrCreateKotlinClass(final Class clazz) {
        return Reflection.factory.getOrCreateKotlinClass(clazz);
    }
    
    public static KClass getOrCreateKotlinClass(final Class clazz, final String s) {
        return Reflection.factory.getOrCreateKotlinClass(clazz, s);
    }
    
    public static KClass[] getOrCreateKotlinClasses(final Class[] array) {
        final int length = array.length;
        if (length == 0) {
            return Reflection.EMPTY_K_CLASS_ARRAY;
        }
        final KClass[] array2 = new KClass[length];
        for (int i = 0; i < length; ++i) {
            array2[i] = getOrCreateKotlinClass(array[i]);
        }
        return array2;
    }
    
    public static KDeclarationContainer getOrCreateKotlinPackage(final Class clazz, final String s) {
        return Reflection.factory.getOrCreateKotlinPackage(clazz, s);
    }
    
    public static KMutableProperty0 mutableProperty0(final MutablePropertyReference0 mutablePropertyReference0) {
        return Reflection.factory.mutableProperty0(mutablePropertyReference0);
    }
    
    public static KMutableProperty1 mutableProperty1(final MutablePropertyReference1 mutablePropertyReference1) {
        return Reflection.factory.mutableProperty1(mutablePropertyReference1);
    }
    
    public static KMutableProperty2 mutableProperty2(final MutablePropertyReference2 mutablePropertyReference2) {
        return Reflection.factory.mutableProperty2(mutablePropertyReference2);
    }
    
    public static KProperty0 property0(final PropertyReference0 propertyReference0) {
        return Reflection.factory.property0(propertyReference0);
    }
    
    public static KProperty1 property1(final PropertyReference1 propertyReference1) {
        return Reflection.factory.property1(propertyReference1);
    }
    
    public static KProperty2 property2(final PropertyReference2 propertyReference2) {
        return Reflection.factory.property2(propertyReference2);
    }
    
    @SinceKotlin(version = "1.1")
    public static String renderLambdaToString(final Lambda lambda) {
        return Reflection.factory.renderLambdaToString(lambda);
    }
}
