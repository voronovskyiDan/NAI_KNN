public class ObservationPair implements Comparable<ObservationPair> {
    private final Double priority;
    private final Observation value;

    public ObservationPair(Double priority, Observation value) {
        this.priority = priority;
        this.value = value;
    }
    public Double getPriority() {
        return priority;
    }
    public Observation getValue() {
        return value;
    }
    @Override
    public int compareTo(ObservationPair o) {
        return priority.compareTo(o.priority);
    }
}
