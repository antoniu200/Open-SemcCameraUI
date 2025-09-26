// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.content;

import java.util.Iterator;
import android.net.Uri$Builder;
import java.util.Map;
import android.text.TextUtils;
import android.database.MatrixCursor;
import android.database.Cursor;
import java.io.FileNotFoundException;
import android.os.ParcelFileDescriptor;
import android.content.ContentValues;
import android.webkit.MimeTypeMap;
import android.support.annotation.Nullable;
import android.content.pm.ProviderInfo;
import android.content.res.XmlResourceParser;
import android.os.Build$VERSION;
import android.os.Environment;
import android.net.Uri;
import android.support.annotation.NonNull;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import android.content.Context;
import android.support.annotation.GuardedBy;
import java.util.HashMap;
import java.io.File;
import android.content.ContentProvider;

public class FileProvider extends ContentProvider
{
    private static final String ATTR_NAME = "name";
    private static final String ATTR_PATH = "path";
    private static final String[] COLUMNS;
    private static final File DEVICE_ROOT;
    private static final String META_DATA_FILE_PROVIDER_PATHS = "android.support.FILE_PROVIDER_PATHS";
    private static final String TAG_CACHE_PATH = "cache-path";
    private static final String TAG_EXTERNAL = "external-path";
    private static final String TAG_EXTERNAL_CACHE = "external-cache-path";
    private static final String TAG_EXTERNAL_FILES = "external-files-path";
    private static final String TAG_EXTERNAL_MEDIA = "external-media-path";
    private static final String TAG_FILES_PATH = "files-path";
    private static final String TAG_ROOT_PATH = "root-path";
    @GuardedBy("sCache")
    private static HashMap<String, PathStrategy> sCache;
    private PathStrategy mStrategy;
    
    static {
        COLUMNS = new String[] { "_display_name", "_size" };
        DEVICE_ROOT = new File("/");
        FileProvider.sCache = new HashMap<String, PathStrategy>();
    }
    
    private static File buildPath(File parent, final String... array) {
        File file;
        for (int length = array.length, i = 0; i < length; ++i, parent = file) {
            final String child = array[i];
            file = parent;
            if (child != null) {
                file = new File(parent, child);
            }
        }
        return parent;
    }
    
    private static Object[] copyOf(final Object[] array, final int n) {
        final Object[] array2 = new Object[n];
        System.arraycopy(array, 0, array2, 0, n);
        return array2;
    }
    
    private static String[] copyOf(final String[] array, final int n) {
        final String[] array2 = new String[n];
        System.arraycopy(array, 0, array2, 0, n);
        return array2;
    }
    
    private static PathStrategy getPathStrategy(final Context context, final String s) {
        synchronized (FileProvider.sCache) {
            PathStrategy pathStrategy;
            if ((pathStrategy = FileProvider.sCache.get(s)) == null) {
                try {
                    pathStrategy = parsePathStrategy(context, s);
                    FileProvider.sCache.put(s, pathStrategy);
                }
                catch (final XmlPullParserException cause) {
                    throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", (Throwable)cause);
                }
                catch (final IOException cause2) {
                    throw new IllegalArgumentException("Failed to parse android.support.FILE_PROVIDER_PATHS meta-data", cause2);
                }
            }
            return pathStrategy;
        }
    }
    
    public static Uri getUriForFile(@NonNull final Context context, @NonNull final String s, @NonNull final File file) {
        return getPathStrategy(context, s).getUriForFile(file);
    }
    
    private static int modeToMode(final String str) {
        int n;
        if ("r".equals(str)) {
            n = 268435456;
        }
        else if (!"w".equals(str) && !"wt".equals(str)) {
            if ("wa".equals(str)) {
                n = 704643072;
            }
            else if ("rw".equals(str)) {
                n = 939524096;
            }
            else {
                if (!"rwt".equals(str)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Invalid mode: ");
                    sb.append(str);
                    throw new IllegalArgumentException(sb.toString());
                }
                n = 1006632960;
            }
        }
        else {
            n = 738197504;
        }
        return n;
    }
    
