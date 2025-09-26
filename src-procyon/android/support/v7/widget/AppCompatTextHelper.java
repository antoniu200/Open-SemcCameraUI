// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.annotation.SuppressLint;
import android.support.v4.widget.TextViewCompat;
import android.support.v4.widget.AutoSizeableTextView;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.support.annotation.RestrictTo;
import android.os.Build$VERSION;
import android.content.res.Resources$NotFoundException;
import android.support.v4.content.res.ResourcesCompat;
import java.lang.ref.WeakReference;
import android.support.v7.appcompat.R;
import android.content.res.ColorStateList;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;
import android.graphics.Typeface;
import android.support.annotation.NonNull;

class AppCompatTextHelper
{
    private static final int MONOSPACE = 3;
    private static final int SANS = 1;
    private static final int SERIF = 2;
    private boolean mAsyncFontPending;
    @NonNull
    private final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
    private TintInfo mDrawableBottomTint;
    private TintInfo mDrawableEndTint;
    private TintInfo mDrawableLeftTint;
    private TintInfo mDrawableRightTint;
    private TintInfo mDrawableStartTint;
    private TintInfo mDrawableTopTint;
    private Typeface mFontTypeface;
    private int mStyle;
    private final TextView mView;
    
    AppCompatTextHelper(final TextView mView) {
        this.mStyle = 0;
        this.mView = mView;
        this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(this.mView);
    }
    
    private void applyCompoundDrawableTint(final Drawable drawable, final TintInfo tintInfo) {
        if (drawable != null && tintInfo != null) {
            AppCompatDrawableManager.tintDrawable(drawable, tintInfo, this.mView.getDrawableState());
        }
    }
    
    private static TintInfo createTintInfo(final Context context, final AppCompatDrawableManager appCompatDrawableManager, final int n) {
        final ColorStateList tintList = appCompatDrawableManager.getTintList(context, n);
        if (tintList != null) {
            final TintInfo tintInfo = new TintInfo();
            tintInfo.mHasTintList = true;
            tintInfo.mTintList = tintList;
            return tintInfo;
        }
        return null;
    }
    
    private void setTextSizeInternal(final int n, final float n2) {
        this.mAutoSizeTextHelper.setTextSizeInternal(n, n2);
    }
    
    private void updateTypefaceAndStyle(final Context context, final TintTypedArray tintTypedArray) {
        this.mStyle = tintTypedArray.getInt(R.styleable.TextAppearance_android_textStyle, this.mStyle);
        final boolean hasValue = tintTypedArray.hasValue(R.styleable.TextAppearance_android_fontFamily);
        boolean mAsyncFontPending = true;
        if (!hasValue && !tintTypedArray.hasValue(R.styleable.TextAppearance_fontFamily)) {
            if (tintTypedArray.hasValue(R.styleable.TextAppearance_android_typeface)) {
                this.mAsyncFontPending = false;
                switch (tintTypedArray.getInt(R.styleable.TextAppearance_android_typeface, 1)) {
                    case 3: {
                        this.mFontTypeface = Typeface.MONOSPACE;
                        break;
                    }
                    case 2: {
                        this.mFontTypeface = Typeface.SERIF;
                        break;
                    }
                    case 1: {
                        this.mFontTypeface = Typeface.SANS_SERIF;
                        break;
                    }
                }
            }
            return;
        }
        this.mFontTypeface = null;
        int n;
        if (tintTypedArray.hasValue(R.styleable.TextAppearance_fontFamily)) {
            n = R.styleable.TextAppearance_fontFamily;
        }
        else {
            n = R.styleable.TextAppearance_android_fontFamily;
        }
        while (true) {
            if (context.isRestricted()) {
                break Label_0213;
            }
            final ResourcesCompat.FontCallback fontCallback = new ResourcesCompat.FontCallback(this, new WeakReference((T)this.mView)) {
                final AppCompatTextHelper this$0;
                final WeakReference val$textViewWeak;
                
                @Override
                public void onFontRetrievalFailed(final int n) {
                }
                
                @Override
                public void onFontRetrieved(@NonNull final Typeface typeface) {
                    this.this$0.onAsyncTypefaceReceived(this.val$textViewWeak, typeface);
                }
            };
            try {
                this.mFontTypeface = tintTypedArray.getFont(n, this.mStyle, fontCallback);
                if (this.mFontTypeface != null) {
                    mAsyncFontPending = false;
                }
                this.mAsyncFontPending = mAsyncFontPending;
                if (this.mFontTypeface == null) {
                    final String string = tintTypedArray.getString(n);
                    if (string != null) {
                        this.mFontTypeface = Typeface.create(string, this.mStyle);
                    }
                }
            }
            catch (final UnsupportedOperationException | Resources$NotFoundException ex) {
                continue;
            }
            break;
        }
    }
    
