package com.fuzis.lab5.Exception;

/**
 * КАРЛСОН ПРОПАЛ!!!! НЕПОРЯДОК, RuntimeExcept!!!
 */
public class KarlsonMissedRuntimeException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Карлсон исчез!!!!";
    }
}
