package ru.ifmo.egalkin.vought.sensors.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SensorData {

    private Integer employeesInOffice;
    private Integer activeIncidents;
    private TensionLevel tensionLevel;
    private ProtectionLevel protectionLevel;

}
