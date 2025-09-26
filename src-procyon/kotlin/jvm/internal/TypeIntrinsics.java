// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import kotlin.Function;
import kotlin.jvm.functions.Function22;
import kotlin.jvm.functions.Function21;
import kotlin.jvm.functions.Function20;
import kotlin.jvm.functions.Function19;
import kotlin.jvm.functions.Function18;
import kotlin.jvm.functions.Function17;
import kotlin.jvm.functions.Function16;
import kotlin.jvm.functions.Function15;
import kotlin.jvm.functions.Function14;
import kotlin.jvm.functions.Function13;
import kotlin.jvm.functions.Function12;
import kotlin.jvm.functions.Function11;
import kotlin.jvm.functions.Function10;
import kotlin.jvm.functions.Function9;
import kotlin.jvm.functions.Function8;
import kotlin.jvm.functions.Function7;
import kotlin.jvm.functions.Function6;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.markers.KMutableSet;
import java.util.Set;
import kotlin.jvm.internal.markers.KMutableMap;
import java.util.Map;
import kotlin.jvm.internal.markers.KMutableListIterator;
import java.util.ListIterator;
import kotlin.jvm.internal.markers.KMutableList;
import java.util.List;
import kotlin.jvm.internal.markers.KMutableIterator;
import java.util.Iterator;
import kotlin.jvm.internal.markers.KMutableIterable;
import kotlin.jvm.internal.markers.KMutableCollection;
import kotlin.jvm.internal.markers.KMappedMarker;
import java.util.Collection;

public class TypeIntrinsics
{
    public static Collection asMutableCollection(final Object o) {
        if (o instanceof KMappedMarker && !(o instanceof KMutableCollection)) {
            throwCce(o, "kotlin.collections.MutableCollection");
        }
        return castToCollection(o);
    }
    
    public static Collection asMutableCollection(final Object o, final String s) {
        if (o instanceof KMappedMarker && !(o instanceof KMutableCollection)) {
            throwCce(s);
        }
        return castToCollection(o);
    }
    
    public static Iterable asMutableIterable(final Object o) {
        if (o instanceof KMappedMarker && !(o instanceof KMutableIterable)) {
            throwCce(o, "kotlin.collections.MutableIterable");
        }
        return castToIterable(o);
    }
    
    public static Iterable asMutableIterable(final Object o, final String s) {
        if (o instanceof KMappedMarker && !(o instanceof KMutableIterable)) {
            throwCce(s);
        }
        return castToIterable(o);
    }
    
    public static Iterator asMutableIterator(final Object o) {
        if (o instanceof KMappedMarker && !(o instanceof KMutableIterator)) {
            throwCce(o, "kotlin.collections.MutableIterator");
        }
        return castToIterator(o);
    }
    
    public static Iterator asMutableIterator(final Object o, final String s) {
        if (o instanceof KMappedMarker && !(o instanceof KMutableIterator)) {
            throwCce(s);
        }
        return castToIterator(o);
    }
    
    public static List asMutableList(final Object o) {
        if (o instanceof KMappedMarker && !(o instanceof KMutableList)) {
            throwCce(o, "kotlin.collections.MutableList");
        }
        return castToList(o);
    }
    
    public static List asMutableList(final Object o, final String s) {
        if (o instanceof KMappedMarker && !(o instanceof KMutableList)) {
            throwCce(s);
        }
        return castToList(o);
    }
    
    public static ListIterator asMutableListIterator(final Object o) {
        if (o instanceof KMappedMarker && !(o instanceof KMutableListIterator)) {
            throwCce(o, "kotlin.collections.MutableListIterator");
        }
        return castToListIterator(o);
    }
    
    public static ListIterator asMutableListIterator(final Object o, final String s) {
        if (o instanceof KMappedMarker && !(o instanceof KMutableListIterator)) {
            throwCce(s);
        }
        return castToListIterator(o);
    }
    
    public static Map asMutableMap(final Object o) {
        if (o instanceof KMappedMarker && !(o instanceof KMutableMap)) {
            throwCce(o, "kotlin.collections.MutableMap");
        }
        return castToMap(o);
    }
    
    public static Map asMutableMap(final Object o, final String s) {
        if (o instanceof KMappedMarker && !(o instanceof KMutableMap)) {
            throwCce(s);
        }
        return castToMap(o);
    }
    
    public static Map.Entry asMutableMapEntry(final Object o) {
        if (o instanceof KMappedMarker && !(o instanceof KMutableMap.Entry)) {
            throwCce(o, "kotlin.collections.MutableMap.MutableEntry");
        }
        return castToMapEntry(o);
    }
    
    public static Map.Entry asMutableMapEntry(final Object o, final String s) {
        if (o instanceof KMappedMarker && !(o instanceof KMutableMap.Entry)) {
            throwCce(s);
        }
        return castToMapEntry(o);
    }
    
    public static Set asMutableSet(final Object o) {
        if (o instanceof KMappedMarker && !(o instanceof KMutableSet)) {
            throwCce(o, "kotlin.collections.MutableSet");
        }
        return castToSet(o);
    }
    
    public static Set asMutableSet(final Object o, final String s) {
        if (o instanceof KMappedMarker && !(o instanceof KMutableSet)) {
            throwCce(s);
        }
        return castToSet(o);
    }
    
    public static Object beforeCheckcastToFunctionOfArity(final Object o, final int i) {
        if (o != null && !isFunctionOfArity(o, i)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("kotlin.jvm.functions.Function");
            sb.append(i);
            throwCce(o, sb.toString());
        }
        return o;
    }
    
