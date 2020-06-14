package controllers;
import MatchingUtil.RoadTrip;
import TripRequests.TripRequest;
import TripSuggestUtil.TripSuggest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Map;

public class TripSuggestController {
    private AppController mainController;

    @FXML
    private Button addTripSuggestButton;

    @FXML
    Accordion tripSuggestAccordion;


    ArrayList<TextField> inputAddTripSuggest = null;
    static final int INPUT_ADD_TRIP_SUGGEST_SIZE = 7;
    Stage addTripSuggestStage = null;

    Stage rankStage = null;
    TextField rankingTextField = null;


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public TripSuggestController() {
        this.inputAddTripSuggest = new ArrayList<>(INPUT_ADD_TRIP_SUGGEST_SIZE);
    }

    @FXML
    void addTripSuggestButtonActionListener() {
        if(mainController.isXMLLoaded()) {
            getAddTripSuggestWindow();
        }
        else {
            mainController.getAlertErrorWindow("XML doesnt load yet - please load one");
        }
    }


    @FXML
    void rankTripSuggestButtonActionListener() {
        if(mainController.isXMLLoaded() && mainController.getMatchingAccordion() != null) {
            getRankTripSuggestWindow();
        }
        else {
            mainController.getAlertErrorWindow("XML doesnt load yet / No match available");
        }
    }

    void getRankTripSuggestWindow() {
        rankStage = new Stage();
        VBox rankWindow = new VBox();
        javafx.geometry.Insets margin = new javafx.geometry.Insets(10,10,10,10);
        rankWindow.setSpacing(10);

        Map<TripRequest, RoadTrip> matchingTripSuggests = mainController.getAllMatchingTripSuggestMap();

        Label rankLabel = new Label("Rank your suggest driver from the following suggest trips which have match:"
                + System.lineSeparator() +  System.lineSeparator() +
                matchingTripSuggests.toString() +
                System.lineSeparator() + System.lineSeparator() +
                "In the following way:" + System.lineSeparator() +
                "Separated numbers with , :" + System.lineSeparator() +
                "Suggest ID, Rank number between 1 to 5 (1-Basa 5-Sababa)" + System.lineSeparator() +
                "For example:" + System.lineSeparator() +
                "1,4 (1 - suggest trip id, 4 - almost sababa)");
        rankWindow.setPrefWidth(600);
        rankWindow.getChildren().add(rankLabel);

        rankingTextField = new TextField("1,4");
        rankWindow.getChildren().add(rankingTextField);

        Button matchingLoadButton= new Button("Rank");
        rankingTextField.setPrefWidth(50);
        matchingLoadButton.setTranslateX(165);
        matchingLoadButton.setOnAction(this::rankLoadButtonAction);
        rankWindow.getChildren().add(matchingLoadButton);

        rankWindow.setMargin(rankLabel, margin);
        rankWindow.setMargin(rankingTextField, margin);
        rankWindow.setMargin(matchingLoadButton, margin);

        Scene scene = new Scene(rankWindow, 600, 400);

        rankStage.setTitle("Matching Action");
        rankStage.setScene(scene);
        rankStage.show();
    }

    private void rankLoadButtonAction(ActionEvent event) {

    }

    private void addInputTripSuggestButtonAction(ActionEvent event) {
        String[] inputTripSuggestString = new String[INPUT_ADD_TRIP_SUGGEST_SIZE];
        int index = 0;
        for(TextField inputTextField : inputAddTripSuggest) {
            inputTripSuggestString[index] = inputTextField.getText();
            index ++;
        }
        mainController.addTripSuggestAction(inputTripSuggestString);
    }

        public void loadTripSuggestFromXML() {
           Map<TripSuggest, Integer> initTripSuggest = mainController.getTripSuggestMap();
               if(!initTripSuggest.isEmpty()) {
                 initTripSuggest.forEach((tripSuggest,tripSuggestID) -> {
                addNewTripSuggestAccordion(tripSuggest);
                 });
              }
            }


