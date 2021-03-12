package pl.redny.cqrs.domain.query;

import io.vavr.control.Try;

public interface QueryDispatcher {

    <T> Try<T> dispatchQuery(final Query query);

}
