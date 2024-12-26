package sonar.dto;

import lombok.Data;

@Data
public class JavaFileLine {
    private String line;
    private int lineNum = 0;

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }
}
