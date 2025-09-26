package com.sonymobile.cameracommon.testevent;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class TestEventSender {
    private static TestEventListener sListener = getBlankListener();

    public static void setListener(TestEventListener testEventListener) {
        if (testEventListener == null) {
            sListener = getBlankListener();
        } else {
            sListener = testEventListener;
        }
    }

    public static void onPictureTaken() {
        sListener.onPictureTaken();
    }

    public static void onCapturedFrameStored(long j) {
        sListener.onCapturedFrameStored(j);
    }

    private static TestEventListener getBlankListener() {
        return new TestEventListener() { // from class: com.sonymobile.cameracommon.testevent.TestEventSender.1
            @Override // com.sonymobile.cameracommon.testevent.TestEventListener
            public void onCapturedFrameStored(long j) {
            }

            @Override // com.sonymobile.cameracommon.testevent.TestEventListener
            public void onPictureTaken() {
            }
        };
    }
}
