package sonar.sensor;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import sonar.dto.JavaFileLine;
import sonar.dto.SensorInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JavaFileSensor implements Sensor {
    private static final Logger LOGGER = Loggers.get(JavaFileSensor.class);

    /**
     * 重写 describe 方法，描述Sensor信息
     *
     * @param sensorDescriptor 传感器描述符对象，用于设置传感器的信息
     */
    @Override
    public void describe(SensorDescriptor sensorDescriptor) {
        sensorDescriptor.name("java sensor");
        sensorDescriptor.onlyOnLanguage("java");
    }

    /**
     * 执行方法，对Java文件进行规则检查
     *
     * @param sensorContext 传感器上下文，包含文件系统等信息
     */
    @Override
    public void execute(SensorContext sensorContext) {
        FileSystem fs = sensorContext.fileSystem();
        Iterable<InputFile> javaFiles = fs.inputFiles(fs.predicates().hasLanguage("java"));
        SensorInfo sensorInfo = new SensorInfo();
        sensorInfo.setSensorContext(sensorContext);
        for (InputFile javaFile : javaFiles) {
            sensorInfo.setInputFile(javaFile);
            InputStream inputStream;
            BufferedReader reader;
            try {
                inputStream = javaFile.inputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            } catch (IOException e) {
                //LOGGER.error("File IOException:" + javaFile.filename() + ":" + e.getMessage());
                continue;
            }
            CompilationUnit compilationUnit = JavaParser.parse(inputStream);

            // 文件规则
            doJavaFile(compilationUnit, sensorInfo);

            for (MethodDeclaration method : compilationUnit.findAll(MethodDeclaration.class)) {
                // 方法规则
                doJavaMethod(method, sensorInfo);
            }

            try {
                String line;
                JavaFileLine fileLine = new JavaFileLine();
                while ((line = reader.readLine()) != null) {
                    fileLine.setLine(line);
                    fileLine.setLineNum(fileLine.getLineNum() + 1);

                    // 单行规则
                    doJavaFileLine(fileLine, sensorInfo);
                }
            } catch (IOException e) {
                LOGGER.error("Line IOException:" + javaFile.filename() + ":" + e.getMessage());
            }
        }
    }

    /**
     * 对Java文件进行特定处理。
     *
     * @param compilationUnit 要处理的Java编译单元
     * @param sensorInfo      包含传感器上下文和其他相关信息的对象
     */
    public void doJavaFile(CompilationUnit compilationUnit, SensorInfo sensorInfo) {

    }

    /**
     * 对Java方法进行特定处理。
     *
     * @param methodDeclaration 要处理的Java方法声明
     * @param sensorInfo        包含传感器上下文和其他相关信息的对象
     */
    public void doJavaMethod(MethodDeclaration methodDeclaration, SensorInfo sensorInfo) {

    }

    /**
     * 对Java文件中的每一行进行特定处理。
     *
     * @param fileLine  包含Java文件某一行信息的对象
     * @param sensorInfo 包含传感器上下文和其他相关信息的对象
     */
    public void doJavaFileLine(JavaFileLine fileLine, SensorInfo sensorInfo) {

    }
}
