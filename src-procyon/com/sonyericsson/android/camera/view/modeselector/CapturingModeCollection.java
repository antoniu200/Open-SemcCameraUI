// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.modeselector;

import android.os.RemoteException;
import android.content.OperationApplicationException;
import java.util.Collection;
import android.content.res.Resources;
import android.content.pm.PackageManager$NameNotFoundException;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import android.content.pm.PackageManager;
import com.sonyericsson.android.camera.util.CamLog;
import android.content.ContentProviderOperation;
import android.database.Cursor;
import android.content.ContentResolver;

public class CapturingModeCollection
{
    private static final int INVALID_POSITION = -1;
    private static final String[] PROTECTION;
    private static final String SORT_ASC = "sort_order ASC";
    private static final String TAG = "CapturingModeCollection";
    private static final String WHERE_WITH_ID = "_id=?";
    private final ContentResolver mResolver;
    
    static {
        PROTECTION = new String[] { "_id", "package", "activity", "mode_name", "selectoricon_id", "selectorlabel_id", "shortcuticon_id", "shortcutlabel_id", "descriptionlabel_id", "capture_type", "visibility_normal", "visibility_oneshot", "visibility_shortcut" };
    }
    
    public CapturingModeCollection(final ContentResolver mResolver) {
        this.mResolver = mResolver;
    }
    
    private static CapturingModeAttributes convert(final Cursor cursor) {
        return new CapturingModeAttributes(cursor.getLong(cursor.getColumnIndex("_id")), cursor.getString(cursor.getColumnIndex("package")), cursor.getString(cursor.getColumnIndex("activity")), cursor.getString(cursor.getColumnIndex("mode_name")), cursor.getInt(cursor.getColumnIndex("selectoricon_id")), cursor.getInt(cursor.getColumnIndex("selectorlabel_id")), cursor.getInt(cursor.getColumnIndex("descriptionlabel_id")), cursor.getInt(cursor.getColumnIndex("shortcuticon_id")), cursor.getInt(cursor.getColumnIndex("shortcutlabel_id")), CaptureTypeCode.toCaptureType(cursor.getInt(cursor.getColumnIndex("capture_type"))), VisibilityTypeCode.toVisibilityType(cursor.getInt(cursor.getColumnIndex("visibility_normal"))), VisibilityTypeCode.toVisibilityType(cursor.getInt(cursor.getColumnIndex("visibility_oneshot"))), VisibilityTypeCode.toVisibilityType(cursor.getInt(cursor.getColumnIndex("visibility_shortcut"))));
    }
    
    private static ContentProviderOperation createDeleteOperation(final long i) {
        if (CamLog.VERBOSE) {
            CamLog.d("createDeleteOperation()");
        }
        return ContentProviderOperation.newDelete(CameraCommonProviderConstants.CAPTURINGMODE_CONTENT_URI).withSelection("_id=?", new String[] { Long.toString(i) }).build();
    }
    
