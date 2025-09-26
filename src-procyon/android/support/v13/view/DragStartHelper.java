// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v13.view;

import android.support.v4.view.MotionEventCompat;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.View$OnTouchListener;
import android.view.View$OnLongClickListener;

public class DragStartHelper
{
    private boolean mDragging;
    private int mLastTouchX;
    private int mLastTouchY;
    private final OnDragStartListener mListener;
    private final View$OnLongClickListener mLongClickListener;
    private final View$OnTouchListener mTouchListener;
    private final View mView;
    
    public DragStartHelper(final View mView, final OnDragStartListener mListener) {
        this.mLongClickListener = (View$OnLongClickListener)new View$OnLongClickListener() {
            final DragStartHelper this$0;
            
            public boolean onLongClick(final View view) {
                return this.this$0.onLongClick(view);
            }
        };
        this.mTouchListener = (View$OnTouchListener)new View$OnTouchListener() {
            final DragStartHelper this$0;
            
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                return this.this$0.onTouch(view, motionEvent);
            }
        };
        this.mView = mView;
        this.mListener = mListener;
    }
    
    public void attach() {
        this.mView.setOnLongClickListener(this.mLongClickListener);
        this.mView.setOnTouchListener(this.mTouchListener);
    }
    
    public void detach() {
        this.mView.setOnLongClickListener((View$OnLongClickListener)null);
        this.mView.setOnTouchListener((View$OnTouchListener)null);
    }
    
    public void getTouchPosition(final Point point) {
        point.set(this.mLastTouchX, this.mLastTouchY);
    }
    
    public boolean onLongClick(final View view) {
        return this.mListener.onDragStart(view, this);
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        final int n = (int)motionEvent.getX();
        final int n2 = (int)motionEvent.getY();
        switch (motionEvent.getAction()) {
            case 2: {
                if (!MotionEventCompat.isFromSource(motionEvent, 8194)) {
                    break;
                }
                if ((motionEvent.getButtonState() & 0x1) == 0x0) {
                    break;
                }
                if (this.mDragging) {
                    break;
                }
                if (this.mLastTouchX == n && this.mLastTouchY == n2) {
                    break;
                }
                this.mLastTouchX = n;
                this.mLastTouchY = n2;
                return this.mDragging = this.mListener.onDragStart(view, this);
            }
            case 1:
            case 3: {
                this.mDragging = false;
                break;
            }
            case 0: {
                this.mLastTouchX = n;
                this.mLastTouchY = n2;
                break;
            }
        }
        return false;
    }
    
    public interface OnDragStartListener
    {
        boolean onDragStart(final View p0, final DragStartHelper p1);
    }
}
