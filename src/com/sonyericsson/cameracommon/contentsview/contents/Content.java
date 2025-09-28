package com.sonyericsson.cameracommon.contentsview.contents;

import android.app.Activity;
import android.net.Uri;
import com.sonyericsson.cameracommon.contentsview.contents.optionmenu.OptionOperating;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class Content implements OptionOperating {
    public static final String TAG = "Content";
    protected String mCountText;
    private final int mExtraIconId;
    protected final ContentInfo mInfo;
    private final int mPlayableIconId;

    public enum ContentsType {
        NONE,
        PHOTO,
        VIDEO,
        BURST,
        PREDICTIVE_CAPTURE,
        TIME_SHIFT,
        TIME_SHIFT_VIDEO,
        TIME_SHIFT_VIDEO_120F,
        VIDEO_4K,
        SOUND_PHOTO,
        SUPER_SLOW_MOTION_VIDEO,
        SUPER_SLOW_SHOT_VIDEO,
        STANDARD_SLOW_MOTION_VIDEO,
        HIGH_FRAME_RATE_VIDEO,
        HDR_VIDEO,
        HDR_VIDEO_4K
    }

    @Override // com.sonyericsson.cameracommon.contentsview.contents.optionmenu.OptionOperating
    public void viewContent(Activity activity) {
    }

    public static class ContentInfo {
        public int mBucketId;
        public ContentsType mContentType;
        public int mGroupedImage;
        public int mHeight;
        public long mId;
        public boolean mIsContainDetails;
        public boolean mIsMediaDataVerified;
        public boolean mIsVideoHdr;
        public String mMimeType;
        public int mOrientation;
        public String mOriginalPath;
        public Uri mOriginalUri;
        public int mPredictiveNum;
        public int mSomcType;
        public int mType;
        public int mWidth;
        public List<Long> mMediaStoreIds = new ArrayList();
        public boolean mIsInTrash = false;

        public ContentInfo getSnapShot() {
            ContentInfo contentInfo = new ContentInfo();
            contentInfo.mId = this.mId;
            contentInfo.mType = this.mType;
            contentInfo.mOriginalUri = this.mOriginalUri;
            contentInfo.mOriginalPath = this.mOriginalPath;
            contentInfo.mOrientation = this.mOrientation;
            contentInfo.mWidth = this.mWidth;
            contentInfo.mHeight = this.mHeight;
            contentInfo.mMimeType = this.mMimeType != null ? this.mMimeType.toString() : null;
            contentInfo.mGroupedImage = this.mGroupedImage;
            contentInfo.mSomcType = this.mSomcType;
            contentInfo.mIsVideoHdr = this.mIsVideoHdr;
            contentInfo.mBucketId = this.mBucketId;
            contentInfo.mContentType = this.mContentType;
            contentInfo.mIsContainDetails = this.mIsContainDetails;
            contentInfo.mIsInTrash = this.mIsInTrash;
            contentInfo.mPredictiveNum = this.mPredictiveNum;
            contentInfo.mIsMediaDataVerified = this.mIsMediaDataVerified;
            if (this.mContentType == ContentsType.BURST) {
                Iterator<Long> it = this.mMediaStoreIds.iterator();
                while (it.hasNext()) {
                    contentInfo.mMediaStoreIds.add(it.next());
                }
            }
            return contentInfo;
        }
    }

    public Content(ContentInfo contentInfo, int i, int i2) {
        this.mInfo = contentInfo;
        this.mExtraIconId = i;
        this.mPlayableIconId = i2;
    }

    public ContentInfo getContentInfo() {
        return this.mInfo;
    }

    public boolean shouldShowPlayableIcon() {
        return this.mPlayableIconId != -1;
    }

    public boolean shouldShowExtraIcon() {
        return this.mExtraIconId != -1;
    }

    public int getExtraIconResourceId() {
        return this.mExtraIconId;
    }

    public int getPlayIconResourceId() {
        return this.mPlayableIconId;
    }

    public boolean isMediaDataVerified() {
        return this.mInfo.mIsMediaDataVerified;
    }

    public String getCountText() {
        return this.mCountText;
    }
}
