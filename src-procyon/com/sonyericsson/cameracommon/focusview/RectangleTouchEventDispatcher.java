// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.focusview;

import android.view.View;
import android.graphics.Rect;
import com.sonyericsson.cameracommon.utility.FaceDetectUtil;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import android.view.MotionEvent;
import java.util.HashMap;
import android.view.View$OnTouchListener;

public class RectangleTouchEventDispatcher implements View$OnTouchListener
{
    public static final String TAG = "RectangleTouchEventDispatcher";
    private FaceInformationList mFacetList;
    private HashMap<String, TaggedRectangle> mRectangles;
    private TaggedRectangle mTargetRect;
    
    public RectangleTouchEventDispatcher(final HashMap<String, TaggedRectangle> mRectangles) {
        this.mRectangles = mRectangles;
    }
    
    private TaggedRectangle updateTouchView(final FaceInformationList list, final MotionEvent motionEvent) {
        TaggedRectangle taggedRectangle = null;
        if (list == null) {
            return null;
        }
        final int n = (int)motionEvent.getX();
        final int n2 = (int)motionEvent.getY();
        TaggedRectangle overwriteTaggedRectangle;
        for (int i = 0; i < 5; ++i, taggedRectangle = overwriteTaggedRectangle) {
            overwriteTaggedRectangle = taggedRectangle;
            if (i < list.getNamedFaceList().size()) {
                final NamedFace namedFace = list.getNamedFace(i);
                if (namedFace == null) {
                    overwriteTaggedRectangle = taggedRectangle;
                }
                else {
                    final Rect convertFromActiveArrayToView = PositionConverter.getInstance().convertFromActiveArrayToView(namedFace.mFacePosition);
                    overwriteTaggedRectangle = taggedRectangle;
                    if (convertFromActiveArrayToView.contains(n, n2)) {
                        if (this.mRectangles.containsKey(namedFace.mUuid)) {
                            overwriteTaggedRectangle = this.mRectangles.get(namedFace.mUuid);
                            overwriteTaggedRectangle.setRawPosition(convertFromActiveArrayToView);
                        }
                        else {
                            overwriteTaggedRectangle = FaceDetectUtil.overwriteTaggedRectangle(this.mRectangles, namedFace.mUuid, list);
                        }
                    }
                }
            }
        }
        return taggedRectangle;
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        if (this.mFacetList != null) {
            final int action = motionEvent.getAction();
            if (action == 0) {
                this.mTargetRect = this.updateTouchView(this.mFacetList, motionEvent);
            }
            Rectangle rectangle;
            if (this.mTargetRect != null) {
                rectangle = (Rectangle)this.mTargetRect.findViewById(2131296527);
            }
            else {
                rectangle = null;
            }
            if (action == 1 || action == 3) {
                this.mTargetRect = null;
            }
            if (rectangle != null) {
                return rectangle.onTouchEvent(motionEvent);
            }
        }
        return false;
    }
    
    public void updateFaceList(final FaceInformationList mFacetList) {
        this.mFacetList = mFacetList;
    }
}
