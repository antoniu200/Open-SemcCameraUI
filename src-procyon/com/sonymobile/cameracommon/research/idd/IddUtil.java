// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.research.idd;

import java.util.Iterator;
import java.io.IOException;
import android.util.JsonWriter;
import android.util.ArrayMap;
import android.support.annotation.NonNull;
import com.sonymobile.cameracommon.research.parameters.Screen;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import android.text.TextUtils;
import com.sonyericsson.idd.api.Idd;
import org.json.JSONException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Map;
import com.sonymobile.cameracommon.research.parameters.Event;
import java.util.Locale;
import java.util.TimeZone;
import com.sonyericsson.android.camera.util.CamLog;
import org.json.JSONObject;
import com.sonyericsson.android.camera.CameraApplication;
import android.content.Context;

public class IddUtil
{
    private static final String EMPTY_STRING = "";
    private static final String IDD_CLASS_NAME = "com.sonyericsson.idd.api.Idd";
    private static final boolean IDD_DUMP_ENABLE = false;
    private static final String IDD_METHOD_NAME = "addAppDataJSON";
    private static final String KEY_ACTION = "action";
    private static final String KEY_ENVIRONMENT = "environment";
    private static final String KEY_LABEL = "label";
    private static final String KEY_LAUNCHEDBY = "launched_by";
    private static final String KEY_MODE = "mode";
    private static final String KEY_PERFORMANCE_BATTERY_LEVEL = "battery_level";
    private static final String KEY_PERFORMANCE_TARGET = "target";
    private static final String KEY_PERFORMANCE_THERMAL_STATUS = "thermal_status";
    private static final String KEY_PERFORMANCE_TIME = "time";
    private static final String KEY_SETTING = "setting";
    private static final String KEY_SUB_TYPE = "subtype";
    private static final String KEY_TIMESTAMP_ORIGINAL_EVENT = "timestamp_original_event";
    public static final String KEY_TYPE = "type";
    private static final String KEY_VALUE = "value";
    private static final String KEY_VALUE_AFTER = "after";
    private static final String KEY_VALUE_BEFORE = "before";
    private static final String KEY_VALUE_PAGE = "page";
    private static final String KEY_VALUE_RESULT = "result";
    private static final String KEY_VALUE_TIME = "time";
    public static final String KEY_VALUE_TO = "to";
    private static final String KEY_VALUE_WAY = "way";
    public static final String TAG = "IddUtil";
    private static final String THERMAL_STATUS_HIGH = "HIGH";
    private static final String THERMAL_STATUS_NORMAL = "NORMAL";
    private static final String TYPE_AUTO_POWEROFF_EVENT = "AUTO_POWEROFF";
    private static final String TYPE_CAMERA_NOT_AVAILABLE_EVENT = "CAMERA_NOT_AVAILABLE";
    public static final String TYPE_CHANGE_SETTING_EVENT = "CHANGE_SETTING_EVENT";
    private static final String TYPE_LOWBATTERY_ERROR_EVENT = "LOWBATTERY_ERROR";
    private static final String TYPE_MODE_CHANGE_EVENT = "MODE_CHANGE_EVENT";
    private static final String TYPE_MODE_SELECTOR_EVENT = "MODE_SELECTOR_EVENT";
    private static final String TYPE_PANORAMA_EVENT = "PANORAMA";
    private static final String TYPE_PERFORMANCE_EVENT = "PERFORMANCE";
    public static final String TYPE_PHOTO_EVENT = "PHOTO_EVENT";
    private static final String TYPE_PREDICTIVE_LAUNCH_EVENT = "PREDICTIVE_LAUNCH";
    private static final String TYPE_SELFTIMER_CANCEL_EVENT = "SELFTIMER_CANCEL_EVENT";
    private static final String TYPE_SETUP_WIZARD_EVENT = "SETUP_WIZARD_EVENT";
    private static final String TYPE_SLOW_MOTION_EVENT = "SLOW_MOTION_EVENT";
    private static final String TYPE_THERMAL_ERROR_EVENT = "THERMAL_ERROR";
    public static final String TYPE_VIDEO_EVENT = "VIDEO_EVENT";
    private static Context mContext;
    private static boolean mIsIddSupportAlreadyChecked = false;
    private static boolean mIsIddSupported = false;
    private static boolean mIsSendingAllowed = true;
    private static String mLaunchedBy = "";
    private static String mPackageName = "";
    private static int mVersionCode = 0;
    private static String mVersionName = "";
    private static String mView = "";
    
