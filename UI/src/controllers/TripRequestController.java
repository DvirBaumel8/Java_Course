package controllers;

import TripRequests.TripRequest;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TripRequestController {
    private AppController mainController;

    @FXML
    private Button addTripRequestButton;

    @FXML Accordion tripRequestAccordion;

    ArrayList<TextField> inputAddTripRequest = null;
    static final int INPUT_ADD_TRIP_REQUEST_SIZE = 6;
    String[] inputTripRequestString = new String[INPUT_ADD_TRIP_REQUEST_SIZE];
    Stage addTripRequestStage = null;


    public TripRequestController() {
        this.inputAddTripRequest = new ArrayList<>();
        inputTripRequestString = new String[INPUT_ADD_TRIP_REQUEST_SIZE];
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }


    //-------------------------------Add TripRequest Flow UI---------------------------------------
    @FXML
    void addTripRequestButtonActionListener() {
        if(mainController.isXMLLoaded()) {
            getAddTripRequestWindow();
        }
        else {
            List<String> errors = new LinkedList<>();
            errors.add("XML doesnt load yet - please load one");
            mainController.getAlertErrorWindow(errors);
        }
    }

    private void addInputTripRequestButtonAction(ActionEvent event) {
        int index = 0;
        for(TextField inputTextField : inputAddTripRequest) {
            inputTripRequestString[index] = inputTextField.getText();
            index ++;
        }
        mainController.addTripRequestAction(inputTripRequestString);
    }

    void getAddTripRequestWindow() {
        addTripRequestStage = new Stage();
        VBox addTripRequestWindow = new VBox();
        javafx.geometry.Insets margin = new javafx.geometry.Insets(12,12,12,12);
        javafx.geometry.Insets generalMargin = new javafx.geometry.Insets(0,4,0,4);
        addTripRequestWindow.setSpacing(10);

        if(tripRequestAccordion.getPanes().size() > 0) {
            this.inputAddTripRequest = null;
            this.inputAddTripRequest = new ArrayList<>();
        }
        Label detailsLabel = new Label("Please insert the following details:");
        detailsLabel.setTranslateX(15);

        detailsLabel.setFont(new javafx.scene.text.Font("Arial", 21));
        addTripRequestWindow.getChildren().add(detailsLabel);

        //-----------------------------------------------------

        String allStationsNames = mainController.getAllStationsNames();
        Label allStationsNamesLabel = new Label(allStationsNames);
        allStationsNamesLabel.setTranslateX(15);
        addTripRequestWindow.getChildren().add(allStationsNamesLabel);

        //-----------------------------------------------------

        Label exampleLabel = new Label("Example: Ohad§,A,C,12:25,s");
        exampleLabel.setTranslateX(15);
        addTripRequestWindow.getChildren().add(exampleLabel);

        //-----------------------------------------------------

        Label nameOfOwnerLabel = new Label("- Name of owner:");
        nameOfOwnerLabel.setTranslateX(15);
        nameOfOwnerLabel.setTextFill(Color.web("#0076a3"));
        nameOfOwnerLabel.setPrefWidth(300);
        addTripRequestWindow.getChildren().add(nameOfOwnerLabel);

        inputAddTripRequest.add(0, new TextField("Ohad"));
        inputAddTripRequest.get(0).setMaxWidth(250);
        inputAddTripRequest.get(0).setTranslateX(15);
        addTripRequestWindow.getChildren().add(inputAddTripRequest.get(0));

        //-----------------------------------------------------

        Label sourceStationLabel = new Label("- Source station:");
        sourceStationLabel.setTranslateX(15);
        sourceStationLabel.setTextFill(Color.web("#0076a3"));
        sourceStationLabel.setPrefWidth(300);
        addTripRequestWindow.getChildren().add(sourceStationLabel);

        inputAddTripRequest.add(1, new TextField("A"));
        inputAddTripRequest.get(1).setMaxWidth(250);
        inputAddTripRequest.get(1).setTranslateX(15);
        addTripRequestWindow.getChildren().add(inputAddTripRequest.get(1));

        //-----------------------------------------------------

        Label destinationStationLabel = new Label("- Destination station:");
        destinationStationLabel.setTranslateX(15);
        destinationStationLabel.setTextFill(Color.web("#0076a3"));
        destinationStationLabel.setPrefWidth(300);
        addTripRequestWindow.getChildren().add(destinationStationLabel);

        inputAddTripRequest.add(2, new TextField("B"));
        inputAddTripRequest.get(2).setMaxWidth(250);
        inputAddTripRequest.get(2).setTranslateX(15);
        addTripRequestWindow.getChildren().add(inputAddTripRequest.get(2));

        //-----------------------------------------------------

        Label startOrArrivalTimeLabel = new Label("- Start or Arrival time of the trip (**:** format every 5 min):");
        startOrArrivalTimeLabel.setTranslateX(15);
        startOrArrivalTimeLabel.setTextFill(Color.web("#0076a3"));
        startOrArrivalTimeLabel.setPrefWidth(400);
        addTripRequestWindow.getChildren().add(startOrArrivalTimeLabel);

        inputAddTripRequest.add(3, new TextField("08:00"));
        inputAddTripRequest.get(3).setMaxWidth(250);
        inputAddTripRequest.get(3).setTranslateX(15);
        addTripRequestWindow.getChildren().add(inputAddTripRequest.get(3));

        //-----------------------------------------------------

        Label timeTypeLabel = new Label("-S to choose start time, A to choose arrival time:");
        timeTypeLabel.setTranslateX(15);
        timeTypeLabel.setTextFill(Color.web("#0076a3"));
        timeTypeLabel.setPrefWidth(400);
        addTripRequestWindow.getChildren().add(timeTypeLabel);

        inputAddTripRequest.add(4, new TextField("S"));
        inputAddTripRequest.get(4).setMaxWidth(250);
        inputAddTripRequest.get(4).setTranslateX(15);
        addTripRequestWindow.getChildren().add(inputAddTripRequest.get(4));

        //-----------------------------------------------------

        Label startDayNumberLabel = new Label("- Start Day Number:");
        startDayNumberLabel.setTranslateX(15);
        startDayNumberLabel.setTextFill(Color.web("#0076a3"));
        startDayNumberLabel.setPrefWidth(400);
        addTripRequestWindow.getChildren().add(startDayNumberLabel);

        inputAddTripRequest.add(5, new TextField("2"));
        inputAddTripRequest.get(5).setTranslateX(15);
        inputAddTripRequest.get(5).setMaxWidth(250);
        addTripRequestWindow.getChildren().add(inputAddTripRequest.get(5));

        //-----------------------------------------------------

        Button addInputTripRequestButton= new Button("Add");
        addInputTripRequestButton.setMinWidth(150);
        addInputTripRequestButton.setTranslateY(1);
        addInputTripRequestButton.setTranslateX(15);
        addInputTripRequestButton.setOnAction(this::addInputTripRequestButtonAction);
        addTripRequestWindow.getChildren().add(addInputTripRequestButton);

        addTripRequestWindow.setMargin(detailsLabel, margin);
        addTripRequestWindow.setMargin(addInputTripRequestButton, margin);
        addTripRequestWindow.setMargin(allStationsNamesLabel, margin);
        addTripRequestWindow.setMargin(exampleLabel, margin);

        addTripRequestWindow.setMargin(nameOfOwnerLabel, generalMargin);
        addTripRequestWindow.setMargin(sourceStationLabel, generalMargin);
        addTripRequestWindow.setMargin(destinationStationLabel, generalMargin);
        addTripRequestWindow.setMargin(startOrArrivalTimeLabel, generalMargin);
        addTripRequestWindow.setMargin(timeTypeLabel, generalMargin);
        addTripRequestWindow.setMargin(startDayNumberLabel, generalMargin);
        addTripRequestWindow.setMargin(addInputTripRequestButton, generalMargin);

        addTripRequestWindow.setMargin(inputAddTripRequest.get(0), generalMargin);
        addTripRequestWindow.setMargin(inputAddTripRequest.get(1), generalMargin);
        addTripRequestWindow.setMargin(inputAddTripRequest.get(2), generalMargin);
        addTripRequestWindow.setMargin(inputAddTripRequest.get(3), generalMargin);
        addTripRequestWindow.setMargin(inputAddTripRequest.get(4), generalMargin);
        addTripRequestWindow.setMargin(inputAddTripRequest.get(5), generalMargin);

        ScrollPane scrollPane = new ScrollPane(addTripRequestWindow);
        scrollPane.setMaxHeight(800);
        scrollPane.setMaxWidth(500);

        scrollPane.setMinWidth(250);
        scrollPane.setMinHeight(500);

        scrollPane.setBackground((new Background(new BackgroundFill(Color.gray(0.865),
                CornerRadii.EMPTY, Insets.EMPTY))));

        Scene scene = new Scene(scrollPane, 500, 800);

        addTripRequestStage.setTitle("Add New Trip Request");
        addTripRequestStage.setScene(scene);
        addTripRequestStage.show();
        addTripRequestStage.setMaxHeight(scrollPane.getHeight());

        /*
            System.out.println("Please insert the following details separated with ',' (Insert 'b' to go back to the main menu):\n" +
                "- Name of owner\n" +
                "- Source station\n" +
                "- Destination station\n" +
                "- start or arrival time of the trip (**:** format every 5 min) \n" +
                "- s to choose start time, a to choose arrival time.\n" +
                "Example: Ohad§,A,C,12:25,s");
         */
    }

    public void addNewTripRequestAccordion(TripRequest newRequest) throws Exception{
        String startOrArrival = null;
        TextArea newTripRequestTextArea;
        if(newRequest.getIsStartTime()) {
            startOrArrival = "start";
            newTripRequestTextArea =
                    new TextArea("-Src station:" + newRequest.getSourceStation() + System.lineSeparator() +
                            "-Dest station:" + newRequest.getDestinationStation() + System.lineSeparator() +
                            "-Time:" + newRequest.getStartTime() + System.lineSeparator() +
                            "-Start/Arrival" + startOrArrival);
        }
        else {
            startOrArrival = "arrival";
            newTripRequestTextArea =
                    new TextArea("-Src station:" + newRequest.getSourceStation() + System.lineSeparator() +
                            "-Dest station:" + newRequest.getDestinationStation() + System.lineSeparator() +
                            "-Time:" + newRequest.getArrivalTime() + System.lineSeparator() +
                            "-Start/Arrival" + startOrArrival);
        }

        newTripRequestTextArea.setPrefRowCount(4);
        TitledPane title = new TitledPane(newRequest.getNameOfOwner() + ", id:" + newRequest.getRequestID(),
                newTripRequestTextArea);
        // title.setOnMouseClicked(event-> tripsAccordionOnAction());
        int sizeOfCurrentPanes = tripRequestAccordion.getPanes().size();
        tripRequestAccordion.getPanes().add(sizeOfCurrentPanes,title);

            //liveMapComponentController.updateTripOnMap(trip);
    }

    public void closeAddNewTripRequestStage() {
        addTripRequestStage.close();
    }



    //-------------------------------Set TripRequest For Match UI---------------------------------------
    public void setNeededTripRequestForMatchAccordion(String requestId) {
        ObservableList<TitledPane> requestTripList =  tripRequestAccordion.getPanes();

        requestTripList.forEach((titledPane -> {
            String requestTripText = titledPane.getText();
            for(int i = 0 ; i < requestTripText.length() ; i ++) {
                if(String.valueOf(requestTripText.charAt(i)).equals(requestId)) {
                    StringBuilder sb = new StringBuilder(requestTripText);
                    int indexAccordion = Integer.parseInt(requestId) - 1;
                    TitledPane titledPane2 = requestTripList.get(indexAccordion);
                    Node temp = titledPane2.getContent();
                    TitledPane title3 = new TitledPane(sb + "Matched",
                            temp);
                    tripRequestAccordion.getPanes().set(indexAccordion,title3);
                    // tripSuggestAccordion.getPanes().set(i,sb)
                }
            }
        }));
    }
}
