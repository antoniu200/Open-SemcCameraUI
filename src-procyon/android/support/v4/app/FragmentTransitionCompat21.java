// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.app;

import android.transition.Transition$EpicenterCallback;
import android.graphics.Rect;
import android.transition.Transition$TransitionListener;
import java.util.List;
import java.util.Collection;
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.transition.TransitionSet;
import java.util.ArrayList;
import android.view.View;
import android.transition.Transition;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class FragmentTransitionCompat21 extends FragmentTransitionImpl
{
    private static boolean hasSimpleTarget(final Transition transition) {
        return !FragmentTransitionImpl.isNullOrEmpty(transition.getTargetIds()) || !FragmentTransitionImpl.isNullOrEmpty(transition.getTargetNames()) || !FragmentTransitionImpl.isNullOrEmpty(transition.getTargetTypes());
    }
    
    @Override
    public void addTarget(final Object o, final View view) {
        if (o != null) {
            ((Transition)o).addTarget(view);
        }
    }
    
    @Override
    public void addTargets(final Object o, final ArrayList<View> list) {
        final Transition transition = (Transition)o;
        if (transition == null) {
            return;
        }
        final boolean b = transition instanceof TransitionSet;
        final int n = 0;
        int i = 0;
        if (b) {
            for (TransitionSet set = (TransitionSet)transition; i < set.getTransitionCount(); ++i) {
                this.addTargets(set.getTransitionAt(i), list);
            }
        }
        else if (!hasSimpleTarget(transition) && FragmentTransitionImpl.isNullOrEmpty(transition.getTargets())) {
            for (int size = list.size(), j = n; j < size; ++j) {
                transition.addTarget((View)list.get(j));
            }
        }
    }
    
    @Override
    public void beginDelayedTransition(final ViewGroup viewGroup, final Object o) {
        TransitionManager.beginDelayedTransition(viewGroup, (Transition)o);
    }
    
    @Override
    public boolean canHandle(final Object o) {
        return o instanceof Transition;
    }
    
    @Override
    public Object cloneTransition(final Object o) {
        Transition clone;
        if (o != null) {
            clone = ((Transition)o).clone();
        }
        else {
            clone = null;
        }
        return clone;
    }
    
    @Override
    public Object mergeTransitionsInSequence(final Object o, Object o2, final Object o3) {
        Object setOrdering = o;
        final Transition transition = (Transition)o2;
        final Transition transition2 = (Transition)o3;
        if (setOrdering != null && transition != null) {
            setOrdering = new TransitionSet().addTransition((Transition)setOrdering).addTransition(transition).setOrdering(1);
        }
        else if (setOrdering == null) {
            if (transition != null) {
                setOrdering = transition;
            }
            else {
                setOrdering = null;
            }
        }
        if (transition2 != null) {
            o2 = new TransitionSet();
            if (setOrdering != null) {
                ((TransitionSet)o2).addTransition((Transition)setOrdering);
            }
            ((TransitionSet)o2).addTransition(transition2);
            return o2;
        }
        return setOrdering;
    }
    
    @Override
    public Object mergeTransitionsTogether(final Object o, final Object o2, final Object o3) {
        final TransitionSet set = new TransitionSet();
        if (o != null) {
            set.addTransition((Transition)o);
        }
        if (o2 != null) {
            set.addTransition((Transition)o2);
        }
        if (o3 != null) {
            set.addTransition((Transition)o3);
        }
        return set;
    }
    
    @Override
    public void removeTarget(final Object o, final View view) {
        if (o != null) {
            ((Transition)o).removeTarget(view);
        }
    }
    
    @Override
    public void replaceTargets(final Object o, final ArrayList<View> list, final ArrayList<View> list2) {
        final Transition transition = (Transition)o;
        final boolean b = transition instanceof TransitionSet;
        int i = 0;
        int j = 0;
        if (b) {
            for (TransitionSet set = (TransitionSet)transition; j < set.getTransitionCount(); ++j) {
                this.replaceTargets(set.getTransitionAt(j), list, list2);
            }
        }
        else if (!hasSimpleTarget(transition)) {
            final List targets = transition.getTargets();
            if (targets != null && targets.size() == list.size() && targets.containsAll(list)) {
                int size;
                if (list2 == null) {
                    size = 0;
                }
                else {
                    size = list2.size();
                }
                while (i < size) {
                    transition.addTarget((View)list2.get(i));
                    ++i;
                }
                for (int k = list.size() - 1; k >= 0; --k) {
                    transition.removeTarget((View)list.get(k));
                }
            }
        }
    }
    
    @Override
    public void scheduleHideFragmentView(final Object o, final View view, final ArrayList<View> list) {
        ((Transition)o).addListener((Transition$TransitionListener)new Transition$TransitionListener(this, view, list) {
            final FragmentTransitionCompat21 this$0;
            final ArrayList val$exitingViews;
            final View val$fragmentView;
            
            public void onTransitionCancel(final Transition transition) {
            }
            
            public void onTransitionEnd(final Transition transition) {
                transition.removeListener((Transition$TransitionListener)this);
                this.val$fragmentView.setVisibility(8);
                for (int size = this.val$exitingViews.size(), i = 0; i < size; ++i) {
                    ((View)this.val$exitingViews.get(i)).setVisibility(0);
                }
            }
            
            public void onTransitionPause(final Transition transition) {
            }
            
            public void onTransitionResume(final Transition transition) {
            }
            
            public void onTransitionStart(final Transition transition) {
            }
        });
    }
    
    @Override
    public void scheduleRemoveTargets(final Object o, final Object o2, final ArrayList<View> list, final Object o3, final ArrayList<View> list2, final Object o4, final ArrayList<View> list3) {
        ((Transition)o).addListener((Transition$TransitionListener)new Transition$TransitionListener(this, o2, list, o3, list2, o4, list3) {
            final FragmentTransitionCompat21 this$0;
            final Object val$enterTransition;
            final ArrayList val$enteringViews;
            final Object val$exitTransition;
            final ArrayList val$exitingViews;
            final Object val$sharedElementTransition;
            final ArrayList val$sharedElementsIn;
            
            public void onTransitionCancel(final Transition transition) {
            }
            
            public void onTransitionEnd(final Transition transition) {
            }
            
            public void onTransitionPause(final Transition transition) {
            }
            
            public void onTransitionResume(final Transition transition) {
            }
            
            public void onTransitionStart(final Transition transition) {
                if (this.val$enterTransition != null) {
                    this.this$0.replaceTargets(this.val$enterTransition, this.val$enteringViews, null);
                }
                if (this.val$exitTransition != null) {
                    this.this$0.replaceTargets(this.val$exitTransition, this.val$exitingViews, null);
                }
                if (this.val$sharedElementTransition != null) {
                    this.this$0.replaceTargets(this.val$sharedElementTransition, this.val$sharedElementsIn, null);
                }
            }
        });
    }
    
    @Override
    public void setEpicenter(final Object o, final Rect rect) {
        if (o != null) {
            ((Transition)o).setEpicenterCallback((Transition$EpicenterCallback)new Transition$EpicenterCallback(this, rect) {
                final FragmentTransitionCompat21 this$0;
                final Rect val$epicenter;
                
                public Rect onGetEpicenter(final Transition transition) {
                    if (this.val$epicenter != null && !this.val$epicenter.isEmpty()) {
                        return this.val$epicenter;
                    }
                    return null;
                }
            });
        }
    }
    
    @Override
    public void setEpicenter(final Object o, final View view) {
        if (view != null) {
            final Transition transition = (Transition)o;
            final Rect rect = new Rect();
            this.getBoundsOnScreen(view, rect);
            transition.setEpicenterCallback((Transition$EpicenterCallback)new Transition$EpicenterCallback(this, rect) {
                final FragmentTransitionCompat21 this$0;
                final Rect val$epicenter;
                
                public Rect onGetEpicenter(final Transition transition) {
                    return this.val$epicenter;
                }
            });
        }
    }
    
    @Override
    public void setSharedElementTargets(final Object o, final View e, final ArrayList<View> list) {
        final TransitionSet set = (TransitionSet)o;
        final List targets = set.getTargets();
        targets.clear();
        for (int size = list.size(), i = 0; i < size; ++i) {
            FragmentTransitionImpl.bfsAddViewChildren(targets, (View)list.get(i));
        }
        targets.add(e);
        list.add(e);
        this.addTargets(set, list);
    }
    
    @Override
    public void swapSharedElementTargets(final Object o, final ArrayList<View> list, final ArrayList<View> list2) {
        final TransitionSet set = (TransitionSet)o;
        if (set != null) {
            set.getTargets().clear();
            set.getTargets().addAll(list2);
            this.replaceTargets(set, list, list2);
        }
    }
    
    @Override
    public Object wrapTransitionInSet(final Object o) {
        if (o == null) {
            return null;
        }
        final TransitionSet set = new TransitionSet();
        set.addTransition((Transition)o);
        return set;
    }
}
