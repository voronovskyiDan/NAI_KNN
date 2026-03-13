import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class KNNUserInterface extends JFrame {

    private List<Observation> data;
    private boolean firstLine = true;

    private final KNearestNeighbours knn;

    private JTextField kField;
    private JTextField sepalLengthField;
    private JTextField sepalWidthField;
    private JTextField petalLengthField;
    private JTextField petalWidthField;
    private JTextField filePathField;

    private JLabel currentKLabel;
    private JLabel singlePredictionLabel;
    private JLabel batchAccuracyLabel;

    public KNNUserInterface(KNearestNeighbours knn) {
        this.knn = knn;
        data = new ArrayList<>();

        setTitle("KNN Classifier - Iris Dataset");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        topPanel.add(createKPanel());
        topPanel.add(Box.createVerticalStrut(15));
        topPanel.add(createSinglePredictionPanel());
        topPanel.add(Box.createVerticalStrut(15));
        topPanel.add(createBatchPredictionPanel());

        mainPanel.add(topPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createKPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("K Parameter Settings"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        kField = new JTextField(10);
        JButton setKButton = new JButton("Set k");
        setKButton.addActionListener(e -> changeK());

        currentKLabel = new JLabel("Current k: not set");
        currentKLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("New k value:"), gbc);

        gbc.gridx = 1;
        panel.add(kField, gbc);

        gbc.gridx = 2;
        panel.add(setKButton, gbc);

        gbc.gridx = 3;
        panel.add(currentKLabel, gbc);

        return panel;
    }
    private JPanel createSinglePredictionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Predict New Observation"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        sepalLengthField = new JTextField(10);
        sepalWidthField = new JTextField(10);
        petalLengthField = new JTextField(10);
        petalWidthField = new JTextField(10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Sepal Length:"), gbc);
        gbc.gridx = 1;
        panel.add(sepalLengthField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Sepal Width:"), gbc);
        gbc.gridx = 1;
        panel.add(sepalWidthField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        panel.add(new JLabel("Petal Length:"), gbc);
        gbc.gridx = 3;
        panel.add(petalLengthField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        panel.add(new JLabel("Petal Width:"), gbc);
        gbc.gridx = 3;
        panel.add(petalWidthField, gbc);

        JButton predictButton = new JButton("Predict Class");
        predictButton.addActionListener(e -> predictSingleObservation());

        singlePredictionLabel = new JLabel("Predicted class: ");
        singlePredictionLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(predictButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(singlePredictionLabel, gbc);

        return panel;
    }
    private JPanel createBatchPredictionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Test File Evaluation"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        filePathField = new JTextField(30);
        filePathField.setEditable(false);

        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(e -> chooseTestFile());

        JButton evaluateButton = new JButton("Evaluate File");
        evaluateButton.addActionListener(e -> evaluateTestFile());

        batchAccuracyLabel = new JLabel("Accuracy: ");
        batchAccuracyLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Test CSV File:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(filePathField, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        panel.add(browseButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(evaluateButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(batchAccuracyLabel, gbc);

        return panel;
    }

    private void changeK() {
        try {
            int newK = Integer.parseInt(kField.getText().trim());

            if (newK <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "k must be greater than 0.",
                        "Invalid k",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            knn.setK(newK);
            currentKLabel.setText("Current k: " + newK);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid integer for k.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void predictSingleObservation() {
        try {
            Observation observation = new Observation(new ArrayList<>(List.of(
                    Double.parseDouble(sepalLengthField.getText().trim()),
                    Double.parseDouble(sepalWidthField.getText().trim()),
                    Double.parseDouble(petalLengthField.getText().trim()),
                    Double.parseDouble(petalWidthField.getText().trim())
            )));
            String predictedClass = knn.predict(observation);
            singlePredictionLabel.setText("Predicted class: " + predictedClass);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void chooseTestFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            filePathField.setText(selectedFile.getAbsolutePath());
        }

        String path = filePathField.getText().trim();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(",");

                Observation observation = new Observation(new ArrayList<>(List.of(
                        Double.parseDouble(parts[0].trim()),
                        Double.parseDouble(parts[1].trim()),
                        Double.parseDouble(parts[2].trim()),
                        Double.parseDouble(parts[3].trim())
                )), parts[4].trim());

                data.add(observation);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error while reading the file:\n" + ex.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void evaluateTestFile() {
        try{
            ArrayList<Observation> trainData = new ArrayList<>();
            ArrayList<Observation> testData = new ArrayList<>();

            PrepareDataset.trainTestSplit(data, testData, trainData);
            knn.setTrainDataset(trainData);

            ArrayList<String> realClasses = new ArrayList<>();
            ArrayList<String> predictedClasses = new ArrayList<>();

            for (Observation observation : testData) {
                predictedClasses.add( knn.predict(observation));
                realClasses.add(observation.getObservationType());
            }

            double accuracy = EvaluationMetrics.measureAccuracy(realClasses, predictedClasses);
            batchAccuracyLabel.setText("Accuracy: " + String.format("%.2f%%", accuracy * 100));
        }catch (Exception ex){
            JOptionPane.showMessageDialog(
                    this,
                    "Error evaluating the file:\n" + ex.getMessage(),
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    public static void main(String[] args) {
        KNearestNeighbours knn = new KNearestNeighbours();

        SwingUtilities.invokeLater(() -> {
            KNNUserInterface ui = new KNNUserInterface(knn);
            ui.setVisible(true);
        });
    }
}