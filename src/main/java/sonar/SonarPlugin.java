package sonar;

import org.sonar.api.Plugin;
import sonar.rules.java.JavaRuleDefinition;
import sonar.sensor.JavaFileSensor;

public class SonarPlugin implements Plugin {
    @Override
    public void define(Context context) {
        // rules
        context.addExtensions(JavaRuleDefinition.class, JavaFileSensor.class);

    }
}
