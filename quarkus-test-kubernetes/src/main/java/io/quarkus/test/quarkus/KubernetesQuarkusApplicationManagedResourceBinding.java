package io.quarkus.test.quarkus;

import io.quarkus.test.ManagedResource;
import io.quarkus.test.ServiceContext;
import io.quarkus.test.annotation.KubernetesTest;

public class KubernetesQuarkusApplicationManagedResourceBinding implements QuarkusApplicationManagedResourceBinding {

    @Override
    public boolean appliesFor(ServiceContext context) {
        return context.getTestContext().getRequiredTestClass().isAnnotationPresent(KubernetesTest.class);
    }

    @Override
    public ManagedResource init(QuarkusApplicationManagedResourceBuilder builder) {
        return new KubernetesQuarkusApplicationManagedResource(builder);
    }

}
