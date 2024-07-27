package command;

import service.GoogleApiFacade;

public class DeleteDomainCommand implements Command {
    private GoogleApiFacade apiFacade;
    private String domainName;

    public DeleteDomainCommand(GoogleApiFacade apiFacade, String domainName) {
        this.apiFacade = apiFacade;
        this.domainName = domainName;
    }

    @Override
    public void execute() {
        apiFacade.deleteDomain(domainName);
    }
}
