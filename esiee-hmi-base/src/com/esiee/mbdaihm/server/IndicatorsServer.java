package com.esiee.mbdaihm.server;

import com.esiee.mbdaihm.Launch;
import com.esiee.mbdaihm.dataaccess.wdi.WDIDataDecoder;
import com.esiee.mbdaihm.datamodel.DataManager;
import com.esiee.mbdaihm.datamodel.countries.Country;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class used to launch a web server sending indicators data.
 */
public class IndicatorsServer extends AbstractVerticle
{
    private static final Logger LOGGER = LoggerFactory.getLogger(IndicatorsServer.class);

    /**
     * Entry point to launch the web server sending indicators data.
     *
     * @param args no argument used
     */
    public static void main(String[] args)
    {
        Launch.initData();

        Vertx.vertx().deployVerticle(new IndicatorsServer());
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        Router router = Router.router(vertx);
        router.route("/").handler(
                routingCtx ->
        {
            HttpServerResponse response = routingCtx.response();
            response.
                    putHeader("content-type", "text/html").
                    end("<h1>Indicator server running...</h1>"
                            + "<p>Use /indicators URL to get list of availables indicators</p>"
                            + "<p>Use /indicators/INDICATOR_NAME URL to get values for a specific indicator</p>"
                            + "<p>Use /indicators/INDICATOR_NAME?country=XXX to filter for country XXX</p>");

        }
        );

        router.get("/indicators").handler(this::getIndicators);

        router.get("/indicators/:indicatorName").handler(this::getIndicator);

        vertx.createHttpServer().
                requestHandler(router::accept).
                listen(8080, result ->
               {
                   if (result.succeeded())
                   {
                       startFuture.complete();
                   }
                   else
                   {
                       startFuture.fail(result.cause());
                   }
               });
    }

    private void getIndicators(RoutingContext context)
    {
        context.request().headers().entries().forEach(
                e -> System.out.println(e.getKey() + " = " + e.getValue()));

        context.request().params().forEach(
                e -> System.out.println(e.getKey() + " = " + e.getValue()));

        context.response().
                putHeader("content-type", "application/json; charset=utf-8").
                end(Json.encode(
                        DataManager.INSTANCE.getIndicators().collect(Collectors.toList())));

    }

    private void getIndicator(RoutingContext context)
    {
        String name = context.request().getParam("indicatorName");

        LOGGER.info("Asking for indicator {} values.", name);

        List<String> countriesParams = context.request().params().getAll("country");

        // Decoding the indicator will change Countries content as a side effect
        WDIDataDecoder.decode(Launch.WDI_FOLDER, name);

        List<Country> populatedCountries = DataManager.INSTANCE.getCountries();

        String response;

        if (countriesParams.isEmpty())
        {
            response = Json.encode(populatedCountries);
        }
        else
        {
            response = Json.encode(populatedCountries.stream().
                    filter(c -> countriesParams.contains(c.getIsoCode())).
                    collect(Collectors.toList()));
        }

        context.response().
                putHeader("content-type", "application/json; charset=utf-8").
                end(response);
    }
}
