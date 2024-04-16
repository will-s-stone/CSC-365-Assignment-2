import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class GUI2 extends JFrame {
    private JTextField userInputField;
    private JButton storeButton;
    private JLabel resultLabel;
    public GUI2(List<List<String>> clusters) {
            // Set up the JFrame
            setTitle("User Input GUI");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 300);
            setLocationRelativeTo(null);

            // Create components
            userInputField = new JTextField(20);
            storeButton = new JButton("Enter a term or link to look up then press me :)");
            resultLabel = new JLabel("");

            // Set layout manager
            setLayout(new GridLayout(3, 1));

            // Add components to the content pane
            add(userInputField);
            add(storeButton);
            add(resultLabel);

            // Add action listener to the button
            storeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        String userInput = userInputField.getText();
                        for (int i = 0; i < clusters.size(); i++) {
                            if (clusters.get(i).contains(userInput)){
                                resultLabel.setText(userInput + " belongs to cluster " + i + ". The most similar key in this cluster is: " + clusters.get(i).get(KMedoidsClusterer.mostRecentMediod(clusters.get(i))));
                            }
                        }



                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }
            });
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        StorageHandler handler = new StorageHandler();
        List<List<String>> clusters = handler.getClusters();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI2(clusters).setVisible(true);
            }
        });
    }
}



