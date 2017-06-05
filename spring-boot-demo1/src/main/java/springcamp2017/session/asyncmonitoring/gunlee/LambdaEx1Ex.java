package springcamp2017.session.asyncmonitoring.gunlee;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 4. 17.
 */


public class LambdaEx1Ex {
    void doIt(int idx) {
        Runnable r = () -> System.out.println(idx);
    }

    //private static synthetic lambda$doIt$0(I)V
//    private static void lambda$doIt$0(int paramIdx) {
//        System.out.println(paramIdx);
//    }

}





    // access flags 0x100A
//    private static synthetic lambda$doIt$0(I)V
//   L0
//           LINENUMBER 10 L0
//           GETSTATIC java/lang/System.out : Ljava/io/PrintStream;
//           ILOAD 0
//           INVOKEVIRTUAL java/io/PrintStream.println (I)V
//           RETURN
//           MAXSTACK = 2
//           MAXLOCALS = 1
//           }