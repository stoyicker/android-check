package org.stoyicker.androidcheck.pmd

import net.sourceforge.pmd.ant.Formatter
import groovy.util.slurpersupport.GPathResult
import org.stoyicker.androidcheck.CheckExtension
import org.stoyicker.androidcheck.CommonCheck
import org.stoyicker.androidcheck.CommonConfig
import net.sourceforge.pmd.ant.PMDTask
import org.gradle.api.Project

import java.nio.file.Paths

class PmdCheck extends CommonCheck {

    PmdCheck() { super('pmd', 'androidPmd', 'Runs Android PMD') }

    @Override
    protected CommonConfig getConfig(CheckExtension extension) { return extension.pmd }

    @Override
    protected void performCheck(Project project, List<File> sources,
                                File configFile, File xmlReportFile) {
        PMDTask pmdTask = new PMDTask()

        pmdTask.cacheLocation = new File("./build/.pmd-cache")
        pmdTask.project = project.ant.antProject
        pmdTask.ruleSetFiles = configFile.toString()
        pmdTask.addFormatter(new Formatter(type: 'xml', toFile: xmlReportFile))

        pmdTask.failOnError = false
        pmdTask.failOnRuleViolation = false

        sources.findAll { it.exists() }.each {
            pmdTask.addFileset(project.ant.fileset(dir: it))
        }

        pmdTask.perform()
    }

    @Override
    protected int getErrorCount(File xmlReportFile) {
        GPathResult xml = new XmlSlurper().parseText(xmlReportFile.text)
        return xml.file.inject(0) { count, file -> count + file.violation.size() }
    }

    @Override
    protected String getErrorMessage(int errorCount, File htmlReportFile) {
        def reportPath = htmlReportFile.toURI()
        return "$errorCount PMD rule violations were found. See the report at: " +
                """\\e]8;;""" + reportPath + """"\\e\\\\""" + reportPath + """\\e]8;;\\e\\\\"""
    }
}
