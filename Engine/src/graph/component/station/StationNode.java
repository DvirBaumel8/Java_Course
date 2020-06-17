package graph.component.station;

import graph.component.details.StationDetailsDTO;
import graph.component.station.Visual.StationController;
import com.fxgraph.cells.AbstractCell;
import com.fxgraph.graph.Graph;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;

import java.net.URL;
import java.util.function.Supplier;

public class StationNode extends AbstractCell {
    private int x;
    private int y;
    private String name;
    private Supplier<StationDetailsDTO> detailsSupplier;
    public StationNode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetailsSupplier(Supplier<StationDetailsDTO> detailsSupplier) {
        this.detailsSupplier = detailsSupplier;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    /*
    Creates the graphical representation of the station.
     */
    public Region getGraphic(Graph graph) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("visual/StationView.fxml");
            fxmlLoader.setLocation(url);
            HBox root = fxmlLoader.load(url.openStream());

            // updates information on the actual node's controller
            StationController controller = fxmlLoader.getController();
            controller.setX(x);
            controller.setY(y);
            controller.setName(name);
            controller.setDetailsDTOSupplier(detailsSupplier);

            return root;
        } catch (Exception e) {
            return new Label("Error when tried to create graphic node !");
        }
    }
}
