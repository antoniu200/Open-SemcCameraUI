// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.support.annotation.RestrictTo;
import android.text.method.TransformationMethod;
import android.view.View;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.Log;
import android.support.annotation.NonNull;
import android.os.Build$VERSION;
import android.support.annotation.RequiresApi;
import android.text.StaticLayout$Builder;
import android.text.TextDirectionHeuristics;
import android.text.TextDirectionHeuristic;
import android.text.StaticLayout;
import android.text.Layout$Alignment;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import android.widget.TextView;
import android.text.TextPaint;
import android.content.Context;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import android.graphics.RectF;

class AppCompatTextViewAutoSizeHelper
{
    private static final int DEFAULT_AUTO_SIZE_GRANULARITY_IN_PX = 1;
    private static final int DEFAULT_AUTO_SIZE_MAX_TEXT_SIZE_IN_SP = 112;
    private static final int DEFAULT_AUTO_SIZE_MIN_TEXT_SIZE_IN_SP = 12;
    private static final String TAG = "ACTVAutoSizeHelper";
    private static final RectF TEMP_RECTF;
    static final float UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE = -1.0f;
    private static final int VERY_WIDE = 1048576;
    private static ConcurrentHashMap<String, Method> sTextViewMethodByNameCache;
    private float mAutoSizeMaxTextSizeInPx;
    private float mAutoSizeMinTextSizeInPx;
    private float mAutoSizeStepGranularityInPx;
    private int[] mAutoSizeTextSizesInPx;
    private int mAutoSizeTextType;
    private final Context mContext;
    private boolean mHasPresetAutoSizeValues;
    private boolean mNeedsAutoSizeText;
    private TextPaint mTempTextPaint;
    private final TextView mTextView;
    
    static {
        TEMP_RECTF = new RectF();
        AppCompatTextViewAutoSizeHelper.sTextViewMethodByNameCache = new ConcurrentHashMap<String, Method>();
    }
    
    AppCompatTextViewAutoSizeHelper(final TextView mTextView) {
        this.mAutoSizeTextType = 0;
        this.mNeedsAutoSizeText = false;
        this.mAutoSizeStepGranularityInPx = -1.0f;
        this.mAutoSizeMinTextSizeInPx = -1.0f;
        this.mAutoSizeMaxTextSizeInPx = -1.0f;
        this.mAutoSizeTextSizesInPx = new int[0];
        this.mHasPresetAutoSizeValues = false;
        this.mTextView = mTextView;
        this.mContext = this.mTextView.getContext();
    }
    
    private int[] cleanupAutoSizePresetSizes(int[] a) {
        final int length = a.length;
        if (length == 0) {
            return a;
        }
        Arrays.sort(a);
        final ArrayList list = new ArrayList();
        final int n = 0;
        for (final int n2 : a) {
            if (n2 > 0 && Collections.binarySearch(list, n2) < 0) {
                list.add(n2);
            }
        }
        if (length == list.size()) {
            return a;
        }
        final int size = list.size();
        a = new int[size];
        for (int j = n; j < size; ++j) {
            a[j] = (int)list.get(j);
        }
        return a;
    }
    
    private void clearAutoSizeConfiguration() {
        this.mAutoSizeTextType = 0;
        this.mAutoSizeMinTextSizeInPx = -1.0f;
        this.mAutoSizeMaxTextSizeInPx = -1.0f;
        this.mAutoSizeStepGranularityInPx = -1.0f;
        this.mAutoSizeTextSizesInPx = new int[0];
        this.mNeedsAutoSizeText = false;
    }
    
    @RequiresApi(23)
    private StaticLayout createStaticLayoutForMeasuring(final CharSequence charSequence, final Layout$Alignment alignment, int maxLines, final int n) {
        final TextDirectionHeuristic textDirection = this.invokeAndReturnWithDefault(this.mTextView, "getTextDirectionHeuristic", TextDirectionHeuristics.FIRSTSTRONG_LTR);
        final StaticLayout$Builder setHyphenationFrequency = StaticLayout$Builder.obtain(charSequence, 0, charSequence.length(), this.mTempTextPaint, maxLines).setAlignment(alignment).setLineSpacing(this.mTextView.getLineSpacingExtra(), this.mTextView.getLineSpacingMultiplier()).setIncludePad(this.mTextView.getIncludeFontPadding()).setBreakStrategy(this.mTextView.getBreakStrategy()).setHyphenationFrequency(this.mTextView.getHyphenationFrequency());
        maxLines = n;
        if (n == -1) {
            maxLines = Integer.MAX_VALUE;
        }
        return setHyphenationFrequency.setMaxLines(maxLines).setTextDirection(textDirection).build();
    }
    
