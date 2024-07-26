import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ArrayMap;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.directory.Directory;
import com.google.api.services.directory.DirectoryScopes;
import com.google.api.services.directory.model.*;
import com.google.api.services.siteVerification.SiteVerification;
import com.google.api.services.siteVerification.model.SiteVerificationWebResourceGettokenRequest;
import com.google.api.services.siteVerification.model.SiteVerificationWebResourceGettokenResponse;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class Main {

    static NetHttpTransport HTTP_TRANSPORT;
    static Directory service;
    static SiteVerification verificationService;

    public static void main(String[] args) throws IOException, GeneralSecurityException {

//        File tokensDirectory = new File(TOKENS_DIRECTORY_PATH);
//        if (tokensDirectory.exists()) {
//            for (File file : tokensDirectory.listFiles()) {
//                file.delete();
//            }
//            tokensDirectory.delete();
//        }
// Build a new authorized API client service.
        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        service =
                new Directory.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                        .setApplicationName(APPLICATION_NAME)
                        .build();

        // Print the first 10 users in the domain.
//        Users result = service.users().list()
//                .setCustomer("my_customer")
//                .setMaxResults(10)
//                .setOrderBy("email")
//                .execute();
//        List<User> users = result.getUsers();
//        if (users == null || users.size() == 0) {
//            System.out.println("No users found.");
//        } else {
//            System.out.println("Users:");
//            for (User user : users) {
//                System.out.println(user.getName().getFullName());
//            }
//        }

//        addDomain("kontolkejepitlawang.com");
        System.out.println(getDomainVerificationToken("kontolkejepitlawang.com"));
        listDomains();
//        deleteAllAliases("testpost1@567ray.com");

//        String filePath = "C:\\Users\\Fajar\\Documents\\Gsuite\\out\\artifacts\\Gsuite_jar\\domains.txt";
//        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
//            stream.forEach( domain -> {
//                try {
//                    addDomain(domain);
//                } catch (IOException e) {
//                    System.out.println("Error adding domain: " + e.getMessage());
//                }
//            });
//        } catch (IOException e) {
//            System.err.println("Error reading domains from file: " + e.getMessage());
//        }
    }

    public static void addDomain(String domainName) throws IOException {
        // Buat objek Domain
        Domains domain = new Domains();
        domain.setDomainName(domainName);

        try {
            // Panggil API untuk menambahkan domain
            Domains result = service.domains().insert("my_customer", domain).execute();
            System.out.println("Domain added: " + result.getDomainName());
        } catch (Exception e) {
            System.err.println("Unable to add domain: " + e.getMessage());
        }
    }

    public static void deleteAllAliases(String userEmail) {
        try {
            // List all aliases for the user
            List<Object> aliasObjects = service.users().aliases().list(userEmail).execute().getAliases();
            if (aliasObjects == null || aliasObjects.isEmpty()) {
                System.out.println("No aliases found for user: " + userEmail);
                return;
            }

            List<Alias> aliases = new ArrayList<>();
            for (Object aliasObject : aliasObjects) {
                if (aliasObject instanceof ArrayMap) {
                    ArrayMap aliasMap = (ArrayMap) aliasObject;
                    Alias alias = new Alias();
                    alias.setAlias((String) aliasMap.get("alias"));
                    aliases.add(alias);
                }
            }

            for (int i = 0; i < aliases.size(); i++) {
                System.out.println(aliases.get(i).getAlias());
            }

//             Delete each alias
            for (Alias alias : aliases) {
                System.out.println(alias.getAlias());
                try {
                    service.users().aliases().delete(userEmail, alias.getAlias()).execute();
                    System.out.println("Alias deleted: " + alias.getAlias());
                } catch (Exception e) {
                    System.err.println("Unable to delete alias: " + alias.getAlias() + " - " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Unable to list aliases for user: " + userEmail + " - " + e.getMessage());
        }
    }

    public static void listDomains() throws IOException {
        Directory.Domains.List request = service.domains().list("my_customer");
        Domains2 response = request.execute();
        List<Domains> domains = response.getDomains();
        if (domains == null || domains.isEmpty()) {
            System.out.println("No domains found.");
        } else {
            System.out.println("Domains:");
            for (Domains domain : domains) {
                System.out.println(domain.getDomainName());
            }
        }
    }

    public static void deleteDomain(String domainName) {
        try {
            // Call the API to delete the domain
            service.domains().delete("my_customer", domainName).execute();
            System.out.println("Domain deleted: " + domainName);
        } catch (IOException e) {
            System.err.println("Unable to delete domain: " + e.getMessage());
        }
    }

    private static GoogleCredentials getGoogleCredentials() throws IOException {
        InputStream in = Main.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        return GoogleCredentials.fromStream(in).createScoped(SCOPES);
    }

    public static String getDomainVerificationToken(String domainName) throws IOException {
        try {
            // Create the API service
            verificationService = new SiteVerification.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY,
                    getCredentials(HTTP_TRANSPORT)
            )
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            // Create the request
            SiteVerificationWebResourceGettokenRequest request = new SiteVerificationWebResourceGettokenRequest()
                    .setVerificationMethod("DNS_TXT")
                    .setSite(new SiteVerificationWebResourceGettokenRequest.Site()
                            .setType("INET_DOMAIN")
                            .setIdentifier(domainName));

            // Execute the request
            SiteVerificationWebResourceGettokenResponse response = verificationService.webResource().getToken(request).execute();
            return response.getToken();
        } catch (GoogleJsonResponseException e) {
            System.err.println("Unable to get verification token: " + e.getDetails().getMessage());
            return "Unable to get verification token";
        }
    }

    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "Test Bang";
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens for this application.
     */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Arrays.asList(
            DirectoryScopes.ADMIN_DIRECTORY_USER_READONLY,
            DirectoryScopes.ADMIN_DIRECTORY_DOMAIN_READONLY,
            DirectoryScopes.ADMIN_DIRECTORY_DOMAIN,
            "https://www.googleapis.com/auth/siteverification",
            DirectoryScopes.ADMIN_DIRECTORY_USER_ALIAS,
            DirectoryScopes.ADMIN_DIRECTORY_USER_ALIAS_READONLY
    );
    private static final String CREDENTIALS_FILE_PATH = "/tokens/client_secret_56334120283-j15u9afo8b6mjsjcv18124r09i4qlevm.apps.googleusercontent.com.json";

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
//        InputStream in = Main.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        String jsonString = "{\"installed\":{\"client_id\":\"56334120283-j15u9afo8b6mjsjcv18124r09i4qlevm.apps.googleusercontent.com\",\"project_id\":\"omega-sorter-430605-j8\",\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\"token_uri\":\"https://oauth2.googleapis.com/token\",\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\",\"client_secret\":\"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\",\"redirect_uris\":[\"http://localhost\"]}}";
        InputStream in = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
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
