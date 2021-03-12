package pl.redny.cqrs.domain.command;

import pl.redny.cqrs.domain.Processor;
import pl.redny.cqrs.exception.CommandException;

public interface CommandProcessor  extends Processor {

    <T extends CommandHandler<?>> void process(T commandHandler, Object command) throws CommandException;

}
