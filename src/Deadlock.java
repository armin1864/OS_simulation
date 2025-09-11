public class Deadlock {

    static Process victim=null;

    static boolean deadlockDetection(){
        int[] work= OS.a.clone();
        boolean[] finish=new boolean[OS.processes.size()];
        boolean progress =true;
        while(progress){
            progress=false;
            for(int i=0;i<OS.processes.size();i++){
                Process p=OS.processes.get(i);
                if(p.finished||finish[i]) continue;
                if(canAllocate(p.request,work)){
                    addRes(work,p.allocation);
                    finish[i]=true;
                    progress=true;
                }
            }
        }
        for(int i=0;i<OS.processes.size();i++){
            if(!OS.processes.get(i).finished && !finish[i]){
                victim = OS.processes.get(i);  // this process is in deadlock cycle
                return true;
            }
        }
        return false;
    }

    static boolean canAllocate(int[] req, int[] work){
        for(int j = 0; j< OS.m; j++)
            if(req[j]>work[j]) return false;
        return true;
    }

    static void addRes(int[] w, int[] alloc){
        for(int j = 0; j< OS.m; j++)
            w[j]+=alloc[j];
    }

    static void recovery(){
        /*
        // preempt resources from process that has most allocated resources
        int max =0;
        for(Process p:OS.processes){
            if(p.finished) continue;
            if(p.totalAllocation()> max){
                max =p.totalAllocation();
                victim=p;
            }
        } */
        if(victim==null) return;
        for(int j=0;j<OS.m;j++){
            if(victim.allocation[j]>0){
                System.out.println("TAKE"+ victim.number + victim.allocation[j] + j + OS.currentTime); //TODO: write in stdout
                OS.a[j]+=victim.allocation[j];
                victim.allocation[j]=0;
            }
        }
        victim.rollback();
    }
}
