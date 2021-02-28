package cn.gmj.taotao.page.service.impl;

import cn.gmj.taotao.page.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class PageServiceImplTest {
    @Autowired
    private PageService pageService;
    @Test
    public void createHtml() {
        pageService.createHtml(141L);
    }

}