public class Deadlock {

    static Process victim=null;

    public static boolean deadlockDetection(){ // deadlock detection algorithm with matrix
        int[] work= OS.a.clone();
        boolean[] finish=new boolean[OS.processes.size()];
        for(int i=0; i<finish.length; i++){
            Process p =OS.processes.get(i);
            for(int j=0; j<OS.m; j++){
                finish[i] = p.isFinished();
            }
        }
        boolean progress = true;
        while(progress){
            progress=false;
            for(int i=0;i<OS.processes.size();i++){
                Process p=OS.processes.get(i);
                if(!finish[i]) {
                    if (canAllocate(p.request, work)) {
                        addResource(p.allocation, work);
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

    static boolean canAllocate(int[] request, int[] work){
        for(int j = 0; j< OS.m; j++) {
            if (request[j] > work[j]) {
                return false;
            }
        }
        return true;
    }

    static void addResource(int[] allocation, int[] work){
        for(int j = 0; j< OS.m; j++)
            work[j]+=allocation[j];
    }

    public static void recoverykill(){ // preempts allocated resources from the process which is first process that causes deadlock
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

    public static void recoveryPreempt(){ // preempts allocated resources from the process which is first process that causes deadlock
        if(victim==null) return;
        for(int j=0;j<OS.m;j++){
            if(victim.allocation[j]>0){
                OS.write("TAKE"+" "+victim.number+" "+victim.allocation[j]+" "+j+" "+OS.currentTime);
                OS.a[j]+=victim.allocation[j];
                victim.request[j] += victim.allocation[j];
                victim.allocation[j]=0;
            }
        }
        if(OS.readyQ.contains(victim) || OS.ioQ.contains(victim)) {
            OS.waitingQ.offer(victim);
            OS.ioQ.remove(victim);
            OS.readyQ.remove(victim);
        }
    }
}
