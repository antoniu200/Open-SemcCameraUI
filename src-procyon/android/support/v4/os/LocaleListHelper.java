// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.os;

import java.util.Arrays;
import android.support.annotation.IntRange;
import android.os.Build$VERSION;
import android.support.annotation.Size;
import android.support.annotation.Nullable;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import android.support.annotation.NonNull;
import android.support.annotation.GuardedBy;
import java.util.Locale;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
final class LocaleListHelper
{
    private static final Locale EN_LATN;
    private static final Locale LOCALE_AR_XB;
    private static final Locale LOCALE_EN_XA;
    private static final int NUM_PSEUDO_LOCALES = 2;
    private static final String STRING_AR_XB = "ar-XB";
    private static final String STRING_EN_XA = "en-XA";
    @GuardedBy("sLock")
    private static LocaleListHelper sDefaultAdjustedLocaleList;
    @GuardedBy("sLock")
    private static LocaleListHelper sDefaultLocaleList;
    private static final Locale[] sEmptyList;
    private static final LocaleListHelper sEmptyLocaleList;
    @GuardedBy("sLock")
    private static Locale sLastDefaultLocale;
    @GuardedBy("sLock")
    private static LocaleListHelper sLastExplicitlySetLocaleList;
    private static final Object sLock;
    private final Locale[] mList;
    @NonNull
    private final String mStringRepresentation;
    
    static {
        sEmptyList = new Locale[0];
        sEmptyLocaleList = new LocaleListHelper(new Locale[0]);
        LOCALE_EN_XA = new Locale("en", "XA");
        LOCALE_AR_XB = new Locale("ar", "XB");
        EN_LATN = LocaleHelper.forLanguageTag("en-Latn");
        sLock = new Object();
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    LocaleListHelper(@NonNull final Locale locale, final LocaleListHelper localeListHelper) {
        if (locale == null) {
            throw new NullPointerException("topLocale is null");
        }
        final int n = 0;
        int length;
        if (localeListHelper == null) {
            length = 0;
        }
        else {
            length = localeListHelper.mList.length;
        }
        while (true) {
            for (int i = 0; i < length; ++i) {
                if (locale.equals(localeListHelper.mList[i])) {
                    int n2;
                    if (i == -1) {
                        n2 = 1;
                    }
                    else {
                        n2 = 0;
                    }
                    final int n3 = n2 + length;
                    final Locale[] mList = new Locale[n3];
                    mList[0] = (Locale)locale.clone();
                    if (i == -1) {
                        int n4;
                        for (int j = 0; j < length; j = n4) {
                            n4 = j + 1;
                            mList[n4] = (Locale)localeListHelper.mList[j].clone();
                        }
                    }
                    else {
                        int n5;
                        for (int k = 0; k < i; k = n5) {
                            n5 = k + 1;
                            mList[n5] = (Locale)localeListHelper.mList[k].clone();
                        }
                        ++i;
                        while (i < length) {
                            mList[i] = (Locale)localeListHelper.mList[i].clone();
                            ++i;
                        }
                    }
                    final StringBuilder sb = new StringBuilder();
                    for (int l = n; l < n3; ++l) {
                        sb.append(LocaleHelper.toLanguageTag(mList[l]));
                        if (l < n3 - 1) {
                            sb.append(',');
                        }
                    }
                    this.mList = mList;
                    this.mStringRepresentation = sb.toString();
                    return;
                }
            }
            int i = -1;
            continue;
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    LocaleListHelper(@NonNull final Locale... array) {
        if (array.length == 0) {
            this.mList = LocaleListHelper.sEmptyList;
            this.mStringRepresentation = "";
        }
        else {
            final Locale[] mList = new Locale[array.length];
            final HashSet set = new HashSet();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
                final Locale o = array[i];
                if (o == null) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("list[");
                    sb2.append(i);
                    sb2.append("] is null");
                    throw new NullPointerException(sb2.toString());
                }
                if (set.contains(o)) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("list[");
                    sb3.append(i);
                    sb3.append("] is a repetition");
                    throw new IllegalArgumentException(sb3.toString());
                }
                final Locale e = (Locale)o.clone();
                mList[i] = e;
                sb.append(LocaleHelper.toLanguageTag(e));
                if (i < array.length - 1) {
                    sb.append(',');
                }
                set.add(e);
            }
            this.mList = mList;
            this.mStringRepresentation = sb.toString();
        }
    }
    
