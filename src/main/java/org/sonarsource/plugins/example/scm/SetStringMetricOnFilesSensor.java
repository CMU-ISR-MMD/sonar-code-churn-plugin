package org.sonarsource.plugins.example.scm;

import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;

import static org.sonarsource.plugins.example.scm.ScmMetrics.STRING_METRIC_TEST;

/**
 * Created by Yinong Xun on 2020/12/9.
 */
public class SetStringMetricOnFilesSensor implements Sensor {
    @Override
    public void describe(SensorDescriptor sensorDescriptor) {
        sensorDescriptor.name("Set a string on a file");
    }

    @Override
    public void execute(SensorContext context) {
        FileSystem fs = context.fileSystem();

        String temp = "Abraham Lincoln was an American statesman and lawyer who served as the 16th president of the United States from 1861 to 1865. Lincoln led the nation through the American Civil War, the country's greatest moral, constitutional, and political crisis. He succeeded in preserving the Union, abolishing slavery, bolstering the federal government, and modernizing the U.S. economy. Lincoln was born into poverty in a log cabin and was raised on the frontier primarily in Indiana. He was self-educated and became a lawyer, Whig Party leader, Illinois state legislator, and U.S. Congressman from Illinois. In 1849, he returned to his law practice but became vexed by the opening of additional lands to slavery as a result of the Kansas–Nebraska Act. He reentered politics in 1854, becoming a leader in the new Republican Party, and he reached a national audience in the 1858 debates against Stephen Douglas. Lincoln ran for President in 1860, sweeping the North in victory. Pro-slavery elements in the South equated his success with the North's rejection of their right to practice slavery, and southern states began seceding from the union. To secure its independence, the new Confederate States fired on Fort Sumter, a U.S. fort in the South, and Lincoln called up forces to suppress the rebellion and restore the Union. As the leader of moderate Republicans, Lincoln had to navigate a contentious array of factions with friends and opponents on both sides. War Democrats rallied a large faction of former opponents into his moderate camp, but they were countered by Radical Republicans, who demanded harsh treatment of the Southern traitors. Anti-war Democrats (called \"Copperheads\") despised him, and irreconcilable pro-Confederate elements plotted his assassination. Lincoln managed the factions by exploiting their mutual enmity, by carefully distributing political patronage, and by appealing to the U.S. people. His Gettysburg Address became a historic clarion call for nationalism, republicanism, equal rights, liberty, and democracy. Lincoln scrutinized the strategy and tactics in the war effort, including the selection of generals and the naval blockade of the South's trade. He suspended habeas corpus, and he averted British intervention by defusing the Trent Affair. He engineered the end to slavery with his Emancipation Proclamation and his order that the Army protect and recruit former slaves. He also encouraged border states to outlaw slavery, and promoted the Thirteenth Amendment to the United States Constitution, which outlawed slavery across the country. Lincoln managed his own successful re-election campaign. He sought to heal the war-torn nation through reconciliation. On April 14, 1865, just days after the war's end at Appomattox, Lincoln was attending a play at Ford's Theatre with his wife Mary when he was assassinated by Confederate sympathizer John Wilkes Booth. His marriage had produced four sons, two of whom preceded him in death, with severe emotional impact upon him and Mary. Lincoln is remembered as the martyr hero of the United States and he is consistently ranked as one of the greatest presidents in American history.";

        // only "main" files, but not "tests"
        Iterable<InputFile> files = fs.inputFiles(fs.predicates().hasType(InputFile.Type.MAIN));
        for (InputFile file : files) {
            context.<String>newMeasure()
                    .forMetric(STRING_METRIC_TEST)
                    .on(file)
                    .withValue(temp)
                    .save();
        }
    }
}
