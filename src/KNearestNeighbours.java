import java.util.*;

public class KNearestNeighbours {
    private int k;
    private ArrayList<Observation> trainDataset;

    public KNearestNeighbours() {
        k = 0;
        trainDataset = new ArrayList<>();
    }
//    private ArrayList<Observation> sortDistances(Observation observation) {
//        PriorityQueue<ObservationPair> distances
//                = new PriorityQueue<>(this.trainDataset.size());
//        for (int i = 0; i < this.trainDataset.size(); i++) {
//            distances.add(
//                    new ObservationPair(
//                            observation.calculateEuclideanDistance(this.trainDataset.get(i)),
//                            this.trainDataset.get(i)
//                    )
//            );
//        }
//
//        ArrayList<Observation> sortedDistances = new ArrayList<>(k);
//        for(int i = 0; i < k; i++){
//            sortedDistances.add(distances.poll().getValue());
//        }
//        return sortedDistances;
//    }
    private ArrayList<ObservationPair> sortDistances(Observation observation) {
        ArrayList<ObservationPair> distances = new ArrayList<>(this.trainDataset.size());
        for (Observation value : this.trainDataset) {
            distances.add(
                    new ObservationPair(
                            observation.calculateEuclideanDistance(value),
                            value
                    )
            );
        }
        for(int i = 1; i < distances.size(); i++) {
            ObservationPair key = distances.get(i);
            int j = i - 1;

            while (j >= 0 && distances.get(j).compareTo(key) > 0) {
                distances.set(j+1, distances.get(j));
                j = j - 1;
            }
            distances.set(j+1, key);
        }

        return distances;
    }
    private String findPredictedClass(ArrayList<ObservationPair> sortedDistances) {
        if(sortedDistances.size() < k)
            throw new IllegalArgumentException("Value K is too big");

        Map<String, Integer> mode = new HashMap<>();
        int maxCount = 0;

        for(int i = 0; i < k; i++) {
            String type = sortedDistances.get(i).getValue().getObservationType();
            int count = mode.getOrDefault(type, 0) + 1;

            mode.put(type, count);
            maxCount = Math.max(maxCount, count);
        }

        List<String> mostFrequents = new ArrayList<>();
        for(Map.Entry<String, Integer> entry : mode.entrySet()) {
            if(entry.getValue() == maxCount) {
                mostFrequents.add(entry.getKey());
            }
        }

        return mostFrequents.get(new Random().nextInt(mostFrequents.size()));
    }
    public String predict(Observation observation) throws Exception {
        if(trainDataset.isEmpty())
            throw new Exception("No train dataset found\nEvaluate file first");
        if(trainDataset.size() < k)
            throw new Exception("Value K is too big");
        if(k == 0)
            throw new Exception("Value of K is not set");

        ArrayList<ObservationPair> sortedDistances = sortDistances(observation);
        return findPredictedClass(sortedDistances);
    }

    public void setK(int k) {
        this.k = k;
    }
    public void setTrainDataset(ArrayList<Observation> trainDataset) {
        this.trainDataset = trainDataset;
    }
}