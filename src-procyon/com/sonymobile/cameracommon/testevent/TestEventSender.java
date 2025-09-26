// 
// Decompiled by Procyon v0.6.0
// 

package com.sonymobile.cameracommon.testevent;

public class TestEventSender
{
    private static TestEventListener sListener;
    
    static {
        TestEventSender.sListener = getBlankListener();
    }
    
    private static TestEventListener getBlankListener() {
        return new TestEventListener() {
            @Override
            public void onCapturedFrameStored(final long n) {
            }
            
            @Override
            public void onPictureTaken() {
            }
        };
    }
    
    public static void onCapturedFrameStored(final long n) {
        TestEventSender.sListener.onCapturedFrameStored(n);
    }
    
    public static void onPictureTaken() {
        TestEventSender.sListener.onPictureTaken();
    }
    
    public static void setListener(final TestEventListener sListener) {
        if (sListener == null) {
            TestEventSender.sListener = getBlankListener();
            return;
        }
        TestEventSender.sListener = sListener;
    }
}
