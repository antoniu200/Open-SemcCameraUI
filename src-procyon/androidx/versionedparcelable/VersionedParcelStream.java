// 
// Decompiled by Procyon v0.6.0
// 

package androidx.versionedparcelable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import android.os.IInterface;
import java.util.Iterator;
import java.util.Set;
import android.os.IBinder;
import java.io.IOException;
import android.os.Parcelable;
import android.os.Bundle;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import android.util.SparseArray;
import java.nio.charset.Charset;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY })
class VersionedParcelStream extends VersionedParcel
{
    private static final int TYPE_BOOLEAN = 5;
    private static final int TYPE_BOOLEAN_ARRAY = 6;
    private static final int TYPE_DOUBLE = 7;
    private static final int TYPE_DOUBLE_ARRAY = 8;
    private static final int TYPE_FLOAT = 13;
    private static final int TYPE_FLOAT_ARRAY = 14;
    private static final int TYPE_INT = 9;
    private static final int TYPE_INT_ARRAY = 10;
    private static final int TYPE_LONG = 11;
    private static final int TYPE_LONG_ARRAY = 12;
    private static final int TYPE_NULL = 0;
    private static final int TYPE_STRING = 3;
    private static final int TYPE_STRING_ARRAY = 4;
    private static final int TYPE_SUB_BUNDLE = 1;
    private static final int TYPE_SUB_PERSISTABLE_BUNDLE = 2;
    private static final Charset UTF_16;
    private final SparseArray<InputBuffer> mCachedFields;
    private DataInputStream mCurrentInput;
    private DataOutputStream mCurrentOutput;
    private FieldBuffer mFieldBuffer;
    private boolean mIgnoreParcelables;
    private final DataInputStream mMasterInput;
    private final DataOutputStream mMasterOutput;
    
    static {
        UTF_16 = Charset.forName("UTF-16");
    }
    
    public VersionedParcelStream(final InputStream in, final OutputStream out) {
        this.mCachedFields = (SparseArray<InputBuffer>)new SparseArray();
        final DataOutputStream dataOutputStream = null;
        DataInputStream mMasterInput;
        if (in != null) {
            mMasterInput = new DataInputStream(in);
        }
        else {
            mMasterInput = null;
        }
        this.mMasterInput = mMasterInput;
        DataOutputStream mMasterOutput = dataOutputStream;
        if (out != null) {
            mMasterOutput = new DataOutputStream(out);
        }
        this.mMasterOutput = mMasterOutput;
        this.mCurrentInput = this.mMasterInput;
        this.mCurrentOutput = this.mMasterOutput;
    }
    
