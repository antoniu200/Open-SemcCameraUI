package com.sonyericsson.android.camera.view.tutorial;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.sonyericsson.android.camera.R;
import com.sonyericsson.android.camera.view.tutorial.TutorialContentView;
import com.sonyericsson.android.camera.view.tutorial.TutorialController;
import java.util.ArrayList;
import java.util.List;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class SimpleTutorialContentView extends TutorialContentView {
    protected TextView mTutorialDescription;
    protected ImageView mTutorialIcon;
    protected TextView mTutorialTitle;

    public SimpleTutorialContentView(Context context) {
        super(context);
    }

    public SimpleTutorialContentView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public SimpleTutorialContentView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView
    protected void onLayoutToLandscape() {
        this.mTutorialTitle = (TextView) findViewById(R.id.cam_core_tutorial_title_land);
        this.mTutorialDescription = (TextView) findViewById(R.id.cam_core_tutorial_description_land);
        this.mTutorialIcon = (ImageView) findViewById(R.id.cam_core_tutorial_icn);
        super.onLayoutToLandscape();
    }

    @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView
    protected void onLayoutToPortrait() {
        this.mTutorialTitle = (TextView) findViewById(R.id.cam_core_tutorial_title_port);
        this.mTutorialDescription = (TextView) findViewById(R.id.cam_core_tutorial_description_port);
        this.mTutorialIcon = (ImageView) findViewById(R.id.cam_core_tutorial_port_icn);
        super.onLayoutToPortrait();
    }

    @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView
    protected void onUpdateViewContent() {
        SimpleTutorialContent content = getContent();
        if (content == null) {
            return;
        }
        findViewById(R.id.tutorial_yes_button).setOnClickListener(this);
        findViewById(R.id.tutorial_no_button).setOnClickListener(this);
        this.mTutorialTitle.setText(content.mTitleResourceId);
        this.mTutorialTitle.setContentDescription(getResources().getString(content.mTitleResourceId));
        this.mTutorialDescription.setText(content.mDescriptionResourceId);
        this.mTutorialDescription.setContentDescription(getResources().getString(content.mDescriptionResourceId));
        this.mTutorialDescription.setVerticalScrollBarEnabled(true);
        this.mTutorialDescription.setMovementMethod(ScrollingMovementMethod.getInstance());
        this.mTutorialIcon.setImageResource(content.mIconResourceId);
    }

    private SimpleTutorialContent getContent() {
        if (this.mContent == null || !SimpleTutorialContent.class.isAssignableFrom(this.mContent.getClass())) {
            return null;
        }
        return (SimpleTutorialContent) this.mContent;
    }

    private static abstract class SimpleTutorialContent extends TutorialContentView.TutorialContent {
        protected int mDescriptionResourceId;
        protected int mIconResourceId;
        protected int mTitleResourceId;

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected int getPages() {
            return 1;
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected TutorialContentView.TutorialContent getTutorialContent(TutorialController.TutorialType tutorialType) {
            return this;
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected boolean isSimpleTutorialContent() {
            return true;
        }

        public SimpleTutorialContent(int i) {
            super(i);
        }
    }

    private static abstract class ShortTutorialContent extends SimpleTutorialContent {
        public ShortTutorialContent(int i) {
            super(i);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected void setupResource() {
            this.mLayoutId = isPortrait() ? R.layout.simple_tutorial_layout_port : R.layout.simple_tutorial_layout_land;
        }
    }

    protected static final class SaveLocationTutorialContent extends ShortTutorialContent {
        protected SaveLocationTutorialContent(int i) {
            super(i);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.SimpleTutorialContentView.ShortTutorialContent, com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected void setupResource() {
            super.setupResource();
            this.mTitleResourceId = R.string.cam_strings_remember_geotag_title_txt;
            this.mDescriptionResourceId = R.string.cam_strings_remember_geotag_txt;
            this.mIconResourceId = isPortrait() ? R.drawable.cam_save_location_wizard_port_icn : R.drawable.cam_save_location_wizard_icn;
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected TutorialContentView.TutorialPageInfo getCurrentTutorialPageInfo() {
            return getCurrentTutorialPageInfo(0);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected TutorialContentView.TutorialPageInfo getCurrentTutorialPageInfo(int i) {
            return new TutorialContentView.TutorialPageInfo(TutorialController.TutorialType.SAVE_LOCATION, 0);
        }

        @Override // com.sonyericsson.android.camera.view.tutorial.TutorialContentView.TutorialContent
        protected List<TutorialController.TutorialType> getTutorialTypes() {
            ArrayList arrayList = new ArrayList();
            arrayList.add(TutorialController.TutorialType.SAVE_LOCATION);
            return arrayList;
        }
    }
}
