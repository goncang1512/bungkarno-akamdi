package lib;
import java.util.prefs.Preferences;

public class SaveToken {
    private static final Preferences prefs = Preferences.userNodeForPackage(SaveToken.class);
    private static final String KEY_TOKEN = "myToken";

    // Simpan data
    public static void saveToken(String token) {
        prefs.put(KEY_TOKEN, token);
    }

    // Ambil data
    public static String loadToken() {
        return prefs.get(KEY_TOKEN, null); // null kalau tidak ada
    }

    // Hapus data
    public static void deleteToken() {
        prefs.remove(KEY_TOKEN);
    }
}
