package example.routes;

import example.config.FrameworkConfigFactory;

public class Routes {

    private Routes() {}

    public static String resourceOrderUrl(){
       var c = FrameworkConfigFactory.getFrameworkConfig();
       return c.resourceBaseUrl() + c.resourceOrdersPath();
    }

    public static String resourceCustomerUrl(){
       var c = FrameworkConfigFactory.getFrameworkConfig();
       return c.resourceBaseUrl()+c.resourceCustomerPath();
    }

    public static String authUrl(){
        var c = FrameworkConfigFactory.getFrameworkConfig();
        return c.authBaseUrl() + c.authPath();
    }
    
}
