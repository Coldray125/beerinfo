package org.beerinfo;

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.http.HttpResponseException;
import lombok.extern.slf4j.Slf4j;
import org.beerinfo.handlers.beer.*;
import org.beerinfo.handlers.brewery.GetAllBreweriesHandler;
import org.beerinfo.handlers.brewery.GetBreweryBeersHandler;
import org.beerinfo.handlers.brewery.UpdateBreweryByIdHandler;
import org.beerinfo.service.BeerService;
import org.beerinfo.service.BreweriesService;

import static org.beerinfo.config.OpenApiConfig.configureOpenApi;
import static org.beerinfo.config.OpenApiConfig.logDocumentationUrls;
import static org.beerinfo.config.PropertyUtil.getProperty;
import static org.beerinfo.db.PostgresSessionProvider.getBeerInfoSessionFactory;
import static org.beerinfo.utils.ResponseUtil.respondWithInternalServerError;

@Slf4j
public class App {
    private static final int PORT = Integer.parseInt(getProperty("application.port"));

    public static void main(String[] args) {
        BeerService beerService = new BeerService(getBeerInfoSessionFactory());
        BreweriesService breweriesService = new BreweriesService(getBeerInfoSessionFactory());

        Javalin app = Javalin.create(config -> {

            //global exception handler
            config.routes.exception(Exception.class, (e, ctx) -> {
                log.error("Unexpected server error at {} {}", ctx.method(), ctx.path(), e);
                respondWithInternalServerError(ctx);
            });

            config.routes.exception(HttpResponseException.class, new WrongEndpointHandler());

            config.routes.before(ctx -> {
                String logMessage = String.format("📥 %s %s | IP: %s:%d | Body: %s",
                        ctx.method(), ctx.fullUrl(), ctx.ip(), ctx.port(), ctx.body());
                log.info(logMessage);
            });

            config.routes.apiBuilder(() -> {
                configureOpenApi(config);
                config.http.defaultContentType = "application/json";
                ApiBuilder.get("/beers", new GetAllBeersHandler(beerService));
                ApiBuilder.get("/beer", new GetBeerByIdHandler(beerService));
                ApiBuilder.delete("/beer", new DeleteBeerByIdHandler(beerService));
                ApiBuilder.post("/beer", new AddBeerHandler(beerService, breweriesService));
                ApiBuilder.put("/beer", new UpdateBeerByIdHandler(beerService));
                ApiBuilder.get("/breweries", new GetAllBreweriesHandler(breweriesService));
                ApiBuilder.put("/brewery", new UpdateBreweryByIdHandler(breweriesService));
                ApiBuilder.get("/brewery-beers", new GetBreweryBeersHandler(breweriesService));
                ApiBuilder.get("/beer-brewery", new GetBeerBreweryHandler(beerService));
            });

            config.events.serverStopping(() -> log.info("server stopping"));
            config.events.serverStopped(() -> log.info("server stopped"));
        }).start(PORT);

        logDocumentationUrls(app);
    }
}