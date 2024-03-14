package cn.hamm.airpower.tests.dictionary;

import cn.hamm.airpower.interfaces.IDictionary;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DemoDictionary implements IDictionary {
    Hello(1, "HELLO"),
    World(2, "WORLD");

    private final int key;
    private final String label;
}
