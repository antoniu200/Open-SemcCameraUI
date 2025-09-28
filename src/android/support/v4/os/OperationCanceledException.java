package android.support.v4.os;

/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public class OperationCanceledException extends RuntimeException {
    public OperationCanceledException() {
        this(null);
    }

    public OperationCanceledException(String str) {
        super(str == null ? "The operation has been canceled." : str);
    }
}
