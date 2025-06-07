package com.example.ppb_proyek.utilities;

import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FCMHelper {
    private static final String TAG = "FCMHelper";
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String FCM_SEND_ENDPOINT = "https://fcm.googleapis.com/v1/projects/chat-app-8e37b/messages:send";

    private static final String SERVICE_ACCOUNT_JSON = "{\n" +
            "  \"type\": \"service_account\",\n" +
            "  \"project_id\": \"chat-app-8e37b\",\n" +
            "  \"private_key_id\": \"9828a3ada9d12ba47bfb3c63a3917d036c2fa623\",\n" +
            "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\n" +
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC8KS/wsPf/RcMP\\n" +
            "N3oSLYG6YBh/cd4tTGRyZk56B8YgZ3T4QjMadicjcAjmbgWXpdbs8xou//eV/i1f\\n" +
            "plm2DTIv8LRPXsREhcO7joRAr0i9gpag53jyz390vW1G4beTNtpQgqZXhZPpuNBe\\n" +
            "5gMaxdCT1S6CyNggnNkC6GobbqjlySiqCrb+u7l7sp22au5HNB8oPJCvt5lPojbS\\n" +
            "uEu5EJKUqo1yombaB8SzDYlXP/Eww9ppXJZV46ez2Iv7/AjnJtMRL0zEnO0a5Fod\\n" +
            "sXBoYOb5DB+3gXTrHiM1SzFi1BkkYlIiTFd0EBYbtCdepbWqMnc4NCHx2c9ewOR/\\n" +
            "L3lPhfD7AgMBAAECggEAELd+DV3DorfRdSQiIcROPr8z3frBWPftAqbpOPlA1ncq\\n" +
            "krXa8XJm1sSo3cQPliIDPsj4Xg2aswZChLl0RfLVLyIs+tI6ELZrpdmrEU4hYcjc\\n" +
            "1g3KlpPtyHLd5wq9swQQEFATNDpnYj/rr0qzIulJNyEdATyTyw30T8hTSP2jXsrB\\n" +
            "kmwhatTznA4suozp1Uu6YmSSwSDmLlRlLyq+0+Ihjk5vm9+axInLlsGdeNIeVH+V\\n" +
            "zk1KNBf1rZKrF1fpiatyLlBaL4JH7eq0WtvOgMXZSwFyd5YKeAQ4nN2HDkDd34lM\\n" +
            "Vuy5/szwq6Cif7974lXIz4HSVWMiI8Y8J/HHaWevLQKBgQD5l/Uwz/97TLCOTne6\\n" +
            "tDR9uFHDXdVutnONlq1bvgzvGDEjPKOSswi3usVadD/fuMJdgjog4HLbbNYaV0+U\\n" +
            "pTtDzSDSO+PO1O05b2aP90xC/NikTpknYgv48wFQQZoRstWT6M/st2fPZsUQqcoG\\n" +
            "6htimHl/+JVoB93M0wLr0pS4pQKBgQDA/ZB8ZscKGp3eN/mxF4N1dujtTBhSEi1X\\n" +
            "aJWy3DeF1T6n/lLHODqODkdWdc2nLCpzvZpsbbeYcDxXe26eh00m2plwAo7j638g\\n" +
            "9r5c9C0Ef/uUKXZQp5NyCChV9fG4G9Pug03nKdTFv1zeoZnm0RN9pi+5jZISInvF\\n" +
            "sACGk7wxHwKBgHhIKepHIqjp8V/zIrIALBe2HvkWnEfJd3Bxf8Ppl9QVhYCktXN6\\n" +
            "AiaAxKDtyuZm5IwpuRek8tUZ4yg4kWpiImhvT3ThVcyUQmpebD/O/pp9vW2ZTydy\\n" +
            "kbB8c1KY+sWyuEiadrFuRYHm1etM18ZzRZVDQ7JPOrkyqUoPGqZJClDdAoGBAIVX\\n" +
            "bNoXJRS9GTBtmm6aYXDqjLtQJUmnV36aOzxOXWpx0Q2/cbBGEGcQRIffcw07PcWk\\n" +
            "Tir2j7Cac4a6OM79ivf6d6NlkRa3IqpC8XpGF0iRqOBQ2XeWoRBbxIrIGI/tYjvc\\n" +
            "qxm51FIhCuij3TMWaBIbncPlpfQzsTMAjQxuzMtfAoGAdvhV7J11XUpuSeSpKZMG\\n" +
            "pQTHRozFIfl3BueptPgwlgPRkLTaf9LpOgoJeaRXu/CoNhfEqG1jU+M0//GDyxUn\\n" +
            "tMbcL4PydsbeSilhln80xuFsJgP4440/hYcOdirshJDPFKv5u4kRMxuNWPFIwtx6\\n" +
            "8uC8SKbSFc8RfZRynxz05Ok=\\n" +
            "-----END PRIVATE KEY-----\\n\",\n" +
            "  \"client_email\": \"firebase-adminsdk-fbsvc@chat-app-8e37b.iam.gserviceaccount.com\",\n" +
            "  \"client_id\": \"101500347090962040434\",\n" +
            "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
            "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
            "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
            "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-fbsvc%40chat-app-8e37b.iam.gserviceaccount.com\",\n" +
            "  \"universe_domain\": \"googleapis.com\"\n" +
            "}";

    private static GoogleCredentials googleCredentials;
    private static String cachedAccessToken;
    private static long tokenExpiryTime = 0;

    public static CompletableFuture<String> getAccessToken() {
        CompletableFuture<String> future = new CompletableFuture<>();

        // Check if we have a valid cached token
        if (cachedAccessToken != null && System.currentTimeMillis() < tokenExpiryTime) {
            future.complete(cachedAccessToken);
            return future;
        }

        // Get new token in background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (googleCredentials == null) {
                        InputStream serviceAccountStream = new ByteArrayInputStream(
                                SERVICE_ACCOUNT_JSON.getBytes()
                        );
                        googleCredentials = GoogleCredentials.fromStream(serviceAccountStream)
                                .createScoped(Arrays.asList(MESSAGING_SCOPE));
                    }

                    googleCredentials.refresh();
                    cachedAccessToken = googleCredentials.getAccessToken().getTokenValue();
                    // Cache for 50 minutes (tokens expire in 1 hour)
                    tokenExpiryTime = System.currentTimeMillis() + (50 * 60 * 1000);

                    future.complete(cachedAccessToken);
                } catch (IOException e) {
                    Log.e(TAG, "Error getting access token", e);
                    future.completeExceptionally(e);
                }
            }
        }).start();

        return future;
    }

    public static void sendNotification(String receiverToken, String title, String body, Map<String, String> data) {
        getAccessToken().thenAccept(new java.util.function.Consumer<String>() {
            @Override
            public void accept(String token) {
                if (token != null) {
                    sendNotificationWithToken(token, receiverToken, title, body, data);
                }
            }
        }).exceptionally(new java.util.function.Function<Throwable, Void>() {
            @Override
            public Void apply(Throwable throwable) {
                Log.e(TAG, "Failed to get access token", throwable);
                return null;
            }
        });
    }

    private static void sendNotificationWithToken(String accessToken, String receiverToken,
                                                  String title, String body, Map<String, String> data) {
        try {
            // Create FCM v1 message format
            JsonObject message = new JsonObject();
            JsonObject messageWrapper = new JsonObject();

            // Set target token
            message.addProperty("token", receiverToken);

            // Set notification
            JsonObject notification = new JsonObject();
            notification.addProperty("title", title);
            notification.addProperty("body", body);
            message.add("notification", notification);

            // Set data payload
            if (data != null && !data.isEmpty()) {
                JsonObject dataObject = new JsonObject();
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    dataObject.addProperty(entry.getKey(), entry.getValue());
                }
                message.add("data", dataObject);
            }

            messageWrapper.add("message", message);
            String json = new Gson().toJson(messageWrapper);

            // Send HTTP request
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = RequestBody.create(
                    json, MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(FCM_SEND_ENDPOINT)
                    .post(requestBody)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("Content-Type", "application/json")
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Failed to send notification", e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Notification sent successfully");
                    } else {
                        Log.e(TAG, "Failed to send notification: " + response.body().string());
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error creating notification", e);
        }
    }
}