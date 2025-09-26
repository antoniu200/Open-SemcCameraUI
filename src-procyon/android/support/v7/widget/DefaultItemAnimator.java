// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v7.widget;

import android.support.v4.view.ViewCompat;
import java.util.Iterator;
import java.util.Collection;
import android.support.annotation.NonNull;
import android.animation.ValueAnimator;
import java.util.List;
import android.animation.Animator$AnimatorListener;
import android.animation.Animator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.animation.AnimatorListenerAdapter;
import java.util.ArrayList;
import android.animation.TimeInterpolator;

public class DefaultItemAnimator extends SimpleItemAnimator
{
    private static final boolean DEBUG = false;
    private static TimeInterpolator sDefaultInterpolator;
    ArrayList<ViewHolder> mAddAnimations;
    ArrayList<ArrayList<ViewHolder>> mAdditionsList;
    ArrayList<ViewHolder> mChangeAnimations;
    ArrayList<ArrayList<ChangeInfo>> mChangesList;
    ArrayList<ViewHolder> mMoveAnimations;
    ArrayList<ArrayList<MoveInfo>> mMovesList;
    private ArrayList<ViewHolder> mPendingAdditions;
    private ArrayList<ChangeInfo> mPendingChanges;
    private ArrayList<MoveInfo> mPendingMoves;
    private ArrayList<ViewHolder> mPendingRemovals;
    ArrayList<ViewHolder> mRemoveAnimations;
    
    public DefaultItemAnimator() {
        this.mPendingRemovals = new ArrayList<ViewHolder>();
        this.mPendingAdditions = new ArrayList<ViewHolder>();
        this.mPendingMoves = new ArrayList<MoveInfo>();
        this.mPendingChanges = new ArrayList<ChangeInfo>();
        this.mAdditionsList = new ArrayList<ArrayList<ViewHolder>>();
        this.mMovesList = new ArrayList<ArrayList<MoveInfo>>();
        this.mChangesList = new ArrayList<ArrayList<ChangeInfo>>();
        this.mAddAnimations = new ArrayList<ViewHolder>();
        this.mMoveAnimations = new ArrayList<ViewHolder>();
        this.mRemoveAnimations = new ArrayList<ViewHolder>();
        this.mChangeAnimations = new ArrayList<ViewHolder>();
    }
    
