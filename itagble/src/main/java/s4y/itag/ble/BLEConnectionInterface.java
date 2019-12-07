package s4y.itag.ble;

import s4y.rasat.Observable;

public interface BLEConnectionInterface extends AutoCloseable {
    Observable<AlertVolume> observableImmediateAlert();
    Observable<Boolean> observableFindeMe();
    Observable<Boolean> observableLost();
    Observable<Integer> observableRSSI();

    boolean isConnected();
    BLEError connect() throws InterruptedException;
    BLEError disconnect(int timeout);
    BLEError disconnect();
    BLEError connect(int timeout);
    BLEError writeImmediateAlert(AlertVolume volume, int timeout);
    BLEError writeImmediateAlert(AlertVolume volume);
    void enableRSSI();
    void disableRSSI();
    boolean rssi();
    int getLastStatus();
}
