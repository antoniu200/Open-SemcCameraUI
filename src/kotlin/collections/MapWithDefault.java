package kotlin.collections;

import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.markers.KMappedMarker;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.jetbrains.annotations.NotNull;

/* compiled from: MapWithDefault.kt */
@Metadata(bv = {1, 0, 2}, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\b\u0007\bb\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0006\b\u0001\u0010\u0002 \u00012\u000e\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u00020\u0003J\u0015\u0010\u0007\u001a\u00028\u00012\u0006\u0010\b\u001a\u00028\u0000H&¢\u0006\u0002\u0010\tR\u001e\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\n"}, d2 = {"Lkotlin/collections/MapWithDefault;", "K", GpsTagConstants.GPS_TAG_GPS_STATUS_VALUE_MEASUREMENT_INTEROPERABILITY, "", "map", "getMap", "()Ljava/util/Map;", "getOrImplicitDefault", "key", "(Ljava/lang/Object;)Ljava/lang/Object;", "kotlin-stdlib"}, k = 1, mv = {1, 1, 10})
/* loaded from: C:\Users\User\Desktop\camera\SemcCameraUI\classes.dex */
interface MapWithDefault<K, V> extends Map<K, V>, KMappedMarker {
    @NotNull
    Map<K, V> getMap();

    V getOrImplicitDefault(K key);
}