    private StaticLayout createStaticLayoutForMeasuringPre23(final CharSequence charSequence, final Layout$Alignment layout$Alignment, final int n) {
        float n2;
        float n3;
        boolean b;
        if (Build$VERSION.SDK_INT >= 16) {
            n2 = this.mTextView.getLineSpacingMultiplier();
            n3 = this.mTextView.getLineSpacingExtra();
            b = this.mTextView.getIncludeFontPadding();
        }
        else {
            n2 = this.invokeAndReturnWithDefault(this.mTextView, "getLineSpacingMultiplier", 1.0f);
            n3 = this.invokeAndReturnWithDefault(this.mTextView, "getLineSpacingExtra", 0.0f);
            b = this.invokeAndReturnWithDefault(this.mTextView, "getIncludeFontPadding", true);
        }
        return new StaticLayout(charSequence, this.mTempTextPaint, n, layout$Alignment, n2, n3, b);
    }
    
    private int findLargestTextSizeWhichFits(final RectF rectF) {
        int length = this.mAutoSizeTextSizesInPx.length;
        if (length == 0) {
            throw new IllegalStateException("No available text sizes to choose from.");
        }
        int n = 0;
        int i = 1;
        --length;
        while (i <= length) {
            n = (i + length) / 2;
            if (this.suggestedSizeFitsInSpace(this.mAutoSizeTextSizesInPx[n], rectF)) {
                final int n2 = n + 1;
                n = i;
                i = n2;
            }
            else {
                length = --n;
            }
        }
        return this.mAutoSizeTextSizesInPx[n];
    }
    
    @Nullable
    private Method getTextViewMethod(@NonNull final String s) {
        try {
            Method method;
            if ((method = AppCompatTextViewAutoSizeHelper.sTextViewMethodByNameCache.get(s)) == null) {
                final Method declaredMethod = TextView.class.getDeclaredMethod(s, (Class<?>[])new Class[0]);
                if ((method = declaredMethod) != null) {
                    declaredMethod.setAccessible(true);
                    AppCompatTextViewAutoSizeHelper.sTextViewMethodByNameCache.put(s, declaredMethod);
                    method = declaredMethod;
                }
            }
            return method;
        }
        catch (final Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to retrieve TextView#");
            sb.append(s);
            sb.append("() method");
            Log.w("ACTVAutoSizeHelper", sb.toString(), (Throwable)ex);
            return null;
        }
    }
    
    private <T> T invokeAndReturnWithDefault(@NonNull Object obj, @NonNull final String str, @NonNull T invoke) {
        try {
            try {
                obj = (invoke = (T)this.getTextViewMethod(str).invoke(obj, new Object[0]));
            }
            finally {}
        }
        catch (final Exception ex) {
            obj = new StringBuilder();
            ((StringBuilder)obj).append("Failed to invoke TextView#");
            ((StringBuilder)obj).append(str);
            ((StringBuilder)obj).append("() method");
            Log.w("ACTVAutoSizeHelper", ((StringBuilder)obj).toString(), (Throwable)ex);
        }
        return invoke;
    }
    
    private void setRawTextSize(final float textSize) {
        if (textSize != this.mTextView.getPaint().getTextSize()) {
            this.mTextView.getPaint().setTextSize(textSize);
            final boolean b = Build$VERSION.SDK_INT >= 18 && this.mTextView.isInLayout();
            if (this.mTextView.getLayout() != null) {
                this.mNeedsAutoSizeText = false;
                try {
                    final Method textViewMethod = this.getTextViewMethod("nullLayouts");
                    if (textViewMethod != null) {
                        textViewMethod.invoke(this.mTextView, new Object[0]);
                    }
                }
                catch (final Exception ex) {
                    Log.w("ACTVAutoSizeHelper", "Failed to invoke TextView#nullLayouts() method", (Throwable)ex);
                }
                if (!b) {
                    this.mTextView.requestLayout();
                }
                else {
                    this.mTextView.forceLayout();
                }
                this.mTextView.invalidate();
            }
        }
    }
    
