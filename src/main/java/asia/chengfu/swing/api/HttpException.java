package asia.chengfu.swing.api;

import lombok.Getter;

@Getter
public class HttpException extends RuntimeException{
    private final int code;
    private final String message;

    public HttpException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return super.getMessage();
    }
}
