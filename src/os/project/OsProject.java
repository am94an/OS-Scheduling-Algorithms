package os.project;

import java.util.*;

public class OsProject {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Choose the scheduling algorithm to execute:");
            System.out.println("1. First-Come, First-Served (FCFS)");
            System.out.println("2. Shortest Job First (SJF)");
            System.out.println("3. Round Robin (RR)");
            System.out.println("4. Priority (Non-Preemptive)");
            System.out.println("5. Priority (Preemptive)");
            System.out.print("Enter the number of the algorithm (1-5): ");
            int choice = scanner.nextInt();

            ArrayList<Process> processes = ProcessScanner.scanProcesses(choice);

            int timeQuantum = 0;
            if (choice == 3) {
                timeQuantum = ProcessScanner.scanTimeQuantum();
            }

            ArrayList<Process> completedProcesses = new ArrayList<>();
            switch (choice) {
                case 1:
                    completedProcesses = SchedulingAlgorithms.executeFCFS(processes);
                    break;
                case 2:
                    completedProcesses = SchedulingAlgorithms.executeSJF(processes);
                    break;
                case 3:
                    completedProcesses = SchedulingAlgorithms.executeRoundRobin(processes, timeQuantum);
                    break;
                case 4:
                    completedProcesses = SchedulingAlgorithms.executePriorityNonPreemptive(processes);
                    break;
                case 5:
                    completedProcesses = SchedulingAlgorithms.executePriorityPreemptive(processes);
                    break;
                default:
                    System.out.println("Invalid choice! Please enter a number between 1 and 5.");
                    return;
            }

            double totalTurnaroundTime = completedProcesses.stream().mapToInt(p -> p.turnaroundTime).sum();
            double totalWaitingTime = completedProcesses.stream().mapToInt(p -> p.waitingTime).sum();
            double averageTurnaroundTime = totalTurnaroundTime / completedProcesses.size();
            double averageWaitingTime = totalWaitingTime / completedProcesses.size();

            OutputPrinter.printProcesses(completedProcesses);
            OutputPrinter.printAverageTimes(averageTurnaroundTime, averageWaitingTime);

        } catch (InputMismatchException e) {
            System.out.println("Error: Invalid input. Please enter valid numbers.");
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
