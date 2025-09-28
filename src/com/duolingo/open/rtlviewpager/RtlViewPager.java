package com.duolingo.open.rtlviewpager;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashMap;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RtlViewPager extends ViewPager {
    private int mLayoutDirection;
    private HashMap<ViewPager.OnPageChangeListener, ReversingOnPageChangeListener> mPageChangeListeners;

    public RtlViewPager(Context context) {
        super(context);
        this.mLayoutDirection = 0;
        this.mPageChangeListeners = new HashMap<>();
    }

    public RtlViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLayoutDirection = 0;
        this.mPageChangeListeners = new HashMap<>();
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int i) throws Resources.NotFoundException {
        super.onRtlPropertiesChanged(i);
        int i2 = i != 1 ? 0 : 1;
        if (i2 != this.mLayoutDirection) {
            PagerAdapter adapter = super.getAdapter();
            int currentItem = adapter != null ? getCurrentItem() : 0;
            this.mLayoutDirection = i2;
            if (adapter != null) {
                adapter.notifyDataSetChanged();
                setCurrentItem(currentItem);
            }
        }
    }

    @Override // android.support.v4.view.ViewPager
    public void setAdapter(PagerAdapter pagerAdapter) throws Resources.NotFoundException {
        if (pagerAdapter != null) {
            pagerAdapter = new ReversingAdapter(pagerAdapter);
        }
        super.setAdapter(pagerAdapter);
        setCurrentItem(0);
    }

    @Override // android.support.v4.view.ViewPager
    public PagerAdapter getAdapter() {
        ReversingAdapter reversingAdapter = (ReversingAdapter) super.getAdapter();
        if (reversingAdapter == null) {
            return null;
        }
        return reversingAdapter.getDelegate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isRtl() {
        return this.mLayoutDirection == 1;
    }

    @Override // android.support.v4.view.ViewPager
    public int getCurrentItem() {
        int currentItem = super.getCurrentItem();
        return (super.getAdapter() == null || !isRtl()) ? currentItem : (r1.getCount() - currentItem) - 1;
    }

    @Override // android.support.v4.view.ViewPager
    public void setCurrentItem(int i, boolean z) throws Resources.NotFoundException {
        PagerAdapter adapter = super.getAdapter();
        if (adapter != null && isRtl()) {
            i = (adapter.getCount() - i) - 1;
        }
        super.setCurrentItem(i, z);
    }

    @Override // android.support.v4.view.ViewPager
    public void setCurrentItem(int i) throws Resources.NotFoundException {
        PagerAdapter adapter = super.getAdapter();
        if (adapter != null && isRtl()) {
            i = (adapter.getCount() - i) - 1;
        }
        super.setCurrentItem(i);
    }

    public static class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = ParcelableCompat.newCreator(new ParcelableCompatCreatorCallbacks<SavedState>() { // from class: com.duolingo.open.rtlviewpager.RtlViewPager.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.support.v4.os.ParcelableCompatCreatorCallbacks
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.support.v4.os.ParcelableCompatCreatorCallbacks
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        });
        private final int mLayoutDirection;
        private final Parcelable mViewPagerSavedState;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        private SavedState(Parcelable parcelable, int i) {
            this.mViewPagerSavedState = parcelable;
            this.mLayoutDirection = i;
        }

        private SavedState(Parcel parcel, ClassLoader classLoader) {
            this.mViewPagerSavedState = parcel.readParcelable(classLoader == null ? getClass().getClassLoader() : classLoader);
            this.mLayoutDirection = parcel.readInt();
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeParcelable(this.mViewPagerSavedState, i);
            parcel.writeInt(this.mLayoutDirection);
        }
    }

    @Override // android.support.v4.view.ViewPager, android.view.View
    public Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), this.mLayoutDirection);
    }

    @Override // android.support.v4.view.ViewPager, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) throws Resources.NotFoundException {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        this.mLayoutDirection = savedState.mLayoutDirection;
        super.onRestoreInstanceState(savedState.mViewPagerSavedState);
    }

    @Override // android.support.v4.view.ViewPager
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        super.setOnPageChangeListener(new ReversingOnPageChangeListener(onPageChangeListener));
    }

    @Override // android.support.v4.view.ViewPager
    public void addOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        ReversingOnPageChangeListener reversingOnPageChangeListener = new ReversingOnPageChangeListener(onPageChangeListener);
        this.mPageChangeListeners.put(onPageChangeListener, reversingOnPageChangeListener);
        super.addOnPageChangeListener(reversingOnPageChangeListener);
    }

    @Override // android.support.v4.view.ViewPager
    public void removeOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
        ReversingOnPageChangeListener reversingOnPageChangeListenerRemove = this.mPageChangeListeners.remove(onPageChangeListener);
        if (reversingOnPageChangeListenerRemove != null) {
            super.removeOnPageChangeListener(reversingOnPageChangeListenerRemove);
        }
    }

    @Override // android.support.v4.view.ViewPager
    public void clearOnPageChangeListeners() {
        super.clearOnPageChangeListeners();
        this.mPageChangeListeners.clear();
    }

    @Override // android.support.v4.view.ViewPager, android.view.View
    protected void onMeasure(int i, int i2) throws Resources.NotFoundException {
        if (View.MeasureSpec.getMode(i2) == 0) {
            int i3 = 0;
            for (int i4 = 0; i4 < getChildCount(); i4++) {
                View childAt = getChildAt(i4);
                childAt.measure(i, View.MeasureSpec.makeMeasureSpec(0, 0));
                int measuredHeight = childAt.getMeasuredHeight();
                if (measuredHeight > i3) {
                    i3 = measuredHeight;
                }
            }
            i2 = View.MeasureSpec.makeMeasureSpec(i3, 1073741824);
        }
        super.onMeasure(i, i2);
    }

    private class ReversingOnPageChangeListener implements ViewPager.OnPageChangeListener {
        private final ViewPager.OnPageChangeListener mListener;

        public ReversingOnPageChangeListener(ViewPager.OnPageChangeListener onPageChangeListener) {
            this.mListener = onPageChangeListener;
        }

        @Override // android.support.v4.view.ViewPager.OnPageChangeListener
        public void onPageScrolled(int i, float f, int i2) {
            int width = RtlViewPager.this.getWidth();
            PagerAdapter adapter = RtlViewPager.super.getAdapter();
            if (RtlViewPager.this.isRtl() && adapter != null) {
                int count = adapter.getCount();
                float f2 = width;
                int pageWidth = ((int) ((1.0f - adapter.getPageWidth(i)) * f2)) + i2;
                while (i < count && pageWidth > 0) {
                    i++;
                    pageWidth -= (int) (adapter.getPageWidth(i) * f2);
                }
                i = (count - i) - 1;
                i2 = -pageWidth;
                f = i2 / (f2 * adapter.getPageWidth(i));
            }
            this.mListener.onPageScrolled(i, f, i2);
        }

        @Override // android.support.v4.view.ViewPager.OnPageChangeListener
        public void onPageSelected(int i) {
            PagerAdapter adapter = RtlViewPager.super.getAdapter();
            if (RtlViewPager.this.isRtl() && adapter != null) {
                i = (adapter.getCount() - i) - 1;
            }
            this.mListener.onPageSelected(i);
        }

        @Override // android.support.v4.view.ViewPager.OnPageChangeListener
        public void onPageScrollStateChanged(int i) {
            this.mListener.onPageScrollStateChanged(i);
        }
    }

    private class ReversingAdapter extends DelegatingPagerAdapter {
        public ReversingAdapter(@NonNull PagerAdapter pagerAdapter) {
            super(pagerAdapter);
        }

        @Override // com.duolingo.open.rtlviewpager.DelegatingPagerAdapter, android.support.v4.view.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            if (RtlViewPager.this.isRtl()) {
                i = (getCount() - i) - 1;
            }
            super.destroyItem(viewGroup, i, obj);
        }

        @Override // com.duolingo.open.rtlviewpager.DelegatingPagerAdapter, android.support.v4.view.PagerAdapter
        public void destroyItem(View view, int i, Object obj) {
            if (RtlViewPager.this.isRtl()) {
                i = (getCount() - i) - 1;
            }
            super.destroyItem(view, i, obj);
        }

        @Override // com.duolingo.open.rtlviewpager.DelegatingPagerAdapter, android.support.v4.view.PagerAdapter
        public int getItemPosition(Object obj) {
            int itemPosition = super.getItemPosition(obj);
            if (!RtlViewPager.this.isRtl()) {
                return itemPosition;
            }
            if (itemPosition == -1 || itemPosition == -2) {
                return -2;
            }
            return (getCount() - itemPosition) - 1;
        }

        @Override // com.duolingo.open.rtlviewpager.DelegatingPagerAdapter, android.support.v4.view.PagerAdapter
        public CharSequence getPageTitle(int i) {
            if (RtlViewPager.this.isRtl()) {
                i = (getCount() - i) - 1;
            }
            return super.getPageTitle(i);
        }

        @Override // com.duolingo.open.rtlviewpager.DelegatingPagerAdapter, android.support.v4.view.PagerAdapter
        public float getPageWidth(int i) {
            if (RtlViewPager.this.isRtl()) {
                i = (getCount() - i) - 1;
            }
            return super.getPageWidth(i);
        }

        @Override // com.duolingo.open.rtlviewpager.DelegatingPagerAdapter, android.support.v4.view.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            if (RtlViewPager.this.isRtl()) {
                i = (getCount() - i) - 1;
            }
            return super.instantiateItem(viewGroup, i);
        }

        @Override // com.duolingo.open.rtlviewpager.DelegatingPagerAdapter, android.support.v4.view.PagerAdapter
        public Object instantiateItem(View view, int i) {
            if (RtlViewPager.this.isRtl()) {
                i = (getCount() - i) - 1;
            }
            return super.instantiateItem(view, i);
        }

        @Override // com.duolingo.open.rtlviewpager.DelegatingPagerAdapter, android.support.v4.view.PagerAdapter
        public void setPrimaryItem(View view, int i, Object obj) {
            if (RtlViewPager.this.isRtl()) {
                i = (getCount() - i) - 1;
            }
            super.setPrimaryItem(view, i, obj);
        }

        @Override // com.duolingo.open.rtlviewpager.DelegatingPagerAdapter, android.support.v4.view.PagerAdapter
        public void setPrimaryItem(ViewGroup viewGroup, int i, Object obj) {
            if (RtlViewPager.this.isRtl()) {
                i = (getCount() - i) - 1;
            }
            super.setPrimaryItem(viewGroup, i, obj);
        }
    }
}
