// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.collections;

import kotlin.jvm.functions.Function1;
import kotlin.TypeCastException;
import org.jetbrains.annotations.Nullable;
import java.util.Iterator;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Set;
import kotlin.SinceKotlin;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;
import java.util.Map;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000D\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\u0002\n\u0002\u0010\"\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010&\n\u0002\b\b\n\u0002\u0010\u0000\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0003\b'\u0018\u0000 )*\u0004\b\u0000\u0010\u0001*\u0006\b\u0001\u0010\u0002 \u00012\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u0003:\u0001)B\u0007\b\u0004¢\u0006\u0002\u0010\u0004J\u001f\u0010\u0013\u001a\u00020\u00142\u0010\u0010\u0015\u001a\f\u0012\u0002\b\u0003\u0012\u0002\b\u0003\u0018\u00010\u0016H\u0000¢\u0006\u0002\b\u0017J\u0015\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u001aJ\u0015\u0010\u001b\u001a\u00020\u00142\u0006\u0010\u001c\u001a\u00028\u0001H\u0016¢\u0006\u0002\u0010\u001aJ\u0013\u0010\u001d\u001a\u00020\u00142\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0096\u0002J\u0018\u0010 \u001a\u0004\u0018\u00018\u00012\u0006\u0010\u0019\u001a\u00028\u0000H\u0096\u0002¢\u0006\u0002\u0010!J\b\u0010\"\u001a\u00020\rH\u0016J#\u0010#\u001a\u0010\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u0001\u0018\u00010\u00162\u0006\u0010\u0019\u001a\u00028\u0000H\u0002¢\u0006\u0002\u0010$J\b\u0010%\u001a\u00020\u0014H\u0016J\b\u0010&\u001a\u00020'H\u0016J\u0012\u0010&\u001a\u00020'2\b\u0010(\u001a\u0004\u0018\u00010\u001fH\u0002J\u001c\u0010&\u001a\u00020'2\u0012\u0010\u0015\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0016H\bR\u001a\u0010\u0005\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u00068\u0002@\u0002X\u0083\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\n\u0012\u0004\u0012\u00028\u0001\u0018\u00010\b8\b@\bX\u0089\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\t\u001a\b\u0012\u0004\u0012\u00028\u00000\u00068VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u0014\u0010\f\u001a\u00020\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u001a\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00010\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012¨\u0006*" }, d2 = { "Lkotlin/collections/AbstractMap;", "K", "V", "", "()V", "_keys", "", "_values", "", "keys", "getKeys", "()Ljava/util/Set;", "size", "", "getSize", "()I", "values", "getValues", "()Ljava/util/Collection;", "containsEntry", "", "entry", "", "containsEntry$kotlin_stdlib", "containsKey", "key", "(Ljava/lang/Object;)Z", "containsValue", "value", "equals", "other", "", "get", "(Ljava/lang/Object;)Ljava/lang/Object;", "hashCode", "implFindEntry", "(Ljava/lang/Object;)Ljava/util/Map$Entry;", "isEmpty", "toString", "", "o", "Companion", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
@SinceKotlin(version = "1.1")
public abstract class AbstractMap<K, V> implements Map<K, V>, KMappedMarker
{
    public static final Companion Companion;
    private volatile Set<? extends K> _keys;
    private volatile Collection<? extends V> _values;
    
    static {
        Companion = new Companion(null);
    }
    
    protected AbstractMap() {
    }
    
    private final Entry<K, V> implFindEntry(final K k) {
        for (final Object next : this.entrySet()) {
            if (Intrinsics.areEqual(((Entry<Object, V>)next).getKey(), k)) {
                final Entry<Object, V> entry = (Entry<Object, V>)next;
                return (Entry)entry;
            }
        }
        final Entry<Object, V> entry = null;
        return (Entry)entry;
    }
    
    private final String toString(final Object obj) {
        String value;
        if (obj == this) {
            value = "(this Map)";
        }
        else {
            value = String.valueOf(obj);
        }
        return value;
    }
    
    private final String toString(final Entry<? extends K, ? extends V> entry) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.toString(entry.getKey()));
        sb.append("=");
        sb.append(this.toString(entry.getValue()));
        return sb.toString();
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    public final boolean containsEntry$kotlin_stdlib(@Nullable final Entry<?, ?> entry) {
        if (!(entry instanceof Entry)) {
            return false;
        }
        final Object key = entry.getKey();
        final Object value = entry.getValue();
        if (this == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.Map<K, V>");
        }
        final Map map = this;
        final Object value2 = map.get(key);
        if (Intrinsics.areEqual(value, value2) ^ true) {
            return false;
        }
        if (value2 == null) {
            if (this == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.collections.Map<K, *>");
            }
            if (!map.containsKey(key)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean containsKey(final Object o) {
        return this.implFindEntry(o) != null;
    }
    
    @Override
    public boolean containsValue(final Object o) {
        final Iterable iterable = this.entrySet();
        final boolean b = iterable instanceof Collection;
        final boolean b2 = false;
        boolean b3;
        if (b && ((Collection)iterable).isEmpty()) {
            b3 = b2;
        }
        else {
            final Iterator iterator = iterable.iterator();
            do {
                b3 = b2;
                if (iterator.hasNext()) {
                    continue;
                }
                return b3;
            } while (!Intrinsics.areEqual(((Entry<K, Object>)iterator.next()).getValue(), o));
            b3 = true;
        }
        return b3;
    }
    
    @Override
    public final /* bridge */ Set<Entry<K, V>> entrySet() {
        return this.getEntries();
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        final AbstractMap abstractMap = this;
        final boolean b = true;
        if (o == abstractMap) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        final int size = this.size();
        final Map map = (Map)o;
        if (size != map.size()) {
            return false;
        }
        final Iterable iterable = map.entrySet();
        boolean b2;
        if (iterable instanceof Collection && ((Collection)iterable).isEmpty()) {
            b2 = b;
        }
        else {
            final Iterator iterator = iterable.iterator();
            do {
                b2 = b;
                if (iterator.hasNext()) {
                    continue;
                }
                return b2;
            } while (this.containsEntry$kotlin_stdlib((Entry<?, ?>)iterator.next()));
            b2 = false;
        }
        return b2;
    }
    
    @Nullable
    @Override
    public V get(Object value) {
        final Entry<K, Object> implFindEntry = this.implFindEntry((K)value);
        if (implFindEntry != null) {
            value = implFindEntry.getValue();
        }
        else {
            value = null;
        }
        return (V)value;
    }
    
    public abstract Set getEntries();
    
    @NotNull
    public Set<K> getKeys() {
        if (this._keys == null) {
            this._keys = (Set<? extends K>)new AbstractMap$keys.AbstractMap$keys$1(this);
        }
        final Set<? extends K> keys = this._keys;
        if (keys == null) {
            Intrinsics.throwNpe();
        }
        return (Set<K>)keys;
    }
    
    public int getSize() {
        return this.entrySet().size();
    }
    
    @NotNull
    public Collection<V> getValues() {
        if (this._values == null) {
            this._values = (Collection<? extends V>)new AbstractMap$values.AbstractMap$values$1(this);
        }
        final Collection<? extends V> values = this._values;
        if (values == null) {
            Intrinsics.throwNpe();
        }
        return (Collection<V>)values;
    }
    
    @Override
    public int hashCode() {
        return this.entrySet().hashCode();
    }
    
    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }
    
    @Override
    public final /* bridge */ Set<K> keySet() {
        return this.getKeys();
    }
    
    @Override
    public V put(final K k, final V v) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public void putAll(final Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public V remove(final Object o) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }
    
    @Override
    public final /* bridge */ int size() {
        return this.getSize();
    }
    
    @NotNull
    @Override
    public String toString() {
        return CollectionsKt___CollectionsKt.joinToString$default((Iterable)this.entrySet(), (CharSequence)", ", (CharSequence)"{", (CharSequence)"}", 0, (CharSequence)null, (Function1)new AbstractMap$toString.AbstractMap$toString$1(this), 24, (Object)null);
    }
    
    @Override
    public final /* bridge */ Collection<V> values() {
        return this.getValues();
    }
    
    @Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010&\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J'\u0010\u0003\u001a\u00020\u00042\u000e\u0010\u0005\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\u0001H\u0000¢\u0006\u0002\b\bJ\u001d\u0010\t\u001a\u00020\n2\u000e\u0010\u0005\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0006H\u0000¢\u0006\u0002\b\u000bJ\u001d\u0010\f\u001a\u00020\r2\u000e\u0010\u0005\u001a\n\u0012\u0002\b\u0003\u0012\u0002\b\u00030\u0006H\u0000¢\u0006\u0002\b\u000e¨\u0006\u000f" }, d2 = { "Lkotlin/collections/AbstractMap$Companion;", "", "()V", "entryEquals", "", "e", "", "other", "entryEquals$kotlin_stdlib", "entryHashCode", "", "entryHashCode$kotlin_stdlib", "entryToString", "", "entryToString$kotlin_stdlib", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
    public static final class Companion
    {
        private Companion() {
        }
        
        public final boolean entryEquals$kotlin_stdlib(@NotNull final Entry<?, ?> entry, @Nullable final Object o) {
            Intrinsics.checkParameterIsNotNull(entry, "e");
            final boolean b = o instanceof Entry;
            final boolean b2 = false;
            if (!b) {
                return false;
            }
            final Object key = entry.getKey();
            final Entry entry2 = (Entry)o;
            boolean b3 = b2;
            if (Intrinsics.areEqual(key, entry2.getKey())) {
                b3 = b2;
                if (Intrinsics.areEqual(entry.getValue(), entry2.getValue())) {
                    b3 = true;
                }
            }
            return b3;
        }
        
        public final int entryHashCode$kotlin_stdlib(@NotNull final Entry<?, ?> entry) {
            Intrinsics.checkParameterIsNotNull(entry, "e");
            final Object key = entry.getKey();
            int hashCode = 0;
            int hashCode2;
            if (key != null) {
                hashCode2 = key.hashCode();
            }
            else {
                hashCode2 = 0;
            }
            final Object value = entry.getValue();
            if (value != null) {
                hashCode = value.hashCode();
            }
            return hashCode2 ^ hashCode;
        }
        
        @NotNull
        public final String entryToString$kotlin_stdlib(@NotNull final Entry<?, ?> entry) {
            Intrinsics.checkParameterIsNotNull(entry, "e");
            final StringBuilder sb = new StringBuilder();
            sb.append(entry.getKey());
            sb.append('=');
            sb.append(entry.getValue());
            return sb.toString();
        }
    }
}
