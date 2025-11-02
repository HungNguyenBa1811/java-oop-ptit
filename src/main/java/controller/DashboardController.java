package main.java.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class DashboardController {

    @FXML
    private TextField inputField;

    @FXML
    private Button addButton;

    @FXML
    private TableView<Item> tableView;

    @FXML
    private TableColumn<Item, Integer> colIndex;

    @FXML
    private TableColumn<Item, String> colValue;

    private final ObservableList<Item> items = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colIndex.setCellValueFactory(new PropertyValueFactory<>("index"));
        colValue.setCellValueFactory(new PropertyValueFactory<>("value"));
        tableView.setItems(items);
    }

    @FXML
    private void handleAdd() {
        String text = inputField.getText();
        if (text == null || text.trim().isEmpty()) return;
        int idx = items.size() + 1;
        items.add(new Item(idx, text.trim()));
        inputField.clear();
    }

    // Simple POJO for table rows
    public static class Item {
        private final Integer index;
        private final String value;

        public Item(Integer index, String value) {
            this.index = index;
            this.value = value;
        }

        public Integer getIndex() { return index; }
        public String getValue() { return value; }
    }
}
