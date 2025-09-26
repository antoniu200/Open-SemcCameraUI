// 
// Decompiled by Procyon v0.6.0
// 

package kotlin.concurrent;

import java.util.concurrent.locks.Lock;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import kotlin.jvm.JvmName;
import kotlin.Metadata;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u001a\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a&\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004H\u0087\b¢\u0006\u0002\u0010\u0005\u001a&\u0010\u0006\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u00072\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004H\u0087\b¢\u0006\u0002\u0010\b\u001a&\u0010\t\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004H\u0087\b¢\u0006\u0002\u0010\u0005¨\u0006\n" }, d2 = { "read", "T", "Ljava/util/concurrent/locks/ReentrantReadWriteLock;", "action", "Lkotlin/Function0;", "(Ljava/util/concurrent/locks/ReentrantReadWriteLock;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "withLock", "Ljava/util/concurrent/locks/Lock;", "(Ljava/util/concurrent/locks/Lock;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "write", "kotlin-stdlib" }, k = 2, mv = { 1, 1, 10 })
@JvmName(name = "LocksKt")
public final class LocksKt
{
    @InlineOnly
    private static final <T> T read(@NotNull ReentrantReadWriteLock lock, final Function0<? extends T> function0) {
        lock = (ReentrantReadWriteLock)lock.readLock();
        ((ReentrantReadWriteLock.ReadLock)lock).lock();
        try {
            return (T)function0.invoke();
        }
        finally {
            InlineMarker.finallyStart(1);
            ((ReentrantReadWriteLock.ReadLock)lock).unlock();
            InlineMarker.finallyEnd(1);
        }
    }
    
    @InlineOnly
    private static final <T> T withLock(@NotNull final Lock lock, final Function0<? extends T> function0) {
        lock.lock();
        try {
            return (T)function0.invoke();
        }
        finally {
            InlineMarker.finallyStart(1);
            lock.unlock();
            InlineMarker.finallyEnd(1);
        }
    }
    
    @InlineOnly
    private static final <T> T write(@NotNull ReentrantReadWriteLock writeLock, final Function0<? extends T> function0) {
        final ReentrantReadWriteLock.ReadLock lock = writeLock.readLock();
        final int writeHoldCount = writeLock.getWriteHoldCount();
        final int n = 0;
        int readHoldCount;
        if (writeHoldCount == 0) {
            readHoldCount = writeLock.getReadHoldCount();
        }
        else {
            readHoldCount = 0;
        }
        for (int i = 0; i < readHoldCount; ++i) {
            lock.unlock();
        }
        writeLock = (ReentrantReadWriteLock)writeLock.writeLock();
        ((ReentrantReadWriteLock.WriteLock)writeLock).lock();
        try {
            return (T)function0.invoke();
        }
        finally {
            InlineMarker.finallyStart(1);
            for (int j = n; j < readHoldCount; ++j) {
                lock.lock();
            }
            ((ReentrantReadWriteLock.WriteLock)writeLock).unlock();
            InlineMarker.finallyEnd(1);
        }
    }
}
