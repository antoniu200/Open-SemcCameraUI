// 
// Decompiled by Procyon v0.6.0
// 

package kotlin;

import org.jetbrains.annotations.Nullable;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import kotlin.jvm.JvmField;

@Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0007\u0018\u0000 \u00172\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001\u0017B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003¢\u0006\u0002\u0010\u0007J\u0011\u0010\r\u001a\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u0000H\u0096\u0002J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u000e\u001a\u0004\u0018\u00010\u0011H\u0096\u0002J\b\u0010\u0012\u001a\u00020\u0003H\u0016J\u0016\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003J\u001e\u0010\u0013\u001a\u00020\u00102\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0003J\b\u0010\u0014\u001a\u00020\u0015H\u0016J \u0010\u0016\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0006\u001a\u00020\u0003H\u0002R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u000e\u0010\f\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0018" }, d2 = { "Lkotlin/KotlinVersion;", "", "major", "", "minor", "(II)V", "patch", "(III)V", "getMajor", "()I", "getMinor", "getPatch", "version", "compareTo", "other", "equals", "", "", "hashCode", "isAtLeast", "toString", "", "versionOf", "Companion", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
@SinceKotlin(version = "1.1")
public final class KotlinVersion implements Comparable<KotlinVersion>
{
    @JvmField
    @NotNull
    public static final KotlinVersion CURRENT;
    public static final Companion Companion;
    public static final int MAX_COMPONENT_VALUE = 255;
    private final int major;
    private final int minor;
    private final int patch;
    private final int version;
    
    static {
        Companion = new Companion(null);
        CURRENT = new KotlinVersion(1, 2, 60);
    }
    
    public KotlinVersion(final int n, final int n2) {
        this(n, n2, 0);
    }
    
    public KotlinVersion(final int major, final int minor, final int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.version = this.versionOf(this.major, this.minor, this.patch);
    }
    
    private final int versionOf(final int i, final int j, final int k) {
        boolean b = false;
        Label_0051: {
            if (i >= 0) {
                if (255 >= i) {
                    if (j >= 0) {
                        if (255 >= j) {
                            if (k >= 0) {
                                if (255 >= k) {
                                    b = true;
                                    break Label_0051;
                                }
                            }
                        }
                    }
                }
            }
            b = false;
        }
        if (!b) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Version components are out of range: ");
            sb.append(i);
            sb.append('.');
            sb.append(j);
            sb.append('.');
            sb.append(k);
            throw new IllegalArgumentException(sb.toString().toString());
        }
        return (i << 16) + (j << 8) + k;
    }
    
    @Override
    public int compareTo(@NotNull final KotlinVersion kotlinVersion) {
        Intrinsics.checkParameterIsNotNull(kotlinVersion, "other");
        return this.version - kotlinVersion.version;
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        if (this == o) {
            return true;
        }
        Object o2 = o;
        if (!(o instanceof KotlinVersion)) {
            o2 = null;
        }
        final KotlinVersion kotlinVersion = (KotlinVersion)o2;
        boolean b = false;
        if (kotlinVersion != null) {
            if (this.version == kotlinVersion.version) {
                b = true;
            }
            return b;
        }
        return false;
    }
    
    public final int getMajor() {
        return this.major;
    }
    
    public final int getMinor() {
        return this.minor;
    }
    
    public final int getPatch() {
        return this.patch;
    }
    
    @Override
    public int hashCode() {
        return this.version;
    }
    
    public final boolean isAtLeast(final int n, final int n2) {
        return this.major > n || (this.major == n && this.minor >= n2);
    }
    
    public final boolean isAtLeast(final int n, final int n2, final int n3) {
        if (this.major <= n) {
            if (this.major == n) {
                if (this.minor > n2) {
                    return true;
                }
                if (this.minor == n2 && this.patch >= n3) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    @NotNull
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.major);
        sb.append('.');
        sb.append(this.minor);
        sb.append('.');
        sb.append(this.patch);
        return sb.toString();
    }
    
    @Metadata(bv = { 1, 0, 2 }, d1 = { "\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006X\u0087\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0007" }, d2 = { "Lkotlin/KotlinVersion$Companion;", "", "()V", "CURRENT", "Lkotlin/KotlinVersion;", "MAX_COMPONENT_VALUE", "", "kotlin-stdlib" }, k = 1, mv = { 1, 1, 10 })
    public static final class Companion
    {
        private Companion() {
        }
    }
}
