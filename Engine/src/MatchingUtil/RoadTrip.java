package MatchingUtil;

import Manager.EngineManager;
import Time.Time;
import TripRequests.TripRequest;
import TripSuggestUtil.TripSuggest;
import XML.XMLLoading.jaxb.schema.generated.Route;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RoadTrip {
    private LinkedList<SubTrip> completeTrip;
    private int totalCost;
    private int requiredFuel;
    private Time startTime;
    private Time arrivalTime;
    private String RoadStory;
    private List<TripSuggest> ratedTripSuggested;
    private TripRequest tripRequest;

    public RoadTrip() {
        completeTrip = new LinkedList<>();
        ratedTripSuggested = new ArrayList<>();
    }

    public RoadTrip(RoadTrip roadTrip) {
        this.completeTrip = roadTrip.getSubTrips();
        this.totalCost = getTotalCost();
        this.requiredFuel = roadTrip.getRequiredFuel();
        this.startTime = roadTrip.getStartTime();
        this.arrivalTime = roadTrip.getArrivalTime();
        this.RoadStory = roadTrip.getRoadStory();
        this.ratedTripSuggested = roadTrip.getRatedTripSuggested();
        this.tripRequest = roadTrip.getTripRequest();
    }

    public TripRequest getTripRequest() {
        return tripRequest;
    }

    public void setTripRequest(TripRequest tripRequest) {
        this.tripRequest = tripRequest;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public int getRequiredFuel() {
        return requiredFuel;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getArrivalTime() {
        return arrivalTime;
    }

    public String getRoadStory() {
        return RoadStory;
    }

    public LinkedList<SubTrip> getSubTrips() {
        return completeTrip;
    }

    public List<TripSuggest> getRatedTripSuggested() {
        return ratedTripSuggested;
    }

    public void addSubTripToRoadTrip(SubTrip subTrip) {
        this.completeTrip.add(subTrip);
    }

    public void calcTotalCost() {
        int cost = 0;
        for (SubTrip subTrip : completeTrip) {
            cost += subTrip.getCost();
        }

        this.totalCost = cost;
    }

    public void calcRequiredFuel() {
        int fuel = 0;
        for (SubTrip subTrip : completeTrip) {
            fuel += subTrip.getRequiredFuel();
        }

        this.requiredFuel = fuel;
    }

    public void calcStartArrivalTime() {
        this.startTime = completeTrip.getFirst().getStartTime();
        this.arrivalTime = completeTrip.getLast().getArrivalTime();
    }

    public void buildRoadTripStory() {
        StringBuilder str = new StringBuilder();
        str.append("Road Story:\n");
        for(SubTrip subTrip : completeTrip) {
            str.append(subTrip.getSubTripStory());
        }
        str.append(String.format("Total trip cost: %d\n",this.totalCost));
        str.append(String.format("Total required fuel: %d\n", this.requiredFuel));
        if(tripRequest.isRequestByStartTime()) {
            str.append(String.format("Arrival time: %s", arrivalTime.toString()));
        }
        else {
            str.append(String.format("Starting time: %s", startTime.toString()));
        }

        this.RoadStory = str.toString();
    }

    public Station getLastStation() {
        return completeTrip.getLast().getLastStation();
    }

    public Station getFirstStation() {
        return completeTrip.getFirst().getFirstStation();
    }

}
