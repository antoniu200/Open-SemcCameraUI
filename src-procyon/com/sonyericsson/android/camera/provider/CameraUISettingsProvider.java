// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.provider;

import android.database.sqlite.SQLiteDatabase$CursorFactory;
import android.content.Context;
import com.sonyericsson.android.camera.view.modeselector.CameraCommonProviderConstants;
import android.database.sqlite.SQLiteConstraintException;
import android.content.ContentValues;
import java.util.Arrays;
import android.content.OperationApplicationException;
import java.util.Iterator;
import java.util.HashSet;
import android.content.ContentProviderResult;
import android.content.ContentProviderOperation;
import java.util.ArrayList;
import android.database.ContentObserver;
import android.database.SQLException;
import android.net.Uri;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.sonyericsson.android.camera.util.CamLog;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.UriMatcher;
import android.util.SparseArray;
import android.content.ContentProvider;

public class CameraUISettingsProvider extends ContentProvider
{
    private static final String AUTHORITY = "com.sonymobile.camerauicommon.provider";
    private static final String CAMERA_UI_AUTHORITY = "com.sonymobile.camerauicommon.provider";
    private static final String DATABASE_NAME = "cameraui.db";
    private static final int DATABASE_VERSION = 5;
    private static final String DIR_TYPE_BASE = "vnd.android.cursor.dir/";
    private static final String ITEM_TYPE_BASE = "vnd.android.cursor.item/";
    private static final SparseArray<String> MIMETYPE_LIST;
    private static final String TAG = "CameraUISettingsProvider";
    private static final UriMatcher URI_MATCHER;
    private SQLiteOpenHelper mOpenHelper;
    int mProcessingBatchCount;
    
    static {
        MIMETYPE_LIST = new SparseArray();
        (URI_MATCHER = new UriMatcher(-1)).addURI("com.sonymobile.camerauicommon.provider", "capturingmodes", 1);
        CameraUISettingsProvider.URI_MATCHER.addURI("com.sonymobile.camerauicommon.provider", "capturingmodes/#", 2);
        CameraUISettingsProvider.MIMETYPE_LIST.put(1, (Object)"vnd.android.cursor.dir/capturingmode");
        CameraUISettingsProvider.MIMETYPE_LIST.put(2, (Object)"vnd.android.cursor.item/capturingmode");
    }
    
    public CameraUISettingsProvider() {
        this.mProcessingBatchCount = 0;
    }
    
