// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.graphics.drawable;

import android.animation.TimeInterpolator;
import android.animation.ObjectAnimator;
import android.util.StateSet;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.SparseArrayCompat;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.Canvas;
import android.support.annotation.RequiresApi;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.AnimationDrawable;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build$VERSION;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.res.XmlResourceParser;
import java.io.IOException;
import android.util.Log;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;
import android.content.res.Resources$Theme;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.content.Context;
import android.support.annotation.Nullable;
import android.content.res.Resources;

public class AnimatedStateListDrawableCompat extends StateListDrawable
{
    private static final String ELEMENT_ITEM = "item";
    private static final String ELEMENT_TRANSITION = "transition";
    private static final String ITEM_MISSING_DRAWABLE_ERROR = ": <item> tag requires a 'drawable' attribute or child tag defining a drawable";
    private static final String LOGTAG = "AnimatedStateListDrawableCompat";
    private static final String TRANSITION_MISSING_DRAWABLE_ERROR = ": <transition> tag requires a 'drawable' attribute or child tag defining a drawable";
    private static final String TRANSITION_MISSING_FROM_TO_ID = ": <transition> tag requires 'fromId' & 'toId' attributes";
    private boolean mMutated;
    private AnimatedStateListState mState;
    private Transition mTransition;
    private int mTransitionFromIndex;
    private int mTransitionToIndex;
    
    public AnimatedStateListDrawableCompat() {
        this((AnimatedStateListState)null, null);
    }
    
    AnimatedStateListDrawableCompat(@Nullable final AnimatedStateListState animatedStateListState, @Nullable final Resources resources) {
        super(null);
        this.mTransitionToIndex = -1;
        this.mTransitionFromIndex = -1;
        this.setConstantState(new AnimatedStateListState(animatedStateListState, this, resources));
        this.onStateChange(this.getState());
        this.jumpToCurrentState();
    }
    
    @Nullable
    public static AnimatedStateListDrawableCompat create(@NonNull final Context context, @DrawableRes int next, @Nullable final Resources$Theme resources$Theme) {
        try {
            final Resources resources = context.getResources();
            final XmlResourceParser xml = resources.getXml(next);
            final AttributeSet attributeSet = Xml.asAttributeSet((XmlPullParser)xml);
            do {
                next = ((XmlPullParser)xml).next();
            } while (next != 2 && next != 1);
            if (next != 2) {
                throw new XmlPullParserException("No start tag found");
            }
            return createFromXmlInner(context, resources, (XmlPullParser)xml, attributeSet, resources$Theme);
        }
        catch (final IOException ex) {
            Log.e(AnimatedStateListDrawableCompat.LOGTAG, "parser error", (Throwable)ex);
        }
        catch (final XmlPullParserException ex2) {
            Log.e(AnimatedStateListDrawableCompat.LOGTAG, "parser error", (Throwable)ex2);
        }
        return null;
    }
    
    public static AnimatedStateListDrawableCompat createFromXmlInner(@NonNull final Context context, @NonNull final Resources resources, @NonNull final XmlPullParser xmlPullParser, @NonNull final AttributeSet set, @Nullable final Resources$Theme resources$Theme) throws IOException, XmlPullParserException {
        final String name = xmlPullParser.getName();
        if (!name.equals("animated-selector")) {
            final StringBuilder sb = new StringBuilder();
            sb.append(xmlPullParser.getPositionDescription());
            sb.append(": invalid animated-selector tag ");
            sb.append(name);
            throw new XmlPullParserException(sb.toString());
        }
        final AnimatedStateListDrawableCompat animatedStateListDrawableCompat = new AnimatedStateListDrawableCompat();
        animatedStateListDrawableCompat.inflate(context, resources, xmlPullParser, set, resources$Theme);
        return animatedStateListDrawableCompat;
    }
    
