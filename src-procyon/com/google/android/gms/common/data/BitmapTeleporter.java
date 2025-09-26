// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.common.data;

import android.graphics.Bitmap$Config;
import java.io.InputStream;
import java.io.DataInputStream;
import android.os.ParcelFileDescriptor$AutoCloseInputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import android.os.Parcel;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import android.graphics.Bitmap;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable$Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class BitmapTeleporter implements SafeParcelable
{
    public static final Parcelable$Creator<BitmapTeleporter> CREATOR;
    final int mVersionCode;
    ParcelFileDescriptor zzFc;
    final int zzWJ;
    private Bitmap zzadf;
    private boolean zzadg;
    private File zzadh;
    
    static {
        CREATOR = (Parcelable$Creator)new zza();
    }
    
    BitmapTeleporter(final int mVersionCode, final ParcelFileDescriptor zzFc, final int zzWJ) {
        this.mVersionCode = mVersionCode;
        this.zzFc = zzFc;
        this.zzWJ = zzWJ;
        this.zzadf = null;
        this.zzadg = false;
    }
    
    public BitmapTeleporter(final Bitmap zzadf) {
        this.mVersionCode = 1;
        this.zzFc = null;
        this.zzWJ = 0;
        this.zzadf = zzadf;
        this.zzadg = true;
    }
    
    private void zza(final Closeable closeable) {
        try {
            closeable.close();
        }
        catch (final IOException ex) {
            Log.w("BitmapTeleporter", "Could not close stream", (Throwable)ex);
        }
    }
    
    private FileOutputStream zzot() {
        if (this.zzadh == null) {
            throw new IllegalStateException("setTempDir() must be called before writing this object to a parcel");
        }
        try {
            final File tempFile = File.createTempFile("teleporter", ".tmp", this.zzadh);
            try {
                final FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
                this.zzFc = ParcelFileDescriptor.open(tempFile, 268435456);
                tempFile.delete();
                return fileOutputStream;
            }
            catch (final FileNotFoundException ex) {
                throw new IllegalStateException("Temporary file is somehow already deleted");
            }
        }
        catch (final IOException cause) {
            throw new IllegalStateException("Could not create temporary file", cause);
        }
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void release() {
        if (!this.zzadg) {
            try {
                this.zzFc.close();
            }
            catch (final IOException ex) {
                Log.w("BitmapTeleporter", "Could not close PFD", (Throwable)ex);
            }
        }
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        if (this.zzFc == null) {
            final Bitmap zzadf = this.zzadf;
            final ByteBuffer allocate = ByteBuffer.allocate(zzadf.getRowBytes() * zzadf.getHeight());
            zzadf.copyPixelsToBuffer((Buffer)allocate);
            final byte[] array = allocate.array();
            final DataOutputStream dataOutputStream = new DataOutputStream(this.zzot());
            try {
                try {
                    dataOutputStream.writeInt(array.length);
                    dataOutputStream.writeInt(zzadf.getWidth());
                    dataOutputStream.writeInt(zzadf.getHeight());
                    dataOutputStream.writeUTF(zzadf.getConfig().toString());
                    dataOutputStream.write(array);
                    this.zza(dataOutputStream);
                }
                finally {}
            }
            catch (final IOException cause) {
                throw new IllegalStateException("Could not write into unlinked file", cause);
            }
            this.zza(dataOutputStream);
        }
        zza.zza(this, parcel, n | 0x1);
        this.zzFc = null;
    }
    
    public void zzc(final File zzadh) {
        if (zzadh == null) {
            throw new NullPointerException("Cannot set null temp directory");
        }
        this.zzadh = zzadh;
    }
    
    public Bitmap zzos() {
        if (!this.zzadg) {
            Object wrap = new DataInputStream((InputStream)new ParcelFileDescriptor$AutoCloseInputStream(this.zzFc));
            try {
                try {
                    final byte[] array = new byte[((DataInputStream)wrap).readInt()];
                    final int int1 = ((DataInputStream)wrap).readInt();
                    final int int2 = ((DataInputStream)wrap).readInt();
                    final Bitmap$Config value = Bitmap$Config.valueOf(((DataInputStream)wrap).readUTF());
                    ((DataInputStream)wrap).read(array);
                    this.zza((Closeable)wrap);
                    wrap = ByteBuffer.wrap(array);
                    final Bitmap bitmap = Bitmap.createBitmap(int1, int2, value);
                    bitmap.copyPixelsFromBuffer((Buffer)wrap);
                    this.zzadf = bitmap;
                    this.zzadg = true;
                }
                finally {}
            }
            catch (final IOException cause) {
                throw new IllegalStateException("Could not read from parcel file descriptor", cause);
            }
            this.zza((Closeable)wrap);
        }
        return this.zzadf;
    }
}
