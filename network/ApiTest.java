import java.io.*;
import java.net.*;
import java.util.*;
import com.google.gson.*;
import com.squareup.okhttp.*;

class ApiTest {

    public static void main(String[] args) {
        try {
            TranslatorTextApi translateRequest = new TranslatorTextApi();
            String response = translateRequest.Post();
            System.out.println((new JsonParser()).prettify(response));
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
