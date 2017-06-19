package com.dabaicong.file;

/**
 * Created by liuchong on 2017/6/17.
 * Copyright(c) 2017 quanmincai Co., Ltd.
 * All right reserved.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FileSplit {

    /**
     * 程序入口
     */
    public static void main(String[] args) {
        FileSplit instance = new FileSplit();
        instance.testcase();
    }

    /**
     * 测试用例，教你怎样使用这些代码。
     */
    public void testcase() {
        File file = new File("/Users/liuchong/Downloads/xzhdx.txt");
        DataHandler dataHandler = new DataHandler() {
            public void doHandler(String[] data) {
                synchronized (System.out) {
                    for (String s : data) {
                        System.out.print(s);
                        System.out.print('\t');
                    }
                    System.out.println();
                }
            }
        };
        BigFileProcessor processor = new BigFileProcessor(file, dataHandler);
        processor.start();
        processor.join();
    }

    interface DataHandler {
        void doHandler(String[] data);
    }

    interface ErrorHandler {
        ErrorHandler PRINTER = new ErrorHandler() {
            public void doHandler(Throwable t) {
                t.printStackTrace();
            }
        };

        void doHandler(Throwable t);
    }

    /**
     * 核心类，大文件数据处理器
     */
    class BigFileProcessor {
        /**
         * 记录的分隔符，每个分隔符占一行。
         */
        public static final String DEFAULT_SEPERATOR = "*****************";
        /**
         * 致命毒药，用于干掉处理数据的线程。
         */
        public final Object POISON = new Object();
        private BlockingQueue<Object> queue = new ArrayBlockingQueue<Object>(64);
        private String seperator = DEFAULT_SEPERATOR;
        private ErrorHandler errorHandler = ErrorHandler.PRINTER;
        /**
         * 用于终止读取线程，非强制终止。
         */
        private volatile boolean running = false;
        /**
         * 数据读取线程
         */
        private Thread fileReader;
        /**
         * 数据处理线程
         */
        private Thread[] proccessors;

        public BigFileProcessor(final File file, final DataHandler handler) {
            fileReader = new Thread(new Runnable() {
                public void run() {
                    try {
                        Charset charset = Charset.defaultCharset();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
                        String line = null;
                        ArrayList<String> cache = new ArrayList<String>();
                        while (running && (line = reader.readLine()) != null) {
                            line = line.trim();
                            if (seperator.equals(line)) {
                                String[] data = cache.toArray(new String[cache.size()]);
                                cache.clear();
                                queue.put(data);
                            } else {
                                cache.add(line);
                            }
                        }
                    } catch (Throwable t) {
                        errorHandler.doHandler(t);
                    } finally {
                        try {
                            queue.put(POISON);
                        } catch (InterruptedException e) {
                            errorHandler.doHandler(e);
                        }
                    }
                }
            }, "reader_thread");
            //默认创建的线程数，与CPU处理的内核数相同，楼主可以自行更改。
            proccessors = new Thread[Runtime.getRuntime().availableProcessors()];
            Runnable worker = new Runnable() {
                public void run() {
                    try {
                        for (; ; ) {
                            Object obj = queue.take();
                            if (obj == POISON) {
                                queue.put(obj);
                                break;
                            } else {
                                String[] data = (String[]) obj;
                                handler.doHandler(data);
                            }
                        }
                    } catch (Throwable t) {
                        errorHandler.doHandler(t);
                    }
                }
            };
            for (int i = 0; i < proccessors.length; i++) {
                proccessors[i] = new Thread(worker, "proccessor-thread_" + i);
            }
        }

        public void setErrorHandler(ErrorHandler errorHandler) {
            this.errorHandler = errorHandler;
        }

        public void setSeperator(String seperator) {
            this.seperator = seperator;
        }

        /**
         * 开启处理过程
         */
        public synchronized void start() {
            if (running) return;
            running = true;
            fileReader.start();
            for (int i = 0; i < proccessors.length; i++) {
                proccessors[i].start();
            }
        }

        /**
         * 中断处理过程，非强制中断
         */
        public synchronized void shutdown() {
            if (running) {
                running = false;
            }
        }

        /**
         * 试图等待整个处理过程完毕
         */
        public void join() {
            try {
                fileReader.join();
            } catch (InterruptedException e) {
                errorHandler.doHandler(e);
            }
            for (int i = 0; i < proccessors.length; i++) {
                try {
                    proccessors[i].join();
                } catch (InterruptedException e) {
                    errorHandler.doHandler(e);
                }
            }
        }
    }

}
