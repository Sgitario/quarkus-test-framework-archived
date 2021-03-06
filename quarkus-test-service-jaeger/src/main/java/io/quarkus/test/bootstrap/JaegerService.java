package io.quarkus.test.bootstrap;

public class JaegerService extends BaseService<JaegerService> {

    public static final String JAEGER_TRACE_URL_PROPERTY = "ts.jaeger.trace.url";
    public static final String JAEGER_API_PATH = "/api/traces";

    public String getRestUrl() {
        return getHost() + ":" + getPort() + JAEGER_API_PATH;
    }

    public String getTraceUrl() {
        return getPropertyFromContext(JAEGER_TRACE_URL_PROPERTY) + JAEGER_API_PATH;
    }
}
