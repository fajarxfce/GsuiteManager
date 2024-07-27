package command;

import service.GoogleApiFacade;

import java.io.IOException;

public class AddDomainCommand implements Command {
    private GoogleApiFacade apiFacade;
    private String domainName;

    public AddDomainCommand(GoogleApiFacade apiFacade, String domainName) {
        this.apiFacade = apiFacade;
        this.domainName = domainName;
    }

    @Override
    public void execute() throws IOException {
        apiFacade.addDomain(domainName);
    }
}