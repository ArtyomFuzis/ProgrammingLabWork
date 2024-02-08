package Exception;

public class UndefinedOpinionException extends Exception{
    @Override
    public String getMessage() {
        return "Отношение персонажа к другому не определено!!!";
    }
}
