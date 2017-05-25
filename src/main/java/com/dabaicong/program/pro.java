package com.dabaicong.program;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class pro {

	public static void main(String[] args) throws IOException {
        Set<String> userno = new HashSet<String>();
        userno.add("1");
        userno.add("2");
        System.out.println(userno.toString());

        String temps = "3,4,5";
        String[] users = temps.split(",");
        for (String s : users) {
            userno.add(s);
        }
        System.out.println(userno.toString());
    }

}
