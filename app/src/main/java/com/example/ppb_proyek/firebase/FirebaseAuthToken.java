package com.example.ppb_proyek.firebase;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonParser;
import com.google.auth.oauth2.ServiceAccountCredentials;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

public class FirebaseAuthToken {
    public static void main(String[] args) {
        try {
            // Muat file JSON Service Account
            FileInputStream serviceAccount = new FileInputStream("path/to/your/service-account-file.json");

            // Gunakan ServiceAccountCredentials untuk mendapatkan token akses
            GoogleCredentials credentials = ServiceAccountCredentials.fromStream(serviceAccount)
                    .createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.messaging"));

            // Dapatkan token akses
            String accessToken = credentials.refreshAccessToken().getTokenValue();
            System.out.println("Access Token: " + accessToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
