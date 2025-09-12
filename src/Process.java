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

    public boolean isFinished(){
        return finished;
    }
    public int id(){
        return number;
    }

    public int nextBurstTime() {
        if (finished) {
            return Integer.MAX_VALUE;
        }
        if (instructions[currentInstruction].name.equals("Run")) {
            return instructions[currentInstruction].T;
        }
        for (int i = currentInstruction; i < instructions.length; i++) {
            if (instructions[i].name.equals("Run")) {
                return instructions[i].T;
            }
        }
        return 0;
    }

    public void rollback(){
        currentInstruction = 0;
        finished = false;
        ioEndTime = 0;
        for(int i=0; i<request.length; i++){
            request[i]=0;
            allocation[i]=0;
        }
    }

}
