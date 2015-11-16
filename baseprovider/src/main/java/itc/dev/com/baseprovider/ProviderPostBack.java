package itc.dev.com.baseprovider;

/**
 * Created by pavel on 11/16/15.
 */
public interface ProviderPostBack {
    void onOperationComplete(String provider, String operation, long time, String details);
    void error(String provider, String operation, String message);
}
