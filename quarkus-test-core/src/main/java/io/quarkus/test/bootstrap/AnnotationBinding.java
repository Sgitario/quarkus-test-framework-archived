package io.quarkus.test.bootstrap;

import java.lang.reflect.Field;

public interface AnnotationBinding {
    boolean isFor(Field field);

    ManagedResourceBuilder createBuilder(Field field) throws Exception;
}
