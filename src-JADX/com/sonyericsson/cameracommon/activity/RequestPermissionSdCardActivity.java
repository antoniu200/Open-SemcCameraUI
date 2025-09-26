package com.sonyericsson.cameracommon.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.util.CamLog;
import java.io.File;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RequestPermissionSdCardActivity extends Activity {
    public static final String EXTERNAL_STORAGE_PROVIDER_AUTHORITY = "com.android.externalstorage.documents";
    public static final String EXTRA_SHOW_ADVANCED = "android.provider.extra.SHOW_ADVANCED";
    public static final String EXTRA_UUID = "extra_key_uuid";
    private final int REQUEST_CODE_SD_CARD_GRANTED = 256;
    private final int FLAG_SD_PERMISSION = 3;

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        if (CamLog.VERBOSE) {
            CamLog.d("onCreate() start");
        }
        super.onCreate(bundle);
        setContentView(R.layout.activity_request_permission);
        String stringExtra = getIntent().getStringExtra(EXTRA_UUID);
        if (!TextUtils.isEmpty(stringExtra)) {
            requestPermissionSdCard(stringExtra);
        } else {
            finish(0);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("onCreate() end");
        }
    }

    private void finish(int i) {
        setResult(i, new Intent());
        finish();
    }

    private void requestPermissionSdCard(String str) {
        for (StorageVolume storageVolume : ((StorageManager) getSystemService("storage")).getStorageVolumes()) {
            if (storageVolume != null && storageVolume.isRemovable() && storageVolume.getUuid().equals(str)) {
                startActivityForResult(new Intent("android.intent.action.OPEN_DOCUMENT_TREE").putExtra("android.provider.extra.INITIAL_URI", DocumentsContract.buildRootUri(EXTERNAL_STORAGE_PROVIDER_AUTHORITY, str)).putExtra(EXTRA_SHOW_ADVANCED, true), 256);
                return;
            }
        }
    }

    @Override // android.app.Activity
    protected void onStop() {
        if (CamLog.VERBOSE) {
            CamLog.d("onStop() start");
        }
        super.onStop();
        if (CamLog.VERBOSE) {
            CamLog.d("onStop() end");
        }
    }

    @Override // android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        if (CamLog.VERBOSE) {
            CamLog.d("onActivityResult: requestCode: " + i + ", resultCode: " + i2);
        }
        super.onActivityResult(i, i2, intent);
        if (i != 256) {
            return;
        }
        switch (i2) {
            case -1:
                Uri data = intent.getData();
                String stringExtra = getIntent().getStringExtra(EXTRA_UUID);
                File file = new File(data.getPath());
                if (!file.getName().equals(stringExtra + ":")) {
                    if (!file.getName().equals(stringExtra + ":" + Environment.DIRECTORY_DCIM)) {
                        finish(0);
                        break;
                    }
                }
                getContentResolver().takePersistableUriPermission(data, intent.getFlags() & 3);
                if (CamLog.DEBUG) {
                    CamLog.d("Storage URI Permissin granted :" + data);
                }
                finish(-1);
                break;
            case 0:
                finish(0);
                break;
        }
    }
}
