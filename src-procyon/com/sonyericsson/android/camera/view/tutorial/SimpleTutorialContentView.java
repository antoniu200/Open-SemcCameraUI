// 
// Decompiled by Procyon v0.6.0
// 

package com.sonyericsson.android.camera.view.tutorial;

import java.util.ArrayList;
import java.util.List;
import android.text.method.ScrollingMovementMethod;
import android.view.View$OnClickListener;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

public class SimpleTutorialContentView extends TutorialContentView
{
    protected TextView mTutorialDescription;
    protected ImageView mTutorialIcon;
    protected TextView mTutorialTitle;
    
    public SimpleTutorialContentView(final Context context) {
        super(context);
    }
    
    public SimpleTutorialContentView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public SimpleTutorialContentView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    private SimpleTutorialContent getContent() {
        if (this.mContent != null && SimpleTutorialContent.class.isAssignableFrom(this.mContent.getClass())) {
            return (SimpleTutorialContent)this.mContent;
        }
        return null;
    }
    
    @Override
    protected void onLayoutToLandscape() {
        this.mTutorialTitle = (TextView)this.findViewById(2131296333);
        this.mTutorialDescription = (TextView)this.findViewById(2131296328);
        this.mTutorialIcon = (ImageView)this.findViewById(2131296330);
        super.onLayoutToLandscape();
    }
    
    @Override
    protected void onLayoutToPortrait() {
        this.mTutorialTitle = (TextView)this.findViewById(2131296334);
        this.mTutorialDescription = (TextView)this.findViewById(2131296329);
        this.mTutorialIcon = (ImageView)this.findViewById(2131296332);
        super.onLayoutToPortrait();
    }
    
    @Override
    protected void onUpdateViewContent() {
        final SimpleTutorialContent content = this.getContent();
        if (content == null) {
            return;
        }
        this.findViewById(2131296684).setOnClickListener((View$OnClickListener)this);
        this.findViewById(2131296676).setOnClickListener((View$OnClickListener)this);
        this.mTutorialTitle.setText(content.mTitleResourceId);
        this.mTutorialTitle.setContentDescription((CharSequence)this.getResources().getString(content.mTitleResourceId));
        this.mTutorialDescription.setText(content.mDescriptionResourceId);
        this.mTutorialDescription.setContentDescription((CharSequence)this.getResources().getString(content.mDescriptionResourceId));
        this.mTutorialDescription.setVerticalScrollBarEnabled(true);
        this.mTutorialDescription.setMovementMethod(ScrollingMovementMethod.getInstance());
        this.mTutorialIcon.setImageResource(content.mIconResourceId);
    }
    
    protected static final class SaveLocationTutorialContent extends ShortTutorialContent
    {
        protected SaveLocationTutorialContent(final int n) {
            super(n);
        }
        
        @Override
        protected TutorialPageInfo getCurrentTutorialPageInfo() {
            return this.getCurrentTutorialPageInfo(0);
        }
        
        @Override
        protected TutorialPageInfo getCurrentTutorialPageInfo(final int n) {
            return new TutorialPageInfo(TutorialController.TutorialType.SAVE_LOCATION, 0);
        }
        
        @Override
        protected List<TutorialController.TutorialType> getTutorialTypes() {
            final ArrayList list = new ArrayList();
            list.add(TutorialController.TutorialType.SAVE_LOCATION);
            return list;
        }
        
        @Override
        protected void setupResource() {
            super.setupResource();
            this.mTitleResourceId = 2131690028;
            this.mDescriptionResourceId = 2131690029;
            int mIconResourceId;
            if (((TutorialContent)this).isPortrait()) {
                mIconResourceId = 2131231266;
            }
            else {
                mIconResourceId = 2131231265;
            }
            this.mIconResourceId = mIconResourceId;
        }
    }
    
    private abstract static class ShortTutorialContent extends SimpleTutorialContent
    {
        public ShortTutorialContent(final int n) {
            super(n);
        }
        
        @Override
        protected void setupResource() {
            int mLayoutId;
            if (((TutorialContent)this).isPortrait()) {
                mLayoutId = 2131493014;
            }
            else {
                mLayoutId = 2131493013;
            }
            this.mLayoutId = mLayoutId;
        }
    }
    
    private abstract static class SimpleTutorialContent extends TutorialContent
    {
        protected int mDescriptionResourceId;
        protected int mIconResourceId;
        protected int mTitleResourceId;
        
        public SimpleTutorialContent(final int n) {
            super(n);
        }
        
        @Override
        protected int getPages() {
            return 1;
        }
        
        @Override
        protected TutorialContent getTutorialContent(final TutorialController.TutorialType tutorialType) {
            return this;
        }
        
        @Override
        protected boolean isSimpleTutorialContent() {
            return true;
        }
    }
}
