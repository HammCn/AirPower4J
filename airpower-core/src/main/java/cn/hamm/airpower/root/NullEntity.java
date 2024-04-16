package cn.hamm.airpower.root;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <h1>空实体</h1>
 *
 * @author Hamm
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NullEntity extends RootEntity<NullEntity> {
}
