package ru.ifmo.egalkin.vought.sensors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ifmo.egalkin.vought.sensors.model.ProtectionLevel;
import ru.ifmo.egalkin.vought.sensors.model.SensorData;
import ru.ifmo.egalkin.vought.sensors.model.TensionLevel;
import ru.ifmo.egalkin.vought.service.EmployeeService;
import ru.ifmo.egalkin.vought.service.IncidentService;

import java.util.Random;

@Service
public class SensorHandler {

    @Autowired
    private IncidentService incidentService;

    @Autowired
    private EmployeeService employeeService;

    private Long updateTimestamp = System.currentTimeMillis();
    private final SensorData cachedSensorData = new SensorData();

    {
        Random random = new Random();
        int employeeToAdd = random.nextInt(100);
        cachedSensorData.setEmployeesInOffice(500 + (employeeToAdd % 2 == 0 ? employeeToAdd : -employeeToAdd));
    }

    public SensorData getSensorData() {
        if (System.currentTimeMillis() - updateTimestamp >= 300000) {
            Random random = new Random();
            int employeeToAdd = random.nextInt(100);
            cachedSensorData.setEmployeesInOffice(500 + (employeeToAdd % 2 == 0 ? employeeToAdd : -employeeToAdd));
            updateTimestamp = System.currentTimeMillis();
        }
        Integer activeIncidents = incidentService.getActiveIncidentsNumber();
        cachedSensorData.setActiveIncidents(activeIncidents);
        cachedSensorData.setTensionLevel(TensionLevel.resolveTensionLevel(activeIncidents));
        cachedSensorData.setProtectionLevel(ProtectionLevel.resolveProtectionLevel(employeeService.countActiveHeroes()));
        return cachedSensorData;
    }

}
