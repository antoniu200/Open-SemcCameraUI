// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.parameter;

import android.content.SharedPreferences$Editor;
import android.annotation.SuppressLint;
import com.sonyericsson.android.camera.configuration.parameters.ObjectTracking;
import com.sonyericsson.android.camera.view.setting.SettingUi;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValueHolder;
import com.sonyericsson.android.camera.setting.ExtraSettings;
import android.net.Uri;
import android.content.Intent;
import com.sonyericsson.android.camera.configuration.IntentReader;
import com.sonyericsson.cameracommon.storage.Storage;
import com.sonyericsson.android.camera.LaunchCondition;
import com.sonyericsson.android.camera.setting.UserSettings;
import android.content.SharedPreferences;
import com.sonyericsson.android.camera.configuration.ParameterCategory;
import java.util.Iterator;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import android.os.Environment;
import com.sonyericsson.android.camera.configuration.parameters.TouchIntention;
import com.sonyericsson.android.camera.configuration.parameters.Metering;
import com.sonyericsson.android.camera.configuration.parameters.UserSettingValue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import android.content.Context;
import java.util.Collections;
import java.util.Arrays;
import java.util.HashMap;
import android.util.Log;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import java.util.Map;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import java.util.List;

class CameraSettingsMigrator
{
    private static List<UserSettingKey> COMMON_SETTINGS;
    private static final boolean DEBUG_ENABLED;
    private static Map<CapturingMode, List<UserSettingKey>> SETTINGS;
    private static final String TAG = "cameramigrator";
    
