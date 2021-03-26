package io.quarkus.test.containers;

import java.util.List;

import io.quarkus.test.ManagedResource;
import io.quarkus.test.extension.KubernetesExtensionBootstrap;
import io.quarkus.test.kubernetes.KubectlFacade;
import io.quarkus.test.logging.KubernetesLoggingHandler;
import io.quarkus.test.logging.LoggingHandler;

public class KubernetesContainerManagedResource implements ManagedResource {

    private static final int HTTP_PORT = 80;

    private final ContainerManagedResourceBuilder model;
    private final KubectlFacade facade;

    private LoggingHandler loggingHandler;
    private boolean init;
    private boolean running;

    protected KubernetesContainerManagedResource(ContainerManagedResourceBuilder model) {
        this.model = model;
        this.facade = model.getContext().get(KubernetesExtensionBootstrap.CLIENT);
    }

    @Override
    public void start() {
        if (running) {
            return;
        }

        if (!init) {
            facade.createApplication(model.getContext().getOwner().getName(), model.getImage());
            init = true;
        }

        facade.setReplicaTo(model.getContext().getOwner().getName(), 1);
        running = true;

        loggingHandler = new KubernetesLoggingHandler(model.getContext());
        loggingHandler.startWatching();
    }

    @Override
    public void stop() {
        if (loggingHandler != null) {
            loggingHandler.stopWatching();
        }

        facade.setReplicaTo(model.getContext().getOwner().getName(), 0);
        running = false;
    }

    @Override
    public String getHost() {
        return facade.getHostByService(model.getContext().getOwner().getName());
    }

    @Override
    public int getPort() {
        return HTTP_PORT;
    }

    @Override
    public boolean isRunning() {
        return loggingHandler != null && loggingHandler.logsContains(model.getExpectedLog());
    }

    @Override
    public List<String> logs() {
        return loggingHandler.logs();
    }

}
