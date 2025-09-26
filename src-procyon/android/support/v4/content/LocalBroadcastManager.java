// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.content;

import java.util.Set;
import android.net.Uri;
import android.util.Log;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.os.Message;
import android.os.Looper;
import android.content.BroadcastReceiver;
import android.os.Handler;
import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;

public final class LocalBroadcastManager
{
    private static final boolean DEBUG = false;
    static final int MSG_EXEC_PENDING_BROADCASTS = 1;
    private static final String TAG = "LocalBroadcastManager";
    private static LocalBroadcastManager mInstance;
    private static final Object mLock;
    private final HashMap<String, ArrayList<ReceiverRecord>> mActions;
    private final Context mAppContext;
    private final Handler mHandler;
    private final ArrayList<BroadcastRecord> mPendingBroadcasts;
    private final HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>> mReceivers;
    
    static {
        mLock = new Object();
    }
    
    private LocalBroadcastManager(final Context mAppContext) {
        this.mReceivers = new HashMap<BroadcastReceiver, ArrayList<ReceiverRecord>>();
        this.mActions = new HashMap<String, ArrayList<ReceiverRecord>>();
        this.mPendingBroadcasts = new ArrayList<BroadcastRecord>();
        this.mAppContext = mAppContext;
        this.mHandler = new Handler(this, mAppContext.getMainLooper()) {
            final LocalBroadcastManager this$0;
            
            public void handleMessage(final Message message) {
                if (message.what != 1) {
                    super.handleMessage(message);
                }
                else {
                    this.this$0.executePendingBroadcasts();
                }
            }
        };
    }
    
    @NonNull
    public static LocalBroadcastManager getInstance(@NonNull final Context context) {
        synchronized (LocalBroadcastManager.mLock) {
            if (LocalBroadcastManager.mInstance == null) {
                LocalBroadcastManager.mInstance = new LocalBroadcastManager(context.getApplicationContext());
            }
            return LocalBroadcastManager.mInstance;
        }
    }
    
    void executePendingBroadcasts() {
        while (true) {
            Object mReceivers = this.mReceivers;
            synchronized (mReceivers) {
                final int size = this.mPendingBroadcasts.size();
                if (size <= 0) {
                    return;
                }
                final BroadcastRecord[] a = new BroadcastRecord[size];
                this.mPendingBroadcasts.toArray(a);
                this.mPendingBroadcasts.clear();
                monitorexit(mReceivers);
                for (int i = 0; i < a.length; ++i) {
                    final BroadcastRecord broadcastRecord = a[i];
                    for (int size2 = broadcastRecord.receivers.size(), j = 0; j < size2; ++j) {
                        mReceivers = broadcastRecord.receivers.get(j);
                        if (!((ReceiverRecord)mReceivers).dead) {
                            ((ReceiverRecord)mReceivers).receiver.onReceive(this.mAppContext, broadcastRecord.intent);
                        }
                    }
                }
            }
        }
    }
    
    public void registerReceiver(@NonNull final BroadcastReceiver broadcastReceiver, @NonNull final IntentFilter intentFilter) {
        synchronized (this.mReceivers) {
            final ReceiverRecord receiverRecord = new ReceiverRecord(intentFilter, broadcastReceiver);
            ArrayList value;
            if ((value = this.mReceivers.get(broadcastReceiver)) == null) {
                value = new ArrayList(1);
                this.mReceivers.put(broadcastReceiver, value);
            }
            value.add(receiverRecord);
            for (int i = 0; i < intentFilter.countActions(); ++i) {
                final String action = intentFilter.getAction(i);
                ArrayList value2;
                if ((value2 = this.mActions.get(action)) == null) {
                    value2 = new ArrayList(1);
                    this.mActions.put(action, value2);
                }
                value2.add(receiverRecord);
            }
        }
    }
    
