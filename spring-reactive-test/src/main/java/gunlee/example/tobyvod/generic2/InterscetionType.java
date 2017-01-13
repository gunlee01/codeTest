package gunlee.example.tobyvod.generic2;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 1. 13.
 */
public class InterscetionType {
    interface Hello {
        default void hello() {
            System.out.println("Hello");

        }
    }

    interface Hi {
        default void hi() {
            System.out.println("Hi");
        }
    }

    //4
    interface Printer {
        default void print(String s) {
            System.out.println(s);
        }
    }

    public static void main(String[] args) {
        //ex1
        hello((Function & Hello & Hi)s->s);

        //ex2 - 계속 함수의 generic 파람에서 계속 람다에서 선언한 type을 추가해야 되니까 의미 없다
        hello2((Function & Hello & Hi)s->s);

        //ex3 - callback stryle intersection type
        run((Function & Hello & Hi)s->s, o -> {
            o.hello();
            o.hi();
        });

        //ex4 - callback stryle intersection type
        //이렇게 추가가 가능하지만..
        //의미 없다 연관성 없는 것들을 계속 조합해봐야 static method 여러개 부르는 것과 별반 다를게 없다
        //이렇게 사용해서는 소용이 없으며 deligation 방식의 사용을 보도록 하자
        run((Function & Hello & Hi & Printer)s->s, o -> {
            o.hello();
            o.hi();
            o.print("Lambda");
        });
    }

    private static <T extends Function> void run(T t, Consumer<T> consumer) {
        consumer.accept(t);
    }

    public static void hello(Function function) {
        ((Hello)function).hello();
        ((Hi)function).hi();
    }

    public static <T extends Function & Hello & Hi> void hello2(T t) {
        System.out.println(t.apply(1));
        t.hello();
        t.hi();
    }
}
