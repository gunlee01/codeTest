package gunlee.example.tobyvod.generic2;

import java.util.function.Consumer;

/**
 * 앞의 예제에서 기능을 하나 더 추가하는 것은 쉽다
 *
 *
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 1. 13.
 */
public class InterscetionType3 {
    interface DelegateTo<T> {
        T delegate();
    }

    interface Hello extends DelegateTo<String> {
        default void hello() {
            System.out.println("Hello " + delegate() );

        }
    }

    interface UpperCase extends DelegateTo<String> {
        default void upperCase() {
            System.out.println(delegate().toUpperCase());
        }
    }

    public static void main(String[] args) {
        run((DelegateTo<String> & Hello & UpperCase) () -> "Daniel Jung", o-> {
            o.hello();
            o.upperCase();
        });
    }

    private static <T extends DelegateTo<S>, S> void run(T t, Consumer<T> consumer) {
        consumer.accept(t);
    }
}
