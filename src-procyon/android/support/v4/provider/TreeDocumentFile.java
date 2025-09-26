// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.provider;

import android.database.Cursor;
import android.content.ContentResolver;
import android.util.Log;
import java.util.ArrayList;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.net.Uri;
import android.content.Context;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class TreeDocumentFile extends DocumentFile
{
    private Context mContext;
    private Uri mUri;
    
    TreeDocumentFile(@Nullable final DocumentFile documentFile, final Context mContext, final Uri mUri) {
        super(documentFile);
        this.mContext = mContext;
        this.mUri = mUri;
    }
    
    private static void closeQuietly(@Nullable final AutoCloseable autoCloseable) {
        if (autoCloseable == null) {
            goto Label_0016;
        }
        try {
            autoCloseable.close();
            goto Label_0016;
        }
        catch (final RuntimeException ex) {
            throw ex;
        }
        catch (final Exception ex2) {
            goto Label_0016;
        }
    }
    
    @Nullable
    private static Uri createFile(final Context context, final Uri uri, final String s, final String s2) {
        try {
            return DocumentsContract.createDocument(context.getContentResolver(), uri, s, s2);
        }
        catch (final Exception ex) {
            return null;
        }
    }
    
    @Override
    public boolean canRead() {
        return DocumentsContractApi19.canRead(this.mContext, this.mUri);
    }
    
    @Override
    public boolean canWrite() {
        return DocumentsContractApi19.canWrite(this.mContext, this.mUri);
    }
    
    @Nullable
    @Override
    public DocumentFile createDirectory(final String s) {
        final Uri file = createFile(this.mContext, this.mUri, "vnd.android.document/directory", s);
        TreeDocumentFile treeDocumentFile;
        if (file != null) {
            treeDocumentFile = new TreeDocumentFile(this, this.mContext, file);
        }
        else {
            treeDocumentFile = null;
        }
        return treeDocumentFile;
    }
    
    @Nullable
    @Override
    public DocumentFile createFile(final String s, final String s2) {
        final Uri file = createFile(this.mContext, this.mUri, s, s2);
        TreeDocumentFile treeDocumentFile;
        if (file != null) {
            treeDocumentFile = new TreeDocumentFile(this, this.mContext, file);
        }
        else {
            treeDocumentFile = null;
        }
        return treeDocumentFile;
    }
    
    @Override
    public boolean delete() {
        try {
            return DocumentsContract.deleteDocument(this.mContext.getContentResolver(), this.mUri);
        }
        catch (final Exception ex) {
            return false;
        }
    }
    
    @Override
    public boolean exists() {
        return DocumentsContractApi19.exists(this.mContext, this.mUri);
    }
    
    @Nullable
    @Override
    public String getName() {
        return DocumentsContractApi19.getName(this.mContext, this.mUri);
    }
    
    @Nullable
    @Override
    public String getType() {
        return DocumentsContractApi19.getType(this.mContext, this.mUri);
    }
    
    @Override
    public Uri getUri() {
        return this.mUri;
    }
    
    @Override
    public boolean isDirectory() {
        return DocumentsContractApi19.isDirectory(this.mContext, this.mUri);
    }
    
    @Override
    public boolean isFile() {
        return DocumentsContractApi19.isFile(this.mContext, this.mUri);
    }
    
    @Override
    public boolean isVirtual() {
        return DocumentsContractApi19.isVirtual(this.mContext, this.mUri);
    }
    
    @Override
    public long lastModified() {
        return DocumentsContractApi19.lastModified(this.mContext, this.mUri);
    }
    
    @Override
    public long length() {
        return DocumentsContractApi19.length(this.mContext, this.mUri);
    }
    
    @Override
    public DocumentFile[] listFiles() {
        final ContentResolver contentResolver = this.mContext.getContentResolver();
        final Uri buildChildDocumentsUriUsingTree = DocumentsContract.buildChildDocumentsUriUsingTree(this.mUri, DocumentsContract.getDocumentId(this.mUri));
        final ArrayList list = new ArrayList();
        int i = 0;
        final AutoCloseable autoCloseable = null;
        Object string = null;
        Object o;
        final Exception obj;
        try {
            try {
                final Cursor query = contentResolver.query(buildChildDocumentsUriUsingTree, new String[] { "document_id" }, (String)null, (String[])null, (String)null);
                try {
                    while (query.moveToNext()) {
                        string = query.getString(0);
                        list.add(DocumentsContract.buildDocumentUriUsingTree(this.mUri, (String)string));
                    }
                    closeQuietly((AutoCloseable)query);
                }
                catch (final Exception ex) {}
            }
            finally {
                o = string;
                final Exception ex2 = obj;
            }
        }
        catch (final Exception obj) {
            o = autoCloseable;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Failed query: ");
        sb.append(obj);
        Log.w("DocumentFile", sb.toString());
        closeQuietly((AutoCloseable)o);
        final Uri[] array = list.toArray(new Uri[list.size()]);
        final DocumentFile[] array2 = new DocumentFile[array.length];
        while (i < array.length) {
            array2[i] = new TreeDocumentFile(this, this.mContext, array[i]);
            ++i;
        }
        return array2;
        closeQuietly((AutoCloseable)o);
        throw null;
    }
    
    @Override
    public boolean renameTo(final String s) {
        try {
            final Uri renameDocument = DocumentsContract.renameDocument(this.mContext.getContentResolver(), this.mUri, s);
            if (renameDocument != null) {
                this.mUri = renameDocument;
                return true;
            }
            return false;
        }
        catch (final Exception ex) {
            return false;
        }
    }
}
