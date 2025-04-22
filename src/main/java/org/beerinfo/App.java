package org.beerinfo;

import io.javalin.Javalin;
import io.javalin.http.HttpResponseException;
import lombok.extern.slf4j.Slf4j;
import org.beerinfo.handlers.beer.*;
import org.beerinfo.handlers.brewery.GetAllBreweriesHandler;
import org.beerinfo.handlers.brewery.GetBreweryBeersHandler;
import org.beerinfo.handlers.brewery.UpdateBreweryByIdHandler;
import org.beerinfo.service.BeerService;
import org.beerinfo.service.BreweriesService;

import static org.beerinfo.db.PostgresSessionProvider.getBeerInfoSessionFactory;

@Slf4j
public class App {
    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
        }).start(8080);

        BeerService beerService = new BeerService(getBeerInfoSessionFactory());
        BreweriesService breweriesService = new BreweriesService(getBeerInfoSessionFactory());

        app.before(ctx -> {
            String logMessage = String.format("📥 %s %s | IP: %s:%d | Body: %s",
                    ctx.method(), ctx.fullUrl(), ctx.ip(), ctx.port(), ctx.body());
            log.info(logMessage);
        });

        app.get("/beers", new GetAllBeersHandler(beerService));
        app.get("/beer", new GetBeerByIdHandler(beerService));
        app.delete("/beer", new DeleteBeerByIdHandler(beerService));
        app.post("/beer", new AddBeerHandler(beerService, breweriesService));
        app.put("/beer", new UpdateBeerByIdHandler(beerService));
        app.get("/breweries", new GetAllBreweriesHandler(breweriesService));
        app.put("/brewery", new UpdateBreweryByIdHandler(breweriesService));
        app.get("/brewery-beers", new GetBreweryBeersHandler(breweriesService));
        app.get("/beer-brewery", new GetBeerBreweryHandler(beerService));

        app.exception(HttpResponseException.class, new WrongEndpointHandler());

        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));

        app.events(event -> {
            event.serverStopping(() -> log.info("server stopping"));
            event.serverStopped(() -> log.info("server stopped"));
        });
    }
}