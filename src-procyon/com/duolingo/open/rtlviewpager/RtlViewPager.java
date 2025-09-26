// 
// Decompiled by Procyon v0.6.0
// 

package com.duolingo.open.rtlviewpager;

import android.support.v4.os.ParcelableCompat;
import android.os.Parcel;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.os.Parcelable$Creator;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.os.Parcelable;
import android.view.View;
import android.view.View$MeasureSpec;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.content.Context;
import java.util.HashMap;
import android.support.v4.view.ViewPager;

public class RtlViewPager extends ViewPager
{
    private int mLayoutDirection;
    private HashMap<OnPageChangeListener, ReversingOnPageChangeListener> mPageChangeListeners;
    
    public RtlViewPager(final Context context) {
        super(context);
        this.mLayoutDirection = 0;
        this.mPageChangeListeners = new HashMap<OnPageChangeListener, ReversingOnPageChangeListener>();
    }
    
    public RtlViewPager(final Context context, final AttributeSet set) {
        super(context, set);
        this.mLayoutDirection = 0;
        this.mPageChangeListeners = new HashMap<OnPageChangeListener, ReversingOnPageChangeListener>();
    }
    
    private boolean isRtl() {
        final int mLayoutDirection = this.mLayoutDirection;
        boolean b = true;
        if (mLayoutDirection != 1) {
            b = false;
        }
        return b;
    }
    
    @Override
    public void addOnPageChangeListener(final OnPageChangeListener key) {
        final ReversingOnPageChangeListener value = new ReversingOnPageChangeListener(key);
        this.mPageChangeListeners.put(key, value);
        super.addOnPageChangeListener((OnPageChangeListener)value);
    }
    
    @Override
    public void clearOnPageChangeListeners() {
        super.clearOnPageChangeListeners();
        this.mPageChangeListeners.clear();
    }
    
    @Override
    public PagerAdapter getAdapter() {
        final ReversingAdapter reversingAdapter = (ReversingAdapter)super.getAdapter();
        PagerAdapter delegate;
        if (reversingAdapter == null) {
            delegate = null;
        }
        else {
            delegate = reversingAdapter.getDelegate();
        }
        return delegate;
    }
    
    @Override
    public int getCurrentItem() {
        final int currentItem = super.getCurrentItem();
        final PagerAdapter adapter = super.getAdapter();
        int n = currentItem;
        if (adapter != null) {
            n = currentItem;
            if (this.isRtl()) {
                n = adapter.getCount() - currentItem - 1;
            }
        }
        return n;
    }
    
    @Override
    protected void onMeasure(final int n, int n2) {
        int measureSpec = n2;
        if (View$MeasureSpec.getMode(n2) == 0) {
            int i = 0;
            int n3 = 0;
            while (i < this.getChildCount()) {
                final View child = this.getChildAt(i);
                child.measure(n, View$MeasureSpec.makeMeasureSpec(0, 0));
                final int measuredHeight = child.getMeasuredHeight();
                if (measuredHeight > (n2 = n3)) {
                    n2 = measuredHeight;
                }
                ++i;
                n3 = n2;
            }
            measureSpec = View$MeasureSpec.makeMeasureSpec(n3, 1073741824);
        }
        super.onMeasure(n, measureSpec);
    }
    
