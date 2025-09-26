// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.provider;

import android.database.Cursor;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import android.support.v4.util.Preconditions;
import android.support.annotation.IntRange;
import android.provider.BaseColumns;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.TypefaceCompatUtil;
import java.util.HashMap;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;
import android.content.pm.PackageManager;
import java.util.concurrent.Callable;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.annotation.VisibleForTesting;
import android.content.ContentResolver;
import android.net.Uri;
import android.content.ContentUris;
import android.os.Build$VERSION;
import android.net.Uri$Builder;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.content.res.Resources;
import android.content.pm.PackageManager$NameNotFoundException;
import android.content.pm.ProviderInfo;
import java.util.Arrays;
import java.util.List;
import android.content.pm.Signature;
import android.support.v4.graphics.TypefaceCompat;
import android.support.annotation.Nullable;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.support.annotation.GuardedBy;
import java.util.ArrayList;
import android.support.v4.util.SimpleArrayMap;
import java.util.Comparator;
import android.support.annotation.RestrictTo;

public class FontsContractCompat
{
    private static final int BACKGROUND_THREAD_KEEP_ALIVE_DURATION_MS = 10000;
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static final String PARCEL_FONT_RESULTS = "font_results";
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    static final int RESULT_CODE_PROVIDER_NOT_FOUND = -1;
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    static final int RESULT_CODE_WRONG_CERTIFICATES = -2;
    private static final String TAG = "FontsContractCompat";
    private static final SelfDestructiveThread sBackgroundThread;
    private static final Comparator<byte[]> sByteArrayComparator;
    static final Object sLock;
    @GuardedBy("sLock")
    static final SimpleArrayMap<String, ArrayList<SelfDestructiveThread.ReplyCallback<TypefaceResult>>> sPendingReplies;
    static final LruCache<String, Typeface> sTypefaceCache;
    
    static {
        sTypefaceCache = new LruCache<String, Typeface>(16);
        sBackgroundThread = new SelfDestructiveThread("fonts", 10, 10000);
        sLock = new Object();
        sPendingReplies = new SimpleArrayMap<String, ArrayList<SelfDestructiveThread.ReplyCallback<TypefaceResult>>>();
        sByteArrayComparator = new Comparator<byte[]>() {
            @Override
            public int compare(final byte[] array, final byte[] array2) {
                if (array.length != array2.length) {
                    return array.length - array2.length;
                }
                for (int i = 0; i < array.length; ++i) {
                    if (array[i] != array2[i]) {
                        return array[i] - array2[i];
                    }
                }
                return 0;
            }
        };
    }
    
    private FontsContractCompat() {
    }
    
    @Nullable
    public static Typeface buildTypeface(@NonNull final Context context, @Nullable final CancellationSignal cancellationSignal, @NonNull final FontInfo[] array) {
        return TypefaceCompat.createFromFontInfo(context, cancellationSignal, array, 0);
    }
    
    private static List<byte[]> convertToByteArrayList(final Signature[] array) {
        final ArrayList list = new ArrayList();
        for (int i = 0; i < array.length; ++i) {
            list.add(array[i].toByteArray());
        }
        return list;
    }
    
