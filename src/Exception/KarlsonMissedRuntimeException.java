package Exception;

public class KarlsonMissedRuntimeException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Карлсон исчез!!!!";
    }
}
