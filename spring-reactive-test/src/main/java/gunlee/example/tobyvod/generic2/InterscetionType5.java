package gunlee.example.tobyvod.generic2;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 앞의 예제에서 기능을 하나 더 추가하는 것은 쉽다
 *
 *
 * @author Gun Lee (gunlee01@gmail.com) on 2017. 1. 13.
 */
public class InterscetionType5 {
    interface Pair<T> {
        T getFirst();
        T getSecond();

        void setFirst(T first);
        void setSecond(T second);
    }

    interface DelegateTo<T> {
        T delegate();
    }

    static class Name implements Pair<String> {
        String firstName;
        String lastName;

        public Name(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        @Override
        public String getFirst() {
            return this.firstName;
        }

        @Override
        public String getSecond() {
            return this.lastName;
        }

        @Override
        public void setFirst(String first) {
            this.firstName = first;
        }

        @Override
        public void setSecond(String second) {
            this.lastName = lastName;
        }
    }

    interface ForwardingPair<T> extends DelegateTo<Pair<T>>, Pair<T> {
        @Override
        default T getFirst() { return delegate().getFirst(); };
        @Override
        default T getSecond() { return delegate().getSecond(); };
        @Override
        default void setFirst(T first) { delegate().setFirst(first);};
        @Override
        default void setSecond(T second) { delegate().setSecond(second);};
    }

    private static <T extends DelegateTo<S>, S> void run(T t, Consumer<T> consumer) {
        consumer.accept(t);
    }

    interface Convertable<T> extends DelegateTo<Pair<T>> {
        default void convert(Function<T, T> mapper) {
            Pair<T> pair = delegate();
            pair.setFirst(mapper.apply(pair.getFirst()));
            pair.setSecond(mapper.apply(pair.getSecond()));
        }
    }

    public static void main(String[] args) {
        Pair<String> name = new Name("Toby", "Lee");
        run((ForwardingPair<String> & Convertable<String>)()->name, o-> {
            o.convert(s->s.toUpperCase());
            System.out.println(o.getFirst());
            System.out.println(o.getSecond());
        });
    }
}
