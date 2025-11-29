package br.com.coregate.finalizer.grpc.server.support;

import io.grpc.BindableService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GrpcServiceRegistry {

    private final List<BindableService> services = new ArrayList<>();

    public void add(BindableService service) {
        services.add(service);
    }

    public List<BindableService> getServices() {
        return services;
    }
}
