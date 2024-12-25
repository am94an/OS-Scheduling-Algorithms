
package os.project;

import java.util.ArrayList;

public class OutputPrinter {
    public static void printProcesses(ArrayList<Process> processes) {
        System.out.println("-------------------------------------------------------------------");
        System.out.println("| Name | Arrival Time | Burst Time | Waiting Time | Turnaround Time |");
        System.out.println("-------------------------------------------------------------------");
        for (Process process : processes) {
            System.out.printf("| %-4s | %-12d | %-10d | %-12d | %-15d |%n",
                    process.name, process.arrivalTime, process.burstTime, process.waitingTime, process.turnaroundTime);
        }
        System.out.println("-------------------------------------------------------------------");
    }

    public static void printAverageTimes(double averageTurnaroundTime, double averageWaitingTime) {
        System.out.printf("Average Turnaround Time: %.2f\n", averageTurnaroundTime);
        System.out.printf("Average Waiting Time: %.2f\n", averageWaitingTime);
    }
}
