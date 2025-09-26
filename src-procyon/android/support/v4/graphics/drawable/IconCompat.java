// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.graphics.drawable;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import android.support.annotation.ColorInt;
import java.io.OutputStream;
import android.graphics.Bitmap$CompressFormat;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import android.app.ActivityManager;
import android.support.v4.content.ContextCompat;
import android.content.Intent$ShortcutIconResource;
import android.content.Intent;
import java.io.InputStream;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.graphics.BitmapFactory;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.File;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import android.support.annotation.IdRes;
import java.lang.reflect.InvocationTargetException;
import android.os.Build$VERSION;
import android.support.annotation.DrawableRes;
import android.net.Uri;
import android.support.annotation.VisibleForTesting;
import android.graphics.Shader;
import android.graphics.Matrix;
import android.graphics.BitmapShader;
import android.graphics.Shader$TileMode;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Bitmap$Config;
import android.graphics.Bitmap;
import android.content.res.Resources;
import android.support.annotation.RequiresApi;
import android.content.res.Resources$NotFoundException;
import android.support.v4.util.Preconditions;
import android.graphics.drawable.Icon;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.content.res.ColorStateList;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;
import android.graphics.PorterDuff$Mode;
import androidx.versionedparcelable.CustomVersionedParcelable;

public class IconCompat extends CustomVersionedParcelable
{
    private static final float ADAPTIVE_ICON_INSET_FACTOR = 0.25f;
    private static final int AMBIENT_SHADOW_ALPHA = 30;
    private static final float BLUR_FACTOR = 0.010416667f;
    static final PorterDuff$Mode DEFAULT_TINT_MODE;
    private static final float DEFAULT_VIEW_PORT_SCALE = 0.6666667f;
    private static final String EXTRA_INT1 = "int1";
    private static final String EXTRA_INT2 = "int2";
    private static final String EXTRA_OBJ = "obj";
    private static final String EXTRA_TINT_LIST = "tint_list";
    private static final String EXTRA_TINT_MODE = "tint_mode";
    private static final String EXTRA_TYPE = "type";
    private static final float ICON_DIAMETER_FACTOR = 0.9166667f;
    private static final int KEY_SHADOW_ALPHA = 61;
    private static final float KEY_SHADOW_OFFSET_FACTOR = 0.020833334f;
    private static final String TAG = "IconCompat";
    public static final int TYPE_UNKNOWN = -1;
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public byte[] mData;
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public int mInt1;
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public int mInt2;
    Object mObj1;
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public Parcelable mParcelable;
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public ColorStateList mTintList;
    PorterDuff$Mode mTintMode;
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public String mTintModeStr;
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public int mType;
    
