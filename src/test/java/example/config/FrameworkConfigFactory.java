package example.config;

import org.aeonbits.owner.ConfigFactory;

public final class FrameworkConfigFactory {

    private static FrameworkConfig cfg;
    private FrameworkConfigFactory() {}
    public static FrameworkConfig getFrameworkConfig() {
        if (cfg == null) {
            String env = System.getProperty("env","dev");
            System.setProperty("env",env);
            cfg= ConfigFactory.create(FrameworkConfig.class);
        }
        return cfg;
    }
    
}
