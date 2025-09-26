// 
// Decompiled by Procyon v0.6.0
// 

package com.google.gson.internal;

import java.util.NoSuchElementException;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Properties;
import java.util.Collection;
import java.util.Arrays;
import java.lang.reflect.TypeVariable;
import java.io.Serializable;
import java.lang.reflect.WildcardType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

public final class $Gson$Types
{
    static final Type[] EMPTY_TYPE_ARRAY;
    
    static {
        EMPTY_TYPE_ARRAY = new Type[0];
    }
    
    private $Gson$Types() {
    }
    
    public static GenericArrayType arrayOf(final Type type) {
        return new GenericArrayTypeImpl(type);
    }
    
    public static Type canonicalize(final Type type) {
        if (type instanceof Class) {
            Serializable s;
            final Class clazz = (Class)(s = (Class)type);
            if (clazz.isArray()) {
                s = new GenericArrayTypeImpl(canonicalize(clazz.getComponentType()));
            }
            return (Type)s;
        }
        if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)type;
            return new ParameterizedTypeImpl(parameterizedType.getOwnerType(), parameterizedType.getRawType(), parameterizedType.getActualTypeArguments());
        }
        if (type instanceof GenericArrayType) {
            return new GenericArrayTypeImpl(((GenericArrayType)type).getGenericComponentType());
        }
        if (type instanceof WildcardType) {
            final WildcardType wildcardType = (WildcardType)type;
            return new WildcardTypeImpl(wildcardType.getUpperBounds(), wildcardType.getLowerBounds());
        }
        return type;
    }
    
    private static void checkNotPrimitive(final Type type) {
        $Gson$Preconditions.checkArgument(!(type instanceof Class) || !((Class)type).isPrimitive());
    }
    
    private static Class<?> declaringClassOf(final TypeVariable<?> typeVariable) {
        final Object genericDeclaration = typeVariable.getGenericDeclaration();
        Class clazz;
        if (genericDeclaration instanceof Class) {
            clazz = (Class)genericDeclaration;
        }
        else {
            clazz = null;
        }
        return clazz;
    }
    
    static boolean equal(final Object o, final Object obj) {
        return o == obj || (o != null && o.equals(obj));
    }
    
    public static boolean equals(final Type type, final Type obj) {
        final boolean b = true;
        boolean b2 = true;
        final boolean b3 = true;
        if (type == obj) {
            return true;
        }
        if (type instanceof Class) {
            return type.equals(obj);
        }
        if (type instanceof ParameterizedType) {
            if (!(obj instanceof ParameterizedType)) {
                return false;
            }
            final ParameterizedType parameterizedType = (ParameterizedType)type;
            final ParameterizedType parameterizedType2 = (ParameterizedType)obj;
            return equal(parameterizedType.getOwnerType(), parameterizedType2.getOwnerType()) && parameterizedType.getRawType().equals(parameterizedType2.getRawType()) && Arrays.equals(parameterizedType.getActualTypeArguments(), parameterizedType2.getActualTypeArguments()) && b3;
        }
        else {
            if (type instanceof GenericArrayType) {
                return obj instanceof GenericArrayType && equals(((GenericArrayType)type).getGenericComponentType(), ((GenericArrayType)obj).getGenericComponentType());
            }
            if (type instanceof WildcardType) {
                if (!(obj instanceof WildcardType)) {
                    return false;
                }
                final WildcardType wildcardType = (WildcardType)type;
                final WildcardType wildcardType2 = (WildcardType)obj;
                return Arrays.equals(wildcardType.getUpperBounds(), wildcardType2.getUpperBounds()) && Arrays.equals(wildcardType.getLowerBounds(), wildcardType2.getLowerBounds()) && b;
            }
            else {
                if (!(type instanceof TypeVariable)) {
                    return false;
                }
                if (!(obj instanceof TypeVariable)) {
                    return false;
                }
                final TypeVariable typeVariable = (TypeVariable)type;
                final TypeVariable typeVariable2 = (TypeVariable)obj;
                if (typeVariable.getGenericDeclaration() != typeVariable2.getGenericDeclaration() || !typeVariable.getName().equals(typeVariable2.getName())) {
                    b2 = false;
                }
                return b2;
            }
        }
    }
    
    public static Type getArrayComponentType(Type type) {
        if (type instanceof GenericArrayType) {
            type = ((GenericArrayType)type).getGenericComponentType();
        }
        else {
            type = ((Class)type).getComponentType();
        }
        return type;
    }
    
    public static Type getCollectionElementType(Type supertype, final Class<?> clazz) {
        final Type type = supertype = getSupertype(supertype, clazz, Collection.class);
        if (type instanceof WildcardType) {
            supertype = ((WildcardType)type).getUpperBounds()[0];
        }
        if (supertype instanceof ParameterizedType) {
            return ((ParameterizedType)supertype).getActualTypeArguments()[0];
        }
        return Object.class;
    }
    
    static Type getGenericSupertype(final Type type, Class<?> clazz, final Class<?> clazz2) {
        if (clazz2 == clazz) {
            return type;
        }
        if (clazz2.isInterface()) {
            final Class[] interfaces = clazz.getInterfaces();
            for (int i = 0; i < interfaces.length; ++i) {
                if (interfaces[i] == clazz2) {
                    return clazz.getGenericInterfaces()[i];
                }
                if (clazz2.isAssignableFrom(interfaces[i])) {
                    return getGenericSupertype(clazz.getGenericInterfaces()[i], interfaces[i], clazz2);
                }
            }
        }
        if (!clazz.isInterface()) {
            while (clazz != Object.class) {
                final Class superclass = clazz.getSuperclass();
                if (superclass == clazz2) {
                    return clazz.getGenericSuperclass();
                }
                if (clazz2.isAssignableFrom(superclass)) {
                    return getGenericSupertype(clazz.getGenericSuperclass(), superclass, clazz2);
                }
                clazz = superclass;
            }
        }
        return clazz2;
    }
    
    public static Type[] getMapKeyAndValueTypes(Type supertype, final Class<?> clazz) {
        if (supertype == Properties.class) {
            return new Type[] { String.class, String.class };
        }
        supertype = getSupertype(supertype, clazz, Map.class);
        if (supertype instanceof ParameterizedType) {
            return ((ParameterizedType)supertype).getActualTypeArguments();
        }
        return new Type[] { Object.class, Object.class };
    }
    
    public static Class<?> getRawType(Type rawType) {
        if (rawType instanceof Class) {
            return (Class)rawType;
        }
        if (rawType instanceof ParameterizedType) {
            rawType = ((ParameterizedType)rawType).getRawType();
            $Gson$Preconditions.checkArgument(rawType instanceof Class);
            return (Class)rawType;
        }
        if (rawType instanceof GenericArrayType) {
            return Array.newInstance(getRawType(((GenericArrayType)rawType).getGenericComponentType()), 0).getClass();
        }
        if (rawType instanceof TypeVariable) {
            return Object.class;
        }
        if (rawType instanceof WildcardType) {
            return getRawType(((WildcardType)rawType).getUpperBounds()[0]);
        }
        String name;
        if (rawType == null) {
            name = "null";
        }
        else {
            name = rawType.getClass().getName();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Expected a Class, ParameterizedType, or GenericArrayType, but <");
        sb.append(rawType);
        sb.append("> is of type ");
        sb.append(name);
        throw new IllegalArgumentException(sb.toString());
    }
    
    static Type getSupertype(final Type type, final Class<?> clazz, final Class<?> clazz2) {
        $Gson$Preconditions.checkArgument(clazz2.isAssignableFrom(clazz));
        return resolve(type, clazz, getGenericSupertype(type, clazz, clazz2));
    }
    
    private static int hashCodeOrZero(final Object o) {
        int hashCode;
        if (o != null) {
            hashCode = o.hashCode();
        }
        else {
            hashCode = 0;
        }
        return hashCode;
    }
    
    private static int indexOf(final Object[] array, final Object o) {
        for (int i = 0; i < array.length; ++i) {
            if (o.equals(array[i])) {
                return i;
            }
        }
        throw new NoSuchElementException();
    }
    
    public static ParameterizedType newParameterizedTypeWithOwner(final Type type, final Type type2, final Type... array) {
        return new ParameterizedTypeImpl(type, type2, array);
    }
    
    public static Type resolve(Type type, final Class<?> clazz, Type type2) {
        while (type2 instanceof TypeVariable) {
            final TypeVariable typeVariable = (TypeVariable)type2;
            type2 = resolveTypeVariable(type, clazz, typeVariable);
            if (type2 == typeVariable) {
                return type2;
            }
        }
        if (type2 instanceof Class) {
            final Class clazz2 = (Class)type2;
            if (clazz2.isArray()) {
                final Class componentType = clazz2.getComponentType();
                type = resolve(type, clazz, componentType);
                Object array;
                if (componentType == type) {
                    array = clazz2;
                }
                else {
                    array = arrayOf(type);
                }
                return (Type)array;
            }
        }
        if (type2 instanceof GenericArrayType) {
            final GenericArrayType genericArrayType = (GenericArrayType)type2;
            final Type genericComponentType = genericArrayType.getGenericComponentType();
            type = resolve(type, clazz, genericComponentType);
            GenericArrayType array2;
            if (genericComponentType == type) {
                array2 = genericArrayType;
            }
            else {
                array2 = arrayOf(type);
            }
            return array2;
        }
        final boolean b = type2 instanceof ParameterizedType;
        int i = 0;
        if (b) {
            final ParameterizedType parameterizedType = (ParameterizedType)type2;
            type2 = parameterizedType.getOwnerType();
            final Type resolve = resolve(type, clazz, type2);
            int n;
            if (resolve != type2) {
                n = 1;
            }
            else {
                n = 0;
            }
            Type[] actualTypeArguments;
            int n2;
            Type[] array3;
            for (actualTypeArguments = parameterizedType.getActualTypeArguments(); i < actualTypeArguments.length; ++i, n = n2, actualTypeArguments = array3) {
                final Type resolve2 = resolve(type, clazz, actualTypeArguments[i]);
                n2 = n;
                array3 = actualTypeArguments;
                if (resolve2 != actualTypeArguments[i]) {
                    n2 = n;
                    array3 = actualTypeArguments;
                    if (n == 0) {
                        array3 = actualTypeArguments.clone();
                        n2 = 1;
                    }
                    array3[i] = resolve2;
                }
            }
            ParameterizedType parameterizedTypeWithOwner = parameterizedType;
            if (n != 0) {
                parameterizedTypeWithOwner = newParameterizedTypeWithOwner(resolve, parameterizedType.getRawType(), actualTypeArguments);
            }
            return parameterizedTypeWithOwner;
        }
        if (type2 instanceof WildcardType) {
            final WildcardType wildcardType = (WildcardType)type2;
            final Type[] lowerBounds = wildcardType.getLowerBounds();
            final Type[] upperBounds = wildcardType.getUpperBounds();
            if (lowerBounds.length == 1) {
                type = resolve(type, clazz, lowerBounds[0]);
                if (type != lowerBounds[0]) {
                    return supertypeOf(type);
                }
            }
            else if (upperBounds.length == 1) {
                type = resolve(type, clazz, upperBounds[0]);
                if (type != upperBounds[0]) {
                    return subtypeOf(type);
                }
            }
            return wildcardType;
        }
        return type2;
    }
    
    static Type resolveTypeVariable(Type genericSupertype, final Class<?> clazz, final TypeVariable<?> typeVariable) {
        final Class<?> declaringClass = declaringClassOf(typeVariable);
        if (declaringClass == null) {
            return typeVariable;
        }
        genericSupertype = getGenericSupertype(genericSupertype, clazz, declaringClass);
        if (genericSupertype instanceof ParameterizedType) {
            return ((ParameterizedType)genericSupertype).getActualTypeArguments()[indexOf(declaringClass.getTypeParameters(), typeVariable)];
        }
        return typeVariable;
    }
    
    public static WildcardType subtypeOf(final Type type) {
        return new WildcardTypeImpl(new Type[] { type }, $Gson$Types.EMPTY_TYPE_ARRAY);
    }
    
    public static WildcardType supertypeOf(final Type type) {
        return new WildcardTypeImpl(new Type[] { Object.class }, new Type[] { type });
    }
    
    public static String typeToString(final Type type) {
        String s;
        if (type instanceof Class) {
            s = ((Class)type).getName();
        }
        else {
            s = type.toString();
        }
        return s;
    }
    
    private static final class GenericArrayTypeImpl implements GenericArrayType, Serializable
    {
        private static final long serialVersionUID = 0L;
        private final Type componentType;
        
        public GenericArrayTypeImpl(final Type type) {
            this.componentType = $Gson$Types.canonicalize(type);
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof GenericArrayType && $Gson$Types.equals(this, (Type)o);
        }
        
        @Override
        public Type getGenericComponentType() {
            return this.componentType;
        }
        
        @Override
        public int hashCode() {
            return this.componentType.hashCode();
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append($Gson$Types.typeToString(this.componentType));
            sb.append("[]");
            return sb.toString();
        }
    }
    
    private static final class ParameterizedTypeImpl implements ParameterizedType, Serializable
    {
        private static final long serialVersionUID = 0L;
        private final Type ownerType;
        private final Type rawType;
        private final Type[] typeArguments;
        
        public ParameterizedTypeImpl(Type canonicalize, final Type type, final Type... array) {
            final boolean b = type instanceof Class;
            int i = 0;
            if (b) {
                final Class clazz = (Class)type;
                final boolean b2 = true;
                $Gson$Preconditions.checkArgument(canonicalize != null || clazz.getEnclosingClass() == null);
                boolean b3 = b2;
                if (canonicalize != null) {
                    b3 = (clazz.getEnclosingClass() != null && b2);
                }
                $Gson$Preconditions.checkArgument(b3);
            }
            if (canonicalize == null) {
                canonicalize = null;
            }
            else {
                canonicalize = $Gson$Types.canonicalize(canonicalize);
            }
            this.ownerType = canonicalize;
            this.rawType = $Gson$Types.canonicalize(type);
            this.typeArguments = array.clone();
            while (i < this.typeArguments.length) {
                $Gson$Preconditions.checkNotNull(this.typeArguments[i]);
                checkNotPrimitive(this.typeArguments[i]);
                this.typeArguments[i] = $Gson$Types.canonicalize(this.typeArguments[i]);
                ++i;
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof ParameterizedType && $Gson$Types.equals(this, (Type)o);
        }
        
        @Override
        public Type[] getActualTypeArguments() {
            return this.typeArguments.clone();
        }
        
        @Override
        public Type getOwnerType() {
            return this.ownerType;
        }
        
        @Override
        public Type getRawType() {
            return this.rawType;
        }
        
        @Override
        public int hashCode() {
            return hashCodeOrZero(this.ownerType) ^ (Arrays.hashCode(this.typeArguments) ^ this.rawType.hashCode());
        }
        
        @Override
        public String toString() {
            final int length = this.typeArguments.length;
            int i = 1;
            final StringBuilder sb = new StringBuilder(30 * (length + 1));
            sb.append($Gson$Types.typeToString(this.rawType));
            if (this.typeArguments.length == 0) {
                return sb.toString();
            }
            sb.append("<");
            sb.append($Gson$Types.typeToString(this.typeArguments[0]));
            while (i < this.typeArguments.length) {
                sb.append(", ");
                sb.append($Gson$Types.typeToString(this.typeArguments[i]));
                ++i;
            }
            sb.append(">");
            return sb.toString();
        }
    }
    
    private static final class WildcardTypeImpl implements WildcardType, Serializable
    {
        private static final long serialVersionUID = 0L;
        private final Type lowerBound;
        private final Type upperBound;
        
        public WildcardTypeImpl(final Type[] array, final Type[] array2) {
            final int length = array2.length;
            final boolean b = true;
            $Gson$Preconditions.checkArgument(length <= 1);
            $Gson$Preconditions.checkArgument(array.length == 1);
            if (array2.length == 1) {
                $Gson$Preconditions.checkNotNull(array2[0]);
                checkNotPrimitive(array2[0]);
                $Gson$Preconditions.checkArgument(array[0] == Object.class && b);
                this.lowerBound = $Gson$Types.canonicalize(array2[0]);
                this.upperBound = Object.class;
            }
            else {
                $Gson$Preconditions.checkNotNull(array[0]);
                checkNotPrimitive(array[0]);
                this.lowerBound = null;
                this.upperBound = $Gson$Types.canonicalize(array[0]);
            }
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof WildcardType && $Gson$Types.equals(this, (Type)o);
        }
        
        @Override
        public Type[] getLowerBounds() {
            Type[] empty_TYPE_ARRAY;
            if (this.lowerBound != null) {
                empty_TYPE_ARRAY = new Type[] { this.lowerBound };
            }
            else {
                empty_TYPE_ARRAY = $Gson$Types.EMPTY_TYPE_ARRAY;
            }
            return empty_TYPE_ARRAY;
        }
        
        @Override
        public Type[] getUpperBounds() {
            return new Type[] { this.upperBound };
        }
        
        @Override
        public int hashCode() {
            int n;
            if (this.lowerBound != null) {
                n = this.lowerBound.hashCode() + 31;
            }
            else {
                n = 1;
            }
            return n ^ 31 + this.upperBound.hashCode();
        }
        
        @Override
        public String toString() {
            if (this.lowerBound != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("? super ");
                sb.append($Gson$Types.typeToString(this.lowerBound));
                return sb.toString();
            }
            if (this.upperBound == Object.class) {
                return "?";
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("? extends ");
            sb2.append($Gson$Types.typeToString(this.upperBound));
            return sb2.toString();
        }
    }
}
