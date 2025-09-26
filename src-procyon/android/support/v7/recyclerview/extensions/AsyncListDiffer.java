// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.recyclerview.extensions;

import android.os.Looper;
import android.os.Handler;
import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import java.util.Collections;
import android.support.v7.util.ListUpdateCallback;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.List;
import java.util.concurrent.Executor;

public class AsyncListDiffer<T>
{
    private static final Executor sMainThreadExecutor;
    final AsyncDifferConfig<T> mConfig;
    @Nullable
    private List<T> mList;
    final Executor mMainThreadExecutor;
    int mMaxScheduledGeneration;
    @NonNull
    private List<T> mReadOnlyList;
    private final ListUpdateCallback mUpdateCallback;
    
    static {
        sMainThreadExecutor = new MainThreadExecutor();
    }
    
    public AsyncListDiffer(@NonNull final ListUpdateCallback mUpdateCallback, @NonNull final AsyncDifferConfig<T> mConfig) {
        this.mReadOnlyList = Collections.emptyList();
        this.mUpdateCallback = mUpdateCallback;
        this.mConfig = mConfig;
        if (mConfig.getMainThreadExecutor() != null) {
            this.mMainThreadExecutor = mConfig.getMainThreadExecutor();
        }
        else {
            this.mMainThreadExecutor = AsyncListDiffer.sMainThreadExecutor;
        }
    }
    
    public AsyncListDiffer(@NonNull final RecyclerView.Adapter adapter, @NonNull final DiffUtil.ItemCallback<T> itemCallback) {
        this(new AdapterListUpdateCallback(adapter), new AsyncDifferConfig.Builder<T>(itemCallback).build());
    }
    
    @NonNull
    public List<T> getCurrentList() {
        return this.mReadOnlyList;
    }
    
    void latchList(@NonNull final List<T> list, @NonNull final DiffUtil.DiffResult diffResult) {
        this.mList = list;
        this.mReadOnlyList = Collections.unmodifiableList((List<? extends T>)list);
        diffResult.dispatchUpdatesTo(this.mUpdateCallback);
    }
    
    public void submitList(@Nullable final List<T> list) {
        final int mMaxScheduledGeneration = this.mMaxScheduledGeneration + 1;
        this.mMaxScheduledGeneration = mMaxScheduledGeneration;
        if (list == this.mList) {
            return;
        }
        if (list == null) {
            final int size = this.mList.size();
            this.mList = null;
            this.mReadOnlyList = Collections.emptyList();
            this.mUpdateCallback.onRemoved(0, size);
            return;
        }
        if (this.mList == null) {
            this.mList = list;
            this.mReadOnlyList = Collections.unmodifiableList((List<? extends T>)list);
            this.mUpdateCallback.onInserted(0, list.size());
            return;
        }
        this.mConfig.getBackgroundThreadExecutor().execute(new Runnable(this, this.mList, list, mMaxScheduledGeneration) {
            final AsyncListDiffer this$0;
            final List val$newList;
            final List val$oldList;
            final int val$runGeneration;
            
            @Override
            public void run() {
                this.this$0.mMainThreadExecutor.execute(new Runnable(this, DiffUtil.calculateDiff((DiffUtil.Callback)new DiffUtil.Callback(this) {
                    final AsyncListDiffer$1 this$1;
                    
                    @Override
                    public boolean areContentsTheSame(final int n, final int n2) {
                        final T value = this.this$1.val$oldList.get(n);
                        final T value2 = this.this$1.val$newList.get(n2);
                        if (value != null && value2 != null) {
                            return this.this$1.this$0.mConfig.getDiffCallback().areContentsTheSame(value, value2);
                        }
                        if (value == null && value2 == null) {
                            return true;
                        }
                        throw new AssertionError();
                    }
                    
                    @Override
                    public boolean areItemsTheSame(final int n, final int n2) {
                        final T value = this.this$1.val$oldList.get(n);
                        final T value2 = this.this$1.val$newList.get(n2);
                        if (value != null && value2 != null) {
                            return this.this$1.this$0.mConfig.getDiffCallback().areItemsTheSame(value, value2);
                        }
                        return value == null && value2 == null;
                    }
                    
                    @Nullable
                    @Override
                    public Object getChangePayload(final int n, final int n2) {
                        final T value = this.this$1.val$oldList.get(n);
                        final T value2 = this.this$1.val$newList.get(n2);
                        if (value != null && value2 != null) {
                            return this.this$1.this$0.mConfig.getDiffCallback().getChangePayload(value, value2);
                        }
                        throw new AssertionError();
                    }
                    
                    @Override
                    public int getNewListSize() {
                        return this.this$1.val$newList.size();
                    }
                    
                    @Override
                    public int getOldListSize() {
                        return this.this$1.val$oldList.size();
                    }
                })) {
                    final AsyncListDiffer$1 this$1;
                    final DiffUtil.DiffResult val$result;
                    
                    @Override
                    public void run() {
                        if (this.this$1.this$0.mMaxScheduledGeneration == this.this$1.val$runGeneration) {
                            this.this$1.this$0.latchList(this.this$1.val$newList, this.val$result);
                        }
                    }
                });
            }
        });
    }
    
    private static class MainThreadExecutor implements Executor
    {
        final Handler mHandler;
        
        MainThreadExecutor() {
            this.mHandler = new Handler(Looper.getMainLooper());
        }
        
        @Override
        public void execute(@NonNull final Runnable runnable) {
            this.mHandler.post(runnable);
        }
    }
}
