package noie.linmimeng.noiedigitalsystem.api.adapter;

import noie.linmimeng.noiedigitalsystem.api.event.EventId;
import noie.linmimeng.noiedigitalsystem.api.event.EventType;
import noie.linmimeng.noiedigitalsystem.api.event.NdsEvent;
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import com.google.protobuf.Timestamp;
import java.time.Instant;

/**
 * [Index] NDS-JAVA-EVENT-000
 * [Semantic] NdsEvent / EventId / EventType Domain ↔ Proto conversion.
 *
 * @since 2.0.0
 */
public final class EventAdapter {
    
    private EventAdapter() {
        // [Index] NDS-JAVA-EVENT-001 [Constraint] Utility class; instantiation is prohibited.
    }
    
    // ========================================================================
    // [Index] NDS-JAVA-EVENT-010 [Semantic] EventId conversion.
    // ========================================================================
    
    /**
     * @param domain domain EventId (nullable)
     * @return Proto EventId; null if input is null
     */
    public static noie.linmimeng.noiedigitalsystem.api.proto.event.EventId toProto(EventId domain) {
        if (domain == null) {
            return null;
        }
        
        return noie.linmimeng.noiedigitalsystem.api.proto.event.EventId.newBuilder()
            .setValue(domain.value())
            .build();
    }
    
    /**
     * @param proto Proto EventId (nullable)
     * @return domain EventId; null if proto is null or value is empty
     */
    public static EventId fromProto(noie.linmimeng.noiedigitalsystem.api.proto.event.EventId proto) {
        if (proto == null || proto.getValue().isEmpty()) {
            return null;
        }
        
        return EventId.fromString(proto.getValue());
    }
    
    // ========================================================================
    // [Index] NDS-JAVA-EVENT-020 [Semantic] EventType conversion.
    // ========================================================================
    
    /**
     * @param type domain EventType (nullable; treated as UNSPECIFIED if null)
     * @return Proto EventType
     */
    public static noie.linmimeng.noiedigitalsystem.api.proto.event.EventType toProtoType(EventType type) {
        if (type == null) {
            return noie.linmimeng.noiedigitalsystem.api.proto.event.EventType.EVENT_TYPE_UNSPECIFIED;
        }
        
        return switch (type) {
            case TRANSACTION -> noie.linmimeng.noiedigitalsystem.api.proto.event.EventType.EVENT_TYPE_TRANSACTION;
            case ASSET_CREATED -> noie.linmimeng.noiedigitalsystem.api.proto.event.EventType.EVENT_TYPE_ASSET_CREATED;
            case ASSET_UPDATED -> noie.linmimeng.noiedigitalsystem.api.proto.event.EventType.EVENT_TYPE_ASSET_UPDATED;
            case ASSET_DELETED -> noie.linmimeng.noiedigitalsystem.api.proto.event.EventType.EVENT_TYPE_ASSET_DELETED;
            case IDENTITY_CREATED -> noie.linmimeng.noiedigitalsystem.api.proto.event.EventType.EVENT_TYPE_IDENTITY_CREATED;
            case IDENTITY_UPDATED -> noie.linmimeng.noiedigitalsystem.api.proto.event.EventType.EVENT_TYPE_IDENTITY_UPDATED;
            case SYSTEM -> noie.linmimeng.noiedigitalsystem.api.proto.event.EventType.EVENT_TYPE_SYSTEM;
            case CUSTOM -> noie.linmimeng.noiedigitalsystem.api.proto.event.EventType.EVENT_TYPE_CUSTOM;
        };
    }
    
    /**
     * @param protoType Proto EventType (nullable; defaults to CUSTOM if null or unrecognized)
     * @return domain EventType
     */
    public static EventType fromProtoType(noie.linmimeng.noiedigitalsystem.api.proto.event.EventType protoType) {
        if (protoType == null) {
            return EventType.CUSTOM;
        }
        
        return switch (protoType) {
            case EVENT_TYPE_TRANSACTION -> EventType.TRANSACTION;
            case EVENT_TYPE_ASSET_CREATED -> EventType.ASSET_CREATED;
            case EVENT_TYPE_ASSET_UPDATED -> EventType.ASSET_UPDATED;
            case EVENT_TYPE_ASSET_DELETED -> EventType.ASSET_DELETED;
            case EVENT_TYPE_IDENTITY_CREATED -> EventType.IDENTITY_CREATED;
            case EVENT_TYPE_IDENTITY_UPDATED -> EventType.IDENTITY_UPDATED;
            case EVENT_TYPE_SYSTEM -> EventType.SYSTEM;
            case EVENT_TYPE_CUSTOM, EVENT_TYPE_UNSPECIFIED, UNRECOGNIZED -> EventType.CUSTOM;
        };
    }
    
    // ========================================================================
    // [Index] NDS-JAVA-EVENT-030 [Semantic] Timestamp conversion.
    // ========================================================================
    
    /**
     * @param instant Java Instant (nullable)
     * @return Proto Timestamp; null if input is null
     */
    public static Timestamp toProtoTimestamp(Instant instant) {
        if (instant == null) {
            return null;
        }
        
        return Timestamp.newBuilder()
            .setSeconds(instant.getEpochSecond())
            .setNanos(instant.getNano())
            .build();
    }
    
    /**
     * @param timestamp Proto Timestamp (nullable)
     * @return Java Instant; null if input is null
     */
    public static Instant fromProtoTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }
    
    // ========================================================================
    // [Index] NDS-JAVA-EVENT-900 [Semantic] NdsEvent conversion.
    // [Trace] Full conversion requires payload/context handling and is implemented in runtime modules when needed.
    // ========================================================================
    
    // [Index] NDS-JAVA-EVENT-901 [TODO] Implement full NdsEvent <-> proto conversion when required.
    // [Trace] Requires payload conversion (Struct <-> domain), which is intentionally deferred.
}
