package com.sonyericsson.android.camera.view.tutorial;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.duolingo.open.rtlviewpager.DelegatingPagerAdapter;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.configuration.UserSettingKey;
import com.sonyericsson.android.camera.configuration.parameters.CapturingMode;
import com.sonyericsson.android.camera.configuration.parameters.SlowMotion;
import com.sonyericsson.android.camera.device.CameraInfo;
import com.sonyericsson.android.camera.research.LocalResearchUtil;
import com.sonyericsson.android.camera.setting.MessageSettings;
import com.sonyericsson.android.camera.setting.MessageType;
import com.sonyericsson.android.camera.setting.StoredSettings;
import com.sonyericsson.android.camera.setting.UserSettings;
import com.sonyericsson.android.camera.util.CamLog;
import com.sonyericsson.android.camera.util.capability.PlatformCapability;
import com.sonyericsson.android.camera.view.baselayout.LayoutDependencyResolver;
import com.sonyericsson.android.camera.view.tutorial.TutorialContentView;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;
import com.sonymobile.cameracommon.research.parameters.Event;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class PagingTutorialContentView extends TutorialContentView {
    private static final float REVERSE_DEGREE = 180.0f;
    private static final String TAG = "PagingTutorialContentView";
    private static final boolean TRACE = false;
    private static final double VIDEO_ASPECT_RATIO_LANDSCAPE = 0.75d;
    private static final double VIDEO_ASPECT_RATIO_PORTRAIT = 0.816d;
    private ImageView mIcon;
    private MediaPlayer mMediaPlayer;
    private PagingTutorialNavigator mNavigator;
    MediaPlayer.OnErrorListener mOnErrorListener;
    private final ViewPager.OnPageChangeListener mOnPageChangeListener;
    private ViewPager mPager;
    private SurfaceTexture mSurfaceTexture;
    private TutorialVideoView mVideo;

    private enum MediaContentsResourceType {
        IMAGE,
        VIDEO
    }

    enum TutorialNavigatorType {
        NORMAL,
        CONFIRM
    }

    private static void trace(String str) {
        CamLog.d(str);
    }

    private static class MediaContentsResource {
        private final int mResourceId;
        private final MediaContentsResourceType mType;

        MediaContentsResource(int i, MediaContentsResourceType mediaContentsResourceType) {
            this.mResourceId = i;
            this.mType = mediaContentsResourceType;
        }

        public int getId() {
            return this.mResourceId;
        }

        public MediaContentsResourceType getType() {
            return this.mType;
        }
    }

    public PagingTutorialContentView(Context context) {
        super(context);
        this.mOnPageChangeListener = new ViewPager.OnPageChangeListener() { // from class: com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.1
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i, float f, int i2) {
                PagingTutorialContent content = PagingTutorialContentView.this.getContent();
                if (content.mMediaContentsResourceId != null) {
                    MediaContentsResource mediaContentsResource = content.mMediaContentsResourceId.get(i);
                    if (PagingTutorialContentView.this.mIcon == null) {
                        Iterator<MediaContentsResource> it = content.mMediaContentsResourceId.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            if (it.next().getType() == MediaContentsResourceType.IMAGE) {
                                PagingTutorialContentView.this.mIcon = (ImageView) PagingTutorialContentView.this.findViewById(R.id.tutorial_page_icon);
                                break;
                            }
                        }
                        if (mediaContentsResource.getType() == MediaContentsResourceType.IMAGE) {
                            PagingTutorialContentView.this.mIcon.setVisibility(0);
                            PagingTutorialContentView.this.mIcon.setImageResource(mediaContentsResource.getId());
                        }
                    }
                    if (PagingTutorialContentView.this.mVideo == null) {
                        Iterator<MediaContentsResource> it2 = content.mMediaContentsResourceId.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                break;
                            }
                            if (it2.next().getType() == MediaContentsResourceType.VIDEO) {
                                PagingTutorialContentView.this.mVideo = (TutorialVideoView) PagingTutorialContentView.this.findViewById(R.id.tutorial_page_video);
                                PagingTutorialContentView.this.prepareTutorialVideoView(PagingTutorialContentView.this.mVideo);
                                break;
                            }
                        }
                        if (mediaContentsResource.getType() == MediaContentsResourceType.VIDEO) {
                            PagingTutorialContentView.this.mVideo.setSurfaceTextureListener(PagingTutorialContentView.this.createSurfaceTextureListener(i));
                            PagingTutorialContentView.this.mVideo.setVisibility(0);
                        }
                    }
                }
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int i) throws IllegalStateException {
                PagingTutorialContent content = PagingTutorialContentView.this.getContent();
                int i2 = content.mPageIndex;
                content.mPageIndex = i;
                if (content.mMediaContentsResourceId != null) {
                    MediaContentsResource mediaContentsResource = content.mMediaContentsResourceId.get(i);
                    if (PagingTutorialContentView.this.mIcon == null || mediaContentsResource.getType() != MediaContentsResourceType.IMAGE) {
                        if (PagingTutorialContentView.this.mVideo != null && mediaContentsResource.getType() == MediaContentsResourceType.VIDEO) {
                            if (PagingTutorialContentView.this.mSurfaceTexture != null) {
                                PagingTutorialContentView.this.createMediaPlayer(i);
                            } else {
                                PagingTutorialContentView.this.mVideo.setSurfaceTextureListener(PagingTutorialContentView.this.createSurfaceTextureListener(i));
                            }
                            if (PagingTutorialContentView.this.mIcon != null) {
                                PagingTutorialContentView.this.mIcon.setVisibility(8);
                            }
                            PagingTutorialContentView.this.mVideo.setVisibility(0);
                        }
                    } else {
                        PagingTutorialContentView.this.mIcon.setImageResource(mediaContentsResource.getId());
                        if (PagingTutorialContentView.this.mVideo != null) {
                            PagingTutorialContentView.this.mVideo.setVisibility(8);
                            PagingTutorialContentView.this.clearMediaPlayer();
                        }
                        PagingTutorialContentView.this.mIcon.setVisibility(0);
                    }
                }
                TutorialContentView.TutorialPageInfo currentTutorialPageInfo = content.getCurrentTutorialPageInfo(i2);
                if (i2 < content.mPageIndex) {
                    LocalResearchUtil.getInstance().sendSetupWizardEvent(currentTutorialPageInfo.type, currentTutorialPageInfo.pageIndexByType, Event.WizardResult.NEXT);
                } else {
                    LocalResearchUtil.getInstance().sendSetupWizardEvent(currentTutorialPageInfo.type, currentTutorialPageInfo.pageIndexByType, Event.WizardResult.PREVIOUS);
                }
                TutorialContentView.TutorialPageInfo currentTutorialPageInfo2 = content.getCurrentTutorialPageInfo(i);
                LocalResearchUtil.getInstance().startSetupWizard(currentTutorialPageInfo2.type, currentTutorialPageInfo2.pageIndexByType);
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i) throws IllegalStateException {
                switch (i) {
                    case 0:
                        PagingTutorialContentView.this.resume();
                        break;
                    case 1:
                        PagingTutorialContentView.this.suspend();
                        break;
                }
            }
        };
        this.mOnErrorListener = new MediaPlayer.OnErrorListener() { // from class: com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.4
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                CamLog.e("onError() : MediaPlayer = " + mediaPlayer.hashCode() + " what = " + i + " extra = " + i2);
                return false;
            }
        };
    }

    public PagingTutorialContentView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mOnPageChangeListener = new ViewPager.OnPageChangeListener() { // from class: com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.1
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i2, float f, int i22) {
                PagingTutorialContent content = PagingTutorialContentView.this.getContent();
                if (content.mMediaContentsResourceId != null) {
                    MediaContentsResource mediaContentsResource = content.mMediaContentsResourceId.get(i2);
                    if (PagingTutorialContentView.this.mIcon == null) {
                        Iterator<MediaContentsResource> it = content.mMediaContentsResourceId.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            if (it.next().getType() == MediaContentsResourceType.IMAGE) {
                                PagingTutorialContentView.this.mIcon = (ImageView) PagingTutorialContentView.this.findViewById(R.id.tutorial_page_icon);
                                break;
                            }
                        }
                        if (mediaContentsResource.getType() == MediaContentsResourceType.IMAGE) {
                            PagingTutorialContentView.this.mIcon.setVisibility(0);
                            PagingTutorialContentView.this.mIcon.setImageResource(mediaContentsResource.getId());
                        }
                    }
                    if (PagingTutorialContentView.this.mVideo == null) {
                        Iterator<MediaContentsResource> it2 = content.mMediaContentsResourceId.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                break;
                            }
                            if (it2.next().getType() == MediaContentsResourceType.VIDEO) {
                                PagingTutorialContentView.this.mVideo = (TutorialVideoView) PagingTutorialContentView.this.findViewById(R.id.tutorial_page_video);
                                PagingTutorialContentView.this.prepareTutorialVideoView(PagingTutorialContentView.this.mVideo);
                                break;
                            }
                        }
                        if (mediaContentsResource.getType() == MediaContentsResourceType.VIDEO) {
                            PagingTutorialContentView.this.mVideo.setSurfaceTextureListener(PagingTutorialContentView.this.createSurfaceTextureListener(i2));
                            PagingTutorialContentView.this.mVideo.setVisibility(0);
                        }
                    }
                }
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int i2) throws IllegalStateException {
                PagingTutorialContent content = PagingTutorialContentView.this.getContent();
                int i22 = content.mPageIndex;
                content.mPageIndex = i2;
                if (content.mMediaContentsResourceId != null) {
                    MediaContentsResource mediaContentsResource = content.mMediaContentsResourceId.get(i2);
                    if (PagingTutorialContentView.this.mIcon == null || mediaContentsResource.getType() != MediaContentsResourceType.IMAGE) {
                        if (PagingTutorialContentView.this.mVideo != null && mediaContentsResource.getType() == MediaContentsResourceType.VIDEO) {
                            if (PagingTutorialContentView.this.mSurfaceTexture != null) {
                                PagingTutorialContentView.this.createMediaPlayer(i2);
                            } else {
                                PagingTutorialContentView.this.mVideo.setSurfaceTextureListener(PagingTutorialContentView.this.createSurfaceTextureListener(i2));
                            }
                            if (PagingTutorialContentView.this.mIcon != null) {
                                PagingTutorialContentView.this.mIcon.setVisibility(8);
                            }
                            PagingTutorialContentView.this.mVideo.setVisibility(0);
                        }
                    } else {
                        PagingTutorialContentView.this.mIcon.setImageResource(mediaContentsResource.getId());
                        if (PagingTutorialContentView.this.mVideo != null) {
                            PagingTutorialContentView.this.mVideo.setVisibility(8);
                            PagingTutorialContentView.this.clearMediaPlayer();
                        }
                        PagingTutorialContentView.this.mIcon.setVisibility(0);
                    }
                }
                TutorialContentView.TutorialPageInfo currentTutorialPageInfo = content.getCurrentTutorialPageInfo(i22);
                if (i22 < content.mPageIndex) {
                    LocalResearchUtil.getInstance().sendSetupWizardEvent(currentTutorialPageInfo.type, currentTutorialPageInfo.pageIndexByType, Event.WizardResult.NEXT);
                } else {
                    LocalResearchUtil.getInstance().sendSetupWizardEvent(currentTutorialPageInfo.type, currentTutorialPageInfo.pageIndexByType, Event.WizardResult.PREVIOUS);
                }
                TutorialContentView.TutorialPageInfo currentTutorialPageInfo2 = content.getCurrentTutorialPageInfo(i2);
                LocalResearchUtil.getInstance().startSetupWizard(currentTutorialPageInfo2.type, currentTutorialPageInfo2.pageIndexByType);
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i2) throws IllegalStateException {
                switch (i2) {
                    case 0:
                        PagingTutorialContentView.this.resume();
                        break;
                    case 1:
                        PagingTutorialContentView.this.suspend();
                        break;
                }
            }
        };
        this.mOnErrorListener = new MediaPlayer.OnErrorListener() { // from class: com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.4
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mediaPlayer, int i2, int i22) {
                CamLog.e("onError() : MediaPlayer = " + mediaPlayer.hashCode() + " what = " + i2 + " extra = " + i22);
                return false;
            }
        };
    }

    public PagingTutorialContentView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mOnPageChangeListener = new ViewPager.OnPageChangeListener() { // from class: com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.1
            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrolled(int i2, float f, int i22) {
                PagingTutorialContent content = PagingTutorialContentView.this.getContent();
                if (content.mMediaContentsResourceId != null) {
                    MediaContentsResource mediaContentsResource = content.mMediaContentsResourceId.get(i2);
                    if (PagingTutorialContentView.this.mIcon == null) {
                        Iterator<MediaContentsResource> it = content.mMediaContentsResourceId.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            if (it.next().getType() == MediaContentsResourceType.IMAGE) {
                                PagingTutorialContentView.this.mIcon = (ImageView) PagingTutorialContentView.this.findViewById(R.id.tutorial_page_icon);
                                break;
                            }
                        }
                        if (mediaContentsResource.getType() == MediaContentsResourceType.IMAGE) {
                            PagingTutorialContentView.this.mIcon.setVisibility(0);
                            PagingTutorialContentView.this.mIcon.setImageResource(mediaContentsResource.getId());
                        }
                    }
                    if (PagingTutorialContentView.this.mVideo == null) {
                        Iterator<MediaContentsResource> it2 = content.mMediaContentsResourceId.iterator();
                        while (true) {
                            if (!it2.hasNext()) {
                                break;
                            }
                            if (it2.next().getType() == MediaContentsResourceType.VIDEO) {
                                PagingTutorialContentView.this.mVideo = (TutorialVideoView) PagingTutorialContentView.this.findViewById(R.id.tutorial_page_video);
                                PagingTutorialContentView.this.prepareTutorialVideoView(PagingTutorialContentView.this.mVideo);
                                break;
                            }
                        }
                        if (mediaContentsResource.getType() == MediaContentsResourceType.VIDEO) {
                            PagingTutorialContentView.this.mVideo.setSurfaceTextureListener(PagingTutorialContentView.this.createSurfaceTextureListener(i2));
                            PagingTutorialContentView.this.mVideo.setVisibility(0);
                        }
                    }
                }
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageSelected(int i2) throws IllegalStateException {
                PagingTutorialContent content = PagingTutorialContentView.this.getContent();
                int i22 = content.mPageIndex;
                content.mPageIndex = i2;
                if (content.mMediaContentsResourceId != null) {
                    MediaContentsResource mediaContentsResource = content.mMediaContentsResourceId.get(i2);
                    if (PagingTutorialContentView.this.mIcon == null || mediaContentsResource.getType() != MediaContentsResourceType.IMAGE) {
                        if (PagingTutorialContentView.this.mVideo != null && mediaContentsResource.getType() == MediaContentsResourceType.VIDEO) {
                            if (PagingTutorialContentView.this.mSurfaceTexture != null) {
                                PagingTutorialContentView.this.createMediaPlayer(i2);
                            } else {
                                PagingTutorialContentView.this.mVideo.setSurfaceTextureListener(PagingTutorialContentView.this.createSurfaceTextureListener(i2));
                            }
                            if (PagingTutorialContentView.this.mIcon != null) {
                                PagingTutorialContentView.this.mIcon.setVisibility(8);
                            }
                            PagingTutorialContentView.this.mVideo.setVisibility(0);
                        }
                    } else {
                        PagingTutorialContentView.this.mIcon.setImageResource(mediaContentsResource.getId());
                        if (PagingTutorialContentView.this.mVideo != null) {
                            PagingTutorialContentView.this.mVideo.setVisibility(8);
                            PagingTutorialContentView.this.clearMediaPlayer();
                        }
                        PagingTutorialContentView.this.mIcon.setVisibility(0);
                    }
                }
                TutorialContentView.TutorialPageInfo currentTutorialPageInfo = content.getCurrentTutorialPageInfo(i22);
                if (i22 < content.mPageIndex) {
                    LocalResearchUtil.getInstance().sendSetupWizardEvent(currentTutorialPageInfo.type, currentTutorialPageInfo.pageIndexByType, Event.WizardResult.NEXT);
                } else {
                    LocalResearchUtil.getInstance().sendSetupWizardEvent(currentTutorialPageInfo.type, currentTutorialPageInfo.pageIndexByType, Event.WizardResult.PREVIOUS);
                }
                TutorialContentView.TutorialPageInfo currentTutorialPageInfo2 = content.getCurrentTutorialPageInfo(i2);
                LocalResearchUtil.getInstance().startSetupWizard(currentTutorialPageInfo2.type, currentTutorialPageInfo2.pageIndexByType);
            }

            @Override // android.support.v4.view.ViewPager.OnPageChangeListener
            public void onPageScrollStateChanged(int i2) throws IllegalStateException {
                switch (i2) {
                    case 0:
                        PagingTutorialContentView.this.resume();
                        break;
                    case 1:
                        PagingTutorialContentView.this.suspend();
                        break;
                }
            }
        };
        this.mOnErrorListener = new MediaPlayer.OnErrorListener() { // from class: com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.4
            @Override // android.media.MediaPlayer.OnErrorListener
            public boolean onError(MediaPlayer mediaPlayer, int i2, int i22) {
                CamLog.e("onError() : MediaPlayer = " + mediaPlayer.hashCode() + " what = " + i2 + " extra = " + i22);
                return false;
            }
        };
    }

    @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView
    protected void onUpdateViewContent() throws Resources.NotFoundException {
        PagingTutorialContent content = getContent();
        if (content == null) {
            return;
        }
        this.mPager = (ViewPager) findViewById(R.id.tutorial_view_pager);
        this.mPager.setAdapter(new DelegatingPagerAdapter(new PageContentsAdapter(content)));
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.tutorial_paging_navigator_container);
        if (AnonymousClass5.$SwitchMap$com$sonyericsson$android$camera$view$tutorial$PagingTutorialContentView$TutorialNavigatorType[content.mNavigatorType.ordinal()] == 1) {
            this.mNavigator = (PagingTutorialConfirmNavigator) View.inflate(getContext(), R.layout.paging_tutorial_confirm_navigator, null);
        } else {
            this.mNavigator = (PagingTutorialNormalNavigator) View.inflate(getContext(), R.layout.paging_tutorial_navigator, null);
        }
        viewGroup.addView(this.mNavigator);
        this.mNavigator.setViewController(new PagingTutorialController());
        this.mNavigator.setPageSize(content.mPageResources.size());
        if (getResources().getConfiguration().getLayoutDirection() == 1) {
            this.mNavigator.setRotationY(REVERSE_DEGREE);
        }
        this.mNavigator.onPageSelected(content.mPageIndex);
        this.mPager.setCurrentItem(content.mPageIndex);
        this.mPager.addOnPageChangeListener(this.mNavigator);
        this.mPager.addOnPageChangeListener(this.mOnPageChangeListener);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mPager.clearOnPageChangeListeners();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TextureView.SurfaceTextureListener createSurfaceTextureListener(final int i) {
        return new TextureView.SurfaceTextureListener() { // from class: com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.2
            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i2, int i3) {
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i2, int i3) throws IllegalStateException {
                PagingTutorialContentView.this.mSurfaceTexture = surfaceTexture;
                PagingTutorialContentView.this.createMediaPlayer(i);
            }

            @Override // android.view.TextureView.SurfaceTextureListener
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) throws IllegalStateException {
                PagingTutorialContentView.this.clearMediaPlayer();
                return false;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prepareTutorialVideoView(TutorialVideoView tutorialVideoView) {
        int i;
        Context context = getContext();
        PagingTutorialContent content = getContent();
        int iHeight = LayoutDependencyResolver.getViewFinderSize(context).height();
        double d = content.isPortrait() ? VIDEO_ASPECT_RATIO_PORTRAIT : VIDEO_ASPECT_RATIO_LANDSCAPE;
        if (content.isPortrait()) {
            iHeight = (int) (iHeight * d);
            i = iHeight;
        } else {
            i = (int) (iHeight * d);
        }
        ViewGroup.LayoutParams layoutParams = tutorialVideoView.getLayoutParams();
        layoutParams.width = i;
        layoutParams.height = iHeight;
        tutorialVideoView.setLayoutParams(layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createMediaPlayer(int i) throws IllegalStateException {
        clearMediaPlayer();
        this.mMediaPlayer = MediaPlayer.create(getContext(), getContent().mMediaContentsResourceId.get(i).getId());
        this.mVideo.setVideoAspectRatio(this.mMediaPlayer.getVideoHeight() / this.mMediaPlayer.getVideoWidth());
        this.mMediaPlayer.setSurface(new Surface(this.mSurfaceTexture));
        this.mMediaPlayer.setOnErrorListener(this.mOnErrorListener);
        this.mMediaPlayer.setLooping(true);
        this.mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() { // from class: com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.3
            @Override // android.media.MediaPlayer.OnPreparedListener
            public void onPrepared(MediaPlayer mediaPlayer) throws IllegalStateException {
                PagingTutorialContentView.this.mVideo.updateScale();
                PagingTutorialContentView.this.mMediaPlayer.start();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearMediaPlayer() throws IllegalStateException {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.reset();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resume() throws IllegalStateException {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void suspend() throws IllegalStateException {
        if (this.mMediaPlayer == null || !this.mMediaPlayer.isPlaying()) {
            return;
        }
        this.mMediaPlayer.pause();
    }

    protected final class PagingTutorialController {
        protected PagingTutorialController() {
        }

        public void closeTutorial(View view) {
            PagingTutorialContentView.this.notifyOnDoneClicked(view);
        }

        public void movePageToBack() {
            PagingTutorialContentView.this.mPager.setCurrentItem(PagingTutorialContentView.this.getContent().mPageIndex - 1);
        }

        public void movePageToNext() {
            PagingTutorialContentView.this.mPager.setCurrentItem(PagingTutorialContentView.this.getContent().mPageIndex + 1);
        }
    }

    private static final class PageContentsAdapter extends PagerAdapter {
        private PagingTutorialContent mContent;

        public PageContentsAdapter(PagingTutorialContent pagingTutorialContent) {
            this.mContent = pagingTutorialContent;
        }

        @Override // android.support.v4.view.PagerAdapter
        public int getCount() {
            return this.mContent.mPageResources.size();
        }

        @Override // android.support.v4.view.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            return view.equals(obj);
        }

        @Override // android.support.v4.view.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            ViewGroup pageContentView = this.mContent.getPageContentView(viewGroup.getContext(), i);
            viewGroup.addView(pageContentView);
            return pageContentView;
        }

        @Override // android.support.v4.view.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((View) obj);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PagingTutorialContent getContent() {
        return (PagingTutorialContent) this.mContent;
    }

    static abstract class PagingTutorialContent extends TutorialContentView.TutorialContent {
        protected ArrayList<Integer> mDescriptionResourceId;
        protected ArrayList<MediaContentsResource> mMediaContentsResourceId;
        protected TutorialNavigatorType mNavigatorType;
        protected int mPageIndex;
        protected ArrayList<Integer> mPageResources;
        protected ArrayList<Integer> mTitleResourceId;
        protected List<TutorialContentView.TutorialContent> mTutorialContents;
        protected List<TutorialController.TutorialType> mTutorialTypes;

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected boolean isSimpleTutorialContent() {
            return false;
        }

        protected PagingTutorialContent(int i) {
            super(i);
            this.mPageIndex = 0;
        }

        protected PagingTutorialContent(int i, Object... objArr) {
            super(i, objArr);
            this.mPageIndex = 0;
        }

        protected PagingTutorialContent(int i, List<TutorialController.TutorialType> list, List<TutorialContentView.TutorialContent> list2) {
            super(i, list2);
            this.mPageIndex = 0;
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected TutorialContentView.TutorialPageInfo getCurrentTutorialPageInfo() {
            return getCurrentTutorialPageInfo(this.mPageIndex);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected TutorialContentView.TutorialPageInfo getCurrentTutorialPageInfo(int i) {
            int size = this.mTutorialTypes.size();
            int i2 = i;
            int pages = 0;
            for (int i3 = 0; i3 < size; i3++) {
                pages += this.mTutorialContents.get(i3).getPages();
                if (i <= pages - 1) {
                    return new TutorialContentView.TutorialPageInfo(this.mTutorialTypes.get(i3), i2);
                }
                i2 -= pages;
            }
            return new TutorialContentView.TutorialPageInfo(this.mTutorialTypes.get(size - 1), i2);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected TutorialContentView.TutorialContent getTutorialContent(TutorialController.TutorialType tutorialType) {
            for (int i = 0; i < this.mTutorialTypes.size(); i++) {
                if (this.mTutorialTypes.get(i) == tutorialType) {
                    return this.mTutorialContents.get(i);
                }
            }
            return null;
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected List<TutorialController.TutorialType> getTutorialTypes() {
            return this.mTutorialTypes;
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected void setupResource() {
            this.mPageResources = new ArrayList<>();
            this.mTitleResourceId = new ArrayList<>();
            this.mDescriptionResourceId = new ArrayList<>();
            this.mMediaContentsResourceId = new ArrayList<>();
            if (this.mNavigatorType == null) {
                this.mNavigatorType = TutorialNavigatorType.NORMAL;
            }
            this.mLayoutId = isPortrait() ? R.layout.paging_tutorial_layout_port : R.layout.paging_tutorial_layout_land;
        }

        protected ViewGroup getPageContentView(Context context, int i) {
            ViewGroup viewGroup = (ViewGroup) View.inflate(context, this.mPageResources.get(i).intValue(), null);
            TextView titleView = getTitleView(viewGroup);
            titleView.setText(this.mTitleResourceId.get(i).intValue());
            titleView.setContentDescription(viewGroup.getResources().getString(this.mTitleResourceId.get(i).intValue()));
            TextView descriptionView = getDescriptionView(viewGroup);
            descriptionView.setText(this.mDescriptionResourceId.get(i).intValue());
            descriptionView.setContentDescription(viewGroup.getResources().getString(this.mDescriptionResourceId.get(i).intValue()));
            descriptionView.setVerticalScrollBarEnabled(true);
            descriptionView.setMovementMethod(ScrollingMovementMethod.getInstance());
            return viewGroup;
        }

        protected TextView getTitleView(ViewGroup viewGroup) {
            return (TextView) viewGroup.findViewById(R.id.tutorial_page_title);
        }

        protected TextView getDescriptionView(ViewGroup viewGroup) {
            return (TextView) viewGroup.findViewById(R.id.tutorial_page_description);
        }

        protected String getStepText(Context context, int i, int i2) {
            return context.getString(R.string.cam_strings_step_tutorial_title_txt, Integer.toString(i), Integer.toString(i2));
        }

        public final List<Integer> getPageResources() {
            return this.mPageResources;
        }

        public final List<Integer> getTitleResources() {
            return this.mTitleResourceId;
        }

        public final List<Integer> getDescriptionResources() {
            return this.mDescriptionResourceId;
        }

        public final List<MediaContentsResource> getMediaContentsResources() {
            return this.mMediaContentsResourceId;
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected int getPages() {
            return this.mPageResources.size();
        }

        protected TutorialNavigatorType getNavigatorType() {
            return this.mNavigatorType;
        }
    }

    protected static final class CustomTutorialContent extends PagingTutorialContent {
        public CustomTutorialContent(int i, List<TutorialController.TutorialType> list, List<TutorialContentView.TutorialContent> list2) {
            super(i);
            this.mTutorialTypes = list;
            this.mTutorialContents = list2;
            this.mNavigatorType = ((PagingTutorialContent) list2.get(0)).getNavigatorType();
            mergeContents(list2);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.PagingTutorialContent, com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected void setupResource() {
            super.setupResource();
            if (this.mTutorialContents != null) {
                for (TutorialContentView.TutorialContent tutorialContent : this.mTutorialContents) {
                    tutorialContent.mOrientation = this.mOrientation;
                    tutorialContent.setupResource();
                }
                mergeContents(this.mTutorialContents);
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.PagingTutorialContent
        protected ViewGroup getPageContentView(Context context, int i) {
            TutorialController.TutorialType tutorialType = getCurrentTutorialPageInfo().type;
            int i2 = getCurrentTutorialPageInfo(i).pageIndexByType;
            TutorialContentView.TutorialContent tutorialContent = getTutorialContent(tutorialType);
            ViewGroup pageContentView = super.getPageContentView(context, i);
            switch (tutorialType) {
                case SUPER_SLOW_MOTION:
                    if (i2 > 0) {
                        TextView titleView = getTitleView(pageContentView);
                        String stepText = getStepText(context, i2, this.mPageResources.size() - 1);
                        titleView.setText(stepText);
                        titleView.setContentDescription(stepText);
                    }
                    return pageContentView;
                case SUPER_SLOW_MOTION_MORE_OPTIONS:
                    if (i2 > 0 && i2 < 3) {
                        TextView titleView2 = getTitleView(pageContentView);
                        String stepText2 = getStepText(context, i2, this.mPageResources.size() - 2);
                        titleView2.setText(stepText2);
                        titleView2.setContentDescription(stepText2);
                    }
                    return pageContentView;
                case SUPER_SLOW_MOTION_SHOT:
                    int iIntValue = ((Integer) tutorialContent.mParams[0]).intValue();
                    if (i2 == 0) {
                        TextView descriptionView = getDescriptionView(pageContentView);
                        String string = context.getString(R.string.cam_strings_slow_motion_shot_tutorial_1_txt, Integer.toString(iIntValue));
                        descriptionView.setText(string);
                        descriptionView.setContentDescription(string);
                    }
                    return pageContentView;
                default:
                    return pageContentView;
            }
        }

        private void mergeContents(List<TutorialContentView.TutorialContent> list) {
            for (TutorialContentView.TutorialContent tutorialContent : list) {
                if (tutorialContent instanceof PagingTutorialContent) {
                    PagingTutorialContent pagingTutorialContent = (PagingTutorialContent) tutorialContent;
                    Iterator<Integer> it = pagingTutorialContent.getPageResources().iterator();
                    while (it.hasNext()) {
                        this.mPageResources.add(Integer.valueOf(it.next().intValue()));
                    }
                    Iterator<Integer> it2 = pagingTutorialContent.getTitleResources().iterator();
                    while (it2.hasNext()) {
                        this.mTitleResourceId.add(Integer.valueOf(it2.next().intValue()));
                    }
                    Iterator<Integer> it3 = pagingTutorialContent.getDescriptionResources().iterator();
                    while (it3.hasNext()) {
                        this.mDescriptionResourceId.add(Integer.valueOf(it3.next().intValue()));
                    }
                    Iterator<MediaContentsResource> it4 = pagingTutorialContent.getMediaContentsResources().iterator();
                    while (it4.hasNext()) {
                        this.mMediaContentsResourceId.add(it4.next());
                    }
                }
            }
        }
    }

    /* renamed from: com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView$5, reason: invalid class name */
    static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$sonyericsson$android$camera$view$tutorial$PagingTutorialContentView$TutorialNavigatorType;

        static {
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialController.TutorialType.SUPER_SLOW_MOTION.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialController.TutorialType.SUPER_SLOW_MOTION_MORE_OPTIONS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$TutorialController$TutorialType[TutorialController.TutorialType.SUPER_SLOW_MOTION_SHOT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            $SwitchMap$com$sonyericsson$android$camera$view$tutorial$PagingTutorialContentView$TutorialNavigatorType = new int[TutorialNavigatorType.values().length];
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$PagingTutorialContentView$TutorialNavigatorType[TutorialNavigatorType.CONFIRM.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$sonyericsson$android$camera$view$tutorial$PagingTutorialContentView$TutorialNavigatorType[TutorialNavigatorType.NORMAL.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    protected static final class OneShotSlowTutorialContent extends PagingTutorialContent {
        protected OneShotSlowTutorialContent(int i) {
            super(i);
        }

        protected OneShotSlowTutorialContent(int i, Object... objArr) {
            super(i, objArr);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.PagingTutorialContent, com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected void setupResource() {
            super.setupResource();
            this.mPageResources.add(Integer.valueOf(isPortrait() ? R.layout.tutorial_page_layout_port : R.layout.tutorial_page_layout_land));
            this.mMediaContentsResourceId.add(new MediaContentsResource(isPortrait() ? R.drawable.cam_slow_motion_shot_wizard_1_port_icn : R.drawable.cam_slow_motion_shot_wizard_1_icn, MediaContentsResourceType.IMAGE));
            this.mTitleResourceId.add(Integer.valueOf(R.string.cam_strings_slow_motion_settings_shot_txt));
            this.mDescriptionResourceId.add(Integer.valueOf(R.string.cam_strings_slow_motion_shot_bright_place_tutorial_1_txt));
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.PagingTutorialContent
        @SuppressLint({"StringFormatInvalid"})
        protected ViewGroup getPageContentView(Context context, int i) {
            int iIntValue = ((Integer) this.mParams[0]).intValue();
            ViewGroup pageContentView = super.getPageContentView(context, i);
            if (i == 0) {
                TextView descriptionView = getDescriptionView(pageContentView);
                String string = context.getString(R.string.cam_strings_slow_motion_shot_tutorial_1_txt, Integer.toString(iIntValue));
                descriptionView.setText(string);
                descriptionView.setContentDescription(string);
            }
            return pageContentView;
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected boolean canShowContent(StoredSettings storedSettings) {
            UserSettings userSettings = storedSettings.getUserSettings();
            if (((CapturingMode) userSettings.get(UserSettingKey.CAPTURING_MODE)).isFront() || ((SlowMotion) userSettings.get(UserSettingKey.SLOW_MOTION)) != SlowMotion.SUPER_SLOW_SHOT) {
                return false;
            }
            return !storedSettings.getMessageSettings().isNeverShow(MessageType.TUTORIAL_SUPER_SLOW_MOTION_SHOT);
        }
    }

    protected static final class StandardSlowTutorialContent extends PagingTutorialContent {
        protected StandardSlowTutorialContent(int i) {
            super(i);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.PagingTutorialContent, com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected void setupResource() {
            super.setupResource();
            ArrayList<Integer> arrayList = this.mPageResources;
            boolean zIsPortrait = isPortrait();
            int i = R.layout.tutorial_page_layout_land;
            arrayList.add(Integer.valueOf(zIsPortrait ? 2131493024 : 2131493023));
            ArrayList<Integer> arrayList2 = this.mPageResources;
            if (isPortrait()) {
                i = 2131493024;
            }
            arrayList2.add(Integer.valueOf(i));
            this.mMediaContentsResourceId.add(new MediaContentsResource(isPortrait() ? R.drawable.cam_slow_motion_standard_wizard_1_port_icn : R.drawable.cam_slow_motion_standard_wizard_1_icn, MediaContentsResourceType.IMAGE));
            this.mMediaContentsResourceId.add(new MediaContentsResource(isPortrait() ? R.drawable.cam_slow_motion_standard_wizard_2_port_icn : R.drawable.cam_slow_motion_standard_wizard_2_icn, MediaContentsResourceType.IMAGE));
            this.mTitleResourceId.add(Integer.valueOf(R.string.cam_strings_slow_motion_settings_standard_txt));
            this.mTitleResourceId.add(Integer.valueOf(R.string.cam_strings_slow_motion_standard_tutorial_title_2_txt));
            this.mDescriptionResourceId.add(Integer.valueOf(R.string.cam_strings_slow_motion_standard_tutorial_1_txt));
            this.mDescriptionResourceId.add(Integer.valueOf(R.string.cam_strings_slow_motion_standard_tutorial_2_txt));
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected boolean canShowContent(StoredSettings storedSettings) {
            UserSettings userSettings = storedSettings.getUserSettings();
            if (((CapturingMode) userSettings.get(UserSettingKey.CAPTURING_MODE)).isFront() || ((SlowMotion) userSettings.get(UserSettingKey.SLOW_MOTION)) != SlowMotion.STANDARD_SLOW_MOTION) {
                return false;
            }
            return !storedSettings.getMessageSettings().isNeverShow(MessageType.TUTORIAL_STANDARD_SLOW_MOTION);
        }
    }

    protected static final class SuperSlowTutorialContent extends PagingTutorialContent {
        protected SuperSlowTutorialContent(int i) {
            super(i);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.PagingTutorialContent, com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected void setupResource() {
            super.setupResource();
            ArrayList<Integer> arrayList = this.mPageResources;
            boolean zIsPortrait = isPortrait();
            int i = R.layout.tutorial_page_layout_land;
            arrayList.add(Integer.valueOf(zIsPortrait ? 2131493024 : 2131493023));
            this.mPageResources.add(Integer.valueOf(isPortrait() ? 2131493024 : 2131493023));
            ArrayList<Integer> arrayList2 = this.mPageResources;
            if (isPortrait()) {
                i = 2131493024;
            }
            arrayList2.add(Integer.valueOf(i));
            this.mMediaContentsResourceId.add(new MediaContentsResource(isPortrait() ? R.drawable.cam_slow_motion_super_wizard_1_port_icn : R.drawable.cam_slow_motion_super_wizard_1_icn, MediaContentsResourceType.IMAGE));
            this.mMediaContentsResourceId.add(new MediaContentsResource(isPortrait() ? R.drawable.cam_slow_motion_super_wizard_2_port_icn : R.drawable.cam_slow_motion_super_wizard_2_icn, MediaContentsResourceType.IMAGE));
            this.mMediaContentsResourceId.add(new MediaContentsResource(isPortrait() ? R.drawable.cam_slow_motion_super_wizard_3_port_icn : R.drawable.cam_slow_motion_super_wizard_3_icn, MediaContentsResourceType.IMAGE));
            this.mTitleResourceId.add(Integer.valueOf(R.string.cam_strings_slow_motion_settings_super_txt));
            this.mTitleResourceId.add(Integer.valueOf(R.string.cam_strings_step_tutorial_title_txt));
            this.mTitleResourceId.add(Integer.valueOf(R.string.cam_strings_step_tutorial_title_txt));
            this.mDescriptionResourceId.add(Integer.valueOf(R.string.cam_strings_slow_motion_super_bright_place_tutorial_1_txt));
            this.mDescriptionResourceId.add(Integer.valueOf(R.string.cam_strings_slow_motion_super_tutorial_2_txt));
            this.mDescriptionResourceId.add(Integer.valueOf(R.string.cam_strings_slow_motion_super_tutorial_3_txt));
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.PagingTutorialContent
        protected ViewGroup getPageContentView(Context context, int i) {
            ViewGroup pageContentView = super.getPageContentView(context, i);
            if (i > 0) {
                TextView titleView = getTitleView(pageContentView);
                String stepText = getStepText(context, i, this.mPageResources.size() - 1);
                titleView.setText(stepText);
                titleView.setContentDescription(stepText);
            }
            return pageContentView;
        }
    }

    protected static final class SuperSlowMoreOptionsTutorialContent extends PagingTutorialContent {
        protected SuperSlowMoreOptionsTutorialContent(int i) {
            super(i);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.PagingTutorialContent, com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected void setupResource() {
            super.setupResource();
            ArrayList<Integer> arrayList = this.mPageResources;
            boolean zIsPortrait = isPortrait();
            int i = R.layout.tutorial_page_layout_land;
            arrayList.add(Integer.valueOf(zIsPortrait ? 2131493024 : 2131493023));
            this.mPageResources.add(Integer.valueOf(isPortrait() ? 2131493024 : 2131493023));
            this.mPageResources.add(Integer.valueOf(isPortrait() ? 2131493024 : 2131493023));
            ArrayList<Integer> arrayList2 = this.mPageResources;
            if (isPortrait()) {
                i = 2131493024;
            }
            arrayList2.add(Integer.valueOf(i));
            this.mMediaContentsResourceId.add(new MediaContentsResource(isPortrait() ? R.drawable.cam_slow_motion_super_wizard_1_port_icn : R.drawable.cam_slow_motion_super_wizard_1_icn, MediaContentsResourceType.IMAGE));
            this.mMediaContentsResourceId.add(new MediaContentsResource(isPortrait() ? R.drawable.cam_slow_motion_super_wizard_2_port_icn : R.drawable.cam_slow_motion_super_wizard_2_icn, MediaContentsResourceType.IMAGE));
            this.mMediaContentsResourceId.add(new MediaContentsResource(isPortrait() ? R.drawable.cam_slow_motion_super_wizard_3_port_icn : R.drawable.cam_slow_motion_super_wizard_3_icn, MediaContentsResourceType.IMAGE));
            this.mMediaContentsResourceId.add(new MediaContentsResource(isPortrait() ? R.drawable.cam_slow_motion_settings_wizard_port_icn : R.drawable.cam_slow_motion_settings_wizard_icn, MediaContentsResourceType.IMAGE));
            this.mTitleResourceId.add(Integer.valueOf(R.string.cam_strings_slow_motion_settings_super_txt));
            this.mTitleResourceId.add(Integer.valueOf(R.string.cam_strings_step_tutorial_title_txt));
            this.mTitleResourceId.add(Integer.valueOf(R.string.cam_strings_step_tutorial_title_txt));
            this.mTitleResourceId.add(Integer.valueOf(R.string.cam_strings_slow_motion_settings_tutorial_title_txt));
            this.mDescriptionResourceId.add(Integer.valueOf(R.string.cam_strings_slow_motion_super_bright_place_tutorial_1_txt));
            this.mDescriptionResourceId.add(Integer.valueOf(R.string.cam_strings_slow_motion_super_tutorial_2_txt));
            this.mDescriptionResourceId.add(Integer.valueOf(R.string.cam_strings_slow_motion_super_tutorial_3_txt));
            this.mDescriptionResourceId.add(Integer.valueOf(R.string.cam_strings_slow_motion_settings_tutorial_1_txt));
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.PagingTutorialContent
        protected ViewGroup getPageContentView(Context context, int i) {
            ViewGroup pageContentView = super.getPageContentView(context, i);
            if (i > 0 && i < 3) {
                TextView titleView = getTitleView(pageContentView);
                String stepText = getStepText(context, i, this.mPageResources.size() - 2);
                titleView.setText(stepText);
                titleView.setContentDescription(stepText);
            }
            return pageContentView;
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected boolean canShowContent(StoredSettings storedSettings) {
            UserSettings userSettings = storedSettings.getUserSettings();
            if (((CapturingMode) userSettings.get(UserSettingKey.CAPTURING_MODE)).isFront() || ((SlowMotion) userSettings.get(UserSettingKey.SLOW_MOTION)) != SlowMotion.SUPER_SLOW_MOTION) {
                return false;
            }
            return !storedSettings.getMessageSettings().isNeverShow(MessageType.TUTORIAL_SUPER_SLOW_MOTION);
        }
    }

    protected static final class DualCameraTutorialContent extends PagingTutorialContent {
        protected DualCameraTutorialContent(int i) {
            super(i);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.PagingTutorialContent, com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected void setupResource() {
            super.setupResource();
            this.mPageResources.add(Integer.valueOf(isPortrait() ? R.layout.tutorial_page_layout_port : R.layout.tutorial_page_layout_land));
            this.mMediaContentsResourceId.add(new MediaContentsResource(isPortrait() ? R.drawable.cam_superior_auto_fusion_wizard_port_icn : R.drawable.cam_superior_auto_fusion_wizard_icn, MediaContentsResourceType.IMAGE));
            this.mTitleResourceId.add(Integer.valueOf(R.string.cam_strings_superior_auto_fusion_tutorial_title_txt));
            this.mDescriptionResourceId.add(Integer.valueOf(R.string.cam_strings_superior_auto_fusion_tutorial_txt));
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected boolean canShowContent(StoredSettings storedSettings) {
            return PlatformCapability.isHighSensitivityFusionSupported(CameraInfo.CameraId.BACK);
        }
    }

    protected static final class EyeGuideTutorialContent extends PagingTutorialContent {
        protected EyeGuideTutorialContent(int i) {
            super(i);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.PagingTutorialContent, com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected void setupResource() {
            super.setupResource();
            this.mPageResources.add(Integer.valueOf(isPortrait() ? R.layout.tutorial_page_layout_port : R.layout.tutorial_page_layout_land));
            this.mMediaContentsResourceId.add(new MediaContentsResource(isPortrait() ? R.drawable.cam_eye_position_wizard_port_icn : R.drawable.cam_eye_position_wizard_icn, MediaContentsResourceType.IMAGE));
            this.mTitleResourceId.add(Integer.valueOf(R.string.cam_strings_eye_position_tutorial_title_txt));
            this.mDescriptionResourceId.add(Integer.valueOf(R.string.cam_strings_eye_position_tutorial_txt));
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected boolean canShowContent(StoredSettings storedSettings) {
            if (((CapturingMode) storedSettings.getUserSettings().get(UserSettingKey.CAPTURING_MODE)).isFront()) {
                return !storedSettings.getMessageSettings().isNeverShow(MessageType.TUTORIAL_EYE_GUIDE);
            }
            return false;
        }
    }

    protected static final class HandShutterTutorialContent extends PagingTutorialContent {
        protected HandShutterTutorialContent(int i) {
            super(i);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.PagingTutorialContent, com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected void setupResource() {
            super.setupResource();
            this.mPageResources.add(Integer.valueOf(isPortrait() ? R.layout.tutorial_page_layout_port : R.layout.tutorial_page_layout_land));
            this.mMediaContentsResourceId.add(new MediaContentsResource(isPortrait() ? R.drawable.cam_handshutter_wizard_port_icn : R.drawable.cam_handshutter_wizard_icn, MediaContentsResourceType.IMAGE));
            this.mTitleResourceId.add(Integer.valueOf(R.string.cam_strings_hand_shutter_txt));
            this.mDescriptionResourceId.add(Integer.valueOf(R.string.cam_strings_hand_shutter_hint_detail_txt));
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected boolean canShowContent(StoredSettings storedSettings) {
            UserSettings userSettings = storedSettings.getUserSettings();
            MessageSettings messageSettings = storedSettings.getMessageSettings();
            CapturingMode capturingMode = (CapturingMode) userSettings.get(UserSettingKey.CAPTURING_MODE);
            if (!capturingMode.isFront() || capturingMode.isVideo()) {
                return false;
            }
            return !messageSettings.isNeverShow(MessageType.TUTORIAL_HAND_SHUTTER);
        }
    }

    protected static final class ManualFusionTutorialContent extends PagingTutorialContent {
        protected ManualFusionTutorialContent(int i) {
            super(i);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.PagingTutorialContent, com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected void setupResource() {
            super.setupResource();
            ArrayList<Integer> arrayList = this.mPageResources;
            boolean zIsPortrait = isPortrait();
            int i = R.layout.tutorial_page_layout_land;
            arrayList.add(Integer.valueOf(zIsPortrait ? 2131493024 : 2131493023));
            ArrayList<Integer> arrayList2 = this.mPageResources;
            if (isPortrait()) {
                i = 2131493024;
            }
            arrayList2.add(Integer.valueOf(i));
            this.mMediaContentsResourceId.add(new MediaContentsResource(isPortrait() ? R.drawable.cam_manual_fusion_wizard_port_icn : R.drawable.cam_manual_fusion_wizard_icn, MediaContentsResourceType.IMAGE));
            this.mMediaContentsResourceId.add(new MediaContentsResource(isPortrait() ? R.drawable.cam_video_fusion_1_wizard_port_icn : R.drawable.cam_video_fusion_1_wizard_icn, MediaContentsResourceType.IMAGE));
            this.mTitleResourceId.add(Integer.valueOf(R.string.cam_strings_manual_fusion_tutorial_title_txt));
            this.mTitleResourceId.add(Integer.valueOf(R.string.cam_strings_manual_fusion_tutorial_2_title_txt));
            this.mDescriptionResourceId.add(Integer.valueOf(R.string.cam_strings_manual_fusion_tutorial_txt));
            this.mDescriptionResourceId.add(Integer.valueOf(R.string.cam_strings_manual_fusion_tutorial_2_txt));
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected boolean canShowContent(StoredSettings storedSettings) {
            return !storedSettings.getMessageSettings().isNeverShow(MessageType.TUTORIAL_MANUAL_FUSION);
        }
    }

    protected static final class VideoFusionTutorialContent extends PagingTutorialContent {
        protected VideoFusionTutorialContent(int i) {
            super(i);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.PagingTutorialContent, com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected void setupResource() {
            super.setupResource();
            this.mNavigatorType = TutorialNavigatorType.CONFIRM;
            ArrayList<Integer> arrayList = this.mPageResources;
            boolean zIsPortrait = isPortrait();
            int i = R.layout.tutorial_page_layout_land;
            arrayList.add(Integer.valueOf(zIsPortrait ? 2131493024 : 2131493023));
            ArrayList<Integer> arrayList2 = this.mPageResources;
            if (isPortrait()) {
                i = 2131493024;
            }
            arrayList2.add(Integer.valueOf(i));
            this.mMediaContentsResourceId.add(new MediaContentsResource(isPortrait() ? R.drawable.cam_video_fusion_1_wizard_port_icn : R.drawable.cam_video_fusion_1_wizard_icn, MediaContentsResourceType.IMAGE));
            this.mMediaContentsResourceId.add(new MediaContentsResource(isPortrait() ? R.drawable.cam_video_fusion_2_wizard_port_icn : R.drawable.cam_video_fusion_2_wizard_icn, MediaContentsResourceType.IMAGE));
            this.mTitleResourceId.add(Integer.valueOf(R.string.cam_strings_video_fusion_tutorial_1_title_txt));
            this.mTitleResourceId.add(Integer.valueOf(R.string.cam_strings_video_fusion_tutorial_2_title_txt));
            this.mDescriptionResourceId.add(Integer.valueOf(R.string.cam_strings_video_fusion_tutorial_1_txt));
            this.mDescriptionResourceId.add(Integer.valueOf(R.string.cam_strings_video_fusion_tutorial_2_txt));
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected boolean canShowContent(StoredSettings storedSettings) {
            CapturingMode capturingMode = (CapturingMode) storedSettings.getUserSettings().get(UserSettingKey.CAPTURING_MODE);
            return capturingMode.isVideo() && capturingMode != CapturingMode.SLOW_MOTION && !storedSettings.getMessageSettings().isNeverShow(MessageType.TUTORIAL_VIDEO_FUSION) && PlatformCapability.isHighSensitivityFusionSupported(capturingMode.getCameraId());
        }
    }

    protected static final class PredictiveLaunchTutorialContent extends PagingTutorialContent {
        protected PredictiveLaunchTutorialContent(int i) {
            super(i);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.PagingTutorialContent, com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected void setupResource() {
            super.setupResource();
            this.mPageResources.add(Integer.valueOf(isPortrait() ? R.layout.tutorial_page_layout_port : R.layout.tutorial_page_layout_land));
            this.mMediaContentsResourceId.add(new MediaContentsResource(R.raw.cam_predictive_launch_wizard, MediaContentsResourceType.VIDEO));
            this.mTitleResourceId.add(Integer.valueOf(R.string.cam_strings_predictive_launch_tutorial_title_txt));
            this.mDescriptionResourceId.add(Integer.valueOf(R.string.cam_strings_predictive_launch_tutorial_2_txt));
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected boolean canShowContent(StoredSettings storedSettings) {
            return PlatformCapability.isLiftTriggerSupported();
        }
    }

    protected static final class SideSenseTutorialContent extends PagingTutorialContent {
        public SideSenseTutorialContent(int i) {
            super(i);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.PagingTutorialContentView.PagingTutorialContent, com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected void setupResource() {
            super.setupResource();
            this.mPageResources.add(Integer.valueOf(isPortrait() ? R.layout.tutorial_page_layout_port : R.layout.tutorial_page_layout_land));
            this.mMediaContentsResourceId.add(new MediaContentsResource(R.raw.cam_side_touch_wizard, MediaContentsResourceType.VIDEO));
            this.mTitleResourceId.add(Integer.valueOf(R.string.cam_strings_side_touch_tutorial_title_txt));
            this.mDescriptionResourceId.add(Integer.valueOf(R.string.cam_strings_side_touch_tutorial_without_zoom_txt));
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected boolean canShowContent(StoredSettings storedSettings) {
            return PlatformCapability.isSideTouchSupported();
        }
    }
}