    static {
        IddUtil.mContext = CameraApplication.getContext();
    }
    
    private IddUtil() {
    }
    
    private static boolean checkIddSupported() {
        if (IddUtil.mIsIddSupportAlreadyChecked) {
            return IddUtil.mIsIddSupported;
        }
        IddUtil.mIsIddSupported = false;
        try {
            Class.forName("com.sonyericsson.idd.api.Idd").getMethod("addAppDataJSON", String.class, String.class, Integer.TYPE, JSONObject.class);
            IddUtil.mIsIddSupported = true;
            if (CamLog.VERBOSE) {
                CamLog.d("Idd.addAppDataJSON is supported");
            }
        }
        catch (final ClassNotFoundException | NoSuchMethodException | LinkageError classNotFoundException | NoSuchMethodException | LinkageError) {
            if (CamLog.VERBOSE) {
                CamLog.w("Idd.addAppDataJSON is not supported");
            }
        }
        IddUtil.mIsIddSupportAlreadyChecked = true;
        return IddUtil.mIsIddSupported;
    }
    
    private static String getCurrentTimeZone() {
        final int n = TimeZone.getDefault().getRawOffset() / 60000;
        final StringBuilder sb = new StringBuilder(6);
        if (n < 0) {
            sb.append(String.format(Locale.US, "-%02d:%02d", Math.abs(n / 60), Math.abs(n % 60)));
        }
        else {
            sb.append(String.format(Locale.US, "+%02d:%02d", n / 60, n % 60));
        }
        return sb.toString();
    }
    
    private static String getTypeName(final Event.Category category) {
        if (category == null) {
            return null;
        }
        switch (IddUtil$1.$SwitchMap$com$sonymobile$cameracommon$research$parameters$Event$Category[category.ordinal()]) {
            default: {
                return null;
            }
            case 12: {
                return "AUTO_POWEROFF";
            }
            case 11: {
                return "PREDICTIVE_LAUNCH";
            }
            case 10: {
                return "SLOW_MOTION_EVENT";
            }
            case 9: {
                return "LOWBATTERY_ERROR";
            }
            case 8: {
                return "SELFTIMER_CANCEL_EVENT";
            }
            case 7: {
                return "PANORAMA";
            }
            case 6: {
                return "CAMERA_NOT_AVAILABLE";
            }
            case 5: {
                return "THERMAL_ERROR";
            }
            case 3:
            case 4: {
                return "MODE_SELECTOR_EVENT";
            }
            case 2: {
                return "VIDEO_EVENT";
            }
            case 1: {
                return "PHOTO_EVENT";
            }
        }
    }
    
    public static void onCreate() {
        if (CamLog.VERBOSE) {
            CamLog.d("onCreate()");
        }
    }
    
    public static void onDestroy() {
        if (CamLog.VERBOSE) {
            CamLog.d("onDestroy()");
        }
    }
    
    public static void onPause(final boolean b) {
    }
    
    public static void onResume() {
    }
    
    public static void sendEvent(final Event.Category category, final String str, final String str2, final long n) {
        if (getTypeName(category) == null) {
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("sendEvent(): category = ");
            sb.append(category.toString());
            sb.append(", action = ");
            sb.append(str);
            sb.append(", label = ");
            sb.append(str2);
            sb.append(", value = ");
            sb.append(n);
            CamLog.d(sb.toString());
        }
        final JsonStringBuilder jsonStringBuilder = new JsonStringBuilder(getTypeName(category));
        String s;
        if ((s = str) == null) {
            s = "";
        }
        final JsonStringBuilder set = jsonStringBuilder.set("action", s);
        String s2;
        if ((s2 = str2) == null) {
            s2 = "";
        }
        sendJsonData(set.set("label", s2).set("value", Long.toString(n)).build());
    }
    
