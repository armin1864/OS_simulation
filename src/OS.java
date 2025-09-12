import java.io.*;
import java.util.*;

public class OS {

    static int n; // number of processes
    static int m; // number of resource types
    static int[] a; // available instances of each resource type
    static int ps; // page size
    static int pc; // page frame
    static List<Process> processes; // processes
    static int currentTime = 0;

    static PriorityQueue<Process> readyQ = new PriorityQueue<>(Comparator.comparingInt(p -> p.nextBurstTime()));
    static Queue<Process> ioQ = new LinkedList<>();
    static Queue<Process> waitingQ = new LinkedList<>();

    // produce output string and output lines count
    private static String output = "";
    private static int linesCount;

    public static void write(String s) {
        linesCount++;
        output += s + "\n";
    }

    private static void writeOutputs() {
        try (FileWriter writer = new FileWriter("stdout.txt", true)) {
            writer.write(linesCount + "\n");
            writer.write(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void readInputs() {
        try {
            File inputFile = new File("stdin.txt");
            Scanner reader = new Scanner(inputFile);
            n = reader.nextInt();
            m = reader.nextInt();
            if (m == 0) reader.nextInt();
            a = new int[m];
            for (int i = 0; i < m; i++) {
                a[i] = reader.nextInt();
            }
            ps = reader.nextInt();
            pc = reader.nextInt();
            processes = new ArrayList<>(n);
            int ic;
            int number = 0;
            for (int j = 0; j < n; j++) {
                ic = reader.nextInt();
                reader.nextLine();
                Process process = new Process(number, ic);
                for (int k = 0; k < ic; k++) {
                    process.addInstruction(reader.nextLine());
                }
                processes.add(process);
                number++;
            }

        } catch (FileNotFoundException e) {
            System.out.println("ERROR READING INPUT FILE");
        }
    }

    private static void checkIo() {
        Iterator<Process> ioIterator = ioQ.iterator(); // iterator for sending processes from ioQ to readyQ
        while (ioIterator.hasNext()) {
            Process p = ioIterator.next();
            if (p.ioEndTime <= currentTime) {
                readyQ.offer(p);
                ioIterator.remove();
            }
        }
    }

    private static void checkWaiting() {
        Iterator<Process> waitingIterator = waitingQ.iterator(); // iterator for sending processes from waitingQ to readyQ
        while (waitingIterator.hasNext()) {
            Process p = waitingIterator.next();
            int count = 0;  // if available resources are more,equal than requested count goes up by 1.
                            // if count == length means that in all indexes available is more,equal and we can allocate them to process
            for (int i = 0; i < m; i++) {
                if (a[i] >= p.request[i]) {
                    count++;
                }
            }
            if (count == m) {
                for(int j=0; j<m; j++){
                    p.request[j]=0;
                }
                readyQ.add(p);
                waitingIterator.remove();
            }
        }
    }

    private static void cpuSchedule() {
        readyQ.addAll(processes);
        while (!readyQ.isEmpty() || !ioQ.isEmpty()) {
            checkIo();
            if (!readyQ.isEmpty()) {
                Process current = readyQ.poll();
                while (!current.finished) {
                    Process.Instruction instruction = current.instructions[current.currentInstruction];

                    if (instruction.name.equals("Run")) {
                        int runTime = current.instructions[current.currentInstruction].T;
                        write("EXECUTE" + " " + current.number + " " + currentTime + " " + (currentTime + runTime));
                        currentTime += runTime;
                        current.nextInstruction();
                    }

                    else if (instruction.name.equals("Sleep")) {
                        int sleepTime = current.instructions[current.currentInstruction].T;
                        write("WAIT" + " " + current.number + " " + currentTime + " " + (currentTime + sleepTime));
                        current.ioEndTime = (currentTime + sleepTime);
                        ioQ.offer(current);
                        current.nextInstruction();
                        break;
                    }

                    else if (instruction.name.equals("Allocate")) {
                        if (Deadlock.deadlockDetection()) {
                            Deadlock.recovery();
                            System.out.println("recovery");
                        }
                        checkWaiting();
                        if (a[instruction.Y] >= instruction.X) {
                            current.allocation[instruction.Y] += instruction.X;
                            a[instruction.Y] -= instruction.X;
                            current.request[instruction.Y] = 0;
                            write("GIVE" + " " + current.number + " " + instruction.X + " " + instruction.Y + " " + currentTime);
                            current.nextInstruction();
                        }
                        else {
                            current.request[instruction.Y] = instruction.X;
                            waitingQ.offer(current);
                            System.out.println("blocked");
                            break;
                        }

                    }

                    else if (instruction.name.equals("Free")) {
                       if (current.allocation[instruction.Y] >= instruction.X) {
                            a[instruction.Y] += instruction.X;
                            current.allocation[instruction.Y] -= instruction.X;
                            write("TAKE" + " " + current.number + " " + instruction.X + " " + instruction.Y + " " + currentTime);

                        } else {
                            a[instruction.Y] += current.allocation[instruction.Y];
                            write("TAKE" + " " + current.number + " " + current.allocation[instruction.Y] + " " + instruction.Y + " " + currentTime);
                            current.allocation[instruction.Y] = 0;
                        }
                        checkWaiting();
                        current.nextInstruction();
                    }
                }
                if(current.finished){
                    for(int i=0; i<a.length; i++){
                        a[i] += current.allocation[i];
                        current.allocation[i]=0;
                    }
                    checkWaiting();
                }
            } else
                currentTime++;
        }
    }


    public static void main(String[] args) {
        readInputs();
        cpuSchedule();
        writeOutputs();
    }
}
