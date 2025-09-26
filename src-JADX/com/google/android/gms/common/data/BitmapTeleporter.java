package com.google.android.gms.common.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.util.Log;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class BitmapTeleporter implements SafeParcelable {
    public static final Parcelable.Creator<BitmapTeleporter> CREATOR = new zza();
    final int mVersionCode;
    ParcelFileDescriptor zzFc;
    final int zzWJ;
    private Bitmap zzadf;
    private boolean zzadg;
    private File zzadh;

    BitmapTeleporter(int i, ParcelFileDescriptor parcelFileDescriptor, int i2) {
        this.mVersionCode = i;
        this.zzFc = parcelFileDescriptor;
        this.zzWJ = i2;
        this.zzadf = null;
        this.zzadg = false;
    }

    public BitmapTeleporter(Bitmap bitmap) {
        this.mVersionCode = 1;
        this.zzFc = null;
        this.zzWJ = 0;
        this.zzadf = bitmap;
        this.zzadg = true;
    }

    private void zza(Closeable closeable) throws IOException {
        try {
            closeable.close();
        } catch (IOException e) {
            Log.w("BitmapTeleporter", "Could not close stream", e);
        }
    }

    private FileOutputStream zzot() throws IOException {
        if (this.zzadh == null) {
            throw new IllegalStateException("setTempDir() must be called before writing this object to a parcel");
        }
        try {
            File fileCreateTempFile = File.createTempFile("teleporter", ".tmp", this.zzadh);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(fileCreateTempFile);
                this.zzFc = ParcelFileDescriptor.open(fileCreateTempFile, 268435456);
                fileCreateTempFile.delete();
                return fileOutputStream;
            } catch (FileNotFoundException unused) {
                throw new IllegalStateException("Temporary file is somehow already deleted");
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not create temporary file", e);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public void release() throws IOException {
        if (this.zzadg) {
            return;
        }
        try {
            this.zzFc.close();
        } catch (IOException e) {
            Log.w("BitmapTeleporter", "Could not close PFD", e);
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) throws IOException {
        if (this.zzFc == null) {
            Bitmap bitmap = this.zzadf;
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(bitmap.getRowBytes() * bitmap.getHeight());
            bitmap.copyPixelsToBuffer(byteBufferAllocate);
            byte[] bArrArray = byteBufferAllocate.array();
            DataOutputStream dataOutputStream = new DataOutputStream(zzot());
            try {
                try {
                    dataOutputStream.writeInt(bArrArray.length);
                    dataOutputStream.writeInt(bitmap.getWidth());
                    dataOutputStream.writeInt(bitmap.getHeight());
                    dataOutputStream.writeUTF(bitmap.getConfig().toString());
                    dataOutputStream.write(bArrArray);
                } catch (IOException e) {
                    throw new IllegalStateException("Could not write into unlinked file", e);
                }
            } finally {
                zza(dataOutputStream);
            }
        }
        zza.zza(this, parcel, i | 1);
        this.zzFc = null;
    }

    public void zzc(File file) {
        if (file == null) {
            throw new NullPointerException("Cannot set null temp directory");
        }
        this.zzadh = file;
    }

    public Bitmap zzos() throws IOException {
        if (!this.zzadg) {
            DataInputStream dataInputStream = new DataInputStream(new ParcelFileDescriptor.AutoCloseInputStream(this.zzFc));
            try {
                try {
                    byte[] bArr = new byte[dataInputStream.readInt()];
                    int i = dataInputStream.readInt();
                    int i2 = dataInputStream.readInt();
                    Bitmap.Config configValueOf = Bitmap.Config.valueOf(dataInputStream.readUTF());
                    dataInputStream.read(bArr);
                    zza(dataInputStream);
                    ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
                    Bitmap bitmapCreateBitmap = Bitmap.createBitmap(i, i2, configValueOf);
                    bitmapCreateBitmap.copyPixelsFromBuffer(byteBufferWrap);
                    this.zzadf = bitmapCreateBitmap;
                    this.zzadg = true;
                } catch (IOException e) {
                    throw new IllegalStateException("Could not read from parcel file descriptor", e);
                }
            } catch (Throwable th) {
                zza(dataInputStream);
                throw th;
            }
        }
        return this.zzadf;
    }
}
