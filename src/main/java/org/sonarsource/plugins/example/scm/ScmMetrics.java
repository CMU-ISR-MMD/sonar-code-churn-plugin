package org.sonarsource.plugins.example.scm;

import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * Created by Yinong Xun on 2020/12/9.
 */
public class ScmMetrics implements Metrics {
    public static final Metric<String> COMMIT_HISTORY = new Metric.Builder("commit_history", "Commit History", Metric.ValueType.STRING)
            .setDescription("History of commits")
            .setDirection(Metric.DIRECTION_NONE)
            .setQualitative(false)
            .setDomain(CoreMetrics.DOMAIN_GENERAL)
            .create();

    public static final Metric<String> CODE_CHURN_HISTORY = new Metric.Builder("code_churn_history", "Code Churn History", Metric.ValueType.STRING)
            .setDescription("History of code churn")
            .setDirection(Metric.DIRECTION_NONE)
            .setQualitative(false)
            .setDomain(CoreMetrics.DOMAIN_GENERAL)
            .create();

    public static final Metric<String> STRING_METRIC_TEST = new Metric.Builder("string_metric_test", "String Metric Test", Metric.ValueType.STRING)
            .setDescription("Test the string metric")
            .setDirection(Metric.DIRECTION_NONE)
            .setQualitative(false)
            .setDomain(CoreMetrics.DOMAIN_GENERAL)
            .create();

    @Override
    public List<Metric> getMetrics() {
        return asList(COMMIT_HISTORY, CODE_CHURN_HISTORY, STRING_METRIC_TEST);
    }
}
