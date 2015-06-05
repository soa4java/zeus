package com.ctrip.zeus.client;

import com.ctrip.zeus.nginx.entity.NginxResponse;
import com.ctrip.zeus.nginx.entity.NginxServerStatus;
import com.ctrip.zeus.nginx.entity.TrafficStatusList;
import com.ctrip.zeus.nginx.transform.DefaultJsonParser;
import com.ctrip.zeus.util.IOUtils;
import jersey.repackaged.com.google.common.cache.CacheBuilder;
import jersey.repackaged.com.google.common.cache.CacheLoader;
import jersey.repackaged.com.google.common.cache.LoadingCache;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author:xingchaowang
 * @date: 3/15/2015.
 */
public class NginxClient extends AbstractRestClient {
    private static LoadingCache<String,NginxClient> cache = CacheBuilder.newBuilder().maximumSize(20)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build(new CacheLoader<String, NginxClient>() {
                       @Override
                       public NginxClient load(String url) throws Exception {
                           return new NginxClient(url);
                       }
                   }
            );

    public static NginxClient getClient(String url) throws ExecutionException {
        return cache.get(url);
    }

    public NginxClient(String url) {
        super(url);
    }


    public NginxResponse load() throws IOException{
        String responseStr = getTarget().path("/api/nginx/load").request().headers(getDefaultHeaders()).get(String.class);
        return DefaultJsonParser.parse(NginxResponse.class, responseStr);
    }

    public NginxResponse write()throws IOException{
        String responseStr = getTarget().path("/api/nginx/write").request().headers(getDefaultHeaders()).get(String.class);
        return DefaultJsonParser.parse(NginxResponse.class, responseStr);
    }

    public NginxResponse dyups(String upsName ,String upsCommands)throws IOException{
        String responseStr = getTarget().path("/api/nginx/dyups/" + upsName).request().headers(getDefaultHeaders()).post(Entity.entity(upsCommands,
                MediaType.APPLICATION_JSON
        ),String.class);
        return DefaultJsonParser.parse(NginxResponse.class,responseStr);
    }

    public TrafficStatusList getTrafficStatus() throws Exception {
        Response response = getTarget().path("").path("/api/nginx/trafficStatus").request().headers(getDefaultHeaders()).get();
        InputStream is = (InputStream)response.getEntity();
        try {
            return DefaultJsonParser.parse(TrafficStatusList.class, IOUtils.inputStreamStringify(is));
        } catch (Exception ex) {
            throw new Exception("Fail to parse traffic status object.");
        }
    }

    public NginxServerStatus getNginxServerStatus() throws IOException {
        //TODO
        return new NginxServerStatus();
    }
}
