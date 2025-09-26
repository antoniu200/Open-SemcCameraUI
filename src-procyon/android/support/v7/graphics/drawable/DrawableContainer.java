// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.graphics.drawable;

import android.util.SparseArray;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.drawable.Drawable$ConstantState;
import android.graphics.Canvas;
import android.support.annotation.RequiresApi;
import android.support.annotation.NonNull;
import android.content.res.Resources$Theme;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.content.res.Resources;
import android.annotation.TargetApi;
import android.annotation.SuppressLint;
import android.os.Build$VERSION;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.graphics.Rect;
import android.support.annotation.RestrictTo;
import android.graphics.drawable.Drawable$Callback;
import android.graphics.drawable.Drawable;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
class DrawableContainer extends Drawable implements Drawable$Callback
{
    private static final boolean DEBUG = false;
    private static final boolean DEFAULT_DITHER = true;
    private static final String TAG = "DrawableContainer";
    private int mAlpha;
    private Runnable mAnimationRunnable;
    private BlockInvalidateCallback mBlockInvalidateCallback;
    private int mCurIndex;
    private Drawable mCurrDrawable;
    private DrawableContainerState mDrawableContainerState;
    private long mEnterAnimationEnd;
    private long mExitAnimationEnd;
    private boolean mHasAlpha;
    private Rect mHotspotBounds;
    private Drawable mLastDrawable;
    private int mLastIndex;
    private boolean mMutated;
    
    DrawableContainer() {
        this.mAlpha = 255;
        this.mCurIndex = -1;
        this.mLastIndex = -1;
    }
    
    private void initializeDrawableForDisplay(final Drawable drawable) {
        if (this.mBlockInvalidateCallback == null) {
            this.mBlockInvalidateCallback = new BlockInvalidateCallback();
        }
        drawable.setCallback((Drawable$Callback)this.mBlockInvalidateCallback.wrap(drawable.getCallback()));
        try {
            if (this.mDrawableContainerState.mEnterFadeDuration <= 0 && this.mHasAlpha) {
                drawable.setAlpha(this.mAlpha);
            }
            if (this.mDrawableContainerState.mHasColorFilter) {
                drawable.setColorFilter(this.mDrawableContainerState.mColorFilter);
            }
            else {
                if (this.mDrawableContainerState.mHasTintList) {
                    DrawableCompat.setTintList(drawable, this.mDrawableContainerState.mTintList);
                }
                if (this.mDrawableContainerState.mHasTintMode) {
                    DrawableCompat.setTintMode(drawable, this.mDrawableContainerState.mTintMode);
                }
            }
            drawable.setVisible(this.isVisible(), true);
            drawable.setDither(this.mDrawableContainerState.mDither);
            drawable.setState(this.getState());
            drawable.setLevel(this.getLevel());
            drawable.setBounds(this.getBounds());
            if (Build$VERSION.SDK_INT >= 23) {
                drawable.setLayoutDirection(this.getLayoutDirection());
            }
            if (Build$VERSION.SDK_INT >= 19) {
                drawable.setAutoMirrored(this.mDrawableContainerState.mAutoMirrored);
            }
            final Rect mHotspotBounds = this.mHotspotBounds;
            if (Build$VERSION.SDK_INT >= 21 && mHotspotBounds != null) {
                drawable.setHotspotBounds(mHotspotBounds.left, mHotspotBounds.top, mHotspotBounds.right, mHotspotBounds.bottom);
            }
        }
        finally {
            drawable.setCallback(this.mBlockInvalidateCallback.unwrap());
        }
    }
    
    @SuppressLint({ "WrongConstant" })
    @TargetApi(23)
    private boolean needsMirroring() {
        final boolean autoMirrored = this.isAutoMirrored();
        boolean b = true;
        if (!autoMirrored || this.getLayoutDirection() != 1) {
            b = false;
        }
        return b;
    }
    
    static int resolveDensity(@Nullable final Resources resources, int densityDpi) {
        if (resources != null) {
            densityDpi = resources.getDisplayMetrics().densityDpi;
        }
        int n = densityDpi;
        if (densityDpi == 0) {
            n = 160;
        }
        return n;
    }
    