    private static PathStrategy parsePathStrategy(final Context context, final String s) throws IOException, XmlPullParserException {
        final SimplePathStrategy simplePathStrategy = new SimplePathStrategy(s);
        final XmlResourceParser loadXmlMetaData = context.getPackageManager().resolveContentProvider(s, 128).loadXmlMetaData(context.getPackageManager(), "android.support.FILE_PROVIDER_PATHS");
        if (loadXmlMetaData == null) {
            throw new IllegalArgumentException("Missing android.support.FILE_PROVIDER_PATHS meta-data");
        }
        while (true) {
            final int next = loadXmlMetaData.next();
            if (next == 1) {
                break;
            }
            if (next != 2) {
                continue;
            }
            final String name = loadXmlMetaData.getName();
            final File file = null;
            final String attributeValue = loadXmlMetaData.getAttributeValue((String)null, "name");
            final String attributeValue2 = loadXmlMetaData.getAttributeValue((String)null, "path");
            File file2;
            if ("root-path".equals(name)) {
                file2 = FileProvider.DEVICE_ROOT;
            }
            else if ("files-path".equals(name)) {
                file2 = context.getFilesDir();
            }
            else if ("cache-path".equals(name)) {
                file2 = context.getCacheDir();
            }
            else if ("external-path".equals(name)) {
                file2 = Environment.getExternalStorageDirectory();
            }
            else if ("external-files-path".equals(name)) {
                final File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(context, null);
                file2 = file;
                if (externalFilesDirs.length > 0) {
                    file2 = externalFilesDirs[0];
                }
            }
            else if ("external-cache-path".equals(name)) {
                final File[] externalCacheDirs = ContextCompat.getExternalCacheDirs(context);
                file2 = file;
                if (externalCacheDirs.length > 0) {
                    file2 = externalCacheDirs[0];
                }
            }
            else {
                file2 = file;
                if (Build$VERSION.SDK_INT >= 21) {
                    file2 = file;
                    if ("external-media-path".equals(name)) {
                        final File[] externalMediaDirs = context.getExternalMediaDirs();
                        file2 = file;
                        if (externalMediaDirs.length > 0) {
                            file2 = externalMediaDirs[0];
                        }
                    }
                }
            }
            if (file2 == null) {
                continue;
            }
            simplePathStrategy.addRoot(attributeValue, buildPath(file2, attributeValue2));
        }
        return (PathStrategy)simplePathStrategy;
    }
    
    public void attachInfo(@NonNull final Context context, @NonNull final ProviderInfo providerInfo) {
        super.attachInfo(context, providerInfo);
        if (providerInfo.exported) {
            throw new SecurityException("Provider must not be exported");
        }
        if (!providerInfo.grantUriPermissions) {
            throw new SecurityException("Provider must grant uri permissions");
        }
        this.mStrategy = getPathStrategy(context, providerInfo.authority);
    }
    
    public int delete(@NonNull final Uri uri, @Nullable final String s, @Nullable final String[] array) {
        return this.mStrategy.getFileForUri(uri).delete() ? 1 : 0;
    }
    
