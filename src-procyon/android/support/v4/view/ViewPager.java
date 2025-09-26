// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.view;

import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.support.annotation.Px;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.content.res.TypedArray;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Inherited;
import android.support.v4.content.ContextCompat;
import android.support.annotation.DrawableRes;
import android.database.DataSetObserver;
import android.content.res.Resources$NotFoundException;
import android.support.annotation.CallSuper;
import android.view.View$MeasureSpec;
import android.view.ViewConfiguration;
import android.graphics.Canvas;
import android.view.accessibility.AccessibilityEvent;
import android.view.KeyEvent;
import android.os.SystemClock;
import android.view.SoundEffectConstants;
import android.view.FocusFinder;
import android.util.Log;
import android.view.ViewGroup$LayoutParams;
import java.util.Collections;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.support.annotation.NonNull;
import android.content.Context;
import android.view.VelocityTracker;
import android.graphics.Rect;
import android.widget.Scroller;
import android.os.Parcelable;
import android.graphics.drawable.Drawable;
import android.widget.EdgeEffect;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import android.view.animation.Interpolator;
import java.util.Comparator;
import android.view.ViewGroup;

public class ViewPager extends ViewGroup
{
    private static final int CLOSE_ENOUGH = 2;
    private static final Comparator<ItemInfo> COMPARATOR;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    private static final int DRAW_ORDER_DEFAULT = 0;
    private static final int DRAW_ORDER_FORWARD = 1;
    private static final int DRAW_ORDER_REVERSE = 2;
    private static final int INVALID_POINTER = -1;
    static final int[] LAYOUT_ATTRS;
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int MIN_FLING_VELOCITY = 400;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "ViewPager";
    private static final boolean USE_CACHE = false;
    private static final Interpolator sInterpolator;
    private static final ViewPositionComparator sPositionComparator;
    private int mActivePointerId;
    PagerAdapter mAdapter;
    private List<OnAdapterChangeListener> mAdapterChangeListeners;
    private int mBottomPageBounds;
    private boolean mCalledSuper;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCloseEnough;
    int mCurItem;
    private int mDecorChildCount;
    private int mDefaultGutterSize;
    private int mDrawingOrder;
    private ArrayList<View> mDrawingOrderedChildren;
    private final Runnable mEndScrollRunnable;
    private int mExpectedAdapterCount;
    private long mFakeDragBeginTime;
    private boolean mFakeDragging;
    private boolean mFirstLayout;
    private float mFirstOffset;
    private int mFlingDistance;
    private int mGutterSize;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsScrollStarted;
    private boolean mIsUnableToDrag;
    private final ArrayList<ItemInfo> mItems;
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset;
    private EdgeEffect mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedCalculatePageOffsets;
    private PagerObserver mObserver;
    private int mOffscreenPageLimit;
    private OnPageChangeListener mOnPageChangeListener;
    private List<OnPageChangeListener> mOnPageChangeListeners;
    private int mPageMargin;
    private PageTransformer mPageTransformer;
    private int mPageTransformerLayerType;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState;
    private ClassLoader mRestoredClassLoader;
    private int mRestoredCurItem;
    private EdgeEffect mRightEdge;
    private int mScrollState;
    private Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private final ItemInfo mTempItem;
    private final Rect mTempRect;
    private int mTopPageBounds;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    
    static {
        LAYOUT_ATTRS = new int[] { 16842931 };
        COMPARATOR = new Comparator<ItemInfo>() {
            @Override
            public int compare(final ItemInfo itemInfo, final ItemInfo itemInfo2) {
                return itemInfo.position - itemInfo2.position;
            }
        };
        sInterpolator = (Interpolator)new Interpolator() {
            public float getInterpolation(float n) {
                --n;
                return n * n * n * n * n + 1.0f;
            }
        };
        sPositionComparator = new ViewPositionComparator();
    }
    
    public ViewPager(@NonNull final Context context) {
        super(context);
        this.mItems = new ArrayList<ItemInfo>();
        this.mTempItem = new ItemInfo();
        this.mTempRect = new Rect();
        this.mRestoredCurItem = -1;
        this.mRestoredAdapterState = null;
        this.mRestoredClassLoader = null;
        this.mFirstOffset = -3.4028235E38f;
        this.mLastOffset = Float.MAX_VALUE;
        this.mOffscreenPageLimit = 1;
        this.mActivePointerId = -1;
        this.mFirstLayout = true;
        this.mNeedCalculatePageOffsets = false;
        this.mEndScrollRunnable = new Runnable() {
            final ViewPager this$0;
            
            @Override
            public void run() {
                this.this$0.setScrollState(0);
                this.this$0.populate();
            }
        };
        this.mScrollState = 0;
        this.initViewPager();
    }
    
    public ViewPager(@NonNull final Context context, @Nullable final AttributeSet set) {
        super(context, set);
        this.mItems = new ArrayList<ItemInfo>();
        this.mTempItem = new ItemInfo();
        this.mTempRect = new Rect();
        this.mRestoredCurItem = -1;
        this.mRestoredAdapterState = null;
        this.mRestoredClassLoader = null;
        this.mFirstOffset = -3.4028235E38f;
        this.mLastOffset = Float.MAX_VALUE;
        this.mOffscreenPageLimit = 1;
        this.mActivePointerId = -1;
        this.mFirstLayout = true;
        this.mNeedCalculatePageOffsets = false;
        this.mEndScrollRunnable = new Runnable() {
            final ViewPager this$0;
            
            @Override
            public void run() {
                this.this$0.setScrollState(0);
                this.this$0.populate();
            }
        };
        this.mScrollState = 0;
        this.initViewPager();
    }
    
    private void calculatePageOffsets(ItemInfo itemInfo, int i, ItemInfo itemInfo2) {
        int count = this.mAdapter.getCount();
        final int clientWidth = this.getClientWidth();
        float n;
        if (clientWidth > 0) {
            n = this.mPageMargin / (float)clientWidth;
        }
        else {
            n = 0.0f;
        }
        if (itemInfo2 != null) {
            int position = itemInfo2.position;
            if (position < itemInfo.position) {
                float n2 = itemInfo2.offset + itemInfo2.widthFactor + n;
                ++position;
                int j;
                for (int n3 = 0; position <= itemInfo.position && n3 < this.mItems.size(); position = j + 1) {
                    itemInfo2 = this.mItems.get(n3);
                    float offset;
                    while (true) {
                        j = position;
                        offset = n2;
                        if (position <= itemInfo2.position) {
                            break;
                        }
                        j = position;
                        offset = n2;
                        if (n3 >= this.mItems.size() - 1) {
                            break;
                        }
                        ++n3;
                        itemInfo2 = this.mItems.get(n3);
                    }
                    while (j < itemInfo2.position) {
                        offset += this.mAdapter.getPageWidth(j) + n;
                        ++j;
                    }
                    itemInfo2.offset = offset;
                    n2 = offset + (itemInfo2.widthFactor + n);
                }
            }
            else if (position > itemInfo.position) {
                int n4 = this.mItems.size() - 1;
                float offset2 = itemInfo2.offset;
                --position;
                while (position >= itemInfo.position && n4 >= 0) {
                    itemInfo2 = this.mItems.get(n4);
                    int k;
                    float n5;
                    while (true) {
                        k = position;
                        n5 = offset2;
                        if (position >= itemInfo2.position) {
                            break;
                        }
                        k = position;
                        n5 = offset2;
                        if (n4 <= 0) {
                            break;
                        }
                        --n4;
                        itemInfo2 = this.mItems.get(n4);
                    }
                    while (k > itemInfo2.position) {
                        n5 -= this.mAdapter.getPageWidth(k) + n;
                        --k;
                    }
                    offset2 = n5 - (itemInfo2.widthFactor + n);
                    itemInfo2.offset = offset2;
                    position = k - 1;
                }
            }
        }
        final int size = this.mItems.size();
        final float offset3 = itemInfo.offset;
        int l = itemInfo.position - 1;
        float offset4;
        if (itemInfo.position == 0) {
            offset4 = itemInfo.offset;
        }
        else {
            offset4 = -3.4028235E38f;
        }
        this.mFirstOffset = offset4;
        final int position2 = itemInfo.position;
        --count;
        float mLastOffset;
        if (position2 == count) {
            mLastOffset = itemInfo.offset + itemInfo.widthFactor - 1.0f;
        }
        else {
            mLastOffset = Float.MAX_VALUE;
        }
        this.mLastOffset = mLastOffset;
        int index = i - 1;
        float n6 = offset3;
        while (index >= 0) {
            for (itemInfo2 = this.mItems.get(index); l > itemInfo2.position; --l) {
                n6 -= this.mAdapter.getPageWidth(l) + n;
            }
            n6 -= itemInfo2.widthFactor + n;
            itemInfo2.offset = n6;
            if (itemInfo2.position == 0) {
                this.mFirstOffset = n6;
            }
            --index;
            --l;
        }
        float offset5 = itemInfo.offset + itemInfo.widthFactor + n;
        final int n7 = itemInfo.position + 1;
        int index2;
        for (index2 = i + 1, i = n7; index2 < size; ++index2, ++i) {
            for (itemInfo = this.mItems.get(index2); i < itemInfo.position; ++i) {
                offset5 += this.mAdapter.getPageWidth(i) + n;
            }
            if (itemInfo.position == count) {
                this.mLastOffset = itemInfo.widthFactor + offset5 - 1.0f;
            }
            itemInfo.offset = offset5;
            offset5 += itemInfo.widthFactor + n;
        }
        this.mNeedCalculatePageOffsets = false;
    }
    
    private void completeScroll(final boolean b) {
        final boolean b2 = this.mScrollState == 2;
        if (b2) {
            this.setScrollingCacheEnabled(false);
            if (this.mScroller.isFinished() ^ true) {
                this.mScroller.abortAnimation();
                final int scrollX = this.getScrollX();
                final int scrollY = this.getScrollY();
                final int currX = this.mScroller.getCurrX();
                final int currY = this.mScroller.getCurrY();
                if (scrollX != currX || scrollY != currY) {
                    this.scrollTo(currX, currY);
                    if (currX != scrollX) {
                        this.pageScrolled(currX);
                    }
                }
            }
        }
        this.mPopulatePending = false;
        final int n = 0;
        boolean b3 = b2;
        for (int i = n; i < this.mItems.size(); ++i) {
            final ItemInfo itemInfo = this.mItems.get(i);
            if (itemInfo.scrolling) {
                itemInfo.scrolling = false;
                b3 = true;
            }
        }
        if (b3) {
            if (b) {
                ViewCompat.postOnAnimation((View)this, this.mEndScrollRunnable);
            }
            else {
                this.mEndScrollRunnable.run();
            }
        }
    }
    
