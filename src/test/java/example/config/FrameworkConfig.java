package example.config;

import org.aeonbits.owner.Config;

@Config.Sources({"classpath:${env}.properties"})
public interface FrameworkConfig extends Config {
    String resourceBaseUrl();
    String authBaseUrl();
    String resourceOrdersPath();
    String resourceCustomerPath();
    String resourceCustomerIdPath();
    String authPath();
    int timeoutMs();
}
