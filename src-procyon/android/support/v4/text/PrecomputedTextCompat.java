// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.text;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import android.text.TextDirectionHeuristics;
import android.support.v4.util.ObjectsCompat;
import android.text.PrecomputedText$Params$Builder;
import android.text.PrecomputedText$Params;
import android.text.TextDirectionHeuristic;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.support.annotation.RestrictTo;
import android.support.annotation.UiThread;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import android.support.annotation.IntRange;
import android.text.Layout$Alignment;
import android.text.StaticLayout;
import android.text.StaticLayout$Builder;
import android.text.TextUtils;
import java.util.ArrayList;
import android.os.Build$VERSION;
import android.support.v4.os.TraceCompat;
import android.support.v4.util.Preconditions;
import android.text.SpannableString;
import android.support.annotation.RequiresApi;
import android.support.annotation.Nullable;
import android.text.PrecomputedText;
import android.support.annotation.NonNull;
import android.support.annotation.GuardedBy;
import java.util.concurrent.Executor;
import android.text.Spannable;

public class PrecomputedTextCompat implements Spannable
{
    private static final char LINE_FEED = '\n';
    @GuardedBy("sLock")
    @NonNull
    private static Executor sExecutor;
    private static final Object sLock;
    @NonNull
    private final int[] mParagraphEnds;
    @NonNull
    private final Params mParams;
    @NonNull
    private final Spannable mText;
    @Nullable
    private final PrecomputedText mWrapped;
    
    static {
        sLock = new Object();
    }
    
    @RequiresApi(28)
    private PrecomputedTextCompat(@NonNull final PrecomputedText precomputedText, @NonNull final Params mParams) {
        this.mText = (Spannable)precomputedText;
        this.mParams = mParams;
        this.mParagraphEnds = null;
        this.mWrapped = precomputedText;
    }
    
    private PrecomputedTextCompat(@NonNull final CharSequence charSequence, @NonNull final Params mParams, @NonNull final int[] mParagraphEnds) {
        this.mText = (Spannable)new SpannableString(charSequence);
        this.mParams = mParams;
        this.mParagraphEnds = mParagraphEnds;
        this.mWrapped = null;
    }
    
