package com.sonyericsson.android.camera.parameter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import com.sonyericsson.android.camera.LaunchCondition;
import com.sonyericsson.android.camera.configuration.IntentReader;
import com.sonyericsson.android.camera.configuration.ParameterCategory;
import com.sonyericsson.android.camera.configuration.SharedPreferencesConstants;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.Metering;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.configuration.parameters.TouchIntention;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.view.setting.SettingUi;
import com.sonyericsson.cameracommon.storage.Storage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
class CameraSettingsMigrator {
    private static final String TAG = "cameramigrator";
    private static final boolean DEBUG_ENABLED = Log.isLoggable(TAG, 3);
    private static Map<CapturingMode, List<UserSettingKey>> SETTINGS = Collections.unmodifiableMap(new HashMap<CapturingMode, List<UserSettingKey>>() { // from class: com.sonyericsson.android.camera.parameter.CameraSettingsMigrator.1
        {
            put(CapturingMode.NORMAL, Arrays.asList(UserSettingKey.RESOLUTION, UserSettingKey.WHITE_BALANCE, UserSettingKey.EV, UserSettingKey.SHUTTER_SPEED, UserSettingKey.FOCUS_RANGE, UserSettingKey.SELF_TIMER, UserSettingKey.FUSION_MODE, UserSettingKey.ISO, UserSettingKey.HDR, UserSettingKey.TOUCH_INTENTION, UserSettingKey.OBJECT_TRACKING, UserSettingKey.METERING, UserSettingKey.SHUTTER_TRIGGER));
            put(CapturingMode.SCENE_RECOGNITION, Arrays.asList(UserSettingKey.RESOLUTION, UserSettingKey.SELF_TIMER, UserSettingKey.OBJECT_TRACKING, UserSettingKey.SHUTTER_TRIGGER, UserSettingKey.TOUCH_INTENTION, UserSettingKey.PREDICTIVE_CAPTURE, UserSettingKey.FUSION_MODE));
            put(CapturingMode.VIDEO, Arrays.asList(UserSettingKey.VIDEO_HDR, UserSettingKey.FUSION_MODE, UserSettingKey.VIDEO_SIZE, UserSettingKey.OBJECT_TRACKING, UserSettingKey.VIDEO_SHUTTER_TRIGGER, UserSettingKey.VIDEO_STABILIZER, UserSettingKey.VIDEO_CODEC));
            put(CapturingMode.FRONT_PHOTO, Arrays.asList(UserSettingKey.RESOLUTION, UserSettingKey.SELF_TIMER, UserSettingKey.WHITE_BALANCE, UserSettingKey.EV, UserSettingKey.HDR, UserSettingKey.SHUTTER_TRIGGER, UserSettingKey.SOFT_SKIN));
            put(CapturingMode.SUPERIOR_FRONT, Arrays.asList(UserSettingKey.RESOLUTION, UserSettingKey.SELF_TIMER, UserSettingKey.SOFT_SKIN, UserSettingKey.SHUTTER_TRIGGER));
            put(CapturingMode.FRONT_VIDEO, Arrays.asList(UserSettingKey.VIDEO_SIZE, UserSettingKey.VIDEO_SHUTTER_TRIGGER, UserSettingKey.VIDEO_STABILIZER));
            put(CapturingMode.SLOW_MOTION, Arrays.asList(UserSettingKey.VIDEO_SIZE, UserSettingKey.SLOW_MOTION));
        }
    });
    private static List<UserSettingKey> COMMON_SETTINGS = Collections.unmodifiableList(Arrays.asList(UserSettingKey.FLASH, UserSettingKey.PHOTO_LIGHT, UserSettingKey.DISPLAY_FLASH, UserSettingKey.FRONT_ANGLE, UserSettingKey.GEO_TAG, UserSettingKey.CAMERA_KEY, UserSettingKey.TOUCH_CAPTURE, UserSettingKey.GRID_LINE, UserSettingKey.SIDE_SENSE, UserSettingKey.AUTO_REVIEW, UserSettingKey.DISTORTION_CORRECTION, UserSettingKey.VOLUME_KEY, UserSettingKey.SHUTTER_SOUND, UserSettingKey.DESTINATION_TO_SAVE, UserSettingKey.PREDICTIVE_LAUNCH, UserSettingKey.FAST_CAPTURE));

