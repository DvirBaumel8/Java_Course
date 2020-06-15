package TripSuggestUtil;

import java.util.ArrayList;
import java.util.List;

public class DriverRating {
    private double ratingAVG;
    private int numOfRatings;
    private List<String> literallyRatings;

    public DriverRating() {
        literallyRatings = new ArrayList<>();
    }

    public void addOneToNumOfRatings() {
        this.numOfRatings++;
    }

    public String getDriverRatingInfo() {
        StringBuilder str = new StringBuilder();
        str.append(String.format("Average driver rating- %f\n", ratingAVG));
        str.append(String.format("Amount of raters- %d\n", numOfRatings));
        str.append(String.format("Literal ratings:\n"));
        for(String literalRating : literallyRatings) {
            str.append(literalRating);
            str.append("\n");
        }

        return str.toString();
    }

    public void addRatingToDriver(int rating) {
        addOneToNumOfRatings();
        ratingAVG = (ratingAVG + rating)/2.0;
    }

    public void addRatingToDriver(int rating, String literalRating) {
        addOneToNumOfRatings();
        ratingAVG = (ratingAVG + rating)/numOfRatings;
        literallyRatings.add(literalRating);
    }

    public double getRatingAVG() {
        return ratingAVG;
    }

    public int getNumOfRatings() {
        return numOfRatings;
    }

    public List<String> getLiterallyRatings() {
        return literallyRatings;
    }
}
