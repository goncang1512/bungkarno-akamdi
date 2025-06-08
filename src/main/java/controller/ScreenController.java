package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;

public class ScreenController {
    private static HashMap<String, Parent> screenMap = new HashMap<>();
    private static Scene main;

    public static void init(Scene scene) {
        main = scene;
    }

    public static void addScreen(String name, Parent screen) {
        screenMap.put(name, screen);
    }

    public static void removeScreen(String name) {
        screenMap.remove(name);
    }

    public static void activate(String name) {
        if (main != null && screenMap.containsKey(name)) {
            main.setRoot(screenMap.get(name));
        } else {
            System.out.println("Scene belum diinisialisasi atau screen tidak ditemukan: " + name);
        }
    }

    public static Parent loadWithTemplate(FXMLLoader contentLoader) throws IOException {
        // Load template
        FXMLLoader templateLoader = new FXMLLoader(ScreenController.class.getResource("/component/side-template.fxml"));
        Parent templateRoot = templateLoader.load();

        // Ambil controller template
        TemplateController templateController = templateLoader.getController();

        // Load konten
        Parent content = contentLoader.load();

        // Set konten ke dalam template
        templateController.setContent(content);

        return templateRoot;
    }

    public static Parent loadWithOutTemplate(FXMLLoader contentLoader) throws IOException {
        return contentLoader.load();
    }

    // Versi default tanpa parameter controller
    public static void loadPage(String link) throws IOException {
        loadPage(link, null);
    }

    // Versi dengan akses ke controller
    public static void loadPage(String link, Consumer<Object> controllerSetup) throws IOException {
        String fxmlPath = "com/example/bungkarnoacademy/" + link + "-page.fxml";
        FXMLLoader loader = new FXMLLoader(ScreenController.class.getResource("/" + fxmlPath));

        Parent page;
        if (Objects.equals(link, "login") || Objects.equals(link, "register")) {
            page = loadWithOutTemplate(loader);
        } else {
            page = loadWithTemplate(loader);
        }

        // Setup controller jika diminta
        Object controller = loader.getController();
        if (controllerSetup != null && controller != null) {
            controllerSetup.accept(controller);
        }

        addScreen(link, page);
        activate(link);
    }
}
