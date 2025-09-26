// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import java.math.BigDecimal;
import android.content.ComponentName;
import java.util.Collections;
import java.util.Collection;
import android.os.AsyncTask;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.content.Context;
import java.util.List;
import java.util.Map;
import android.database.DataSetObservable;

class ActivityChooserModel extends DataSetObservable
{
    static final String ATTRIBUTE_ACTIVITY = "activity";
    static final String ATTRIBUTE_TIME = "time";
    static final String ATTRIBUTE_WEIGHT = "weight";
    static final boolean DEBUG = false;
    private static final int DEFAULT_ACTIVITY_INFLATION = 5;
    private static final float DEFAULT_HISTORICAL_RECORD_WEIGHT = 1.0f;
    public static final String DEFAULT_HISTORY_FILE_NAME = "activity_choser_model_history.xml";
    public static final int DEFAULT_HISTORY_MAX_LENGTH = 50;
    private static final String HISTORY_FILE_EXTENSION = ".xml";
    private static final int INVALID_INDEX = -1;
    static final String LOG_TAG = "ActivityChooserModel";
    static final String TAG_HISTORICAL_RECORD = "historical-record";
    static final String TAG_HISTORICAL_RECORDS = "historical-records";
    private static final Map<String, ActivityChooserModel> sDataModelRegistry;
    private static final Object sRegistryLock;
    private final List<ActivityResolveInfo> mActivities;
    private OnChooseActivityListener mActivityChoserModelPolicy;
    private ActivitySorter mActivitySorter;
    boolean mCanReadHistoricalData;
    final Context mContext;
    private final List<HistoricalRecord> mHistoricalRecords;
    private boolean mHistoricalRecordsChanged;
    final String mHistoryFileName;
    private int mHistoryMaxSize;
    private final Object mInstanceLock;
    private Intent mIntent;
    private boolean mReadShareHistoryCalled;
    private boolean mReloadActivities;
    
    static {
        sRegistryLock = new Object();
        sDataModelRegistry = new HashMap<String, ActivityChooserModel>();
    }
    
