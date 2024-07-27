package command;

import service.GoogleApiFacade;

import java.io.IOException;

public class GetUserCommand implements Command{

    private GoogleApiFacade apiFacade;
    public GetUserCommand(GoogleApiFacade apiFacade) {
        this.apiFacade = apiFacade;
    }

    @Override
    public void execute() throws IOException {
        apiFacade.getUser();
    }
}
