// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector;

import android.content.SharedPreferences$Editor;
import org.xmlpull.v1.XmlPullParser;
import android.content.pm.ApplicationInfo;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.os.Build;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.pm.ActivityInfo;
import java.util.Iterator;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import com.sonyericsson.android.camera.util.CamLog;
import android.content.ComponentName;
import android.content.pm.ResolveInfo;
import android.content.Intent;
import java.util.ArrayList;
import android.content.Context;
import java.util.List;

public class CapturingModePluginsPMLoader
{
    private static final String FINGERPRINT = "android.os.Build.FINGERPRINT";
    private static final String INTENT_FILTER_ACTION_NAME_FOR_QUERY = "com.sonymobile.camera.addon.action.REGISTER_MODE";
    private static final String META_DATA_NAME = "com.sonymobile.camera.addon.MODE_ATTRIBUTES";
    private static final String MODES_TAG = "modes";
    private static final String MODE_ATTRIBUTE_DESCRIPTION_LABEL = "descriptionLabel";
    private static final String MODE_ATTRIBUTE_NAME = "name";
    private static final String MODE_ATTRIBUTE_SELECTOR_ICON = "selectorIcon";
    private static final String MODE_ATTRIBUTE_SELECTOR_LABEL = "selectorLabel";
    private static final String MODE_ATTRIBUTE_SHORTCUT_ICON = "shortcutIcon";
    private static final String MODE_ATTRIBUTE_SHORTCUT_LABEL = "shortcutLabel";
    private static final String MODE_DESCRIPTION_LABEL = "com.sonymobile.camera.addon.MODE_DESCRIPTION_LABEL";
    private static final String MODE_SELECTOR_ICON = "com.sonymobile.camera.addon.MODE_SELECTOR_ICON";
    private static final String MODE_SELECTOR_LABEL = "com.sonymobile.camera.addon.MODE_SELECTOR_LABEL";
    private static final String MODE_SELECTOR_NAME = "com.sonymobile.camera.addon.MODE_NAME";
    private static final String MODE_SHORTCUT_ICON = "com.sonymobile.camera.addon.MODE_SHORTCUT_ICON";
    private static final String MODE_SHORTCUT_LABEL = "com.sonymobile.camera.addon.MODE_SHORTCUT_LABEL";
    private static final String MODE_TAG = "mode";
    private static final String[] PluginConfigurationList;
    private static final String SHARED_PREFERENCE_NAME = "com.sonyericsson.cameracommon.appsui.fingerprint_sharedprefs";
    private static final String TAG = "CapturingModePluginsPMLoader";
    private static final String USES_FEATURE_ATTRIBUTE_NAME = "name";
    private static final String USES_FEATURE_TAG = "uses-feature";
    private static final String XMLNS_ANDROID = "http://schemas.android.com/apk/res/android";
    private static List<CapturingModeAttributes> mPackageManagerPluginList;
    private Context mContext;
    private TmpUiAttributes mTmpAttributes;
    
    static {
        PluginConfigurationList = new String[] { "HighFrameRateVideoActivity", "enable_timeshift_video_rec_app", "HighResolutionVideoActivity", "enable_4k_video_rec_app" };
    }
    
    public CapturingModePluginsPMLoader(final Context mContext) {
        this.mContext = mContext;
    }
    
    private void addPluginToList() {
        this.mTmpAttributes.mInternalCaptureType = CapturingModeAttributes.InternalCaptureType.Photo;
        final TmpUiAttributes mTmpAttributes = this.mTmpAttributes;
        boolean mIsVisibleShortcut = true;
        mTmpAttributes.mIsVisibleNormal = true;
        this.mTmpAttributes.mIsVisibleOneshot = false;
        final TmpUiAttributes mTmpAttributes2 = this.mTmpAttributes;
        if (this.mTmpAttributes.mShortcutLabelId == 0 || this.mTmpAttributes.mShortcutIconId == 0) {
            mIsVisibleShortcut = false;
        }
        mTmpAttributes2.mIsVisibleShortcut = mIsVisibleShortcut;
        CapturingModePluginsPMLoader.mPackageManagerPluginList.add(new CapturingModeAttributes(this.mTmpAttributes.mPackageName, this.mTmpAttributes.mActivityName, this.mTmpAttributes.mModeName, this.mTmpAttributes.mSelectorIconId, this.mTmpAttributes.mSelectorLabelId, this.mTmpAttributes.mDescriptionLabelId, this.mTmpAttributes.mShortcutIconId, this.mTmpAttributes.mShortcutLabelId, this.mTmpAttributes.mInternalCaptureType, this.mTmpAttributes.mIsVisibleNormal, this.mTmpAttributes.mIsVisibleOneshot, this.mTmpAttributes.mIsVisibleShortcut, null));
    }
    
