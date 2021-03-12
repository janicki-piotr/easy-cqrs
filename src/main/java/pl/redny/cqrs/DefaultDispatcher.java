package pl.redny.cqrs;

import io.vavr.control.Try;
import pl.redny.cqrs.command.Command;
import pl.redny.cqrs.command.CommandDispatcher;
import pl.redny.cqrs.command.CommandHandler;
import pl.redny.cqrs.command.CommandProcessor;
import pl.redny.cqrs.query.Query;
import pl.redny.cqrs.query.QueryDispatcher;
import pl.redny.cqrs.query.QueryHandler;
import pl.redny.cqrs.query.QueryProcessor;
import pl.redny.cqrs.exception.CommandException;
import pl.redny.cqrs.exception.QueryException;

import java.util.ArrayList;
import java.util.List;

public class DefaultDispatcher implements CommandDispatcher, QueryDispatcher {

    private static final CommandHandler<Command> DEFAULT_COMMAND_HANDLER = new CommandHandler<Command>() {
        @Override
        public void execute(final Command command) throws CommandException {
            throw new CommandException("No command handler implementations for " + command);
        }

        @Override
        public boolean canHandle(final Command command) {
            return false;
        }
    };

    private static final QueryHandler<Query, Object> DEFAULT_QUERY_HANDLER = new QueryHandler<Query, Object>() {
        @Override
        public Try<Object> execute(final Query query) {
            return Try.failure(new QueryException("No query handler for " + query));
        }

        @Override
        public boolean canHandle(final Query query) {
            return false;
        }
    };

    private final List<CommandHandler<Command>> commandHandlers = new ArrayList<>();
    private final List<QueryHandler<Query, ?>> queryHandlers = new ArrayList<>();
    private final List<Processor> processors = new ArrayList<>();

    public DefaultDispatcher(final List<CommandHandler<Command>> commandHandlers,
                             final List<QueryHandler<Query, ?>> queryHandlers) {
        if (commandHandlers != null) {
            this.commandHandlers.addAll(commandHandlers);
        }
        if (queryHandlers != null) {
            this.queryHandlers.addAll(queryHandlers);
        }
    }

    public DefaultDispatcher(final List<CommandHandler<Command>> commandHandlers,
                             final List<QueryHandler<Query, ?>> queryHandlers,
                             final List<Processor> processors) {
        this(commandHandlers, queryHandlers);

        if (processors != null) {
            this.processors.addAll(processors);
        }
    }


    @Override
    public Try<Void> dispatchCommand(final Command command) {
        if (command == null) {
            return Try.failure(new CommandException("Command cannot be null"));
        }

        final CommandHandler<Command> commandHandler = findCommandHandler(command, commandHandlers);
        try {
            processCommandProcessors(command, commandHandler, findPreProcessors(processors));

            commandHandler.execute(command);

            processCommandProcessors(command, commandHandler, findPostProcessors(processors));

        } catch (CommandException e) {
            return Try.failure(e);
        }

        return Try.success(null);
    }

    private void processCommandProcessors(final Command command, final CommandHandler<Command> commandHandler,
                                          final io.vavr.collection.List<Processor> postProcessors) throws CommandException {
        for (Processor processor : postProcessors) {
            if (processor instanceof CommandProcessor) {
                ((CommandProcessor) processor).process(commandHandler, command);
            }
        }
    }

    private CommandHandler<Command> findCommandHandler(final Command command,
                                                       final List<CommandHandler<Command>> commandHandlers) {
        return commandHandlers.stream()
                .filter(handler -> handler.canHandle(command))
                .findFirst()
                .orElse(DEFAULT_COMMAND_HANDLER);
    }

    @Override
    public Try<?> dispatchQuery(final Query query) {
        if (query == null) {
            return Try.failure(new QueryException("Query cannot be null"));
        }

        final QueryHandler<Query, ?> queryHandler = findQueryHandler(query, queryHandlers);
        try {
            processQueryProcessors(query, queryHandler, findPreProcessors(processors));

            final Try<?> result = queryHandler.execute(query);

            processQueryProcessors(query, queryHandler, findPostProcessors(processors));

            return result;

        } catch (QueryException e) {
            return Try.failure(e);
        }
    }

    private void processQueryProcessors(final Query query, final QueryHandler<Query, ?> queryHandler,
                                        final io.vavr.collection.List<Processor> postProcessors) throws QueryException {
        for (Processor processor : postProcessors) {
            if (processor instanceof CommandProcessor) {
                ((QueryProcessor) processor).process(queryHandler, query);
            }
        }
    }

    private QueryHandler<Query, ?> findQueryHandler(final Query query, final List<QueryHandler<Query, ?>> queryHandlers) {
        return queryHandlers.stream()
                .filter(handler -> handler.canHandle(query))
                .findFirst()
                .orElse(DEFAULT_QUERY_HANDLER);
    }

    private io.vavr.collection.List<Processor> findPreProcessors(List<Processor> processors) {
        return io.vavr.collection.List.ofAll(processors.stream()
                .filter(processor -> processor.getType() == Processor.ProcessorType.PRE_PROCESSOR
                        || processor.getType() == Processor.ProcessorType.DUPLEX_PROCESSOR)
        );
    }

    private io.vavr.collection.List<Processor> findPostProcessors(List<Processor> processors) {
        return io.vavr.collection.List.ofAll(processors.stream()
                .filter(processor -> processor.getType() == Processor.ProcessorType.POST_PROCESSOR
                        || processor.getType() == Processor.ProcessorType.DUPLEX_PROCESSOR)
        );
    }

}
