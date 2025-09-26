// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.activity;

import android.text.TextUtils;
import android.os.Bundle;
import android.net.Uri;
import android.os.Environment;
import java.io.File;
import com.sonyericsson.android.camera.util.CamLog;
import java.util.Iterator;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.os.storage.StorageVolume;
import android.os.storage.StorageManager;
import android.content.Intent;
import android.app.Activity;

public class RequestPermissionSdCardActivity extends Activity
{
    public static final String EXTERNAL_STORAGE_PROVIDER_AUTHORITY = "com.android.externalstorage.documents";
    public static final String EXTRA_SHOW_ADVANCED = "android.provider.extra.SHOW_ADVANCED";
    public static final String EXTRA_UUID = "extra_key_uuid";
    private final int FLAG_SD_PERMISSION;
    private final int REQUEST_CODE_SD_CARD_GRANTED;
    
    public RequestPermissionSdCardActivity() {
        this.REQUEST_CODE_SD_CARD_GRANTED = 256;
        this.FLAG_SD_PERMISSION = 3;
    }
    
    private void finish(final int n) {
        this.setResult(n, new Intent());
        this.finish();
    }
    
    private void requestPermissionSdCard(final String anObject) {
        for (final StorageVolume storageVolume : ((StorageManager)this.getSystemService("storage")).getStorageVolumes()) {
            if (storageVolume != null && storageVolume.isRemovable() && storageVolume.getUuid().equals(anObject)) {
                this.startActivityForResult(new Intent("android.intent.action.OPEN_DOCUMENT_TREE").putExtra("android.provider.extra.INITIAL_URI", (Parcelable)DocumentsContract.buildRootUri("com.android.externalstorage.documents", anObject)).putExtra("android.provider.extra.SHOW_ADVANCED", true), 256);
                break;
            }
        }
    }
    
    public void onActivityResult(int flags, final int i, final Intent intent) {
        if (CamLog.VERBOSE) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onActivityResult: requestCode: ");
            sb.append(flags);
            sb.append(", resultCode: ");
            sb.append(i);
            CamLog.d(sb.toString());
        }
        super.onActivityResult(flags, i, intent);
        if (flags == 256) {
            switch (i) {
                case 0: {
                    this.finish(0);
                    break;
                }
                case -1: {
                    final Uri data = intent.getData();
                    final String stringExtra = this.getIntent().getStringExtra("extra_key_uuid");
                    final File file = new File(data.getPath());
                    final String name = file.getName();
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(stringExtra);
                    sb2.append(":");
                    if (!name.equals(sb2.toString())) {
                        final String name2 = file.getName();
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append(stringExtra);
                        sb3.append(":");
                        sb3.append(Environment.DIRECTORY_DCIM);
                        if (!name2.equals(sb3.toString())) {
                            this.finish(0);
                            break;
                        }
                    }
                    flags = intent.getFlags();
                    this.getContentResolver().takePersistableUriPermission(data, flags & 0x3);
                    if (CamLog.DEBUG) {
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("Storage URI Permissin granted :");
                        sb4.append(data);
                        CamLog.d(sb4.toString());
                    }
                    this.finish(-1);
                    break;
                }
            }
        }
    }
    
    protected void onCreate(final Bundle bundle) {
        if (CamLog.VERBOSE) {
            CamLog.d("onCreate() start");
        }
        super.onCreate(bundle);
        this.setContentView(2131492892);
        final String stringExtra = this.getIntent().getStringExtra("extra_key_uuid");
        if (!TextUtils.isEmpty((CharSequence)stringExtra)) {
            this.requestPermissionSdCard(stringExtra);
        }
        else {
            this.finish(0);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("onCreate() end");
        }
    }
    
    protected void onStop() {
        if (CamLog.VERBOSE) {
            CamLog.d("onStop() start");
        }
        super.onStop();
        if (CamLog.VERBOSE) {
            CamLog.d("onStop() end");
        }
    }
}
