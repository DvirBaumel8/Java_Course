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
    private Time startArrivalTime;
    private String RoadStory;
    private List<TripSuggest> ratedTripSuggested;
    private TripRequest tripRequest;

    public RoadTrip() {
        completeTrip = new LinkedList<>();
        ratedTripSuggested = new ArrayList<>();
    }

    public RoadTrip(RoadTrip roadTrip) {
        this.completeTrip = roadTrip.getParticipantSuggestTripsToRoadPart();
        this.totalCost = getTotalCost();
        this.requiredFuel = roadTrip.getRequiredFuel();
        this.startArrivalTime = roadTrip.getStartArrivalTime();
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

    public Time getStartArrivalTime() {
        return startArrivalTime;
    }

    public String getRoadStory() {
        return RoadStory;
    }

    public LinkedList<SubTrip> getParticipantSuggestTripsToRoadPart() {
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
            fuel += subTrip.getCost();
        }

        this.totalCost = fuel;
    }

    public void calcStartArrivalTime() {

    }

    public void buildRoadTripStory() {
//        StringBuilder str = new StringBuilder();
//        str.append("Road Story:/n");
//        for(Map.Entry<TripSuggest,Route> entry : completeTrip.entrySet()) {
//            String route = entry.getValue().getPath();
//            str.append(String.format("Go up to %s's car in station %s\n", entry.getKey().getTripOwnerName(), route.split(",")[0]));
//            str.append(String.format("Go down from %s's car in station %s\n", entry.getKey().getTripOwnerName(), route.split(",")[route.split(",").length - 1]));
//        }
//        str.append(String.format("Total trip cost: %d\n",this.totalCost));
//        str.append(String.format("Total required fuel: %d\n", this.requiredFuel));
//        if(tripRequest.isRequestByStartTime()) {
//            str.append(String.format("Arrival time: %s", tripRequest.getArrivalTime().toString()));
//        }
//        else {
//            str.append(String.format("Starting time: %s", tripRequest.getStartTime().toString()));
//        }
//
//        this.RoadStory = str.toString();
    }

    private int calculateRoutePriceByPpk(int ppk, Route route) {
        int sum = 0;
        String[] paths = route.getPath().split(",");
        for(int i = 0; i < paths.length - 1; i++) {
            int km = getLengthBetweenStations(paths[i], paths[i+1]);
            sum += km * ppk;
        }
        return sum;
    }

    private int getLengthBetweenStations(String pathFrom, String pathTo) {
        return EngineManager.getEngineManagerInstance().getLengthBetweenStations(pathFrom, pathTo);
    }

    public Station getLastStation() {
        return completeTrip.getLast().getLastStation();
    }

    public Station getFirstStation() {
        return completeTrip.getFirst().getFirstStation();
    }

}
