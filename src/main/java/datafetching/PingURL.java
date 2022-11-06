package datafetching;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class PingURL implements Callable<String> {
    String url;

    public PingURL(String url) {
        this.url = url;
    }

    @Override
    public String call() throws Exception {
        String result = "ERROR";
        int code = 0;
        try {
            URL siteURL = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) siteURL.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            code = connection.getResponseCode();
            if (code == 200) {
                result = "GREEN";
            }
            if (code == 301) {
                result = "REDIRECT";
            }


        } catch (Exception e) {
            result = "RED" + code;
        }
        return result +", code:" + code;
    }
}
