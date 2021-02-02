package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class GameController {
    private Stage stage;

    @FXML private Pane mapPane;
    @FXML private ScatterChart<Integer, Integer> universeMap;
    @FXML private BorderPane marketPane;
    @FXML private BorderPane encounterPane;

    @FXML private Label buyFuelLabel;
    @FXML private Button buyFuelButton;
    @FXML private Button repairSingleButton;
    @FXML private Button repairMaxButton;
    @FXML private Label travelCostLabel;

    @FXML private Label planetName;
    @FXML private Label planetCoordinates;
    @FXML private Label planetTechLevel;
    @FXML private Label planetDescription;

    @FXML private VBox marketBuyGoodsContainer;
    @FXML private VBox marketBuyUpgradesContainer;
    @FXML private VBox marketSellContainer;
    @FXML private VBox marketInventoryContainer;
    @FXML private VBox marketEquippedContainer;
    @FXML private Button equipButton;
    private ToggleGroup equipToggle = new ToggleGroup();
    private RadioButton selectedEquipment;
    @FXML private Label creditsRemaining;

    @FXML private Label encounterSituation;
    @FXML private HBox encounterChoiceContainer;

    private SoundsManager soundsManager = new SoundsManager();

    private XYChart.Series<Integer, Integer> mapData = new XYChart.Series<>();
    private Region selectedRegion;
    private int fuelToBuy;
    private boolean canTravel = true;
    private Universe universe;

    @FXML
    private void initialize() {
        universeMap.getData().add(mapData);
    }

    public void setup(Stage stage, Universe universe) {
        this.stage = stage;
        this.universe = universe;
        ArrayList<Region> regions = universe.getRegions();
        for (int i = 0; i < regions.size(); i++) {
            XYChart.Data<Integer, Integer> regionData = new XYChart.Data<>(
                    regions.get(i).getPosition().getX(), regions.get(i).getPosition().getY());
            mapData.getData().add(regionData);
            regionData.getNode().getProperties().put("systemID", i);
            regionData.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, this::handleRegionClick);
        }
        selectedRegion = universe.getPlayer().getLocation();
        updateSystemInfo();
        updateMap();
        updateFuelStore();
        updateRepairCost();
    }

    private void updateMap() {
        //loop through regions and update colors
        canTravel = false;
        for (int i = 0; i < universe.getRegions().size(); i++) {
            mapData.getData().get(i).getNode().getStyleClass().remove("current-system");
            mapData.getData().get(i).getNode().getStyleClass().remove("visited-system");
            mapData.getData().get(i).getNode().getStyleClass().remove("inrange-system");
            if (universe.getRegions().get(i).isVisited()) {
                mapData.getData().get(i).getNode().getStyleClass().add("visited-system");
            }
            if (universe.getPlayer().canTravelTo(universe.getRegions().get(i))) {
                mapData.getData().get(i).getNode().getStyleClass().add("inrange-system");
                canTravel = true;
            }
            if (universe.getRegions().get(i) == universe.getPlayer().getLocation()) {
                mapData.getData().get(i).getNode().getStyleClass().add("current-system");
            }
        }
    }

    private void updateSystemInfo() {
        planetName.setText(
                "Name: "
                        + (selectedRegion.isVisited() ? selectedRegion.getName() : "???"));
        planetCoordinates.setText("Sector: "
                + selectedRegion.getPosition().getX()
                + "," + selectedRegion.getPosition().getY());
        planetTechLevel.setText(
                "Tech level: "
                        + (selectedRegion.isVisited() ? selectedRegion.getTechLevel() : "???"));
        planetDescription.setText(
                selectedRegion.isVisited() ? selectedRegion.getDescription() : "???");


    }

    private void updateFuelStore() {
        int fuelPricePerUnit = 1; //probably should vary
        int fuelToBuy = universe.getPlayer().getCurrentShip().getFuelCapacity()
                - universe.getPlayer().getCurrentShip().getFuel();
        if (fuelToBuy == 0) {
            buyFuelButton.setDisable(true);
            buyFuelLabel.setText("Fuel tank full");
            return;
        }
        int playerCredits = universe.getPlayer().getCredits();
        if (fuelPricePerUnit * fuelToBuy > playerCredits) {
            fuelToBuy = playerCredits / fuelPricePerUnit;
        }
        buyFuelButton.setDisable(false);
        this.fuelToBuy = fuelToBuy;
        buyFuelLabel.setText(String.format("Buy %d fuel", fuelToBuy));
        buyFuelButton.setText(String.format("Cost: ©%d", fuelPricePerUnit * fuelToBuy));
    }

    private void updateUpgradeInventory() {
        marketInventoryContainer.getChildren().clear();
        universe.getPlayer().getCurrentShip().getInventory().forEach(
            (good, count) -> {
                if (good.isUpgrade()) {
                    RadioButton item = new RadioButton(
                            count + "x " + good.getName());
                    item.getProperties().put("type", good);
                    item.setToggleGroup(equipToggle);
                    item.addEventHandler(ActionEvent.ACTION, this :: setSelectedUpgrade);
                    marketInventoryContainer.getChildren().add(item);
                }
            }
        );
        marketEquippedContainer.getChildren().clear();
        for (Good good : universe.getPlayer().getEquipment()) {
            RadioButton item = new RadioButton(good.getName()
                    + "\n  " + good.getUpgradeType()
                    + ": " + good.getUpgradeLevel());
            item.getProperties().put("type", good);
            item.setToggleGroup(equipToggle);
            item.addEventHandler(ActionEvent.ACTION, this :: setSelectedUpgrade);
            marketEquippedContainer.getChildren().add(item);
        }
    }

    private void setSelectedUpgrade(ActionEvent e) {
        selectedEquipment = (RadioButton) e.getTarget();
        equipButton.setDisable(false);
    }

    private void updateMarketStock() {
        if (universe.getPlayer().getCurrentShip().getCargoSpace() < 1) {
            marketBuyGoodsContainer.setDisable(true);
            marketBuyUpgradesContainer.setDisable(true);
        } else {
            marketBuyGoodsContainer.setDisable(false);
            marketBuyUpgradesContainer.setDisable(false);
        }
        marketBuyGoodsContainer.getChildren().clear();
        marketBuyUpgradesContainer.getChildren().clear();
        Region location = universe.getPlayer().getLocation();
        Map<Good, Integer> market = location.getMarketplace();
        updateCredits();
        int creditsLeft = universe.getPlayer().getCredits();
        market.forEach((good, price) -> {
            Button item = new Button(good.getName() + ": " + price + "©");
            item.setOnAction((ActionEvent e) -> {
                universe.getPlayer().buy(good);
                updateMarketStock();
            });
            item.setUserData(good);
            if (creditsLeft < price) {
                item.setDisable(true);
            }
            if (good.isUpgrade()) {
                marketBuyUpgradesContainer.getChildren().add(item);
            } else {
                marketBuyGoodsContainer.getChildren().add(item);
            }
        });

        marketSellContainer.getChildren().clear();
        universe.getPlayer().getCurrentShip().getInventory().forEach(
            (good, quantity) -> {
                int price = location.getSellPrice(good)
                        + universe.getPlayer().getMerchantPoints() - 10;
                Button item = new Button(
                        quantity + "x "
                        + good.getName()
                        + ": " + price + "©");
                marketSellContainer.getChildren().add(item);
                item.setOnAction((ActionEvent e) -> {
                    universe.getPlayer().sell(good);
                    updateMarketStock();
                });
            }
        );
        updateUpgradeInventory();
    }

    private void updateEncounterPane(Encounter encounter) {
        encounterSituation.setText(encounter.getDescription());
        String[] responses = encounter.getResponses();
        encounterChoiceContainer.getChildren().clear();
        for (String response : responses) {
            Button responseButton = new Button();
            responseButton.getStyleClass().add("choices");
            responseButton.setText(response);
            responseButton.setOnAction((ActionEvent e) -> {
                encounter.resolve(response);
                if (encounter.isResolved()) {
                    universe.getPlayer().travelTo(selectedRegion, encounter.getTravelSuccess());
                    updateMap();
                    updateSystemInfo();
                    updateRepairCost();
                    toggleEncounter();
                }
                updateEncounterPane(encounter);
            });
            encounterChoiceContainer.getChildren().add(responseButton);
        }
        updateMap();
        updateFuelCost();
        updateFuelStore();
        updateRepairCost();
        endGameCheck();
    }

    @FXML
    private void equipClicked() {
        Good upgrade = (Good) selectedEquipment.getProperties().get("type");
        if (selectedEquipment == null) {
            return;
        }
        if (selectedEquipment.getParent() == marketInventoryContainer) {
            //item is NOT equipped
            universe.getPlayer().equip(upgrade);
        } else {
            //item is already equipped
            universe.getPlayer().unequip(upgrade);
        }
        equipToggle.selectToggle(null);
        selectedEquipment = null;
        equipButton.setDisable(true);
        updateMarketStock();
    }

    private void updateCredits() {
        creditsRemaining.setText("Credits: "
                + universe.getPlayer().getCredits()
                + "©");
    }

    @FXML
    private void buyFuel(ActionEvent e) {
        int fuelCostPerUnit = 1; //shouldn't always be 1
        int cost = fuelToBuy * fuelCostPerUnit;
        universe.getPlayer().loseCredits(cost);
        universe.getPlayer().getCurrentShip().setFuel(
                universe.getPlayer().getCurrentShip().getFuel() + fuelToBuy);
        updateFuelCost();
        updateFuelStore();
        updateRepairCost();
        updateMap();
        endGameCheck();
    }

    @FXML
    private void repairSingle() {
        universe.getPlayer().repairShipSlow();
        updateRepairCost();
    }

    @FXML
    private void repairMax() {
        universe.getPlayer().repairShipFast();
        updateRepairCost();
    }

    private void updateRepairCost() {
        if (universe.getPlayer().getCurrentShip().getHealth()
                < universe.getPlayer().getCurrentShip().getMaxHealth()) {
            repairSingleButton.setDisable(
                    universe.getPlayer().getCredits() < universe.getPlayer().getSlowRepairPrice());
            repairSingleButton.setText("Cost: ©" + universe.getPlayer().getSlowRepairPrice());
            repairMaxButton.setDisable(
                    universe.getPlayer().getCredits() < universe.getPlayer().getFastRepairPrice());
            repairMaxButton.setText("Cost: ©" + universe.getPlayer().getFastRepairPrice());
        } else {
            repairSingleButton.setText("Hull 100%");
            repairSingleButton.setDisable(true);
            repairMaxButton.setText("Hull 100%");
            repairMaxButton.setDisable(true);
        }
    }

    @FXML
    private void handleRegionClick(MouseEvent event) {
        SoundsManager.selectPlay(.1);
        int index = (int) ((Node) event.getTarget()).getProperties().get("systemID");
        selectedRegion = universe.getRegions().get(index);
        for (XYChart.Data data : mapData.getData()) {
            data.getNode().getStyleClass().remove("selected");
        }
        mapData.getData().get(index).getNode().getStyleClass().add("selected");
        updateFuelCost();
        updateFuelStore();
        updateSystemInfo();
    }

    private void updateFuelCost() {
        int cost = (int) selectedRegion.getPosition().distance(
                universe.getPlayer().getLocation().getPosition());
        travelCostLabel.setText("" + cost);
    }

    @FXML
    private void warpButtonClicked() {
        if (universe.getPlayer().canTravelTo(selectedRegion)) {
            SoundsManager.warpPlay(.3);
            Encounter encounter = universe.getPlayer().tryToTravel(selectedRegion);
            if (encounter == null) {
                universe.getPlayer().travelTo(selectedRegion, true);
            } else {
                updateEncounterPane(encounter);
                toggleEncounter();
            }
            updateFuelStore();
            updateFuelCost();
            updateRepairCost();
            updateMap();
            updateSystemInfo();
            endGameCheck();
        }
    }

    @FXML
    private void toggleMarket() {
        if (marketPane.getStyleClass().contains("closedPane")) {
            updateMarketStock();
            marketPane.getStyleClass().remove("closedPane");
            marketPane.toFront();
        } else {
            marketPane.getStyleClass().add("closedPane");
            marketPane.toBack();
            updateFuelCost();
            updateFuelStore();
            updateRepairCost();
        }
        endGameCheck();
    }
    
    private void toggleEncounter() {
        if (encounterPane.getStyleClass().contains("closedPane")) {
            encounterPane.getStyleClass().remove("closedPane");
            encounterPane.toFront();
        } else {
            encounterPane.getStyleClass().add("closedPane");
            encounterPane.toBack();
        }
    }

    private void endGameCheck() {
        String endMessage = "";
        if (universe.gameWon()) {
            endMessage = "You Win!";
        } else if (universe.getPlayer().getCredits() == 0
            && universe.getPlayer().getCurrentShip().getInventory().size() == 0
            && !canTravel) {
            //anticlimactic ending
            endMessage = "You were defeated by capitalism and/or your own greed and/or bandits.";
        } else if (universe.getPlayer().getCurrentShip().getHealth() <= 0) {
            //ship ded ending
            endMessage = "You were defeated by bandits and/or your own greed and/or the police.";
        }
        if (!endMessage.equals("")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "layouts/endScene_layout.fxml"));
            try {
                Parent root = loader.load();
                Scene scene = new Scene(root);
                ((EndScreenController) loader.getController()).setup(
                        stage, endMessage);
                stage.setScene(scene);
            } catch (IOException e) {
                System.out.println("oops, this is broked");
            }
        }
    }
}
