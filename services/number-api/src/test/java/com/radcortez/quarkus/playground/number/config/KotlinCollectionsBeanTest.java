package com.radcortez.quarkus.playground.number.config;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class KotlinCollectionsBeanTest {
    @Inject
    KotlinCollectionsBean kotlinCollectionsBean;

    @Test
    void kotlinCollections() {
        assertEquals(3, kotlinCollectionsBean.typeList.size());
        assertEquals("1", kotlinCollectionsBean.typeList.get(0).getValue());
        assertEquals("2", kotlinCollectionsBean.typeList.get(1).getValue());
        assertEquals("3", kotlinCollectionsBean.typeList.get(2).getValue());
        assertEquals("1234", kotlinCollectionsBean.singleType.getValue());
    }
}