    public static void sendEventAddonModeChange(final Event.Category category, final String str, final String str2, final String str3) {
        if (getTypeName(category) == null) {
            return;
        }
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("sendEventAddonModeChange(): category = ");
            sb.append(category.toString());
            sb.append(", action = ");
            sb.append(str);
            sb.append(", label = ");
            sb.append(str2);
            sb.append(", method = ");
            sb.append(str3);
            CamLog.d(sb.toString());
        }
        final JsonStringBuilder jsonStringBuilder = new JsonStringBuilder(getTypeName(category));
        String s;
        if ((s = str) == null) {
            s = "";
        }
        final JsonStringBuilder set = jsonStringBuilder.set("action", s);
        String s2;
        if ((s2 = str2) == null) {
            s2 = "";
        }
        final JsonStringBuilder set2 = set.set("label", s2);
        String s3;
        if ((s3 = str3) == null) {
            s3 = "";
        }
        sendJsonData(set2.set("way", s3).build());
    }
    
    public static void sendEventAllSettings(final Event.Category category, final Map<String, String> obj, final Map<String, String> obj2) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("sendEventAllSettings(): category = ");
            sb.append(category.toString());
            sb.append(", env = ");
            sb.append(obj);
            sb.append(", settings = ");
            sb.append(obj2);
            CamLog.d(sb.toString());
        }
        sendJsonData(new JsonStringBuilder(getTypeName(category)).set("environment", obj).set("setting", obj2).build());
    }
    
    public static void sendEventChangedSetting(String str, final String str2, final String str3) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("sendEventChangedSetting(): setting = ");
            sb.append(str);
            sb.append(", before = ");
            sb.append(str2);
            sb.append(", after = ");
            sb.append(str3);
            CamLog.d(sb.toString());
        }
        final JsonStringBuilder jsonStringBuilder = new JsonStringBuilder("CHANGE_SETTING_EVENT");
        String s;
        if ((s = str) == null) {
            s = "";
        }
        final JsonStringBuilder set = jsonStringBuilder.set("setting", s);
        if ((str = str2) == null) {
            str = "";
        }
        final JsonStringBuilder set2 = set.set("before", str);
        if ((str = str3) == null) {
            str = "";
        }
        sendJsonData(set2.set("after", str).build());
    }
    
    public static void sendEventInternalModeChange(String str, final String str2, final String str3) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("sendEventInternalModeChange(): mode :");
            sb.append(str);
            sb.append("  To : ");
            sb.append(str2);
            sb.append(" Way : ");
            sb.append(str3);
            CamLog.d(sb.toString());
        }
        final JsonStringBuilder jsonStringBuilder = new JsonStringBuilder("MODE_CHANGE_EVENT");
        String s;
        if ((s = str) == null) {
            s = "";
        }
        final JsonStringBuilder set = jsonStringBuilder.set("mode", s);
        if ((str = str2) == null) {
            str = "";
        }
        final JsonStringBuilder set2 = set.set("to", str);
        if ((str = str3) == null) {
            str = "";
        }
        sendJsonData(set2.set("way", str).build());
    }
    
    public static void sendExternalCameraAppEvent(final JSONObject jsonObject, final long n) throws JSONException {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("sendExternalCameraAppEvent(): json = ");
            sb.append(jsonObject.toString());
            sb.append(" timestamp = ");
            sb.append(n);
            CamLog.d("IddUtil", sb.toString());
        }
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss");
        final Date date = new Date(n);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(simpleDateFormat.format(date));
        sb2.append("T");
        sb2.append(simpleDateFormat2.format(date));
        sb2.append(getCurrentTimeZone());
        jsonObject.put("timestamp_original_event", (Object)sb2.toString());
        sendJsonData(jsonObject.toString());
    }
    
    private static void sendJsonData(final String s) {
        if (IddUtil.mIsSendingAllowed) {
            setPackageInfo();
            if (checkIddSupported()) {
                try {
                    Idd.addAppDataJSON(IddUtil.mPackageName, IddUtil.mVersionName, IddUtil.mVersionCode, new JSONObject(s));
                }
                catch (final Throwable t) {
                    if (CamLog.VERBOSE) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("sendJsonData(): Could not send event: ");
                        sb.append(t.getMessage());
                        CamLog.w(sb.toString());
                    }
                }
            }
        }
    }
    
    public static void sendPerformanceData(String string, final long n, final boolean b, final String str) {
        final StringBuilder sb = new StringBuilder();
        sb.append("sendPerformanceData(): key = ");
        String string2;
        if (string == null) {
            string2 = "";
        }
        else {
            string2 = string.toString();
        }
        sb.append(string2);
        sb.append(", time(millis) = ");
        sb.append(n);
        sb.append(", isHeated = ");
        sb.append(b);
        final String string3 = sb.toString();
        final JsonStringBuilder jsonStringBuilder = new JsonStringBuilder("PERFORMANCE");
        String s = string;
        if (string == null) {
            s = "";
        }
        final JsonStringBuilder set = jsonStringBuilder.set("target", s).set("time", Long.toString(n));
        if (b) {
            string = "HIGH";
        }
        else {
            string = "NORMAL";
        }
        final JsonStringBuilder set2 = set.set("thermal_status", string);
        string = string3;
        if (!TextUtils.isEmpty((CharSequence)str)) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string3);
            sb2.append(", batteryLevel = ");
            sb2.append(str);
            string = sb2.toString();
            set2.set("battery_level", str);
        }
        if (CamLog.DEBUG) {
            CamLog.d(string);
        }
        sendJsonData(set2.build());
    }
    
    public static void sendWizardEvent(String str, final String str2, final String str3) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("sendWizardEvent(): page = ");
            sb.append(str);
            sb.append(", time = ");
            sb.append(str2);
            sb.append(", reslut = ");
            sb.append(str3);
            CamLog.d(sb.toString());
        }
        final JsonStringBuilder jsonStringBuilder = new JsonStringBuilder("SETUP_WIZARD_EVENT");
        String s;
        if ((s = str) == null) {
            s = "";
        }
        final JsonStringBuilder set = jsonStringBuilder.set("page", s);
        if ((str = str2) == null) {
            str = "";
        }
        final JsonStringBuilder set2 = set.set("time", str);
        if ((str = str3) == null) {
            str = "";
        }
        sendJsonData(set2.set("result", str).build());
    }
    
    public static void setLaunchedBy(final String s) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setLaunchedBy() : launchedBy = ");
            sb.append(s);
            CamLog.d(sb.toString());
        }
        IddUtil.mLaunchedBy = s;
    }
    
    private static void setPackageInfo() {
        if (IddUtil.mContext != null && "".equals(IddUtil.mPackageName)) {
            IddUtil.mPackageName = IddUtil.mContext.getPackageName();
            final PackageManager packageManager = IddUtil.mContext.getPackageManager();
            try {
                IddUtil.mVersionName = packageManager.getPackageInfo(IddUtil.mPackageName, 0).versionName;
                IddUtil.mVersionCode = packageManager.getPackageInfo(IddUtil.mPackageName, 0).versionCode;
            }
            catch (final PackageManager$NameNotFoundException ex) {
                if (CamLog.VERBOSE) {
                    CamLog.w("setPackageInfo(): Could not get version info");
                }
            }
        }
    }
    
    public static void setView(final Screen obj) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("sendView() : screen = ");
            sb.append(obj);
            CamLog.d(sb.toString());
        }
        if (obj == null) {
            return;
        }
        IddUtil.mView = obj.toString();
    }
    
    private static class JsonStringBuilder
    {
        private final Map<String, ValueMap> mMap;
        
        public JsonStringBuilder(@NonNull final String s) {
            (this.mMap = (Map<String, ValueMap>)new ArrayMap()).put("type", new ValueMap(s));
            this.mMap.put("mode", new ValueMap(IddUtil.mView));
            this.mMap.put("launched_by", new ValueMap(IddUtil.mLaunchedBy));
        }
        
        private void write(final JsonWriter jsonWriter, final String s, final ValueMap valueMap) throws IOException {
            final String value = valueMap.getValue();
            if (value == null) {
                jsonWriter.name(s.toLowerCase(Locale.ROOT));
                this.write(jsonWriter, valueMap.getMap());
            }
            else {
                jsonWriter.name(s.toLowerCase(Locale.ROOT)).value(value);
            }
        }
        
        private void write(final JsonWriter jsonWriter, final Map<String, ValueMap> map) throws IOException {
            jsonWriter.beginObject();
            for (final Map.Entry entry : map.entrySet()) {
                this.write(jsonWriter, (String)entry.getKey(), (ValueMap)entry.getValue());
            }
            jsonWriter.endObject();
        }
        
        public String build() {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     3: dup            
            //     4: invokespecial   java/io/StringWriter.<init>:()V
            //     7: astore_2       
            //     8: new             Landroid/util/JsonWriter;
            //    11: dup            
            //    12: aload_2        
            //    13: invokespecial   android/util/JsonWriter.<init>:(Ljava/io/Writer;)V
            //    16: astore_1       
            //    17: aload_1        
            //    18: ldc             " "
            //    20: invokevirtual   android/util/JsonWriter.setIndent:(Ljava/lang/String;)V
            //    23: aload_0        
            //    24: aload_1        
            //    25: aload_0        
            //    26: getfield        com/sonymobile/cameracommon/research/idd/IddUtil$JsonStringBuilder.mMap:Ljava/util/Map;
            //    29: invokespecial   com/sonymobile/cameracommon/research/idd/IddUtil$JsonStringBuilder.write:(Landroid/util/JsonWriter;Ljava/util/Map;)V
            //    32: aload_2        
            //    33: invokevirtual   java/io/StringWriter.toString:()Ljava/lang/String;
            //    36: astore_2       
            //    37: aload_1        
            //    38: invokevirtual   android/util/JsonWriter.close:()V
            //    41: aload_2        
            //    42: areturn        
            //    43: astore_2       
            //    44: aload_1        
            //    45: invokevirtual   android/util/JsonWriter.close:()V
            //    48: aload_2        
            //    49: athrow         
            //    50: astore_2       
            //    51: aload_1        
            //    52: invokevirtual   android/util/JsonWriter.close:()V
            //    55: ldc             ""
            //    57: areturn        
            //    58: astore_1       
            //    59: goto            41
            //    62: astore_1       
            //    63: goto            48
            //    66: astore_1       
            //    67: goto            55
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  17     37     50     58     Ljava/io/IOException;
            //  17     37     43     50     Any
            //  37     41     58     62     Ljava/io/IOException;
            //  44     48     62     66     Ljava/io/IOException;
            //  51     55     66     70     Ljava/io/IOException;
            // 
            // The error that occurred was:
            // 
            // java.lang.IndexOutOfBoundsException: Index 40 out of bounds for length 40
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
        
        public JsonStringBuilder set(final String s, final String s2) {
            this.mMap.put(s, new ValueMap(s2));
            return this;
        }
        
        public JsonStringBuilder set(final String s, final Map<String, String> map) {
            this.mMap.put(s, new ValueMap(map));
            return this;
        }
    }
    
    private static class ValueMap
    {
        private final Map<String, ValueMap> mMap;
        private final String mValue;
        
        public ValueMap(@NonNull final String mValue) {
            this.mMap = null;
            this.mValue = mValue;
        }
        
        public ValueMap(@NonNull final Map<String, String> map) {
            this.mMap = (Map<String, ValueMap>)new ArrayMap();
            this.mValue = null;
            for (final Map.Entry<Object, V> entry : map.entrySet()) {
                this.mMap.put(entry.getKey(), new ValueMap((String)entry.getValue()));
            }
        }
        
        public Map<String, ValueMap> getMap() {
            return this.mMap;
        }
        
        public String getValue() {
            return this.mValue;
        }
    }
}
