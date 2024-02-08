package Exception;

public class UndefinedOpinionRuntimeException extends RuntimeException{
    @Override
    public String getMessage() {
        return "Отношение персонажа к другому не определено!!!";
    }
}
