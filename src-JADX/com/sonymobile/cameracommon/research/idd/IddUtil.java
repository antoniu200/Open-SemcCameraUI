package com.sonymobile.cameracommon.research.idd;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.JsonWriter;
import com.sonyericsson.android.camera.CameraApplication;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.idd.api.Idd;
import com.sonymobile.cameracommon.research.parameters.Event;
import com.sonymobile.cameracommon.research.parameters.Screen;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class IddUtil {
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
    private static Context mContext = CameraApplication.getContext();
    private static boolean mIsIddSupportAlreadyChecked = false;
    private static boolean mIsIddSupported = false;
    private static boolean mIsSendingAllowed = true;
    private static String mLaunchedBy = "";
    private static String mPackageName = "";
    private static int mVersionCode = 0;
    private static String mVersionName = "";
    private static String mView = "";

    public static void onPause(boolean z) {
    }

    public static void onResume() {
    }

    private IddUtil() {
    }

    public static void onCreate() {
        if (CamLog.VERBOSE) {
            CamLog.d("onCreate()");
        }
    }

    private static void setPackageInfo() {
        if (mContext == null || !"".equals(mPackageName)) {
            return;
        }
        mPackageName = mContext.getPackageName();
        PackageManager packageManager = mContext.getPackageManager();
        try {
            mVersionName = packageManager.getPackageInfo(mPackageName, 0).versionName;
            mVersionCode = packageManager.getPackageInfo(mPackageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException unused) {
            if (CamLog.VERBOSE) {
                CamLog.w("setPackageInfo(): Could not get version info");
            }
        }
    }

    private static boolean checkIddSupported() throws NoSuchMethodException, SecurityException {
        if (mIsIddSupportAlreadyChecked) {
            return mIsIddSupported;
        }
        mIsIddSupported = false;
        try {
            Class.forName(IDD_CLASS_NAME).getMethod(IDD_METHOD_NAME, String.class, String.class, Integer.TYPE, JSONObject.class);
            mIsIddSupported = true;
            if (CamLog.VERBOSE) {
                CamLog.d("Idd.addAppDataJSON is supported");
            }
        } catch (ClassNotFoundException | LinkageError | NoSuchMethodException unused) {
            if (CamLog.VERBOSE) {
                CamLog.w("Idd.addAppDataJSON is not supported");
            }
        }
        mIsIddSupportAlreadyChecked = true;
        return mIsIddSupported;
    }

    public static void onDestroy() {
        if (CamLog.VERBOSE) {
            CamLog.d("onDestroy()");
        }
    }

    public static void setLaunchedBy(String str) {
        if (CamLog.DEBUG) {
            CamLog.d("setLaunchedBy() : launchedBy = " + str);
        }
        mLaunchedBy = str;
    }

    public static void setView(Screen screen) {
        if (CamLog.DEBUG) {
            CamLog.d("sendView() : screen = " + screen);
        }
        if (screen == null) {
            return;
        }
        mView = screen.toString();
    }

    public static void sendEvent(Event.Category category, String str, String str2, long j) {
        if (getTypeName(category) == null) {
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("sendEvent(): category = " + category.toString() + ", action = " + str + ", label = " + str2 + ", value = " + j);
        }
        JsonStringBuilder jsonStringBuilder = new JsonStringBuilder(getTypeName(category));
        if (str == null) {
            str = "";
        }
        JsonStringBuilder jsonStringBuilder2 = jsonStringBuilder.set("action", str);
        if (str2 == null) {
            str2 = "";
        }
        sendJsonData(jsonStringBuilder2.set(KEY_LABEL, str2).set(KEY_VALUE, Long.toString(j)).build());
    }

    private static String getTypeName(Event.Category category) {
        if (category == null) {
            return null;
        }
        switch (category) {
        }
        return null;
    }

    public static void sendPerformanceData(String str, long j, boolean z, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append("sendPerformanceData(): key = ");
        sb.append(str == null ? "" : str.toString());
        sb.append(", time(millis) = ");
        sb.append(j);
        sb.append(", isHeated = ");
        sb.append(z);
        String string = sb.toString();
        JsonStringBuilder jsonStringBuilder = new JsonStringBuilder(TYPE_PERFORMANCE_EVENT);
        if (str == null) {
            str = "";
        }
        JsonStringBuilder jsonStringBuilder2 = jsonStringBuilder.set(KEY_PERFORMANCE_TARGET, str).set("time", Long.toString(j)).set(KEY_PERFORMANCE_THERMAL_STATUS, z ? THERMAL_STATUS_HIGH : THERMAL_STATUS_NORMAL);
        if (!TextUtils.isEmpty(str2)) {
            string = string + ", batteryLevel = " + str2;
            jsonStringBuilder2.set(KEY_PERFORMANCE_BATTERY_LEVEL, str2);
        }
        if (CamLog.DEBUG) {
            CamLog.d(string);
        }
        sendJsonData(jsonStringBuilder2.build());
    }

    public static void sendEventAllSettings(Event.Category category, Map<String, String> map, Map<String, String> map2) {
        if (CamLog.DEBUG) {
            CamLog.d("sendEventAllSettings(): category = " + category.toString() + ", env = " + map + ", settings = " + map2);
        }
        sendJsonData(new JsonStringBuilder(getTypeName(category)).set(KEY_ENVIRONMENT, map).set(KEY_SETTING, map2).build());
    }

    public static void sendEventChangedSetting(String str, String str2, String str3) {
        if (CamLog.DEBUG) {
            CamLog.d("sendEventChangedSetting(): setting = " + str + ", before = " + str2 + ", after = " + str3);
        }
        JsonStringBuilder jsonStringBuilder = new JsonStringBuilder(TYPE_CHANGE_SETTING_EVENT);
        if (str == null) {
            str = "";
        }
        JsonStringBuilder jsonStringBuilder2 = jsonStringBuilder.set(KEY_SETTING, str);
        if (str2 == null) {
            str2 = "";
        }
        JsonStringBuilder jsonStringBuilder3 = jsonStringBuilder2.set(KEY_VALUE_BEFORE, str2);
        if (str3 == null) {
            str3 = "";
        }
        sendJsonData(jsonStringBuilder3.set(KEY_VALUE_AFTER, str3).build());
    }

    public static void sendEventInternalModeChange(String str, String str2, String str3) {
        if (CamLog.DEBUG) {
            CamLog.d("sendEventInternalModeChange(): mode :" + str + "  To : " + str2 + " Way : " + str3);
        }
        JsonStringBuilder jsonStringBuilder = new JsonStringBuilder(TYPE_MODE_CHANGE_EVENT);
        if (str == null) {
            str = "";
        }
        JsonStringBuilder jsonStringBuilder2 = jsonStringBuilder.set(KEY_MODE, str);
        if (str2 == null) {
            str2 = "";
        }
        JsonStringBuilder jsonStringBuilder3 = jsonStringBuilder2.set(KEY_VALUE_TO, str2);
        if (str3 == null) {
            str3 = "";
        }
        sendJsonData(jsonStringBuilder3.set(KEY_VALUE_WAY, str3).build());
    }

    public static void sendEventAddonModeChange(Event.Category category, String str, String str2, String str3) {
        if (getTypeName(category) == null) {
            return;
        }
        if (CamLog.DEBUG) {
            CamLog.d("sendEventAddonModeChange(): category = " + category.toString() + ", action = " + str + ", label = " + str2 + ", method = " + str3);
        }
        JsonStringBuilder jsonStringBuilder = new JsonStringBuilder(getTypeName(category));
        if (str == null) {
            str = "";
        }
        JsonStringBuilder jsonStringBuilder2 = jsonStringBuilder.set("action", str);
        if (str2 == null) {
            str2 = "";
        }
        JsonStringBuilder jsonStringBuilder3 = jsonStringBuilder2.set(KEY_LABEL, str2);
        if (str3 == null) {
            str3 = "";
        }
        sendJsonData(jsonStringBuilder3.set(KEY_VALUE_WAY, str3).build());
    }

    public static void sendWizardEvent(String str, String str2, String str3) {
        if (CamLog.DEBUG) {
            CamLog.d("sendWizardEvent(): page = " + str + ", time = " + str2 + ", reslut = " + str3);
        }
        JsonStringBuilder jsonStringBuilder = new JsonStringBuilder(TYPE_SETUP_WIZARD_EVENT);
        if (str == null) {
            str = "";
        }
        JsonStringBuilder jsonStringBuilder2 = jsonStringBuilder.set(KEY_VALUE_PAGE, str);
        if (str2 == null) {
            str2 = "";
        }
        JsonStringBuilder jsonStringBuilder3 = jsonStringBuilder2.set("time", str2);
        if (str3 == null) {
            str3 = "";
        }
        sendJsonData(jsonStringBuilder3.set(KEY_VALUE_RESULT, str3).build());
    }

    public static void sendExternalCameraAppEvent(JSONObject jSONObject, long j) throws JSONException {
        if (CamLog.DEBUG) {
            CamLog.d(TAG, "sendExternalCameraAppEvent(): json = " + jSONObject.toString() + " timestamp = " + j);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(j);
        jSONObject.put(KEY_TIMESTAMP_ORIGINAL_EVENT, simpleDateFormat.format(date) + "T" + simpleDateFormat2.format(date) + getCurrentTimeZone());
        sendJsonData(jSONObject.toString());
    }

    private static String getCurrentTimeZone() {
        int rawOffset = TimeZone.getDefault().getRawOffset() / 60000;
        StringBuilder sb = new StringBuilder(6);
        if (rawOffset < 0) {
            sb.append(String.format(Locale.US, "-%02d:%02d", Integer.valueOf(Math.abs(rawOffset / 60)), Integer.valueOf(Math.abs(rawOffset % 60))));
        } else {
            sb.append(String.format(Locale.US, "+%02d:%02d", Integer.valueOf(rawOffset / 60), Integer.valueOf(rawOffset % 60)));
        }
        return sb.toString();
    }

    private static void sendJsonData(String str) {
        if (mIsSendingAllowed) {
            setPackageInfo();
            if (checkIddSupported()) {
                try {
                    Idd.addAppDataJSON(mPackageName, mVersionName, mVersionCode, new JSONObject(str));
                } catch (Throwable th) {
                    if (CamLog.VERBOSE) {
                        CamLog.w("sendJsonData(): Could not send event: " + th.getMessage());
                    }
                }
            }
        }
    }

    private static class ValueMap {
        private final Map<String, ValueMap> mMap;
        private final String mValue;

        public ValueMap(@NonNull String str) {
            this.mMap = null;
            this.mValue = str;
        }

        public ValueMap(@NonNull Map<String, String> map) {
            this.mMap = new ArrayMap();
            this.mValue = null;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                this.mMap.put(entry.getKey(), new ValueMap(entry.getValue()));
            }
        }

        public String getValue() {
            return this.mValue;
        }

        public Map<String, ValueMap> getMap() {
            return this.mMap;
        }
    }

    private static class JsonStringBuilder {
        private final Map<String, ValueMap> mMap = new ArrayMap();

        public JsonStringBuilder(@NonNull String str) {
            this.mMap.put(IddUtil.KEY_TYPE, new ValueMap(str));
            this.mMap.put(IddUtil.KEY_MODE, new ValueMap(IddUtil.mView));
            this.mMap.put(IddUtil.KEY_LAUNCHEDBY, new ValueMap(IddUtil.mLaunchedBy));
        }

        public JsonStringBuilder set(String str, String str2) {
            this.mMap.put(str, new ValueMap(str2));
            return this;
        }

        public JsonStringBuilder set(String str, Map<String, String> map) {
            this.mMap.put(str, new ValueMap(map));
            return this;
        }

        public String build() throws IOException {
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            try {
                try {
                    jsonWriter.setIndent(" ");
                    write(jsonWriter, this.mMap);
                    String string = stringWriter.toString();
                    try {
                        jsonWriter.close();
                    } catch (IOException unused) {
                    }
                    return string;
                } catch (IOException unused2) {
                    jsonWriter.close();
                    return "";
                } catch (Throwable th) {
                    try {
                        jsonWriter.close();
                    } catch (IOException unused3) {
                    }
                    throw th;
                }
            } catch (IOException unused4) {
                return "";
            }
        }

        private void write(JsonWriter jsonWriter, Map<String, ValueMap> map) throws IOException {
            jsonWriter.beginObject();
            for (Map.Entry<String, ValueMap> entry : map.entrySet()) {
                write(jsonWriter, entry.getKey(), entry.getValue());
            }
            jsonWriter.endObject();
        }

        private void write(JsonWriter jsonWriter, String str, ValueMap valueMap) throws IOException {
            String value = valueMap.getValue();
            if (value == null) {
                jsonWriter.name(str.toLowerCase(Locale.ROOT));
                write(jsonWriter, valueMap.getMap());
            } else {
                jsonWriter.name(str.toLowerCase(Locale.ROOT)).value(value);
            }
        }
    }
}
