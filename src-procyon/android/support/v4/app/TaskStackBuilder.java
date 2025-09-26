// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.app;

import android.support.v4.content.ContextCompat;
import java.util.Iterator;
import android.os.Build$VERSION;
import android.os.Bundle;
import android.app.PendingIntent;
import android.support.annotation.Nullable;
import android.content.pm.PackageManager$NameNotFoundException;
import android.util.Log;
import android.app.Activity;
import android.content.ComponentName;
import android.support.annotation.NonNull;
import android.content.Context;
import java.util.ArrayList;
import android.content.Intent;

public final class TaskStackBuilder implements Iterable<Intent>
{
    private static final String TAG = "TaskStackBuilder";
    private final ArrayList<Intent> mIntents;
    private final Context mSourceContext;
    
    private TaskStackBuilder(final Context mSourceContext) {
        this.mIntents = new ArrayList<Intent>();
        this.mSourceContext = mSourceContext;
    }
    
    @NonNull
    public static TaskStackBuilder create(@NonNull final Context context) {
        return new TaskStackBuilder(context);
    }
    
    @Deprecated
    public static TaskStackBuilder from(final Context context) {
        return create(context);
    }
    
    @NonNull
    public TaskStackBuilder addNextIntent(@NonNull final Intent e) {
        this.mIntents.add(e);
        return this;
    }
    
    @NonNull
    public TaskStackBuilder addNextIntentWithParentStack(@NonNull final Intent intent) {
        ComponentName componentName;
        if ((componentName = intent.getComponent()) == null) {
            componentName = intent.resolveActivity(this.mSourceContext.getPackageManager());
        }
        if (componentName != null) {
            this.addParentStack(componentName);
        }
        this.addNextIntent(intent);
        return this;
    }
    
    @NonNull
    public TaskStackBuilder addParentStack(@NonNull final Activity activity) {
        Intent supportParentActivityIntent;
        if (activity instanceof SupportParentable) {
            supportParentActivityIntent = ((SupportParentable)activity).getSupportParentActivityIntent();
        }
        else {
            supportParentActivityIntent = null;
        }
        Intent parentActivityIntent = supportParentActivityIntent;
        if (supportParentActivityIntent == null) {
            parentActivityIntent = NavUtils.getParentActivityIntent(activity);
        }
        if (parentActivityIntent != null) {
            ComponentName componentName;
            if ((componentName = parentActivityIntent.getComponent()) == null) {
                componentName = parentActivityIntent.resolveActivity(this.mSourceContext.getPackageManager());
            }
            this.addParentStack(componentName);
            this.addNextIntent(parentActivityIntent);
        }
        return this;
    }
    
    public TaskStackBuilder addParentStack(final ComponentName componentName) {
        final int size = this.mIntents.size();
        try {
            for (Intent element = NavUtils.getParentActivityIntent(this.mSourceContext, componentName); element != null; element = NavUtils.getParentActivityIntent(this.mSourceContext, element.getComponent())) {
                this.mIntents.add(size, element);
            }
            return this;
        }
        catch (final PackageManager$NameNotFoundException cause) {
            Log.e("TaskStackBuilder", "Bad ComponentName while traversing activity parent metadata");
            throw new IllegalArgumentException((Throwable)cause);
        }
    }
    
    @NonNull
    public TaskStackBuilder addParentStack(@NonNull final Class<?> clazz) {
        return this.addParentStack(new ComponentName(this.mSourceContext, (Class)clazz));
    }
    
    @Nullable
    public Intent editIntentAt(final int index) {
        return this.mIntents.get(index);
    }
    
    @Deprecated
    public Intent getIntent(final int n) {
        return this.editIntentAt(n);
    }
    
    public int getIntentCount() {
        return this.mIntents.size();
    }
    
    @NonNull
    public Intent[] getIntents() {
        final Intent[] array = new Intent[this.mIntents.size()];
        if (array.length == 0) {
            return array;
        }
        array[0] = new Intent((Intent)this.mIntents.get(0)).addFlags(268484608);
        for (int i = 1; i < array.length; ++i) {
            array[i] = new Intent((Intent)this.mIntents.get(i));
        }
        return array;
    }
    
    @Nullable
    public PendingIntent getPendingIntent(final int n, final int n2) {
        return this.getPendingIntent(n, n2, null);
    }
    
    @Nullable
    public PendingIntent getPendingIntent(final int n, final int n2, @Nullable final Bundle bundle) {
        if (this.mIntents.isEmpty()) {
            throw new IllegalStateException("No intents added to TaskStackBuilder; cannot getPendingIntent");
        }
        final Intent[] array = this.mIntents.toArray(new Intent[this.mIntents.size()]);
        array[0] = new Intent(array[0]).addFlags(268484608);
        if (Build$VERSION.SDK_INT >= 16) {
            return PendingIntent.getActivities(this.mSourceContext, n, array, n2, bundle);
        }
        return PendingIntent.getActivities(this.mSourceContext, n, array, n2);
    }
    
    @Deprecated
    @Override
    public Iterator<Intent> iterator() {
        return this.mIntents.iterator();
    }
    
    public void startActivities() {
        this.startActivities(null);
    }
    
    public void startActivities(@Nullable final Bundle bundle) {
        if (this.mIntents.isEmpty()) {
            throw new IllegalStateException("No intents added to TaskStackBuilder; cannot startActivities");
        }
        final Intent[] array = this.mIntents.toArray(new Intent[this.mIntents.size()]);
        array[0] = new Intent(array[0]).addFlags(268484608);
        if (!ContextCompat.startActivities(this.mSourceContext, array, bundle)) {
            final Intent intent = new Intent(array[array.length - 1]);
            intent.addFlags(268435456);
            this.mSourceContext.startActivity(intent);
        }
    }
    
    public interface SupportParentable
    {
        @Nullable
        Intent getSupportParentActivityIntent();
    }
}
