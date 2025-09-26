// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.evf;

import android.view.ViewGroup$LayoutParams;
import android.view.SurfaceHolder$Callback;
import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.View;
import com.sonyericsson.android.camera.util.CamLog;
import android.view.Surface;
import android.view.SurfaceView;
import android.util.Size;
import android.graphics.Rect;

public class SurfaceViewEvf implements Evf
{
    private static final String TAG = "SurfaceViewEvf";
    private Rect mEvfRect;
    private boolean mIsSurfaceAvailable;
    private LifeCycleCallback mLifeCycleCallback;
    private Size mSurfaceSize;
    private SurfaceView mSurfaceView;
    private SurfaceViewCallback mSurfaceViewCallback;
    
    public SurfaceViewEvf() {
        this.mSurfaceView = null;
        this.mEvfRect = new Rect();
        this.mIsSurfaceAvailable = false;
        this.mSurfaceViewCallback = new SurfaceViewCallback();
        this.mLifeCycleCallback = null;
    }
    
    private void setSurfaceAvailability(final boolean mIsSurfaceAvailable) {
        synchronized (this) {
            this.mIsSurfaceAvailable = mIsSurfaceAvailable;
        }
    }
    
    @Override
    public Surface asSurface() {
        if (CamLog.DEBUG) {
            CamLog.d("asSurface()");
        }
        return this.mSurfaceView.getHolder().getSurface();
    }
    
    @Override
    public View asView() {
        if (CamLog.DEBUG) {
            CamLog.d("asView()");
        }
        return (View)this.mSurfaceView;
    }
    
    @Override
    public void clear() {
        synchronized (this) {
            if (this.mIsSurfaceAvailable) {
                final SurfaceHolder holder = this.mSurfaceView.getHolder();
                final Canvas lockCanvas = holder.lockCanvas();
                if (lockCanvas != null) {
                    lockCanvas.drawColor(-16777216);
                    holder.unlockCanvasAndPost(lockCanvas);
                }
            }
        }
    }
    
