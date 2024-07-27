package service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.ArrayMap;
import com.google.api.services.directory.Directory;
import com.google.api.services.directory.model.*;
import com.google.api.services.siteVerification.SiteVerification;
import com.google.api.services.siteVerification.model.SiteVerificationWebResourceGettokenRequest;
import com.google.api.services.siteVerification.model.SiteVerificationWebResourceGettokenResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GoogleApiFacade {
    private Directory directoryService;
    private SiteVerification siteVerificationService;

    public GoogleApiFacade(NetHttpTransport httpTransport, JsonFactory jsonFactory, Credential credentials) throws IOException {
        this.directoryService = GoogleServiceSingleton.getDirectoryService(httpTransport, jsonFactory, credentials);
        this.siteVerificationService = GoogleServiceSingleton.getSiteVerificationService(httpTransport, jsonFactory, credentials);
    }

    public void addDomain(String domainName) throws IOException {
        Domains domain = new Domains();
        domain.setDomainName(domainName);
        try {
            Domains result = directoryService.domains().insert("my_customer", domain).execute();
            System.out.println("Domain added: " + result.getDomainName());
        } catch (Exception e) {
            System.err.println("Unable to add domain: " + e.getMessage());
        }
    }

    public void deleteDomain(String domainName) {
        try {
            directoryService.domains().delete("my_customer", domainName).execute();
            System.out.println("Domain deleted: " + domainName);
        } catch (IOException e) {
            System.err.println("Unable to delete domain: " + e.getMessage());
        }
    }

    public void listDomains() throws IOException {
        Directory.Domains.List request = directoryService.domains().list("my_customer");
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

    public void getUser()throws IOException{
        Users result = directoryService.users().list()
                .setCustomer("my_customer")
                .setMaxResults(10)
                .setOrderBy("email")
                .execute();
        List<User> users = result.getUsers();
        if (users == null || users.size() == 0) {
            System.out.println("No users found.");
        } else {
            System.out.println("Users:");
            for (User user : users) {
                System.out.println(user.getName().getFullName());
            }
        }
    }

    public void deleteAllAliases(String userEmail) {
        try {
            // List all aliases for the user
            List<Object> aliasObjects = directoryService.users().aliases().list(userEmail).execute().getAliases();
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
                    directoryService.users().aliases().delete(userEmail, alias.getAlias()).execute();
                    System.out.println("Alias deleted: " + alias.getAlias());
                } catch (Exception e) {
                    System.err.println("Unable to delete alias: " + alias.getAlias() + " - " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Unable to list aliases for user: " + userEmail + " - " + e.getMessage());
        }
    }

    public String getDomainVerificationToken(String domainName) throws IOException {
        try {
            SiteVerificationWebResourceGettokenRequest request = new SiteVerificationWebResourceGettokenRequest()
                    .setVerificationMethod("DNS_TXT")
                    .setSite(new SiteVerificationWebResourceGettokenRequest.Site()
                            .setType("INET_DOMAIN")
                            .setIdentifier(domainName));
            SiteVerificationWebResourceGettokenResponse response = siteVerificationService.webResource().getToken(request).execute();
            return response.getToken();
        } catch (GoogleJsonResponseException e) {
            System.err.println("Unable to get verification token: " + e.getDetails().getMessage());
            return "Unable to get verification token";
        }
    }
}
