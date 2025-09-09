import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class OS {
    static int n; // number of processes
    static int m; // number of resource types
    static int[] a; // available instances of each resource type
    static int ps; // page size
    static int pc; // page frame
    static List<Process> processes; // processes

    public static void readInputs(){
        try {
            File inputFile = new File("stdin.txt");
            Scanner reader = new Scanner(inputFile);
            n = reader.nextInt();
            m = reader.nextInt();
            if(m==0) reader.nextInt();
            a = new int[m];
            for(int i=0; i<m; i++){
                a[i] = reader.nextInt();
            }
            ps = reader.nextInt();
            pc = reader.nextInt();
            processes = new ArrayList<>(n);
            int ic;
            int number =0;
            for(int j=0; j<n; j++){
                ic = reader.nextInt();
                reader.nextLine();
                Process process = new Process(number, ic);
                for(int k=0; k<ic; k++){
                    process.addInstruction(reader.nextLine());
                }
                processes.add(process);
                number++;
            }

        } catch (FileNotFoundException e){
            System.out.println("ERROR READING INPUT FILE");
        }
    }



    public static void cpuSchedule(){

        int currentTime=0;
        processes.sort(Comparator.comparingInt(p -> p.totalRunTime()));
        Queue<Process> readyQ = new LinkedList<>(processes);
        Queue<Process> ioQ = new LinkedList<>();

        while (!readyQ.isEmpty() || !ioQ.isEmpty()){

            Iterator<Process> ioIterator = ioQ.iterator(); // iterator for sending processes from ioQ to readyQ
            while (ioIterator.hasNext()){
                Process p = ioIterator.next();
                if(p.ioEndTime <= currentTime){
                    readyQ.offer(p);
                    ioIterator.remove();
                }
            }

            if(!readyQ.isEmpty()) {
                Process current = readyQ.poll();
                while (!current.isEnded()) {
                    String instruction = current.instructions[current.currentInstruction].name;

                    if (instruction.equals("Run")) {
                        int runTime = current.instructions[current.currentInstruction].T;
                        System.out.println("EXECUTE" + " " + current.number + " " + currentTime + " " + (currentTime + runTime)); // TODO: write in stdout
                        currentTime += runTime;
                        if(current.getNextInstruction().equals("Run")) {
                            readyQ.offer(current);
                        }
                    }
                    else if (instruction.equals("Sleep")) {
                        int sleepTime = current.instructions[current.currentInstruction].T;
                        System.out.println("WAIT" + " " + current.number + " " + currentTime + " " + (currentTime + sleepTime)); // TODO: write in stdout
                        current.ioEndTime += (currentTime + sleepTime);
                        ioQ.offer(current);
                        current.nextInstruction();
                        break;
                    }
                    //TODO: Add other instructions


                    current.nextInstruction();
                }
            } else
                currentTime++;
        }
    }


    public static void main(String [] args){
        readInputs();
        cpuSchedule();
    }
}
