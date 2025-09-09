import java.util.List;

public class Deadlock {

    static int resType = OS.m;
    static List<Process> processes = OS.processes;
    static int[] available = OS.a;

    static boolean deadlockDetection(){
        int[] work= available.clone();
        boolean[] finish=new boolean[processes.size()];
        boolean progress =true;
        while(progress){
            progress=false;
            for(int i=0;i<processes.size();i++){
                Process p=processes.get(i);
                if(p.finished||finish[i]) continue;
                if(canAllocate(p.request,work)){
                    addRes(work,p.allocation);
                    finish[i]=true;
                    progress=true;
                }
            }
        }
        for(int i=0;i<processes.size();i++)
            if(!processes.get(i).finished && !finish[i])
                return true;
        return false;
    }

    static boolean canAllocate(int[] req, int[] work){
        for(int j = 0; j< resType; j++)
            if(req[j]>work[j]) return false;
        return true;
    }

    static void addRes(int[] w, int[] alloc){
        for(int j = 0; j< resType; j++)
            w[j]+=alloc[j];
    }
}