    private void clearCapturingModes() {
        this.mContext.getContentResolver().delete(CameraCommonProviderConstants.CAPTURINGMODE_CONTENT_URI, (String)null, (String[])null);
    }
    
    private void getPluginsFromPackageManager() {
        if (CapturingModePluginsPMLoader.mPackageManagerPluginList == null) {
            CapturingModePluginsPMLoader.mPackageManagerPluginList = new ArrayList<CapturingModeAttributes>();
        }
        CapturingModePluginsPMLoader.mPackageManagerPluginList.clear();
        final PackageManager packageManager = this.mContext.getPackageManager();
        for (final ResolveInfo resolveInfo : packageManager.queryIntentActivities(new Intent("com.sonymobile.camera.addon.action.REGISTER_MODE"), 0)) {
            if (!this.isPluginConfigurationOn(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name)) {
                continue;
            }
            this.mTmpAttributes = new TmpUiAttributes();
            this.mTmpAttributes.mPackageName = resolveInfo.activityInfo.packageName;
            this.mTmpAttributes.mActivityName = resolveInfo.activityInfo.name;
            if (packageManager.checkPermission("com.sonymobile.permission.CAMERA_ADDON", this.mTmpAttributes.mPackageName) == 0) {
                try {
                    final ActivityInfo activityInfo = this.mContext.getPackageManager().getActivityInfo(new ComponentName(this.mTmpAttributes.mPackageName, this.mTmpAttributes.mActivityName), 128);
                    if (this.parsePluginFromMetaData(resolveInfo, activityInfo)) {
                        this.addPluginToList();
                        continue;
                    }
                    try {
                        final Resources resourcesForApplication = packageManager.getResourcesForApplication(resolveInfo.activityInfo.packageName);
                        try {
                            final boolean modeAttributesFromMetaData = this.parseModeAttributesFromMetaData(resolveInfo, activityInfo, resourcesForApplication);
                            if (resourcesForApplication != null && modeAttributesFromMetaData) {
                                if (this.parsePluginFromModeAttributesXmlFile(activityInfo, resourcesForApplication)) {
                                    this.addPluginToList();
                                    continue;
                                }
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Failed to add plugin for this package: ");
                                sb.append(resolveInfo.activityInfo.packageName);
                                CamLog.e(sb.toString());
                                continue;
                            }
                        }
                        catch (final IOException ex) {
                            CamLog.e("IOException: Problem when parse mode attributes xml file.");
                        }
                        catch (final XmlPullParserException ex2) {
                            CamLog.e("XmlPullParserException: Problem when parse mode attributes xml file.");
                        }
                        if (this.parsePluginFromApplicationInfo(resolveInfo)) {
                            this.addPluginToList();
                        }
                        else {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Failed to add plugin for this package: ");
                            sb2.append(resolveInfo.activityInfo.packageName);
                            CamLog.e(sb2.toString());
                        }
                    }
                    catch (final PackageManager$NameNotFoundException ex3) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("NameNotFoundException: Problem when geting Application Resources with packageName: ");
                        sb3.append(resolveInfo.activityInfo.packageName);
                        CamLog.e(sb3.toString());
                        CamLog.e("Failed to add plugin for this package.");
                    }
                    continue;
                }
                catch (final PackageManager$NameNotFoundException ex4) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("NameNotFoundException: Problem when geting Application META_DATA with packageName: ");
                    sb4.append(resolveInfo.activityInfo.packageName);
                    CamLog.e(sb4.toString());
                    CamLog.e("Failed to add plugin for this package.");
                    continue;
                }
                break;
            }
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("This application has no CAMERA_ADDON permission: ");
            sb5.append(resolveInfo.activityInfo.packageName);
            CamLog.e(sb5.toString());
            CamLog.e("Failed to add plugin for this package.");
        }
    }
    
    private int getResIDFromXmlString(final String s, final String s2) {
        int identifier;
        try {
            identifier = this.mContext.getPackageManager().getResourcesForApplication(s).getIdentifier(s2, (String)null, s);
        }
        catch (final PackageManager$NameNotFoundException ex) {
            CamLog.e("Could not get resource id for plugin");
            identifier = 0;
        }
        return identifier;
    }
    
    private boolean isCapturingModeFingerprintModified(final SharedPreferences sharedPreferences) {
        final String string = sharedPreferences.getString("android.os.Build.FINGERPRINT", "");
        final String fingerprint = Build.FINGERPRINT;
        if (!string.equals(fingerprint)) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("version mismatch: cached: ");
                sb.append(string);
                sb.append(", current : ");
                sb.append(fingerprint);
                CamLog.d(sb.toString());
            }
            return true;
        }
        return false;
    }
    
    private boolean isPluginConfigurationOn(final String s, final String s2) {
        int n = 0;
        boolean valueOn;
        while (true) {
            final int length = CapturingModePluginsPMLoader.PluginConfigurationList.length;
            valueOn = true;
            if (n >= length) {
                break;
            }
            if (CapturingModePluginsPMLoader.PluginConfigurationList[n].equals(s2.substring(s2.lastIndexOf(".") + 1))) {
                valueOn = this.isValueOn(CapturingModePluginsPMLoader.PluginConfigurationList[n + 1], s);
                break;
            }
            n += 2;
        }
        return valueOn;
    }
    
    private boolean isValueOn(final String s, final String s2) {
        final PackageManager packageManager = this.mContext.getPackageManager();
        final boolean b = true;
        boolean boolean1;
        try {
            final Resources resourcesForApplication = packageManager.getResourcesForApplication(s2);
            final int identifier = resourcesForApplication.getIdentifier(s, "bool", s2);
            boolean1 = b;
            if (identifier != 0) {
                boolean1 = resourcesForApplication.getBoolean(identifier);
            }
        }
        catch (final PackageManager$NameNotFoundException ex) {
            CamLog.e("Error reading out plugin configuration value");
            boolean1 = b;
        }
        return boolean1;
    }
    
    private boolean parseModeAttributesFromMetaData(final ResolveInfo resolveInfo, ActivityInfo activityInfo, final Resources resources) throws XmlPullParserException, IOException {
        final Bundle metaData = activityInfo.metaData;
        int n = 1;
        if (metaData == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("This activity has no meta-data: ");
            sb.append(resolveInfo.activityInfo.name);
            CamLog.e(sb.toString());
            return false;
        }
        if (resources != null) {
            final int int1 = activityInfo.metaData.getInt("com.sonymobile.camera.addon.MODE_ATTRIBUTES", 0);
            if (int1 != 0) {
                try {
                    final XmlResourceParser xml = resources.getXml(int1);
                    final ActivityInfo activityInfo2 = null;
                    Label_0123: {
                        if (xml != null) {
                            break Label_0123;
                        }
                        activityInfo = activityInfo2;
                        try {
                            try {
                                CamLog.e("Problem when get XmlResourceParser from mode attributes xml resource Id.");
                                if (xml != null) {
                                    xml.close();
                                }
                                return false;
                                Label_0179: {
                                    return n != 0;
                                }
                                int n3 = 0;
                            Block_12:
                                while (true) {
                                    Label_0137: {
                                        int n2 = 0;
                                        Label_0341: {
                                            while (true) {
                                                activityInfo = activityInfo2;
                                                activityInfo = activityInfo2;
                                                final StringBuilder sb2 = new StringBuilder();
                                                activityInfo = activityInfo2;
                                                sb2.append("Mode of \"");
                                                activityInfo = activityInfo2;
                                                sb2.append(resolveInfo.activityInfo.packageName);
                                                activityInfo = activityInfo2;
                                                sb2.append("\" requires \"");
                                                activityInfo = activityInfo2;
                                                final String attributeValue;
                                                sb2.append(attributeValue);
                                                activityInfo = activityInfo2;
                                                sb2.append("\" but this platform doesn't support the feature.");
                                                activityInfo = activityInfo2;
                                                CamLog.w(sb2.toString());
                                                n2 = 0;
                                                break Label_0341;
                                                activityInfo = activityInfo2;
                                                attributeValue = xml.getAttributeValue("http://schemas.android.com/apk/res/android", "name");
                                                n2 = n;
                                                activityInfo = activityInfo2;
                                                iftrue(Label_0341:)(this.mContext.getPackageManager().hasSystemFeature(attributeValue));
                                                continue;
                                            }
                                            activityInfo = activityInfo2;
                                            n3 = xml.next();
                                            n = 1;
                                            break Label_0137;
                                        }
                                        activityInfo = activityInfo2;
                                        n3 = xml.next();
                                        n = n2;
                                    }
                                    iftrue(Label_0182:)(n3 != 3);
                                    activityInfo = activityInfo2;
                                    iftrue(Label_0167:)("mode".equals(xml.getName()));
                                    break Block_12;
                                    int n2 = n;
                                    activityInfo = activityInfo2;
                                    iftrue(Label_0341:)(!"uses-feature".equals(xml.getName()));
                                    continue;
                                }
                                Label_0182: {
                                    break Label_0182;
                                    while (true) {
                                        xml.close();
                                        return n != 0;
                                        Label_0167:
                                        iftrue(Label_0179:)(xml == null);
                                        continue;
                                    }
                                }
                                int n2 = n;
                                iftrue(Label_0341:)(n3 != 2);
                            }
                            finally {
                                if (xml != null) {
                                    if (activityInfo != null) {
                                        final XmlResourceParser xmlResourceParser = xml;
                                        xmlResourceParser.close();
                                    }
                                    else {
                                        xml.close();
                                    }
                                }
                            }
                        }
                        catch (final Throwable t) {}
                    }
                    try {
                        final XmlResourceParser xmlResourceParser = xml;
                        xmlResourceParser.close();
                    }
                    catch (final Throwable t2) {}
                }
                catch (final XmlPullParserException | IOException ex) {
                    CamLog.e("Problem when parse mode attributes xml file");
                    return false;
                }
            }
            return n != 0;
        }
        return false;
    }
    
    private boolean parsePluginFromApplicationInfo(final ResolveInfo resolveInfo) {
        try {
            final ApplicationInfo applicationInfo = this.mContext.getPackageManager().getPackageInfo(resolveInfo.activityInfo.packageName, 0).applicationInfo;
            this.mTmpAttributes.mModeName = applicationInfo.loadLabel(this.mContext.getPackageManager()).toString();
            this.mTmpAttributes.mSelectorIconId = applicationInfo.icon;
            this.mTmpAttributes.mSelectorLabelId = applicationInfo.labelRes;
            if (applicationInfo.descriptionRes != 0) {
                this.mTmpAttributes.mDescriptionLabelId = applicationInfo.descriptionRes;
            }
            else {
                this.mTmpAttributes.mDescriptionLabelId = this.mTmpAttributes.mSelectorLabelId;
            }
            return true;
        }
        catch (final PackageManager$NameNotFoundException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("NameNotFoundException: ");
            sb.append(resolveInfo.activityInfo.packageName);
            CamLog.e(sb.toString());
            return false;
        }
    }
    
    private boolean parsePluginFromMetaData(final ResolveInfo resolveInfo, final ActivityInfo activityInfo) {
        final Bundle metaData = activityInfo.metaData;
        final boolean b = true;
        if (metaData == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("This activity has no meta-data: ");
            sb.append(resolveInfo.activityInfo.name);
            CamLog.e(sb.toString());
            return false;
        }
        this.mTmpAttributes.mModeName = activityInfo.metaData.getString("com.sonymobile.camera.addon.MODE_NAME");
        this.mTmpAttributes.mSelectorIconId = activityInfo.metaData.getInt("com.sonymobile.camera.addon.MODE_SELECTOR_ICON");
        this.mTmpAttributes.mSelectorLabelId = activityInfo.metaData.getInt("com.sonymobile.camera.addon.MODE_SELECTOR_LABEL");
        this.mTmpAttributes.mDescriptionLabelId = activityInfo.metaData.getInt("com.sonymobile.camera.addon.MODE_DESCRIPTION_LABEL");
        this.mTmpAttributes.mShortcutIconId = activityInfo.metaData.getInt("com.sonymobile.camera.addon.MODE_SHORTCUT_ICON");
        this.mTmpAttributes.mShortcutLabelId = activityInfo.metaData.getInt("com.sonymobile.camera.addon.MODE_SHORTCUT_LABEL");
        boolean b2 = b;
        if (this.mTmpAttributes.mModeName == null) {
            b2 = b;
            if (this.mTmpAttributes.mSelectorIconId == 0) {
                b2 = b;
                if (this.mTmpAttributes.mSelectorLabelId == 0) {
                    b2 = b;
                    if (this.mTmpAttributes.mDescriptionLabelId == 0) {
                        b2 = b;
                        if (this.mTmpAttributes.mShortcutIconId == 0) {
                            b2 = (this.mTmpAttributes.mShortcutLabelId != 0 && b);
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    private boolean parsePluginFromModeAttributesXmlFile(ActivityInfo activityInfo, final Resources resources) throws XmlPullParserException, IOException {
        if (resources != null) {
            final int int1 = activityInfo.metaData.getInt("com.sonymobile.camera.addon.MODE_ATTRIBUTES", 0);
            if (int1 != 0) {
                try {
                    final XmlResourceParser xml = resources.getXml(int1);
                    activityInfo = null;
                    if (xml != null) {
                        while (true) {
                            try {
                                try {
                                    this.parsePluginXMLData((XmlPullParser)xml, resources);
                                    if (xml != null) {
                                        xml.close();
                                    }
                                    return true;
                                }
                                finally {
                                    if (xml != null) {
                                        if (activityInfo != null) {
                                            final XmlResourceParser xmlResourceParser = xml;
                                            xmlResourceParser.close();
                                        }
                                        else {
                                            xml.close();
                                        }
                                    }
                                }
                            }
                            catch (final Throwable t) {}
                            try {
                                final XmlResourceParser xmlResourceParser = xml;
                                xmlResourceParser.close();
                                continue;
                            }
                            catch (final Throwable t2) {}
                            break;
                        }
                    }
                    if (xml != null) {
                        xml.close();
                    }
                }
                catch (final XmlPullParserException | IOException ex) {
                    CamLog.e("Problem when parse mode attributes xml file.");
                }
            }
        }
        return false;
    }
    
    private void parsePluginXMLData(final XmlPullParser xmlPullParser, final Resources resources) throws XmlPullParserException, IOException {
        for (int i = xmlPullParser.getEventType(); i != 1; i = xmlPullParser.next()) {
            for (int n = xmlPullParser.next(); n != 3 || !"modes".equals(xmlPullParser.getName()); n = xmlPullParser.next()) {
                if (n == 2 && "mode".equals(xmlPullParser.getName())) {
                    this.parsePluginXMLModeTag(xmlPullParser, resources);
                }
            }
        }
    }
    
    private void parsePluginXMLModeTag(final XmlPullParser xmlPullParser, final Resources resources) throws XmlPullParserException, IOException {
        for (int attributeCount = xmlPullParser.getAttributeCount(), i = 0; i < attributeCount; ++i) {
            final String attributeName = xmlPullParser.getAttributeName(i);
            final String attributeValue = xmlPullParser.getAttributeValue(i);
            if (attributeName != null) {
                if ("name".equals(attributeName)) {
                    this.mTmpAttributes.mModeName = attributeValue;
                }
                else if ("selectorIcon".equals(attributeName)) {
                    this.mTmpAttributes.mSelectorIconId = this.getResIDFromXmlString(this.mTmpAttributes.mPackageName, attributeValue);
                }
                else if ("selectorLabel".equals(attributeName)) {
                    this.mTmpAttributes.mSelectorLabelId = this.getResIDFromXmlString(this.mTmpAttributes.mPackageName, attributeValue);
                }
                else if ("descriptionLabel".equals(attributeName)) {
                    this.mTmpAttributes.mDescriptionLabelId = this.getResIDFromXmlString(this.mTmpAttributes.mPackageName, attributeValue);
                }
                else if ("shortcutIcon".equals(attributeName)) {
                    this.mTmpAttributes.mShortcutIconId = this.getResIDFromXmlString(this.mTmpAttributes.mPackageName, attributeValue);
                }
                else if ("shortcutLabel".equals(attributeName)) {
                    this.mTmpAttributes.mShortcutLabelId = this.getResIDFromXmlString(this.mTmpAttributes.mPackageName, attributeValue);
                }
            }
        }
    }
    
    public void updatePluginsInDB() {
        final SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("com.sonyericsson.cameracommon.appsui.fingerprint_sharedprefs", 0);
        if (this.isCapturingModeFingerprintModified(sharedPreferences)) {
            this.clearCapturingModes();
            final SharedPreferences$Editor edit = sharedPreferences.edit();
            edit.putString("android.os.Build.FINGERPRINT", Build.FINGERPRINT);
            edit.commit();
        }
        this.getPluginsFromPackageManager();
        CapturingModeCollection.register(this.mContext.getContentResolver(), CapturingModePluginsPMLoader.mPackageManagerPluginList, this.mContext.getPackageManager());
    }
    
    private static class TmpUiAttributes
    {
        String mActivityName;
        int mDescriptionLabelId;
        CapturingModeAttributes.InternalCaptureType mInternalCaptureType;
        boolean mIsVisibleNormal;
        boolean mIsVisibleOneshot;
        boolean mIsVisibleShortcut;
        String mModeName;
        String mPackageName;
        int mSelectorIconId;
        int mSelectorLabelId;
        int mShortcutIconId;
        int mShortcutLabelId;
    }
}
