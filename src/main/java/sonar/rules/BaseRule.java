package sonar.rules;

import org.aspectj.lang.JoinPoint;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;
import sonar.dto.SensorInfo;

public class BaseRule {
    protected void createIssue(SensorContext sensorContext, InputFile inputFile,
                               RuleKey ruleKey, int lineNum, String message) {
        NewIssue newIssue = sensorContext.newIssue()
                .forRule(ruleKey);

        NewIssueLocation primaryLocation = newIssue.newLocation()
                .on(inputFile)
                .at(inputFile.selectLine(lineNum))
                .message(message);
        newIssue.at(primaryLocation);
        newIssue.save();
    }

    protected SensorInfo getSensorInfo(JoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            return  (SensorInfo) args[1];
        } catch (Exception e) {
            return null;
        }
    }
}
