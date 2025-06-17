import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class OS {
    static int n; // number of processes
    static int m; // number of resource types
    static int[] a; // available instances of each resource type
    static int ps; // page size
    static int pc; // page frame
    static Process[] processes; // processes

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
            processes = new Process[n];
            int ic;
            for(int j=0; j<n; j++){
                ic = reader.nextInt();
                reader.nextLine();
                Process process = new Process(ic);
                for(int k=0; k<ic; k++){
                    process.addInstruction(reader.nextLine());
                }
                processes[j] = process;
            }

        } catch (FileNotFoundException e){
            System.out.println("ERROR READING INPUT FILE");
        }
    }

    public static void main(String [] args){
        readInputs();
    }
}
