package com.dabaicong.file;

import java.io.*;
import java.util.*;

enum TerminalType {

    //	出票商,用于出票的
    DYJ(1, "大赢家"),
    ZhongCaiHui(5, "中彩汇"),
    Huacai(9, "华彩"),
    RuiLangYangGuang(10, "睿朗阳光"),
    YuXiao(13, "语笑通信"),

    GuoXinCai(11, "国信彩"),
    ZhongYue(12, "大奖365"),
    JinNuo(14, "金诺"),
    GaoDe(15, "高德"),
    GaoDeDPT(16, "高德大平台"),
    FYZC(17, "风云众成"),
    ZCLT(18, "中彩乐投"),
    GDHM(19, "高德豪门"),
    gxc_two(20, "国信彩2"),
    FYZC_two(21, "风云众成2"),
    CQFC(22, "重庆福彩"),
    HBZYZD(23, "自有终端"),
    FJEX(24, "福建恩夏"),
    gxc_three(25, "国信彩3"),
    DYWC(26, "点易吾彩"),
    BEMORE(27, "bemore出票商"),
    zclt2(28, "中彩乐投2"),

    //	非出票商，用于查询，抓取数据的
    WubaiWan(100, "500w彩票网"),
    //Okooo(101, "澳客网"),

    //	虚拟出票，仅供测试
    Virtual(0, "虚拟出票"),
    all(-1, "默认");

    public int value;

    public String name;

    TerminalType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static TerminalType get(int value) {
        TerminalType[] type = values();
        for (TerminalType terminalType : type) {
            if (terminalType.value == value) {
                return terminalType;
            }
        }
        return null;
    }

    public static List<TerminalType> get() {
        return Arrays.asList(values());
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "[value=" + value + ",name=" + name + "]";
    }

}

/**
 * Created by liuchong on 2017/5/21.
 * Copyright(c) 2017 quanmincai Co., Ltd.
 * All right reserved.
 */
public class findFirst {

    public static String filePath = "/Users/liuchong/Downloads/log";
    private static Set<String> allTicket = new HashSet<String>();

    static {
        allTicket.add("97add7a491e9a3ad54c02db3867e4c7d");
        allTicket.add("96997cb310aefc30b0b458daf096bc2d");
        allTicket.add("c9ef50ec7adad1d4ffa3756f9aee7e54");
        allTicket.add("dc3f5fb7aa2f979df70102e0729465bb");
        allTicket.add("dd62a1be7312bd859ac95855da6506f8");
        allTicket.add("e2edcfde035742874787ad9728f91848");

    }

    public static void main(String[] args) throws Exception {
        File file = new File(filePath);
        File[] files = file.listFiles();
        Map<String, Set<String>> terminalTicket = new HashMap<String, Set<String>>();
        if (files.length != allTicket.size()) {
            System.out.println("解析出的文件个数与实际票数不符。解析文件个数是：" + files.length + "实际票数是：" + allTicket.size());
        } else {
            System.out.println("实际票数是：" + allTicket.size());
        }
        for (File file1 : files) {
            BufferedReader fileRead = new BufferedReader(new FileReader(file1));
            String[] line = fileRead.readLine().substring(0, 100).split("\\|");
            System.out.println(file1.getName());
            System.out.println(file1.getName() + "    " + line[3]);
            if (terminalTicket.containsKey(line[3])) {
                Set<String> ticketSet = terminalTicket.get(line[3]);
                ticketSet.add(file1.getName());
            } else {
                Set<String> ticketSet = new HashSet<String>();
                ticketSet.add(file1.getName());
                terminalTicket.put(line[3], ticketSet);
            }

        }
        //写入文件
        BufferedWriter fileWriter = null;
        for (Map.Entry<String, Set<String>> entry : terminalTicket.entrySet()) {
            System.out.println("Key = " + entry.getKey() + "size :" + entry.getValue().size() + ", Value = " + entry.getValue());
            Set<String> ticketSet = entry.getValue();
            String name = TerminalType.get(Integer.parseInt(entry.getKey())).getName();
            fileWriter = new BufferedWriter(new FileWriter(new File(name)));
            for (String s : ticketSet) {
                fileWriter.write(s);
                fileWriter.newLine();
            }
            fileWriter.flush();
            fileWriter.close();
        }

    }
}
