package jrails;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class JRouter {
    private final Map<String, Route> routes = new HashMap<>();

    private static class Route {
        Class<?> clazz;
        String methodName;

        Route(Class<?> clazz, String methodName) {
            this.clazz = clazz;
            this.methodName = methodName;
        }
    }

    public void addRoute(String verb, String path, Class clazz, String method) {
        String key = generateKey(verb, path);
        routes.put(key, new Route(clazz, method));
    }

    // Generate unique hash key from verb and path
    private String generateKey(String verb, String path) {
        return verb + ":" + path;
    }

    public String getRoute(String verb, String path) {
        Route route = routes.get(generateKey(verb, path));
        if (route != null) {
            return route.clazz.getName() + "#" + route.methodName;
        }
        return null;
    }

    public Html route(String verb, String path, Map<String, String> params) {
        Route route = routes.get(generateKey(verb, path));
        if (route == null) {
            throw new UnsupportedOperationException("No route found for verb " + verb + " and path " + path);
        }
        try {
            Method method = route.clazz.getMethod(route.methodName, Map.class);
            Object controllerInstance = route.clazz.getDeclaredConstructor().newInstance();
            return (Html) method.invoke(controllerInstance, params);
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute route");
        }
    }
}
