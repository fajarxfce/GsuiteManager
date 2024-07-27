package service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.auth.oauth2.GoogleCredentials;
import com.sun.tools.javac.Main;
import util.Config;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static util.Config.*;

public class GoogleCredentialsFactory {
    public static GoogleCredentials getGoogleCredentials(String credentialsFilePath) throws IOException {
        InputStream in = Main.class.getResourceAsStream(credentialsFilePath);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + credentialsFilePath);
        }
        return GoogleCredentials.fromStream(in).createScoped(SCOPES);
    }

    public static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
//        InputStream in = Main.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        String jsonString = "{\"installed\":{\"client_id\":\"56334120283-j15u9afo8b6mjsjcv18124r09i4qlevm.apps.googleusercontent.com\",\"project_id\":\"omega-sorter-430605-j8\",\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\"token_uri\":\"https://oauth2.googleapis.com/token\",\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\",\"client_secret\":\"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\",\"redirect_uris\":[\"http://localhost\"]}}";
        InputStream in = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
        if (in == null) {
            throw new FileNotFoundException("Resource not found: ");
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}
