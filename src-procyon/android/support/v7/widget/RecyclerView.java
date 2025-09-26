// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.support.v4.view.AbsSavedState;
import java.lang.ref.WeakReference;
import java.util.Collections;
import android.support.annotation.RestrictTo;
import android.os.Bundle;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.annotation.CallSuper;
import android.graphics.Matrix;
import android.view.ViewGroup$MarginLayoutParams;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import android.database.Observable;
import android.os.SystemClock;
import android.support.v4.util.Preconditions;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.MotionEventCompat;
import android.view.View$MeasureSpec;
import android.view.Display;
import android.support.annotation.Px;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.FocusFinder;
import android.widget.OverScroller;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.SparseArray;
import android.support.v4.os.TraceCompat;
import android.support.v4.widget.EdgeEffectCompat;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MotionEvent;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.view.accessibility.AccessibilityEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import android.view.ViewParent;
import android.view.ViewGroup$LayoutParams;
import android.content.res.TypedArray;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.recyclerview.R;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.view.ViewConfiguration;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.content.Context;
import android.os.Build$VERSION;
import android.view.VelocityTracker;
import android.graphics.RectF;
import android.graphics.Rect;
import android.support.v4.view.NestedScrollingChildHelper;
import java.util.List;
import java.util.ArrayList;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.NonNull;
import android.widget.EdgeEffect;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Interpolator;
import android.support.v4.view.NestedScrollingChild2;
import android.support.v4.view.ScrollingView;
import android.view.ViewGroup;

public class RecyclerView extends ViewGroup implements ScrollingView, NestedScrollingChild2
{
    static final boolean ALLOW_SIZE_IN_UNSPECIFIED_SPEC;
    static final boolean ALLOW_THREAD_GAP_WORK;
    private static final int[] CLIP_TO_PADDING_ATTR;
    static final boolean DEBUG = false;
    static final int DEFAULT_ORIENTATION = 1;
    static final boolean DISPATCH_TEMP_DETACH = false;
    private static final boolean FORCE_ABS_FOCUS_SEARCH_DIRECTION;
    static final boolean FORCE_INVALIDATE_DISPLAY_LIST;
    static final long FOREVER_NS = Long.MAX_VALUE;
    public static final int HORIZONTAL = 0;
    private static final boolean IGNORE_DETACHED_FOCUSED_CHILD;
    private static final int INVALID_POINTER = -1;
    public static final int INVALID_TYPE = -1;
    private static final Class<?>[] LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE;
    static final int MAX_SCROLL_DURATION = 2000;
    private static final int[] NESTED_SCROLLING_ATTRS;
    public static final long NO_ID = -1L;
    public static final int NO_POSITION = -1;
    static final boolean POST_UPDATES_ON_ANIMATION;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    static final String TAG = "RecyclerView";
    public static final int TOUCH_SLOP_DEFAULT = 0;
    public static final int TOUCH_SLOP_PAGING = 1;
    static final String TRACE_BIND_VIEW_TAG = "RV OnBindView";
    static final String TRACE_CREATE_VIEW_TAG = "RV CreateView";
    private static final String TRACE_HANDLE_ADAPTER_UPDATES_TAG = "RV PartialInvalidate";
    static final String TRACE_NESTED_PREFETCH_TAG = "RV Nested Prefetch";
    private static final String TRACE_ON_DATA_SET_CHANGE_LAYOUT_TAG = "RV FullInvalidate";
    private static final String TRACE_ON_LAYOUT_TAG = "RV OnLayout";
    static final String TRACE_PREFETCH_TAG = "RV Prefetch";
    static final String TRACE_SCROLL_TAG = "RV Scroll";
    static final boolean VERBOSE_TRACING = false;
    public static final int VERTICAL = 1;
    static final Interpolator sQuinticInterpolator;
    RecyclerViewAccessibilityDelegate mAccessibilityDelegate;
    private final AccessibilityManager mAccessibilityManager;
    private OnItemTouchListener mActiveOnItemTouchListener;
    Adapter mAdapter;
    AdapterHelper mAdapterHelper;
    boolean mAdapterUpdateDuringMeasure;
    private EdgeEffect mBottomGlow;
    private ChildDrawingOrderCallback mChildDrawingOrderCallback;
    ChildHelper mChildHelper;
    boolean mClipToPadding;
    boolean mDataSetHasChangedAfterLayout;
    boolean mDispatchItemsChangedEvent;
    private int mDispatchScrollCounter;
    private int mEatenAccessibilityChangeFlags;
    @NonNull
    private EdgeEffectFactory mEdgeEffectFactory;
    boolean mEnableFastScroller;
    @VisibleForTesting
    boolean mFirstLayoutComplete;
    GapWorker mGapWorker;
    boolean mHasFixedSize;
    private boolean mIgnoreMotionEventTillDown;
    private int mInitialTouchX;
    private int mInitialTouchY;
    private int mInterceptRequestLayoutDepth;
    boolean mIsAttached;
    ItemAnimator mItemAnimator;
    private ItemAnimatorListener mItemAnimatorListener;
    private Runnable mItemAnimatorRunner;
    final ArrayList<ItemDecoration> mItemDecorations;
    boolean mItemsAddedOrRemoved;
    boolean mItemsChanged;
    private int mLastTouchX;
    private int mLastTouchY;
    @VisibleForTesting
    LayoutManager mLayout;
    boolean mLayoutFrozen;
    private int mLayoutOrScrollCounter;
    boolean mLayoutWasDefered;
    private EdgeEffect mLeftGlow;
    private final int mMaxFlingVelocity;
    private final int mMinFlingVelocity;
    private final int[] mMinMaxLayoutPositions;
    private final int[] mNestedOffsets;
    private final RecyclerViewDataObserver mObserver;
    private List<OnChildAttachStateChangeListener> mOnChildAttachStateListeners;
    private OnFlingListener mOnFlingListener;
    private final ArrayList<OnItemTouchListener> mOnItemTouchListeners;
    @VisibleForTesting
    final List<ViewHolder> mPendingAccessibilityImportanceChange;
    private SavedState mPendingSavedState;
    boolean mPostedAnimatorRunner;
    GapWorker.LayoutPrefetchRegistryImpl mPrefetchRegistry;
    private boolean mPreserveFocusAfterLayout;
    final Recycler mRecycler;
    RecyclerListener mRecyclerListener;
    private EdgeEffect mRightGlow;
    private float mScaledHorizontalScrollFactor;
    private float mScaledVerticalScrollFactor;
    final int[] mScrollConsumed;
    private OnScrollListener mScrollListener;
    private List<OnScrollListener> mScrollListeners;
    private final int[] mScrollOffset;
    private int mScrollPointerId;
    private int mScrollState;
    final int[] mScrollStepConsumed;
    private NestedScrollingChildHelper mScrollingChildHelper;
    final State mState;
    final Rect mTempRect;
    private final Rect mTempRect2;
    final RectF mTempRectF;
    private EdgeEffect mTopGlow;
    private int mTouchSlop;
    final Runnable mUpdateChildViewsRunnable;
    private VelocityTracker mVelocityTracker;
    final ViewFlinger mViewFlinger;
    private final ViewInfoStore.ProcessCallback mViewInfoProcessCallback;
    final ViewInfoStore mViewInfoStore;
    
    static {
        NESTED_SCROLLING_ATTRS = new int[] { 16843830 };
        CLIP_TO_PADDING_ATTR = new int[] { 16842987 };
        FORCE_INVALIDATE_DISPLAY_LIST = (Build$VERSION.SDK_INT == 18 || Build$VERSION.SDK_INT == 19 || Build$VERSION.SDK_INT == 20);
        ALLOW_SIZE_IN_UNSPECIFIED_SPEC = (Build$VERSION.SDK_INT >= 23);
        POST_UPDATES_ON_ANIMATION = (Build$VERSION.SDK_INT >= 16);
        ALLOW_THREAD_GAP_WORK = (Build$VERSION.SDK_INT >= 21);
        FORCE_ABS_FOCUS_SEARCH_DIRECTION = (Build$VERSION.SDK_INT <= 15);
        IGNORE_DETACHED_FOCUSED_CHILD = (Build$VERSION.SDK_INT <= 15);
        LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE = new Class[] { Context.class, AttributeSet.class, Integer.TYPE, Integer.TYPE };
        sQuinticInterpolator = (Interpolator)new Interpolator() {
            public float getInterpolation(float n) {
                --n;
                return n * n * n * n * n + 1.0f;
            }
        };
    }
    
    public RecyclerView(@NonNull final Context context) {
        this(context, null);
    }
    
    public RecyclerView(@NonNull final Context context, @Nullable final AttributeSet set) {
        this(context, set, 0);
    }
    
