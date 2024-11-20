package jrails;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class JRouter {

     // Internal storage for routes
    private final Map<String, Route> routes = new HashMap<>();

    // Represents a route with its class and method name
    private static class Route {
        Class<?> clazz;
        String methodName;

        Route(Class<?> clazz, String methodName) {
            this.clazz = clazz;
            this.methodName = methodName;
        }
    }

    // Add a route to the router
    public void addRoute(String verb, String path, Class clazz, String method) {
        String key = generateKey(verb, path);
        routes.put(key, new Route(clazz, method));
    }

    // Generate a unique key for a combination of verb and path
    private String generateKey(String verb, String path) {
        return verb + ":" + path;
    }

    // Get the route in "clazz#method" format
    public String getRoute(String verb, String path) {
        Route route = routes.get(generateKey(verb, path));
        if (route != null) {
            return route.clazz.getName() + "#" + route.methodName;
        }
        return null;
    }

    // Call the appropriate controller method and return the result
    public Html route(String verb, String path, Map<String, String> params) {
        Route route = routes.get(generateKey(verb, path));
        if (route == null) {
            throw new UnsupportedOperationException("No route found for verb " + verb + " and path " + path);
        }
        try {
            // Reflectively invoke the controller method
            Method method = route.clazz.getMethod(route.methodName, Map.class);
            Object controllerInstance = route.clazz.getDeclaredConstructor().newInstance();
            return (Html) method.invoke(controllerInstance, params);
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute route: " + e.getMessage(), e);
        }
    }
}
