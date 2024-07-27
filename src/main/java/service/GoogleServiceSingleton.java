package service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.directory.Directory;
import com.google.api.services.siteVerification.SiteVerification;

import java.io.IOException;

public class GoogleServiceSingleton {
    private static Directory directoryService;
    private static SiteVerification siteVerificationService;

    private GoogleServiceSingleton() {
    }

    public static Directory getDirectoryService(NetHttpTransport httpTransport, JsonFactory jsonFactory, Credential credentials) throws IOException {
        if (directoryService == null) {
            directoryService = new Directory.Builder(httpTransport, jsonFactory, credentials)
                    .setApplicationName("Test Bang")
                    .build();
        }
        return directoryService;
    }

    public static SiteVerification getSiteVerificationService(NetHttpTransport httpTransport, JsonFactory jsonFactory, Credential credentials) throws IOException {
        if (siteVerificationService == null) {
            siteVerificationService = new SiteVerification.Builder(httpTransport, jsonFactory, credentials)
                    .setApplicationName("Test Bang")
                    .build();
        }
        return siteVerificationService;
    }
}
