# OS Scheduling Algorithms

This project implements several CPU scheduling algorithms used in operating systems to manage the execution of processes. The following algorithms are supported:

- **First-Come, First-Served (FCFS)**
- **Shortest Job First (SJF)**
- **Round Robin (RR)**
- **Priority Scheduling (Non-Preemptive)**
- **Priority Scheduling (Preemptive)**

## Features

- **FCFS**: The first process to arrive is executed first.
- **SJF**: The process with the shortest burst time is executed first.
- **Round Robin (RR)**: Processes are executed in a circular order with a fixed time quantum.
- **Priority Scheduling (Non-Preemptive)**: Processes are executed based on their priority, with the highest priority being executed first.
- **Priority Scheduling (Preemptive)**: Processes are preemptively executed based on priority, allowing for interruptions if a higher priority process arrives.

## Requirements

- **Java 8** or higher
- Any IDE supporting Java (e.g., IntelliJ IDEA, Eclipse)

## How to Run

1. Clone this repository to your local machine:
   ```bash
   git clone https://github.com/am94an/os-scheduling-algorithms.git
