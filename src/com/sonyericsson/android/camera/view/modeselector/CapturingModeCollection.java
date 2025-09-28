package com.sonyericsson.android.camera.view.modeselector;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.RemoteException;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.view.modeselector.CameraCommonProviderConstants;
import com.sonyericsson.android.camera.view.modeselector.CapturingModeAttributes;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class CapturingModeCollection {
    private static final int INVALID_POSITION = -1;
    private static final String[] PROTECTION = {"_id", CameraCommonProviderConstants.CapturingModeColumns.PACKAGE, CameraCommonProviderConstants.CapturingModeColumns.ACTIVITY, CameraCommonProviderConstants.CapturingModeColumns.MODE_NAME, CameraCommonProviderConstants.CapturingModeColumns.SELECTORICON_ID, CameraCommonProviderConstants.CapturingModeColumns.SELECTORLABEL_ID, CameraCommonProviderConstants.CapturingModeColumns.SHORTCUTICON_ID, CameraCommonProviderConstants.CapturingModeColumns.SHORTCUTLABEL_ID, CameraCommonProviderConstants.CapturingModeColumns.DESCRIPTIONLABEL_ID, CameraCommonProviderConstants.CapturingModeColumns.CAPTURE_TYPE, CameraCommonProviderConstants.CapturingModeColumns.VISIBILITY_NORMAL, CameraCommonProviderConstants.CapturingModeColumns.VISIBILITY_ONESHOT, CameraCommonProviderConstants.CapturingModeColumns.VISIBILITY_SHORTCUT};
    private static final String SORT_ASC = "sort_order ASC";
    private static final String TAG = "CapturingModeCollection";
    private static final String WHERE_WITH_ID = "_id=?";
    private final ContentResolver mResolver;

    public void release() {
    }

    private static CapturingModeAttributes convert(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex("_id");
        int columnIndex2 = cursor.getColumnIndex(CameraCommonProviderConstants.CapturingModeColumns.PACKAGE);
        int columnIndex3 = cursor.getColumnIndex(CameraCommonProviderConstants.CapturingModeColumns.ACTIVITY);
        int columnIndex4 = cursor.getColumnIndex(CameraCommonProviderConstants.CapturingModeColumns.MODE_NAME);
        int columnIndex5 = cursor.getColumnIndex(CameraCommonProviderConstants.CapturingModeColumns.SELECTORICON_ID);
        int columnIndex6 = cursor.getColumnIndex(CameraCommonProviderConstants.CapturingModeColumns.SELECTORLABEL_ID);
        int columnIndex7 = cursor.getColumnIndex(CameraCommonProviderConstants.CapturingModeColumns.DESCRIPTIONLABEL_ID);
        int columnIndex8 = cursor.getColumnIndex(CameraCommonProviderConstants.CapturingModeColumns.SHORTCUTICON_ID);
        int columnIndex9 = cursor.getColumnIndex(CameraCommonProviderConstants.CapturingModeColumns.SHORTCUTLABEL_ID);
        int columnIndex10 = cursor.getColumnIndex(CameraCommonProviderConstants.CapturingModeColumns.CAPTURE_TYPE);
        int columnIndex11 = cursor.getColumnIndex(CameraCommonProviderConstants.CapturingModeColumns.VISIBILITY_NORMAL);
        int columnIndex12 = cursor.getColumnIndex(CameraCommonProviderConstants.CapturingModeColumns.VISIBILITY_ONESHOT);
        int columnIndex13 = cursor.getColumnIndex(CameraCommonProviderConstants.CapturingModeColumns.VISIBILITY_SHORTCUT);
        long j = cursor.getLong(columnIndex);
        return new CapturingModeAttributes(Long.valueOf(j), cursor.getString(columnIndex2), cursor.getString(columnIndex3), cursor.getString(columnIndex4), cursor.getInt(columnIndex5), cursor.getInt(columnIndex6), cursor.getInt(columnIndex7), cursor.getInt(columnIndex8), cursor.getInt(columnIndex9), CaptureTypeCode.toCaptureType(cursor.getInt(columnIndex10)), VisibilityTypeCode.toVisibilityType(cursor.getInt(columnIndex11)), VisibilityTypeCode.toVisibilityType(cursor.getInt(columnIndex12)), VisibilityTypeCode.toVisibilityType(cursor.getInt(columnIndex13)));
    }

    private static ContentProviderOperation createUpdateSortOrderOperation(CapturingModeAttributes capturingModeAttributes, int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("createUpdateSortOrderOperation()");
        }
        return ContentProviderOperation.newUpdate(CameraCommonProviderConstants.CAPTURINGMODE_CONTENT_URI).withSelection(WHERE_WITH_ID, new String[]{Long.toString(capturingModeAttributes.getId().longValue())}).withValue(CameraCommonProviderConstants.CapturingModeColumns.SORT_ORDER, Integer.valueOf(i)).build();
    }

    private static ContentProviderOperation createInsertOperation(CapturingModeAttributes capturingModeAttributes, int i) {
        if (CamLog.VERBOSE) {
            CamLog.d("createUpdateSortOrderOperation()");
        }
        return ContentProviderOperation.newUpdate(CameraCommonProviderConstants.CAPTURINGMODE_CONTENT_URI).withValue(CameraCommonProviderConstants.CapturingModeColumns.PACKAGE, capturingModeAttributes.getPackageName()).withValue(CameraCommonProviderConstants.CapturingModeColumns.ACTIVITY, capturingModeAttributes.getActivityName()).withValue(CameraCommonProviderConstants.CapturingModeColumns.MODE_NAME, capturingModeAttributes.getModeName()).withValue(CameraCommonProviderConstants.CapturingModeColumns.SORT_ORDER, Integer.valueOf(i)).withValue(CameraCommonProviderConstants.CapturingModeColumns.CAPTURE_TYPE, Integer.valueOf(CaptureTypeCode.toCode(capturingModeAttributes.getInternalCaptureType()))).withValue(CameraCommonProviderConstants.CapturingModeColumns.VISIBILITY_NORMAL, Integer.valueOf(VisibilityTypeCode.toCode(capturingModeAttributes.isVisibleNormal()))).withValue(CameraCommonProviderConstants.CapturingModeColumns.VISIBILITY_ONESHOT, Integer.valueOf(VisibilityTypeCode.toCode(capturingModeAttributes.isVisibleOneshot()))).withValue(CameraCommonProviderConstants.CapturingModeColumns.VISIBILITY_SHORTCUT, Integer.valueOf(VisibilityTypeCode.toCode(capturingModeAttributes.isVisibleShortcut()))).withValue(CameraCommonProviderConstants.CapturingModeColumns.SELECTORICON_ID, Integer.valueOf(capturingModeAttributes.getSelectorIconId())).withValue(CameraCommonProviderConstants.CapturingModeColumns.SELECTORLABEL_ID, Integer.valueOf(capturingModeAttributes.getSelectorLabelId())).withValue(CameraCommonProviderConstants.CapturingModeColumns.DESCRIPTIONLABEL_ID, Integer.valueOf(capturingModeAttributes.getDescriptionLabelId())).withValue(CameraCommonProviderConstants.CapturingModeColumns.SHORTCUTICON_ID, Integer.valueOf(capturingModeAttributes.getShortcutIconId())).withValue(CameraCommonProviderConstants.CapturingModeColumns.SHORTCUTLABEL_ID, Integer.valueOf(capturingModeAttributes.getShortcutLabelId())).build();
    }

    private static ContentProviderOperation createDeleteOperation(long j) {
        if (CamLog.VERBOSE) {
            CamLog.d("createDeleteOperation()");
        }
        return ContentProviderOperation.newDelete(CameraCommonProviderConstants.CAPTURINGMODE_CONTENT_URI).withSelection(WHERE_WITH_ID, new String[]{Long.toString(j)}).build();
    }

    private static class CaptureTypeCode {
        static final int PHOTO = 1;
        static final int VIDEO = 2;

        private CaptureTypeCode() {
        }

        static int toCode(CapturingModeAttributes.InternalCaptureType internalCaptureType) {
            if (internalCaptureType.equals(CapturingModeAttributes.InternalCaptureType.Photo)) {
                return 1;
            }
            if (internalCaptureType.equals(CapturingModeAttributes.InternalCaptureType.Video)) {
                return 2;
            }
            throw new IllegalArgumentException();
        }

        static CapturingModeAttributes.InternalCaptureType toCaptureType(int i) {
            if (i == 1) {
                return CapturingModeAttributes.InternalCaptureType.Photo;
            }
            if (i == 2) {
                return CapturingModeAttributes.InternalCaptureType.Video;
            }
            throw new IllegalArgumentException();
        }
    }

    private static class VisibilityTypeCode {
        static final int FALSE = 0;
        static final int TRUE = 1;

        static int toCode(boolean z) {
            return z ? 1 : 0;
        }

        static boolean toVisibilityType(int i) {
            return i == 1;
        }

        private VisibilityTypeCode() {
        }
    }

    public CapturingModeCollection(ContentResolver contentResolver) {
        this.mResolver = contentResolver;
    }

    public List<CapturingModeAttributes> getCapturingModeList(CapturingModeAttributes.InternalCaptureType[] internalCaptureTypeArr, CapturingModeAttributes.VisibilityType[] visibilityTypeArr) {
        StringBuffer stringBuffer = new StringBuffer();
        if (internalCaptureTypeArr.length > 0) {
            stringBuffer.append("(");
            for (CapturingModeAttributes.InternalCaptureType internalCaptureType : internalCaptureTypeArr) {
                int code = CaptureTypeCode.toCode(internalCaptureType);
                if (internalCaptureTypeArr[0] != internalCaptureType) {
                    stringBuffer.append(" OR ");
                }
                stringBuffer.append("(" + CameraCommonProviderConstants.CapturingModeColumns.CAPTURE_TYPE + "=" + code + ")");
            }
            stringBuffer.append(")");
        }
        if (internalCaptureTypeArr.length > 0 && visibilityTypeArr.length > 0) {
            stringBuffer.append(" AND ");
        }
        if (visibilityTypeArr.length > 0) {
            stringBuffer.append("(");
            for (CapturingModeAttributes.VisibilityType visibilityType : visibilityTypeArr) {
                if (visibilityTypeArr[0] != visibilityType) {
                    stringBuffer.append(" OR ");
                }
                stringBuffer.append("(" + visibilityType.mColumn + "=1)");
            }
            stringBuffer.append(")");
        }
        ArrayList arrayList = new ArrayList();
        Cursor cursorQuery = this.mResolver.query(CameraCommonProviderConstants.CAPTURINGMODE_CONTENT_URI, PROTECTION, stringBuffer.toString(), null, SORT_ASC);
        if (cursorQuery != null) {
            while (cursorQuery.moveToNext()) {
                try {
                    arrayList.add(convert(cursorQuery));
                } finally {
                    cursorQuery.close();
                }
            }
        } else if (CamLog.VERBOSE) {
            CamLog.w("Fail to retrieve the capturing mode list from CameraCommonProvider.");
        }
        return arrayList;
    }

    private void unregisterCapturingModes(List<CapturingModeAttributes> list) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
        for (CapturingModeAttributes capturingModeAttributes : list) {
            if (capturingModeAttributes.getId() != null) {
                arrayList.add(createDeleteOperation(capturingModeAttributes.getId().longValue()));
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        try {
            this.mResolver.applyBatch(CameraCommonProviderConstants.AUTHORITY, arrayList);
        } catch (OperationApplicationException e) {
            CamLog.e("Failed to unregister modes. Message : " + e.getMessage());
        } catch (RemoteException e2) {
            CamLog.e("Failed to unregister modes. Message : " + e2.getMessage());
        }
    }

    private void saveCapturingModeSortedList(List<CapturingModeAttributes> list) throws RemoteException, OperationApplicationException {
        if (CamLog.VERBOSE) {
            CamLog.d("saveCapturingModeList size:" + list.size());
        }
        ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            CapturingModeAttributes capturingModeAttributes = list.get(i);
            if (capturingModeAttributes.getId() != null) {
                arrayList.add(createUpdateSortOrderOperation(capturingModeAttributes, i));
            } else {
                arrayList.add(createInsertOperation(capturingModeAttributes, i));
            }
        }
        try {
            this.mResolver.applyBatch(CameraCommonProviderConstants.AUTHORITY, arrayList);
        } catch (OperationApplicationException e) {
            CamLog.e("Failed to save mode order. Message : " + e.getMessage());
        } catch (RemoteException e2) {
            CamLog.e("Failed to save mode order. Message : " + e2.getMessage());
        } catch (IllegalArgumentException e3) {
            CamLog.e("Failed to save mode order. Message : " + e3.getMessage());
        }
    }

    public static void register(ContentResolver contentResolver, List<CapturingModeAttributes> list, PackageManager packageManager) throws RemoteException, OperationApplicationException {
        CapturingModeCollection capturingModeCollection = new CapturingModeCollection(contentResolver);
        List<CapturingModeAttributes> capturingModeList = capturingModeCollection.getCapturingModeList(new CapturingModeAttributes.InternalCaptureType[0], new CapturingModeAttributes.VisibilityType[0]);
        list.removeAll(findIllegalCapturingMode(packageManager, list));
        List<CapturingModeAttributes> listFindIllegalCapturingMode = findIllegalCapturingMode(packageManager, capturingModeList);
        capturingModeList.removeAll(listFindIllegalCapturingMode);
        capturingModeCollection.unregisterCapturingModes(listFindIllegalCapturingMode);
        boolean z = false;
        for (CapturingModeAttributes capturingModeAttributes : list) {
            if (CamLog.VERBOSE) {
                CamLog.d("register(", " package:", capturingModeAttributes.getPackageName(), " mode:", capturingModeAttributes.getModeName(), " )");
            }
            int iIndexOf = indexOf(capturingModeAttributes, capturingModeList);
            if (iIndexOf == -1) {
                if (CamLog.VERBOSE) {
                    CamLog.d("  This mode has not been registered.");
                }
                capturingModeList.add(capturingModeAttributes);
                if (CapturingModeUtil.hasDefaultSortOrder(capturingModeAttributes)) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("  This mode is pre-installed.");
                    }
                    capturingModeList = CapturingModeUtil.sortCapturingMode(capturingModeList);
                }
            } else {
                if (CamLog.VERBOSE) {
                    CamLog.d("  This mode has been registered.");
                }
                if (isResourceUpdated(capturingModeList.get(iIndexOf), capturingModeAttributes)) {
                    if (CamLog.VERBOSE) {
                        CamLog.d("  This mode is updated.");
                    }
                    capturingModeList.set(iIndexOf, capturingModeAttributes);
                } else if (CamLog.VERBOSE) {
                    CamLog.d("  This mode is not updated, so not need register.");
                }
            }
            z = true;
        }
        if (z) {
            if (CamLog.VERBOSE) {
                CamLog.d("Update data base.");
            }
            capturingModeCollection.saveCapturingModeSortedList(capturingModeList);
        }
    }

    private static int indexOf(CapturingModeAttributes capturingModeAttributes, List<CapturingModeAttributes> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).is(capturingModeAttributes.getPackageName(), capturingModeAttributes.getModeName())) {
                return i;
            }
        }
        return -1;
    }

    private static List<CapturingModeAttributes> findIllegalCapturingMode(PackageManager packageManager, List<CapturingModeAttributes> list) {
        ArrayList arrayList = new ArrayList();
        if (packageManager == null) {
            return arrayList;
        }
        for (CapturingModeAttributes capturingModeAttributes : list) {
            if (isIllegalCapturingMode(packageManager, capturingModeAttributes)) {
                CamLog.e(TAG, "This attributes is illegal. [" + capturingModeAttributes.getAttributes() + "]");
                arrayList.add(capturingModeAttributes);
            }
        }
        return arrayList;
    }

    private static boolean isIllegalCapturingMode(PackageManager packageManager, CapturingModeAttributes capturingModeAttributes) throws PackageManager.NameNotFoundException {
        Resources resourcesForApplication;
        try {
            resourcesForApplication = packageManager.getResourcesForApplication(capturingModeAttributes.getPackageName());
        } catch (PackageManager.NameNotFoundException unused) {
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
        if (!capturingModeAttributes.isVisibleShortcut()) {
            return false;
        }
        if (!ResourceUtil.isDrawableResource(resourcesForApplication, capturingModeAttributes.getShortcutIconId())) {
            CamLog.e("Resource type of shortcut icon is not drawable.");
            return true;
        }
        if (ResourceUtil.isStringResource(resourcesForApplication, capturingModeAttributes.getShortcutLabelId())) {
            return false;
        }
        CamLog.e("Resource type of shortcut label is not string.");
        return true;
    }

    private static boolean isResourceUpdated(CapturingModeAttributes capturingModeAttributes, CapturingModeAttributes capturingModeAttributes2) {
        return (capturingModeAttributes.getActivityName().equals(capturingModeAttributes2.getActivityName()) && capturingModeAttributes.getSelectorIconId() == capturingModeAttributes2.getSelectorIconId() && capturingModeAttributes.getSelectorLabelId() == capturingModeAttributes2.getSelectorLabelId() && capturingModeAttributes.getShortcutIconId() == capturingModeAttributes2.getShortcutIconId() && capturingModeAttributes.getShortcutLabelId() == capturingModeAttributes2.getShortcutLabelId() && capturingModeAttributes.getInternalCaptureType() == capturingModeAttributes2.getInternalCaptureType() && capturingModeAttributes.isVisibleNormal() == capturingModeAttributes2.isVisibleNormal() && capturingModeAttributes.isVisibleOneshot() == capturingModeAttributes2.isVisibleOneshot() && capturingModeAttributes.isVisibleShortcut() == capturingModeAttributes2.isVisibleShortcut()) ? false : true;
    }
}
