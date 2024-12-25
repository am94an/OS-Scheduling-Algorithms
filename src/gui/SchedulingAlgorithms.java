package gui;

import java.util.*;
import os.project.OutputPrinter;

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

        System.out.println("\nSJF Scheduling Execution:");

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                readyQueue.add(processes.remove(0));
            }

            if (!readyQueue.isEmpty()) {
                readyQueue.sort(Comparator.comparingInt(p -> p.burstTime));
                Process process = readyQueue.remove(0);
                process.waitingTime = currentTime - process.arrivalTime;
                currentTime += process.burstTime;
                process.turnaroundTime = process.waitingTime + process.burstTime;
                completedProcesses.add(process);

                System.out.println("Process " + process.name + " executed from " 
                                   + (currentTime - process.burstTime) + " to " + currentTime 
                                   + ", Waiting Time: " + process.waitingTime 
                                   + ", Turnaround Time: " + process.turnaroundTime);
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
        executionTimeline.add(String.format("Time %d-%d: Process %s executed", 
                                             currentTime, currentTime + executionTime, process.name));
        currentTime += executionTime;

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

    double avgTurnaroundTime = totalTurnaroundTime / completedProcesses.size();
    double avgWaitingTime = totalWaitingTime / completedProcesses.size();

    OutputPrinter.printAverageTimes(avgTurnaroundTime, avgWaitingTime);

    System.out.println("-------------------------------------------------------------------");
    System.out.println("| Name | Arrival Time | Burst Time | Waiting Time | Turnaround Time |");
    System.out.println("-------------------------------------------------------------------");
    for (Process p : completedProcesses) {
        System.out.printf("| %-4s | %-12d | %-10d | %-12d | %-15d |%n", 
                          p.name, p.arrivalTime, p.burstTime, p.waitingTime, p.turnaroundTime);
    }
    System.out.println("-------------------------------------------------------------------");

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

    return completedProcesses;
}


// Priority Scheduling (Preemptive)
    public static ArrayList<Process> executePriorityPreemptive(ArrayList<Process> processes) {
        processes.sort(Comparator.comparingInt(p -> p.priority));

        ArrayList<Process> completedProcesses = new ArrayList<>();
        int currentTime = 0;

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(Comparator.comparingInt(p -> p.priority));

        System.out.println("\nPriority Preemptive Scheduling Execution:");

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            while (!processes.isEmpty() && processes.get(0).arrivalTime <= currentTime) {
                readyQueue.add(processes.remove(0));
            }

            if (!readyQueue.isEmpty()) {
                Process currentProcess = readyQueue.poll();
                int executionTime = currentProcess.burstTime;
                currentProcess.turnaroundTime = currentTime + executionTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;
                completedProcesses.add(currentProcess);
                currentTime += executionTime;

                System.out.println("Process " + currentProcess.name + " executed from " 
                                   + (currentTime - executionTime) + " to " + currentTime 
                                   + ", Waiting Time: " + currentProcess.waitingTime 
                                   + ", Turnaround Time: " + currentProcess.turnaroundTime);
            } else {
                currentTime++;
            }
        }

        return completedProcesses;
    }
}
