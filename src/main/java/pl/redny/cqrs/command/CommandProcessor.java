package pl.redny.cqrs.command;

import pl.redny.cqrs.Processor;
import pl.redny.cqrs.exception.CommandException;

public interface CommandProcessor  extends Processor {

    <T extends CommandHandler<?>> void process(T commandHandler, Object command) throws CommandException;

}