    static {
        DEBUG_ENABLED = Log.isLoggable("cameramigrator", 3);
        CameraSettingsMigrator.SETTINGS = Collections.unmodifiableMap((Map<? extends CapturingMode, ? extends List<UserSettingKey>>)new HashMap<CapturingMode, List<UserSettingKey>>() {
            {
                this.put(CapturingMode.NORMAL, Arrays.asList(UserSettingKey.RESOLUTION, UserSettingKey.WHITE_BALANCE, UserSettingKey.EV, UserSettingKey.SHUTTER_SPEED, UserSettingKey.FOCUS_RANGE, UserSettingKey.SELF_TIMER, UserSettingKey.FUSION_MODE, UserSettingKey.ISO, UserSettingKey.HDR, UserSettingKey.TOUCH_INTENTION, UserSettingKey.OBJECT_TRACKING, UserSettingKey.METERING, UserSettingKey.SHUTTER_TRIGGER));
                this.put(CapturingMode.SCENE_RECOGNITION, Arrays.asList(UserSettingKey.RESOLUTION, UserSettingKey.SELF_TIMER, UserSettingKey.OBJECT_TRACKING, UserSettingKey.SHUTTER_TRIGGER, UserSettingKey.TOUCH_INTENTION, UserSettingKey.PREDICTIVE_CAPTURE, UserSettingKey.FUSION_MODE));
                this.put(CapturingMode.VIDEO, Arrays.asList(UserSettingKey.VIDEO_HDR, UserSettingKey.FUSION_MODE, UserSettingKey.VIDEO_SIZE, UserSettingKey.OBJECT_TRACKING, UserSettingKey.VIDEO_SHUTTER_TRIGGER, UserSettingKey.VIDEO_STABILIZER, UserSettingKey.VIDEO_CODEC));
                this.put(CapturingMode.FRONT_PHOTO, Arrays.asList(UserSettingKey.RESOLUTION, UserSettingKey.SELF_TIMER, UserSettingKey.WHITE_BALANCE, UserSettingKey.EV, UserSettingKey.HDR, UserSettingKey.SHUTTER_TRIGGER, UserSettingKey.SOFT_SKIN));
                this.put(CapturingMode.SUPERIOR_FRONT, Arrays.asList(UserSettingKey.RESOLUTION, UserSettingKey.SELF_TIMER, UserSettingKey.SOFT_SKIN, UserSettingKey.SHUTTER_TRIGGER));
                this.put(CapturingMode.FRONT_VIDEO, Arrays.asList(UserSettingKey.VIDEO_SIZE, UserSettingKey.VIDEO_SHUTTER_TRIGGER, UserSettingKey.VIDEO_STABILIZER));
                this.put(CapturingMode.SLOW_MOTION, Arrays.asList(UserSettingKey.VIDEO_SIZE, UserSettingKey.SLOW_MOTION));
            }
        });
        CameraSettingsMigrator.COMMON_SETTINGS = Collections.unmodifiableList((List<? extends UserSettingKey>)Arrays.asList(UserSettingKey.FLASH, UserSettingKey.PHOTO_LIGHT, UserSettingKey.DISPLAY_FLASH, UserSettingKey.FRONT_ANGLE, UserSettingKey.GEO_TAG, UserSettingKey.CAMERA_KEY, UserSettingKey.TOUCH_CAPTURE, UserSettingKey.GRID_LINE, UserSettingKey.SIDE_SENSE, UserSettingKey.AUTO_REVIEW, UserSettingKey.DISTORTION_CORRECTION, UserSettingKey.VOLUME_KEY, UserSettingKey.SHUTTER_SOUND, UserSettingKey.DESTINATION_TO_SAVE, UserSettingKey.PREDICTIVE_LAUNCH, UserSettingKey.FAST_CAPTURE));
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
    
    private static UserSettingValue convertUnsupportedValue(final UserSettingManager userSettingManager, final UserSettingValue userSettingValue, final CapturingMode capturingMode) {
        if (CameraSettingsMigrator$2.$SwitchMap$com$sonyericsson$android$camera$configuration$UserSettingKey[userSettingValue.getKey().ordinal()] == 1) {
            if (capturingMode == CapturingMode.NORMAL && userSettingValue == Metering.TOUCH) {
                userSettingManager.set(TouchIntention.FOCUS_AND_EXPOSURE);
                return Metering.getDefaultValue(capturingMode);
            }
        }
        return null;
    }
    
    private static void createMigrationReport(final Map<String, ?> map, final Map<String, ?> map2, final Map<String, ?> map3, final Map<String, String> map4) {
        final File file = new File(Environment.getExternalStorageDirectory(), "migration_report.xls");
        try {
            final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            Object o2;
            final Object o = o2 = null;
            try {
                try {
                    bufferedWriter.write("KEY\tLEGACY_VALUE\tDEFAULT_VALUE\tFINAL_VALUE\tSTATUS");
                    o2 = o;
                    bufferedWriter.newLine();
                    o2 = o;
                    o2 = o;
                    final HashSet c = new HashSet();
                    o2 = o;
                    c.addAll(map.keySet());
                    o2 = o;
                    c.addAll(map2.keySet());
                    o2 = o;
                    c.addAll(map3.keySet());
                    o2 = o;
                    o2 = o;
                    final ArrayList list = new ArrayList<Comparable>(c);
                    o2 = o;
                    Collections.sort((List<Comparable>)list);
                    o2 = o;
                    final Iterator iterator = list.iterator();
                    while (true) {
                        o2 = o;
                        if (!iterator.hasNext()) {
                            break;
                        }
                        o2 = o;
                        final String s = (String)iterator.next();
                        o2 = o;
                        final String string = Objects.toString(map.get(s), "NO_VALUE");
                        o2 = o;
                        final String string2 = Objects.toString(map2.get(s), "NO_VALUE");
                        o2 = o;
                        final String string3 = Objects.toString(map3.get(s), "NO_VALUE");
                        o2 = o;
                        final String string4 = Objects.toString(map4.get(s), "NO_VALUE");
                        String string5 = string;
                        String string6 = string3;
                        o2 = o;
                        if (s.startsWith("CAPTURING_MODE_")) {
                            o2 = o;
                            string5 = parse(string).toString();
                            o2 = o;
                            string6 = parse(string3).toString();
                        }
                        o2 = o;
                        bufferedWriter.write(String.format("%s\t%s\t%s\t%s\t%s", s, string5, string2, string6, string4));
                        o2 = o;
                        bufferedWriter.newLine();
                    }
                    o2 = o;
                    bufferedWriter.flush();
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                    return;
                }
                finally {
                    if (bufferedWriter != null) {
                        if (o2 != null) {
                            final BufferedWriter bufferedWriter2 = bufferedWriter;
                            bufferedWriter2.close();
                        }
                        else {
                            bufferedWriter.close();
                        }
                    }
                }
            }
            catch (final Throwable t) {}
            try {
                final BufferedWriter bufferedWriter2 = bufferedWriter;
                bufferedWriter2.close();
            }
            catch (final Throwable t2) {}
        }
        catch (final IOException obj) {
            final StringBuilder sb = new StringBuilder();
            sb.append("create migration report failed: ");
            sb.append(obj);
            Log.e("cameramigrator", sb.toString());
        }
    }
    
    private static String createParameterKeyPrefixForCapturingMode(final CapturingMode obj) {
        final StringBuilder sb = new StringBuilder();
        sb.append(ParameterCategory.CAPTURING_MODE);
        sb.append('_');
        sb.append(obj);
        sb.append('_');
        sb.append("PARAMS_");
        return sb.toString();
    }
    
    private static SharedPreferences getSharedPrefs(final Context context, final boolean b) {
        String s = "com.sonyericsson.android.camera.shared_preferences";
        if (b) {
            s = "com.sonyericsson.android.camera.shared_preferences_legacy";
        }
        return context.getSharedPreferences(s, 0);
    }
    
    private static File getSharedPrefsFile(final Context context, final boolean b) {
        final File parent = new File(context.getDataDir(), "shared_prefs");
        String str = "com.sonyericsson.android.camera.shared_preferences";
        if (b) {
            str = "com.sonyericsson.android.camera.shared_preferences_legacy";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(".xml");
        return new File(parent, sb.toString());
    }
    
    private static boolean isSupported(final UserSettings userSettings, final UserSettingValue userSettingValue) {
        final UserSettingValue[] options = userSettings.getOptions(userSettingValue.getKey());
        return options != null && options.length >= 1 && Arrays.asList(options).contains(userSettingValue);
    }
    
    private static Map<CapturingMode, Parameters> loadMigrationData(final CapturingMode capturingMode, final UserSettingManager userSettingManager, final UserSettingsLoaderImpl userSettingsLoaderImpl) {
        if (CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "loadMigrationDataSync() E");
        }
        final Map<CapturingMode, Parameters> loadMigrateParameters = userSettingsLoaderImpl.loadMigrateParameters();
        userSettingManager.replaceParameterEntries(loadMigrateParameters);
        for (final Parameters parameters : loadMigrateParameters.values()) {
            for (final UserSettingKey userSettingKey : UserSettingKey.values()) {
                if (!userSettingManager.isNeededToLoad(userSettingKey, LaunchCondition.OneShotMode.NONE)) {
                    parameters.mHolders.remove(userSettingKey);
                }
            }
        }
        final ArrayList defaultToNonExistentVideoShutterTrigger = new ArrayList();
        for (final Parameters parameters2 : loadMigrateParameters.values()) {
            parameters2.updatePhotoLight();
            defaultToNonExistentVideoShutterTrigger.add(parameters2);
        }
        userSettingManager.setDefaultToNonExistentResolution(defaultToNonExistentVideoShutterTrigger);
        userSettingManager.setDefaultToNonExistentVideoSize(defaultToNonExistentVideoShutterTrigger);
        userSettingManager.setDefaultToNonExistentVideoShutterTrigger(defaultToNonExistentVideoShutterTrigger);
        userSettingManager.changeCapturingMode(capturingMode);
        for (final Parameters parameters3 : loadMigrateParameters.values()) {
            if (parameters3.capturingMode.getType() == 2) {
                userSettingManager.setupVideoOption(loadMigrateParameters.get(parameters3.capturingMode));
            }
        }
        final Iterator<Parameters> iterator4 = loadMigrateParameters.values().iterator();
        while (iterator4.hasNext()) {
            iterator4.next().commit();
        }
        if (CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "loadMigrationDataSync() X");
        }
        return loadMigrateParameters;
    }
    
