package com.fuzis.lab5.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для обозначения команды интерактивного режима
 * поля отражают возможное кол-во аргументов, описание использования и общую информацию для отображения в help
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InteractiveCommand {
    int[] args();
    String[] usage();
    String help();
}
