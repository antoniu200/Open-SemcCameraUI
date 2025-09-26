// 
// Decompiled by Procyon v0.6.0
// 

package com.google.android.apps.gsa.search.shared.service.proto;

import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.Internal$EnumLiteMap;
import com.google.protobuf.MessageLite;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.WireFormat$FieldType;
import com.google.protobuf.GeneratedMessageLite$GeneratedExtension;

public final class LensServiceClientEvent
{
    public static final int LENS_SERVICE_CLIENT_EVENT_DATA_FIELD_NUMBER = 152666889;
    public static final GeneratedMessageLite$GeneratedExtension<ClientEventProto, LensServiceClientEventData> lensServiceClientEventData;
    
    static {
        lensServiceClientEventData = GeneratedMessageLite.newSingularGeneratedExtension((MessageLite)ClientEventProto.getDefaultInstance(), (Object)LensServiceClientEventData.getDefaultInstance(), (MessageLite)LensServiceClientEventData.getDefaultInstance(), (Internal$EnumLiteMap)null, 152666889, WireFormat$FieldType.MESSAGE, (Class)LensServiceClientEventData.class);
    }
    
    private LensServiceClientEvent() {
    }
    
    public static void registerAllExtensions(final ExtensionRegistryLite extensionRegistryLite) {
        extensionRegistryLite.add((GeneratedMessageLite$GeneratedExtension)LensServiceClientEvent.lensServiceClientEventData);
    }
}
