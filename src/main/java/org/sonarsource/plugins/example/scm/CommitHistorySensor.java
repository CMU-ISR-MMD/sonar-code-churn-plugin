package org.sonarsource.plugins.example.scm;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.log.Loggers;

import java.io.IOException;

import static org.sonarsource.plugins.example.scm.ScmMetrics.COMMIT_HISTORY;
import static org.sonarsource.plugins.example.scm.ScmMetrics.STRING_METRIC_TEST;

/**
 * Created by Yinong Xun on 2020/12/9.
 */
public class CommitHistorySensor implements Sensor {
    @Override
    public void describe(SensorDescriptor sensorDescriptor) {
        sensorDescriptor.name("Get commit history and pass is to measure computer");
    }

    @Override
    public void execute(SensorContext context) {
        FileSystem fs = context.fileSystem();

        String workDir = fs.workDir().getAbsolutePath();
        String baseDir = fs.baseDir().getAbsolutePath();
        Loggers.get(getClass()).info(fs.workDir().getAbsolutePath());
        Loggers.get(getClass()).info(fs.baseDir().getAbsolutePath());

        String commitHistoryJson;
        try {
            commitHistoryJson = CommitUtil.establishCommitHistory(baseDir);
        } catch (IOException | GitAPIException e) {
            commitHistoryJson = "[]";
        }

        Loggers.get(getClass()).info("CommitHistorySensor is executing, and the length of commitHistory is: " + commitHistoryJson.length());

        context.<String>newMeasure()
                .forMetric(COMMIT_HISTORY)
                .on(context.module())
                .withValue(commitHistoryJson)
                .save();
    }
}
