package com.duolingo.open.rtlviewpager;

import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class DelegatingPagerAdapter extends PagerAdapter {

    @NonNull
    private final PagerAdapter mDelegate;

    public DelegatingPagerAdapter(@NonNull PagerAdapter pagerAdapter) {
        this.mDelegate = pagerAdapter;
        pagerAdapter.registerDataSetObserver(new MyDataSetObserver());
    }

    @NonNull
    public PagerAdapter getDelegate() {
        return this.mDelegate;
    }

    @Override // android.support.v4.view.PagerAdapter
    public int getCount() {
        return this.mDelegate.getCount();
    }

    @Override // android.support.v4.view.PagerAdapter
    public void startUpdate(ViewGroup viewGroup) {
        this.mDelegate.startUpdate(viewGroup);
    }

    @Override // android.support.v4.view.PagerAdapter
    public Object instantiateItem(ViewGroup viewGroup, int i) {
        return this.mDelegate.instantiateItem(viewGroup, i);
    }

    @Override // android.support.v4.view.PagerAdapter
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        this.mDelegate.destroyItem(viewGroup, i, obj);
    }

    @Override // android.support.v4.view.PagerAdapter
    public void setPrimaryItem(ViewGroup viewGroup, int i, Object obj) {
        this.mDelegate.setPrimaryItem(viewGroup, i, obj);
    }

    @Override // android.support.v4.view.PagerAdapter
    public void finishUpdate(ViewGroup viewGroup) {
        this.mDelegate.finishUpdate(viewGroup);
    }

    @Override // android.support.v4.view.PagerAdapter
    @Deprecated
    public void startUpdate(View view) {
        this.mDelegate.startUpdate(view);
    }

    @Override // android.support.v4.view.PagerAdapter
    @Deprecated
    public Object instantiateItem(View view, int i) {
        return this.mDelegate.instantiateItem(view, i);
    }

    @Override // android.support.v4.view.PagerAdapter
    @Deprecated
    public void destroyItem(View view, int i, Object obj) {
        this.mDelegate.destroyItem(view, i, obj);
    }

    @Override // android.support.v4.view.PagerAdapter
    @Deprecated
    public void setPrimaryItem(View view, int i, Object obj) {
        this.mDelegate.setPrimaryItem(view, i, obj);
    }

    @Override // android.support.v4.view.PagerAdapter
    @Deprecated
    public void finishUpdate(View view) {
        this.mDelegate.finishUpdate(view);
    }

    @Override // android.support.v4.view.PagerAdapter
    public boolean isViewFromObject(View view, Object obj) {
        return this.mDelegate.isViewFromObject(view, obj);
    }

    @Override // android.support.v4.view.PagerAdapter
    public Parcelable saveState() {
        return this.mDelegate.saveState();
    }

    @Override // android.support.v4.view.PagerAdapter
    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
        this.mDelegate.restoreState(parcelable, classLoader);
    }

    @Override // android.support.v4.view.PagerAdapter
    public int getItemPosition(Object obj) {
        return this.mDelegate.getItemPosition(obj);
    }

    @Override // android.support.v4.view.PagerAdapter
    public void notifyDataSetChanged() {
        this.mDelegate.notifyDataSetChanged();
    }

    void superNotifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override // android.support.v4.view.PagerAdapter
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        this.mDelegate.registerDataSetObserver(dataSetObserver);
    }

    @Override // android.support.v4.view.PagerAdapter
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        this.mDelegate.unregisterDataSetObserver(dataSetObserver);
    }

    @Override // android.support.v4.view.PagerAdapter
    public CharSequence getPageTitle(int i) {
        return this.mDelegate.getPageTitle(i);
    }

    @Override // android.support.v4.view.PagerAdapter
    public float getPageWidth(int i) {
        return this.mDelegate.getPageWidth(i);
    }

    private static class MyDataSetObserver extends DataSetObserver {
        final DelegatingPagerAdapter mParent;

        private MyDataSetObserver(DelegatingPagerAdapter delegatingPagerAdapter) {
            this.mParent = delegatingPagerAdapter;
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            if (this.mParent != null) {
                this.mParent.superNotifyDataSetChanged();
            }
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            onChanged();
        }
    }
}
