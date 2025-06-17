public class Process {
    private int ic;
    private String[] instructions;
    private int i=0;
    public Process(int n){
        ic = n;
        instructions = new String[ic];
    }

    public int getIc(){
        return ic;
    }

    public String[] getInstructions(){
        return instructions;
    }

    public void printInstructions(){
        for(String s : instructions) System.out.println(s);
    }

    public void addInstruction(String instruction){
        if(i<instructions.length) {
            instructions[i] = instruction;
            i++;
        } else System.out.println("ERROR: instructions numbers are finished");
    }
}