    CameraSettingsMigrator() {
    }

    @SuppressLint({"ApplySharedPref"})
    static void migrate(Context context, Storage storage, UserSettingsLoaderImpl userSettingsLoaderImpl) {
        if (DEBUG_ENABLED) {
            Log.d(TAG, "+ camera settings migration");
        }
        if (DEBUG_ENABLED) {
            Log.d(TAG, "+ backup shared preferences");
        }
        backupSharedPrefs(context);
        if (DEBUG_ENABLED) {
            Log.d(TAG, "- backup shared preferences");
        }
        if (DEBUG_ENABLED) {
            Log.d(TAG, "+ cache all camera settings");
        }
        Map<String, ?> all = getSharedPrefs(context, true).getAll();
        if (DEBUG_ENABLED) {
            Log.d(TAG, "- cache all camera settings");
        }
        if (all.isEmpty()) {
            if (DEBUG_ENABLED) {
                Log.w(TAG, "ignore migration since legacy camera settings is empty");
                return;
            }
            return;
        }
        if (DEBUG_ENABLED) {
            Log.d(TAG, "+ cleanup shared preferences");
        }
        SharedPreferences sharedPrefs = getSharedPrefs(context, false);
        sharedPrefs.edit().clear().commit();
        if (DEBUG_ENABLED) {
            Log.d(TAG, "- cleanup shared preferences");
        }
        if (context.checkSelfPermission("android.permission.CAMERA") != 0) {
            if (DEBUG_ENABLED) {
                Log.w(TAG, "ignore migration since not have camera permission");
                return;
            }
            return;
        }
        if (DEBUG_ENABLED) {
            Log.d(TAG, "+ setup camera parameter manager");
        }
        IntentReader.VideoQualityConfigurations videoQualityConfigurations = new IntentReader().getVideoQualityConfigurations(new Intent("android.intent.action.MAIN").addCategory("android.intent.category.LAUNCHER"));
        UserSettingManager userSettingManager = new UserSettingManager(context, storage);
        userSettingManager.prepare(context, LaunchCondition.OneShotMode.NONE, null, videoQualityConfigurations, null);
        Map<CapturingMode, Parameters> mapLoadMigrationData = loadMigrationData(CapturingMode.NORMAL, userSettingManager, userSettingsLoaderImpl);
        if (DEBUG_ENABLED) {
            Log.d(TAG, "- setup camera parameter manager");
        }
        HashMap map = new HashMap();
        HashMap map2 = new HashMap();
        ArrayList arrayList = new ArrayList();
        if (DEBUG_ENABLED) {
            Log.d(TAG, "+ migrate capturing mode settings");
        }
        for (CapturingMode capturingMode : CapturingMode.getValidOptions()) {
            if (DEBUG_ENABLED) {
                Log.d(TAG, "+ migrate " + capturingMode.toString().toLowerCase() + " settings");
            }
            userSettingManager.changeCapturingMode(capturingMode);
            userSettingManager.applyCapturingMode();
            String strCreateParameterKeyPrefixForCapturingMode = createParameterKeyPrefixForCapturingMode(capturingMode);
            if (DEBUG_ENABLED) {
                for (Map.Entry<UserSettingKey, UserSettingValueHolder<?>> entry : userSettingManager.getParameters().getHolder().entrySet()) {
                    map2.put(strCreateParameterKeyPrefixForCapturingMode + entry.getKey(), entry.getValue().toString());
                }
            }
            ArrayList<UserSettingKey> arrayList2 = new ArrayList();
            arrayList2.addAll(SETTINGS.get(capturingMode));
            arrayList2.addAll(COMMON_SETTINGS);
            for (UserSettingKey userSettingKey : arrayList2) {
                String str = strCreateParameterKeyPrefixForCapturingMode + userSettingKey;
                String string = Objects.toString(all.get(str), UserSettingValueHolder.NO_VALUE);
                if (UserSettingValueHolder.NO_VALUE.equals(string)) {
                    if (DEBUG_ENABLED) {
                        map.put(str, "ignore-null-legacy-value");
                    }
                } else {
                    UserSettingValue userSettingValueConvertUnsupportedValue = parse(string).get();
                    if (userSettingValueConvertUnsupportedValue == null) {
                        if (DEBUG_ENABLED) {
                            map.put(str, "ignore-null-current-value");
                        }
                    } else if (!SettingUi.isSelectableValues(userSettingKey, userSettingManager.getParameters(), userSettingValueConvertUnsupportedValue)) {
                        if (DEBUG_ENABLED) {
                            map.put(str, "ignore-unselectable-current-value");
                        }
                    } else if (!isSupported(userSettingManager, userSettingValueConvertUnsupportedValue) && (userSettingValueConvertUnsupportedValue = convertUnsupportedValue(userSettingManager, userSettingValueConvertUnsupportedValue, capturingMode)) == null) {
                        if (DEBUG_ENABLED) {
                            map.put(str, "ignore-unsupported-current-value");
                        }
                    } else {
                        if (!arrayList.contains(userSettingValueConvertUnsupportedValue.getKey())) {
                            arrayList.add(userSettingValueConvertUnsupportedValue.getKey());
                        }
                        if (capturingMode == CapturingMode.NORMAL && userSettingValueConvertUnsupportedValue == ObjectTracking.ON) {
                            userSettingManager.set(TouchIntention.OBJECT_TRACKING);
                        }
                        userSettingManager.set(userSettingValueConvertUnsupportedValue);
                        if (DEBUG_ENABLED) {
                            map.put(str, "apply-selectable-supported-current-value");
                        }
                    }
                }
            }
            if (DEBUG_ENABLED) {
                Log.d(TAG, "- migrate " + capturingMode.toString().toLowerCase() + " settings");
            }
        }
        if (DEBUG_ENABLED) {
            Log.d(TAG, "- migrate capturing mode settings");
        }
        if (DEBUG_ENABLED) {
            Log.d(TAG, "+ release camera parameter manager");
        }
        userSettingsLoaderImpl.saveMigrateParameters(mapLoadMigrationData, CapturingMode.SCENE_RECOGNITION);
        userSettingManager.release();
        if (DEBUG_ENABLED) {
            Log.d(TAG, "- release camera parameter manager");
        }
        if (DEBUG_ENABLED) {
            Log.d(TAG, "+ migrate non-capturing mode settings");
        }
        if (!migrateOtherSettingsToSharedPrefs(all, sharedPrefs) && DEBUG_ENABLED) {
            Log.d(TAG, "- failed to write non-capturing mode settings");
        }
        if (DEBUG_ENABLED) {
            Log.d(TAG, "- migrate non-capturing mode settings");
        }
        if (DEBUG_ENABLED) {
            Log.d(TAG, "+ generate camera settings migration report");
            createMigrationReport(all, map2, Collections.unmodifiableMap(sharedPrefs.getAll()), map);
            Log.d(TAG, "- generate camera settings migration report");
        }
        if (DEBUG_ENABLED) {
            Log.d(TAG, "- camera settings migration");
        }
    }

