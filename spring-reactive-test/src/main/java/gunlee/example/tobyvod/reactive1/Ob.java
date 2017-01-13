package gunlee.example.tobyvod.reactive1;

import java.util.Observable;
import java.util.Observer;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 1. 12.
 */
public class Ob {
    public static void main(String[] args) {

        Observer ob = new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(arg);
            }
        };

        IntObservable io = new IntObservable();
        io.addObserver(ob);

        io.run();
    }

    static class IntObservable extends Observable implements Runnable {
        @Override
        public void run() {
            for(int i=1; i<=10; i ++) {
                setChanged();
                notifyObservers(i);
            }
        }
    }
}
