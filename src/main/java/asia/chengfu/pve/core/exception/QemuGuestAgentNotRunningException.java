package asia.chengfu.pve.core.exception;

public class QemuGuestAgentNotRunningException extends HttpStatusException{
    public QemuGuestAgentNotRunningException(String url, int status) {
        super(url, status);
    }
}
