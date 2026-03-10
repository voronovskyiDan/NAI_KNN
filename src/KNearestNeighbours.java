import java.util.*;

public class KNearestNeighbours {
    private int k;
    private ArrayList<Observation> trainDataset;

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
        for(int i = 0; i < this.trainDataset.size(); i++) {
            distances.add(
                    new ObservationPair(
                            observation.calculateEuclideanDistance(this.trainDataset.get(i)),
                            this.trainDataset.get(i)
                    )
            );
        }
        ArrayList<ObservationPair> result = new ArrayList<>();
        for(int i = 1; i < distances.size(); i++) {
            ObservationPair key = distances.get(i);
            int j = i - 1;

            while (j >= 0 && distances.get(j).compareTo(key) > 0) {
                distances.set(j+1, distances.get(j));
                j = j - 1;
            }
            distances.set(j+1, key);
        }

        return result;
    }
    private String findPredictedClass(ArrayList<ObservationPair> sortedDistances) {
        if(sortedDistances.size() < k)
            throw new IllegalArgumentException("Value K is too big");

        Map<String, Integer> mode = new HashMap<>();
        int maxCount = 0;

        for(int i = 0; i < k; i++) {
            String type = sortedDistances.get(i).getValue().getObservationType();
            int count = mode.getOrDefault(type, 0);

            mode.put(type, count+1);
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
    public String predict(Observation observation) {
        ArrayList<ObservationPair> sortedDistances = sortDistances(observation);
        return findPredictedClass(sortedDistances);
    }

    public int getK() {
        return k;
    }
    public void setK(int k) {
        this.k = k;
    }
    public void setTrainDataset(ArrayList<Observation> trainDataset) {
        this.trainDataset = trainDataset;
    }
}