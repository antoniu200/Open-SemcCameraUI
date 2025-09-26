// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.support.v4.util.LruCache;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.graphics.drawable.AnimatedStateListDrawableCompat;
import android.support.annotation.Nullable;
import android.content.res.Resources$Theme;
import android.support.annotation.RequiresApi;
import android.support.v7.content.res.AppCompatResources;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.graphics.ColorFilter;
import android.util.AttributeSet;
import android.content.res.XmlResourceParser;
import android.content.res.Resources;
import android.util.Log;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.os.Build$VERSION;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.graphics.ColorUtils;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.appcompat.R;
import android.util.TypedValue;
import android.content.res.ColorStateList;
import android.support.v4.util.SparseArrayCompat;
import android.graphics.drawable.Drawable$ConstantState;
import java.lang.ref.WeakReference;
import android.support.v4.util.LongSparseArray;
import android.content.Context;
import java.util.WeakHashMap;
import android.support.v4.util.ArrayMap;
import android.graphics.PorterDuff$Mode;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public final class AppCompatDrawableManager
{
    private static final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY;
    private static final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED;
    private static final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL;
    private static final ColorFilterLruCache COLOR_FILTER_CACHE;
    private static final boolean DEBUG = false;
    private static final PorterDuff$Mode DEFAULT_MODE;
    private static AppCompatDrawableManager INSTANCE;
    private static final String PLATFORM_VD_CLAZZ = "android.graphics.drawable.VectorDrawable";
    private static final String SKIP_DRAWABLE_TAG = "appcompat_skip_skip";
    private static final String TAG = "AppCompatDrawableManag";
    private static final int[] TINT_CHECKABLE_BUTTON_LIST;
    private static final int[] TINT_COLOR_CONTROL_NORMAL;
    private static final int[] TINT_COLOR_CONTROL_STATE_LIST;
    private ArrayMap<String, InflateDelegate> mDelegates;
    private final WeakHashMap<Context, LongSparseArray<WeakReference<Drawable$ConstantState>>> mDrawableCaches;
    private boolean mHasCheckedVectorDrawableSetup;
    private SparseArrayCompat<String> mKnownDrawableIdTags;
    private WeakHashMap<Context, SparseArrayCompat<ColorStateList>> mTintLists;
    private TypedValue mTypedValue;
    
    static {
        DEFAULT_MODE = PorterDuff$Mode.SRC_IN;
        COLOR_FILTER_CACHE = new ColorFilterLruCache(6);
        COLORFILTER_TINT_COLOR_CONTROL_NORMAL = new int[] { R.drawable.abc_textfield_search_default_mtrl_alpha, R.drawable.abc_textfield_default_mtrl_alpha, R.drawable.abc_ab_share_pack_mtrl_alpha };
        TINT_COLOR_CONTROL_NORMAL = new int[] { R.drawable.abc_ic_commit_search_api_mtrl_alpha, R.drawable.abc_seekbar_tick_mark_material, R.drawable.abc_ic_menu_share_mtrl_alpha, R.drawable.abc_ic_menu_copy_mtrl_am_alpha, R.drawable.abc_ic_menu_cut_mtrl_alpha, R.drawable.abc_ic_menu_selectall_mtrl_alpha, R.drawable.abc_ic_menu_paste_mtrl_am_alpha };
        COLORFILTER_COLOR_CONTROL_ACTIVATED = new int[] { R.drawable.abc_textfield_activated_mtrl_alpha, R.drawable.abc_textfield_search_activated_mtrl_alpha, R.drawable.abc_cab_background_top_mtrl_alpha, R.drawable.abc_text_cursor_material, R.drawable.abc_text_select_handle_left_mtrl_dark, R.drawable.abc_text_select_handle_middle_mtrl_dark, R.drawable.abc_text_select_handle_right_mtrl_dark, R.drawable.abc_text_select_handle_left_mtrl_light, R.drawable.abc_text_select_handle_middle_mtrl_light, R.drawable.abc_text_select_handle_right_mtrl_light };
        COLORFILTER_COLOR_BACKGROUND_MULTIPLY = new int[] { R.drawable.abc_popup_background_mtrl_mult, R.drawable.abc_cab_background_internal_bg, R.drawable.abc_menu_hardkey_panel_mtrl_mult };
        TINT_COLOR_CONTROL_STATE_LIST = new int[] { R.drawable.abc_tab_indicator_material, R.drawable.abc_textfield_search_material };
        TINT_CHECKABLE_BUTTON_LIST = new int[] { R.drawable.abc_btn_check_material, R.drawable.abc_btn_radio_material };
    }
    
    public AppCompatDrawableManager() {
        this.mDrawableCaches = new WeakHashMap<Context, LongSparseArray<WeakReference<Drawable$ConstantState>>>(0);
    }
    
    private void addDelegate(@NonNull final String s, @NonNull final InflateDelegate inflateDelegate) {
        if (this.mDelegates == null) {
            this.mDelegates = new ArrayMap<String, InflateDelegate>();
        }
        this.mDelegates.put(s, inflateDelegate);
    }
    
    private boolean addDrawableToCache(@NonNull final Context context, final long n, @NonNull final Drawable drawable) {
        synchronized (this) {
            final Drawable$ConstantState constantState = drawable.getConstantState();
            if (constantState != null) {
                LongSparseArray value;
                if ((value = this.mDrawableCaches.get(context)) == null) {
                    value = new LongSparseArray();
                    this.mDrawableCaches.put(context, value);
                }
                value.put(n, new WeakReference(constantState));
                return true;
            }
            return false;
        }
    }
    
    private void addTintListToCache(@NonNull final Context context, @DrawableRes final int n, @NonNull final ColorStateList list) {
        if (this.mTintLists == null) {
            this.mTintLists = new WeakHashMap<Context, SparseArrayCompat<ColorStateList>>();
        }
        SparseArrayCompat value;
        if ((value = this.mTintLists.get(context)) == null) {
            value = new SparseArrayCompat();
            this.mTintLists.put(context, value);
        }
        value.append(n, list);
    }
    
    private static boolean arrayContains(final int[] array, final int n) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i] == n) {
                return true;
            }
        }
        return false;
    }
    
    private void checkVectorDrawableSetup(@NonNull final Context context) {
        if (this.mHasCheckedVectorDrawableSetup) {
            return;
        }
        this.mHasCheckedVectorDrawableSetup = true;
        final Drawable drawable = this.getDrawable(context, R.drawable.abc_vector_test);
        if (drawable != null && isVectorDrawable(drawable)) {
            return;
        }
        this.mHasCheckedVectorDrawableSetup = false;
        throw new IllegalStateException("This app has been built with an incorrect configuration. Please configure your build for VectorDrawableCompat.");
    }
    
    private ColorStateList createBorderlessButtonColorStateList(@NonNull final Context context) {
        return this.createButtonColorStateList(context, 0);
    }
    
    private ColorStateList createButtonColorStateList(@NonNull final Context context, @ColorInt final int n) {
        final int themeAttrColor = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlHighlight);
        return new ColorStateList(new int[][] { ThemeUtils.DISABLED_STATE_SET, ThemeUtils.PRESSED_STATE_SET, ThemeUtils.FOCUSED_STATE_SET, ThemeUtils.EMPTY_STATE_SET }, new int[] { ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorButtonNormal), ColorUtils.compositeColors(themeAttrColor, n), ColorUtils.compositeColors(themeAttrColor, n), n });
    }
    
    private static long createCacheKey(final TypedValue typedValue) {
        return (long)typedValue.assetCookie << 32 | (long)typedValue.data;
    }
    
    private ColorStateList createColoredButtonColorStateList(@NonNull final Context context) {
        return this.createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, R.attr.colorAccent));
    }
    
    private ColorStateList createDefaultButtonColorStateList(@NonNull final Context context) {
        return this.createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, R.attr.colorButtonNormal));
    }
    
    private Drawable createDrawableIfNeeded(@NonNull final Context context, @DrawableRes final int n) {
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        final TypedValue mTypedValue = this.mTypedValue;
        context.getResources().getValue(n, mTypedValue, true);
        final long cacheKey = createCacheKey(mTypedValue);
        Object cachedDrawable = this.getCachedDrawable(context, cacheKey);
        if (cachedDrawable != null) {
            return (Drawable)cachedDrawable;
        }
        if (n == R.drawable.abc_cab_background_top_material) {
            cachedDrawable = new LayerDrawable(new Drawable[] { this.getDrawable(context, R.drawable.abc_cab_background_internal_bg), this.getDrawable(context, R.drawable.abc_cab_background_top_mtrl_alpha) });
        }
        if (cachedDrawable != null) {
            ((Drawable)cachedDrawable).setChangingConfigurations(mTypedValue.changingConfigurations);
            this.addDrawableToCache(context, cacheKey, (Drawable)cachedDrawable);
        }
        return (Drawable)cachedDrawable;
    }
    
    private ColorStateList createSwitchThumbColorStateList(final Context context) {
        final int[][] array = new int[3][];
        final int[] array2 = new int[3];
        final ColorStateList themeAttrColorStateList = ThemeUtils.getThemeAttrColorStateList(context, R.attr.colorSwitchThumbNormal);
        if (themeAttrColorStateList != null && themeAttrColorStateList.isStateful()) {
            array[0] = ThemeUtils.DISABLED_STATE_SET;
            array2[0] = themeAttrColorStateList.getColorForState(array[0], 0);
            array[1] = ThemeUtils.CHECKED_STATE_SET;
            array2[1] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
            array[2] = ThemeUtils.EMPTY_STATE_SET;
            array2[2] = themeAttrColorStateList.getDefaultColor();
        }
        else {
            array[0] = ThemeUtils.DISABLED_STATE_SET;
            array2[0] = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorSwitchThumbNormal);
            array[1] = ThemeUtils.CHECKED_STATE_SET;
            array2[1] = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated);
            array[2] = ThemeUtils.EMPTY_STATE_SET;
            array2[2] = ThemeUtils.getThemeAttrColor(context, R.attr.colorSwitchThumbNormal);
        }
        return new ColorStateList(array, array2);
    }
    
    private static PorterDuffColorFilter createTintFilter(final ColorStateList list, final PorterDuff$Mode porterDuff$Mode, final int[] array) {
        if (list != null && porterDuff$Mode != null) {
            return getPorterDuffColorFilter(list.getColorForState(array, 0), porterDuff$Mode);
        }
        return null;
    }
    
    public static AppCompatDrawableManager get() {
        synchronized (AppCompatDrawableManager.class) {
            if (AppCompatDrawableManager.INSTANCE == null) {
                installDefaultInflateDelegates(AppCompatDrawableManager.INSTANCE = new AppCompatDrawableManager());
            }
            return AppCompatDrawableManager.INSTANCE;
        }
    }
    
    private Drawable getCachedDrawable(@NonNull final Context key, final long n) {
        synchronized (this) {
            final LongSparseArray longSparseArray = this.mDrawableCaches.get(key);
            if (longSparseArray == null) {
                return null;
            }
            final WeakReference weakReference = (WeakReference)longSparseArray.get(n);
            if (weakReference != null) {
                final Drawable$ConstantState drawable$ConstantState = (Drawable$ConstantState)weakReference.get();
                if (drawable$ConstantState != null) {
                    return drawable$ConstantState.newDrawable(key.getResources());
                }
                longSparseArray.delete(n);
            }
            return null;
        }
    }
    
    public static PorterDuffColorFilter getPorterDuffColorFilter(final int n, final PorterDuff$Mode porterDuff$Mode) {
        synchronized (AppCompatDrawableManager.class) {
            PorterDuffColorFilter value;
            if ((value = AppCompatDrawableManager.COLOR_FILTER_CACHE.get(n, porterDuff$Mode)) == null) {
                value = new PorterDuffColorFilter(n, porterDuff$Mode);
                AppCompatDrawableManager.COLOR_FILTER_CACHE.put(n, porterDuff$Mode, value);
            }
            return value;
        }
    }
    
    private ColorStateList getTintListFromCache(@NonNull final Context key, @DrawableRes final int n) {
        final WeakHashMap<Context, SparseArrayCompat<ColorStateList>> mTintLists = this.mTintLists;
        final ColorStateList list = null;
        if (mTintLists != null) {
            final SparseArrayCompat sparseArrayCompat = this.mTintLists.get(key);
            ColorStateList list2 = list;
            if (sparseArrayCompat != null) {
                list2 = (ColorStateList)sparseArrayCompat.get(n);
            }
            return list2;
        }
        return null;
    }
    
    static PorterDuff$Mode getTintMode(final int n) {
        PorterDuff$Mode multiply;
        if (n == R.drawable.abc_switch_thumb_material) {
            multiply = PorterDuff$Mode.MULTIPLY;
        }
        else {
            multiply = null;
        }
        return multiply;
    }
    
    private static void installDefaultInflateDelegates(@NonNull final AppCompatDrawableManager appCompatDrawableManager) {
        if (Build$VERSION.SDK_INT < 24) {
            appCompatDrawableManager.addDelegate("vector", (InflateDelegate)new VdcInflateDelegate());
            appCompatDrawableManager.addDelegate("animated-vector", (InflateDelegate)new AvdcInflateDelegate());
            appCompatDrawableManager.addDelegate("animated-selector", (InflateDelegate)new AsldcInflateDelegate());
        }
    }
    
    private static boolean isVectorDrawable(@NonNull final Drawable drawable) {
        return drawable instanceof VectorDrawableCompat || "android.graphics.drawable.VectorDrawable".equals(drawable.getClass().getName());
    }
    
    private Drawable loadDrawableFromDelegates(@NonNull final Context context, @DrawableRes final int n) {
        if (this.mDelegates == null || this.mDelegates.isEmpty()) {
            return null;
        }
        if (this.mKnownDrawableIdTags != null) {
            final String anObject = this.mKnownDrawableIdTags.get(n);
            if ("appcompat_skip_skip".equals(anObject) || (anObject != null && this.mDelegates.get(anObject) == null)) {
                return null;
            }
        }
        else {
            this.mKnownDrawableIdTags = new SparseArrayCompat<String>();
        }
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        final TypedValue mTypedValue = this.mTypedValue;
        final Resources resources = context.getResources();
        resources.getValue(n, mTypedValue, true);
        final long cacheKey = createCacheKey(mTypedValue);
        final Drawable cachedDrawable = this.getCachedDrawable(context, cacheKey);
        if (cachedDrawable != null) {
            return cachedDrawable;
        }
        Drawable drawable = cachedDrawable;
        if (mTypedValue.string != null) {
            drawable = cachedDrawable;
            if (mTypedValue.string.toString().endsWith(".xml")) {
                drawable = cachedDrawable;
                try {
                    final XmlResourceParser xml = resources.getXml(n);
                    drawable = cachedDrawable;
                    final AttributeSet attributeSet = Xml.asAttributeSet((XmlPullParser)xml);
                    int next;
                    do {
                        drawable = cachedDrawable;
                        next = ((XmlPullParser)xml).next();
                    } while (next != 2 && next != 1);
                    if (next != 2) {
                        drawable = cachedDrawable;
                        drawable = cachedDrawable;
                        final XmlPullParserException ex = new XmlPullParserException("No start tag found");
                        drawable = cachedDrawable;
                        throw ex;
                    }
                    drawable = cachedDrawable;
                    final String name = ((XmlPullParser)xml).getName();
                    drawable = cachedDrawable;
                    this.mKnownDrawableIdTags.append(n, name);
                    drawable = cachedDrawable;
                    final InflateDelegate inflateDelegate = this.mDelegates.get(name);
                    Drawable fromXmlInner = cachedDrawable;
                    if (inflateDelegate != null) {
                        drawable = cachedDrawable;
                        fromXmlInner = inflateDelegate.createFromXmlInner(context, (XmlPullParser)xml, attributeSet, context.getTheme());
                    }
                    if ((drawable = fromXmlInner) != null) {
                        drawable = fromXmlInner;
                        fromXmlInner.setChangingConfigurations(mTypedValue.changingConfigurations);
                        drawable = fromXmlInner;
                        this.addDrawableToCache(context, cacheKey, fromXmlInner);
                        drawable = fromXmlInner;
                    }
                }
                catch (final Exception ex2) {
                    Log.e("AppCompatDrawableManag", "Exception while inflating drawable", (Throwable)ex2);
                }
            }
        }
        if (drawable == null) {
            this.mKnownDrawableIdTags.append(n, "appcompat_skip_skip");
        }
        return drawable;
    }
    
    private void removeDelegate(@NonNull final String s, @NonNull final InflateDelegate inflateDelegate) {
        if (this.mDelegates != null && this.mDelegates.get(s) == inflateDelegate) {
            this.mDelegates.remove(s);
        }
    }
    
    private static void setPorterDuffColorFilter(final Drawable drawable, final int n, final PorterDuff$Mode porterDuff$Mode) {
        Drawable mutate = drawable;
        if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
            mutate = drawable.mutate();
        }
        PorterDuff$Mode default_MODE;
        if ((default_MODE = porterDuff$Mode) == null) {
            default_MODE = AppCompatDrawableManager.DEFAULT_MODE;
        }
        mutate.setColorFilter((ColorFilter)getPorterDuffColorFilter(n, default_MODE));
    }
    
    private Drawable tintDrawable(@NonNull final Context context, @DrawableRes final int n, final boolean b, @NonNull final Drawable drawable) {
        final ColorStateList tintList = this.getTintList(context, n);
        Drawable drawable2;
        if (tintList != null) {
            Drawable mutate = drawable;
            if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
                mutate = drawable.mutate();
            }
            final Drawable wrap = DrawableCompat.wrap(mutate);
            DrawableCompat.setTintList(wrap, tintList);
            final PorterDuff$Mode tintMode = getTintMode(n);
            drawable2 = wrap;
            if (tintMode != null) {
                DrawableCompat.setTintMode(wrap, tintMode);
                drawable2 = wrap;
            }
        }
        else if (n == R.drawable.abc_seekbar_track_material) {
            final LayerDrawable layerDrawable = (LayerDrawable)drawable;
            setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908288), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), AppCompatDrawableManager.DEFAULT_MODE);
            setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), AppCompatDrawableManager.DEFAULT_MODE);
            setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), AppCompatDrawableManager.DEFAULT_MODE);
            drawable2 = drawable;
        }
        else if (n != R.drawable.abc_ratingbar_material && n != R.drawable.abc_ratingbar_indicator_material && n != R.drawable.abc_ratingbar_small_material) {
            drawable2 = drawable;
            if (!tintDrawableUsingColorFilter(context, n, drawable)) {
                drawable2 = drawable;
                if (b) {
                    drawable2 = null;
                }
            }
        }
        else {
            final LayerDrawable layerDrawable2 = (LayerDrawable)drawable;
            setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(16908288), ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorControlNormal), AppCompatDrawableManager.DEFAULT_MODE);
            setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), AppCompatDrawableManager.DEFAULT_MODE);
            setPorterDuffColorFilter(layerDrawable2.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), AppCompatDrawableManager.DEFAULT_MODE);
            drawable2 = drawable;
        }
        return drawable2;
    }
    
    static void tintDrawable(final Drawable drawable, final TintInfo tintInfo, final int[] array) {
        if (DrawableUtils.canSafelyMutateDrawable(drawable) && drawable.mutate() != drawable) {
            Log.d("AppCompatDrawableManag", "Mutated drawable is not the same instance as the input.");
            return;
        }
        if (!tintInfo.mHasTintList && !tintInfo.mHasTintMode) {
            drawable.clearColorFilter();
        }
        else {
            ColorStateList mTintList;
            if (tintInfo.mHasTintList) {
                mTintList = tintInfo.mTintList;
            }
            else {
                mTintList = null;
            }
            PorterDuff$Mode porterDuff$Mode;
            if (tintInfo.mHasTintMode) {
                porterDuff$Mode = tintInfo.mTintMode;
            }
            else {
                porterDuff$Mode = AppCompatDrawableManager.DEFAULT_MODE;
            }
            drawable.setColorFilter((ColorFilter)createTintFilter(mTintList, porterDuff$Mode, array));
        }
        if (Build$VERSION.SDK_INT <= 23) {
            drawable.invalidateSelf();
        }
    }
    
    static boolean tintDrawableUsingColorFilter(@NonNull final Context context, @DrawableRes int n, @NonNull final Drawable drawable) {
        PorterDuff$Mode porterDuff$Mode = AppCompatDrawableManager.DEFAULT_MODE;
        final boolean arrayContains = arrayContains(AppCompatDrawableManager.COLORFILTER_TINT_COLOR_CONTROL_NORMAL, n);
        final int n2 = 16842801;
        int round = 0;
        int n3 = 0;
        Label_0118: {
            Label_0029: {
                if (arrayContains) {
                    n = R.attr.colorControlNormal;
                }
                else if (arrayContains(AppCompatDrawableManager.COLORFILTER_COLOR_CONTROL_ACTIVATED, n)) {
                    n = R.attr.colorControlActivated;
                }
                else if (arrayContains(AppCompatDrawableManager.COLORFILTER_COLOR_BACKGROUND_MULTIPLY, n)) {
                    porterDuff$Mode = PorterDuff$Mode.MULTIPLY;
                    n = n2;
                }
                else {
                    if (n == R.drawable.abc_list_divider_mtrl_alpha) {
                        n = 16842800;
                        round = Math.round(40.8f);
                        break Label_0029;
                    }
                    if (n != R.drawable.abc_dialog_material_background) {
                        round = -1;
                        n = 0;
                        n3 = 0;
                        break Label_0118;
                    }
                    n = n2;
                }
                round = -1;
            }
            final int n4 = 1;
            n3 = n;
            n = n4;
        }
        if (n != 0) {
            Drawable mutate = drawable;
            if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
                mutate = drawable.mutate();
            }
            mutate.setColorFilter((ColorFilter)getPorterDuffColorFilter(ThemeUtils.getThemeAttrColor(context, n3), porterDuff$Mode));
            if (round != -1) {
                mutate.setAlpha(round);
            }
            return true;
        }
        return false;
    }
    
    public Drawable getDrawable(@NonNull final Context context, @DrawableRes final int n) {
        synchronized (this) {
            return this.getDrawable(context, n, false);
        }
    }
    
    Drawable getDrawable(@NonNull final Context context, @DrawableRes final int n, final boolean b) {
        synchronized (this) {
            this.checkVectorDrawableSetup(context);
            Drawable drawable;
            if ((drawable = this.loadDrawableFromDelegates(context, n)) == null) {
                drawable = this.createDrawableIfNeeded(context, n);
            }
            Drawable drawable2;
            if ((drawable2 = drawable) == null) {
                drawable2 = ContextCompat.getDrawable(context, n);
            }
            Drawable tintDrawable;
            if ((tintDrawable = drawable2) != null) {
                tintDrawable = this.tintDrawable(context, n, b, drawable2);
            }
            if (tintDrawable != null) {
                DrawableUtils.fixDrawable(tintDrawable);
            }
            return tintDrawable;
        }
    }
    
    ColorStateList getTintList(@NonNull final Context context, @DrawableRes final int n) {
        synchronized (this) {
            ColorStateList tintListFromCache;
            ColorStateList list = tintListFromCache = this.getTintListFromCache(context, n);
            if (list == null) {
                if (n == R.drawable.abc_edit_text_material) {
                    list = AppCompatResources.getColorStateList(context, R.color.abc_tint_edittext);
                }
                else if (n == R.drawable.abc_switch_track_mtrl_alpha) {
                    list = AppCompatResources.getColorStateList(context, R.color.abc_tint_switch_track);
                }
                else if (n == R.drawable.abc_switch_thumb_material) {
                    list = this.createSwitchThumbColorStateList(context);
                }
                else if (n == R.drawable.abc_btn_default_mtrl_shape) {
                    list = this.createDefaultButtonColorStateList(context);
                }
                else if (n == R.drawable.abc_btn_borderless_material) {
                    list = this.createBorderlessButtonColorStateList(context);
                }
                else if (n == R.drawable.abc_btn_colored_material) {
                    list = this.createColoredButtonColorStateList(context);
                }
                else if (n != R.drawable.abc_spinner_mtrl_am_alpha && n != R.drawable.abc_spinner_textfield_background_material) {
                    if (arrayContains(AppCompatDrawableManager.TINT_COLOR_CONTROL_NORMAL, n)) {
                        list = ThemeUtils.getThemeAttrColorStateList(context, R.attr.colorControlNormal);
                    }
                    else if (arrayContains(AppCompatDrawableManager.TINT_COLOR_CONTROL_STATE_LIST, n)) {
                        list = AppCompatResources.getColorStateList(context, R.color.abc_tint_default);
                    }
                    else if (arrayContains(AppCompatDrawableManager.TINT_CHECKABLE_BUTTON_LIST, n)) {
                        list = AppCompatResources.getColorStateList(context, R.color.abc_tint_btn_checkable);
                    }
                    else if (n == R.drawable.abc_seekbar_thumb_material) {
                        list = AppCompatResources.getColorStateList(context, R.color.abc_tint_seek_thumb);
                    }
                }
                else {
                    list = AppCompatResources.getColorStateList(context, R.color.abc_tint_spinner);
                }
                if ((tintListFromCache = list) != null) {
                    this.addTintListToCache(context, n, list);
                    tintListFromCache = list;
                }
            }
            return tintListFromCache;
        }
    }
    
    public void onConfigurationChanged(@NonNull final Context key) {
        synchronized (this) {
            final LongSparseArray longSparseArray = this.mDrawableCaches.get(key);
            if (longSparseArray != null) {
                longSparseArray.clear();
            }
        }
    }
    
    Drawable onDrawableLoadedFromResources(@NonNull final Context context, @NonNull final VectorEnabledTintResources vectorEnabledTintResources, @DrawableRes final int n) {
        synchronized (this) {
            Drawable drawable;
            if ((drawable = this.loadDrawableFromDelegates(context, n)) == null) {
                drawable = vectorEnabledTintResources.superGetDrawable(n);
            }
            if (drawable != null) {
                return this.tintDrawable(context, n, false, drawable);
            }
            return null;
        }
    }
    
    @RequiresApi(11)
    static class AsldcInflateDelegate implements InflateDelegate
    {
        @Override
        public Drawable createFromXmlInner(@NonNull final Context context, @NonNull final XmlPullParser xmlPullParser, @NonNull final AttributeSet set, @Nullable final Resources$Theme resources$Theme) {
            try {
                return AnimatedStateListDrawableCompat.createFromXmlInner(context, context.getResources(), xmlPullParser, set, resources$Theme);
            }
            catch (final Exception ex) {
                Log.e("AsldcInflateDelegate", "Exception while inflating <animated-selector>", (Throwable)ex);
                return null;
            }
        }
    }
    
    private interface InflateDelegate
    {
        Drawable createFromXmlInner(@NonNull final Context p0, @NonNull final XmlPullParser p1, @NonNull final AttributeSet p2, @Nullable final Resources$Theme p3);
    }
    
    private static class AvdcInflateDelegate implements InflateDelegate
    {
        AvdcInflateDelegate() {
        }
        
        @Override
        public Drawable createFromXmlInner(@NonNull final Context context, @NonNull final XmlPullParser xmlPullParser, @NonNull final AttributeSet set, @Nullable final Resources$Theme resources$Theme) {
            try {
                return AnimatedVectorDrawableCompat.createFromXmlInner(context, context.getResources(), xmlPullParser, set, resources$Theme);
            }
            catch (final Exception ex) {
                Log.e("AvdcInflateDelegate", "Exception while inflating <animated-vector>", (Throwable)ex);
                return null;
            }
        }
    }
    
    private static class ColorFilterLruCache extends LruCache<Integer, PorterDuffColorFilter>
    {
        public ColorFilterLruCache(final int n) {
            super(n);
        }
        
        private static int generateCacheKey(final int n, final PorterDuff$Mode porterDuff$Mode) {
            return 31 * (n + 31) + porterDuff$Mode.hashCode();
        }
        
        PorterDuffColorFilter get(final int n, final PorterDuff$Mode porterDuff$Mode) {
            return this.get(generateCacheKey(n, porterDuff$Mode));
        }
        
        PorterDuffColorFilter put(final int n, final PorterDuff$Mode porterDuff$Mode, final PorterDuffColorFilter porterDuffColorFilter) {
            return this.put(generateCacheKey(n, porterDuff$Mode), porterDuffColorFilter);
        }
    }
    
    private static class VdcInflateDelegate implements InflateDelegate
    {
        VdcInflateDelegate() {
        }
        
        @Override
        public Drawable createFromXmlInner(@NonNull final Context context, @NonNull final XmlPullParser xmlPullParser, @NonNull final AttributeSet set, @Nullable final Resources$Theme resources$Theme) {
            try {
                return VectorDrawableCompat.createFromXmlInner(context.getResources(), xmlPullParser, set, resources$Theme);
            }
            catch (final Exception ex) {
                Log.e("VdcInflateDelegate", "Exception while inflating <vector>", (Throwable)ex);
                return null;
            }
        }
    }
}
