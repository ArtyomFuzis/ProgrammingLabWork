/*
"Если меня не кормят, я уже не я" -- так он однажды выразился. Фрекен Бок мечтала,
 чтобы Карлсон не проводил у них время, но тщетно, потому что Малыш и дядя Юлиус были
 на его стороне. Фрекен Бок всегда ворчала, если он появлялся как раз в тот момент,
 когда надо садиться за стол, но сделать она ничего не могла, и Карлсон ел вместе со всеми.
 */
package com.fuzis.lab5;


import com.fuzis.lab5.Characters.FrekenBock;
import com.fuzis.lab5.Characters.Karlson;
import com.fuzis.lab5.GeneralObjects.DefaultCartoonPersonCharacter;

import java.util.Arrays;
import java.util.Collection;

/**
 * Нуждается ли Main в пояснении¿
 * Ну ладно это класс, в котором содержится точка входа программы - функция main
 */
public class Main {
    /**
     * Точка входа в программу
     * Первый переданный аргумент - имя файла, хранящего данные коллекции, остальные аргументы игнорируются
     * После обработки аргументов, запускает интерактивный режим
     * @param args содержит аргументы командной строки переданные программе
     */
    public static void main(String[] args) {
        if(args.length >= 1) CharacterCollection.fileName = args[0];
        Interactive.start();
    }

}