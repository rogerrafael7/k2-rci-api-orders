package k2.rci.api.orders.infra.shared.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class AppConfig {

    @Inject
    @ConfigProperty(name = "custom.api-products-url")
    String apiProductUrl;

    public String getApiProductUrl() {
        return apiProductUrl;
    }
}
