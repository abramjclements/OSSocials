package edu.byu.cs.tweeter.model.net;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import edu.byu.cs.tweeter.model.service.request.LoginRequest;
import edu.byu.cs.tweeter.model.service.response.LoginResponse;

public class Server {
    private final String baseAPI = "https://x5404fmdia.execute-api.us-west-2.amazonaws.com/Beta";

    public LoginResponse login(LoginRequest request) throws TweeterRemoteException {
        String urlString = baseAPI + "/login";

        String stringResponse = makeAPICall(urlString, "POST", request);

        Gson gson = new Gson();
        LoginResponse response = gson.fromJson(stringResponse, LoginResponse.class);
        return response;

    }

    private String makeAPICall(String apiURL, String method, Object request) throws TweeterRemoteException {
        try {
            URL url = new URL(apiURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            Gson gson = new Gson();
            String json = gson.toJson(request);
            System.out.println(json);

            OutputStreamWriter os = new OutputStreamWriter(connection.getOutputStream());
            os.write(json);
            os.close();

            BufferedReader is = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line = null;
            while ((line = is.readLine()) != null) {
                response.append(line);
            }
            return response.toString();

        } catch (IOException e) {
            throw new TweeterRemoteException(e.getMessage(), null, null);
        }
    }

}