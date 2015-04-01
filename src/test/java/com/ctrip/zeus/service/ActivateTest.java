package com.ctrip.zeus.service;

import com.ctrip.zeus.ao.AbstractAPITest;
import com.ctrip.zeus.ao.AopSpring;
import com.ctrip.zeus.ao.Checker;
import com.ctrip.zeus.ao.ReqClient;
import org.junit.Test;

/**
 * Created by fanqq on 2015/3/31.
 */
public class ActivateTest extends AbstractAPITest {

    @Test
    public void activeteTest(){
        AopSpring.addChecker("com.ctrip.zeus.service.Activate.impl.ActivateServiceImpl.activate", new Checker() {
            @Override
            public void check() {
                System.out.println("123");
            }
        });

        new ReqClient("http://127.0.0.1:8099").request("/api/conf/activate","{\n" +
                "   \"conf-slb-names\": [\n" +
                "      {\n" +
                "         \"slbname\": \"default\"\n" +
                "      }\n" +
                "   ],\n" +
                "   \"conf-app-names\": [\n" +
                "      {\n" +
                "         \"appname\": \"123\"\n" +
                "      }\n" +
                "   ]\n" +
                "}\n");

    }
}