    private void readObject(final int i, final String s, final Bundle bundle) {
        switch (i) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown type ");
                sb.append(i);
                throw new RuntimeException(sb.toString());
            }
            case 14: {
                bundle.putFloatArray(s, this.readFloatArray());
                break;
            }
            case 13: {
                bundle.putFloat(s, this.readFloat());
                break;
            }
            case 12: {
                bundle.putLongArray(s, this.readLongArray());
                break;
            }
            case 11: {
                bundle.putLong(s, this.readLong());
                break;
            }
            case 10: {
                bundle.putIntArray(s, this.readIntArray());
                break;
            }
            case 9: {
                bundle.putInt(s, this.readInt());
                break;
            }
            case 8: {
                bundle.putDoubleArray(s, this.readDoubleArray());
                break;
            }
            case 7: {
                bundle.putDouble(s, this.readDouble());
                break;
            }
            case 6: {
                bundle.putBooleanArray(s, this.readBooleanArray());
                break;
            }
            case 5: {
                bundle.putBoolean(s, this.readBoolean());
                break;
            }
            case 4: {
                bundle.putStringArray(s, (String[])this.readArray(new String[0]));
                break;
            }
            case 3: {
                bundle.putString(s, this.readString());
                break;
            }
            case 2: {
                bundle.putBundle(s, this.readBundle());
                break;
            }
            case 1: {
                bundle.putBundle(s, this.readBundle());
                break;
            }
            case 0: {
                bundle.putParcelable(s, (Parcelable)null);
                break;
            }
        }
    }
    
    private void writeObject(final Object o) {
        if (o == null) {
            this.writeInt(0);
        }
        else if (o instanceof Bundle) {
            this.writeInt(1);
            this.writeBundle((Bundle)o);
        }
        else if (o instanceof String) {
            this.writeInt(3);
            this.writeString((String)o);
        }
        else if (o instanceof String[]) {
            this.writeInt(4);
            this.writeArray((String[])o);
        }
        else if (o instanceof Boolean) {
            this.writeInt(5);
            this.writeBoolean((boolean)o);
        }
        else if (o instanceof boolean[]) {
            this.writeInt(6);
            this.writeBooleanArray((boolean[])o);
        }
        else if (o instanceof Double) {
            this.writeInt(7);
            this.writeDouble((double)o);
        }
        else if (o instanceof double[]) {
            this.writeInt(8);
            this.writeDoubleArray((double[])o);
        }
        else if (o instanceof Integer) {
            this.writeInt(9);
            this.writeInt((int)o);
        }
        else if (o instanceof int[]) {
            this.writeInt(10);
            this.writeIntArray((int[])o);
        }
        else if (o instanceof Long) {
            this.writeInt(11);
            this.writeLong((long)o);
        }
        else if (o instanceof long[]) {
            this.writeInt(12);
            this.writeLongArray((long[])o);
        }
        else if (o instanceof Float) {
            this.writeInt(13);
            this.writeFloat((float)o);
        }
        else {
            if (!(o instanceof float[])) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unsupported type ");
                sb.append(o.getClass());
                throw new IllegalArgumentException(sb.toString());
            }
            this.writeInt(14);
            this.writeFloatArray((float[])o);
        }
    }
    
    public void closeField() {
        if (this.mFieldBuffer != null) {
            try {
                if (this.mFieldBuffer.mOutput.size() != 0) {
                    this.mFieldBuffer.flushField();
                }
                this.mFieldBuffer = null;
            }
            catch (final IOException ex) {
                throw new ParcelException(ex);
            }
        }
    }
    
    @Override
    protected VersionedParcel createSubParcel() {
        return new VersionedParcelStream(this.mCurrentInput, this.mCurrentOutput);
    }
    
    @Override
    public boolean isStream() {
        return true;
    }
    
    public boolean readBoolean() {
        try {
            return this.mCurrentInput.readBoolean();
        }
        catch (final IOException ex) {
            throw new ParcelException(ex);
        }
    }
    
    public Bundle readBundle() {
        final int int1 = this.readInt();
        if (int1 < 0) {
            return null;
        }
        final Bundle bundle = new Bundle();
        for (int i = 0; i < int1; ++i) {
            this.readObject(this.readInt(), this.readString(), bundle);
        }
        return bundle;
    }
    
    public byte[] readByteArray() {
        try {
            final int int1 = this.mCurrentInput.readInt();
            if (int1 > 0) {
                final byte[] b = new byte[int1];
                this.mCurrentInput.readFully(b);
                return b;
            }
            return null;
        }
        catch (final IOException ex) {
            throw new ParcelException(ex);
        }
    }
    
    public double readDouble() {
        try {
            return this.mCurrentInput.readDouble();
        }
        catch (final IOException ex) {
            throw new ParcelException(ex);
        }
    }
    
    public boolean readField(final int n) {
        final InputBuffer inputBuffer = (InputBuffer)this.mCachedFields.get(n);
        if (inputBuffer != null) {
            this.mCachedFields.remove(n);
            this.mCurrentInput = inputBuffer.mInputStream;
            return true;
        }
        Label_0037: {
            break Label_0037;
            try {
                InputBuffer inputBuffer2;
                while (true) {
                    final int int1 = this.mMasterInput.readInt();
                    int int2;
                    if ((int2 = (int1 & 0xFFFF)) == 65535) {
                        int2 = this.mMasterInput.readInt();
                    }
                    inputBuffer2 = new InputBuffer(int1 >> 16 & 0xFFFF, int2, this.mMasterInput);
                    if (inputBuffer2.mFieldId == n) {
                        break;
                    }
                    this.mCachedFields.put(inputBuffer2.mFieldId, (Object)inputBuffer2);
                }
                this.mCurrentInput = inputBuffer2.mInputStream;
                return true;
            }
            catch (final IOException ex) {
                return false;
            }
        }
    }
    
    public float readFloat() {
        try {
            return this.mCurrentInput.readFloat();
        }
        catch (final IOException ex) {
            throw new ParcelException(ex);
        }
    }
    
    public int readInt() {
        try {
            return this.mCurrentInput.readInt();
        }
        catch (final IOException ex) {
            throw new ParcelException(ex);
        }
    }
    
    public long readLong() {
        try {
            return this.mCurrentInput.readLong();
        }
        catch (final IOException ex) {
            throw new ParcelException(ex);
        }
    }
    
    public <T extends Parcelable> T readParcelable() {
        return null;
    }
    
    public String readString() {
        try {
            final int int1 = this.mCurrentInput.readInt();
            if (int1 > 0) {
                final byte[] array = new byte[int1];
                this.mCurrentInput.readFully(array);
                return new String(array, VersionedParcelStream.UTF_16);
            }
            return null;
        }
        catch (final IOException ex) {
            throw new ParcelException(ex);
        }
    }
    
    public IBinder readStrongBinder() {
        return null;
    }
    
    public void setOutputField(final int n) {
        this.closeField();
        this.mFieldBuffer = new FieldBuffer(n, this.mMasterOutput);
        this.mCurrentOutput = this.mFieldBuffer.mDataStream;
    }
    
    @Override
    public void setSerializationFlags(final boolean b, final boolean mIgnoreParcelables) {
        if (!b) {
            throw new RuntimeException("Serialization of this object is not allowed");
        }
        this.mIgnoreParcelables = mIgnoreParcelables;
    }
    
    public void writeBoolean(final boolean v) {
        try {
            this.mCurrentOutput.writeBoolean(v);
        }
        catch (final IOException ex) {
            throw new ParcelException(ex);
        }
    }
    
    public void writeBundle(final Bundle bundle) {
        if (bundle != null) {
            try {
                final Set keySet = bundle.keySet();
                this.mCurrentOutput.writeInt(keySet.size());
                for (final String s : keySet) {
                    this.writeString(s);
                    this.writeObject(bundle.get(s));
                }
                return;
            }
            catch (final IOException ex) {
                throw new ParcelException(ex);
            }
        }
        this.mCurrentOutput.writeInt(-1);
    }
    
    public void writeByteArray(final byte[] b) {
        if (b != null) {
            try {
                this.mCurrentOutput.writeInt(b.length);
                this.mCurrentOutput.write(b);
                return;
            }
            catch (final IOException ex) {
                throw new ParcelException(ex);
            }
        }
        this.mCurrentOutput.writeInt(-1);
    }
    
    public void writeByteArray(final byte[] b, final int off, final int n) {
        if (b != null) {
            try {
                this.mCurrentOutput.writeInt(n);
                this.mCurrentOutput.write(b, off, n);
                return;
            }
            catch (final IOException ex) {
                throw new ParcelException(ex);
            }
        }
        this.mCurrentOutput.writeInt(-1);
    }
    
    public void writeDouble(final double v) {
        try {
            this.mCurrentOutput.writeDouble(v);
        }
        catch (final IOException ex) {
            throw new ParcelException(ex);
        }
    }
    
    public void writeFloat(final float v) {
        try {
            this.mCurrentOutput.writeFloat(v);
        }
        catch (final IOException ex) {
            throw new ParcelException(ex);
        }
    }
    
    public void writeInt(final int v) {
        try {
            this.mCurrentOutput.writeInt(v);
        }
        catch (final IOException ex) {
            throw new ParcelException(ex);
        }
    }
    
    public void writeLong(final long v) {
        try {
            this.mCurrentOutput.writeLong(v);
        }
        catch (final IOException ex) {
            throw new ParcelException(ex);
        }
    }
    
    public void writeParcelable(final Parcelable parcelable) {
        if (!this.mIgnoreParcelables) {
            throw new RuntimeException("Parcelables cannot be written to an OutputStream");
        }
    }
    
    public void writeString(final String s) {
        if (s != null) {
            try {
                final byte[] bytes = s.getBytes(VersionedParcelStream.UTF_16);
                this.mCurrentOutput.writeInt(bytes.length);
                this.mCurrentOutput.write(bytes);
                return;
            }
            catch (final IOException ex) {
                throw new ParcelException(ex);
            }
        }
        this.mCurrentOutput.writeInt(-1);
    }
    
    public void writeStrongBinder(final IBinder binder) {
        if (!this.mIgnoreParcelables) {
            throw new RuntimeException("Binders cannot be written to an OutputStream");
        }
    }
    
    public void writeStrongInterface(final IInterface interface1) {
        if (!this.mIgnoreParcelables) {
            throw new RuntimeException("Binders cannot be written to an OutputStream");
        }
    }
    
    private static class FieldBuffer
    {
        final DataOutputStream mDataStream;
        private final int mFieldId;
        final ByteArrayOutputStream mOutput;
        private final DataOutputStream mTarget;
        
        FieldBuffer(final int mFieldId, final DataOutputStream mTarget) {
            this.mOutput = new ByteArrayOutputStream();
            this.mDataStream = new DataOutputStream(this.mOutput);
            this.mFieldId = mFieldId;
            this.mTarget = mTarget;
        }
        
        void flushField() throws IOException {
            this.mDataStream.flush();
            final int size = this.mOutput.size();
            final int mFieldId = this.mFieldId;
            int n;
            if (size >= 65535) {
                n = 65535;
            }
            else {
                n = size;
            }
            this.mTarget.writeInt(mFieldId << 16 | n);
            if (size >= 65535) {
                this.mTarget.writeInt(size);
            }
            this.mOutput.writeTo(this.mTarget);
        }
    }
    
    private static class InputBuffer
    {
        final int mFieldId;
        final DataInputStream mInputStream;
        private final int mSize;
        
        InputBuffer(final int mFieldId, final int mSize, final DataInputStream dataInputStream) throws IOException {
            this.mSize = mSize;
            this.mFieldId = mFieldId;
            final byte[] array = new byte[this.mSize];
            dataInputStream.readFully(array);
            this.mInputStream = new DataInputStream(new ByteArrayInputStream(array));
        }
    }
}