    private void inflateChildElements(@NonNull final Context context, @NonNull final Resources resources, @NonNull final XmlPullParser xmlPullParser, @NonNull final AttributeSet set, @Nullable final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        final int n = xmlPullParser.getDepth() + 1;
        while (true) {
            final int next = xmlPullParser.next();
            if (next == 1) {
                break;
            }
            final int depth = xmlPullParser.getDepth();
            if (depth < n && next == 3) {
                break;
            }
            if (next != 2) {
                continue;
            }
            if (depth > n) {
                continue;
            }
            if (xmlPullParser.getName().equals("item")) {
                this.parseItem(context, resources, xmlPullParser, set, resources$Theme);
            }
            else {
                if (!xmlPullParser.getName().equals("transition")) {
                    continue;
                }
                this.parseTransition(context, resources, xmlPullParser, set, resources$Theme);
            }
        }
    }
    
    private void init() {
        this.onStateChange(this.getState());
    }
    
    private int parseItem(@NonNull final Context context, @NonNull final Resources resources, @NonNull final XmlPullParser xmlPullParser, @NonNull final AttributeSet set, @Nullable final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, R.styleable.AnimatedStateListDrawableItem);
        final int resourceId = obtainAttributes.getResourceId(R.styleable.AnimatedStateListDrawableItem_android_id, 0);
        final int resourceId2 = obtainAttributes.getResourceId(R.styleable.AnimatedStateListDrawableItem_android_drawable, -1);
        Drawable drawable;
        if (resourceId2 > 0) {
            drawable = AppCompatResources.getDrawable(context, resourceId2);
        }
        else {
            drawable = null;
        }
        obtainAttributes.recycle();
        final int[] stateSet = this.extractStateSet(set);
        Drawable drawable2 = drawable;
        if (drawable == null) {
            int next;
            do {
                next = xmlPullParser.next();
            } while (next == 4);
            if (next != 2) {
                final StringBuilder sb = new StringBuilder();
                sb.append(xmlPullParser.getPositionDescription());
                sb.append(": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
                throw new XmlPullParserException(sb.toString());
            }
            if (xmlPullParser.getName().equals("vector")) {
                drawable2 = VectorDrawableCompat.createFromXmlInner(resources, xmlPullParser, set, resources$Theme);
            }
            else if (Build$VERSION.SDK_INT >= 21) {
                drawable2 = Drawable.createFromXmlInner(resources, xmlPullParser, set, resources$Theme);
            }
            else {
                drawable2 = Drawable.createFromXmlInner(resources, xmlPullParser, set);
            }
        }
        if (drawable2 == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(xmlPullParser.getPositionDescription());
            sb2.append(": <item> tag requires a 'drawable' attribute or child tag defining a drawable");
            throw new XmlPullParserException(sb2.toString());
        }
        return this.mState.addStateSet(stateSet, drawable2, resourceId);
    }
    
