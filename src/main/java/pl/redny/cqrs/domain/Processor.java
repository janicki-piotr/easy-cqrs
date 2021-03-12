package pl.redny.cqrs.domain;

public interface Processor {

    ProcessorType getType();

    public enum ProcessorType {
        PRE_PROCESSOR, POST_PROCESSOR, DUPLEX_PROCESSOR
    }
}
