package main.java.utils.helper;

import java.util.function.Consumer;
import java.util.function.Function;

import javafx.scene.control.TableView;
import main.java.utils.FXUtils;

public class AdminControllerHelper {
    
    /**
     * Generic helper to require table selection, fetch full entity, and execute callback.
     * 
     * @param <T> DTO type (row in table)
     * @param <E> Entity type (full model from service)
     * @param <ID> ID type (String, Integer, etc.)
     */
    public static <T, E, ID> void requireSelectedAndFetch(
        TableView<T> table,
        String noSelectionMessage,
        String invalidRowMessage,
        String notFoundMessage,
        Function<T, ID> idExtractor,
        Function<ID, E> fetcher,
        Consumer<E> onSuccess
    ) {
        // Check selection
        T selected = table != null ? table.getSelectionModel().getSelectedItem() : null;
        if (selected == null) {
            FXUtils.showError(noSelectionMessage);
            return;
        }
        
        // Extract ID
        ID id = idExtractor.apply(selected);
        if (id == null || (id instanceof String && ("-".equals(id) || ((String)id).trim().isEmpty()))) {
            FXUtils.showError(invalidRowMessage);
            return;
        }
        
        // Fetch entity
        E entity = fetcher.apply(id);
        if (entity == null) {
            FXUtils.showError(notFoundMessage);
            return;
        }
        
        // Execute callback
        onSuccess.accept(entity);
    }
}
