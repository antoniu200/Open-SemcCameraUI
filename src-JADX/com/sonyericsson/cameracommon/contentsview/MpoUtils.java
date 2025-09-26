package com.sonyericsson.cameracommon.contentsview;

import com.sonyericsson.android.camera.util.CamLog;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class MpoUtils {
    public static final int MULTIANGLE = 2;
    public static final int STEREO = 1;
    public static final String TAG = "MpoUtils";
    public static final int UNKNOWN = 0;

    static boolean isAPP(byte b, byte b2) {
        return b == -1 && b2 >= -32 && b2 <= -17;
    }

    static boolean isAPP2(byte b, byte b2) {
        return b == -1 && b2 == -30;
    }

    static boolean isEOI(byte b, byte b2) {
        return b == -1 && b2 == -39;
    }

    static boolean isSOI(byte b, byte b2) {
        return b == -1 && b2 == -40;
    }

    private MpoUtils() {
    }

    /* JADX WARN: Code restructure failed: missing block: B:52:0x0122, code lost:
    
        if (r3 == null) goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x0124, code lost:
    
        r3.close();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int getType(java.lang.String r9) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 358
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sonyericsson.cameracommon.contentsview.MpoUtils.getType(java.lang.String):int");
    }

    private static class JpegMaker {
        static final byte APP0 = -32;
        static final byte APP15 = -17;
        static final byte APP2 = -30;
        static final byte EOI = -39;
        static final byte MARKER = -1;
        static final byte SOI = -40;

        private JpegMaker() {
        }
    }

    static int typeFromEntries(int i) {
        if (i == 2) {
            if (CamLog.VERBOSE) {
                CamLog.d("This mpo is stereo image. entries:" + i);
            }
            return 1;
        }
        if (i == 15) {
            if (CamLog.VERBOSE) {
                CamLog.d("This mpo is multi angle image. entries:" + i);
            }
            return 2;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("This mpo is unknown image. entries:" + i);
        }
        return 0;
    }

    static boolean checkFormatIdentifier(RandomAccessFile randomAccessFile) throws IOException {
        byte[] bArr = new byte[4];
        return bArr.length == randomAccessFile.read(bArr) && bArr[0] == 77 && bArr[1] == 80 && bArr[2] == 70 && bArr[3] == 0;
    }

    static boolean checkMPEntryTag(RandomAccessFile randomAccessFile) throws IOException {
        return 45058 == (randomAccessFile.readShort() & 65535);
    }

    static void skip(RandomAccessFile randomAccessFile, int i) throws IOException {
        if (i != randomAccessFile.skipBytes(i)) {
            throw new EOFException();
        }
    }
}
