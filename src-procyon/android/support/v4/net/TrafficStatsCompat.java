// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v4.net;

import java.net.SocketException;
import java.net.Socket;
import android.os.ParcelFileDescriptor;
import android.os.Build$VERSION;
import android.support.annotation.NonNull;
import java.net.DatagramSocket;
import android.net.TrafficStats;

public final class TrafficStatsCompat
{
    private TrafficStatsCompat() {
    }
    
    @Deprecated
    public static void clearThreadStatsTag() {
        TrafficStats.clearThreadStatsTag();
    }
    
    @Deprecated
    public static int getThreadStatsTag() {
        return TrafficStats.getThreadStatsTag();
    }
    
    @Deprecated
    public static void incrementOperationCount(final int n) {
        TrafficStats.incrementOperationCount(n);
    }
    
    @Deprecated
    public static void incrementOperationCount(final int n, final int n2) {
        TrafficStats.incrementOperationCount(n, n2);
    }
    
    @Deprecated
    public static void setThreadStatsTag(final int threadStatsTag) {
        TrafficStats.setThreadStatsTag(threadStatsTag);
    }
    
    public static void tagDatagramSocket(@NonNull final DatagramSocket datagramSocket) throws SocketException {
        if (Build$VERSION.SDK_INT >= 24) {
            TrafficStats.tagDatagramSocket(datagramSocket);
        }
        else {
            final ParcelFileDescriptor fromDatagramSocket = ParcelFileDescriptor.fromDatagramSocket(datagramSocket);
            TrafficStats.tagSocket((Socket)new DatagramSocketWrapper(datagramSocket, fromDatagramSocket.getFileDescriptor()));
            fromDatagramSocket.detachFd();
        }
    }
    
    @Deprecated
    public static void tagSocket(final Socket socket) throws SocketException {
        TrafficStats.tagSocket(socket);
    }
    
    public static void untagDatagramSocket(@NonNull final DatagramSocket datagramSocket) throws SocketException {
        if (Build$VERSION.SDK_INT >= 24) {
            TrafficStats.untagDatagramSocket(datagramSocket);
        }
        else {
            final ParcelFileDescriptor fromDatagramSocket = ParcelFileDescriptor.fromDatagramSocket(datagramSocket);
            TrafficStats.untagSocket((Socket)new DatagramSocketWrapper(datagramSocket, fromDatagramSocket.getFileDescriptor()));
            fromDatagramSocket.detachFd();
        }
    }
    
    @Deprecated
    public static void untagSocket(final Socket socket) throws SocketException {
        TrafficStats.untagSocket(socket);
    }
}
