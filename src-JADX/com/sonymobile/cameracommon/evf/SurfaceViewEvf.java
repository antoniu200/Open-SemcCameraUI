package com.sonymobile.cameracommon.evf;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonymobile.cameracommon.evf.Evf;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SurfaceViewEvf implements Evf {
    private static final String TAG = "SurfaceViewEvf";
    private Size mSurfaceSize;
    private SurfaceView mSurfaceView = null;
    private Rect mEvfRect = new Rect();
    private boolean mIsSurfaceAvailable = false;
    private SurfaceViewCallback mSurfaceViewCallback = new SurfaceViewCallback();
    private Evf.LifeCycleCallback mLifeCycleCallback = null;

    @Override // com.sonymobile.cameracommon.evf.Evf
    public void show() {
        if (CamLog.DEBUG) {
            CamLog.d("show()");
        }
        if (this.mSurfaceView != null) {
            this.mSurfaceView.setVisibility(0);
        }
    }

    @Override // com.sonymobile.cameracommon.evf.Evf
    public void hide() {
        if (CamLog.DEBUG) {
            CamLog.d("hide()");
        }
        if (this.mSurfaceView != null) {
            this.mSurfaceView.setVisibility(8);
        }
    }

    @Override // com.sonymobile.cameracommon.evf.Evf
    public boolean isShown() {
        return this.mSurfaceView != null && this.mSurfaceView.getVisibility() == 0;
    }

    @Override // com.sonymobile.cameracommon.evf.Evf
    public View asView() {
        if (CamLog.DEBUG) {
            CamLog.d("asView()");
        }
        return this.mSurfaceView;
    }

    @Override // com.sonymobile.cameracommon.evf.Evf
    public Rect getRect() {
        this.mEvfRect.set(this.mSurfaceView.getLeft(), this.mSurfaceView.getTop(), this.mSurfaceView.getRight(), this.mSurfaceView.getBottom());
        if (CamLog.DEBUG) {
            CamLog.d("getRect() : Rect=" + this.mEvfRect.toString());
        }
        return this.mEvfRect;
    }

    @Override // com.sonymobile.cameracommon.evf.Evf
    public void resize(int i, int i2) {
        if (CamLog.DEBUG) {
            CamLog.d("resize() : Width=" + i + ", Height = " + i2);
        }
        ViewGroup.LayoutParams layoutParams = this.mSurfaceView.getLayoutParams();
        layoutParams.width = i;
        layoutParams.height = i2;
        this.mSurfaceView.setLayoutParams(layoutParams);
    }

    @Override // com.sonymobile.cameracommon.evf.Evf
    public void setLifeCycleCallback(Evf.LifeCycleCallback lifeCycleCallback) {
        this.mLifeCycleCallback = lifeCycleCallback;
    }

    @Override // com.sonymobile.cameracommon.evf.Evf
    public void onCreate(Context context) {
        if (CamLog.DEBUG) {
            CamLog.d("onCreate() : E");
        }
        this.mSurfaceView = new SurfaceView(context);
        this.mSurfaceView.setVisibility(4);
        this.mSurfaceView.getHolder().addCallback(this.mSurfaceViewCallback);
        if (CamLog.DEBUG) {
            CamLog.d("onCreate() : X");
        }
    }

    @Override // com.sonymobile.cameracommon.evf.Evf
    public void onResume() {
        if (CamLog.DEBUG) {
            CamLog.d("onResume() : E");
        }
        if (CamLog.DEBUG) {
            CamLog.d("onResume() : X");
        }
    }

    @Override // com.sonymobile.cameracommon.evf.Evf
    public void onPause() {
        if (CamLog.DEBUG) {
            CamLog.d("onPause() : E");
        }
        if (CamLog.DEBUG) {
            CamLog.d("onPause() : X");
        }
    }

    @Override // com.sonymobile.cameracommon.evf.Evf
    public void onDestroy() {
        if (CamLog.DEBUG) {
            CamLog.d("onDestroy() : E");
        }
        if (this.mSurfaceView != null) {
            this.mSurfaceView.getHolder().removeCallback(this.mSurfaceViewCallback);
            this.mSurfaceView = null;
        }
        this.mLifeCycleCallback = null;
        if (CamLog.DEBUG) {
            CamLog.d("onDestroy() : X");
        }
    }

    @Override // com.sonymobile.cameracommon.evf.Evf
    public synchronized void clear() {
        SurfaceHolder holder;
        Canvas canvasLockCanvas;
        if (this.mIsSurfaceAvailable && (canvasLockCanvas = (holder = this.mSurfaceView.getHolder()).lockCanvas()) != null) {
            canvasLockCanvas.drawColor(ViewCompat.MEASURED_STATE_MASK);
            holder.unlockCanvasAndPost(canvasLockCanvas);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void setSurfaceAvailability(boolean z) {
        this.mIsSurfaceAvailable = z;
    }

    private class SurfaceViewCallback implements SurfaceHolder.Callback {
        private SurfaceViewCallback() {
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            Rect surfaceFrame = surfaceHolder.getSurfaceFrame();
            if (CamLog.DEBUG) {
                CamLog.d("surfaceCreated(" + surfaceFrame.width() + "," + surfaceFrame.height() + ") : E");
            }
            if (verifySurfaceSize(surfaceFrame.width(), surfaceFrame.height())) {
                SurfaceViewEvf.this.setSurfaceAvailability(true);
                SurfaceViewEvf.this.mSurfaceSize = new Size(surfaceFrame.width(), surfaceFrame.height());
                SurfaceViewEvf.this.mLifeCycleCallback.onEvfInitialized(SurfaceViewEvf.this, surfaceFrame.width(), surfaceFrame.height());
                if (CamLog.DEBUG) {
                    CamLog.d("surfaceCreated() : X");
                }
            }
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            if (CamLog.DEBUG) {
                CamLog.d("surfaceChanged(" + i2 + "," + i3 + ") : E");
            }
            if (verifySurfaceSize(i2, i3)) {
                SurfaceViewEvf.this.mSurfaceSize = new Size(i2, i3);
                SurfaceViewEvf.this.mLifeCycleCallback.onEvfSizeChanged(SurfaceViewEvf.this, i2, i3);
                if (CamLog.DEBUG) {
                    CamLog.d("surfaceChanged() : X");
                }
            }
        }

        @Override // android.view.SurfaceHolder.Callback
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            if (CamLog.DEBUG) {
                CamLog.d("surfaceDestroyed() : E");
            }
            SurfaceViewEvf.this.setSurfaceAvailability(false);
            SurfaceViewEvf.this.mSurfaceSize = null;
            SurfaceViewEvf.this.mLifeCycleCallback.onEvfFinalized(SurfaceViewEvf.this);
            if (CamLog.DEBUG) {
                CamLog.d("surfaceDestroyed() : X");
            }
        }

        private boolean verifySurfaceSize(int i, int i2) {
            if (SurfaceViewEvf.this.mSurfaceView != null) {
                int iWidth = SurfaceViewEvf.this.mSurfaceView.getHolder().getSurfaceFrame().width();
                if (i == iWidth) {
                    int iHeight = SurfaceViewEvf.this.mSurfaceView.getHolder().getSurfaceFrame().height();
                    if (i2 == iHeight) {
                        return true;
                    }
                    CamLog.w("Surface height is not matched: expected = " + iHeight + " actual = " + i2);
                    return false;
                }
                CamLog.w("Surface width is not matched: expected = " + iWidth + " actual = " + i);
                return false;
            }
            CamLog.w("Surface view has been destroyed");
            return false;
        }
    }

    @Override // com.sonymobile.cameracommon.evf.Evf
    public Surface asSurface() {
        if (CamLog.DEBUG) {
            CamLog.d("asSurface()");
        }
        return this.mSurfaceView.getHolder().getSurface();
    }

    @Override // com.sonymobile.cameracommon.evf.Evf
    public void setFixedSurfaceSize(int i, int i2) {
        if (CamLog.DEBUG) {
            CamLog.d("setFixedSurfaceSize(w:" + i + ", h:" + i2 + ")");
        }
        this.mSurfaceView.getHolder().setFixedSize(i, i2);
    }

    @Override // com.sonymobile.cameracommon.evf.Evf
    public Size getSurfaceSize() {
        return this.mSurfaceSize;
    }
}
