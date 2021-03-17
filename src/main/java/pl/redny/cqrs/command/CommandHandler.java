package pl.redny.cqrs.command;

import io.vavr.control.Try;
import pl.redny.cqrs.exception.CommandException;

public interface CommandHandler<T extends Command> {

    Try<Void> execute(T command) throws CommandException;

    boolean canHandle(Command command);

}
