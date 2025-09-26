// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson.internal;

import java.lang.reflect.Field;
import java.io.ObjectStreamClass;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;

public abstract class UnsafeAllocator
{
    public static UnsafeAllocator create() {
        try {
            final Class<?> forName = Class.forName("sun.misc.Unsafe");
            final Field declaredField = forName.getDeclaredField("theUnsafe");
            declaredField.setAccessible(true);
            return new UnsafeAllocator(forName.getMethod("allocateInstance", Class.class), declaredField.get(null)) {
                final Method val$allocateInstance;
                final Object val$unsafe;
                
                @Override
                public <T> T newInstance(final Class<T> clazz) throws Exception {
                    return (T)this.val$allocateInstance.invoke(this.val$unsafe, clazz);
                }
            };
        }
        catch (final Exception ex) {
            try {
                final Method declaredMethod = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
                declaredMethod.setAccessible(true);
                return new UnsafeAllocator(declaredMethod) {
                    final Method val$newInstance;
                    
                    @Override
                    public <T> T newInstance(final Class<T> clazz) throws Exception {
                        return (T)this.val$newInstance.invoke(null, clazz, Object.class);
                    }
                };
            }
            catch (final Exception ex2) {
                try {
                    final Method declaredMethod2 = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
                    declaredMethod2.setAccessible(true);
                    final int intValue = (int)declaredMethod2.invoke(null, Object.class);
                    final Method declaredMethod3 = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, Integer.TYPE);
                    declaredMethod3.setAccessible(true);
                    return new UnsafeAllocator(declaredMethod3, intValue) {
                        final int val$constructorId;
                        final Method val$newInstance;
                        
                        @Override
                        public <T> T newInstance(final Class<T> clazz) throws Exception {
                            return (T)this.val$newInstance.invoke(null, clazz, this.val$constructorId);
                        }
                    };
                }
                catch (final Exception ex3) {
                    return new UnsafeAllocator() {
                        @Override
                        public <T> T newInstance(final Class<T> obj) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Cannot allocate ");
                            sb.append(obj);
                            throw new UnsupportedOperationException(sb.toString());
                        }
                    };
                }
            }
        }
    }
    
    public abstract <T> T newInstance(final Class<T> p0) throws Exception;
}
