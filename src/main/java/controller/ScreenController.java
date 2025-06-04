package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

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

    public static Parent loadWithTemplate(String contentFXML) throws IOException {
        // Load template

        FXMLLoader templateLoader = new FXMLLoader(ScreenController.class.getResource("/component/side-template.fxml"));
        Parent templateRoot = templateLoader.load();

        // Ambil kontroller
        TemplateController templateController = templateLoader.getController();

        // Load konten dinamis
        Parent content = FXMLLoader.load(ScreenController.class.getResource("/" + contentFXML));

        // Set konten ke dalam contentPane
        templateController.setContent(content);

        return templateRoot;
    }

    public static Parent loadWithOutTemplate(String contentFXML) throws IOException {
        FXMLLoader loader = new FXMLLoader(ScreenController.class.getResource("/" + contentFXML));
        return loader.load();
    }

    public static void loadPage(String link) throws IOException {
        Parent page;
        if(Objects.equals(link, "login") || Objects.equals(link, "register")) {
            page = loadWithOutTemplate("com/example/bungkarnoacademy/" + link + "-page.fxml");
        } else {
            page = ScreenController.loadWithTemplate("com/example/bungkarnoacademy/" + link + "-page.fxml");
        }

        ScreenController.addScreen(link, page);
        ScreenController.activate(link);
    }

}
