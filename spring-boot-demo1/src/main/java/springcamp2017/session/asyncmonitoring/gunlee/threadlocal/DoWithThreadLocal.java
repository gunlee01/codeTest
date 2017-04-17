package springcamp2017.session.asyncmonitoring.gunlee.threadlocal;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 4. 16.
 */
public class DoWithThreadLocal {

    public static void main(String[] args) {
        new DoWithThreadLocal().startWork();
        //...
        AnotherBiz anotherBiz = new AnotherBiz();
        anotherBiz.doAnother();
    }

    void startWork() {
        MyNameHolder.setMyName("gunLee");
    }

    static class AnotherBiz {
        public void doAnother() {
            System.out.println("[Another Biz] " + MyNameHolder.getMyName());
        }
    }
}
