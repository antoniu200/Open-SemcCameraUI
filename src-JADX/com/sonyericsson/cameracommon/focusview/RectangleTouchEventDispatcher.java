package com.sonyericsson.cameracommon.focusview;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.cameracommon.utility.FaceDetectUtil;
import com.sonyericsson.cameracommon.utility.PositionConverter;
import java.util.HashMap;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class RectangleTouchEventDispatcher implements View.OnTouchListener {
    public static final String TAG = "RectangleTouchEventDispatcher";
    private FaceInformationList mFacetList;
    private HashMap<String, TaggedRectangle> mRectangles;
    private TaggedRectangle mTargetRect;

    public RectangleTouchEventDispatcher(HashMap<String, TaggedRectangle> map) {
        this.mRectangles = map;
    }

    public void updateFaceList(FaceInformationList faceInformationList) {
        this.mFacetList = faceInformationList;
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (this.mFacetList == null) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mTargetRect = updateTouchView(this.mFacetList, motionEvent);
        }
        Rectangle rectangle = this.mTargetRect != null ? (Rectangle) this.mTargetRect.findViewById(R.id.rect) : null;
        if (action == 1 || action == 3) {
            this.mTargetRect = null;
        }
        if (rectangle != null) {
            return rectangle.onTouchEvent(motionEvent);
        }
        return false;
    }

    private TaggedRectangle updateTouchView(FaceInformationList faceInformationList, MotionEvent motionEvent) {
        NamedFace namedFace;
        TaggedRectangle taggedRectangleOverwriteTaggedRectangle = null;
        if (faceInformationList == null) {
            return null;
        }
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        for (int i = 0; i < 5; i++) {
            if (i < faceInformationList.getNamedFaceList().size() && (namedFace = faceInformationList.getNamedFace(i)) != null) {
                Rect rectConvertFromActiveArrayToView = PositionConverter.getInstance().convertFromActiveArrayToView(namedFace.mFacePosition);
                if (rectConvertFromActiveArrayToView.contains(x, y)) {
                    if (this.mRectangles.containsKey(namedFace.mUuid)) {
                        taggedRectangleOverwriteTaggedRectangle = this.mRectangles.get(namedFace.mUuid);
                        taggedRectangleOverwriteTaggedRectangle.setRawPosition(rectConvertFromActiveArrayToView);
                    } else {
                        taggedRectangleOverwriteTaggedRectangle = FaceDetectUtil.overwriteTaggedRectangle(this.mRectangles, namedFace.mUuid, faceInformationList);
                    }
                }
            }
        }
        return taggedRectangleOverwriteTaggedRectangle;
    }
}
