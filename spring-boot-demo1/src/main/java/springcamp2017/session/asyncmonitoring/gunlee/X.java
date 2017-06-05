package springcamp2017.session.asyncmonitoring.gunlee;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 4. 17.
 */


public class X {
    void doIt(int idx) {
        Runnable r = () -> System.out.println(idx);
        r.run();
    }

    //private static synthetic lambda$doIt$0(I)V
//    private static void lambda$doIt$0(int paramIdx) {
//        System.out.println(paramIdx);
//    }
//
//    public static void main(String[] args) {
//        new X().doIt(100);
//    }
//
//    static class X$$Lambda$1 implements Runnable {
//        private final int arg$1;
//
//        private X$$Lambda$1(int arg$1) {
//            this.arg$1 = arg$1;
//        }
//
//        private static Runnable get$Lambda(int arg$1) {
//            return new X$$Lambda$1(arg$1);
//        }
//
//        @Override
//        public void run() {
//            lambda$doIt$0(arg$1);
//        }
//    }
}
