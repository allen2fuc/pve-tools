package asia.chengfu.pve.core.exception;

public class HttpStatusException extends RuntimeException{

    private String url;
    private int status;

    public HttpStatusException(String url, int status) {
        super("HTTP access failed status code " + status);
        this.status = status;
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "HttpStatusException{" +
                "url='" + url + '\'' +
                ", status=" + status +
                '}';
    }
}
