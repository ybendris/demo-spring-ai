package dev.ybendris.demo_spring_ai.translation.exception;

import dev.ybendris.demo_spring_ai.common.exception.GenericException;

public class TranslationException extends GenericException {
    public TranslationException(String message) {
        super(message);
    }
}
