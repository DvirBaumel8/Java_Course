package TripRequests;

import Manager.EngineManager;
import Time.Time;
import TripSuggestUtil.TripSuggest;
import com.sun.xml.internal.bind.v2.TODO;

public class TripRequest {
    private int RequestID;
    private String OwnerName;
    private String sourceStation;
    private String destinationStation;
    private boolean isMatched;
    private TripSuggest matchTrip;
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

        public TripSuggest getMatchTrip () {
            return matchTrip;
        }

        public boolean isRequestByStartTime() {
            return requestByStartTime;
        }

        public void addRatingToSuggestTrip(int rating, String literalRating, boolean isLiteralRatingExist) {
            if(matchTrip == null) {
                //ToDO - return an error that tripRequest can't rate drivers before matching.
            }
            else {
                if(isLiteralRatingExist) {
                    matchTrip.addRatingToDriver(rating, literalRating);
                }
                else {
                    matchTrip.addRatingToDriver(rating);
                }
            }
        }

        public Time getStartTime() {
            return startTime;
        }

        public Time getArrivalTime() {
            return arrivalTime;
        }
    }
