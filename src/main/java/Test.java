import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.directory.Directory;
import com.google.api.services.siteVerification.SiteVerification;
import command.AddDomainCommand;
import command.DeleteDomainCommand;
import command.ListDomainsCommand;
import service.GoogleApiFacade;
import service.GoogleCredentialsFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Random;

import static util.Config.CREDENTIALS_FILE_PATH;
import static util.Config.JSON_FACTORY;

public class Test {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    private static final int LENGTH = 15;
    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            System.out.println(generateRandomString()+".com");
        }
    }

    public static String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(LENGTH);

        for (int i = 0; i < LENGTH; i++) {
            int index = random.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(index));
        }

        return sb.toString();
    }
    String filePath = "C:\\Users\\Fajar\\Documents\\Gsuite\\out\\artifacts\\Gsuite_jar\\domains.txt";
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

//    static NetHttpTransport HTTP_TRANSPORT;
//    static Directory service;
//    static SiteVerification verificationService;
//
//    public static void main(String[] args) throws IOException, GeneralSecurityException {
//        var HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        var credentials = GoogleCredentialsFactory.getCredentials(HTTP_TRANSPORT, JSON_FACTORY, CREDENTIALS_FILE_PATH);
//        var apiFacade = new GoogleApiFacade(HTTP_TRANSPORT, JSON_FACTORY, credentials);
//
//        new AddDomainCommand(apiFacade, "example.com").execute();
//        new ListDomainsCommand(apiFacade).execute();
//        new DeleteDomainCommand(apiFacade, "example.com").execute();
//    }

//    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
//            throws IOException {
//        // Load client secrets.
////        InputStream in = Main.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
//        String jsonString = "{\"installed\":{\"client_id\":\"56334120283-j15u9afo8b6mjsjcv18124r09i4qlevm.apps.googleusercontent.com\",\"project_id\":\"omega-sorter-430605-j8\",\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\"token_uri\":\"https://oauth2.googleapis.com/token\",\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\",\"client_secret\":\"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\",\"redirect_uris\":[\"http://localhost\"]}}";
//        InputStream in = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
//        if (in == null) {
//            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
//        }
//        GoogleClientSecrets clientSecrets =
//                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
//
//        // Build flow and trigger user authorization request.
//        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
//                .setAccessType("offline")
//                .build();
//        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
//        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
//    }
}
