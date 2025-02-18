
package os.project;

import java.util.*;

public class SchedulingAlgorithms {

    // FCFS (First-Come, First-Served) Scheduling
    public static ArrayList<Process> executeFCFS(ArrayList<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        int currentTime = 0;
        ArrayList<Process> completedProcesses = new ArrayList<>();
        
        for (Process process : processes) {
            currentTime = Math.max(currentTime, process.arrivalTime);
            process.waitingTime = currentTime - process.arrivalTime;
            currentTime += process.burstTime;
            process.turnaroundTime = process.waitingTime + process.burstTime;
            completedProcesses.add(process);
        }
        
        return completedProcesses;
    }

    // SJF (Shortest Job First) Scheduling
    public static ArrayList<Process> executeSJF(ArrayList<Process> processes) {
        ArrayList<Process> completedProcesses = new ArrayList<>();
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
        ArrayList<Process> readyQueue = new ArrayList<>();
        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                readyQueue.add(processes.remove(0));
            }

            if (!readyQueue.isEmpty()) {
                readyQueue.sort(Comparator.comparingInt(p -> p.burstTime));
                Process process = readyQueue.remove(0);
                process.waitingTime = currentTime - process.arrivalTime;
                totalWaitingTime += process.waitingTime;
                currentTime += process.burstTime;
                process.turnaroundTime = process.waitingTime + process.burstTime;
                totalTurnaroundTime += process.turnaroundTime;
                completedProcesses.add(process);
            } else {
                currentTime++;
            }
        }
                
        return completedProcesses;
    }

    // Round Robin Scheduling
public static ArrayList<Process> executeRoundRobin(ArrayList<Process> processes, int timeQuantum) {
    Queue<Process> processQueue = new ArrayDeque<>();
    ArrayList<Process> completedProcesses = new ArrayList<>();
    ArrayList<String> executionTimeline = new ArrayList<>();
    int currentTime = 0;

    processes.sort(Comparator.comparingInt(p -> p.arrivalTime));
    int nextProcessIndex = 0;

    while (!processQueue.isEmpty() || nextProcessIndex < processes.size()) {
        while (nextProcessIndex < processes.size() && processes.get(nextProcessIndex).arrivalTime <= currentTime) {
            processQueue.add(processes.get(nextProcessIndex));
            nextProcessIndex++;
        }

        if (processQueue.isEmpty()) {
            currentTime = processes.get(nextProcessIndex).arrivalTime;
            continue;
        }

        Process process = processQueue.poll();

        if (process.getStartTime() == -1) {
            process.setStartTime(currentTime);
        }

        int executionTime = Math.min(timeQuantum, process.getRemainingTime());
        process.setRemainingTime(process.getRemainingTime() - executionTime);
        currentTime += executionTime;

        
        executionTimeline.add(String.format("Time %d-%d: Process %s executed", 
                                             currentTime - executionTime, currentTime, process.name));

        if (process.getRemainingTime() == 0) {
            process.turnaroundTime = currentTime - process.arrivalTime;
            process.waitingTime = process.turnaroundTime - process.burstTime;
            completedProcesses.add(process);
        } else {
            processQueue.add(process);
        }
    }

    System.out.println("\nExecution Timeline:");
    System.out.println("-------------------------");
    for (String step : executionTimeline) {
        System.out.println(step);
    }
    System.out.println("-------------------------");

    double totalTurnaroundTime = 0, totalWaitingTime = 0;
    for (Process p : completedProcesses) {
        totalTurnaroundTime += p.turnaroundTime;
        totalWaitingTime += p.waitingTime;
    }


    return completedProcesses;
}

    // Priority Scheduling (Non-Preemptive)
public static ArrayList<Process> executePriorityNonPreemptive(ArrayList<Process> processes) {
    processes.sort(Comparator.comparingInt((Process p) -> p.arrivalTime)
                             .thenComparingInt(p -> p.priority));

    int currentTime = 0;
    ArrayList<Process> completedProcesses = new ArrayList<>();

    while (!processes.isEmpty()) {
        Process nextProcess = null;
        for (Process p : processes) {
            if (p.arrivalTime <= currentTime) {
                if (nextProcess == null || p.priority < nextProcess.priority) {
                    nextProcess = p;
                }
            }
        }
        if (nextProcess == null) {
            currentTime++;
            continue;
        }

        processes.remove(nextProcess);

        if (currentTime < nextProcess.arrivalTime) {
            currentTime = nextProcess.arrivalTime;
        }

        nextProcess.startTime = currentTime;
        currentTime += nextProcess.burstTime;

        nextProcess.turnaroundTime = currentTime - nextProcess.arrivalTime;
        nextProcess.waitingTime = nextProcess.turnaroundTime - nextProcess.burstTime;
        completedProcesses.add(nextProcess);

        System.out.println("Process " + nextProcess.name + " executed from " 
                           + nextProcess.startTime + " to " + currentTime 
                           + ", Waiting Time: " + nextProcess.waitingTime 
                           + ", Turnaround Time: " + nextProcess.turnaroundTime);
    }


    double totalTurnaroundTime = 0, totalWaitingTime = 0;
    for (Process p : completedProcesses) {
        totalTurnaroundTime += p.turnaroundTime;
        totalWaitingTime += p.waitingTime;
    }

    double avgTurnaroundTime = totalTurnaroundTime / completedProcesses.size();
    double avgWaitingTime = totalWaitingTime / completedProcesses.size();


    return completedProcesses;
}
}
