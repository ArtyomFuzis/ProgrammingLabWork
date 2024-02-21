package com.fuzis.lab5.Exception;

/**
 * Ну порой бывает сложно определить отношение между персонажами, особенно когда они цундерэ...
 * <br>
 * <img width="300" src="https://cdn.pixilart.com/photos/large/936e4539ea1e07f.jpg">
 */
public class UndefinedOpinionException extends Exception{
    @Override
    public String getMessage() {
        return "Отношение персонажа к другому не определено!!!";
    }
}