    @SuppressLint({ "ApplySharedPref" })
    static void migrate(final Context context, final Storage storage, final UserSettingsLoaderImpl userSettingsLoaderImpl) {
        if (CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "+ camera settings migration");
        }
        if (CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "+ backup shared preferences");
        }
        backupSharedPrefs(context);
        if (CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "- backup shared preferences");
        }
        if (CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "+ cache all camera settings");
        }
        final Map all = getSharedPrefs(context, true).getAll();
        if (CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "- cache all camera settings");
        }
        if (all.isEmpty()) {
            if (CameraSettingsMigrator.DEBUG_ENABLED) {
                Log.w("cameramigrator", "ignore migration since legacy camera settings is empty");
            }
            return;
        }
        if (CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "+ cleanup shared preferences");
        }
        final SharedPreferences sharedPrefs = getSharedPrefs(context, false);
        sharedPrefs.edit().clear().commit();
        if (CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "- cleanup shared preferences");
        }
        if (context.checkSelfPermission("android.permission.CAMERA") != 0) {
            if (CameraSettingsMigrator.DEBUG_ENABLED) {
                Log.w("cameramigrator", "ignore migration since not have camera permission");
            }
            return;
        }
        if (CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "+ setup camera parameter manager");
        }
        final IntentReader.VideoQualityConfigurations videoQualityConfigurations = new IntentReader().getVideoQualityConfigurations(new Intent("android.intent.action.MAIN").addCategory("android.intent.category.LAUNCHER"));
        final UserSettingManager userSettingManager = new UserSettingManager(context, storage);
        userSettingManager.prepare(context, LaunchCondition.OneShotMode.NONE, null, videoQualityConfigurations, null);
        final Map<CapturingMode, Parameters> loadMigrationData = loadMigrationData(CapturingMode.NORMAL, userSettingManager, userSettingsLoaderImpl);
        if (CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "- setup camera parameter manager");
        }
        final HashMap hashMap = new HashMap();
        final HashMap hashMap2 = new HashMap();
        final ArrayList list = new ArrayList();
        if (CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "+ migrate capturing mode settings");
        }
        for (final CapturingMode capturingMode : CapturingMode.getValidOptions()) {
            if (CameraSettingsMigrator.DEBUG_ENABLED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("+ migrate ");
                sb.append(capturingMode.toString().toLowerCase());
                sb.append(" settings");
                Log.d("cameramigrator", sb.toString());
            }
            userSettingManager.changeCapturingMode(capturingMode);
            userSettingManager.applyCapturingMode();
            final String parameterKeyPrefixForCapturingMode = createParameterKeyPrefixForCapturingMode(capturingMode);
            if (CameraSettingsMigrator.DEBUG_ENABLED) {
                for (final Map.Entry<Object, V> entry : userSettingManager.getParameters().getHolder().entrySet()) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(parameterKeyPrefixForCapturingMode);
                    sb2.append(entry.getKey());
                    hashMap2.put(sb2.toString(), ((UserSettingValueHolder)entry.getValue()).toString());
                }
            }
            final ArrayList list2 = new ArrayList();
            list2.addAll(CameraSettingsMigrator.SETTINGS.get(capturingMode));
            list2.addAll(CameraSettingsMigrator.COMMON_SETTINGS);
            for (final UserSettingKey obj : list2) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(parameterKeyPrefixForCapturingMode);
                sb3.append(obj);
                final String string = sb3.toString();
                final String string2 = Objects.toString(all.get(string), "NO_VALUE");
                if ("NO_VALUE".equals(string2)) {
                    if (!CameraSettingsMigrator.DEBUG_ENABLED) {
                        continue;
                    }
                    hashMap.put(string, "ignore-null-legacy-value");
                }
                else {
                    final UserSettingValue value = parse(string2).get();
                    if (value == null) {
                        if (!CameraSettingsMigrator.DEBUG_ENABLED) {
                            continue;
                        }
                        hashMap.put(string, "ignore-null-current-value");
                    }
                    else if (!SettingUi.isSelectableValues(obj, userSettingManager.getParameters(), value)) {
                        if (!CameraSettingsMigrator.DEBUG_ENABLED) {
                            continue;
                        }
                        hashMap.put(string, "ignore-unselectable-current-value");
                    }
                    else {
                        UserSettingValue convertUnsupportedValue = value;
                        if (!isSupported(userSettingManager, value) && (convertUnsupportedValue = convertUnsupportedValue(userSettingManager, value, capturingMode)) == null) {
                            if (!CameraSettingsMigrator.DEBUG_ENABLED) {
                                continue;
                            }
                            hashMap.put(string, "ignore-unsupported-current-value");
                        }
                        else {
                            if (!list.contains(convertUnsupportedValue.getKey())) {
                                list.add(convertUnsupportedValue.getKey());
                            }
                            if (capturingMode == CapturingMode.NORMAL && convertUnsupportedValue == ObjectTracking.ON) {
                                userSettingManager.set(TouchIntention.OBJECT_TRACKING);
                            }
                            userSettingManager.set(convertUnsupportedValue);
                            if (!CameraSettingsMigrator.DEBUG_ENABLED) {
                                continue;
                            }
                            hashMap.put(string, "apply-selectable-supported-current-value");
                        }
                    }
                }
            }
            if (CameraSettingsMigrator.DEBUG_ENABLED) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("- migrate ");
                sb4.append(capturingMode.toString().toLowerCase());
                sb4.append(" settings");
                Log.d("cameramigrator", sb4.toString());
            }
        }
        if (CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "- migrate capturing mode settings");
        }
        if (CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "+ release camera parameter manager");
        }
        userSettingsLoaderImpl.saveMigrateParameters(loadMigrationData, CapturingMode.SCENE_RECOGNITION);
        userSettingManager.release();
        if (CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "- release camera parameter manager");
        }
        if (CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "+ migrate non-capturing mode settings");
        }
        if (!migrateOtherSettingsToSharedPrefs(all, sharedPrefs) && CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "- failed to write non-capturing mode settings");
        }
        if (CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "- migrate non-capturing mode settings");
        }
        if (CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "+ generate camera settings migration report");
            createMigrationReport(all, hashMap2, Collections.unmodifiableMap((Map<? extends String, ?>)sharedPrefs.getAll()), hashMap);
            Log.d("cameramigrator", "- generate camera settings migration report");
        }
        if (CameraSettingsMigrator.DEBUG_ENABLED) {
            Log.d("cameramigrator", "- camera settings migration");
        }
    }
    
    private static boolean migrateOtherSettingsToSharedPrefs(final Map<String, ?> map, final SharedPreferences sharedPreferences) {
        final SharedPreferences$Editor edit = sharedPreferences.edit();
        for (final Map.Entry<String, V> entry : map.entrySet()) {
            final String s = entry.getKey();
            final String string = Objects.toString(entry.getValue(), "NO_VALUE");
            if ("NO_VALUE".equals(string)) {
                continue;
            }
            if (s.startsWith("CAPTURING_MODE_")) {
                continue;
            }
            int n = -1;
            if (s.hashCode() == 2111842220) {
                if (s.equals("KEY_LAST_MODE")) {
                    n = 0;
                }
            }
            if (n != 0) {
                continue;
            }
            edit.putString(s, string);
        }
        return edit.commit();
    }
    
    private static UserSettingValueHolder<UserSettingValue> parse(final String s) {
        final UserSettingValueHolder userSettingValueHolder = new UserSettingValueHolder(null);
        userSettingValueHolder.parseValueString(s);
        return userSettingValueHolder;
    }
}
