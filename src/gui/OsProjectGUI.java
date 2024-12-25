package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Comparator;

public class OsProjectGUI {

    private JFrame frame;
    private JTable inputTable;
    private JTable resultTable;
    private JTextField timeQuantumField;
    private JComboBox<String> algorithmSelector;
    private JLabel averageWaitingTimeLabel;
    private JLabel averageTurnaroundTimeLabel;
    private int processCount = 1;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                OsProjectGUI window = new OsProjectGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public OsProjectGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("OS Scheduling Algorithms");
        frame.setBounds(100, 100, 1000, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        inputTable = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Process", "Arrival Time", "Burst Time", "Priority"}
        ));
        JScrollPane inputScrollPane = new JScrollPane(inputTable);
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new GridLayout(2, 1));

        JPanel addProcessPanel = new JPanel();
        JButton addRowButton = new JButton("Add Row");
        addRowButton.addActionListener(e -> addRow());
        JButton removeRowButton = new JButton("Remove Row");
        removeRowButton.addActionListener(e -> removeRow());
        addProcessPanel.add(addRowButton);
        addProcessPanel.add(removeRowButton);

        controlPanel.add(addProcessPanel);

        JPanel algorithmPanel = new JPanel();

        JLabel timeQuantumLabel = new JLabel("Time Quantum (Round Robin):");
        algorithmPanel.add(timeQuantumLabel);

        timeQuantumField = new JTextField(5);
        algorithmPanel.add(timeQuantumField);

        JLabel algorithmLabel = new JLabel("Select Algorithm:");
        algorithmPanel.add(algorithmLabel);

        String[] algorithms = {"", "FCFS", "SJF", "Round Robin", "Priority Non-Preemptive"};
        algorithmSelector = new JComboBox<>(algorithms);
        algorithmSelector.setSelectedIndex(0);
        algorithmSelector.addActionListener(e -> updateInputFields());
        algorithmPanel.add(algorithmSelector);

        JButton runButton = new JButton("Run");
        runButton.addActionListener(this::runAlgorithm);
        algorithmPanel.add(runButton);

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearInputs());
        algorithmPanel.add(clearButton);

        controlPanel.add(algorithmPanel);

        inputPanel.add(controlPanel, BorderLayout.SOUTH);
        frame.add(inputPanel, BorderLayout.NORTH);

        resultTable = new JTable(new DefaultTableModel(
                new Object[][]{},
                new String[]{"Process", "Arrival Time", "Burst Time", "Priority", "Waiting Time", "Turnaround Time"}
        ));
        frame.add(new JScrollPane(resultTable), BorderLayout.CENTER);

        JPanel footerPanel = new JPanel(new GridLayout(2, 1));
        averageWaitingTimeLabel = new JLabel("Average Waiting Time: 0");
        footerPanel.add(averageWaitingTimeLabel);

        averageTurnaroundTimeLabel = new JLabel("Average Turnaround Time: 0");
        footerPanel.add(averageTurnaroundTimeLabel);

        frame.add(footerPanel, BorderLayout.SOUTH);
    }

    private void addRow() {
        DefaultTableModel model = (DefaultTableModel) inputTable.getModel();
        model.addRow(new Object[]{processCount++, 0, 0, 0});
    }

    private void removeRow() {
        DefaultTableModel model = (DefaultTableModel) inputTable.getModel();
        int selectedRow = inputTable.getSelectedRow();
        if (selectedRow != -1) {
            model.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a row to remove.", "No Row Selected", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateProcessNames() {
        DefaultTableModel model = (DefaultTableModel) inputTable.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(i + 1, i, 0);
        }
        processCount = model.getRowCount() + 1;
    }

    private void runAlgorithm(ActionEvent e) {
        try {
            String selectedAlgorithm = (String) algorithmSelector.getSelectedItem();
            if (selectedAlgorithm == null || selectedAlgorithm.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please select an algorithm.", "No Algorithm Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ArrayList<Process> processes = getProcessesFromTable();
            int timeQuantum = 0;
            if (selectedAlgorithm.equals("Round Robin")) {
                timeQuantum = Integer.parseInt(timeQuantumField.getText());
            }

            ArrayList<Process> resultProcesses;
            switch (selectedAlgorithm) {
                case "FCFS":
                    resultProcesses = SchedulingAlgorithms.executeFCFS(processes);
                    break;
                case "SJF":
                    resultProcesses = SchedulingAlgorithms.executeSJF(processes);
                    break;
                case "Round Robin":
                    resultProcesses = SchedulingAlgorithms.executeRoundRobin(processes, timeQuantum);
                    break;
                case "Priority Non-Preemptive":
                    resultProcesses = SchedulingAlgorithms.executePriorityNonPreemptive(processes);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid algorithm selection");
            }

            updateResultTable(resultProcesses);
            calculateAndDisplayAverages(resultProcesses);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Please enter valid numbers in the table and time quantum.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ArrayList<Process> getProcessesFromTable() {
        DefaultTableModel model = (DefaultTableModel) inputTable.getModel();
        ArrayList<Process> processes = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            String name = model.getValueAt(i, 0).toString();
            int arrivalTime = Integer.parseInt(model.getValueAt(i, 1).toString());
            int burstTime = Integer.parseInt(model.getValueAt(i, 2).toString());
            int priority = Integer.parseInt(model.getValueAt(i, 3).toString());
            processes.add(new Process(name, arrivalTime, burstTime, priority));
        }
        return processes;
    }

    private void clearInputs() {
        ((DefaultTableModel) inputTable.getModel()).setRowCount(0);
        ((DefaultTableModel) resultTable.getModel()).setRowCount(0);
        averageWaitingTimeLabel.setText("Average Waiting Time: 0");
        averageTurnaroundTimeLabel.setText("Average Turnaround Time: 0");
        processCount = 1;
        updateInputFields();

    }

    private void updateResultTable(ArrayList<Process> processes) {
        DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
        model.setRowCount(0);
        for (Process process : processes) {
            model.addRow(new Object[]{
                process.name, process.arrivalTime, process.burstTime, process.priority, process.waitingTime, process.turnaroundTime
            });
        }
    }

    private void calculateAndDisplayAverages(ArrayList<Process> processes) {
        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        for (Process process : processes) {
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnaroundTime;
        }

        int size = processes.size();
        averageWaitingTimeLabel.setText(String.format("Average Waiting Time: %.2f", totalWaitingTime / size));
        averageTurnaroundTimeLabel.setText(String.format("Average Turnaround Time: %.2f", totalTurnaroundTime / size));
    }

    private void updateInputFields() {
        String selectedAlgorithm = (String) algorithmSelector.getSelectedItem();
        boolean isRoundRobin = selectedAlgorithm.equals("Round Robin");
        boolean isPriority = selectedAlgorithm.equals("Priority Non-Preemptive");

        timeQuantumField.setEnabled(isRoundRobin);
        timeQuantumField.setVisible(isRoundRobin);

        if (isPriority) {
            inputTable.getColumnModel().getColumn(3).setMinWidth(50);
            inputTable.getColumnModel().getColumn(3).setMaxWidth(100);
            inputTable.getColumnModel().getColumn(3).setWidth(100);
        } else {
            inputTable.getColumnModel().getColumn(3).setMinWidth(0);
            inputTable.getColumnModel().getColumn(3).setMaxWidth(0);
            inputTable.getColumnModel().getColumn(3).setWidth(0);
        }
            SwingUtilities.invokeLater(() -> {
        inputTable.revalidate();
        inputTable.repaint();
    });


    }

}
