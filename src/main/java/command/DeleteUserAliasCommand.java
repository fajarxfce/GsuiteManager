package command;

import service.GoogleApiFacade;

import java.io.IOException;

public class DeleteUserAliasCommand implements Command{
    private GoogleApiFacade apiFacade;
    private String userEmail;
    public DeleteUserAliasCommand(GoogleApiFacade apiFacade, String userEmail) {
        this.apiFacade = apiFacade;
        this.userEmail = userEmail;
    }

    @Override
    public void execute() throws IOException {
        apiFacade.deleteAllAliases(userEmail);
    }
}