    public String getType(@NonNull final Uri uri) {
        final File fileForUri = this.mStrategy.getFileForUri(uri);
        final int lastIndex = fileForUri.getName().lastIndexOf(46);
        if (lastIndex >= 0) {
            final String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileForUri.getName().substring(lastIndex + 1));
            if (mimeTypeFromExtension != null) {
                return mimeTypeFromExtension;
            }
        }
        return "application/octet-stream";
    }
    
    public Uri insert(@NonNull final Uri uri, final ContentValues contentValues) {
        throw new UnsupportedOperationException("No external inserts");
    }
    
    public boolean onCreate() {
        return true;
    }
    
    public ParcelFileDescriptor openFile(@NonNull final Uri uri, @NonNull final String s) throws FileNotFoundException {
        return ParcelFileDescriptor.open(this.mStrategy.getFileForUri(uri), modeToMode(s));
    }
    
    public Cursor query(@NonNull final Uri uri, @Nullable final String[] array, @Nullable final String s, @Nullable String[] array2, @Nullable String s2) {
        final File fileForUri = this.mStrategy.getFileForUri(uri);
        String[] columns = array;
        if (array == null) {
            columns = FileProvider.COLUMNS;
        }
        array2 = new String[columns.length];
        final Object[] array3 = new Object[columns.length];
        final int length = columns.length;
        int i = 0;
        int n = 0;
        while (i < length) {
            s2 = columns[i];
            int n4 = 0;
            Label_0144: {
                int n3;
                if ("_display_name".equals(s2)) {
                    array2[n] = "_display_name";
                    final int n2 = n + 1;
                    array3[n] = fileForUri.getName();
                    n3 = n2;
                }
                else {
                    n4 = n;
                    if (!"_size".equals(s2)) {
                        break Label_0144;
                    }
                    array2[n] = "_size";
                    final int n5 = n + 1;
                    array3[n] = fileForUri.length();
                    n3 = n5;
                }
                n4 = n3;
            }
            ++i;
            n = n4;
        }
        final String[] copy = copyOf(array2, n);
        final Object[] copy2 = copyOf(array3, n);
        final MatrixCursor matrixCursor = new MatrixCursor(copy, 1);
        matrixCursor.addRow(copy2);
        return (Cursor)matrixCursor;
    }
    
    public int update(@NonNull final Uri uri, final ContentValues contentValues, @Nullable final String s, @Nullable final String[] array) {
        throw new UnsupportedOperationException("No external updates");
    }
    
    interface PathStrategy
    {
        File getFileForUri(final Uri p0);
        
        Uri getUriForFile(final File p0);
    }
    
    static class SimplePathStrategy implements PathStrategy
    {
        private final String mAuthority;
        private final HashMap<String, File> mRoots;
        
        SimplePathStrategy(final String mAuthority) {
            this.mRoots = new HashMap<String, File>();
            this.mAuthority = mAuthority;
        }
        
        void addRoot(final String key, final File obj) {
            if (TextUtils.isEmpty((CharSequence)key)) {
                throw new IllegalArgumentException("Name must not be empty");
            }
            try {
                this.mRoots.put(key, obj.getCanonicalFile());
            }
            catch (final IOException cause) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Failed to resolve canonical path for ");
                sb.append(obj);
                throw new IllegalArgumentException(sb.toString(), cause);
            }
        }
        
        @Override
        public File getFileForUri(Uri uri) {
            final String encodedPath = uri.getEncodedPath();
            final int index = encodedPath.indexOf(47, 1);
            final String decode = Uri.decode(encodedPath.substring(1, index));
            final String decode2 = Uri.decode(encodedPath.substring(index + 1));
            final File parent = this.mRoots.get(decode);
            if (parent == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to find configured root for ");
                sb.append(uri);
                throw new IllegalArgumentException(sb.toString());
            }
            uri = (Uri)new File(parent, decode2);
            try {
                final File canonicalFile = ((File)uri).getCanonicalFile();
                if (!canonicalFile.getPath().startsWith(parent.getPath())) {
                    throw new SecurityException("Resolved path jumped beyond configured root");
                }
                return canonicalFile;
            }
            catch (final IOException ex) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Failed to resolve canonical path for ");
                sb2.append(uri);
                throw new IllegalArgumentException(sb2.toString());
            }
        }
        
        @Override
        public Uri getUriForFile(File string) {
            try {
                final String canonicalPath = string.getCanonicalPath();
                string = null;
                for (final Map.Entry<?, File> entry : this.mRoots.entrySet()) {
                    final String path = entry.getValue().getPath();
                    if (canonicalPath.startsWith(path) && (string == null || path.length() > ((Map.Entry<K, File>)string).getValue().getPath().length())) {
                        string = (File)entry;
                    }
                }
                if (string == null) {
                    string = (File)new StringBuilder();
                    ((StringBuilder)string).append("Failed to find configured root that contains ");
                    ((StringBuilder)string).append(canonicalPath);
                    throw new IllegalArgumentException(((StringBuilder)string).toString());
                }
                final String path2 = ((Map.Entry<K, File>)string).getValue().getPath();
                String s;
                if (path2.endsWith("/")) {
                    s = canonicalPath.substring(path2.length());
                }
                else {
                    s = canonicalPath.substring(path2.length() + 1);
                }
                final StringBuilder sb = new StringBuilder();
                sb.append(Uri.encode((String)((Map.Entry<String, V>)string).getKey()));
                sb.append('/');
                sb.append(Uri.encode(s, "/"));
                string = (File)sb.toString();
                return new Uri$Builder().scheme("content").authority(this.mAuthority).encodedPath((String)string).build();
            }
            catch (final IOException ex) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Failed to resolve canonical path for ");
                sb2.append(string);
                throw new IllegalArgumentException(sb2.toString());
            }
        }
    }
}
