package pl.novic.context.example;

@FunctionalInterface
interface GreeterFactory {
    Greeter make(UserId userId);
}
