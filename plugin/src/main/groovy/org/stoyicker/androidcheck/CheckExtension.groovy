package org.stoyicker.androidcheck

import org.stoyicker.androidcheck.checkstyle.CheckstyleConfig
import org.stoyicker.androidcheck.pmd.PmdConfig
import org.gradle.api.Action
import org.gradle.api.Project

class CheckExtension {

    static final String NAME = 'check'

    private final Project project

    CheckstyleConfig checkstyle

    void checkstyle(Action<CheckstyleConfig> action) { action.execute(checkstyle) }

    PmdConfig pmd

    void pmd(Action<PmdConfig> action) { action.execute(pmd) }

    CheckExtension(Project project) {
        this.project = project
        this.checkstyle = new CheckstyleConfig(project)
        this.pmd = new PmdConfig(project)
    }

    private boolean skip = false

    void skip(boolean skip) { this.skip = skip }

    Boolean getSkip() {
        return skip
    }

    private boolean abortOnError = true

    void abortOnError(boolean abortOnError) { this.abortOnError = abortOnError }

    boolean getAbortOnError() {
        return abortOnError
    }
}
