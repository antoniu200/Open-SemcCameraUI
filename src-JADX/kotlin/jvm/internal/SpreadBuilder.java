package kotlin.jvm.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SpreadBuilder {
    private final ArrayList<Object> list;

    public SpreadBuilder(int i) {
        this.list = new ArrayList<>(i);
    }

    public void addSpread(Object obj) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Object[]) {
            Object[] objArr = (Object[]) obj;
            if (objArr.length > 0) {
                this.list.ensureCapacity(this.list.size() + objArr.length);
                for (Object obj2 : objArr) {
                    this.list.add(obj2);
                }
                return;
            }
            return;
        }
        if (obj instanceof Collection) {
            this.list.addAll((Collection) obj);
            return;
        }
        if (obj instanceof Iterable) {
            Iterator it = ((Iterable) obj).iterator();
            while (it.hasNext()) {
                this.list.add(it.next());
            }
            return;
        }
        if (obj instanceof Iterator) {
            Iterator it2 = (Iterator) obj;
            while (it2.hasNext()) {
                this.list.add(it2.next());
            }
        } else {
            throw new UnsupportedOperationException("Don't know how to spread " + obj.getClass());
        }
    }

    public int size() {
        return this.list.size();
    }

    public void add(Object obj) {
        this.list.add(obj);
    }

    public Object[] toArray(Object[] objArr) {
        return this.list.toArray(objArr);
    }
}
