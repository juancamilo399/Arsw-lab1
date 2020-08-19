package snakepackage;

public class EventNotifier {

    private Thread[] threads;
    public EventNotifier(Thread[] threads){
        this.threads=threads;
    }

    public void printThreadsStatus(){
        System.out.println("END OF GAME");
        System.out.println("THREADS STATUS:");
        for (int i = 0; i != threads.length; i++) {
            System.out.println("[" + (i+1) + "] :" + threads[i].getState());
        }
    }
}

