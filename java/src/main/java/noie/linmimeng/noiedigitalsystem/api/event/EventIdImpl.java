package noie.linmimeng.noiedigitalsystem.api.event;

/**
 * [Index] NDS-JAVA-EVENTID-IMPL-000
 * [Semantic] Default implementation of {@link EventId}.
 * 
 * @since 2.0.0
 */
final class EventIdImpl implements EventId {
    private final String value;
    
    EventIdImpl(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("EventId value cannot be null or empty");
        }
        this.value = value;
    }
    
    @Override
    public String value() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventIdImpl eventId = (EventIdImpl) o;
        return value.equals(eventId.value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
    
    @Override
    public String toString() {
        return "EventId{value='" + value + "'}";
    }
}

