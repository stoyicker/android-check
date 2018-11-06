package org.stoyicker.androidcheck

import org.gradle.api.GradleException
import org.gradle.api.Project

import java.awt.*
import java.util.List

abstract class CommonCheck<Config extends CommonConfig> {

    final String taskCode
    final String taskName
    final String taskDescription

    CommonCheck(String taskCode, String taskName, String taskDescription) {
        this.taskCode = taskCode
        this.taskName = taskName
        this.taskDescription = taskDescription
    }

    private static enum OsType {
        TYPE_LINUX("xdg-open %s"),
        TYPE_WINDOWS("start microsoft-edge:%s"),
        TYPE_MAC_OS("open %s")

        private String mCmdReplacement

        OsType(String cmdReplacement) {
            mCmdReplacement = cmdReplacement
        }
    }

    protected Set<String> getDependencies() { [] }

    protected abstract Config getConfig(CheckExtension extension)

    protected abstract void performCheck(Project project, List<File> sources,
                                         File configFile, File xmlReportFile)

    protected abstract int getErrorCount(File xmlReportFile)

    protected abstract String getErrorMessage(int errorCount, File htmlReportFile)

    protected void reformatReport(Project project, File styleFile,
                                  File xmlReportFile, File htmlReportFile) {
        project.ant.xslt(in: xmlReportFile, out: htmlReportFile) {
            style { string(styleFile.text) }
        }
    }

    void apply(Project target) {
        target.task(
                [group      : 'verification',
                 description: taskDescription],
                taskName).doLast {
            CheckExtension extension = target.extensions.findByType(CheckExtension)
            Config config = getConfig(extension)

            boolean skip = config.resolveSkip(extension.getSkip())
            boolean abortOnError = config.resolveAbortOnError(extension.getAbortOnError())
            File configFile = config.resolveConfigFile(taskCode)
            File styleFile = config.resolveStyleFile(taskCode)
            File xmlReportFile = config.resolveXmlReportFile(taskCode)
            File htmlReportFile = config.resolveHtmlReportFile(taskCode)
            List<File> sources = config.getAndroidSources()

            if (skip) {
                target.logger.warn "Skipping $taskName"
            } else {
                xmlReportFile.parentFile.mkdirs()
                performCheck(target, sources, configFile, xmlReportFile)
                htmlReportFile.parentFile.mkdirs()
                reformatReport(target, styleFile, xmlReportFile, htmlReportFile)

                int errorCount = getErrorCount(xmlReportFile)
                if (errorCount) {
                    String errorMessage = getErrorMessage(errorCount, htmlReportFile)
                    if (abortOnError) {
                        final String path = "file://" + htmlReportFile.absolutePath
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().browse(new URI(path))
                        } else {
                            final String osName = System.getProperty("os.name")
                            final String osNameMatch = osName.toLowerCase(Locale.ENGLISH)
                            final OsType osType
                            if(osNameMatch.contains("linux")) {
                                osType = OsType.TYPE_LINUX
                            } else if(osNameMatch.contains("windows")) {
                                osType = OsType.TYPE_WINDOWS
                            } else if(osNameMatch.contains("mac os") || osNameMatch.contains("macos") || osNameMatch.contains("darwin")) {
                                osType = OsType.TYPE_MAC_OS
                            } else {
                                target.logger.warn "Your system was not identified as able to auto-open the report. " +
                                        "Please open an issue at https://github.com/stoyicker/android-check-2/issues/"
                                osType = null
                            }
                            if (osType != null) {
                                Runtime.getRuntime().exec(String.format(osType.mCmdReplacement, path, Locale.ENGLISH))
                            }
                        }
                        throw new GradleException(errorMessage)
                    } else {
                        target.logger.warn errorMessage
                    }
                }
            }
        }

        target.afterEvaluate {
            if (target.tasks.find({ it.name == 'check' }) != null) {
                target.tasks.getByName('check').dependsOn taskName
            } else {
                target.logger.warn
                "task check not found in project $target.name. You may need to run the plugin tasks manually"
            }
        }
        dependencies.each { target.tasks.getByName(taskName).dependsOn it }
    }

}
