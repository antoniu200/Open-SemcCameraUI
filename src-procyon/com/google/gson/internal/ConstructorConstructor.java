// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson.internal;

import java.util.LinkedHashMap;
import com.google.gson.reflect.TypeToken;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.LinkedHashSet;
import java.util.Set;
import com.google.gson.JsonIOException;
import java.lang.reflect.ParameterizedType;
import java.util.EnumSet;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.Collection;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import com.google.gson.InstanceCreator;
import java.lang.reflect.Type;
import java.util.Map;

public final class ConstructorConstructor
{
    private final Map<Type, InstanceCreator<?>> instanceCreators;
    
    public ConstructorConstructor(final Map<Type, InstanceCreator<?>> instanceCreators) {
        this.instanceCreators = instanceCreators;
    }
    
    private <T> ObjectConstructor<T> newDefaultConstructor(final Class<? super T> clazz) {
        try {
            final Constructor<? super T> declaredConstructor = clazz.getDeclaredConstructor((Class<?>[])new Class[0]);
            if (!declaredConstructor.isAccessible()) {
                declaredConstructor.setAccessible(true);
            }
            return new ObjectConstructor<T>(this, declaredConstructor) {
                final ConstructorConstructor this$0;
                final Constructor val$constructor;
                
                @Override
                public T construct() {
                    try {
                        return this.val$constructor.newInstance((Object[])null);
                    }
                    catch (final IllegalAccessException detailMessage) {
                        throw new AssertionError((Object)detailMessage);
                    }
                    catch (final InvocationTargetException ex) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Failed to invoke ");
                        sb.append(this.val$constructor);
                        sb.append(" with no args");
                        throw new RuntimeException(sb.toString(), ex.getTargetException());
                    }
                    catch (final InstantiationException cause) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Failed to invoke ");
                        sb2.append(this.val$constructor);
                        sb2.append(" with no args");
                        throw new RuntimeException(sb2.toString(), cause);
                    }
                }
            };
        }
        catch (final NoSuchMethodException ex) {
            return null;
        }
    }
    
    private <T> ObjectConstructor<T> newDefaultImplementationConstructor(final Type type, final Class<? super T> clazz) {
        if (Collection.class.isAssignableFrom(clazz)) {
            if (SortedSet.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>(this) {
                    final ConstructorConstructor this$0;
                    
                    @Override
                    public T construct() {
                        return (T)new TreeSet();
                    }
                };
            }
            if (EnumSet.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>(this, type) {
                    final ConstructorConstructor this$0;
                    final Type val$type;
                    
                    @Override
                    public T construct() {
                        if (!(this.val$type instanceof ParameterizedType)) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Invalid EnumSet type: ");
                            sb.append(this.val$type.toString());
                            throw new JsonIOException(sb.toString());
                        }
                        final Type type = ((ParameterizedType)this.val$type).getActualTypeArguments()[0];
                        if (type instanceof Class) {
                            return (T)EnumSet.noneOf((Class<Enum>)type);
                        }
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Invalid EnumSet type: ");
                        sb2.append(this.val$type.toString());
                        throw new JsonIOException(sb2.toString());
                    }
                };
            }
            if (Set.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>(this) {
                    final ConstructorConstructor this$0;
                    
                    @Override
                    public T construct() {
                        return (T)new LinkedHashSet();
                    }
                };
            }
            if (Queue.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>(this) {
                    final ConstructorConstructor this$0;
                    
                    @Override
                    public T construct() {
                        return (T)new LinkedList();
                    }
                };
            }
            return new ObjectConstructor<T>(this) {
                final ConstructorConstructor this$0;
                
                @Override
                public T construct() {
                    return (T)new ArrayList();
                }
            };
        }
        else {
            if (!Map.class.isAssignableFrom(clazz)) {
                return null;
            }
            if (SortedMap.class.isAssignableFrom(clazz)) {
                return new ObjectConstructor<T>(this) {
                    final ConstructorConstructor this$0;
                    
                    @Override
                    public T construct() {
                        return (T)new TreeMap();
                    }
                };
            }
            if (type instanceof ParameterizedType && !String.class.isAssignableFrom(TypeToken.get(((ParameterizedType)type).getActualTypeArguments()[0]).getRawType())) {
                return new ObjectConstructor<T>(this) {
                    final ConstructorConstructor this$0;
                    
                    @Override
                    public T construct() {
                        return (T)new LinkedHashMap();
                    }
                };
            }
            return new ObjectConstructor<T>(this) {
                final ConstructorConstructor this$0;
                
                @Override
                public T construct() {
                    return (T)new LinkedTreeMap();
                }
            };
        }
    }
    
    private <T> ObjectConstructor<T> newUnsafeAllocator(final Type type, final Class<? super T> clazz) {
        return new ObjectConstructor<T>(this, clazz, type) {
            final ConstructorConstructor this$0;
            private final UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
            final Class val$rawType;
            final Type val$type;
            
            @Override
            public T construct() {
                try {
                    return this.unsafeAllocator.newInstance((Class<T>)this.val$rawType);
                }
                catch (final Exception cause) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unable to invoke no-args constructor for ");
                    sb.append(this.val$type);
                    sb.append(". ");
                    sb.append("Register an InstanceCreator with Gson for this type may fix this problem.");
                    throw new RuntimeException(sb.toString(), cause);
                }
            }
        };
    }
    
    public <T> ObjectConstructor<T> get(final TypeToken<T> typeToken) {
        final Type type = typeToken.getType();
        final Class rawType = typeToken.getRawType();
        final InstanceCreator instanceCreator = this.instanceCreators.get(type);
        if (instanceCreator != null) {
            return new ObjectConstructor<T>(this, instanceCreator, type) {
                final ConstructorConstructor this$0;
                final Type val$type;
                final InstanceCreator val$typeCreator;
                
                @Override
                public T construct() {
                    return this.val$typeCreator.createInstance(this.val$type);
                }
            };
        }
        final InstanceCreator instanceCreator2 = this.instanceCreators.get(rawType);
        if (instanceCreator2 != null) {
            return new ObjectConstructor<T>(this, instanceCreator2, type) {
                final ConstructorConstructor this$0;
                final InstanceCreator val$rawTypeCreator;
                final Type val$type;
                
                @Override
                public T construct() {
                    return this.val$rawTypeCreator.createInstance(this.val$type);
                }
            };
        }
        final ObjectConstructor<Object> defaultConstructor = this.newDefaultConstructor(rawType);
        if (defaultConstructor != null) {
            return (ObjectConstructor<T>)defaultConstructor;
        }
        final ObjectConstructor<Object> defaultImplementationConstructor = this.newDefaultImplementationConstructor(type, rawType);
        if (defaultImplementationConstructor != null) {
            return (ObjectConstructor<T>)defaultImplementationConstructor;
        }
        return (ObjectConstructor<T>)this.newUnsafeAllocator(type, rawType);
    }
    
    @Override
    public String toString() {
        return this.instanceCreators.toString();
    }
}
