package io.quarkus.test.extension;

import static org.junit.jupiter.api.Assertions.fail;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.extension.ExtensionContext;

import io.quarkus.test.ServiceContext;
import io.quarkus.test.annotation.KubernetesTest;
import io.quarkus.test.extensions.ExtensionBootstrap;
import io.quarkus.test.kubernetes.KubectlFacade;

public class KubernetesExtensionBootstrap implements ExtensionBootstrap {

    public static final String CLIENT = "kubectl-client";
    public static final String CONTAINER_REGISTER_URL = "container-registry-url";

    private static final String CONTAINER_REGISTY_SERVICE_NAME = "container-registry";
    private static final String CONTAINER_REGISTRY_DEPLOYMENT = "/container-registry.yml";
    private static final int PROJECT_NAME_SIZE = 10;

    private KubectlFacade facade;

    @Override
    public boolean appliesFor(ExtensionContext context) {
        return context.getRequiredTestClass().isAnnotationPresent(KubernetesTest.class);
    }

    @Override
    public void beforeAll(ExtensionContext context) {
        facade = KubectlFacade.create(generateRandomProject());
        deployContainerRegistry();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        facade.deleteProject();
    }

    @Override
    public void updateServiceContext(ServiceContext context) {
        context.put(CLIENT, facade);
        context.put(CONTAINER_REGISTER_URL, getContainerServiceEndpoint());
    }

    private String getContainerServiceEndpoint() {
        return String.format("%s:%s", facade.getHostByService(CONTAINER_REGISTY_SERVICE_NAME),
                facade.getPortByService(CONTAINER_REGISTY_SERVICE_NAME));
    }

    private void deployContainerRegistry() {

        try {
            URL containerRegistryFile = KubernetesExtensionBootstrap.class.getResource(CONTAINER_REGISTRY_DEPLOYMENT);
            facade.apply(Paths.get(containerRegistryFile.toURI()));
        } catch (URISyntaxException e) {
            fail("Failed to load the container registry");
        }
    }

    private String generateRandomProject() {
        return ThreadLocalRandom.current().ints(PROJECT_NAME_SIZE, 'a', 'z' + 1)
                .collect(() -> new StringBuilder("ts-"), StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

}
