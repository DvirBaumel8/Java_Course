package TripRequests;

import Manager.EngineManager;
import MatchingUtil.RoadTrip;
import Time.Time;
import TripSuggestUtil.TripSuggest;
import com.sun.xml.internal.bind.v2.TODO;

public class TripRequest {
    private int RequestID;
    private String OwnerName;
    private String sourceStation;
    private String destinationStation;
    private boolean isMatched;
    private RoadTrip matchTrip;
    private boolean requestByStartTime;
    private Time startTime;
    private Time arrivalTime;
    private boolean isStartTime;

    public TripRequest(String name, String sourceStation, String destinationStation, int minutes, int hours, int day, boolean requestByStartTime){
            this.OwnerName = name;
            this.sourceStation = sourceStation;
            this.destinationStation = destinationStation;
            this.isMatched = false;
            this.matchTrip = null;
            this.requestByStartTime = requestByStartTime;
            if(requestByStartTime) {
                this.startTime = new Time(minutes, hours, day);
            }
            else {
                this.arrivalTime = this.startTime = new Time(minutes, hours, day);
            }
            this.isStartTime = requestByStartTime;
        }

        public boolean getIsStartTime() {
            return this.isStartTime;
        }

        public String getNameOfOwner() {
            return OwnerName;
        }

        public boolean isMatched() {
            return isMatched;
        }

        public String getSourceStation() {
            return sourceStation;
        }

        public String getDestinationStation() {
            return destinationStation;
        }

        public int getRequestID() {
            return RequestID;
        }

        public void setRequestID(int requestID) {
            RequestID = requestID;
        }

        public RoadTrip getMatchTrip () {
            return matchTrip;
        }

        public boolean isRequestByStartTime() {
            return requestByStartTime;
        }

        public Time getStartTime() {
            return startTime;
        }

        public Time getArrivalTime() {
            return arrivalTime;
        }
    }
