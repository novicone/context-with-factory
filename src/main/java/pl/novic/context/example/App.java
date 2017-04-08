package pl.novic.context.example;

import static java.util.UUID.randomUUID;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import pl.novic.context.ContextEntry;
import pl.novic.context.InContextBeanFactory;

import javax.inject.Provider;

@Slf4j
public class App {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        GreeterFactory greeterFactory = context.getBean(GreeterFactory.class);

        Greeter greeter = greeterFactory.make(new UserId(randomUUID()));
        System.out.println(greeter.greeting());
    }

    @Import(Greeter.class)
    static class Config {

        @Bean
        ContextEntry<UserId> userIdContextHolder() {
            return new ContextEntry<>("userId", id -> id.getValue().toString());
        }

        @Bean
        @Scope(SCOPE_PROTOTYPE)
        UserId userId(ContextEntry<UserId> contextEntry) {
            return contextEntry.value();
        }

        @Bean
        GreeterFactory greeterFactory(Provider<Greeter> greeterProvider, ContextEntry<UserId> contextEntry) {
            return new InContextBeanFactory<>(greeterProvider, contextEntry)::make;
        }

    }
}
