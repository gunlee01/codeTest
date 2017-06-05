package springcamp2017.session.asyncmonitoring.gunlee;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 4. 17.
 */


public class LambdaEx1Ex2 {
    void doIt(int idx) {

        Runnable r = () -> System.out.println(idx);

        r.run();
    }

    public static void main(String[] args) {
        new LambdaEx1Ex2().doIt(100);
    }

    class LambdaEx1Ex2$$Lambda$1 {

    }
}
