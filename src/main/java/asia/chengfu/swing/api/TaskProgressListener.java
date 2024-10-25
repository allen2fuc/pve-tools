package asia.chengfu.swing.api;

public interface TaskProgressListener {
    void onProgress(VMAction vmAction);

    void onComplete(VMAction vmAction);

    void onError(Exception e);
}