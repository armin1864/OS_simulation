public class Process {

    class Instruction {
        String name;
        int T; // Time ms
        int X; // number of instances
        int Y; // resource type
        int A; // logical address
        Instruction(String s){
            name = s;
            T = X = Y = A = -1;
        }
    }
    int number;
    int ic;
    Instruction[] instructions;
    int ioEndTime;
    int currentInstruction =0;
    boolean finished=false;
    boolean blocked=false;
    int[] request = new int[OS.m];
    int[] allocation = new int[OS.m];
    public Process(int num,int n){
        number = num;
        ic = n;
        instructions = new Instruction[ic];
    }

    private int Index =0;
    public void addInstruction(String instruction){
        if(Index <ic) {
            String name = instruction.split("\\s+")[0];
            Instruction ins = new Instruction(name);
            switch (name) {
                case "Run", "Sleep" -> ins.T = Integer.parseInt(instruction.split("\\s+")[1]);
                case "Allocate", "Free" -> {
                    ins.X = Integer.parseInt(instruction.split("\\s+")[1]);
                    ins.Y = Integer.parseInt(instruction.split("\\s+")[2]);
                }
                case "Read", "Write" -> ins.A = Integer.parseInt(instruction.split("\\s+")[1]);
            }
            instructions[Index] = ins;
            Index++;
        } else System.out.println("ERROR: instructions numbers are finished");
    }

    public void nextInstruction(){
        currentInstruction++;
        if (currentInstruction >= instructions.length)
            finished = true;
    }

    public int nextBurstTime() {
        if (finished || currentInstruction >= instructions.length) {
            return Integer.MAX_VALUE;
        }
        Instruction nextInstruction = instructions[currentInstruction];
        if (nextInstruction.name.equals("Run")) {
            return nextInstruction.T;
        }
        return 0;
    }

    public void rollback(){
        currentInstruction = 0;
        finished = false;
        ioEndTime = 0;
    }
}