    void getAddTripSuggestWindow() {
        addTripSuggestStage = new Stage();
        VBox addTripSuggestWindow = new VBox();
        javafx.geometry.Insets margin = new javafx.geometry.Insets(12,12,12,12);
        javafx.geometry.Insets generalMargin = new javafx.geometry.Insets(0,4,0,4);
        addTripSuggestWindow.setSpacing(10);
        //addTripSuggestWindow.setBackground((new Background(new BackgroundFill(Color.gray(0.865),
          //      CornerRadii.EMPTY, Insets.EMPTY))));

        Label detailsLabel = new Label("Please insert the following details:");
        detailsLabel.setTranslateX(15);

        detailsLabel.setFont(new javafx.scene.text.Font("Arial", 21));
        addTripSuggestWindow.getChildren().add(detailsLabel);

        //-----------------------------------------------------

        String allStationsNames = mainController.getAllStationsNames();
        Label allStationsNamesLabel = new Label(allStationsNames);
        allStationsNamesLabel.setTranslateX(15);
        addTripSuggestWindow.getChildren().add(allStationsNamesLabel);

        //-----------------------------------------------------

        Label exampleLabel = new Label("EXAMPLE: Dvir,A-B-C,3,13:25,4,30,2");
        exampleLabel.setTranslateX(15);
        addTripSuggestWindow.getChildren().add(exampleLabel);

        //-----------------------------------------------------

        Label nameOfOwnerLabel = new Label("- Name of owner:");
        nameOfOwnerLabel.setTextFill(Color.web("#0076a3"));
        nameOfOwnerLabel.setPrefWidth(300);
        addTripSuggestWindow.getChildren().add(nameOfOwnerLabel);
        nameOfOwnerLabel.setTranslateX(15);

        inputAddTripSuggest.add(new TextField("Dvir"));
        inputAddTripSuggest.get(0).setMaxWidth(250);
        inputAddTripSuggest.get(0).setTranslateX(15);
        addTripSuggestWindow.getChildren().add(inputAddTripSuggest.get(0));

        //-----------------------------------------------------

        Label suggestedTripLabel = new Label("- Route of suggested trip separate with '-'");
        suggestedTripLabel.setTextFill(Color.web("#0076a3"));
        suggestedTripLabel.setPrefWidth(300);
        addTripSuggestWindow.getChildren().add(suggestedTripLabel);
        suggestedTripLabel.setTranslateX(15);

        inputAddTripSuggest.add(new TextField("A-B-C"));
        inputAddTripSuggest.get(1).setMaxWidth(250);
        inputAddTripSuggest.get(1).setTranslateX(15);
        addTripSuggestWindow.getChildren().add(inputAddTripSuggest.get(1));

        //-----------------------------------------------------

        Label arrivalDayNumberLabel = new Label("- Arrival Day Number:");
        arrivalDayNumberLabel.setTextFill(Color.web("#0076a3"));
        arrivalDayNumberLabel.setTranslateX(15);
        arrivalDayNumberLabel.setPrefWidth(300);
        addTripSuggestWindow.getChildren().add(arrivalDayNumberLabel);

        arrivalDayNumberLabel.setTranslateX(15);
        inputAddTripSuggest.add(new TextField("3"));
        inputAddTripSuggest.get(2).setMaxWidth(250);
        inputAddTripSuggest.get(2).setTranslateX(15);
        addTripSuggestWindow.getChildren().add(inputAddTripSuggest.get(2));

        //-----------------------------------------------------

        Label startTimeLabel = new Label("- Starting Time:" + System.lineSeparator() +
                "* Hours at 24 (0 - 23) " + System.lineSeparator() +
                "* Minutes in multiples of 5 (0 - 55)");
        startTimeLabel.setTextFill(Color.web("#0076a3"));
        startTimeLabel.setTranslateX(15);

        addTripSuggestWindow.getChildren().add(startTimeLabel);

        inputAddTripSuggest.add(new TextField("11:35"));
        inputAddTripSuggest.get(3).setMaxWidth(250);
        inputAddTripSuggest.get(3).setTranslateX(5);
        inputAddTripSuggest.get(3).setTranslateX(15);
        addTripSuggestWindow.getChildren().add(inputAddTripSuggest.get(3));

        //-----------------------------------------------------

        Label tripScheduleTypeLabel = new Label("-Trip schedule type:" + System.lineSeparator() +
                "* insert 1 - one time" + System.lineSeparator() +
                "* insert 2 - daily" + System.lineSeparator() +
                "* insert 3 - twice a week" + System.lineSeparator() +
                "* insert 4 - weekly" + System.lineSeparator() +
                "* insert 5 - monthly");
        tripScheduleTypeLabel.setTextFill(Color.web("#0076a3"));
        tripScheduleTypeLabel.setTranslateX(15);
        addTripSuggestWindow.getChildren().add(tripScheduleTypeLabel);

        inputAddTripSuggest.add(new TextField("4"));
        inputAddTripSuggest.get(4).setMaxWidth(250);
        inputAddTripSuggest.get(4).setTranslateX(15);
        addTripSuggestWindow.getChildren().add(inputAddTripSuggest.get(4));

        //-----------------------------------------------------

        Label ppkLabel = new Label("-PPK: cost of trip per kilometer");
        ppkLabel.setTextFill(Color.web("#0076a3"));
        ppkLabel.setPrefWidth(400);
        ppkLabel.setTranslateX(15);
        addTripSuggestWindow.getChildren().add(ppkLabel);

        inputAddTripSuggest.add(new TextField("30"));
        inputAddTripSuggest.get(5).setMaxWidth(250);
        inputAddTripSuggest.get(5).setTranslateX(15);
        addTripSuggestWindow.getChildren().add(inputAddTripSuggest.get(5));

        //-----------------------------------------------------

        Label passengersCapacityLabel = new Label("-Passengers capacity");
        passengersCapacityLabel.setTextFill(Color.web("#0076a3"));
        passengersCapacityLabel.setPrefWidth(400);
        passengersCapacityLabel.setTranslateX(15);
        addTripSuggestWindow.getChildren().add(passengersCapacityLabel);

        inputAddTripSuggest.add(new TextField("2"));
        inputAddTripSuggest.get(6).setMaxWidth(250);
        inputAddTripSuggest.get(6).setTranslateX(15);
        addTripSuggestWindow.getChildren().add(inputAddTripSuggest.get(6));

        //-----------------------------------------------------

        Button addInputTripSuggestButton= new Button("Add");

        addInputTripSuggestButton.setMinWidth(150);
        addInputTripSuggestButton.setTranslateY(5);
        addInputTripSuggestButton.setTranslateX(15);
        addInputTripSuggestButton.setOnAction(this::addInputTripSuggestButtonAction);
        addTripSuggestWindow.getChildren().add(addInputTripSuggestButton);

        ScrollPane scrollPane = new ScrollPane(addTripSuggestWindow);
        scrollPane.setMaxHeight(900);
        scrollPane.setMaxWidth(500);

        scrollPane.setMinWidth(250);
        scrollPane.setMinHeight(500);

        scrollPane.setBackground((new Background(new BackgroundFill(Color.gray(0.865),
                CornerRadii.EMPTY, Insets.EMPTY))));

        Scene scene = new Scene(scrollPane, 500, 900);

        addTripSuggestStage.setTitle("Add New Trip Suggest");
        addTripSuggestStage.setScene(scene);
        addTripSuggestStage.show();
        addTripSuggestStage.setMaxHeight(scrollPane.getHeight());

    }


