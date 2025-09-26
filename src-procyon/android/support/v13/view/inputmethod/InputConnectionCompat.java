// 
// Decompiled by Procyon v0.6.0
// 

package android.support.v13.view.inputmethod;

import android.net.Uri;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.inputmethod.InputConnectionWrapper;
import android.content.ClipDescription;
import android.os.Parcelable;
import android.view.inputmethod.InputContentInfo;
import android.os.Build$VERSION;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.support.annotation.NonNull;
import android.view.inputmethod.InputConnection;

public final class InputConnectionCompat
{
    private static final String COMMIT_CONTENT_ACTION = "android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT";
    private static final String COMMIT_CONTENT_CONTENT_URI_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI";
    private static final String COMMIT_CONTENT_DESCRIPTION_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION";
    private static final String COMMIT_CONTENT_FLAGS_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS";
    private static final String COMMIT_CONTENT_LINK_URI_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI";
    private static final String COMMIT_CONTENT_OPTS_KEY = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS";
    private static final String COMMIT_CONTENT_RESULT_RECEIVER = "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER";
    public static final int INPUT_CONTENT_GRANT_READ_URI_PERMISSION = 1;
    
    @Deprecated
    public InputConnectionCompat() {
    }
    
    public static boolean commitContent(@NonNull final InputConnection inputConnection, @NonNull final EditorInfo editorInfo, @NonNull final InputContentInfoCompat inputContentInfoCompat, final int n, @Nullable final Bundle bundle) {
        final ClipDescription description = inputContentInfoCompat.getDescription();
        final String[] contentMimeTypes = EditorInfoCompat.getContentMimeTypes(editorInfo);
        final int length = contentMimeTypes.length;
        int i = 0;
        while (true) {
            while (i < length) {
                if (description.hasMimeType(contentMimeTypes[i])) {
                    final boolean b = true;
                    if (!b) {
                        return false;
                    }
                    if (Build$VERSION.SDK_INT >= 25) {
                        return inputConnection.commitContent((InputContentInfo)inputContentInfoCompat.unwrap(), n, bundle);
                    }
                    final Bundle bundle2 = new Bundle();
                    bundle2.putParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI", (Parcelable)inputContentInfoCompat.getContentUri());
                    bundle2.putParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION", (Parcelable)inputContentInfoCompat.getDescription());
                    bundle2.putParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI", (Parcelable)inputContentInfoCompat.getLinkUri());
                    bundle2.putInt("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS", n);
                    bundle2.putParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS", (Parcelable)bundle);
                    return inputConnection.performPrivateCommand("android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", bundle2);
                }
                else {
                    ++i;
                }
            }
            final boolean b = false;
            continue;
        }
    }
    
    @NonNull
    public static InputConnection createWrapper(@NonNull final InputConnection inputConnection, @NonNull final EditorInfo editorInfo, @NonNull final OnCommitContentListener onCommitContentListener) {
        if (inputConnection == null) {
            throw new IllegalArgumentException("inputConnection must be non-null");
        }
        if (editorInfo == null) {
            throw new IllegalArgumentException("editorInfo must be non-null");
        }
        if (onCommitContentListener == null) {
            throw new IllegalArgumentException("onCommitContentListener must be non-null");
        }
        if (Build$VERSION.SDK_INT >= 25) {
            return (InputConnection)new InputConnectionWrapper(inputConnection, false, onCommitContentListener) {
                final OnCommitContentListener val$listener;
                
                public boolean commitContent(final InputContentInfo inputContentInfo, final int n, final Bundle bundle) {
                    return this.val$listener.onCommitContent(InputContentInfoCompat.wrap(inputContentInfo), n, bundle) || super.commitContent(inputContentInfo, n, bundle);
                }
            };
        }
        if (EditorInfoCompat.getContentMimeTypes(editorInfo).length == 0) {
            return inputConnection;
        }
        return (InputConnection)new InputConnectionWrapper(inputConnection, false, onCommitContentListener) {
            final OnCommitContentListener val$listener;
            
            public boolean performPrivateCommand(final String s, final Bundle bundle) {
                return InputConnectionCompat.handlePerformPrivateCommand(s, bundle, this.val$listener) || super.performPrivateCommand(s, bundle);
            }
        };
    }
    
    static boolean handlePerformPrivateCommand(@Nullable final String s, @NonNull final Bundle bundle, @NonNull final OnCommitContentListener onCommitContentListener) {
        if (!TextUtils.equals((CharSequence)"android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", (CharSequence)s)) {
            return false;
        }
        if (bundle == null) {
            return false;
        }
        ResultReceiver resultReceiver2;
        try {
            final ResultReceiver resultReceiver = (ResultReceiver)bundle.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER");
            try {
                final int onCommitContent = onCommitContentListener.onCommitContent(new InputContentInfoCompat((Uri)bundle.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI"), (ClipDescription)bundle.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION"), (Uri)bundle.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI")), bundle.getInt("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS"), (Bundle)bundle.getParcelable("android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS")) ? 1 : 0;
                if (resultReceiver != null) {
                    resultReceiver.send(onCommitContent, (Bundle)null);
                }
                return onCommitContent != 0;
            }
            finally {}
        }
        finally {
            resultReceiver2 = null;
        }
        if (resultReceiver2 != null) {
            resultReceiver2.send(0, (Bundle)null);
        }
    }
    
    public interface OnCommitContentListener
    {
        boolean onCommitContent(final InputContentInfoCompat p0, final int p1, final Bundle p2);
    }
}
