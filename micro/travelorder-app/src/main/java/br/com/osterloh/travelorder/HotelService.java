package br.com.osterloh.travelorder;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.temporal.ChronoUnit;

@RegisterRestClient(baseUri = "http://hotel-app-johnatanosterloh-dev.apps.sandbox-m4.g2pi.p1.openshiftapps.com/hotel")
public interface HotelService {

    @GET
    @Path("findById")
    @Produces(MediaType.APPLICATION_JSON)
    Hotel findById(@QueryParam("id") long id);

    @GET
    @Path("findByTravelOrderId")
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(unit = ChronoUnit.SECONDS, value = 2)
    @Fallback(fallbackMethod = "fallback")
    @CircuitBreaker(requestVolumeThreshold = 4, failureRatio = 0.5, delay = 5000, successThreshold = 2)
    Hotel findByTravelOrderId(@QueryParam("travelOrderId") long ravelOrderId);

    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @POST
    Hotel newHotel(Hotel hotel);

    default Hotel fallback(long travelOrderId){
        Hotel hotel = new Hotel();
        hotel.setNights(-1);
        hotel.setTravelOrderId(travelOrderId);
        return hotel;
    }
}
