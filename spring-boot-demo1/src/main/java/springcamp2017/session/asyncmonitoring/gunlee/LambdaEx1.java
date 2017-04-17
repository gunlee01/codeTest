package springcamp2017.session.asyncmonitoring.gunlee;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 4. 17.
 */


public class LambdaEx1 {
    void doIt(int idx) {
        Runnable lambdaRunnable = () -> {
            System.out.println(idx + ":lambda-this:" + this);
        };

        Runnable innerClassRunnable = new Runnable() {
            public void run() {
                System.out.println(idx + ":innerclass-this:" + this);
            }
        };

        System.out.println(idx + ":this:" + this);
        lambdaRunnable.run();
        innerClassRunnable.run();
    }


    public static void main(String[] args) {
        new LambdaEx1().doIt(100);
    }
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