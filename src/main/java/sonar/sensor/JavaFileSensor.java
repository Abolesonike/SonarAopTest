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

    @Override
    public void describe(SensorDescriptor sensorDescriptor) {
        sensorDescriptor.name("java sensor");
        sensorDescriptor.onlyOnLanguage("java");
    }

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

    public void doJavaFile(CompilationUnit compilationUnit, SensorInfo sensorInfo) {

    }

    public void doJavaMethod(MethodDeclaration methodDeclaration, SensorInfo sensorInfo) {

    }

    public void doJavaFileLine(JavaFileLine fileLine, SensorInfo sensorInfo) {

    }
}
