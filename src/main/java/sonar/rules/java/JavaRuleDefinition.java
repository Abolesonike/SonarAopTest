package sonar.rules.java;

import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RulesDefinition;
import sonar.rules.RuleConstant;

public class JavaRuleDefinition implements RulesDefinition {
    public static final String REPO_KEY = "JavaLint";
    protected static final String REPO_NAME = REPO_KEY;
    public static final String LANGUAGE = "java";

    public static final RuleKey JAVA_INVALID_PAGE_HELPER = RuleKey.of(REPO_KEY, RuleConstant.JAVA_INVALID_PAGE_HELPER);
    public static final RuleKey JAVA_LOOP_QUERY = RuleKey.of(REPO_KEY, RuleConstant.JAVA_LOOP_QUERY);

    @Override
    public void define(Context context) {
        NewRepository repository = context.createRepository(REPO_KEY, LANGUAGE).setName(REPO_NAME);

        NewRule javaRule01 = repository.createRule(JAVA_INVALID_PAGE_HELPER.rule())
                .setName("invalid pageHelper")
                .setHtmlDescription("失效的pageHelper分页查询。")
                .setTags("java", "pagehelper")
                .setType(RuleType.BUG)
                .setSeverity(Severity.MINOR);
        javaRule01.setDebtRemediationFunction(javaRule01.debtRemediationFunctions().linear("20min"));

        NewRule javaRule02 = repository.createRule(JAVA_LOOP_QUERY.rule())
                .setName("loop query")
                .setHtmlDescription("循环语句查询数据库。")
                .setTags("java")
                .setType(RuleType.CODE_SMELL)
                .setSeverity(Severity.MINOR);
        javaRule02.setDebtRemediationFunction(javaRule02.debtRemediationFunctions().linear("20min"));

        repository.done();
    }
}
