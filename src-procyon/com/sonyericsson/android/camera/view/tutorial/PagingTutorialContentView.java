// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.tutorial;

import android.text.method.ScrollingMovementMethod;
import android.annotation.SuppressLint;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.setting.MessageSettings;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.setting.MessageType;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import java.util.ArrayList;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.setting.StoredSettings;
import android.widget.TextView;
import java.util.List;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.view.PagerAdapter;
import com.duolingo.open.rtlviewpager.DelegatingPagerAdapter;
import android.view.ViewGroup$LayoutParams;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import android.media.MediaPlayer$OnPreparedListener;
import android.view.Surface;
import android.view.TextureView$SurfaceTextureListener;
import android.util.AttributeSet;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonymobile.cameracommon.research.parameters.Event;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import java.util.Iterator;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.support.v4.view.ViewPager;
import android.media.MediaPlayer$OnErrorListener;
import android.media.MediaPlayer;
import android.widget.ImageView;

public class PagingTutorialContentView extends TutorialContentView
{
    private static final float REVERSE_DEGREE = 180.0f;
    private static final String TAG = "PagingTutorialContentView";
    private static final boolean TRACE = false;
    private static final double VIDEO_ASPECT_RATIO_LANDSCAPE = 0.75;
    private static final double VIDEO_ASPECT_RATIO_PORTRAIT = 0.816;
    private ImageView mIcon;
    private MediaPlayer mMediaPlayer;
    private PagingTutorialNavigator mNavigator;
    MediaPlayer$OnErrorListener mOnErrorListener;
    private final ViewPager.OnPageChangeListener mOnPageChangeListener;
    private ViewPager mPager;
    private SurfaceTexture mSurfaceTexture;
    private TutorialVideoView mVideo;
    
