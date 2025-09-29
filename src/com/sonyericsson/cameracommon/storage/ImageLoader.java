package com.sonyericsson.cameracommon.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.cameracommon.utility.RotationUtil;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InvalidObjectException;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class ImageLoader {
    private static final int FIRST_REDUCE_RATIO_FULL_IMG = 2;
    private static final int FULL_SIZE_MAX_LENGTH = 1025;
    public static final String TAG = "ImageLoader";
    private final Context mContext;
    private final byte[] mImageData;
    private final BitmapFactory.Options mOption;
    private final int mOrientation;
    private final Uri mUri;

    public ImageLoader(Context context, Uri uri, int i) {
        this.mContext = context;
        this.mUri = uri;
        this.mImageData = null;
        this.mOrientation = i;
        this.mOption = new BitmapFactory.Options();
    }

    public ImageLoader(Context context, byte[] bArr, int i) {
        this.mContext = context;
        this.mUri = null;
        this.mImageData = bArr;
        this.mOrientation = i;
        this.mOption = new BitmapFactory.Options();
    }

	public android.graphics.Bitmap load() throws java.lang.Throwable {
		if (CamLog.VERBOSE) {
			CamLog.d(new String[] { "Loading full size image started" });
		}

		android.graphics.Bitmap result = null;
		java.io.InputStream current = null;

		try {
			if (CamLog.VERBOSE) {
				CamLog.d(new String[] { "Start loading original image:" + mUri });
			}

			// === Phase 1: bounds-only open ===
			current = (mImageData != null)
					? new java.io.ByteArrayInputStream(mImageData)
					: com.sonyericsson.cameracommon.storage.ContentResolverUtil
							.crOpenInputStream(mContext, mUri);

			if (current != null) {
				try {
					calcBounds(current, mOption);
					mOption.inSampleSize = calcRatio(mOption, mOption.inSampleSize, 0x401);
				} finally {
					try { current.close(); } catch (Exception ignore) {}
					current = null;
				}
			}

			// === Phase 2: decode open ===
			java.io.InputStream decodeIn = (mImageData != null)
					? new java.io.ByteArrayInputStream(mImageData)
					: com.sonyericsson.cameracommon.storage.ContentResolverUtil
							.crOpenInputStream(mContext, mUri);

			if (decodeIn != null) {
				try {
					result = loadFullSize(decodeIn, mOption);
				} catch (java.io.InvalidObjectException ioe) {
					CamLog.e(new String[] { "Load full size error:" + ioe });
				} finally {
					try { decodeIn.close(); }
					catch (Exception ex) { CamLog.e("Close stream failed:" + ex.toString(), ex); }
				}
			}

			if (CamLog.VERBOSE) {
				CamLog.d(new String[] { "Loading full size image finished" });
			}
			return result;

		} catch (java.io.InvalidObjectException e) {
			CamLog.e(new String[] { "Load full size error:" + e });
			if (current != null) try { current.close(); } catch (Exception ex) { CamLog.e("Close stream failed:" + ex.toString(), ex); }
			return result;

		} catch (java.io.FileNotFoundException e) {
			CamLog.e(new String[] { "File not found:" + mUri });
			if (current != null) try { current.close(); } catch (Exception ex) { CamLog.e("Close stream failed:" + ex.toString(), ex); }
			return result;

		} catch (java.io.IOException e) {
			CamLog.e(new String[] { "Close failed:" + mUri });
			if (current != null) try { current.close(); } catch (Exception ex) { CamLog.e("Close stream failed:" + ex.toString(), ex); }
			return result;

		} catch (java.lang.IllegalArgumentException e) {
			CamLog.e(new String[] { "Maybe File access error." });
			if (current != null) try { current.close(); } catch (Exception ex) { CamLog.e("Close stream failed:" + ex.toString(), ex); }
			return result;

		} catch (java.lang.Throwable t) {
			if (current != null) try { current.close(); } catch (Exception ex) { CamLog.e("Close stream failed:" + ex.toString(), ex); }
			throw t; // required by the signature you asked to keep
		}
	}

    private void calcBounds(InputStream inputStream, BitmapFactory.Options options) throws InvalidObjectException, FileNotFoundException {
        if (CamLog.VERBOSE) {
            CamLog.d("calcBounds()");
        }
        options.inSampleSize = 2;
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmapDecodeStream = decodeStream(inputStream, options);
        if (bitmapDecodeStream != null && !bitmapDecodeStream.isRecycled()) {
            bitmapDecodeStream.recycle();
        }
        if (options.outWidth == -1 || options.outHeight == -1) {
            CamLog.e("Bitmap read error");
            throw new InvalidObjectException("Failed to calculate bounds of bitmap");
        }
        if (CamLog.VERBOSE) {
            CamLog.d("BMP out height:" + options.outHeight);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("BMP out width:" + options.outWidth);
        }
        if (CamLog.VERBOSE) {
            CamLog.d("Scale ratio:" + options.inSampleSize);
        }
    }

    private Bitmap loadFullSize(InputStream inputStream, BitmapFactory.Options options) throws InvalidObjectException, FileNotFoundException {
        if (CamLog.VERBOSE) {
            CamLog.d("loadFullSize()");
        }
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmapDecodeStream = decodeStream(inputStream, options);
        if (bitmapDecodeStream == null) {
            CamLog.e("loadFullSize: Decode read error");
            throw new InvalidObjectException("Failed to decode full size image");
        }
        CamLog.d("loadFullSize: mOrientation", RotationUtil.orientationToString(this.mOrientation));
        if (this.mOrientation == 0) {
            return bitmapDecodeStream;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(this.mOrientation, bitmapDecodeStream.getWidth() / 2.0f, bitmapDecodeStream.getHeight() / 2.0f);
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(bitmapDecodeStream, 0, 0, bitmapDecodeStream.getWidth(), bitmapDecodeStream.getHeight(), matrix, false);
        bitmapDecodeStream.recycle();
        Bitmap bitmapCopy = bitmapCreateBitmap.copy(Bitmap.Config.ARGB_8888, false);
        bitmapCreateBitmap.recycle();
        return bitmapCopy;
    }

    private Bitmap decodeStream(InputStream inputStream, BitmapFactory.Options options) throws FileNotFoundException {
        Rect rect = new Rect(0, 0, 0, 0);
        if (CamLog.VERBOSE) {
            CamLog.d("Loading full size image started");
        }
        Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(inputStream, rect, options);
        if (CamLog.VERBOSE) {
            CamLog.d("Loading full size image finished");
        }
        return bitmapDecodeStream;
    }

    private int calcRatio(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outWidth * i;
        int i4 = options.outHeight * i;
        int iMax = Math.max(((i4 + i2) - 1) / i2, ((i3 + i2) - 1) / i2);
        if (iMax == 0) {
            if (CamLog.VERBOSE) {
                CamLog.d("Full size image loading ratio: error");
            }
            return 1;
        }
        if (iMax > 1 && (i3 / iMax > i2 || i4 / iMax > i2)) {
            iMax--;
        }
        if (CamLog.VERBOSE) {
            CamLog.d("Full size image loading ratio:" + iMax);
        }
        return iMax;
    }
}
