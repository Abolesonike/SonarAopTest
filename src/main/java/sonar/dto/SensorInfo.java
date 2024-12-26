package sonar.dto;

import lombok.Data;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;

@Data
public class SensorInfo {
    private SensorContext sensorContext;
    private InputFile inputFile;

    public SensorContext getSensorContext() {
        return sensorContext;
    }

    public void setSensorContext(SensorContext sensorContext) {
        this.sensorContext = sensorContext;
    }

    public InputFile getInputFile() {
        return inputFile;
    }

    public void setInputFile(InputFile inputFile) {
        this.inputFile = inputFile;
    }
}
