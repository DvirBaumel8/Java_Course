package controllers;

import TripRequests.TripRequest;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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

public class TripRequestController {
    private AppController mainController;

    @FXML
    private Button addTripRequestButton;

    @FXML
    Accordion tripRequestAccordion;

    ArrayList<TextField> inputAddTripRequest = null;
    static final int INPUT_ADD_TRIP_REQUEST_SIZE = 6;
    Stage addTripRequestStage = null;

    public TripRequestController() {
        this.inputAddTripRequest = new ArrayList<>(INPUT_ADD_TRIP_REQUEST_SIZE);;
    }

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void addTripRequestButtonActionListener() {
        if(mainController.isXMLLoaded()) {
            getAddTripRequestWindow();
        }
        else {
            mainController.getAlertErrorWindow("XML doesnt load yet - please load one");
        }
    }

    private void addInputTripRequestButtonAction(ActionEvent event) {
        String[] inputTripRequestString = new String[INPUT_ADD_TRIP_REQUEST_SIZE];
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

        inputAddTripRequest.add(new TextField("Ohad"));
        inputAddTripRequest.get(0).setMaxWidth(250);
        inputAddTripRequest.get(0).setTranslateX(15);
        addTripRequestWindow.getChildren().add(inputAddTripRequest.get(0));

        //-----------------------------------------------------

        Label sourceStationLabel = new Label("- Source station:");
        sourceStationLabel.setTranslateX(15);
        sourceStationLabel.setTextFill(Color.web("#0076a3"));
        sourceStationLabel.setPrefWidth(300);
        addTripRequestWindow.getChildren().add(sourceStationLabel);

        inputAddTripRequest.add(new TextField("A"));
        inputAddTripRequest.get(1).setMaxWidth(250);
        inputAddTripRequest.get(1).setTranslateX(15);
        addTripRequestWindow.getChildren().add(inputAddTripRequest.get(1));

        //-----------------------------------------------------

        Label destinationStationLabel = new Label("- Destination station:");
        destinationStationLabel.setTranslateX(15);
        destinationStationLabel.setTextFill(Color.web("#0076a3"));
        destinationStationLabel.setPrefWidth(300);
        addTripRequestWindow.getChildren().add(destinationStationLabel);

        inputAddTripRequest.add(new TextField("B"));
        inputAddTripRequest.get(2).setMaxWidth(250);
        inputAddTripRequest.get(2).setTranslateX(15);
        addTripRequestWindow.getChildren().add(inputAddTripRequest.get(2));

        //-----------------------------------------------------

        Label startOrArrivalTimeLabel = new Label("- Start or Arrival time of the trip (**:** format every 5 min):");
        startOrArrivalTimeLabel.setTranslateX(15);
        startOrArrivalTimeLabel.setTextFill(Color.web("#0076a3"));
        startOrArrivalTimeLabel.setPrefWidth(400);
        addTripRequestWindow.getChildren().add(startOrArrivalTimeLabel);

        inputAddTripRequest.add(new TextField("12:00"));
        inputAddTripRequest.get(3).setMaxWidth(250);
        inputAddTripRequest.get(3).setTranslateX(15);
        addTripRequestWindow.getChildren().add(inputAddTripRequest.get(3));

        //-----------------------------------------------------

        Label timeTypeLabel = new Label("-s to choose start time, a to choose arrival time:5");
        timeTypeLabel.setTranslateX(15);
        timeTypeLabel.setTextFill(Color.web("#0076a3"));
        timeTypeLabel.setPrefWidth(400);
        addTripRequestWindow.getChildren().add(timeTypeLabel);

        inputAddTripRequest.add(new TextField("s"));
        inputAddTripRequest.get(4).setMaxWidth(250);
        inputAddTripRequest.get(4).setTranslateX(15);
        addTripRequestWindow.getChildren().add(inputAddTripRequest.get(4));

        //-----------------------------------------------------

        Label arrivalDayNumberLabel = new Label("- Arrival Day Number:");
        arrivalDayNumberLabel.setTranslateX(15);
        arrivalDayNumberLabel.setTextFill(Color.web("#0076a3"));
        arrivalDayNumberLabel.setPrefWidth(400);
        addTripRequestWindow.getChildren().add(arrivalDayNumberLabel);

        inputAddTripRequest.add(new TextField("2"));
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
        addTripRequestWindow.setMargin(arrivalDayNumberLabel, generalMargin);
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
        if(newRequest.getIsStartTime()) {
            startOrArrival = "start";
        }
        else {
            startOrArrival = "arrival";
        }
        TextArea newTripRequestTextArea =
                new TextArea("-Src station:" + newRequest.getSourceStation() + System.lineSeparator() +
                "-Dest station:" + newRequest.getDestinationStation() + System.lineSeparator() +
                "-Time:" + newRequest.getTimeStr() + System.lineSeparator() +
                "-Start/Arrival" + startOrArrival);
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
}
