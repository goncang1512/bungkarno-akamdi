package lib;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Fetch {
    private String fetchData;

    // Method utama untuk fetch data sebagai String
    public String fetch(String urlStr, String method, String body) throws  Exception {
        String data = fetch(urlStr, method, body, null);

        this.fetchData = data;

        return data; // gunakan overload tanpa headers
    }

    public String fetch(String urlStr, String method, String body, Map<String, String> headers) throws Exception {
        StringBuilder response = new StringBuilder();

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);

        conn.setRequestProperty("x-api-key", Env.API_KEY);

        // Tambahkan headers jika ada
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        // Kirim body jika bukan GET
        if (body != null && !method.equalsIgnoreCase("GET")) {
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json"); // sesuaikan jika perlu
            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes("UTF-8"));
            }
        }

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        // Gunakan getInputStream untuk success, getErrorStream untuk error
        BufferedReader reader;
        if (responseCode >= 200 && responseCode < 300) {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            reader = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }

        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();

        fetchData = response.toString();

        return response.toString();
    }

    public JSONArray getData() {
        JSONArray array = new JSONArray(fetchData);
        return array;
    }

    public JSONObject getObj() {
        JSONObject data = new JSONObject(fetchData);

        return data;
    }
}
