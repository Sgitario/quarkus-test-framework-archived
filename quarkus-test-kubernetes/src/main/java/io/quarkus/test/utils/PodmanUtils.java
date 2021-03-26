package io.quarkus.test.utils;

import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Path;

import io.quarkus.test.ServiceContext;

public final class PodmanUtils {

    private static final String PODMAN = "podman";

    private PodmanUtils() {

    }

    public static void buildService(Path dockerFile, ServiceContext service) {
        try {
            new Command(PODMAN, "build", "-f", dockerFile.toString(), "-t", service.getOwner().getName(), ".").runAndWait();
        } catch (Exception e) {
            fail("Failed to build image " + service.getServiceFolder().toAbsolutePath().toString() + " . Caused by "
                    + e.getMessage());
        }
    }

    public static void pushToContainerRegistryUrl(ServiceContext service, String containerRegistryUrl) {
        try {
            String targetImage = containerRegistryUrl + "/" + service.getOwner().getName();
            new Command(PODMAN, "tag", service.getOwner().getName(), targetImage).runAndWait();
            new Command(PODMAN, "push", "--tls-verify=false", targetImage).runAndWait();
        } catch (Exception e) {
            fail("Failed to push image " + service.getOwner().getName() + " into " + containerRegistryUrl + ". Caused by "
                    + e.getMessage());
        }
    }
}
