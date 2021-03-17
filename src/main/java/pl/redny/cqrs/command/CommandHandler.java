package pl.redny.cqrs.command;

import pl.redny.cqrs.exception.CommandException;

public interface CommandHandler<T extends Command> {

    void execute(T command) throws CommandException;

    boolean canHandle(Command command);

}
