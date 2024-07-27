package util;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.directory.DirectoryScopes;

import java.util.Arrays;
import java.util.List;

public class Config {
    public static final String APPLICATION_NAME = "Test Bang";
    public static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    public static final String TOKENS_DIRECTORY_PATH = "tokens";
    public static final String CREDENTIALS_FILE_PATH = "/tokens/client_secret_56334120283-j15u9afo8b6mjsjcv18124r09i4qlevm.apps.googleusercontent.com.json";
    public static final List<String> SCOPES = Arrays.asList(
            DirectoryScopes.ADMIN_DIRECTORY_USER_READONLY,
            DirectoryScopes.ADMIN_DIRECTORY_DOMAIN_READONLY,
            DirectoryScopes.ADMIN_DIRECTORY_DOMAIN,
            "https://www.googleapis.com/auth/siteverification",
            DirectoryScopes.ADMIN_DIRECTORY_USER_ALIAS,
            DirectoryScopes.ADMIN_DIRECTORY_USER_ALIAS_READONLY
    );
}
