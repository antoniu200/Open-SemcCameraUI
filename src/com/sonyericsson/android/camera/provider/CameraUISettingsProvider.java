package com.sonyericsson.android.camera.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.SparseArray;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.modeselector.CameraCommonProviderConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CameraUISettingsProvider extends ContentProvider {
    private static final String AUTHORITY = "com.sonymobile.camerauicommon.provider";
    private static final String CAMERA_UI_AUTHORITY = "com.sonymobile.camerauicommon.provider";
    private static final String DATABASE_NAME = "cameraui.db";
    private static final int DATABASE_VERSION = 5;
    private static final String DIR_TYPE_BASE = "vnd.android.cursor.dir/";
    private static final String ITEM_TYPE_BASE = "vnd.android.cursor.item/";
    private static final String TAG = "CameraUISettingsProvider";
    private SQLiteOpenHelper mOpenHelper;
    int mProcessingBatchCount = 0;
    private static final SparseArray<String> MIMETYPE_LIST = new SparseArray<>();
    private static final UriMatcher URI_MATCHER = new UriMatcher(-1);

    interface CapturingMode {
        public static final Uri CONTENT_URI = CameraCommonProviderConstants.CAPTURINGMODE_CONTENT_URI;
        public static final String MIME_TYPE = "capturingmode";
        public static final String NAME = "capturingmodes";
        public static final String PATH = "capturingmodes";
    }

    interface Path {
        public static final int CAPTURINGMODE_DIR = 1;
        public static final int CAPTURINGMODE_ITEM = 2;
    }

    protected String getDataBaseName() {
        return DATABASE_NAME;
    }

    static {
        URI_MATCHER.addURI(CameraCommonProviderConstants.AUTHORITY, "capturingmodes", 1);
        URI_MATCHER.addURI(CameraCommonProviderConstants.AUTHORITY, "capturingmodes/#", 2);
        MIMETYPE_LIST.put(1, "vnd.android.cursor.dir/capturingmode");
        MIMETYPE_LIST.put(2, "vnd.android.cursor.item/capturingmode");
    }

    void incrementProcessingBatchCount() {
        synchronized (this) {
            this.mProcessingBatchCount++;
        }
    }

    void decrementProcessingBatchCount() {
        synchronized (this) {
            this.mProcessingBatchCount--;
        }
    }

    private boolean isProcessingBatch() {
        boolean z;
        synchronized (this) {
            z = this.mProcessingBatchCount > 0;
        }
        return z;
    }

    static class MyOpenHelper extends SQLiteOpenHelper {
        private final Context mContext;

        public MyOpenHelper(Context context, String str) {
            super(context, str, (SQLiteDatabase.CursorFactory) null, 5);
            this.mContext = context;
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onCreate(SQLiteDatabase sQLiteDatabase) throws SQLException {
            if (existTable(sQLiteDatabase, "capturingmodes")) {
                return;
            }
            createCapturingModeTable(sQLiteDatabase);
        }

        @Override // android.database.sqlite.SQLiteOpenHelper
        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) throws SQLException {
            if (CamLog.VERBOSE) {
                CamLog.d("onUpgrade()");
                CamLog.d("  oldVersion:" + i);
                CamLog.d("  newVersion:" + i2);
            }
            deleteTables(sQLiteDatabase);
            createCapturingModeTable(sQLiteDatabase);
        }

        static void createCapturingModeTable(SQLiteDatabase sQLiteDatabase) throws SQLException {
            sQLiteDatabase.execSQL("CREATE TABLE capturingmodes (_id INTEGER PRIMARY KEY AUTOINCREMENT,package TEXT, activity TEXT, mode_name TEXT, capture_type INTEGER, visibility_normal INTEGER, visibility_oneshot INTEGER, visibility_shortcut INTEGER, sort_order INTEGER, selectorlabel_id INTEGER, selectoricon_id INTEGER, shortcutlabel_id INTEGER, shortcuticon_id INTEGER, descriptionlabel_id INTEGER, UNIQUE(package,mode_name));");
        }

        static void deleteTables(SQLiteDatabase sQLiteDatabase) throws SQLException {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS capturingmodes");
        }

        static boolean existTable(SQLiteDatabase sQLiteDatabase, String str) throws Throwable {
            Cursor cursor = null;
            try {
                Cursor cursorRawQuery = sQLiteDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + str + "'", null);
                if (cursorRawQuery != null) {
                    try {
                        if (cursorRawQuery.getCount() > 0) {
                            if (cursorRawQuery != null) {
                                cursorRawQuery.close();
                            }
                            return true;
                        }
                    } catch (Throwable th) {
                        th = th;
                        cursor = cursorRawQuery;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th;
                    }
                }
                if (cursorRawQuery == null) {
                    return false;
                }
                cursorRawQuery.close();
                return false;
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        this.mOpenHelper = new MyOpenHelper(getContext(), getDataBaseName());
        return true;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        if (CamLog.VERBOSE) {
            in(uri);
        }
        Cursor cursorQuery = this.mOpenHelper.getReadableDatabase().query(getTableName(uri), strArr, str, strArr2, null, null, str2);
        if (CamLog.VERBOSE) {
            out();
        }
        return cursorQuery;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (CamLog.VERBOSE) {
            in(uri);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("insert uri:" + uri + " values:" + contentValues);
        }
        Uri uriWithAppendedPath = null;
        long jInsert = this.mOpenHelper.getWritableDatabase().insert(getTableName(uri), null, contentValues);
        if (jInsert != -1) {
            uriWithAppendedPath = Uri.withAppendedPath(uri, String.valueOf(jInsert));
            if (!isProcessingBatch()) {
                onCompleteOperation(uri);
            }
        }
        if (CamLog.VERBOSE) {
            out();
        }
        return uriWithAppendedPath;
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        if (CamLog.VERBOSE) {
            in(uri);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("delete uri:" + uri.toString() + " selection:" + str + " arguments:" + Arrays.toString(strArr));
        }
        int iDelete = this.mOpenHelper.getWritableDatabase().delete(getTableName(uri), str, strArr);
        if (iDelete > 0 && !isProcessingBatch()) {
            onCompleteOperation(uri);
        }
        if (CamLog.VERBOSE) {
            out();
        }
        return iDelete;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] args) {
        if (CamLog.VERBOSE) {
            in(uri);
        }

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final String table = getTableName(uri);
        int rows = 0;

        if (where != null) {
            try {
                rows = db.update(table, values, where, args);
                if (rows > 0 && !isProcessingBatch()) {
                    onCompleteOperation(uri);
                }
            } catch (android.database.sqlite.SQLiteConstraintException e) {
                CamLog.e(new String[] { "Failed to update the record. Message : " + e.getMessage() });
            }
        } else {
            // Upsert behavior when no where-clause is provided
            long id = db.replace(table, null, values);
            if (id != -1) {
                if (!isProcessingBatch()) {
                    onCompleteOperation(uri);
                }
                rows = 1;
            } else {
                rows = 0;
            }
        }

        if (CamLog.VERBOSE) {
            out();
        }
        return rows;
    }

    @Override // android.content.ContentProvider
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> arrayList) throws OperationApplicationException {
        if (CamLog.VERBOSE) {
            in(null);
        }
        incrementProcessingBatchCount();
        ContentProviderResult[] contentProviderResultArrApplyBatch = super.applyBatch(arrayList);
        decrementProcessingBatchCount();
        HashSet hashSet = new HashSet();
        Iterator<ContentProviderOperation> it = arrayList.iterator();
        while (it.hasNext()) {
            ContentProviderOperation next = it.next();
            if (next.getUri() != null) {
                hashSet.add(next.getUri());
            }
        }
        Iterator it2 = hashSet.iterator();
        while (it2.hasNext()) {
            onCompleteOperation((Uri) it2.next());
        }
        if (CamLog.VERBOSE) {
            out();
        }
        return contentProviderResultArrApplyBatch;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return MIMETYPE_LIST.get(URI_MATCHER.match(uri));
    }

    private String getTableName(Uri uri) {
        if (URI_MATCHER.match(uri) == 1) {
            return "capturingmodes";
        }
        throw new SQLException("Invalid uri for this content provider. uri:" + uri);
    }

    private void onCompleteOperation(Uri uri) {
        boolean z;
        if (CamLog.VERBOSE) {
            in(uri);
        }
        if (uri == null) {
            if (z) {
                return;
            } else {
                return;
            }
        }
        try {
            if (CamLog.VERBOSE) {
                debug("notify changes uri:" + uri);
            }
            getContext().getContentResolver().notifyChange(uri, null);
            if (CamLog.VERBOSE) {
                out();
            }
        } finally {
            if (CamLog.VERBOSE) {
                out();
            }
        }
    }

    private int getCountOf(String str) {
        Cursor cursorQuery = this.mOpenHelper.getReadableDatabase().query(str, new String[0], null, null, null, null, null);
        if (cursorQuery == null) {
            return 0;
        }
        int count = cursorQuery.getCount();
        cursorQuery.close();
        return count;
    }

    private void in(Uri uri) {
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        long id = Thread.currentThread().getId();
        if (CamLog.DEBUG) {
            CamLog.d("### [" + id + "]" + methodName + "() --> start #" + uri);
        }
    }

    private void out() {
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        long id = Thread.currentThread().getId();
        if (CamLog.DEBUG) {
            CamLog.d("### [" + id + "]" + methodName + "() --> end");
        }
    }

    private void debug(String str) {
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        long id = Thread.currentThread().getId();
        if (CamLog.DEBUG) {
            CamLog.d("### [" + id + "]" + methodName + "() " + str);
        }
    }
}
