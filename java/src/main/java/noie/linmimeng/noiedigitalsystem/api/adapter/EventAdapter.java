package noie.linmimeng.noiedigitalsystem.api.adapter;

import noie.linmimeng.noiedigitalsystem.api.event.EventId;
import noie.linmimeng.noiedigitalsystem.api.event.EventType;
import noie.linmimeng.noiedigitalsystem.api.event.NdsEvent;
import noie.linmimeng.noiedigitalsystem.api.identity.NdsIdentity;
import com.google.protobuf.Timestamp;
import java.time.Instant;

/**
 * Event Adapter - NdsEvent/EventId/EventType Domain ↔ Proto 轉換
 * 
 * <p>處理事件相關 Domain 對象與 Proto 消息之間的雙向轉換。</p>
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
     * 將 Domain EventId 轉換為 Proto EventId
     * 
     * @param domain Domain EventId（可為 null）
     * @return Proto EventId，如果輸入為 null 則返回 null
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
     * 將 Proto EventId 轉換為 Domain EventId
     * 
     * @param proto Proto EventId（可為 null）
     * @return Domain EventId，如果輸入為 null 則返回 null
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
     * 將 Domain EventType 轉換為 Proto EventType
     * 
     * @param type Domain EventType
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
     * 將 Proto EventType 轉換為 Domain EventType
     * 
     * @param protoType Proto EventType
     * @return Domain EventType
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
     * 將 Java Instant 轉換為 Proto Timestamp
     * 
     * @param instant Java Instant（可為 null）
     * @return Proto Timestamp，如果輸入為 null 則返回 null
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
     * 將 Proto Timestamp 轉換為 Java Instant
     * 
     * @param timestamp Proto Timestamp（可為 null）
     * @return Java Instant，如果輸入為 null 則返回 null
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
