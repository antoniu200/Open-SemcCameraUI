// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.provider;

import android.provider.DocumentsContract;
import android.os.Build$VERSION;
import android.net.Uri;
import android.content.Context;
import android.support.annotation.NonNull;
import java.io.File;
import android.support.annotation.Nullable;

public abstract class DocumentFile
{
    static final String TAG = "DocumentFile";
    @Nullable
    private final DocumentFile mParent;
    
    DocumentFile(@Nullable final DocumentFile mParent) {
        this.mParent = mParent;
    }
    
    @NonNull
    public static DocumentFile fromFile(@NonNull final File file) {
        return new RawDocumentFile(null, file);
    }
    
    @Nullable
    public static DocumentFile fromSingleUri(@NonNull final Context context, @NonNull final Uri uri) {
        if (Build$VERSION.SDK_INT >= 19) {
            return new SingleDocumentFile(null, context, uri);
        }
        return null;
    }
    
    @Nullable
    public static DocumentFile fromTreeUri(@NonNull final Context context, @NonNull final Uri uri) {
        if (Build$VERSION.SDK_INT >= 21) {
            return new TreeDocumentFile(null, context, DocumentsContract.buildDocumentUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri)));
        }
        return null;
    }
    
    public static boolean isDocumentUri(@NonNull final Context context, @Nullable final Uri uri) {
        return Build$VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context, uri);
    }
    
    public abstract boolean canRead();
    
    public abstract boolean canWrite();
    
    @Nullable
    public abstract DocumentFile createDirectory(@NonNull final String p0);
    
    @Nullable
    public abstract DocumentFile createFile(@NonNull final String p0, @NonNull final String p1);
    
    public abstract boolean delete();
    
    public abstract boolean exists();
    
    @Nullable
    public DocumentFile findFile(@NonNull final String s) {
        for (final DocumentFile documentFile : this.listFiles()) {
            if (s.equals(documentFile.getName())) {
                return documentFile;
            }
        }
        return null;
    }
    
    @Nullable
    public abstract String getName();
    
    @Nullable
    public DocumentFile getParentFile() {
        return this.mParent;
    }
    
    @Nullable
    public abstract String getType();
    
    @NonNull
    public abstract Uri getUri();
    
    public abstract boolean isDirectory();
    
    public abstract boolean isFile();
    
    public abstract boolean isVirtual();
    
    public abstract long lastModified();
    
    public abstract long length();
    
    @NonNull
    public abstract DocumentFile[] listFiles();
    
    public abstract boolean renameTo(@NonNull final String p0);
}
