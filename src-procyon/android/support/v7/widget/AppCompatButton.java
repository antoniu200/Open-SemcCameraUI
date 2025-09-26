// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.support.v4.widget.TextViewCompat;
import android.view.ActionMode$Callback;
import android.support.annotation.DrawableRes;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityEvent;
import android.graphics.PorterDuff$Mode;
import android.support.annotation.Nullable;
import android.content.res.ColorStateList;
import android.support.annotation.RestrictTo;
import android.widget.TextView;
import android.view.View;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v4.widget.AutoSizeableTextView;
import android.support.v4.view.TintableBackgroundView;
import android.widget.Button;

public class AppCompatButton extends Button implements TintableBackgroundView, AutoSizeableTextView
{
    private final AppCompatBackgroundHelper mBackgroundTintHelper;
    private final AppCompatTextHelper mTextHelper;
    
    public AppCompatButton(final Context context) {
        this(context, null);
    }
    
    public AppCompatButton(final Context context, final AttributeSet set) {
        this(context, set, R.attr.buttonStyle);
    }
    
    public AppCompatButton(final Context context, final AttributeSet set, final int n) {
        super(TintContextWrapper.wrap(context), set, n);
        (this.mBackgroundTintHelper = new AppCompatBackgroundHelper((View)this)).loadFromAttributes(set, n);
        (this.mTextHelper = new AppCompatTextHelper((TextView)this)).loadFromAttributes(set, n);
        this.mTextHelper.applyCompoundDrawablesTints();
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.applySupportBackgroundTint();
        }
        if (this.mTextHelper != null) {
            this.mTextHelper.applyCompoundDrawablesTints();
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public int getAutoSizeMaxTextSize() {
        if (AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE) {
            return super.getAutoSizeMaxTextSize();
        }
        if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeMaxTextSize();
        }
        return -1;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public int getAutoSizeMinTextSize() {
        if (AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE) {
            return super.getAutoSizeMinTextSize();
        }
        if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeMinTextSize();
        }
        return -1;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public int getAutoSizeStepGranularity() {
        if (AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE) {
            return super.getAutoSizeStepGranularity();
        }
        if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeStepGranularity();
        }
        return -1;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public int[] getAutoSizeTextAvailableSizes() {
        if (AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE) {
            return super.getAutoSizeTextAvailableSizes();
        }
        if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeTextAvailableSizes();
        }
        return new int[0];
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public int getAutoSizeTextType() {
        if (AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE) {
            final int autoSizeTextType = super.getAutoSizeTextType();
            int n = 1;
            if (autoSizeTextType != 1) {
                n = 0;
            }
            return n;
        }
        if (this.mTextHelper != null) {
            return this.mTextHelper.getAutoSizeTextType();
        }
        return 0;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public ColorStateList getSupportBackgroundTintList() {
        ColorStateList supportBackgroundTintList;
        if (this.mBackgroundTintHelper != null) {
            supportBackgroundTintList = this.mBackgroundTintHelper.getSupportBackgroundTintList();
        }
        else {
            supportBackgroundTintList = null;
        }
        return supportBackgroundTintList;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public PorterDuff$Mode getSupportBackgroundTintMode() {
        PorterDuff$Mode supportBackgroundTintMode;
        if (this.mBackgroundTintHelper != null) {
            supportBackgroundTintMode = this.mBackgroundTintHelper.getSupportBackgroundTintMode();
        }
        else {
            supportBackgroundTintMode = null;
        }
        return supportBackgroundTintMode;
    }
    
    public void onInitializeAccessibilityEvent(final AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName((CharSequence)Button.class.getName());
    }
    
    public void onInitializeAccessibilityNodeInfo(final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName((CharSequence)Button.class.getName());
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        if (this.mTextHelper != null) {
            this.mTextHelper.onLayout(b, n, n2, n3, n4);
        }
    }
    
    protected void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
        super.onTextChanged(charSequence, n, n2, n3);
        if (this.mTextHelper != null && !AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE && this.mTextHelper.isAutoSizeEnabled()) {
            this.mTextHelper.autoSizeText();
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setAutoSizeTextTypeUniformWithConfiguration(final int n, final int n2, final int n3, final int n4) throws IllegalArgumentException {
        if (AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE) {
            super.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
        }
        else if (this.mTextHelper != null) {
            this.mTextHelper.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull final int[] array, final int n) throws IllegalArgumentException {
        if (AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE) {
            super.setAutoSizeTextTypeUniformWithPresetSizes(array, n);
        }
        else if (this.mTextHelper != null) {
            this.mTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(array, n);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setAutoSizeTextTypeWithDefaults(final int n) {
        if (AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE) {
            super.setAutoSizeTextTypeWithDefaults(n);
        }
        else if (this.mTextHelper != null) {
            this.mTextHelper.setAutoSizeTextTypeWithDefaults(n);
        }
    }
    
    public void setBackgroundDrawable(final Drawable backgroundDrawable) {
        super.setBackgroundDrawable(backgroundDrawable);
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.onSetBackgroundDrawable(backgroundDrawable);
        }
    }
    
    public void setBackgroundResource(@DrawableRes final int backgroundResource) {
        super.setBackgroundResource(backgroundResource);
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.onSetBackgroundResource(backgroundResource);
        }
    }
    
    public void setCustomSelectionActionModeCallback(final ActionMode$Callback actionMode$Callback) {
        super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback((TextView)this, actionMode$Callback));
    }
    
    public void setSupportAllCaps(final boolean allCaps) {
        if (this.mTextHelper != null) {
            this.mTextHelper.setAllCaps(allCaps);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setSupportBackgroundTintList(@Nullable final ColorStateList supportBackgroundTintList) {
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.setSupportBackgroundTintList(supportBackgroundTintList);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setSupportBackgroundTintMode(@Nullable final PorterDuff$Mode supportBackgroundTintMode) {
        if (this.mBackgroundTintHelper != null) {
            this.mBackgroundTintHelper.setSupportBackgroundTintMode(supportBackgroundTintMode);
        }
    }
    
    public void setTextAppearance(final Context context, final int n) {
        super.setTextAppearance(context, n);
        if (this.mTextHelper != null) {
            this.mTextHelper.onSetTextAppearance(context, n);
        }
    }
    
    public void setTextSize(final int n, final float n2) {
        if (AppCompatButton.PLATFORM_SUPPORTS_AUTOSIZE) {
            super.setTextSize(n, n2);
        }
        else if (this.mTextHelper != null) {
            this.mTextHelper.setTextSize(n, n2);
        }
    }
}
