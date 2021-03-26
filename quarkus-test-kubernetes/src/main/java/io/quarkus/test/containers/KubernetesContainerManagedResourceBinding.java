package io.quarkus.test.containers;

import io.quarkus.test.ManagedResource;
import io.quarkus.test.ServiceContext;
import io.quarkus.test.annotation.KubernetesTest;

public class KubernetesContainerManagedResourceBinding implements ContainerManagedResourceBinding {

    @Override
    public boolean appliesFor(ServiceContext context) {
        return context.getTestContext().getRequiredTestClass().isAnnotationPresent(KubernetesTest.class);
    }

    @Override
    public ManagedResource init(ContainerManagedResourceBuilder builder) {
        return new KubernetesContainerManagedResource(builder);
    }

}