    public void closeAddNewTripSuggestStage() {
        addTripSuggestStage.close();
    }

    public void addNewTripSuggestAccordion(TripSuggest newSuggest) {
        String scheduleTypeString = String.valueOf(newSuggest.getRecurrencesType());

        TextArea newTripSuggestTextArea =
                new TextArea("-Suggest trip:" + newSuggest.getTripRoute() + System.lineSeparator() +
                        "-Starting day:" + newSuggest.getStartingDay() + System.lineSeparator() +
                        "-Starting time:" + newSuggest.getStartingTime().toString() + System.lineSeparator() +
                        "-Schedule type:" + scheduleTypeString + System.lineSeparator() +
                        "-PPK:" + newSuggest.getPpk() + System.lineSeparator() +
                        "- Pass capacity:" + newSuggest.getPassengers());
        newTripSuggestTextArea.setPrefRowCount(6);
        TitledPane title = new TitledPane(newSuggest.getTripOwnerName() + ", id:" + newSuggest.getSuggestID(),
                newTripSuggestTextArea);
        // title.setOnMouseClicked(event-> tripsAccordionOnAction());
        int sizeOfCurrentPanes = tripSuggestAccordion.getPanes().size();
        tripSuggestAccordion.getPanes().add(sizeOfCurrentPanes,title);

        //liveMapComponentController.updateTripOnMap(trip);
    }
}
