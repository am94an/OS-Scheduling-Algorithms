
package os.project;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

class ProcessScanner {
    public static ArrayList<Process> scanProcesses(int algorithmChoice) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Process> processes = new ArrayList<>();
        System.out.print("Enter the number of processes: ");
        int processCount = scanner.nextInt();

        for (int i = 0; i < processCount; i++) {
            System.out.print("Enter process name: ");
            String name = scanner.next();
            System.out.print("Enter arrival time: ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Enter burst time: ");
            int burstTime = scanner.nextInt();

            int priority = 0; 
            if (algorithmChoice == 4 || algorithmChoice == 5) { 
                System.out.print("Enter priority: ");
                priority = scanner.nextInt();
            }

            processes.add(new Process(name, arrivalTime, burstTime, priority));
        }
        return processes;
    }

    public static int scanTimeQuantum() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter time quantum: ");
        return scanner.nextInt();
    }
}
