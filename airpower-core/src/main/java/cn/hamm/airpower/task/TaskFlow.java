package cn.hamm.airpower.task;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <h1>任务流程管理器</h1>
 *
 * @author Hamm.cn
 */
public class TaskFlow<D> {
    /**
     * <h2>使用的线程池服务</h2>
     */
    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(
            1,
            5,
            60,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy()
    );

    /**
     * <h2>任务步骤</h2>
     */
    private final List<Function<D, TaskFlow<D>>> steps = new ArrayList<>();

    /**
     * <h2>任务数据</h2>
     */
    private D data = null;

    /**
     * <h2>任务步骤处理前置方法</h2>
     */
    private Consumer<D> beforeStep = null;

    /**
     * <h2>任务步骤处理后置方法</h2>
     */
    private Consumer<D> afterStep = null;

    /**
     * <h2>任务执行成功方法</h2>
     */
    private Consumer<D> onSuccess = null;

    /**
     * <h2>任务执行失败方法</h2>
     */
    private BiConsumer<Exception, TaskFlow<D>> onError = null;

    private TaskFlow() {
        // 禁止外部实例化
    }

    /**
     * <h2>创建一个任务</h2>
     *
     * @param data 任务传递的数据
     * @param <D>  任务数据类型
     * @return 任务对象
     */
    public static <D> @NotNull TaskFlow<D> init(D data) {
        return next(data);
    }

    /**
     * <h2>任务的下一步</h2>
     *
     * @param data 任务数据
     * @param <D>  任务数据类型
     * @return 任务对象
     */
    public static <D> @NotNull TaskFlow<D> next(D data) {
        TaskFlow<D> taskFlow = new TaskFlow<>();
        taskFlow.data = data;
        return taskFlow;
    }

    /**
     * <h2>添加下一步</h2>
     *
     * @param next 下一步
     * @return 任务对象
     */
    public TaskFlow<D> next(Function<D, TaskFlow<D>> next) {
        steps.add(next);
        return this;
    }

    /**
     * <h2>开始任务</h2>
     */
    public final void start() {
        if (steps.isEmpty()) {
            if (Objects.nonNull(onSuccess)) {
                onSuccess.accept(data);
            }
            return;
        }
        Function<D, TaskFlow<D>> function = steps.get(0);
        steps.remove(0);
        try {
            TaskFlow.EXECUTOR.execute(() -> {
                if (Objects.nonNull(beforeStep)) {
                    beforeStep.accept(data);
                }
                TaskFlow<D> taskFlow = function.apply(data);
                if (Objects.nonNull(afterStep)) {
                    afterStep.accept(data);
                }
                this.data = taskFlow.data;
                this.start();
            });
        } catch (Exception exception) {
            if (Objects.nonNull(onError)) {
                onError.accept(exception, this);
                return;
            }
            throw exception;
        }
    }

    /**
     * <h2>任务成功后执行</h2>
     *
     * @param onSuccess 回调方法
     * @return 任务对象
     */
    @Contract(value = "_ -> this", mutates = "this")
    public final TaskFlow<D> onSuccess(Consumer<D> onSuccess) {
        this.onSuccess = onSuccess;
        return this;
    }

    /**
     * <h2>任务失败后执行</h2>
     *
     * @param onError 回调方法
     * @return 任务对象
     */
    @Contract(value = "_ -> this", mutates = "this")
    public final TaskFlow<D> onError(BiConsumer<Exception, TaskFlow<D>> onError) {
        this.onError = onError;
        return this;
    }

    /**
     * <h2>任务步骤执行前执行</h2>
     *
     * @param beforeStep 回调方法
     * @return 任务对象
     */
    @Contract(value = "_ -> this", mutates = "this")
    public final TaskFlow<D> beforeStep(Consumer<D> beforeStep) {
        this.beforeStep = beforeStep;
        return this;
    }

    /**
     * <h2>任务步骤执行后执行</h2>
     *
     * @param afterStep 回调方法
     * @return 任务对象
     */
    @Contract(value = "_ -> this", mutates = "this")
    public final TaskFlow<D> afterStep(Consumer<D> afterStep) {
        this.afterStep = afterStep;
        return this;
    }
}