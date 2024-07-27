package command;

import service.GoogleApiFacade;

import java.io.IOException;

public class ListDomainsCommand implements Command{
    private GoogleApiFacade apiFacade;

    public ListDomainsCommand(GoogleApiFacade apiFacade) {
        this.apiFacade = apiFacade;
    }
    @Override
    public void execute() throws IOException {
        apiFacade.listDomains();
    }
}
