package Engine.MatchingUtil;

import Engine.TripRequests.TripRequest;
import Engine.TripSuggestUtil.TripSuggest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchingUtil {
    private Map<TripSuggest, List<TripRequest>> matches;

    public MatchingUtil() {
        matches = new HashMap<>();
    }

}