    static {
        DEFAULT_TINT_MODE = PorterDuff$Mode.SRC_IN;
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public IconCompat() {
        this.mTintList = null;
        this.mTintMode = IconCompat.DEFAULT_TINT_MODE;
    }
    
    private IconCompat(final int mType) {
        this.mTintList = null;
        this.mTintMode = IconCompat.DEFAULT_TINT_MODE;
        this.mType = mType;
    }
    
    @Nullable
    public static IconCompat createFromBundle(@NonNull final Bundle bundle) {
        final int int1 = bundle.getInt("type");
        final IconCompat iconCompat = new IconCompat(int1);
        iconCompat.mInt1 = bundle.getInt("int1");
        iconCompat.mInt2 = bundle.getInt("int2");
        if (bundle.containsKey("tint_list")) {
            iconCompat.mTintList = (ColorStateList)bundle.getParcelable("tint_list");
        }
        if (bundle.containsKey("tint_mode")) {
            iconCompat.mTintMode = PorterDuff$Mode.valueOf(bundle.getString("tint_mode"));
        }
        if (int1 != -1) {
            switch (int1) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown type ");
                    sb.append(int1);
                    Log.w("IconCompat", sb.toString());
                    return null;
                }
                case 3: {
                    iconCompat.mObj1 = bundle.getByteArray("obj");
                    return iconCompat;
                }
                case 2:
                case 4: {
                    iconCompat.mObj1 = bundle.getString("obj");
                    return iconCompat;
                }
                case 1:
                case 5: {
                    break;
                }
            }
        }
        iconCompat.mObj1 = bundle.getParcelable("obj");
        return iconCompat;
    }
    
    @Nullable
    @RequiresApi(23)
    public static IconCompat createFromIcon(@NonNull final Context context, @NonNull final Icon mObj1) {
        Preconditions.checkNotNull(mObj1);
        final int type = getType(mObj1);
        if (type != 2) {
            if (type != 4) {
                final IconCompat iconCompat = new IconCompat(-1);
                iconCompat.mObj1 = mObj1;
                return iconCompat;
            }
            return createWithContentUri(getUri(mObj1));
        }
        else {
            final String resPackage = getResPackage(mObj1);
            try {
                return createWithResource(getResources(context, resPackage), resPackage, getResId(mObj1));
            }
            catch (final Resources$NotFoundException ex) {
                throw new IllegalArgumentException("Icon resource cannot be found");
            }
        }
    }
    
    @Nullable
    @RequiresApi(23)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static IconCompat createFromIcon(@NonNull final Icon mObj1) {
        Preconditions.checkNotNull(mObj1);
        final int type = getType(mObj1);
        if (type == 2) {
            return createWithResource(null, getResPackage(mObj1), getResId(mObj1));
        }
        if (type != 4) {
            final IconCompat iconCompat = new IconCompat(-1);
            iconCompat.mObj1 = mObj1;
            return iconCompat;
        }
        return createWithContentUri(getUri(mObj1));
    }
    
    @VisibleForTesting
    static Bitmap createLegacyIconFromAdaptiveIcon(final Bitmap bitmap, final boolean b) {
        final int n = (int)(0.6666667f * Math.min(bitmap.getWidth(), bitmap.getHeight()));
        final Bitmap bitmap2 = Bitmap.createBitmap(n, n, Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap2);
        final Paint paint = new Paint(3);
        final float n2 = (float)n;
        final float n3 = 0.5f * n2;
        final float n4 = 0.9166667f * n3;
        if (b) {
            final float n5 = 0.010416667f * n2;
            paint.setColor(0);
            paint.setShadowLayer(n5, 0.0f, 0.020833334f * n2, 1023410176);
            canvas.drawCircle(n3, n3, n4, paint);
            paint.setShadowLayer(n5, 0.0f, 0.0f, 503316480);
            canvas.drawCircle(n3, n3, n4, paint);
            paint.clearShadowLayer();
        }
        paint.setColor(-16777216);
        final BitmapShader shader = new BitmapShader(bitmap, Shader$TileMode.CLAMP, Shader$TileMode.CLAMP);
        final Matrix localMatrix = new Matrix();
        localMatrix.setTranslate((float)(-(bitmap.getWidth() - n) / 2), (float)(-(bitmap.getHeight() - n) / 2));
        shader.setLocalMatrix(localMatrix);
        paint.setShader((Shader)shader);
        canvas.drawCircle(n3, n3, n4, paint);
        canvas.setBitmap((Bitmap)null);
        return bitmap2;
    }
    
    public static IconCompat createWithAdaptiveBitmap(final Bitmap mObj1) {
        if (mObj1 == null) {
            throw new IllegalArgumentException("Bitmap must not be null.");
        }
        final IconCompat iconCompat = new IconCompat(5);
        iconCompat.mObj1 = mObj1;
        return iconCompat;
    }
    
    public static IconCompat createWithBitmap(final Bitmap mObj1) {
        if (mObj1 == null) {
            throw new IllegalArgumentException("Bitmap must not be null.");
        }
        final IconCompat iconCompat = new IconCompat(1);
        iconCompat.mObj1 = mObj1;
        return iconCompat;
    }
    
    public static IconCompat createWithContentUri(final Uri uri) {
        if (uri == null) {
            throw new IllegalArgumentException("Uri must not be null.");
        }
        return createWithContentUri(uri.toString());
    }
    
    public static IconCompat createWithContentUri(final String mObj1) {
        if (mObj1 == null) {
            throw new IllegalArgumentException("Uri must not be null.");
        }
        final IconCompat iconCompat = new IconCompat(4);
        iconCompat.mObj1 = mObj1;
        return iconCompat;
    }
    
    public static IconCompat createWithData(final byte[] mObj1, final int mInt1, final int mInt2) {
        if (mObj1 == null) {
            throw new IllegalArgumentException("Data must not be null.");
        }
        final IconCompat iconCompat = new IconCompat(3);
        iconCompat.mObj1 = mObj1;
        iconCompat.mInt1 = mInt1;
        iconCompat.mInt2 = mInt2;
        return iconCompat;
    }
    
    public static IconCompat createWithResource(final Context context, @DrawableRes final int n) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }
        return createWithResource(context.getResources(), context.getPackageName(), n);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public static IconCompat createWithResource(final Resources resources, final String mObj1, @DrawableRes final int mInt1) {
        if (mObj1 == null) {
            throw new IllegalArgumentException("Package must not be null.");
        }
        if (mInt1 == 0) {
            throw new IllegalArgumentException("Drawable resource ID must not be 0");
        }
        final IconCompat iconCompat = new IconCompat(2);
        iconCompat.mInt1 = mInt1;
        if (resources != null) {
            try {
                iconCompat.mObj1 = resources.getResourceName(mInt1);
                return iconCompat;
            }
            catch (final Resources$NotFoundException ex) {
                throw new IllegalArgumentException("Icon resource cannot be found");
            }
        }
        iconCompat.mObj1 = mObj1;
        return iconCompat;
    }
    
    @DrawableRes
    @IdRes
    @RequiresApi(23)
    private static int getResId(@NonNull final Icon obj) {
        if (Build$VERSION.SDK_INT >= 28) {
            return obj.getResId();
        }
        try {
            return (int)obj.getClass().getMethod("getResId", (Class<?>[])new Class[0]).invoke(obj, new Object[0]);
        }
        catch (final NoSuchMethodException ex) {
            Log.e("IconCompat", "Unable to get icon resource", (Throwable)ex);
            return 0;
        }
        catch (final InvocationTargetException ex2) {
            Log.e("IconCompat", "Unable to get icon resource", (Throwable)ex2);
            return 0;
        }
        catch (final IllegalAccessException ex3) {
            Log.e("IconCompat", "Unable to get icon resource", (Throwable)ex3);
            return 0;
        }
    }
    
    @Nullable
    @RequiresApi(23)
    private static String getResPackage(@NonNull final Icon obj) {
        if (Build$VERSION.SDK_INT >= 28) {
            return obj.getResPackage();
        }
        try {
            return (String)obj.getClass().getMethod("getResPackage", (Class<?>[])new Class[0]).invoke(obj, new Object[0]);
        }
        catch (final NoSuchMethodException ex) {
            Log.e("IconCompat", "Unable to get icon package", (Throwable)ex);
            return null;
        }
        catch (final InvocationTargetException ex2) {
            Log.e("IconCompat", "Unable to get icon package", (Throwable)ex2);
            return null;
        }
        catch (final IllegalAccessException ex3) {
            Log.e("IconCompat", "Unable to get icon package", (Throwable)ex3);
            return null;
        }
    }
    
    private static Resources getResources(final Context context, final String anObject) {
        if ("android".equals(anObject)) {
            return Resources.getSystem();
        }
        final PackageManager packageManager = context.getPackageManager();
        try {
            final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(anObject, 8192);
            if (applicationInfo != null) {
                return packageManager.getResourcesForApplication(applicationInfo);
            }
            return null;
        }
        catch (final PackageManager$NameNotFoundException ex) {
            Log.e("IconCompat", String.format("Unable to find pkg=%s for icon", anObject), (Throwable)ex);
            return null;
        }
    }
    
    @RequiresApi(23)
    private static int getType(@NonNull final Icon icon) {
        if (Build$VERSION.SDK_INT >= 28) {
            return icon.getType();
        }
        try {
            return (int)icon.getClass().getMethod("getType", (Class<?>[])new Class[0]).invoke(icon, new Object[0]);
        }
        catch (final NoSuchMethodException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to get icon type ");
            sb.append(icon);
            Log.e("IconCompat", sb.toString(), (Throwable)ex);
            return -1;
        }
        catch (final InvocationTargetException ex2) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to get icon type ");
            sb2.append(icon);
            Log.e("IconCompat", sb2.toString(), (Throwable)ex2);
            return -1;
        }
        catch (final IllegalAccessException ex3) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Unable to get icon type ");
            sb3.append(icon);
            Log.e("IconCompat", sb3.toString(), (Throwable)ex3);
            return -1;
        }
    }
    
    @Nullable
    @RequiresApi(23)
    private static Uri getUri(@NonNull final Icon obj) {
        if (Build$VERSION.SDK_INT >= 28) {
            return obj.getUri();
        }
        try {
            return (Uri)obj.getClass().getMethod("getUri", (Class<?>[])new Class[0]).invoke(obj, new Object[0]);
        }
        catch (final NoSuchMethodException ex) {
            Log.e("IconCompat", "Unable to get icon uri", (Throwable)ex);
            return null;
        }
        catch (final InvocationTargetException ex2) {
            Log.e("IconCompat", "Unable to get icon uri", (Throwable)ex2);
            return null;
        }
        catch (final IllegalAccessException ex3) {
            Log.e("IconCompat", "Unable to get icon uri", (Throwable)ex3);
            return null;
        }
    }
    
    private Drawable loadDrawableInner(final Context context) {
        switch (this.mType) {
            case 5: {
                return (Drawable)new BitmapDrawable(context.getResources(), createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, false));
            }
            case 4: {
                final Uri parse = Uri.parse((String)this.mObj1);
                final String scheme = parse.getScheme();
                InputStream openInputStream = null;
                Label_0223: {
                    Label_0221: {
                        if (!"content".equals(scheme)) {
                            if (!"file".equals(scheme)) {
                                try {
                                    openInputStream = new FileInputStream(new File((String)this.mObj1));
                                    break Label_0223;
                                }
                                catch (final FileNotFoundException ex) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("Unable to load image from path: ");
                                    sb.append(parse);
                                    Log.w("IconCompat", sb.toString(), (Throwable)ex);
                                    break Label_0221;
                                }
                            }
                        }
                        try {
                            openInputStream = context.getContentResolver().openInputStream(parse);
                            break Label_0223;
                        }
                        catch (final Exception ex2) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Unable to load image from URI: ");
                            sb2.append(parse);
                            Log.w("IconCompat", sb2.toString(), (Throwable)ex2);
                        }
                    }
                    openInputStream = null;
                }
                if (openInputStream != null) {
                    return (Drawable)new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(openInputStream));
                }
                break;
            }
            case 3: {
                return (Drawable)new BitmapDrawable(context.getResources(), BitmapFactory.decodeByteArray((byte[])this.mObj1, this.mInt1, this.mInt2));
            }
            case 2: {
                String s;
                if (TextUtils.isEmpty((CharSequence)(s = this.getResPackage()))) {
                    s = context.getPackageName();
                }
                final Resources resources = getResources(context, s);
                try {
                    return ResourcesCompat.getDrawable(resources, this.mInt1, context.getTheme());
                }
                catch (final RuntimeException ex3) {
                    Log.e("IconCompat", String.format("Unable to load resource 0x%08x from pkg=%s", this.mInt1, this.mObj1), (Throwable)ex3);
                    break;
                }
            }
            case 1: {
                return (Drawable)new BitmapDrawable(context.getResources(), (Bitmap)this.mObj1);
            }
        }
        return null;
    }
    
    private static String typeToString(final int n) {
        switch (n) {
            default: {
                return "UNKNOWN";
            }
            case 5: {
                return "BITMAP_MASKABLE";
            }
            case 4: {
                return "URI";
            }
            case 3: {
                return "DATA";
            }
            case 2: {
                return "RESOURCE";
            }
            case 1: {
                return "BITMAP";
            }
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void addToShortcutIntent(@NonNull final Intent intent, @Nullable final Drawable drawable, @NonNull Context packageContext) {
        this.checkResource(packageContext);
        final int mType = this.mType;
        Bitmap bitmap = null;
        if (mType != 5) {
            switch (mType) {
                default: {
                    throw new IllegalArgumentException("Icon type not supported for intent shortcuts");
                }
                case 2: {
                    try {
                        packageContext = packageContext.createPackageContext(this.getResPackage(), 0);
                        if (drawable == null) {
                            intent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", (Parcelable)Intent$ShortcutIconResource.fromContext(packageContext, this.mInt1));
                            return;
                        }
                        final Drawable drawable2 = ContextCompat.getDrawable(packageContext, this.mInt1);
                        if (drawable2.getIntrinsicWidth() > 0 && drawable2.getIntrinsicHeight() > 0) {
                            bitmap = Bitmap.createBitmap(drawable2.getIntrinsicWidth(), drawable2.getIntrinsicHeight(), Bitmap$Config.ARGB_8888);
                        }
                        else {
                            final int launcherLargeIconSize = ((ActivityManager)packageContext.getSystemService("activity")).getLauncherLargeIconSize();
                            bitmap = Bitmap.createBitmap(launcherLargeIconSize, launcherLargeIconSize, Bitmap$Config.ARGB_8888);
                        }
                        drawable2.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                        drawable2.draw(new Canvas(bitmap));
                        break;
                    }
                    catch (final PackageManager$NameNotFoundException cause) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Can't find package ");
                        sb.append(this.mObj1);
                        throw new IllegalArgumentException(sb.toString(), (Throwable)cause);
                    }
                }
                case 1: {
                    final Bitmap bitmap2 = bitmap = (Bitmap)this.mObj1;
                    if (drawable != null) {
                        bitmap = bitmap2.copy(bitmap2.getConfig(), true);
                        break;
                    }
                    break;
                }
            }
        }
        else {
            bitmap = createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, true);
        }
        if (drawable != null) {
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();
            drawable.setBounds(width / 2, height / 2, width, height);
            drawable.draw(new Canvas(bitmap));
        }
        intent.putExtra("android.intent.extra.shortcut.ICON", (Parcelable)bitmap);
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void checkResource(final Context context) {
        if (this.mType == 2) {
            final String s = (String)this.mObj1;
            if (!s.contains(":")) {
                return;
            }
            final String s2 = s.split(":", -1)[1];
            final String s3 = s2.split("/", -1)[0];
            final String str = s2.split("/", -1)[1];
            final String str2 = s.split(":", -1)[0];
            final int identifier = getResources(context, str2).getIdentifier(str, s3, str2);
            if (this.mInt1 != identifier) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Id has changed for ");
                sb.append(str2);
                sb.append("/");
                sb.append(str);
                Log.i("IconCompat", sb.toString());
                this.mInt1 = identifier;
            }
        }
    }
    
    @IdRes
    public int getResId() {
        if (this.mType == -1 && Build$VERSION.SDK_INT >= 23) {
            return getResId((Icon)this.mObj1);
        }
        if (this.mType != 2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("called getResId() on ");
            sb.append(this);
            throw new IllegalStateException(sb.toString());
        }
        return this.mInt1;
    }
    
    @NonNull
    public String getResPackage() {
        if (this.mType == -1 && Build$VERSION.SDK_INT >= 23) {
            return getResPackage((Icon)this.mObj1);
        }
        if (this.mType != 2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("called getResPackage() on ");
            sb.append(this);
            throw new IllegalStateException(sb.toString());
        }
        return ((String)this.mObj1).split(":", -1)[0];
    }
    
    public int getType() {
        if (this.mType == -1 && Build$VERSION.SDK_INT >= 23) {
            return getType((Icon)this.mObj1);
        }
        return this.mType;
    }
    
    @NonNull
    public Uri getUri() {
        if (this.mType == -1 && Build$VERSION.SDK_INT >= 23) {
            return getUri((Icon)this.mObj1);
        }
        return Uri.parse((String)this.mObj1);
    }
    
    public Drawable loadDrawable(final Context context) {
        this.checkResource(context);
        if (Build$VERSION.SDK_INT >= 23) {
            return this.toIcon().loadDrawable(context);
        }
        final Drawable loadDrawableInner = this.loadDrawableInner(context);
        if (loadDrawableInner != null && (this.mTintList != null || this.mTintMode != IconCompat.DEFAULT_TINT_MODE)) {
            loadDrawableInner.mutate();
            DrawableCompat.setTintList(loadDrawableInner, this.mTintList);
            DrawableCompat.setTintMode(loadDrawableInner, this.mTintMode);
        }
        return loadDrawableInner;
    }
    
    @Override
    public void onPostParceling() {
        this.mTintMode = PorterDuff$Mode.valueOf(this.mTintModeStr);
        final int mType = this.mType;
        if (mType != -1) {
            switch (mType) {
                case 3: {
                    this.mObj1 = this.mData;
                    break;
                }
                case 2:
                case 4: {
                    this.mObj1 = new String(this.mData, Charset.forName("UTF-16"));
                    break;
                }
                case 1:
                case 5: {
                    if (this.mParcelable != null) {
                        this.mObj1 = this.mParcelable;
                        break;
                    }
                    this.mObj1 = this.mData;
                    this.mType = 3;
                    this.mInt1 = 0;
                    this.mInt2 = this.mData.length;
                    break;
                }
            }
        }
        else {
            if (this.mParcelable == null) {
                throw new IllegalArgumentException("Invalid icon");
            }
            this.mObj1 = this.mParcelable;
        }
    }
    
    @Override
    public void onPreParceling(final boolean b) {
        this.mTintModeStr = this.mTintMode.name();
        final int mType = this.mType;
        if (mType != -1) {
            switch (mType) {
                case 4: {
                    this.mData = this.mObj1.toString().getBytes(Charset.forName("UTF-16"));
                    break;
                }
                case 3: {
                    this.mData = (byte[])this.mObj1;
                    break;
                }
                case 2: {
                    this.mData = ((String)this.mObj1).getBytes(Charset.forName("UTF-16"));
                    break;
                }
                case 1:
                case 5: {
                    if (b) {
                        final Bitmap bitmap = (Bitmap)this.mObj1;
                        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap$CompressFormat.PNG, 90, (OutputStream)byteArrayOutputStream);
                        this.mData = byteArrayOutputStream.toByteArray();
                        break;
                    }
                    this.mParcelable = (Parcelable)this.mObj1;
                    break;
                }
            }
        }
        else {
            if (b) {
                throw new IllegalArgumentException("Can't serialize Icon created with IconCompat#createFromIcon");
            }
            this.mParcelable = (Parcelable)this.mObj1;
        }
    }
    
    public IconCompat setTint(@ColorInt final int n) {
        return this.setTintList(ColorStateList.valueOf(n));
    }
    
    public IconCompat setTintList(final ColorStateList mTintList) {
        this.mTintList = mTintList;
        return this;
    }
    
    public IconCompat setTintMode(final PorterDuff$Mode mTintMode) {
        this.mTintMode = mTintMode;
        return this;
    }
    
    public Bundle toBundle() {
        final Bundle bundle = new Bundle();
        final int mType = this.mType;
        if (mType != -1) {
            switch (mType) {
                default: {
                    throw new IllegalArgumentException("Invalid icon");
                }
                case 3: {
                    bundle.putByteArray("obj", (byte[])this.mObj1);
                    break;
                }
                case 2:
                case 4: {
                    bundle.putString("obj", (String)this.mObj1);
                    break;
                }
                case 1:
                case 5: {
                    bundle.putParcelable("obj", (Parcelable)this.mObj1);
                    break;
                }
            }
        }
        else {
            bundle.putParcelable("obj", (Parcelable)this.mObj1);
        }
        bundle.putInt("type", this.mType);
        bundle.putInt("int1", this.mInt1);
        bundle.putInt("int2", this.mInt2);
        if (this.mTintList != null) {
            bundle.putParcelable("tint_list", (Parcelable)this.mTintList);
        }
        if (this.mTintMode != IconCompat.DEFAULT_TINT_MODE) {
            bundle.putString("tint_mode", this.mTintMode.name());
        }
        return bundle;
    }
    
    @RequiresApi(23)
    public Icon toIcon() {
        final int mType = this.mType;
        if (mType != -1) {
            Icon icon = null;
            switch (mType) {
                default: {
                    throw new IllegalArgumentException("Unknown type");
                }
                case 5: {
                    if (Build$VERSION.SDK_INT >= 26) {
                        icon = Icon.createWithAdaptiveBitmap((Bitmap)this.mObj1);
                        break;
                    }
                    icon = Icon.createWithBitmap(createLegacyIconFromAdaptiveIcon((Bitmap)this.mObj1, false));
                    break;
                }
                case 4: {
                    icon = Icon.createWithContentUri((String)this.mObj1);
                    break;
                }
                case 3: {
                    icon = Icon.createWithData((byte[])this.mObj1, this.mInt1, this.mInt2);
                    break;
                }
                case 2: {
                    icon = Icon.createWithResource(this.getResPackage(), this.mInt1);
                    break;
                }
                case 1: {
                    icon = Icon.createWithBitmap((Bitmap)this.mObj1);
                    break;
                }
            }
            if (this.mTintList != null) {
                icon.setTintList(this.mTintList);
            }
            if (this.mTintMode != IconCompat.DEFAULT_TINT_MODE) {
                icon.setTintMode(this.mTintMode);
            }
            return icon;
        }
        return (Icon)this.mObj1;
    }
    
    @Override
    public String toString() {
        if (this.mType == -1) {
            return String.valueOf(this.mObj1);
        }
        final StringBuilder sb = new StringBuilder("Icon(typ=");
        sb.append(typeToString(this.mType));
        switch (this.mType) {
            case 4: {
                sb.append(" uri=");
                sb.append(this.mObj1);
                break;
            }
            case 3: {
                sb.append(" len=");
                sb.append(this.mInt1);
                if (this.mInt2 != 0) {
                    sb.append(" off=");
                    sb.append(this.mInt2);
                    break;
                }
                break;
            }
            case 2: {
                sb.append(" pkg=");
                sb.append(this.getResPackage());
                sb.append(" id=");
                sb.append(String.format("0x%08x", this.getResId()));
                break;
            }
            case 1:
            case 5: {
                sb.append(" size=");
                sb.append(((Bitmap)this.mObj1).getWidth());
                sb.append("x");
                sb.append(((Bitmap)this.mObj1).getHeight());
                break;
            }
        }
        if (this.mTintList != null) {
            sb.append(" tint=");
            sb.append(this.mTintList);
        }
        if (this.mTintMode != IconCompat.DEFAULT_TINT_MODE) {
            sb.append(" mode=");
            sb.append(this.mTintMode);
        }
        sb.append(")");
        return sb.toString();
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY })
    public @interface IconType {
    }
}