    public PagingTutorialContentView(final Context context) {
        super(context);
        this.mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            final PagingTutorialContentView this$0;
            
            @Override
            public void onPageScrollStateChanged(final int n) {
                switch (n) {
                    case 1: {
                        this.this$0.suspend();
                        break;
                    }
                    case 0: {
                        this.this$0.resume();
                        break;
                    }
                }
            }
            
            @Override
            public void onPageScrolled(final int index, final float n, final int n2) {
                final PagingTutorialContent access$000 = this.this$0.getContent();
                if (access$000.mMediaContentsResourceId != null) {
                    final MediaContentsResource mediaContentsResource = access$000.mMediaContentsResourceId.get(index);
                    if (this.this$0.mIcon == null) {
                        final Iterator<MediaContentsResource> iterator = access$000.mMediaContentsResourceId.iterator();
                        while (iterator.hasNext()) {
                            if (iterator.next().getType() == MediaContentsResourceType.IMAGE) {
                                this.this$0.mIcon = (ImageView)this.this$0.findViewById(2131296678);
                                break;
                            }
                        }
                        if (mediaContentsResource.getType() == MediaContentsResourceType.IMAGE) {
                            this.this$0.mIcon.setVisibility(0);
                            this.this$0.mIcon.setImageResource(mediaContentsResource.getId());
                        }
                    }
                    if (this.this$0.mVideo == null) {
                        final Iterator<MediaContentsResource> iterator2 = access$000.mMediaContentsResourceId.iterator();
                        while (iterator2.hasNext()) {
                            if (iterator2.next().getType() == MediaContentsResourceType.VIDEO) {
                                this.this$0.mVideo = (TutorialVideoView)this.this$0.findViewById(2131296680);
                                this.this$0.prepareTutorialVideoView(this.this$0.mVideo);
                                break;
                            }
                        }
                        if (mediaContentsResource.getType() == MediaContentsResourceType.VIDEO) {
                            this.this$0.mVideo.setSurfaceTextureListener(this.this$0.createSurfaceTextureListener(index));
                            this.this$0.mVideo.setVisibility(0);
                        }
                    }
                }
            }
            
            @Override
            public void onPageSelected(final int n) {
                final PagingTutorialContent access$000 = this.this$0.getContent();
                final int mPageIndex = access$000.mPageIndex;
                access$000.mPageIndex = n;
                if (access$000.mMediaContentsResourceId != null) {
                    final MediaContentsResource mediaContentsResource = access$000.mMediaContentsResourceId.get(n);
                    if (this.this$0.mIcon != null && mediaContentsResource.getType() == MediaContentsResourceType.IMAGE) {
                        this.this$0.mIcon.setImageResource(mediaContentsResource.getId());
                        if (this.this$0.mVideo != null) {
                            this.this$0.mVideo.setVisibility(8);
                            this.this$0.clearMediaPlayer();
                        }
                        this.this$0.mIcon.setVisibility(0);
                    }
                    else if (this.this$0.mVideo != null && mediaContentsResource.getType() == MediaContentsResourceType.VIDEO) {
                        if (this.this$0.mSurfaceTexture != null) {
                            this.this$0.createMediaPlayer(n);
                        }
                        else {
                            this.this$0.mVideo.setSurfaceTextureListener(this.this$0.createSurfaceTextureListener(n));
                        }
                        if (this.this$0.mIcon != null) {
                            this.this$0.mIcon.setVisibility(8);
                        }
                        this.this$0.mVideo.setVisibility(0);
                    }
                }
                final TutorialPageInfo currentTutorialPageInfo = access$000.getCurrentTutorialPageInfo(mPageIndex);
                if (mPageIndex < access$000.mPageIndex) {
                    LocalResearchUtil.getInstance().sendSetupWizardEvent(currentTutorialPageInfo.type, currentTutorialPageInfo.pageIndexByType, Event.WizardResult.NEXT);
                }
                else {
                    LocalResearchUtil.getInstance().sendSetupWizardEvent(currentTutorialPageInfo.type, currentTutorialPageInfo.pageIndexByType, Event.WizardResult.PREVIOUS);
                }
                final TutorialPageInfo currentTutorialPageInfo2 = access$000.getCurrentTutorialPageInfo(n);
                LocalResearchUtil.getInstance().startSetupWizard(currentTutorialPageInfo2.type, currentTutorialPageInfo2.pageIndexByType);
            }
        };
        this.mOnErrorListener = (MediaPlayer$OnErrorListener)new MediaPlayer$OnErrorListener() {
            final PagingTutorialContentView this$0;
            
            public boolean onError(final MediaPlayer mediaPlayer, final int i, final int j) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onError() : MediaPlayer = ");
                sb.append(mediaPlayer.hashCode());
                sb.append(" what = ");
                sb.append(i);
                sb.append(" extra = ");
                sb.append(j);
                CamLog.e(sb.toString());
                return false;
            }
        };
    }
    
    public PagingTutorialContentView(final Context context, final AttributeSet set) {
        super(context, set);
        this.mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            final PagingTutorialContentView this$0;
            
            @Override
            public void onPageScrollStateChanged(final int n) {
                switch (n) {
                    case 1: {
                        this.this$0.suspend();
                        break;
                    }
                    case 0: {
                        this.this$0.resume();
                        break;
                    }
                }
            }
            
            @Override
            public void onPageScrolled(final int index, final float n, final int n2) {
                final PagingTutorialContent access$000 = this.this$0.getContent();
                if (access$000.mMediaContentsResourceId != null) {
                    final MediaContentsResource mediaContentsResource = access$000.mMediaContentsResourceId.get(index);
                    if (this.this$0.mIcon == null) {
                        final Iterator<MediaContentsResource> iterator = access$000.mMediaContentsResourceId.iterator();
                        while (iterator.hasNext()) {
                            if (iterator.next().getType() == MediaContentsResourceType.IMAGE) {
                                this.this$0.mIcon = (ImageView)this.this$0.findViewById(2131296678);
                                break;
                            }
                        }
                        if (mediaContentsResource.getType() == MediaContentsResourceType.IMAGE) {
                            this.this$0.mIcon.setVisibility(0);
                            this.this$0.mIcon.setImageResource(mediaContentsResource.getId());
                        }
                    }
                    if (this.this$0.mVideo == null) {
                        final Iterator<MediaContentsResource> iterator2 = access$000.mMediaContentsResourceId.iterator();
                        while (iterator2.hasNext()) {
                            if (iterator2.next().getType() == MediaContentsResourceType.VIDEO) {
                                this.this$0.mVideo = (TutorialVideoView)this.this$0.findViewById(2131296680);
                                this.this$0.prepareTutorialVideoView(this.this$0.mVideo);
                                break;
                            }
                        }
                        if (mediaContentsResource.getType() == MediaContentsResourceType.VIDEO) {
                            this.this$0.mVideo.setSurfaceTextureListener(this.this$0.createSurfaceTextureListener(index));
                            this.this$0.mVideo.setVisibility(0);
                        }
                    }
                }
            }
            
            @Override
            public void onPageSelected(final int n) {
                final PagingTutorialContent access$000 = this.this$0.getContent();
                final int mPageIndex = access$000.mPageIndex;
                access$000.mPageIndex = n;
                if (access$000.mMediaContentsResourceId != null) {
                    final MediaContentsResource mediaContentsResource = access$000.mMediaContentsResourceId.get(n);
                    if (this.this$0.mIcon != null && mediaContentsResource.getType() == MediaContentsResourceType.IMAGE) {
                        this.this$0.mIcon.setImageResource(mediaContentsResource.getId());
                        if (this.this$0.mVideo != null) {
                            this.this$0.mVideo.setVisibility(8);
                            this.this$0.clearMediaPlayer();
                        }
                        this.this$0.mIcon.setVisibility(0);
                    }
                    else if (this.this$0.mVideo != null && mediaContentsResource.getType() == MediaContentsResourceType.VIDEO) {
                        if (this.this$0.mSurfaceTexture != null) {
                            this.this$0.createMediaPlayer(n);
                        }
                        else {
                            this.this$0.mVideo.setSurfaceTextureListener(this.this$0.createSurfaceTextureListener(n));
                        }
                        if (this.this$0.mIcon != null) {
                            this.this$0.mIcon.setVisibility(8);
                        }
                        this.this$0.mVideo.setVisibility(0);
                    }
                }
                final TutorialPageInfo currentTutorialPageInfo = access$000.getCurrentTutorialPageInfo(mPageIndex);
                if (mPageIndex < access$000.mPageIndex) {
                    LocalResearchUtil.getInstance().sendSetupWizardEvent(currentTutorialPageInfo.type, currentTutorialPageInfo.pageIndexByType, Event.WizardResult.NEXT);
                }
                else {
                    LocalResearchUtil.getInstance().sendSetupWizardEvent(currentTutorialPageInfo.type, currentTutorialPageInfo.pageIndexByType, Event.WizardResult.PREVIOUS);
                }
                final TutorialPageInfo currentTutorialPageInfo2 = access$000.getCurrentTutorialPageInfo(n);
                LocalResearchUtil.getInstance().startSetupWizard(currentTutorialPageInfo2.type, currentTutorialPageInfo2.pageIndexByType);
            }
        };
        this.mOnErrorListener = (MediaPlayer$OnErrorListener)new MediaPlayer$OnErrorListener() {
            final PagingTutorialContentView this$0;
            
            public boolean onError(final MediaPlayer mediaPlayer, final int i, final int j) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onError() : MediaPlayer = ");
                sb.append(mediaPlayer.hashCode());
                sb.append(" what = ");
                sb.append(i);
                sb.append(" extra = ");
                sb.append(j);
                CamLog.e(sb.toString());
                return false;
            }
        };
    }
    
    public PagingTutorialContentView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            final PagingTutorialContentView this$0;
            
            @Override
            public void onPageScrollStateChanged(final int n) {
                switch (n) {
                    case 1: {
                        this.this$0.suspend();
                        break;
                    }
                    case 0: {
                        this.this$0.resume();
                        break;
                    }
                }
            }
            
            @Override
            public void onPageScrolled(final int index, final float n, final int n2) {
                final PagingTutorialContent access$000 = this.this$0.getContent();
                if (access$000.mMediaContentsResourceId != null) {
                    final MediaContentsResource mediaContentsResource = access$000.mMediaContentsResourceId.get(index);
                    if (this.this$0.mIcon == null) {
                        final Iterator<MediaContentsResource> iterator = access$000.mMediaContentsResourceId.iterator();
                        while (iterator.hasNext()) {
                            if (iterator.next().getType() == MediaContentsResourceType.IMAGE) {
                                this.this$0.mIcon = (ImageView)this.this$0.findViewById(2131296678);
                                break;
                            }
                        }
                        if (mediaContentsResource.getType() == MediaContentsResourceType.IMAGE) {
                            this.this$0.mIcon.setVisibility(0);
                            this.this$0.mIcon.setImageResource(mediaContentsResource.getId());
                        }
                    }
                    if (this.this$0.mVideo == null) {
                        final Iterator<MediaContentsResource> iterator2 = access$000.mMediaContentsResourceId.iterator();
                        while (iterator2.hasNext()) {
                            if (iterator2.next().getType() == MediaContentsResourceType.VIDEO) {
                                this.this$0.mVideo = (TutorialVideoView)this.this$0.findViewById(2131296680);
                                this.this$0.prepareTutorialVideoView(this.this$0.mVideo);
                                break;
                            }
                        }
                        if (mediaContentsResource.getType() == MediaContentsResourceType.VIDEO) {
                            this.this$0.mVideo.setSurfaceTextureListener(this.this$0.createSurfaceTextureListener(index));
                            this.this$0.mVideo.setVisibility(0);
                        }
                    }
                }
            }
            
            @Override
            public void onPageSelected(final int n) {
                final PagingTutorialContent access$000 = this.this$0.getContent();
                final int mPageIndex = access$000.mPageIndex;
                access$000.mPageIndex = n;
                if (access$000.mMediaContentsResourceId != null) {
                    final MediaContentsResource mediaContentsResource = access$000.mMediaContentsResourceId.get(n);
                    if (this.this$0.mIcon != null && mediaContentsResource.getType() == MediaContentsResourceType.IMAGE) {
                        this.this$0.mIcon.setImageResource(mediaContentsResource.getId());
                        if (this.this$0.mVideo != null) {
                            this.this$0.mVideo.setVisibility(8);
                            this.this$0.clearMediaPlayer();
                        }
                        this.this$0.mIcon.setVisibility(0);
                    }
                    else if (this.this$0.mVideo != null && mediaContentsResource.getType() == MediaContentsResourceType.VIDEO) {
                        if (this.this$0.mSurfaceTexture != null) {
                            this.this$0.createMediaPlayer(n);
                        }
                        else {
                            this.this$0.mVideo.setSurfaceTextureListener(this.this$0.createSurfaceTextureListener(n));
                        }
                        if (this.this$0.mIcon != null) {
                            this.this$0.mIcon.setVisibility(8);
                        }
                        this.this$0.mVideo.setVisibility(0);
                    }
                }
                final TutorialPageInfo currentTutorialPageInfo = access$000.getCurrentTutorialPageInfo(mPageIndex);
                if (mPageIndex < access$000.mPageIndex) {
                    LocalResearchUtil.getInstance().sendSetupWizardEvent(currentTutorialPageInfo.type, currentTutorialPageInfo.pageIndexByType, Event.WizardResult.NEXT);
                }
                else {
                    LocalResearchUtil.getInstance().sendSetupWizardEvent(currentTutorialPageInfo.type, currentTutorialPageInfo.pageIndexByType, Event.WizardResult.PREVIOUS);
                }
                final TutorialPageInfo currentTutorialPageInfo2 = access$000.getCurrentTutorialPageInfo(n);
                LocalResearchUtil.getInstance().startSetupWizard(currentTutorialPageInfo2.type, currentTutorialPageInfo2.pageIndexByType);
            }
        };
        this.mOnErrorListener = (MediaPlayer$OnErrorListener)new MediaPlayer$OnErrorListener() {
            final PagingTutorialContentView this$0;
            
            public boolean onError(final MediaPlayer mediaPlayer, final int i, final int j) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onError() : MediaPlayer = ");
                sb.append(mediaPlayer.hashCode());
                sb.append(" what = ");
                sb.append(i);
                sb.append(" extra = ");
                sb.append(j);
                CamLog.e(sb.toString());
                return false;
            }
        };
    }
    
    private void clearMediaPlayer() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }
    
    private void createMediaPlayer(final int index) {
        this.clearMediaPlayer();
        this.mMediaPlayer = MediaPlayer.create(this.getContext(), ((MediaContentsResource)this.getContent().mMediaContentsResourceId.get(index)).getId());
        this.mVideo.setVideoAspectRatio(this.mMediaPlayer.getVideoHeight() / (float)this.mMediaPlayer.getVideoWidth());
        this.mMediaPlayer.setSurface(new Surface(this.mSurfaceTexture));
        this.mMediaPlayer.setOnErrorListener(this.mOnErrorListener);
        this.mMediaPlayer.setLooping(true);
        this.mMediaPlayer.setOnPreparedListener((MediaPlayer$OnPreparedListener)new MediaPlayer$OnPreparedListener(this) {
            final PagingTutorialContentView this$0;
            
            public void onPrepared(final MediaPlayer mediaPlayer) {
                this.this$0.mVideo.updateScale();
                this.this$0.mMediaPlayer.start();
            }
        });
    }
    
    private TextureView$SurfaceTextureListener createSurfaceTextureListener(final int n) {
        return (TextureView$SurfaceTextureListener)new TextureView$SurfaceTextureListener(this, n) {
            final PagingTutorialContentView this$0;
            final int val$position;
            
            public void onSurfaceTextureAvailable(final SurfaceTexture surfaceTexture, final int n, final int n2) {
                this.this$0.mSurfaceTexture = surfaceTexture;
                this.this$0.createMediaPlayer(this.val$position);
            }
            
            public boolean onSurfaceTextureDestroyed(final SurfaceTexture surfaceTexture) {
                this.this$0.clearMediaPlayer();
                return false;
            }
            
            public void onSurfaceTextureSizeChanged(final SurfaceTexture surfaceTexture, final int n, final int n2) {
            }
            
            public void onSurfaceTextureUpdated(final SurfaceTexture surfaceTexture) {
            }
        };
    }
    
    private PagingTutorialContent getContent() {
        return (PagingTutorialContent)this.mContent;
    }
    
    private void prepareTutorialVideoView(final TutorialVideoView tutorialVideoView) {
        final Context context = this.getContext();
        final PagingTutorialContent content = this.getContent();
        final int height = LayoutDependencyResolver.getViewFinderSize(context).height();
        double n;
        if (((TutorialContent)content).isPortrait()) {
            n = 0.816;
        }
        else {
            n = 0.75;
        }
        int height2;
        int width;
        if (((TutorialContent)content).isPortrait()) {
            height2 = (int)(height * n);
            width = height;
        }
        else {
            width = (int)(height * n);
            height2 = height;
        }
        final ViewGroup$LayoutParams layoutParams = tutorialVideoView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height2;
        tutorialVideoView.setLayoutParams(layoutParams);
    }
    
    private void resume() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.start();
        }
    }
    
    private void suspend() {
        if (this.mMediaPlayer != null && this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.pause();
        }
    }
    
    private static void trace(final String s) {
        CamLog.d(s);
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mPager.clearOnPageChangeListeners();
    }
    
    @Override
    protected void onUpdateViewContent() {
        final PagingTutorialContent content = this.getContent();
        if (content == null) {
            return;
        }
        (this.mPager = (ViewPager)this.findViewById(2131296682)).setAdapter(new DelegatingPagerAdapter(new PageContentsAdapter(content)));
        final ViewGroup viewGroup = (ViewGroup)this.findViewById(2131296681);
        if (PagingTutorialContentView$5.$SwitchMap$com$sonyericsson$android$camera$view$tutorial$PagingTutorialContentView$TutorialNavigatorType[content.mNavigatorType.ordinal()] != 1) {
            this.mNavigator = (PagingTutorialNormalNavigator)View.inflate(this.getContext(), 2131492963, (ViewGroup)null);
        }
        else {
            this.mNavigator = (PagingTutorialConfirmNavigator)View.inflate(this.getContext(), 2131492960, (ViewGroup)null);
        }
        viewGroup.addView((View)this.mNavigator);
        this.mNavigator.setViewController(new PagingTutorialController());
        this.mNavigator.setPageSize(content.mPageResources.size());
        if (this.getResources().getConfiguration().getLayoutDirection() == 1) {
            this.mNavigator.setRotationY(180.0f);
        }
        this.mNavigator.onPageSelected(content.mPageIndex);
        this.mPager.setCurrentItem(content.mPageIndex);
        this.mPager.addOnPageChangeListener((ViewPager.OnPageChangeListener)this.mNavigator);
        this.mPager.addOnPageChangeListener(this.mOnPageChangeListener);
    }
    
    protected static final class CustomTutorialContent extends PagingTutorialContent
    {
        public CustomTutorialContent(final int n, final List<TutorialController.TutorialType> mTutorialTypes, final List<TutorialContent> mTutorialContents) {
            super(n);
            this.mTutorialTypes = mTutorialTypes;
            this.mTutorialContents = mTutorialContents;
            this.mNavigatorType = mTutorialContents.get(0).getNavigatorType();
            this.mergeContents(mTutorialContents);
        }
        
        private void mergeContents(final List<TutorialContent> list) {
            for (final TutorialContent tutorialContent : list) {
                if (!(tutorialContent instanceof PagingTutorialContent)) {
                    continue;
                }
                final PagingTutorialContent pagingTutorialContent = (PagingTutorialContent)tutorialContent;
                final Iterator<Integer> iterator2 = pagingTutorialContent.getPageResources().iterator();
                while (iterator2.hasNext()) {
                    this.mPageResources.add((int)iterator2.next());
                }
                final Iterator<Integer> iterator3 = pagingTutorialContent.getTitleResources().iterator();
                while (iterator3.hasNext()) {
                    this.mTitleResourceId.add((int)iterator3.next());
                }
                final Iterator<Integer> iterator4 = pagingTutorialContent.getDescriptionResources().iterator();
                while (iterator4.hasNext()) {
                    this.mDescriptionResourceId.add((int)iterator4.next());
                }
                final Iterator<MediaContentsResource> iterator5 = pagingTutorialContent.getMediaContentsResources().iterator();
                while (iterator5.hasNext()) {
                    this.mMediaContentsResourceId.add(iterator5.next());
                }
            }
        }
        
        @Override
        protected ViewGroup getPageContentView(final Context context, int intValue) {
            final TutorialController.TutorialType type = ((PagingTutorialContent)this).getCurrentTutorialPageInfo().type;
            final int pageIndexByType = ((PagingTutorialContent)this).getCurrentTutorialPageInfo(intValue).pageIndexByType;
            final TutorialContent tutorialContent = ((PagingTutorialContent)this).getTutorialContent(type);
            final ViewGroup pageContentView = super.getPageContentView(context, intValue);
            switch (PagingTutorialContentView$5.$SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[type.ordinal()]) {
                case 3: {
                    intValue = (int)tutorialContent.mParams[0];
                    if (pageIndexByType == 0) {
                        final TextView descriptionView = ((PagingTutorialContent)this).getDescriptionView(pageContentView);
                        final String string = context.getString(2131690140, new Object[] { Integer.toString(intValue) });
                        descriptionView.setText((CharSequence)string);
                        descriptionView.setContentDescription((CharSequence)string);
                        break;
                    }
                    break;
                }
                case 2: {
                    if (pageIndexByType > 0 && pageIndexByType < 3) {
                        final TextView titleView = ((PagingTutorialContent)this).getTitleView(pageContentView);
                        final String stepText = ((PagingTutorialContent)this).getStepText(context, pageIndexByType, this.mPageResources.size() - 2);
                        titleView.setText((CharSequence)stepText);
                        titleView.setContentDescription((CharSequence)stepText);
                        break;
                    }
                    break;
                }
                case 1: {
                    if (pageIndexByType > 0) {
                        final TextView titleView2 = ((PagingTutorialContent)this).getTitleView(pageContentView);
                        final String stepText2 = ((PagingTutorialContent)this).getStepText(context, pageIndexByType, this.mPageResources.size() - 1);
                        titleView2.setText((CharSequence)stepText2);
                        titleView2.setContentDescription((CharSequence)stepText2);
                        break;
                    }
                    break;
                }
            }
            return pageContentView;
        }
        
        @Override
        protected void setupResource() {
            super.setupResource();
            if (this.mTutorialContents != null) {
                for (final TutorialContent tutorialContent : this.mTutorialContents) {
                    tutorialContent.mOrientation = this.mOrientation;
                    tutorialContent.setupResource();
                }
                this.mergeContents(this.mTutorialContents);
            }
        }
    }
    
    protected static final class DualCameraTutorialContent extends PagingTutorialContent
    {
        protected DualCameraTutorialContent(final int n) {
            super(n);
        }
        
        @Override
        protected boolean canShowContent(final StoredSettings storedSettings) {
            return PlatformCapability.isHighSensitivityFusionSupported(CameraInfo.CameraId.BACK);
        }
        
        @Override
        protected void setupResource() {
            super.setupResource();
            final ArrayList<Integer> mPageResources = this.mPageResources;
            int i;
            if (((TutorialContent)this).isPortrait()) {
                i = 2131493024;
            }
            else {
                i = 2131493023;
            }
            mPageResources.add(i);
            final ArrayList<MediaContentsResource> mMediaContentsResourceId = this.mMediaContentsResourceId;
            int n;
            if (((TutorialContent)this).isPortrait()) {
                n = 2131231372;
            }
            else {
                n = 2131231371;
            }
            mMediaContentsResourceId.add(new MediaContentsResource(n, MediaContentsResourceType.IMAGE));
            this.mTitleResourceId.add(2131690175);
            this.mDescriptionResourceId.add(2131690176);
        }
    }
    
    protected static final class EyeGuideTutorialContent extends PagingTutorialContent
    {
        protected EyeGuideTutorialContent(final int n) {
            super(n);
        }
        
        @Override
        protected boolean canShowContent(final StoredSettings storedSettings) {
            return ((CapturingMode)storedSettings.getUserSettings().get(UserSettingKey.CAPTURING_MODE)).isFront() && (storedSettings.getMessageSettings().isNeverShow(MessageType.TUTORIAL_EYE_GUIDE) ^ true);
        }
        
        @Override
        protected void setupResource() {
            super.setupResource();
            final ArrayList<Integer> mPageResources = this.mPageResources;
            int i;
            if (((TutorialContent)this).isPortrait()) {
                i = 2131493024;
            }
            else {
                i = 2131493023;
            }
            mPageResources.add(i);
            final ArrayList<MediaContentsResource> mMediaContentsResourceId = this.mMediaContentsResourceId;
            int n;
            if (((TutorialContent)this).isPortrait()) {
                n = 2131231227;
            }
            else {
                n = 2131231226;
            }
            mMediaContentsResourceId.add(new MediaContentsResource(n, MediaContentsResourceType.IMAGE));
            this.mTitleResourceId.add(2131689813);
            this.mDescriptionResourceId.add(2131689814);
        }
    }
    
    protected static final class HandShutterTutorialContent extends PagingTutorialContent
    {
        protected HandShutterTutorialContent(final int n) {
            super(n);
        }
        
        @Override
        protected boolean canShowContent(final StoredSettings storedSettings) {
            final UserSettings userSettings = storedSettings.getUserSettings();
            final MessageSettings messageSettings = storedSettings.getMessageSettings();
            final CapturingMode capturingMode = (CapturingMode)userSettings.get(UserSettingKey.CAPTURING_MODE);
            return capturingMode.isFront() && !capturingMode.isVideo() && (messageSettings.isNeverShow(MessageType.TUTORIAL_HAND_SHUTTER) ^ true);
        }
        
        @Override
        protected void setupResource() {
            super.setupResource();
            final ArrayList<Integer> mPageResources = this.mPageResources;
            int i;
            if (((TutorialContent)this).isPortrait()) {
                i = 2131493024;
            }
            else {
                i = 2131493023;
            }
            mPageResources.add(i);
            final ArrayList<MediaContentsResource> mMediaContentsResourceId = this.mMediaContentsResourceId;
            int n;
            if (((TutorialContent)this).isPortrait()) {
                n = 2131231237;
            }
            else {
                n = 2131231236;
            }
            mMediaContentsResourceId.add(new MediaContentsResource(n, MediaContentsResourceType.IMAGE));
            this.mTitleResourceId.add(2131689868);
            this.mDescriptionResourceId.add(2131689866);
        }
    }
    
    protected static final class ManualFusionTutorialContent extends PagingTutorialContent
    {
        protected ManualFusionTutorialContent(final int n) {
            super(n);
        }
        
        @Override
        protected boolean canShowContent(final StoredSettings storedSettings) {
            return storedSettings.getMessageSettings().isNeverShow(MessageType.TUTORIAL_MANUAL_FUSION) ^ true;
        }
        
        @Override
        protected void setupResource() {
            super.setupResource();
            final ArrayList<Integer> mPageResources = this.mPageResources;
            final boolean portrait = ((TutorialContent)this).isPortrait();
            final int n = 2131493023;
            int i;
            if (portrait) {
                i = 2131493024;
            }
            else {
                i = 2131493023;
            }
            mPageResources.add(i);
            final ArrayList<Integer> mPageResources2 = this.mPageResources;
            int j = n;
            if (((TutorialContent)this).isPortrait()) {
                j = 2131493024;
            }
            mPageResources2.add(j);
            final ArrayList<MediaContentsResource> mMediaContentsResourceId = this.mMediaContentsResourceId;
            int n2;
            if (((TutorialContent)this).isPortrait()) {
                n2 = 2131231243;
            }
            else {
                n2 = 2131231242;
            }
            mMediaContentsResourceId.add(new MediaContentsResource(n2, MediaContentsResourceType.IMAGE));
            final ArrayList<MediaContentsResource> mMediaContentsResourceId2 = this.mMediaContentsResourceId;
            int n3;
            if (((TutorialContent)this).isPortrait()) {
                n3 = 2131231411;
            }
            else {
                n3 = 2131231410;
            }
            mMediaContentsResourceId2.add(new MediaContentsResource(n3, MediaContentsResourceType.IMAGE));
            this.mTitleResourceId.add(2131689940);
            this.mTitleResourceId.add(2131689938);
            this.mDescriptionResourceId.add(2131689941);
            this.mDescriptionResourceId.add(2131689939);
        }
    }
    
    private static class MediaContentsResource
    {
        private final int mResourceId;
        private final MediaContentsResourceType mType;
        
        MediaContentsResource(final int mResourceId, final MediaContentsResourceType mType) {
            this.mResourceId = mResourceId;
            this.mType = mType;
        }
        
        public int getId() {
            return this.mResourceId;
        }
        
        public MediaContentsResourceType getType() {
            return this.mType;
        }
    }
    
    private enum MediaContentsResourceType
    {
        private static final MediaContentsResourceType[] $VALUES;
        
        IMAGE, 
        VIDEO;
        
        static {
            $VALUES = new MediaContentsResourceType[] { MediaContentsResourceType.IMAGE, MediaContentsResourceType.VIDEO };
        }
    }
    
    protected static final class OneShotSlowTutorialContent extends PagingTutorialContent
    {
        protected OneShotSlowTutorialContent(final int n) {
            super(n);
        }
        
        protected OneShotSlowTutorialContent(final int n, final Object... array) {
            super(n, array);
        }
        
        @Override
        protected boolean canShowContent(final StoredSettings storedSettings) {
            final UserSettings userSettings = storedSettings.getUserSettings();
            return !((CapturingMode)userSettings.get(UserSettingKey.CAPTURING_MODE)).isFront() && userSettings.get(UserSettingKey.SLOW_MOTION) == SlowMotion.SUPER_SLOW_SHOT && (storedSettings.getMessageSettings().isNeverShow(MessageType.TUTORIAL_SUPER_SLOW_MOTION_SHOT) ^ true);
        }
        
        @SuppressLint({ "StringFormatInvalid" })
        @Override
        protected ViewGroup getPageContentView(final Context context, final int n) {
            final int intValue = (int)this.mParams[0];
            final ViewGroup pageContentView = super.getPageContentView(context, n);
            if (n == 0) {
                final TextView descriptionView = ((PagingTutorialContent)this).getDescriptionView(pageContentView);
                final String string = context.getString(2131690140, new Object[] { Integer.toString(intValue) });
                descriptionView.setText((CharSequence)string);
                descriptionView.setContentDescription((CharSequence)string);
            }
            return pageContentView;
        }
        
        @Override
        protected void setupResource() {
            super.setupResource();
            final ArrayList<Integer> mPageResources = this.mPageResources;
            int i;
            if (((TutorialContent)this).isPortrait()) {
                i = 2131493024;
            }
            else {
                i = 2131493023;
            }
            mPageResources.add(i);
            final ArrayList<MediaContentsResource> mMediaContentsResourceId = this.mMediaContentsResourceId;
            int n;
            if (((TutorialContent)this).isPortrait()) {
                n = 2131231342;
            }
            else {
                n = 2131231341;
            }
            mMediaContentsResourceId.add(new MediaContentsResource(n, MediaContentsResourceType.IMAGE));
            this.mTitleResourceId.add(2131690133);
            this.mDescriptionResourceId.add(2131690138);
        }
    }
    
    private static final class PageContentsAdapter extends PagerAdapter
    {
        private PagingTutorialContent mContent;
        
        public PageContentsAdapter(final PagingTutorialContent mContent) {
            this.mContent = mContent;
        }
        
        @Override
        public void destroyItem(final ViewGroup viewGroup, final int n, final Object o) {
            viewGroup.removeView((View)o);
        }
        
        @Override
        public int getCount() {
            return this.mContent.mPageResources.size();
        }
        
        @Override
        public Object instantiateItem(final ViewGroup viewGroup, final int n) {
            final ViewGroup pageContentView = this.mContent.getPageContentView(viewGroup.getContext(), n);
            viewGroup.addView((View)pageContentView);
            return pageContentView;
        }
        
        @Override
        public boolean isViewFromObject(final View view, final Object obj) {
            return view.equals(obj);
        }
    }
    
    abstract static class PagingTutorialContent extends TutorialContent
    {
        protected ArrayList<Integer> mDescriptionResourceId;
        protected ArrayList<MediaContentsResource> mMediaContentsResourceId;
        protected TutorialNavigatorType mNavigatorType;
        protected int mPageIndex;
        protected ArrayList<Integer> mPageResources;
        protected ArrayList<Integer> mTitleResourceId;
        protected List<TutorialContent> mTutorialContents;
        protected List<TutorialController.TutorialType> mTutorialTypes;
        
        protected PagingTutorialContent(final int n) {
            super(n);
            this.mPageIndex = 0;
        }
        
        protected PagingTutorialContent(final int n, final List<TutorialController.TutorialType> list, final List<TutorialContent> list2) {
            super(n, new Object[] { list2 });
            this.mPageIndex = 0;
        }
        
        protected PagingTutorialContent(final int n, final Object... array) {
            super(n, array);
            this.mPageIndex = 0;
        }
        
        @Override
        protected TutorialPageInfo getCurrentTutorialPageInfo() {
            return this.getCurrentTutorialPageInfo(this.mPageIndex);
        }
        
        @Override
        protected TutorialPageInfo getCurrentTutorialPageInfo(final int n) {
            final int size = this.mTutorialTypes.size();
            int i = 0;
            int n2 = n;
            int n3 = 0;
            while (i < size) {
                n3 += this.mTutorialContents.get(i).getPages();
                if (n <= n3 - 1) {
                    return new TutorialPageInfo(this.mTutorialTypes.get(i), n2);
                }
                n2 -= n3;
                ++i;
            }
            return new TutorialPageInfo(this.mTutorialTypes.get(size - 1), n2);
        }
        
        public final List<Integer> getDescriptionResources() {
            return this.mDescriptionResourceId;
        }
        
        protected TextView getDescriptionView(final ViewGroup viewGroup) {
            return (TextView)viewGroup.findViewById(2131296677);
        }
        
        public final List<MediaContentsResource> getMediaContentsResources() {
            return this.mMediaContentsResourceId;
        }
        
        protected TutorialNavigatorType getNavigatorType() {
            return this.mNavigatorType;
        }
        
        protected ViewGroup getPageContentView(final Context context, final int index) {
            final ViewGroup viewGroup = (ViewGroup)View.inflate(context, (int)this.mPageResources.get(index), (ViewGroup)null);
            final TextView titleView = this.getTitleView(viewGroup);
            titleView.setText((int)this.mTitleResourceId.get(index));
            titleView.setContentDescription((CharSequence)viewGroup.getResources().getString((int)this.mTitleResourceId.get(index)));
            final TextView descriptionView = this.getDescriptionView(viewGroup);
            descriptionView.setText((int)this.mDescriptionResourceId.get(index));
            descriptionView.setContentDescription((CharSequence)viewGroup.getResources().getString((int)this.mDescriptionResourceId.get(index)));
            descriptionView.setVerticalScrollBarEnabled(true);
            descriptionView.setMovementMethod(ScrollingMovementMethod.getInstance());
            return viewGroup;
        }
        
        public final List<Integer> getPageResources() {
            return this.mPageResources;
        }
        
        @Override
        protected int getPages() {
            return this.mPageResources.size();
        }
        
        protected String getStepText(final Context context, final int i, final int j) {
            return context.getString(2131690171, new Object[] { Integer.toString(i), Integer.toString(j) });
        }
        
        public final List<Integer> getTitleResources() {
            return this.mTitleResourceId;
        }
        
        protected TextView getTitleView(final ViewGroup viewGroup) {
            return (TextView)viewGroup.findViewById(2131296679);
        }
        
        @Override
        protected TutorialContent getTutorialContent(final TutorialController.TutorialType tutorialType) {
            for (int i = 0; i < this.mTutorialTypes.size(); ++i) {
                if (this.mTutorialTypes.get(i) == tutorialType) {
                    return this.mTutorialContents.get(i);
                }
            }
            return null;
        }
        
        @Override
        protected List<TutorialController.TutorialType> getTutorialTypes() {
            return this.mTutorialTypes;
        }
        
        @Override
        protected boolean isSimpleTutorialContent() {
            return false;
        }
        
        @Override
        protected void setupResource() {
            this.mPageResources = new ArrayList<Integer>();
            this.mTitleResourceId = new ArrayList<Integer>();
            this.mDescriptionResourceId = new ArrayList<Integer>();
            this.mMediaContentsResourceId = new ArrayList<MediaContentsResource>();
            if (this.mNavigatorType == null) {
                this.mNavigatorType = TutorialNavigatorType.NORMAL;
            }
            int mLayoutId;
            if (((TutorialContent)this).isPortrait()) {
                mLayoutId = 2131492962;
            }
            else {
                mLayoutId = 2131492961;
            }
            this.mLayoutId = mLayoutId;
        }
    }
    
    protected final class PagingTutorialController
    {
        final PagingTutorialContentView this$0;
        
        protected PagingTutorialController(final PagingTutorialContentView this$0) {
            this.this$0 = this$0;
        }
        
        public void closeTutorial(final View view) {
            this.this$0.notifyOnDoneClicked(view);
        }
        
        public void movePageToBack() {
            this.this$0.mPager.setCurrentItem(this.this$0.getContent().mPageIndex - 1);
        }
        
        public void movePageToNext() {
            this.this$0.mPager.setCurrentItem(this.this$0.getContent().mPageIndex + 1);
        }
    }
    
    protected static final class PredictiveLaunchTutorialContent extends PagingTutorialContent
    {
        protected PredictiveLaunchTutorialContent(final int n) {
            super(n);
        }
        
        @Override
        protected boolean canShowContent(final StoredSettings storedSettings) {
            return PlatformCapability.isLiftTriggerSupported();
        }
        
        @Override
        protected void setupResource() {
            super.setupResource();
            final ArrayList<Integer> mPageResources = this.mPageResources;
            int i;
            if (((TutorialContent)this).isPortrait()) {
                i = 2131493024;
            }
            else {
                i = 2131493023;
            }
            mPageResources.add(i);
            this.mMediaContentsResourceId.add(new MediaContentsResource(2131623936, MediaContentsResourceType.VIDEO));
            this.mTitleResourceId.add(2131690014);
            this.mDescriptionResourceId.add(2131690013);
        }
    }
    
    protected static final class SideSenseTutorialContent extends PagingTutorialContent
    {
        public SideSenseTutorialContent(final int n) {
            super(n);
        }
        
        @Override
        protected boolean canShowContent(final StoredSettings storedSettings) {
            return PlatformCapability.isSideTouchSupported();
        }
        
        @Override
        protected void setupResource() {
            super.setupResource();
            final ArrayList<Integer> mPageResources = this.mPageResources;
            int i;
            if (((TutorialContent)this).isPortrait()) {
                i = 2131493024;
            }
            else {
                i = 2131493023;
            }
            mPageResources.add(i);
            this.mMediaContentsResourceId.add(new MediaContentsResource(2131623937, MediaContentsResourceType.VIDEO));
            this.mTitleResourceId.add(2131690126);
            this.mDescriptionResourceId.add(2131690130);
        }
    }
    
    protected static final class StandardSlowTutorialContent extends PagingTutorialContent
    {
        protected StandardSlowTutorialContent(final int n) {
            super(n);
        }
        
        @Override
        protected boolean canShowContent(final StoredSettings storedSettings) {
            final UserSettings userSettings = storedSettings.getUserSettings();
            return !((CapturingMode)userSettings.get(UserSettingKey.CAPTURING_MODE)).isFront() && userSettings.get(UserSettingKey.SLOW_MOTION) == SlowMotion.STANDARD_SLOW_MOTION && (storedSettings.getMessageSettings().isNeverShow(MessageType.TUTORIAL_STANDARD_SLOW_MOTION) ^ true);
        }
        
        @Override
        protected void setupResource() {
            super.setupResource();
            final ArrayList<Integer> mPageResources = this.mPageResources;
            final boolean portrait = ((TutorialContent)this).isPortrait();
            final int n = 2131493023;
            int i;
            if (portrait) {
                i = 2131493024;
            }
            else {
                i = 2131493023;
            }
            mPageResources.add(i);
            final ArrayList<Integer> mPageResources2 = this.mPageResources;
            int j = n;
            if (((TutorialContent)this).isPortrait()) {
                j = 2131493024;
            }
            mPageResources2.add(j);
            final ArrayList<MediaContentsResource> mMediaContentsResourceId = this.mMediaContentsResourceId;
            int n2;
            if (((TutorialContent)this).isPortrait()) {
                n2 = 2131231348;
            }
            else {
                n2 = 2131231347;
            }
            mMediaContentsResourceId.add(new MediaContentsResource(n2, MediaContentsResourceType.IMAGE));
            final ArrayList<MediaContentsResource> mMediaContentsResourceId2 = this.mMediaContentsResourceId;
            int n3;
            if (((TutorialContent)this).isPortrait()) {
                n3 = 2131231350;
            }
            else {
                n3 = 2131231349;
            }
            mMediaContentsResourceId2.add(new MediaContentsResource(n3, MediaContentsResourceType.IMAGE));
            this.mTitleResourceId.add(2131690134);
            this.mTitleResourceId.add(2131690147);
            this.mDescriptionResourceId.add(2131690144);
            this.mDescriptionResourceId.add(2131690145);
        }
    }
    
    protected static final class SuperSlowMoreOptionsTutorialContent extends PagingTutorialContent
    {
        protected SuperSlowMoreOptionsTutorialContent(final int n) {
            super(n);
        }
        
        @Override
        protected boolean canShowContent(final StoredSettings storedSettings) {
            final UserSettings userSettings = storedSettings.getUserSettings();
            return !((CapturingMode)userSettings.get(UserSettingKey.CAPTURING_MODE)).isFront() && userSettings.get(UserSettingKey.SLOW_MOTION) == SlowMotion.SUPER_SLOW_MOTION && (storedSettings.getMessageSettings().isNeverShow(MessageType.TUTORIAL_SUPER_SLOW_MOTION) ^ true);
        }
        
        @Override
        protected ViewGroup getPageContentView(final Context context, final int n) {
            final ViewGroup pageContentView = super.getPageContentView(context, n);
            if (n > 0 && n < 3) {
                final TextView titleView = ((PagingTutorialContent)this).getTitleView(pageContentView);
                final String stepText = ((PagingTutorialContent)this).getStepText(context, n, this.mPageResources.size() - 2);
                titleView.setText((CharSequence)stepText);
                titleView.setContentDescription((CharSequence)stepText);
            }
            return pageContentView;
        }
        
        @Override
        protected void setupResource() {
            super.setupResource();
            final ArrayList<Integer> mPageResources = this.mPageResources;
            final boolean portrait = ((TutorialContent)this).isPortrait();
            final int n = 2131493023;
            int i;
            if (portrait) {
                i = 2131493024;
            }
            else {
                i = 2131493023;
            }
            mPageResources.add(i);
            final ArrayList<Integer> mPageResources2 = this.mPageResources;
            int j;
            if (((TutorialContent)this).isPortrait()) {
                j = 2131493024;
            }
            else {
                j = 2131493023;
            }
            mPageResources2.add(j);
            final ArrayList<Integer> mPageResources3 = this.mPageResources;
            int k;
            if (((TutorialContent)this).isPortrait()) {
                k = 2131493024;
            }
            else {
                k = 2131493023;
            }
            mPageResources3.add(k);
            final ArrayList<Integer> mPageResources4 = this.mPageResources;
            int l = n;
            if (((TutorialContent)this).isPortrait()) {
                l = 2131493024;
            }
            mPageResources4.add(l);
            final ArrayList<MediaContentsResource> mMediaContentsResourceId = this.mMediaContentsResourceId;
            int n2;
            if (((TutorialContent)this).isPortrait()) {
                n2 = 2131231360;
            }
            else {
                n2 = 2131231359;
            }
            mMediaContentsResourceId.add(new MediaContentsResource(n2, MediaContentsResourceType.IMAGE));
            final ArrayList<MediaContentsResource> mMediaContentsResourceId2 = this.mMediaContentsResourceId;
            int n3;
            if (((TutorialContent)this).isPortrait()) {
                n3 = 2131231362;
            }
            else {
                n3 = 2131231361;
            }
            mMediaContentsResourceId2.add(new MediaContentsResource(n3, MediaContentsResourceType.IMAGE));
            final ArrayList<MediaContentsResource> mMediaContentsResourceId3 = this.mMediaContentsResourceId;
            int n4;
            if (((TutorialContent)this).isPortrait()) {
                n4 = 2131231364;
            }
            else {
                n4 = 2131231363;
            }
            mMediaContentsResourceId3.add(new MediaContentsResource(n4, MediaContentsResourceType.IMAGE));
            final ArrayList<MediaContentsResource> mMediaContentsResourceId4 = this.mMediaContentsResourceId;
            int n5;
            if (((TutorialContent)this).isPortrait()) {
                n5 = 2131231336;
            }
            else {
                n5 = 2131231335;
            }
            mMediaContentsResourceId4.add(new MediaContentsResource(n5, MediaContentsResourceType.IMAGE));
            this.mTitleResourceId.add(2131690135);
            this.mTitleResourceId.add(2131690171);
            this.mTitleResourceId.add(2131690171);
            this.mTitleResourceId.add(2131690137);
            this.mDescriptionResourceId.add(2131690149);
            this.mDescriptionResourceId.add(2131690154);
            this.mDescriptionResourceId.add(2131690155);
            this.mDescriptionResourceId.add(2131690136);
        }
    }
    
    protected static final class SuperSlowTutorialContent extends PagingTutorialContent
    {
        protected SuperSlowTutorialContent(final int n) {
            super(n);
        }
        
        @Override
        protected ViewGroup getPageContentView(final Context context, final int n) {
            final ViewGroup pageContentView = super.getPageContentView(context, n);
            if (n > 0) {
                final TextView titleView = ((PagingTutorialContent)this).getTitleView(pageContentView);
                final String stepText = ((PagingTutorialContent)this).getStepText(context, n, this.mPageResources.size() - 1);
                titleView.setText((CharSequence)stepText);
                titleView.setContentDescription((CharSequence)stepText);
            }
            return pageContentView;
        }
        
        @Override
        protected void setupResource() {
            super.setupResource();
            final ArrayList<Integer> mPageResources = this.mPageResources;
            final boolean portrait = ((TutorialContent)this).isPortrait();
            final int n = 2131493023;
            int i;
            if (portrait) {
                i = 2131493024;
            }
            else {
                i = 2131493023;
            }
            mPageResources.add(i);
            final ArrayList<Integer> mPageResources2 = this.mPageResources;
            int j;
            if (((TutorialContent)this).isPortrait()) {
                j = 2131493024;
            }
            else {
                j = 2131493023;
            }
            mPageResources2.add(j);
            final ArrayList<Integer> mPageResources3 = this.mPageResources;
            int k = n;
            if (((TutorialContent)this).isPortrait()) {
                k = 2131493024;
            }
            mPageResources3.add(k);
            final ArrayList<MediaContentsResource> mMediaContentsResourceId = this.mMediaContentsResourceId;
            int n2;
            if (((TutorialContent)this).isPortrait()) {
                n2 = 2131231360;
            }
            else {
                n2 = 2131231359;
            }
            mMediaContentsResourceId.add(new MediaContentsResource(n2, MediaContentsResourceType.IMAGE));
            final ArrayList<MediaContentsResource> mMediaContentsResourceId2 = this.mMediaContentsResourceId;
            int n3;
            if (((TutorialContent)this).isPortrait()) {
                n3 = 2131231362;
            }
            else {
                n3 = 2131231361;
            }
            mMediaContentsResourceId2.add(new MediaContentsResource(n3, MediaContentsResourceType.IMAGE));
            final ArrayList<MediaContentsResource> mMediaContentsResourceId3 = this.mMediaContentsResourceId;
            int n4;
            if (((TutorialContent)this).isPortrait()) {
                n4 = 2131231364;
            }
            else {
                n4 = 2131231363;
            }
            mMediaContentsResourceId3.add(new MediaContentsResource(n4, MediaContentsResourceType.IMAGE));
            this.mTitleResourceId.add(2131690135);
            this.mTitleResourceId.add(2131690171);
            this.mTitleResourceId.add(2131690171);
            this.mDescriptionResourceId.add(2131690149);
            this.mDescriptionResourceId.add(2131690154);
            this.mDescriptionResourceId.add(2131690155);
        }
    }
    
    enum TutorialNavigatorType
    {
        private static final TutorialNavigatorType[] $VALUES;
        
        CONFIRM, 
        NORMAL;
        
        static {
            $VALUES = new TutorialNavigatorType[] { TutorialNavigatorType.NORMAL, TutorialNavigatorType.CONFIRM };
        }
    }
    
    protected static final class VideoFusionTutorialContent extends PagingTutorialContent
    {
        protected VideoFusionTutorialContent(final int n) {
            super(n);
        }
        
        @Override
        protected boolean canShowContent(final StoredSettings storedSettings) {
            final CapturingMode capturingMode = (CapturingMode)storedSettings.getUserSettings().get(UserSettingKey.CAPTURING_MODE);
            return capturingMode.isVideo() && capturingMode != CapturingMode.SLOW_MOTION && !storedSettings.getMessageSettings().isNeverShow(MessageType.TUTORIAL_VIDEO_FUSION) && PlatformCapability.isHighSensitivityFusionSupported(capturingMode.getCameraId());
        }
        
        @Override
        protected void setupResource() {
            super.setupResource();
            this.mNavigatorType = TutorialNavigatorType.CONFIRM;
            final ArrayList<Integer> mPageResources = this.mPageResources;
            final boolean portrait = ((TutorialContent)this).isPortrait();
            final int n = 2131493023;
            int i;
            if (portrait) {
                i = 2131493024;
            }
            else {
                i = 2131493023;
            }
            mPageResources.add(i);
            final ArrayList<Integer> mPageResources2 = this.mPageResources;
            int j = n;
            if (((TutorialContent)this).isPortrait()) {
                j = 2131493024;
            }
            mPageResources2.add(j);
            final ArrayList<MediaContentsResource> mMediaContentsResourceId = this.mMediaContentsResourceId;
            int n2;
            if (((TutorialContent)this).isPortrait()) {
                n2 = 2131231411;
            }
            else {
                n2 = 2131231410;
            }
            mMediaContentsResourceId.add(new MediaContentsResource(n2, MediaContentsResourceType.IMAGE));
            final ArrayList<MediaContentsResource> mMediaContentsResourceId2 = this.mMediaContentsResourceId;
            int n3;
            if (((TutorialContent)this).isPortrait()) {
                n3 = 2131231413;
            }
            else {
                n3 = 2131231412;
            }
            mMediaContentsResourceId2.add(new MediaContentsResource(n3, MediaContentsResourceType.IMAGE));
            this.mTitleResourceId.add(2131690218);
            this.mTitleResourceId.add(2131690220);
            this.mDescriptionResourceId.add(2131690219);
            this.mDescriptionResourceId.add(2131690221);
        }
    }
}
