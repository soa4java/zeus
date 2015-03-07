package com.ctrip.zeus.service;

import com.ctrip.zeus.model.entity.App;
import com.ctrip.zeus.model.entity.Slb;

import java.util.List;

/**
 * @author:xingchaowang
 * @date: 3/4/2015.
 */
public interface AppRepository extends Repository {
    List<App> list();

    App get(String appName);

    void add(App app);
}