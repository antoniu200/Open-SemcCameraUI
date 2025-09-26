// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.storage;

import android.os.StatFs;
import java.util.concurrent.Callable;
import android.os.storage.DiskInfo;
import java.lang.reflect.InvocationTargetException;
import android.os.storage.StorageVolume;
import android.os.storage.StorageManager;
import com.sonyericsson.cameracommon.mediasaving.StorageManagerProxy;
import com.sonyericsson.android.camera.setting.SharedPreferencesAccessor;
import android.content.ContentResolver;
import android.os.Environment;
import java.util.Iterator;
import android.os.storage.VolumeInfo;
import android.support.annotation.NonNull;
import android.database.Cursor;
import android.webkit.MimeTypeMap;
import android.text.TextUtils;
import java.io.File;
import java.io.FileNotFoundException;
import android.provider.DocumentsContract;
import com.sonyericsson.android.camera.util.CamLog;
import android.net.Uri;
import android.content.Context;
import java.util.ArrayList;
import android.os.UserHandle;
import java.lang.reflect.Method;
import java.util.List;

public class StorageUtil
{
    private static final String DUMMY_FILE_MIME_TYPE = "text/plane";
    private static final String DUMMY_FILE_NAME = "sdcard_write_test";
    private static final List<Storage.StorageType> MOUNTABLE_STORAGE_TYPES;
    public static final String TAG = "StorageUtil";
    private static Method mMethodMyUserId;
    
    static {
        try {
            StorageUtil.mMethodMyUserId = UserHandle.class.getMethod("myUserId", (Class<?>[])new Class[0]);
            (MOUNTABLE_STORAGE_TYPES = new ArrayList<Storage.StorageType>()).add(Storage.StorageType.EXTERNAL_CARD);
            StorageUtil.MOUNTABLE_STORAGE_TYPES.add(Storage.StorageType.INTERNAL);
        }
        catch (final NoSuchMethodException cause) {
            throw new RuntimeException(cause);
        }
    }
    
