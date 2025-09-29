package android.support.v4.graphics;

import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.Typeface.Builder;
import android.graphics.fonts.FontVariationAxis;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.util.Log;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Map;

@RequiresApi(26)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TypefaceCompatApi26Impl extends TypefaceCompatApi21Impl {
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
        Constructor constructorObtainFontFamilyCtor;
        Method methodObtainAddFontFromAssetManagerMethod;
        Method methodObtainAddFontFromBufferMethod;
        Method methodObtainFreezeMethod;
        Method methodObtainAbortCreationMethod;
        Method methodObtainCreateFromFamiliesWithDefaultMethod;
        Class cls = null;
        try {
            Class clsObtainFontFamily = obtainFontFamily();
            constructorObtainFontFamilyCtor = obtainFontFamilyCtor(clsObtainFontFamily);
            methodObtainAddFontFromAssetManagerMethod = obtainAddFontFromAssetManagerMethod(clsObtainFontFamily);
            methodObtainAddFontFromBufferMethod = obtainAddFontFromBufferMethod(clsObtainFontFamily);
            methodObtainFreezeMethod = obtainFreezeMethod(clsObtainFontFamily);
            methodObtainAbortCreationMethod = obtainAbortCreationMethod(clsObtainFontFamily);
            methodObtainCreateFromFamiliesWithDefaultMethod = obtainCreateFromFamiliesWithDefaultMethod(clsObtainFontFamily);
            cls = clsObtainFontFamily;
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            Log.e(TAG, "Unable to collect necessary methods for class " + e.getClass().getName(), e);
            constructorObtainFontFamilyCtor = null;
            methodObtainAddFontFromAssetManagerMethod = null;
            methodObtainAddFontFromBufferMethod = null;
            methodObtainFreezeMethod = null;
            methodObtainAbortCreationMethod = null;
            methodObtainCreateFromFamiliesWithDefaultMethod = null;
        }
        this.mFontFamily = cls;
        this.mFontFamilyCtor = constructorObtainFontFamilyCtor;
        this.mAddFontFromAssetManager = methodObtainAddFontFromAssetManagerMethod;
        this.mAddFontFromBuffer = methodObtainAddFontFromBufferMethod;
        this.mFreeze = methodObtainFreezeMethod;
        this.mAbortCreation = methodObtainAbortCreationMethod;
        this.mCreateFromFamiliesWithDefault = methodObtainCreateFromFamiliesWithDefaultMethod;
    }

    private boolean isFontFamilyPrivateAPIAvailable() {
        if (this.mAddFontFromAssetManager == null) {
            Log.w(TAG, "Unable to collect necessary private methods. Fallback to legacy implementation.");
        }
        return this.mAddFontFromAssetManager != null;
    }

    private Object newFamily() {
        try {
            return this.mFontFamilyCtor.newInstance(new Object[0]);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean addFontFromAssetManager(Context context, Object obj, String str, int i, int i2, int i3, @Nullable FontVariationAxis[] fontVariationAxisArr) {
        try {
            return ((Boolean) this.mAddFontFromAssetManager.invoke(obj, context.getAssets(), str, 0, false, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), fontVariationAxisArr)).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean addFontFromBuffer(Object obj, ByteBuffer byteBuffer, int i, int i2, int i3) {
        try {
            return ((Boolean) this.mAddFontFromBuffer.invoke(obj, byteBuffer, Integer.valueOf(i), null, Integer.valueOf(i2), Integer.valueOf(i3))).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    protected Typeface createFromFamiliesWithDefault(Object obj) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, NegativeArraySizeException {
        try {
            Object objNewInstance = Array.newInstance((Class<?>) this.mFontFamily, 1);
            Array.set(objNewInstance, 0, obj);
            return (Typeface) this.mCreateFromFamiliesWithDefault.invoke(null, objNewInstance, -1, -1);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean freeze(Object obj) {
        try {
            return ((Boolean) this.mFreeze.invoke(obj, new Object[0])).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void abortCreation(Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            this.mAbortCreation.invoke(obj, new Object[0]);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // android.support.v4.graphics.TypefaceCompatBaseImpl
    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFamilyFilesResourceEntry, Resources resources, int i) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (!isFontFamilyPrivateAPIAvailable()) {
            return super.createFromFontFamilyFilesResourceEntry(context, fontFamilyFilesResourceEntry, resources, i);
        }
        Object objNewFamily = newFamily();
        for (FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry : fontFamilyFilesResourceEntry.getEntries()) {
            if (!addFontFromAssetManager(context, objNewFamily, fontFileResourceEntry.getFileName(), fontFileResourceEntry.getTtcIndex(), fontFileResourceEntry.getWeight(), fontFileResourceEntry.isItalic() ? 1 : 0, FontVariationAxis.fromFontVariationSettings(fontFileResourceEntry.getVariationSettings()))) {
                abortCreation(objNewFamily);
                return null;
            }
        }
        if (freeze(objNewFamily)) {
            return createFromFamiliesWithDefault(objNewFamily);
        }
        return null;
    }

    @Override // android.support.v4.graphics.TypefaceCompatApi21Impl, android.support.v4.graphics.TypefaceCompatBaseImpl
    public Typeface createFromFontInfo(Context context,
            @Nullable CancellationSignal cancellationSignal,
            @NonNull FontsContractCompat.FontInfo[] fonts, int style) {
        if (fonts.length < 1) {
            return null;
        }
        if (!isFontFamilyPrivateAPIAvailable()) {
            // Even if the private API is not avaiable, don't use API 21 implemenation and use
            // public API to create Typeface from file descriptor.
            final FontsContractCompat.FontInfo bestFont = findBestInfo(fonts, style);
            final ContentResolver resolver = context.getContentResolver();
            try (ParcelFileDescriptor pfd =
                    resolver.openFileDescriptor(bestFont.getUri(), "r", cancellationSignal)) {
                if (pfd == null) {
                    return null;
                }
                return new Typeface.Builder(pfd.getFileDescriptor())
                        .setWeight(bestFont.getWeight())
                        .setItalic(bestFont.isItalic())
                        .build();
            } catch (IOException e) {
                return null;
            }
        }
        Map<Uri, ByteBuffer> uriBuffer = FontsContractCompat.prepareFontData(
                context, fonts, cancellationSignal);
        final Object fontFamily = newFamily();
        boolean atLeastOneFont = false;
        for (FontsContractCompat.FontInfo font : fonts) {
            final ByteBuffer fontBuffer = uriBuffer.get(font.getUri());
            if (fontBuffer == null) {
                continue;  // skip
            }
            final boolean success = addFontFromBuffer(fontFamily, fontBuffer,
                    font.getTtcIndex(), font.getWeight(), font.isItalic() ? 1 : 0);
            if (!success) {
                abortCreation(fontFamily);
                return null;
            }
            atLeastOneFont = true;
        }
        if (!atLeastOneFont) {
            abortCreation(fontFamily);
            return null;
        }
        if (!freeze(fontFamily)) {
            return null;
        }
        final Typeface typeface = createFromFamiliesWithDefault(fontFamily);
        return Typeface.create(typeface, style);
    }

    @Override // android.support.v4.graphics.TypefaceCompatBaseImpl
    @Nullable
    public Typeface createFromResourcesFontFile(Context context, Resources resources, int i, String str, int i2) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (!isFontFamilyPrivateAPIAvailable()) {
            return super.createFromResourcesFontFile(context, resources, i, str, i2);
        }
        Object objNewFamily = newFamily();
        if (!addFontFromAssetManager(context, objNewFamily, str, 0, -1, -1, null)) {
            abortCreation(objNewFamily);
            return null;
        }
        if (freeze(objNewFamily)) {
            return createFromFamiliesWithDefault(objNewFamily);
        }
        return null;
    }

    protected Class obtainFontFamily() throws ClassNotFoundException {
        return Class.forName(FONT_FAMILY_CLASS);
    }

    protected Constructor obtainFontFamilyCtor(Class cls) throws NoSuchMethodException {
        return cls.getConstructor(new Class[0]);
    }

    protected Method obtainAddFontFromAssetManagerMethod(Class cls) throws NoSuchMethodException {
        return cls.getMethod(ADD_FONT_FROM_ASSET_MANAGER_METHOD, AssetManager.class, String.class, Integer.TYPE, Boolean.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, FontVariationAxis[].class);
    }

    protected Method obtainAddFontFromBufferMethod(Class cls) throws NoSuchMethodException {
        return cls.getMethod(ADD_FONT_FROM_BUFFER_METHOD, ByteBuffer.class, Integer.TYPE, FontVariationAxis[].class, Integer.TYPE, Integer.TYPE);
    }

    protected Method obtainFreezeMethod(Class cls) throws NoSuchMethodException {
        return cls.getMethod(FREEZE_METHOD, new Class[0]);
    }

    protected Method obtainAbortCreationMethod(Class cls) throws NoSuchMethodException {
        return cls.getMethod(ABORT_CREATION_METHOD, new Class[0]);
    }

    protected Method obtainCreateFromFamiliesWithDefaultMethod(Class cls) throws NoSuchMethodException, SecurityException {
        Method declaredMethod = Typeface.class.getDeclaredMethod(CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD, Array.newInstance((Class<?>) cls, 1).getClass(), Integer.TYPE, Integer.TYPE);
        declaredMethod.setAccessible(true);
        return declaredMethod;
    }
}
