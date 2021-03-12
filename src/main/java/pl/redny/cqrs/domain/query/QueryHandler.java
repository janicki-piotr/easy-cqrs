package pl.redny.cqrs.domain.query;


import io.vavr.control.Try;

public interface QueryHandler<T extends Query, U> {

    Try<U> execute(T query);
    
    boolean canHandle(Query query);

}
