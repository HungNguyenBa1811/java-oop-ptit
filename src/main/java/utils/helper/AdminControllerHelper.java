package main.java.utils.helper;

import java.util.function.Consumer;
import java.util.function.Function;

import javafx.scene.control.TableView;
import main.java.utils.FXUtils;

/**
 * Small helpers for AdminController to avoid bloating the main utils class.
 */
public class AdminControllerHelper {
	private AdminControllerHelper() {}

	/**
	 * Generic helper that requires a selected row from a TableView, extracts an id,
	 * fetches the full entity by id and invokes the provided consumer. All UI
	 * error messages are shown via FXUtils. Returns true when onFound was invoked.
	 *
	 * @param table                TableView containing row objects
	 * @param selectMessage        Message when no row is selected
	 * @param invalidRowMessage    Message when selected row id is invalid
	 * @param notFoundMessage      Message when fetched entity is not found
	 * @param idExtractor          Function to extract id string from the row
	 * @param fetcher              Function to fetch entity by id
	 * @param onFound              Consumer invoked with the fetched entity
	 * @param <Row>                Row type in the TableView
	 * @param <Entity>             Entity type returned by fetcher
	 * @return true if the entity was found and onFound invoked; false otherwise
	 */
	public static <Row, Entity> boolean requireSelectedAndFetch(
		TableView<Row> table,
		String selectMessage,
		String invalidRowMessage,
		String notFoundMessage,
		Function<Row, String> idExtractor,
		Function<String, Entity> fetcher,
		Consumer<Entity> onFound
	) {
		Row selected = table != null ? table.getSelectionModel().getSelectedItem() : null;
		if (selected == null) {
			FXUtils.showError(selectMessage);
			return false;
		}
		String id = null;
		try {
			id = idExtractor.apply(selected);
		} catch (Exception e) {
			FXUtils.showError(invalidRowMessage);
			return false;
		}
		if (id == null || id.trim().isEmpty() || "-".equals(id)) {
			FXUtils.showError(invalidRowMessage);
			return false;
		}
		Entity entity = null;
		try {
			entity = fetcher.apply(id);
		} catch (Exception e) {
			FXUtils.showError(notFoundMessage + ": " + e.getMessage());
			return false;
		}
		if (entity == null) {
			FXUtils.showError(notFoundMessage);
			return false;
		}
		try {
			onFound.accept(entity);
			return true;
		} catch (Exception e) {
			FXUtils.showError("Hành động thất bại: " + e.getMessage());
			return false;
		}
	}
}
