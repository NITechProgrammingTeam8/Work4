import java.io.*;
import java.net.*;
import java.util.*;
import com.squareup.okhttp.*;

class TranslatorTextApi {

    private static String subscriptionKey = System.getenv("TRANSLATOR_TEXT_SUBSCRIPTION_KEY");
    private static String endpoint = System.getenv("TRANSLATOR_TEXT_ENDPOINT");
    String url = endpoint + "/translate?api-version=3.0&to=de,it";

    // This function performs a POST request.
    public String Post() throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType,
                "[{\n\t\"Text\": \"Welcome to Microsoft Translator. Guess how many languages I speak!\"\n}]");
        Request request = new Request.Builder()
                .url(url).post(body)
                .addHeader("Ocp-Apim-Subscription-Key", subscriptionKey)
                .addHeader("Content-type", "application/json").build();
        Response response = HttpClient().httpClient.newCall(request).execute();
        return response.body().string();
    }
}