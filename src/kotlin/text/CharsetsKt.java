package kotlin.text;

import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: Charsets.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\b¨\u0006\u0004"}, d2 = {"charset", "Ljava/nio/charset/Charset;", "charsetName", "", "kotlin-stdlib"}, k = 2, mv = {1, 1, 10})
@JvmName(name = "CharsetsKt")
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
public final class CharsetsKt {
    @InlineOnly
    private static final Charset charset(String str) {
        Charset charsetForName = Charset.forName(str);
        Intrinsics.checkExpressionValueIsNotNull(charsetForName, "Charset.forName(charsetName)");
        return charsetForName;
    }
}
