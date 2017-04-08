package pl.novic.context;

import static lombok.AccessLevel.PRIVATE;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.slf4j.MDC;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;

@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ContextEntry<C> {
    ContextEntry(String key) {
        this(key, Object::toString);
    }

    ThreadLocal<C> threadLocal = new ThreadLocal<C>();
    String key;
    Function<C, String> stringFunction;

    @SneakyThrows
    <R> R enter(C value, Callable<R> callable) {
        try {
            threadLocal.set(value);
            MDC.put(key, stringFunction.apply(value));
            return callable.call();
        } finally {
            MDC.remove(key);
            threadLocal.remove();
        }
    }

    public C value() {
        return Optional.ofNullable(threadLocal.get())
                .orElseThrow(IllegalStateException::new);
    }

}
