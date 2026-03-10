import java.util.ArrayList;

public class Observation {
    private ArrayList<Double> attributes;
    private String observationType;

    public Observation(ArrayList<Double> attributes, String observationType) {
        this.attributes = attributes;
        this.observationType = observationType;
    }

    public double calculateEuclideanDistance(Observation observation) {
        if(attributes.size() != observation.getAttributes().size())
            throw new IllegalArgumentException();

        double sum = 0.0;
        for(int i = 0; i < attributes.size(); i++) {
            sum += Math.pow(attributes.get(i) - observation.getAttributes().get(i), 2);
        }
        return Math.sqrt(sum);
    }

    public ArrayList<Double> getAttributes() {
        return attributes;
    }
    public void setAttributes(ArrayList<Double> attributes) {
        this.attributes = attributes;
    }
    public String getObservationType() {
        return observationType;
    }
}
 