    private int parseTransition(@NonNull final Context context, @NonNull final Resources resources, @NonNull final XmlPullParser xmlPullParser, @NonNull final AttributeSet set, @Nullable final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, R.styleable.AnimatedStateListDrawableTransition);
        final int resourceId = obtainAttributes.getResourceId(R.styleable.AnimatedStateListDrawableTransition_android_fromId, -1);
        final int resourceId2 = obtainAttributes.getResourceId(R.styleable.AnimatedStateListDrawableTransition_android_toId, -1);
        final int resourceId3 = obtainAttributes.getResourceId(R.styleable.AnimatedStateListDrawableTransition_android_drawable, -1);
        Drawable drawable;
        if (resourceId3 > 0) {
            drawable = AppCompatResources.getDrawable(context, resourceId3);
        }
        else {
            drawable = null;
        }
        final boolean boolean1 = obtainAttributes.getBoolean(R.styleable.AnimatedStateListDrawableTransition_android_reversible, false);
        obtainAttributes.recycle();
        Drawable drawable2 = drawable;
        if (drawable == null) {
            int next;
            do {
                next = xmlPullParser.next();
            } while (next == 4);
            if (next != 2) {
                final StringBuilder sb = new StringBuilder();
                sb.append(xmlPullParser.getPositionDescription());
                sb.append(": <transition> tag requires a 'drawable' attribute or child tag defining a drawable");
                throw new XmlPullParserException(sb.toString());
            }
            if (xmlPullParser.getName().equals("animated-vector")) {
                drawable2 = AnimatedVectorDrawableCompat.createFromXmlInner(context, resources, xmlPullParser, set, resources$Theme);
            }
            else if (Build$VERSION.SDK_INT >= 21) {
                drawable2 = Drawable.createFromXmlInner(resources, xmlPullParser, set, resources$Theme);
            }
            else {
                drawable2 = Drawable.createFromXmlInner(resources, xmlPullParser, set);
            }
        }
        if (drawable2 == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(xmlPullParser.getPositionDescription());
            sb2.append(": <transition> tag requires a 'drawable' attribute or child tag defining a drawable");
            throw new XmlPullParserException(sb2.toString());
        }
        if (resourceId != -1 && resourceId2 != -1) {
            return this.mState.addTransition(resourceId, resourceId2, drawable2, boolean1);
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(xmlPullParser.getPositionDescription());
        sb3.append(": <transition> tag requires 'fromId' & 'toId' attributes");
        throw new XmlPullParserException(sb3.toString());
    }
    
    private boolean selectTransition(final int n) {
        final Transition mTransition = this.mTransition;
        int mTransitionFromIndex;
        if (mTransition != null) {
            if (n == this.mTransitionToIndex) {
                return true;
            }
            if (n == this.mTransitionFromIndex && mTransition.canReverse()) {
                mTransition.reverse();
                this.mTransitionToIndex = this.mTransitionFromIndex;
                this.mTransitionFromIndex = n;
                return true;
            }
            mTransitionFromIndex = this.mTransitionToIndex;
            mTransition.stop();
        }
        else {
            mTransitionFromIndex = this.getCurrentIndex();
        }
        this.mTransition = null;
        this.mTransitionFromIndex = -1;
        this.mTransitionToIndex = -1;
        final AnimatedStateListState mState = this.mState;
        final int keyframeId = mState.getKeyframeIdAt(mTransitionFromIndex);
        final int keyframeId2 = mState.getKeyframeIdAt(n);
        if (keyframeId2 == 0 || keyframeId == 0) {
            return false;
        }
        final int indexOfTransition = mState.indexOfTransition(keyframeId, keyframeId2);
        if (indexOfTransition < 0) {
            return false;
        }
        final boolean transitionHasReversibleFlag = mState.transitionHasReversibleFlag(keyframeId, keyframeId2);
        this.selectDrawable(indexOfTransition);
        final Drawable current = this.getCurrent();
        Transition mTransition2;
        if (current instanceof AnimationDrawable) {
            mTransition2 = new AnimationDrawableTransition((AnimationDrawable)current, mState.isTransitionReversed(keyframeId, keyframeId2), transitionHasReversibleFlag);
        }
        else if (current instanceof AnimatedVectorDrawableCompat) {
            mTransition2 = new AnimatedVectorDrawableTransition((AnimatedVectorDrawableCompat)current);
        }
        else {
            if (!(current instanceof Animatable)) {
                return false;
            }
            mTransition2 = new AnimatableTransition((Animatable)current);
        }
        mTransition2.start();
        this.mTransition = mTransition2;
        this.mTransitionFromIndex = mTransitionFromIndex;
        this.mTransitionToIndex = n;
        return true;
    }
    
