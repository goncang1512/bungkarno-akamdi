package controller;

import com.example.bungkarnoacademy.Main;
import lib.Env;
import lib.Fetch;
import org.json.JSONObject;

public class MyTaskController {
    public void initialize() throws Exception {
        String userId = Main.getSession.getString("id");

        Fetch fetcher = new Fetch();
        fetcher.fetch(Env.URL_API + "/mytask/" + userId, "GET", null, null);
        JSONObject data = fetcher.getObj();

        System.out.println(data);
    }
}
