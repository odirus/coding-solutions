package concurrency.multithreadprint;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

public class PrintAnyNumberPerThreadTest {

    @Test
    public void print() {
        final int threadCount = 4;
        final int numberPerThread = 2;
        final int startNumber = 1;
        final int maxNumber = 80;

        PrintAnyNumberPerThread solution = new PrintAnyNumberPerThread();
        List<PrintAnyNumberPerThread.Result> resultList = solution.print(threadCount, numberPerThread, startNumber, maxNumber);
        // 线程执行顺序
        List<Integer> threadNoList = resultList.stream().map(PrintAnyNumberPerThread.Result::getThreadNo).collect(Collectors.toList());
        Assert.assertArrayEquals(new Integer[]{0, 0, 1, 1, 2, 2, 3, 3, 0, 0}, threadNoList.subList(0, 10).toArray(new Integer[10]));
        // 数字输出顺序
        List<Integer> numberList = resultList.stream().map(PrintAnyNumberPerThread.Result::getNumber).collect(Collectors.toList());
        Assert.assertArrayEquals(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, numberList.subList(0, 10).toArray(new Integer[10]));
    }
}