    private void debug(final String str) {
        final String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        final long id = Thread.currentThread().getId();
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("### [");
            sb.append(id);
            sb.append("]");
            sb.append(methodName);
            sb.append("() ");
            sb.append(str);
            CamLog.d(sb.toString());
        }
    }
    
    private int getCountOf(final String s) {
        final SQLiteDatabase readableDatabase = this.mOpenHelper.getReadableDatabase();
        int count = 0;
        final Cursor query = readableDatabase.query(s, new String[0], (String)null, (String[])null, (String)null, (String)null, (String)null);
        if (query != null) {
            count = query.getCount();
            query.close();
        }
        return count;
    }
    
    private String getTableName(final Uri obj) {
        if (CameraUISettingsProvider.URI_MATCHER.match(obj) != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid uri for this content provider. uri:");
            sb.append(obj);
            throw new SQLException(sb.toString());
        }
        return "capturingmodes";
    }
    
    private void in(final Uri obj) {
        final String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        final long id = Thread.currentThread().getId();
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("### [");
            sb.append(id);
            sb.append("]");
            sb.append(methodName);
            sb.append("() --> start #");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
    }
    
    private boolean isProcessingBatch() {
        synchronized (this) {
            return this.mProcessingBatchCount > 0;
        }
    }
    
    private void onCompleteOperation(final Uri obj) {
        if (CamLog.VERBOSE) {
            this.in(obj);
        }
        if (obj == null) {
            if (CamLog.VERBOSE) {
                this.out();
            }
            return;
        }
        try {
            if (CamLog.VERBOSE) {
                final StringBuilder sb = new StringBuilder();
                sb.append("notify changes uri:");
                sb.append(obj);
                this.debug(sb.toString());
            }
            this.getContext().getContentResolver().notifyChange(obj, (ContentObserver)null);
        }
        finally {
            if (CamLog.VERBOSE) {
                this.out();
            }
        }
    }
    
    private void out() {
        final String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        final long id = Thread.currentThread().getId();
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("### [");
            sb.append(id);
            sb.append("]");
            sb.append(methodName);
            sb.append("() --> end");
            CamLog.d(sb.toString());
        }
    }
    
    public ContentProviderResult[] applyBatch(final ArrayList<ContentProviderOperation> list) throws OperationApplicationException {
        if (CamLog.VERBOSE) {
            this.in(null);
        }
        this.incrementProcessingBatchCount();
        final ContentProviderResult[] applyBatch = super.applyBatch((ArrayList)list);
        this.decrementProcessingBatchCount();
        final HashSet set = new HashSet();
        for (final ContentProviderOperation contentProviderOperation : list) {
            if (contentProviderOperation.getUri() != null) {
                set.add(contentProviderOperation.getUri());
            }
        }
        final Iterator iterator2 = set.iterator();
        while (iterator2.hasNext()) {
            this.onCompleteOperation((Uri)iterator2.next());
        }
        if (CamLog.VERBOSE) {
            this.out();
        }
        return applyBatch;
    }
    
    void decrementProcessingBatchCount() {
        synchronized (this) {
            --this.mProcessingBatchCount;
        }
    }
    
    public int delete(final Uri uri, final String str, final String[] a) {
        if (CamLog.VERBOSE) {
            this.in(uri);
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("delete uri:");
            sb.append(uri.toString());
            sb.append(" selection:");
            sb.append(str);
            sb.append(" arguments:");
            sb.append(Arrays.toString(a));
            CamLog.d(sb.toString());
        }
        final int delete = this.mOpenHelper.getWritableDatabase().delete(this.getTableName(uri), str, a);
        if (delete > 0 && !this.isProcessingBatch()) {
            this.onCompleteOperation(uri);
        }
        if (CamLog.VERBOSE) {
            this.out();
        }
        return delete;
    }
    
    protected String getDataBaseName() {
        return "cameraui.db";
    }
    
    public String getType(final Uri uri) {
        return (String)CameraUISettingsProvider.MIMETYPE_LIST.get(CameraUISettingsProvider.URI_MATCHER.match(uri));
    }
    
    void incrementProcessingBatchCount() {
        synchronized (this) {
            ++this.mProcessingBatchCount;
        }
    }
    
    public Uri insert(final Uri obj, final ContentValues obj2) {
        if (CamLog.VERBOSE) {
            this.in(obj);
        }
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("insert uri:");
            sb.append(obj);
            sb.append(" values:");
            sb.append(obj2);
            CamLog.d(sb.toString());
        }
        final SQLiteDatabase writableDatabase = this.mOpenHelper.getWritableDatabase();
        final String tableName = this.getTableName(obj);
        final Uri uri = null;
        final long insert = writableDatabase.insert(tableName, (String)null, obj2);
        Uri withAppendedPath = uri;
        if (insert != -1L) {
            withAppendedPath = Uri.withAppendedPath(obj, String.valueOf(insert));
            if (!this.isProcessingBatch()) {
                this.onCompleteOperation(obj);
                withAppendedPath = withAppendedPath;
            }
        }
        if (CamLog.VERBOSE) {
            this.out();
        }
        return withAppendedPath;
    }
    
    public boolean onCreate() {
        this.mOpenHelper = new MyOpenHelper(this.getContext(), this.getDataBaseName());
        return true;
    }
    
    public Cursor query(final Uri uri, final String[] array, final String s, final String[] array2, final String s2) {
        if (CamLog.VERBOSE) {
            this.in(uri);
        }
        final Cursor query = this.mOpenHelper.getReadableDatabase().query(this.getTableName(uri), array, s, array2, (String)null, (String)null, s2);
        if (CamLog.VERBOSE) {
            this.out();
        }
        return query;
    }
    
    public int update(final Uri uri, final ContentValues contentValues, final String s, final String[] array) {
        if (CamLog.VERBOSE) {
            this.in(uri);
        }
        final SQLiteDatabase writableDatabase = this.mOpenHelper.getWritableDatabase();
        final String tableName = this.getTableName(uri);
        int update = 0;
        Label_0170: {
            if (s != null) {
                try {
                    final int n = update = writableDatabase.update(tableName, contentValues, s, array);
                    if (n <= 0) {
                        break Label_0170;
                    }
                    update = n;
                    try {
                        if (!this.isProcessingBatch()) {
                            this.onCompleteOperation(uri);
                            update = n;
                        }
                        break Label_0170;
                    }
                    catch (final SQLiteConstraintException ex) {
                        update = n;
                    }
                }
                catch (final SQLiteConstraintException ex) {
                    update = 0;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("Failed to update the record. Message : ");
                final SQLiteConstraintException ex;
                sb.append(ex.getMessage());
                CamLog.e(sb.toString());
            }
            else if (writableDatabase.replace(this.getTableName(uri), (String)null, contentValues) != -1L) {
                if (!this.isProcessingBatch()) {
                    this.onCompleteOperation(uri);
                }
                update = 1;
            }
            else {
                update = 0;
            }
        }
        if (CamLog.VERBOSE) {
            this.out();
        }
        return update;
    }
    
    interface CapturingMode
    {
        public static final Uri CONTENT_URI = CameraCommonProviderConstants.CAPTURINGMODE_CONTENT_URI;
        public static final String MIME_TYPE = "capturingmode";
        public static final String NAME = "capturingmodes";
        public static final String PATH = "capturingmodes";
    }
    
    static class MyOpenHelper extends SQLiteOpenHelper
    {
        private final Context mContext;
        
        public MyOpenHelper(final Context mContext, final String s) {
            super(mContext, s, (SQLiteDatabase$CursorFactory)null, 5);
            this.mContext = mContext;
        }
        
        static void createCapturingModeTable(final SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("CREATE TABLE capturingmodes (_id INTEGER PRIMARY KEY AUTOINCREMENT,package TEXT, activity TEXT, mode_name TEXT, capture_type INTEGER, visibility_normal INTEGER, visibility_oneshot INTEGER, visibility_shortcut INTEGER, sort_order INTEGER, selectorlabel_id INTEGER, selectoricon_id INTEGER, shortcutlabel_id INTEGER, shortcuticon_id INTEGER, descriptionlabel_id INTEGER, UNIQUE(package,mode_name));");
        }
        
        static void deleteTables(final SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS capturingmodes");
        }
        
        static boolean existTable(final SQLiteDatabase sqLiteDatabase, String rawQuery) {
            final Cursor cursor = null;
            Cursor cursor2 = null;
            Label_0092: {
                try {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("SELECT name FROM sqlite_master WHERE type='table' AND name='");
                    sb.append(rawQuery);
                    sb.append("'");
                    rawQuery = (String)sqLiteDatabase.rawQuery(sb.toString(), (String[])null);
                    if (rawQuery != null) {
                        try {
                            if (((Cursor)rawQuery).getCount() > 0) {
                                if (rawQuery != null) {
                                    ((Cursor)rawQuery).close();
                                }
                                return true;
                            }
                        }
                        finally {
                            break Label_0092;
                        }
                    }
                    if (rawQuery != null) {
                        ((Cursor)rawQuery).close();
                    }
                    return false;
                }
                finally {
                    cursor2 = cursor;
                }
            }
            if (cursor2 != null) {
                cursor2.close();
            }
        }
        
        public void onCreate(final SQLiteDatabase sqLiteDatabase) {
            if (!existTable(sqLiteDatabase, "capturingmodes")) {
                createCapturingModeTable(sqLiteDatabase);
            }
        }
        
        public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int i, final int j) {
            if (CamLog.VERBOSE) {
                CamLog.d("onUpgrade()");
                final StringBuilder sb = new StringBuilder();
                sb.append("  oldVersion:");
                sb.append(i);
                CamLog.d(sb.toString());
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("  newVersion:");
                sb2.append(j);
                CamLog.d(sb2.toString());
            }
            deleteTables(sqLiteDatabase);
            createCapturingModeTable(sqLiteDatabase);
        }
    }
    
    interface Path
    {
        public static final int CAPTURINGMODE_DIR = 1;
        public static final int CAPTURINGMODE_ITEM = 2;
    }
}
