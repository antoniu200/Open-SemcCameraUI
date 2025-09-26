// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.widget;

import android.support.annotation.Nullable;
import java.util.List;
import android.support.annotation.NonNull;
import java.util.HashSet;
import android.support.v4.util.Pools;
import java.util.ArrayList;
import android.support.v4.util.SimpleArrayMap;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY })
public final class DirectedAcyclicGraph<T>
{
    private final SimpleArrayMap<T, ArrayList<T>> mGraph;
    private final Pools.Pool<ArrayList<T>> mListPool;
    private final ArrayList<T> mSortResult;
    private final HashSet<T> mSortTmpMarked;
    
    public DirectedAcyclicGraph() {
        this.mListPool = new Pools.SimplePool<ArrayList<T>>(10);
        this.mGraph = new SimpleArrayMap<T, ArrayList<T>>();
        this.mSortResult = new ArrayList<T>();
        this.mSortTmpMarked = new HashSet<T>();
    }
    
    private void dfs(final T e, final ArrayList<T> list, final HashSet<T> set) {
        if (list.contains(e)) {
            return;
        }
        if (set.contains(e)) {
            throw new RuntimeException("This graph contains cyclic dependencies");
        }
        set.add(e);
        final ArrayList list2 = this.mGraph.get(e);
        if (list2 != null) {
            for (int i = 0; i < list2.size(); ++i) {
                this.dfs(list2.get(i), (ArrayList<Object>)list, (HashSet<Object>)set);
            }
        }
        set.remove(e);
        list.add(e);
    }
    
    @NonNull
    private ArrayList<T> getEmptyList() {
        ArrayList list;
        if ((list = this.mListPool.acquire()) == null) {
            list = new ArrayList();
        }
        return list;
    }
    
    private void poolList(@NonNull final ArrayList<T> list) {
        list.clear();
        this.mListPool.release(list);
    }
    
    public void addEdge(@NonNull final T t, @NonNull final T e) {
        if (this.mGraph.containsKey(t) && this.mGraph.containsKey(e)) {
            ArrayList<T> emptyList;
            if ((emptyList = this.mGraph.get(t)) == null) {
                emptyList = this.getEmptyList();
                this.mGraph.put(t, emptyList);
            }
            emptyList.add(e);
            return;
        }
        throw new IllegalArgumentException("All nodes must be present in the graph before being added as an edge");
    }
    
    public void addNode(@NonNull final T t) {
        if (!this.mGraph.containsKey(t)) {
            this.mGraph.put(t, null);
        }
    }
    
    public void clear() {
        for (int size = this.mGraph.size(), i = 0; i < size; ++i) {
            final ArrayList list = this.mGraph.valueAt(i);
            if (list != null) {
                this.poolList(list);
            }
        }
        this.mGraph.clear();
    }
    
    public boolean contains(@NonNull final T t) {
        return this.mGraph.containsKey(t);
    }
    
    @Nullable
    public List getIncomingEdges(@NonNull final T t) {
        return this.mGraph.get(t);
    }
    
    @Nullable
    public List<T> getOutgoingEdges(@NonNull final T o) {
        final int size = this.mGraph.size();
        ArrayList<T> list = null;
        ArrayList<T> list3;
        for (int i = 0; i < size; ++i, list = list3) {
            final ArrayList list2 = this.mGraph.valueAt(i);
            list3 = list;
            if (list2 != null) {
                list3 = list;
                if (list2.contains(o)) {
                    if ((list3 = list) == null) {
                        list3 = new ArrayList<T>();
                    }
                    list3.add(this.mGraph.keyAt(i));
                }
            }
        }
        return list;
    }
    
    @NonNull
    public ArrayList<T> getSortedList() {
        this.mSortResult.clear();
        this.mSortTmpMarked.clear();
        for (int size = this.mGraph.size(), i = 0; i < size; ++i) {
            this.dfs(this.mGraph.keyAt(i), this.mSortResult, this.mSortTmpMarked);
        }
        return this.mSortResult;
    }
    
    public boolean hasOutgoingEdges(@NonNull final T o) {
        for (int size = this.mGraph.size(), i = 0; i < size; ++i) {
            final ArrayList list = this.mGraph.valueAt(i);
            if (list != null && list.contains(o)) {
                return true;
            }
        }
        return false;
    }
    
    int size() {
        return this.mGraph.size();
    }
}
