package sonar.sensor;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import sonar.dto.JavaFileLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class JavaFileSensor implements Sensor {

    @Override
    public void describe(SensorDescriptor sensorDescriptor) {

    }

    @Override
    public void execute(SensorContext sensorContext) {
        FileSystem fs = sensorContext.fileSystem();
        Iterable<InputFile> javaFiles = fs.inputFiles(fs.predicates().hasLanguage("java"));
        for (InputFile javaFile : javaFiles) {
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
            doJavaFile(compilationUnit);

            for (MethodDeclaration method : compilationUnit.findAll(MethodDeclaration.class)) {
                // 方法规则
                doJavaMethod(method);
            }

            try {
                String line;
                JavaFileLine fileLine = new JavaFileLine();
                while ((line = reader.readLine()) != null) {
                    fileLine.setLine(line);
                    fileLine.setLineNum(fileLine.getLineNum() + 1);

                    // 单行规则
                    doJavaFileLine(fileLine);
                }
            } catch (IOException e) {
                //LOGGER.error("Line IOException:" + javaFile.filename() + ":" + e.getMessage());
            }
        }
    }

    public void doJavaFile(CompilationUnit compilationUnit) {

    }

    public void doJavaMethod(MethodDeclaration methodDeclaration) {

    }

    public void doJavaFileLine(JavaFileLine fileLine) {

    }
}
