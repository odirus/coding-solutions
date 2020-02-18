package concurrency.multithreadprint;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题：启动 N 个线程，分别打印 M 个数，线程 1 打印1、2、3..M，线程 2 打印 M+1、M+2、M+3..2M 以此类推
 */
public class PrintAnyNumberPerThread {

    private static final Object LOCK = new Object();
    private static final List<Result> resultList = new ArrayList<>();

    /**
     * 当前打印数字
     */
    private static int currentNumber = 0;

    static class PrintSequenceThread implements Runnable {

        private int numberPerThread;
        private int startNumber;
        private int maxNumber;
        private int threadNo;
        private int threadCount;

        PrintSequenceThread(int numberPerThread, int startNumber, int maxNumber, int threadNo, int threadCount) {
            this.numberPerThread = numberPerThread;
            this.startNumber = startNumber;
            this.maxNumber = maxNumber;
            this.threadNo = threadNo;
            this.threadCount = threadCount;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (LOCK) {
                    if (currentNumber == 0) {
                        currentNumber = startNumber;
                    }

                    while ((currentNumber - startNumber) % (threadCount * numberPerThread) / numberPerThread != threadNo) {
                        if (currentNumber > maxNumber)
                            break;
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if (currentNumber > maxNumber)
                        break;

                    resultList.add(new Result(threadNo, currentNumber));
                    currentNumber++;
                    LOCK.notifyAll();
                }
            }
        }
    }

    public static class Result {
        private int threadNo;
        private int number;

        Result(int threadNo, int number) {
            this.threadNo = threadNo;
            this.number = number;
        }

        public int getThreadNo() {
            return threadNo;
        }

        public int getNumber() {
            return number;
        }

        @Override
        public String toString() {
            return "ThreadNo: " + threadNo + ", number=" + number;
        }
    }

    public List<Result> print(int threadCount, int numberPerThread, int startNumber, int maxNumber) {
        List<Thread> threadList = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(new PrintSequenceThread(numberPerThread, startNumber, maxNumber, i, threadCount));
            threadList.add(thread);
            thread.start();
        }

        for (Thread thread : threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Exit via interrupted exception");
            }
        }

        return resultList;
    }

}