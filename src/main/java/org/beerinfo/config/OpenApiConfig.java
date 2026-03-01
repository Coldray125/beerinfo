package org.beerinfo.config;

import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.redoc.ReDocPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import lombok.extern.slf4j.Slf4j;

/// [javalin openAPI example](https://github.com/javalin/javalin-openapi/blob/main/examples/javalin-gradle-kotlin/src/main/java/io/javalin/openapi/plugin/test/JavalinTest.java)
@Slf4j
public final class OpenApiConfig {
    public static final String SWAGGER_PATH = "/swagger";
    public static final String REDOC_PATH = "/redoc";
    public static final String OPENAPI_PATH = "/openapi";

    public static void configureOpenApi(JavalinConfig config) {
        config.registerPlugin(new OpenApiPlugin(pluginConfig -> {
            pluginConfig.withDocumentationPath(OPENAPI_PATH);
            pluginConfig.withDefinitionConfiguration((_, definition) -> {
                definition.info(info -> {
                    info.title("Javalin beerInfo OpenAPI example");
                    info.version("1.0.0");
                    info.description("Generated API documentation");
                });
            });
        }));

        config.registerPlugin(new SwaggerPlugin(swaggerConfig ->
                swaggerConfig.withUiPath(SWAGGER_PATH)));

        config.registerPlugin(new ReDocPlugin(reDocConfig ->
                reDocConfig.withUiPath(REDOC_PATH)));
    }

    public static void logDocumentationUrls(Javalin app) {
        String baseUrl = "http://localhost:" + app.port();
        log.info("");
        log.info("=".repeat(60));
        log.info("🚀 Server started on port {}", app.port());
        log.info("📚 Documentation:");
        log.info("   ├─ OpenAPI:    {}{}", baseUrl, OpenApiConfig.OPENAPI_PATH);
        log.info("   ├─ Swagger UI: {}{}", baseUrl, OpenApiConfig.SWAGGER_PATH);
        log.info("   └─ ReDoc:      {}{}", baseUrl, OpenApiConfig.REDOC_PATH);
        log.info("=".repeat(60));
        log.info("");
    }
}
