// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.gms.internal;

import java.io.ByteArrayOutputStream;
import android.os.ParcelFileDescriptor;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;

public final class zzmt
{
    public static long zza(final InputStream inputStream, final OutputStream outputStream, final boolean b) throws IOException {
        return zza(inputStream, outputStream, b, 1024);
    }
    
    public static long zza(final InputStream inputStream, final OutputStream outputStream, final boolean b, int read) throws IOException {
        final byte[] array = new byte[read];
        long n = 0L;
        try {
            while (true) {
                read = inputStream.read(array, 0, array.length);
                if (read == -1) {
                    break;
                }
                n += read;
                outputStream.write(array, 0, read);
            }
            return n;
        }
        finally {
            if (b) {
                zzb(inputStream);
                zzb(outputStream);
            }
        }
    }
    
    public static void zza(final ParcelFileDescriptor parcelFileDescriptor) {
        if (parcelFileDescriptor == null) {
            return;
        }
        try {
            parcelFileDescriptor.close();
        }
        catch (final IOException ex) {}
    }
    
    public static byte[] zza(final InputStream inputStream, final boolean b) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        zza(inputStream, byteArrayOutputStream, b);
        return byteArrayOutputStream.toByteArray();
    }
    
    public static void zzb(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        }
        catch (final IOException ex) {}
    }
    
    public static byte[] zzk(final InputStream inputStream) throws IOException {
        return zza(inputStream, true);
    }
}
