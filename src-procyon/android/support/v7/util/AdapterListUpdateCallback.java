// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

public final class AdapterListUpdateCallback implements ListUpdateCallback
{
    @NonNull
    private final RecyclerView.Adapter mAdapter;
    
    public AdapterListUpdateCallback(@NonNull final RecyclerView.Adapter mAdapter) {
        this.mAdapter = mAdapter;
    }
    
    @Override
    public void onChanged(final int n, final int n2, final Object o) {
        this.mAdapter.notifyItemRangeChanged(n, n2, o);
    }
    
    @Override
    public void onInserted(final int n, final int n2) {
        this.mAdapter.notifyItemRangeInserted(n, n2);
    }
    
    @Override
    public void onMoved(final int n, final int n2) {
        this.mAdapter.notifyItemMoved(n, n2);
    }
    
    @Override
    public void onRemoved(final int n, final int n2) {
        this.mAdapter.notifyItemRangeRemoved(n, n2);
    }
}
