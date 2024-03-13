package com.fuzis.proglab.Exception;

/**
 * КАРЛСОН ПРОПАЛ!!!! НЕПОРЯДОК, RuntimeExcept!!!
 */
public class KarlsonMissedRuntimeException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Карлсон исчез!!!!";
    }
}
