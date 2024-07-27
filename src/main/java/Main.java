import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import command.AddDomainCommand;
import command.DeleteDomainCommand;
import command.DeleteUserAliasCommand;
import service.GoogleApiFacade;
import service.GoogleCredentialsFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Scanner;

import static util.Config.JSON_FACTORY;

public class Main {

    private static String DOMAIN_PATH = "C:\\Users\\Fajar\\Documents\\Gsuite\\out\\artifacts\\Gsuite_jar\\domains.txt";
    static NetHttpTransport HTTP_TRANSPORT;
    static Credential credentials;
    static GoogleApiFacade apiFacade;

    public static void main(String[] args) throws GeneralSecurityException, IOException {

        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        credentials = GoogleCredentialsFactory.getCredentials(HTTP_TRANSPORT);
        apiFacade = new GoogleApiFacade(HTTP_TRANSPORT, JSON_FACTORY, credentials);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Add domain");
            System.out.println("2. Delete domain");
            System.out.println("3. Delete User Alias");
            System.out.println("4. Exit");
            System.out.println("Note: Please provide the file path for the domain list.");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter file: ");
                    String addDomain = scanner.nextLine();
                    addDomain(addDomain);
                    break;
                case 2:
                    System.out.print("Enter file: ");
                    String deleteDomain = scanner.nextLine();
                    deleteDomain(deleteDomain);
                    break;
                case 3:
                    System.out.print("Enter file: ");
                    String userEmail = scanner.nextLine();
                    deleteUserAlias(userEmail);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.print("Invalid choice. Please try again.");
            }
        }

    }

    public static void deleteUserAlias(String userEmail) throws IOException {
        try (var stream = Files.lines(Paths.get(userEmail))) {
            stream.forEach(value -> {
                try {
                    new DeleteUserAliasCommand(apiFacade, value).execute();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            System.err.println("Error reading email from file: " + e.getMessage());
        }
    }

    public static void deleteDomain(String domain) throws IOException {
        try (var stream = Files.lines(Paths.get(domain))) {
            stream.forEach(value -> {
                new DeleteDomainCommand(apiFacade, value).execute();
            });
        } catch (IOException e) {
            System.err.println("Error reading domains from file: " + e.getMessage());
        }
    }

    public static void addDomain(String domain) throws IOException {
        try (var stream = Files.lines(Paths.get(domain))) {
            stream.forEach(value -> {
                try {
                    new AddDomainCommand(apiFacade, value).execute();
                } catch (IOException e) {
                    System.out.println("Error adding domain: " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("Error reading domains from file: " + e.getMessage());
        }
    }
}