package controllers;

import Routes.CommonResourcesPaths;
import TripRequests.TripRequest;
import TripSuggestUtil.TripSuggest;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
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

import java.net.URL;
import java.util.ArrayList;

public class TripSuggestController {
    private AppController mainController;

    @FXML
    private Button addTripSuggestButton;

    ArrayList<TextField> inputAddTripSuggest = null;
    static final int INPUT_ADD_TRIP_SUGGEST_SIZE = 7;
    Stage addTripSuggestStage = null;

    public void setMainController(AppController mainController) {
        this.mainController = mainController;
    }

    public TripSuggestController() {
        this.inputAddTripSuggest = new ArrayList<>(INPUT_ADD_TRIP_SUGGEST_SIZE);;
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

        inputAddTripSuggest.add(new TextField("Enter owner name"));
        inputAddTripSuggest.get(0).setMaxWidth(250);
        inputAddTripSuggest.get(0).setTranslateX(15);
        addTripSuggestWindow.getChildren().add(inputAddTripSuggest.get(0));

        //-----------------------------------------------------

        Label suggestedTripLabel = new Label("- Route of suggested trip separate with '-'");
        suggestedTripLabel.setTextFill(Color.web("#0076a3"));
        suggestedTripLabel.setPrefWidth(300);
        addTripSuggestWindow.getChildren().add(suggestedTripLabel);
        suggestedTripLabel.setTranslateX(15);

        inputAddTripSuggest.add(new TextField("Enter suggested trip separate with '-'"));
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
        inputAddTripSuggest.add(new TextField("Enter arrival Day Number"));
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

        inputAddTripSuggest.add(new TextField("Enter start time"));
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

        inputAddTripSuggest.add(new TextField("Enter number between 1 to 5"));
        inputAddTripSuggest.get(4).setMaxWidth(250);
        inputAddTripSuggest.get(4).setTranslateX(15);
        addTripSuggestWindow.getChildren().add(inputAddTripSuggest.get(4));

        //-----------------------------------------------------

        Label ppkLabel = new Label("-PPK: cost of trip per kilometer");
        ppkLabel.setTextFill(Color.web("#0076a3"));
        ppkLabel.setPrefWidth(400);
        ppkLabel.setTranslateX(15);
        addTripSuggestWindow.getChildren().add(ppkLabel);

        inputAddTripSuggest.add(new TextField("Enter ppk"));
        inputAddTripSuggest.get(5).setMaxWidth(250);
        inputAddTripSuggest.get(5).setTranslateX(15);
        addTripSuggestWindow.getChildren().add(inputAddTripSuggest.get(5));

        //-----------------------------------------------------

        Label passengersCapacityLabel = new Label("-Passengers capacity");
        passengersCapacityLabel.setTextFill(Color.web("#0076a3"));
        passengersCapacityLabel.setPrefWidth(400);
        passengersCapacityLabel.setTranslateX(15);
        addTripSuggestWindow.getChildren().add(passengersCapacityLabel);

        inputAddTripSuggest.add(new TextField("Enter passengers capacity"));
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


    private void newIdSuggestButtonDisplay(ActionEvent actionEvent) {
    }

    public void closeAddNewTripSuggestStage() {
        addTripSuggestStage.close();
    }

    public void addNewTripSuggestLabel(TripSuggest newSuggest) throws Exception {
        CommonResourcesPaths route = CommonResourcesPaths.getInstance();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource(route.TRIP_SUGGEST_fXML_RESOURCE);
        ScrollPane borderPaneTripSuggest = fxmlLoader.load(url.openStream());


        Node node = borderPaneTripSuggest.getContent();

        Button newIdSuggestButton = new Button(Integer.toString(newSuggest.getSuggestID()));
        borderPaneTripSuggest.getChildrenUnmodifiable().add(newIdSuggestButton);

        Scene scene = new Scene(borderPaneTripSuggest);




        //newIdSuggestButton.setOnAction(this::newIdSuggestButtonDisplay);

        //ObjectProperty<Node> x = borderPaneTripSuggest.contentProperty();

        //VBox check1 = (VBox) node;

        //check1.getChildren().add(newIdSuggestButton);


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
}
