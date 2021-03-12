package pl.redny.cqrs.domain.command;

import pl.redny.cqrs.exception.CommandException;

public interface CommandHandler<T extends Command> {

    void execute(T command) throws CommandException;

    boolean canHandle(Command command);

}
