package gunlee.example.tobyvod.generic2;

import java.util.function.Consumer;

/**
 * 실제 앞의 예제는 의미없다 그냥 static method 여러개 조합하는 것과 차이 없음
 *
 *
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 1. 13.
 */
public class InterscetionType2 {
    interface DelegateTo<T> {
        T delegate();
    }

    interface Hello extends DelegateTo<String> {
        default void hello() {
            System.out.println("Hello " + delegate() );

        }
    }

    public static void main(String[] args) {
        run((DelegateTo<String> & Hello) () -> "Daniel Jung", o-> {
            o.hello();
        });
    }

    private static <T extends DelegateTo<S>, S> void run(T t, Consumer<T> consumer) {
        consumer.accept(t);
    }
}
