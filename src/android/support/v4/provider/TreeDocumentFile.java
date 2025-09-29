package android.support.v4.provider;

import android.content.Context;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import java.io.FileNotFoundException;

@RequiresApi(21)
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class TreeDocumentFile extends DocumentFile {
    private Context mContext;
    private Uri mUri;

    TreeDocumentFile(@Nullable DocumentFile documentFile, Context context, Uri uri) {
        super(documentFile);
        this.mContext = context;
        this.mUri = uri;
    }

    @Override // android.support.v4.provider.DocumentFile
    @Nullable
    public DocumentFile createFile(String str, String str2) {
        Uri uriCreateFile = createFile(this.mContext, this.mUri, str, str2);
        if (uriCreateFile != null) {
            return new TreeDocumentFile(this, this.mContext, uriCreateFile);
        }
        return null;
    }

    @Nullable
    private static Uri createFile(Context context, Uri uri, String str, String str2) {
        try {
            return DocumentsContract.createDocument(context.getContentResolver(), uri, str, str2);
        } catch (Exception unused) {
            return null;
        }
    }

    @Override // android.support.v4.provider.DocumentFile
    @Nullable
    public DocumentFile createDirectory(String str) {
        Uri uriCreateFile = createFile(this.mContext, this.mUri, "vnd.android.document/directory", str);
        if (uriCreateFile != null) {
            return new TreeDocumentFile(this, this.mContext, uriCreateFile);
        }
        return null;
    }

    @Override // android.support.v4.provider.DocumentFile
    public Uri getUri() {
        return this.mUri;
    }

    @Override // android.support.v4.provider.DocumentFile
    @Nullable
    public String getName() {
        return DocumentsContractApi19.getName(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    @Nullable
    public String getType() {
        return DocumentsContractApi19.getType(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    public boolean isDirectory() {
        return DocumentsContractApi19.isDirectory(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    public boolean isFile() {
        return DocumentsContractApi19.isFile(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    public boolean isVirtual() {
        return DocumentsContractApi19.isVirtual(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    public long lastModified() {
        return DocumentsContractApi19.lastModified(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    public long length() {
        return DocumentsContractApi19.length(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    public boolean canRead() {
        return DocumentsContractApi19.canRead(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    public boolean canWrite() {
        return DocumentsContractApi19.canWrite(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    public boolean delete() {
        try {
            return DocumentsContract.deleteDocument(this.mContext.getContentResolver(), this.mUri);
        } catch (Exception unused) {
            return false;
        }
    }

    @Override // android.support.v4.provider.DocumentFile
    public boolean exists() {
        return DocumentsContractApi19.exists(this.mContext, this.mUri);
    }

    @Override // android.support.v4.provider.DocumentFile
    public DocumentFile[] listFiles() {
        final ContentResolver resolver = mContext.getContentResolver();
        final Uri childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(mUri,
                DocumentsContract.getDocumentId(mUri));
        final ArrayList<Uri> results = new ArrayList<>();
        Cursor c = null;
        try {
            c = resolver.query(childrenUri, new String[] {
                    DocumentsContract.Document.COLUMN_DOCUMENT_ID }, null, null, null);
            while (c.moveToNext()) {
                final String documentId = c.getString(0);
                final Uri documentUri = DocumentsContract.buildDocumentUriUsingTree(mUri,
                        documentId);
                results.add(documentUri);
            }
        } catch (Exception e) {
            Log.w(TAG, "Failed query: " + e);
        } finally {
            closeQuietly(c);
        }
        final Uri[] result = results.toArray(new Uri[results.size()]);
        final DocumentFile[] resultFiles = new DocumentFile[result.length];
        for (int i = 0; i < result.length; i++) {
            resultFiles[i] = new TreeDocumentFile(this, mContext, result[i]);
        }
        return resultFiles;
    }

    private static void closeQuietly(@Nullable AutoCloseable autoCloseable) throws Exception {
        if (autoCloseable != null) {
            try {
                autoCloseable.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception unused) {
            }
        }
    }

    @Override // android.support.v4.provider.DocumentFile
    public boolean renameTo(String str) throws FileNotFoundException {
        try {
            Uri uriRenameDocument = DocumentsContract.renameDocument(this.mContext.getContentResolver(), this.mUri, str);
            if (uriRenameDocument == null) {
                return false;
            }
            this.mUri = uriRenameDocument;
            return true;
        } catch (Exception unused) {
            return false;
        }
    }
}
