package cn.hamm.airpower.interfaces;

import java.io.Serializable;
import java.util.function.Function;

/**
 * <h1>带序列化的 <code>Function</code></h1>
 *
 * @author Hamm.cn
 */
@FunctionalInterface
public interface IFunction<T, R> extends Function<T, R>, Serializable {
}