    public static Object beforeCheckcastToFunctionOfArity(final Object o, final int n, final String s) {
        if (o != null && !isFunctionOfArity(o, n)) {
            throwCce(s);
        }
        return o;
    }
    
    public static Collection castToCollection(final Object o) {
        try {
            return (Collection)o;
        }
        catch (final ClassCastException ex) {
            throw throwCce(ex);
        }
    }
    
    public static Iterable castToIterable(final Object o) {
        try {
            return (Iterable)o;
        }
        catch (final ClassCastException ex) {
            throw throwCce(ex);
        }
    }
    
    public static Iterator castToIterator(final Object o) {
        try {
            return (Iterator)o;
        }
        catch (final ClassCastException ex) {
            throw throwCce(ex);
        }
    }
    
    public static List castToList(final Object o) {
        try {
            return (List)o;
        }
        catch (final ClassCastException ex) {
            throw throwCce(ex);
        }
    }
    
    public static ListIterator castToListIterator(final Object o) {
        try {
            return (ListIterator)o;
        }
        catch (final ClassCastException ex) {
            throw throwCce(ex);
        }
    }
    
    public static Map castToMap(final Object o) {
        try {
            return (Map)o;
        }
        catch (final ClassCastException ex) {
            throw throwCce(ex);
        }
    }
    
    public static Map.Entry castToMapEntry(final Object o) {
        try {
            return (Map.Entry)o;
        }
        catch (final ClassCastException ex) {
            throw throwCce(ex);
        }
    }
    
    public static Set castToSet(final Object o) {
        try {
            return (Set)o;
        }
        catch (final ClassCastException ex) {
            throw throwCce(ex);
        }
    }
    
    public static int getFunctionArity(final Object o) {
        if (o instanceof FunctionBase) {
            return ((FunctionBase)o).getArity();
        }
        if (o instanceof Function0) {
            return 0;
        }
        if (o instanceof Function1) {
            return 1;
        }
        if (o instanceof Function2) {
            return 2;
        }
        if (o instanceof Function3) {
            return 3;
        }
        if (o instanceof Function4) {
            return 4;
        }
        if (o instanceof Function5) {
            return 5;
        }
        if (o instanceof Function6) {
            return 6;
        }
        if (o instanceof Function7) {
            return 7;
        }
        if (o instanceof Function8) {
            return 8;
        }
        if (o instanceof Function9) {
            return 9;
        }
        if (o instanceof Function10) {
            return 10;
        }
        if (o instanceof Function11) {
            return 11;
        }
        if (o instanceof Function12) {
            return 12;
        }
        if (o instanceof Function13) {
            return 13;
        }
        if (o instanceof Function14) {
            return 14;
        }
        if (o instanceof Function15) {
            return 15;
        }
        if (o instanceof Function16) {
            return 16;
        }
        if (o instanceof Function17) {
            return 17;
        }
        if (o instanceof Function18) {
            return 18;
        }
        if (o instanceof Function19) {
            return 19;
        }
        if (o instanceof Function20) {
            return 20;
        }
        if (o instanceof Function21) {
            return 21;
        }
        if (o instanceof Function22) {
            return 22;
        }
        return -1;
    }
    
    public static boolean isFunctionOfArity(final Object o, final int n) {
        return o instanceof Function && getFunctionArity(o) == n;
    }
    
    public static boolean isMutableCollection(final Object o) {
        return o instanceof Collection && (!(o instanceof KMappedMarker) || o instanceof KMutableCollection);
    }
    
    public static boolean isMutableIterable(final Object o) {
        return o instanceof Iterable && (!(o instanceof KMappedMarker) || o instanceof KMutableIterable);
    }
    
    public static boolean isMutableIterator(final Object o) {
        return o instanceof Iterator && (!(o instanceof KMappedMarker) || o instanceof KMutableIterator);
    }
    
    public static boolean isMutableList(final Object o) {
        return o instanceof List && (!(o instanceof KMappedMarker) || o instanceof KMutableList);
    }
    
    public static boolean isMutableListIterator(final Object o) {
        return o instanceof ListIterator && (!(o instanceof KMappedMarker) || o instanceof KMutableListIterator);
    }
    
    public static boolean isMutableMap(final Object o) {
        return o instanceof Map && (!(o instanceof KMappedMarker) || o instanceof KMutableMap);
    }
    
    public static boolean isMutableMapEntry(final Object o) {
        return o instanceof Map.Entry && (!(o instanceof KMappedMarker) || o instanceof KMutableMap.Entry);
    }
    
    public static boolean isMutableSet(final Object o) {
        return o instanceof Set && (!(o instanceof KMappedMarker) || o instanceof KMutableSet);
    }
    
    private static <T extends Throwable> T sanitizeStackTrace(final T t) {
        return Intrinsics.sanitizeStackTrace(t, TypeIntrinsics.class.getName());
    }
    
    public static ClassCastException throwCce(final ClassCastException ex) {
        throw sanitizeStackTrace(ex);
    }
    
    public static void throwCce(final Object o, final String str) {
        String name;
        if (o == null) {
            name = "null";
        }
        else {
            name = o.getClass().getName();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" cannot be cast to ");
        sb.append(str);
        throwCce(sb.toString());
    }
    
    public static void throwCce(final String s) {
        throw throwCce(new ClassCastException(s));
    }
}
