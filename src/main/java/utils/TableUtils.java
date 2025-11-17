package main.java.utils;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableView;

public class TableUtils {
    public static void autoResizeColumns(TableView<?> table) {
        table.setColumnResizePolicy(param -> {
            double tableWidth = table.getWidth();
            int columnCount = table.getColumns().size();
            double totalMinWidth = table.getColumns().stream().mapToDouble(TableColumnBase::getMinWidth).sum();
            double extraWidth = Math.max(0, tableWidth - totalMinWidth);
            for (TableColumn<?, ?> col : table.getColumns()) {
                double newWidth = col.getMinWidth() + extraWidth / columnCount;
                col.setPrefWidth(newWidth);
            }
            return true;
        });
    }

    @SafeVarargs
    public static <T> void setupTable(TableView<T> table, ObservableList<T> data, TableColumn<T, ?>... columns) {
        
        // UI settings
        for (TableColumn<T, ?> col : columns) {
            col.setReorderable(false);
        }
        table.setItems(data);

        // Gọi hàm resize
        autoResizeColumns(table);
    }
}