    private Locale computeFirstMatch(final Collection<String> collection, final boolean b) {
        final int computeFirstMatchIndex = this.computeFirstMatchIndex(collection, b);
        Locale locale;
        if (computeFirstMatchIndex == -1) {
            locale = null;
        }
        else {
            locale = this.mList[computeFirstMatchIndex];
        }
        return locale;
    }
    
    private int computeFirstMatchIndex(final Collection<String> collection, final boolean b) {
        if (this.mList.length == 1) {
            return 0;
        }
        if (this.mList.length == 0) {
            return -1;
        }
        int firstMatchIndex = 0;
        Label_0051: {
            if (b) {
                firstMatchIndex = this.findFirstMatchIndex(LocaleListHelper.EN_LATN);
                if (firstMatchIndex == 0) {
                    return 0;
                }
                if (firstMatchIndex < Integer.MAX_VALUE) {
                    break Label_0051;
                }
            }
            firstMatchIndex = Integer.MAX_VALUE;
        }
        final Iterator<String> iterator = collection.iterator();
        while (iterator.hasNext()) {
            final int firstMatchIndex2 = this.findFirstMatchIndex(LocaleHelper.forLanguageTag(iterator.next()));
            if (firstMatchIndex2 == 0) {
                return 0;
            }
            if (firstMatchIndex2 >= firstMatchIndex) {
                continue;
            }
            firstMatchIndex = firstMatchIndex2;
        }
        if (firstMatchIndex == Integer.MAX_VALUE) {
            return 0;
        }
        return firstMatchIndex;
    }
    
    private int findFirstMatchIndex(final Locale locale) {
        for (int i = 0; i < this.mList.length; ++i) {
            if (matchScore(locale, this.mList[i]) > 0) {
                return i;
            }
        }
        return Integer.MAX_VALUE;
    }
    
    @NonNull
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    static LocaleListHelper forLanguageTags(@Nullable final String s) {
        if (s != null && !s.isEmpty()) {
            final String[] split = s.split(",", -1);
            final Locale[] array = new Locale[split.length];
            for (int i = 0; i < array.length; ++i) {
                array[i] = LocaleHelper.forLanguageTag(split[i]);
            }
            return new LocaleListHelper(array);
        }
        return getEmptyLocaleList();
    }
    
    @NonNull
    @Size(min = 1L)
    static LocaleListHelper getAdjustedDefault() {
        getDefault();
        synchronized (LocaleListHelper.sLock) {
            return LocaleListHelper.sDefaultAdjustedLocaleList;
        }
    }
    
    @NonNull
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @Size(min = 1L)
    static LocaleListHelper getDefault() {
        final Locale default1 = Locale.getDefault();
        synchronized (LocaleListHelper.sLock) {
            if (!default1.equals(LocaleListHelper.sLastDefaultLocale)) {
                LocaleListHelper.sLastDefaultLocale = default1;
                if (LocaleListHelper.sDefaultLocaleList != null && default1.equals(LocaleListHelper.sDefaultLocaleList.get(0))) {
                    return LocaleListHelper.sDefaultLocaleList;
                }
                LocaleListHelper.sDefaultLocaleList = new LocaleListHelper(default1, LocaleListHelper.sLastExplicitlySetLocaleList);
                LocaleListHelper.sDefaultAdjustedLocaleList = LocaleListHelper.sDefaultLocaleList;
            }
            return LocaleListHelper.sDefaultLocaleList;
        }
    }
    
    @NonNull
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    static LocaleListHelper getEmptyLocaleList() {
        return LocaleListHelper.sEmptyLocaleList;
    }
    
    private static String getLikelyScript(final Locale locale) {
        if (Build$VERSION.SDK_INT < 21) {
            return "";
        }
        final String script = locale.getScript();
        if (!script.isEmpty()) {
            return script;
        }
        return "";
    }
    
    private static boolean isPseudoLocale(final String s) {
        return "en-XA".equals(s) || "ar-XB".equals(s);
    }
    
