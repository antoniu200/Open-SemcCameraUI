// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.jvm.internal;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

public class SpreadBuilder
{
    private final ArrayList<Object> list;
    
    public SpreadBuilder(final int initialCapacity) {
        this.list = new ArrayList<Object>(initialCapacity);
    }
    
    public void add(final Object e) {
        this.list.add(e);
    }
    
    public void addSpread(Object e) {
        if (e == null) {
            return;
        }
        if (e instanceof Object[]) {
            final Object[] array = (Object[])e;
            if (array.length > 0) {
                this.list.ensureCapacity(this.list.size() + array.length);
                for (int length = array.length, i = 0; i < length; ++i) {
                    e = array[i];
                    this.list.add(e);
                }
            }
        }
        else if (e instanceof Collection) {
            this.list.addAll((Collection<?>)e);
        }
        else if (e instanceof Iterable) {
            final Iterator iterator = ((Iterable)e).iterator();
            while (iterator.hasNext()) {
                this.list.add(iterator.next());
            }
        }
        else {
            if (!(e instanceof Iterator)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Don't know how to spread ");
                sb.append(e.getClass());
                throw new UnsupportedOperationException(sb.toString());
            }
            final Iterator iterator2 = (Iterator)e;
            while (iterator2.hasNext()) {
                this.list.add(iterator2.next());
            }
        }
    }
    
    public int size() {
        return this.list.size();
    }
    
    public Object[] toArray(final Object[] a) {
        return this.list.toArray(a);
    }
}