    public boolean sendBroadcast(@NonNull final Intent obj) {
        synchronized (this.mReceivers) {
            final String action = obj.getAction();
            final String resolveTypeIfNeeded = obj.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
            final Uri data = obj.getData();
            final String scheme = obj.getScheme();
            final Set categories = obj.getCategories();
            final boolean b = (obj.getFlags() & 0x8) != 0x0;
            if (b) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Resolving type ");
                sb.append(resolveTypeIfNeeded);
                sb.append(" scheme ");
                sb.append(scheme);
                sb.append(" of intent ");
                sb.append(obj);
                Log.v("LocalBroadcastManager", sb.toString());
            }
            final ArrayList obj2 = this.mActions.get(obj.getAction());
            if (obj2 != null) {
                if (b) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Action list: ");
                    sb2.append(obj2);
                    Log.v("LocalBroadcastManager", sb2.toString());
                }
                ArrayList<ReceiverRecord> list = null;
                for (int i = 0; i < obj2.size(); ++i) {
                    final ReceiverRecord e = (ReceiverRecord)obj2.get(i);
                    if (b) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Matching against filter ");
                        sb3.append(e.filter);
                        Log.v("LocalBroadcastManager", sb3.toString());
                    }
                    if (e.broadcasting) {
                        if (b) {
                            Log.v("LocalBroadcastManager", "  Filter's target already added");
                        }
                    }
                    else {
                        final int match = e.filter.match(action, resolveTypeIfNeeded, scheme, data, categories, "LocalBroadcastManager");
                        if (match >= 0) {
                            if (b) {
                                final StringBuilder sb4 = new StringBuilder();
                                sb4.append("  Filter matched!  match=0x");
                                sb4.append(Integer.toHexString(match));
                                Log.v("LocalBroadcastManager", sb4.toString());
                            }
                            if (list == null) {
                                list = new ArrayList<ReceiverRecord>();
                            }
                            list.add(e);
                            e.broadcasting = true;
                        }
                        else if (b) {
                            String str = null;
                            switch (match) {
                                default: {
                                    str = "unknown reason";
                                    break;
                                }
                                case -1: {
                                    str = "type";
                                    break;
                                }
                                case -2: {
                                    str = "data";
                                    break;
                                }
                                case -3: {
                                    str = "action";
                                    break;
                                }
                                case -4: {
                                    str = "category";
                                    break;
                                }
                            }
                            final StringBuilder sb5 = new StringBuilder();
                            sb5.append("  Filter did not match: ");
                            sb5.append(str);
                            Log.v("LocalBroadcastManager", sb5.toString());
                        }
                    }
                }
                if (list != null) {
                    for (int j = 0; j < list.size(); ++j) {
                        list.get(j).broadcasting = false;
                    }
                    this.mPendingBroadcasts.add(new BroadcastRecord(obj, list));
                    if (!this.mHandler.hasMessages(1)) {
                        this.mHandler.sendEmptyMessage(1);
                    }
                    return true;
                }
            }
            return false;
        }
    }
    
    public void sendBroadcastSync(@NonNull final Intent intent) {
        if (this.sendBroadcast(intent)) {
            this.executePendingBroadcasts();
        }
    }
    
    public void unregisterReceiver(@NonNull final BroadcastReceiver key) {
        synchronized (this.mReceivers) {
            final ArrayList list = this.mReceivers.remove(key);
            if (list == null) {
                return;
            }
            for (int i = list.size() - 1; i >= 0; --i) {
                final ReceiverRecord receiverRecord = (ReceiverRecord)list.get(i);
                receiverRecord.dead = true;
                for (int j = 0; j < receiverRecord.filter.countActions(); ++j) {
                    final String action = receiverRecord.filter.getAction(j);
                    final ArrayList list2 = this.mActions.get(action);
                    if (list2 != null) {
                        for (int k = list2.size() - 1; k >= 0; --k) {
                            final ReceiverRecord receiverRecord2 = (ReceiverRecord)list2.get(k);
                            if (receiverRecord2.receiver == key) {
                                receiverRecord2.dead = true;
                                list2.remove(k);
                            }
                        }
                        if (list2.size() <= 0) {
                            this.mActions.remove(action);
                        }
                    }
                }
            }
        }
    }
    
    private static final class BroadcastRecord
    {
        final Intent intent;
        final ArrayList<ReceiverRecord> receivers;
        
        BroadcastRecord(final Intent intent, final ArrayList<ReceiverRecord> receivers) {
            this.intent = intent;
            this.receivers = receivers;
        }
    }
    
    private static final class ReceiverRecord
    {
        boolean broadcasting;
        boolean dead;
        final IntentFilter filter;
        final BroadcastReceiver receiver;
        
        ReceiverRecord(final IntentFilter filter, final BroadcastReceiver receiver) {
            this.filter = filter;
            this.receiver = receiver;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder(128);
            sb.append("Receiver{");
            sb.append(this.receiver);
            sb.append(" filter=");
            sb.append(this.filter);
            if (this.dead) {
                sb.append(" DEAD");
            }
            sb.append("}");
            return sb.toString();
        }
    }
}
