// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.cameracommon.contentsview.contents;

import java.util.Iterator;
import java.util.ArrayList;
import android.net.Uri;
import java.util.List;
import android.app.Activity;
import com.sonyericsson.cameracommon.contentsview.contents.optionmenu.OptionOperating;

public class Content implements OptionOperating
{
    public static final String TAG = "Content";
    protected String mCountText;
    private final int mExtraIconId;
    protected final ContentInfo mInfo;
    private final int mPlayableIconId;
    
    public Content(final ContentInfo mInfo, final int mExtraIconId, final int mPlayableIconId) {
        this.mInfo = mInfo;
        this.mExtraIconId = mExtraIconId;
        this.mPlayableIconId = mPlayableIconId;
    }
    
    public ContentInfo getContentInfo() {
        return this.mInfo;
    }
    
    public String getCountText() {
        return this.mCountText;
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
    
    public boolean shouldShowExtraIcon() {
        return this.mExtraIconId != -1;
    }
    
    public boolean shouldShowPlayableIcon() {
        return this.mPlayableIconId != -1;
    }
    
    @Override
    public void viewContent(final Activity activity) {
    }
    
    public static class ContentInfo
    {
        public int mBucketId;
        public ContentsType mContentType;
        public int mGroupedImage;
        public int mHeight;
        public long mId;
        public boolean mIsContainDetails;
        public boolean mIsInTrash;
        public boolean mIsMediaDataVerified;
        public boolean mIsVideoHdr;
        public List<Long> mMediaStoreIds;
        public String mMimeType;
        public int mOrientation;
        public String mOriginalPath;
        public Uri mOriginalUri;
        public int mPredictiveNum;
        public int mSomcType;
        public int mType;
        public int mWidth;
        
        public ContentInfo() {
            this.mMediaStoreIds = new ArrayList<Long>();
            this.mIsInTrash = false;
        }
        
        public ContentInfo getSnapShot() {
            final ContentInfo contentInfo = new ContentInfo();
            contentInfo.mId = this.mId;
            contentInfo.mType = this.mType;
            contentInfo.mOriginalUri = this.mOriginalUri;
            contentInfo.mOriginalPath = this.mOriginalPath;
            contentInfo.mOrientation = this.mOrientation;
            contentInfo.mWidth = this.mWidth;
            contentInfo.mHeight = this.mHeight;
            String string;
            if (this.mMimeType != null) {
                string = this.mMimeType.toString();
            }
            else {
                string = null;
            }
            contentInfo.mMimeType = string;
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
                final Iterator<Long> iterator = this.mMediaStoreIds.iterator();
                while (iterator.hasNext()) {
                    contentInfo.mMediaStoreIds.add(iterator.next());
                }
            }
            return contentInfo;
        }
    }
    
    public enum ContentsType
    {
        private static final ContentsType[] $VALUES;
        
        BURST, 
        HDR_VIDEO, 
        HDR_VIDEO_4K, 
        HIGH_FRAME_RATE_VIDEO, 
        NONE, 
        PHOTO, 
        PREDICTIVE_CAPTURE, 
        SOUND_PHOTO, 
        STANDARD_SLOW_MOTION_VIDEO, 
        SUPER_SLOW_MOTION_VIDEO, 
        SUPER_SLOW_SHOT_VIDEO, 
        TIME_SHIFT, 
        TIME_SHIFT_VIDEO, 
        TIME_SHIFT_VIDEO_120F, 
        VIDEO, 
        VIDEO_4K;
        
        static {
            $VALUES = new ContentsType[] { ContentsType.NONE, ContentsType.PHOTO, ContentsType.VIDEO, ContentsType.BURST, ContentsType.PREDICTIVE_CAPTURE, ContentsType.TIME_SHIFT, ContentsType.TIME_SHIFT_VIDEO, ContentsType.TIME_SHIFT_VIDEO_120F, ContentsType.VIDEO_4K, ContentsType.SOUND_PHOTO, ContentsType.SUPER_SLOW_MOTION_VIDEO, ContentsType.SUPER_SLOW_SHOT_VIDEO, ContentsType.STANDARD_SLOW_MOTION_VIDEO, ContentsType.HIGH_FRAME_RATE_VIDEO, ContentsType.HDR_VIDEO, ContentsType.HDR_VIDEO_4K };
        }
    }
}