    private void animateRemoveImpl(final ViewHolder e) {
        final View itemView = e.itemView;
        final ViewPropertyAnimator animate = itemView.animate();
        this.mRemoveAnimations.add(e);
        animate.setDuration(((RecyclerView.ItemAnimator)this).getRemoveDuration()).alpha(0.0f).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter(this, e, animate, itemView) {
            final DefaultItemAnimator this$0;
            final ViewPropertyAnimator val$animation;
            final ViewHolder val$holder;
            final View val$view;
            
            public void onAnimationEnd(final Animator animator) {
                this.val$animation.setListener((Animator$AnimatorListener)null);
                this.val$view.setAlpha(1.0f);
                this.this$0.dispatchRemoveFinished(this.val$holder);
                this.this$0.mRemoveAnimations.remove(this.val$holder);
                this.this$0.dispatchFinishedWhenDone();
            }
            
            public void onAnimationStart(final Animator animator) {
                this.this$0.dispatchRemoveStarting(this.val$holder);
            }
        }).start();
    }
    
    private void endChangeAnimation(final List<ChangeInfo> list, final ViewHolder viewHolder) {
        for (int i = list.size() - 1; i >= 0; --i) {
            final ChangeInfo changeInfo = list.get(i);
            if (this.endChangeAnimationIfNecessary(changeInfo, viewHolder) && changeInfo.oldHolder == null && changeInfo.newHolder == null) {
                list.remove(changeInfo);
            }
        }
    }
    
    private void endChangeAnimationIfNecessary(final ChangeInfo changeInfo) {
        if (changeInfo.oldHolder != null) {
            this.endChangeAnimationIfNecessary(changeInfo, changeInfo.oldHolder);
        }
        if (changeInfo.newHolder != null) {
            this.endChangeAnimationIfNecessary(changeInfo, changeInfo.newHolder);
        }
    }
    
    private boolean endChangeAnimationIfNecessary(final ChangeInfo changeInfo, final ViewHolder viewHolder) {
        final ViewHolder newHolder = changeInfo.newHolder;
        boolean b = false;
        if (newHolder == viewHolder) {
            changeInfo.newHolder = null;
        }
        else {
            if (changeInfo.oldHolder != viewHolder) {
                return false;
            }
            changeInfo.oldHolder = null;
            b = true;
        }
        viewHolder.itemView.setAlpha(1.0f);
        viewHolder.itemView.setTranslationX(0.0f);
        viewHolder.itemView.setTranslationY(0.0f);
        this.dispatchChangeFinished(viewHolder, b);
        return true;
    }
    
    private void resetAnimation(final ViewHolder viewHolder) {
        if (DefaultItemAnimator.sDefaultInterpolator == null) {
            DefaultItemAnimator.sDefaultInterpolator = new ValueAnimator().getInterpolator();
        }
        viewHolder.itemView.animate().setInterpolator(DefaultItemAnimator.sDefaultInterpolator);
        this.endAnimation(viewHolder);
    }
    
    @Override
    public boolean animateAdd(final ViewHolder e) {
        this.resetAnimation(e);
        e.itemView.setAlpha(0.0f);
        this.mPendingAdditions.add(e);
        return true;
    }
    
    void animateAddImpl(final ViewHolder e) {
        final View itemView = e.itemView;
        final ViewPropertyAnimator animate = itemView.animate();
        this.mAddAnimations.add(e);
        animate.alpha(1.0f).setDuration(((RecyclerView.ItemAnimator)this).getAddDuration()).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter(this, e, itemView, animate) {
            final DefaultItemAnimator this$0;
            final ViewPropertyAnimator val$animation;
            final ViewHolder val$holder;
            final View val$view;
            
            public void onAnimationCancel(final Animator animator) {
                this.val$view.setAlpha(1.0f);
            }
            
            public void onAnimationEnd(final Animator animator) {
                this.val$animation.setListener((Animator$AnimatorListener)null);
                this.this$0.dispatchAddFinished(this.val$holder);
                this.this$0.mAddAnimations.remove(this.val$holder);
                this.this$0.dispatchFinishedWhenDone();
            }
            
            public void onAnimationStart(final Animator animator) {
                this.this$0.dispatchAddStarting(this.val$holder);
            }
        }).start();
    }
    
    @Override
    public boolean animateChange(final ViewHolder viewHolder, final ViewHolder viewHolder2, final int n, final int n2, final int n3, final int n4) {
        if (viewHolder == viewHolder2) {
            return this.animateMove(viewHolder, n, n2, n3, n4);
        }
        final float translationX = viewHolder.itemView.getTranslationX();
        final float translationY = viewHolder.itemView.getTranslationY();
        final float alpha = viewHolder.itemView.getAlpha();
        this.resetAnimation(viewHolder);
        final int n5 = (int)(n3 - n - translationX);
        final int n6 = (int)(n4 - n2 - translationY);
        viewHolder.itemView.setTranslationX(translationX);
        viewHolder.itemView.setTranslationY(translationY);
        viewHolder.itemView.setAlpha(alpha);
        if (viewHolder2 != null) {
            this.resetAnimation(viewHolder2);
            viewHolder2.itemView.setTranslationX((float)(-n5));
            viewHolder2.itemView.setTranslationY((float)(-n6));
            viewHolder2.itemView.setAlpha(0.0f);
        }
        this.mPendingChanges.add(new ChangeInfo(viewHolder, viewHolder2, n, n2, n3, n4));
        return true;
    }
    
    void animateChangeImpl(final ChangeInfo changeInfo) {
        final ViewHolder oldHolder = changeInfo.oldHolder;
        View itemView = null;
        View itemView2;
        if (oldHolder == null) {
            itemView2 = null;
        }
        else {
            itemView2 = oldHolder.itemView;
        }
        final ViewHolder newHolder = changeInfo.newHolder;
        if (newHolder != null) {
            itemView = newHolder.itemView;
        }
        if (itemView2 != null) {
            final ViewPropertyAnimator setDuration = itemView2.animate().setDuration(((RecyclerView.ItemAnimator)this).getChangeDuration());
            this.mChangeAnimations.add(changeInfo.oldHolder);
            setDuration.translationX((float)(changeInfo.toX - changeInfo.fromX));
            setDuration.translationY((float)(changeInfo.toY - changeInfo.fromY));
            setDuration.alpha(0.0f).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter(this, changeInfo, setDuration, itemView2) {
                final DefaultItemAnimator this$0;
                final ChangeInfo val$changeInfo;
                final ViewPropertyAnimator val$oldViewAnim;
                final View val$view;
                
                public void onAnimationEnd(final Animator animator) {
                    this.val$oldViewAnim.setListener((Animator$AnimatorListener)null);
                    this.val$view.setAlpha(1.0f);
                    this.val$view.setTranslationX(0.0f);
                    this.val$view.setTranslationY(0.0f);
                    this.this$0.dispatchChangeFinished(this.val$changeInfo.oldHolder, true);
                    this.this$0.mChangeAnimations.remove(this.val$changeInfo.oldHolder);
                    this.this$0.dispatchFinishedWhenDone();
                }
                
                public void onAnimationStart(final Animator animator) {
                    this.this$0.dispatchChangeStarting(this.val$changeInfo.oldHolder, true);
                }
            }).start();
        }
        if (itemView != null) {
            final ViewPropertyAnimator animate = itemView.animate();
            this.mChangeAnimations.add(changeInfo.newHolder);
            animate.translationX(0.0f).translationY(0.0f).setDuration(((RecyclerView.ItemAnimator)this).getChangeDuration()).alpha(1.0f).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter(this, changeInfo, animate, itemView) {
                final DefaultItemAnimator this$0;
                final ChangeInfo val$changeInfo;
                final View val$newView;
                final ViewPropertyAnimator val$newViewAnimation;
                
                public void onAnimationEnd(final Animator animator) {
                    this.val$newViewAnimation.setListener((Animator$AnimatorListener)null);
                    this.val$newView.setAlpha(1.0f);
                    this.val$newView.setTranslationX(0.0f);
                    this.val$newView.setTranslationY(0.0f);
                    this.this$0.dispatchChangeFinished(this.val$changeInfo.newHolder, false);
                    this.this$0.mChangeAnimations.remove(this.val$changeInfo.newHolder);
                    this.this$0.dispatchFinishedWhenDone();
                }
                
                public void onAnimationStart(final Animator animator) {
                    this.this$0.dispatchChangeStarting(this.val$changeInfo.newHolder, false);
                }
            }).start();
        }
    }
    
    @Override
    public boolean animateMove(final ViewHolder viewHolder, int n, int n2, final int n3, final int n4) {
        final View itemView = viewHolder.itemView;
        n += (int)viewHolder.itemView.getTranslationX();
        final int n5 = n2 + (int)viewHolder.itemView.getTranslationY();
        this.resetAnimation(viewHolder);
        final int n6 = n3 - n;
        n2 = n4 - n5;
        if (n6 == 0 && n2 == 0) {
            this.dispatchMoveFinished(viewHolder);
            return false;
        }
        if (n6 != 0) {
            itemView.setTranslationX((float)(-n6));
        }
        if (n2 != 0) {
            itemView.setTranslationY((float)(-n2));
        }
        this.mPendingMoves.add(new MoveInfo(viewHolder, n, n5, n3, n4));
        return true;
    }
    
    void animateMoveImpl(final ViewHolder e, int n, int n2, final int n3, final int n4) {
        final View itemView = e.itemView;
        n = n3 - n;
        n2 = n4 - n2;
        if (n != 0) {
            itemView.animate().translationX(0.0f);
        }
        if (n2 != 0) {
            itemView.animate().translationY(0.0f);
        }
        final ViewPropertyAnimator animate = itemView.animate();
        this.mMoveAnimations.add(e);
        animate.setDuration(((RecyclerView.ItemAnimator)this).getMoveDuration()).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter(this, e, n, itemView, n2, animate) {
            final DefaultItemAnimator this$0;
            final ViewPropertyAnimator val$animation;
            final int val$deltaX;
            final int val$deltaY;
            final ViewHolder val$holder;
            final View val$view;
            
            public void onAnimationCancel(final Animator animator) {
                if (this.val$deltaX != 0) {
                    this.val$view.setTranslationX(0.0f);
                }
                if (this.val$deltaY != 0) {
                    this.val$view.setTranslationY(0.0f);
                }
            }
            
            public void onAnimationEnd(final Animator animator) {
                this.val$animation.setListener((Animator$AnimatorListener)null);
                this.this$0.dispatchMoveFinished(this.val$holder);
                this.this$0.mMoveAnimations.remove(this.val$holder);
                this.this$0.dispatchFinishedWhenDone();
            }
            
            public void onAnimationStart(final Animator animator) {
                this.this$0.dispatchMoveStarting(this.val$holder);
            }
        }).start();
    }
    
    @Override
    public boolean animateRemove(final ViewHolder e) {
        this.resetAnimation(e);
        this.mPendingRemovals.add(e);
        return true;
    }
    
    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull final ViewHolder viewHolder, @NonNull final List<Object> list) {
        return !list.isEmpty() || super.canReuseUpdatedViewHolder(viewHolder, list);
    }
    
    void cancelAll(final List<ViewHolder> list) {
        for (int i = list.size() - 1; i >= 0; --i) {
            ((ViewHolder)list.get(i)).itemView.animate().cancel();
        }
    }
    
    void dispatchFinishedWhenDone() {
        if (!this.isRunning()) {
            ((RecyclerView.ItemAnimator)this).dispatchAnimationsFinished();
        }
    }
    
    @Override
    public void endAnimation(final ViewHolder o) {
        final View itemView = o.itemView;
        itemView.animate().cancel();
        for (int i = this.mPendingMoves.size() - 1; i >= 0; --i) {
            if (this.mPendingMoves.get(i).holder == o) {
                itemView.setTranslationY(0.0f);
                itemView.setTranslationX(0.0f);
                this.dispatchMoveFinished(o);
                this.mPendingMoves.remove(i);
            }
        }
        this.endChangeAnimation(this.mPendingChanges, o);
        if (this.mPendingRemovals.remove(o)) {
            itemView.setAlpha(1.0f);
            this.dispatchRemoveFinished(o);
        }
        if (this.mPendingAdditions.remove(o)) {
            itemView.setAlpha(1.0f);
            this.dispatchAddFinished(o);
        }
        for (int j = this.mChangesList.size() - 1; j >= 0; --j) {
            final ArrayList list = this.mChangesList.get(j);
            this.endChangeAnimation(list, o);
            if (list.isEmpty()) {
                this.mChangesList.remove(j);
            }
        }
        for (int k = this.mMovesList.size() - 1; k >= 0; --k) {
            final ArrayList list2 = this.mMovesList.get(k);
            int l = list2.size() - 1;
            while (l >= 0) {
                if (((MoveInfo)list2.get(l)).holder == o) {
                    itemView.setTranslationY(0.0f);
                    itemView.setTranslationX(0.0f);
                    this.dispatchMoveFinished(o);
                    list2.remove(l);
                    if (list2.isEmpty()) {
                        this.mMovesList.remove(k);
                        break;
                    }
                    break;
                }
                else {
                    --l;
                }
            }
        }
        for (int n = this.mAdditionsList.size() - 1; n >= 0; --n) {
            final ArrayList list3 = this.mAdditionsList.get(n);
            if (list3.remove(o)) {
                itemView.setAlpha(1.0f);
                this.dispatchAddFinished(o);
                if (list3.isEmpty()) {
                    this.mAdditionsList.remove(n);
                }
            }
        }
        this.mRemoveAnimations.remove(o);
        this.mAddAnimations.remove(o);
        this.mChangeAnimations.remove(o);
        this.mMoveAnimations.remove(o);
        this.dispatchFinishedWhenDone();
    }
    
    @Override
    public void endAnimations() {
        for (int i = this.mPendingMoves.size() - 1; i >= 0; --i) {
            final MoveInfo moveInfo = this.mPendingMoves.get(i);
            final View itemView = moveInfo.holder.itemView;
            itemView.setTranslationY(0.0f);
            itemView.setTranslationX(0.0f);
            this.dispatchMoveFinished(moveInfo.holder);
            this.mPendingMoves.remove(i);
        }
        for (int j = this.mPendingRemovals.size() - 1; j >= 0; --j) {
            this.dispatchRemoveFinished(this.mPendingRemovals.get(j));
            this.mPendingRemovals.remove(j);
        }
        for (int k = this.mPendingAdditions.size() - 1; k >= 0; --k) {
            final ViewHolder viewHolder = this.mPendingAdditions.get(k);
            viewHolder.itemView.setAlpha(1.0f);
            this.dispatchAddFinished(viewHolder);
            this.mPendingAdditions.remove(k);
        }
        for (int l = this.mPendingChanges.size() - 1; l >= 0; --l) {
            this.endChangeAnimationIfNecessary(this.mPendingChanges.get(l));
        }
        this.mPendingChanges.clear();
        if (!this.isRunning()) {
            return;
        }
        for (int index = this.mMovesList.size() - 1; index >= 0; --index) {
            final ArrayList o = this.mMovesList.get(index);
            for (int n = o.size() - 1; n >= 0; --n) {
                final MoveInfo moveInfo2 = (MoveInfo)o.get(n);
                final View itemView2 = moveInfo2.holder.itemView;
                itemView2.setTranslationY(0.0f);
                itemView2.setTranslationX(0.0f);
                this.dispatchMoveFinished(moveInfo2.holder);
                o.remove(n);
                if (o.isEmpty()) {
                    this.mMovesList.remove(o);
                }
            }
        }
        for (int index2 = this.mAdditionsList.size() - 1; index2 >= 0; --index2) {
            final ArrayList o2 = this.mAdditionsList.get(index2);
            for (int n2 = o2.size() - 1; n2 >= 0; --n2) {
                final ViewHolder viewHolder2 = (ViewHolder)o2.get(n2);
                viewHolder2.itemView.setAlpha(1.0f);
                this.dispatchAddFinished(viewHolder2);
                o2.remove(n2);
                if (o2.isEmpty()) {
                    this.mAdditionsList.remove(o2);
                }
            }
        }
        for (int index3 = this.mChangesList.size() - 1; index3 >= 0; --index3) {
            final ArrayList o3 = this.mChangesList.get(index3);
            for (int index4 = o3.size() - 1; index4 >= 0; --index4) {
                this.endChangeAnimationIfNecessary((ChangeInfo)o3.get(index4));
                if (o3.isEmpty()) {
                    this.mChangesList.remove(o3);
                }
            }
        }
        this.cancelAll(this.mRemoveAnimations);
        this.cancelAll(this.mMoveAnimations);
        this.cancelAll(this.mAddAnimations);
        this.cancelAll(this.mChangeAnimations);
        ((RecyclerView.ItemAnimator)this).dispatchAnimationsFinished();
    }
    
    @Override
    public boolean isRunning() {
        return !this.mPendingAdditions.isEmpty() || !this.mPendingChanges.isEmpty() || !this.mPendingMoves.isEmpty() || !this.mPendingRemovals.isEmpty() || !this.mMoveAnimations.isEmpty() || !this.mRemoveAnimations.isEmpty() || !this.mAddAnimations.isEmpty() || !this.mChangeAnimations.isEmpty() || !this.mMovesList.isEmpty() || !this.mAdditionsList.isEmpty() || !this.mChangesList.isEmpty();
    }
    
    @Override
    public void runPendingAnimations() {
        final boolean b = this.mPendingRemovals.isEmpty() ^ true;
        final boolean b2 = this.mPendingMoves.isEmpty() ^ true;
        final boolean b3 = this.mPendingChanges.isEmpty() ^ true;
        final boolean b4 = this.mPendingAdditions.isEmpty() ^ true;
        if (!b && !b2 && !b4 && !b3) {
            return;
        }
        final Iterator<ViewHolder> iterator = this.mPendingRemovals.iterator();
        while (iterator.hasNext()) {
            this.animateRemoveImpl(iterator.next());
        }
        this.mPendingRemovals.clear();
        if (b2) {
            final ArrayList e = new ArrayList();
            e.addAll(this.mPendingMoves);
            this.mMovesList.add(e);
            this.mPendingMoves.clear();
            final Runnable runnable = new Runnable(this, e) {
                final DefaultItemAnimator this$0;
                final ArrayList val$moves;
                
                @Override
                public void run() {
                    for (final MoveInfo moveInfo : this.val$moves) {
                        this.this$0.animateMoveImpl(moveInfo.holder, moveInfo.fromX, moveInfo.fromY, moveInfo.toX, moveInfo.toY);
                    }
                    this.val$moves.clear();
                    this.this$0.mMovesList.remove(this.val$moves);
                }
            };
            if (b) {
                ViewCompat.postOnAnimationDelayed(e.get(0).holder.itemView, runnable, ((RecyclerView.ItemAnimator)this).getRemoveDuration());
            }
            else {
                runnable.run();
            }
        }
        if (b3) {
            final ArrayList e2 = new ArrayList();
            e2.addAll(this.mPendingChanges);
            this.mChangesList.add(e2);
            this.mPendingChanges.clear();
            final Runnable runnable2 = new Runnable(this, e2) {
                final DefaultItemAnimator this$0;
                final ArrayList val$changes;
                
                @Override
                public void run() {
                    final Iterator iterator = this.val$changes.iterator();
                    while (iterator.hasNext()) {
                        this.this$0.animateChangeImpl((ChangeInfo)iterator.next());
                    }
                    this.val$changes.clear();
                    this.this$0.mChangesList.remove(this.val$changes);
                }
            };
            if (b) {
                ViewCompat.postOnAnimationDelayed(((ChangeInfo)e2.get(0)).oldHolder.itemView, runnable2, ((RecyclerView.ItemAnimator)this).getRemoveDuration());
            }
            else {
                runnable2.run();
            }
        }
        if (b4) {
            final ArrayList e3 = new ArrayList();
            e3.addAll(this.mPendingAdditions);
            this.mAdditionsList.add(e3);
            this.mPendingAdditions.clear();
            final Runnable runnable3 = new Runnable(this, e3) {
                final DefaultItemAnimator this$0;
                final ArrayList val$additions;
                
                @Override
                public void run() {
                    final Iterator iterator = this.val$additions.iterator();
                    while (iterator.hasNext()) {
                        this.this$0.animateAddImpl((ViewHolder)iterator.next());
                    }
                    this.val$additions.clear();
                    this.this$0.mAdditionsList.remove(this.val$additions);
                }
            };
            if (!b && !b2 && !b3) {
                runnable3.run();
            }
            else {
                long changeDuration = 0L;
                long removeDuration;
                if (b) {
                    removeDuration = ((RecyclerView.ItemAnimator)this).getRemoveDuration();
                }
                else {
                    removeDuration = 0L;
                }
                long moveDuration;
                if (b2) {
                    moveDuration = ((RecyclerView.ItemAnimator)this).getMoveDuration();
                }
                else {
                    moveDuration = 0L;
                }
                if (b3) {
                    changeDuration = ((RecyclerView.ItemAnimator)this).getChangeDuration();
                }
                ViewCompat.postOnAnimationDelayed(((ViewHolder)e3.get(0)).itemView, runnable3, removeDuration + Math.max(moveDuration, changeDuration));
            }
        }
    }
    
    private static class ChangeInfo
    {
        public int fromX;
        public int fromY;
        public ViewHolder newHolder;
        public ViewHolder oldHolder;
        public int toX;
        public int toY;
        
        private ChangeInfo(final ViewHolder oldHolder, final ViewHolder newHolder) {
            this.oldHolder = oldHolder;
            this.newHolder = newHolder;
        }
        
        ChangeInfo(final ViewHolder viewHolder, final ViewHolder viewHolder2, final int fromX, final int fromY, final int toX, final int toY) {
            this(viewHolder, viewHolder2);
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("ChangeInfo{oldHolder=");
            sb.append(this.oldHolder);
            sb.append(", newHolder=");
            sb.append(this.newHolder);
            sb.append(", fromX=");
            sb.append(this.fromX);
            sb.append(", fromY=");
            sb.append(this.fromY);
            sb.append(", toX=");
            sb.append(this.toX);
            sb.append(", toY=");
            sb.append(this.toY);
            sb.append('}');
            return sb.toString();
        }
    }
    
    private static class MoveInfo
    {
        public int fromX;
        public int fromY;
        public ViewHolder holder;
        public int toX;
        public int toY;
        
        MoveInfo(final ViewHolder holder, final int fromX, final int fromY, final int toX, final int toY) {
            this.holder = holder;
            this.fromX = fromX;
            this.fromY = fromY;
            this.toX = toX;
            this.toY = toY;
        }
    }
}
