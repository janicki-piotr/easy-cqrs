package pl.redny.cqrs.query;

import pl.redny.cqrs.Processor;
import pl.redny.cqrs.exception.QueryException;

public interface QueryProcessor extends Processor {

    void process(Object queryHandler, Object query) throws QueryException;

}
