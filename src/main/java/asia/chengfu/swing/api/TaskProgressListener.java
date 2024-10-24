package asia.chengfu.swing.api;

public interface TaskProgressListener {
    void onProgress(String progress);

    void onComplete(String status);

    void onError(Exception e);
}