    private ActivityChooserModel(final Context context, final String s) {
        this.mInstanceLock = new Object();
        this.mActivities = new ArrayList<ActivityResolveInfo>();
        this.mHistoricalRecords = new ArrayList<HistoricalRecord>();
        this.mActivitySorter = (ActivitySorter)new DefaultSorter();
        this.mHistoryMaxSize = 50;
        this.mCanReadHistoricalData = true;
        this.mReadShareHistoryCalled = false;
        this.mHistoricalRecordsChanged = true;
        this.mReloadActivities = false;
        this.mContext = context.getApplicationContext();
        if (!TextUtils.isEmpty((CharSequence)s) && !s.endsWith(".xml")) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(".xml");
            this.mHistoryFileName = sb.toString();
        }
        else {
            this.mHistoryFileName = s;
        }
    }
    
    private boolean addHistoricalRecord(final HistoricalRecord historicalRecord) {
        final boolean add = this.mHistoricalRecords.add(historicalRecord);
        if (add) {
            this.mHistoricalRecordsChanged = true;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            this.persistHistoricalDataIfNeeded();
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
        return add;
    }
    
    private void ensureConsistentState() {
        final boolean loadActivitiesIfNeeded = this.loadActivitiesIfNeeded();
        final boolean historicalDataIfNeeded = this.readHistoricalDataIfNeeded();
        this.pruneExcessiveHistoricalRecordsIfNeeded();
        if (loadActivitiesIfNeeded | historicalDataIfNeeded) {
            this.sortActivitiesIfNeeded();
            this.notifyChanged();
        }
    }
    
    public static ActivityChooserModel get(final Context context, final String s) {
        synchronized (ActivityChooserModel.sRegistryLock) {
            ActivityChooserModel activityChooserModel;
            if ((activityChooserModel = ActivityChooserModel.sDataModelRegistry.get(s)) == null) {
                activityChooserModel = new ActivityChooserModel(context, s);
                ActivityChooserModel.sDataModelRegistry.put(s, activityChooserModel);
            }
            return activityChooserModel;
        }
    }
    
    private boolean loadActivitiesIfNeeded() {
        final boolean mReloadActivities = this.mReloadActivities;
        int i = 0;
        if (mReloadActivities && this.mIntent != null) {
            this.mReloadActivities = false;
            this.mActivities.clear();
            for (List queryIntentActivities = this.mContext.getPackageManager().queryIntentActivities(this.mIntent, 0); i < queryIntentActivities.size(); ++i) {
                this.mActivities.add(new ActivityResolveInfo((ResolveInfo)queryIntentActivities.get(i)));
            }
            return true;
        }
        return false;
    }
    
    private void persistHistoricalDataIfNeeded() {
        if (!this.mReadShareHistoryCalled) {
            throw new IllegalStateException("No preceding call to #readHistoricalData");
        }
        if (!this.mHistoricalRecordsChanged) {
            return;
        }
        this.mHistoricalRecordsChanged = false;
        if (!TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) {
            new PersistHistoryAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[] { new ArrayList(this.mHistoricalRecords), this.mHistoryFileName });
        }
    }
    
    private void pruneExcessiveHistoricalRecordsIfNeeded() {
        final int n = this.mHistoricalRecords.size() - this.mHistoryMaxSize;
        if (n <= 0) {
            return;
        }
        this.mHistoricalRecordsChanged = true;
        for (int i = 0; i < n; ++i) {
            final HistoricalRecord historicalRecord = this.mHistoricalRecords.remove(0);
        }
    }
    
    private boolean readHistoricalDataIfNeeded() {
        if (this.mCanReadHistoricalData && this.mHistoricalRecordsChanged && !TextUtils.isEmpty((CharSequence)this.mHistoryFileName)) {
            this.mCanReadHistoricalData = false;
            this.mReadShareHistoryCalled = true;
            this.readHistoricalDataImpl();
            return true;
        }
        return false;
    }
    
    private void readHistoricalDataImpl() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        android/support/v7/widget/ActivityChooserModel.mContext:Landroid/content/Context;
        //     4: aload_0        
        //     5: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //     8: invokevirtual   android/content/Context.openFileInput:(Ljava/lang/String;)Ljava/io/FileInputStream;
        //    11: astore          5
        //    13: invokestatic    android/util/Xml.newPullParser:()Lorg/xmlpull/v1/XmlPullParser;
        //    16: astore          7
        //    18: aload           7
        //    20: aload           5
        //    22: ldc_w           "UTF-8"
        //    25: invokeinterface org/xmlpull/v1/XmlPullParser.setInput:(Ljava/io/InputStream;Ljava/lang/String;)V
        //    30: iconst_0       
        //    31: istore_2       
        //    32: iload_2        
        //    33: iconst_1       
        //    34: if_icmpeq       53
        //    37: iload_2        
        //    38: iconst_2       
        //    39: if_icmpeq       53
        //    42: aload           7
        //    44: invokeinterface org/xmlpull/v1/XmlPullParser.next:()I
        //    49: istore_2       
        //    50: goto            32
        //    53: ldc             "historical-records"
        //    55: aload           7
        //    57: invokeinterface org/xmlpull/v1/XmlPullParser.getName:()Ljava/lang/String;
        //    62: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    65: ifne            84
        //    68: new             Lorg/xmlpull/v1/XmlPullParserException;
        //    71: astore          6
        //    73: aload           6
        //    75: ldc_w           "Share records file does not start with historical-records tag."
        //    78: invokespecial   org/xmlpull/v1/XmlPullParserException.<init>:(Ljava/lang/String;)V
        //    81: aload           6
        //    83: athrow         
        //    84: aload_0        
        //    85: getfield        android/support/v7/widget/ActivityChooserModel.mHistoricalRecords:Ljava/util/List;
        //    88: astore          9
        //    90: aload           9
        //    92: invokeinterface java/util/List.clear:()V
        //    97: aload           7
        //    99: invokeinterface org/xmlpull/v1/XmlPullParser.next:()I
        //   104: istore_2       
        //   105: iload_2        
        //   106: iconst_1       
        //   107: if_icmpne       123
        //   110: aload           5
        //   112: ifnull          353
        //   115: aload           5
        //   117: invokevirtual   java/io/FileInputStream.close:()V
        //   120: goto            353
        //   123: iload_2        
        //   124: iconst_3       
        //   125: if_icmpeq       97
        //   128: iload_2        
        //   129: iconst_4       
        //   130: if_icmpne       136
        //   133: goto            97
        //   136: ldc             "historical-record"
        //   138: aload           7
        //   140: invokeinterface org/xmlpull/v1/XmlPullParser.getName:()Ljava/lang/String;
        //   145: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   148: ifne            167
        //   151: new             Lorg/xmlpull/v1/XmlPullParserException;
        //   154: astore          6
        //   156: aload           6
        //   158: ldc_w           "Share records file not well-formed."
        //   161: invokespecial   org/xmlpull/v1/XmlPullParserException.<init>:(Ljava/lang/String;)V
        //   164: aload           6
        //   166: athrow         
        //   167: aload           7
        //   169: aconst_null    
        //   170: ldc             "activity"
        //   172: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   177: astore          6
        //   179: aload           7
        //   181: aconst_null    
        //   182: ldc             "time"
        //   184: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   189: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //   192: lstore_3       
        //   193: aload           7
        //   195: aconst_null    
        //   196: ldc             "weight"
        //   198: invokeinterface org/xmlpull/v1/XmlPullParser.getAttributeValue:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   203: invokestatic    java/lang/Float.parseFloat:(Ljava/lang/String;)F
        //   206: fstore_1       
        //   207: new             Landroid/support/v7/widget/ActivityChooserModel$HistoricalRecord;
        //   210: astore          8
        //   212: aload           8
        //   214: aload           6
        //   216: lload_3        
        //   217: fload_1        
        //   218: invokespecial   android/support/v7/widget/ActivityChooserModel$HistoricalRecord.<init>:(Ljava/lang/String;JF)V
        //   221: aload           9
        //   223: aload           8
        //   225: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   230: pop            
        //   231: goto            97
        //   234: astore          6
        //   236: goto            354
        //   239: astore          8
        //   241: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
        //   244: astore          7
        //   246: new             Ljava/lang/StringBuilder;
        //   249: astore          6
        //   251: aload           6
        //   253: invokespecial   java/lang/StringBuilder.<init>:()V
        //   256: aload           6
        //   258: ldc_w           "Error reading historical recrod file: "
        //   261: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   264: pop            
        //   265: aload           6
        //   267: aload_0        
        //   268: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //   271: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   274: pop            
        //   275: aload           7
        //   277: aload           6
        //   279: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   282: aload           8
        //   284: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   287: pop            
        //   288: aload           5
        //   290: ifnull          353
        //   293: goto            115
        //   296: astore          7
        //   298: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
        //   301: astore          8
        //   303: new             Ljava/lang/StringBuilder;
        //   306: astore          6
        //   308: aload           6
        //   310: invokespecial   java/lang/StringBuilder.<init>:()V
        //   313: aload           6
        //   315: ldc_w           "Error reading historical recrod file: "
        //   318: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   321: pop            
        //   322: aload           6
        //   324: aload_0        
        //   325: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
        //   328: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   331: pop            
        //   332: aload           8
        //   334: aload           6
        //   336: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   339: aload           7
        //   341: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   344: pop            
        //   345: aload           5
        //   347: ifnull          353
        //   350: goto            115
        //   353: return         
        //   354: aload           5
        //   356: ifnull          364
        //   359: aload           5
        //   361: invokevirtual   java/io/FileInputStream.close:()V
        //   364: aload           6
        //   366: athrow         
        //   367: astore          5
        //   369: return         
        //   370: astore          5
        //   372: goto            353
        //   375: astore          5
        //   377: goto            364
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                   
        //  -----  -----  -----  -----  ---------------------------------------
        //  0      13     367    370    Ljava/io/FileNotFoundException;
        //  13     30     296    353    Lorg/xmlpull/v1/XmlPullParserException;
        //  13     30     239    296    Ljava/io/IOException;
        //  13     30     234    367    Any
        //  42     50     296    353    Lorg/xmlpull/v1/XmlPullParserException;
        //  42     50     239    296    Ljava/io/IOException;
        //  42     50     234    367    Any
        //  53     84     296    353    Lorg/xmlpull/v1/XmlPullParserException;
        //  53     84     239    296    Ljava/io/IOException;
        //  53     84     234    367    Any
        //  84     97     296    353    Lorg/xmlpull/v1/XmlPullParserException;
        //  84     97     239    296    Ljava/io/IOException;
        //  84     97     234    367    Any
        //  97     105    296    353    Lorg/xmlpull/v1/XmlPullParserException;
        //  97     105    239    296    Ljava/io/IOException;
        //  97     105    234    367    Any
        //  115    120    370    375    Ljava/io/IOException;
        //  136    167    296    353    Lorg/xmlpull/v1/XmlPullParserException;
        //  136    167    239    296    Ljava/io/IOException;
        //  136    167    234    367    Any
        //  167    231    296    353    Lorg/xmlpull/v1/XmlPullParserException;
        //  167    231    239    296    Ljava/io/IOException;
        //  167    231    234    367    Any
        //  241    288    234    367    Any
        //  298    345    234    367    Any
        //  359    364    375    380    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.util.ConcurrentModificationException
        //     at java.base/java.util.ArrayList$Itr.checkForComodification(ArrayList.java:1095)
        //     at java.base/java.util.ArrayList$Itr.next(ArrayList.java:1049)
        //     at com.strobel.decompiler.ast.AstBuilder.convertLocalVariables(AstBuilder.java:2913)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2501)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private boolean sortActivitiesIfNeeded() {
        if (this.mActivitySorter != null && this.mIntent != null && !this.mActivities.isEmpty() && !this.mHistoricalRecords.isEmpty()) {
            this.mActivitySorter.sort(this.mIntent, this.mActivities, Collections.unmodifiableList((List<? extends HistoricalRecord>)this.mHistoricalRecords));
            return true;
        }
        return false;
    }
    
    public Intent chooseActivity(final int n) {
        synchronized (this.mInstanceLock) {
            if (this.mIntent == null) {
                return null;
            }
            this.ensureConsistentState();
            final ActivityResolveInfo activityResolveInfo = this.mActivities.get(n);
            final ComponentName component = new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name);
            final Intent intent = new Intent(this.mIntent);
            intent.setComponent(component);
            if (this.mActivityChoserModelPolicy != null && this.mActivityChoserModelPolicy.onChooseActivity(this, new Intent(intent))) {
                return null;
            }
            this.addHistoricalRecord(new HistoricalRecord(component, System.currentTimeMillis(), 1.0f));
            return intent;
        }
    }
    
    public ResolveInfo getActivity(final int n) {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            return this.mActivities.get(n).resolveInfo;
        }
    }
    
    public int getActivityCount() {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            return this.mActivities.size();
        }
    }
    
    public int getActivityIndex(final ResolveInfo resolveInfo) {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            final List<ActivityResolveInfo> mActivities = this.mActivities;
            for (int size = mActivities.size(), i = 0; i < size; ++i) {
                if (((ActivityResolveInfo)mActivities.get(i)).resolveInfo == resolveInfo) {
                    return i;
                }
            }
            return -1;
        }
    }
    
    public ResolveInfo getDefaultActivity() {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            if (!this.mActivities.isEmpty()) {
                return this.mActivities.get(0).resolveInfo;
            }
            return null;
        }
    }
    
    public int getHistoryMaxSize() {
        synchronized (this.mInstanceLock) {
            return this.mHistoryMaxSize;
        }
    }
    
    public int getHistorySize() {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            return this.mHistoricalRecords.size();
        }
    }
    
    public Intent getIntent() {
        synchronized (this.mInstanceLock) {
            return this.mIntent;
        }
    }
    
    public void setActivitySorter(final ActivitySorter mActivitySorter) {
        synchronized (this.mInstanceLock) {
            if (this.mActivitySorter == mActivitySorter) {
                return;
            }
            this.mActivitySorter = mActivitySorter;
            if (this.sortActivitiesIfNeeded()) {
                this.notifyChanged();
            }
        }
    }
    
    public void setDefaultActivity(final int n) {
        synchronized (this.mInstanceLock) {
            this.ensureConsistentState();
            final ActivityResolveInfo activityResolveInfo = this.mActivities.get(n);
            final ActivityResolveInfo activityResolveInfo2 = this.mActivities.get(0);
            float n2;
            if (activityResolveInfo2 != null) {
                n2 = activityResolveInfo2.weight - activityResolveInfo.weight + 5.0f;
            }
            else {
                n2 = 1.0f;
            }
            this.addHistoricalRecord(new HistoricalRecord(new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name), System.currentTimeMillis(), n2));
        }
    }
    
    public void setHistoryMaxSize(final int mHistoryMaxSize) {
        synchronized (this.mInstanceLock) {
            if (this.mHistoryMaxSize == mHistoryMaxSize) {
                return;
            }
            this.mHistoryMaxSize = mHistoryMaxSize;
            this.pruneExcessiveHistoricalRecordsIfNeeded();
            if (this.sortActivitiesIfNeeded()) {
                this.notifyChanged();
            }
        }
    }
    
    public void setIntent(final Intent mIntent) {
        synchronized (this.mInstanceLock) {
            if (this.mIntent == mIntent) {
                return;
            }
            this.mIntent = mIntent;
            this.mReloadActivities = true;
            this.ensureConsistentState();
        }
    }
    
    public void setOnChooseActivityListener(final OnChooseActivityListener mActivityChoserModelPolicy) {
        synchronized (this.mInstanceLock) {
            this.mActivityChoserModelPolicy = mActivityChoserModelPolicy;
        }
    }
    
    public interface ActivityChooserModelClient
    {
        void setActivityChooserModel(final ActivityChooserModel p0);
    }
    
    public static final class ActivityResolveInfo implements Comparable<ActivityResolveInfo>
    {
        public final ResolveInfo resolveInfo;
        public float weight;
        
        public ActivityResolveInfo(final ResolveInfo resolveInfo) {
            this.resolveInfo = resolveInfo;
        }
        
        @Override
        public int compareTo(final ActivityResolveInfo activityResolveInfo) {
            return Float.floatToIntBits(activityResolveInfo.weight) - Float.floatToIntBits(this.weight);
        }
        
        @Override
        public boolean equals(final Object o) {
            return this == o || (o != null && this.getClass() == o.getClass() && Float.floatToIntBits(this.weight) == Float.floatToIntBits(((ActivityResolveInfo)o).weight));
        }
        
        @Override
        public int hashCode() {
            return 31 + Float.floatToIntBits(this.weight);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append("resolveInfo:");
            sb.append(this.resolveInfo.toString());
            sb.append("; weight:");
            sb.append(new BigDecimal(this.weight));
            sb.append("]");
            return sb.toString();
        }
    }
    
    public interface ActivitySorter
    {
        void sort(final Intent p0, final List<ActivityResolveInfo> p1, final List<HistoricalRecord> p2);
    }
    
    private static final class DefaultSorter implements ActivitySorter
    {
        private static final float WEIGHT_DECAY_COEFFICIENT = 0.95f;
        private final Map<ComponentName, ActivityResolveInfo> mPackageNameToActivityMap;
        
        DefaultSorter() {
            this.mPackageNameToActivityMap = new HashMap<ComponentName, ActivityResolveInfo>();
        }
        
        @Override
        public void sort(final Intent intent, final List<ActivityResolveInfo> list, final List<HistoricalRecord> list2) {
            final Map<ComponentName, ActivityResolveInfo> mPackageNameToActivityMap = this.mPackageNameToActivityMap;
            mPackageNameToActivityMap.clear();
            for (int size = list.size(), i = 0; i < size; ++i) {
                final ActivityResolveInfo activityResolveInfo = list.get(i);
                activityResolveInfo.weight = 0.0f;
                mPackageNameToActivityMap.put(new ComponentName(activityResolveInfo.resolveInfo.activityInfo.packageName, activityResolveInfo.resolveInfo.activityInfo.name), activityResolveInfo);
            }
            int j = list2.size() - 1;
            float n = 1.0f;
            while (j >= 0) {
                final HistoricalRecord historicalRecord = list2.get(j);
                final ActivityResolveInfo activityResolveInfo2 = mPackageNameToActivityMap.get(historicalRecord.activity);
                float n2 = n;
                if (activityResolveInfo2 != null) {
                    activityResolveInfo2.weight += historicalRecord.weight * n;
                    n2 = n * 0.95f;
                }
                --j;
                n = n2;
            }
            Collections.sort((List<Comparable>)list);
        }
    }
    
    public static final class HistoricalRecord
    {
        public final ComponentName activity;
        public final long time;
        public final float weight;
        
        public HistoricalRecord(final ComponentName activity, final long time, final float weight) {
            this.activity = activity;
            this.time = time;
            this.weight = weight;
        }
        
        public HistoricalRecord(final String s, final long n, final float n2) {
            this(ComponentName.unflattenFromString(s), n, n2);
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final HistoricalRecord historicalRecord = (HistoricalRecord)o;
            if (this.activity == null) {
                if (historicalRecord.activity != null) {
                    return false;
                }
            }
            else if (!this.activity.equals((Object)historicalRecord.activity)) {
                return false;
            }
            return this.time == historicalRecord.time && Float.floatToIntBits(this.weight) == Float.floatToIntBits(historicalRecord.weight);
        }
        
        @Override
        public int hashCode() {
            int hashCode;
            if (this.activity == null) {
                hashCode = 0;
            }
            else {
                hashCode = this.activity.hashCode();
            }
            return 31 * ((hashCode + 31) * 31 + (int)(this.time ^ this.time >>> 32)) + Float.floatToIntBits(this.weight);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append("; activity:");
            sb.append(this.activity);
            sb.append("; time:");
            sb.append(this.time);
            sb.append("; weight:");
            sb.append(new BigDecimal(this.weight));
            sb.append("]");
            return sb.toString();
        }
    }
    
    public interface OnChooseActivityListener
    {
        boolean onChooseActivity(final ActivityChooserModel p0, final Intent p1);
    }
    
    private final class PersistHistoryAsyncTask extends AsyncTask<Object, Void, Void>
    {
        final ActivityChooserModel this$0;
        
        PersistHistoryAsyncTask(final ActivityChooserModel this$0) {
            this.this$0 = this$0;
        }
        
        public Void doInBackground(final Object... p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: iconst_0       
            //     2: aaload         
            //     3: checkcast       Ljava/util/List;
            //     6: astore          5
            //     8: aload_1        
            //     9: iconst_1       
            //    10: aaload         
            //    11: checkcast       Ljava/lang/String;
            //    14: astore          4
            //    16: aload_0        
            //    17: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //    20: getfield        android/support/v7/widget/ActivityChooserModel.mContext:Landroid/content/Context;
            //    23: aload           4
            //    25: iconst_0       
            //    26: invokevirtual   android/content/Context.openFileOutput:(Ljava/lang/String;I)Ljava/io/FileOutputStream;
            //    29: astore_1       
            //    30: invokestatic    android/util/Xml.newSerializer:()Lorg/xmlpull/v1/XmlSerializer;
            //    33: astore          4
            //    35: aload           4
            //    37: aload_1        
            //    38: aconst_null    
            //    39: invokeinterface org/xmlpull/v1/XmlSerializer.setOutput:(Ljava/io/OutputStream;Ljava/lang/String;)V
            //    44: aload           4
            //    46: ldc             "UTF-8"
            //    48: iconst_1       
            //    49: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
            //    52: invokeinterface org/xmlpull/v1/XmlSerializer.startDocument:(Ljava/lang/String;Ljava/lang/Boolean;)V
            //    57: aload           4
            //    59: aconst_null    
            //    60: ldc             "historical-records"
            //    62: invokeinterface org/xmlpull/v1/XmlSerializer.startTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //    67: pop            
            //    68: aload           5
            //    70: invokeinterface java/util/List.size:()I
            //    75: istore_3       
            //    76: iconst_0       
            //    77: istore_2       
            //    78: iload_2        
            //    79: iload_3        
            //    80: if_icmpge       181
            //    83: aload           5
            //    85: iconst_0       
            //    86: invokeinterface java/util/List.remove:(I)Ljava/lang/Object;
            //    91: checkcast       Landroid/support/v7/widget/ActivityChooserModel$HistoricalRecord;
            //    94: astore          6
            //    96: aload           4
            //    98: aconst_null    
            //    99: ldc             "historical-record"
            //   101: invokeinterface org/xmlpull/v1/XmlSerializer.startTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   106: pop            
            //   107: aload           4
            //   109: aconst_null    
            //   110: ldc             "activity"
            //   112: aload           6
            //   114: getfield        android/support/v7/widget/ActivityChooserModel$HistoricalRecord.activity:Landroid/content/ComponentName;
            //   117: invokevirtual   android/content/ComponentName.flattenToString:()Ljava/lang/String;
            //   120: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   125: pop            
            //   126: aload           4
            //   128: aconst_null    
            //   129: ldc             "time"
            //   131: aload           6
            //   133: getfield        android/support/v7/widget/ActivityChooserModel$HistoricalRecord.time:J
            //   136: invokestatic    java/lang/String.valueOf:(J)Ljava/lang/String;
            //   139: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   144: pop            
            //   145: aload           4
            //   147: aconst_null    
            //   148: ldc             "weight"
            //   150: aload           6
            //   152: getfield        android/support/v7/widget/ActivityChooserModel$HistoricalRecord.weight:F
            //   155: invokestatic    java/lang/String.valueOf:(F)Ljava/lang/String;
            //   158: invokeinterface org/xmlpull/v1/XmlSerializer.attribute:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   163: pop            
            //   164: aload           4
            //   166: aconst_null    
            //   167: ldc             "historical-record"
            //   169: invokeinterface org/xmlpull/v1/XmlSerializer.endTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   174: pop            
            //   175: iinc            2, 1
            //   178: goto            78
            //   181: aload           4
            //   183: aconst_null    
            //   184: ldc             "historical-records"
            //   186: invokeinterface org/xmlpull/v1/XmlSerializer.endTag:(Ljava/lang/String;Ljava/lang/String;)Lorg/xmlpull/v1/XmlSerializer;
            //   191: pop            
            //   192: aload           4
            //   194: invokeinterface org/xmlpull/v1/XmlSerializer.endDocument:()V
            //   199: aload_0        
            //   200: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   203: iconst_1       
            //   204: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   207: aload_1        
            //   208: ifnull          421
            //   211: aload_1        
            //   212: invokevirtual   java/io/FileOutputStream.close:()V
            //   215: goto            421
            //   218: astore          4
            //   220: goto            423
            //   223: astore          6
            //   225: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   228: astore          5
            //   230: new             Ljava/lang/StringBuilder;
            //   233: astore          4
            //   235: aload           4
            //   237: invokespecial   java/lang/StringBuilder.<init>:()V
            //   240: aload           4
            //   242: ldc             "Error writing historical record file: "
            //   244: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   247: pop            
            //   248: aload           4
            //   250: aload_0        
            //   251: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   254: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   257: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   260: pop            
            //   261: aload           5
            //   263: aload           4
            //   265: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   268: aload           6
            //   270: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   273: pop            
            //   274: aload_0        
            //   275: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   278: iconst_1       
            //   279: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   282: aload_1        
            //   283: ifnull          421
            //   286: goto            211
            //   289: astore          5
            //   291: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   294: astore          6
            //   296: new             Ljava/lang/StringBuilder;
            //   299: astore          4
            //   301: aload           4
            //   303: invokespecial   java/lang/StringBuilder.<init>:()V
            //   306: aload           4
            //   308: ldc             "Error writing historical record file: "
            //   310: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   313: pop            
            //   314: aload           4
            //   316: aload_0        
            //   317: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   320: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   323: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   326: pop            
            //   327: aload           6
            //   329: aload           4
            //   331: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   334: aload           5
            //   336: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   339: pop            
            //   340: aload_0        
            //   341: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   344: iconst_1       
            //   345: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   348: aload_1        
            //   349: ifnull          421
            //   352: goto            211
            //   355: astore          5
            //   357: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   360: astore          4
            //   362: new             Ljava/lang/StringBuilder;
            //   365: astore          6
            //   367: aload           6
            //   369: invokespecial   java/lang/StringBuilder.<init>:()V
            //   372: aload           6
            //   374: ldc             "Error writing historical record file: "
            //   376: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   379: pop            
            //   380: aload           6
            //   382: aload_0        
            //   383: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   386: getfield        android/support/v7/widget/ActivityChooserModel.mHistoryFileName:Ljava/lang/String;
            //   389: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   392: pop            
            //   393: aload           4
            //   395: aload           6
            //   397: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   400: aload           5
            //   402: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   405: pop            
            //   406: aload_0        
            //   407: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   410: iconst_1       
            //   411: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   414: aload_1        
            //   415: ifnull          421
            //   418: goto            211
            //   421: aconst_null    
            //   422: areturn        
            //   423: aload_0        
            //   424: getfield        android/support/v7/widget/ActivityChooserModel$PersistHistoryAsyncTask.this$0:Landroid/support/v7/widget/ActivityChooserModel;
            //   427: iconst_1       
            //   428: putfield        android/support/v7/widget/ActivityChooserModel.mCanReadHistoricalData:Z
            //   431: aload_1        
            //   432: ifnull          439
            //   435: aload_1        
            //   436: invokevirtual   java/io/FileOutputStream.close:()V
            //   439: aload           4
            //   441: athrow         
            //   442: astore          6
            //   444: getstatic       android/support/v7/widget/ActivityChooserModel.LOG_TAG:Ljava/lang/String;
            //   447: astore_1       
            //   448: new             Ljava/lang/StringBuilder;
            //   451: dup            
            //   452: invokespecial   java/lang/StringBuilder.<init>:()V
            //   455: astore          5
            //   457: aload           5
            //   459: ldc             "Error writing historical record file: "
            //   461: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   464: pop            
            //   465: aload           5
            //   467: aload           4
            //   469: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   472: pop            
            //   473: aload_1        
            //   474: aload           5
            //   476: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   479: aload           6
            //   481: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
            //   484: pop            
            //   485: aconst_null    
            //   486: areturn        
            //   487: astore_1       
            //   488: goto            421
            //   491: astore_1       
            //   492: goto            439
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                                
            //  -----  -----  -----  -----  ------------------------------------
            //  16     30     442    487    Ljava/io/FileNotFoundException;
            //  35     76     355    421    Ljava/lang/IllegalArgumentException;
            //  35     76     289    355    Ljava/lang/IllegalStateException;
            //  35     76     223    289    Ljava/io/IOException;
            //  35     76     218    442    Any
            //  83     175    355    421    Ljava/lang/IllegalArgumentException;
            //  83     175    289    355    Ljava/lang/IllegalStateException;
            //  83     175    223    289    Ljava/io/IOException;
            //  83     175    218    442    Any
            //  181    199    355    421    Ljava/lang/IllegalArgumentException;
            //  181    199    289    355    Ljava/lang/IllegalStateException;
            //  181    199    223    289    Ljava/io/IOException;
            //  181    199    218    442    Any
            //  211    215    487    491    Ljava/io/IOException;
            //  225    274    218    442    Any
            //  291    340    218    442    Any
            //  357    406    218    442    Any
            //  435    439    491    495    Ljava/io/IOException;
            // 
            // The error that occurred was:
            // 
            // java.lang.IndexOutOfBoundsException: Index 229 out of bounds for length 229
            //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:100)
            //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:106)
            //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:302)
            //     at java.base/java.util.Objects.checkIndex(Objects.java:385)
            //     at java.base/java.util.ArrayList.get(ArrayList.java:427)
            //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3362)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:112)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:203)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:93)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:868)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:761)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:638)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:662)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:605)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:195)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:162)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:137)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:333)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:254)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:129)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
    }
}
