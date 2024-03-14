package cn.hamm.airpower.tests;

import cn.hamm.airpower.tests.dictionary.DemoDictionary;
import cn.hamm.airpower.util.DictionaryUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DictionaryTests {
    @SuppressWarnings("DataFlowIssue")
    public static void main(String[] args) {
        int key = 1;
        log.info("{}", DictionaryUtil.getDictionaryByKey(DemoDictionary.class, key));
        log.info("{}", DictionaryUtil.getDictionaryByKey(DemoDictionary.class, key).getKey());
        log.info("{}", DictionaryUtil.getDictionaryByKey(DemoDictionary.class, key).getLabel());
        log.info("{}", DictionaryUtil.getDictionaryByKey(DemoDictionary.class, key).equalsKey(key));
    }
}
