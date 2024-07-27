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

        initializeServices();
        runMenu();

    }

    private static void initializeServices() throws GeneralSecurityException, IOException {
        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        credentials = GoogleCredentialsFactory.getCredentials(HTTP_TRANSPORT);
        apiFacade = new GoogleApiFacade(HTTP_TRANSPORT, JSON_FACTORY, credentials);
    }

    private static void runMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    handleAddDomain(scanner);
                    break;
                case 2:
                    handleDeleteDomain(scanner);
                    break;
                case 3:
                    handleDeleteUserAlias(scanner);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleAddDomain(Scanner scanner) {
        System.out.print("Enter file: ");
        String addDomain = scanner.nextLine();
        processFile(addDomain, value -> {
            try {
                new AddDomainCommand(apiFacade, value).execute();
            } catch (IOException e) {
                System.err.println("Error adding domain: " + e.getMessage());
            }
        });
    }

    private static void handleDeleteDomain(Scanner scanner) {
        System.out.print("Enter file: ");
        String deleteDomain = scanner.nextLine();
        processFile(deleteDomain, value -> {
            new DeleteDomainCommand(apiFacade, value).execute();
        });
    }

    private static void handleDeleteUserAlias(Scanner scanner) {
        System.out.print("Enter file: ");
        String userEmail = scanner.nextLine();
        processFile(userEmail, value -> {
            try {
                new DeleteUserAliasCommand(apiFacade, value).execute();
            } catch (IOException e) {
                System.err.println("Error deleting user alias: " + e.getMessage());
            }
        });
    }


    private static void displayMenu() {
        System.out.println("Choose an option:");
        System.out.println("1. Add domain");
        System.out.println("2. Delete domain");
        System.out.println("3. Delete User Alias");
        System.out.println("4. Exit");
        System.out.println("Note: Please provide the file path for the domain list.");
        System.out.print("Enter choice: ");
    }

    private static void processFile(String filePath, FileProcessor processor) {
        try (var stream = Files.lines(Paths.get(filePath))) {
            stream.forEach(processor::process);
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    @FunctionalInterface
    private interface FileProcessor {
        void process(String value);
    }
}