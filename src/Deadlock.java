public class Deadlock {

    static Process victim=null;

    static boolean deadlockDetection(){ // deadlock detection algorithm with matrix
        int[] work= OS.a.clone();
        boolean[] finish=new boolean[OS.processes.size()];
        for(int i=0; i<finish.length; i++){
            Process p =OS.processes.get(i);
            for(int j=0; j<OS.m; j++){
                if(p.allocation[i] > 0)
                    finish[i] = false;
                else
                    finish[i] = true;
            }
        }
        boolean progress =true;
        while(progress){
            progress=false;
            for(int i=0;i<OS.processes.size();i++){
                Process p=OS.processes.get(i);
                if(!finish[i]) {
                    if (canAllocate(p.request, work)) {
                        addResource(work, p.allocation);
                        finish[i] = true;
                        progress = true;
                    }
                }
            }
        }
        for(int j = 0; j <OS.processes.size(); j++){
            if(!finish[j]){
                victim = OS.processes.get(j); // this process is in deadlock cycle
                return true;
            }
        }
        return false;
    }

    static boolean canAllocate(int[] req, int[] work){
        for(int j = 0; j< OS.m; j++) {
            if (req[j] > work[j]) {
                System.out.println("false");
                return false;
            }
        }
        return true;
    }

    static void addResource(int[] w, int[] alloc){
        for(int j = 0; j< OS.m; j++)
            w[j]+=alloc[j];
    }

    static void recovery(){ // preempts allocated resources from the process which is first process that causes deadlock
        if(victim==null) return;
        for(int j=0;j<OS.m;j++){
            if(victim.allocation[j]>0){
                OS.write("TAKE"+" "+victim.number+" "+victim.allocation[j]+" "+j+" "+OS.currentTime);
                OS.a[j]+=victim.allocation[j];
                victim.allocation[j]=0;
            }
        }
        victim.rollback();
    }
}
