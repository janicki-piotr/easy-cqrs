package pl.redny.cqrs.domain.command;

import io.vavr.control.Try;
import pl.redny.cqrs.exception.CommandException;

public interface CommandDispatcher {

    Try<Void> dispatchCommand(final Command command) throws CommandException;

}