    void applyCompoundDrawablesTints() {
        if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
            final Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
            this.applyCompoundDrawableTint(compoundDrawables[0], this.mDrawableLeftTint);
            this.applyCompoundDrawableTint(compoundDrawables[1], this.mDrawableTopTint);
            this.applyCompoundDrawableTint(compoundDrawables[2], this.mDrawableRightTint);
            this.applyCompoundDrawableTint(compoundDrawables[3], this.mDrawableBottomTint);
        }
        if (Build$VERSION.SDK_INT >= 17 && (this.mDrawableStartTint != null || this.mDrawableEndTint != null)) {
            final Drawable[] compoundDrawablesRelative = this.mView.getCompoundDrawablesRelative();
            this.applyCompoundDrawableTint(compoundDrawablesRelative[0], this.mDrawableStartTint);
            this.applyCompoundDrawableTint(compoundDrawablesRelative[2], this.mDrawableEndTint);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void autoSizeText() {
        this.mAutoSizeTextHelper.autoSizeText();
    }
    
    int getAutoSizeMaxTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMaxTextSize();
    }
    
    int getAutoSizeMinTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMinTextSize();
    }
    
    int getAutoSizeStepGranularity() {
        return this.mAutoSizeTextHelper.getAutoSizeStepGranularity();
    }
    
    int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
    }
    
    int getAutoSizeTextType() {
        return this.mAutoSizeTextHelper.getAutoSizeTextType();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    boolean isAutoSizeEnabled() {
        return this.mAutoSizeTextHelper.isAutoSizeEnabled();
    }
    
    @SuppressLint({ "NewApi" })
    void loadFromAttributes(final AttributeSet set, int dimensionPixelSize) {
        final Context context = this.mView.getContext();
        final AppCompatDrawableManager value = AppCompatDrawableManager.get();
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, set, R.styleable.AppCompatTextHelper, dimensionPixelSize, 0);
        final int resourceId = obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_textAppearance, -1);
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableLeft)) {
            this.mDrawableLeftTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableLeft, 0));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableTop)) {
            this.mDrawableTopTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableTop, 0));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableRight)) {
            this.mDrawableRightTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableRight, 0));
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableBottom)) {
            this.mDrawableBottomTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableBottom, 0));
        }
        if (Build$VERSION.SDK_INT >= 17) {
            if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableStart)) {
                this.mDrawableStartTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableStart, 0));
            }
            if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextHelper_android_drawableEnd)) {
                this.mDrawableEndTint = createTintInfo(context, value, obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextHelper_android_drawableEnd, 0));
            }
        }
        obtainStyledAttributes.recycle();
        final boolean b = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
        ColorStateList list = null;
        final ColorStateList list2 = null;
        ColorStateList colorStateList = null;
        boolean boolean1;
        boolean b2;
        ColorStateList list3;
        if (resourceId != -1) {
            final TintTypedArray obtainStyledAttributes2 = TintTypedArray.obtainStyledAttributes(context, resourceId, R.styleable.TextAppearance);
            if (!b && obtainStyledAttributes2.hasValue(R.styleable.TextAppearance_textAllCaps)) {
                boolean1 = obtainStyledAttributes2.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
                b2 = true;
            }
            else {
                b2 = false;
                boolean1 = false;
            }
            this.updateTypefaceAndStyle(context, obtainStyledAttributes2);
            if (Build$VERSION.SDK_INT < 23) {
                if (obtainStyledAttributes2.hasValue(R.styleable.TextAppearance_android_textColor)) {
                    list = obtainStyledAttributes2.getColorStateList(R.styleable.TextAppearance_android_textColor);
                }
                else {
                    list = null;
                }
                if (obtainStyledAttributes2.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
                    list3 = obtainStyledAttributes2.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
                }
                else {
                    list3 = null;
                }
                if (obtainStyledAttributes2.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
                    colorStateList = obtainStyledAttributes2.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
                }
            }
            else {
                colorStateList = null;
                list3 = null;
            }
            obtainStyledAttributes2.recycle();
        }
        else {
            b2 = false;
            boolean1 = false;
            colorStateList = null;
            list3 = null;
            list = list2;
        }
        final TintTypedArray obtainStyledAttributes3 = TintTypedArray.obtainStyledAttributes(context, set, R.styleable.TextAppearance, dimensionPixelSize, 0);
        boolean b3 = b2;
        boolean boolean2 = boolean1;
        if (!b) {
            b3 = b2;
            boolean2 = boolean1;
            if (obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_textAllCaps)) {
                boolean2 = obtainStyledAttributes3.getBoolean(R.styleable.TextAppearance_textAllCaps, false);
                b3 = true;
            }
        }
        ColorStateList textColor = list;
        ColorStateList colorStateList2 = colorStateList;
        ColorStateList hintTextColor = list3;
        if (Build$VERSION.SDK_INT < 23) {
            if (obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_android_textColor)) {
                list = obtainStyledAttributes3.getColorStateList(R.styleable.TextAppearance_android_textColor);
            }
            if (obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_android_textColorHint)) {
                list3 = obtainStyledAttributes3.getColorStateList(R.styleable.TextAppearance_android_textColorHint);
            }
            textColor = list;
            colorStateList2 = colorStateList;
            hintTextColor = list3;
            if (obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_android_textColorLink)) {
                colorStateList2 = obtainStyledAttributes3.getColorStateList(R.styleable.TextAppearance_android_textColorLink);
                hintTextColor = list3;
                textColor = list;
            }
        }
        if (Build$VERSION.SDK_INT >= 28 && obtainStyledAttributes3.hasValue(R.styleable.TextAppearance_android_textSize) && obtainStyledAttributes3.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, -1) == 0) {
            this.mView.setTextSize(0, 0.0f);
        }
        this.updateTypefaceAndStyle(context, obtainStyledAttributes3);
        obtainStyledAttributes3.recycle();
        if (textColor != null) {
            this.mView.setTextColor(textColor);
        }
        if (hintTextColor != null) {
            this.mView.setHintTextColor(hintTextColor);
        }
        if (colorStateList2 != null) {
            this.mView.setLinkTextColor(colorStateList2);
        }
        if (!b && b3) {
            this.setAllCaps(boolean2);
        }
        if (this.mFontTypeface != null) {
            this.mView.setTypeface(this.mFontTypeface, this.mStyle);
        }
        this.mAutoSizeTextHelper.loadFromAttributes(set, dimensionPixelSize);
        if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && this.mAutoSizeTextHelper.getAutoSizeTextType() != 0) {
            final int[] autoSizeTextAvailableSizes = this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
            if (autoSizeTextAvailableSizes.length > 0) {
                if (this.mView.getAutoSizeStepGranularity() != -1.0f) {
                    this.mView.setAutoSizeTextTypeUniformWithConfiguration(this.mAutoSizeTextHelper.getAutoSizeMinTextSize(), this.mAutoSizeTextHelper.getAutoSizeMaxTextSize(), this.mAutoSizeTextHelper.getAutoSizeStepGranularity(), 0);
                }
                else {
                    this.mView.setAutoSizeTextTypeUniformWithPresetSizes(autoSizeTextAvailableSizes, 0);
                }
            }
        }
        final TintTypedArray obtainStyledAttributes4 = TintTypedArray.obtainStyledAttributes(context, set, R.styleable.AppCompatTextView);
        final int dimensionPixelSize2 = obtainStyledAttributes4.getDimensionPixelSize(R.styleable.AppCompatTextView_firstBaselineToTopHeight, -1);
        dimensionPixelSize = obtainStyledAttributes4.getDimensionPixelSize(R.styleable.AppCompatTextView_lastBaselineToBottomHeight, -1);
        final int dimensionPixelSize3 = obtainStyledAttributes4.getDimensionPixelSize(R.styleable.AppCompatTextView_lineHeight, -1);
        obtainStyledAttributes4.recycle();
        if (dimensionPixelSize2 != -1) {
            TextViewCompat.setFirstBaselineToTopHeight(this.mView, dimensionPixelSize2);
        }
        if (dimensionPixelSize != -1) {
            TextViewCompat.setLastBaselineToBottomHeight(this.mView, dimensionPixelSize);
        }
        if (dimensionPixelSize3 != -1) {
            TextViewCompat.setLineHeight(this.mView, dimensionPixelSize3);
        }
    }
    
    void onAsyncTypefaceReceived(final WeakReference<TextView> weakReference, final Typeface mFontTypeface) {
        if (this.mAsyncFontPending) {
            this.mFontTypeface = mFontTypeface;
            final TextView textView = weakReference.get();
            if (textView != null) {
                textView.setTypeface(mFontTypeface, this.mStyle);
            }
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            this.autoSizeText();
        }
    }
    
    void onSetTextAppearance(final Context context, final int n) {
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, n, R.styleable.TextAppearance);
        if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_textAllCaps)) {
            this.setAllCaps(obtainStyledAttributes.getBoolean(R.styleable.TextAppearance_textAllCaps, false));
        }
        if (Build$VERSION.SDK_INT < 23 && obtainStyledAttributes.hasValue(R.styleable.TextAppearance_android_textColor)) {
            final ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(R.styleable.TextAppearance_android_textColor);
            if (colorStateList != null) {
                this.mView.setTextColor(colorStateList);
            }
        }
        if (obtainStyledAttributes.hasValue(R.styleable.TextAppearance_android_textSize) && obtainStyledAttributes.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, -1) == 0) {
            this.mView.setTextSize(0, 0.0f);
        }
        this.updateTypefaceAndStyle(context, obtainStyledAttributes);
        obtainStyledAttributes.recycle();
        if (this.mFontTypeface != null) {
            this.mView.setTypeface(this.mFontTypeface, this.mStyle);
        }
    }
    
    void setAllCaps(final boolean allCaps) {
        this.mView.setAllCaps(allCaps);
    }
    
    void setAutoSizeTextTypeUniformWithConfiguration(final int n, final int n2, final int n3, final int n4) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
    }
    
    void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull final int[] array, final int n) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(array, n);
    }
    
    void setAutoSizeTextTypeWithDefaults(final int autoSizeTextTypeWithDefaults) {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(autoSizeTextTypeWithDefaults);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void setTextSize(final int n, final float n2) {
        if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && !this.isAutoSizeEnabled()) {
            this.setTextSizeInternal(n, n2);
        }
    }
}
