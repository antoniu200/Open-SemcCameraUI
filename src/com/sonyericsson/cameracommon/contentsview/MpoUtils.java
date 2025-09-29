package com.sonyericsson.cameracommon.contentsview;

import com.sonyericsson.android.camera.util.CamLog;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Locale;

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

    // inside MpoUtils.java
    public static int getType(String path) throws Throwable {
        int UNKNOWN = 0;

        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(path, "r");
            final byte[] marker = new byte[2];

            // read marker stream
            for (;;) {
                int read = raf.read(marker);
                if (read < 2) {
                    // EOF or short read → unknown
                    break;
                }

                if (CamLog.VERBOSE) {
                    CamLog.d(new String[] {
                            "read:" + Integer.toHexString(marker[0] & 0xFF) + " "
                                    + Integer.toHexString(marker[1] & 0xFF)
                    });
                }

                // SOI
                if (isSOI(marker[0], marker[1])) {
                    if (CamLog.VERBOSE) {
                        CamLog.d(new String[] { "This segments is SOI." });
                    }
                    continue;
                }

                // EOI
                if (isEOI(marker[0], marker[1])) {
                    if (CamLog.VERBOSE) {
                        CamLog.d(new String[] { "This segments is EOI." });
                    }
                    continue;
                }

                // APPn
                if (isAPP(marker[0], marker[1])) {
                    if (CamLog.VERBOSE) {
                        // byte is signed; +32 maps E0..EF → 0..15
                        CamLog.d(new String[] {
                                String.format(Locale.UK, "This segments is APP%d.", marker[1] + 32)
                        });
                    }

                    // remember start of this segment + size to jump to next
                    long nextPos = raf.getFilePointer() + (raf.readShort() & 0xFFFF);

                    // APP2 → MPF check
                    if (isAPP2(marker[0], marker[1]) && checkFormatIdentifier(raf)) {
                        if (CamLog.VERBOSE) {
                            CamLog.d(new String[] { "This section has MPF." });
                        }

                        raf.readShort();     // endian tag (already validated by checkFormatIdentifier)
                        skip(raf, 6);        // offset to IFD0
                        short count = raf.readShort();

                        for (int i = 0; i < count; i++) {
                            if (checkMPEntryTag(raf)) {
                                if (CamLog.VERBOSE) {
                                    CamLog.d(new String[] { "This tag is MP entry." });
                                }
                                skip(raf, 2);                 // type
                                int flags = raf.readInt();    // contains number-of-images * 16
                                int num = flags / 16;
                                int type = typeFromEntries(num);

                                try { raf.close(); } catch (IOException ignore) {}
                                return type;
                            } else {
                                if (CamLog.VERBOSE) {
                                    CamLog.d(new String[] { "This tag is not MP entry." });
                                }
                                skip(raf, 10); // move to next tag
                            }
                        }
                    }

                    // skip to next segment
                    raf.seek(nextPos);
                    continue;
                }

                // Unknown marker
                if (CamLog.VERBOSE) {
                    CamLog.d(new String[] { "Found unknown marker." });
                }
                // fall through; loop continues
            }

            // no type detected
            return UNKNOWN;

        } catch (IOException e) {
            CamLog.e(new String[] {
                    "Fail to analize a mpo file by IO Exception. message:" + e.getMessage()
            });
            if (CamLog.VERBOSE) {
                CamLog.d(new String[] { "This mpo is unknown image." });
            }
            return UNKNOWN;

        } catch (Throwable t) {
            // close then rethrow to honor the throws Throwable contract
            if (raf != null) {
                try { raf.close(); } catch (IOException ignore) {}
            }
            throw t;

        } finally {
            if (raf != null) {
                try { raf.close(); } catch (IOException ignore) {}
            }
        }
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
