// 
// Decompiled by Procyon v0.6.0
// 

package android.arch.lifecycle;

public class MutableLiveData<T> extends LiveData<T>
{
    public void postValue(final T t) {
        super.postValue(t);
    }
    
    public void setValue(final T value) {
        super.setValue(value);
    }
}