    private static Map<CapturingMode, Parameters> loadMigrationData(CapturingMode capturingMode, UserSettingManager userSettingManager, UserSettingsLoaderImpl userSettingsLoaderImpl) {
        if (DEBUG_ENABLED) {
            Log.d(TAG, "loadMigrationDataSync() E");
        }
        Map<CapturingMode, Parameters> mapLoadMigrateParameters = userSettingsLoaderImpl.loadMigrateParameters();
        userSettingManager.replaceParameterEntries(mapLoadMigrateParameters);
        for (Parameters parameters : mapLoadMigrateParameters.values()) {
            for (UserSettingKey userSettingKey : UserSettingKey.values()) {
                if (!userSettingManager.isNeededToLoad(userSettingKey, LaunchCondition.OneShotMode.NONE)) {
                    parameters.mHolders.remove(userSettingKey);
                }
            }
        }
        ArrayList arrayList = new ArrayList();
        for (Parameters parameters2 : mapLoadMigrateParameters.values()) {
            parameters2.updatePhotoLight();
            arrayList.add(parameters2);
        }
        userSettingManager.setDefaultToNonExistentResolution(arrayList);
        userSettingManager.setDefaultToNonExistentVideoSize(arrayList);
        userSettingManager.setDefaultToNonExistentVideoShutterTrigger(arrayList);
        userSettingManager.changeCapturingMode(capturingMode);
        for (Parameters parameters3 : mapLoadMigrateParameters.values()) {
            if (parameters3.capturingMode.getType() == 2) {
                userSettingManager.setupVideoOption(mapLoadMigrateParameters.get(parameters3.capturingMode));
            }
        }
        Iterator<Parameters> it = mapLoadMigrateParameters.values().iterator();
        while (it.hasNext()) {
            it.next().commit();
        }
        if (DEBUG_ENABLED) {
            Log.d(TAG, "loadMigrationDataSync() X");
        }
        return mapLoadMigrateParameters;
    }

