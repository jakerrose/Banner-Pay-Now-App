package com.doctums.banner.paymentgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.env.Environment;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

@EnableConfigurationProperties
@SpringBootApplication
//for external tomcat deployment
//public class PayNowApplication extends SpringBootServletInitializer {
//
//    public static void main(String[] args) {
//        SpringApplication.run(PayNowApplication.class, args);
//    }
//
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(PayNowApplication.class);
//    }
//}
//for running in intellij
public class PayNowApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayNowApplication.class, args);

        // Debug: detect if running inside external Tomcat (catalina.home present) and print a message for catalina logs
        String catalinaHome = System.getProperty("catalina.home");
        if (catalinaHome == null || catalinaHome.isEmpty()) {
            catalinaHome = System.getenv("CATALINA_HOME");
        }
        if (catalinaHome != null && !catalinaHome.isEmpty()) {
            System.out.println("[DEBUG] PayNowApplication - detected external Tomcat (CATALINA_HOME=" + catalinaHome + ")");
        } else {
            System.out.println("[DEBUG] PayNowApplication - no CATALINA_HOME detected; likely running embedded or non-Tomcat container");
        }
    }
}

//        SpringApplication app = new SpringApplication(PayNowApplication.class);
//
//        //SpringApplication.run(PayNowApplication.class, args);
//        Environment env = app.run(args).getEnvironment();
//
//        String mode = env.getProperty("app.server-mode", "embedded");
//
//        if ("embedded".equalsIgnoreCase(mode)) {
//            System.out.println("Running with embedded Tomcat...");
//            // nothing special, Spring Boot runs normally
//        } else {
//            System.out.println("Running in external Tomcat...");
            // you might disable some beans or configs here if needed
//        }

         //Open browser after app starts
//        String url = "https://localhost:7171/index";
//        openBrowser(url);
//        openBrowser("https://localhost:" + env.getProperty("server.port", "7171") + "/index");
//         //Only open browser if embedded
//        if ("embedded".equalsIgnoreCase(env.getProperty("app.server-mode", "embedded"))) {
//            openBrowser("https://localhost:" + env.getProperty("server.port", "7171") + "/index");
//        }
//    }

//    private static void openBrowser(String url) {
//        try {
//            // Use Desktop to launch browser
//            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
//                Desktop.getDesktop().browse(URI.create(url));
//            } else {
//                // Fallback for Windows
//                String os = System.getProperty("os.name").toLowerCase();
//                if (os.contains("win")) {
//                    Runtime.getRuntime().exec(new String[]{"rundll32", "url.dll,FileProtocolHandler", url});
//                } else if (os.contains("mac")) {
//                    Runtime.getRuntime().exec(new String[]{"open", url});
//                } else if (os.contains("nix") || os.contains("nux")) {
//                    Runtime.getRuntime().exec(new String[]{"xdg-open", url});
//                } else {
//                    System.err.println("No supported method to open browser");
//                }
//            }
//        } catch (IOException e) {
//            System.err.println("Failed to open browser: " + e.getMessage());
//        }
//    }
//    private static void openBrowser(String url) {
//        try {
//            java.awt.Desktop.getDesktop().browse(new java.net.URI(url));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