    static CameraStorageManager.GrantCheckResult checkSdCardGranted(final Context context, Uri uri) {
        if (CamLog.DEBUG) {
            CamLog.d("isSdCardGranted()");
        }
        final CameraStorageManager.GrantCheckResult ungranted = CameraStorageManager.GrantCheckResult.UNGRANTED;
        if (uri == null) {
            CamLog.w("checkUri is null");
            return ungranted;
        }
        uri = DocumentsContract.buildDocumentUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri));
        Enum<CameraStorageManager.GrantCheckResult> obj;
        try {
            uri = DocumentsContract.createDocument(context.getContentResolver(), uri, "text/plane", "sdcard_write_test");
            if (uri != null) {
                if (DocumentsContract.deleteDocument(context.getContentResolver(), uri)) {
                    if (CamLog.DEBUG) {
                        CamLog.d("SD Card is granted.");
                    }
                    obj = CameraStorageManager.GrantCheckResult.GRANTED;
                }
                else {
                    CamLog.e("SD Card is no granted for delete error.");
                    obj = ungranted;
                }
            }
            else {
                obj = ungranted;
                if (CamLog.DEBUG) {
                    CamLog.d("SD Card is no granted for createDocument failed.");
                    obj = ungranted;
                }
            }
        }
        catch (final IllegalStateException ex) {
            CamLog.w("file create failed", ex);
            obj = CameraStorageManager.GrantCheckResult.READ_ONLY;
        }
        catch (final FileNotFoundException | SecurityException ex2) {
            CamLog.w("file create failed", (Throwable)ex2);
            obj = ungranted;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("isSDCardGranted() result :");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        return (CameraStorageManager.GrantCheckResult)obj;
    }
    
    private static Uri child(final Uri uri, final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append(DocumentsContract.getTreeDocumentId(uri));
        sb.append("/");
        sb.append(str);
        return DocumentsContract.buildDocumentUriUsingTree(uri, sb.toString());
    }
    
    public static Uri createDirectory(final Context context, final Uri uri, final String pathname) {
        synchronized (StorageUtil.class) {
            final Uri child = child(uri, pathname);
            if (exists(context, child)) {
                return child;
            }
            final Uri buildDocumentUriUsingTree = DocumentsContract.buildDocumentUriUsingTree(child, DocumentsContract.getTreeDocumentId(child));
            final File file = new File(pathname);
            final String parent = file.getParent();
            Uri directory = buildDocumentUriUsingTree;
            if (parent != null) {
                directory = buildDocumentUriUsingTree;
                if (!parent.isEmpty()) {
                    directory = createDirectory(context, uri, parent);
                }
            }
            final String name = file.getName();
            Uri document;
            try {
                document = DocumentsContract.createDocument(context.getContentResolver(), directory, "vnd.android.document/directory", name);
            }
            catch (final FileNotFoundException | SecurityException ex) {
                CamLog.w("createDirectory() failed", (Throwable)ex);
                document = null;
            }
            return document;
        }
    }
    
    public static Uri createDocumentSdCard(final Context context, final String s) {
        final Uri sdCardGrantedUri = getSdCardGrantedUri(context);
        Uri file;
        if (sdCardGrantedUri != null) {
            file = createFile(context, sdCardGrantedUri, getPathAfterDcim(sdCardGrantedUri, s));
        }
        else {
            file = null;
        }
        return file;
    }
    
    public static Uri createFile(final Context context, final Uri uri, final String pathname) {
        synchronized (StorageUtil.class) {
            final Uri child = child(uri, pathname);
            if (exists(context, child)) {
                return child;
            }
            final Uri buildDocumentUriUsingTree = DocumentsContract.buildDocumentUriUsingTree(child, DocumentsContract.getTreeDocumentId(child));
            final String parent = new File(pathname).getParent();
            Uri directory = buildDocumentUriUsingTree;
            if (!TextUtils.isEmpty((CharSequence)parent)) {
                directory = createDirectory(context, uri, parent);
            }
            final String ext = getExt(child);
            final String documentName = getDocumentName(child);
            if (!TextUtils.isEmpty((CharSequence)ext) && !TextUtils.isEmpty((CharSequence)documentName)) {
                final String mimeTypeFromExtension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
                try {
                    return DocumentsContract.createDocument(context.getContentResolver(), directory, mimeTypeFromExtension, documentName);
                }
                catch (final FileNotFoundException | SecurityException ex) {
                    CamLog.w("createFile() failed", (Throwable)ex);
                }
            }
            return null;
        }
    }
    
    public static Uri existFile(final Context context, Uri child, final String s) {
        child = child(child, s);
        if (exists(context, child)) {
            return child;
        }
        return null;
    }
    
    public static boolean exists(Context query, final Uri ex) {
        final Cursor cursor = null;
        Object o = null;
        Label_0104: {
            Cursor cursor2;
            try {
                try {
                    query = (Context)query.getContentResolver().query((Uri)ex, (String[])null, (String)null, (String[])null, (String)null);
                    if (query != null) {
                        try {
                            final boolean moveToNext = ((Cursor)query).moveToNext();
                            ((Cursor)query).close();
                            if (moveToNext) {
                                if (query != null) {
                                    ((Cursor)query).close();
                                }
                                return true;
                            }
                        }
                        catch (final RuntimeException ex) {}
                        finally {
                            o = query;
                            query = (Context)ex;
                            break Label_0104;
                        }
                    }
                    if (query != null) {
                        ((Cursor)query).close();
                        return false;
                    }
                    return false;
                }
                finally {}
            }
            catch (final RuntimeException ex) {
                cursor2 = cursor;
            }
            CamLog.w("exists not found", ex);
            if (cursor2 != null) {
                cursor2.close();
            }
            return false;
        }
        if (o != null) {
            ((Cursor)o).close();
        }
    }
    
    private static String getDocumentName(final Uri uri) {
        final File file = new File(DocumentsContract.getDocumentId(uri));
        if (file != null) {
            return file.getName();
        }
        return null;
    }
    
    private static String getExt(final Uri uri) {
        final String documentName = getDocumentName(uri);
        if (!TextUtils.isEmpty((CharSequence)documentName)) {
            final int lastIndex = documentName.lastIndexOf(46);
            if (lastIndex >= 0) {
                return documentName.substring(lastIndex + 1);
            }
        }
        return "";
    }
    
    @NonNull
    public static List<Storage.StorageType> getMountableStorageTypes() {
        return StorageUtil.MOUNTABLE_STORAGE_TYPES;
    }
    
    public static String[] getMountedPaths(final Context context) {
        final ArrayList list = new ArrayList();
        final Iterator<VolumeInfo> iterator = getStorageManager(context).getVolumes().iterator();
        while (iterator.hasNext()) {
            final String volumePath = getVolumePath(iterator.next());
            if (volumePath != null) {
                list.add(volumePath);
                if (!CamLog.VERBOSE) {
                    continue;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("mount point: ");
                sb.append(volumePath);
                CamLog.d(sb.toString());
            }
        }
        return (String[])list.toArray(new String[0]);
    }
    
    public static String getPathAfterDcim(final Uri uri, final String s) {
        final String string = uri.toString();
        String substring;
        if (!TextUtils.isEmpty((CharSequence)string)) {
            if (string.contains(Environment.DIRECTORY_DCIM)) {
                final StringBuilder sb = new StringBuilder();
                sb.append(Environment.DIRECTORY_DCIM);
                sb.append("/");
                substring = s.split(sb.toString(), 0)[1];
            }
            else {
                substring = s.substring(s.indexOf(Environment.DIRECTORY_DCIM));
            }
        }
        else {
            substring = null;
        }
        return substring;
    }
    
    public static String getPathFromType(final Storage.StorageType storageType, final Context context) {
        for (final VolumeInfo volumeInfo : getStorageManager(context).getVolumes()) {
            if (storageType.equals(getVolumeType(volumeInfo))) {
                return getVolumePath(volumeInfo);
            }
        }
        return null;
    }
    
    static String getPathFromUri(final Context context, final Uri uri) {
        if (uri == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("uri: null");
            }
            return null;
        }
        final String path = uri.getPath();
        if (path == null) {
            if (CamLog.VERBOSE) {
                CamLog.d("getPath(): null");
            }
            return null;
        }
        final Iterator<VolumeInfo> iterator = getStorageManager(context).getVolumes().iterator();
        while (iterator.hasNext()) {
            final String volumePath = getVolumePath(iterator.next());
            if (volumePath != null && path.contains(volumePath)) {
                if (CamLog.VERBOSE) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("valid file: ");
                    sb.append(volumePath);
                    CamLog.d(sb.toString());
                }
                return volumePath;
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme()) && "media".equalsIgnoreCase(uri.getAuthority())) {
            final String pathFromUriByMediaDb = getPathFromUriByMediaDb(context, uri);
            if (pathFromUriByMediaDb != null) {
                return pathFromUriByMediaDb;
            }
        }
        final String pathFromType = getPathFromType(Storage.StorageType.INTERNAL, context);
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("extracted path: ");
            sb2.append(pathFromType);
            CamLog.d(sb2.toString());
        }
        return pathFromType;
    }
    
    private static String getPathFromUriByMediaDb(final Context context, Uri query) {
        final ContentResolver contentResolver = context.getContentResolver();
        try {
            query = (Uri)contentResolver.query(query, new String[] { "_data" }, (String)null, (String[])null, (String)null);
            if (query == null) {
                goto Label_0279;
            }
            try {
                if (!((Cursor)query).moveToFirst()) {
                    goto Label_0279;
                }
                final int columnIndexOrThrow = ((Cursor)query).getColumnIndexOrThrow("_data");
                if (((Cursor)query).getType(columnIndexOrThrow) != 3) {
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("no path in content: ");
                        sb.append(((Cursor)query).getType(0));
                        CamLog.d(sb.toString());
                    }
                    if (query != null) {
                        ((Cursor)query).close();
                    }
                    return null;
                }
                final String string = ((Cursor)query).getString(columnIndexOrThrow);
                final Iterator<VolumeInfo> iterator = getStorageManager(context).getVolumes().iterator();
                while (iterator.hasNext()) {
                    final String volumePath = getVolumePath(iterator.next());
                    if (volumePath != null && string.contains(volumePath)) {
                        if (CamLog.VERBOSE) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("valid content: ");
                            sb2.append(volumePath);
                            CamLog.d(sb2.toString());
                        }
                        if (query != null) {
                            ((Cursor)query).close();
                        }
                        return volumePath;
                    }
                }
                if (CamLog.VERBOSE) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("invalid content path: ");
                    sb3.append(string);
                    CamLog.d(sb3.toString());
                    goto Label_0279;
                }
                goto Label_0279;
            }
            catch (final Exception ex) {}
        }
        catch (final Exception ex2) {}
    }
    
    public static Uri getSdCardGrantedUri(final Context context) {
        final SharedPreferencesAccessor sharedPreferencesAccessor = new SharedPreferencesAccessor(context, "com.sonyericsson.android.camera.shared_preferences");
        Uri parse = null;
        final String string = sharedPreferencesAccessor.readString("KEY_SD_CARD_GRANT_URI", null);
        if (string != null) {
            parse = Uri.parse(string);
        }
        return parse;
    }
    
    private static StorageManagerProxy getStorageManager(final Context context) {
        return StorageManagerProxy.createProxy((StorageManager)context.getSystemService("storage"));
    }
    
    public static long getStorageMaximumFileSize(final Context context, final Storage.StorageType storageType) {
        long maxFileSize = 0L;
        Label_0084: {
            if (storageType == Storage.StorageType.EXTERNAL_CARD) {
                final String volumeUuid = getVolumeUuid(storageType, context);
                final Iterator iterator = ((StorageManager)context.getSystemService("storage")).getStorageVolumes().iterator();
                while (iterator.hasNext()) {
                    final StorageVolumeWrapper storageVolumeWrapper = new StorageVolumeWrapper((StorageVolume)iterator.next());
                    if (volumeUuid.equals(storageVolumeWrapper.getUuid())) {
                        maxFileSize = storageVolumeWrapper.getMaxFileSize();
                        break Label_0084;
                    }
                }
            }
            maxFileSize = Long.MAX_VALUE;
        }
        long lng = maxFileSize;
        if (maxFileSize == 0L) {
            lng = Long.MAX_VALUE;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getStorageMaximumFileSize() maxFileSize: ");
            sb.append(lng);
            CamLog.d(sb.toString());
        }
        return lng;
    }
    
    public static Storage.StorageType getStorageTypeFromPath(final String str, final Context context) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getStorageTypeFromPath: ");
            sb.append(str);
            CamLog.d(sb.toString());
        }
        if (str == null) {
            return Storage.StorageType.UNKNOWN;
        }
        final Storage.StorageType unknown = Storage.StorageType.UNKNOWN;
        final Iterator<VolumeInfo> iterator = getStorageManager(context).getVolumes().iterator();
        Storage.StorageType volumeType = unknown;
        while (iterator.hasNext()) {
            final VolumeInfo volumeInfo = iterator.next();
            final String volumePath = getVolumePath(volumeInfo);
            if (volumePath != null && str.startsWith(volumePath)) {
                volumeType = getVolumeType(volumeInfo);
            }
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("getStorageTypeFromPath: ");
            sb2.append(volumeType);
            CamLog.d(sb2.toString());
        }
        return volumeType;
    }
    
    public static Storage.StorageType getStorageTypeFromUri(final Uri obj, final Context context) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getStorageTypeFromUri uri: ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        final Storage.StorageType storageTypeFromPath = getStorageTypeFromPath(getPathFromUri(context, obj), context);
        if (CamLog.VERBOSE) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("getStorageTypeFromUri type: ");
            sb2.append(storageTypeFromPath);
            CamLog.d(sb2.toString());
        }
        return storageTypeFromPath;
    }
    
    private static String getVolumePath(final VolumeInfo volumeInfo) {
        File pathForUser;
        try {
            pathForUser = volumeInfo.getPathForUser((int)StorageUtil.mMethodMyUserId.invoke(null, new Object[0]));
        }
        catch (final IllegalAccessException | InvocationTargetException ex) {
            pathForUser = null;
        }
        File path = pathForUser;
        if (pathForUser == null) {
            path = volumeInfo.getPath();
        }
        if (path == null) {
            return null;
        }
        return path.getPath();
    }
    
    public static String getVolumeState(final Storage.StorageType obj, final Context context) {
        final String s = "removed";
        final Iterator<VolumeInfo> iterator = getStorageManager(context).getVolumes().iterator();
        while (true) {
            VolumeInfo volumeInfo;
            do {
                final String environmentForState = s;
                if (!iterator.hasNext()) {
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("getVolumeState type: ");
                        sb.append(obj);
                        sb.append(" = ");
                        sb.append(environmentForState);
                        CamLog.d(sb.toString());
                    }
                    return environmentForState;
                }
                volumeInfo = iterator.next();
            } while (getVolumeType(volumeInfo) != obj);
            final String environmentForState = VolumeInfo.getEnvironmentForState(volumeInfo.getState());
            continue;
        }
    }
    
    private static Storage.StorageType getVolumeType(final VolumeInfo volumeInfo) {
        final int type = volumeInfo.getType();
        if (type == 2) {
            return Storage.StorageType.INTERNAL;
        }
        if (type == 0) {
            final DiskInfo disk = volumeInfo.getDisk();
            int flags;
            if (disk != null) {
                flags = disk.flags;
            }
            else {
                flags = 0;
            }
            if ((flags & 0x4) != 0x0) {
                return Storage.StorageType.EXTERNAL_CARD;
            }
            if ((flags & 0x8) != 0x0) {
                return Storage.StorageType.USB;
            }
        }
        return Storage.StorageType.UNKNOWN;
    }
    
    public static String getVolumeUuid(final Storage.StorageType storageType, final Context context) {
        while (true) {
            for (final VolumeInfo volumeInfo : getStorageManager(context).getVolumes()) {
                if (getVolumeType(volumeInfo) == storageType) {
                    final String fsUuid = volumeInfo.getFsUuid();
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("getVolumeUuid : ");
                        sb.append(fsUuid);
                        CamLog.d(sb.toString());
                    }
                    return fsUuid;
                }
            }
            final String fsUuid = null;
            continue;
        }
    }
    
    public static boolean isExistDcimDirectory(final Uri uri) {
        final String string = uri.toString();
        return !TextUtils.isEmpty((CharSequence)string) && string.contains(Environment.DIRECTORY_DCIM);
    }
    
    public static boolean isExistRemovableStorage(final Context context) {
        for (final File file : context.getExternalFilesDirs((String)null)) {
            if (file != null && Environment.isExternalStorageRemovable(file)) {
                return true;
            }
        }
        return false;
    }
    
    public static void preload() {
    }
    
    public static Uri searchDocumentSdCard(final Context context, final String s) {
        final Uri sdCardGrantedUri = getSdCardGrantedUri(context);
        Uri existFile;
        if (sdCardGrantedUri != null) {
            existFile = existFile(context, sdCardGrantedUri, getPathAfterDcim(sdCardGrantedUri, s));
        }
        else {
            existFile = null;
        }
        return existFile;
    }
    
    public static void setSdCardGranted(final Context context, final Uri uri) {
        if (uri != null) {
            new SharedPreferencesAccessor(context, "com.sonyericsson.android.camera.shared_preferences").writeString("KEY_SD_CARD_GRANT_URI", uri.toString(), true);
        }
    }
    
    public static class GetStatFsTask implements Callable<StatFs>
    {
        private final String mPath;
        
        public GetStatFsTask(final String mPath) {
            if (mPath == null) {
                throw new IllegalArgumentException("Target path is null.");
            }
            this.mPath = mPath;
        }
        
        @Override
        public StatFs call() {
            StatFs statFs;
            try {
                statFs = new StatFs(this.mPath);
            }
            catch (final IllegalArgumentException ex) {
                CamLog.e("Create StatFs failed.", ex);
                statFs = null;
            }
            return statFs;
        }
    }
}
