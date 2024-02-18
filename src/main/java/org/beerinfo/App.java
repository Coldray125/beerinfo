package org.beerinfo;

import io.javalin.Javalin;
import org.beerinfo.handlers.beer.*;
import org.beerinfo.handlers.brewery.GetAllBreweriesHandler;
import org.beerinfo.handlers.brewery.UpdateBreweryByIdHandler;
import org.beerinfo.service.BeerService;
import org.beerinfo.service.BreweriesService;
import org.beerinfo.utils.HibernateUtil;
import org.hibernate.SessionFactory;

public class App {
    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.http.defaultContentType = "application/json";
        }).start(8080);

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        BeerService beerService = new BeerService(sessionFactory);
        BreweriesService breweriesService = new BreweriesService(sessionFactory);

        app.get("/beers", new GetAllBeersHandler(beerService));
        app.get("/beer", new GetBeerByIdHandler(beerService));
        app.delete("/beer", new DeleteBeerByIdHandler(beerService));
        app.post("/beer", new AddBeerHandler(beerService, breweriesService));
        app.put("/beer", new UpdateBeerByIdHandler(beerService));
        app.get("/breweries", new GetAllBreweriesHandler(breweriesService));
        app.put("/brewery", new UpdateBreweryByIdHandler(breweriesService));

        //app.error(404, new WrongEndpointRequestHandler());

        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));

        app.events(event -> {
            event.serverStopping(() -> { /* Your code here, executed when server is stopping */ });
            event.serverStopped(() -> { /* Your code here, executed after server has stopped */ });
        });
    }
}