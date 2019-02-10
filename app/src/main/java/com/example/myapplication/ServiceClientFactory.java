package com.example.myapplication;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ServiceClientFactory {
    private static final Map<String, ServiceClient> clientCache = new HashMap<>();

    public static class ServiceClientImpl1 implements ServiceClient {
        private static final String TAG = "ServiceClientImpl1";
        ServiceClientImpl1() {
            Log.d(TAG, "init");
        }

        @Override
        public void blockContact(int reqSeq, String id) {
            Log.d(TAG, "block " + reqSeq + " id: " + id);
        }

        @Override
        public void connect(String hostname) {
            Log.d(TAG, "connect: " + hostname);
        }
    }

    private static <T extends ServiceClient> T getServiceClient(final String type) {
        ServiceClient client;
        synchronized (clientCache) {
            client = clientCache.get(type);
            if (client == null) {
                client = wrap(new ServiceClientImpl1(), ServiceClient.class);
                clientCache.put(type, client);
            }
        }
        return (T) client;
    }

    public static ServiceClient getClientForType1() {
        return getServiceClient("type1");
    }

    private static <T> T wrap(T client, Class<T> clazz) {
        Object proxyInstance = Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},
                new InvocationHandler() {
              @Override
              public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                Log.d("WRAP", "Invoke " + method + " through proxy");
                return null;
              }
            });
        return (T) proxyInstance;
    }
}