    private int determineTargetPage(int a, final float n, int max, final int a2) {
        if (Math.abs(a2) > this.mFlingDistance && Math.abs(max) > this.mMinimumVelocity) {
            if (max <= 0) {
                ++a;
            }
        }
        else {
            float n2;
            if (a >= this.mCurItem) {
                n2 = 0.4f;
            }
            else {
                n2 = 0.6f;
            }
            a += (int)(n + n2);
        }
        max = a;
        if (this.mItems.size() > 0) {
            max = Math.max(this.mItems.get(0).position, Math.min(a, this.mItems.get(this.mItems.size() - 1).position));
        }
        return max;
    }
    
    private void dispatchOnPageScrolled(final int n, final float n2, final int n3) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrolled(n, n2, n3);
        }
        if (this.mOnPageChangeListeners != null) {
            for (int i = 0; i < this.mOnPageChangeListeners.size(); ++i) {
                final OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(i);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrolled(n, n2, n3);
                }
            }
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrolled(n, n2, n3);
        }
    }
    
    private void dispatchOnPageSelected(final int n) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageSelected(n);
        }
        if (this.mOnPageChangeListeners != null) {
            for (int i = 0; i < this.mOnPageChangeListeners.size(); ++i) {
                final OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(i);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(n);
                }
            }
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageSelected(n);
        }
    }
    
    private void dispatchOnScrollStateChanged(final int n) {
        if (this.mOnPageChangeListener != null) {
            this.mOnPageChangeListener.onPageScrollStateChanged(n);
        }
        if (this.mOnPageChangeListeners != null) {
            for (int i = 0; i < this.mOnPageChangeListeners.size(); ++i) {
                final OnPageChangeListener onPageChangeListener = this.mOnPageChangeListeners.get(i);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(n);
                }
            }
        }
        if (this.mInternalPageChangeListener != null) {
            this.mInternalPageChangeListener.onPageScrollStateChanged(n);
        }
    }
    
    private void enableLayers(final boolean b) {
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            int mPageTransformerLayerType;
            if (b) {
                mPageTransformerLayerType = this.mPageTransformerLayerType;
            }
            else {
                mPageTransformerLayerType = 0;
            }
            this.getChildAt(i).setLayerType(mPageTransformerLayerType, (Paint)null);
        }
    }
    
    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }
    
    private Rect getChildRectInPagerCoordinates(final Rect rect, final View view) {
        Rect rect2 = rect;
        if (rect == null) {
            rect2 = new Rect();
        }
        if (view == null) {
            rect2.set(0, 0, 0, 0);
            return rect2;
        }
        rect2.left = view.getLeft();
        rect2.right = view.getRight();
        rect2.top = view.getTop();
        rect2.bottom = view.getBottom();
        ViewGroup viewGroup;
        for (ViewParent viewParent = view.getParent(); viewParent instanceof ViewGroup && viewParent != this; viewParent = viewGroup.getParent()) {
            viewGroup = (ViewGroup)viewParent;
            rect2.left += viewGroup.getLeft();
            rect2.right += viewGroup.getRight();
            rect2.top += viewGroup.getTop();
            rect2.bottom += viewGroup.getBottom();
        }
        return rect2;
    }
    
    private int getClientWidth() {
        return this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
    }
    
    private ItemInfo infoForCurrentScrollPosition() {
        final int clientWidth = this.getClientWidth();
        float n;
        if (clientWidth > 0) {
            n = this.getScrollX() / (float)clientWidth;
        }
        else {
            n = 0.0f;
        }
        float n2;
        if (clientWidth > 0) {
            n2 = this.mPageMargin / (float)clientWidth;
        }
        else {
            n2 = 0.0f;
        }
        float offset = 0.0f;
        float widthFactor = 0.0f;
        int i = 0;
        int position = -1;
        ItemInfo itemInfo = null;
        int n3 = 1;
        while (i < this.mItems.size()) {
            final ItemInfo itemInfo2 = this.mItems.get(i);
            int n4 = i;
            ItemInfo mTempItem = itemInfo2;
            if (n3 == 0) {
                final int position2 = itemInfo2.position;
                ++position;
                n4 = i;
                mTempItem = itemInfo2;
                if (position2 != position) {
                    mTempItem = this.mTempItem;
                    mTempItem.offset = offset + widthFactor + n2;
                    mTempItem.position = position;
                    mTempItem.widthFactor = this.mAdapter.getPageWidth(mTempItem.position);
                    n4 = i - 1;
                }
            }
            offset = mTempItem.offset;
            final float widthFactor2 = mTempItem.widthFactor;
            if (n3 == 0 && n < offset) {
                return itemInfo;
            }
            if (n < widthFactor2 + offset + n2 || n4 == this.mItems.size() - 1) {
                return mTempItem;
            }
            position = mTempItem.position;
            widthFactor = mTempItem.widthFactor;
            i = n4 + 1;
            n3 = 0;
            itemInfo = mTempItem;
        }
        return itemInfo;
    }
    
    private static boolean isDecorView(@NonNull final View view) {
        return view.getClass().getAnnotation(DecorView.class) != null;
    }
    
    private boolean isGutterDrag(final float n, final float n2) {
        return (n < this.mGutterSize && n2 > 0.0f) || (n > this.getWidth() - this.mGutterSize && n2 < 0.0f);
    }
    
    private void onSecondaryPointerUp(final MotionEvent motionEvent) {
        final int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) == this.mActivePointerId) {
            int n;
            if (actionIndex == 0) {
                n = 1;
            }
            else {
                n = 0;
            }
            this.mLastMotionX = motionEvent.getX(n);
            this.mActivePointerId = motionEvent.getPointerId(n);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }
    
    private boolean pageScrolled(int n) {
        if (this.mItems.size() == 0) {
            if (this.mFirstLayout) {
                return false;
            }
            this.mCalledSuper = false;
            this.onPageScrolled(0, 0.0f, 0);
            if (!this.mCalledSuper) {
                throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            }
            return false;
        }
        else {
            final ItemInfo infoForCurrentScrollPosition = this.infoForCurrentScrollPosition();
            final int clientWidth = this.getClientWidth();
            final int mPageMargin = this.mPageMargin;
            final float n2 = (float)this.mPageMargin;
            final float n3 = (float)clientWidth;
            final float n4 = n2 / n3;
            final int position = infoForCurrentScrollPosition.position;
            final float n5 = (n / n3 - infoForCurrentScrollPosition.offset) / (infoForCurrentScrollPosition.widthFactor + n4);
            n = (int)((mPageMargin + clientWidth) * n5);
            this.mCalledSuper = false;
            this.onPageScrolled(position, n5, n);
            if (!this.mCalledSuper) {
                throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            }
            return true;
        }
    }
    
    private boolean performDrag(float mLastMotionX) {
        final float mLastMotionX2 = this.mLastMotionX;
        this.mLastMotionX = mLastMotionX;
        final float n = this.getScrollX() + (mLastMotionX2 - mLastMotionX);
        final float n2 = (float)this.getClientWidth();
        mLastMotionX = this.mFirstOffset * n2;
        float n3 = this.mLastOffset * n2;
        final ArrayList<ItemInfo> mItems = this.mItems;
        final boolean b = false;
        final boolean b2 = false;
        boolean b3 = false;
        final ItemInfo itemInfo = mItems.get(0);
        final ItemInfo itemInfo2 = this.mItems.get(this.mItems.size() - 1);
        boolean b4;
        if (itemInfo.position != 0) {
            mLastMotionX = itemInfo.offset * n2;
            b4 = false;
        }
        else {
            b4 = true;
        }
        boolean b5;
        if (itemInfo2.position != this.mAdapter.getCount() - 1) {
            n3 = itemInfo2.offset * n2;
            b5 = false;
        }
        else {
            b5 = true;
        }
        if (n < mLastMotionX) {
            if (b4) {
                this.mLeftEdge.onPull(Math.abs(mLastMotionX - n) / n2);
                b3 = true;
            }
        }
        else {
            b3 = b2;
            mLastMotionX = n;
            if (n > n3) {
                b3 = b;
                if (b5) {
                    this.mRightEdge.onPull(Math.abs(n - n3) / n2);
                    b3 = true;
                }
                mLastMotionX = n3;
            }
        }
        final float mLastMotionX3 = this.mLastMotionX;
        final int n4 = (int)mLastMotionX;
        this.mLastMotionX = mLastMotionX3 + (mLastMotionX - n4);
        this.scrollTo(n4, this.getScrollY());
        this.pageScrolled(n4);
        return b3;
    }
    
    private void recomputeScrollPosition(int n, final int n2, final int n3, final int n4) {
        if (n2 > 0 && !this.mItems.isEmpty()) {
            if (!this.mScroller.isFinished()) {
                this.mScroller.setFinalX(this.getCurrentItem() * this.getClientWidth());
            }
            else {
                this.scrollTo((int)(this.getScrollX() / (float)(n2 - this.getPaddingLeft() - this.getPaddingRight() + n4) * (n - this.getPaddingLeft() - this.getPaddingRight() + n3)), this.getScrollY());
            }
        }
        else {
            final ItemInfo infoForPosition = this.infoForPosition(this.mCurItem);
            float min;
            if (infoForPosition != null) {
                min = Math.min(infoForPosition.offset, this.mLastOffset);
            }
            else {
                min = 0.0f;
            }
            n = (int)(min * (n - this.getPaddingLeft() - this.getPaddingRight()));
            if (n != this.getScrollX()) {
                this.completeScroll(false);
                this.scrollTo(n, this.getScrollY());
            }
        }
    }
    
    private void removeNonDecorViews() {
        int n;
        for (int i = 0; i < this.getChildCount(); i = n + 1) {
            n = i;
            if (!((LayoutParams)this.getChildAt(i).getLayoutParams()).isDecor) {
                this.removeViewAt(i);
                n = i - 1;
            }
        }
    }
    
    private void requestParentDisallowInterceptTouchEvent(final boolean b) {
        final ViewParent parent = this.getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(b);
        }
    }
    
    private boolean resetTouch() {
        this.mActivePointerId = -1;
        this.endDrag();
        this.mLeftEdge.onRelease();
        this.mRightEdge.onRelease();
        return this.mLeftEdge.isFinished() || this.mRightEdge.isFinished();
    }
    
    private void scrollToItem(final int n, final boolean b, final int n2, final boolean b2) {
        final ItemInfo infoForPosition = this.infoForPosition(n);
        int n3;
        if (infoForPosition != null) {
            n3 = (int)(this.getClientWidth() * Math.max(this.mFirstOffset, Math.min(infoForPosition.offset, this.mLastOffset)));
        }
        else {
            n3 = 0;
        }
        if (b) {
            this.smoothScrollTo(n3, 0, n2);
            if (b2) {
                this.dispatchOnPageSelected(n);
            }
        }
        else {
            if (b2) {
                this.dispatchOnPageSelected(n);
            }
            this.completeScroll(false);
            this.scrollTo(n3, 0);
            this.pageScrolled(n3);
        }
    }
    
    private void setScrollingCacheEnabled(final boolean mScrollingCacheEnabled) {
        if (this.mScrollingCacheEnabled != mScrollingCacheEnabled) {
            this.mScrollingCacheEnabled = mScrollingCacheEnabled;
        }
    }
    
    private void sortChildDrawingOrder() {
        if (this.mDrawingOrder != 0) {
            if (this.mDrawingOrderedChildren == null) {
                this.mDrawingOrderedChildren = new ArrayList<View>();
            }
            else {
                this.mDrawingOrderedChildren.clear();
            }
            for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                this.mDrawingOrderedChildren.add(this.getChildAt(i));
            }
            Collections.sort(this.mDrawingOrderedChildren, ViewPager.sPositionComparator);
        }
    }
    
    public void addFocusables(final ArrayList<View> list, final int n, final int n2) {
        final int size = list.size();
        final int descendantFocusability = this.getDescendantFocusability();
        if (descendantFocusability != 393216) {
            for (int i = 0; i < this.getChildCount(); ++i) {
                final View child = this.getChildAt(i);
                if (child.getVisibility() == 0) {
                    final ItemInfo infoForChild = this.infoForChild(child);
                    if (infoForChild != null && infoForChild.position == this.mCurItem) {
                        child.addFocusables((ArrayList)list, n, n2);
                    }
                }
            }
        }
        if (descendantFocusability != 262144 || size == list.size()) {
            if (!this.isFocusable()) {
                return;
            }
            if ((n2 & 0x1) == 0x1 && this.isInTouchMode() && !this.isFocusableInTouchMode()) {
                return;
            }
            if (list != null) {
                list.add((View)this);
            }
        }
    }
    
    ItemInfo addNewItem(final int position, final int index) {
        final ItemInfo itemInfo = new ItemInfo();
        itemInfo.position = position;
        itemInfo.object = this.mAdapter.instantiateItem(this, position);
        itemInfo.widthFactor = this.mAdapter.getPageWidth(position);
        if (index >= 0 && index < this.mItems.size()) {
            this.mItems.add(index, itemInfo);
        }
        else {
            this.mItems.add(itemInfo);
        }
        return itemInfo;
    }
    
    public void addOnAdapterChangeListener(@NonNull final OnAdapterChangeListener onAdapterChangeListener) {
        if (this.mAdapterChangeListeners == null) {
            this.mAdapterChangeListeners = new ArrayList<OnAdapterChangeListener>();
        }
        this.mAdapterChangeListeners.add(onAdapterChangeListener);
    }
    
    public void addOnPageChangeListener(@NonNull final OnPageChangeListener onPageChangeListener) {
        if (this.mOnPageChangeListeners == null) {
            this.mOnPageChangeListeners = new ArrayList<OnPageChangeListener>();
        }
        this.mOnPageChangeListeners.add(onPageChangeListener);
    }
    
    public void addTouchables(final ArrayList<View> list) {
        for (int i = 0; i < this.getChildCount(); ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() == 0) {
                final ItemInfo infoForChild = this.infoForChild(child);
                if (infoForChild != null && infoForChild.position == this.mCurItem) {
                    child.addTouchables((ArrayList)list);
                }
            }
        }
    }
    
    public void addView(final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        ViewGroup$LayoutParams generateLayoutParams = viewGroup$LayoutParams;
        if (!this.checkLayoutParams(viewGroup$LayoutParams)) {
            generateLayoutParams = this.generateLayoutParams(viewGroup$LayoutParams);
        }
        final LayoutParams layoutParams = (LayoutParams)generateLayoutParams;
        layoutParams.isDecor |= isDecorView(view);
        if (this.mInLayout) {
            if (layoutParams != null && layoutParams.isDecor) {
                throw new IllegalStateException("Cannot add pager decor view during layout");
            }
            layoutParams.needsMeasure = true;
            this.addViewInLayout(view, n, generateLayoutParams);
        }
        else {
            super.addView(view, n, generateLayoutParams);
        }
    }
    
    public boolean arrowScroll(final int n) {
        final View focus = this.findFocus();
        boolean b = false;
        final View view = null;
        View view2 = null;
        Label_0198: {
            if (focus == this) {
                view2 = view;
            }
            else {
                Label_0194: {
                    if (focus != null) {
                        ViewParent viewParent = focus.getParent();
                        while (true) {
                            while (viewParent instanceof ViewGroup) {
                                if (viewParent == this) {
                                    final boolean b2 = true;
                                    if (!b2) {
                                        final StringBuilder sb = new StringBuilder();
                                        sb.append(focus.getClass().getSimpleName());
                                        for (ViewParent viewParent2 = focus.getParent(); viewParent2 instanceof ViewGroup; viewParent2 = viewParent2.getParent()) {
                                            sb.append(" => ");
                                            sb.append(viewParent2.getClass().getSimpleName());
                                        }
                                        final StringBuilder sb2 = new StringBuilder();
                                        sb2.append("arrowScroll tried to find focus based on non-child current focused view ");
                                        sb2.append(sb.toString());
                                        Log.e("ViewPager", sb2.toString());
                                        view2 = view;
                                        break Label_0198;
                                    }
                                    break Label_0194;
                                }
                                else {
                                    viewParent = viewParent.getParent();
                                }
                            }
                            final boolean b2 = false;
                            continue;
                        }
                    }
                }
                view2 = focus;
            }
        }
        final View nextFocus = FocusFinder.getInstance().findNextFocus((ViewGroup)this, view2, n);
        if (nextFocus != null && nextFocus != view2) {
            if (n == 17) {
                final int left = this.getChildRectInPagerCoordinates(this.mTempRect, nextFocus).left;
                final int left2 = this.getChildRectInPagerCoordinates(this.mTempRect, view2).left;
                if (view2 != null && left >= left2) {
                    b = this.pageLeft();
                }
                else {
                    b = nextFocus.requestFocus();
                }
            }
            else if (n == 66) {
                final int left3 = this.getChildRectInPagerCoordinates(this.mTempRect, nextFocus).left;
                final int left4 = this.getChildRectInPagerCoordinates(this.mTempRect, view2).left;
                if (view2 != null && left3 <= left4) {
                    b = this.pageRight();
                }
                else {
                    b = nextFocus.requestFocus();
                }
            }
        }
        else if (n != 17 && n != 1) {
            if (n == 66 || n == 2) {
                b = this.pageRight();
            }
        }
        else {
            b = this.pageLeft();
        }
        if (b) {
            this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(n));
        }
        return b;
    }
    
    public boolean beginFakeDrag() {
        if (this.mIsBeingDragged) {
            return false;
        }
        this.mFakeDragging = true;
        this.setScrollState(1);
        this.mLastMotionX = 0.0f;
        this.mInitialMotionX = 0.0f;
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        else {
            this.mVelocityTracker.clear();
        }
        final long uptimeMillis = SystemClock.uptimeMillis();
        final MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 0, 0.0f, 0.0f, 0);
        this.mVelocityTracker.addMovement(obtain);
        obtain.recycle();
        this.mFakeDragBeginTime = uptimeMillis;
        return true;
    }
    
    protected boolean canScroll(final View view, final boolean b, final int n, final int n2, final int n3) {
        final boolean b2 = view instanceof ViewGroup;
        final boolean b3 = true;
        if (b2) {
            final ViewGroup viewGroup = (ViewGroup)view;
            final int scrollX = view.getScrollX();
            final int scrollY = view.getScrollY();
            for (int i = viewGroup.getChildCount() - 1; i >= 0; --i) {
                final View child = viewGroup.getChildAt(i);
                final int n4 = n2 + scrollX;
                if (n4 >= child.getLeft() && n4 < child.getRight()) {
                    final int n5 = n3 + scrollY;
                    if (n5 >= child.getTop() && n5 < child.getBottom() && this.canScroll(child, true, n, n4 - child.getLeft(), n5 - child.getTop())) {
                        return true;
                    }
                }
            }
        }
        return b && view.canScrollHorizontally(-n) && b3;
    }
    
    public boolean canScrollHorizontally(final int n) {
        final PagerAdapter mAdapter = this.mAdapter;
        boolean b = false;
        final boolean b2 = false;
        if (mAdapter == null) {
            return false;
        }
        final int clientWidth = this.getClientWidth();
        final int scrollX = this.getScrollX();
        if (n < 0) {
            boolean b3 = b2;
            if (scrollX > (int)(clientWidth * this.mFirstOffset)) {
                b3 = true;
            }
            return b3;
        }
        if (n > 0) {
            if (scrollX < (int)(clientWidth * this.mLastOffset)) {
                b = true;
            }
            return b;
        }
        return false;
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams && super.checkLayoutParams(viewGroup$LayoutParams);
    }
    
    public void clearOnPageChangeListeners() {
        if (this.mOnPageChangeListeners != null) {
            this.mOnPageChangeListeners.clear();
        }
    }
    
    public void computeScroll() {
        this.mIsScrollStarted = true;
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            final int scrollX = this.getScrollX();
            final int scrollY = this.getScrollY();
            final int currX = this.mScroller.getCurrX();
            final int currY = this.mScroller.getCurrY();
            if (scrollX != currX || scrollY != currY) {
                this.scrollTo(currX, currY);
                if (!this.pageScrolled(currX)) {
                    this.mScroller.abortAnimation();
                    this.scrollTo(0, currY);
                }
            }
            ViewCompat.postInvalidateOnAnimation((View)this);
            return;
        }
        this.completeScroll(true);
    }
    
    void dataSetChanged() {
        final int count = this.mAdapter.getCount();
        this.mExpectedAdapterCount = count;
        final boolean b = this.mItems.size() < this.mOffscreenPageLimit * 2 + 1 && this.mItems.size() < count;
        final int mCurItem = this.mCurItem;
        boolean b2 = b;
        int max = mCurItem;
        int i = 0;
        int n = 0;
        while (i < this.mItems.size()) {
            final ItemInfo itemInfo = this.mItems.get(i);
            final int itemPosition = this.mAdapter.getItemPosition(itemInfo.object);
            int n2 = 0;
            int n3 = 0;
            int n4 = 0;
            Label_0278: {
                if (itemPosition == -1) {
                    n2 = i;
                    n3 = n;
                    n4 = max;
                }
                else {
                    if (itemPosition == -2) {
                        this.mItems.remove(i);
                        final int n5 = i - 1;
                        int n6;
                        if ((n6 = n) == 0) {
                            this.mAdapter.startUpdate(this);
                            n6 = 1;
                        }
                        this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
                        i = n5;
                        n = n6;
                        if (this.mCurItem == itemInfo.position) {
                            max = Math.max(0, Math.min(this.mCurItem, count - 1));
                            n = n6;
                            i = n5;
                        }
                    }
                    else {
                        n2 = i;
                        n3 = n;
                        n4 = max;
                        if (itemInfo.position == itemPosition) {
                            break Label_0278;
                        }
                        if (itemInfo.position == this.mCurItem) {
                            max = itemPosition;
                        }
                        itemInfo.position = itemPosition;
                    }
                    b2 = true;
                    n2 = i;
                    n3 = n;
                    n4 = max;
                }
            }
            i = n2 + 1;
            n = n3;
            max = n4;
        }
        if (n != 0) {
            this.mAdapter.finishUpdate(this);
        }
        Collections.sort(this.mItems, ViewPager.COMPARATOR);
        if (b2) {
            for (int childCount = this.getChildCount(), j = 0; j < childCount; ++j) {
                final LayoutParams layoutParams = (LayoutParams)this.getChildAt(j).getLayoutParams();
                if (!layoutParams.isDecor) {
                    layoutParams.widthFactor = 0.0f;
                }
            }
            this.setCurrentItemInternal(max, false, true);
            this.requestLayout();
        }
    }
    
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent) || this.executeKeyEvent(keyEvent);
    }
    
    public boolean dispatchPopulateAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == 4096) {
            return super.dispatchPopulateAccessibilityEvent(accessibilityEvent);
        }
        for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() == 0) {
                final ItemInfo infoForChild = this.infoForChild(child);
                if (infoForChild != null && infoForChild.position == this.mCurItem && child.dispatchPopulateAccessibilityEvent(accessibilityEvent)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    float distanceInfluenceForSnapDuration(final float n) {
        return (float)Math.sin((n - 0.5f) * 0.47123894f);
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        final int overScrollMode = this.getOverScrollMode();
        int n = 0;
        int n2 = 0;
        if (overScrollMode != 0 && (overScrollMode != 1 || this.mAdapter == null || this.mAdapter.getCount() <= 1)) {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        }
        else {
            if (!this.mLeftEdge.isFinished()) {
                final int save = canvas.save();
                final int n3 = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                final int width = this.getWidth();
                canvas.rotate(270.0f);
                canvas.translate((float)(-n3 + this.getPaddingTop()), this.mFirstOffset * width);
                this.mLeftEdge.setSize(n3, width);
                n2 = ((false | this.mLeftEdge.draw(canvas)) ? 1 : 0);
                canvas.restoreToCount(save);
            }
            n = n2;
            if (!this.mRightEdge.isFinished()) {
                final int save2 = canvas.save();
                final int width2 = this.getWidth();
                final int height = this.getHeight();
                final int paddingTop = this.getPaddingTop();
                final int paddingBottom = this.getPaddingBottom();
                canvas.rotate(90.0f);
                canvas.translate((float)(-this.getPaddingTop()), -(this.mLastOffset + 1.0f) * width2);
                this.mRightEdge.setSize(height - paddingTop - paddingBottom, width2);
                n = (n2 | (this.mRightEdge.draw(canvas) ? 1 : 0));
                canvas.restoreToCount(save2);
            }
        }
        if (n != 0) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final Drawable mMarginDrawable = this.mMarginDrawable;
        if (mMarginDrawable != null && mMarginDrawable.isStateful()) {
            mMarginDrawable.setState(this.getDrawableState());
        }
    }
    
    public void endFakeDrag() {
        if (!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        }
        if (this.mAdapter != null) {
            final VelocityTracker mVelocityTracker = this.mVelocityTracker;
            mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
            final int n = (int)mVelocityTracker.getXVelocity(this.mActivePointerId);
            this.mPopulatePending = true;
            final int clientWidth = this.getClientWidth();
            final int scrollX = this.getScrollX();
            final ItemInfo infoForCurrentScrollPosition = this.infoForCurrentScrollPosition();
            this.setCurrentItemInternal(this.determineTargetPage(infoForCurrentScrollPosition.position, (scrollX / (float)clientWidth - infoForCurrentScrollPosition.offset) / infoForCurrentScrollPosition.widthFactor, n, (int)(this.mLastMotionX - this.mInitialMotionX)), true, true, n);
        }
        this.endDrag();
        this.mFakeDragging = false;
    }
    
    public boolean executeKeyEvent(@NonNull final KeyEvent keyEvent) {
        if (keyEvent.getAction() == 0) {
            final int keyCode = keyEvent.getKeyCode();
            if (keyCode != 61) {
                switch (keyCode) {
                    case 22: {
                        if (keyEvent.hasModifiers(2)) {
                            return this.pageRight();
                        }
                        return this.arrowScroll(66);
                    }
                    case 21: {
                        if (keyEvent.hasModifiers(2)) {
                            return this.pageLeft();
                        }
                        return this.arrowScroll(17);
                    }
                }
            }
            else {
                if (keyEvent.hasNoModifiers()) {
                    return this.arrowScroll(2);
                }
                if (keyEvent.hasModifiers(1)) {
                    return this.arrowScroll(1);
                }
            }
        }
        return false;
    }
    
    public void fakeDragBy(float n) {
        if (!this.mFakeDragging) {
            throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
        }
        if (this.mAdapter == null) {
            return;
        }
        this.mLastMotionX += n;
        final float n2 = this.getScrollX() - n;
        final float n3 = (float)this.getClientWidth();
        n = this.mFirstOffset * n3;
        float n4 = this.mLastOffset * n3;
        final ItemInfo itemInfo = this.mItems.get(0);
        final ItemInfo itemInfo2 = this.mItems.get(this.mItems.size() - 1);
        if (itemInfo.position != 0) {
            n = itemInfo.offset * n3;
        }
        if (itemInfo2.position != this.mAdapter.getCount() - 1) {
            n4 = itemInfo2.offset * n3;
        }
        if (n2 >= n) {
            n = n2;
            if (n2 > n4) {
                n = n4;
            }
        }
        final float mLastMotionX = this.mLastMotionX;
        final int n5 = (int)n;
        this.mLastMotionX = mLastMotionX + (n - n5);
        this.scrollTo(n5, this.getScrollY());
        this.pageScrolled(n5);
        final MotionEvent obtain = MotionEvent.obtain(this.mFakeDragBeginTime, SystemClock.uptimeMillis(), 2, this.mLastMotionX, 0.0f, 0);
        this.mVelocityTracker.addMovement(obtain);
        obtain.recycle();
    }
    
    protected ViewGroup$LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }
    
    public ViewGroup$LayoutParams generateLayoutParams(final AttributeSet set) {
        return new LayoutParams(this.getContext(), set);
    }
    
    protected ViewGroup$LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return this.generateDefaultLayoutParams();
    }
    
    @Nullable
    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }
    
    protected int getChildDrawingOrder(final int n, final int n2) {
        int index = n2;
        if (this.mDrawingOrder == 2) {
            index = n - 1 - n2;
        }
        return ((LayoutParams)this.mDrawingOrderedChildren.get(index).getLayoutParams()).childIndex;
    }
    
    public int getCurrentItem() {
        return this.mCurItem;
    }
    
    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }
    
    public int getPageMargin() {
        return this.mPageMargin;
    }
    
    ItemInfo infoForAnyChild(View view) {
        while (true) {
            final ViewParent parent = view.getParent();
            if (parent == this) {
                return this.infoForChild(view);
            }
            if (parent == null || !(parent instanceof View)) {
                return null;
            }
            view = (View)parent;
        }
    }
    
    ItemInfo infoForChild(final View view) {
        for (int i = 0; i < this.mItems.size(); ++i) {
            final ItemInfo itemInfo = this.mItems.get(i);
            if (this.mAdapter.isViewFromObject(view, itemInfo.object)) {
                return itemInfo;
            }
        }
        return null;
    }
    
    ItemInfo infoForPosition(final int n) {
        for (int i = 0; i < this.mItems.size(); ++i) {
            final ItemInfo itemInfo = this.mItems.get(i);
            if (itemInfo.position == n) {
                return itemInfo;
            }
        }
        return null;
    }
    
    void initViewPager() {
        this.setWillNotDraw(false);
        this.setDescendantFocusability(262144);
        this.setFocusable(true);
        final Context context = this.getContext();
        this.mScroller = new Scroller(context, ViewPager.sInterpolator);
        final ViewConfiguration value = ViewConfiguration.get(context);
        final float density = context.getResources().getDisplayMetrics().density;
        this.mTouchSlop = value.getScaledPagingTouchSlop();
        this.mMinimumVelocity = (int)(400.0f * density);
        this.mMaximumVelocity = value.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffect(context);
        this.mRightEdge = new EdgeEffect(context);
        this.mFlingDistance = (int)(25.0f * density);
        this.mCloseEnough = (int)(2.0f * density);
        this.mDefaultGutterSize = (int)(16.0f * density);
        ViewCompat.setAccessibilityDelegate((View)this, new MyAccessibilityDelegate());
        if (ViewCompat.getImportantForAccessibility((View)this) == 0) {
            ViewCompat.setImportantForAccessibility((View)this, 1);
        }
        ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener(this) {
            private final Rect mTempRect = new Rect();
            final ViewPager this$0;
            
            @Override
            public WindowInsetsCompat onApplyWindowInsets(final View view, WindowInsetsCompat onApplyWindowInsets) {
                onApplyWindowInsets = ViewCompat.onApplyWindowInsets(view, onApplyWindowInsets);
                if (onApplyWindowInsets.isConsumed()) {
                    return onApplyWindowInsets;
                }
                final Rect mTempRect = this.mTempRect;
                mTempRect.left = onApplyWindowInsets.getSystemWindowInsetLeft();
                mTempRect.top = onApplyWindowInsets.getSystemWindowInsetTop();
                mTempRect.right = onApplyWindowInsets.getSystemWindowInsetRight();
                mTempRect.bottom = onApplyWindowInsets.getSystemWindowInsetBottom();
                for (int i = 0; i < this.this$0.getChildCount(); ++i) {
                    final WindowInsetsCompat dispatchApplyWindowInsets = ViewCompat.dispatchApplyWindowInsets(this.this$0.getChildAt(i), onApplyWindowInsets);
                    mTempRect.left = Math.min(dispatchApplyWindowInsets.getSystemWindowInsetLeft(), mTempRect.left);
                    mTempRect.top = Math.min(dispatchApplyWindowInsets.getSystemWindowInsetTop(), mTempRect.top);
                    mTempRect.right = Math.min(dispatchApplyWindowInsets.getSystemWindowInsetRight(), mTempRect.right);
                    mTempRect.bottom = Math.min(dispatchApplyWindowInsets.getSystemWindowInsetBottom(), mTempRect.bottom);
                }
                return onApplyWindowInsets.replaceSystemWindowInsets(mTempRect.left, mTempRect.top, mTempRect.right, mTempRect.bottom);
            }
        });
    }
    
    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }
    
    protected void onDetachedFromWindow() {
        this.removeCallbacks(this.mEndScrollRunnable);
        if (this.mScroller != null && !this.mScroller.isFinished()) {
            this.mScroller.abortAnimation();
        }
        super.onDetachedFromWindow();
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
            final int scrollX = this.getScrollX();
            final int width = this.getWidth();
            final float n = (float)this.mPageMargin;
            final float n2 = (float)width;
            final float n3 = n / n2;
            final ArrayList<ItemInfo> mItems = this.mItems;
            int index = 0;
            ItemInfo itemInfo = mItems.get(0);
            float offset = itemInfo.offset;
            for (int size = this.mItems.size(), i = itemInfo.position; i < this.mItems.get(size - 1).position; ++i) {
                while (i > itemInfo.position && index < size) {
                    final ArrayList<ItemInfo> mItems2 = this.mItems;
                    ++index;
                    itemInfo = mItems2.get(index);
                }
                float a;
                if (i == itemInfo.position) {
                    final float offset2 = itemInfo.offset;
                    final float widthFactor = itemInfo.widthFactor;
                    final float offset3 = itemInfo.offset;
                    final float widthFactor2 = itemInfo.widthFactor;
                    a = (offset2 + widthFactor) * n2;
                    offset = offset3 + widthFactor2 + n3;
                }
                else {
                    final float pageWidth = this.mAdapter.getPageWidth(i);
                    a = (offset + pageWidth) * n2;
                    offset += pageWidth + n3;
                }
                if (this.mPageMargin + a > scrollX) {
                    this.mMarginDrawable.setBounds(Math.round(a), this.mTopPageBounds, Math.round(this.mPageMargin + a), this.mBottomPageBounds);
                    this.mMarginDrawable.draw(canvas);
                }
                if (a > scrollX + width) {
                    break;
                }
            }
        }
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        final int n = motionEvent.getAction() & 0xFF;
        if (n != 3 && n != 1) {
            if (n != 0) {
                if (this.mIsBeingDragged) {
                    return true;
                }
                if (this.mIsUnableToDrag) {
                    return false;
                }
            }
            if (n != 0) {
                if (n != 2) {
                    if (n == 6) {
                        this.onSecondaryPointerUp(motionEvent);
                    }
                }
                else {
                    final int mActivePointerId = this.mActivePointerId;
                    if (mActivePointerId != -1) {
                        final int pointerIndex = motionEvent.findPointerIndex(mActivePointerId);
                        final float x = motionEvent.getX(pointerIndex);
                        final float a = x - this.mLastMotionX;
                        final float abs = Math.abs(a);
                        final float y = motionEvent.getY(pointerIndex);
                        final float abs2 = Math.abs(y - this.mInitialMotionY);
                        final float n2 = fcmpl(a, 0.0f);
                        if (n2 != 0 && !this.isGutterDrag(this.mLastMotionX, a) && this.canScroll((View)this, false, (int)a, (int)x, (int)y)) {
                            this.mLastMotionX = x;
                            this.mLastMotionY = y;
                            this.mIsUnableToDrag = true;
                            return false;
                        }
                        if (abs > this.mTouchSlop && abs * 0.5f > abs2) {
                            this.requestParentDisallowInterceptTouchEvent(this.mIsBeingDragged = true);
                            this.setScrollState(1);
                            float mLastMotionX;
                            if (n2 > 0) {
                                mLastMotionX = this.mInitialMotionX + this.mTouchSlop;
                            }
                            else {
                                mLastMotionX = this.mInitialMotionX - this.mTouchSlop;
                            }
                            this.mLastMotionX = mLastMotionX;
                            this.mLastMotionY = y;
                            this.setScrollingCacheEnabled(true);
                        }
                        else if (abs2 > this.mTouchSlop) {
                            this.mIsUnableToDrag = true;
                        }
                        if (this.mIsBeingDragged && this.performDrag(x)) {
                            ViewCompat.postInvalidateOnAnimation((View)this);
                        }
                    }
                }
            }
            else {
                final float x2 = motionEvent.getX();
                this.mInitialMotionX = x2;
                this.mLastMotionX = x2;
                final float y2 = motionEvent.getY();
                this.mInitialMotionY = y2;
                this.mLastMotionY = y2;
                this.mActivePointerId = motionEvent.getPointerId(0);
                this.mIsUnableToDrag = false;
                this.mIsScrollStarted = true;
                this.mScroller.computeScrollOffset();
                if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
                    this.mScroller.abortAnimation();
                    this.mPopulatePending = false;
                    this.populate();
                    this.requestParentDisallowInterceptTouchEvent(this.mIsBeingDragged = true);
                    this.setScrollState(1);
                }
                else {
                    this.completeScroll(false);
                    this.mIsBeingDragged = false;
                }
            }
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            return this.mIsBeingDragged;
        }
        this.resetTouch();
        return false;
    }
    
    protected void onLayout(final boolean b, int paddingTop, int b2, int i, int paddingBottom) {
        final int childCount = this.getChildCount();
        final int n = i - paddingTop;
        final int n2 = paddingBottom - b2;
        b2 = this.getPaddingLeft();
        paddingTop = this.getPaddingTop();
        int paddingRight = this.getPaddingRight();
        paddingBottom = this.getPaddingBottom();
        final int scrollX = this.getScrollX();
        int mDecorChildCount = 0;
        int n3;
        int n4;
        int n5;
        int n6;
        for (int j = 0; j < childCount; ++j, b2 = n3, paddingRight = n4, paddingTop = n5, paddingBottom = n6, mDecorChildCount = i) {
            final View child = this.getChildAt(j);
            n3 = b2;
            n4 = paddingRight;
            n5 = paddingTop;
            n6 = paddingBottom;
            i = mDecorChildCount;
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                n3 = b2;
                n4 = paddingRight;
                n5 = paddingTop;
                n6 = paddingBottom;
                i = mDecorChildCount;
                if (layoutParams.isDecor) {
                    i = (layoutParams.gravity & 0x7);
                    final int n7 = layoutParams.gravity & 0x70;
                    if (i != 1) {
                        if (i != 3) {
                            if (i != 5) {
                                i = b2;
                                n3 = b2;
                            }
                            else {
                                i = n - paddingRight - child.getMeasuredWidth();
                                paddingRight += child.getMeasuredWidth();
                                n3 = b2;
                            }
                        }
                        else {
                            final int measuredWidth = child.getMeasuredWidth();
                            i = b2;
                            n3 = measuredWidth + b2;
                        }
                    }
                    else {
                        i = Math.max((n - child.getMeasuredWidth()) / 2, b2);
                        n3 = b2;
                    }
                    if (n7 != 16) {
                        if (n7 != 48) {
                            if (n7 != 80) {
                                b2 = paddingTop;
                            }
                            else {
                                b2 = n2 - paddingBottom - child.getMeasuredHeight();
                                paddingBottom += child.getMeasuredHeight();
                            }
                        }
                        else {
                            final int measuredHeight = child.getMeasuredHeight();
                            b2 = paddingTop;
                            paddingTop += measuredHeight;
                        }
                    }
                    else {
                        b2 = Math.max((n2 - child.getMeasuredHeight()) / 2, paddingTop);
                    }
                    i += scrollX;
                    child.layout(i, b2, child.getMeasuredWidth() + i, b2 + child.getMeasuredHeight());
                    i = mDecorChildCount + 1;
                    n6 = paddingBottom;
                    n5 = paddingTop;
                    n4 = paddingRight;
                }
            }
        }
        View child2;
        LayoutParams layoutParams2;
        ItemInfo infoForChild;
        float n8;
        int n9;
        for (i = 0; i < childCount; ++i) {
            child2 = this.getChildAt(i);
            if (child2.getVisibility() != 8) {
                layoutParams2 = (LayoutParams)child2.getLayoutParams();
                if (!layoutParams2.isDecor) {
                    infoForChild = this.infoForChild(child2);
                    if (infoForChild != null) {
                        n8 = (float)(n - b2 - paddingRight);
                        n9 = (int)(infoForChild.offset * n8) + b2;
                        if (layoutParams2.needsMeasure) {
                            layoutParams2.needsMeasure = false;
                            child2.measure(View$MeasureSpec.makeMeasureSpec((int)(n8 * layoutParams2.widthFactor), 1073741824), View$MeasureSpec.makeMeasureSpec(n2 - paddingTop - paddingBottom, 1073741824));
                        }
                        child2.layout(n9, paddingTop, child2.getMeasuredWidth() + n9, child2.getMeasuredHeight() + paddingTop);
                    }
                }
            }
        }
        this.mTopPageBounds = paddingTop;
        this.mBottomPageBounds = n2 - paddingBottom;
        this.mDecorChildCount = mDecorChildCount;
        if (this.mFirstLayout) {
            this.scrollToItem(this.mCurItem, false, 0, false);
        }
        this.mFirstLayout = false;
    }
    
    protected void onMeasure(int paddingRight, int i) {
        this.setMeasuredDimension(getDefaultSize(0, paddingRight), getDefaultSize(0, i));
        final int measuredWidth = this.getMeasuredWidth();
        this.mGutterSize = Math.min(measuredWidth / 10, this.mDefaultGutterSize);
        final int paddingLeft = this.getPaddingLeft();
        paddingRight = this.getPaddingRight();
        final int measuredHeight = this.getMeasuredHeight();
        final int paddingTop = this.getPaddingTop();
        i = this.getPaddingBottom();
        final int childCount = this.getChildCount();
        i = measuredHeight - paddingTop - i;
        paddingRight = measuredWidth - paddingLeft - paddingRight;
        int n = 0;
        while (true) {
            final int n2 = 1;
            int n3 = 1073741824;
            if (n >= childCount) {
                break;
            }
            final View child = this.getChildAt(n);
            int n4 = paddingRight;
            int n5 = i;
            if (child.getVisibility() != 8) {
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                n4 = paddingRight;
                n5 = i;
                if (layoutParams != null) {
                    n4 = paddingRight;
                    n5 = i;
                    if (layoutParams.isDecor) {
                        final int n6 = layoutParams.gravity & 0x7;
                        final int n7 = layoutParams.gravity & 0x70;
                        final boolean b = n7 == 48 || n7 == 80;
                        int n8 = n2;
                        if (n6 != 3) {
                            if (n6 == 5) {
                                n8 = n2;
                            }
                            else {
                                n8 = 0;
                            }
                        }
                        int n9 = Integer.MIN_VALUE;
                        int n10;
                        if (b) {
                            n10 = Integer.MIN_VALUE;
                            n9 = 1073741824;
                        }
                        else if (n8 != 0) {
                            n10 = 1073741824;
                        }
                        else {
                            n10 = Integer.MIN_VALUE;
                        }
                        int n11;
                        int n12;
                        if (layoutParams.width != -2) {
                            int width;
                            if (layoutParams.width != -1) {
                                width = layoutParams.width;
                            }
                            else {
                                width = paddingRight;
                            }
                            n11 = 1073741824;
                            n12 = width;
                        }
                        else {
                            n12 = paddingRight;
                            n11 = n9;
                        }
                        int height;
                        if (layoutParams.height != -2) {
                            if (layoutParams.height != -1) {
                                height = layoutParams.height;
                            }
                            else {
                                height = i;
                            }
                        }
                        else {
                            final int n13 = i;
                            n3 = n10;
                            height = n13;
                        }
                        child.measure(View$MeasureSpec.makeMeasureSpec(n12, n11), View$MeasureSpec.makeMeasureSpec(height, n3));
                        if (b) {
                            n5 = i - child.getMeasuredHeight();
                            n4 = paddingRight;
                        }
                        else {
                            n4 = paddingRight;
                            n5 = i;
                            if (n8 != 0) {
                                n4 = paddingRight - child.getMeasuredWidth();
                                n5 = i;
                            }
                        }
                    }
                }
            }
            ++n;
            paddingRight = n4;
            i = n5;
        }
        this.mChildWidthMeasureSpec = View$MeasureSpec.makeMeasureSpec(paddingRight, 1073741824);
        this.mChildHeightMeasureSpec = View$MeasureSpec.makeMeasureSpec(i, 1073741824);
        this.mInLayout = true;
        this.populate();
        i = 0;
        this.mInLayout = false;
        while (i < this.getChildCount()) {
            final View child2 = this.getChildAt(i);
            if (child2.getVisibility() != 8) {
                final LayoutParams layoutParams2 = (LayoutParams)child2.getLayoutParams();
                if (layoutParams2 == null || !layoutParams2.isDecor) {
                    child2.measure(View$MeasureSpec.makeMeasureSpec((int)(paddingRight * layoutParams2.widthFactor), 1073741824), this.mChildHeightMeasureSpec);
                }
            }
            ++i;
        }
    }
    
    @CallSuper
    protected void onPageScrolled(int i, float n, int scrollX) {
        final int mDecorChildCount = this.mDecorChildCount;
        final int n2 = 0;
        if (mDecorChildCount > 0) {
            final int scrollX2 = this.getScrollX();
            int paddingLeft = this.getPaddingLeft();
            int paddingRight = this.getPaddingRight();
            final int width = this.getWidth();
            for (int childCount = this.getChildCount(), j = 0; j < childCount; ++j) {
                final View child = this.getChildAt(j);
                final LayoutParams layoutParams = (LayoutParams)child.getLayoutParams();
                if (layoutParams.isDecor) {
                    final int n3 = layoutParams.gravity & 0x7;
                    int max;
                    if (n3 != 1) {
                        if (n3 != 3) {
                            if (n3 != 5) {
                                final int n4 = paddingLeft;
                                max = paddingLeft;
                                paddingLeft = n4;
                            }
                            else {
                                max = width - paddingRight - child.getMeasuredWidth();
                                paddingRight += child.getMeasuredWidth();
                            }
                        }
                        else {
                            final int n5 = child.getWidth() + paddingLeft;
                            max = paddingLeft;
                            paddingLeft = n5;
                        }
                    }
                    else {
                        max = Math.max((width - child.getMeasuredWidth()) / 2, paddingLeft);
                    }
                    final int n6 = max + scrollX2 - child.getLeft();
                    if (n6 != 0) {
                        child.offsetLeftAndRight(n6);
                    }
                }
            }
        }
        this.dispatchOnPageScrolled(i, n, scrollX);
        if (this.mPageTransformer != null) {
            scrollX = this.getScrollX();
            int childCount2;
            View child2;
            for (childCount2 = this.getChildCount(), i = n2; i < childCount2; ++i) {
                child2 = this.getChildAt(i);
                if (!((LayoutParams)child2.getLayoutParams()).isDecor) {
                    n = (child2.getLeft() - scrollX) / (float)this.getClientWidth();
                    this.mPageTransformer.transformPage(child2, n);
                }
            }
        }
        this.mCalledSuper = true;
    }
    
    protected boolean onRequestFocusInDescendants(final int n, final Rect rect) {
        int i = this.getChildCount();
        int n2 = -1;
        int n3;
        if ((n & 0x2) != 0x0) {
            n2 = i;
            i = 0;
            n3 = 1;
        }
        else {
            --i;
            n3 = -1;
        }
        while (i != n2) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() == 0) {
                final ItemInfo infoForChild = this.infoForChild(child);
                if (infoForChild != null && infoForChild.position == this.mCurItem && child.requestFocus(n, rect)) {
                    return true;
                }
            }
            i += n3;
        }
        return false;
    }
    
    public void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        if (this.mAdapter != null) {
            this.mAdapter.restoreState(savedState.adapterState, savedState.loader);
            this.setCurrentItemInternal(savedState.position, false, true);
        }
        else {
            this.mRestoredCurItem = savedState.position;
            this.mRestoredAdapterState = savedState.adapterState;
            this.mRestoredClassLoader = savedState.loader;
        }
    }
    
    public Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.position = this.mCurItem;
        if (this.mAdapter != null) {
            savedState.adapterState = this.mAdapter.saveState();
        }
        return (Parcelable)savedState;
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (n != n3) {
            this.recomputeScrollPosition(n, n3, this.mPageMargin, this.mPageMargin);
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (this.mFakeDragging) {
            return true;
        }
        final int action = motionEvent.getAction();
        boolean b = false;
        if (action == 0 && motionEvent.getEdgeFlags() != 0) {
            return false;
        }
        if (this.mAdapter != null && this.mAdapter.getCount() != 0) {
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            switch (motionEvent.getAction() & 0xFF) {
                case 6: {
                    this.onSecondaryPointerUp(motionEvent);
                    this.mLastMotionX = motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId));
                    break;
                }
                case 5: {
                    final int actionIndex = motionEvent.getActionIndex();
                    this.mLastMotionX = motionEvent.getX(actionIndex);
                    this.mActivePointerId = motionEvent.getPointerId(actionIndex);
                    break;
                }
                case 3: {
                    if (this.mIsBeingDragged) {
                        this.scrollToItem(this.mCurItem, true, 0, false);
                        b = this.resetTouch();
                        break;
                    }
                    break;
                }
                case 2: {
                    if (!this.mIsBeingDragged) {
                        final int pointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                        if (pointerIndex == -1) {
                            b = this.resetTouch();
                            break;
                        }
                        final float x = motionEvent.getX(pointerIndex);
                        final float abs = Math.abs(x - this.mLastMotionX);
                        final float y = motionEvent.getY(pointerIndex);
                        final float abs2 = Math.abs(y - this.mLastMotionY);
                        if (abs > this.mTouchSlop && abs > abs2) {
                            this.requestParentDisallowInterceptTouchEvent(this.mIsBeingDragged = true);
                            float mLastMotionX;
                            if (x - this.mInitialMotionX > 0.0f) {
                                mLastMotionX = this.mInitialMotionX + this.mTouchSlop;
                            }
                            else {
                                mLastMotionX = this.mInitialMotionX - this.mTouchSlop;
                            }
                            this.mLastMotionX = mLastMotionX;
                            this.mLastMotionY = y;
                            this.setScrollState(1);
                            this.setScrollingCacheEnabled(true);
                            final ViewParent parent = this.getParent();
                            if (parent != null) {
                                parent.requestDisallowInterceptTouchEvent(true);
                            }
                        }
                    }
                    if (this.mIsBeingDragged) {
                        b = (false | this.performDrag(motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId))));
                        break;
                    }
                    break;
                }
                case 1: {
                    if (this.mIsBeingDragged) {
                        final VelocityTracker mVelocityTracker = this.mVelocityTracker;
                        mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                        final int n = (int)mVelocityTracker.getXVelocity(this.mActivePointerId);
                        this.mPopulatePending = true;
                        final int clientWidth = this.getClientWidth();
                        final int scrollX = this.getScrollX();
                        final ItemInfo infoForCurrentScrollPosition = this.infoForCurrentScrollPosition();
                        final float n2 = (float)this.mPageMargin;
                        final float n3 = (float)clientWidth;
                        this.setCurrentItemInternal(this.determineTargetPage(infoForCurrentScrollPosition.position, (scrollX / n3 - infoForCurrentScrollPosition.offset) / (infoForCurrentScrollPosition.widthFactor + n2 / n3), n, (int)(motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId)) - this.mInitialMotionX)), true, true, n);
                        b = this.resetTouch();
                        break;
                    }
                    break;
                }
                case 0: {
                    this.mScroller.abortAnimation();
                    this.mPopulatePending = false;
                    this.populate();
                    final float x2 = motionEvent.getX();
                    this.mInitialMotionX = x2;
                    this.mLastMotionX = x2;
                    final float y2 = motionEvent.getY();
                    this.mInitialMotionY = y2;
                    this.mLastMotionY = y2;
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    break;
                }
            }
            if (b) {
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
            return true;
        }
        return false;
    }
    
    boolean pageLeft() {
        if (this.mCurItem > 0) {
            this.setCurrentItem(this.mCurItem - 1, true);
            return true;
        }
        return false;
    }
    
    boolean pageRight() {
        if (this.mAdapter != null && this.mCurItem < this.mAdapter.getCount() - 1) {
            this.setCurrentItem(this.mCurItem + 1, true);
            return true;
        }
        return false;
    }
    
    void populate() {
        this.populate(this.mCurItem);
    }
    
    void populate(int i) {
        ItemInfo infoForPosition;
        if (this.mCurItem != i) {
            infoForPosition = this.infoForPosition(this.mCurItem);
            this.mCurItem = i;
        }
        else {
            infoForPosition = null;
        }
        if (this.mAdapter == null) {
            this.sortChildDrawingOrder();
            return;
        }
        if (this.mPopulatePending) {
            this.sortChildDrawingOrder();
            return;
        }
        if (this.getWindowToken() == null) {
            return;
        }
        this.mAdapter.startUpdate(this);
        i = this.mOffscreenPageLimit;
        final int max = Math.max(0, this.mCurItem - i);
        final int count = this.mAdapter.getCount();
        final int min = Math.min(count - 1, this.mCurItem + i);
        if (count != this.mExpectedAdapterCount) {
            String str;
            try {
                str = this.getResources().getResourceName(this.getId());
            }
            catch (final Resources$NotFoundException ex) {
                str = Integer.toHexString(this.getId());
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: ");
            sb.append(this.mExpectedAdapterCount);
            sb.append(", found: ");
            sb.append(count);
            sb.append(" Pager id: ");
            sb.append(str);
            sb.append(" Pager class: ");
            sb.append(this.getClass());
            sb.append(" Problematic adapter: ");
            sb.append(this.mAdapter.getClass());
            throw new IllegalStateException(sb.toString());
        }
        i = 0;
        while (true) {
            while (i < this.mItems.size()) {
                final ItemInfo itemInfo = this.mItems.get(i);
                if (itemInfo.position >= this.mCurItem) {
                    if (itemInfo.position == this.mCurItem) {
                        ItemInfo addNewItem = itemInfo;
                        if (itemInfo == null) {
                            addNewItem = itemInfo;
                            if (count > 0) {
                                addNewItem = this.addNewItem(this.mCurItem, i);
                            }
                        }
                        if (addNewItem != null) {
                            int index = i - 1;
                            ItemInfo itemInfo2;
                            if (index >= 0) {
                                itemInfo2 = this.mItems.get(index);
                            }
                            else {
                                itemInfo2 = null;
                            }
                            final int clientWidth = this.getClientWidth();
                            float n;
                            if (clientWidth <= 0) {
                                n = 0.0f;
                            }
                            else {
                                n = 2.0f - addNewItem.widthFactor + this.getPaddingLeft() / (float)clientWidth;
                            }
                            int j = this.mCurItem - 1;
                            float n2 = 0.0f;
                            int n3 = i;
                            ItemInfo itemInfo3 = itemInfo2;
                            while (j >= 0) {
                                float n4 = 0.0f;
                                ItemInfo itemInfo4 = null;
                                int n5 = 0;
                                Label_0707: {
                                    Label_0703: {
                                        int n6;
                                        if (n2 >= n && j < max) {
                                            if (itemInfo3 == null) {
                                                break;
                                            }
                                            n4 = n2;
                                            i = index;
                                            itemInfo4 = itemInfo3;
                                            n5 = n3;
                                            if (j != itemInfo3.position) {
                                                break Label_0707;
                                            }
                                            n4 = n2;
                                            i = index;
                                            itemInfo4 = itemInfo3;
                                            n5 = n3;
                                            if (itemInfo3.scrolling) {
                                                break Label_0707;
                                            }
                                            this.mItems.remove(index);
                                            this.mAdapter.destroyItem(this, j, itemInfo3.object);
                                            --index;
                                            --n3;
                                            n4 = n2;
                                            i = index;
                                            n6 = n3;
                                            if (index >= 0) {
                                                itemInfo4 = this.mItems.get(index);
                                                n4 = n2;
                                                i = index;
                                                break Label_0703;
                                            }
                                        }
                                        else if (itemInfo3 != null && j == itemInfo3.position) {
                                            final float n7 = n2 + itemInfo3.widthFactor;
                                            --index;
                                            n4 = n7;
                                            i = index;
                                            n6 = n3;
                                            if (index >= 0) {
                                                itemInfo4 = this.mItems.get(index);
                                                n4 = n7;
                                                i = index;
                                                break Label_0703;
                                            }
                                        }
                                        else {
                                            final float n8 = n2 + this.addNewItem(j, index + 1).widthFactor;
                                            ++n3;
                                            n4 = n8;
                                            i = index;
                                            n6 = n3;
                                            if (index >= 0) {
                                                itemInfo4 = this.mItems.get(index);
                                                n4 = n8;
                                                i = index;
                                                break Label_0703;
                                            }
                                        }
                                        itemInfo4 = null;
                                        n3 = n6;
                                    }
                                    n5 = n3;
                                }
                                --j;
                                n2 = n4;
                                index = i;
                                itemInfo3 = itemInfo4;
                                n3 = n5;
                            }
                            float widthFactor = addNewItem.widthFactor;
                            int index2 = n3 + 1;
                            if (widthFactor < 2.0f) {
                                ItemInfo itemInfo5;
                                if (index2 < this.mItems.size()) {
                                    itemInfo5 = this.mItems.get(index2);
                                }
                                else {
                                    itemInfo5 = null;
                                }
                                float n9;
                                if (clientWidth <= 0) {
                                    n9 = 0.0f;
                                }
                                else {
                                    n9 = this.getPaddingRight() / (float)clientWidth + 2.0f;
                                }
                                i = this.mCurItem;
                                ItemInfo itemInfo6 = itemInfo5;
                                while (true) {
                                    final int n10 = i + 1;
                                    if (n10 >= count) {
                                        break;
                                    }
                                    float n11 = 0.0f;
                                    ItemInfo itemInfo7 = null;
                                    Label_1071: {
                                        if (widthFactor >= n9 && n10 > min) {
                                            if (itemInfo6 == null) {
                                                break;
                                            }
                                            n11 = widthFactor;
                                            i = index2;
                                            itemInfo7 = itemInfo6;
                                            if (n10 != itemInfo6.position) {
                                                break Label_1071;
                                            }
                                            n11 = widthFactor;
                                            i = index2;
                                            itemInfo7 = itemInfo6;
                                            if (itemInfo6.scrolling) {
                                                break Label_1071;
                                            }
                                            this.mItems.remove(index2);
                                            this.mAdapter.destroyItem(this, n10, itemInfo6.object);
                                            n11 = widthFactor;
                                            if ((i = index2) < this.mItems.size()) {
                                                itemInfo7 = this.mItems.get(index2);
                                                n11 = widthFactor;
                                                i = index2;
                                                break Label_1071;
                                            }
                                        }
                                        else if (itemInfo6 != null && n10 == itemInfo6.position) {
                                            final float n12 = widthFactor + itemInfo6.widthFactor;
                                            ++index2;
                                            n11 = n12;
                                            if ((i = index2) < this.mItems.size()) {
                                                itemInfo7 = this.mItems.get(index2);
                                                n11 = n12;
                                                i = index2;
                                                break Label_1071;
                                            }
                                        }
                                        else {
                                            final ItemInfo addNewItem2 = this.addNewItem(n10, index2);
                                            ++index2;
                                            n11 = widthFactor + addNewItem2.widthFactor;
                                            if ((i = index2) < this.mItems.size()) {
                                                itemInfo7 = this.mItems.get(index2);
                                                i = index2;
                                                n11 = n11;
                                                break Label_1071;
                                            }
                                        }
                                        itemInfo7 = null;
                                    }
                                    widthFactor = n11;
                                    index2 = i;
                                    itemInfo6 = itemInfo7;
                                    i = n10;
                                }
                            }
                            this.calculatePageOffsets(addNewItem, n3, infoForPosition);
                            this.mAdapter.setPrimaryItem(this, this.mCurItem, addNewItem.object);
                        }
                        this.mAdapter.finishUpdate(this);
                        int childCount;
                        View child;
                        LayoutParams layoutParams;
                        ItemInfo infoForChild;
                        for (childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                            child = this.getChildAt(i);
                            layoutParams = (LayoutParams)child.getLayoutParams();
                            layoutParams.childIndex = i;
                            if (!layoutParams.isDecor && layoutParams.widthFactor == 0.0f) {
                                infoForChild = this.infoForChild(child);
                                if (infoForChild != null) {
                                    layoutParams.widthFactor = infoForChild.widthFactor;
                                    layoutParams.position = infoForChild.position;
                                }
                            }
                        }
                        this.sortChildDrawingOrder();
                        if (this.hasFocus()) {
                            final View focus = this.findFocus();
                            ItemInfo infoForAnyChild;
                            if (focus != null) {
                                infoForAnyChild = this.infoForAnyChild(focus);
                            }
                            else {
                                infoForAnyChild = null;
                            }
                            if (infoForAnyChild == null || infoForAnyChild.position != this.mCurItem) {
                                View child2;
                                ItemInfo infoForChild2;
                                for (i = 0; i < this.getChildCount(); ++i) {
                                    child2 = this.getChildAt(i);
                                    infoForChild2 = this.infoForChild(child2);
                                    if (infoForChild2 != null && infoForChild2.position == this.mCurItem && child2.requestFocus(2)) {
                                        break;
                                    }
                                }
                            }
                        }
                        return;
                    }
                    break;
                }
                else {
                    ++i;
                }
            }
            final ItemInfo itemInfo = null;
            continue;
        }
    }
    
    public void removeOnAdapterChangeListener(@NonNull final OnAdapterChangeListener onAdapterChangeListener) {
        if (this.mAdapterChangeListeners != null) {
            this.mAdapterChangeListeners.remove(onAdapterChangeListener);
        }
    }
    
    public void removeOnPageChangeListener(@NonNull final OnPageChangeListener onPageChangeListener) {
        if (this.mOnPageChangeListeners != null) {
            this.mOnPageChangeListeners.remove(onPageChangeListener);
        }
    }
    
    public void removeView(final View view) {
        if (this.mInLayout) {
            this.removeViewInLayout(view);
        }
        else {
            super.removeView(view);
        }
    }
    
    public void setAdapter(@Nullable final PagerAdapter mAdapter) {
        final PagerAdapter mAdapter2 = this.mAdapter;
        final int n = 0;
        if (mAdapter2 != null) {
            this.mAdapter.setViewPagerObserver(null);
            this.mAdapter.startUpdate(this);
            for (int i = 0; i < this.mItems.size(); ++i) {
                final ItemInfo itemInfo = this.mItems.get(i);
                this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
            }
            this.mAdapter.finishUpdate(this);
            this.mItems.clear();
            this.removeNonDecorViews();
            this.scrollTo(this.mCurItem = 0, 0);
        }
        final PagerAdapter mAdapter3 = this.mAdapter;
        this.mAdapter = mAdapter;
        this.mExpectedAdapterCount = 0;
        if (this.mAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new PagerObserver();
            }
            this.mAdapter.setViewPagerObserver(this.mObserver);
            this.mPopulatePending = false;
            final boolean mFirstLayout = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                this.setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            }
            else if (!mFirstLayout) {
                this.populate();
            }
            else {
                this.requestLayout();
            }
        }
        if (this.mAdapterChangeListeners != null && !this.mAdapterChangeListeners.isEmpty()) {
            for (int size = this.mAdapterChangeListeners.size(), j = n; j < size; ++j) {
                this.mAdapterChangeListeners.get(j).onAdapterChanged(this, mAdapter3, mAdapter);
            }
        }
    }
    
    public void setCurrentItem(final int n) {
        this.mPopulatePending = false;
        this.setCurrentItemInternal(n, this.mFirstLayout ^ true, false);
    }
    
    public void setCurrentItem(final int n, final boolean b) {
        this.setCurrentItemInternal(n, b, this.mPopulatePending = false);
    }
    
    void setCurrentItemInternal(final int n, final boolean b, final boolean b2) {
        this.setCurrentItemInternal(n, b, b2, 0);
    }
    
    void setCurrentItemInternal(int i, final boolean b, final boolean b2, final int n) {
        if (this.mAdapter == null || this.mAdapter.getCount() <= 0) {
            this.setScrollingCacheEnabled(false);
            return;
        }
        if (!b2 && this.mCurItem == i && this.mItems.size() != 0) {
            this.setScrollingCacheEnabled(false);
            return;
        }
        boolean b3 = true;
        int mCurItem;
        if (i < 0) {
            mCurItem = 0;
        }
        else if ((mCurItem = i) >= this.mAdapter.getCount()) {
            mCurItem = this.mAdapter.getCount() - 1;
        }
        i = this.mOffscreenPageLimit;
        if (mCurItem > this.mCurItem + i || mCurItem < this.mCurItem - i) {
            for (i = 0; i < this.mItems.size(); ++i) {
                this.mItems.get(i).scrolling = true;
            }
        }
        if (this.mCurItem == mCurItem) {
            b3 = false;
        }
        if (this.mFirstLayout) {
            this.mCurItem = mCurItem;
            if (b3) {
                this.dispatchOnPageSelected(mCurItem);
            }
            this.requestLayout();
        }
        else {
            this.populate(mCurItem);
            this.scrollToItem(mCurItem, b, n, b3);
        }
    }
    
    OnPageChangeListener setInternalPageChangeListener(final OnPageChangeListener mInternalPageChangeListener) {
        final OnPageChangeListener mInternalPageChangeListener2 = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = mInternalPageChangeListener;
        return mInternalPageChangeListener2;
    }
    
    public void setOffscreenPageLimit(final int i) {
        int mOffscreenPageLimit = i;
        if (i < 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Requested offscreen page limit ");
            sb.append(i);
            sb.append(" too small; defaulting to ");
            sb.append(1);
            Log.w("ViewPager", sb.toString());
            mOffscreenPageLimit = 1;
        }
        if (mOffscreenPageLimit != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = mOffscreenPageLimit;
            this.populate();
        }
    }
    
    @Deprecated
    public void setOnPageChangeListener(final OnPageChangeListener mOnPageChangeListener) {
        this.mOnPageChangeListener = mOnPageChangeListener;
    }
    
    public void setPageMargin(final int mPageMargin) {
        final int mPageMargin2 = this.mPageMargin;
        this.mPageMargin = mPageMargin;
        final int width = this.getWidth();
        this.recomputeScrollPosition(width, width, mPageMargin, mPageMargin2);
        this.requestLayout();
    }
    
    public void setPageMarginDrawable(@DrawableRes final int n) {
        this.setPageMarginDrawable(ContextCompat.getDrawable(this.getContext(), n));
    }
    
    public void setPageMarginDrawable(@Nullable final Drawable mMarginDrawable) {
        this.mMarginDrawable = mMarginDrawable;
        if (mMarginDrawable != null) {
            this.refreshDrawableState();
        }
        this.setWillNotDraw(mMarginDrawable == null);
        this.invalidate();
    }
    
    public void setPageTransformer(final boolean b, @Nullable final PageTransformer pageTransformer) {
        this.setPageTransformer(b, pageTransformer, 2);
    }
    
    public void setPageTransformer(final boolean b, @Nullable final PageTransformer mPageTransformer, final int mPageTransformerLayerType) {
        int mDrawingOrder = 1;
        final boolean childrenDrawingOrderEnabled = mPageTransformer != null;
        final boolean b2 = childrenDrawingOrderEnabled != (this.mPageTransformer != null);
        this.mPageTransformer = mPageTransformer;
        this.setChildrenDrawingOrderEnabled(childrenDrawingOrderEnabled);
        if (childrenDrawingOrderEnabled) {
            if (b) {
                mDrawingOrder = 2;
            }
            this.mDrawingOrder = mDrawingOrder;
            this.mPageTransformerLayerType = mPageTransformerLayerType;
        }
        else {
            this.mDrawingOrder = 0;
        }
        if (b2) {
            this.populate();
        }
    }
    
    void setScrollState(final int mScrollState) {
        if (this.mScrollState == mScrollState) {
            return;
        }
        this.mScrollState = mScrollState;
        if (this.mPageTransformer != null) {
            this.enableLayers(mScrollState != 0);
        }
        this.dispatchOnScrollStateChanged(mScrollState);
    }
    
    void smoothScrollTo(final int n, final int n2) {
        this.smoothScrollTo(n, n2, 0);
    }
    
    void smoothScrollTo(int a, int n, final int a2) {
        if (this.getChildCount() == 0) {
            this.setScrollingCacheEnabled(false);
            return;
        }
        int n2;
        if (this.mScroller != null && !this.mScroller.isFinished()) {
            if (this.mIsScrollStarted) {
                n2 = this.mScroller.getCurrX();
            }
            else {
                n2 = this.mScroller.getStartX();
            }
            this.mScroller.abortAnimation();
            this.setScrollingCacheEnabled(false);
        }
        else {
            n2 = this.getScrollX();
        }
        final int scrollY = this.getScrollY();
        final int n3 = a - n2;
        n -= scrollY;
        if (n3 == 0 && n == 0) {
            this.completeScroll(false);
            this.populate();
            this.setScrollState(0);
            return;
        }
        this.setScrollingCacheEnabled(true);
        this.setScrollState(2);
        a = this.getClientWidth();
        final int n4 = a / 2;
        final float n5 = (float)Math.abs(n3);
        final float n6 = (float)a;
        final float min = Math.min(1.0f, n5 * 1.0f / n6);
        final float n7 = (float)n4;
        final float distanceInfluenceForSnapDuration = this.distanceInfluenceForSnapDuration(min);
        a = Math.abs(a2);
        if (a > 0) {
            a = 4 * Math.round(1000.0f * Math.abs((n7 + distanceInfluenceForSnapDuration * n7) / a));
        }
        else {
            a = (int)((Math.abs(n3) / (n6 * this.mAdapter.getPageWidth(this.mCurItem) + this.mPageMargin) + 1.0f) * 100.0f);
        }
        a = Math.min(a, 600);
        this.mIsScrollStarted = false;
        this.mScroller.startScroll(n2, scrollY, n3, n, a);
        ViewCompat.postInvalidateOnAnimation((View)this);
    }
    
    protected boolean verifyDrawable(final Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mMarginDrawable;
    }
    
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE })
    public @interface DecorView {
    }
    
    static class ItemInfo
    {
        Object object;
        float offset;
        int position;
        boolean scrolling;
        float widthFactor;
    }
    
    public static class LayoutParams extends ViewGroup$LayoutParams
    {
        int childIndex;
        public int gravity;
        public boolean isDecor;
        boolean needsMeasure;
        int position;
        float widthFactor;
        
        public LayoutParams() {
            super(-1, -1);
            this.widthFactor = 0.0f;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.widthFactor = 0.0f;
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, ViewPager.LAYOUT_ATTRS);
            this.gravity = obtainStyledAttributes.getInteger(0, 48);
            obtainStyledAttributes.recycle();
        }
    }
    
    class MyAccessibilityDelegate extends AccessibilityDelegateCompat
    {
        final ViewPager this$0;
        
        MyAccessibilityDelegate(final ViewPager this$0) {
            this.this$0 = this$0;
        }
        
        private boolean canScroll() {
            final PagerAdapter mAdapter = this.this$0.mAdapter;
            boolean b = true;
            if (mAdapter == null || this.this$0.mAdapter.getCount() <= 1) {
                b = false;
            }
            return b;
        }
        
        @Override
        public void onInitializeAccessibilityEvent(final View view, final AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)ViewPager.class.getName());
            accessibilityEvent.setScrollable(this.canScroll());
            if (accessibilityEvent.getEventType() == 4096 && this.this$0.mAdapter != null) {
                accessibilityEvent.setItemCount(this.this$0.mAdapter.getCount());
                accessibilityEvent.setFromIndex(this.this$0.mCurItem);
                accessibilityEvent.setToIndex(this.this$0.mCurItem);
            }
        }
        
        @Override
        public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.setClassName(ViewPager.class.getName());
            accessibilityNodeInfoCompat.setScrollable(this.canScroll());
            if (this.this$0.canScrollHorizontally(1)) {
                accessibilityNodeInfoCompat.addAction(4096);
            }
            if (this.this$0.canScrollHorizontally(-1)) {
                accessibilityNodeInfoCompat.addAction(8192);
            }
        }
        
        @Override
        public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
            if (super.performAccessibilityAction(view, n, bundle)) {
                return true;
            }
            if (n != 4096) {
                if (n != 8192) {
                    return false;
                }
                if (this.this$0.canScrollHorizontally(-1)) {
                    this.this$0.setCurrentItem(this.this$0.mCurItem - 1);
                    return true;
                }
                return false;
            }
            else {
                if (this.this$0.canScrollHorizontally(1)) {
                    this.this$0.setCurrentItem(this.this$0.mCurItem + 1);
                    return true;
                }
                return false;
            }
        }
    }
    
    public interface OnAdapterChangeListener
    {
        void onAdapterChanged(@NonNull final ViewPager p0, @Nullable final PagerAdapter p1, @Nullable final PagerAdapter p2);
    }
    
    public interface OnPageChangeListener
    {
        void onPageScrollStateChanged(final int p0);
        
        void onPageScrolled(final int p0, final float p1, @Px final int p2);
        
        void onPageSelected(final int p0);
    }
    
    public interface PageTransformer
    {
        void transformPage(@NonNull final View p0, final float p1);
    }
    
    private class PagerObserver extends DataSetObserver
    {
        final ViewPager this$0;
        
        PagerObserver(final ViewPager this$0) {
            this.this$0 = this$0;
        }
        
        public void onChanged() {
            this.this$0.dataSetChanged();
        }
        
        public void onInvalidated() {
            this.this$0.dataSetChanged();
        }
    }
    
    public static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        Parcelable adapterState;
        ClassLoader loader;
        int position;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$ClassLoaderCreator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel, null);
                }
                
                public SavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                    return new SavedState(parcel, classLoader);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        SavedState(final Parcel parcel, final ClassLoader classLoader) {
            super(parcel, classLoader);
            ClassLoader classLoader2 = classLoader;
            if (classLoader == null) {
                classLoader2 = this.getClass().getClassLoader();
            }
            this.position = parcel.readInt();
            this.adapterState = parcel.readParcelable(classLoader2);
            this.loader = classLoader2;
        }
        
        public SavedState(@NonNull final Parcelable parcelable) {
            super(parcelable);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("FragmentPager.SavedState{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" position=");
            sb.append(this.position);
            sb.append("}");
            return sb.toString();
        }
        
        @Override
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.position);
            parcel.writeParcelable(this.adapterState, n);
        }
    }
    
    public static class SimpleOnPageChangeListener implements OnPageChangeListener
    {
        @Override
        public void onPageScrollStateChanged(final int n) {
        }
        
        @Override
        public void onPageScrolled(final int n, final float n2, final int n3) {
        }
        
        @Override
        public void onPageSelected(final int n) {
        }
    }
    
    static class ViewPositionComparator implements Comparator<View>
    {
        @Override
        public int compare(final View view, final View view2) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            final LayoutParams layoutParams2 = (LayoutParams)view2.getLayoutParams();
            if (layoutParams.isDecor != layoutParams2.isDecor) {
                int n;
                if (layoutParams.isDecor) {
                    n = 1;
                }
                else {
                    n = -1;
                }
                return n;
            }
            return layoutParams.position - layoutParams2.position;
        }
    }
}