    private boolean setupAutoSizeText() {
        final boolean supportsAutoSizeText = this.supportsAutoSizeText();
        int i = 0;
        if (supportsAutoSizeText && this.mAutoSizeTextType == 1) {
            if (!this.mHasPresetAutoSizeValues || this.mAutoSizeTextSizesInPx.length == 0) {
                float n = (float)Math.round(this.mAutoSizeMinTextSizeInPx);
                int n2 = 1;
                while (Math.round(this.mAutoSizeStepGranularityInPx + n) <= Math.round(this.mAutoSizeMaxTextSizeInPx)) {
                    ++n2;
                    n += this.mAutoSizeStepGranularityInPx;
                }
                final int[] array = new int[n2];
                float mAutoSizeMinTextSizeInPx = this.mAutoSizeMinTextSizeInPx;
                while (i < n2) {
                    array[i] = Math.round(mAutoSizeMinTextSizeInPx);
                    mAutoSizeMinTextSizeInPx += this.mAutoSizeStepGranularityInPx;
                    ++i;
                }
                this.mAutoSizeTextSizesInPx = this.cleanupAutoSizePresetSizes(array);
            }
            this.mNeedsAutoSizeText = true;
        }
        else {
            this.mNeedsAutoSizeText = false;
        }
        return this.mNeedsAutoSizeText;
    }
    
    private void setupAutoSizeUniformPresetSizes(final TypedArray typedArray) {
        final int length = typedArray.length();
        final int[] array = new int[length];
        if (length > 0) {
            for (int i = 0; i < length; ++i) {
                array[i] = typedArray.getDimensionPixelSize(i, -1);
            }
            this.mAutoSizeTextSizesInPx = this.cleanupAutoSizePresetSizes(array);
            this.setupAutoSizeUniformPresetSizesConfiguration();
        }
    }
    
    private boolean setupAutoSizeUniformPresetSizesConfiguration() {
        final int length = this.mAutoSizeTextSizesInPx.length;
        this.mHasPresetAutoSizeValues = (length > 0);
        if (this.mHasPresetAutoSizeValues) {
            this.mAutoSizeTextType = 1;
            this.mAutoSizeMinTextSizeInPx = (float)this.mAutoSizeTextSizesInPx[0];
            this.mAutoSizeMaxTextSizeInPx = (float)this.mAutoSizeTextSizesInPx[length - 1];
            this.mAutoSizeStepGranularityInPx = -1.0f;
        }
        return this.mHasPresetAutoSizeValues;
    }
    
    private boolean suggestedSizeFitsInSpace(final int n, final RectF rectF) {
        final CharSequence text = this.mTextView.getText();
        final TransformationMethod transformationMethod = this.mTextView.getTransformationMethod();
        CharSequence charSequence = text;
        if (transformationMethod != null) {
            final CharSequence transformation = transformationMethod.getTransformation(text, (View)this.mTextView);
            charSequence = text;
            if (transformation != null) {
                charSequence = transformation;
            }
        }
        int maxLines;
        if (Build$VERSION.SDK_INT >= 16) {
            maxLines = this.mTextView.getMaxLines();
        }
        else {
            maxLines = -1;
        }
        if (this.mTempTextPaint == null) {
            this.mTempTextPaint = new TextPaint();
        }
        else {
            this.mTempTextPaint.reset();
        }
        this.mTempTextPaint.set(this.mTextView.getPaint());
        this.mTempTextPaint.setTextSize((float)n);
        final Layout$Alignment layout$Alignment = this.invokeAndReturnWithDefault(this.mTextView, "getLayoutAlignment", Layout$Alignment.ALIGN_NORMAL);
        StaticLayout staticLayout;
        if (Build$VERSION.SDK_INT >= 23) {
            staticLayout = this.createStaticLayoutForMeasuring(charSequence, layout$Alignment, Math.round(rectF.right), maxLines);
        }
        else {
            staticLayout = this.createStaticLayoutForMeasuringPre23(charSequence, layout$Alignment, Math.round(rectF.right));
        }
        return (maxLines == -1 || (staticLayout.getLineCount() <= maxLines && staticLayout.getLineEnd(staticLayout.getLineCount() - 1) == charSequence.length())) && staticLayout.getHeight() <= rectF.bottom;
    }
    
    private boolean supportsAutoSizeText() {
        return !(this.mTextView instanceof AppCompatEditText);
    }
    
