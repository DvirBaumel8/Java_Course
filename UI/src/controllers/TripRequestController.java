package controllers;

import Manager.BorderPaneWrapper;
import Manager.ViewWrapper;
import Routes.CommonResourcesPaths;
import TripRequests.TripRequest;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TripRequestController {
    private AppController mainController;

    @FXML
    private Button addTripRequestButton;

    ArrayList<TextField> inputAddTripRequest = null;
    static final int INPUT_ADD_TRIP_REQUEST_SIZE = 5;
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
        addTripRequestWindow.setBackground((new Background(new BackgroundFill(Color.gray(0.865),
                CornerRadii.EMPTY, Insets.EMPTY))));

        Label detailsLabel = new Label("Please insert the following details:");

        detailsLabel.setFont(new javafx.scene.text.Font("Arial", 21));
        addTripRequestWindow.getChildren().add(detailsLabel);

        //-----------------------------------------------------

        String allStationsNames = mainController.getAllStationsNames();
        Label allStationsNamesLabel = new Label(allStationsNames);
        addTripRequestWindow.getChildren().add(allStationsNamesLabel);

        //-----------------------------------------------------

        Label exampleLabel = new Label("Example: Ohad§,A,C,12:25,s");
        addTripRequestWindow.getChildren().add(exampleLabel);

        //-----------------------------------------------------

        Label nameOfOwnerLabel = new Label("- Name of owner:");
        nameOfOwnerLabel.setTextFill(Color.web("#0076a3"));
        nameOfOwnerLabel.setPrefWidth(300);
        addTripRequestWindow.getChildren().add(nameOfOwnerLabel);

        inputAddTripRequest.add(new TextField("Enter owner name"));
        inputAddTripRequest.get(0).setMaxWidth(250);
        addTripRequestWindow.getChildren().add(inputAddTripRequest.get(0));

        //-----------------------------------------------------

        Label sourceStationLabel = new Label("- Source station:");
        sourceStationLabel.setTextFill(Color.web("#0076a3"));
        sourceStationLabel.setPrefWidth(300);
        addTripRequestWindow.getChildren().add(sourceStationLabel);

        inputAddTripRequest.add(new TextField("Enter source station"));
        inputAddTripRequest.get(1).setMaxWidth(250);
        addTripRequestWindow.getChildren().add(inputAddTripRequest.get(1));

        //-----------------------------------------------------

        Label destinationStationLabel = new Label("- Destination station:");
        destinationStationLabel.setTextFill(Color.web("#0076a3"));
        destinationStationLabel.setPrefWidth(300);
        addTripRequestWindow.getChildren().add(destinationStationLabel);

        inputAddTripRequest.add(new TextField("Enter destination station"));
        inputAddTripRequest.get(2).setMaxWidth(250);
        addTripRequestWindow.getChildren().add(inputAddTripRequest.get(2));

        //-----------------------------------------------------

        Label startOrArrivalTimeLabel = new Label("- Start or Arrival time of the trip (**:** format every 5 min):");
        startOrArrivalTimeLabel.setTextFill(Color.web("#0076a3"));
        startOrArrivalTimeLabel.setPrefWidth(400);
        addTripRequestWindow.getChildren().add(startOrArrivalTimeLabel);

        inputAddTripRequest.add(new TextField("Enter start or arrival time"));
        inputAddTripRequest.get(3).setMaxWidth(250);
        addTripRequestWindow.getChildren().add(inputAddTripRequest.get(3));

        //-----------------------------------------------------

        Label timeTypeLabel = new Label("-s to choose start time, a to choose arrival time:5");
        timeTypeLabel.setTextFill(Color.web("#0076a3"));
        timeTypeLabel.setPrefWidth(400);
        addTripRequestWindow.getChildren().add(timeTypeLabel);

        inputAddTripRequest.add(new TextField("Enter s or a"));
        inputAddTripRequest.get(4).setMaxWidth(250);
        addTripRequestWindow.getChildren().add(inputAddTripRequest.get(4));

        //-----------------------------------------------------

        Button addInputTripRequestButton= new Button("Add");
        addInputTripRequestButton.setMinWidth(150);
        addInputTripRequestButton.setTranslateY(25);
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
        addTripRequestWindow.setMargin(addInputTripRequestButton, generalMargin);

        addTripRequestWindow.setMargin(inputAddTripRequest.get(0), generalMargin);
        addTripRequestWindow.setMargin(inputAddTripRequest.get(1), generalMargin);
        addTripRequestWindow.setMargin(inputAddTripRequest.get(2), generalMargin);
        addTripRequestWindow.setMargin(inputAddTripRequest.get(3), generalMargin);
        addTripRequestWindow.setMargin(inputAddTripRequest.get(4), generalMargin);


        Scene scene = new Scene(addTripRequestWindow, 450, 650);

        addTripRequestStage.setTitle("Add New Trip Request");
        addTripRequestStage.setScene(scene);
        addTripRequestStage.show();
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

    public void addNewTripRequestLabel(TripRequest newRequest) throws Exception{
        CommonResourcesPaths route = CommonResourcesPaths.getInstance();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(route.TRIP_SUGGEST_fXML_RESOURCE);
        ScrollPane borderPaneTripRequest = fxmlLoader.load(url.openStream());
        Node node = borderPaneTripRequest.getContent();
        VBox vBox =(VBox) node;
        Button newIdRequestButton = new Button(Integer.toString(newRequest.getRequestID()));
        Label bindingLabel = new Label(String.valueOf(newRequest.getRequestID()));
        //for (int i = 0; i < 4; i++) {
          //  vbox.getChildren().add(new Label("Item "+(i+1)));
        //}
        vBox.getChildren().setAll(bindingLabel);
        borderPaneTripRequest.setContent(vBox);
        newIdRequestButton.setOnAction(this::newIdRequestButtonDisplay);

        ViewWrapper viewWrapper = ViewWrapper.getInstance();

        //viewWrapper.getRoot().setLeft(borderPaneTripRequest);
        //Scene scene = new Scene(viewWrapper.getRoot(), 700, 690);
        //viewWrapper.getPrimaryStage().setScene(scene);
        //viewWrapper.getPrimaryStage().show();

        //Scene scene = button.getScene();
        //Parent root = MyApplication.pages.get("LoginPage");
        //scene.setRoot(root);
        //VBox check = (VBox) x.get();
        //check.getChildren().add(newIdRequestButton);

        //id:
        //sourceStation:
        //destinationStation:
        //    private String OwnerName;
        //    private String sourceStation;
        //    private String destinationStation;
        //    private double requiredTime;
        //    private String arrivalHourAsTime;
        //    private boolean isMatched;
        //    private TripSuggest matchTrip;
        //    private boolean requestByStartTime;

        //int x = 3;
    }

    private void newIdRequestButtonDisplay(ActionEvent actionEvent) {
    }

    public void closeAddNewTripRequestStage() {
        addTripRequestStage.close();
    }


}
