package sonar.rules;

import org.aspectj.lang.JoinPoint;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.rule.RuleKey;
import sonar.dto.SensorInfo;

public class BaseRule {
    /**
     * 创建并保存一个新的issue。
     *
     * @param sensorContext 传感器上下文对象，用于创建新的issue
     * @param inputFile     引发issue的输入文件
     * @param ruleKey       触发issue的规则键
     * @param lineNum       触发issue的行号
     * @param message       issue的详细信息
     */
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

    /**
     * 从连接点（JoinPoint）中获取SensorInfo对象。
     *
     * @param joinPoint 连接点对象，通常表示一个方法执行点
     * @return 返回从连接点参数中获取的SensorInfo对象，如果获取失败则返回null
     */
    protected SensorInfo getSensorInfo(JoinPoint joinPoint) {
        try {
            Object[] args = joinPoint.getArgs();
            return  (SensorInfo) args[1];
        } catch (Exception e) {
            return null;
        }
    }
}
