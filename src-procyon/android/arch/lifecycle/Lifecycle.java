// 
// Decompiled by Procyon v0.6.0
// 

package android.arch.lifecycle;

import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

public abstract class Lifecycle
{
    @MainThread
    public abstract void addObserver(@NonNull final LifecycleObserver p0);
    
    @MainThread
    @NonNull
    public abstract State getCurrentState();
    
    @MainThread
    public abstract void removeObserver(@NonNull final LifecycleObserver p0);
    
    public enum Event
    {
        private static final Event[] $VALUES;
        
        ON_ANY, 
        ON_CREATE, 
        ON_DESTROY, 
        ON_PAUSE, 
        ON_RESUME, 
        ON_START, 
        ON_STOP;
        
        static {
            $VALUES = new Event[] { Event.ON_CREATE, Event.ON_START, Event.ON_RESUME, Event.ON_PAUSE, Event.ON_STOP, Event.ON_DESTROY, Event.ON_ANY };
        }
    }
    
    public enum State
    {
        private static final State[] $VALUES;
        
        CREATED, 
        DESTROYED, 
        INITIALIZED, 
        RESUMED, 
        STARTED;
        
        static {
            $VALUES = new State[] { State.DESTROYED, State.INITIALIZED, State.CREATED, State.STARTED, State.RESUMED };
        }
        
        public boolean isAtLeast(@NonNull final State o) {
            return this.compareTo(o) >= 0;
        }
    }
}
