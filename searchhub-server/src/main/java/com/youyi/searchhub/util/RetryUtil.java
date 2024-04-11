package com.youyi.searchhub.util;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;

/**
 * 重试工具
 *
 * @author <a href="https://github.com/dingxinliang88">youyi</a>
 */
@Slf4j
public class RetryUtil {


    // region 退避策略
    public static final IntUnaryOperator EXPONENTIAL_BACKOFF = retry -> (int) Math.pow(2, retry);
    public static final IntUnaryOperator LINEAR_BACKOFF = retry -> retry;
    public static final IntUnaryOperator NO_BACKOFF = retry -> 0;

    // endregion

    private static final int DEFAULT_WAIT_TIME = 1000;


    /**
     * 尝试执行一个任务，当任务失败时，根据指定的重试策略进行重试。
     *
     * @param task             需要执行的任务，是一个 Callable 类型，不能为空。
     * @param maxRetries       最大重试次数。
     * @param initialWaitTime  初始等待时间。
     * @param waitStrategy     等待策略，通过一个函数接口表示，接受重试次数作为输入，返回等待时间。
     * @param retryOnException 决定在捕获到哪种异常时应该重试的断言，是一个 Predicate 类型，不能为空。
     * @param maxWaitTime      最大等待时间，防止无限等待。
     * @return 任务执行的结果。
     * @throws Exception 如果任务执行失败且超过了最大重试次数，则抛出异常。
     */
    public static <T, E extends Exception> T retry(Callable<T> task, int maxRetries,
            int initialWaitTime, IntUnaryOperator waitStrategy, Predicate<E> retryOnException,
            int maxWaitTime) throws Exception {
        int retries = 0;
        while (true) {
            try {
                // 尝试执行任务，成功则返回结果
                return task.call();
            } catch (Exception e) {
                // 判断是否超过最大重试次数或不应重试的异常类型
                if (++retries > maxRetries || !retryOnException.test((E) e)) {
                    throw e;
                }
                // 计算等待时间，并确保不超过最大等待时间
                int waitTime = Math.min(waitStrategy.applyAsInt(retries) * initialWaitTime,
                        maxWaitTime);
                // 等待一段时间后再次尝试执行任务
                TimeUnit.MILLISECONDS.sleep(waitTime);
            }
        }
    }


    /**
     * 以线性退避策略重试给定的任务，直到成功执行或达到最大重试次数。
     *
     * @param task       需要执行的任务，是一个Callable接口的实例，任务执行成功将返回其call方法的返回值。
     * @param maxRetries 最大重试次数。
     * @param waitTime   初始等待时间，单位为毫秒。
     * @return 任务执行成功时返回的任务结果。
     * @throws Exception 如果任务执行失败且达到最大重试次数，则抛出异常。
     */
    public static <T> T retry(Callable<T> task, int maxRetries, int waitTime) throws Exception {
        return retry(task, maxRetries, waitTime, LINEAR_BACKOFF, e -> true, Integer.MAX_VALUE);
    }

    /**
     * 以线性退避策略重试给定的任务，直到最大重试次数达到或任务成功执行。
     *
     * @param task       需要执行并可能需要重试的任务，是一个 Runnable 对象。
     * @param maxRetries 最大重试次数。
     * @param waitTime   每次重试前的等待时间（毫秒）。
     * @throws Exception 如果任务执行失败且达到最大重试次数，则抛出异常。
     */
    public static void retry(Runnable task, int maxRetries, int waitTime) throws Exception {
        // 将 Runnable 包装成 Callable，以便在 retry 方法中使用，该方法支持有返回值的任务，但此处任务无返回值，故返回 null
        retry(() -> {
            task.run();
            return null;
        }, maxRetries, waitTime, LINEAR_BACKOFF, e -> true, Integer.MAX_VALUE);
    }


    /**
     * 尝试执行一个任务，直到成功或达到最大重试次数。
     *
     * @param task       需要执行的任务，是一个Callable接口的实例，任务执行成功时应返回一个T类型的值。
     * @param maxRetries 最大重试次数。如果任务在指定次数内失败，则会抛出异常。
     * @return 任务执行成功时返回的T类型的值。
     * @throws Exception 如果任务执行失败且已达到最大重试次数，则抛出异常。
     */
    public static <T> T retry(Callable<T> task, int maxRetries) throws Exception {
        return retry(task, maxRetries, DEFAULT_WAIT_TIME);
    }

    /**
     * 尝试执行给定的任务，最多重试指定次数。如果任务在执行时抛出异常，则会尝试重新执行任务， 直到任务成功执行或达到最大重试次数。这是对任务进行简单重试逻辑的便捷方法。
     *
     * @param task       将要执行的任务，是一个 Runnable 接口实例。
     * @param maxRetries 最大重试次数。如果任务在尝试执行这么多次之后仍然失败，则会抛出异常。
     * @throws Exception 如果任务在所有重试尝试之后仍然失败，则抛出异常。
     */
    public static void retry(Runnable task, int maxRetries) throws Exception {
        // 使用默认等待时间、线性退避策略、无条件重试和无限重试间隔上限来调用具有更多选项的 retry 方法
        retry(() -> {
            task.run();
            return null;
        }, maxRetries, DEFAULT_WAIT_TIME, LINEAR_BACKOFF, e -> true, Integer.MAX_VALUE);
    }


    /**
     * 尝试执行一个任务，当任务执行失败时，根据指定的条件重新尝试执行，最多重试指定次数。
     * <p>此方法专门用于那些没有任何返回值的任务，它会根据提供的异常断言决定是否重试任务。</p>
     *
     * @param task             需要执行的任务，是一个 Runnable 接口的实例。
     * @param maxRetries       最大重试次数。
     * @param retryOnException 一个断言，用于判断哪种类型的异常应该触发重试逻辑。E 是异常的类型，需要是 Runnable 抛出的异常类型或其子类型。
     * @throws Exception 如果任务执行失败且超过了最大重试次数，则会抛出异常。
     */
    public static <E extends Exception> void retry(Runnable task, int maxRetries,
            Predicate<E> retryOnException) throws Exception {
        // 将无返回值的 Runnable 任务包装成 Callable，以便在 retry 方法中使用，该方法提供了更丰富的重试逻辑和策略。
        retry(() -> {
            task.run();
            return null;
        }, maxRetries, DEFAULT_WAIT_TIME, LINEAR_BACKOFF, retryOnException, Integer.MAX_VALUE);
    }

}