    @Override
    public Rect getRect() {
        this.mEvfRect.set(this.mSurfaceView.getLeft(), this.mSurfaceView.getTop(), this.mSurfaceView.getRight(), this.mSurfaceView.getBottom());
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("getRect() : Rect=");
            sb.append(this.mEvfRect.toString());
            CamLog.d(sb.toString());
        }
        return this.mEvfRect;
    }
    
    @Override
    public Size getSurfaceSize() {
        return this.mSurfaceSize;
    }
    
    @Override
    public void hide() {
        if (CamLog.DEBUG) {
            CamLog.d("hide()");
        }
        if (this.mSurfaceView != null) {
            this.mSurfaceView.setVisibility(8);
        }
    }
    
    @Override
    public boolean isShown() {
        final SurfaceView mSurfaceView = this.mSurfaceView;
        boolean b = false;
        if (mSurfaceView != null) {
            if (this.mSurfaceView.getVisibility() == 0) {
                b = true;
            }
            return b;
        }
        return false;
    }
    
    @Override
    public void onCreate(final Context context) {
        if (CamLog.DEBUG) {
            CamLog.d("onCreate() : E");
        }
        (this.mSurfaceView = new SurfaceView(context)).setVisibility(4);
        this.mSurfaceView.getHolder().addCallback((SurfaceHolder$Callback)this.mSurfaceViewCallback);
        if (CamLog.DEBUG) {
            CamLog.d("onCreate() : X");
        }
    }
    
    @Override
    public void onDestroy() {
        if (CamLog.DEBUG) {
            CamLog.d("onDestroy() : E");
        }
        if (this.mSurfaceView != null) {
            this.mSurfaceView.getHolder().removeCallback((SurfaceHolder$Callback)this.mSurfaceViewCallback);
            this.mSurfaceView = null;
        }
        this.mLifeCycleCallback = null;
        if (CamLog.DEBUG) {
            CamLog.d("onDestroy() : X");
        }
    }
    
    @Override
    public void onPause() {
        if (CamLog.DEBUG) {
            CamLog.d("onPause() : E");
        }
        if (CamLog.DEBUG) {
            CamLog.d("onPause() : X");
        }
    }
    
    @Override
    public void onResume() {
        if (CamLog.DEBUG) {
            CamLog.d("onResume() : E");
        }
        if (CamLog.DEBUG) {
            CamLog.d("onResume() : X");
        }
    }
    
    @Override
    public void resize(final int n, final int n2) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("resize() : Width=");
            sb.append(n);
            sb.append(", Height = ");
            sb.append(n2);
            CamLog.d(sb.toString());
        }
        final ViewGroup$LayoutParams layoutParams = this.mSurfaceView.getLayoutParams();
        layoutParams.width = n;
        layoutParams.height = n2;
        this.mSurfaceView.setLayoutParams(layoutParams);
    }
    
    @Override
    public void setFixedSurfaceSize(final int i, final int j) {
        if (CamLog.DEBUG) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setFixedSurfaceSize(w:");
            sb.append(i);
            sb.append(", h:");
            sb.append(j);
            sb.append(")");
            CamLog.d(sb.toString());
        }
        this.mSurfaceView.getHolder().setFixedSize(i, j);
    }
    
    @Override
    public void setLifeCycleCallback(final LifeCycleCallback mLifeCycleCallback) {
        this.mLifeCycleCallback = mLifeCycleCallback;
    }
    
    @Override
    public void show() {
        if (CamLog.DEBUG) {
            CamLog.d("show()");
        }
        if (this.mSurfaceView != null) {
            this.mSurfaceView.setVisibility(0);
        }
    }
    
    private class SurfaceViewCallback implements SurfaceHolder$Callback
    {
        final SurfaceViewEvf this$0;
        
        private SurfaceViewCallback(final SurfaceViewEvf this$0) {
            this.this$0 = this$0;
        }
        
        private boolean verifySurfaceSize(int height, final int i) {
            if (this.this$0.mSurfaceView == null) {
                CamLog.w("Surface view has been destroyed");
                return false;
            }
            final int width = this.this$0.mSurfaceView.getHolder().getSurfaceFrame().width();
            if (height != width) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Surface width is not matched: expected = ");
                sb.append(width);
                sb.append(" actual = ");
                sb.append(height);
                CamLog.w(sb.toString());
                return false;
            }
            height = this.this$0.mSurfaceView.getHolder().getSurfaceFrame().height();
            if (i != height) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Surface height is not matched: expected = ");
                sb2.append(height);
                sb2.append(" actual = ");
                sb2.append(i);
                CamLog.w(sb2.toString());
                return false;
            }
            return true;
        }
        
        public void surfaceChanged(final SurfaceHolder surfaceHolder, final int n, final int i, final int j) {
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("surfaceChanged(");
                sb.append(i);
                sb.append(",");
                sb.append(j);
                sb.append(") : E");
                CamLog.d(sb.toString());
            }
            if (!this.verifySurfaceSize(i, j)) {
                return;
            }
            this.this$0.mSurfaceSize = new Size(i, j);
            this.this$0.mLifeCycleCallback.onEvfSizeChanged(this.this$0, i, j);
            if (CamLog.DEBUG) {
                CamLog.d("surfaceChanged() : X");
            }
        }
        
        public void surfaceCreated(final SurfaceHolder surfaceHolder) {
            final Rect surfaceFrame = surfaceHolder.getSurfaceFrame();
            if (CamLog.DEBUG) {
                final StringBuilder sb = new StringBuilder();
                sb.append("surfaceCreated(");
                sb.append(surfaceFrame.width());
                sb.append(",");
                sb.append(surfaceFrame.height());
                sb.append(") : E");
                CamLog.d(sb.toString());
            }
            if (!this.verifySurfaceSize(surfaceFrame.width(), surfaceFrame.height())) {
                return;
            }
            this.this$0.setSurfaceAvailability(true);
            this.this$0.mSurfaceSize = new Size(surfaceFrame.width(), surfaceFrame.height());
            this.this$0.mLifeCycleCallback.onEvfInitialized(this.this$0, surfaceFrame.width(), surfaceFrame.height());
            if (CamLog.DEBUG) {
                CamLog.d("surfaceCreated() : X");
            }
        }
        
        public void surfaceDestroyed(final SurfaceHolder surfaceHolder) {
            if (CamLog.DEBUG) {
                CamLog.d("surfaceDestroyed() : E");
            }
            this.this$0.setSurfaceAvailability(false);
            this.this$0.mSurfaceSize = null;
            this.this$0.mLifeCycleCallback.onEvfFinalized(this.this$0);
            if (CamLog.DEBUG) {
                CamLog.d("surfaceDestroyed() : X");
            }
        }
    }
}