    void animate(final boolean b) {
        final int n = 1;
        this.mHasAlpha = true;
        final long uptimeMillis = SystemClock.uptimeMillis();
        int n2 = 0;
        Label_0111: {
            if (this.mCurrDrawable != null) {
                if (this.mEnterAnimationEnd != 0L) {
                    if (this.mEnterAnimationEnd > uptimeMillis) {
                        this.mCurrDrawable.setAlpha((255 - (int)((this.mEnterAnimationEnd - uptimeMillis) * 255L) / this.mDrawableContainerState.mEnterFadeDuration) * this.mAlpha / 255);
                        n2 = 1;
                        break Label_0111;
                    }
                    this.mCurrDrawable.setAlpha(this.mAlpha);
                    this.mEnterAnimationEnd = 0L;
                }
            }
            else {
                this.mEnterAnimationEnd = 0L;
            }
            n2 = 0;
        }
        if (this.mLastDrawable != null) {
            if (this.mExitAnimationEnd != 0L) {
                if (this.mExitAnimationEnd <= uptimeMillis) {
                    this.mLastDrawable.setVisible(false, false);
                    this.mLastDrawable = null;
                    this.mLastIndex = -1;
                    this.mExitAnimationEnd = 0L;
                }
                else {
                    this.mLastDrawable.setAlpha((int)((this.mExitAnimationEnd - uptimeMillis) * 255L) / this.mDrawableContainerState.mExitFadeDuration * this.mAlpha / 255);
                    n2 = n;
                }
            }
        }
        else {
            this.mExitAnimationEnd = 0L;
        }
        if (b && n2 != 0) {
            this.scheduleSelf(this.mAnimationRunnable, uptimeMillis + 16L);
        }
    }
    
    @RequiresApi(21)
    public void applyTheme(@NonNull final Resources$Theme resources$Theme) {
        this.mDrawableContainerState.applyTheme(resources$Theme);
    }
    
    @RequiresApi(21)
    public boolean canApplyTheme() {
        return this.mDrawableContainerState.canApplyTheme();
    }
    
    void clearMutated() {
        this.mDrawableContainerState.clearMutated();
        this.mMutated = false;
    }
    
    DrawableContainerState cloneConstantState() {
        return this.mDrawableContainerState;
    }
    