    private void updateStateFromTypedArray(final TypedArray typedArray) {
        final AnimatedStateListState mState = this.mState;
        if (Build$VERSION.SDK_INT >= 21) {
            mState.mChangingConfigurations |= typedArray.getChangingConfigurations();
        }
        ((DrawableContainerState)mState).setVariablePadding(typedArray.getBoolean(R.styleable.AnimatedStateListDrawableCompat_android_variablePadding, mState.mVariablePadding));
        ((DrawableContainerState)mState).setConstantSize(typedArray.getBoolean(R.styleable.AnimatedStateListDrawableCompat_android_constantSize, mState.mConstantSize));
        ((DrawableContainerState)mState).setEnterFadeDuration(typedArray.getInt(R.styleable.AnimatedStateListDrawableCompat_android_enterFadeDuration, mState.mEnterFadeDuration));
        ((DrawableContainerState)mState).setExitFadeDuration(typedArray.getInt(R.styleable.AnimatedStateListDrawableCompat_android_exitFadeDuration, mState.mExitFadeDuration));
        this.setDither(typedArray.getBoolean(R.styleable.AnimatedStateListDrawableCompat_android_dither, mState.mDither));
    }
    
    public void addState(@NonNull final int[] array, @NonNull final Drawable drawable, final int n) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable must not be null");
        }
        this.mState.addStateSet(array, drawable, n);
        this.onStateChange(this.getState());
    }
    
    public <T extends Drawable & Animatable> void addTransition(final int n, final int n2, @NonNull final T t, final boolean b) {
        if (t == null) {
            throw new IllegalArgumentException("Transition drawable must not be null");
        }
        this.mState.addTransition(n, n2, t, b);
    }
    
    @Override
    void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }
    
    AnimatedStateListState cloneConstantState() {
        return new AnimatedStateListState(this.mState, this, null);
    }
    
    @Override
    public void inflate(@NonNull final Context context, @NonNull final Resources resources, @NonNull final XmlPullParser xmlPullParser, @NonNull final AttributeSet set, @Nullable final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        final TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, resources$Theme, set, R.styleable.AnimatedStateListDrawableCompat);
        this.setVisible(obtainAttributes.getBoolean(R.styleable.AnimatedStateListDrawableCompat_android_visible, true), true);
        this.updateStateFromTypedArray(obtainAttributes);
        this.updateDensity(resources);
        obtainAttributes.recycle();
        this.inflateChildElements(context, resources, xmlPullParser, set, resources$Theme);
        this.init();
    }
    
    @Override
    public boolean isStateful() {
        return true;
    }
    
    @Override
    public void jumpToCurrentState() {
        super.jumpToCurrentState();
        if (this.mTransition != null) {
            this.mTransition.stop();
            this.mTransition = null;
            this.selectDrawable(this.mTransitionToIndex);
            this.mTransitionToIndex = -1;
            this.mTransitionFromIndex = -1;
        }
    }
    
    @Override
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mState.mutate();
            this.mMutated = true;
        }
        return this;
    }
    
    @Override
    protected boolean onStateChange(final int[] state) {
        final int indexOfKeyframe = this.mState.indexOfKeyframe(state);
        final boolean b = indexOfKeyframe != this.getCurrentIndex() && (this.selectTransition(indexOfKeyframe) || this.selectDrawable(indexOfKeyframe));
        final Drawable current = this.getCurrent();
        boolean b2 = b;
        if (current != null) {
            b2 = (b | current.setState(state));
        }
        return b2;
    }
    
    @Override
    protected void setConstantState(@NonNull final DrawableContainerState constantState) {
        super.setConstantState(constantState);
        if (constantState instanceof AnimatedStateListState) {
            this.mState = (AnimatedStateListState)constantState;
        }
    }
    
    @Override
    public boolean setVisible(final boolean b, final boolean b2) {
        final boolean setVisible = super.setVisible(b, b2);
        if (this.mTransition != null && (setVisible || b2)) {
            if (b) {
                this.mTransition.start();
            }
            else {
                this.jumpToCurrentState();
            }
        }
        return setVisible;
    }
    
    private static class AnimatableTransition extends Transition
    {
        private final Animatable mA;
        
        AnimatableTransition(final Animatable ma) {
            this.mA = ma;
        }
        
        @Override
        public void start() {
            this.mA.start();
        }
        
        @Override
        public void stop() {
            this.mA.stop();
        }
    }
    
    private abstract static class Transition
    {
        public boolean canReverse() {
            return false;
        }
        
        public void reverse() {
        }
        
        public abstract void start();
        
        public abstract void stop();
    }
    
    static class AnimatedStateListState extends StateListState
    {
        private static final long REVERSED_BIT = 4294967296L;
        private static final long REVERSIBLE_FLAG_BIT = 8589934592L;
        SparseArrayCompat<Integer> mStateIds;
        LongSparseArray<Long> mTransitions;
        
        AnimatedStateListState(@Nullable final AnimatedStateListState animatedStateListState, @NonNull final AnimatedStateListDrawableCompat animatedStateListDrawableCompat, @Nullable final Resources resources) {
            super((StateListState)animatedStateListState, animatedStateListDrawableCompat, resources);
            if (animatedStateListState != null) {
                this.mTransitions = animatedStateListState.mTransitions;
                this.mStateIds = animatedStateListState.mStateIds;
            }
            else {
                this.mTransitions = new LongSparseArray<Long>();
                this.mStateIds = new SparseArrayCompat<Integer>();
            }
        }
        
        private static long generateTransitionKey(final int n, final int n2) {
            return (long)n2 | (long)n << 32;
        }
        
        int addStateSet(@NonNull final int[] array, @NonNull final Drawable drawable, final int i) {
            final int addStateSet = super.addStateSet(array, drawable);
            this.mStateIds.put(addStateSet, i);
            return addStateSet;
        }
        
        int addTransition(final int n, final int n2, @NonNull final Drawable drawable, final boolean b) {
            final int addChild = super.addChild(drawable);
            final long generateTransitionKey = generateTransitionKey(n, n2);
            long n3;
            if (b) {
                n3 = 8589934592L;
            }
            else {
                n3 = 0L;
            }
            final LongSparseArray<Long> mTransitions = this.mTransitions;
            final long n4 = addChild;
            mTransitions.append(generateTransitionKey, n4 | n3);
            if (b) {
                this.mTransitions.append(generateTransitionKey(n2, n), 0x100000000L | n4 | n3);
            }
            return addChild;
        }
        
        int getKeyframeIdAt(int intValue) {
            final int n = 0;
            if (intValue < 0) {
                intValue = n;
            }
            else {
                intValue = this.mStateIds.get(intValue, 0);
            }
            return intValue;
        }
        
        int indexOfKeyframe(@NonNull final int[] array) {
            final int indexOfStateSet = super.indexOfStateSet(array);
            if (indexOfStateSet >= 0) {
                return indexOfStateSet;
            }
            return super.indexOfStateSet(StateSet.WILD_CARD);
        }
        
        int indexOfTransition(final int n, final int n2) {
            return (int)(long)this.mTransitions.get(generateTransitionKey(n, n2), -1L);
        }
        
        boolean isTransitionReversed(final int n, final int n2) {
            return ((long)this.mTransitions.get(generateTransitionKey(n, n2), -1L) & 0x100000000L) != 0x0L;
        }
        
        @Override
        void mutate() {
            this.mTransitions = this.mTransitions.clone();
            this.mStateIds = this.mStateIds.clone();
        }
        
        @NonNull
        @Override
        public Drawable newDrawable() {
            return new AnimatedStateListDrawableCompat(this, null);
        }
        
        @NonNull
        @Override
        public Drawable newDrawable(final Resources resources) {
            return new AnimatedStateListDrawableCompat(this, resources);
        }
        
        boolean transitionHasReversibleFlag(final int n, final int n2) {
            return ((long)this.mTransitions.get(generateTransitionKey(n, n2), -1L) & 0x200000000L) != 0x0L;
        }
    }
    
    private static class AnimatedVectorDrawableTransition extends Transition
    {
        private final AnimatedVectorDrawableCompat mAvd;
        
        AnimatedVectorDrawableTransition(final AnimatedVectorDrawableCompat mAvd) {
            this.mAvd = mAvd;
        }
        
        @Override
        public void start() {
            this.mAvd.start();
        }
        
        @Override
        public void stop() {
            this.mAvd.stop();
        }
    }
    
    private static class AnimationDrawableTransition extends Transition
    {
        private final ObjectAnimator mAnim;
        private final boolean mHasReversibleFlag;
        
        AnimationDrawableTransition(final AnimationDrawable animationDrawable, final boolean b, final boolean mHasReversibleFlag) {
            int numberOfFrames = animationDrawable.getNumberOfFrames();
            int n;
            if (b) {
                n = numberOfFrames - 1;
            }
            else {
                n = 0;
            }
            if (b) {
                numberOfFrames = 0;
            }
            else {
                --numberOfFrames;
            }
            final FrameInterpolator interpolator = new FrameInterpolator(animationDrawable, b);
            final ObjectAnimator ofInt = ObjectAnimator.ofInt((Object)animationDrawable, "currentIndex", new int[] { n, numberOfFrames });
            if (Build$VERSION.SDK_INT >= 18) {
                ofInt.setAutoCancel(true);
            }
            ofInt.setDuration((long)interpolator.getTotalDuration());
            ofInt.setInterpolator((TimeInterpolator)interpolator);
            this.mHasReversibleFlag = mHasReversibleFlag;
            this.mAnim = ofInt;
        }
        
        @Override
        public boolean canReverse() {
            return this.mHasReversibleFlag;
        }
        
        @Override
        public void reverse() {
            this.mAnim.reverse();
        }
        
        @Override
        public void start() {
            this.mAnim.start();
        }
        
        @Override
        public void stop() {
            this.mAnim.cancel();
        }
    }
    
    private static class FrameInterpolator implements TimeInterpolator
    {
        private int[] mFrameTimes;
        private int mFrames;
        private int mTotalDuration;
        
        FrameInterpolator(final AnimationDrawable animationDrawable, final boolean b) {
            this.updateFrames(animationDrawable, b);
        }
        
        public float getInterpolation(float n) {
            int n2;
            int mFrames;
            int[] mFrameTimes;
            int n3;
            for (n2 = (int)(n * this.mTotalDuration + 0.5f), mFrames = this.mFrames, mFrameTimes = this.mFrameTimes, n3 = 0; n3 < mFrames && n2 >= mFrameTimes[n3]; n2 -= mFrameTimes[n3], ++n3) {}
            if (n3 < mFrames) {
                n = n2 / (float)this.mTotalDuration;
            }
            else {
                n = 0.0f;
            }
            return n3 / (float)mFrames + n;
        }
        
        int getTotalDuration() {
            return this.mTotalDuration;
        }
        
        int updateFrames(final AnimationDrawable animationDrawable, final boolean b) {
            final int numberOfFrames = animationDrawable.getNumberOfFrames();
            this.mFrames = numberOfFrames;
            if (this.mFrameTimes == null || this.mFrameTimes.length < numberOfFrames) {
                this.mFrameTimes = new int[numberOfFrames];
            }
            final int[] mFrameTimes = this.mFrameTimes;
            int i = 0;
            int mTotalDuration = 0;
            while (i < numberOfFrames) {
                int n;
                if (b) {
                    n = numberOfFrames - i - 1;
                }
                else {
                    n = i;
                }
                final int duration = animationDrawable.getDuration(n);
                mFrameTimes[i] = duration;
                mTotalDuration += duration;
                ++i;
            }
            return this.mTotalDuration = mTotalDuration;
        }
    }
}
