// 
// Decompiled by Procyon v0.6.0
// 

package android.arch.lifecycle;

import android.support.annotation.NonNull;
import android.annotation.SuppressLint;
import android.app.Application;

public class AndroidViewModel extends ViewModel
{
    @SuppressLint({ "StaticFieldLeak" })
    private Application mApplication;
    
    public AndroidViewModel(@NonNull final Application mApplication) {
        this.mApplication = mApplication;
    }
    
    @NonNull
    public <T extends Application> T getApplication() {
        return (T)this.mApplication;
    }
}