    public RecyclerView(@NonNull final Context context, @Nullable final AttributeSet set, final int n) {
        super(context, set, n);
        this.mObserver = new RecyclerViewDataObserver();
        this.mRecycler = new Recycler();
        this.mViewInfoStore = new ViewInfoStore();
        this.mUpdateChildViewsRunnable = new Runnable() {
            final RecyclerView this$0;
            
            @Override
            public void run() {
                if (!this.this$0.mFirstLayoutComplete || this.this$0.isLayoutRequested()) {
                    return;
                }
                if (!this.this$0.mIsAttached) {
                    this.this$0.requestLayout();
                    return;
                }
                if (this.this$0.mLayoutFrozen) {
                    this.this$0.mLayoutWasDefered = true;
                    return;
                }
                this.this$0.consumePendingUpdateOperations();
            }
        };
        this.mTempRect = new Rect();
        this.mTempRect2 = new Rect();
        this.mTempRectF = new RectF();
        this.mItemDecorations = new ArrayList<ItemDecoration>();
        this.mOnItemTouchListeners = new ArrayList<OnItemTouchListener>();
        this.mInterceptRequestLayoutDepth = 0;
        this.mDataSetHasChangedAfterLayout = false;
        this.mDispatchItemsChangedEvent = false;
        this.mLayoutOrScrollCounter = 0;
        this.mDispatchScrollCounter = 0;
        this.mEdgeEffectFactory = new EdgeEffectFactory();
        this.mItemAnimator = (ItemAnimator)new DefaultItemAnimator();
        this.mScrollState = 0;
        this.mScrollPointerId = -1;
        this.mScaledHorizontalScrollFactor = Float.MIN_VALUE;
        this.mScaledVerticalScrollFactor = Float.MIN_VALUE;
        final boolean b = true;
        this.mPreserveFocusAfterLayout = true;
        this.mViewFlinger = new ViewFlinger();
        GapWorker.LayoutPrefetchRegistryImpl mPrefetchRegistry;
        if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
            mPrefetchRegistry = new GapWorker.LayoutPrefetchRegistryImpl();
        }
        else {
            mPrefetchRegistry = null;
        }
        this.mPrefetchRegistry = mPrefetchRegistry;
        this.mState = new State();
        this.mItemsAddedOrRemoved = false;
        this.mItemsChanged = false;
        this.mItemAnimatorListener = (ItemAnimatorListener)new ItemAnimatorRestoreListener();
        this.mPostedAnimatorRunner = false;
        this.mMinMaxLayoutPositions = new int[2];
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.mNestedOffsets = new int[2];
        this.mScrollStepConsumed = new int[2];
        this.mPendingAccessibilityImportanceChange = new ArrayList<ViewHolder>();
        this.mItemAnimatorRunner = new Runnable() {
            final RecyclerView this$0;
            
            @Override
            public void run() {
                if (this.this$0.mItemAnimator != null) {
                    this.this$0.mItemAnimator.runPendingAnimations();
                }
                this.this$0.mPostedAnimatorRunner = false;
            }
        };
        this.mViewInfoProcessCallback = new ViewInfoStore.ProcessCallback() {
            final RecyclerView this$0;
            
            @Override
            public void processAppeared(final ViewHolder viewHolder, final ItemHolderInfo itemHolderInfo, final ItemHolderInfo itemHolderInfo2) {
                this.this$0.animateAppearance(viewHolder, itemHolderInfo, itemHolderInfo2);
            }
            
            @Override
            public void processDisappeared(final ViewHolder viewHolder, @NonNull final ItemHolderInfo itemHolderInfo, @Nullable final ItemHolderInfo itemHolderInfo2) {
                this.this$0.mRecycler.unscrapView(viewHolder);
                this.this$0.animateDisappearance(viewHolder, itemHolderInfo, itemHolderInfo2);
            }
            
            @Override
            public void processPersistent(final ViewHolder viewHolder, @NonNull final ItemHolderInfo itemHolderInfo, @NonNull final ItemHolderInfo itemHolderInfo2) {
                viewHolder.setIsRecyclable(false);
                if (this.this$0.mDataSetHasChangedAfterLayout) {
                    if (this.this$0.mItemAnimator.animateChange(viewHolder, viewHolder, itemHolderInfo, itemHolderInfo2)) {
                        this.this$0.postAnimationRunner();
                    }
                }
                else if (this.this$0.mItemAnimator.animatePersistence(viewHolder, itemHolderInfo, itemHolderInfo2)) {
                    this.this$0.postAnimationRunner();
                }
            }
            
            @Override
            public void unused(final ViewHolder viewHolder) {
                this.this$0.mLayout.removeAndRecycleView(viewHolder.itemView, this.this$0.mRecycler);
            }
        };
        if (set != null) {
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, RecyclerView.CLIP_TO_PADDING_ATTR, n, 0);
            this.mClipToPadding = obtainStyledAttributes.getBoolean(0, true);
            obtainStyledAttributes.recycle();
        }
        else {
            this.mClipToPadding = true;
        }
        this.setScrollContainer(true);
        this.setFocusableInTouchMode(true);
        final ViewConfiguration value = ViewConfiguration.get(context);
        this.mTouchSlop = value.getScaledTouchSlop();
        this.mScaledHorizontalScrollFactor = ViewConfigurationCompat.getScaledHorizontalScrollFactor(value, context);
        this.mScaledVerticalScrollFactor = ViewConfigurationCompat.getScaledVerticalScrollFactor(value, context);
        this.mMinFlingVelocity = value.getScaledMinimumFlingVelocity();
        this.mMaxFlingVelocity = value.getScaledMaximumFlingVelocity();
        this.setWillNotDraw(this.getOverScrollMode() == 2);
        this.mItemAnimator.setListener(this.mItemAnimatorListener);
        this.initAdapterManager();
        this.initChildrenHelper();
        this.initAutofill();
        if (ViewCompat.getImportantForAccessibility((View)this) == 0) {
            ViewCompat.setImportantForAccessibility((View)this, 1);
        }
        this.mAccessibilityManager = (AccessibilityManager)this.getContext().getSystemService("accessibility");
        this.setAccessibilityDelegateCompat(new RecyclerViewAccessibilityDelegate(this));
        boolean boolean1;
        if (set != null) {
            final TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(set, R.styleable.RecyclerView, n, 0);
            final String string = obtainStyledAttributes2.getString(R.styleable.RecyclerView_layoutManager);
            if (obtainStyledAttributes2.getInt(R.styleable.RecyclerView_android_descendantFocusability, -1) == -1) {
                this.setDescendantFocusability(262144);
            }
            this.mEnableFastScroller = obtainStyledAttributes2.getBoolean(R.styleable.RecyclerView_fastScrollEnabled, false);
            if (this.mEnableFastScroller) {
                this.initFastScroller((StateListDrawable)obtainStyledAttributes2.getDrawable(R.styleable.RecyclerView_fastScrollVerticalThumbDrawable), obtainStyledAttributes2.getDrawable(R.styleable.RecyclerView_fastScrollVerticalTrackDrawable), (StateListDrawable)obtainStyledAttributes2.getDrawable(R.styleable.RecyclerView_fastScrollHorizontalThumbDrawable), obtainStyledAttributes2.getDrawable(R.styleable.RecyclerView_fastScrollHorizontalTrackDrawable));
            }
            obtainStyledAttributes2.recycle();
            this.createLayoutManager(context, string, set, n, 0);
            boolean1 = b;
            if (Build$VERSION.SDK_INT >= 21) {
                final TypedArray obtainStyledAttributes3 = context.obtainStyledAttributes(set, RecyclerView.NESTED_SCROLLING_ATTRS, n, 0);
                boolean1 = obtainStyledAttributes3.getBoolean(0, true);
                obtainStyledAttributes3.recycle();
            }
        }
        else {
            this.setDescendantFocusability(262144);
            boolean1 = b;
        }
        this.setNestedScrollingEnabled(boolean1);
    }
    
    static /* synthetic */ void access$000(final RecyclerView recyclerView, final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        recyclerView.attachViewToParent(view, n, viewGroup$LayoutParams);
    }
    
    static /* synthetic */ void access$100(final RecyclerView recyclerView, final int n) {
        recyclerView.detachViewFromParent(n);
    }
    
    static /* synthetic */ boolean access$200(final RecyclerView recyclerView) {
        return recyclerView.awakenScrollBars();
    }
    
    static /* synthetic */ void access$300(final RecyclerView recyclerView, final int n, final int n2) {
        recyclerView.setMeasuredDimension(n, n2);
    }
    
    private void addAnimatingView(final ViewHolder viewHolder) {
        final View itemView = viewHolder.itemView;
        final boolean b = itemView.getParent() == this;
        this.mRecycler.unscrapView(this.getChildViewHolder(itemView));
        if (viewHolder.isTmpDetached()) {
            this.mChildHelper.attachViewToParent(itemView, -1, itemView.getLayoutParams(), true);
        }
        else if (!b) {
            this.mChildHelper.addView(itemView, true);
        }
        else {
            this.mChildHelper.hide(itemView);
        }
    }
    
    private void animateChange(@NonNull final ViewHolder mShadowingHolder, @NonNull final ViewHolder mShadowedHolder, @NonNull final ItemHolderInfo itemHolderInfo, @NonNull final ItemHolderInfo itemHolderInfo2, final boolean b, final boolean b2) {
        mShadowingHolder.setIsRecyclable(false);
        if (b) {
            this.addAnimatingView(mShadowingHolder);
        }
        if (mShadowingHolder != mShadowedHolder) {
            if (b2) {
                this.addAnimatingView(mShadowedHolder);
            }
            mShadowingHolder.mShadowedHolder = mShadowedHolder;
            this.addAnimatingView(mShadowingHolder);
            this.mRecycler.unscrapView(mShadowingHolder);
            mShadowedHolder.setIsRecyclable(false);
            mShadowedHolder.mShadowingHolder = mShadowingHolder;
        }
        if (this.mItemAnimator.animateChange(mShadowingHolder, mShadowedHolder, itemHolderInfo, itemHolderInfo2)) {
            this.postAnimationRunner();
        }
    }
    
    private void cancelTouch() {
        this.resetTouch();
        this.setScrollState(0);
    }
    
    static void clearNestedRecyclerViewIfNotNested(@NonNull final ViewHolder viewHolder) {
        if (viewHolder.mNestedRecyclerView != null) {
            View view = viewHolder.mNestedRecyclerView.get();
            while (view != null) {
                if (view == viewHolder.itemView) {
                    return;
                }
                final ViewParent parent = view.getParent();
                if (parent instanceof View) {
                    view = (View)parent;
                }
                else {
                    view = null;
                }
            }
            viewHolder.mNestedRecyclerView = null;
        }
    }
    
    private void createLayoutManager(Context ex, String trim, final AttributeSet set, final int i, final int j) {
        if (trim != null) {
            trim = trim.trim();
            if (!trim.isEmpty()) {
                final String fullClassName = this.getFullClassName((Context)ex, trim);
                try {
                    ClassLoader classLoader;
                    if (this.isInEditMode()) {
                        classLoader = this.getClass().getClassLoader();
                    }
                    else {
                        classLoader = ((Context)ex).getClassLoader();
                    }
                    final Class<? extends LayoutManager> subclass = classLoader.loadClass(fullClassName).asSubclass(LayoutManager.class);
                    final NoSuchMethodException ex2 = null;
                    try {
                        final Constructor<? extends LayoutManager> constructor = subclass.getConstructor(RecyclerView.LAYOUT_MANAGER_CONSTRUCTOR_SIGNATURE);
                        ex = (NoSuchMethodException)new Object[] { ex, set, i, j };
                    }
                    catch (final NoSuchMethodException ex) {
                        try {
                            final Constructor<? extends LayoutManager> constructor = subclass.getConstructor((Class<?>[])new Class[0]);
                            ex = ex2;
                            constructor.setAccessible(true);
                            this.setLayoutManager((LayoutManager)constructor.newInstance((Object[])(Object)ex));
                        }
                        catch (final NoSuchMethodException cause) {
                            cause.initCause(ex);
                            final StringBuilder sb = new StringBuilder();
                            sb.append(set.getPositionDescription());
                            sb.append(": Error creating LayoutManager ");
                            sb.append(fullClassName);
                            throw new IllegalStateException(sb.toString(), cause);
                        }
                    }
                }
                catch (final ClassCastException cause2) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(set.getPositionDescription());
                    sb2.append(": Class is not a LayoutManager ");
                    sb2.append(fullClassName);
                    throw new IllegalStateException(sb2.toString(), cause2);
                }
                catch (final IllegalAccessException cause3) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append(set.getPositionDescription());
                    sb3.append(": Cannot access non-public constructor ");
                    sb3.append(fullClassName);
                    throw new IllegalStateException(sb3.toString(), cause3);
                }
                catch (final InstantiationException cause4) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append(set.getPositionDescription());
                    sb4.append(": Could not instantiate the LayoutManager: ");
                    sb4.append(fullClassName);
                    throw new IllegalStateException(sb4.toString(), cause4);
                }
                catch (final InvocationTargetException cause5) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append(set.getPositionDescription());
                    sb5.append(": Could not instantiate the LayoutManager: ");
                    sb5.append(fullClassName);
                    throw new IllegalStateException(sb5.toString(), cause5);
                }
                catch (final ClassNotFoundException cause6) {
                    final StringBuilder sb6 = new StringBuilder();
                    sb6.append(set.getPositionDescription());
                    sb6.append(": Unable to find LayoutManager ");
                    sb6.append(fullClassName);
                    throw new IllegalStateException(sb6.toString(), cause6);
                }
            }
        }
    }
    
    private boolean didChildRangeChange(final int n, final int n2) {
        this.findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
        final int[] mMinMaxLayoutPositions = this.mMinMaxLayoutPositions;
        boolean b = false;
        if (mMinMaxLayoutPositions[0] != n || this.mMinMaxLayoutPositions[1] != n2) {
            b = true;
        }
        return b;
    }
    
    private void dispatchContentChangedIfNecessary() {
        final int mEatenAccessibilityChangeFlags = this.mEatenAccessibilityChangeFlags;
        this.mEatenAccessibilityChangeFlags = 0;
        if (mEatenAccessibilityChangeFlags != 0 && this.isAccessibilityEnabled()) {
            final AccessibilityEvent obtain = AccessibilityEvent.obtain();
            obtain.setEventType(2048);
            AccessibilityEventCompat.setContentChangeTypes(obtain, mEatenAccessibilityChangeFlags);
            this.sendAccessibilityEventUnchecked(obtain);
        }
    }
    
    private void dispatchLayoutStep1() {
        final State mState = this.mState;
        boolean mTrackOldChangeHolders = true;
        mState.assertLayoutStep(1);
        this.fillRemainingScrollValues(this.mState);
        this.mState.mIsMeasuring = false;
        this.startInterceptRequestLayout();
        this.mViewInfoStore.clear();
        this.onEnterLayoutOrScroll();
        this.processAdapterUpdatesAndSetAnimationFlags();
        this.saveFocusInfo();
        final State mState2 = this.mState;
        if (!this.mState.mRunSimpleAnimations || !this.mItemsChanged) {
            mTrackOldChangeHolders = false;
        }
        mState2.mTrackOldChangeHolders = mTrackOldChangeHolders;
        this.mItemsChanged = false;
        this.mItemsAddedOrRemoved = false;
        this.mState.mInPreLayout = this.mState.mRunPredictiveAnimations;
        this.mState.mItemCount = this.mAdapter.getItemCount();
        this.findMinMaxChildLayoutPositions(this.mMinMaxLayoutPositions);
        if (this.mState.mRunSimpleAnimations) {
            for (int childCount = this.mChildHelper.getChildCount(), i = 0; i < childCount; ++i) {
                final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
                if (!childViewHolderInt.shouldIgnore()) {
                    if (!childViewHolderInt.isInvalid() || this.mAdapter.hasStableIds()) {
                        this.mViewInfoStore.addToPreLayout(childViewHolderInt, this.mItemAnimator.recordPreLayoutInformation(this.mState, childViewHolderInt, ItemAnimator.buildAdapterChangeFlagsForAnimations(childViewHolderInt), childViewHolderInt.getUnmodifiedPayloads()));
                        if (this.mState.mTrackOldChangeHolders && childViewHolderInt.isUpdated() && !childViewHolderInt.isRemoved() && !childViewHolderInt.shouldIgnore() && !childViewHolderInt.isInvalid()) {
                            this.mViewInfoStore.addToOldChangeHolders(this.getChangedHolderKey(childViewHolderInt), childViewHolderInt);
                        }
                    }
                }
            }
        }
        if (this.mState.mRunPredictiveAnimations) {
            this.saveOldPositions();
            final boolean mStructureChanged = this.mState.mStructureChanged;
            this.mState.mStructureChanged = false;
            this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
            this.mState.mStructureChanged = mStructureChanged;
            for (int j = 0; j < this.mChildHelper.getChildCount(); ++j) {
                final ViewHolder childViewHolderInt2 = getChildViewHolderInt(this.mChildHelper.getChildAt(j));
                if (!childViewHolderInt2.shouldIgnore()) {
                    if (!this.mViewInfoStore.isInPreLayout(childViewHolderInt2)) {
                        final int buildAdapterChangeFlagsForAnimations = ItemAnimator.buildAdapterChangeFlagsForAnimations(childViewHolderInt2);
                        final boolean hasAnyOfTheFlags = childViewHolderInt2.hasAnyOfTheFlags(8192);
                        int n = buildAdapterChangeFlagsForAnimations;
                        if (!hasAnyOfTheFlags) {
                            n = (buildAdapterChangeFlagsForAnimations | 0x1000);
                        }
                        final ItemHolderInfo recordPreLayoutInformation = this.mItemAnimator.recordPreLayoutInformation(this.mState, childViewHolderInt2, n, childViewHolderInt2.getUnmodifiedPayloads());
                        if (hasAnyOfTheFlags) {
                            this.recordAnimationInfoIfBouncedHiddenView(childViewHolderInt2, recordPreLayoutInformation);
                        }
                        else {
                            this.mViewInfoStore.addToAppearedInPreLayoutHolders(childViewHolderInt2, recordPreLayoutInformation);
                        }
                    }
                }
            }
            this.clearOldPositions();
        }
        else {
            this.clearOldPositions();
        }
        this.onExitLayoutOrScroll();
        this.stopInterceptRequestLayout(false);
        this.mState.mLayoutStep = 2;
    }
    
    private void dispatchLayoutStep2() {
        this.startInterceptRequestLayout();
        this.onEnterLayoutOrScroll();
        this.mState.assertLayoutStep(6);
        this.mAdapterHelper.consumeUpdatesInOnePass();
        this.mState.mItemCount = this.mAdapter.getItemCount();
        this.mState.mDeletedInvisibleItemCountSincePreviousLayout = 0;
        this.mState.mInPreLayout = false;
        this.mLayout.onLayoutChildren(this.mRecycler, this.mState);
        this.mState.mStructureChanged = false;
        this.mPendingSavedState = null;
        this.mState.mRunSimpleAnimations = (this.mState.mRunSimpleAnimations && this.mItemAnimator != null);
        this.mState.mLayoutStep = 4;
        this.onExitLayoutOrScroll();
        this.stopInterceptRequestLayout(false);
    }
    
    private void dispatchLayoutStep3() {
        this.mState.assertLayoutStep(4);
        this.startInterceptRequestLayout();
        this.onEnterLayoutOrScroll();
        this.mState.mLayoutStep = 1;
        if (this.mState.mRunSimpleAnimations) {
            for (int i = this.mChildHelper.getChildCount() - 1; i >= 0; --i) {
                final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
                if (!childViewHolderInt.shouldIgnore()) {
                    final long changedHolderKey = this.getChangedHolderKey(childViewHolderInt);
                    final ItemHolderInfo recordPostLayoutInformation = this.mItemAnimator.recordPostLayoutInformation(this.mState, childViewHolderInt);
                    final ViewHolder fromOldChangeHolders = this.mViewInfoStore.getFromOldChangeHolders(changedHolderKey);
                    if (fromOldChangeHolders != null && !fromOldChangeHolders.shouldIgnore()) {
                        final boolean disappearing = this.mViewInfoStore.isDisappearing(fromOldChangeHolders);
                        final boolean disappearing2 = this.mViewInfoStore.isDisappearing(childViewHolderInt);
                        if (disappearing && fromOldChangeHolders == childViewHolderInt) {
                            this.mViewInfoStore.addToPostLayout(childViewHolderInt, recordPostLayoutInformation);
                        }
                        else {
                            final ItemHolderInfo popFromPreLayout = this.mViewInfoStore.popFromPreLayout(fromOldChangeHolders);
                            this.mViewInfoStore.addToPostLayout(childViewHolderInt, recordPostLayoutInformation);
                            final ItemHolderInfo popFromPostLayout = this.mViewInfoStore.popFromPostLayout(childViewHolderInt);
                            if (popFromPreLayout == null) {
                                this.handleMissingPreInfoForChangeError(changedHolderKey, childViewHolderInt, fromOldChangeHolders);
                            }
                            else {
                                this.animateChange(fromOldChangeHolders, childViewHolderInt, popFromPreLayout, popFromPostLayout, disappearing, disappearing2);
                            }
                        }
                    }
                    else {
                        this.mViewInfoStore.addToPostLayout(childViewHolderInt, recordPostLayoutInformation);
                    }
                }
            }
            this.mViewInfoStore.process(this.mViewInfoProcessCallback);
        }
        this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
        this.mState.mPreviousLayoutItemCount = this.mState.mItemCount;
        this.mDataSetHasChangedAfterLayout = false;
        this.mDispatchItemsChangedEvent = false;
        this.mState.mRunSimpleAnimations = false;
        this.mState.mRunPredictiveAnimations = false;
        this.mLayout.mRequestedSimpleAnimations = false;
        if (this.mRecycler.mChangedScrap != null) {
            this.mRecycler.mChangedScrap.clear();
        }
        if (this.mLayout.mPrefetchMaxObservedInInitialPrefetch) {
            this.mLayout.mPrefetchMaxCountObserved = 0;
            this.mLayout.mPrefetchMaxObservedInInitialPrefetch = false;
            this.mRecycler.updateViewCacheSize();
        }
        this.mLayout.onLayoutCompleted(this.mState);
        this.onExitLayoutOrScroll();
        this.stopInterceptRequestLayout(false);
        this.mViewInfoStore.clear();
        if (this.didChildRangeChange(this.mMinMaxLayoutPositions[0], this.mMinMaxLayoutPositions[1])) {
            this.dispatchOnScrolled(0, 0);
        }
        this.recoverFocusFromState();
        this.resetFocusInfo();
    }
    
    private boolean dispatchOnItemTouch(final MotionEvent motionEvent) {
        final int action = motionEvent.getAction();
        if (this.mActiveOnItemTouchListener != null) {
            if (action != 0) {
                this.mActiveOnItemTouchListener.onTouchEvent(this, motionEvent);
                if (action == 3 || action == 1) {
                    this.mActiveOnItemTouchListener = null;
                }
                return true;
            }
            this.mActiveOnItemTouchListener = null;
        }
        if (action != 0) {
            for (int size = this.mOnItemTouchListeners.size(), i = 0; i < size; ++i) {
                final OnItemTouchListener mActiveOnItemTouchListener = this.mOnItemTouchListeners.get(i);
                if (mActiveOnItemTouchListener.onInterceptTouchEvent(this, motionEvent)) {
                    this.mActiveOnItemTouchListener = mActiveOnItemTouchListener;
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean dispatchOnItemTouchIntercept(final MotionEvent motionEvent) {
        final int action = motionEvent.getAction();
        if (action == 3 || action == 0) {
            this.mActiveOnItemTouchListener = null;
        }
        for (int size = this.mOnItemTouchListeners.size(), i = 0; i < size; ++i) {
            final OnItemTouchListener mActiveOnItemTouchListener = this.mOnItemTouchListeners.get(i);
            if (mActiveOnItemTouchListener.onInterceptTouchEvent(this, motionEvent) && action != 3) {
                this.mActiveOnItemTouchListener = mActiveOnItemTouchListener;
                return true;
            }
        }
        return false;
    }
    
    private void findMinMaxChildLayoutPositions(final int[] array) {
        final int childCount = this.mChildHelper.getChildCount();
        if (childCount == 0) {
            array[1] = (array[0] = -1);
            return;
        }
        int n = Integer.MIN_VALUE;
        int n2 = Integer.MAX_VALUE;
        int n3;
        for (int i = 0; i < childCount; ++i, n = n3) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (childViewHolderInt.shouldIgnore()) {
                n3 = n;
            }
            else {
                final int layoutPosition = childViewHolderInt.getLayoutPosition();
                int n4;
                if (layoutPosition < (n4 = n2)) {
                    n4 = layoutPosition;
                }
                n2 = n4;
                if (layoutPosition > (n3 = n)) {
                    n3 = layoutPosition;
                    n2 = n4;
                }
            }
        }
        array[0] = n2;
        array[1] = n;
    }
    
    @Nullable
    static RecyclerView findNestedRecyclerView(@NonNull final View view) {
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        if (view instanceof RecyclerView) {
            return (RecyclerView)view;
        }
        final ViewGroup viewGroup = (ViewGroup)view;
        for (int childCount = viewGroup.getChildCount(), i = 0; i < childCount; ++i) {
            final RecyclerView nestedRecyclerView = findNestedRecyclerView(viewGroup.getChildAt(i));
            if (nestedRecyclerView != null) {
                return nestedRecyclerView;
            }
        }
        return null;
    }
    
    @Nullable
    private View findNextViewToFocus() {
        int mFocusedItemPosition;
        if (this.mState.mFocusedItemPosition != -1) {
            mFocusedItemPosition = this.mState.mFocusedItemPosition;
        }
        else {
            mFocusedItemPosition = 0;
        }
        final int itemCount = this.mState.getItemCount();
        for (int i = mFocusedItemPosition; i < itemCount; ++i) {
            final ViewHolder viewHolderForAdapterPosition = this.findViewHolderForAdapterPosition(i);
            if (viewHolderForAdapterPosition == null) {
                break;
            }
            if (viewHolderForAdapterPosition.itemView.hasFocusable()) {
                return viewHolderForAdapterPosition.itemView;
            }
        }
        for (int j = Math.min(itemCount, mFocusedItemPosition) - 1; j >= 0; --j) {
            final ViewHolder viewHolderForAdapterPosition2 = this.findViewHolderForAdapterPosition(j);
            if (viewHolderForAdapterPosition2 == null) {
                return null;
            }
            if (viewHolderForAdapterPosition2.itemView.hasFocusable()) {
                return viewHolderForAdapterPosition2.itemView;
            }
        }
        return null;
    }
    
    static ViewHolder getChildViewHolderInt(final View view) {
        if (view == null) {
            return null;
        }
        return ((LayoutParams)view.getLayoutParams()).mViewHolder;
    }
    
    static void getDecoratedBoundsWithMarginsInt(final View view, final Rect rect) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        final Rect mDecorInsets = layoutParams.mDecorInsets;
        rect.set(view.getLeft() - mDecorInsets.left - layoutParams.leftMargin, view.getTop() - mDecorInsets.top - layoutParams.topMargin, view.getRight() + mDecorInsets.right + layoutParams.rightMargin, view.getBottom() + mDecorInsets.bottom + layoutParams.bottomMargin);
    }
    
    private int getDeepestFocusedViewWithId(View focusedChild) {
        int n = focusedChild.getId();
        while (!focusedChild.isFocused() && focusedChild instanceof ViewGroup && focusedChild.hasFocus()) {
            final View view = focusedChild = ((ViewGroup)focusedChild).getFocusedChild();
            if (view.getId() != -1) {
                n = view.getId();
                focusedChild = view;
            }
        }
        return n;
    }
    
    private String getFullClassName(final Context context, final String s) {
        if (s.charAt(0) == '.') {
            final StringBuilder sb = new StringBuilder();
            sb.append(context.getPackageName());
            sb.append(s);
            return sb.toString();
        }
        if (s.contains(".")) {
            return s;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(RecyclerView.class.getPackage().getName());
        sb2.append('.');
        sb2.append(s);
        return sb2.toString();
    }
    
    private NestedScrollingChildHelper getScrollingChildHelper() {
        if (this.mScrollingChildHelper == null) {
            this.mScrollingChildHelper = new NestedScrollingChildHelper((View)this);
        }
        return this.mScrollingChildHelper;
    }
    
    private void handleMissingPreInfoForChangeError(final long n, final ViewHolder obj, final ViewHolder obj2) {
        for (int childCount = this.mChildHelper.getChildCount(), i = 0; i < childCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (childViewHolderInt != obj) {
                if (this.getChangedHolderKey(childViewHolderInt) == n) {
                    if (this.mAdapter != null && this.mAdapter.hasStableIds()) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Two different ViewHolders have the same stable ID. Stable IDs in your adapter MUST BE unique and SHOULD NOT change.\n ViewHolder 1:");
                        sb.append(childViewHolderInt);
                        sb.append(" \n View Holder 2:");
                        sb.append(obj);
                        sb.append(this.exceptionLabel());
                        throw new IllegalStateException(sb.toString());
                    }
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Two different ViewHolders have the same change ID. This might happen due to inconsistent Adapter update events or if the LayoutManager lays out the same View multiple times.\n ViewHolder 1:");
                    sb2.append(childViewHolderInt);
                    sb2.append(" \n View Holder 2:");
                    sb2.append(obj);
                    sb2.append(this.exceptionLabel());
                    throw new IllegalStateException(sb2.toString());
                }
            }
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("Problem while matching changed view holders with the newones. The pre-layout information for the change holder ");
        sb3.append(obj2);
        sb3.append(" cannot be found but it is necessary for ");
        sb3.append(obj);
        sb3.append(this.exceptionLabel());
        Log.e("RecyclerView", sb3.toString());
    }
    
    private boolean hasUpdatedView() {
        for (int childCount = this.mChildHelper.getChildCount(), i = 0; i < childCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getChildAt(i));
            if (childViewHolderInt != null) {
                if (!childViewHolderInt.shouldIgnore()) {
                    if (childViewHolderInt.isUpdated()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    @SuppressLint({ "InlinedApi" })
    private void initAutofill() {
        if (ViewCompat.getImportantForAutofill((View)this) == 0) {
            ViewCompat.setImportantForAutofill((View)this, 8);
        }
    }
    
    private void initChildrenHelper() {
        this.mChildHelper = new ChildHelper((ChildHelper.Callback)new ChildHelper.Callback(this) {
            final RecyclerView this$0;
            
            @Override
            public void addView(final View view, final int n) {
                this.this$0.addView(view, n);
                this.this$0.dispatchChildAttached(view);
            }
            
            @Override
            public void attachViewToParent(final View view, final int n, final ViewGroup$LayoutParams viewGroup$LayoutParams) {
                final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
                if (childViewHolderInt != null) {
                    if (!childViewHolderInt.isTmpDetached() && !childViewHolderInt.shouldIgnore()) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Called attach on a child which is not detached: ");
                        sb.append(childViewHolderInt);
                        sb.append(this.this$0.exceptionLabel());
                        throw new IllegalArgumentException(sb.toString());
                    }
                    childViewHolderInt.clearTmpDetachFlag();
                }
                RecyclerView.access$000(this.this$0, view, n, viewGroup$LayoutParams);
            }
            
            @Override
            public void detachViewFromParent(final int n) {
                final View child = this.getChildAt(n);
                if (child != null) {
                    final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(child);
                    if (childViewHolderInt != null) {
                        if (childViewHolderInt.isTmpDetached() && !childViewHolderInt.shouldIgnore()) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("called detach on an already detached child ");
                            sb.append(childViewHolderInt);
                            sb.append(this.this$0.exceptionLabel());
                            throw new IllegalArgumentException(sb.toString());
                        }
                        childViewHolderInt.addFlags(256);
                    }
                }
                RecyclerView.access$100(this.this$0, n);
            }
            
            @Override
            public View getChildAt(final int n) {
                return this.this$0.getChildAt(n);
            }
            
            @Override
            public int getChildCount() {
                return this.this$0.getChildCount();
            }
            
            @Override
            public ViewHolder getChildViewHolder(final View view) {
                return RecyclerView.getChildViewHolderInt(view);
            }
            
            @Override
            public int indexOfChild(final View view) {
                return this.this$0.indexOfChild(view);
            }
            
            @Override
            public void onEnteredHiddenState(final View view) {
                final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
                if (childViewHolderInt != null) {
                    childViewHolderInt.onEnteredHiddenState(this.this$0);
                }
            }
            
            @Override
            public void onLeftHiddenState(final View view) {
                final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
                if (childViewHolderInt != null) {
                    childViewHolderInt.onLeftHiddenState(this.this$0);
                }
            }
            
            @Override
            public void removeAllViews() {
                for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                    final View child = this.getChildAt(i);
                    this.this$0.dispatchChildDetached(child);
                    child.clearAnimation();
                }
                this.this$0.removeAllViews();
            }
            
            @Override
            public void removeViewAt(final int n) {
                final View child = this.this$0.getChildAt(n);
                if (child != null) {
                    this.this$0.dispatchChildDetached(child);
                    child.clearAnimation();
                }
                this.this$0.removeViewAt(n);
            }
        });
    }
    
    private boolean isPreferredNextFocus(final View view, final View view2, final int i) {
        final boolean b = false;
        final boolean b2 = false;
        final boolean b3 = false;
        final boolean b4 = false;
        boolean b5 = false;
        final boolean b6 = false;
        if (view2 == null || view2 == this) {
            return false;
        }
        if (this.findContainingItemView(view2) == null) {
            return false;
        }
        if (view == null) {
            return true;
        }
        if (this.findContainingItemView(view) == null) {
            return true;
        }
        this.mTempRect.set(0, 0, view.getWidth(), view.getHeight());
        this.mTempRect2.set(0, 0, view2.getWidth(), view2.getHeight());
        this.offsetDescendantRectToMyCoords(view, this.mTempRect);
        this.offsetDescendantRectToMyCoords(view2, this.mTempRect2);
        final int layoutDirection = this.mLayout.getLayoutDirection();
        int n = -1;
        int n2;
        if (layoutDirection == 1) {
            n2 = -1;
        }
        else {
            n2 = 1;
        }
        int n3;
        if ((this.mTempRect.left < this.mTempRect2.left || this.mTempRect.right <= this.mTempRect2.left) && this.mTempRect.right < this.mTempRect2.right) {
            n3 = 1;
        }
        else if ((this.mTempRect.right > this.mTempRect2.right || this.mTempRect.left >= this.mTempRect2.right) && this.mTempRect.left > this.mTempRect2.left) {
            n3 = -1;
        }
        else {
            n3 = 0;
        }
        if ((this.mTempRect.top < this.mTempRect2.top || this.mTempRect.bottom <= this.mTempRect2.top) && this.mTempRect.bottom < this.mTempRect2.bottom) {
            n = 1;
        }
        else if ((this.mTempRect.bottom <= this.mTempRect2.bottom && this.mTempRect.top < this.mTempRect2.bottom) || this.mTempRect.top <= this.mTempRect2.top) {
            n = 0;
        }
        if (i == 17) {
            if (n3 < 0) {
                b5 = true;
            }
            return b5;
        }
        if (i == 33) {
            boolean b7 = b4;
            if (n < 0) {
                b7 = true;
            }
            return b7;
        }
        if (i == 66) {
            boolean b8 = b3;
            if (n3 > 0) {
                b8 = true;
            }
            return b8;
        }
        if (i == 130) {
            boolean b9 = b2;
            if (n > 0) {
                b9 = true;
            }
            return b9;
        }
        switch (i) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid direction: ");
                sb.append(i);
                sb.append(this.exceptionLabel());
                throw new IllegalArgumentException(sb.toString());
            }
            case 2: {
                if (n <= 0) {
                    boolean b10 = b6;
                    if (n != 0) {
                        return b10;
                    }
                    b10 = b6;
                    if (n3 * n2 < 0) {
                        return b10;
                    }
                }
                return true;
            }
            case 1: {
                if (n >= 0) {
                    boolean b11 = b;
                    if (n != 0) {
                        return b11;
                    }
                    b11 = b;
                    if (n3 * n2 > 0) {
                        return b11;
                    }
                }
                return true;
            }
        }
    }
    
    private void onPointerUp(final MotionEvent motionEvent) {
        final int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) == this.mScrollPointerId) {
            int n;
            if (actionIndex == 0) {
                n = 1;
            }
            else {
                n = 0;
            }
            this.mScrollPointerId = motionEvent.getPointerId(n);
            final int n2 = (int)(motionEvent.getX(n) + 0.5f);
            this.mLastTouchX = n2;
            this.mInitialTouchX = n2;
            final int n3 = (int)(motionEvent.getY(n) + 0.5f);
            this.mLastTouchY = n3;
            this.mInitialTouchY = n3;
        }
    }
    
    private boolean predictiveItemAnimationsEnabled() {
        return this.mItemAnimator != null && this.mLayout.supportsPredictiveItemAnimations();
    }
    
    private void processAdapterUpdatesAndSetAnimationFlags() {
        if (this.mDataSetHasChangedAfterLayout) {
            this.mAdapterHelper.reset();
            if (this.mDispatchItemsChangedEvent) {
                this.mLayout.onItemsChanged(this);
            }
        }
        if (this.predictiveItemAnimationsEnabled()) {
            this.mAdapterHelper.preProcess();
        }
        else {
            this.mAdapterHelper.consumeUpdatesInOnePass();
        }
        final boolean mItemsAddedOrRemoved = this.mItemsAddedOrRemoved;
        final boolean b = true;
        final boolean b2 = mItemsAddedOrRemoved || this.mItemsChanged;
        this.mState.mRunSimpleAnimations = (this.mFirstLayoutComplete && this.mItemAnimator != null && (this.mDataSetHasChangedAfterLayout || b2 || this.mLayout.mRequestedSimpleAnimations) && (!this.mDataSetHasChangedAfterLayout || this.mAdapter.hasStableIds()));
        this.mState.mRunPredictiveAnimations = (this.mState.mRunSimpleAnimations && b2 && !this.mDataSetHasChangedAfterLayout && this.predictiveItemAnimationsEnabled() && b);
    }
    
    private void pullGlows(final float n, final float n2, final float n3, final float n4) {
        final int n5 = 1;
        int n6 = 0;
        Label_0080: {
            if (n2 < 0.0f) {
                this.ensureLeftGlow();
                EdgeEffectCompat.onPull(this.mLeftGlow, -n2 / this.getWidth(), 1.0f - n3 / this.getHeight());
            }
            else {
                if (n2 <= 0.0f) {
                    n6 = 0;
                    break Label_0080;
                }
                this.ensureRightGlow();
                EdgeEffectCompat.onPull(this.mRightGlow, n2 / this.getWidth(), n3 / this.getHeight());
            }
            n6 = 1;
        }
        if (n4 < 0.0f) {
            this.ensureTopGlow();
            EdgeEffectCompat.onPull(this.mTopGlow, -n4 / this.getHeight(), n / this.getWidth());
            n6 = n5;
        }
        else if (n4 > 0.0f) {
            this.ensureBottomGlow();
            EdgeEffectCompat.onPull(this.mBottomGlow, n4 / this.getHeight(), 1.0f - n / this.getWidth());
            n6 = n5;
        }
        if (n6 != 0 || n2 != 0.0f || n4 != 0.0f) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    private void recoverFocusFromState() {
        if (this.mPreserveFocusAfterLayout && this.mAdapter != null && this.hasFocus() && this.getDescendantFocusability() != 393216 && (this.getDescendantFocusability() != 131072 || !this.isFocused())) {
            if (!this.isFocused()) {
                final View focusedChild = this.getFocusedChild();
                if (RecyclerView.IGNORE_DETACHED_FOCUSED_CHILD && (focusedChild.getParent() == null || !focusedChild.hasFocus())) {
                    if (this.mChildHelper.getChildCount() == 0) {
                        this.requestFocus();
                        return;
                    }
                }
                else if (!this.mChildHelper.isHidden(focusedChild)) {
                    return;
                }
            }
            final long mFocusedItemId = this.mState.mFocusedItemId;
            final View view = null;
            ViewHolder viewHolderForItemId;
            if (mFocusedItemId != -1L && this.mAdapter.hasStableIds()) {
                viewHolderForItemId = this.findViewHolderForItemId(this.mState.mFocusedItemId);
            }
            else {
                viewHolderForItemId = null;
            }
            View view2;
            if (viewHolderForItemId != null && !this.mChildHelper.isHidden(viewHolderForItemId.itemView) && viewHolderForItemId.itemView.hasFocusable()) {
                view2 = viewHolderForItemId.itemView;
            }
            else {
                view2 = view;
                if (this.mChildHelper.getChildCount() > 0) {
                    view2 = this.findNextViewToFocus();
                }
            }
            if (view2 != null) {
                if (this.mState.mFocusedSubChildId != -1L) {
                    final View viewById = view2.findViewById(this.mState.mFocusedSubChildId);
                    if (viewById != null && viewById.isFocusable()) {
                        view2 = viewById;
                    }
                }
                view2.requestFocus();
            }
        }
    }
    
    private void releaseGlows() {
        int finished;
        if (this.mLeftGlow != null) {
            this.mLeftGlow.onRelease();
            finished = (this.mLeftGlow.isFinished() ? 1 : 0);
        }
        else {
            finished = 0;
        }
        int n = finished;
        if (this.mTopGlow != null) {
            this.mTopGlow.onRelease();
            n = (finished | (this.mTopGlow.isFinished() ? 1 : 0));
        }
        int n2 = n;
        if (this.mRightGlow != null) {
            this.mRightGlow.onRelease();
            n2 = (n | (this.mRightGlow.isFinished() ? 1 : 0));
        }
        int n3 = n2;
        if (this.mBottomGlow != null) {
            this.mBottomGlow.onRelease();
            n3 = (n2 | (this.mBottomGlow.isFinished() ? 1 : 0));
        }
        if (n3 != 0) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    private void requestChildOnScreen(@NonNull final View view, @Nullable final View view2) {
        View view3;
        if (view2 != null) {
            view3 = view2;
        }
        else {
            view3 = view;
        }
        this.mTempRect.set(0, 0, view3.getWidth(), view3.getHeight());
        final ViewGroup$LayoutParams layoutParams = view3.getLayoutParams();
        if (layoutParams instanceof LayoutParams) {
            final LayoutParams layoutParams2 = (LayoutParams)layoutParams;
            if (!layoutParams2.mInsetsDirty) {
                final Rect mDecorInsets = layoutParams2.mDecorInsets;
                final Rect mTempRect = this.mTempRect;
                mTempRect.left -= mDecorInsets.left;
                final Rect mTempRect2 = this.mTempRect;
                mTempRect2.right += mDecorInsets.right;
                final Rect mTempRect3 = this.mTempRect;
                mTempRect3.top -= mDecorInsets.top;
                final Rect mTempRect4 = this.mTempRect;
                mTempRect4.bottom += mDecorInsets.bottom;
            }
        }
        if (view2 != null) {
            this.offsetDescendantRectToMyCoords(view2, this.mTempRect);
            this.offsetRectIntoDescendantCoords(view, this.mTempRect);
        }
        this.mLayout.requestChildRectangleOnScreen(this, view, this.mTempRect, this.mFirstLayoutComplete ^ true, view2 == null);
    }
    
    private void resetFocusInfo() {
        this.mState.mFocusedItemId = -1L;
        this.mState.mFocusedItemPosition = -1;
        this.mState.mFocusedSubChildId = -1;
    }
    
    private void resetTouch() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.clear();
        }
        this.stopNestedScroll(0);
        this.releaseGlows();
    }
    
    private void saveFocusInfo() {
        final boolean mPreserveFocusAfterLayout = this.mPreserveFocusAfterLayout;
        final ViewHolder viewHolder = null;
        View focusedChild;
        if (mPreserveFocusAfterLayout && this.hasFocus() && this.mAdapter != null) {
            focusedChild = this.getFocusedChild();
        }
        else {
            focusedChild = null;
        }
        ViewHolder containingViewHolder;
        if (focusedChild == null) {
            containingViewHolder = viewHolder;
        }
        else {
            containingViewHolder = this.findContainingViewHolder(focusedChild);
        }
        if (containingViewHolder == null) {
            this.resetFocusInfo();
        }
        else {
            final State mState = this.mState;
            long itemId;
            if (this.mAdapter.hasStableIds()) {
                itemId = containingViewHolder.getItemId();
            }
            else {
                itemId = -1L;
            }
            mState.mFocusedItemId = itemId;
            final State mState2 = this.mState;
            int mFocusedItemPosition;
            if (this.mDataSetHasChangedAfterLayout) {
                mFocusedItemPosition = -1;
            }
            else if (containingViewHolder.isRemoved()) {
                mFocusedItemPosition = containingViewHolder.mOldPosition;
            }
            else {
                mFocusedItemPosition = containingViewHolder.getAdapterPosition();
            }
            mState2.mFocusedItemPosition = mFocusedItemPosition;
            this.mState.mFocusedSubChildId = this.getDeepestFocusedViewWithId(containingViewHolder.itemView);
        }
    }
    
    private void setAdapterInternal(@Nullable final Adapter mAdapter, final boolean b, final boolean b2) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterAdapterDataObserver(this.mObserver);
            this.mAdapter.onDetachedFromRecyclerView(this);
        }
        if (!b || b2) {
            this.removeAndRecycleViews();
        }
        this.mAdapterHelper.reset();
        final Adapter mAdapter2 = this.mAdapter;
        if ((this.mAdapter = mAdapter) != null) {
            mAdapter.registerAdapterDataObserver(this.mObserver);
            mAdapter.onAttachedToRecyclerView(this);
        }
        if (this.mLayout != null) {
            this.mLayout.onAdapterChanged(mAdapter2, this.mAdapter);
        }
        this.mRecycler.onAdapterChanged(mAdapter2, this.mAdapter, b);
        this.mState.mStructureChanged = true;
    }
    
    private void stopScrollersInternal() {
        this.mViewFlinger.stop();
        if (this.mLayout != null) {
            this.mLayout.stopSmoothScroller();
        }
    }
    
    void absorbGlows(final int n, final int n2) {
        if (n < 0) {
            this.ensureLeftGlow();
            this.mLeftGlow.onAbsorb(-n);
        }
        else if (n > 0) {
            this.ensureRightGlow();
            this.mRightGlow.onAbsorb(n);
        }
        if (n2 < 0) {
            this.ensureTopGlow();
            this.mTopGlow.onAbsorb(-n2);
        }
        else if (n2 > 0) {
            this.ensureBottomGlow();
            this.mBottomGlow.onAbsorb(n2);
        }
        if (n != 0 || n2 != 0) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    public void addFocusables(final ArrayList<View> list, final int n, final int n2) {
        if (this.mLayout == null || !this.mLayout.onAddFocusables(this, list, n, n2)) {
            super.addFocusables((ArrayList)list, n, n2);
        }
    }
    
    public void addItemDecoration(@NonNull final ItemDecoration itemDecoration) {
        this.addItemDecoration(itemDecoration, -1);
    }
    
    public void addItemDecoration(@NonNull final ItemDecoration itemDecoration, final int index) {
        if (this.mLayout != null) {
            this.mLayout.assertNotInLayoutOrScroll("Cannot add item decoration during a scroll  or layout");
        }
        if (this.mItemDecorations.isEmpty()) {
            this.setWillNotDraw(false);
        }
        if (index < 0) {
            this.mItemDecorations.add(itemDecoration);
        }
        else {
            this.mItemDecorations.add(index, itemDecoration);
        }
        this.markItemDecorInsetsDirty();
        this.requestLayout();
    }
    
    public void addOnChildAttachStateChangeListener(@NonNull final OnChildAttachStateChangeListener onChildAttachStateChangeListener) {
        if (this.mOnChildAttachStateListeners == null) {
            this.mOnChildAttachStateListeners = new ArrayList<OnChildAttachStateChangeListener>();
        }
        this.mOnChildAttachStateListeners.add(onChildAttachStateChangeListener);
    }
    
    public void addOnItemTouchListener(@NonNull final OnItemTouchListener e) {
        this.mOnItemTouchListeners.add(e);
    }
    
    public void addOnScrollListener(@NonNull final OnScrollListener onScrollListener) {
        if (this.mScrollListeners == null) {
            this.mScrollListeners = new ArrayList<OnScrollListener>();
        }
        this.mScrollListeners.add(onScrollListener);
    }
    
    void animateAppearance(@NonNull final ViewHolder viewHolder, @Nullable final ItemHolderInfo itemHolderInfo, @NonNull final ItemHolderInfo itemHolderInfo2) {
        viewHolder.setIsRecyclable(false);
        if (this.mItemAnimator.animateAppearance(viewHolder, itemHolderInfo, itemHolderInfo2)) {
            this.postAnimationRunner();
        }
    }
    
    void animateDisappearance(@NonNull final ViewHolder viewHolder, @NonNull final ItemHolderInfo itemHolderInfo, @Nullable final ItemHolderInfo itemHolderInfo2) {
        this.addAnimatingView(viewHolder);
        viewHolder.setIsRecyclable(false);
        if (this.mItemAnimator.animateDisappearance(viewHolder, itemHolderInfo, itemHolderInfo2)) {
            this.postAnimationRunner();
        }
    }
    
    void assertInLayoutOrScroll(final String str) {
        if (this.isComputingLayout()) {
            return;
        }
        if (str == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot call this method unless RecyclerView is computing a layout or scrolling");
            sb.append(this.exceptionLabel());
            throw new IllegalStateException(sb.toString());
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(this.exceptionLabel());
        throw new IllegalStateException(sb2.toString());
    }
    
    void assertNotInLayoutOrScroll(final String s) {
        if (!this.isComputingLayout()) {
            if (this.mDispatchScrollCounter > 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(this.exceptionLabel());
                Log.w("RecyclerView", "Cannot call this method in a scroll callback. Scroll callbacks mightbe run during a measure & layout pass where you cannot change theRecyclerView data. Any method call that might change the structureof the RecyclerView or the adapter contents should be postponed tothe next frame.", (Throwable)new IllegalStateException(sb.toString()));
            }
            return;
        }
        if (s == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Cannot call this method while RecyclerView is computing a layout or scrolling");
            sb2.append(this.exceptionLabel());
            throw new IllegalStateException(sb2.toString());
        }
        throw new IllegalStateException(s);
    }
    
    boolean canReuseUpdatedViewHolder(final ViewHolder viewHolder) {
        return this.mItemAnimator == null || this.mItemAnimator.canReuseUpdatedViewHolder(viewHolder, viewHolder.getUnmodifiedPayloads());
    }
    
    protected boolean checkLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        return viewGroup$LayoutParams instanceof LayoutParams && this.mLayout.checkLayoutParams((LayoutParams)viewGroup$LayoutParams);
    }
    
    void clearOldPositions() {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!childViewHolderInt.shouldIgnore()) {
                childViewHolderInt.clearOldPosition();
            }
        }
        this.mRecycler.clearOldPositions();
    }
    
    public void clearOnChildAttachStateChangeListeners() {
        if (this.mOnChildAttachStateListeners != null) {
            this.mOnChildAttachStateListeners.clear();
        }
    }
    
    public void clearOnScrollListeners() {
        if (this.mScrollListeners != null) {
            this.mScrollListeners.clear();
        }
    }
    
    public int computeHorizontalScrollExtent() {
        final LayoutManager mLayout = this.mLayout;
        int computeHorizontalScrollExtent = 0;
        if (mLayout == null) {
            return 0;
        }
        if (this.mLayout.canScrollHorizontally()) {
            computeHorizontalScrollExtent = this.mLayout.computeHorizontalScrollExtent(this.mState);
        }
        return computeHorizontalScrollExtent;
    }
    
    public int computeHorizontalScrollOffset() {
        final LayoutManager mLayout = this.mLayout;
        int computeHorizontalScrollOffset = 0;
        if (mLayout == null) {
            return 0;
        }
        if (this.mLayout.canScrollHorizontally()) {
            computeHorizontalScrollOffset = this.mLayout.computeHorizontalScrollOffset(this.mState);
        }
        return computeHorizontalScrollOffset;
    }
    
    public int computeHorizontalScrollRange() {
        final LayoutManager mLayout = this.mLayout;
        int computeHorizontalScrollRange = 0;
        if (mLayout == null) {
            return 0;
        }
        if (this.mLayout.canScrollHorizontally()) {
            computeHorizontalScrollRange = this.mLayout.computeHorizontalScrollRange(this.mState);
        }
        return computeHorizontalScrollRange;
    }
    
    public int computeVerticalScrollExtent() {
        final LayoutManager mLayout = this.mLayout;
        int computeVerticalScrollExtent = 0;
        if (mLayout == null) {
            return 0;
        }
        if (this.mLayout.canScrollVertically()) {
            computeVerticalScrollExtent = this.mLayout.computeVerticalScrollExtent(this.mState);
        }
        return computeVerticalScrollExtent;
    }
    
    public int computeVerticalScrollOffset() {
        final LayoutManager mLayout = this.mLayout;
        int computeVerticalScrollOffset = 0;
        if (mLayout == null) {
            return 0;
        }
        if (this.mLayout.canScrollVertically()) {
            computeVerticalScrollOffset = this.mLayout.computeVerticalScrollOffset(this.mState);
        }
        return computeVerticalScrollOffset;
    }
    
    public int computeVerticalScrollRange() {
        final LayoutManager mLayout = this.mLayout;
        int computeVerticalScrollRange = 0;
        if (mLayout == null) {
            return 0;
        }
        if (this.mLayout.canScrollVertically()) {
            computeVerticalScrollRange = this.mLayout.computeVerticalScrollRange(this.mState);
        }
        return computeVerticalScrollRange;
    }
    
    void considerReleasingGlowsOnScroll(final int n, final int n2) {
        int finished;
        if (this.mLeftGlow != null && !this.mLeftGlow.isFinished() && n > 0) {
            this.mLeftGlow.onRelease();
            finished = (this.mLeftGlow.isFinished() ? 1 : 0);
        }
        else {
            finished = 0;
        }
        int n3 = finished;
        if (this.mRightGlow != null) {
            n3 = finished;
            if (!this.mRightGlow.isFinished()) {
                n3 = finished;
                if (n < 0) {
                    this.mRightGlow.onRelease();
                    n3 = (finished | (this.mRightGlow.isFinished() ? 1 : 0));
                }
            }
        }
        int n4 = n3;
        if (this.mTopGlow != null) {
            n4 = n3;
            if (!this.mTopGlow.isFinished()) {
                n4 = n3;
                if (n2 > 0) {
                    this.mTopGlow.onRelease();
                    n4 = (n3 | (this.mTopGlow.isFinished() ? 1 : 0));
                }
            }
        }
        int n5 = n4;
        if (this.mBottomGlow != null) {
            n5 = n4;
            if (!this.mBottomGlow.isFinished()) {
                n5 = n4;
                if (n2 < 0) {
                    this.mBottomGlow.onRelease();
                    n5 = (n4 | (this.mBottomGlow.isFinished() ? 1 : 0));
                }
            }
        }
        if (n5 != 0) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    void consumePendingUpdateOperations() {
        if (!this.mFirstLayoutComplete || this.mDataSetHasChangedAfterLayout) {
            TraceCompat.beginSection("RV FullInvalidate");
            this.dispatchLayout();
            TraceCompat.endSection();
            return;
        }
        if (!this.mAdapterHelper.hasPendingUpdates()) {
            return;
        }
        if (this.mAdapterHelper.hasAnyUpdateTypes(4) && !this.mAdapterHelper.hasAnyUpdateTypes(11)) {
            TraceCompat.beginSection("RV PartialInvalidate");
            this.startInterceptRequestLayout();
            this.onEnterLayoutOrScroll();
            this.mAdapterHelper.preProcess();
            if (!this.mLayoutWasDefered) {
                if (this.hasUpdatedView()) {
                    this.dispatchLayout();
                }
                else {
                    this.mAdapterHelper.consumePostponedUpdates();
                }
            }
            this.stopInterceptRequestLayout(true);
            this.onExitLayoutOrScroll();
            TraceCompat.endSection();
        }
        else if (this.mAdapterHelper.hasPendingUpdates()) {
            TraceCompat.beginSection("RV FullInvalidate");
            this.dispatchLayout();
            TraceCompat.endSection();
        }
    }
    
    void defaultOnMeasure(final int n, final int n2) {
        this.setMeasuredDimension(LayoutManager.chooseSize(n, this.getPaddingLeft() + this.getPaddingRight(), ViewCompat.getMinimumWidth((View)this)), LayoutManager.chooseSize(n2, this.getPaddingTop() + this.getPaddingBottom(), ViewCompat.getMinimumHeight((View)this)));
    }
    
    void dispatchChildAttached(final View view) {
        final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
        this.onChildAttachedToWindow(view);
        if (this.mAdapter != null && childViewHolderInt != null) {
            this.mAdapter.onViewAttachedToWindow(childViewHolderInt);
        }
        if (this.mOnChildAttachStateListeners != null) {
            for (int i = this.mOnChildAttachStateListeners.size() - 1; i >= 0; --i) {
                this.mOnChildAttachStateListeners.get(i).onChildViewAttachedToWindow(view);
            }
        }
    }
    
    void dispatchChildDetached(final View view) {
        final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
        this.onChildDetachedFromWindow(view);
        if (this.mAdapter != null && childViewHolderInt != null) {
            this.mAdapter.onViewDetachedFromWindow(childViewHolderInt);
        }
        if (this.mOnChildAttachStateListeners != null) {
            for (int i = this.mOnChildAttachStateListeners.size() - 1; i >= 0; --i) {
                this.mOnChildAttachStateListeners.get(i).onChildViewDetachedFromWindow(view);
            }
        }
    }
    
    void dispatchLayout() {
        if (this.mAdapter == null) {
            Log.e("RecyclerView", "No adapter attached; skipping layout");
            return;
        }
        if (this.mLayout == null) {
            Log.e("RecyclerView", "No layout manager attached; skipping layout");
            return;
        }
        this.mState.mIsMeasuring = false;
        if (this.mState.mLayoutStep == 1) {
            this.dispatchLayoutStep1();
            this.mLayout.setExactMeasureSpecsFrom(this);
            this.dispatchLayoutStep2();
        }
        else if (!this.mAdapterHelper.hasUpdates() && this.mLayout.getWidth() == this.getWidth() && this.mLayout.getHeight() == this.getHeight()) {
            this.mLayout.setExactMeasureSpecsFrom(this);
        }
        else {
            this.mLayout.setExactMeasureSpecsFrom(this);
            this.dispatchLayoutStep2();
        }
        this.dispatchLayoutStep3();
    }
    
    public boolean dispatchNestedFling(final float n, final float n2, final boolean b) {
        return this.getScrollingChildHelper().dispatchNestedFling(n, n2, b);
    }
    
    public boolean dispatchNestedPreFling(final float n, final float n2) {
        return this.getScrollingChildHelper().dispatchNestedPreFling(n, n2);
    }
    
    public boolean dispatchNestedPreScroll(final int n, final int n2, final int[] array, final int[] array2) {
        return this.getScrollingChildHelper().dispatchNestedPreScroll(n, n2, array, array2);
    }
    
    public boolean dispatchNestedPreScroll(final int n, final int n2, final int[] array, final int[] array2, final int n3) {
        return this.getScrollingChildHelper().dispatchNestedPreScroll(n, n2, array, array2, n3);
    }
    
    public boolean dispatchNestedScroll(final int n, final int n2, final int n3, final int n4, final int[] array) {
        return this.getScrollingChildHelper().dispatchNestedScroll(n, n2, n3, n4, array);
    }
    
    public boolean dispatchNestedScroll(final int n, final int n2, final int n3, final int n4, final int[] array, final int n5) {
        return this.getScrollingChildHelper().dispatchNestedScroll(n, n2, n3, n4, array, n5);
    }
    
    void dispatchOnScrollStateChanged(final int n) {
        if (this.mLayout != null) {
            this.mLayout.onScrollStateChanged(n);
        }
        this.onScrollStateChanged(n);
        if (this.mScrollListener != null) {
            this.mScrollListener.onScrollStateChanged(this, n);
        }
        if (this.mScrollListeners != null) {
            for (int i = this.mScrollListeners.size() - 1; i >= 0; --i) {
                this.mScrollListeners.get(i).onScrollStateChanged(this, n);
            }
        }
    }
    
    void dispatchOnScrolled(final int n, final int n2) {
        ++this.mDispatchScrollCounter;
        final int scrollX = this.getScrollX();
        final int scrollY = this.getScrollY();
        this.onScrollChanged(scrollX, scrollY, scrollX, scrollY);
        this.onScrolled(n, n2);
        if (this.mScrollListener != null) {
            this.mScrollListener.onScrolled(this, n, n2);
        }
        if (this.mScrollListeners != null) {
            for (int i = this.mScrollListeners.size() - 1; i >= 0; --i) {
                this.mScrollListeners.get(i).onScrolled(this, n, n2);
            }
        }
        --this.mDispatchScrollCounter;
    }
    
    void dispatchPendingImportantForAccessibilityChanges() {
        for (int i = this.mPendingAccessibilityImportanceChange.size() - 1; i >= 0; --i) {
            final ViewHolder viewHolder = this.mPendingAccessibilityImportanceChange.get(i);
            if (viewHolder.itemView.getParent() == this) {
                if (!viewHolder.shouldIgnore()) {
                    final int mPendingAccessibilityState = viewHolder.mPendingAccessibilityState;
                    if (mPendingAccessibilityState != -1) {
                        ViewCompat.setImportantForAccessibility(viewHolder.itemView, mPendingAccessibilityState);
                        viewHolder.mPendingAccessibilityState = -1;
                    }
                }
            }
        }
        this.mPendingAccessibilityImportanceChange.clear();
    }
    
    protected void dispatchRestoreInstanceState(final SparseArray<Parcelable> sparseArray) {
        this.dispatchThawSelfOnly((SparseArray)sparseArray);
    }
    
    protected void dispatchSaveInstanceState(final SparseArray<Parcelable> sparseArray) {
        this.dispatchFreezeSelfOnly((SparseArray)sparseArray);
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        final int size = this.mItemDecorations.size();
        final int n = 0;
        for (int i = 0; i < size; ++i) {
            this.mItemDecorations.get(i).onDrawOver(canvas, this, this.mState);
        }
        int n2;
        if (this.mLeftGlow != null && !this.mLeftGlow.isFinished()) {
            final int save = canvas.save();
            int paddingBottom;
            if (this.mClipToPadding) {
                paddingBottom = this.getPaddingBottom();
            }
            else {
                paddingBottom = 0;
            }
            canvas.rotate(270.0f);
            canvas.translate((float)(-this.getHeight() + paddingBottom), 0.0f);
            n2 = ((this.mLeftGlow != null && this.mLeftGlow.draw(canvas)) ? 1 : 0);
            canvas.restoreToCount(save);
        }
        else {
            n2 = 0;
        }
        int n3 = n2;
        if (this.mTopGlow != null) {
            n3 = n2;
            if (!this.mTopGlow.isFinished()) {
                final int save2 = canvas.save();
                if (this.mClipToPadding) {
                    canvas.translate((float)this.getPaddingLeft(), (float)this.getPaddingTop());
                }
                n3 = (n2 | ((this.mTopGlow != null && this.mTopGlow.draw(canvas)) ? 1 : 0));
                canvas.restoreToCount(save2);
            }
        }
        int n4 = n3;
        if (this.mRightGlow != null) {
            n4 = n3;
            if (!this.mRightGlow.isFinished()) {
                final int save3 = canvas.save();
                final int width = this.getWidth();
                int paddingTop;
                if (this.mClipToPadding) {
                    paddingTop = this.getPaddingTop();
                }
                else {
                    paddingTop = 0;
                }
                canvas.rotate(90.0f);
                canvas.translate((float)(-paddingTop), (float)(-width));
                boolean b;
                if (this.mRightGlow != null && this.mRightGlow.draw(canvas)) {
                    b = true;
                }
                else {
                    b = false;
                }
                n4 = (n3 | (b ? 1 : 0));
                canvas.restoreToCount(save3);
            }
        }
        int n6;
        if (this.mBottomGlow != null && !this.mBottomGlow.isFinished()) {
            final int save4 = canvas.save();
            canvas.rotate(180.0f);
            if (this.mClipToPadding) {
                canvas.translate((float)(-this.getWidth() + this.getPaddingRight()), (float)(-this.getHeight() + this.getPaddingBottom()));
            }
            else {
                canvas.translate((float)(-this.getWidth()), (float)(-this.getHeight()));
            }
            int n5 = n;
            if (this.mBottomGlow != null) {
                n5 = n;
                if (this.mBottomGlow.draw(canvas)) {
                    n5 = 1;
                }
            }
            n6 = (n5 | n4);
            canvas.restoreToCount(save4);
        }
        else {
            n6 = n4;
        }
        int n7 = n6;
        if (n6 == 0) {
            n7 = n6;
            if (this.mItemAnimator != null) {
                n7 = n6;
                if (this.mItemDecorations.size() > 0) {
                    n7 = n6;
                    if (this.mItemAnimator.isRunning()) {
                        n7 = 1;
                    }
                }
            }
        }
        if (n7 != 0) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }
    
    public boolean drawChild(final Canvas canvas, final View view, final long n) {
        return super.drawChild(canvas, view, n);
    }
    
    void ensureBottomGlow() {
        if (this.mBottomGlow != null) {
            return;
        }
        this.mBottomGlow = this.mEdgeEffectFactory.createEdgeEffect(this, 3);
        if (this.mClipToPadding) {
            this.mBottomGlow.setSize(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight(), this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom());
        }
        else {
            this.mBottomGlow.setSize(this.getMeasuredWidth(), this.getMeasuredHeight());
        }
    }
    
    void ensureLeftGlow() {
        if (this.mLeftGlow != null) {
            return;
        }
        this.mLeftGlow = this.mEdgeEffectFactory.createEdgeEffect(this, 0);
        if (this.mClipToPadding) {
            this.mLeftGlow.setSize(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom(), this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight());
        }
        else {
            this.mLeftGlow.setSize(this.getMeasuredHeight(), this.getMeasuredWidth());
        }
    }
    
    void ensureRightGlow() {
        if (this.mRightGlow != null) {
            return;
        }
        this.mRightGlow = this.mEdgeEffectFactory.createEdgeEffect(this, 2);
        if (this.mClipToPadding) {
            this.mRightGlow.setSize(this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom(), this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight());
        }
        else {
            this.mRightGlow.setSize(this.getMeasuredHeight(), this.getMeasuredWidth());
        }
    }
    
    void ensureTopGlow() {
        if (this.mTopGlow != null) {
            return;
        }
        this.mTopGlow = this.mEdgeEffectFactory.createEdgeEffect(this, 1);
        if (this.mClipToPadding) {
            this.mTopGlow.setSize(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight(), this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom());
        }
        else {
            this.mTopGlow.setSize(this.getMeasuredWidth(), this.getMeasuredHeight());
        }
    }
    
    String exceptionLabel() {
        final StringBuilder sb = new StringBuilder();
        sb.append(" ");
        sb.append(super.toString());
        sb.append(", adapter:");
        sb.append(this.mAdapter);
        sb.append(", layout:");
        sb.append(this.mLayout);
        sb.append(", context:");
        sb.append(this.getContext());
        return sb.toString();
    }
    
    final void fillRemainingScrollValues(final State state) {
        if (this.getScrollState() == 2) {
            final OverScroller mScroller = this.mViewFlinger.mScroller;
            state.mRemainingScrollHorizontal = mScroller.getFinalX() - mScroller.getCurrX();
            state.mRemainingScrollVertical = mScroller.getFinalY() - mScroller.getCurrY();
        }
        else {
            state.mRemainingScrollHorizontal = 0;
            state.mRemainingScrollVertical = 0;
        }
    }
    
    @Nullable
    public View findChildViewUnder(final float n, final float n2) {
        for (int i = this.mChildHelper.getChildCount() - 1; i >= 0; --i) {
            final View child = this.mChildHelper.getChildAt(i);
            final float translationX = child.getTranslationX();
            final float translationY = child.getTranslationY();
            if (n >= child.getLeft() + translationX && n <= child.getRight() + translationX && n2 >= child.getTop() + translationY && n2 <= child.getBottom() + translationY) {
                return child;
            }
        }
        return null;
    }
    
    @Nullable
    public View findContainingItemView(@NonNull View view) {
        ViewParent viewParent;
        for (viewParent = view.getParent(); viewParent != null && viewParent != this && viewParent instanceof View; viewParent = view.getParent()) {
            view = (View)viewParent;
        }
        if (viewParent != this) {
            view = null;
        }
        return view;
    }
    
    @Nullable
    public ViewHolder findContainingViewHolder(@NonNull View containingItemView) {
        containingItemView = this.findContainingItemView(containingItemView);
        ViewHolder childViewHolder;
        if (containingItemView == null) {
            childViewHolder = null;
        }
        else {
            childViewHolder = this.getChildViewHolder(containingItemView);
        }
        return childViewHolder;
    }
    
    @Nullable
    public ViewHolder findViewHolderForAdapterPosition(final int n) {
        final boolean mDataSetHasChangedAfterLayout = this.mDataSetHasChangedAfterLayout;
        ViewHolder viewHolder = null;
        if (mDataSetHasChangedAfterLayout) {
            return null;
        }
        ViewHolder viewHolder2;
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i, viewHolder = viewHolder2) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            viewHolder2 = viewHolder;
            if (childViewHolderInt != null) {
                viewHolder2 = viewHolder;
                if (!childViewHolderInt.isRemoved()) {
                    viewHolder2 = viewHolder;
                    if (this.getAdapterPositionFor(childViewHolderInt) == n) {
                        if (!this.mChildHelper.isHidden(childViewHolderInt.itemView)) {
                            return childViewHolderInt;
                        }
                        viewHolder2 = childViewHolderInt;
                    }
                }
            }
        }
        return viewHolder;
    }
    
    public ViewHolder findViewHolderForItemId(final long n) {
        final Adapter mAdapter = this.mAdapter;
        ViewHolder viewHolder = null;
        if (mAdapter != null && this.mAdapter.hasStableIds()) {
            ViewHolder viewHolder2;
            for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i, viewHolder = viewHolder2) {
                final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
                viewHolder2 = viewHolder;
                if (childViewHolderInt != null) {
                    viewHolder2 = viewHolder;
                    if (!childViewHolderInt.isRemoved()) {
                        viewHolder2 = viewHolder;
                        if (childViewHolderInt.getItemId() == n) {
                            if (!this.mChildHelper.isHidden(childViewHolderInt.itemView)) {
                                return childViewHolderInt;
                            }
                            viewHolder2 = childViewHolderInt;
                        }
                    }
                }
            }
            return viewHolder;
        }
        return null;
    }
    
    @Nullable
    public ViewHolder findViewHolderForLayoutPosition(final int n) {
        return this.findViewHolderForPosition(n, false);
    }
    
    @Deprecated
    @Nullable
    public ViewHolder findViewHolderForPosition(final int n) {
        return this.findViewHolderForPosition(n, false);
    }
    
    @Nullable
    ViewHolder findViewHolderForPosition(final int n, final boolean b) {
        final int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        ViewHolder viewHolder = null;
        ViewHolder viewHolder2;
        for (int i = 0; i < unfilteredChildCount; ++i, viewHolder = viewHolder2) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            viewHolder2 = viewHolder;
            if (childViewHolderInt != null) {
                viewHolder2 = viewHolder;
                if (!childViewHolderInt.isRemoved()) {
                    if (b) {
                        if (childViewHolderInt.mPosition != n) {
                            viewHolder2 = viewHolder;
                            continue;
                        }
                    }
                    else if (childViewHolderInt.getLayoutPosition() != n) {
                        viewHolder2 = viewHolder;
                        continue;
                    }
                    if (!this.mChildHelper.isHidden(childViewHolderInt.itemView)) {
                        return childViewHolderInt;
                    }
                    viewHolder2 = childViewHolderInt;
                }
            }
        }
        return viewHolder;
    }
    
    public boolean fling(int max, int max2) {
        final LayoutManager mLayout = this.mLayout;
        final int n = 0;
        if (mLayout == null) {
            Log.e("RecyclerView", "Cannot fling without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return false;
        }
        if (this.mLayoutFrozen) {
            return false;
        }
        final boolean canScrollHorizontally = this.mLayout.canScrollHorizontally();
        final boolean canScrollVertically = this.mLayout.canScrollVertically();
        int a = 0;
        Label_0074: {
            if (canScrollHorizontally) {
                a = max;
                if (Math.abs(max) >= this.mMinFlingVelocity) {
                    break Label_0074;
                }
            }
            a = 0;
        }
        int a2 = 0;
        Label_0096: {
            if (canScrollVertically) {
                a2 = max2;
                if (Math.abs(max2) >= this.mMinFlingVelocity) {
                    break Label_0096;
                }
            }
            a2 = 0;
        }
        if (a == 0 && a2 == 0) {
            return false;
        }
        final float n2 = (float)a;
        final float n3 = (float)a2;
        if (!this.dispatchNestedPreFling(n2, n3)) {
            final boolean b = canScrollHorizontally || canScrollVertically;
            this.dispatchNestedFling(n2, n3, b);
            if (this.mOnFlingListener != null && this.mOnFlingListener.onFling(a, a2)) {
                return true;
            }
            if (b) {
                max = n;
                if (canScrollHorizontally) {
                    max = 1;
                }
                max2 = max;
                if (canScrollVertically) {
                    max2 = (max | 0x2);
                }
                this.startNestedScroll(max2, 1);
                max = Math.max(-this.mMaxFlingVelocity, Math.min(a, this.mMaxFlingVelocity));
                max2 = Math.max(-this.mMaxFlingVelocity, Math.min(a2, this.mMaxFlingVelocity));
                this.mViewFlinger.fling(max, max2);
                return true;
            }
        }
        return false;
    }
    
    public View focusSearch(final View view, int n) {
        final View onInterceptFocusSearch = this.mLayout.onInterceptFocusSearch(view, n);
        if (onInterceptFocusSearch != null) {
            return onInterceptFocusSearch;
        }
        final boolean b = this.mAdapter != null && this.mLayout != null && !this.isComputingLayout() && !this.mLayoutFrozen;
        final FocusFinder instance = FocusFinder.getInstance();
        View view2;
        if (b && (n == 2 || n == 1)) {
            int n3;
            if (this.mLayout.canScrollVertically()) {
                int n2;
                if (n == 2) {
                    n2 = 130;
                }
                else {
                    n2 = 33;
                }
                final boolean b2 = (n3 = ((instance.findNextFocus((ViewGroup)this, view, n2) == null) ? 1 : 0)) != 0;
                if (RecyclerView.FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
                    n = n2;
                    n3 = (b2 ? 1 : 0);
                }
            }
            else {
                n3 = 0;
            }
            int n4 = n3;
            int n5 = n;
            if (n3 == 0) {
                n4 = n3;
                n5 = n;
                if (this.mLayout.canScrollHorizontally()) {
                    int n6;
                    if (this.mLayout.getLayoutDirection() == 1 ^ n == 2) {
                        n6 = 66;
                    }
                    else {
                        n6 = 17;
                    }
                    final boolean b3 = (n4 = ((instance.findNextFocus((ViewGroup)this, view, n6) == null) ? 1 : 0)) != 0;
                    n5 = n;
                    if (RecyclerView.FORCE_ABS_FOCUS_SEARCH_DIRECTION) {
                        n5 = n6;
                        n4 = (b3 ? 1 : 0);
                    }
                }
            }
            if (n4 != 0) {
                this.consumePendingUpdateOperations();
                if (this.findContainingItemView(view) == null) {
                    return null;
                }
                this.startInterceptRequestLayout();
                this.mLayout.onFocusSearchFailed(view, n5, this.mRecycler, this.mState);
                this.stopInterceptRequestLayout(false);
            }
            view2 = instance.findNextFocus((ViewGroup)this, view, n5);
            n = n5;
        }
        else {
            view2 = instance.findNextFocus((ViewGroup)this, view, n);
            if (view2 == null && b) {
                this.consumePendingUpdateOperations();
                if (this.findContainingItemView(view) == null) {
                    return null;
                }
                this.startInterceptRequestLayout();
                view2 = this.mLayout.onFocusSearchFailed(view, n, this.mRecycler, this.mState);
                this.stopInterceptRequestLayout(false);
            }
        }
        if (view2 == null || view2.hasFocusable()) {
            if (!this.isPreferredNextFocus(view, view2, n)) {
                view2 = super.focusSearch(view, n);
            }
            return view2;
        }
        if (this.getFocusedChild() == null) {
            return super.focusSearch(view, n);
        }
        this.requestChildOnScreen(view2, null);
        return view;
    }
    
    protected ViewGroup$LayoutParams generateDefaultLayoutParams() {
        if (this.mLayout == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("RecyclerView has no LayoutManager");
            sb.append(this.exceptionLabel());
            throw new IllegalStateException(sb.toString());
        }
        return (ViewGroup$LayoutParams)this.mLayout.generateDefaultLayoutParams();
    }
    
    public ViewGroup$LayoutParams generateLayoutParams(final AttributeSet set) {
        if (this.mLayout == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("RecyclerView has no LayoutManager");
            sb.append(this.exceptionLabel());
            throw new IllegalStateException(sb.toString());
        }
        return (ViewGroup$LayoutParams)this.mLayout.generateLayoutParams(this.getContext(), set);
    }
    
    protected ViewGroup$LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
        if (this.mLayout == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("RecyclerView has no LayoutManager");
            sb.append(this.exceptionLabel());
            throw new IllegalStateException(sb.toString());
        }
        return (ViewGroup$LayoutParams)this.mLayout.generateLayoutParams(viewGroup$LayoutParams);
    }
    
    @Nullable
    public Adapter getAdapter() {
        return this.mAdapter;
    }
    
    int getAdapterPositionFor(final ViewHolder viewHolder) {
        if (!viewHolder.hasAnyOfTheFlags(524) && viewHolder.isBound()) {
            return this.mAdapterHelper.applyPendingUpdatesToPosition(viewHolder.mPosition);
        }
        return -1;
    }
    
    public int getBaseline() {
        if (this.mLayout != null) {
            return this.mLayout.getBaseline();
        }
        return super.getBaseline();
    }
    
    long getChangedHolderKey(final ViewHolder viewHolder) {
        long itemId;
        if (this.mAdapter.hasStableIds()) {
            itemId = viewHolder.getItemId();
        }
        else {
            itemId = viewHolder.mPosition;
        }
        return itemId;
    }
    
    public int getChildAdapterPosition(@NonNull final View view) {
        final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
        int adapterPosition;
        if (childViewHolderInt != null) {
            adapterPosition = childViewHolderInt.getAdapterPosition();
        }
        else {
            adapterPosition = -1;
        }
        return adapterPosition;
    }
    
    protected int getChildDrawingOrder(final int n, final int n2) {
        if (this.mChildDrawingOrderCallback == null) {
            return super.getChildDrawingOrder(n, n2);
        }
        return this.mChildDrawingOrderCallback.onGetChildDrawingOrder(n, n2);
    }
    
    public long getChildItemId(@NonNull final View view) {
        final Adapter mAdapter = this.mAdapter;
        long itemId = -1L;
        if (mAdapter != null && this.mAdapter.hasStableIds()) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
            if (childViewHolderInt != null) {
                itemId = childViewHolderInt.getItemId();
            }
            return itemId;
        }
        return -1L;
    }
    
    public int getChildLayoutPosition(@NonNull final View view) {
        final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
        int layoutPosition;
        if (childViewHolderInt != null) {
            layoutPosition = childViewHolderInt.getLayoutPosition();
        }
        else {
            layoutPosition = -1;
        }
        return layoutPosition;
    }
    
    @Deprecated
    public int getChildPosition(@NonNull final View view) {
        return this.getChildAdapterPosition(view);
    }
    
    public ViewHolder getChildViewHolder(@NonNull final View obj) {
        final ViewParent parent = obj.getParent();
        if (parent != null && parent != this) {
            final StringBuilder sb = new StringBuilder();
            sb.append("View ");
            sb.append(obj);
            sb.append(" is not a direct child of ");
            sb.append(this);
            throw new IllegalArgumentException(sb.toString());
        }
        return getChildViewHolderInt(obj);
    }
    
    public boolean getClipToPadding() {
        return this.mClipToPadding;
    }
    
    @Nullable
    public RecyclerViewAccessibilityDelegate getCompatAccessibilityDelegate() {
        return this.mAccessibilityDelegate;
    }
    
    public void getDecoratedBoundsWithMargins(@NonNull final View view, @NonNull final Rect rect) {
        getDecoratedBoundsWithMarginsInt(view, rect);
    }
    
    @NonNull
    public EdgeEffectFactory getEdgeEffectFactory() {
        return this.mEdgeEffectFactory;
    }
    
    @Nullable
    public ItemAnimator getItemAnimator() {
        return this.mItemAnimator;
    }
    
    Rect getItemDecorInsetsForChild(final View view) {
        final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (!layoutParams.mInsetsDirty) {
            return layoutParams.mDecorInsets;
        }
        if (this.mState.isPreLayout() && (layoutParams.isItemChanged() || layoutParams.isViewInvalid())) {
            return layoutParams.mDecorInsets;
        }
        final Rect mDecorInsets = layoutParams.mDecorInsets;
        mDecorInsets.set(0, 0, 0, 0);
        for (int size = this.mItemDecorations.size(), i = 0; i < size; ++i) {
            this.mTempRect.set(0, 0, 0, 0);
            this.mItemDecorations.get(i).getItemOffsets(this.mTempRect, view, this, this.mState);
            mDecorInsets.left += this.mTempRect.left;
            mDecorInsets.top += this.mTempRect.top;
            mDecorInsets.right += this.mTempRect.right;
            mDecorInsets.bottom += this.mTempRect.bottom;
        }
        layoutParams.mInsetsDirty = false;
        return mDecorInsets;
    }
    
    @NonNull
    public ItemDecoration getItemDecorationAt(final int n) {
        final int itemDecorationCount = this.getItemDecorationCount();
        if (n >= 0 && n < itemDecorationCount) {
            return this.mItemDecorations.get(n);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(n);
        sb.append(" is an invalid index for size ");
        sb.append(itemDecorationCount);
        throw new IndexOutOfBoundsException(sb.toString());
    }
    
    public int getItemDecorationCount() {
        return this.mItemDecorations.size();
    }
    
    @Nullable
    public LayoutManager getLayoutManager() {
        return this.mLayout;
    }
    
    public int getMaxFlingVelocity() {
        return this.mMaxFlingVelocity;
    }
    
    public int getMinFlingVelocity() {
        return this.mMinFlingVelocity;
    }
    
    long getNanoTime() {
        if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
            return System.nanoTime();
        }
        return 0L;
    }
    
    @Nullable
    public OnFlingListener getOnFlingListener() {
        return this.mOnFlingListener;
    }
    
    public boolean getPreserveFocusAfterLayout() {
        return this.mPreserveFocusAfterLayout;
    }
    
    @NonNull
    public RecycledViewPool getRecycledViewPool() {
        return this.mRecycler.getRecycledViewPool();
    }
    
    public int getScrollState() {
        return this.mScrollState;
    }
    
    public boolean hasFixedSize() {
        return this.mHasFixedSize;
    }
    
    public boolean hasNestedScrollingParent() {
        return this.getScrollingChildHelper().hasNestedScrollingParent();
    }
    
    public boolean hasNestedScrollingParent(final int n) {
        return this.getScrollingChildHelper().hasNestedScrollingParent(n);
    }
    
    public boolean hasPendingAdapterUpdates() {
        return !this.mFirstLayoutComplete || this.mDataSetHasChangedAfterLayout || this.mAdapterHelper.hasPendingUpdates();
    }
    
    void initAdapterManager() {
        this.mAdapterHelper = new AdapterHelper((AdapterHelper.Callback)new AdapterHelper.Callback(this) {
            final RecyclerView this$0;
            
            void dispatchUpdate(final UpdateOp updateOp) {
                final int cmd = updateOp.cmd;
                if (cmd != 4) {
                    if (cmd != 8) {
                        switch (cmd) {
                            case 2: {
                                this.this$0.mLayout.onItemsRemoved(this.this$0, updateOp.positionStart, updateOp.itemCount);
                                break;
                            }
                            case 1: {
                                this.this$0.mLayout.onItemsAdded(this.this$0, updateOp.positionStart, updateOp.itemCount);
                                break;
                            }
                        }
                    }
                    else {
                        this.this$0.mLayout.onItemsMoved(this.this$0, updateOp.positionStart, updateOp.itemCount, 1);
                    }
                }
                else {
                    this.this$0.mLayout.onItemsUpdated(this.this$0, updateOp.positionStart, updateOp.itemCount, updateOp.payload);
                }
            }
            
            @Override
            public ViewHolder findViewHolder(final int n) {
                final ViewHolder viewHolderForPosition = this.this$0.findViewHolderForPosition(n, true);
                if (viewHolderForPosition == null) {
                    return null;
                }
                if (this.this$0.mChildHelper.isHidden(viewHolderForPosition.itemView)) {
                    return null;
                }
                return viewHolderForPosition;
            }
            
            @Override
            public void markViewHoldersUpdated(final int n, final int n2, final Object o) {
                this.this$0.viewRangeUpdate(n, n2, o);
                this.this$0.mItemsChanged = true;
            }
            
            @Override
            public void offsetPositionsForAdd(final int n, final int n2) {
                this.this$0.offsetPositionRecordsForInsert(n, n2);
                this.this$0.mItemsAddedOrRemoved = true;
            }
            
            @Override
            public void offsetPositionsForMove(final int n, final int n2) {
                this.this$0.offsetPositionRecordsForMove(n, n2);
                this.this$0.mItemsAddedOrRemoved = true;
            }
            
            @Override
            public void offsetPositionsForRemovingInvisible(final int n, final int n2) {
                this.this$0.offsetPositionRecordsForRemove(n, n2, true);
                this.this$0.mItemsAddedOrRemoved = true;
                final State mState = this.this$0.mState;
                mState.mDeletedInvisibleItemCountSincePreviousLayout += n2;
            }
            
            @Override
            public void offsetPositionsForRemovingLaidOutOrNewView(final int n, final int n2) {
                this.this$0.offsetPositionRecordsForRemove(n, n2, false);
                this.this$0.mItemsAddedOrRemoved = true;
            }
            
            @Override
            public void onDispatchFirstPass(final UpdateOp updateOp) {
                this.dispatchUpdate(updateOp);
            }
            
            @Override
            public void onDispatchSecondPass(final UpdateOp updateOp) {
                this.dispatchUpdate(updateOp);
            }
        });
    }
    
    @VisibleForTesting
    void initFastScroller(final StateListDrawable stateListDrawable, final Drawable drawable, final StateListDrawable stateListDrawable2, final Drawable drawable2) {
        if (stateListDrawable != null && drawable != null && stateListDrawable2 != null && drawable2 != null) {
            final Resources resources = this.getContext().getResources();
            new FastScroller(this, stateListDrawable, drawable, stateListDrawable2, drawable2, resources.getDimensionPixelSize(R.dimen.fastscroll_default_thickness), resources.getDimensionPixelSize(R.dimen.fastscroll_minimum_range), resources.getDimensionPixelOffset(R.dimen.fastscroll_margin));
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Trying to set fast scroller without both required drawables.");
        sb.append(this.exceptionLabel());
        throw new IllegalArgumentException(sb.toString());
    }
    
    void invalidateGlows() {
        this.mBottomGlow = null;
        this.mTopGlow = null;
        this.mRightGlow = null;
        this.mLeftGlow = null;
    }
    
    public void invalidateItemDecorations() {
        if (this.mItemDecorations.size() == 0) {
            return;
        }
        if (this.mLayout != null) {
            this.mLayout.assertNotInLayoutOrScroll("Cannot invalidate item decorations during a scroll or layout");
        }
        this.markItemDecorInsetsDirty();
        this.requestLayout();
    }
    
    boolean isAccessibilityEnabled() {
        return this.mAccessibilityManager != null && this.mAccessibilityManager.isEnabled();
    }
    
    public boolean isAnimating() {
        return this.mItemAnimator != null && this.mItemAnimator.isRunning();
    }
    
    public boolean isAttachedToWindow() {
        return this.mIsAttached;
    }
    
    public boolean isComputingLayout() {
        return this.mLayoutOrScrollCounter > 0;
    }
    
    public boolean isLayoutFrozen() {
        return this.mLayoutFrozen;
    }
    
    public boolean isNestedScrollingEnabled() {
        return this.getScrollingChildHelper().isNestedScrollingEnabled();
    }
    
    void jumpToPositionForSmoothScroller(final int n) {
        if (this.mLayout == null) {
            return;
        }
        this.mLayout.scrollToPosition(n);
        this.awakenScrollBars();
    }
    
    void markItemDecorInsetsDirty() {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            ((LayoutParams)this.mChildHelper.getUnfilteredChildAt(i).getLayoutParams()).mInsetsDirty = true;
        }
        this.mRecycler.markItemDecorInsetsDirty();
    }
    
    void markKnownViewsInvalid() {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore()) {
                childViewHolderInt.addFlags(6);
            }
        }
        this.markItemDecorInsetsDirty();
        this.mRecycler.markKnownViewsInvalid();
    }
    
    public void offsetChildrenHorizontal(@Px final int n) {
        for (int childCount = this.mChildHelper.getChildCount(), i = 0; i < childCount; ++i) {
            this.mChildHelper.getChildAt(i).offsetLeftAndRight(n);
        }
    }
    
    public void offsetChildrenVertical(@Px final int n) {
        for (int childCount = this.mChildHelper.getChildCount(), i = 0; i < childCount; ++i) {
            this.mChildHelper.getChildAt(i).offsetTopAndBottom(n);
        }
    }
    
    void offsetPositionRecordsForInsert(final int n, final int n2) {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore() && childViewHolderInt.mPosition >= n) {
                childViewHolderInt.offsetPosition(n2, false);
                this.mState.mStructureChanged = true;
            }
        }
        this.mRecycler.offsetPositionRecordsForInsert(n, n2);
        this.requestLayout();
    }
    
    void offsetPositionRecordsForMove(final int n, final int n2) {
        final int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount();
        int n3;
        int n4;
        int n5;
        if (n < n2) {
            n3 = n2;
            n4 = -1;
            n5 = n;
        }
        else {
            n3 = n;
            n5 = n2;
            n4 = 1;
        }
        for (int i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && childViewHolderInt.mPosition >= n5) {
                if (childViewHolderInt.mPosition <= n3) {
                    if (childViewHolderInt.mPosition == n) {
                        childViewHolderInt.offsetPosition(n2 - n, false);
                    }
                    else {
                        childViewHolderInt.offsetPosition(n4, false);
                    }
                    this.mState.mStructureChanged = true;
                }
            }
        }
        this.mRecycler.offsetPositionRecordsForMove(n, n2);
        this.requestLayout();
    }
    
    void offsetPositionRecordsForRemove(final int n, final int n2, final boolean b) {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (childViewHolderInt != null && !childViewHolderInt.shouldIgnore()) {
                if (childViewHolderInt.mPosition >= n + n2) {
                    childViewHolderInt.offsetPosition(-n2, b);
                    this.mState.mStructureChanged = true;
                }
                else if (childViewHolderInt.mPosition >= n) {
                    childViewHolderInt.flagRemovedAndOffsetPosition(n - 1, -n2, b);
                    this.mState.mStructureChanged = true;
                }
            }
        }
        this.mRecycler.offsetPositionRecordsForRemove(n, n2, b);
        this.requestLayout();
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mLayoutOrScrollCounter = 0;
        boolean mFirstLayoutComplete = true;
        this.mIsAttached = true;
        if (!this.mFirstLayoutComplete || this.isLayoutRequested()) {
            mFirstLayoutComplete = false;
        }
        this.mFirstLayoutComplete = mFirstLayoutComplete;
        if (this.mLayout != null) {
            this.mLayout.dispatchAttachedToWindow(this);
        }
        this.mPostedAnimatorRunner = false;
        if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
            this.mGapWorker = GapWorker.sGapWorker.get();
            if (this.mGapWorker == null) {
                this.mGapWorker = new GapWorker();
                final Display display = ViewCompat.getDisplay((View)this);
                float refreshRate = 0.0f;
                Label_0133: {
                    if (!this.isInEditMode() && display != null) {
                        refreshRate = display.getRefreshRate();
                        if (refreshRate >= 30.0f) {
                            break Label_0133;
                        }
                    }
                    refreshRate = 60.0f;
                }
                this.mGapWorker.mFrameIntervalNs = (long)(1.0E9f / refreshRate);
                GapWorker.sGapWorker.set(this.mGapWorker);
            }
            this.mGapWorker.add(this);
        }
    }
    
    public void onChildAttachedToWindow(@NonNull final View view) {
    }
    
    public void onChildDetachedFromWindow(@NonNull final View view) {
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mItemAnimator != null) {
            this.mItemAnimator.endAnimations();
        }
        this.stopScroll();
        this.mIsAttached = false;
        if (this.mLayout != null) {
            this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
        }
        this.mPendingAccessibilityImportanceChange.clear();
        this.removeCallbacks(this.mItemAnimatorRunner);
        this.mViewInfoStore.onDetach();
        if (RecyclerView.ALLOW_THREAD_GAP_WORK && this.mGapWorker != null) {
            this.mGapWorker.remove(this);
            this.mGapWorker = null;
        }
    }
    
    public void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        for (int size = this.mItemDecorations.size(), i = 0; i < size; ++i) {
            this.mItemDecorations.get(i).onDraw(canvas, this, this.mState);
        }
    }
    
    void onEnterLayoutOrScroll() {
        ++this.mLayoutOrScrollCounter;
    }
    
    void onExitLayoutOrScroll() {
        this.onExitLayoutOrScroll(true);
    }
    
    void onExitLayoutOrScroll(final boolean b) {
        --this.mLayoutOrScrollCounter;
        if (this.mLayoutOrScrollCounter < 1) {
            this.mLayoutOrScrollCounter = 0;
            if (b) {
                this.dispatchContentChangedIfNecessary();
                this.dispatchPendingImportantForAccessibilityChanges();
            }
        }
    }
    
    public boolean onGenericMotionEvent(final MotionEvent motionEvent) {
        if (this.mLayout == null) {
            return false;
        }
        if (this.mLayoutFrozen) {
            return false;
        }
        if (motionEvent.getAction() == 8) {
            float n = 0.0f;
            float n3 = 0.0f;
            Label_0145: {
                float n2 = 0.0f;
                Label_0081: {
                    if ((motionEvent.getSource() & 0x2) == 0x0) {
                        if ((motionEvent.getSource() & 0x400000) != 0x0) {
                            n = motionEvent.getAxisValue(26);
                            if (this.mLayout.canScrollVertically()) {
                                n2 = -n;
                                break Label_0081;
                            }
                            if (this.mLayout.canScrollHorizontally()) {
                                n3 = 0.0f;
                                break Label_0145;
                            }
                        }
                        n3 = 0.0f;
                        n = 0.0f;
                        break Label_0145;
                    }
                    if (this.mLayout.canScrollVertically()) {
                        n3 = -motionEvent.getAxisValue(9);
                    }
                    else {
                        n3 = 0.0f;
                    }
                    n2 = n3;
                    if (this.mLayout.canScrollHorizontally()) {
                        n = motionEvent.getAxisValue(10);
                        break Label_0145;
                    }
                }
                final float n4 = 0.0f;
                n3 = n2;
                n = n4;
            }
            if (n3 != 0.0f || n != 0.0f) {
                this.scrollByInternal((int)(n * this.mScaledHorizontalScrollFactor), (int)(n3 * this.mScaledVerticalScrollFactor), motionEvent);
            }
        }
        return false;
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent motionEvent) {
        final boolean mLayoutFrozen = this.mLayoutFrozen;
        boolean b = false;
        if (mLayoutFrozen) {
            return false;
        }
        if (this.dispatchOnItemTouchIntercept(motionEvent)) {
            this.cancelTouch();
            return true;
        }
        if (this.mLayout == null) {
            return false;
        }
        final int canScrollHorizontally = this.mLayout.canScrollHorizontally() ? 1 : 0;
        final boolean canScrollVertically = this.mLayout.canScrollVertically();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        final int actionMasked = motionEvent.getActionMasked();
        final int actionIndex = motionEvent.getActionIndex();
        switch (actionMasked) {
            case 6: {
                this.onPointerUp(motionEvent);
                break;
            }
            case 5: {
                this.mScrollPointerId = motionEvent.getPointerId(actionIndex);
                final int n = (int)(motionEvent.getX(actionIndex) + 0.5f);
                this.mLastTouchX = n;
                this.mInitialTouchX = n;
                final int n2 = (int)(motionEvent.getY(actionIndex) + 0.5f);
                this.mLastTouchY = n2;
                this.mInitialTouchY = n2;
                break;
            }
            case 3: {
                this.cancelTouch();
                break;
            }
            case 2: {
                final int pointerIndex = motionEvent.findPointerIndex(this.mScrollPointerId);
                if (pointerIndex < 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Error processing scroll; pointer index for id ");
                    sb.append(this.mScrollPointerId);
                    sb.append(" not found. Did any MotionEvents get skipped?");
                    Log.e("RecyclerView", sb.toString());
                    return false;
                }
                final int mLastTouchX = (int)(motionEvent.getX(pointerIndex) + 0.5f);
                final int mLastTouchY = (int)(motionEvent.getY(pointerIndex) + 0.5f);
                if (this.mScrollState == 1) {
                    break;
                }
                final int mInitialTouchX = this.mInitialTouchX;
                final int mInitialTouchY = this.mInitialTouchY;
                boolean b2;
                if (canScrollHorizontally && Math.abs(mLastTouchX - mInitialTouchX) > this.mTouchSlop) {
                    this.mLastTouchX = mLastTouchX;
                    b2 = true;
                }
                else {
                    b2 = false;
                }
                boolean b3 = b2;
                if (canScrollVertically) {
                    b3 = b2;
                    if (Math.abs(mLastTouchY - mInitialTouchY) > this.mTouchSlop) {
                        this.mLastTouchY = mLastTouchY;
                        b3 = true;
                    }
                }
                if (b3) {
                    this.setScrollState(1);
                    break;
                }
                break;
            }
            case 1: {
                this.mVelocityTracker.clear();
                this.stopNestedScroll(0);
                break;
            }
            case 0: {
                if (this.mIgnoreMotionEventTillDown) {
                    this.mIgnoreMotionEventTillDown = false;
                }
                this.mScrollPointerId = motionEvent.getPointerId(0);
                final int n3 = (int)(motionEvent.getX() + 0.5f);
                this.mLastTouchX = n3;
                this.mInitialTouchX = n3;
                final int n4 = (int)(motionEvent.getY() + 0.5f);
                this.mLastTouchY = n4;
                this.mInitialTouchY = n4;
                if (this.mScrollState == 2) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                    this.setScrollState(1);
                }
                this.mNestedOffsets[this.mNestedOffsets[1] = 0] = 0;
                int n6;
                final int n5 = n6 = canScrollHorizontally;
                if (canScrollVertically) {
                    n6 = (n5 | 0x2);
                }
                this.startNestedScroll(n6, 0);
                break;
            }
        }
        if (this.mScrollState == 1) {
            b = true;
        }
        return b;
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        TraceCompat.beginSection("RV OnLayout");
        this.dispatchLayout();
        TraceCompat.endSection();
        this.mFirstLayoutComplete = true;
    }
    
    protected void onMeasure(final int n, final int n2) {
        if (this.mLayout == null) {
            this.defaultOnMeasure(n, n2);
            return;
        }
        final boolean autoMeasureEnabled = this.mLayout.isAutoMeasureEnabled();
        final int n3 = 0;
        if (autoMeasureEnabled) {
            final int mode = View$MeasureSpec.getMode(n);
            final int mode2 = View$MeasureSpec.getMode(n2);
            this.mLayout.onMeasure(this.mRecycler, this.mState, n, n2);
            int n4 = n3;
            if (mode == 1073741824) {
                n4 = n3;
                if (mode2 == 1073741824) {
                    n4 = 1;
                }
            }
            if (n4 != 0 || this.mAdapter == null) {
                return;
            }
            if (this.mState.mLayoutStep == 1) {
                this.dispatchLayoutStep1();
            }
            this.mLayout.setMeasureSpecs(n, n2);
            this.mState.mIsMeasuring = true;
            this.dispatchLayoutStep2();
            this.mLayout.setMeasuredDimensionFromChildren(n, n2);
            if (this.mLayout.shouldMeasureTwice()) {
                this.mLayout.setMeasureSpecs(View$MeasureSpec.makeMeasureSpec(this.getMeasuredWidth(), 1073741824), View$MeasureSpec.makeMeasureSpec(this.getMeasuredHeight(), 1073741824));
                this.mState.mIsMeasuring = true;
                this.dispatchLayoutStep2();
                this.mLayout.setMeasuredDimensionFromChildren(n, n2);
            }
        }
        else {
            if (this.mHasFixedSize) {
                this.mLayout.onMeasure(this.mRecycler, this.mState, n, n2);
                return;
            }
            if (this.mAdapterUpdateDuringMeasure) {
                this.startInterceptRequestLayout();
                this.onEnterLayoutOrScroll();
                this.processAdapterUpdatesAndSetAnimationFlags();
                this.onExitLayoutOrScroll();
                if (this.mState.mRunPredictiveAnimations) {
                    this.mState.mInPreLayout = true;
                }
                else {
                    this.mAdapterHelper.consumeUpdatesInOnePass();
                    this.mState.mInPreLayout = false;
                }
                this.stopInterceptRequestLayout(this.mAdapterUpdateDuringMeasure = false);
            }
            else if (this.mState.mRunPredictiveAnimations) {
                this.setMeasuredDimension(this.getMeasuredWidth(), this.getMeasuredHeight());
                return;
            }
            if (this.mAdapter != null) {
                this.mState.mItemCount = this.mAdapter.getItemCount();
            }
            else {
                this.mState.mItemCount = 0;
            }
            this.startInterceptRequestLayout();
            this.mLayout.onMeasure(this.mRecycler, this.mState, n, n2);
            this.stopInterceptRequestLayout(false);
            this.mState.mInPreLayout = false;
        }
    }
    
    protected boolean onRequestFocusInDescendants(final int n, final Rect rect) {
        return !this.isComputingLayout() && super.onRequestFocusInDescendants(n, rect);
    }
    
    protected void onRestoreInstanceState(final Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        this.mPendingSavedState = (SavedState)parcelable;
        super.onRestoreInstanceState(this.mPendingSavedState.getSuperState());
        if (this.mLayout != null && this.mPendingSavedState.mLayoutState != null) {
            this.mLayout.onRestoreInstanceState(this.mPendingSavedState.mLayoutState);
        }
    }
    
    protected Parcelable onSaveInstanceState() {
        final SavedState savedState = new SavedState(super.onSaveInstanceState());
        if (this.mPendingSavedState != null) {
            savedState.copyFrom(this.mPendingSavedState);
        }
        else if (this.mLayout != null) {
            savedState.mLayoutState = this.mLayout.onSaveInstanceState();
        }
        else {
            savedState.mLayoutState = null;
        }
        return (Parcelable)savedState;
    }
    
    public void onScrollStateChanged(final int n) {
    }
    
    public void onScrolled(@Px final int n, @Px final int n2) {
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (n != n3 || n2 != n4) {
            this.invalidateGlows();
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final boolean mLayoutFrozen = this.mLayoutFrozen;
        final boolean b = false;
        if (mLayoutFrozen || this.mIgnoreMotionEventTillDown) {
            return false;
        }
        if (this.dispatchOnItemTouch(motionEvent)) {
            this.cancelTouch();
            return true;
        }
        if (this.mLayout == null) {
            return false;
        }
        final boolean canScrollHorizontally = this.mLayout.canScrollHorizontally();
        final boolean canScrollVertically = this.mLayout.canScrollVertically();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        final MotionEvent obtain = MotionEvent.obtain(motionEvent);
        final int actionMasked = motionEvent.getActionMasked();
        final int actionIndex = motionEvent.getActionIndex();
        if (actionMasked == 0) {
            this.mNestedOffsets[this.mNestedOffsets[1] = 0] = 0;
        }
        obtain.offsetLocation((float)this.mNestedOffsets[0], (float)this.mNestedOffsets[1]);
        int n = 0;
        switch (actionMasked) {
            default: {
                n = (b ? 1 : 0);
                break;
            }
            case 6: {
                this.onPointerUp(motionEvent);
                n = (b ? 1 : 0);
                break;
            }
            case 5: {
                this.mScrollPointerId = motionEvent.getPointerId(actionIndex);
                final int n2 = (int)(motionEvent.getX(actionIndex) + 0.5f);
                this.mLastTouchX = n2;
                this.mInitialTouchX = n2;
                final int n3 = (int)(motionEvent.getY(actionIndex) + 0.5f);
                this.mLastTouchY = n3;
                this.mInitialTouchY = n3;
                n = (b ? 1 : 0);
                break;
            }
            case 3: {
                this.cancelTouch();
                n = (b ? 1 : 0);
                break;
            }
            case 2: {
                final int pointerIndex = motionEvent.findPointerIndex(this.mScrollPointerId);
                if (pointerIndex < 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Error processing scroll; pointer index for id ");
                    sb.append(this.mScrollPointerId);
                    sb.append(" not found. Did any MotionEvents get skipped?");
                    Log.e("RecyclerView", sb.toString());
                    return false;
                }
                final int n4 = (int)(motionEvent.getX(pointerIndex) + 0.5f);
                final int n5 = (int)(motionEvent.getY(pointerIndex) + 0.5f);
                final int n6 = this.mLastTouchX - n4;
                final int n7 = this.mLastTouchY - n5;
                int a = n6;
                int a2 = n7;
                if (this.dispatchNestedPreScroll(n6, n7, this.mScrollConsumed, this.mScrollOffset, 0)) {
                    a = n6 - this.mScrollConsumed[0];
                    a2 = n7 - this.mScrollConsumed[1];
                    obtain.offsetLocation((float)this.mScrollOffset[0], (float)this.mScrollOffset[1]);
                    final int[] mNestedOffsets = this.mNestedOffsets;
                    mNestedOffsets[0] += this.mScrollOffset[0];
                    final int[] mNestedOffsets2 = this.mNestedOffsets;
                    mNestedOffsets2[1] += this.mScrollOffset[1];
                }
                int n8 = a;
                int n9 = a2;
                if (this.mScrollState != 1) {
                    boolean b2;
                    if (canScrollHorizontally && Math.abs(a) > this.mTouchSlop) {
                        if (a > 0) {
                            a -= this.mTouchSlop;
                        }
                        else {
                            a += this.mTouchSlop;
                        }
                        b2 = true;
                    }
                    else {
                        b2 = false;
                    }
                    boolean b3 = b2;
                    int n10 = a2;
                    if (canScrollVertically) {
                        b3 = b2;
                        n10 = a2;
                        if (Math.abs(a2) > this.mTouchSlop) {
                            if (a2 > 0) {
                                n10 = a2 - this.mTouchSlop;
                            }
                            else {
                                n10 = a2 + this.mTouchSlop;
                            }
                            b3 = true;
                        }
                    }
                    n8 = a;
                    n9 = n10;
                    if (b3) {
                        this.setScrollState(1);
                        n9 = n10;
                        n8 = a;
                    }
                }
                n = (b ? 1 : 0);
                if (this.mScrollState != 1) {
                    break;
                }
                this.mLastTouchX = n4 - this.mScrollOffset[0];
                this.mLastTouchY = n5 - this.mScrollOffset[1];
                int n11;
                if (canScrollHorizontally) {
                    n11 = n8;
                }
                else {
                    n11 = 0;
                }
                int n12;
                if (canScrollVertically) {
                    n12 = n9;
                }
                else {
                    n12 = 0;
                }
                if (this.scrollByInternal(n11, n12, obtain)) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                }
                n = (b ? 1 : 0);
                if (this.mGapWorker != null) {
                    if (n8 == 0) {
                        n = (b ? 1 : 0);
                        if (n9 == 0) {
                            break;
                        }
                    }
                    this.mGapWorker.postFromTraversal(this, n8, n9);
                    n = (b ? 1 : 0);
                    break;
                }
                break;
            }
            case 1: {
                this.mVelocityTracker.addMovement(obtain);
                this.mVelocityTracker.computeCurrentVelocity(1000, (float)this.mMaxFlingVelocity);
                float n13;
                if (canScrollHorizontally) {
                    n13 = -this.mVelocityTracker.getXVelocity(this.mScrollPointerId);
                }
                else {
                    n13 = 0.0f;
                }
                float n14;
                if (canScrollVertically) {
                    n14 = -this.mVelocityTracker.getYVelocity(this.mScrollPointerId);
                }
                else {
                    n14 = 0.0f;
                }
                if ((n13 == 0.0f && n14 == 0.0f) || !this.fling((int)n13, (int)n14)) {
                    this.setScrollState(0);
                }
                this.resetTouch();
                n = 1;
                break;
            }
            case 0: {
                this.mScrollPointerId = motionEvent.getPointerId(0);
                final int n15 = (int)(motionEvent.getX() + 0.5f);
                this.mLastTouchX = n15;
                this.mInitialTouchX = n15;
                final int n16 = (int)(motionEvent.getY() + 0.5f);
                this.mLastTouchY = n16;
                this.mInitialTouchY = n16;
                int n17;
                if (canScrollHorizontally) {
                    n17 = 1;
                }
                else {
                    n17 = 0;
                }
                int n18 = n17;
                if (canScrollVertically) {
                    n18 = (n17 | 0x2);
                }
                this.startNestedScroll(n18, 0);
                n = (b ? 1 : 0);
                break;
            }
        }
        if (n == 0) {
            this.mVelocityTracker.addMovement(obtain);
        }
        obtain.recycle();
        return true;
    }
    
    void postAnimationRunner() {
        if (!this.mPostedAnimatorRunner && this.mIsAttached) {
            ViewCompat.postOnAnimation((View)this, this.mItemAnimatorRunner);
            this.mPostedAnimatorRunner = true;
        }
    }
    
    void processDataSetCompletelyChanged(final boolean b) {
        this.mDispatchItemsChangedEvent |= b;
        this.mDataSetHasChangedAfterLayout = true;
        this.markKnownViewsInvalid();
    }
    
    void recordAnimationInfoIfBouncedHiddenView(final ViewHolder viewHolder, final ItemHolderInfo itemHolderInfo) {
        viewHolder.setFlags(0, 8192);
        if (this.mState.mTrackOldChangeHolders && viewHolder.isUpdated() && !viewHolder.isRemoved() && !viewHolder.shouldIgnore()) {
            this.mViewInfoStore.addToOldChangeHolders(this.getChangedHolderKey(viewHolder), viewHolder);
        }
        this.mViewInfoStore.addToPreLayout(viewHolder, itemHolderInfo);
    }
    
    void removeAndRecycleViews() {
        if (this.mItemAnimator != null) {
            this.mItemAnimator.endAnimations();
        }
        if (this.mLayout != null) {
            this.mLayout.removeAndRecycleAllViews(this.mRecycler);
            this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
        }
        this.mRecycler.clear();
    }
    
    boolean removeAnimatingView(final View view) {
        this.startInterceptRequestLayout();
        final boolean removeViewIfHidden = this.mChildHelper.removeViewIfHidden(view);
        if (removeViewIfHidden) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
            this.mRecycler.unscrapView(childViewHolderInt);
            this.mRecycler.recycleViewHolderInternal(childViewHolderInt);
        }
        this.stopInterceptRequestLayout(removeViewIfHidden ^ true);
        return removeViewIfHidden;
    }
    
    protected void removeDetachedView(final View view, final boolean b) {
        final ViewHolder childViewHolderInt = getChildViewHolderInt(view);
        if (childViewHolderInt != null) {
            if (childViewHolderInt.isTmpDetached()) {
                childViewHolderInt.clearTmpDetachFlag();
            }
            else if (!childViewHolderInt.shouldIgnore()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Called removeDetachedView with a view which is not flagged as tmp detached.");
                sb.append(childViewHolderInt);
                sb.append(this.exceptionLabel());
                throw new IllegalArgumentException(sb.toString());
            }
        }
        view.clearAnimation();
        this.dispatchChildDetached(view);
        super.removeDetachedView(view, b);
    }
    
    public void removeItemDecoration(@NonNull final ItemDecoration o) {
        if (this.mLayout != null) {
            this.mLayout.assertNotInLayoutOrScroll("Cannot remove item decoration during a scroll  or layout");
        }
        this.mItemDecorations.remove(o);
        if (this.mItemDecorations.isEmpty()) {
            this.setWillNotDraw(this.getOverScrollMode() == 2);
        }
        this.markItemDecorInsetsDirty();
        this.requestLayout();
    }
    
    public void removeItemDecorationAt(final int i) {
        final int itemDecorationCount = this.getItemDecorationCount();
        if (i >= 0 && i < itemDecorationCount) {
            this.removeItemDecoration(this.getItemDecorationAt(i));
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(i);
        sb.append(" is an invalid index for size ");
        sb.append(itemDecorationCount);
        throw new IndexOutOfBoundsException(sb.toString());
    }
    
    public void removeOnChildAttachStateChangeListener(@NonNull final OnChildAttachStateChangeListener onChildAttachStateChangeListener) {
        if (this.mOnChildAttachStateListeners == null) {
            return;
        }
        this.mOnChildAttachStateListeners.remove(onChildAttachStateChangeListener);
    }
    
    public void removeOnItemTouchListener(@NonNull final OnItemTouchListener o) {
        this.mOnItemTouchListeners.remove(o);
        if (this.mActiveOnItemTouchListener == o) {
            this.mActiveOnItemTouchListener = null;
        }
    }
    
    public void removeOnScrollListener(@NonNull final OnScrollListener onScrollListener) {
        if (this.mScrollListeners != null) {
            this.mScrollListeners.remove(onScrollListener);
        }
    }
    
    void repositionShadowingViews() {
        for (int childCount = this.mChildHelper.getChildCount(), i = 0; i < childCount; ++i) {
            final View child = this.mChildHelper.getChildAt(i);
            final ViewHolder childViewHolder = this.getChildViewHolder(child);
            if (childViewHolder != null && childViewHolder.mShadowingHolder != null) {
                final View itemView = childViewHolder.mShadowingHolder.itemView;
                final int left = child.getLeft();
                final int top = child.getTop();
                if (left != itemView.getLeft() || top != itemView.getTop()) {
                    itemView.layout(left, top, itemView.getWidth() + left, itemView.getHeight() + top);
                }
            }
        }
    }
    
    public void requestChildFocus(final View view, final View view2) {
        if (!this.mLayout.onRequestChildFocus(this, this.mState, view, view2) && view2 != null) {
            this.requestChildOnScreen(view, view2);
        }
        super.requestChildFocus(view, view2);
    }
    
    public boolean requestChildRectangleOnScreen(final View view, final Rect rect, final boolean b) {
        return this.mLayout.requestChildRectangleOnScreen(this, view, rect, b);
    }
    
    public void requestDisallowInterceptTouchEvent(final boolean b) {
        for (int size = this.mOnItemTouchListeners.size(), i = 0; i < size; ++i) {
            this.mOnItemTouchListeners.get(i).onRequestDisallowInterceptTouchEvent(b);
        }
        super.requestDisallowInterceptTouchEvent(b);
    }
    
    public void requestLayout() {
        if (this.mInterceptRequestLayoutDepth == 0 && !this.mLayoutFrozen) {
            super.requestLayout();
        }
        else {
            this.mLayoutWasDefered = true;
        }
    }
    
    void saveOldPositions() {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final ViewHolder childViewHolderInt = getChildViewHolderInt(this.mChildHelper.getUnfilteredChildAt(i));
            if (!childViewHolderInt.shouldIgnore()) {
                childViewHolderInt.saveOldPosition();
            }
        }
    }
    
    public void scrollBy(int n, int n2) {
        if (this.mLayout == null) {
            Log.e("RecyclerView", "Cannot scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        if (this.mLayoutFrozen) {
            return;
        }
        final boolean canScrollHorizontally = this.mLayout.canScrollHorizontally();
        final boolean canScrollVertically = this.mLayout.canScrollVertically();
        if (canScrollHorizontally || canScrollVertically) {
            if (!canScrollHorizontally) {
                n = 0;
            }
            if (!canScrollVertically) {
                n2 = 0;
            }
            this.scrollByInternal(n, n2, null);
        }
    }
    
    boolean scrollByInternal(final int n, final int n2, final MotionEvent motionEvent) {
        this.consumePendingUpdateOperations();
        final Adapter mAdapter = this.mAdapter;
        final boolean b = true;
        int n5;
        int n6;
        int n7;
        int n8;
        if (mAdapter != null) {
            this.scrollStep(n, n2, this.mScrollStepConsumed);
            final int n3 = this.mScrollStepConsumed[0];
            final int n4 = this.mScrollStepConsumed[1];
            n5 = n3;
            n6 = n4;
            n7 = n - n3;
            n8 = n2 - n4;
        }
        else {
            n8 = 0;
            n5 = 0;
            n6 = (n7 = n5);
        }
        if (!this.mItemDecorations.isEmpty()) {
            this.invalidate();
        }
        if (this.dispatchNestedScroll(n5, n6, n7, n8, this.mScrollOffset, 0)) {
            this.mLastTouchX -= this.mScrollOffset[0];
            this.mLastTouchY -= this.mScrollOffset[1];
            if (motionEvent != null) {
                motionEvent.offsetLocation((float)this.mScrollOffset[0], (float)this.mScrollOffset[1]);
            }
            final int[] mNestedOffsets = this.mNestedOffsets;
            mNestedOffsets[0] += this.mScrollOffset[0];
            final int[] mNestedOffsets2 = this.mNestedOffsets;
            mNestedOffsets2[1] += this.mScrollOffset[1];
        }
        else if (this.getOverScrollMode() != 2) {
            if (motionEvent != null && !MotionEventCompat.isFromSource(motionEvent, 8194)) {
                this.pullGlows(motionEvent.getX(), (float)n7, motionEvent.getY(), (float)n8);
            }
            this.considerReleasingGlowsOnScroll(n, n2);
        }
        if (n5 != 0 || n6 != 0) {
            this.dispatchOnScrolled(n5, n6);
        }
        if (!this.awakenScrollBars()) {
            this.invalidate();
        }
        boolean b2 = b;
        if (n5 == 0) {
            b2 = (n6 != 0 && b);
        }
        return b2;
    }
    
    void scrollStep(int scrollHorizontallyBy, int scrollVerticallyBy, @Nullable final int[] array) {
        this.startInterceptRequestLayout();
        this.onEnterLayoutOrScroll();
        TraceCompat.beginSection("RV Scroll");
        this.fillRemainingScrollValues(this.mState);
        if (scrollHorizontallyBy != 0) {
            scrollHorizontallyBy = this.mLayout.scrollHorizontallyBy(scrollHorizontallyBy, this.mRecycler, this.mState);
        }
        else {
            scrollHorizontallyBy = 0;
        }
        if (scrollVerticallyBy != 0) {
            scrollVerticallyBy = this.mLayout.scrollVerticallyBy(scrollVerticallyBy, this.mRecycler, this.mState);
        }
        else {
            scrollVerticallyBy = 0;
        }
        TraceCompat.endSection();
        this.repositionShadowingViews();
        this.onExitLayoutOrScroll();
        this.stopInterceptRequestLayout(false);
        if (array != null) {
            array[0] = scrollHorizontallyBy;
            array[1] = scrollVerticallyBy;
        }
    }
    
    public void scrollTo(final int n, final int n2) {
        Log.w("RecyclerView", "RecyclerView does not support scrolling to an absolute position. Use scrollToPosition instead");
    }
    
    public void scrollToPosition(final int n) {
        if (this.mLayoutFrozen) {
            return;
        }
        this.stopScroll();
        if (this.mLayout == null) {
            Log.e("RecyclerView", "Cannot scroll to position a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        this.mLayout.scrollToPosition(n);
        this.awakenScrollBars();
    }
    
    public void sendAccessibilityEventUnchecked(final AccessibilityEvent accessibilityEvent) {
        if (this.shouldDeferAccessibilityEvent(accessibilityEvent)) {
            return;
        }
        super.sendAccessibilityEventUnchecked(accessibilityEvent);
    }
    
    public void setAccessibilityDelegateCompat(@Nullable final RecyclerViewAccessibilityDelegate mAccessibilityDelegate) {
        ViewCompat.setAccessibilityDelegate((View)this, this.mAccessibilityDelegate = mAccessibilityDelegate);
    }
    
    public void setAdapter(@Nullable final Adapter adapter) {
        this.setLayoutFrozen(false);
        this.setAdapterInternal(adapter, false, true);
        this.processDataSetCompletelyChanged(false);
        this.requestLayout();
    }
    
    public void setChildDrawingOrderCallback(@Nullable final ChildDrawingOrderCallback mChildDrawingOrderCallback) {
        if (mChildDrawingOrderCallback == this.mChildDrawingOrderCallback) {
            return;
        }
        this.mChildDrawingOrderCallback = mChildDrawingOrderCallback;
        this.setChildrenDrawingOrderEnabled(this.mChildDrawingOrderCallback != null);
    }
    
    @VisibleForTesting
    boolean setChildImportantForAccessibilityInternal(final ViewHolder viewHolder, final int mPendingAccessibilityState) {
        if (this.isComputingLayout()) {
            viewHolder.mPendingAccessibilityState = mPendingAccessibilityState;
            this.mPendingAccessibilityImportanceChange.add(viewHolder);
            return false;
        }
        ViewCompat.setImportantForAccessibility(viewHolder.itemView, mPendingAccessibilityState);
        return true;
    }
    
    public void setClipToPadding(final boolean mClipToPadding) {
        if (mClipToPadding != this.mClipToPadding) {
            this.invalidateGlows();
        }
        super.setClipToPadding(this.mClipToPadding = mClipToPadding);
        if (this.mFirstLayoutComplete) {
            this.requestLayout();
        }
    }
    
    public void setEdgeEffectFactory(@NonNull final EdgeEffectFactory mEdgeEffectFactory) {
        Preconditions.checkNotNull(mEdgeEffectFactory);
        this.mEdgeEffectFactory = mEdgeEffectFactory;
        this.invalidateGlows();
    }
    
    public void setHasFixedSize(final boolean mHasFixedSize) {
        this.mHasFixedSize = mHasFixedSize;
    }
    
    public void setItemAnimator(@Nullable final ItemAnimator mItemAnimator) {
        if (this.mItemAnimator != null) {
            this.mItemAnimator.endAnimations();
            this.mItemAnimator.setListener(null);
        }
        this.mItemAnimator = mItemAnimator;
        if (this.mItemAnimator != null) {
            this.mItemAnimator.setListener(this.mItemAnimatorListener);
        }
    }
    
    public void setItemViewCacheSize(final int viewCacheSize) {
        this.mRecycler.setViewCacheSize(viewCacheSize);
    }
    
    public void setLayoutFrozen(final boolean b) {
        if (b != this.mLayoutFrozen) {
            this.assertNotInLayoutOrScroll("Do not setLayoutFrozen in layout or scroll");
            if (!b) {
                this.mLayoutFrozen = false;
                if (this.mLayoutWasDefered && this.mLayout != null && this.mAdapter != null) {
                    this.requestLayout();
                }
                this.mLayoutWasDefered = false;
            }
            else {
                final long uptimeMillis = SystemClock.uptimeMillis();
                this.onTouchEvent(MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0));
                this.mLayoutFrozen = true;
                this.mIgnoreMotionEventTillDown = true;
                this.stopScroll();
            }
        }
    }
    
    public void setLayoutManager(@Nullable final LayoutManager layoutManager) {
        if (layoutManager == this.mLayout) {
            return;
        }
        this.stopScroll();
        if (this.mLayout != null) {
            if (this.mItemAnimator != null) {
                this.mItemAnimator.endAnimations();
            }
            this.mLayout.removeAndRecycleAllViews(this.mRecycler);
            this.mLayout.removeAndRecycleScrapInt(this.mRecycler);
            this.mRecycler.clear();
            if (this.mIsAttached) {
                this.mLayout.dispatchDetachedFromWindow(this, this.mRecycler);
            }
            this.mLayout.setRecyclerView(null);
            this.mLayout = null;
        }
        else {
            this.mRecycler.clear();
        }
        this.mChildHelper.removeAllViewsUnfiltered();
        this.mLayout = layoutManager;
        if (layoutManager != null) {
            if (layoutManager.mRecyclerView != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("LayoutManager ");
                sb.append(layoutManager);
                sb.append(" is already attached to a RecyclerView:");
                sb.append(layoutManager.mRecyclerView.exceptionLabel());
                throw new IllegalArgumentException(sb.toString());
            }
            this.mLayout.setRecyclerView(this);
            if (this.mIsAttached) {
                this.mLayout.dispatchAttachedToWindow(this);
            }
        }
        this.mRecycler.updateViewCacheSize();
        this.requestLayout();
    }
    
    public void setNestedScrollingEnabled(final boolean nestedScrollingEnabled) {
        this.getScrollingChildHelper().setNestedScrollingEnabled(nestedScrollingEnabled);
    }
    
    public void setOnFlingListener(@Nullable final OnFlingListener mOnFlingListener) {
        this.mOnFlingListener = mOnFlingListener;
    }
    
    @Deprecated
    public void setOnScrollListener(@Nullable final OnScrollListener mScrollListener) {
        this.mScrollListener = mScrollListener;
    }
    
    public void setPreserveFocusAfterLayout(final boolean mPreserveFocusAfterLayout) {
        this.mPreserveFocusAfterLayout = mPreserveFocusAfterLayout;
    }
    
    public void setRecycledViewPool(@Nullable final RecycledViewPool recycledViewPool) {
        this.mRecycler.setRecycledViewPool(recycledViewPool);
    }
    
    public void setRecyclerListener(@Nullable final RecyclerListener mRecyclerListener) {
        this.mRecyclerListener = mRecyclerListener;
    }
    
    void setScrollState(final int mScrollState) {
        if (mScrollState == this.mScrollState) {
            return;
        }
        if ((this.mScrollState = mScrollState) != 2) {
            this.stopScrollersInternal();
        }
        this.dispatchOnScrollStateChanged(mScrollState);
    }
    
    public void setScrollingTouchSlop(final int i) {
        final ViewConfiguration value = ViewConfiguration.get(this.getContext());
        switch (i) {
            case 1: {
                this.mTouchSlop = value.getScaledPagingTouchSlop();
                break;
            }
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("setScrollingTouchSlop(): bad argument constant ");
                sb.append(i);
                sb.append("; using default value");
                Log.w("RecyclerView", sb.toString());
            }
            case 0: {
                this.mTouchSlop = value.getScaledTouchSlop();
                break;
            }
        }
    }
    
    public void setViewCacheExtension(@Nullable final ViewCacheExtension viewCacheExtension) {
        this.mRecycler.setViewCacheExtension(viewCacheExtension);
    }
    
    boolean shouldDeferAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        if (this.isComputingLayout()) {
            int contentChangeTypes;
            if (accessibilityEvent != null) {
                contentChangeTypes = AccessibilityEventCompat.getContentChangeTypes(accessibilityEvent);
            }
            else {
                contentChangeTypes = 0;
            }
            int n = contentChangeTypes;
            if (contentChangeTypes == 0) {
                n = 0;
            }
            this.mEatenAccessibilityChangeFlags |= n;
            return true;
        }
        return false;
    }
    
    public void smoothScrollBy(@Px final int n, @Px final int n2) {
        this.smoothScrollBy(n, n2, null);
    }
    
    public void smoothScrollBy(@Px int n, @Px int n2, @Nullable final Interpolator interpolator) {
        if (this.mLayout == null) {
            Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        if (this.mLayoutFrozen) {
            return;
        }
        if (!this.mLayout.canScrollHorizontally()) {
            n = 0;
        }
        if (!this.mLayout.canScrollVertically()) {
            n2 = 0;
        }
        if (n != 0 || n2 != 0) {
            this.mViewFlinger.smoothScrollBy(n, n2, interpolator);
        }
    }
    
    public void smoothScrollToPosition(final int n) {
        if (this.mLayoutFrozen) {
            return;
        }
        if (this.mLayout == null) {
            Log.e("RecyclerView", "Cannot smooth scroll without a LayoutManager set. Call setLayoutManager with a non-null argument.");
            return;
        }
        this.mLayout.smoothScrollToPosition(this, this.mState, n);
    }
    
    void startInterceptRequestLayout() {
        ++this.mInterceptRequestLayoutDepth;
        if (this.mInterceptRequestLayoutDepth == 1 && !this.mLayoutFrozen) {
            this.mLayoutWasDefered = false;
        }
    }
    
    public boolean startNestedScroll(final int n) {
        return this.getScrollingChildHelper().startNestedScroll(n);
    }
    
    public boolean startNestedScroll(final int n, final int n2) {
        return this.getScrollingChildHelper().startNestedScroll(n, n2);
    }
    
    void stopInterceptRequestLayout(final boolean b) {
        if (this.mInterceptRequestLayoutDepth < 1) {
            this.mInterceptRequestLayoutDepth = 1;
        }
        if (!b && !this.mLayoutFrozen) {
            this.mLayoutWasDefered = false;
        }
        if (this.mInterceptRequestLayoutDepth == 1) {
            if (b && this.mLayoutWasDefered && !this.mLayoutFrozen && this.mLayout != null && this.mAdapter != null) {
                this.dispatchLayout();
            }
            if (!this.mLayoutFrozen) {
                this.mLayoutWasDefered = false;
            }
        }
        --this.mInterceptRequestLayoutDepth;
    }
    
    public void stopNestedScroll() {
        this.getScrollingChildHelper().stopNestedScroll();
    }
    
    public void stopNestedScroll(final int n) {
        this.getScrollingChildHelper().stopNestedScroll(n);
    }
    
    public void stopScroll() {
        this.setScrollState(0);
        this.stopScrollersInternal();
    }
    
    public void swapAdapter(@Nullable final Adapter adapter, final boolean b) {
        this.setLayoutFrozen(false);
        this.setAdapterInternal(adapter, true, b);
        this.processDataSetCompletelyChanged(true);
        this.requestLayout();
    }
    
    void viewRangeUpdate(final int n, final int n2, final Object o) {
        for (int unfilteredChildCount = this.mChildHelper.getUnfilteredChildCount(), i = 0; i < unfilteredChildCount; ++i) {
            final View unfilteredChild = this.mChildHelper.getUnfilteredChildAt(i);
            final ViewHolder childViewHolderInt = getChildViewHolderInt(unfilteredChild);
            if (childViewHolderInt != null) {
                if (!childViewHolderInt.shouldIgnore()) {
                    if (childViewHolderInt.mPosition >= n && childViewHolderInt.mPosition < n + n2) {
                        childViewHolderInt.addFlags(2);
                        childViewHolderInt.addChangePayload(o);
                        ((LayoutParams)unfilteredChild.getLayoutParams()).mInsetsDirty = true;
                    }
                }
            }
        }
        this.mRecycler.viewRangeUpdate(n, n2);
    }
    
    public abstract static class Adapter<VH extends ViewHolder>
    {
        private boolean mHasStableIds;
        private final AdapterDataObservable mObservable;
        
        public Adapter() {
            this.mObservable = new AdapterDataObservable();
            this.mHasStableIds = false;
        }
        
        public final void bindViewHolder(@NonNull final VH vh, final int mPosition) {
            vh.mPosition = mPosition;
            if (this.hasStableIds()) {
                vh.mItemId = this.getItemId(mPosition);
            }
            vh.setFlags(1, 519);
            TraceCompat.beginSection("RV OnBindView");
            this.onBindViewHolder(vh, mPosition, vh.getUnmodifiedPayloads());
            vh.clearPayload();
            final ViewGroup$LayoutParams layoutParams = vh.itemView.getLayoutParams();
            if (layoutParams instanceof LayoutParams) {
                ((LayoutParams)layoutParams).mInsetsDirty = true;
            }
            TraceCompat.endSection();
        }
        
        @NonNull
        public final VH createViewHolder(@NonNull final ViewGroup viewGroup, final int mItemViewType) {
            try {
                TraceCompat.beginSection("RV CreateView");
                final ViewHolder onCreateViewHolder = this.onCreateViewHolder(viewGroup, mItemViewType);
                if (onCreateViewHolder.itemView.getParent() != null) {
                    throw new IllegalStateException("ViewHolder views must not be attached when created. Ensure that you are not passing 'true' to the attachToRoot parameter of LayoutInflater.inflate(..., boolean attachToRoot)");
                }
                onCreateViewHolder.mItemViewType = mItemViewType;
                return (VH)onCreateViewHolder;
            }
            finally {
                TraceCompat.endSection();
            }
        }
        
        public abstract int getItemCount();
        
        public long getItemId(final int n) {
            return -1L;
        }
        
        public int getItemViewType(final int n) {
            return 0;
        }
        
        public final boolean hasObservers() {
            return this.mObservable.hasObservers();
        }
        
        public final boolean hasStableIds() {
            return this.mHasStableIds;
        }
        
        public final void notifyDataSetChanged() {
            this.mObservable.notifyChanged();
        }
        
        public final void notifyItemChanged(final int n) {
            this.mObservable.notifyItemRangeChanged(n, 1);
        }
        
        public final void notifyItemChanged(final int n, @Nullable final Object o) {
            this.mObservable.notifyItemRangeChanged(n, 1, o);
        }
        
        public final void notifyItemInserted(final int n) {
            this.mObservable.notifyItemRangeInserted(n, 1);
        }
        
        public final void notifyItemMoved(final int n, final int n2) {
            this.mObservable.notifyItemMoved(n, n2);
        }
        
        public final void notifyItemRangeChanged(final int n, final int n2) {
            this.mObservable.notifyItemRangeChanged(n, n2);
        }
        
        public final void notifyItemRangeChanged(final int n, final int n2, @Nullable final Object o) {
            this.mObservable.notifyItemRangeChanged(n, n2, o);
        }
        
        public final void notifyItemRangeInserted(final int n, final int n2) {
            this.mObservable.notifyItemRangeInserted(n, n2);
        }
        
        public final void notifyItemRangeRemoved(final int n, final int n2) {
            this.mObservable.notifyItemRangeRemoved(n, n2);
        }
        
        public final void notifyItemRemoved(final int n) {
            this.mObservable.notifyItemRangeRemoved(n, 1);
        }
        
        public void onAttachedToRecyclerView(@NonNull final RecyclerView recyclerView) {
        }
        
        public abstract void onBindViewHolder(@NonNull final VH p0, final int p1);
        
        public void onBindViewHolder(@NonNull final VH vh, final int n, @NonNull final List<Object> list) {
            this.onBindViewHolder(vh, n);
        }
        
        @NonNull
        public abstract VH onCreateViewHolder(@NonNull final ViewGroup p0, final int p1);
        
        public void onDetachedFromRecyclerView(@NonNull final RecyclerView recyclerView) {
        }
        
        public boolean onFailedToRecycleView(@NonNull final VH vh) {
            return false;
        }
        
        public void onViewAttachedToWindow(@NonNull final VH vh) {
        }
        
        public void onViewDetachedFromWindow(@NonNull final VH vh) {
        }
        
        public void onViewRecycled(@NonNull final VH vh) {
        }
        
        public void registerAdapterDataObserver(@NonNull final AdapterDataObserver adapterDataObserver) {
            this.mObservable.registerObserver((Object)adapterDataObserver);
        }
        
        public void setHasStableIds(final boolean mHasStableIds) {
            if (this.hasObservers()) {
                throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
            }
            this.mHasStableIds = mHasStableIds;
        }
        
        public void unregisterAdapterDataObserver(@NonNull final AdapterDataObserver adapterDataObserver) {
            this.mObservable.unregisterObserver((Object)adapterDataObserver);
        }
    }
    
    static class AdapterDataObservable extends Observable<AdapterDataObserver>
    {
        public boolean hasObservers() {
            return this.mObservers.isEmpty() ^ true;
        }
        
        public void notifyChanged() {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onChanged();
            }
        }
        
        public void notifyItemMoved(final int n, final int n2) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeMoved(n, n2, 1);
            }
        }
        
        public void notifyItemRangeChanged(final int n, final int n2) {
            this.notifyItemRangeChanged(n, n2, null);
        }
        
        public void notifyItemRangeChanged(final int n, final int n2, @Nullable final Object o) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeChanged(n, n2, o);
            }
        }
        
        public void notifyItemRangeInserted(final int n, final int n2) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeInserted(n, n2);
            }
        }
        
        public void notifyItemRangeRemoved(final int n, final int n2) {
            for (int i = this.mObservers.size() - 1; i >= 0; --i) {
                ((AdapterDataObserver)this.mObservers.get(i)).onItemRangeRemoved(n, n2);
            }
        }
    }
    
    public abstract static class AdapterDataObserver
    {
        public void onChanged() {
        }
        
        public void onItemRangeChanged(final int n, final int n2) {
        }
        
        public void onItemRangeChanged(final int n, final int n2, @Nullable final Object o) {
            this.onItemRangeChanged(n, n2);
        }
        
        public void onItemRangeInserted(final int n, final int n2) {
        }
        
        public void onItemRangeMoved(final int n, final int n2, final int n3) {
        }
        
        public void onItemRangeRemoved(final int n, final int n2) {
        }
    }
    
    public interface ChildDrawingOrderCallback
    {
        int onGetChildDrawingOrder(final int p0, final int p1);
    }
    
    public static class EdgeEffectFactory
    {
        public static final int DIRECTION_BOTTOM = 3;
        public static final int DIRECTION_LEFT = 0;
        public static final int DIRECTION_RIGHT = 2;
        public static final int DIRECTION_TOP = 1;
        
        @NonNull
        protected EdgeEffect createEdgeEffect(@NonNull final RecyclerView recyclerView, final int n) {
            return new EdgeEffect(recyclerView.getContext());
        }
        
        @Retention(RetentionPolicy.SOURCE)
        public @interface EdgeDirection {
        }
    }
    
    public abstract static class ItemAnimator
    {
        public static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
        public static final int FLAG_CHANGED = 2;
        public static final int FLAG_INVALIDATED = 4;
        public static final int FLAG_MOVED = 2048;
        public static final int FLAG_REMOVED = 8;
        private long mAddDuration;
        private long mChangeDuration;
        private ArrayList<ItemAnimatorFinishedListener> mFinishedListeners;
        private ItemAnimatorListener mListener;
        private long mMoveDuration;
        private long mRemoveDuration;
        
        public ItemAnimator() {
            this.mListener = null;
            this.mFinishedListeners = new ArrayList<ItemAnimatorFinishedListener>();
            this.mAddDuration = 120L;
            this.mRemoveDuration = 120L;
            this.mMoveDuration = 250L;
            this.mChangeDuration = 250L;
        }
        
        static int buildAdapterChangeFlagsForAnimations(final ViewHolder viewHolder) {
            final int n = viewHolder.mFlags & 0xE;
            if (viewHolder.isInvalid()) {
                return 4;
            }
            int n2 = n;
            if ((n & 0x4) == 0x0) {
                final int oldPosition = viewHolder.getOldPosition();
                final int adapterPosition = viewHolder.getAdapterPosition();
                n2 = n;
                if (oldPosition != -1) {
                    n2 = n;
                    if (adapterPosition != -1) {
                        n2 = n;
                        if (oldPosition != adapterPosition) {
                            n2 = (n | 0x800);
                        }
                    }
                }
            }
            return n2;
        }
        
        public abstract boolean animateAppearance(@NonNull final ViewHolder p0, @Nullable final ItemHolderInfo p1, @NonNull final ItemHolderInfo p2);
        
        public abstract boolean animateChange(@NonNull final ViewHolder p0, @NonNull final ViewHolder p1, @NonNull final ItemHolderInfo p2, @NonNull final ItemHolderInfo p3);
        
        public abstract boolean animateDisappearance(@NonNull final ViewHolder p0, @NonNull final ItemHolderInfo p1, @Nullable final ItemHolderInfo p2);
        
        public abstract boolean animatePersistence(@NonNull final ViewHolder p0, @NonNull final ItemHolderInfo p1, @NonNull final ItemHolderInfo p2);
        
        public boolean canReuseUpdatedViewHolder(@NonNull final ViewHolder viewHolder) {
            return true;
        }
        
        public boolean canReuseUpdatedViewHolder(@NonNull final ViewHolder viewHolder, @NonNull final List<Object> list) {
            return this.canReuseUpdatedViewHolder(viewHolder);
        }
        
        public final void dispatchAnimationFinished(@NonNull final ViewHolder viewHolder) {
            this.onAnimationFinished(viewHolder);
            if (this.mListener != null) {
                this.mListener.onAnimationFinished(viewHolder);
            }
        }
        
        public final void dispatchAnimationStarted(@NonNull final ViewHolder viewHolder) {
            this.onAnimationStarted(viewHolder);
        }
        
        public final void dispatchAnimationsFinished() {
            for (int size = this.mFinishedListeners.size(), i = 0; i < size; ++i) {
                this.mFinishedListeners.get(i).onAnimationsFinished();
            }
            this.mFinishedListeners.clear();
        }
        
        public abstract void endAnimation(@NonNull final ViewHolder p0);
        
        public abstract void endAnimations();
        
        public long getAddDuration() {
            return this.mAddDuration;
        }
        
        public long getChangeDuration() {
            return this.mChangeDuration;
        }
        
        public long getMoveDuration() {
            return this.mMoveDuration;
        }
        
        public long getRemoveDuration() {
            return this.mRemoveDuration;
        }
        
        public abstract boolean isRunning();
        
        public final boolean isRunning(@Nullable final ItemAnimatorFinishedListener e) {
            final boolean running = this.isRunning();
            if (e != null) {
                if (!running) {
                    e.onAnimationsFinished();
                }
                else {
                    this.mFinishedListeners.add(e);
                }
            }
            return running;
        }
        
        @NonNull
        public ItemHolderInfo obtainHolderInfo() {
            return new ItemHolderInfo();
        }
        
        public void onAnimationFinished(@NonNull final ViewHolder viewHolder) {
        }
        
        public void onAnimationStarted(@NonNull final ViewHolder viewHolder) {
        }
        
        @NonNull
        public ItemHolderInfo recordPostLayoutInformation(@NonNull final State state, @NonNull final ViewHolder from) {
            return this.obtainHolderInfo().setFrom(from);
        }
        
        @NonNull
        public ItemHolderInfo recordPreLayoutInformation(@NonNull final State state, @NonNull final ViewHolder from, final int n, @NonNull final List<Object> list) {
            return this.obtainHolderInfo().setFrom(from);
        }
        
        public abstract void runPendingAnimations();
        
        public void setAddDuration(final long mAddDuration) {
            this.mAddDuration = mAddDuration;
        }
        
        public void setChangeDuration(final long mChangeDuration) {
            this.mChangeDuration = mChangeDuration;
        }
        
        void setListener(final ItemAnimatorListener mListener) {
            this.mListener = mListener;
        }
        
        public void setMoveDuration(final long mMoveDuration) {
            this.mMoveDuration = mMoveDuration;
        }
        
        public void setRemoveDuration(final long mRemoveDuration) {
            this.mRemoveDuration = mRemoveDuration;
        }
        
        @Retention(RetentionPolicy.SOURCE)
        public @interface AdapterChanges {
        }
        
        public interface ItemAnimatorFinishedListener
        {
            void onAnimationsFinished();
        }
        
        interface ItemAnimatorListener
        {
            void onAnimationFinished(@NonNull final ViewHolder p0);
        }
        
        public static class ItemHolderInfo
        {
            public int bottom;
            public int changeFlags;
            public int left;
            public int right;
            public int top;
            
            @NonNull
            public ItemHolderInfo setFrom(@NonNull final ViewHolder viewHolder) {
                return this.setFrom(viewHolder, 0);
            }
            
            @NonNull
            public ItemHolderInfo setFrom(@NonNull final ViewHolder viewHolder, final int n) {
                final View itemView = viewHolder.itemView;
                this.left = itemView.getLeft();
                this.top = itemView.getTop();
                this.right = itemView.getRight();
                this.bottom = itemView.getBottom();
                return this;
            }
        }
    }
    
    private class ItemAnimatorRestoreListener implements ItemAnimatorListener
    {
        final RecyclerView this$0;
        
        ItemAnimatorRestoreListener(final RecyclerView this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onAnimationFinished(final ViewHolder viewHolder) {
            viewHolder.setIsRecyclable(true);
            if (viewHolder.mShadowedHolder != null && viewHolder.mShadowingHolder == null) {
                viewHolder.mShadowedHolder = null;
            }
            viewHolder.mShadowingHolder = null;
            if (!viewHolder.shouldBeKeptAsChild() && !this.this$0.removeAnimatingView(viewHolder.itemView) && viewHolder.isTmpDetached()) {
                this.this$0.removeDetachedView(viewHolder.itemView, false);
            }
        }
    }
    
    public abstract static class ItemDecoration
    {
        @Deprecated
        public void getItemOffsets(@NonNull final Rect rect, final int n, @NonNull final RecyclerView recyclerView) {
            rect.set(0, 0, 0, 0);
        }
        
        public void getItemOffsets(@NonNull final Rect rect, @NonNull final View view, @NonNull final RecyclerView recyclerView, @NonNull final State state) {
            this.getItemOffsets(rect, ((LayoutParams)view.getLayoutParams()).getViewLayoutPosition(), recyclerView);
        }
        
        @Deprecated
        public void onDraw(@NonNull final Canvas canvas, @NonNull final RecyclerView recyclerView) {
        }
        
        public void onDraw(@NonNull final Canvas canvas, @NonNull final RecyclerView recyclerView, @NonNull final State state) {
            this.onDraw(canvas, recyclerView);
        }
        
        @Deprecated
        public void onDrawOver(@NonNull final Canvas canvas, @NonNull final RecyclerView recyclerView) {
        }
        
        public void onDrawOver(@NonNull final Canvas canvas, @NonNull final RecyclerView recyclerView, @NonNull final State state) {
            this.onDrawOver(canvas, recyclerView);
        }
    }
    
    public abstract static class LayoutManager
    {
        boolean mAutoMeasure;
        ChildHelper mChildHelper;
        private int mHeight;
        private int mHeightMode;
        ViewBoundsCheck mHorizontalBoundCheck;
        private final ViewBoundsCheck.Callback mHorizontalBoundCheckCallback;
        boolean mIsAttachedToWindow;
        private boolean mItemPrefetchEnabled;
        private boolean mMeasurementCacheEnabled;
        int mPrefetchMaxCountObserved;
        boolean mPrefetchMaxObservedInInitialPrefetch;
        RecyclerView mRecyclerView;
        boolean mRequestedSimpleAnimations;
        @Nullable
        SmoothScroller mSmoothScroller;
        ViewBoundsCheck mVerticalBoundCheck;
        private final ViewBoundsCheck.Callback mVerticalBoundCheckCallback;
        private int mWidth;
        private int mWidthMode;
        
        public LayoutManager() {
            this.mHorizontalBoundCheckCallback = new ViewBoundsCheck.Callback() {
                final LayoutManager this$0;
                
                @Override
                public View getChildAt(final int n) {
                    return this.this$0.getChildAt(n);
                }
                
                @Override
                public int getChildCount() {
                    return this.this$0.getChildCount();
                }
                
                @Override
                public int getChildEnd(final View view) {
                    return this.this$0.getDecoratedRight(view) + ((LayoutParams)view.getLayoutParams()).rightMargin;
                }
                
                @Override
                public int getChildStart(final View view) {
                    return this.this$0.getDecoratedLeft(view) - ((LayoutParams)view.getLayoutParams()).leftMargin;
                }
                
                @Override
                public View getParent() {
                    return (View)this.this$0.mRecyclerView;
                }
                
                @Override
                public int getParentEnd() {
                    return this.this$0.getWidth() - this.this$0.getPaddingRight();
                }
                
                @Override
                public int getParentStart() {
                    return this.this$0.getPaddingLeft();
                }
            };
            this.mVerticalBoundCheckCallback = new ViewBoundsCheck.Callback() {
                final LayoutManager this$0;
                
                @Override
                public View getChildAt(final int n) {
                    return this.this$0.getChildAt(n);
                }
                
                @Override
                public int getChildCount() {
                    return this.this$0.getChildCount();
                }
                
                @Override
                public int getChildEnd(final View view) {
                    return this.this$0.getDecoratedBottom(view) + ((LayoutParams)view.getLayoutParams()).bottomMargin;
                }
                
                @Override
                public int getChildStart(final View view) {
                    return this.this$0.getDecoratedTop(view) - ((LayoutParams)view.getLayoutParams()).topMargin;
                }
                
                @Override
                public View getParent() {
                    return (View)this.this$0.mRecyclerView;
                }
                
                @Override
                public int getParentEnd() {
                    return this.this$0.getHeight() - this.this$0.getPaddingBottom();
                }
                
                @Override
                public int getParentStart() {
                    return this.this$0.getPaddingTop();
                }
            };
            this.mHorizontalBoundCheck = new ViewBoundsCheck(this.mHorizontalBoundCheckCallback);
            this.mVerticalBoundCheck = new ViewBoundsCheck(this.mVerticalBoundCheckCallback);
            this.mRequestedSimpleAnimations = false;
            this.mIsAttachedToWindow = false;
            this.mAutoMeasure = false;
            this.mMeasurementCacheEnabled = true;
            this.mItemPrefetchEnabled = true;
        }
        
        private void addViewInt(final View view, final int n, final boolean b) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (!b && !childViewHolderInt.isRemoved()) {
                this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(childViewHolderInt);
            }
            else {
                this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(childViewHolderInt);
            }
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (!childViewHolderInt.wasReturnedFromScrap() && !childViewHolderInt.isScrap()) {
                if (view.getParent() == this.mRecyclerView) {
                    final int indexOfChild = this.mChildHelper.indexOfChild(view);
                    int childCount;
                    if ((childCount = n) == -1) {
                        childCount = this.mChildHelper.getChildCount();
                    }
                    if (indexOfChild == -1) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Added View has RecyclerView as parent but view is not a real child. Unfiltered index:");
                        sb.append(this.mRecyclerView.indexOfChild(view));
                        sb.append(this.mRecyclerView.exceptionLabel());
                        throw new IllegalStateException(sb.toString());
                    }
                    if (indexOfChild != childCount) {
                        this.mRecyclerView.mLayout.moveView(indexOfChild, childCount);
                    }
                }
                else {
                    this.mChildHelper.addView(view, n, false);
                    layoutParams.mInsetsDirty = true;
                    if (this.mSmoothScroller != null && this.mSmoothScroller.isRunning()) {
                        this.mSmoothScroller.onChildAttachedToWindow(view);
                    }
                }
            }
            else {
                if (childViewHolderInt.isScrap()) {
                    childViewHolderInt.unScrap();
                }
                else {
                    childViewHolderInt.clearReturnedFromScrapFlag();
                }
                this.mChildHelper.attachViewToParent(view, n, view.getLayoutParams(), false);
            }
            if (layoutParams.mPendingInvalidate) {
                childViewHolderInt.itemView.invalidate();
                layoutParams.mPendingInvalidate = false;
            }
        }
        
        public static int chooseSize(int size, final int n, final int n2) {
            final int mode = View$MeasureSpec.getMode(size);
            size = View$MeasureSpec.getSize(size);
            if (mode == Integer.MIN_VALUE) {
                return Math.min(size, Math.max(n, n2));
            }
            if (mode != 1073741824) {
                return Math.max(n, n2);
            }
            return size;
        }
        
        private void detachViewInternal(final int n, @NonNull final View view) {
            this.mChildHelper.detachViewFromParent(n);
        }
        
        public static int getChildMeasureSpec(int n, int n2, int n3, final int n4, final boolean b) {
            final int n5 = 0;
            final int max = Math.max(0, n - n3);
            Label_0120: {
                if (b) {
                    if (n4 < 0) {
                        if (n4 == -1) {
                            if (n2 != Integer.MIN_VALUE && (n2 == 0 || n2 != 1073741824)) {
                                n2 = 0;
                                n = 0;
                            }
                            else {
                                n = max;
                            }
                            n3 = n;
                            n = n2;
                            return View$MeasureSpec.makeMeasureSpec(n3, n);
                        }
                        break Label_0120;
                    }
                }
                else if (n4 < 0) {
                    if (n4 == -1) {
                        n = n2;
                        n3 = max;
                        return View$MeasureSpec.makeMeasureSpec(n3, n);
                    }
                    if (n4 == -2) {
                        if (n2 != Integer.MIN_VALUE) {
                            n3 = max;
                            n = n5;
                            if (n2 != 1073741824) {
                                return View$MeasureSpec.makeMeasureSpec(n3, n);
                            }
                        }
                        n = Integer.MIN_VALUE;
                        n3 = max;
                        return View$MeasureSpec.makeMeasureSpec(n3, n);
                    }
                    break Label_0120;
                }
                n3 = n4;
                n = 1073741824;
                return View$MeasureSpec.makeMeasureSpec(n3, n);
            }
            n3 = 0;
            n = n5;
            return View$MeasureSpec.makeMeasureSpec(n3, n);
        }
        
        @Deprecated
        public static int getChildMeasureSpec(int max, int n, final int n2, final boolean b) {
            final int n3 = 0;
            max = Math.max(0, max - n);
            Label_0027: {
                Label_0021: {
                    if (b) {
                        if (n2 < 0) {
                            break Label_0027;
                        }
                    }
                    else if (n2 < 0) {
                        if (n2 == -1) {
                            break Label_0021;
                        }
                        if (n2 == -2) {
                            n = Integer.MIN_VALUE;
                            return View$MeasureSpec.makeMeasureSpec(max, n);
                        }
                        break Label_0027;
                    }
                    max = n2;
                }
                n = 1073741824;
                return View$MeasureSpec.makeMeasureSpec(max, n);
            }
            max = 0;
            n = n3;
            return View$MeasureSpec.makeMeasureSpec(max, n);
        }
        
        private int[] getChildRectangleOnScreenScrollAmount(final RecyclerView recyclerView, final View view, final Rect rect, final boolean b) {
            final int paddingLeft = this.getPaddingLeft();
            final int paddingTop = this.getPaddingTop();
            final int width = this.getWidth();
            final int paddingRight = this.getPaddingRight();
            final int height = this.getHeight();
            final int paddingBottom = this.getPaddingBottom();
            final int n = view.getLeft() + rect.left - view.getScrollX();
            final int n2 = view.getTop() + rect.top - view.getScrollY();
            final int width2 = rect.width();
            final int height2 = rect.height();
            final int n3 = n - paddingLeft;
            int a = Math.min(0, n3);
            final int n4 = n2 - paddingTop;
            int n5 = Math.min(0, n4);
            final int n6 = width2 + n - (width - paddingRight);
            final int max = Math.max(0, n6);
            final int max2 = Math.max(0, height2 + n2 - (height - paddingBottom));
            if (this.getLayoutDirection() == 1) {
                if (max != 0) {
                    a = max;
                }
                else {
                    a = Math.max(a, n6);
                }
            }
            else if (a == 0) {
                a = Math.min(n3, max);
            }
            if (n5 == 0) {
                n5 = Math.min(n4, max2);
            }
            return new int[] { a, n5 };
        }
        
        public static Properties getProperties(@NonNull final Context context, @Nullable final AttributeSet set, final int n, final int n2) {
            final Properties properties = new Properties();
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.RecyclerView, n, n2);
            properties.orientation = obtainStyledAttributes.getInt(R.styleable.RecyclerView_android_orientation, 1);
            properties.spanCount = obtainStyledAttributes.getInt(R.styleable.RecyclerView_spanCount, 1);
            properties.reverseLayout = obtainStyledAttributes.getBoolean(R.styleable.RecyclerView_reverseLayout, false);
            properties.stackFromEnd = obtainStyledAttributes.getBoolean(R.styleable.RecyclerView_stackFromEnd, false);
            obtainStyledAttributes.recycle();
            return properties;
        }
        
        private boolean isFocusedChildVisibleAfterScrolling(final RecyclerView recyclerView, final int n, final int n2) {
            final View focusedChild = recyclerView.getFocusedChild();
            if (focusedChild == null) {
                return false;
            }
            final int paddingLeft = this.getPaddingLeft();
            final int paddingTop = this.getPaddingTop();
            final int width = this.getWidth();
            final int paddingRight = this.getPaddingRight();
            final int height = this.getHeight();
            final int paddingBottom = this.getPaddingBottom();
            final Rect mTempRect = this.mRecyclerView.mTempRect;
            this.getDecoratedBoundsWithMargins(focusedChild, mTempRect);
            return mTempRect.left - n < width - paddingRight && mTempRect.right - n > paddingLeft && mTempRect.top - n2 < height - paddingBottom && mTempRect.bottom - n2 > paddingTop;
        }
        
        private static boolean isMeasurementUpToDate(final int n, int size, final int n2) {
            final int mode = View$MeasureSpec.getMode(size);
            size = View$MeasureSpec.getSize(size);
            boolean b = false;
            final boolean b2 = false;
            if (n2 > 0 && n != n2) {
                return false;
            }
            if (mode == Integer.MIN_VALUE) {
                if (size >= n) {
                    b = true;
                }
                return b;
            }
            if (mode == 0) {
                return true;
            }
            if (mode != 1073741824) {
                return false;
            }
            boolean b3 = b2;
            if (size == n) {
                b3 = true;
            }
            return b3;
        }
        
        private void scrapOrRecycleView(final Recycler recycler, final int n, final View view) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (childViewHolderInt.shouldIgnore()) {
                return;
            }
            if (childViewHolderInt.isInvalid() && !childViewHolderInt.isRemoved() && !this.mRecyclerView.mAdapter.hasStableIds()) {
                this.removeViewAt(n);
                recycler.recycleViewHolderInternal(childViewHolderInt);
            }
            else {
                this.detachViewAt(n);
                recycler.scrapView(view);
                this.mRecyclerView.mViewInfoStore.onViewDetached(childViewHolderInt);
            }
        }
        
        public void addDisappearingView(final View view) {
            this.addDisappearingView(view, -1);
        }
        
        public void addDisappearingView(final View view, final int n) {
            this.addViewInt(view, n, true);
        }
        
        public void addView(final View view) {
            this.addView(view, -1);
        }
        
        public void addView(final View view, final int n) {
            this.addViewInt(view, n, false);
        }
        
        public void assertInLayoutOrScroll(final String s) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.assertInLayoutOrScroll(s);
            }
        }
        
        public void assertNotInLayoutOrScroll(final String s) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.assertNotInLayoutOrScroll(s);
            }
        }
        
        public void attachView(@NonNull final View view) {
            this.attachView(view, -1);
        }
        
        public void attachView(@NonNull final View view, final int n) {
            this.attachView(view, n, (LayoutParams)view.getLayoutParams());
        }
        
        public void attachView(@NonNull final View view, final int n, final LayoutParams layoutParams) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (childViewHolderInt.isRemoved()) {
                this.mRecyclerView.mViewInfoStore.addToDisappearedInLayout(childViewHolderInt);
            }
            else {
                this.mRecyclerView.mViewInfoStore.removeFromDisappearedInLayout(childViewHolderInt);
            }
            this.mChildHelper.attachViewToParent(view, n, (ViewGroup$LayoutParams)layoutParams, childViewHolderInt.isRemoved());
        }
        
        public void calculateItemDecorationsForChild(@NonNull final View view, @NonNull final Rect rect) {
            if (this.mRecyclerView == null) {
                rect.set(0, 0, 0, 0);
                return;
            }
            rect.set(this.mRecyclerView.getItemDecorInsetsForChild(view));
        }
        
        public boolean canScrollHorizontally() {
            return false;
        }
        
        public boolean canScrollVertically() {
            return false;
        }
        
        public boolean checkLayoutParams(final LayoutParams layoutParams) {
            return layoutParams != null;
        }
        
        public void collectAdjacentPrefetchPositions(final int n, final int n2, final State state, final LayoutPrefetchRegistry layoutPrefetchRegistry) {
        }
        
        public void collectInitialPrefetchPositions(final int n, final LayoutPrefetchRegistry layoutPrefetchRegistry) {
        }
        
        public int computeHorizontalScrollExtent(@NonNull final State state) {
            return 0;
        }
        
        public int computeHorizontalScrollOffset(@NonNull final State state) {
            return 0;
        }
        
        public int computeHorizontalScrollRange(@NonNull final State state) {
            return 0;
        }
        
        public int computeVerticalScrollExtent(@NonNull final State state) {
            return 0;
        }
        
        public int computeVerticalScrollOffset(@NonNull final State state) {
            return 0;
        }
        
        public int computeVerticalScrollRange(@NonNull final State state) {
            return 0;
        }
        
        public void detachAndScrapAttachedViews(@NonNull final Recycler recycler) {
            for (int i = this.getChildCount() - 1; i >= 0; --i) {
                this.scrapOrRecycleView(recycler, i, this.getChildAt(i));
            }
        }
        
        public void detachAndScrapView(@NonNull final View view, @NonNull final Recycler recycler) {
            this.scrapOrRecycleView(recycler, this.mChildHelper.indexOfChild(view), view);
        }
        
        public void detachAndScrapViewAt(final int n, @NonNull final Recycler recycler) {
            this.scrapOrRecycleView(recycler, n, this.getChildAt(n));
        }
        
        public void detachView(@NonNull final View view) {
            final int indexOfChild = this.mChildHelper.indexOfChild(view);
            if (indexOfChild >= 0) {
                this.detachViewInternal(indexOfChild, view);
            }
        }
        
        public void detachViewAt(final int n) {
            this.detachViewInternal(n, this.getChildAt(n));
        }
        
        void dispatchAttachedToWindow(final RecyclerView recyclerView) {
            this.mIsAttachedToWindow = true;
            this.onAttachedToWindow(recyclerView);
        }
        
        void dispatchDetachedFromWindow(final RecyclerView recyclerView, final Recycler recycler) {
            this.mIsAttachedToWindow = false;
            this.onDetachedFromWindow(recyclerView, recycler);
        }
        
        public void endAnimation(final View view) {
            if (this.mRecyclerView.mItemAnimator != null) {
                this.mRecyclerView.mItemAnimator.endAnimation(RecyclerView.getChildViewHolderInt(view));
            }
        }
        
        @Nullable
        public View findContainingItemView(@NonNull View containingItemView) {
            if (this.mRecyclerView == null) {
                return null;
            }
            containingItemView = this.mRecyclerView.findContainingItemView(containingItemView);
            if (containingItemView == null) {
                return null;
            }
            if (this.mChildHelper.isHidden(containingItemView)) {
                return null;
            }
            return containingItemView;
        }
        
        @Nullable
        public View findViewByPosition(final int n) {
            for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                final View child = this.getChildAt(i);
                final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(child);
                if (childViewHolderInt != null) {
                    if (childViewHolderInt.getLayoutPosition() == n && !childViewHolderInt.shouldIgnore() && (this.mRecyclerView.mState.isPreLayout() || !childViewHolderInt.isRemoved())) {
                        return child;
                    }
                }
            }
            return null;
        }
        
        public abstract LayoutParams generateDefaultLayoutParams();
        
        public LayoutParams generateLayoutParams(final Context context, final AttributeSet set) {
            return new LayoutParams(context, set);
        }
        
        public LayoutParams generateLayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            if (viewGroup$LayoutParams instanceof LayoutParams) {
                return new LayoutParams((LayoutParams)viewGroup$LayoutParams);
            }
            if (viewGroup$LayoutParams instanceof ViewGroup$MarginLayoutParams) {
                return new LayoutParams((ViewGroup$MarginLayoutParams)viewGroup$LayoutParams);
            }
            return new LayoutParams(viewGroup$LayoutParams);
        }
        
        public int getBaseline() {
            return -1;
        }
        
        public int getBottomDecorationHeight(@NonNull final View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.bottom;
        }
        
        @Nullable
        public View getChildAt(final int n) {
            View child;
            if (this.mChildHelper != null) {
                child = this.mChildHelper.getChildAt(n);
            }
            else {
                child = null;
            }
            return child;
        }
        
        public int getChildCount() {
            int childCount;
            if (this.mChildHelper != null) {
                childCount = this.mChildHelper.getChildCount();
            }
            else {
                childCount = 0;
            }
            return childCount;
        }
        
        public boolean getClipToPadding() {
            return this.mRecyclerView != null && this.mRecyclerView.mClipToPadding;
        }
        
        public int getColumnCountForAccessibility(@NonNull final Recycler recycler, @NonNull final State state) {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            int itemCount = 1;
            if (mRecyclerView != null && this.mRecyclerView.mAdapter != null) {
                if (this.canScrollHorizontally()) {
                    itemCount = this.mRecyclerView.mAdapter.getItemCount();
                }
                return itemCount;
            }
            return 1;
        }
        
        public int getDecoratedBottom(@NonNull final View view) {
            return view.getBottom() + this.getBottomDecorationHeight(view);
        }
        
        public void getDecoratedBoundsWithMargins(@NonNull final View view, @NonNull final Rect rect) {
            RecyclerView.getDecoratedBoundsWithMarginsInt(view, rect);
        }
        
        public int getDecoratedLeft(@NonNull final View view) {
            return view.getLeft() - this.getLeftDecorationWidth(view);
        }
        
        public int getDecoratedMeasuredHeight(@NonNull final View view) {
            final Rect mDecorInsets = ((LayoutParams)view.getLayoutParams()).mDecorInsets;
            return view.getMeasuredHeight() + mDecorInsets.top + mDecorInsets.bottom;
        }
        
        public int getDecoratedMeasuredWidth(@NonNull final View view) {
            final Rect mDecorInsets = ((LayoutParams)view.getLayoutParams()).mDecorInsets;
            return view.getMeasuredWidth() + mDecorInsets.left + mDecorInsets.right;
        }
        
        public int getDecoratedRight(@NonNull final View view) {
            return view.getRight() + this.getRightDecorationWidth(view);
        }
        
        public int getDecoratedTop(@NonNull final View view) {
            return view.getTop() - this.getTopDecorationHeight(view);
        }
        
        @Nullable
        public View getFocusedChild() {
            if (this.mRecyclerView == null) {
                return null;
            }
            final View focusedChild = this.mRecyclerView.getFocusedChild();
            if (focusedChild != null && !this.mChildHelper.isHidden(focusedChild)) {
                return focusedChild;
            }
            return null;
        }
        
        @Px
        public int getHeight() {
            return this.mHeight;
        }
        
        public int getHeightMode() {
            return this.mHeightMode;
        }
        
        public int getItemCount() {
            Adapter adapter;
            if (this.mRecyclerView != null) {
                adapter = this.mRecyclerView.getAdapter();
            }
            else {
                adapter = null;
            }
            int itemCount;
            if (adapter != null) {
                itemCount = adapter.getItemCount();
            }
            else {
                itemCount = 0;
            }
            return itemCount;
        }
        
        public int getItemViewType(@NonNull final View view) {
            return RecyclerView.getChildViewHolderInt(view).getItemViewType();
        }
        
        public int getLayoutDirection() {
            return ViewCompat.getLayoutDirection((View)this.mRecyclerView);
        }
        
        public int getLeftDecorationWidth(@NonNull final View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.left;
        }
        
        @Px
        public int getMinimumHeight() {
            return ViewCompat.getMinimumHeight((View)this.mRecyclerView);
        }
        
        @Px
        public int getMinimumWidth() {
            return ViewCompat.getMinimumWidth((View)this.mRecyclerView);
        }
        
        @Px
        public int getPaddingBottom() {
            int paddingBottom;
            if (this.mRecyclerView != null) {
                paddingBottom = this.mRecyclerView.getPaddingBottom();
            }
            else {
                paddingBottom = 0;
            }
            return paddingBottom;
        }
        
        @Px
        public int getPaddingEnd() {
            int paddingEnd;
            if (this.mRecyclerView != null) {
                paddingEnd = ViewCompat.getPaddingEnd((View)this.mRecyclerView);
            }
            else {
                paddingEnd = 0;
            }
            return paddingEnd;
        }
        
        @Px
        public int getPaddingLeft() {
            int paddingLeft;
            if (this.mRecyclerView != null) {
                paddingLeft = this.mRecyclerView.getPaddingLeft();
            }
            else {
                paddingLeft = 0;
            }
            return paddingLeft;
        }
        
        @Px
        public int getPaddingRight() {
            int paddingRight;
            if (this.mRecyclerView != null) {
                paddingRight = this.mRecyclerView.getPaddingRight();
            }
            else {
                paddingRight = 0;
            }
            return paddingRight;
        }
        
        @Px
        public int getPaddingStart() {
            int paddingStart;
            if (this.mRecyclerView != null) {
                paddingStart = ViewCompat.getPaddingStart((View)this.mRecyclerView);
            }
            else {
                paddingStart = 0;
            }
            return paddingStart;
        }
        
        @Px
        public int getPaddingTop() {
            int paddingTop;
            if (this.mRecyclerView != null) {
                paddingTop = this.mRecyclerView.getPaddingTop();
            }
            else {
                paddingTop = 0;
            }
            return paddingTop;
        }
        
        public int getPosition(@NonNull final View view) {
            return ((LayoutParams)view.getLayoutParams()).getViewLayoutPosition();
        }
        
        public int getRightDecorationWidth(@NonNull final View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.right;
        }
        
        public int getRowCountForAccessibility(@NonNull final Recycler recycler, @NonNull final State state) {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            int itemCount = 1;
            if (mRecyclerView != null && this.mRecyclerView.mAdapter != null) {
                if (this.canScrollVertically()) {
                    itemCount = this.mRecyclerView.mAdapter.getItemCount();
                }
                return itemCount;
            }
            return 1;
        }
        
        public int getSelectionModeForAccessibility(@NonNull final Recycler recycler, @NonNull final State state) {
            return 0;
        }
        
        public int getTopDecorationHeight(@NonNull final View view) {
            return ((LayoutParams)view.getLayoutParams()).mDecorInsets.top;
        }
        
        public void getTransformedBoundingBox(@NonNull final View view, final boolean b, @NonNull final Rect rect) {
            if (b) {
                final Rect mDecorInsets = ((LayoutParams)view.getLayoutParams()).mDecorInsets;
                rect.set(-mDecorInsets.left, -mDecorInsets.top, view.getWidth() + mDecorInsets.right, view.getHeight() + mDecorInsets.bottom);
            }
            else {
                rect.set(0, 0, view.getWidth(), view.getHeight());
            }
            if (this.mRecyclerView != null) {
                final Matrix matrix = view.getMatrix();
                if (matrix != null && !matrix.isIdentity()) {
                    final RectF mTempRectF = this.mRecyclerView.mTempRectF;
                    mTempRectF.set(rect);
                    matrix.mapRect(mTempRectF);
                    rect.set((int)Math.floor(mTempRectF.left), (int)Math.floor(mTempRectF.top), (int)Math.ceil(mTempRectF.right), (int)Math.ceil(mTempRectF.bottom));
                }
            }
            rect.offset(view.getLeft(), view.getTop());
        }
        
        @Px
        public int getWidth() {
            return this.mWidth;
        }
        
        public int getWidthMode() {
            return this.mWidthMode;
        }
        
        boolean hasFlexibleChildInBothOrientations() {
            for (int childCount = this.getChildCount(), i = 0; i < childCount; ++i) {
                final ViewGroup$LayoutParams layoutParams = this.getChildAt(i).getLayoutParams();
                if (layoutParams.width < 0 && layoutParams.height < 0) {
                    return true;
                }
            }
            return false;
        }
        
        public boolean hasFocus() {
            return this.mRecyclerView != null && this.mRecyclerView.hasFocus();
        }
        
        public void ignoreView(@NonNull final View view) {
            if (view.getParent() == this.mRecyclerView && this.mRecyclerView.indexOfChild(view) != -1) {
                final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
                childViewHolderInt.addFlags(128);
                this.mRecyclerView.mViewInfoStore.removeViewHolder(childViewHolderInt);
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("View should be fully attached to be ignored");
            sb.append(this.mRecyclerView.exceptionLabel());
            throw new IllegalArgumentException(sb.toString());
        }
        
        public boolean isAttachedToWindow() {
            return this.mIsAttachedToWindow;
        }
        
        public boolean isAutoMeasureEnabled() {
            return this.mAutoMeasure;
        }
        
        public boolean isFocused() {
            return this.mRecyclerView != null && this.mRecyclerView.isFocused();
        }
        
        public final boolean isItemPrefetchEnabled() {
            return this.mItemPrefetchEnabled;
        }
        
        public boolean isLayoutHierarchical(@NonNull final Recycler recycler, @NonNull final State state) {
            return false;
        }
        
        public boolean isMeasurementCacheEnabled() {
            return this.mMeasurementCacheEnabled;
        }
        
        public boolean isSmoothScrolling() {
            return this.mSmoothScroller != null && this.mSmoothScroller.isRunning();
        }
        
        public boolean isViewPartiallyVisible(@NonNull final View view, final boolean b, final boolean b2) {
            final boolean b3 = this.mHorizontalBoundCheck.isViewWithinBoundFlags(view, 24579) && this.mVerticalBoundCheck.isViewWithinBoundFlags(view, 24579);
            if (b) {
                return b3;
            }
            return b3 ^ true;
        }
        
        public void layoutDecorated(@NonNull final View view, final int n, final int n2, final int n3, final int n4) {
            final Rect mDecorInsets = ((LayoutParams)view.getLayoutParams()).mDecorInsets;
            view.layout(n + mDecorInsets.left, n2 + mDecorInsets.top, n3 - mDecorInsets.right, n4 - mDecorInsets.bottom);
        }
        
        public void layoutDecoratedWithMargins(@NonNull final View view, final int n, final int n2, final int n3, final int n4) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            final Rect mDecorInsets = layoutParams.mDecorInsets;
            view.layout(n + mDecorInsets.left + layoutParams.leftMargin, n2 + mDecorInsets.top + layoutParams.topMargin, n3 - mDecorInsets.right - layoutParams.rightMargin, n4 - mDecorInsets.bottom - layoutParams.bottomMargin);
        }
        
        public void measureChild(@NonNull final View view, int childMeasureSpec, int childMeasureSpec2) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            final Rect itemDecorInsetsForChild = this.mRecyclerView.getItemDecorInsetsForChild(view);
            final int left = itemDecorInsetsForChild.left;
            final int right = itemDecorInsetsForChild.right;
            final int top = itemDecorInsetsForChild.top;
            final int bottom = itemDecorInsetsForChild.bottom;
            childMeasureSpec = getChildMeasureSpec(this.getWidth(), this.getWidthMode(), this.getPaddingLeft() + this.getPaddingRight() + (childMeasureSpec + (left + right)), layoutParams.width, this.canScrollHorizontally());
            childMeasureSpec2 = getChildMeasureSpec(this.getHeight(), this.getHeightMode(), this.getPaddingTop() + this.getPaddingBottom() + (childMeasureSpec2 + (top + bottom)), layoutParams.height, this.canScrollVertically());
            if (this.shouldMeasureChild(view, childMeasureSpec, childMeasureSpec2, layoutParams)) {
                view.measure(childMeasureSpec, childMeasureSpec2);
            }
        }
        
        public void measureChildWithMargins(@NonNull final View view, int childMeasureSpec, int childMeasureSpec2) {
            final LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            final Rect itemDecorInsetsForChild = this.mRecyclerView.getItemDecorInsetsForChild(view);
            final int left = itemDecorInsetsForChild.left;
            final int right = itemDecorInsetsForChild.right;
            final int top = itemDecorInsetsForChild.top;
            final int bottom = itemDecorInsetsForChild.bottom;
            childMeasureSpec = getChildMeasureSpec(this.getWidth(), this.getWidthMode(), this.getPaddingLeft() + this.getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin + (childMeasureSpec + (left + right)), layoutParams.width, this.canScrollHorizontally());
            childMeasureSpec2 = getChildMeasureSpec(this.getHeight(), this.getHeightMode(), this.getPaddingTop() + this.getPaddingBottom() + layoutParams.topMargin + layoutParams.bottomMargin + (childMeasureSpec2 + (top + bottom)), layoutParams.height, this.canScrollVertically());
            if (this.shouldMeasureChild(view, childMeasureSpec, childMeasureSpec2, layoutParams)) {
                view.measure(childMeasureSpec, childMeasureSpec2);
            }
        }
        
        public void moveView(final int i, final int n) {
            final View child = this.getChildAt(i);
            if (child == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Cannot move a child from non-existing index:");
                sb.append(i);
                sb.append(this.mRecyclerView.toString());
                throw new IllegalArgumentException(sb.toString());
            }
            this.detachViewAt(i);
            this.attachView(child, n);
        }
        
        public void offsetChildrenHorizontal(@Px final int n) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.offsetChildrenHorizontal(n);
            }
        }
        
        public void offsetChildrenVertical(@Px final int n) {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.offsetChildrenVertical(n);
            }
        }
        
        public void onAdapterChanged(@Nullable final Adapter adapter, @Nullable final Adapter adapter2) {
        }
        
        public boolean onAddFocusables(@NonNull final RecyclerView recyclerView, @NonNull final ArrayList<View> list, final int n, final int n2) {
            return false;
        }
        
        @CallSuper
        public void onAttachedToWindow(final RecyclerView recyclerView) {
        }
        
        @Deprecated
        public void onDetachedFromWindow(final RecyclerView recyclerView) {
        }
        
        @CallSuper
        public void onDetachedFromWindow(final RecyclerView recyclerView, final Recycler recycler) {
            this.onDetachedFromWindow(recyclerView);
        }
        
        @Nullable
        public View onFocusSearchFailed(@NonNull final View view, final int n, @NonNull final Recycler recycler, @NonNull final State state) {
            return null;
        }
        
        public void onInitializeAccessibilityEvent(@NonNull final Recycler recycler, @NonNull final State state, @NonNull final AccessibilityEvent accessibilityEvent) {
            if (this.mRecyclerView != null && accessibilityEvent != null) {
                final RecyclerView mRecyclerView = this.mRecyclerView;
                boolean scrollable;
                final boolean b = scrollable = true;
                if (!mRecyclerView.canScrollVertically(1)) {
                    scrollable = b;
                    if (!this.mRecyclerView.canScrollVertically(-1)) {
                        scrollable = b;
                        if (!this.mRecyclerView.canScrollHorizontally(-1)) {
                            scrollable = (this.mRecyclerView.canScrollHorizontally(1) && b);
                        }
                    }
                }
                accessibilityEvent.setScrollable(scrollable);
                if (this.mRecyclerView.mAdapter != null) {
                    accessibilityEvent.setItemCount(this.mRecyclerView.mAdapter.getItemCount());
                }
            }
        }
        
        public void onInitializeAccessibilityEvent(@NonNull final AccessibilityEvent accessibilityEvent) {
            this.onInitializeAccessibilityEvent(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, accessibilityEvent);
        }
        
        void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            this.onInitializeAccessibilityNodeInfo(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, accessibilityNodeInfoCompat);
        }
        
        public void onInitializeAccessibilityNodeInfo(@NonNull final Recycler recycler, @NonNull final State state, @NonNull final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (this.mRecyclerView.canScrollVertically(-1) || this.mRecyclerView.canScrollHorizontally(-1)) {
                accessibilityNodeInfoCompat.addAction(8192);
                accessibilityNodeInfoCompat.setScrollable(true);
            }
            if (this.mRecyclerView.canScrollVertically(1) || this.mRecyclerView.canScrollHorizontally(1)) {
                accessibilityNodeInfoCompat.addAction(4096);
                accessibilityNodeInfoCompat.setScrollable(true);
            }
            accessibilityNodeInfoCompat.setCollectionInfo(AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(this.getRowCountForAccessibility(recycler, state), this.getColumnCountForAccessibility(recycler, state), this.isLayoutHierarchical(recycler, state), this.getSelectionModeForAccessibility(recycler, state)));
        }
        
        public void onInitializeAccessibilityNodeInfoForItem(@NonNull final Recycler recycler, @NonNull final State state, @NonNull final View view, @NonNull final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            final boolean canScrollVertically = this.canScrollVertically();
            int position = 0;
            int position2;
            if (canScrollVertically) {
                position2 = this.getPosition(view);
            }
            else {
                position2 = 0;
            }
            if (this.canScrollHorizontally()) {
                position = this.getPosition(view);
            }
            accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(position2, 1, position, 1, false, false));
        }
        
        void onInitializeAccessibilityNodeInfoForItem(final View view, final AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (childViewHolderInt != null && !childViewHolderInt.isRemoved() && !this.mChildHelper.isHidden(childViewHolderInt.itemView)) {
                this.onInitializeAccessibilityNodeInfoForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, view, accessibilityNodeInfoCompat);
            }
        }
        
        @Nullable
        public View onInterceptFocusSearch(@NonNull final View view, final int n) {
            return null;
        }
        
        public void onItemsAdded(@NonNull final RecyclerView recyclerView, final int n, final int n2) {
        }
        
        public void onItemsChanged(@NonNull final RecyclerView recyclerView) {
        }
        
        public void onItemsMoved(@NonNull final RecyclerView recyclerView, final int n, final int n2, final int n3) {
        }
        
        public void onItemsRemoved(@NonNull final RecyclerView recyclerView, final int n, final int n2) {
        }
        
        public void onItemsUpdated(@NonNull final RecyclerView recyclerView, final int n, final int n2) {
        }
        
        public void onItemsUpdated(@NonNull final RecyclerView recyclerView, final int n, final int n2, @Nullable final Object o) {
            this.onItemsUpdated(recyclerView, n, n2);
        }
        
        public void onLayoutChildren(final Recycler recycler, final State state) {
            Log.e("RecyclerView", "You must override onLayoutChildren(Recycler recycler, State state) ");
        }
        
        public void onLayoutCompleted(final State state) {
        }
        
        public void onMeasure(@NonNull final Recycler recycler, @NonNull final State state, final int n, final int n2) {
            this.mRecyclerView.defaultOnMeasure(n, n2);
        }
        
        public boolean onRequestChildFocus(@NonNull final RecyclerView recyclerView, @NonNull final State state, @NonNull final View view, @Nullable final View view2) {
            return this.onRequestChildFocus(recyclerView, view, view2);
        }
        
        @Deprecated
        public boolean onRequestChildFocus(@NonNull final RecyclerView recyclerView, @NonNull final View view, @Nullable final View view2) {
            return this.isSmoothScrolling() || recyclerView.isComputingLayout();
        }
        
        public void onRestoreInstanceState(final Parcelable parcelable) {
        }
        
        @Nullable
        public Parcelable onSaveInstanceState() {
            return null;
        }
        
        public void onScrollStateChanged(final int n) {
        }
        
        void onSmoothScrollerStopped(final SmoothScroller smoothScroller) {
            if (this.mSmoothScroller == smoothScroller) {
                this.mSmoothScroller = null;
            }
        }
        
        boolean performAccessibilityAction(final int n, @Nullable final Bundle bundle) {
            return this.performAccessibilityAction(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, n, bundle);
        }
        
        public boolean performAccessibilityAction(@NonNull final Recycler recycler, @NonNull final State state, int n, @Nullable final Bundle bundle) {
            if (this.mRecyclerView == null) {
                return false;
            }
            int n2 = 0;
            Label_0167: {
                int n3;
                if (n != 4096) {
                    if (n != 8192) {
                        n = 0;
                        n2 = 0;
                        break Label_0167;
                    }
                    if (this.mRecyclerView.canScrollVertically(-1)) {
                        n = -(this.getHeight() - this.getPaddingTop() - this.getPaddingBottom());
                    }
                    else {
                        n = 0;
                    }
                    n3 = n;
                    if (this.mRecyclerView.canScrollHorizontally(-1)) {
                        n2 = -(this.getWidth() - this.getPaddingLeft() - this.getPaddingRight());
                        break Label_0167;
                    }
                }
                else {
                    if (this.mRecyclerView.canScrollVertically(1)) {
                        n = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                    }
                    else {
                        n = 0;
                    }
                    n3 = n;
                    if (this.mRecyclerView.canScrollHorizontally(1)) {
                        n2 = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
                        break Label_0167;
                    }
                }
                n2 = 0;
                n = n3;
            }
            if (n == 0 && n2 == 0) {
                return false;
            }
            this.mRecyclerView.smoothScrollBy(n2, n);
            return true;
        }
        
        public boolean performAccessibilityActionForItem(@NonNull final Recycler recycler, @NonNull final State state, @NonNull final View view, final int n, @Nullable final Bundle bundle) {
            return false;
        }
        
        boolean performAccessibilityActionForItem(@NonNull final View view, final int n, @Nullable final Bundle bundle) {
            return this.performAccessibilityActionForItem(this.mRecyclerView.mRecycler, this.mRecyclerView.mState, view, n, bundle);
        }
        
        public void postOnAnimation(final Runnable runnable) {
            if (this.mRecyclerView != null) {
                ViewCompat.postOnAnimation((View)this.mRecyclerView, runnable);
            }
        }
        
        public void removeAllViews() {
            for (int i = this.getChildCount() - 1; i >= 0; --i) {
                this.mChildHelper.removeViewAt(i);
            }
        }
        
        public void removeAndRecycleAllViews(@NonNull final Recycler recycler) {
            for (int i = this.getChildCount() - 1; i >= 0; --i) {
                if (!RecyclerView.getChildViewHolderInt(this.getChildAt(i)).shouldIgnore()) {
                    this.removeAndRecycleViewAt(i, recycler);
                }
            }
        }
        
        void removeAndRecycleScrapInt(final Recycler recycler) {
            final int scrapCount = recycler.getScrapCount();
            for (int i = scrapCount - 1; i >= 0; --i) {
                final View scrapView = recycler.getScrapViewAt(i);
                final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(scrapView);
                if (!childViewHolderInt.shouldIgnore()) {
                    childViewHolderInt.setIsRecyclable(false);
                    if (childViewHolderInt.isTmpDetached()) {
                        this.mRecyclerView.removeDetachedView(scrapView, false);
                    }
                    if (this.mRecyclerView.mItemAnimator != null) {
                        this.mRecyclerView.mItemAnimator.endAnimation(childViewHolderInt);
                    }
                    childViewHolderInt.setIsRecyclable(true);
                    recycler.quickRecycleScrapView(scrapView);
                }
            }
            recycler.clearScrap();
            if (scrapCount > 0) {
                this.mRecyclerView.invalidate();
            }
        }
        
        public void removeAndRecycleView(@NonNull final View view, @NonNull final Recycler recycler) {
            this.removeView(view);
            recycler.recycleView(view);
        }
        
        public void removeAndRecycleViewAt(final int n, @NonNull final Recycler recycler) {
            final View child = this.getChildAt(n);
            this.removeViewAt(n);
            recycler.recycleView(child);
        }
        
        public boolean removeCallbacks(final Runnable runnable) {
            return this.mRecyclerView != null && this.mRecyclerView.removeCallbacks(runnable);
        }
        
        public void removeDetachedView(@NonNull final View view) {
            this.mRecyclerView.removeDetachedView(view, false);
        }
        
        public void removeView(final View view) {
            this.mChildHelper.removeView(view);
        }
        
        public void removeViewAt(final int n) {
            if (this.getChildAt(n) != null) {
                this.mChildHelper.removeViewAt(n);
            }
        }
        
        public boolean requestChildRectangleOnScreen(@NonNull final RecyclerView recyclerView, @NonNull final View view, @NonNull final Rect rect, final boolean b) {
            return this.requestChildRectangleOnScreen(recyclerView, view, rect, b, false);
        }
        
        public boolean requestChildRectangleOnScreen(@NonNull final RecyclerView recyclerView, @NonNull final View view, @NonNull final Rect rect, final boolean b, final boolean b2) {
            final int[] childRectangleOnScreenScrollAmount = this.getChildRectangleOnScreenScrollAmount(recyclerView, view, rect, b);
            final int n = childRectangleOnScreenScrollAmount[0];
            final int n2 = childRectangleOnScreenScrollAmount[1];
            if ((!b2 || this.isFocusedChildVisibleAfterScrolling(recyclerView, n, n2)) && (n != 0 || n2 != 0)) {
                if (b) {
                    recyclerView.scrollBy(n, n2);
                }
                else {
                    recyclerView.smoothScrollBy(n, n2);
                }
                return true;
            }
            return false;
        }
        
        public void requestLayout() {
            if (this.mRecyclerView != null) {
                this.mRecyclerView.requestLayout();
            }
        }
        
        public void requestSimpleAnimationsInNextLayout() {
            this.mRequestedSimpleAnimations = true;
        }
        
        public int scrollHorizontallyBy(final int n, final Recycler recycler, final State state) {
            return 0;
        }
        
        public void scrollToPosition(final int n) {
        }
        
        public int scrollVerticallyBy(final int n, final Recycler recycler, final State state) {
            return 0;
        }
        
        @Deprecated
        public void setAutoMeasureEnabled(final boolean mAutoMeasure) {
            this.mAutoMeasure = mAutoMeasure;
        }
        
        void setExactMeasureSpecsFrom(final RecyclerView recyclerView) {
            this.setMeasureSpecs(View$MeasureSpec.makeMeasureSpec(recyclerView.getWidth(), 1073741824), View$MeasureSpec.makeMeasureSpec(recyclerView.getHeight(), 1073741824));
        }
        
        public final void setItemPrefetchEnabled(final boolean mItemPrefetchEnabled) {
            if (mItemPrefetchEnabled != this.mItemPrefetchEnabled) {
                this.mItemPrefetchEnabled = mItemPrefetchEnabled;
                this.mPrefetchMaxCountObserved = 0;
                if (this.mRecyclerView != null) {
                    this.mRecyclerView.mRecycler.updateViewCacheSize();
                }
            }
        }
        
        void setMeasureSpecs(final int n, final int n2) {
            this.mWidth = View$MeasureSpec.getSize(n);
            this.mWidthMode = View$MeasureSpec.getMode(n);
            if (this.mWidthMode == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC) {
                this.mWidth = 0;
            }
            this.mHeight = View$MeasureSpec.getSize(n2);
            this.mHeightMode = View$MeasureSpec.getMode(n2);
            if (this.mHeightMode == 0 && !RecyclerView.ALLOW_SIZE_IN_UNSPECIFIED_SPEC) {
                this.mHeight = 0;
            }
        }
        
        public void setMeasuredDimension(final int n, final int n2) {
            RecyclerView.access$300(this.mRecyclerView, n, n2);
        }
        
        public void setMeasuredDimension(final Rect rect, final int n, final int n2) {
            this.setMeasuredDimension(chooseSize(n, rect.width() + this.getPaddingLeft() + this.getPaddingRight(), this.getMinimumWidth()), chooseSize(n2, rect.height() + this.getPaddingTop() + this.getPaddingBottom(), this.getMinimumHeight()));
        }
        
        void setMeasuredDimensionFromChildren(final int n, final int n2) {
            final int childCount = this.getChildCount();
            if (childCount == 0) {
                this.mRecyclerView.defaultOnMeasure(n, n2);
                return;
            }
            int i = 0;
            int n3 = Integer.MAX_VALUE;
            int n4 = Integer.MIN_VALUE;
            int n5 = Integer.MIN_VALUE;
            int n6 = Integer.MAX_VALUE;
            while (i < childCount) {
                final View child = this.getChildAt(i);
                final Rect mTempRect = this.mRecyclerView.mTempRect;
                this.getDecoratedBoundsWithMargins(child, mTempRect);
                int left;
                if (mTempRect.left < (left = n3)) {
                    left = mTempRect.left;
                }
                int right;
                if (mTempRect.right > (right = n4)) {
                    right = mTempRect.right;
                }
                int top;
                if (mTempRect.top < (top = n6)) {
                    top = mTempRect.top;
                }
                int bottom;
                if (mTempRect.bottom > (bottom = n5)) {
                    bottom = mTempRect.bottom;
                }
                ++i;
                n6 = top;
                n3 = left;
                n4 = right;
                n5 = bottom;
            }
            this.mRecyclerView.mTempRect.set(n3, n6, n4, n5);
            this.setMeasuredDimension(this.mRecyclerView.mTempRect, n, n2);
        }
        
        public void setMeasurementCacheEnabled(final boolean mMeasurementCacheEnabled) {
            this.mMeasurementCacheEnabled = mMeasurementCacheEnabled;
        }
        
        void setRecyclerView(final RecyclerView mRecyclerView) {
            if (mRecyclerView == null) {
                this.mRecyclerView = null;
                this.mChildHelper = null;
                this.mWidth = 0;
                this.mHeight = 0;
            }
            else {
                this.mRecyclerView = mRecyclerView;
                this.mChildHelper = mRecyclerView.mChildHelper;
                this.mWidth = mRecyclerView.getWidth();
                this.mHeight = mRecyclerView.getHeight();
            }
            this.mWidthMode = 1073741824;
            this.mHeightMode = 1073741824;
        }
        
        boolean shouldMeasureChild(final View view, final int n, final int n2, final LayoutParams layoutParams) {
            return view.isLayoutRequested() || !this.mMeasurementCacheEnabled || !isMeasurementUpToDate(view.getWidth(), n, layoutParams.width) || !isMeasurementUpToDate(view.getHeight(), n2, layoutParams.height);
        }
        
        boolean shouldMeasureTwice() {
            return false;
        }
        
        boolean shouldReMeasureChild(final View view, final int n, final int n2, final LayoutParams layoutParams) {
            return !this.mMeasurementCacheEnabled || !isMeasurementUpToDate(view.getMeasuredWidth(), n, layoutParams.width) || !isMeasurementUpToDate(view.getMeasuredHeight(), n2, layoutParams.height);
        }
        
        public void smoothScrollToPosition(final RecyclerView recyclerView, final State state, final int n) {
            Log.e("RecyclerView", "You must override smoothScrollToPosition to support smooth scrolling");
        }
        
        public void startSmoothScroll(final SmoothScroller mSmoothScroller) {
            if (this.mSmoothScroller != null && mSmoothScroller != this.mSmoothScroller && this.mSmoothScroller.isRunning()) {
                this.mSmoothScroller.stop();
            }
            (this.mSmoothScroller = mSmoothScroller).start(this.mRecyclerView, this);
        }
        
        public void stopIgnoringView(@NonNull final View view) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            childViewHolderInt.stopIgnoring();
            childViewHolderInt.resetInternal();
            childViewHolderInt.addFlags(4);
        }
        
        void stopSmoothScroller() {
            if (this.mSmoothScroller != null) {
                this.mSmoothScroller.stop();
            }
        }
        
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
        
        public interface LayoutPrefetchRegistry
        {
            void addPosition(final int p0, final int p1);
        }
        
        public static class Properties
        {
            public int orientation;
            public boolean reverseLayout;
            public int spanCount;
            public boolean stackFromEnd;
        }
    }
    
    public static class LayoutParams extends ViewGroup$MarginLayoutParams
    {
        final Rect mDecorInsets;
        boolean mInsetsDirty;
        boolean mPendingInvalidate;
        ViewHolder mViewHolder;
        
        public LayoutParams(final int n, final int n2) {
            super(n, n2);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }
        
        public LayoutParams(final Context context, final AttributeSet set) {
            super(context, set);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }
        
        public LayoutParams(final LayoutParams layoutParams) {
            super((ViewGroup$LayoutParams)layoutParams);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }
        
        public LayoutParams(final ViewGroup$LayoutParams viewGroup$LayoutParams) {
            super(viewGroup$LayoutParams);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }
        
        public LayoutParams(final ViewGroup$MarginLayoutParams viewGroup$MarginLayoutParams) {
            super(viewGroup$MarginLayoutParams);
            this.mDecorInsets = new Rect();
            this.mInsetsDirty = true;
            this.mPendingInvalidate = false;
        }
        
        public int getViewAdapterPosition() {
            return this.mViewHolder.getAdapterPosition();
        }
        
        public int getViewLayoutPosition() {
            return this.mViewHolder.getLayoutPosition();
        }
        
        @Deprecated
        public int getViewPosition() {
            return this.mViewHolder.getPosition();
        }
        
        public boolean isItemChanged() {
            return this.mViewHolder.isUpdated();
        }
        
        public boolean isItemRemoved() {
            return this.mViewHolder.isRemoved();
        }
        
        public boolean isViewInvalid() {
            return this.mViewHolder.isInvalid();
        }
        
        public boolean viewNeedsUpdate() {
            return this.mViewHolder.needsUpdate();
        }
    }
    
    public interface OnChildAttachStateChangeListener
    {
        void onChildViewAttachedToWindow(@NonNull final View p0);
        
        void onChildViewDetachedFromWindow(@NonNull final View p0);
    }
    
    public abstract static class OnFlingListener
    {
        public abstract boolean onFling(final int p0, final int p1);
    }
    
    public interface OnItemTouchListener
    {
        boolean onInterceptTouchEvent(@NonNull final RecyclerView p0, @NonNull final MotionEvent p1);
        
        void onRequestDisallowInterceptTouchEvent(final boolean p0);
        
        void onTouchEvent(@NonNull final RecyclerView p0, @NonNull final MotionEvent p1);
    }
    
    public abstract static class OnScrollListener
    {
        public void onScrollStateChanged(@NonNull final RecyclerView recyclerView, final int n) {
        }
        
        public void onScrolled(@NonNull final RecyclerView recyclerView, final int n, final int n2) {
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface Orientation {
    }
    
    public static class RecycledViewPool
    {
        private static final int DEFAULT_MAX_SCRAP = 5;
        private int mAttachCount;
        SparseArray<ScrapData> mScrap;
        
        public RecycledViewPool() {
            this.mScrap = (SparseArray<ScrapData>)new SparseArray();
            this.mAttachCount = 0;
        }
        
        private ScrapData getScrapDataForType(final int n) {
            ScrapData scrapData;
            if ((scrapData = (ScrapData)this.mScrap.get(n)) == null) {
                scrapData = new ScrapData();
                this.mScrap.put(n, (Object)scrapData);
            }
            return scrapData;
        }
        
        void attach() {
            ++this.mAttachCount;
        }
        
        public void clear() {
            for (int i = 0; i < this.mScrap.size(); ++i) {
                ((ScrapData)this.mScrap.valueAt(i)).mScrapHeap.clear();
            }
        }
        
        void detach() {
            --this.mAttachCount;
        }
        
        void factorInBindTime(final int n, final long n2) {
            final ScrapData scrapDataForType = this.getScrapDataForType(n);
            scrapDataForType.mBindRunningAverageNs = this.runningAverage(scrapDataForType.mBindRunningAverageNs, n2);
        }
        
        void factorInCreateTime(final int n, final long n2) {
            final ScrapData scrapDataForType = this.getScrapDataForType(n);
            scrapDataForType.mCreateRunningAverageNs = this.runningAverage(scrapDataForType.mCreateRunningAverageNs, n2);
        }
        
        @Nullable
        public ViewHolder getRecycledView(final int n) {
            final ScrapData scrapData = (ScrapData)this.mScrap.get(n);
            if (scrapData != null && !scrapData.mScrapHeap.isEmpty()) {
                final ArrayList<ViewHolder> mScrapHeap = scrapData.mScrapHeap;
                return mScrapHeap.remove(mScrapHeap.size() - 1);
            }
            return null;
        }
        
        public int getRecycledViewCount(final int n) {
            return this.getScrapDataForType(n).mScrapHeap.size();
        }
        
        void onAdapterChanged(final Adapter adapter, final Adapter adapter2, final boolean b) {
            if (adapter != null) {
                this.detach();
            }
            if (!b && this.mAttachCount == 0) {
                this.clear();
            }
            if (adapter2 != null) {
                this.attach();
            }
        }
        
        public void putRecycledView(final ViewHolder e) {
            final int itemViewType = e.getItemViewType();
            final ArrayList<ViewHolder> mScrapHeap = this.getScrapDataForType(itemViewType).mScrapHeap;
            if (((ScrapData)this.mScrap.get(itemViewType)).mMaxScrap <= mScrapHeap.size()) {
                return;
            }
            e.resetInternal();
            mScrapHeap.add(e);
        }
        
        long runningAverage(final long n, final long n2) {
            if (n == 0L) {
                return n2;
            }
            return n / 4L * 3L + n2 / 4L;
        }
        
        public void setMaxRecycledViews(final int n, final int mMaxScrap) {
            final ScrapData scrapDataForType = this.getScrapDataForType(n);
            scrapDataForType.mMaxScrap = mMaxScrap;
            final ArrayList<ViewHolder> mScrapHeap = scrapDataForType.mScrapHeap;
            while (mScrapHeap.size() > mMaxScrap) {
                mScrapHeap.remove(mScrapHeap.size() - 1);
            }
        }
        
        int size() {
            int i = 0;
            int n = 0;
            while (i < this.mScrap.size()) {
                final ArrayList<ViewHolder> mScrapHeap = ((ScrapData)this.mScrap.valueAt(i)).mScrapHeap;
                int n2 = n;
                if (mScrapHeap != null) {
                    n2 = n + mScrapHeap.size();
                }
                ++i;
                n = n2;
            }
            return n;
        }
        
        boolean willBindInTime(final int n, final long n2, final long n3) {
            final long mBindRunningAverageNs = this.getScrapDataForType(n).mBindRunningAverageNs;
            return mBindRunningAverageNs == 0L || n2 + mBindRunningAverageNs < n3;
        }
        
        boolean willCreateInTime(final int n, final long n2, final long n3) {
            final long mCreateRunningAverageNs = this.getScrapDataForType(n).mCreateRunningAverageNs;
            return mCreateRunningAverageNs == 0L || n2 + mCreateRunningAverageNs < n3;
        }
        
        static class ScrapData
        {
            long mBindRunningAverageNs;
            long mCreateRunningAverageNs;
            int mMaxScrap;
            final ArrayList<ViewHolder> mScrapHeap;
            
            ScrapData() {
                this.mScrapHeap = new ArrayList<ViewHolder>();
                this.mMaxScrap = 5;
                this.mCreateRunningAverageNs = 0L;
                this.mBindRunningAverageNs = 0L;
            }
        }
    }
    
    public final class Recycler
    {
        static final int DEFAULT_CACHE_SIZE = 2;
        final ArrayList<ViewHolder> mAttachedScrap;
        final ArrayList<ViewHolder> mCachedViews;
        ArrayList<ViewHolder> mChangedScrap;
        RecycledViewPool mRecyclerPool;
        private int mRequestedCacheMax;
        private final List<ViewHolder> mUnmodifiableAttachedScrap;
        private ViewCacheExtension mViewCacheExtension;
        int mViewCacheMax;
        final RecyclerView this$0;
        
        public Recycler(final RecyclerView this$0) {
            this.this$0 = this$0;
            this.mAttachedScrap = new ArrayList<ViewHolder>();
            this.mChangedScrap = null;
            this.mCachedViews = new ArrayList<ViewHolder>();
            this.mUnmodifiableAttachedScrap = Collections.unmodifiableList((List<? extends ViewHolder>)this.mAttachedScrap);
            this.mRequestedCacheMax = 2;
            this.mViewCacheMax = 2;
        }
        
        private void attachAccessibilityDelegateOnBind(final ViewHolder viewHolder) {
            if (this.this$0.isAccessibilityEnabled()) {
                final View itemView = viewHolder.itemView;
                if (ViewCompat.getImportantForAccessibility(itemView) == 0) {
                    ViewCompat.setImportantForAccessibility(itemView, 1);
                }
                if (!ViewCompat.hasAccessibilityDelegate(itemView)) {
                    viewHolder.addFlags(16384);
                    ViewCompat.setAccessibilityDelegate(itemView, this.this$0.mAccessibilityDelegate.getItemDelegate());
                }
            }
        }
        
        private void invalidateDisplayListInt(final ViewHolder viewHolder) {
            if (viewHolder.itemView instanceof ViewGroup) {
                this.invalidateDisplayListInt((ViewGroup)viewHolder.itemView, false);
            }
        }
        
        private void invalidateDisplayListInt(final ViewGroup viewGroup, final boolean b) {
            for (int i = viewGroup.getChildCount() - 1; i >= 0; --i) {
                final View child = viewGroup.getChildAt(i);
                if (child instanceof ViewGroup) {
                    this.invalidateDisplayListInt((ViewGroup)child, true);
                }
            }
            if (!b) {
                return;
            }
            if (viewGroup.getVisibility() == 4) {
                viewGroup.setVisibility(0);
                viewGroup.setVisibility(4);
            }
            else {
                final int visibility = viewGroup.getVisibility();
                viewGroup.setVisibility(4);
                viewGroup.setVisibility(visibility);
            }
        }
        
        private boolean tryBindViewHolderByDeadline(@NonNull final ViewHolder viewHolder, final int n, final int mPreLayoutPosition, long nanoTime) {
            viewHolder.mOwnerRecyclerView = this.this$0;
            final int itemViewType = viewHolder.getItemViewType();
            final long nanoTime2 = this.this$0.getNanoTime();
            if (nanoTime != Long.MAX_VALUE && !this.mRecyclerPool.willBindInTime(itemViewType, nanoTime2, nanoTime)) {
                return false;
            }
            this.this$0.mAdapter.bindViewHolder(viewHolder, n);
            nanoTime = this.this$0.getNanoTime();
            this.mRecyclerPool.factorInBindTime(viewHolder.getItemViewType(), nanoTime - nanoTime2);
            this.attachAccessibilityDelegateOnBind(viewHolder);
            if (this.this$0.mState.isPreLayout()) {
                viewHolder.mPreLayoutPosition = mPreLayoutPosition;
            }
            return true;
        }
        
        void addViewHolderToRecycledViewPool(@NonNull final ViewHolder viewHolder, final boolean b) {
            RecyclerView.clearNestedRecyclerViewIfNotNested(viewHolder);
            if (viewHolder.hasAnyOfTheFlags(16384)) {
                viewHolder.setFlags(0, 16384);
                ViewCompat.setAccessibilityDelegate(viewHolder.itemView, null);
            }
            if (b) {
                this.dispatchViewRecycled(viewHolder);
            }
            viewHolder.mOwnerRecyclerView = null;
            this.getRecycledViewPool().putRecycledView(viewHolder);
        }
        
        public void bindViewToPosition(@NonNull final View view, final int i) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (childViewHolderInt == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("The view does not have a ViewHolder. You cannot pass arbitrary views to this method, they should be created by the Adapter");
                sb.append(this.this$0.exceptionLabel());
                throw new IllegalArgumentException(sb.toString());
            }
            final int positionOffset = this.this$0.mAdapterHelper.findPositionOffset(i);
            if (positionOffset >= 0 && positionOffset < this.this$0.mAdapter.getItemCount()) {
                this.tryBindViewHolderByDeadline(childViewHolderInt, positionOffset, i, Long.MAX_VALUE);
                final ViewGroup$LayoutParams layoutParams = childViewHolderInt.itemView.getLayoutParams();
                LayoutParams layoutParams2;
                if (layoutParams == null) {
                    layoutParams2 = (LayoutParams)this.this$0.generateDefaultLayoutParams();
                    childViewHolderInt.itemView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
                }
                else if (!this.this$0.checkLayoutParams(layoutParams)) {
                    layoutParams2 = (LayoutParams)this.this$0.generateLayoutParams(layoutParams);
                    childViewHolderInt.itemView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
                }
                else {
                    layoutParams2 = (LayoutParams)layoutParams;
                }
                boolean mPendingInvalidate = true;
                layoutParams2.mInsetsDirty = true;
                layoutParams2.mViewHolder = childViewHolderInt;
                if (childViewHolderInt.itemView.getParent() != null) {
                    mPendingInvalidate = false;
                }
                layoutParams2.mPendingInvalidate = mPendingInvalidate;
                return;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Inconsistency detected. Invalid item position ");
            sb2.append(i);
            sb2.append("(offset:");
            sb2.append(positionOffset);
            sb2.append(").");
            sb2.append("state:");
            sb2.append(this.this$0.mState.getItemCount());
            sb2.append(this.this$0.exceptionLabel());
            throw new IndexOutOfBoundsException(sb2.toString());
        }
        
        public void clear() {
            this.mAttachedScrap.clear();
            this.recycleAndClearCachedViews();
        }
        
        void clearOldPositions() {
            final int size = this.mCachedViews.size();
            final int n = 0;
            for (int i = 0; i < size; ++i) {
                this.mCachedViews.get(i).clearOldPosition();
            }
            for (int size2 = this.mAttachedScrap.size(), j = 0; j < size2; ++j) {
                this.mAttachedScrap.get(j).clearOldPosition();
            }
            if (this.mChangedScrap != null) {
                for (int size3 = this.mChangedScrap.size(), k = n; k < size3; ++k) {
                    this.mChangedScrap.get(k).clearOldPosition();
                }
            }
        }
        
        void clearScrap() {
            this.mAttachedScrap.clear();
            if (this.mChangedScrap != null) {
                this.mChangedScrap.clear();
            }
        }
        
        public int convertPreLayoutPositionToPostLayout(final int i) {
            if (i < 0 || i >= this.this$0.mState.getItemCount()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invalid position ");
                sb.append(i);
                sb.append(". State ");
                sb.append("item count is ");
                sb.append(this.this$0.mState.getItemCount());
                sb.append(this.this$0.exceptionLabel());
                throw new IndexOutOfBoundsException(sb.toString());
            }
            if (!this.this$0.mState.isPreLayout()) {
                return i;
            }
            return this.this$0.mAdapterHelper.findPositionOffset(i);
        }
        
        void dispatchViewRecycled(@NonNull final ViewHolder viewHolder) {
            if (this.this$0.mRecyclerListener != null) {
                this.this$0.mRecyclerListener.onViewRecycled(viewHolder);
            }
            if (this.this$0.mAdapter != null) {
                this.this$0.mAdapter.onViewRecycled(viewHolder);
            }
            if (this.this$0.mState != null) {
                this.this$0.mViewInfoStore.removeViewHolder(viewHolder);
            }
        }
        
        ViewHolder getChangedScrapViewForPosition(int i) {
            if (this.mChangedScrap != null) {
                final int size = this.mChangedScrap.size();
                if (size != 0) {
                    final int n = 0;
                    for (int j = 0; j < size; ++j) {
                        final ViewHolder viewHolder = this.mChangedScrap.get(j);
                        if (!viewHolder.wasReturnedFromScrap() && viewHolder.getLayoutPosition() == i) {
                            viewHolder.addFlags(32);
                            return viewHolder;
                        }
                    }
                    if (this.this$0.mAdapter.hasStableIds()) {
                        i = this.this$0.mAdapterHelper.findPositionOffset(i);
                        if (i > 0 && i < this.this$0.mAdapter.getItemCount()) {
                            final long itemId = this.this$0.mAdapter.getItemId(i);
                            ViewHolder viewHolder2;
                            for (i = n; i < size; ++i) {
                                viewHolder2 = this.mChangedScrap.get(i);
                                if (!viewHolder2.wasReturnedFromScrap() && viewHolder2.getItemId() == itemId) {
                                    viewHolder2.addFlags(32);
                                    return viewHolder2;
                                }
                            }
                        }
                    }
                    return null;
                }
            }
            return null;
        }
        
        RecycledViewPool getRecycledViewPool() {
            if (this.mRecyclerPool == null) {
                this.mRecyclerPool = new RecycledViewPool();
            }
            return this.mRecyclerPool;
        }
        
        int getScrapCount() {
            return this.mAttachedScrap.size();
        }
        
        @NonNull
        public List<ViewHolder> getScrapList() {
            return this.mUnmodifiableAttachedScrap;
        }
        
        ViewHolder getScrapOrCachedViewForId(final long n, final int n2, final boolean b) {
            for (int i = this.mAttachedScrap.size() - 1; i >= 0; --i) {
                final ViewHolder viewHolder = this.mAttachedScrap.get(i);
                if (viewHolder.getItemId() == n && !viewHolder.wasReturnedFromScrap()) {
                    if (n2 == viewHolder.getItemViewType()) {
                        viewHolder.addFlags(32);
                        if (viewHolder.isRemoved() && !this.this$0.mState.isPreLayout()) {
                            viewHolder.setFlags(2, 14);
                        }
                        return viewHolder;
                    }
                    if (!b) {
                        this.mAttachedScrap.remove(i);
                        this.this$0.removeDetachedView(viewHolder.itemView, false);
                        this.quickRecycleScrapView(viewHolder.itemView);
                    }
                }
            }
            for (int j = this.mCachedViews.size() - 1; j >= 0; --j) {
                final ViewHolder viewHolder2 = this.mCachedViews.get(j);
                if (viewHolder2.getItemId() == n) {
                    if (n2 == viewHolder2.getItemViewType()) {
                        if (!b) {
                            this.mCachedViews.remove(j);
                        }
                        return viewHolder2;
                    }
                    if (!b) {
                        this.recycleCachedViewAt(j);
                        return null;
                    }
                }
            }
            return null;
        }
        
        ViewHolder getScrapOrHiddenOrCachedHolderForPosition(int indexOfChild, final boolean b) {
            final int size = this.mAttachedScrap.size();
            final int n = 0;
            for (int i = 0; i < size; ++i) {
                final ViewHolder viewHolder = this.mAttachedScrap.get(i);
                if (!viewHolder.wasReturnedFromScrap() && viewHolder.getLayoutPosition() == indexOfChild && !viewHolder.isInvalid() && (this.this$0.mState.mInPreLayout || !viewHolder.isRemoved())) {
                    viewHolder.addFlags(32);
                    return viewHolder;
                }
            }
            if (!b) {
                final View hiddenNonRemovedView = this.this$0.mChildHelper.findHiddenNonRemovedView(indexOfChild);
                if (hiddenNonRemovedView != null) {
                    final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(hiddenNonRemovedView);
                    this.this$0.mChildHelper.unhide(hiddenNonRemovedView);
                    indexOfChild = this.this$0.mChildHelper.indexOfChild(hiddenNonRemovedView);
                    if (indexOfChild == -1) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("layout index should not be -1 after unhiding a view:");
                        sb.append(childViewHolderInt);
                        sb.append(this.this$0.exceptionLabel());
                        throw new IllegalStateException(sb.toString());
                    }
                    this.this$0.mChildHelper.detachViewFromParent(indexOfChild);
                    this.scrapView(hiddenNonRemovedView);
                    childViewHolderInt.addFlags(8224);
                    return childViewHolderInt;
                }
            }
            for (int size2 = this.mCachedViews.size(), j = n; j < size2; ++j) {
                final ViewHolder viewHolder2 = this.mCachedViews.get(j);
                if (!viewHolder2.isInvalid() && viewHolder2.getLayoutPosition() == indexOfChild) {
                    if (!b) {
                        this.mCachedViews.remove(j);
                    }
                    return viewHolder2;
                }
            }
            return null;
        }
        
        View getScrapViewAt(final int index) {
            return this.mAttachedScrap.get(index).itemView;
        }
        
        @NonNull
        public View getViewForPosition(final int n) {
            return this.getViewForPosition(n, false);
        }
        
        View getViewForPosition(final int n, final boolean b) {
            return this.tryGetViewHolderForPositionByDeadline(n, b, Long.MAX_VALUE).itemView;
        }
        
        void markItemDecorInsetsDirty() {
            for (int size = this.mCachedViews.size(), i = 0; i < size; ++i) {
                final LayoutParams layoutParams = (LayoutParams)this.mCachedViews.get(i).itemView.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.mInsetsDirty = true;
                }
            }
        }
        
        void markKnownViewsInvalid() {
            for (int size = this.mCachedViews.size(), i = 0; i < size; ++i) {
                final ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder != null) {
                    viewHolder.addFlags(6);
                    viewHolder.addChangePayload(null);
                }
            }
            if (this.this$0.mAdapter == null || !this.this$0.mAdapter.hasStableIds()) {
                this.recycleAndClearCachedViews();
            }
        }
        
        void offsetPositionRecordsForInsert(final int n, final int n2) {
            for (int size = this.mCachedViews.size(), i = 0; i < size; ++i) {
                final ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder != null && viewHolder.mPosition >= n) {
                    viewHolder.offsetPosition(n2, true);
                }
            }
        }
        
        void offsetPositionRecordsForMove(final int n, final int n2) {
            int n3;
            int n4;
            int n5;
            if (n < n2) {
                n3 = n2;
                n4 = -1;
                n5 = n;
            }
            else {
                n3 = n;
                n4 = 1;
                n5 = n2;
            }
            for (int size = this.mCachedViews.size(), i = 0; i < size; ++i) {
                final ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder != null && viewHolder.mPosition >= n5) {
                    if (viewHolder.mPosition <= n3) {
                        if (viewHolder.mPosition == n) {
                            viewHolder.offsetPosition(n2 - n, false);
                        }
                        else {
                            viewHolder.offsetPosition(n4, false);
                        }
                    }
                }
            }
        }
        
        void offsetPositionRecordsForRemove(final int n, final int n2, final boolean b) {
            for (int i = this.mCachedViews.size() - 1; i >= 0; --i) {
                final ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder != null) {
                    if (viewHolder.mPosition >= n + n2) {
                        viewHolder.offsetPosition(-n2, b);
                    }
                    else if (viewHolder.mPosition >= n) {
                        viewHolder.addFlags(8);
                        this.recycleCachedViewAt(i);
                    }
                }
            }
        }
        
        void onAdapterChanged(final Adapter adapter, final Adapter adapter2, final boolean b) {
            this.clear();
            this.getRecycledViewPool().onAdapterChanged(adapter, adapter2, b);
        }
        
        void quickRecycleScrapView(final View view) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            childViewHolderInt.mScrapContainer = null;
            childViewHolderInt.mInChangeScrap = false;
            childViewHolderInt.clearReturnedFromScrapFlag();
            this.recycleViewHolderInternal(childViewHolderInt);
        }
        
        void recycleAndClearCachedViews() {
            for (int i = this.mCachedViews.size() - 1; i >= 0; --i) {
                this.recycleCachedViewAt(i);
            }
            this.mCachedViews.clear();
            if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                this.this$0.mPrefetchRegistry.clearPrefetchPositions();
            }
        }
        
        void recycleCachedViewAt(final int n) {
            this.addViewHolderToRecycledViewPool(this.mCachedViews.get(n), true);
            this.mCachedViews.remove(n);
        }
        
        public void recycleView(@NonNull final View view) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (childViewHolderInt.isTmpDetached()) {
                this.this$0.removeDetachedView(view, false);
            }
            if (childViewHolderInt.isScrap()) {
                childViewHolderInt.unScrap();
            }
            else if (childViewHolderInt.wasReturnedFromScrap()) {
                childViewHolderInt.clearReturnedFromScrapFlag();
            }
            this.recycleViewHolderInternal(childViewHolderInt);
        }
        
        void recycleViewHolderInternal(final ViewHolder viewHolder) {
            final boolean scrap = viewHolder.isScrap();
            boolean b = false;
            final int n = 0;
            if (scrap || viewHolder.itemView.getParent() != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Scrapped or attached views may not be recycled. isScrap:");
                sb.append(viewHolder.isScrap());
                sb.append(" isAttached:");
                if (viewHolder.itemView.getParent() != null) {
                    b = true;
                }
                sb.append(b);
                sb.append(this.this$0.exceptionLabel());
                throw new IllegalArgumentException(sb.toString());
            }
            if (viewHolder.isTmpDetached()) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Tmp detached view should be removed from RecyclerView before it can be recycled: ");
                sb2.append(viewHolder);
                sb2.append(this.this$0.exceptionLabel());
                throw new IllegalArgumentException(sb2.toString());
            }
            if (viewHolder.shouldIgnore()) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Trying to recycle an ignored view holder. You should first call stopIgnoringView(view) before calling recycle.");
                sb3.append(this.this$0.exceptionLabel());
                throw new IllegalArgumentException(sb3.toString());
            }
            final boolean doesTransientStatePreventRecycling = viewHolder.doesTransientStatePreventRecycling();
            int n3;
            int n4;
            if ((this.this$0.mAdapter == null || !doesTransientStatePreventRecycling || !this.this$0.mAdapter.onFailedToRecycleView(viewHolder)) && !viewHolder.isRecyclable()) {
                final int n2 = 0;
                n3 = n;
                n4 = n2;
            }
            else {
                int n6;
                if (this.mViewCacheMax > 0 && !viewHolder.hasAnyOfTheFlags(526)) {
                    int size;
                    final int n5 = size = this.mCachedViews.size();
                    if (n5 >= this.mViewCacheMax && (size = n5) > 0) {
                        this.recycleCachedViewAt(0);
                        size = n5 - 1;
                    }
                    int index = size;
                    if (RecyclerView.ALLOW_THREAD_GAP_WORK && (index = size) > 0) {
                        index = size;
                        if (!this.this$0.mPrefetchRegistry.lastPrefetchIncludedPosition(viewHolder.mPosition)) {
                            --size;
                            while (size >= 0 && this.this$0.mPrefetchRegistry.lastPrefetchIncludedPosition(this.mCachedViews.get(size).mPosition)) {
                                --size;
                            }
                            index = size + 1;
                        }
                    }
                    this.mCachedViews.add(index, viewHolder);
                    n6 = 1;
                }
                else {
                    n6 = 0;
                }
                n3 = n;
                n4 = n6;
                if (n6 == 0) {
                    this.addViewHolderToRecycledViewPool(viewHolder, true);
                    n3 = 1;
                    n4 = n6;
                }
            }
            this.this$0.mViewInfoStore.removeViewHolder(viewHolder);
            if (n4 == 0 && n3 == 0 && doesTransientStatePreventRecycling) {
                viewHolder.mOwnerRecyclerView = null;
            }
        }
        
        void recycleViewInternal(final View view) {
            this.recycleViewHolderInternal(RecyclerView.getChildViewHolderInt(view));
        }
        
        void scrapView(final View view) {
            final ViewHolder childViewHolderInt = RecyclerView.getChildViewHolderInt(view);
            if (!childViewHolderInt.hasAnyOfTheFlags(12) && childViewHolderInt.isUpdated() && !this.this$0.canReuseUpdatedViewHolder(childViewHolderInt)) {
                if (this.mChangedScrap == null) {
                    this.mChangedScrap = new ArrayList<ViewHolder>();
                }
                childViewHolderInt.setScrapContainer(this, true);
                this.mChangedScrap.add(childViewHolderInt);
            }
            else {
                if (childViewHolderInt.isInvalid() && !childViewHolderInt.isRemoved() && !this.this$0.mAdapter.hasStableIds()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Called scrap view with an invalid view. Invalid views cannot be reused from scrap, they should rebound from recycler pool.");
                    sb.append(this.this$0.exceptionLabel());
                    throw new IllegalArgumentException(sb.toString());
                }
                childViewHolderInt.setScrapContainer(this, false);
                this.mAttachedScrap.add(childViewHolderInt);
            }
        }
        
        void setRecycledViewPool(final RecycledViewPool mRecyclerPool) {
            if (this.mRecyclerPool != null) {
                this.mRecyclerPool.detach();
            }
            this.mRecyclerPool = mRecyclerPool;
            if (this.mRecyclerPool != null && this.this$0.getAdapter() != null) {
                this.mRecyclerPool.attach();
            }
        }
        
        void setViewCacheExtension(final ViewCacheExtension mViewCacheExtension) {
            this.mViewCacheExtension = mViewCacheExtension;
        }
        
        public void setViewCacheSize(final int mRequestedCacheMax) {
            this.mRequestedCacheMax = mRequestedCacheMax;
            this.updateViewCacheSize();
        }
        
        @Nullable
        ViewHolder tryGetViewHolderForPositionByDeadline(final int n, final boolean b, final long n2) {
            if (n >= 0 && n < this.this$0.mState.getItemCount()) {
                final boolean preLayout = this.this$0.mState.isPreLayout();
                final boolean b2 = true;
                ViewHolder viewHolder = null;
                int n3 = 0;
                Label_0073: {
                    if (preLayout) {
                        final ViewHolder changedScrapViewForPosition = this.getChangedScrapViewForPosition(n);
                        if ((viewHolder = changedScrapViewForPosition) != null) {
                            n3 = 1;
                            viewHolder = changedScrapViewForPosition;
                            break Label_0073;
                        }
                    }
                    else {
                        viewHolder = null;
                    }
                    n3 = 0;
                }
                ViewHolder scrapOrHiddenOrCachedHolderForPosition = viewHolder;
                int n4 = n3;
                if (viewHolder == null) {
                    final ViewHolder viewHolder2 = scrapOrHiddenOrCachedHolderForPosition = this.getScrapOrHiddenOrCachedHolderForPosition(n, b);
                    n4 = n3;
                    if (viewHolder2 != null) {
                        if (!this.validateViewHolderForOffsetPosition(viewHolder2)) {
                            if (!b) {
                                viewHolder2.addFlags(4);
                                if (viewHolder2.isScrap()) {
                                    this.this$0.removeDetachedView(viewHolder2.itemView, false);
                                    viewHolder2.unScrap();
                                }
                                else if (viewHolder2.wasReturnedFromScrap()) {
                                    viewHolder2.clearReturnedFromScrapFlag();
                                }
                                this.recycleViewHolderInternal(viewHolder2);
                            }
                            scrapOrHiddenOrCachedHolderForPosition = null;
                            n4 = n3;
                        }
                        else {
                            n4 = 1;
                            scrapOrHiddenOrCachedHolderForPosition = viewHolder2;
                        }
                    }
                }
                ViewHolder viewHolder3 = scrapOrHiddenOrCachedHolderForPosition;
                int n5 = n4;
                int n6 = 0;
                ViewHolder viewHolder7 = null;
                Label_0771: {
                    if (scrapOrHiddenOrCachedHolderForPosition == null) {
                        final int positionOffset = this.this$0.mAdapterHelper.findPositionOffset(n);
                        if (positionOffset < 0 || positionOffset >= this.this$0.mAdapter.getItemCount()) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Inconsistency detected. Invalid item position ");
                            sb.append(n);
                            sb.append("(offset:");
                            sb.append(positionOffset);
                            sb.append(").");
                            sb.append("state:");
                            sb.append(this.this$0.mState.getItemCount());
                            sb.append(this.this$0.exceptionLabel());
                            throw new IndexOutOfBoundsException(sb.toString());
                        }
                        final int itemViewType = this.this$0.mAdapter.getItemViewType(positionOffset);
                        ViewHolder scrapOrCachedViewForId = scrapOrHiddenOrCachedHolderForPosition;
                        n6 = n4;
                        if (this.this$0.mAdapter.hasStableIds()) {
                            final ViewHolder viewHolder4 = scrapOrCachedViewForId = this.getScrapOrCachedViewForId(this.this$0.mAdapter.getItemId(positionOffset), itemViewType, b);
                            n6 = n4;
                            if (viewHolder4 != null) {
                                viewHolder4.mPosition = positionOffset;
                                n6 = 1;
                                scrapOrCachedViewForId = viewHolder4;
                            }
                        }
                        ViewHolder viewHolder5;
                        if ((viewHolder5 = scrapOrCachedViewForId) == null) {
                            viewHolder5 = scrapOrCachedViewForId;
                            if (this.mViewCacheExtension != null) {
                                final View viewForPositionAndType = this.mViewCacheExtension.getViewForPositionAndType(this, n, itemViewType);
                                viewHolder5 = scrapOrCachedViewForId;
                                if (viewForPositionAndType != null) {
                                    final ViewHolder childViewHolder = this.this$0.getChildViewHolder(viewForPositionAndType);
                                    if (childViewHolder == null) {
                                        final StringBuilder sb2 = new StringBuilder();
                                        sb2.append("getViewForPositionAndType returned a view which does not have a ViewHolder");
                                        sb2.append(this.this$0.exceptionLabel());
                                        throw new IllegalArgumentException(sb2.toString());
                                    }
                                    viewHolder5 = childViewHolder;
                                    if (childViewHolder.shouldIgnore()) {
                                        final StringBuilder sb3 = new StringBuilder();
                                        sb3.append("getViewForPositionAndType returned a view that is ignored. You must call stopIgnoring before returning this view.");
                                        sb3.append(this.this$0.exceptionLabel());
                                        throw new IllegalArgumentException(sb3.toString());
                                    }
                                }
                            }
                        }
                        ViewHolder viewHolder6;
                        if ((viewHolder6 = viewHolder5) == null) {
                            final ViewHolder recycledView = this.getRecycledViewPool().getRecycledView(itemViewType);
                            if ((viewHolder6 = recycledView) != null) {
                                recycledView.resetInternal();
                                viewHolder6 = recycledView;
                                if (RecyclerView.FORCE_INVALIDATE_DISPLAY_LIST) {
                                    this.invalidateDisplayListInt(recycledView);
                                    viewHolder6 = recycledView;
                                }
                            }
                        }
                        viewHolder3 = viewHolder6;
                        n5 = n6;
                        if (viewHolder6 == null) {
                            final long nanoTime = this.this$0.getNanoTime();
                            if (n2 != Long.MAX_VALUE && !this.mRecyclerPool.willCreateInTime(itemViewType, nanoTime, n2)) {
                                return null;
                            }
                            viewHolder7 = this.this$0.mAdapter.createViewHolder(this.this$0, itemViewType);
                            if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                                final RecyclerView nestedRecyclerView = RecyclerView.findNestedRecyclerView(viewHolder7.itemView);
                                if (nestedRecyclerView != null) {
                                    viewHolder7.mNestedRecyclerView = new WeakReference<RecyclerView>(nestedRecyclerView);
                                }
                            }
                            this.mRecyclerPool.factorInCreateTime(itemViewType, this.this$0.getNanoTime() - nanoTime);
                            break Label_0771;
                        }
                    }
                    viewHolder7 = viewHolder3;
                    n6 = n5;
                }
                if (n6 != 0 && !this.this$0.mState.isPreLayout() && viewHolder7.hasAnyOfTheFlags(8192)) {
                    viewHolder7.setFlags(0, 8192);
                    if (this.this$0.mState.mRunSimpleAnimations) {
                        this.this$0.recordAnimationInfoIfBouncedHiddenView(viewHolder7, this.this$0.mItemAnimator.recordPreLayoutInformation(this.this$0.mState, viewHolder7, ItemAnimator.buildAdapterChangeFlagsForAnimations(viewHolder7) | 0x1000, viewHolder7.getUnmodifiedPayloads()));
                    }
                }
                boolean tryBindViewHolderByDeadline = false;
                Label_0954: {
                    if (this.this$0.mState.isPreLayout() && viewHolder7.isBound()) {
                        viewHolder7.mPreLayoutPosition = n;
                    }
                    else if (!viewHolder7.isBound() || viewHolder7.needsUpdate() || viewHolder7.isInvalid()) {
                        tryBindViewHolderByDeadline = this.tryBindViewHolderByDeadline(viewHolder7, this.this$0.mAdapterHelper.findPositionOffset(n), n, n2);
                        break Label_0954;
                    }
                    tryBindViewHolderByDeadline = false;
                }
                final ViewGroup$LayoutParams layoutParams = viewHolder7.itemView.getLayoutParams();
                LayoutParams layoutParams2;
                if (layoutParams == null) {
                    layoutParams2 = (LayoutParams)this.this$0.generateDefaultLayoutParams();
                    viewHolder7.itemView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
                }
                else if (!this.this$0.checkLayoutParams(layoutParams)) {
                    layoutParams2 = (LayoutParams)this.this$0.generateLayoutParams(layoutParams);
                    viewHolder7.itemView.setLayoutParams((ViewGroup$LayoutParams)layoutParams2);
                }
                else {
                    layoutParams2 = (LayoutParams)layoutParams;
                }
                layoutParams2.mViewHolder = viewHolder7;
                layoutParams2.mPendingInvalidate = (n6 != 0 && tryBindViewHolderByDeadline && b2);
                return viewHolder7;
            }
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Invalid item position ");
            sb4.append(n);
            sb4.append("(");
            sb4.append(n);
            sb4.append("). Item count:");
            sb4.append(this.this$0.mState.getItemCount());
            sb4.append(this.this$0.exceptionLabel());
            throw new IndexOutOfBoundsException(sb4.toString());
        }
        
        void unscrapView(final ViewHolder viewHolder) {
            if (viewHolder.mInChangeScrap) {
                this.mChangedScrap.remove(viewHolder);
            }
            else {
                this.mAttachedScrap.remove(viewHolder);
            }
            viewHolder.mScrapContainer = null;
            viewHolder.mInChangeScrap = false;
            viewHolder.clearReturnedFromScrapFlag();
        }
        
        void updateViewCacheSize() {
            int mPrefetchMaxCountObserved;
            if (this.this$0.mLayout != null) {
                mPrefetchMaxCountObserved = this.this$0.mLayout.mPrefetchMaxCountObserved;
            }
            else {
                mPrefetchMaxCountObserved = 0;
            }
            this.mViewCacheMax = this.mRequestedCacheMax + mPrefetchMaxCountObserved;
            for (int n = this.mCachedViews.size() - 1; n >= 0 && this.mCachedViews.size() > this.mViewCacheMax; --n) {
                this.recycleCachedViewAt(n);
            }
        }
        
        boolean validateViewHolderForOffsetPosition(final ViewHolder obj) {
            if (obj.isRemoved()) {
                return this.this$0.mState.isPreLayout();
            }
            if (obj.mPosition < 0 || obj.mPosition >= this.this$0.mAdapter.getItemCount()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Inconsistency detected. Invalid view holder adapter position");
                sb.append(obj);
                sb.append(this.this$0.exceptionLabel());
                throw new IndexOutOfBoundsException(sb.toString());
            }
            final boolean preLayout = this.this$0.mState.isPreLayout();
            boolean b = false;
            if (!preLayout && this.this$0.mAdapter.getItemViewType(obj.mPosition) != obj.getItemViewType()) {
                return false;
            }
            if (this.this$0.mAdapter.hasStableIds()) {
                if (obj.getItemId() == this.this$0.mAdapter.getItemId(obj.mPosition)) {
                    b = true;
                }
                return b;
            }
            return true;
        }
        
        void viewRangeUpdate(final int n, final int n2) {
            for (int i = this.mCachedViews.size() - 1; i >= 0; --i) {
                final ViewHolder viewHolder = this.mCachedViews.get(i);
                if (viewHolder != null) {
                    final int mPosition = viewHolder.mPosition;
                    if (mPosition >= n && mPosition < n2 + n) {
                        viewHolder.addFlags(2);
                        this.recycleCachedViewAt(i);
                    }
                }
            }
        }
    }
    
    public interface RecyclerListener
    {
        void onViewRecycled(@NonNull final ViewHolder p0);
    }
    
    private class RecyclerViewDataObserver extends AdapterDataObserver
    {
        final RecyclerView this$0;
        
        RecyclerViewDataObserver(final RecyclerView this$0) {
            this.this$0 = this$0;
        }
        
        @Override
        public void onChanged() {
            this.this$0.assertNotInLayoutOrScroll(null);
            this.this$0.mState.mStructureChanged = true;
            this.this$0.processDataSetCompletelyChanged(true);
            if (!this.this$0.mAdapterHelper.hasPendingUpdates()) {
                this.this$0.requestLayout();
            }
        }
        
        @Override
        public void onItemRangeChanged(final int n, final int n2, final Object o) {
            this.this$0.assertNotInLayoutOrScroll(null);
            if (this.this$0.mAdapterHelper.onItemRangeChanged(n, n2, o)) {
                this.triggerUpdateProcessor();
            }
        }
        
        @Override
        public void onItemRangeInserted(final int n, final int n2) {
            this.this$0.assertNotInLayoutOrScroll(null);
            if (this.this$0.mAdapterHelper.onItemRangeInserted(n, n2)) {
                this.triggerUpdateProcessor();
            }
        }
        
        @Override
        public void onItemRangeMoved(final int n, final int n2, final int n3) {
            this.this$0.assertNotInLayoutOrScroll(null);
            if (this.this$0.mAdapterHelper.onItemRangeMoved(n, n2, n3)) {
                this.triggerUpdateProcessor();
            }
        }
        
        @Override
        public void onItemRangeRemoved(final int n, final int n2) {
            this.this$0.assertNotInLayoutOrScroll(null);
            if (this.this$0.mAdapterHelper.onItemRangeRemoved(n, n2)) {
                this.triggerUpdateProcessor();
            }
        }
        
        void triggerUpdateProcessor() {
            if (RecyclerView.POST_UPDATES_ON_ANIMATION && this.this$0.mHasFixedSize && this.this$0.mIsAttached) {
                ViewCompat.postOnAnimation((View)this.this$0, this.this$0.mUpdateChildViewsRunnable);
            }
            else {
                this.this$0.mAdapterUpdateDuringMeasure = true;
                this.this$0.requestLayout();
            }
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        Parcelable mLayoutState;
        
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
        
        SavedState(final Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            if (classLoader == null) {
                classLoader = LayoutManager.class.getClassLoader();
            }
            this.mLayoutState = parcel.readParcelable(classLoader);
        }
        
        SavedState(final Parcelable parcelable) {
            super(parcelable);
        }
        
        void copyFrom(final SavedState savedState) {
            this.mLayoutState = savedState.mLayoutState;
        }
        
        @Override
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeParcelable(this.mLayoutState, 0);
        }
    }
    
    public static class SimpleOnItemTouchListener implements OnItemTouchListener
    {
        @Override
        public boolean onInterceptTouchEvent(@NonNull final RecyclerView recyclerView, @NonNull final MotionEvent motionEvent) {
            return false;
        }
        
        @Override
        public void onRequestDisallowInterceptTouchEvent(final boolean b) {
        }
        
        @Override
        public void onTouchEvent(@NonNull final RecyclerView recyclerView, @NonNull final MotionEvent motionEvent) {
        }
    }
    
    public abstract static class SmoothScroller
    {
        private LayoutManager mLayoutManager;
        private boolean mPendingInitialRun;
        private RecyclerView mRecyclerView;
        private final Action mRecyclingAction;
        private boolean mRunning;
        private boolean mStarted;
        private int mTargetPosition;
        private View mTargetView;
        
        public SmoothScroller() {
            this.mTargetPosition = -1;
            this.mRecyclingAction = new Action(0, 0);
        }
        
        @Nullable
        public PointF computeScrollVectorForPosition(final int n) {
            final LayoutManager layoutManager = this.getLayoutManager();
            if (layoutManager instanceof ScrollVectorProvider) {
                return ((ScrollVectorProvider)layoutManager).computeScrollVectorForPosition(n);
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("You should override computeScrollVectorForPosition when the LayoutManager does not implement ");
            sb.append(ScrollVectorProvider.class.getCanonicalName());
            Log.w("RecyclerView", sb.toString());
            return null;
        }
        
        public View findViewByPosition(final int n) {
            return this.mRecyclerView.mLayout.findViewByPosition(n);
        }
        
        public int getChildCount() {
            return this.mRecyclerView.mLayout.getChildCount();
        }
        
        public int getChildPosition(final View view) {
            return this.mRecyclerView.getChildLayoutPosition(view);
        }
        
        @Nullable
        public LayoutManager getLayoutManager() {
            return this.mLayoutManager;
        }
        
        public int getTargetPosition() {
            return this.mTargetPosition;
        }
        
        @Deprecated
        public void instantScrollToPosition(final int n) {
            this.mRecyclerView.scrollToPosition(n);
        }
        
        public boolean isPendingInitialRun() {
            return this.mPendingInitialRun;
        }
        
        public boolean isRunning() {
            return this.mRunning;
        }
        
        protected void normalize(@NonNull final PointF pointF) {
            final float n = (float)Math.sqrt(pointF.x * pointF.x + pointF.y * pointF.y);
            pointF.x /= n;
            pointF.y /= n;
        }
        
        void onAnimation(final int n, final int n2) {
            final RecyclerView mRecyclerView = this.mRecyclerView;
            if (!this.mRunning || this.mTargetPosition == -1 || mRecyclerView == null) {
                this.stop();
            }
            if (this.mPendingInitialRun && this.mTargetView == null && this.mLayoutManager != null) {
                final PointF computeScrollVectorForPosition = this.computeScrollVectorForPosition(this.mTargetPosition);
                if (computeScrollVectorForPosition != null && (computeScrollVectorForPosition.x != 0.0f || computeScrollVectorForPosition.y != 0.0f)) {
                    mRecyclerView.scrollStep((int)Math.signum(computeScrollVectorForPosition.x), (int)Math.signum(computeScrollVectorForPosition.y), null);
                }
            }
            this.mPendingInitialRun = false;
            if (this.mTargetView != null) {
                if (this.getChildPosition(this.mTargetView) == this.mTargetPosition) {
                    this.onTargetFound(this.mTargetView, mRecyclerView.mState, this.mRecyclingAction);
                    this.mRecyclingAction.runIfNecessary(mRecyclerView);
                    this.stop();
                }
                else {
                    Log.e("RecyclerView", "Passed over target position while smooth scrolling.");
                    this.mTargetView = null;
                }
            }
            if (this.mRunning) {
                this.onSeekTargetStep(n, n2, mRecyclerView.mState, this.mRecyclingAction);
                final boolean hasJumpTarget = this.mRecyclingAction.hasJumpTarget();
                this.mRecyclingAction.runIfNecessary(mRecyclerView);
                if (hasJumpTarget) {
                    if (this.mRunning) {
                        this.mPendingInitialRun = true;
                        mRecyclerView.mViewFlinger.postOnAnimation();
                    }
                    else {
                        this.stop();
                    }
                }
            }
        }
        
        protected void onChildAttachedToWindow(final View mTargetView) {
            if (this.getChildPosition(mTargetView) == this.getTargetPosition()) {
                this.mTargetView = mTargetView;
            }
        }
        
        protected abstract void onSeekTargetStep(@Px final int p0, @Px final int p1, @NonNull final State p2, @NonNull final Action p3);
        
        protected abstract void onStart();
        
        protected abstract void onStop();
        
        protected abstract void onTargetFound(@NonNull final View p0, @NonNull final State p1, @NonNull final Action p2);
        
        public void setTargetPosition(final int mTargetPosition) {
            this.mTargetPosition = mTargetPosition;
        }
        
        void start(final RecyclerView mRecyclerView, final LayoutManager mLayoutManager) {
            if (this.mStarted) {
                final StringBuilder sb = new StringBuilder();
                sb.append("An instance of ");
                sb.append(this.getClass().getSimpleName());
                sb.append(" was started ");
                sb.append("more than once. Each instance of");
                sb.append(this.getClass().getSimpleName());
                sb.append(" ");
                sb.append("is intended to only be used once. You should create a new instance for ");
                sb.append("each use.");
                Log.w("RecyclerView", sb.toString());
            }
            this.mRecyclerView = mRecyclerView;
            this.mLayoutManager = mLayoutManager;
            if (this.mTargetPosition == -1) {
                throw new IllegalArgumentException("Invalid target position");
            }
            this.mRecyclerView.mState.mTargetPosition = this.mTargetPosition;
            this.mRunning = true;
            this.mPendingInitialRun = true;
            this.mTargetView = this.findViewByPosition(this.getTargetPosition());
            this.onStart();
            this.mRecyclerView.mViewFlinger.postOnAnimation();
            this.mStarted = true;
        }
        
        protected final void stop() {
            if (!this.mRunning) {
                return;
            }
            this.mRunning = false;
            this.onStop();
            this.mRecyclerView.mState.mTargetPosition = -1;
            this.mTargetView = null;
            this.mTargetPosition = -1;
            this.mPendingInitialRun = false;
            this.mLayoutManager.onSmoothScrollerStopped(this);
            this.mLayoutManager = null;
            this.mRecyclerView = null;
        }
        
        public static class Action
        {
            public static final int UNDEFINED_DURATION = Integer.MIN_VALUE;
            private boolean mChanged;
            private int mConsecutiveUpdates;
            private int mDuration;
            private int mDx;
            private int mDy;
            private Interpolator mInterpolator;
            private int mJumpToPosition;
            
            public Action(@Px final int n, @Px final int n2) {
                this(n, n2, Integer.MIN_VALUE, null);
            }
            
            public Action(@Px final int n, @Px final int n2, final int n3) {
                this(n, n2, n3, null);
            }
            
            public Action(@Px final int mDx, @Px final int mDy, final int mDuration, @Nullable final Interpolator mInterpolator) {
                this.mJumpToPosition = -1;
                this.mChanged = false;
                this.mConsecutiveUpdates = 0;
                this.mDx = mDx;
                this.mDy = mDy;
                this.mDuration = mDuration;
                this.mInterpolator = mInterpolator;
            }
            
            private void validate() {
                if (this.mInterpolator != null && this.mDuration < 1) {
                    throw new IllegalStateException("If you provide an interpolator, you must set a positive duration");
                }
                if (this.mDuration < 1) {
                    throw new IllegalStateException("Scroll duration must be a positive number");
                }
            }
            
            public int getDuration() {
                return this.mDuration;
            }
            
            @Px
            public int getDx() {
                return this.mDx;
            }
            
            @Px
            public int getDy() {
                return this.mDy;
            }
            
            @Nullable
            public Interpolator getInterpolator() {
                return this.mInterpolator;
            }
            
            boolean hasJumpTarget() {
                return this.mJumpToPosition >= 0;
            }
            
            public void jumpTo(final int mJumpToPosition) {
                this.mJumpToPosition = mJumpToPosition;
            }
            
            void runIfNecessary(final RecyclerView recyclerView) {
                if (this.mJumpToPosition >= 0) {
                    final int mJumpToPosition = this.mJumpToPosition;
                    this.mJumpToPosition = -1;
                    recyclerView.jumpToPositionForSmoothScroller(mJumpToPosition);
                    this.mChanged = false;
                    return;
                }
                if (this.mChanged) {
                    this.validate();
                    if (this.mInterpolator == null) {
                        if (this.mDuration == Integer.MIN_VALUE) {
                            recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy);
                        }
                        else {
                            recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration);
                        }
                    }
                    else {
                        recyclerView.mViewFlinger.smoothScrollBy(this.mDx, this.mDy, this.mDuration, this.mInterpolator);
                    }
                    ++this.mConsecutiveUpdates;
                    if (this.mConsecutiveUpdates > 10) {
                        Log.e("RecyclerView", "Smooth Scroll action is being updated too frequently. Make sure you are not changing it unless necessary");
                    }
                    this.mChanged = false;
                }
                else {
                    this.mConsecutiveUpdates = 0;
                }
            }
            
            public void setDuration(final int mDuration) {
                this.mChanged = true;
                this.mDuration = mDuration;
            }
            
            public void setDx(@Px final int mDx) {
                this.mChanged = true;
                this.mDx = mDx;
            }
            
            public void setDy(@Px final int mDy) {
                this.mChanged = true;
                this.mDy = mDy;
            }
            
            public void setInterpolator(@Nullable final Interpolator mInterpolator) {
                this.mChanged = true;
                this.mInterpolator = mInterpolator;
            }
            
            public void update(@Px final int mDx, @Px final int mDy, final int mDuration, @Nullable final Interpolator mInterpolator) {
                this.mDx = mDx;
                this.mDy = mDy;
                this.mDuration = mDuration;
                this.mInterpolator = mInterpolator;
                this.mChanged = true;
            }
        }
        
        public interface ScrollVectorProvider
        {
            @Nullable
            PointF computeScrollVectorForPosition(final int p0);
        }
    }
    
    public static class State
    {
        static final int STEP_ANIMATIONS = 4;
        static final int STEP_LAYOUT = 2;
        static final int STEP_START = 1;
        private SparseArray<Object> mData;
        int mDeletedInvisibleItemCountSincePreviousLayout;
        long mFocusedItemId;
        int mFocusedItemPosition;
        int mFocusedSubChildId;
        boolean mInPreLayout;
        boolean mIsMeasuring;
        int mItemCount;
        int mLayoutStep;
        int mPreviousLayoutItemCount;
        int mRemainingScrollHorizontal;
        int mRemainingScrollVertical;
        boolean mRunPredictiveAnimations;
        boolean mRunSimpleAnimations;
        boolean mStructureChanged;
        int mTargetPosition;
        boolean mTrackOldChangeHolders;
        
        public State() {
            this.mTargetPosition = -1;
            this.mPreviousLayoutItemCount = 0;
            this.mDeletedInvisibleItemCountSincePreviousLayout = 0;
            this.mLayoutStep = 1;
            this.mItemCount = 0;
            this.mStructureChanged = false;
            this.mInPreLayout = false;
            this.mTrackOldChangeHolders = false;
            this.mIsMeasuring = false;
            this.mRunSimpleAnimations = false;
            this.mRunPredictiveAnimations = false;
        }
        
        void assertLayoutStep(final int i) {
            if ((this.mLayoutStep & i) == 0x0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Layout state should be one of ");
                sb.append(Integer.toBinaryString(i));
                sb.append(" but it is ");
                sb.append(Integer.toBinaryString(this.mLayoutStep));
                throw new IllegalStateException(sb.toString());
            }
        }
        
        public boolean didStructureChange() {
            return this.mStructureChanged;
        }
        
        public <T> T get(final int n) {
            if (this.mData == null) {
                return null;
            }
            return (T)this.mData.get(n);
        }
        
        public int getItemCount() {
            int mItemCount;
            if (this.mInPreLayout) {
                mItemCount = this.mPreviousLayoutItemCount - this.mDeletedInvisibleItemCountSincePreviousLayout;
            }
            else {
                mItemCount = this.mItemCount;
            }
            return mItemCount;
        }
        
        public int getRemainingScrollHorizontal() {
            return this.mRemainingScrollHorizontal;
        }
        
        public int getRemainingScrollVertical() {
            return this.mRemainingScrollVertical;
        }
        
        public int getTargetScrollPosition() {
            return this.mTargetPosition;
        }
        
        public boolean hasTargetScrollPosition() {
            return this.mTargetPosition != -1;
        }
        
        public boolean isMeasuring() {
            return this.mIsMeasuring;
        }
        
        public boolean isPreLayout() {
            return this.mInPreLayout;
        }
        
        void prepareForNestedPrefetch(final Adapter adapter) {
            this.mLayoutStep = 1;
            this.mItemCount = adapter.getItemCount();
            this.mInPreLayout = false;
            this.mTrackOldChangeHolders = false;
            this.mIsMeasuring = false;
        }
        
        public void put(final int n, final Object o) {
            if (this.mData == null) {
                this.mData = (SparseArray<Object>)new SparseArray();
            }
            this.mData.put(n, o);
        }
        
        public void remove(final int n) {
            if (this.mData == null) {
                return;
            }
            this.mData.remove(n);
        }
        
        State reset() {
            this.mTargetPosition = -1;
            if (this.mData != null) {
                this.mData.clear();
            }
            this.mItemCount = 0;
            this.mStructureChanged = false;
            this.mIsMeasuring = false;
            return this;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("State{mTargetPosition=");
            sb.append(this.mTargetPosition);
            sb.append(", mData=");
            sb.append(this.mData);
            sb.append(", mItemCount=");
            sb.append(this.mItemCount);
            sb.append(", mIsMeasuring=");
            sb.append(this.mIsMeasuring);
            sb.append(", mPreviousLayoutItemCount=");
            sb.append(this.mPreviousLayoutItemCount);
            sb.append(", mDeletedInvisibleItemCountSincePreviousLayout=");
            sb.append(this.mDeletedInvisibleItemCountSincePreviousLayout);
            sb.append(", mStructureChanged=");
            sb.append(this.mStructureChanged);
            sb.append(", mInPreLayout=");
            sb.append(this.mInPreLayout);
            sb.append(", mRunSimpleAnimations=");
            sb.append(this.mRunSimpleAnimations);
            sb.append(", mRunPredictiveAnimations=");
            sb.append(this.mRunPredictiveAnimations);
            sb.append('}');
            return sb.toString();
        }
        
        public boolean willRunPredictiveAnimations() {
            return this.mRunPredictiveAnimations;
        }
        
        public boolean willRunSimpleAnimations() {
            return this.mRunSimpleAnimations;
        }
    }
    
    public abstract static class ViewCacheExtension
    {
        @Nullable
        public abstract View getViewForPositionAndType(@NonNull final Recycler p0, final int p1, final int p2);
    }
    
    class ViewFlinger implements Runnable
    {
        private boolean mEatRunOnAnimationRequest;
        Interpolator mInterpolator;
        private int mLastFlingX;
        private int mLastFlingY;
        private boolean mReSchedulePostAnimationCallback;
        OverScroller mScroller;
        final RecyclerView this$0;
        
        ViewFlinger(final RecyclerView this$0) {
            this.this$0 = this$0;
            this.mInterpolator = RecyclerView.sQuinticInterpolator;
            this.mEatRunOnAnimationRequest = false;
            this.mReSchedulePostAnimationCallback = false;
            this.mScroller = new OverScroller(this$0.getContext(), RecyclerView.sQuinticInterpolator);
        }
        
        private int computeScrollDuration(int n, int a, int n2, int n3) {
            final int abs = Math.abs(n);
            final int abs2 = Math.abs(a);
            final boolean b = abs > abs2;
            n2 = (int)Math.sqrt(n2 * n2 + n3 * n3);
            a = (int)Math.sqrt(n * n + a * a);
            if (b) {
                n = this.this$0.getWidth();
            }
            else {
                n = this.this$0.getHeight();
            }
            n3 = n / 2;
            final float n4 = (float)a;
            final float n5 = (float)n;
            final float min = Math.min(1.0f, n4 * 1.0f / n5);
            final float n6 = (float)n3;
            final float distanceInfluenceForSnapDuration = this.distanceInfluenceForSnapDuration(min);
            if (n2 > 0) {
                n = 4 * Math.round(1000.0f * Math.abs((n6 + distanceInfluenceForSnapDuration * n6) / n2));
            }
            else {
                if (b) {
                    n = abs;
                }
                else {
                    n = abs2;
                }
                n = (int)((n / n5 + 1.0f) * 300.0f);
            }
            return Math.min(n, 2000);
        }
        
        private void disableRunOnAnimationRequests() {
            this.mReSchedulePostAnimationCallback = false;
            this.mEatRunOnAnimationRequest = true;
        }
        
        private float distanceInfluenceForSnapDuration(final float n) {
            return (float)Math.sin((n - 0.5f) * 0.47123894f);
        }
        
        private void enableRunOnAnimationRequests() {
            this.mEatRunOnAnimationRequest = false;
            if (this.mReSchedulePostAnimationCallback) {
                this.postOnAnimation();
            }
        }
        
        public void fling(final int n, final int n2) {
            this.this$0.setScrollState(2);
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            this.mScroller.fling(0, 0, n, n2, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE);
            this.postOnAnimation();
        }
        
        void postOnAnimation() {
            if (this.mEatRunOnAnimationRequest) {
                this.mReSchedulePostAnimationCallback = true;
            }
            else {
                this.this$0.removeCallbacks((Runnable)this);
                ViewCompat.postOnAnimation((View)this.this$0, this);
            }
        }
        
        @Override
        public void run() {
            if (this.this$0.mLayout == null) {
                this.stop();
                return;
            }
            this.disableRunOnAnimationRequests();
            this.this$0.consumePendingUpdateOperations();
            final OverScroller mScroller = this.mScroller;
            final SmoothScroller mSmoothScroller = this.this$0.mLayout.mSmoothScroller;
            if (mScroller.computeScrollOffset()) {
                final int[] mScrollConsumed = this.this$0.mScrollConsumed;
                final int currX = mScroller.getCurrX();
                final int currY = mScroller.getCurrY();
                final int n = currX - this.mLastFlingX;
                final int n2 = currY - this.mLastFlingY;
                this.mLastFlingX = currX;
                this.mLastFlingY = currY;
                int n3 = n;
                int n4 = n2;
                if (this.this$0.dispatchNestedPreScroll(n, n2, mScrollConsumed, null, 1)) {
                    n3 = n - mScrollConsumed[0];
                    n4 = n2 - mScrollConsumed[1];
                }
                int n9;
                int n10;
                int n11;
                int n12;
                if (this.this$0.mAdapter != null) {
                    this.this$0.scrollStep(n3, n4, this.this$0.mScrollStepConsumed);
                    final int n5 = this.this$0.mScrollStepConsumed[0];
                    final int n6 = this.this$0.mScrollStepConsumed[1];
                    final int n7 = n3 - n5;
                    final int n8 = n4 - n6;
                    n9 = n5;
                    n10 = n6;
                    n11 = n7;
                    n12 = n8;
                    if (mSmoothScroller != null) {
                        n9 = n5;
                        n10 = n6;
                        n11 = n7;
                        n12 = n8;
                        if (!mSmoothScroller.isPendingInitialRun()) {
                            n9 = n5;
                            n10 = n6;
                            n11 = n7;
                            n12 = n8;
                            if (mSmoothScroller.isRunning()) {
                                final int itemCount = this.this$0.mState.getItemCount();
                                if (itemCount == 0) {
                                    mSmoothScroller.stop();
                                    n9 = n5;
                                    n10 = n6;
                                    n11 = n7;
                                    n12 = n8;
                                }
                                else if (mSmoothScroller.getTargetPosition() >= itemCount) {
                                    mSmoothScroller.setTargetPosition(itemCount - 1);
                                    mSmoothScroller.onAnimation(n3 - n7, n4 - n8);
                                    n9 = n5;
                                    n10 = n6;
                                    n11 = n7;
                                    n12 = n8;
                                }
                                else {
                                    mSmoothScroller.onAnimation(n3 - n7, n4 - n8);
                                    n9 = n5;
                                    n10 = n6;
                                    n11 = n7;
                                    n12 = n8;
                                }
                            }
                        }
                    }
                }
                else {
                    n9 = 0;
                    n10 = 0;
                    n12 = (n11 = n10);
                }
                if (!this.this$0.mItemDecorations.isEmpty()) {
                    this.this$0.invalidate();
                }
                if (this.this$0.getOverScrollMode() != 2) {
                    this.this$0.considerReleasingGlowsOnScroll(n3, n4);
                }
                if (!this.this$0.dispatchNestedScroll(n9, n10, n11, n12, null, 1) && (n11 != 0 || n12 != 0)) {
                    int n13 = (int)mScroller.getCurrVelocity();
                    int n14 = 0;
                    Label_0502: {
                        if (n11 != currX) {
                            if (n11 < 0) {
                                n14 = -n13;
                                break Label_0502;
                            }
                            if (n11 > 0) {
                                n14 = n13;
                                break Label_0502;
                            }
                        }
                        n14 = 0;
                    }
                    Label_0530: {
                        if (n12 != currY) {
                            if (n12 < 0) {
                                n13 = -n13;
                                break Label_0530;
                            }
                            if (n12 > 0) {
                                break Label_0530;
                            }
                        }
                        n13 = 0;
                    }
                    if (this.this$0.getOverScrollMode() != 2) {
                        this.this$0.absorbGlows(n14, n13);
                    }
                    if ((n14 != 0 || n11 == currX || mScroller.getFinalX() == 0) && (n13 != 0 || n12 == currY || mScroller.getFinalY() == 0)) {
                        mScroller.abortAnimation();
                    }
                }
                if (n9 != 0 || n10 != 0) {
                    this.this$0.dispatchOnScrolled(n9, n10);
                }
                if (!RecyclerView.access$200(this.this$0)) {
                    this.this$0.invalidate();
                }
                final boolean b = n4 != 0 && this.this$0.mLayout.canScrollVertically() && n10 == n4;
                final boolean b2 = n3 != 0 && this.this$0.mLayout.canScrollHorizontally() && n9 == n3;
                final boolean b3 = (n3 == 0 && n4 == 0) || b2 || b;
                if (!mScroller.isFinished() && (b3 || this.this$0.hasNestedScrollingParent(1))) {
                    this.postOnAnimation();
                    if (this.this$0.mGapWorker != null) {
                        this.this$0.mGapWorker.postFromTraversal(this.this$0, n3, n4);
                    }
                }
                else {
                    this.this$0.setScrollState(0);
                    if (RecyclerView.ALLOW_THREAD_GAP_WORK) {
                        this.this$0.mPrefetchRegistry.clearPrefetchPositions();
                    }
                    this.this$0.stopNestedScroll(1);
                }
            }
            if (mSmoothScroller != null) {
                if (mSmoothScroller.isPendingInitialRun()) {
                    mSmoothScroller.onAnimation(0, 0);
                }
                if (!this.mReSchedulePostAnimationCallback) {
                    mSmoothScroller.stop();
                }
            }
            this.enableRunOnAnimationRequests();
        }
        
        public void smoothScrollBy(final int n, final int n2) {
            this.smoothScrollBy(n, n2, 0, 0);
        }
        
        public void smoothScrollBy(final int n, final int n2, final int n3) {
            this.smoothScrollBy(n, n2, n3, RecyclerView.sQuinticInterpolator);
        }
        
        public void smoothScrollBy(final int n, final int n2, final int n3, final int n4) {
            this.smoothScrollBy(n, n2, this.computeScrollDuration(n, n2, n3, n4));
        }
        
        public void smoothScrollBy(final int n, final int n2, final int n3, final Interpolator mInterpolator) {
            if (this.mInterpolator != mInterpolator) {
                this.mInterpolator = mInterpolator;
                this.mScroller = new OverScroller(this.this$0.getContext(), mInterpolator);
            }
            this.this$0.setScrollState(2);
            this.mLastFlingY = 0;
            this.mLastFlingX = 0;
            this.mScroller.startScroll(0, 0, n, n2, n3);
            if (Build$VERSION.SDK_INT < 23) {
                this.mScroller.computeScrollOffset();
            }
            this.postOnAnimation();
        }
        
        public void smoothScrollBy(final int n, final int n2, final Interpolator interpolator) {
            final int computeScrollDuration = this.computeScrollDuration(n, n2, 0, 0);
            Interpolator sQuinticInterpolator = interpolator;
            if (interpolator == null) {
                sQuinticInterpolator = RecyclerView.sQuinticInterpolator;
            }
            this.smoothScrollBy(n, n2, computeScrollDuration, sQuinticInterpolator);
        }
        
        public void stop() {
            this.this$0.removeCallbacks((Runnable)this);
            this.mScroller.abortAnimation();
        }
    }
    
    public abstract static class ViewHolder
    {
        static final int FLAG_ADAPTER_FULLUPDATE = 1024;
        static final int FLAG_ADAPTER_POSITION_UNKNOWN = 512;
        static final int FLAG_APPEARED_IN_PRE_LAYOUT = 4096;
        static final int FLAG_BOUNCED_FROM_HIDDEN_LIST = 8192;
        static final int FLAG_BOUND = 1;
        static final int FLAG_IGNORE = 128;
        static final int FLAG_INVALID = 4;
        static final int FLAG_MOVED = 2048;
        static final int FLAG_NOT_RECYCLABLE = 16;
        static final int FLAG_REMOVED = 8;
        static final int FLAG_RETURNED_FROM_SCRAP = 32;
        static final int FLAG_SET_A11Y_ITEM_DELEGATE = 16384;
        static final int FLAG_TMP_DETACHED = 256;
        static final int FLAG_UPDATE = 2;
        private static final List<Object> FULLUPDATE_PAYLOADS;
        static final int PENDING_ACCESSIBILITY_STATE_NOT_SET = -1;
        @NonNull
        public final View itemView;
        int mFlags;
        boolean mInChangeScrap;
        private int mIsRecyclableCount;
        long mItemId;
        int mItemViewType;
        WeakReference<RecyclerView> mNestedRecyclerView;
        int mOldPosition;
        RecyclerView mOwnerRecyclerView;
        List<Object> mPayloads;
        @VisibleForTesting
        int mPendingAccessibilityState;
        int mPosition;
        int mPreLayoutPosition;
        Recycler mScrapContainer;
        ViewHolder mShadowedHolder;
        ViewHolder mShadowingHolder;
        List<Object> mUnmodifiedPayloads;
        private int mWasImportantForAccessibilityBeforeHidden;
        
        static {
            FULLUPDATE_PAYLOADS = Collections.emptyList();
        }
        
        public ViewHolder(@NonNull final View itemView) {
            this.mPosition = -1;
            this.mOldPosition = -1;
            this.mItemId = -1L;
            this.mItemViewType = -1;
            this.mPreLayoutPosition = -1;
            this.mShadowedHolder = null;
            this.mShadowingHolder = null;
            this.mPayloads = null;
            this.mUnmodifiedPayloads = null;
            this.mIsRecyclableCount = 0;
            this.mScrapContainer = null;
            this.mInChangeScrap = false;
            this.mWasImportantForAccessibilityBeforeHidden = 0;
            this.mPendingAccessibilityState = -1;
            if (itemView == null) {
                throw new IllegalArgumentException("itemView may not be null");
            }
            this.itemView = itemView;
        }
        
        private void createPayloadsIfNeeded() {
            if (this.mPayloads == null) {
                this.mPayloads = new ArrayList<Object>();
                this.mUnmodifiedPayloads = Collections.unmodifiableList((List<?>)this.mPayloads);
            }
        }
        
        void addChangePayload(final Object o) {
            if (o == null) {
                this.addFlags(1024);
            }
            else if ((0x400 & this.mFlags) == 0x0) {
                this.createPayloadsIfNeeded();
                this.mPayloads.add(o);
            }
        }
        
        void addFlags(final int n) {
            this.mFlags |= n;
        }
        
        void clearOldPosition() {
            this.mOldPosition = -1;
            this.mPreLayoutPosition = -1;
        }
        
        void clearPayload() {
            if (this.mPayloads != null) {
                this.mPayloads.clear();
            }
            this.mFlags &= 0xFFFFFBFF;
        }
        
        void clearReturnedFromScrapFlag() {
            this.mFlags &= 0xFFFFFFDF;
        }
        
        void clearTmpDetachFlag() {
            this.mFlags &= 0xFFFFFEFF;
        }
        
        boolean doesTransientStatePreventRecycling() {
            return (this.mFlags & 0x10) == 0x0 && ViewCompat.hasTransientState(this.itemView);
        }
        
        void flagRemovedAndOffsetPosition(final int mPosition, final int n, final boolean b) {
            this.addFlags(8);
            this.offsetPosition(n, b);
            this.mPosition = mPosition;
        }
        
        public final int getAdapterPosition() {
            if (this.mOwnerRecyclerView == null) {
                return -1;
            }
            return this.mOwnerRecyclerView.getAdapterPositionFor(this);
        }
        
        public final long getItemId() {
            return this.mItemId;
        }
        
        public final int getItemViewType() {
            return this.mItemViewType;
        }
        
        public final int getLayoutPosition() {
            int n;
            if (this.mPreLayoutPosition == -1) {
                n = this.mPosition;
            }
            else {
                n = this.mPreLayoutPosition;
            }
            return n;
        }
        
        public final int getOldPosition() {
            return this.mOldPosition;
        }
        
        @Deprecated
        public final int getPosition() {
            int n;
            if (this.mPreLayoutPosition == -1) {
                n = this.mPosition;
            }
            else {
                n = this.mPreLayoutPosition;
            }
            return n;
        }
        
        List<Object> getUnmodifiedPayloads() {
            if ((this.mFlags & 0x400) != 0x0) {
                return ViewHolder.FULLUPDATE_PAYLOADS;
            }
            if (this.mPayloads != null && this.mPayloads.size() != 0) {
                return this.mUnmodifiedPayloads;
            }
            return ViewHolder.FULLUPDATE_PAYLOADS;
        }
        
        boolean hasAnyOfTheFlags(final int n) {
            return (this.mFlags & n) != 0x0;
        }
        
        boolean isAdapterPositionUnknown() {
            return (this.mFlags & 0x200) != 0x0 || this.isInvalid();
        }
        
        boolean isBound() {
            final int mFlags = this.mFlags;
            boolean b = true;
            if ((mFlags & 0x1) == 0x0) {
                b = false;
            }
            return b;
        }
        
        boolean isInvalid() {
            return (this.mFlags & 0x4) != 0x0;
        }
        
        public final boolean isRecyclable() {
            return (this.mFlags & 0x10) == 0x0 && !ViewCompat.hasTransientState(this.itemView);
        }
        
        boolean isRemoved() {
            return (this.mFlags & 0x8) != 0x0;
        }
        
        boolean isScrap() {
            return this.mScrapContainer != null;
        }
        
        boolean isTmpDetached() {
            return (this.mFlags & 0x100) != 0x0;
        }
        
        boolean isUpdated() {
            return (this.mFlags & 0x2) != 0x0;
        }
        
        boolean needsUpdate() {
            return (this.mFlags & 0x2) != 0x0;
        }
        
        void offsetPosition(final int n, final boolean b) {
            if (this.mOldPosition == -1) {
                this.mOldPosition = this.mPosition;
            }
            if (this.mPreLayoutPosition == -1) {
                this.mPreLayoutPosition = this.mPosition;
            }
            if (b) {
                this.mPreLayoutPosition += n;
            }
            this.mPosition += n;
            if (this.itemView.getLayoutParams() != null) {
                ((LayoutParams)this.itemView.getLayoutParams()).mInsetsDirty = true;
            }
        }
        
        void onEnteredHiddenState(final RecyclerView recyclerView) {
            if (this.mPendingAccessibilityState != -1) {
                this.mWasImportantForAccessibilityBeforeHidden = this.mPendingAccessibilityState;
            }
            else {
                this.mWasImportantForAccessibilityBeforeHidden = ViewCompat.getImportantForAccessibility(this.itemView);
            }
            recyclerView.setChildImportantForAccessibilityInternal(this, 4);
        }
        
        void onLeftHiddenState(final RecyclerView recyclerView) {
            recyclerView.setChildImportantForAccessibilityInternal(this, this.mWasImportantForAccessibilityBeforeHidden);
            this.mWasImportantForAccessibilityBeforeHidden = 0;
        }
        
        void resetInternal() {
            this.mFlags = 0;
            this.mPosition = -1;
            this.mOldPosition = -1;
            this.mItemId = -1L;
            this.mPreLayoutPosition = -1;
            this.mIsRecyclableCount = 0;
            this.mShadowedHolder = null;
            this.mShadowingHolder = null;
            this.clearPayload();
            this.mWasImportantForAccessibilityBeforeHidden = 0;
            this.mPendingAccessibilityState = -1;
            RecyclerView.clearNestedRecyclerViewIfNotNested(this);
        }
        
        void saveOldPosition() {
            if (this.mOldPosition == -1) {
                this.mOldPosition = this.mPosition;
            }
        }
        
        void setFlags(final int n, final int n2) {
            this.mFlags = ((n & n2) | (this.mFlags & ~n2));
        }
        
        public final void setIsRecyclable(final boolean b) {
            int mIsRecyclableCount;
            if (b) {
                mIsRecyclableCount = this.mIsRecyclableCount - 1;
            }
            else {
                mIsRecyclableCount = this.mIsRecyclableCount + 1;
            }
            this.mIsRecyclableCount = mIsRecyclableCount;
            if (this.mIsRecyclableCount < 0) {
                this.mIsRecyclableCount = 0;
                final StringBuilder sb = new StringBuilder();
                sb.append("isRecyclable decremented below 0: unmatched pair of setIsRecyable() calls for ");
                sb.append(this);
                Log.e("View", sb.toString());
            }
            else if (!b && this.mIsRecyclableCount == 1) {
                this.mFlags |= 0x10;
            }
            else if (b && this.mIsRecyclableCount == 0) {
                this.mFlags &= 0xFFFFFFEF;
            }
        }
        
        void setScrapContainer(final Recycler mScrapContainer, final boolean mInChangeScrap) {
            this.mScrapContainer = mScrapContainer;
            this.mInChangeScrap = mInChangeScrap;
        }
        
        boolean shouldBeKeptAsChild() {
            return (this.mFlags & 0x10) != 0x0;
        }
        
        boolean shouldIgnore() {
            return (this.mFlags & 0x80) != 0x0;
        }
        
        void stopIgnoring() {
            this.mFlags &= 0xFFFFFF7F;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("ViewHolder{");
            sb.append(Integer.toHexString(this.hashCode()));
            sb.append(" position=");
            sb.append(this.mPosition);
            sb.append(" id=");
            sb.append(this.mItemId);
            sb.append(", oldPos=");
            sb.append(this.mOldPosition);
            sb.append(", pLpos:");
            sb.append(this.mPreLayoutPosition);
            final StringBuilder sb2 = new StringBuilder(sb.toString());
            if (this.isScrap()) {
                sb2.append(" scrap ");
                String str;
                if (this.mInChangeScrap) {
                    str = "[changeScrap]";
                }
                else {
                    str = "[attachedScrap]";
                }
                sb2.append(str);
            }
            if (this.isInvalid()) {
                sb2.append(" invalid");
            }
            if (!this.isBound()) {
                sb2.append(" unbound");
            }
            if (this.needsUpdate()) {
                sb2.append(" update");
            }
            if (this.isRemoved()) {
                sb2.append(" removed");
            }
            if (this.shouldIgnore()) {
                sb2.append(" ignored");
            }
            if (this.isTmpDetached()) {
                sb2.append(" tmpDetached");
            }
            if (!this.isRecyclable()) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(" not recyclable(");
                sb3.append(this.mIsRecyclableCount);
                sb3.append(")");
                sb2.append(sb3.toString());
            }
            if (this.isAdapterPositionUnknown()) {
                sb2.append(" undefined adapter position");
            }
            if (this.itemView.getParent() == null) {
                sb2.append(" no parent");
            }
            sb2.append("}");
            return sb2.toString();
        }
        
        void unScrap() {
            this.mScrapContainer.unscrapView(this);
        }
        
        boolean wasReturnedFromScrap() {
            return (this.mFlags & 0x20) != 0x0;
        }
    }
}
