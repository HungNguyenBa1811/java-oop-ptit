package main.java.view;
import main.java.utils.FXUtils;
import javafx.application.Application;
import javafx.stage.Stage;

public class AppView extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        FXUtils.loadFonts();
        FXUtils.setAppIcon(stage);
        stage.setWidth(1366);
        stage.setHeight(768);

        NavigationManager navigationManager = new NavigationManager(stage);
        navigationManager.showLoginScreen();
    }
}

/*
 * Tổng quan về Functional Interface cơ bản trong Java:
 * 
 * 1) Consumer<T>
 *    - Nhận vào 1 giá trị kiểu T
 *    - Không trả về gì (void)
 *    - Dùng để: thực thi hành động nào đó với dữ liệu (log, mở popup, xóa, update UI, ...)
 *    - Cú pháp lambda: (t) -> { ... }
 *    - Gọi hàm: consumer.accept(t)
 *
 *    Ví dụ:
 *      Consumer<User> openEditor = u -> openEditPopup(u);
 *
 * -------------------------------------------------------
 *
 * 2) Supplier<R>
 *    - Không nhận tham số
 *    - Trả về 1 giá trị kiểu R
 *    - Dùng để: tạo ra dữ liệu (object mới, ID mới, lazy load, config)
 *    - Cú pháp lambda: () -> value
 *    - Gọi hàm: supplier.get()
 *
 *    Ví dụ:
 *      Supplier<String> randomId = () -> UUID.randomUUID().toString();
 *
 * -------------------------------------------------------
 *
 * 3) Function<T, R>
 *    - Nhận vào giá trị kiểu T
 *    - Trả về giá trị kiểu R
 *    - Dùng để: chuyển đổi dữ liệu (map DTO → Entity, lấy ID, convert,...)
 *    - Cú pháp lambda: (t) -> result
 *    - Gọi hàm: function.apply(t)
 *
 *    Ví dụ:
 *      Function<UserDTO, Integer> toId = dto -> dto.getId();
 *
 * -------------------------------------------------------
 *
 * 4) Predicate<T>
 *    - Nhận vào giá trị kiểu T
 *    - Trả về boolean (true/false)
 *    - Dùng để: kiểm tra điều kiện, validate, filter
 *    - Cú pháp lambda: (t) -> true/false
 *    - Gọi hàm: predicate.test(t)
 *
 *    Ví dụ:
 *      Predicate<User> isActive = user -> user.isActive();
 *
 * -------------------------------------------------------
 *
 * Nhớ nhanh:
 *    - Consumer = nhận vào → không trả về
 *    - Supplier = không nhận → trả về
 *    - Function = nhận vào → trả về (chuyển đổi)
 *    - Predicate = nhận vào → boolean (kiểm tra)
 *
 */

/*
 * File chooser notes:
 * - Use `FileChooser` from JavaFX to show open/save dialogs.
 * - Always provide an owner window (stage) when showing dialogs to keep modality and focus.
 *   Use `main.java.utils.GenericUtils.getStageFromSource(source)` to obtain the window from an
 *   event source or a control node.
 * - CSV helpers are available in `main.java.utils.CsvUtils` for common open/save/read/write
 *   operations using semicolon (`;`) as delimiter.
 */