    @Override
    public void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        final SavedState savedState = (SavedState)parcelable;
        this.mLayoutDirection = savedState.mLayoutDirection;
        super.onRestoreInstanceState(savedState.mViewPagerSavedState);
    }
    
    public void onRtlPropertiesChanged(int mLayoutDirection) {
        super.onRtlPropertiesChanged(mLayoutDirection);
        int currentItem = 0;
        final int n = 1;
        if (mLayoutDirection == 1) {
            mLayoutDirection = n;
        }
        else {
            mLayoutDirection = 0;
        }
        if (mLayoutDirection != this.mLayoutDirection) {
            final PagerAdapter adapter = super.getAdapter();
            if (adapter != null) {
                currentItem = this.getCurrentItem();
            }
            this.mLayoutDirection = mLayoutDirection;
            if (adapter != null) {
                adapter.notifyDataSetChanged();
                this.setCurrentItem(currentItem);
            }
        }
    }
    
    @Override
    public Parcelable onSaveInstanceState() {
        return (Parcelable)new SavedState(super.onSaveInstanceState(), this.mLayoutDirection);
    }
    
    @Override
    public void removeOnPageChangeListener(final OnPageChangeListener key) {
        final ReversingOnPageChangeListener reversingOnPageChangeListener = this.mPageChangeListeners.remove(key);
        if (reversingOnPageChangeListener != null) {
            super.removeOnPageChangeListener((OnPageChangeListener)reversingOnPageChangeListener);
        }
    }
    
    @Override
    public void setAdapter(final PagerAdapter pagerAdapter) {
        PagerAdapter adapter = pagerAdapter;
        if (pagerAdapter != null) {
            adapter = new ReversingAdapter(pagerAdapter);
        }
        super.setAdapter(adapter);
        this.setCurrentItem(0);
    }
    
    @Override
    public void setCurrentItem(final int n) {
        final PagerAdapter adapter = super.getAdapter();
        int currentItem = n;
        if (adapter != null) {
            currentItem = n;
            if (this.isRtl()) {
                currentItem = adapter.getCount() - n - 1;
            }
        }
        super.setCurrentItem(currentItem);
    }
    
    @Override
    public void setCurrentItem(final int n, final boolean b) {
        final PagerAdapter adapter = super.getAdapter();
        int n2 = n;
        if (adapter != null) {
            n2 = n;
            if (this.isRtl()) {
                n2 = adapter.getCount() - n - 1;
            }
        }
        super.setCurrentItem(n2, b);
    }
    
    @Override
    public void setOnPageChangeListener(final OnPageChangeListener onPageChangeListener) {
        super.setOnPageChangeListener((OnPageChangeListener)new ReversingOnPageChangeListener(onPageChangeListener));
    }
    
    private class ReversingAdapter extends DelegatingPagerAdapter
    {
        final RtlViewPager this$0;
        
        public ReversingAdapter(@NonNull final RtlViewPager this$0, final PagerAdapter pagerAdapter) {
            this.this$0 = this$0;
            super(pagerAdapter);
        }
        
        @Override
        public void destroyItem(final View view, final int n, final Object o) {
            int n2 = n;
            if (this.this$0.isRtl()) {
                n2 = this.getCount() - n - 1;
            }
            super.destroyItem(view, n2, o);
        }
        
        @Override
        public void destroyItem(final ViewGroup viewGroup, final int n, final Object o) {
            int n2 = n;
            if (this.this$0.isRtl()) {
                n2 = this.getCount() - n - 1;
            }
            super.destroyItem(viewGroup, n2, o);
        }
        
        @Override
        public int getItemPosition(final Object o) {
            int itemPosition;
            final int n = itemPosition = super.getItemPosition(o);
            if (this.this$0.isRtl()) {
                if (n != -1 && n != -2) {
                    itemPosition = this.getCount() - n - 1;
                }
                else {
                    itemPosition = -2;
                }
            }
            return itemPosition;
        }
        
        @Override
        public CharSequence getPageTitle(final int n) {
            int n2 = n;
            if (this.this$0.isRtl()) {
                n2 = this.getCount() - n - 1;
            }
            return super.getPageTitle(n2);
        }
        
        @Override
        public float getPageWidth(final int n) {
            int n2 = n;
            if (this.this$0.isRtl()) {
                n2 = this.getCount() - n - 1;
            }
            return super.getPageWidth(n2);
        }
        
        @Override
        public Object instantiateItem(final View view, final int n) {
            int n2 = n;
            if (this.this$0.isRtl()) {
                n2 = this.getCount() - n - 1;
            }
            return super.instantiateItem(view, n2);
        }
        
        @Override
        public Object instantiateItem(final ViewGroup viewGroup, final int n) {
            int n2 = n;
            if (this.this$0.isRtl()) {
                n2 = this.getCount() - n - 1;
            }
            return super.instantiateItem(viewGroup, n2);
        }
        
        @Override
        public void setPrimaryItem(final View view, final int n, final Object o) {
            int n2 = n;
            if (this.this$0.isRtl()) {
                n2 = this.getCount() - n - 1;
            }
            super.setPrimaryItem(view, n2, o);
        }
        
        @Override
        public void setPrimaryItem(final ViewGroup viewGroup, final int n, final Object o) {
            int n2 = n;
            if (this.this$0.isRtl()) {
                n2 = this.getCount() - n - 1;
            }
            super.setPrimaryItem(viewGroup, n2, o);
        }
    }
    
    private class ReversingOnPageChangeListener implements OnPageChangeListener
    {
        private final OnPageChangeListener mListener;
        final RtlViewPager this$0;
        
        public ReversingOnPageChangeListener(final RtlViewPager this$0, final OnPageChangeListener mListener) {
            this.this$0 = this$0;
            this.mListener = mListener;
        }
        
        @Override
        public void onPageScrollStateChanged(final int n) {
            this.mListener.onPageScrollStateChanged(n);
        }
        
        @Override
        public void onPageScrolled(int n, float n2, int n3) {
            final int width = this.this$0.getWidth();
            final PagerAdapter access$401 = this.this$0.getAdapter();
            int n4 = n;
            float n5 = n2;
            int n6 = n3;
            if (this.this$0.isRtl()) {
                n4 = n;
                n5 = n2;
                n6 = n3;
                if (access$401 != null) {
                    final int count = access$401.getCount();
                    n2 = (float)width;
                    final int n7 = (int)((1.0f - access$401.getPageWidth(n)) * n2) + n3;
                    for (n3 = n, n = n7; n3 < count && n > 0; ++n3, n -= (int)(access$401.getPageWidth(n3) * n2)) {}
                    n4 = count - n3 - 1;
                    n6 = -n;
                    n5 = n6 / (n2 * access$401.getPageWidth(n4));
                }
            }
            this.mListener.onPageScrolled(n4, n5, n6);
        }
        
        @Override
        public void onPageSelected(final int n) {
            final PagerAdapter access$601 = this.this$0.getAdapter();
            int n2 = n;
            if (this.this$0.isRtl()) {
                n2 = n;
                if (access$601 != null) {
                    n2 = access$601.getCount() - n - 1;
                }
            }
            this.mListener.onPageSelected(n2);
        }
    }
    
    public static class SavedState implements Parcelable
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        private final int mLayoutDirection;
        private final Parcelable mViewPagerSavedState;
        
        static {
            CREATOR = ParcelableCompat.newCreator((ParcelableCompatCreatorCallbacks<SavedState>)new ParcelableCompatCreatorCallbacks<SavedState>() {
                @Override
                public SavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                    return new SavedState(parcel, classLoader);
                }
                
                @Override
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            });
        }
        
        private SavedState(final Parcel parcel, final ClassLoader classLoader) {
            ClassLoader classLoader2 = classLoader;
            if (classLoader == null) {
                classLoader2 = this.getClass().getClassLoader();
            }
            this.mViewPagerSavedState = parcel.readParcelable(classLoader2);
            this.mLayoutDirection = parcel.readInt();
        }
        
        private SavedState(final Parcelable mViewPagerSavedState, final int mLayoutDirection) {
            this.mViewPagerSavedState = mViewPagerSavedState;
            this.mLayoutDirection = mLayoutDirection;
        }
        
        public int describeContents() {
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeParcelable(this.mViewPagerSavedState, n);
            parcel.writeInt(this.mLayoutDirection);
        }
    }
}
