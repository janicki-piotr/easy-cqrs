package pl.redny.cqrs.domain.query;

import pl.redny.cqrs.domain.Processor;
import pl.redny.cqrs.exception.QueryException;

public interface QueryProcessor extends Processor {

    void process(Object queryHandler, Object query) throws QueryException;

}
