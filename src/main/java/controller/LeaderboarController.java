package controller;

import com.example.bungkarnoacademy.Main;
import lib.Env;
import lib.Fetch;
import org.json.JSONObject;

public class LeaderboarController {
    public void initialize() throws Exception {
        Fetch fetcher = new Fetch();
        fetcher.fetch(Env.URL_API + "/leaderboard", "GET", null, null);
        JSONObject data = fetcher.getObj();

        System.out.println(data);
    }
}
