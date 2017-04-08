package pl.novic.context;

import static lombok.AccessLevel.PRIVATE;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import javax.inject.Provider;

@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class InContextBeanFactory<B, C> {

    Provider<B> beanProvider;
    ContextEntry<C> contextEntry;

    public B make(C c) {
        return contextEntry.enter(c, () -> {
            B bean = beanProvider.get();
            Class<?> beanClass = bean.getClass();

            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(beanClass);
            enhancer.setCallback((MethodInterceptor) (o, method, args, methodProxy) ->
                    contextEntry.enter(c, () -> method.invoke(bean, args)));

            Class<?>[] argumentTypes = beanClass.getConstructors()[0].getParameterTypes();

            return (B)enhancer.create(argumentTypes, new Object[argumentTypes.length]);
        });
    }
}