    private static boolean equalsByteArrayList(final List<byte[]> list, final List<byte[]> list2) {
        if (list.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list.size(); ++i) {
            if (!Arrays.equals((byte[])list.get(i), (byte[])list2.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    @NonNull
    public static FontFamilyResult fetchFonts(@NonNull final Context context, @Nullable final CancellationSignal cancellationSignal, @NonNull final FontRequest fontRequest) throws PackageManager$NameNotFoundException {
        final ProviderInfo provider = getProvider(context.getPackageManager(), fontRequest, context.getResources());
        if (provider == null) {
            return new FontFamilyResult(1, null);
        }
        return new FontFamilyResult(0, getFontFromProvider(context, fontRequest, provider.authority, cancellationSignal));
    }
    
    private static List<List<byte[]>> getCertificates(final FontRequest fontRequest, final Resources resources) {
        if (fontRequest.getCertificates() != null) {
            return fontRequest.getCertificates();
        }
        return FontResourcesParserCompat.readCerts(resources, fontRequest.getCertificatesArrayResId());
    }
    
    @NonNull
    @VisibleForTesting
    static FontInfo[] getFontFromProvider(final Context context, final FontRequest fontRequest, String s, final CancellationSignal cancellationSignal) {
        final ArrayList list = new ArrayList();
        final Uri build = new Uri$Builder().scheme("content").authority(s).build();
        final Uri build2 = new Uri$Builder().scheme("content").authority(s).appendPath("file").build();
        final String s2 = s = null;
        try {
            Object o;
            if (Build$VERSION.SDK_INT > 16) {
                s = s2;
                final ContentResolver contentResolver = context.getContentResolver();
                s = s2;
                final String query = fontRequest.getQuery();
                s = s2;
                o = contentResolver.query(build, new String[] { "_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code" }, "query = ?", new String[] { query }, (String)null, cancellationSignal);
            }
            else {
                s = s2;
                final ContentResolver contentResolver2 = context.getContentResolver();
                s = s2;
                final String query2 = fontRequest.getQuery();
                s = s2;
                o = contentResolver2.query(build, new String[] { "_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code" }, "query = ?", new String[] { query2 }, (String)null);
            }
            ArrayList<FontInfo> list2 = list;
            if (o != null) {
                list2 = list;
                s = (String)o;
                if (((Cursor)o).getCount() > 0) {
                    s = (String)o;
                    final int columnIndex = ((Cursor)o).getColumnIndex("result_code");
                    s = (String)o;
                    s = (String)o;
                    final ArrayList<FontInfo> list3 = new ArrayList<FontInfo>();
                    s = (String)o;
                    final int columnIndex2 = ((Cursor)o).getColumnIndex("_id");
                    s = (String)o;
                    final int columnIndex3 = ((Cursor)o).getColumnIndex("file_id");
                    s = (String)o;
                    final int columnIndex4 = ((Cursor)o).getColumnIndex("font_ttc_index");
                    s = (String)o;
                    final int columnIndex5 = ((Cursor)o).getColumnIndex("font_weight");
                    s = (String)o;
                    final int columnIndex6 = ((Cursor)o).getColumnIndex("font_italic");
                    while (true) {
                        s = (String)o;
                        if (!((Cursor)o).moveToNext()) {
                            break;
                        }
                        int int1;
                        if (columnIndex != -1) {
                            s = (String)o;
                            int1 = ((Cursor)o).getInt(columnIndex);
                        }
                        else {
                            int1 = 0;
                        }
                        int int2;
                        if (columnIndex4 != -1) {
                            s = (String)o;
                            int2 = ((Cursor)o).getInt(columnIndex4);
                        }
                        else {
                            int2 = 0;
                        }
                        Uri uri;
                        if (columnIndex3 == -1) {
                            s = (String)o;
                            uri = ContentUris.withAppendedId(build, ((Cursor)o).getLong(columnIndex2));
                        }
                        else {
                            s = (String)o;
                            uri = ContentUris.withAppendedId(build2, ((Cursor)o).getLong(columnIndex3));
                        }
                        int int3;
                        if (columnIndex5 != -1) {
                            s = (String)o;
                            int3 = ((Cursor)o).getInt(columnIndex5);
                        }
                        else {
                            int3 = 400;
                        }
                        boolean b = false;
                        Label_0509: {
                            if (columnIndex6 != -1) {
                                s = (String)o;
                                if (((Cursor)o).getInt(columnIndex6) == 1) {
                                    b = true;
                                    break Label_0509;
                                }
                            }
                            b = false;
                        }
                        s = (String)o;
                        s = (String)o;
                        final FontInfo e = new FontInfo(uri, int2, int3, b, int1);
                        s = (String)o;
                        list3.add(e);
                    }
                    list2 = list3;
                }
            }
            if (o != null) {
                ((Cursor)o).close();
            }
            return list2.toArray(new FontInfo[0]);
        }
        finally {
            if (s != null) {
                ((Cursor)s).close();
            }
        }
    }
    
    @NonNull
    static TypefaceResult getFontInternal(final Context context, final FontRequest fontRequest, final int n) {
        try {
            final FontFamilyResult fetchFonts = fetchFonts(context, null, fontRequest);
            final int statusCode = fetchFonts.getStatusCode();
            int n2 = -3;
            if (statusCode == 0) {
                final Typeface fromFontInfo = TypefaceCompat.createFromFontInfo(context, null, fetchFonts.getFonts(), n);
                if (fromFontInfo != null) {
                    n2 = 0;
                }
                return new TypefaceResult(fromFontInfo, n2);
            }
            if (fetchFonts.getStatusCode() == 1) {
                n2 = -2;
            }
            return new TypefaceResult(null, n2);
        }
        catch (final PackageManager$NameNotFoundException ex) {
            return new TypefaceResult(null, -1);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static Typeface getFontSync(final Context context, final FontRequest fontRequest, @Nullable final ResourcesCompat.FontCallback fontCallback, @Nullable final Handler handler, final boolean b, final int n, final int i) {
        final StringBuilder sb = new StringBuilder();
        sb.append(fontRequest.getIdentifier());
        sb.append("-");
        sb.append(i);
        final String string = sb.toString();
        final Typeface typeface = FontsContractCompat.sTypefaceCache.get(string);
        if (typeface != null) {
            if (fontCallback != null) {
                fontCallback.onFontRetrieved(typeface);
            }
            return typeface;
        }
        if (b && n == -1) {
            final TypefaceResult fontInternal = getFontInternal(context, fontRequest, i);
            if (fontCallback != null) {
                if (fontInternal.mResult == 0) {
                    fontCallback.callbackSuccessAsync(fontInternal.mTypeface, handler);
                }
                else {
                    fontCallback.callbackFailAsync(fontInternal.mResult, handler);
                }
            }
            return fontInternal.mTypeface;
        }
        final Callable<TypefaceResult> callable = new Callable<TypefaceResult>(context, fontRequest, i, string) {
            final Context val$context;
            final String val$id;
            final FontRequest val$request;
            final int val$style;
            
            @Override
            public TypefaceResult call() throws Exception {
                final TypefaceResult fontInternal = FontsContractCompat.getFontInternal(this.val$context, this.val$request, this.val$style);
                if (fontInternal.mTypeface != null) {
                    FontsContractCompat.sTypefaceCache.put(this.val$id, fontInternal.mTypeface);
                }
                return fontInternal;
            }
        };
        if (b) {
            try {
                return FontsContractCompat.sBackgroundThread.postAndWait((Callable<TypefaceResult>)callable, n).mTypeface;
            }
            catch (final InterruptedException ex) {
                return null;
            }
        }
        SelfDestructiveThread.ReplyCallback<TypefaceResult> replyCallback;
        if (fontCallback == null) {
            replyCallback = null;
        }
        else {
            replyCallback = new SelfDestructiveThread.ReplyCallback<TypefaceResult>(fontCallback, handler) {
                final ResourcesCompat.FontCallback val$fontCallback;
                final Handler val$handler;
                
                public void onReply(final TypefaceResult typefaceResult) {
                    if (typefaceResult == null) {
                        this.val$fontCallback.callbackFailAsync(1, this.val$handler);
                    }
                    else if (typefaceResult.mResult == 0) {
                        this.val$fontCallback.callbackSuccessAsync(typefaceResult.mTypeface, this.val$handler);
                    }
                    else {
                        this.val$fontCallback.callbackFailAsync(typefaceResult.mResult, this.val$handler);
                    }
                }
            };
        }
        synchronized (FontsContractCompat.sLock) {
            if (FontsContractCompat.sPendingReplies.containsKey(string)) {
                if (replyCallback != null) {
                    FontsContractCompat.sPendingReplies.get(string).add((Object)replyCallback);
                }
                return null;
            }
            if (replyCallback != null) {
                final ArrayList<SelfDestructiveThread.ReplyCallback<TypefaceResult>> list = new ArrayList<SelfDestructiveThread.ReplyCallback<TypefaceResult>>();
                list.add(replyCallback);
                FontsContractCompat.sPendingReplies.put(string, list);
            }
            monitorexit(FontsContractCompat.sLock);
            FontsContractCompat.sBackgroundThread.postAndReply((Callable<T>)callable, (SelfDestructiveThread.ReplyCallback<T>)new SelfDestructiveThread.ReplyCallback<TypefaceResult>(string) {
                final String val$id;
                
                public void onReply(final TypefaceResult typefaceResult) {
                    synchronized (FontsContractCompat.sLock) {
                        final ArrayList list = FontsContractCompat.sPendingReplies.get(this.val$id);
                        if (list == null) {
                            return;
                        }
                        FontsContractCompat.sPendingReplies.remove(this.val$id);
                        monitorexit(FontsContractCompat.sLock);
                        for (int i = 0; i < list.size(); ++i) {
                            ((SelfDestructiveThread.ReplyCallback<TypefaceResult>)list.get(i)).onReply(typefaceResult);
                        }
                    }
                }
            });
            return null;
        }
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    @VisibleForTesting
    public static ProviderInfo getProvider(@NonNull final PackageManager packageManager, @NonNull final FontRequest fontRequest, @Nullable final Resources resources) throws PackageManager$NameNotFoundException {
        final String providerAuthority = fontRequest.getProviderAuthority();
        int i = 0;
        final ProviderInfo resolveContentProvider = packageManager.resolveContentProvider(providerAuthority, 0);
        if (resolveContentProvider == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("No package found for authority: ");
            sb.append(providerAuthority);
            throw new PackageManager$NameNotFoundException(sb.toString());
        }
        if (!resolveContentProvider.packageName.equals(fontRequest.getProviderPackage())) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Found content provider ");
            sb2.append(providerAuthority);
            sb2.append(", but package was not ");
            sb2.append(fontRequest.getProviderPackage());
            throw new PackageManager$NameNotFoundException(sb2.toString());
        }
        final List<byte[]> convertToByteArrayList = convertToByteArrayList(packageManager.getPackageInfo(resolveContentProvider.packageName, 64).signatures);
        Collections.sort((List<Object>)convertToByteArrayList, (Comparator<? super Object>)FontsContractCompat.sByteArrayComparator);
        for (List<List<byte[]>> certificates = getCertificates(fontRequest, resources); i < certificates.size(); ++i) {
            final ArrayList list = new ArrayList<byte[]>((Collection<? extends T>)certificates.get(i));
            Collections.sort((List<E>)list, (Comparator<? super E>)FontsContractCompat.sByteArrayComparator);
            if (equalsByteArrayList(convertToByteArrayList, (List<byte[]>)list)) {
                return resolveContentProvider;
            }
        }
        return null;
    }
    
    @RequiresApi(19)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static Map<Uri, ByteBuffer> prepareFontData(final Context context, final FontInfo[] array, final CancellationSignal cancellationSignal) {
        final HashMap m = new HashMap();
        for (final FontInfo fontInfo : array) {
            if (fontInfo.getResultCode() == 0) {
                final Uri uri = fontInfo.getUri();
                if (!m.containsKey(uri)) {
                    m.put(uri, TypefaceCompatUtil.mmap(context, cancellationSignal, uri));
                }
            }
        }
        return (Map<Uri, ByteBuffer>)Collections.unmodifiableMap((Map<?, ?>)m);
    }
    
    public static void requestFont(@NonNull final Context context, @NonNull final FontRequest fontRequest, @NonNull final FontRequestCallback fontRequestCallback, @NonNull final Handler handler) {
        handler.post((Runnable)new Runnable(context, fontRequest, new Handler(), fontRequestCallback) {
            final FontRequestCallback val$callback;
            final Handler val$callerThreadHandler;
            final Context val$context;
            final FontRequest val$request;
            
            @Override
            public void run() {
                try {
                    final FontFamilyResult fetchFonts = FontsContractCompat.fetchFonts(this.val$context, null, this.val$request);
                    if (fetchFonts.getStatusCode() != 0) {
                        switch (fetchFonts.getStatusCode()) {
                            default: {
                                this.val$callerThreadHandler.post((Runnable)new Runnable(this) {
                                    final FontsContractCompat$4 this$0;
                                    
                                    @Override
                                    public void run() {
                                        this.this$0.val$callback.onTypefaceRequestFailed(-3);
                                    }
                                });
                                return;
                            }
                            case 2: {
                                this.val$callerThreadHandler.post((Runnable)new Runnable(this) {
                                    final FontsContractCompat$4 this$0;
                                    
                                    @Override
                                    public void run() {
                                        this.this$0.val$callback.onTypefaceRequestFailed(-3);
                                    }
                                });
                                return;
                            }
                            case 1: {
                                this.val$callerThreadHandler.post((Runnable)new Runnable(this) {
                                    final FontsContractCompat$4 this$0;
                                    
                                    @Override
                                    public void run() {
                                        this.this$0.val$callback.onTypefaceRequestFailed(-2);
                                    }
                                });
                            }
                        }
                    }
                    else {
                        final FontInfo[] fonts = fetchFonts.getFonts();
                        if (fonts == null || fonts.length == 0) {
                            this.val$callerThreadHandler.post((Runnable)new Runnable(this) {
                                final FontsContractCompat$4 this$0;
                                
                                @Override
                                public void run() {
                                    this.this$0.val$callback.onTypefaceRequestFailed(1);
                                }
                            });
                            return;
                        }
                        for (final FontInfo fontInfo : fonts) {
                            if (fontInfo.getResultCode() != 0) {
                                final int resultCode = fontInfo.getResultCode();
                                if (resultCode < 0) {
                                    this.val$callerThreadHandler.post((Runnable)new Runnable(this) {
                                        final FontsContractCompat$4 this$0;
                                        
                                        @Override
                                        public void run() {
                                            this.this$0.val$callback.onTypefaceRequestFailed(-3);
                                        }
                                    });
                                }
                                else {
                                    this.val$callerThreadHandler.post((Runnable)new Runnable(this, resultCode) {
                                        final FontsContractCompat$4 this$0;
                                        final int val$resultCode;
                                        
                                        @Override
                                        public void run() {
                                            this.this$0.val$callback.onTypefaceRequestFailed(this.val$resultCode);
                                        }
                                    });
                                }
                                return;
                            }
                        }
                        final Typeface buildTypeface = FontsContractCompat.buildTypeface(this.val$context, null, fonts);
                        if (buildTypeface == null) {
                            this.val$callerThreadHandler.post((Runnable)new Runnable(this) {
                                final FontsContractCompat$4 this$0;
                                
                                @Override
                                public void run() {
                                    this.this$0.val$callback.onTypefaceRequestFailed(-3);
                                }
                            });
                            return;
                        }
                        this.val$callerThreadHandler.post((Runnable)new Runnable(this, buildTypeface) {
                            final FontsContractCompat$4 this$0;
                            final Typeface val$typeface;
                            
                            @Override
                            public void run() {
                                this.this$0.val$callback.onTypefaceRetrieved(this.val$typeface);
                            }
                        });
                    }
                }
                catch (final PackageManager$NameNotFoundException ex) {
                    this.val$callerThreadHandler.post((Runnable)new Runnable(this) {
                        final FontsContractCompat$4 this$0;
                        
                        @Override
                        public void run() {
                            this.this$0.val$callback.onTypefaceRequestFailed(-1);
                        }
                    });
                }
            }
        });
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public static void resetCache() {
        FontsContractCompat.sTypefaceCache.evictAll();
    }
    
    public static final class Columns implements BaseColumns
    {
        public static final String FILE_ID = "file_id";
        public static final String ITALIC = "font_italic";
        public static final String RESULT_CODE = "result_code";
        public static final int RESULT_CODE_FONT_NOT_FOUND = 1;
        public static final int RESULT_CODE_FONT_UNAVAILABLE = 2;
        public static final int RESULT_CODE_MALFORMED_QUERY = 3;
        public static final int RESULT_CODE_OK = 0;
        public static final String TTC_INDEX = "font_ttc_index";
        public static final String VARIATION_SETTINGS = "font_variation_settings";
        public static final String WEIGHT = "font_weight";
    }
    
    public static class FontFamilyResult
    {
        public static final int STATUS_OK = 0;
        public static final int STATUS_UNEXPECTED_DATA_PROVIDED = 2;
        public static final int STATUS_WRONG_CERTIFICATES = 1;
        private final FontInfo[] mFonts;
        private final int mStatusCode;
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public FontFamilyResult(final int mStatusCode, @Nullable final FontInfo[] mFonts) {
            this.mStatusCode = mStatusCode;
            this.mFonts = mFonts;
        }
        
        public FontInfo[] getFonts() {
            return this.mFonts;
        }
        
        public int getStatusCode() {
            return this.mStatusCode;
        }
    }
    
    public static class FontInfo
    {
        private final boolean mItalic;
        private final int mResultCode;
        private final int mTtcIndex;
        private final Uri mUri;
        private final int mWeight;
        
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public FontInfo(@NonNull final Uri uri, @IntRange(from = 0L) final int mTtcIndex, @IntRange(from = 1L, to = 1000L) final int mWeight, final boolean mItalic, final int mResultCode) {
            this.mUri = Preconditions.checkNotNull(uri);
            this.mTtcIndex = mTtcIndex;
            this.mWeight = mWeight;
            this.mItalic = mItalic;
            this.mResultCode = mResultCode;
        }
        
        public int getResultCode() {
            return this.mResultCode;
        }
        
        @IntRange(from = 0L)
        public int getTtcIndex() {
            return this.mTtcIndex;
        }
        
        @NonNull
        public Uri getUri() {
            return this.mUri;
        }
        
        @IntRange(from = 1L, to = 1000L)
        public int getWeight() {
            return this.mWeight;
        }
        
        public boolean isItalic() {
            return this.mItalic;
        }
    }
    
    public static class FontRequestCallback
    {
        public static final int FAIL_REASON_FONT_LOAD_ERROR = -3;
        public static final int FAIL_REASON_FONT_NOT_FOUND = 1;
        public static final int FAIL_REASON_FONT_UNAVAILABLE = 2;
        public static final int FAIL_REASON_MALFORMED_QUERY = 3;
        public static final int FAIL_REASON_PROVIDER_NOT_FOUND = -1;
        public static final int FAIL_REASON_SECURITY_VIOLATION = -4;
        public static final int FAIL_REASON_WRONG_CERTIFICATES = -2;
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public static final int RESULT_OK = 0;
        
        public void onTypefaceRequestFailed(final int n) {
        }
        
        public void onTypefaceRetrieved(final Typeface typeface) {
        }
        
        @Retention(RetentionPolicy.SOURCE)
        @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
        public @interface FontRequestFailReason {
        }
    }
    
    private static final class TypefaceResult
    {
        final int mResult;
        final Typeface mTypeface;
        
        TypefaceResult(@Nullable final Typeface mTypeface, final int mResult) {
            this.mTypeface = mTypeface;
            this.mResult = mResult;
        }
    }
}
