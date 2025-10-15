<<<<<<< HEAD
package com.example.smart_attendance.service;



import com.fazecast.jSerialComm.SerialPort;
import org.springframework.stereotype.Service;

@Service
public class BeaconService {

    public void startBeacon(String portDescription) {
        SerialPort[] ports = SerialPort.getCommPorts();
        SerialPort esp32Port = null;

        for (SerialPort port : ports) {
            if (port.getPortDescription().equals(portDescription)) {
                esp32Port = port;
                break;
            }
        }

        if (esp32Port != null) {
            try {
                esp32Port.openPort();
                esp32Port.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
                byte[] startCommand = "START\n".getBytes();
                esp32Port.getOutputStream().write(startCommand);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (esp32Port.isOpen()) {
                    esp32Port.closePort();
                }
            }
        } else {
            System.out.println("ESP32 not found on port: " + portDescription);
        }
    }
}
=======
package com.example.smart_attendance.service;



import com.fazecast.jSerialComm.SerialPort;
import org.springframework.stereotype.Service;

@Service
public class BeaconService {

    public void startBeacon(String portDescription) {
        SerialPort[] ports = SerialPort.getCommPorts();
        SerialPort esp32Port = null;

        for (SerialPort port : ports) {
            if (port.getPortDescription().equals(portDescription)) {
                esp32Port = port;
                break;
            }
        }

        if (esp32Port != null) {
            try {
                esp32Port.openPort();
                esp32Port.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
                byte[] startCommand = "START\n".getBytes();
                esp32Port.getOutputStream().write(startCommand);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (esp32Port.isOpen()) {
                    esp32Port.closePort();
                }
            }
        } else {
            System.out.println("ESP32 not found on port: " + portDescription);
        }
    }
}
>>>>>>> 3373ec0d1e8d9aadfaab4b21072be139b3ff6bd5