    private static boolean isPseudoLocale(final Locale locale) {
        return LocaleListHelper.LOCALE_EN_XA.equals(locale) || LocaleListHelper.LOCALE_AR_XB.equals(locale);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    static boolean isPseudoLocalesOnly(@Nullable final String[] array) {
        if (array == null) {
            return true;
        }
        if (array.length > 3) {
            return false;
        }
        for (final String s : array) {
            if (!s.isEmpty() && !isPseudoLocale(s)) {
                return false;
            }
        }
        return true;
    }
    
    @IntRange(from = 0L, to = 1L)
    private static int matchScore(final Locale locale, final Locale obj) {
        final boolean equals = locale.equals(obj);
        final boolean b = true;
        if (equals) {
            return 1;
        }
        if (!locale.getLanguage().equals(obj.getLanguage())) {
            return 0;
        }
        if (isPseudoLocale(locale) || isPseudoLocale(obj)) {
            return 0;
        }
        final String likelyScript = getLikelyScript(locale);
        if (likelyScript.isEmpty()) {
            final String country = locale.getCountry();
            int n = b ? 1 : 0;
            if (!country.isEmpty()) {
                if (country.equals(obj.getCountry())) {
                    n = (b ? 1 : 0);
                }
                else {
                    n = 0;
                }
            }
            return n;
        }
        return likelyScript.equals(getLikelyScript(obj)) ? 1 : 0;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    static void setDefault(@NonNull @Size(min = 1L) final LocaleListHelper localeListHelper) {
        setDefault(localeListHelper, 0);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    static void setDefault(@NonNull @Size(min = 1L) final LocaleListHelper localeListHelper, final int n) {
        if (localeListHelper == null) {
            throw new NullPointerException("locales is null");
        }
        if (localeListHelper.isEmpty()) {
            throw new IllegalArgumentException("locales is empty");
        }
        synchronized (LocaleListHelper.sLock) {
            Locale.setDefault(LocaleListHelper.sLastDefaultLocale = localeListHelper.get(n));
            LocaleListHelper.sLastExplicitlySetLocaleList = localeListHelper;
            LocaleListHelper.sDefaultLocaleList = localeListHelper;
            if (n == 0) {
                LocaleListHelper.sDefaultAdjustedLocaleList = LocaleListHelper.sDefaultLocaleList;
            }
            else {
                LocaleListHelper.sDefaultAdjustedLocaleList = new LocaleListHelper(LocaleListHelper.sLastDefaultLocale, LocaleListHelper.sDefaultLocaleList);
            }
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LocaleListHelper)) {
            return false;
        }
        final Locale[] mList = ((LocaleListHelper)o).mList;
        if (this.mList.length != mList.length) {
            return false;
        }
        for (int i = 0; i < this.mList.length; ++i) {
            if (!this.mList[i].equals(mList[i])) {
                return false;
            }
        }
        return true;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    Locale get(final int n) {
        Locale locale;
        if (n >= 0 && n < this.mList.length) {
            locale = this.mList[n];
        }
        else {
            locale = null;
        }
        return locale;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    Locale getFirstMatch(final String[] a) {
        return this.computeFirstMatch(Arrays.asList(a), false);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int getFirstMatchIndex(final String[] a) {
        return this.computeFirstMatchIndex(Arrays.asList(a), false);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int getFirstMatchIndexWithEnglishSupported(final Collection<String> collection) {
        return this.computeFirstMatchIndex(collection, true);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int getFirstMatchIndexWithEnglishSupported(final String[] a) {
        return this.getFirstMatchIndexWithEnglishSupported(Arrays.asList(a));
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    Locale getFirstMatchWithEnglishSupported(final String[] a) {
        return this.computeFirstMatch(Arrays.asList(a), true);
    }
    
    @Override
    public int hashCode() {
        int n = 1;
        for (int i = 0; i < this.mList.length; ++i) {
            n = this.mList[i].hashCode() + 31 * n;
        }
        return n;
    }
    
    @IntRange(from = -1L)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int indexOf(final Locale obj) {
        for (int i = 0; i < this.mList.length; ++i) {
            if (this.mList[i].equals(obj)) {
                return i;
            }
        }
        return -1;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    boolean isEmpty() {
        return this.mList.length == 0;
    }
    
    @IntRange(from = 0L)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    int size() {
        return this.mList.length;
    }
    
    @NonNull
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    String toLanguageTags() {
        return this.mStringRepresentation;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < this.mList.length; ++i) {
            sb.append(this.mList[i]);
            if (i < this.mList.length - 1) {
                sb.append(',');
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
