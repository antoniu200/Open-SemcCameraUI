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

    /* JADX WARN: Removed duplicated region for block: B:21:0x0072 A[LOOP:1: B:19:0x006f->B:21:0x0072, LOOP_END] */
    @Override // android.support.v4.provider.DocumentFile
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public android.support.v4.provider.DocumentFile[] listFiles() throws java.lang.Exception {
        /*
            r9 = this;
            android.content.Context r0 = r9.mContext
            android.content.ContentResolver r1 = r0.getContentResolver()
            android.net.Uri r0 = r9.mUri
            android.net.Uri r2 = r9.mUri
            java.lang.String r2 = android.provider.DocumentsContract.getDocumentId(r2)
            android.net.Uri r2 = android.provider.DocumentsContract.buildChildDocumentsUriUsingTree(r0, r2)
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r7 = 0
            r8 = 0
            java.lang.String r3 = "document_id"
            java.lang.String[] r3 = new java.lang.String[]{r3}     // Catch: java.lang.Throwable -> L44 java.lang.Exception -> L46
            r4 = 0
            r5 = 0
            r6 = 0
            android.database.Cursor r1 = r1.query(r2, r3, r4, r5, r6)     // Catch: java.lang.Throwable -> L44 java.lang.Exception -> L46
        L26:
            boolean r2 = r1.moveToNext()     // Catch: java.lang.Throwable -> L3e java.lang.Exception -> L41
            if (r2 == 0) goto L3a
            java.lang.String r2 = r1.getString(r7)     // Catch: java.lang.Throwable -> L3e java.lang.Exception -> L41
            android.net.Uri r3 = r9.mUri     // Catch: java.lang.Throwable -> L3e java.lang.Exception -> L41
            android.net.Uri r2 = android.provider.DocumentsContract.buildDocumentUriUsingTree(r3, r2)     // Catch: java.lang.Throwable -> L3e java.lang.Exception -> L41
            r0.add(r2)     // Catch: java.lang.Throwable -> L3e java.lang.Exception -> L41
            goto L26
        L3a:
            closeQuietly(r1)
            goto L60
        L3e:
            r9 = move-exception
            r8 = r1
            goto L81
        L41:
            r2 = move-exception
            r8 = r1
            goto L47
        L44:
            r9 = move-exception
            goto L81
        L46:
            r2 = move-exception
        L47:
            java.lang.String r1 = "DocumentFile"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L44
            r3.<init>()     // Catch: java.lang.Throwable -> L44
            java.lang.String r4 = "Failed query: "
            r3.append(r4)     // Catch: java.lang.Throwable -> L44
            r3.append(r2)     // Catch: java.lang.Throwable -> L44
            java.lang.String r2 = r3.toString()     // Catch: java.lang.Throwable -> L44
            android.util.Log.w(r1, r2)     // Catch: java.lang.Throwable -> L44
            closeQuietly(r8)
        L60:
            int r1 = r0.size()
            android.net.Uri[] r1 = new android.net.Uri[r1]
            java.lang.Object[] r0 = r0.toArray(r1)
            android.net.Uri[] r0 = (android.net.Uri[]) r0
            int r1 = r0.length
            android.support.v4.provider.DocumentFile[] r1 = new android.support.v4.provider.DocumentFile[r1]
        L6f:
            int r2 = r0.length
            if (r7 >= r2) goto L80
            android.support.v4.provider.TreeDocumentFile r2 = new android.support.v4.provider.TreeDocumentFile
            android.content.Context r3 = r9.mContext
            r4 = r0[r7]
            r2.<init>(r9, r3, r4)
            r1[r7] = r2
            int r7 = r7 + 1
            goto L6f
        L80:
            return r1
        L81:
            closeQuietly(r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.provider.TreeDocumentFile.listFiles():android.support.v4.provider.DocumentFile[]");
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
