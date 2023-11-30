import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersonInfoGUI extends JFrame {
    private List<PersonPanel> personPanels;
    private JTextArea resultTextArea;

    public PersonInfoGUI() {
        setTitle("Person Information");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        personPanels = new ArrayList<>();

        JPanel personInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        resultTextArea = new JTextArea(10, 20);
        resultTextArea.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(resultTextArea);
        resultScrollPane.setPreferredSize(new Dimension(resultScrollPane.getPreferredSize().width, 500));
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                collectData();
            }
        });

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        controlPanel.add(submitButton);

        add(resultScrollPane, BorderLayout.SOUTH);
        add(controlPanel, BorderLayout.NORTH);
        add(personInfoPanel, BorderLayout.CENTER);

        // Add three PersonPanels by default
        for (int i = 0; i < 7; i++) {
            PersonPanel personPanel = new PersonPanel();
            personPanels.add(personPanel);
            personInfoPanel.add(personPanel);
        }

        pack();
        setLocationRelativeTo(null);
    }

    private void collectData() {
        StringBuilder resultBuilder = new StringBuilder("Person Information:\n");
        HashMap<String, Person> people = new HashMap<>();
        for (PersonPanel personPanel : personPanels) {
            String nameString = personPanel.getName();
            String mother = personPanel.getMother();
            String father = personPanel.getFather();
            Boolean trait = personPanel.getTrait();
            Person name = new Person(nameString, mother, father, trait);
            people.put(nameString, name);
    
            resultBuilder.append("Name: ").append(name).append(", Mother: ").append(mother)
                    .append(", Father: ").append(father).append(", Trait: ").append(trait).append("\n");
        }
    
        List<Person> People = new ArrayList<>(people.values());
    
        probabilities probabilities = new probabilities(People);
    
        Set<String> names = people.keySet();
    
        List<Set<String>> subset = heredity.generateSubsets(names);
    
        for (Set<String> have_traits : subset) {
            Boolean fails_evidence = false;
            for (String person : names) {
                if ((people.get(person).getTrait() != null) && (people.get(person).getTrait() != have_traits.contains(person))) {
                    fails_evidence = true;
                    break;
                }
            }
            if (fails_evidence == true) {
                continue;
            }
    
            for (Set<String> one_Gene : subset) {
                Set<String> complementSet = new HashSet<>(names);
                complementSet.removeAll(one_Gene);
                List<Set<String>> complementPowerSet = heredity.generateSubsets(complementSet);
                for (Set<String> two_Genes : complementPowerSet) {
    
    
                    double p = heredity.Joint_probability(people, one_Gene, two_Genes, have_traits);
                    heredity.update(probabilities, one_Gene, two_Genes, have_traits, p);
                }
            }
        }
    
        heredity.normalize(probabilities);
    
        resultBuilder.append("\nProbabilities:\n");
        for (Person person : probabilities.people()) {
            resultBuilder.append(person.getName()).append(":\n");
    
            for (String field : probabilities.probabilities.get(person).keySet()) {
                resultBuilder.append("  ").append(field.substring(0, 1).toUpperCase()).append(field.substring(1)).append(":\n");
    
                for (Integer value : probabilities.probabilities.get(person).get(field).keySet()) {
                    double p = probabilities.probabilities.get(person).get(field).get(value);
                    resultBuilder.append("    ").append(value).append(": ").append(String.format("%.4f", p)).append("\n");
                }
            }
        }
    
        resultTextArea.setText(resultBuilder.toString());
    }

    private class PersonPanel extends JPanel {
        private JTextField nameTextField;
        private JTextField motherTextField;
        private JTextField fatherTextField;
        private JComboBox<String> traitComboBox;

        public PersonPanel() {
            setLayout(new GridLayout(4, 2, 5, 5));

            JLabel nameLabel = new JLabel("Name:");
            nameTextField = new JTextField(10);

            JLabel motherLabel = new JLabel("Mother:");
            motherTextField = new JTextField(10);

            JLabel fatherLabel = new JLabel("Father:");
            fatherTextField = new JTextField(10);

            JLabel traitLabel = new JLabel("Trait:");
            traitComboBox = new JComboBox<>(new String[]{"True", "False", "Null"});

            add(nameLabel);
            add(nameTextField);
            add(motherLabel);
            add(motherTextField);
            add(fatherLabel);
            add(fatherTextField);
            add(traitLabel);
            add(traitComboBox);
        }

        public String getName() {
            String name = nameTextField.getText().trim();
            return name.isEmpty() ? null : name;
        }

        public String getMother() {
            String mother = motherTextField.getText().trim();
            return mother.isEmpty() ? null : mother;
        }

        public String getFather() {
            String father = fatherTextField.getText().trim();
            return father.isEmpty() ? null : father;
        }

        public Boolean getTrait() {
            String traitValue = (String) traitComboBox.getSelectedItem();
            if (traitValue.equals("True")) {
                return true;
            } else if (traitValue.equals("False")) {
                return false;
            } else {
                return null;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PersonInfoGUI().setVisible(true);
            }
        });
    }
}