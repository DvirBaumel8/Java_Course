package TripSuggestUtil;

import Manager.EngineManager;
import Time.Time;
import XML.XMLLoading.jaxb.schema.generated.TransPoolTrip;
import javafx.beans.binding.StringBinding;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TripSuggestUtil {
    private int nextSuggestID;
    private Map<Integer, TripSuggest> suggestedTrips;

    private Map<String, List<String>> suggestedTripsDto = new HashMap<>();
    static final int INPUT_ADD_TRIP_SUGGEST_SIZE = 9;


    public TripSuggestUtil() {
        this.nextSuggestID = 1;
        this.suggestedTrips = new HashMap<>();
    }

    public static int compareHours(Time time1, Time time2) {
        int hour1 = time1.getHours();
        int minutes1 = time1.getMinutes();
        int hour2 = time2.getHours();
        int minutes2 = time2.getMinutes();

        int totalTime1minutes = (hour1 * 60) + (minutes1);
        int totalTime2minutes = (hour2 * 60) + (minutes2);

        if (totalTime1minutes > totalTime2minutes) {
            return 1;
        }
        if (totalTime1minutes == totalTime2minutes) {
            return 2;
        }
        if (totalTime1minutes < totalTime2minutes) {
            return 3;
        }
        return 0;
    }

    public void convertPlannedTripsToSuggestedTrips(List<TransPoolTrip> plannedTrips) {
        for (TransPoolTrip trip : plannedTrips) {
            int ppk = trip.getPPK();
            TripSuggest tripSuggest = new TripSuggest(trip.getOwner(), trip.getRoute(), 0, trip.getScheduling().getHourStart(), trip.getScheduling().getDayStart(), getTripScheduleTypeInt(trip.getScheduling().getRecurrences()), ppk, trip.getCapacity());
            addSuggestTrip(tripSuggest);
        }
    }

    public void addSuggestTrip(TripSuggest suggestTrip) {
        suggestTrip.setSuggestID(nextSuggestID);
        suggestedTrips.put(nextSuggestID, suggestTrip);
        nextSuggestID++;
    }

    public Map<Integer, TripSuggest> getAllSuggestedTrips() {
        return suggestedTrips;
    }

    int getTripScheduleTypeInt(String tripScheduleType) {
        int res = 0;
        switch (tripScheduleType) {
            case "OneTime":
                res = 1;
                break;
            case "Daily":
                res = 2;
                break;
            case "BiDaily":
                res = 3;
                break;
            case "Weekly":
                res = 4;
                break;
            case "Monthly":
                res = 5;
                break;
        }

        return res;
    }

    public static int calcRequiredFuel(String route) {
        int sum = 0;
        String[] paths = route.split(",");
        for (int i = 0; i < paths.length - 1; i++) {
            sum += EngineManager.getEngineManagerInstance().getRequiredFuelToPath(paths[i], paths[i + 1]);
        }
        return sum;
    }

    public TripSuggest getTripSuggestByID(int suggestID) {
        return suggestedTrips.get(suggestID);
    }


    //-----------------------------------Ranking Func------------------------------------------


    public void addSuggestedTripsDto(String[] displayTripSuggestStrArr) {
        List<String> suggestedTripsDtoObj = new LinkedList<>();

        for (int i = 0; i < INPUT_ADD_TRIP_SUGGEST_SIZE; i++) {
            suggestedTripsDtoObj.add(displayTripSuggestStrArr[i]);
        }
        suggestedTripsDto.put(String.valueOf(nextSuggestID - 1), suggestedTripsDtoObj);
    }

    public Map<String, List<String>> getSuggestedTripsDto() {
        return suggestedTripsDto;
    }

    public void setNumOfRanksInTripSuggestMapDto(String tripSuggestId) {
        List<String> suggestedTripDto = suggestedTripsDto.get(tripSuggestId);
        StringBuilder numOfRank = new StringBuilder(suggestedTripDto.get(7));
        Character resNumOfRank = numOfRank.charAt(16);
        int intNumOfRank = Integer.valueOf(resNumOfRank.toString());
        intNumOfRank++;
        char charNumOfRanks = (char) (intNumOfRank + '0');
        numOfRank.setCharAt(16, charNumOfRanks);
        suggestedTripDto.set(7, numOfRank.toString());
        suggestedTripsDto.put(tripSuggestId, suggestedTripDto);
        // "-Average Rank:X";
        // = "-Num of ranking:0";
        //= "-Comments:";
    }

    public void setAverageInTripSuggestMapDto(String currRank, String tripSuggestId) {
        List<String> suggestedTripDto = suggestedTripsDto.get(tripSuggestId);
        StringBuilder rank = new StringBuilder(suggestedTripDto.get(6));
        Character resRank = rank.charAt(14);
        int intRank = 0;
        if (resRank >= '0' && resRank <= '5') {
            intRank = Integer.valueOf(resRank.toString());
        } else {
            intRank = Integer.parseInt(currRank);
        }

        StringBuilder numOfRank = new StringBuilder(suggestedTripDto.get(7));
        Character resNumOfRank = numOfRank.charAt(16);
        int intNumOfRankBefore = Integer.valueOf(resNumOfRank.toString()) - 1;
        int ranksBefore = intRank * intNumOfRankBefore;
        intRank = (ranksBefore + Integer.valueOf(currRank)) / (intNumOfRankBefore + 1);

        char charNewResRank = (char) (intRank + '0');
        rank.setCharAt(14, charNewResRank);
        suggestedTripDto.set(6, rank.toString());
        suggestedTripsDto.put(tripSuggestId, suggestedTripDto);
    }

    public void setAverageReviewsInTripSuggestMapDto(String newReview, String tripSuggestId) {
        List<String> suggestedTripDto = suggestedTripsDto.get(tripSuggestId);
        StringBuilder newReviewSb = new StringBuilder(suggestedTripDto.get(8));
        newReviewSb.append(newReview + ",");
        suggestedTripDto.set(8, newReviewSb.toString());
        suggestedTripsDto.put(tripSuggestId, suggestedTripDto);
    }

    public List<String> getTripSuggestDtoObj(String suggestId) {
        return suggestedTripsDto.get(suggestId);
    }

    //return 1 if the first is higher then the second return 2 if equals return 3 if the second is higher then first, 0 - error
    public static int compareTimes(Time time1, Time time2) {
        int day1 = time1.getDay();
        int hour1 = time1.getHours();
        int minutes1 = time1.getMinutes();
        int day2 = time2.getDay();
        int hour2 = time2.getHours();
        int minutes2 = time2.getMinutes();

        int totalMinutesTime1 = (day1 * 24 * 60) + (hour1 * 60) + (minutes1);
        int totalMinutesTime2 = (day2 * 24 * 60) + (hour2 * 60) + (minutes2);

        if (totalMinutesTime1 > totalMinutesTime2) {
            return 1;
        }
        if (totalMinutesTime1 == totalMinutesTime2) {
            return 2;
        }
        if (totalMinutesTime1 < totalMinutesTime2) {
            return 3;
        }
        return 0;
    }

    public static int compareTimes(int totalMinutes1, int totalMinutes2) {
        if (totalMinutes1 > totalMinutes2) {
            return 1;
        }
        if (totalMinutes1 == totalMinutes2) {
            return 2;
        }
        return 3;
    }

    public void updateSuggestedTrips() {
        for(Map.Entry<Integer, TripSuggest> entry : suggestedTrips.entrySet()) {
            entry.getValue().updateIsActive(EngineManager.getEngineManagerInstance().getCurrentSystemTime());
            entry.getValue().updateCapacityPerTime(EngineManager.getEngineManagerInstance().getCurrentSystemTime());
        }
    }
}

