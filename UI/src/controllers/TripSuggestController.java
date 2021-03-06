package controllers;
import TripSuggestUtil.TripSuggest;
import javafx.collections.ObservableList;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TripSuggestController {
    private AppController mainController;

    @FXML private Button addTripSuggestButton;

    @FXML private Accordion tripSuggestAccordion;


    private ArrayList<TextField> inputAddTripSuggest = null;
    static final int INPUT_ADD_TRIP_SUGGEST_SIZE = 7;
    static final int INPUT_ADD_TRIP_SUGGEST_SIZE_INCLUDE_RANKING = 9;
    private Stage addTripSuggestStage = null;

    private Stage rankMainStage = null;
    private TextField requestIdToRankSuggestIdTextField = null;

    private Stage rankSuggestIdByTripRequestStage = null;
    private TextField suggestIdToRankFromRequestIdRoadTrips = null;


    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public TripSuggestController() {
        this.inputAddTripSuggest = new ArrayList<>();
    }



    //-------------------------------Add TripSuggest Flow UI---------------------------------------
    @FXML
    void addTripSuggestButtonActionListener() {
        if(mainController.isXMLLoaded()) {
            getAddTripSuggestWindow();
        }
        else {
            List<String> errors = new LinkedList<>();
            errors.add("XML doesnt load yet - please load one");
            mainController.getAlertErrorWindow(errors);
        }
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

    void getAddTripSuggestWindow() {
        addTripSuggestStage = new Stage();
        VBox addTripSuggestWindow = new VBox();
        javafx.geometry.Insets margin = new javafx.geometry.Insets(12,12,12,12);
        javafx.geometry.Insets generalMargin = new javafx.geometry.Insets(0,4,0,4);
        addTripSuggestWindow.setSpacing(10);
        //addTripSuggestWindow.setBackground((new Background(new BackgroundFill(Color.gray(0.865),
        //      CornerRadii.EMPTY, Insets.EMPTY))));

        if(tripSuggestAccordion.getPanes().size() > 0) {
            this.inputAddTripSuggest = null;
            this.inputAddTripSuggest = new ArrayList<>();
        }

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

        Label startingDayNumber = new Label("- Starting Day Number:");
        startingDayNumber.setTextFill(Color.web("#0076a3"));
        startingDayNumber.setTranslateX(15);
        startingDayNumber.setPrefWidth(300);
        addTripSuggestWindow.getChildren().add(startingDayNumber);

        startingDayNumber.setTranslateX(15);
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

        String[] displayTripSuggestStrArr = new String[INPUT_ADD_TRIP_SUGGEST_SIZE_INCLUDE_RANKING];
        displayTripSuggestStrArr[0] = "-Suggest trip:" + newSuggest.getTripRoute().getPath();
        displayTripSuggestStrArr[1] = "-Starting day:" + newSuggest.getStartingDay();
        displayTripSuggestStrArr[2] = "-Starting time:" + newSuggest.getStartingTime().toString();
        displayTripSuggestStrArr[3] = "-Schedule type:" + scheduleTypeString;
        displayTripSuggestStrArr[4] = "-PPK:" + newSuggest.getPpk();
        displayTripSuggestStrArr[5] = "-Pass capacity:" + newSuggest.getstaticCapacity();
        displayTripSuggestStrArr[6] = "-Average Rank:X";
        displayTripSuggestStrArr[7] = "-Num of ranking:0";
        displayTripSuggestStrArr[8] = "-Comments:";

        mainController.addSuggestedTripsDto(displayTripSuggestStrArr);

        TextArea newTripSuggestTextArea =
                new TextArea(displayTripSuggestStrArr[0] + System.lineSeparator() +
                        displayTripSuggestStrArr[1] + System.lineSeparator() +
                        displayTripSuggestStrArr[2] + System.lineSeparator() +
                        displayTripSuggestStrArr[3] + System.lineSeparator() +
                        displayTripSuggestStrArr[4] + System.lineSeparator() +
                        displayTripSuggestStrArr[5] + System.lineSeparator() +
                        displayTripSuggestStrArr[6]  + System.lineSeparator() +
                        displayTripSuggestStrArr[7]  + System.lineSeparator() +
                        displayTripSuggestStrArr[8]  + System.lineSeparator());

        newTripSuggestTextArea.setPrefRowCount(9);
        TitledPane title = new TitledPane(newSuggest.getTripOwnerName() + ", id:" + newSuggest.getSuggestID(),
                newTripSuggestTextArea);
        // title.setOnMouseClicked(event-> tripsAccordionOnAction());
        int sizeOfCurrentPanes = tripSuggestAccordion.getPanes().size();
        tripSuggestAccordion.getPanes().add(sizeOfCurrentPanes,title);
        // liveMapComponentController.updateTripOnMap(trip);
    }

    public void resetTripSuggestAccordion() {
        int sizeOfCurrentPanes = tripSuggestAccordion.getPanes().size();

        for(int i = 0 ; i < sizeOfCurrentPanes ; i++) {
            this.tripSuggestAccordion.getPanes().remove(i);
        }
    }




    //-------------------------------Rank TripSuggest Flow UI---------------------------------------
    void getRankTripSuggestMainWindow() {
        rankMainStage = new Stage();
        VBox rankMainWindowVBox = new VBox();
        javafx.geometry.Insets margin = new javafx.geometry.Insets(5,5,5,5);
        rankMainWindowVBox.setSpacing(10);
        ScrollPane scrollRankMainWindow = new ScrollPane();

        List<String> matchingTripRequests = mainController.getAllMatchingTripRequestForRank();

        Label tripRequestIdsLabel = new Label("Here is all the trip request which have match:" + System.lineSeparator() + System.lineSeparator() +
                matchingTripRequests.toString() +
                System.lineSeparator() + System.lineSeparator() +
                "Please copy request id from the following options," + System.lineSeparator() +
                "Which you would like to rank his drivers:");
        tripRequestIdsLabel.setTranslateY(20);
        tripRequestIdsLabel.setTranslateX(10);
        rankMainWindowVBox.getChildren().add(tripRequestIdsLabel);

        requestIdToRankSuggestIdTextField = new TextField("request id Number");
        requestIdToRankSuggestIdTextField.setPrefWidth(135);
        requestIdToRankSuggestIdTextField.setMaxWidth(135);
        requestIdToRankSuggestIdTextField.setTranslateY(5);
        requestIdToRankSuggestIdTextField.setTranslateX(10);
        rankMainWindowVBox.getChildren().add(requestIdToRankSuggestIdTextField);

        Button displayAvailableSuggestIdToRankButton = new Button("Display available suggest id to rank");
        displayAvailableSuggestIdToRankButton.setTranslateX(90);
        displayAvailableSuggestIdToRankButton.setTranslateY(10);
        displayAvailableSuggestIdToRankButton.setOnAction(this::validateAndActionRequestIdInputForRank);
        rankMainWindowVBox.getChildren().add(displayAvailableSuggestIdToRankButton);

        rankMainWindowVBox.setMargin(tripRequestIdsLabel, margin);
        rankMainWindowVBox.setMargin(requestIdToRankSuggestIdTextField, margin);
        rankMainWindowVBox.setMargin(displayAvailableSuggestIdToRankButton, margin);

        scrollRankMainWindow.setContent(rankMainWindowVBox);

        Scene scene = new Scene(scrollRankMainWindow, 450, 350);

        rankMainStage.setTitle("Matching Action - choose request id");
        rankMainStage.setScene(scene);
        rankMainStage.show();
    }

    private void validateAndActionRequestIdInputForRank(ActionEvent event) {
        String rankSuggestID = null;
        List<String> errors = null;
        try {
            rankSuggestID = requestIdToRankSuggestIdTextField.getText();
            errors = mainController.validateRequestIdForRank(rankSuggestID);
            if  (errors.size() == 0) {
                rankSuggestIdByRequestIdWindow();
                rankMainStage.close();
            }
            else {
                throw new Exception();
            }
        }
        catch (Exception e) {
            if(errors.isEmpty()) {
                errors = new LinkedList<>();
                errors.add("You didnt choose rankRequestID from the following options, please try again.");
            }
            mainController.getAlertErrorWindow(errors);
        }
    }


    @FXML
    void rankTripSuggestButtonActionListener() {
        if(mainController.isXMLLoaded()) {
            getRankTripSuggestMainWindow();
        }
        else {
            List<String> errors = new LinkedList<>();
            errors.add("XML doesnt load yet / No match available");
            mainController.getAlertErrorWindow(errors);
        }
    }

    void rankSuggestIdByRequestIdWindow() throws Exception {
        rankSuggestIdByTripRequestStage = new Stage();
        VBox rankSuggestIdByTripRequestWindow = new VBox();
        javafx.geometry.Insets margin = new javafx.geometry.Insets(5,5,5,5);
        rankSuggestIdByTripRequestWindow.setSpacing(10);

        ScrollPane scrollPaneRankSuggestIdByTripRequestWindow = new ScrollPane();
        List<String> tripSuggestIdsFromRequestId = null;
        if(!requestIdToRankSuggestIdTextField.getText().isEmpty()){
            tripSuggestIdsFromRequestId = mainController.
                    getTripSuggestIdsFromTripRequestWhichNotRankYet(requestIdToRankSuggestIdTextField.getText());
        }

        Label rankLabel = new Label("Here is all the trip suggest id from the following trip request road trips:"
                + System.lineSeparator() +  System.lineSeparator() +
                tripSuggestIdsFromRequestId.toString() +
                System.lineSeparator() + System.lineSeparator() +
                "Please follow the following steps to rank:" + System.lineSeparator() +
                "Copy The input to the text field in the following way:" + System.lineSeparator() +
                "SuggestId Number,Rank Number,Review (NO SPACES)" + System.lineSeparator() +
                "SuggestId Number: for the trip suggest id list" + System.lineSeparator() +
                "Rank Digit: between 1 to 5, 1 - Basa | 5 - Sababa" + System.lineSeparator() +
                "Review: review on the driver");
        rankLabel.setTranslateY(20);
        rankLabel.setTranslateX(10);
        rankSuggestIdByTripRequestWindow.getChildren().add(rankLabel);

        suggestIdToRankFromRequestIdRoadTrips = new TextField("1,3,flowing driver");
        suggestIdToRankFromRequestIdRoadTrips.setMaxWidth(240);
        suggestIdToRankFromRequestIdRoadTrips.setPrefWidth(240);
        suggestIdToRankFromRequestIdRoadTrips.setTranslateY(5);
        suggestIdToRankFromRequestIdRoadTrips.setTranslateX(10);
        rankSuggestIdByTripRequestWindow.getChildren().add(suggestIdToRankFromRequestIdRoadTrips);

        Button suggestIdToRankFromRequestIdButton= new Button("Rank suggest id by road trips");
        suggestIdToRankFromRequestIdButton.setPrefWidth(210);
        suggestIdToRankFromRequestIdButton.setMaxWidth(210);
        suggestIdToRankFromRequestIdButton.setTranslateX(120);
        suggestIdToRankFromRequestIdButton.setTranslateY(10);
        suggestIdToRankFromRequestIdButton.setOnAction(this::rankSuggestIdByRequestIdRoadTripsAction);
        rankSuggestIdByTripRequestWindow.getChildren().add(suggestIdToRankFromRequestIdButton);

        rankSuggestIdByTripRequestWindow.setMargin(rankLabel, margin);
        rankSuggestIdByTripRequestWindow.setMargin(suggestIdToRankFromRequestIdRoadTrips, margin);
        rankSuggestIdByTripRequestWindow.setMargin(suggestIdToRankFromRequestIdButton, margin);

        scrollPaneRankSuggestIdByTripRequestWindow.setContent(rankSuggestIdByTripRequestWindow);

        Scene scene = new Scene(scrollPaneRankSuggestIdByTripRequestWindow, 480, 350);

        rankSuggestIdByTripRequestStage.setTitle("Matching Action - choose suggest id");
        rankSuggestIdByTripRequestStage.setScene(scene);
        rankSuggestIdByTripRequestStage.show();
    }

    private void rankSuggestIdByRequestIdRoadTripsAction(ActionEvent event) {
        List<String> errors = null;

        try {
            String[] inputs = suggestIdToRankFromRequestIdRoadTrips.getText().split(",");
            errors = mainController.validateInputOfRatingDriverOfSuggestIDAndRating(inputs[0], inputs[1], inputs[2]);
            if (errors.isEmpty()) {
                mainController.rankDriver(inputs);
                this.setRankFieldsAtSuggestIdAccordion(inputs);
                rankSuggestIdByTripRequestStage.close();
                mainController.getSuccessWindow("Ranking succeed");
            } else {
                mainController.getAlertErrorWindow(errors);
            }
        }
            catch (Exception e) {
                errors = new LinkedList<>();
                errors.add(e.getMessage());
                mainController.getAlertErrorWindow(errors);
            }
    }


    public void setRankFieldsAtSuggestIdAccordion(String[] inputs) {
        String tripSuggestId = inputs[0];
        String currRank = inputs [1];
        String currReview = inputs[2];

        ObservableList<TitledPane> suggestTripList =  tripSuggestAccordion.getPanes();

        suggestTripList.forEach((titledPane -> {
            String suggestTripText = titledPane.getText();
            for(int i = 0 ; i < suggestTripText.length() ; i ++) {
                if(String.valueOf(suggestTripText.charAt(i)).equals(tripSuggestId)) {
                    Map<String, List<String>> tripSuggestMapDto = mainController.getSuggestedTripsMapDto();
                    tripSuggestMapDto.forEach((tripSuggestDtoId ,tripSuggestDtoObj)  -> {
                        if(tripSuggestDtoId.equals(tripSuggestId)) {
                            mainController.setNumOfRanksInTripSuggestMapDto(tripSuggestId);
                            mainController.setAverageInTripSuggestMapDto(currRank, tripSuggestId);
                            mainController.setAverageReviewsInTripSuggestMapDto(currReview, tripSuggestId);
                            List<String> tripSuggestDtoObjToUpdate = mainController.getTripSuggestDtoObj(tripSuggestId);
                            StringBuilder newContent = new StringBuilder();
                            for(int j = 0 ; j < INPUT_ADD_TRIP_SUGGEST_SIZE_INCLUDE_RANKING ; j++) {
                                if (j == (INPUT_ADD_TRIP_SUGGEST_SIZE_INCLUDE_RANKING-1)) {
                                    newContent.append(tripSuggestDtoObjToUpdate.get(j));
                                }
                                else {
                                    newContent.append(tripSuggestDtoObjToUpdate.get(j) + System.lineSeparator());
                                }
                            }
                            titledPane.setContent(new TextArea(newContent.toString()));
                        }
                    });
                }
            }
        }));
    }



    //-------------------------------Add TripSuggest From Xml Flow UI---------------------------------------
    public void loadTripSuggestFromXML() {
        Map<Integer, TripSuggest> initTripSuggest = mainController.getTripSuggestMap();
        if(!initTripSuggest.isEmpty()) {
            initTripSuggest.forEach((tripSuggestID, tripSuggest) -> {
                addNewTripSuggestAccordion(tripSuggest);
            });
        }
    }
}