    private void validateAndSetAutoSizeTextTypeUniformConfiguration(final float mAutoSizeMinTextSizeInPx, final float n, final float n2) throws IllegalArgumentException {
        if (mAutoSizeMinTextSizeInPx <= 0.0f) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Minimum auto-size text size (");
            sb.append(mAutoSizeMinTextSizeInPx);
            sb.append("px) is less or equal to (0px)");
            throw new IllegalArgumentException(sb.toString());
        }
        if (n <= mAutoSizeMinTextSizeInPx) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Maximum auto-size text size (");
            sb2.append(n);
            sb2.append("px) is less or equal to minimum auto-size ");
            sb2.append("text size (");
            sb2.append(mAutoSizeMinTextSizeInPx);
            sb2.append("px)");
            throw new IllegalArgumentException(sb2.toString());
        }
        if (n2 <= 0.0f) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("The auto-size step granularity (");
            sb3.append(n2);
            sb3.append("px) is less or equal to (0px)");
            throw new IllegalArgumentException(sb3.toString());
        }
        this.mAutoSizeTextType = 1;
        this.mAutoSizeMinTextSizeInPx = mAutoSizeMinTextSizeInPx;
        this.mAutoSizeMaxTextSizeInPx = n;
        this.mAutoSizeStepGranularityInPx = n2;
        this.mHasPresetAutoSizeValues = false;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void autoSizeText() {
        if (!this.isAutoSizeEnabled()) {
            return;
        }
        Label_0200: {
            if (this.mNeedsAutoSizeText) {
                if (this.mTextView.getMeasuredHeight() > 0 && this.mTextView.getMeasuredWidth() > 0) {
                    int n;
                    if (this.invokeAndReturnWithDefault(this.mTextView, "getHorizontallyScrolling", false)) {
                        n = 1048576;
                    }
                    else {
                        n = this.mTextView.getMeasuredWidth() - this.mTextView.getTotalPaddingLeft() - this.mTextView.getTotalPaddingRight();
                    }
                    final int n2 = this.mTextView.getHeight() - this.mTextView.getCompoundPaddingBottom() - this.mTextView.getCompoundPaddingTop();
                    if (n > 0) {
                        if (n2 > 0) {
                            synchronized (AppCompatTextViewAutoSizeHelper.TEMP_RECTF) {
                                AppCompatTextViewAutoSizeHelper.TEMP_RECTF.setEmpty();
                                AppCompatTextViewAutoSizeHelper.TEMP_RECTF.right = (float)n;
                                AppCompatTextViewAutoSizeHelper.TEMP_RECTF.bottom = (float)n2;
                                final float n3 = (float)this.findLargestTextSizeWhichFits(AppCompatTextViewAutoSizeHelper.TEMP_RECTF);
                                if (n3 != this.mTextView.getTextSize()) {
                                    this.setTextSizeInternal(0, n3);
                                }
                                break Label_0200;
                            }
                        }
                    }
                }
                return;
            }
        }
        this.mNeedsAutoSizeText = true;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int getAutoSizeMaxTextSize() {
        return Math.round(this.mAutoSizeMaxTextSizeInPx);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int getAutoSizeMinTextSize() {
        return Math.round(this.mAutoSizeMinTextSizeInPx);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int getAutoSizeStepGranularity() {
        return Math.round(this.mAutoSizeStepGranularityInPx);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextSizesInPx;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int getAutoSizeTextType() {
        return this.mAutoSizeTextType;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    boolean isAutoSizeEnabled() {
        return this.supportsAutoSizeText() && this.mAutoSizeTextType != 0;
    }
    
    void loadFromAttributes(final AttributeSet set, int resourceId) {
        final TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(set, R.styleable.AppCompatTextView, resourceId, 0);
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextView_autoSizeTextType)) {
            this.mAutoSizeTextType = obtainStyledAttributes.getInt(R.styleable.AppCompatTextView_autoSizeTextType, 0);
        }
        float dimension;
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextView_autoSizeStepGranularity)) {
            dimension = obtainStyledAttributes.getDimension(R.styleable.AppCompatTextView_autoSizeStepGranularity, -1.0f);
        }
        else {
            dimension = -1.0f;
        }
        float dimension2;
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextView_autoSizeMinTextSize)) {
            dimension2 = obtainStyledAttributes.getDimension(R.styleable.AppCompatTextView_autoSizeMinTextSize, -1.0f);
        }
        else {
            dimension2 = -1.0f;
        }
        float dimension3;
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextView_autoSizeMaxTextSize)) {
            dimension3 = obtainStyledAttributes.getDimension(R.styleable.AppCompatTextView_autoSizeMaxTextSize, -1.0f);
        }
        else {
            dimension3 = -1.0f;
        }
        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatTextView_autoSizePresetSizes)) {
            resourceId = obtainStyledAttributes.getResourceId(R.styleable.AppCompatTextView_autoSizePresetSizes, 0);
            if (resourceId > 0) {
                final TypedArray obtainTypedArray = obtainStyledAttributes.getResources().obtainTypedArray(resourceId);
                this.setupAutoSizeUniformPresetSizes(obtainTypedArray);
                obtainTypedArray.recycle();
            }
        }
        obtainStyledAttributes.recycle();
        if (this.supportsAutoSizeText()) {
            if (this.mAutoSizeTextType == 1) {
                if (!this.mHasPresetAutoSizeValues) {
                    final DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                    float applyDimension = dimension2;
                    if (dimension2 == -1.0f) {
                        applyDimension = TypedValue.applyDimension(2, 12.0f, displayMetrics);
                    }
                    float applyDimension2 = dimension3;
                    if (dimension3 == -1.0f) {
                        applyDimension2 = TypedValue.applyDimension(2, 112.0f, displayMetrics);
                    }
                    float n = dimension;
                    if (dimension == -1.0f) {
                        n = 1.0f;
                    }
                    this.validateAndSetAutoSizeTextTypeUniformConfiguration(applyDimension, applyDimension2, n);
                }
                this.setupAutoSizeText();
            }
        }
        else {
            this.mAutoSizeTextType = 0;
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void setAutoSizeTextTypeUniformWithConfiguration(final int n, final int n2, final int n3, final int n4) throws IllegalArgumentException {
        if (this.supportsAutoSizeText()) {
            final DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
            this.validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension(n4, (float)n, displayMetrics), TypedValue.applyDimension(n4, (float)n2, displayMetrics), TypedValue.applyDimension(n4, (float)n3, displayMetrics));
            if (this.setupAutoSizeText()) {
                this.autoSizeText();
            }
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull final int[] array, final int n) throws IllegalArgumentException {
        if (this.supportsAutoSizeText()) {
            final int length = array.length;
            int n2 = 0;
            if (length > 0) {
                final int[] array2 = new int[length];
                int[] copy;
                if (n == 0) {
                    copy = Arrays.copyOf(array, length);
                }
                else {
                    final DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                    while (true) {
                        copy = array2;
                        if (n2 >= length) {
                            break;
                        }
                        array2[n2] = Math.round(TypedValue.applyDimension(n, (float)array[n2], displayMetrics));
                        ++n2;
                    }
                }
                this.mAutoSizeTextSizesInPx = this.cleanupAutoSizePresetSizes(copy);
                if (!this.setupAutoSizeUniformPresetSizesConfiguration()) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("None of the preset sizes is valid: ");
                    sb.append(Arrays.toString(array));
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            else {
                this.mHasPresetAutoSizeValues = false;
            }
            if (this.setupAutoSizeText()) {
                this.autoSizeText();
            }
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void setAutoSizeTextTypeWithDefaults(final int i) {
        if (this.supportsAutoSizeText()) {
            switch (i) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown auto-size text type: ");
                    sb.append(i);
                    throw new IllegalArgumentException(sb.toString());
                }
                case 1: {
                    final DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
                    this.validateAndSetAutoSizeTextTypeUniformConfiguration(TypedValue.applyDimension(2, 12.0f, displayMetrics), TypedValue.applyDimension(2, 112.0f, displayMetrics), 1.0f);
                    if (this.setupAutoSizeText()) {
                        this.autoSizeText();
                        break;
                    }
                    break;
                }
                case 0: {
                    this.clearAutoSizeConfiguration();
                    break;
                }
            }
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    void setTextSizeInternal(final int n, final float n2) {
        Resources resources;
        if (this.mContext == null) {
            resources = Resources.getSystem();
        }
        else {
            resources = this.mContext.getResources();
        }
        this.setRawTextSize(TypedValue.applyDimension(n, n2, resources.getDisplayMetrics()));
    }
}
