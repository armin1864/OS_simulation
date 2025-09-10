public class Process {

    class instruction{
        String name;
        int T; // Time ms
        int X; // number of instances
        int Y; // resource type
        int A; // logical address
        instruction(String s){
            name = s;
            T = X = Y = A = -1;
        }
    }
    int number;
    int ic;
    instruction[] instructions;
    int ioEndTime;
    int currentInstruction =0;
    boolean finished=false;
    int[] request;
    int[] allocation;
    public Process(int num,int n){
        number = num;
        ic = n;
        instructions = new instruction[ic];
    }

    private int Index =0;
    public void addInstruction(String instruction){
        if(Index <ic) {
            String name = instruction.split("\\s+")[0];
            instruction ins = new instruction(name);
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

    public void printInstructions(){
        for(instruction i : instructions){
            System.out.println(i.name + i.T + i.X + i.Y + i.A);
        }
    }

    public int totalRunTime(){
        int totalRun = 0;
        for(instruction instruction : instructions ){
            if(instruction.name.equals("Run"))
                totalRun += instruction.T;
        }
        return totalRun;
    }

    public void nextInstruction(){
        currentInstruction++;
        if (currentInstruction < instructions.length-1)
            finished = true;
    }

    public String getNextInstruction(){
        if (currentInstruction < instructions.length-1)
            return instructions[currentInstruction+1].name;
        return "empty";
    }

    public boolean isEnded(){
        return currentInstruction >= instructions.length;
    }

    public int totalAllocation(){
        int total =0;
        for(int a :allocation)
            total += a;
        return total;
    }

    public void rollback(){
        currentInstruction = 0;
        finished = false;
    }
}
