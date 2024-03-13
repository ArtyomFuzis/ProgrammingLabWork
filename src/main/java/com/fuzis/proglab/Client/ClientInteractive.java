package com.fuzis.proglab.Client;

import com.fuzis.proglab.Annotations.InteractiveCommand;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ClientInteractive {
    public static void warn(String info) {
        System.out.println("[warn] " + info);
    }

    public static void feedback(String info) {
        System.out.println("[info] " + info);
    }

    public static void error(String info) {
        System.out.println("[error] " + info);
    }
    public static Queue<String> history = new LinkedList<>();
    @InteractiveCommand(args = {0}, usage = {"history - показать последние 14 выполненных команд"}, help = "Показывает историю выполнения команд")
    public void history(List<String> argc) {
        if (!history.isEmpty()) {
            System.out.println("Last commands:");
            for (var el : history) {
                System.out.println(el);
            }
        } else {
            feedback("History is empty");
        }
    }
}
