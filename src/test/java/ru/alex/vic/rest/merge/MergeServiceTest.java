package ru.alex.vic.rest.merge;

import com.google.inject.TypeLiteral;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.alex.vic.InjectorBuilder;
import ru.alex.vic.dao.Dao;
import ru.alex.vic.entities.hh.HHLocation;
import ru.alex.vic.entities.merge.MergeVk;
import ru.alex.vic.entities.vk.VkLocation;

import javax.inject.Inject;

public class MergeServiceTest {
    @Inject
    private MergeService mergeService;

    @Before
    public void setUp() {
        new InjectorBuilder(this)
                .bind(MergeService.class)
                .mock(new TypeLiteral<Dao<Long, HHLocation>>() {
                }, new TypeLiteral<Dao<Long, VkLocation>>() {
                }, new TypeLiteral<Dao<Long, MergeVk>>() {
                })
                .create();
    }

    @Test
    public void testReplace() {
        final String republic = mergeService.replaceRepublic("Республика Карелия");
        Assert.assertEquals("Карелия", republic);

    }


}