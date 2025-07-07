import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BIAHealthMonitorGUI extends JFrame {
    private JPanel contentPane;
    private JTextField nameField;
    private JTextField weightField;
    private JTextField heightField;
    private JTextField voltageField;
    private JTextField ageField;
    private JTextField genderField;
    private JTextArea outputArea;
    private JButton calculateButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                BIAHealthMonitorGUI frame = new BIAHealthMonitorGUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public BIAHealthMonitorGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 600);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        createGUIComponents();
    }

    private void createGUIComponents() {
        contentPane.setLayout(new GridLayout(8, 2));

        contentPane.add(createLabel("Name:"));
        nameField = new JTextField(10);
        nameField.setFont(nameField.getFont().deriveFont(Font.PLAIN, 20)); // Set font size here
        contentPane.add(nameField);

        contentPane.add(createLabel("Weight (kg):"));
        weightField = new JTextField(5);
        weightField.setFont(weightField.getFont().deriveFont(Font.PLAIN, 20)); // Set font size here
        contentPane.add(weightField);

        contentPane.add(createLabel("Height (cm):"));
        heightField = new JTextField(5);
        heightField.setFont(heightField.getFont().deriveFont(Font.PLAIN, 20)); // Set font size here
        contentPane.add(heightField);

        contentPane.add(createLabel("Voltage:"));
        voltageField = new JTextField(5);
        voltageField.setFont(voltageField.getFont().deriveFont(Font.PLAIN, 20)); // Set font size here
        contentPane.add(voltageField);

        contentPane.add(createLabel("Age:"));
        ageField = new JTextField(5);
        ageField.setFont(ageField.getFont().deriveFont(Font.PLAIN, 20)); // Set font size here
        contentPane.add(ageField);

        contentPane.add(createLabel("Gender (M/F):"));
        genderField = new JTextField(3);
        genderField.setFont(genderField.getFont().deriveFont(Font.PLAIN, 20)); // Set font size here
        contentPane.add(genderField);

        calculateButton = new JButton("Calculate");
        contentPane.add(calculateButton);

        outputArea = new JTextArea(10, 20);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        contentPane.add(createLabel("THE RESULT BIA CALCULATOR"));
        contentPane.add(scrollPane);

        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCalculateButtonAction();
            }
        });
    }

    private JLabel createLabel(String labelText) {
        JLabel label = new JLabel(labelText);
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 20)); // Set font size here
        return label;
    }

    private void handleCalculateButtonAction() {
        // Get user input from the text fields
        String name = nameField.getText();
        float weight = Float.parseFloat(weightField.getText());
        float height = Float.parseFloat(heightField.getText()) / 100; // Convert cm to meters
        float voltage = Float.parseFloat(voltageField.getText());
        int age = Integer.parseInt(ageField.getText());
        char gender = genderField.getText().charAt(0);

        // Perform calculations using the provided methods
        float impedance = (5 - (47 * voltage / 10)) / (voltage + 10);
        float bodyFatMass = (gender == 'M') ?
                (-10.463f + (0.671f * weight) - (0.184385f * impedance)) :
                (-9.411f + (0.518f * weight) - (0.206987f * impedance));
        float bodyFatPercentage = (gender == 'M') ?
                calculateBodyFatPercentageMale(weight, impedance) :
                (bodyFatMass / weight) * 100;
        float ffm = calculateFFM(weight, bodyFatMass);
        float tbw = calculateTBW(weight, height, impedance, gender);
        float bmr = calculateBMR(weight, height, age, gender);

        // Display output in the text area
        outputArea.setText("");
        outputArea.append(String.format("Name: %s%n", name));
        outputArea.append(String.format("BMI: %.2f%n", calculateBMI(weight, height)));
        outputArea.append(String.format("Body Fat Percentage: %.2f%%%n", bodyFatPercentage));
        outputArea.append(String.format("Fat-Free Mass: %.2f kg%n", ffm));
        outputArea.append(String.format("Total Body Water: %.2f kg%n", tbw));
        outputArea.append(String.format("Muscle Mass: %.2f kg%n", ffm));
        outputArea.append(String.format("Basal Metabolic Rate: %.2f kcal/day%n", bmr));
        outputArea.append("\n");
    }

    private static float calculateBMI(float weight, float height) {
        return weight / (height * height);
    }

    private static float calculateBodyFatPercentageMale(float weight, float impedance) {
        float bfMass = -10.463f + (0.671f * weight) - (0.184385f * impedance);
        return (bfMass / weight) * 100;
    }

    private static float calculateFFM(float weight, float bodyFatMass) {
        return weight * (1 - (bodyFatMass / 100));
    }

    private static float calculateTBW(float weight, float height, float impedance, char gender) {
        float factor = (gender == 'M') ? 0.155f : 0.245f;
        return (2.447f - (0.09156f * impedance) + (0.1074f * height)) * weight * factor;
    }

    private static float calculateBMR(float weight, float height, int age, char gender) {
        if (gender == 'M') {
            return (10 * weight) + (6.25f * height) - (5 * age) + 5+1000;
        } else {
            return (10 * weight) + (6.25f * height) - (5 * age) - 161+1000;
        }
    }
}
