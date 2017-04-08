package pl.novic.context.example;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

import javax.inject.Inject;
import javax.inject.Named;

@Named
@Slf4j
@Scope(SCOPE_PROTOTYPE)
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class Greeter {
    UserId userId;

    public String greeting() {
        log.info("Generating a greeting");
        return format("Hello, user with id %s", userId.getValue());
    }
}
