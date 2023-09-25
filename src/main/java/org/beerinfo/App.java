package org.beerinfo;

import org.beerinfo.handlers.beer.*;
import org.beerinfo.handlers.brewery.GetAllBreweriesHandler;
import org.beerinfo.handlers.brewery.UpdateBreweryByIdHandler;
import org.beerinfo.service.BeerService;
import org.beerinfo.service.BreweriesService;
import org.beerinfo.utils.HibernateUtil;
import org.hibernate.SessionFactory;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        port(8080);
        init();

        // Get the SessionFactory from HibernateUtil
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        // Create the BeerService instance with the SessionFactory
        BeerService beerService = new BeerService(sessionFactory);
        BreweriesService breweriesService = new BreweriesService(sessionFactory);

        new GetAllBeersHandler(beerService).registerRoute();
        new GetBeerByIdHandler(beerService).registerRoute();
        new DeleteBeerByIdHandler(beerService).registerRoute();
        new AddBeerHandler(beerService, breweriesService).registerRoute();
        new UpdateBeerByIdHandler(beerService).registerRoute();
        new GetAllBreweriesHandler(breweriesService).registerRoute();
        new UpdateBreweryByIdHandler(breweriesService).registerRoute();

        new WrongEndpointRequestHandler().registerRoute();

        // Add a shutdown hook to gracefully stop the server
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stop();
            System.out.println("Server stopped");
        }));
    }
}