    private static ContentProviderOperation createInsertOperation(final CapturingModeAttributes capturingModeAttributes, final int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("createUpdateSortOrderOperation()");
        }
        return ContentProviderOperation.newUpdate(CameraCommonProviderConstants.CAPTURINGMODE_CONTENT_URI).withValue("package", (Object)capturingModeAttributes.getPackageName()).withValue("activity", (Object)capturingModeAttributes.getActivityName()).withValue("mode_name", (Object)capturingModeAttributes.getModeName()).withValue("sort_order", (Object)i).withValue("capture_type", (Object)CaptureTypeCode.toCode(capturingModeAttributes.getInternalCaptureType())).withValue("visibility_normal", (Object)VisibilityTypeCode.toCode(capturingModeAttributes.isVisibleNormal())).withValue("visibility_oneshot", (Object)VisibilityTypeCode.toCode(capturingModeAttributes.isVisibleOneshot())).withValue("visibility_shortcut", (Object)VisibilityTypeCode.toCode(capturingModeAttributes.isVisibleShortcut())).withValue("selectoricon_id", (Object)capturingModeAttributes.getSelectorIconId()).withValue("selectorlabel_id", (Object)capturingModeAttributes.getSelectorLabelId()).withValue("descriptionlabel_id", (Object)capturingModeAttributes.getDescriptionLabelId()).withValue("shortcuticon_id", (Object)capturingModeAttributes.getShortcutIconId()).withValue("shortcutlabel_id", (Object)capturingModeAttributes.getShortcutLabelId()).build();
    }
    
    private static ContentProviderOperation createUpdateSortOrderOperation(final CapturingModeAttributes capturingModeAttributes, final int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("createUpdateSortOrderOperation()");
        }
        return ContentProviderOperation.newUpdate(CameraCommonProviderConstants.CAPTURINGMODE_CONTENT_URI).withSelection("_id=?", new String[] { Long.toString(capturingModeAttributes.getId()) }).withValue("sort_order", (Object)i).build();
    }
    
    private static List<CapturingModeAttributes> findIllegalCapturingMode(final PackageManager packageManager, final List<CapturingModeAttributes> list) {
        final ArrayList list2 = new ArrayList();
        if (packageManager == null) {
            return list2;
        }
        for (final CapturingModeAttributes capturingModeAttributes : list) {
            if (isIllegalCapturingMode(packageManager, capturingModeAttributes)) {
                final String tag = CapturingModeCollection.TAG;
                final StringBuilder sb = new StringBuilder();
                sb.append("This attributes is illegal. [");
                sb.append(capturingModeAttributes.getAttributes());
                sb.append("]");
                CamLog.e(tag, sb.toString());
                list2.add(capturingModeAttributes);
            }
        }
        return list2;
    }
    
    private static int indexOf(final CapturingModeAttributes capturingModeAttributes, final List<CapturingModeAttributes> list) {
        for (int i = 0; i < list.size(); ++i) {
            if (((CapturingModeAttributes)list.get(i)).is(capturingModeAttributes.getPackageName(), capturingModeAttributes.getModeName())) {
                return i;
            }
        }
        return -1;
    }
    
    private static boolean isIllegalCapturingMode(final PackageManager packageManager, final CapturingModeAttributes capturingModeAttributes) {
        Resources resourcesForApplication;
        try {
            resourcesForApplication = packageManager.getResourcesForApplication(capturingModeAttributes.getPackageName());
        }
        catch (final PackageManager$NameNotFoundException ex) {
            resourcesForApplication = null;
        }
        if (resourcesForApplication == null) {
            CamLog.e("Resources could not be found.");
            return true;
        }
        if (!ResourceUtil.isDrawableResource(resourcesForApplication, capturingModeAttributes.getSelectorIconId())) {
            CamLog.e("Resource type of selector icon is not drawable.");
            return true;
        }
        if (!ResourceUtil.isStringResource(resourcesForApplication, capturingModeAttributes.getSelectorLabelId())) {
            CamLog.e("Resource type of selector label is not string.");
            return true;
        }
        if (!ResourceUtil.isStringResource(resourcesForApplication, capturingModeAttributes.getDescriptionLabelId())) {
            CamLog.e("Resource type of description label is not string.");
            return true;
        }
        if (capturingModeAttributes.isVisibleShortcut()) {
            if (!ResourceUtil.isDrawableResource(resourcesForApplication, capturingModeAttributes.getShortcutIconId())) {
                CamLog.e("Resource type of shortcut icon is not drawable.");
                return true;
            }
            if (!ResourceUtil.isStringResource(resourcesForApplication, capturingModeAttributes.getShortcutLabelId())) {
                CamLog.e("Resource type of shortcut label is not string.");
                return true;
            }
        }
        return false;
    }
    
    private static boolean isResourceUpdated(final CapturingModeAttributes capturingModeAttributes, final CapturingModeAttributes capturingModeAttributes2) {
        return !capturingModeAttributes.getActivityName().equals(capturingModeAttributes2.getActivityName()) || capturingModeAttributes.getSelectorIconId() != capturingModeAttributes2.getSelectorIconId() || capturingModeAttributes.getSelectorLabelId() != capturingModeAttributes2.getSelectorLabelId() || capturingModeAttributes.getShortcutIconId() != capturingModeAttributes2.getShortcutIconId() || capturingModeAttributes.getShortcutLabelId() != capturingModeAttributes2.getShortcutLabelId() || capturingModeAttributes.getInternalCaptureType() != capturingModeAttributes2.getInternalCaptureType() || capturingModeAttributes.isVisibleNormal() != capturingModeAttributes2.isVisibleNormal() || capturingModeAttributes.isVisibleOneshot() != capturingModeAttributes2.isVisibleOneshot() || capturingModeAttributes.isVisibleShortcut() != capturingModeAttributes2.isVisibleShortcut();
    }
    
    public static void register(final ContentResolver contentResolver, final List<CapturingModeAttributes> list, final PackageManager packageManager) {
        final CapturingModeCollection collection = new CapturingModeCollection(contentResolver);
        final List<CapturingModeAttributes> capturingModeList = collection.getCapturingModeList(new CapturingModeAttributes.InternalCaptureType[0], new CapturingModeAttributes.VisibilityType[0]);
        list.removeAll(findIllegalCapturingMode(packageManager, list));
        final List<CapturingModeAttributes> illegalCapturingMode = findIllegalCapturingMode(packageManager, capturingModeList);
        capturingModeList.removeAll(illegalCapturingMode);
        collection.unregisterCapturingModes(illegalCapturingMode);
        final Iterator iterator = list.iterator();
        boolean b = false;
        List<CapturingModeAttributes> list2 = capturingModeList;
        while (iterator.hasNext()) {
            final CapturingModeAttributes capturingModeAttributes = (CapturingModeAttributes)iterator.next();
            if (CamLog.VERBOSE) {
                CamLog.d("register(", " package:", capturingModeAttributes.getPackageName(), " mode:", capturingModeAttributes.getModeName(), " )");
            }
            final int index = indexOf(capturingModeAttributes, list2);
            Object sortCapturingMode;
            if (index == -1) {
                if (CamLog.VERBOSE) {
                    CamLog.d("  This mode has not been registered.");
                }
                list2.add((CapturingModeUtil.CapturingMode)capturingModeAttributes);
                sortCapturingMode = list2;
                if (CapturingModeUtil.hasDefaultSortOrder((CapturingModeUtil.CapturingMode)capturingModeAttributes)) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("  This mode is pre-installed.");
                    }
                    sortCapturingMode = CapturingModeUtil.sortCapturingMode((List<CapturingModeUtil.CapturingMode>)list2);
                }
            }
            else {
                if (CamLog.VERBOSE) {
                    CamLog.d("  This mode has been registered.");
                }
                if (isResourceUpdated(list2.get(index), capturingModeAttributes)) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("  This mode is updated.");
                    }
                    list2.set(index, capturingModeAttributes);
                    sortCapturingMode = list2;
                }
                else {
                    if (CamLog.VERBOSE) {
                        CamLog.d("  This mode is not updated, so not need register.");
                        continue;
                    }
                    continue;
                }
            }
            b = true;
            list2 = (List<CapturingModeAttributes>)sortCapturingMode;
        }
        if (b) {
            if (CamLog.VERBOSE) {
                CamLog.d("Update data base.");
            }
            collection.saveCapturingModeSortedList(list2);
        }
    }
    
    private void saveCapturingModeSortedList(final List<CapturingModeAttributes> list) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("saveCapturingModeList size:");
            sb.append(list.size());
            CamLog.d(sb.toString());
        }
        final ArrayList<ContentProviderOperation> list2 = new ArrayList<ContentProviderOperation>();
        for (int i = 0; i < list.size(); ++i) {
            final CapturingModeAttributes capturingModeAttributes = list.get(i);
            if (capturingModeAttributes.getId() != null) {
                list2.add(createUpdateSortOrderOperation(capturingModeAttributes, i));
            }
            else {
                list2.add(createInsertOperation(capturingModeAttributes, i));
            }
        }
        try {
            this.mResolver.applyBatch("com.sonymobile.camerauicommon.provider", (ArrayList)list2);
        }
        catch (final IllegalArgumentException ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed to save mode order. Message : ");
            sb2.append(ex.getMessage());
            CamLog.e(sb2.toString());
        }
        catch (final OperationApplicationException ex2) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Failed to save mode order. Message : ");
            sb3.append(ex2.getMessage());
            CamLog.e(sb3.toString());
        }
        catch (final RemoteException ex3) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Failed to save mode order. Message : ");
            sb4.append(ex3.getMessage());
            CamLog.e(sb4.toString());
        }
    }
    
    private void unregisterCapturingModes(final List<CapturingModeAttributes> list) {
        final ArrayList list2 = new ArrayList();
        for (final CapturingModeAttributes capturingModeAttributes : list) {
            if (capturingModeAttributes.getId() != null) {
                list2.add(createDeleteOperation(capturingModeAttributes.getId()));
            }
        }
        if (list2.isEmpty()) {
            return;
        }
        try {
            this.mResolver.applyBatch("com.sonymobile.camerauicommon.provider", list2);
        }
        catch (final OperationApplicationException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to unregister modes. Message : ");
            sb.append(ex.getMessage());
            CamLog.e(sb.toString());
        }
        catch (final RemoteException ex2) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed to unregister modes. Message : ");
            sb2.append(ex2.getMessage());
            CamLog.e(sb2.toString());
        }
    }
    
    public List<CapturingModeAttributes> getCapturingModeList(CapturingModeAttributes.InternalCaptureType[] query, final CapturingModeAttributes.VisibilityType[] array) {
        final StringBuffer sb = new StringBuffer();
        if (query.length > 0) {
            sb.append("(");
            for (final CapturingModeAttributes.InternalCaptureType internalCaptureType : query) {
                final int code = CaptureTypeCode.toCode(internalCaptureType);
                if (query[0] != internalCaptureType) {
                    sb.append(" OR ");
                }
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("(");
                sb2.append("capture_type");
                sb2.append("=");
                sb2.append(code);
                sb2.append(")");
                sb.append(sb2.toString());
            }
            sb.append(")");
        }
        if (query.length > 0 && array.length > 0) {
            sb.append(" AND ");
        }
        if (array.length > 0) {
            sb.append("(");
            for (final CapturingModeAttributes.VisibilityType visibilityType : array) {
                if (array[0] != visibilityType) {
                    sb.append(" OR ");
                }
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("(");
                sb3.append(visibilityType.mColumn);
                sb3.append("=1)");
                sb.append(sb3.toString());
            }
            sb.append(")");
        }
        final ArrayList list = new ArrayList();
        query = (CapturingModeAttributes.InternalCaptureType[])(Object)this.mResolver.query(CameraCommonProviderConstants.CAPTURINGMODE_CONTENT_URI, CapturingModeCollection.PROTECTION, sb.toString(), (String[])null, "sort_order ASC");
        if (query != null) {
            try {
                while (((Cursor)(Object)query).moveToNext()) {
                    list.add(convert((Cursor)(Object)query));
                }
                return list;
            }
            finally {
                ((Cursor)(Object)query).close();
            }
        }
        if (CamLog.VERBOSE) {
            CamLog.w("Fail to retrieve the capturing mode list from CameraCommonProvider.");
        }
        return list;
    }
    
    public void release() {
    }
    
    private static class CaptureTypeCode
    {
        static final int PHOTO = 1;
        static final int VIDEO = 2;
        
        static CapturingModeAttributes.InternalCaptureType toCaptureType(final int n) {
            if (n == 1) {
                return CapturingModeAttributes.InternalCaptureType.Photo;
            }
            if (n == 2) {
                return CapturingModeAttributes.InternalCaptureType.Video;
            }
            throw new IllegalArgumentException();
        }
        
        static int toCode(final CapturingModeAttributes.InternalCaptureType internalCaptureType) {
            if (internalCaptureType.equals(CapturingModeAttributes.InternalCaptureType.Photo)) {
                return 1;
            }
            if (internalCaptureType.equals(CapturingModeAttributes.InternalCaptureType.Video)) {
                return 2;
            }
            throw new IllegalArgumentException();
        }
    }
    
    private static class VisibilityTypeCode
    {
        static final int FALSE = 0;
        static final int TRUE = 1;
        
        static int toCode(final boolean b) {
            return b ? 1 : 0;
        }
        
        static boolean toVisibilityType(final int n) {
            boolean b = true;
            if (n != 1) {
                b = false;
            }
            return b;
        }
    }
}