    public void draw(@NonNull final Canvas canvas) {
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.draw(canvas);
        }
        if (this.mLastDrawable != null) {
            this.mLastDrawable.draw(canvas);
        }
    }
    
    public int getAlpha() {
        return this.mAlpha;
    }
    
    public int getChangingConfigurations() {
        return this.mDrawableContainerState.getChangingConfigurations() | super.getChangingConfigurations();
    }
    
    public final Drawable$ConstantState getConstantState() {
        if (this.mDrawableContainerState.canConstantState()) {
            this.mDrawableContainerState.mChangingConfigurations = this.getChangingConfigurations();
            return this.mDrawableContainerState;
        }
        return null;
    }
    
    @NonNull
    public Drawable getCurrent() {
        return this.mCurrDrawable;
    }
    
    int getCurrentIndex() {
        return this.mCurIndex;
    }
    
    public void getHotspotBounds(@NonNull final Rect rect) {
        if (this.mHotspotBounds != null) {
            rect.set(this.mHotspotBounds);
        }
        else {
            super.getHotspotBounds(rect);
        }
    }
    
    public int getIntrinsicHeight() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantHeight();
        }
        int intrinsicHeight;
        if (this.mCurrDrawable != null) {
            intrinsicHeight = this.mCurrDrawable.getIntrinsicHeight();
        }
        else {
            intrinsicHeight = -1;
        }
        return intrinsicHeight;
    }
    
    public int getIntrinsicWidth() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantWidth();
        }
        int intrinsicWidth;
        if (this.mCurrDrawable != null) {
            intrinsicWidth = this.mCurrDrawable.getIntrinsicWidth();
        }
        else {
            intrinsicWidth = -1;
        }
        return intrinsicWidth;
    }
    
    public int getMinimumHeight() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantMinimumHeight();
        }
        int minimumHeight;
        if (this.mCurrDrawable != null) {
            minimumHeight = this.mCurrDrawable.getMinimumHeight();
        }
        else {
            minimumHeight = 0;
        }
        return minimumHeight;
    }
    
    public int getMinimumWidth() {
        if (this.mDrawableContainerState.isConstantSize()) {
            return this.mDrawableContainerState.getConstantMinimumWidth();
        }
        int minimumWidth;
        if (this.mCurrDrawable != null) {
            minimumWidth = this.mCurrDrawable.getMinimumWidth();
        }
        else {
            minimumWidth = 0;
        }
        return minimumWidth;
    }
    
    public int getOpacity() {
        int opacity;
        if (this.mCurrDrawable != null && this.mCurrDrawable.isVisible()) {
            opacity = this.mDrawableContainerState.getOpacity();
        }
        else {
            opacity = -2;
        }
        return opacity;
    }
    
    @RequiresApi(21)
    public void getOutline(@NonNull final Outline outline) {
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.getOutline(outline);
        }
    }
    
    public boolean getPadding(@NonNull final Rect rect) {
        final Rect constantPadding = this.mDrawableContainerState.getConstantPadding();
        boolean b;
        if (constantPadding != null) {
            rect.set(constantPadding);
            b = ((constantPadding.right | (constantPadding.left | constantPadding.top | constantPadding.bottom)) != 0x0);
        }
        else if (this.mCurrDrawable != null) {
            b = this.mCurrDrawable.getPadding(rect);
        }
        else {
            b = super.getPadding(rect);
        }
        if (this.needsMirroring()) {
            final int left = rect.left;
            rect.left = rect.right;
            rect.right = left;
        }
        return b;
    }
    
    public void invalidateDrawable(@NonNull final Drawable drawable) {
        if (this.mDrawableContainerState != null) {
            this.mDrawableContainerState.invalidateCache();
        }
        if (drawable == this.mCurrDrawable && this.getCallback() != null) {
            this.getCallback().invalidateDrawable((Drawable)this);
        }
    }
    
    public boolean isAutoMirrored() {
        return this.mDrawableContainerState.mAutoMirrored;
    }
    
    public boolean isStateful() {
        return this.mDrawableContainerState.isStateful();
    }
    
    public void jumpToCurrentState() {
        boolean b;
        if (this.mLastDrawable != null) {
            this.mLastDrawable.jumpToCurrentState();
            this.mLastDrawable = null;
            this.mLastIndex = -1;
            b = true;
        }
        else {
            b = false;
        }
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.jumpToCurrentState();
            if (this.mHasAlpha) {
                this.mCurrDrawable.setAlpha(this.mAlpha);
            }
        }
        if (this.mExitAnimationEnd != 0L) {
            this.mExitAnimationEnd = 0L;
            b = true;
        }
        if (this.mEnterAnimationEnd != 0L) {
            this.mEnterAnimationEnd = 0L;
            b = true;
        }
        if (b) {
            this.invalidateSelf();
        }
    }
    
    @NonNull
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            final DrawableContainerState cloneConstantState = this.cloneConstantState();
            cloneConstantState.mutate();
            this.setConstantState(cloneConstantState);
            this.mMutated = true;
        }
        return this;
    }
    
    protected void onBoundsChange(final Rect rect) {
        if (this.mLastDrawable != null) {
            this.mLastDrawable.setBounds(rect);
        }
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.setBounds(rect);
        }
    }
    
    public boolean onLayoutDirectionChanged(final int n) {
        return this.mDrawableContainerState.setLayoutDirection(n, this.getCurrentIndex());
    }
    
    protected boolean onLevelChange(final int n) {
        if (this.mLastDrawable != null) {
            return this.mLastDrawable.setLevel(n);
        }
        return this.mCurrDrawable != null && this.mCurrDrawable.setLevel(n);
    }
    
    protected boolean onStateChange(final int[] array) {
        if (this.mLastDrawable != null) {
            return this.mLastDrawable.setState(array);
        }
        return this.mCurrDrawable != null && this.mCurrDrawable.setState(array);
    }
    
    public void scheduleDrawable(@NonNull final Drawable drawable, @NonNull final Runnable runnable, final long n) {
        if (drawable == this.mCurrDrawable && this.getCallback() != null) {
            this.getCallback().scheduleDrawable((Drawable)this, runnable, n);
        }
    }
    
    boolean selectDrawable(final int mCurIndex) {
        if (mCurIndex == this.mCurIndex) {
            return false;
        }
        final long uptimeMillis = SystemClock.uptimeMillis();
        if (this.mDrawableContainerState.mExitFadeDuration > 0) {
            if (this.mLastDrawable != null) {
                this.mLastDrawable.setVisible(false, false);
            }
            if (this.mCurrDrawable != null) {
                this.mLastDrawable = this.mCurrDrawable;
                this.mLastIndex = this.mCurIndex;
                this.mExitAnimationEnd = this.mDrawableContainerState.mExitFadeDuration + uptimeMillis;
            }
            else {
                this.mLastDrawable = null;
                this.mLastIndex = -1;
                this.mExitAnimationEnd = 0L;
            }
        }
        else if (this.mCurrDrawable != null) {
            this.mCurrDrawable.setVisible(false, false);
        }
        if (mCurIndex >= 0 && mCurIndex < this.mDrawableContainerState.mNumChildren) {
            final Drawable child = this.mDrawableContainerState.getChild(mCurIndex);
            this.mCurrDrawable = child;
            this.mCurIndex = mCurIndex;
            if (child != null) {
                if (this.mDrawableContainerState.mEnterFadeDuration > 0) {
                    this.mEnterAnimationEnd = uptimeMillis + this.mDrawableContainerState.mEnterFadeDuration;
                }
                this.initializeDrawableForDisplay(child);
            }
        }
        else {
            this.mCurrDrawable = null;
            this.mCurIndex = -1;
        }
        if (this.mEnterAnimationEnd != 0L || this.mExitAnimationEnd != 0L) {
            if (this.mAnimationRunnable == null) {
                this.mAnimationRunnable = new Runnable(this) {
                    final DrawableContainer this$0;
                    
                    @Override
                    public void run() {
                        this.this$0.animate(true);
                        this.this$0.invalidateSelf();
                    }
                };
            }
            else {
                this.unscheduleSelf(this.mAnimationRunnable);
            }
            this.animate(true);
        }
        this.invalidateSelf();
        return true;
    }
    
    public void setAlpha(final int n) {
        if (!this.mHasAlpha || this.mAlpha != n) {
            this.mHasAlpha = true;
            this.mAlpha = n;
            if (this.mCurrDrawable != null) {
                if (this.mEnterAnimationEnd == 0L) {
                    this.mCurrDrawable.setAlpha(n);
                }
                else {
                    this.animate(false);
                }
            }
        }
    }
    
    public void setAutoMirrored(final boolean mAutoMirrored) {
        if (this.mDrawableContainerState.mAutoMirrored != mAutoMirrored) {
            this.mDrawableContainerState.mAutoMirrored = mAutoMirrored;
            if (this.mCurrDrawable != null) {
                DrawableCompat.setAutoMirrored(this.mCurrDrawable, this.mDrawableContainerState.mAutoMirrored);
            }
        }
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.mDrawableContainerState.mHasColorFilter = true;
        if (this.mDrawableContainerState.mColorFilter != colorFilter) {
            this.mDrawableContainerState.mColorFilter = colorFilter;
            if (this.mCurrDrawable != null) {
                this.mCurrDrawable.setColorFilter(colorFilter);
            }
        }
    }
    
    protected void setConstantState(final DrawableContainerState mDrawableContainerState) {
        this.mDrawableContainerState = mDrawableContainerState;
        if (this.mCurIndex >= 0) {
            this.mCurrDrawable = mDrawableContainerState.getChild(this.mCurIndex);
            if (this.mCurrDrawable != null) {
                this.initializeDrawableForDisplay(this.mCurrDrawable);
            }
        }
        this.mLastIndex = -1;
        this.mLastDrawable = null;
    }
    
    void setCurrentIndex(final int n) {
        this.selectDrawable(n);
    }
    
    public void setDither(final boolean mDither) {
        if (this.mDrawableContainerState.mDither != mDither) {
            this.mDrawableContainerState.mDither = mDither;
            if (this.mCurrDrawable != null) {
                this.mCurrDrawable.setDither(this.mDrawableContainerState.mDither);
            }
        }
    }
    
    public void setEnterFadeDuration(final int mEnterFadeDuration) {
        this.mDrawableContainerState.mEnterFadeDuration = mEnterFadeDuration;
    }
    
    public void setExitFadeDuration(final int mExitFadeDuration) {
        this.mDrawableContainerState.mExitFadeDuration = mExitFadeDuration;
    }
    
    public void setHotspot(final float n, final float n2) {
        if (this.mCurrDrawable != null) {
            DrawableCompat.setHotspot(this.mCurrDrawable, n, n2);
        }
    }
    
    public void setHotspotBounds(final int n, final int n2, final int n3, final int n4) {
        if (this.mHotspotBounds == null) {
            this.mHotspotBounds = new Rect(n, n2, n3, n4);
        }
        else {
            this.mHotspotBounds.set(n, n2, n3, n4);
        }
        if (this.mCurrDrawable != null) {
            DrawableCompat.setHotspotBounds(this.mCurrDrawable, n, n2, n3, n4);
        }
    }
    
    public void setTintList(final ColorStateList mTintList) {
        this.mDrawableContainerState.mHasTintList = true;
        if (this.mDrawableContainerState.mTintList != mTintList) {
            this.mDrawableContainerState.mTintList = mTintList;
            DrawableCompat.setTintList(this.mCurrDrawable, mTintList);
        }
    }
    
    public void setTintMode(@NonNull final PorterDuff$Mode mTintMode) {
        this.mDrawableContainerState.mHasTintMode = true;
        if (this.mDrawableContainerState.mTintMode != mTintMode) {
            this.mDrawableContainerState.mTintMode = mTintMode;
            DrawableCompat.setTintMode(this.mCurrDrawable, mTintMode);
        }
    }
    
    public boolean setVisible(final boolean b, final boolean b2) {
        final boolean setVisible = super.setVisible(b, b2);
        if (this.mLastDrawable != null) {
            this.mLastDrawable.setVisible(b, b2);
        }
        if (this.mCurrDrawable != null) {
            this.mCurrDrawable.setVisible(b, b2);
        }
        return setVisible;
    }
    
    public void unscheduleDrawable(@NonNull final Drawable drawable, @NonNull final Runnable runnable) {
        if (drawable == this.mCurrDrawable && this.getCallback() != null) {
            this.getCallback().unscheduleDrawable((Drawable)this, runnable);
        }
    }
    
    final void updateDensity(final Resources resources) {
        this.mDrawableContainerState.updateDensity(resources);
    }
    
    static class BlockInvalidateCallback implements Drawable$Callback
    {
        private Drawable$Callback mCallback;
        
        public void invalidateDrawable(@NonNull final Drawable drawable) {
        }
        
        public void scheduleDrawable(@NonNull final Drawable drawable, @NonNull final Runnable runnable, final long n) {
            if (this.mCallback != null) {
                this.mCallback.scheduleDrawable(drawable, runnable, n);
            }
        }
        
        public void unscheduleDrawable(@NonNull final Drawable drawable, @NonNull final Runnable runnable) {
            if (this.mCallback != null) {
                this.mCallback.unscheduleDrawable(drawable, runnable);
            }
        }
        
        public Drawable$Callback unwrap() {
            final Drawable$Callback mCallback = this.mCallback;
            this.mCallback = null;
            return mCallback;
        }
        
        public BlockInvalidateCallback wrap(final Drawable$Callback mCallback) {
            this.mCallback = mCallback;
            return this;
        }
    }
    
    abstract static class DrawableContainerState extends Drawable$ConstantState
    {
        boolean mAutoMirrored;
        boolean mCanConstantState;
        int mChangingConfigurations;
        boolean mCheckedConstantSize;
        boolean mCheckedConstantState;
        boolean mCheckedOpacity;
        boolean mCheckedPadding;
        boolean mCheckedStateful;
        int mChildrenChangingConfigurations;
        ColorFilter mColorFilter;
        int mConstantHeight;
        int mConstantMinimumHeight;
        int mConstantMinimumWidth;
        Rect mConstantPadding;
        boolean mConstantSize;
        int mConstantWidth;
        int mDensity;
        boolean mDither;
        SparseArray<Drawable$ConstantState> mDrawableFutures;
        Drawable[] mDrawables;
        int mEnterFadeDuration;
        int mExitFadeDuration;
        boolean mHasColorFilter;
        boolean mHasTintList;
        boolean mHasTintMode;
        int mLayoutDirection;
        boolean mMutated;
        int mNumChildren;
        int mOpacity;
        final DrawableContainer mOwner;
        Resources mSourceRes;
        boolean mStateful;
        ColorStateList mTintList;
        PorterDuff$Mode mTintMode;
        boolean mVariablePadding;
        
        DrawableContainerState(final DrawableContainerState drawableContainerState, final DrawableContainer mOwner, final Resources resources) {
            this.mDensity = 160;
            final int n = 0;
            this.mVariablePadding = false;
            this.mConstantSize = false;
            this.mDither = true;
            this.mEnterFadeDuration = 0;
            this.mExitFadeDuration = 0;
            this.mOwner = mOwner;
            Resources mSourceRes;
            if (resources != null) {
                mSourceRes = resources;
            }
            else if (drawableContainerState != null) {
                mSourceRes = drawableContainerState.mSourceRes;
            }
            else {
                mSourceRes = null;
            }
            this.mSourceRes = mSourceRes;
            int mDensity;
            if (drawableContainerState != null) {
                mDensity = drawableContainerState.mDensity;
            }
            else {
                mDensity = 0;
            }
            this.mDensity = DrawableContainer.resolveDensity(resources, mDensity);
            if (drawableContainerState != null) {
                this.mChangingConfigurations = drawableContainerState.mChangingConfigurations;
                this.mChildrenChangingConfigurations = drawableContainerState.mChildrenChangingConfigurations;
                this.mCheckedConstantState = true;
                this.mCanConstantState = true;
                this.mVariablePadding = drawableContainerState.mVariablePadding;
                this.mConstantSize = drawableContainerState.mConstantSize;
                this.mDither = drawableContainerState.mDither;
                this.mMutated = drawableContainerState.mMutated;
                this.mLayoutDirection = drawableContainerState.mLayoutDirection;
                this.mEnterFadeDuration = drawableContainerState.mEnterFadeDuration;
                this.mExitFadeDuration = drawableContainerState.mExitFadeDuration;
                this.mAutoMirrored = drawableContainerState.mAutoMirrored;
                this.mColorFilter = drawableContainerState.mColorFilter;
                this.mHasColorFilter = drawableContainerState.mHasColorFilter;
                this.mTintList = drawableContainerState.mTintList;
                this.mTintMode = drawableContainerState.mTintMode;
                this.mHasTintList = drawableContainerState.mHasTintList;
                this.mHasTintMode = drawableContainerState.mHasTintMode;
                if (drawableContainerState.mDensity == this.mDensity) {
                    if (drawableContainerState.mCheckedPadding) {
                        this.mConstantPadding = new Rect(drawableContainerState.mConstantPadding);
                        this.mCheckedPadding = true;
                    }
                    if (drawableContainerState.mCheckedConstantSize) {
                        this.mConstantWidth = drawableContainerState.mConstantWidth;
                        this.mConstantHeight = drawableContainerState.mConstantHeight;
                        this.mConstantMinimumWidth = drawableContainerState.mConstantMinimumWidth;
                        this.mConstantMinimumHeight = drawableContainerState.mConstantMinimumHeight;
                        this.mCheckedConstantSize = true;
                    }
                }
                if (drawableContainerState.mCheckedOpacity) {
                    this.mOpacity = drawableContainerState.mOpacity;
                    this.mCheckedOpacity = true;
                }
                if (drawableContainerState.mCheckedStateful) {
                    this.mStateful = drawableContainerState.mStateful;
                    this.mCheckedStateful = true;
                }
                final Drawable[] mDrawables = drawableContainerState.mDrawables;
                this.mDrawables = new Drawable[mDrawables.length];
                this.mNumChildren = drawableContainerState.mNumChildren;
                final SparseArray<Drawable$ConstantState> mDrawableFutures = drawableContainerState.mDrawableFutures;
                if (mDrawableFutures != null) {
                    this.mDrawableFutures = (SparseArray<Drawable$ConstantState>)mDrawableFutures.clone();
                }
                else {
                    this.mDrawableFutures = (SparseArray<Drawable$ConstantState>)new SparseArray(this.mNumChildren);
                }
                for (int mNumChildren = this.mNumChildren, i = n; i < mNumChildren; ++i) {
                    if (mDrawables[i] != null) {
                        final Drawable$ConstantState constantState = mDrawables[i].getConstantState();
                        if (constantState != null) {
                            this.mDrawableFutures.put(i, (Object)constantState);
                        }
                        else {
                            this.mDrawables[i] = mDrawables[i];
                        }
                    }
                }
            }
            else {
                this.mDrawables = new Drawable[10];
                this.mNumChildren = 0;
            }
        }
        
        private void createAllFutures() {
            if (this.mDrawableFutures != null) {
                for (int size = this.mDrawableFutures.size(), i = 0; i < size; ++i) {
                    this.mDrawables[this.mDrawableFutures.keyAt(i)] = this.prepareDrawable(((Drawable$ConstantState)this.mDrawableFutures.valueAt(i)).newDrawable(this.mSourceRes));
                }
                this.mDrawableFutures = null;
            }
        }
        
        private Drawable prepareDrawable(Drawable mutate) {
            if (Build$VERSION.SDK_INT >= 23) {
                mutate.setLayoutDirection(this.mLayoutDirection);
            }
            mutate = mutate.mutate();
            mutate.setCallback((Drawable$Callback)this.mOwner);
            return mutate;
        }
        
        public final int addChild(final Drawable drawable) {
            final int mNumChildren = this.mNumChildren;
            if (mNumChildren >= this.mDrawables.length) {
                this.growArray(mNumChildren, mNumChildren + 10);
            }
            drawable.mutate();
            drawable.setVisible(false, true);
            drawable.setCallback((Drawable$Callback)this.mOwner);
            this.mDrawables[mNumChildren] = drawable;
            ++this.mNumChildren;
            this.mChildrenChangingConfigurations |= drawable.getChangingConfigurations();
            this.invalidateCache();
            this.mConstantPadding = null;
            this.mCheckedPadding = false;
            this.mCheckedConstantSize = false;
            this.mCheckedConstantState = false;
            return mNumChildren;
        }
        
        @RequiresApi(21)
        final void applyTheme(final Resources$Theme resources$Theme) {
            if (resources$Theme != null) {
                this.createAllFutures();
                final int mNumChildren = this.mNumChildren;
                final Drawable[] mDrawables = this.mDrawables;
                for (int i = 0; i < mNumChildren; ++i) {
                    if (mDrawables[i] != null && mDrawables[i].canApplyTheme()) {
                        mDrawables[i].applyTheme(resources$Theme);
                        this.mChildrenChangingConfigurations |= mDrawables[i].getChangingConfigurations();
                    }
                }
                this.updateDensity(resources$Theme.getResources());
            }
        }
        
        @RequiresApi(21)
        public boolean canApplyTheme() {
            final int mNumChildren = this.mNumChildren;
            final Drawable[] mDrawables = this.mDrawables;
            for (int i = 0; i < mNumChildren; ++i) {
                final Drawable drawable = mDrawables[i];
                if (drawable != null) {
                    if (drawable.canApplyTheme()) {
                        return true;
                    }
                }
                else {
                    final Drawable$ConstantState drawable$ConstantState = (Drawable$ConstantState)this.mDrawableFutures.get(i);
                    if (drawable$ConstantState != null && drawable$ConstantState.canApplyTheme()) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        public boolean canConstantState() {
            synchronized (this) {
                if (this.mCheckedConstantState) {
                    return this.mCanConstantState;
                }
                this.createAllFutures();
                this.mCheckedConstantState = true;
                final int mNumChildren = this.mNumChildren;
                final Drawable[] mDrawables = this.mDrawables;
                for (int i = 0; i < mNumChildren; ++i) {
                    if (mDrawables[i].getConstantState() == null) {
                        return this.mCanConstantState = false;
                    }
                }
                return this.mCanConstantState = true;
            }
        }
        
        final void clearMutated() {
            this.mMutated = false;
        }
        
        protected void computeConstantSize() {
            this.mCheckedConstantSize = true;
            this.createAllFutures();
            final int mNumChildren = this.mNumChildren;
            final Drawable[] mDrawables = this.mDrawables;
            this.mConstantHeight = -1;
            this.mConstantWidth = -1;
            int i = 0;
            this.mConstantMinimumHeight = 0;
            this.mConstantMinimumWidth = 0;
            while (i < mNumChildren) {
                final Drawable drawable = mDrawables[i];
                final int intrinsicWidth = drawable.getIntrinsicWidth();
                if (intrinsicWidth > this.mConstantWidth) {
                    this.mConstantWidth = intrinsicWidth;
                }
                final int intrinsicHeight = drawable.getIntrinsicHeight();
                if (intrinsicHeight > this.mConstantHeight) {
                    this.mConstantHeight = intrinsicHeight;
                }
                final int minimumWidth = drawable.getMinimumWidth();
                if (minimumWidth > this.mConstantMinimumWidth) {
                    this.mConstantMinimumWidth = minimumWidth;
                }
                final int minimumHeight = drawable.getMinimumHeight();
                if (minimumHeight > this.mConstantMinimumHeight) {
                    this.mConstantMinimumHeight = minimumHeight;
                }
                ++i;
            }
        }
        
        final int getCapacity() {
            return this.mDrawables.length;
        }
        
        public int getChangingConfigurations() {
            return this.mChildrenChangingConfigurations | this.mChangingConfigurations;
        }
        
        public final Drawable getChild(final int n) {
            final Drawable drawable = this.mDrawables[n];
            if (drawable != null) {
                return drawable;
            }
            if (this.mDrawableFutures != null) {
                final int indexOfKey = this.mDrawableFutures.indexOfKey(n);
                if (indexOfKey >= 0) {
                    final Drawable prepareDrawable = this.prepareDrawable(((Drawable$ConstantState)this.mDrawableFutures.valueAt(indexOfKey)).newDrawable(this.mSourceRes));
                    this.mDrawables[n] = prepareDrawable;
                    this.mDrawableFutures.removeAt(indexOfKey);
                    if (this.mDrawableFutures.size() == 0) {
                        this.mDrawableFutures = null;
                    }
                    return prepareDrawable;
                }
            }
            return null;
        }
        
        public final int getChildCount() {
            return this.mNumChildren;
        }
        
        public final int getConstantHeight() {
            if (!this.mCheckedConstantSize) {
                this.computeConstantSize();
            }
            return this.mConstantHeight;
        }
        
        public final int getConstantMinimumHeight() {
            if (!this.mCheckedConstantSize) {
                this.computeConstantSize();
            }
            return this.mConstantMinimumHeight;
        }
        
        public final int getConstantMinimumWidth() {
            if (!this.mCheckedConstantSize) {
                this.computeConstantSize();
            }
            return this.mConstantMinimumWidth;
        }
        
        public final Rect getConstantPadding() {
            if (this.mVariablePadding) {
                return null;
            }
            if (this.mConstantPadding == null && !this.mCheckedPadding) {
                this.createAllFutures();
                final Rect rect = new Rect();
                final int mNumChildren = this.mNumChildren;
                final Drawable[] mDrawables = this.mDrawables;
                Rect mConstantPadding = null;
                Rect rect2;
                for (int i = 0; i < mNumChildren; ++i, mConstantPadding = rect2) {
                    rect2 = mConstantPadding;
                    if (mDrawables[i].getPadding(rect)) {
                        Rect rect3;
                        if ((rect3 = mConstantPadding) == null) {
                            rect3 = new Rect(0, 0, 0, 0);
                        }
                        if (rect.left > rect3.left) {
                            rect3.left = rect.left;
                        }
                        if (rect.top > rect3.top) {
                            rect3.top = rect.top;
                        }
                        if (rect.right > rect3.right) {
                            rect3.right = rect.right;
                        }
                        rect2 = rect3;
                        if (rect.bottom > rect3.bottom) {
                            rect3.bottom = rect.bottom;
                            rect2 = rect3;
                        }
                    }
                }
                this.mCheckedPadding = true;
                return this.mConstantPadding = mConstantPadding;
            }
            return this.mConstantPadding;
        }
        
        public final int getConstantWidth() {
            if (!this.mCheckedConstantSize) {
                this.computeConstantSize();
            }
            return this.mConstantWidth;
        }
        
        public final int getEnterFadeDuration() {
            return this.mEnterFadeDuration;
        }
        
        public final int getExitFadeDuration() {
            return this.mExitFadeDuration;
        }
        
        public final int getOpacity() {
            if (this.mCheckedOpacity) {
                return this.mOpacity;
            }
            this.createAllFutures();
            final int mNumChildren = this.mNumChildren;
            final Drawable[] mDrawables = this.mDrawables;
            int opacity;
            if (mNumChildren > 0) {
                opacity = mDrawables[0].getOpacity();
            }
            else {
                opacity = -2;
            }
            final int n = 1;
            int resolveOpacity = opacity;
            for (int i = n; i < mNumChildren; ++i) {
                resolveOpacity = Drawable.resolveOpacity(resolveOpacity, mDrawables[i].getOpacity());
            }
            this.mOpacity = resolveOpacity;
            this.mCheckedOpacity = true;
            return resolveOpacity;
        }
        
        public void growArray(final int n, final int n2) {
            final Drawable[] mDrawables = new Drawable[n2];
            System.arraycopy(this.mDrawables, 0, mDrawables, 0, n);
            this.mDrawables = mDrawables;
        }
        
        void invalidateCache() {
            this.mCheckedOpacity = false;
            this.mCheckedStateful = false;
        }
        
        public final boolean isConstantSize() {
            return this.mConstantSize;
        }
        
        public final boolean isStateful() {
            if (this.mCheckedStateful) {
                return this.mStateful;
            }
            this.createAllFutures();
            final int mNumChildren = this.mNumChildren;
            final Drawable[] mDrawables = this.mDrawables;
            final boolean b = false;
            int n = 0;
            boolean mStateful;
            while (true) {
                mStateful = b;
                if (n >= mNumChildren) {
                    break;
                }
                if (mDrawables[n].isStateful()) {
                    mStateful = true;
                    break;
                }
                ++n;
            }
            this.mStateful = mStateful;
            this.mCheckedStateful = true;
            return mStateful;
        }
        
        void mutate() {
            final int mNumChildren = this.mNumChildren;
            final Drawable[] mDrawables = this.mDrawables;
            for (int i = 0; i < mNumChildren; ++i) {
                if (mDrawables[i] != null) {
                    mDrawables[i].mutate();
                }
            }
            this.mMutated = true;
        }
        
        public final void setConstantSize(final boolean mConstantSize) {
            this.mConstantSize = mConstantSize;
        }
        
        public final void setEnterFadeDuration(final int mEnterFadeDuration) {
            this.mEnterFadeDuration = mEnterFadeDuration;
        }
        
        public final void setExitFadeDuration(final int mExitFadeDuration) {
            this.mExitFadeDuration = mExitFadeDuration;
        }
        
        final boolean setLayoutDirection(final int n, final int n2) {
            final int mNumChildren = this.mNumChildren;
            final Drawable[] mDrawables = this.mDrawables;
            int i = 0;
            boolean b = false;
            while (i < mNumChildren) {
                boolean b2 = b;
                if (mDrawables[i] != null) {
                    final boolean b3 = Build$VERSION.SDK_INT >= 23 && mDrawables[i].setLayoutDirection(n);
                    b2 = b;
                    if (i == n2) {
                        b2 = b3;
                    }
                }
                ++i;
                b = b2;
            }
            this.mLayoutDirection = n;
            return b;
        }
        
        public final void setVariablePadding(final boolean mVariablePadding) {
            this.mVariablePadding = mVariablePadding;
        }
        
        final void updateDensity(final Resources mSourceRes) {
            if (mSourceRes != null) {
                this.mSourceRes = mSourceRes;
                if (this.mDensity != (this.mDensity = DrawableContainer.resolveDensity(mSourceRes, this.mDensity))) {
                    this.mCheckedConstantSize = false;
                    this.mCheckedPadding = false;
                }
            }
        }
    }
}
