package s4y.itag.ble;

import android.bluetooth.BluetoothGattService;

import java.util.List;

interface CBPeripheralInterace {
    String identifier();
    CBService[] services();
    CBPeripheralState state();
    void connect();
    void setDelegagte(CBPeripheralDelegate delegagte);
    CBPeripheralDelegate delegate();

    //retrievePeripheras
}
