package springcamp2017.session.asyncmonitoring.gunlee.threadlocal;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 4. 16.
 */
public class MyNameHolder {
    private static SimpleThreadLocal threadLocal = new SimpleThreadLocal();

    public static void setMyName(String name) {
        threadLocal.set(name);
    }

    public static String getMyName() {
        return (String)threadLocal.get();
    }
}
