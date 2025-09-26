// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.graphics;

import android.os.ParcelFileDescriptor;
import android.content.res.AssetManager;
import android.net.Uri;
import java.util.Map;
import android.content.ContentResolver;
import java.io.IOException;
import android.graphics.Typeface$Builder;
import android.support.annotation.NonNull;
import android.support.v4.provider.FontsContractCompat;
import android.os.CancellationSignal;
import android.content.res.Resources;
import android.support.v4.content.res.FontResourcesParserCompat;
import java.lang.reflect.Array;
import android.graphics.Typeface;
import java.nio.ByteBuffer;
import android.support.annotation.Nullable;
import android.graphics.fonts.FontVariationAxis;
import android.content.Context;
import java.lang.reflect.InvocationTargetException;
import android.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import android.support.annotation.RestrictTo;
import android.support.annotation.RequiresApi;

@RequiresApi(26)
@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class TypefaceCompatApi26Impl extends TypefaceCompatApi21Impl
{
    private static final String ABORT_CREATION_METHOD = "abortCreation";
    private static final String ADD_FONT_FROM_ASSET_MANAGER_METHOD = "addFontFromAssetManager";
    private static final String ADD_FONT_FROM_BUFFER_METHOD = "addFontFromBuffer";
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String DEFAULT_FAMILY = "sans-serif";
    private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
    private static final String FREEZE_METHOD = "freeze";
    private static final int RESOLVE_BY_FONT_TABLE = -1;
    private static final String TAG = "TypefaceCompatApi26Impl";
    protected final Method mAbortCreation;
    protected final Method mAddFontFromAssetManager;
    protected final Method mAddFontFromBuffer;
    protected final Method mCreateFromFamiliesWithDefault;
    protected final Class mFontFamily;
    protected final Constructor mFontFamilyCtor;
    protected final Method mFreeze;
    
    public TypefaceCompatApi26Impl() {
        Class mFontFamily = null;
        Constructor obtainFontFamilyCtor;
        Method obtainAddFontFromAssetManagerMethod;
        Method obtainAddFontFromBufferMethod;
        Method obtainFreezeMethod;
        Method obtainAbortCreationMethod;
        Method obtainCreateFromFamiliesWithDefaultMethod;
        try {
            final Class obtainFontFamily = this.obtainFontFamily();
            obtainFontFamilyCtor = this.obtainFontFamilyCtor(obtainFontFamily);
            obtainAddFontFromAssetManagerMethod = this.obtainAddFontFromAssetManagerMethod(obtainFontFamily);
            obtainAddFontFromBufferMethod = this.obtainAddFontFromBufferMethod(obtainFontFamily);
            obtainFreezeMethod = this.obtainFreezeMethod(obtainFontFamily);
            obtainAbortCreationMethod = this.obtainAbortCreationMethod(obtainFontFamily);
            obtainCreateFromFamiliesWithDefaultMethod = this.obtainCreateFromFamiliesWithDefaultMethod(obtainFontFamily);
            mFontFamily = obtainFontFamily;
        }
        catch (final ClassNotFoundException | NoSuchMethodException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to collect necessary methods for class ");
            sb.append(ex.getClass().getName());
            Log.e("TypefaceCompatApi26Impl", sb.toString(), (Throwable)ex);
            final Constructor constructor = null;
            obtainAddFontFromAssetManagerMethod = null;
            final Method method = obtainFreezeMethod = obtainAddFontFromAssetManagerMethod;
            obtainAbortCreationMethod = (obtainCreateFromFamiliesWithDefaultMethod = obtainFreezeMethod);
            obtainAddFontFromBufferMethod = method;
            obtainFontFamilyCtor = constructor;
        }
        this.mFontFamily = mFontFamily;
        this.mFontFamilyCtor = obtainFontFamilyCtor;
        this.mAddFontFromAssetManager = obtainAddFontFromAssetManagerMethod;
        this.mAddFontFromBuffer = obtainAddFontFromBufferMethod;
        this.mFreeze = obtainFreezeMethod;
        this.mAbortCreation = obtainAbortCreationMethod;
        this.mCreateFromFamiliesWithDefault = obtainCreateFromFamiliesWithDefaultMethod;
    }
    
    private void abortCreation(final Object obj) {
        try {
            this.mAbortCreation.invoke(obj, new Object[0]);
        }
        catch (final IllegalAccessException | InvocationTargetException cause) {
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    private boolean addFontFromAssetManager(final Context context, final Object obj, final String s, final int i, final int j, final int k, @Nullable final FontVariationAxis[] array) {
        try {
            return (boolean)this.mAddFontFromAssetManager.invoke(obj, context.getAssets(), s, 0, false, i, j, k, array);
        }
        catch (final IllegalAccessException | InvocationTargetException cause) {
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    private boolean addFontFromBuffer(final Object obj, final ByteBuffer byteBuffer, final int i, final int j, final int k) {
        try {
            return (boolean)this.mAddFontFromBuffer.invoke(obj, byteBuffer, i, null, j, k);
        }
        catch (final IllegalAccessException | InvocationTargetException cause) {
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    private boolean freeze(final Object obj) {
        try {
            return (boolean)this.mFreeze.invoke(obj, new Object[0]);
        }
        catch (final IllegalAccessException | InvocationTargetException cause) {
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    private boolean isFontFamilyPrivateAPIAvailable() {
        if (this.mAddFontFromAssetManager == null) {
            Log.w("TypefaceCompatApi26Impl", "Unable to collect necessary private methods. Fallback to legacy implementation.");
        }
        return this.mAddFontFromAssetManager != null;
    }
    
    private Object newFamily() {
        try {
            return this.mFontFamilyCtor.newInstance(new Object[0]);
        }
        catch (final IllegalAccessException | InstantiationException | InvocationTargetException cause) {
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    protected Typeface createFromFamiliesWithDefault(final Object o) {
        try {
            final Object instance = Array.newInstance(this.mFontFamily, 1);
            Array.set(instance, 0, o);
            return (Typeface)this.mCreateFromFamiliesWithDefault.invoke(null, instance, -1, -1);
        }
        catch (final IllegalAccessException | InvocationTargetException cause) {
            throw new RuntimeException((Throwable)cause);
        }
    }
    
    @Override
    public Typeface createFromFontFamilyFilesResourceEntry(final Context context, final FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, final Resources resources, int i) {
        if (!this.isFontFamilyPrivateAPIAvailable()) {
            return super.createFromFontFamilyFilesResourceEntry(context, fontFamilyFilesResourceEntry, resources, i);
        }
        final Object family = this.newFamily();
        final FontResourcesParserCompat.FontFileResourceEntry[] entries = fontFamilyFilesResourceEntry.getEntries();
        int length;
        FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry;
        for (length = entries.length, i = 0; i < length; ++i) {
            fontFileResourceEntry = entries[i];
            if (!this.addFontFromAssetManager(context, family, fontFileResourceEntry.getFileName(), fontFileResourceEntry.getTtcIndex(), fontFileResourceEntry.getWeight(), fontFileResourceEntry.isItalic() ? 1 : 0, FontVariationAxis.fromFontVariationSettings(fontFileResourceEntry.getVariationSettings()))) {
                this.abortCreation(family);
                return null;
            }
        }
        if (!this.freeze(family)) {
            return null;
        }
        return this.createFromFamiliesWithDefault(family);
    }
    
    @Override
    public Typeface createFromFontInfo(final Context context, @Nullable CancellationSignal t, @NonNull FontsContractCompat.FontInfo[] openFileDescriptor, final int n) {
        if (openFileDescriptor.length < 1) {
            return null;
        }
        if (!this.isFontFamilyPrivateAPIAvailable()) {
            final FontsContractCompat.FontInfo bestInfo = this.findBestInfo(openFileDescriptor, n);
            final ContentResolver contentResolver = context.getContentResolver();
            try {
                openFileDescriptor = (FontsContractCompat.FontInfo[])(Object)contentResolver.openFileDescriptor(bestInfo.getUri(), "r", (CancellationSignal)t);
                if (openFileDescriptor == null) {
                    if (openFileDescriptor != null) {
                        ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                    }
                    return null;
                }
                try {
                    final Typeface build = new Typeface$Builder(((ParcelFileDescriptor)(Object)openFileDescriptor).getFileDescriptor()).setWeight(bestInfo.getWeight()).setItalic(bestInfo.isItalic()).build();
                    if (openFileDescriptor != null) {
                        ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                    }
                    return build;
                }
                catch (final Throwable t) {
                    try {
                        throw t;
                    }
                    finally {}
                }
                finally {
                    t = null;
                }
                if (openFileDescriptor != null) {
                    if (t != null) {
                        try {
                            ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                        }
                        catch (final Throwable exception) {
                            t.addSuppressed(exception);
                        }
                    }
                    else {
                        ((ParcelFileDescriptor)(Object)openFileDescriptor).close();
                    }
                }
                throw context;
            }
            catch (final IOException ex) {
                return null;
            }
        }
        final Map<Uri, ByteBuffer> prepareFontData = FontsContractCompat.prepareFontData(context, openFileDescriptor, (CancellationSignal)t);
        final Object family = this.newFamily();
        final int length = openFileDescriptor.length;
        boolean b = false;
        for (final FontsContractCompat.FontInfo fontInfo : openFileDescriptor) {
            final ByteBuffer byteBuffer = prepareFontData.get(fontInfo.getUri());
            if (byteBuffer != null) {
                if (!this.addFontFromBuffer(family, byteBuffer, fontInfo.getTtcIndex(), fontInfo.getWeight(), fontInfo.isItalic() ? 1 : 0)) {
                    this.abortCreation(family);
                    return null;
                }
                b = true;
            }
        }
        if (!b) {
            this.abortCreation(family);
            return null;
        }
        if (!this.freeze(family)) {
            return null;
        }
        return Typeface.create(this.createFromFamiliesWithDefault(family), n);
    }
    
    @Nullable
    @Override
    public Typeface createFromResourcesFontFile(final Context context, final Resources resources, final int n, final String s, final int n2) {
        if (!this.isFontFamilyPrivateAPIAvailable()) {
            return super.createFromResourcesFontFile(context, resources, n, s, n2);
        }
        final Object family = this.newFamily();
        if (!this.addFontFromAssetManager(context, family, s, 0, -1, -1, null)) {
            this.abortCreation(family);
            return null;
        }
        if (!this.freeze(family)) {
            return null;
        }
        return this.createFromFamiliesWithDefault(family);
    }
    
    protected Method obtainAbortCreationMethod(final Class clazz) throws NoSuchMethodException {
        return clazz.getMethod("abortCreation", (Class[])new Class[0]);
    }
    
    protected Method obtainAddFontFromAssetManagerMethod(final Class clazz) throws NoSuchMethodException {
        return clazz.getMethod("addFontFromAssetManager", AssetManager.class, String.class, Integer.TYPE, Boolean.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, FontVariationAxis[].class);
    }
    
    protected Method obtainAddFontFromBufferMethod(final Class clazz) throws NoSuchMethodException {
        return clazz.getMethod("addFontFromBuffer", ByteBuffer.class, Integer.TYPE, FontVariationAxis[].class, Integer.TYPE, Integer.TYPE);
    }
    
    protected Method obtainCreateFromFamiliesWithDefaultMethod(final Class componentType) throws NoSuchMethodException {
        final Method declaredMethod = Typeface.class.getDeclaredMethod("createFromFamiliesWithDefault", Array.newInstance(componentType, 1).getClass(), Integer.TYPE, Integer.TYPE);
        declaredMethod.setAccessible(true);
        return declaredMethod;
    }
    
    protected Class obtainFontFamily() throws ClassNotFoundException {
        return Class.forName("android.graphics.FontFamily");
    }
    
    protected Constructor obtainFontFamilyCtor(final Class clazz) throws NoSuchMethodException {
        return clazz.getConstructor((Class[])new Class[0]);
    }
    
    protected Method obtainFreezeMethod(final Class clazz) throws NoSuchMethodException {
        return clazz.getMethod("freeze", (Class[])new Class[0]);
    }
}
