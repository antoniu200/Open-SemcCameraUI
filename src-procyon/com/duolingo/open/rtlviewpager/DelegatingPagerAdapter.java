// 
// Decompiled by Procyon v0.6.0
// 

package com.duolingo.open.rtlviewpager;

import android.os.Parcelable;
import android.view.ViewGroup;
import android.view.View;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;

public class DelegatingPagerAdapter extends PagerAdapter
{
    @NonNull
    private final PagerAdapter mDelegate;
    
    public DelegatingPagerAdapter(@NonNull final PagerAdapter mDelegate) {
        (this.mDelegate = mDelegate).registerDataSetObserver(new MyDataSetObserver(this));
    }
    
    @Deprecated
    @Override
    public void destroyItem(final View view, final int n, final Object o) {
        this.mDelegate.destroyItem(view, n, o);
    }
    
    @Override
    public void destroyItem(final ViewGroup viewGroup, final int n, final Object o) {
        this.mDelegate.destroyItem(viewGroup, n, o);
    }
    
    @Deprecated
    @Override
    public void finishUpdate(final View view) {
        this.mDelegate.finishUpdate(view);
    }
    
    @Override
    public void finishUpdate(final ViewGroup viewGroup) {
        this.mDelegate.finishUpdate(viewGroup);
    }
    
    @Override
    public int getCount() {
        return this.mDelegate.getCount();
    }
    
    @NonNull
    public PagerAdapter getDelegate() {
        return this.mDelegate;
    }
    
    @Override
    public int getItemPosition(final Object o) {
        return this.mDelegate.getItemPosition(o);
    }
    
    @Override
    public CharSequence getPageTitle(final int n) {
        return this.mDelegate.getPageTitle(n);
    }
    
    @Override
    public float getPageWidth(final int n) {
        return this.mDelegate.getPageWidth(n);
    }
    
    @Deprecated
    @Override
    public Object instantiateItem(final View view, final int n) {
        return this.mDelegate.instantiateItem(view, n);
    }
    
    @Override
    public Object instantiateItem(final ViewGroup viewGroup, final int n) {
        return this.mDelegate.instantiateItem(viewGroup, n);
    }
    
    @Override
    public boolean isViewFromObject(final View view, final Object o) {
        return this.mDelegate.isViewFromObject(view, o);
    }
    
    @Override
    public void notifyDataSetChanged() {
        this.mDelegate.notifyDataSetChanged();
    }
    
    @Override
    public void registerDataSetObserver(final DataSetObserver dataSetObserver) {
        this.mDelegate.registerDataSetObserver(dataSetObserver);
    }
    
    @Override
    public void restoreState(final Parcelable parcelable, final ClassLoader classLoader) {
        this.mDelegate.restoreState(parcelable, classLoader);
    }
    
    @Override
    public Parcelable saveState() {
        return this.mDelegate.saveState();
    }
    
    @Deprecated
    @Override
    public void setPrimaryItem(final View view, final int n, final Object o) {
        this.mDelegate.setPrimaryItem(view, n, o);
    }
    
    @Override
    public void setPrimaryItem(final ViewGroup viewGroup, final int n, final Object o) {
        this.mDelegate.setPrimaryItem(viewGroup, n, o);
    }
    
    @Deprecated
    @Override
    public void startUpdate(final View view) {
        this.mDelegate.startUpdate(view);
    }
    
    @Override
    public void startUpdate(final ViewGroup viewGroup) {
        this.mDelegate.startUpdate(viewGroup);
    }
    
    void superNotifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
    
    @Override
    public void unregisterDataSetObserver(final DataSetObserver dataSetObserver) {
        this.mDelegate.unregisterDataSetObserver(dataSetObserver);
    }
    
    private static class MyDataSetObserver extends DataSetObserver
    {
        final DelegatingPagerAdapter mParent;
        
        private MyDataSetObserver(final DelegatingPagerAdapter mParent) {
            this.mParent = mParent;
        }
        
        public void onChanged() {
            if (this.mParent != null) {
                this.mParent.superNotifyDataSetChanged();
            }
        }
        
        public void onInvalidated() {
            this.onChanged();
        }
    }
}
