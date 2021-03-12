package pl.redny.cqrs;

public interface Processor {

    ProcessorType getType();

    enum ProcessorType {
        PRE_PROCESSOR, POST_PROCESSOR, DUPLEX_PROCESSOR
    }
    
}