    private static String createParameterKeyPrefixForCapturingMode(CapturingMode capturingMode) {
        return ParameterCategory.CAPTURING_MODE + '_' + capturingMode + '_' + SharedPreferencesConstants.KEY_PARAMS;
    }

    private static SharedPreferences getSharedPrefs(Context context, boolean z) {
        String str = SharedPreferencesConstants.CAMERA_SHARED_PREFERENCES_NAME;
        if (z) {
            str = SharedPreferencesConstants.LEGACY_CAMERA_SHARED_PREFERENCES_NAME;
        }
        return context.getSharedPreferences(str, 0);
    }

    private static File getSharedPrefsFile(Context context, boolean z) {
        File file = new File(context.getDataDir(), "shared_prefs");
        String str = SharedPreferencesConstants.CAMERA_SHARED_PREFERENCES_NAME;
        if (z) {
            str = SharedPreferencesConstants.LEGACY_CAMERA_SHARED_PREFERENCES_NAME;
        }
        return new File(file, str + SharedPreferencesConstants.SHARED_PREFERENCES_FILE_EXTENSION);
    }

    private static void backupSharedPrefs(Context context) {
        final File sharedPrefsFile = getSharedPrefsFile(context, false);
        final File sharedPrefsFile2 = getSharedPrefsFile(context, true);
        final byte[] array = new byte[4096];
        try {
            final FileInputStream fileInputStream = new FileInputStream(sharedPrefsFile);
            final Context context2 = context = null;
            try {
                context = context2;
                final FileOutputStream fileOutputStream = new FileOutputStream(sharedPrefsFile2);
                Throwable t = null;
                try {
                    while (true) {
                        final int read = fileInputStream.read(array);
                        if (read < 0) {
                            break;
                        }
                        fileOutputStream.write(array, 0, read);
                    }
                    if (fileOutputStream != null) {
                        context = context2;
                        fileOutputStream.close();
                    }
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    return;
                }
                catch (final Throwable t) {
                    try {
                        throw t;
                    }
                    finally {}
                }
                finally {
                    t = null;
                }
                if (fileOutputStream != null) {
                    if (t != null) {
                        context = context2;
                        try {
                            fileOutputStream.close();
                        }
                        catch (final Throwable exception) {
                            context = context2;
                            t.addSuppressed(exception);
                        }
                    }
                    else {
                        context = context2;
                        fileOutputStream.close();
                    }
                }
                context = context2;
                throw;
            }
            catch (final Throwable t3) {}
            finally {
                if (fileInputStream != null) {
                    if (context != null) {
                        try {
                            fileInputStream.close();
                        }
                        catch (final Throwable exception2) {
                            ((Throwable)context).addSuppressed(exception2);
                        }
                    }
                    else {
                        fileInputStream.close();
                    }
                }
            }
        }
        catch (final IOException obj) {
            final StringBuilder sb = new StringBuilder();
            sb.append("backup shared preferences failed: ");
            sb.append(obj);
            Log.d("cameramigrator", sb.toString());
        }
        catch (final FileNotFoundException obj2) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("backup shared preferences failed: ");
            sb2.append(obj2);
            Log.e("cameramigrator", sb2.toString());
        }
    }

    private static boolean migrateOtherSettingsToSharedPrefs(Map<String, ?> map, SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String key = entry.getKey();
            String string = Objects.toString(entry.getValue(), UserSettingValueHolder.NO_VALUE);
            if (!UserSettingValueHolder.NO_VALUE.equals(string) && !key.startsWith("CAPTURING_MODE_")) {
                char c = 65535;
                if (key.hashCode() == 2111842220 && key.equals(SharedPreferencesConstants.KEY_LAST_MODE)) {
                    c = 0;
                }
                if (c == 0) {
                    editorEdit.putString(key, string);
                }
            }
        }
        return editorEdit.commit();
    }

    private static boolean isSupported(UserSettings userSettings, UserSettingValue userSettingValue) {
        UserSettingValue[] options = userSettings.getOptions(userSettingValue.getKey());
        if (options == null || options.length < 1) {
            return false;
        }
        return Arrays.asList(options).contains(userSettingValue);
    }

    private static UserSettingValueHolder<UserSettingValue> parse(String str) {
        UserSettingValueHolder<UserSettingValue> userSettingValueHolder = new UserSettingValueHolder<>(null);
        userSettingValueHolder.parseValueString(str);
        return userSettingValueHolder;
    }

    private static void createMigrationReport(Map<String, ?> map, Map<String, ?> map2, Map<String, ?> map3, Map<String, String> map4) throws IOException {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(Environment.getExternalStorageDirectory(), "migration_report.xls")));
            Throwable th = null;
            try {
                try {
                    bufferedWriter.write("KEY\tLEGACY_VALUE\tDEFAULT_VALUE\tFINAL_VALUE\tSTATUS");
                    bufferedWriter.newLine();
                    HashSet hashSet = new HashSet();
                    hashSet.addAll(map.keySet());
                    hashSet.addAll(map2.keySet());
                    hashSet.addAll(map3.keySet());
                    ArrayList<String> arrayList = new ArrayList(hashSet);
                    Collections.sort(arrayList);
                    for (String str : arrayList) {
                        String string = Objects.toString(map.get(str), UserSettingValueHolder.NO_VALUE);
                        String string2 = Objects.toString(map2.get(str), UserSettingValueHolder.NO_VALUE);
                        String string3 = Objects.toString(map3.get(str), UserSettingValueHolder.NO_VALUE);
                        String string4 = Objects.toString(map4.get(str), UserSettingValueHolder.NO_VALUE);
                        if (str.startsWith("CAPTURING_MODE_")) {
                            string = parse(string).toString();
                            string3 = parse(string3).toString();
                        }
                        bufferedWriter.write(String.format("%s\t%s\t%s\t%s\t%s", str, string, string2, string3, string4));
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.flush();
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            } finally {
            }
        } catch (IOException e) {
            Log.e(TAG, "create migration report failed: " + e);
        }
    }

    /* renamed from: com.sonyericsson.android.camera.parameter.CameraSettingsMigrator$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey = new int[UserSettingKey.values().length];

        static {
            try {
                $SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[UserSettingKey.METERING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    private static UserSettingValue convertUnsupportedValue(UserSettingManager userSettingManager, UserSettingValue userSettingValue, CapturingMode capturingMode) throws IllegalArgumentException {
        if (AnonymousClass2.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingValue.getKey().ordinal()] != 1 || capturingMode != CapturingMode.NORMAL || userSettingValue != Metering.TOUCH) {
            return null;
        }
        userSettingManager.set(TouchIntention.FOCUS_AND_EXPOSURE);
        return Metering.getDefaultValue(capturingMode);
    }
}
