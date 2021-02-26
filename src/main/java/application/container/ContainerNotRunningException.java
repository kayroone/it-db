package application.container;

public class ContainerNotRunningException extends Exception {
    public ContainerNotRunningException(final String errorMessage) {
        super(errorMessage);
    }
}