    public static PrecomputedTextCompat create(@NonNull final CharSequence charSequence, @NonNull final Params params) {
        Preconditions.checkNotNull(charSequence);
        Preconditions.checkNotNull(params);
        try {
            TraceCompat.beginSection("PrecomputedText");
            if (Build$VERSION.SDK_INT >= 28 && params.mWrapped != null) {
                return new PrecomputedTextCompat(PrecomputedText.create(charSequence, params.mWrapped), params);
            }
            final ArrayList<Integer> list = new ArrayList<Integer>();
            final int length = charSequence.length();
            int i = 0;
            while (i < length) {
                i = TextUtils.indexOf(charSequence, '\n', i, length);
                if (i < 0) {
                    i = length;
                }
                else {
                    ++i;
                }
                list.add(i);
            }
            final int[] array = new int[list.size()];
            for (int j = 0; j < list.size(); ++j) {
                array[j] = list.get(j);
            }
            if (Build$VERSION.SDK_INT >= 23) {
                StaticLayout$Builder.obtain(charSequence, 0, charSequence.length(), params.getTextPaint(), Integer.MAX_VALUE).setBreakStrategy(params.getBreakStrategy()).setHyphenationFrequency(params.getHyphenationFrequency()).setTextDirection(params.getTextDirection()).build();
            }
            else if (Build$VERSION.SDK_INT >= 21) {
                new StaticLayout(charSequence, params.getTextPaint(), Integer.MAX_VALUE, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
            return new PrecomputedTextCompat(charSequence, params, array);
        }
        finally {
            TraceCompat.endSection();
        }
    }
    
    private int findParaIndex(@IntRange(from = 0L) final int i) {
        for (int j = 0; j < this.mParagraphEnds.length; ++j) {
            if (i < this.mParagraphEnds[j]) {
                return j;
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("pos must be less than ");
        sb.append(this.mParagraphEnds[this.mParagraphEnds.length - 1]);
        sb.append(", gave ");
        sb.append(i);
        throw new IndexOutOfBoundsException(sb.toString());
    }
    
    @UiThread
    public static Future<PrecomputedTextCompat> getTextFuture(@NonNull final CharSequence charSequence, @NonNull Params params, @Nullable final Executor executor) {
        params = (Params)new PrecomputedTextFutureTask(params, charSequence);
        final Executor executor2 = executor;
        if (executor == null) {
            synchronized (PrecomputedTextCompat.sLock) {
                if (PrecomputedTextCompat.sExecutor == null) {
                    PrecomputedTextCompat.sExecutor = Executors.newFixedThreadPool(1);
                }
                final Executor sExecutor = PrecomputedTextCompat.sExecutor;
            }
        }
        executor2.execute((Runnable)params);
        return (Future<PrecomputedTextCompat>)params;
    }
    
    public char charAt(final int n) {
        return this.mText.charAt(n);
    }
    
    @IntRange(from = 0L)
    public int getParagraphCount() {
        if (Build$VERSION.SDK_INT >= 28) {
            return this.mWrapped.getParagraphCount();
        }
        return this.mParagraphEnds.length;
    }
    
    @IntRange(from = 0L)
    public int getParagraphEnd(@IntRange(from = 0L) final int n) {
        Preconditions.checkArgumentInRange(n, 0, this.getParagraphCount(), "paraIndex");
        if (Build$VERSION.SDK_INT >= 28) {
            return this.mWrapped.getParagraphEnd(n);
        }
        return this.mParagraphEnds[n];
    }
    
    @IntRange(from = 0L)
    public int getParagraphStart(@IntRange(from = 0L) int n) {
        final int paragraphCount = this.getParagraphCount();
        final int n2 = 0;
        Preconditions.checkArgumentInRange(n, 0, paragraphCount, "paraIndex");
        if (Build$VERSION.SDK_INT >= 28) {
            return this.mWrapped.getParagraphStart(n);
        }
        if (n == 0) {
            n = n2;
        }
        else {
            n = this.mParagraphEnds[n - 1];
        }
        return n;
    }
    
    @NonNull
    public Params getParams() {
        return this.mParams;
    }
    
    @Nullable
    @RequiresApi(28)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public PrecomputedText getPrecomputedText() {
        if (this.mText instanceof PrecomputedText) {
            return (PrecomputedText)this.mText;
        }
        return null;
    }
    
    public int getSpanEnd(final Object o) {
        return this.mText.getSpanEnd(o);
    }
    
    public int getSpanFlags(final Object o) {
        return this.mText.getSpanFlags(o);
    }
    
    public int getSpanStart(final Object o) {
        return this.mText.getSpanStart(o);
    }
    
    public <T> T[] getSpans(final int n, final int n2, final Class<T> clazz) {
        if (Build$VERSION.SDK_INT >= 28) {
            return (T[])this.mWrapped.getSpans(n, n2, (Class)clazz);
        }
        return (T[])this.mText.getSpans(n, n2, (Class)clazz);
    }
    
    public int length() {
        return this.mText.length();
    }
    
    public int nextSpanTransition(final int n, final int n2, final Class clazz) {
        return this.mText.nextSpanTransition(n, n2, clazz);
    }
    
    public void removeSpan(final Object o) {
        if (o instanceof MetricAffectingSpan) {
            throw new IllegalArgumentException("MetricAffectingSpan can not be removed from PrecomputedText.");
        }
        if (Build$VERSION.SDK_INT >= 28) {
            this.mWrapped.removeSpan(o);
        }
        else {
            this.mText.removeSpan(o);
        }
    }
    
    public void setSpan(final Object o, final int n, final int n2, final int n3) {
        if (o instanceof MetricAffectingSpan) {
            throw new IllegalArgumentException("MetricAffectingSpan can not be set to PrecomputedText.");
        }
        if (Build$VERSION.SDK_INT >= 28) {
            this.mWrapped.setSpan(o, n, n2, n3);
        }
        else {
            this.mText.setSpan(o, n, n2, n3);
        }
    }
    
    public CharSequence subSequence(final int n, final int n2) {
        return this.mText.subSequence(n, n2);
    }
    
    @Override
    public String toString() {
        return this.mText.toString();
    }
    
    public static final class Params
    {
        private final int mBreakStrategy;
        private final int mHyphenationFrequency;
        @NonNull
        private final TextPaint mPaint;
        @Nullable
        private final TextDirectionHeuristic mTextDir;
        final PrecomputedText$Params mWrapped;
        
        @RequiresApi(28)
        public Params(@NonNull final PrecomputedText$Params mWrapped) {
            this.mPaint = mWrapped.getTextPaint();
            this.mTextDir = mWrapped.getTextDirection();
            this.mBreakStrategy = mWrapped.getBreakStrategy();
            this.mHyphenationFrequency = mWrapped.getHyphenationFrequency();
            this.mWrapped = mWrapped;
        }
        
        Params(@NonNull final TextPaint mPaint, @NonNull final TextDirectionHeuristic textDirectionHeuristic, final int n, final int n2) {
            if (Build$VERSION.SDK_INT >= 28) {
                this.mWrapped = new PrecomputedText$Params$Builder(mPaint).setBreakStrategy(n).setHyphenationFrequency(n2).setTextDirection(textDirectionHeuristic).build();
            }
            else {
                this.mWrapped = null;
            }
            this.mPaint = mPaint;
            this.mTextDir = textDirectionHeuristic;
            this.mBreakStrategy = n;
            this.mHyphenationFrequency = n2;
        }
        
        @Override
        public boolean equals(@Nullable final Object o) {
            if (o == this) {
                return true;
            }
            if (o == null || !(o instanceof Params)) {
                return false;
            }
            final Params params = (Params)o;
            if (this.mWrapped != null) {
                return this.mWrapped.equals((Object)params.mWrapped);
            }
            if (Build$VERSION.SDK_INT >= 23) {
                if (this.mBreakStrategy != params.getBreakStrategy()) {
                    return false;
                }
                if (this.mHyphenationFrequency != params.getHyphenationFrequency()) {
                    return false;
                }
            }
            if (Build$VERSION.SDK_INT >= 18 && this.mTextDir != params.getTextDirection()) {
                return false;
            }
            if (this.mPaint.getTextSize() != params.getTextPaint().getTextSize()) {
                return false;
            }
            if (this.mPaint.getTextScaleX() != params.getTextPaint().getTextScaleX()) {
                return false;
            }
            if (this.mPaint.getTextSkewX() != params.getTextPaint().getTextSkewX()) {
                return false;
            }
            if (Build$VERSION.SDK_INT >= 21) {
                if (this.mPaint.getLetterSpacing() != params.getTextPaint().getLetterSpacing()) {
                    return false;
                }
                if (!TextUtils.equals((CharSequence)this.mPaint.getFontFeatureSettings(), (CharSequence)params.getTextPaint().getFontFeatureSettings())) {
                    return false;
                }
            }
            if (this.mPaint.getFlags() != params.getTextPaint().getFlags()) {
                return false;
            }
            if (Build$VERSION.SDK_INT >= 24) {
                if (!this.mPaint.getTextLocales().equals((Object)params.getTextPaint().getTextLocales())) {
                    return false;
                }
            }
            else if (Build$VERSION.SDK_INT >= 17 && !this.mPaint.getTextLocale().equals(params.getTextPaint().getTextLocale())) {
                return false;
            }
            if (this.mPaint.getTypeface() == null) {
                if (params.getTextPaint().getTypeface() != null) {
                    return false;
                }
            }
            else if (!this.mPaint.getTypeface().equals((Object)params.getTextPaint().getTypeface())) {
                return false;
            }
            return true;
        }
        
        @RequiresApi(23)
        public int getBreakStrategy() {
            return this.mBreakStrategy;
        }
        
        @RequiresApi(23)
        public int getHyphenationFrequency() {
            return this.mHyphenationFrequency;
        }
        
        @Nullable
        @RequiresApi(18)
        public TextDirectionHeuristic getTextDirection() {
            return this.mTextDir;
        }
        
        @NonNull
        public TextPaint getTextPaint() {
            return this.mPaint;
        }
        
        @Override
        public int hashCode() {
            if (Build$VERSION.SDK_INT >= 24) {
                return ObjectsCompat.hash(this.mPaint.getTextSize(), this.mPaint.getTextScaleX(), this.mPaint.getTextSkewX(), this.mPaint.getLetterSpacing(), this.mPaint.getFlags(), this.mPaint.getTextLocales(), this.mPaint.getTypeface(), this.mPaint.isElegantTextHeight(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }
            if (Build$VERSION.SDK_INT >= 21) {
                return ObjectsCompat.hash(this.mPaint.getTextSize(), this.mPaint.getTextScaleX(), this.mPaint.getTextSkewX(), this.mPaint.getLetterSpacing(), this.mPaint.getFlags(), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mPaint.isElegantTextHeight(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }
            if (Build$VERSION.SDK_INT >= 18) {
                return ObjectsCompat.hash(this.mPaint.getTextSize(), this.mPaint.getTextScaleX(), this.mPaint.getTextSkewX(), this.mPaint.getFlags(), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }
            if (Build$VERSION.SDK_INT >= 17) {
                return ObjectsCompat.hash(this.mPaint.getTextSize(), this.mPaint.getTextScaleX(), this.mPaint.getTextSkewX(), this.mPaint.getFlags(), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }
            return ObjectsCompat.hash(this.mPaint.getTextSize(), this.mPaint.getTextScaleX(), this.mPaint.getTextSkewX(), this.mPaint.getFlags(), this.mPaint.getTypeface(), this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("{");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("textSize=");
            sb2.append(this.mPaint.getTextSize());
            sb.append(sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(", textScaleX=");
            sb3.append(this.mPaint.getTextScaleX());
            sb.append(sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append(", textSkewX=");
            sb4.append(this.mPaint.getTextSkewX());
            sb.append(sb4.toString());
            if (Build$VERSION.SDK_INT >= 21) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append(", letterSpacing=");
                sb5.append(this.mPaint.getLetterSpacing());
                sb.append(sb5.toString());
                final StringBuilder sb6 = new StringBuilder();
                sb6.append(", elegantTextHeight=");
                sb6.append(this.mPaint.isElegantTextHeight());
                sb.append(sb6.toString());
            }
            if (Build$VERSION.SDK_INT >= 24) {
                final StringBuilder sb7 = new StringBuilder();
                sb7.append(", textLocale=");
                sb7.append(this.mPaint.getTextLocales());
                sb.append(sb7.toString());
            }
            else if (Build$VERSION.SDK_INT >= 17) {
                final StringBuilder sb8 = new StringBuilder();
                sb8.append(", textLocale=");
                sb8.append(this.mPaint.getTextLocale());
                sb.append(sb8.toString());
            }
            final StringBuilder sb9 = new StringBuilder();
            sb9.append(", typeface=");
            sb9.append(this.mPaint.getTypeface());
            sb.append(sb9.toString());
            if (Build$VERSION.SDK_INT >= 26) {
                final StringBuilder sb10 = new StringBuilder();
                sb10.append(", variationSettings=");
                sb10.append(this.mPaint.getFontVariationSettings());
                sb.append(sb10.toString());
            }
            final StringBuilder sb11 = new StringBuilder();
            sb11.append(", textDir=");
            sb11.append(this.mTextDir);
            sb.append(sb11.toString());
            final StringBuilder sb12 = new StringBuilder();
            sb12.append(", breakStrategy=");
            sb12.append(this.mBreakStrategy);
            sb.append(sb12.toString());
            final StringBuilder sb13 = new StringBuilder();
            sb13.append(", hyphenationFrequency=");
            sb13.append(this.mHyphenationFrequency);
            sb.append(sb13.toString());
            sb.append("}");
            return sb.toString();
        }
        
        public static class Builder
        {
            private int mBreakStrategy;
            private int mHyphenationFrequency;
            @NonNull
            private final TextPaint mPaint;
            private TextDirectionHeuristic mTextDir;
            
            public Builder(@NonNull final TextPaint mPaint) {
                this.mPaint = mPaint;
                if (Build$VERSION.SDK_INT >= 23) {
                    this.mBreakStrategy = 1;
                    this.mHyphenationFrequency = 1;
                }
                else {
                    this.mHyphenationFrequency = 0;
                    this.mBreakStrategy = 0;
                }
                if (Build$VERSION.SDK_INT >= 18) {
                    this.mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
                }
                else {
                    this.mTextDir = null;
                }
            }
            
            @NonNull
            public Params build() {
                return new Params(this.mPaint, this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }
            
            @RequiresApi(23)
            public Builder setBreakStrategy(final int mBreakStrategy) {
                this.mBreakStrategy = mBreakStrategy;
                return this;
            }
            
            @RequiresApi(23)
            public Builder setHyphenationFrequency(final int mHyphenationFrequency) {
                this.mHyphenationFrequency = mHyphenationFrequency;
                return this;
            }
            
            @RequiresApi(18)
            public Builder setTextDirection(@NonNull final TextDirectionHeuristic mTextDir) {
                this.mTextDir = mTextDir;
                return this;
            }
        }
    }
    
    private static class PrecomputedTextFutureTask extends FutureTask<PrecomputedTextCompat>
    {
        PrecomputedTextFutureTask(@NonNull final Params params, @NonNull final CharSequence charSequence) {
            super(new PrecomputedTextCallback(params, charSequence));
        }
        
        private static class PrecomputedTextCallback implements Callable<PrecomputedTextCompat>
        {
            private Params mParams;
            private CharSequence mText;
            
            PrecomputedTextCallback(@NonNull final Params mParams, @NonNull final CharSequence mText) {
                this.mParams = mParams;
                this.mText = mText;
            }
            
            @Override
            public PrecomputedTextCompat call() throws Exception {
                return PrecomputedTextCompat.create(this.mText, this.mParams);
            }
        }
    }
}
