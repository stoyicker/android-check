Android Check 2
===============

Static code analysis plugin for Android projects.
This is a fork of [the original android-check plugin][1], which implements a really useful concept, but unfortunately seems abandoned.

*Current version tested with Android plugin for Gradle 3.1.3*.

Who uses this?
------------
[![Schibsted Products & Technology](https://i.imgur.com/YwLGGgJ.png)](http://www.schibsted.com/en/About-Schibsted/Schibsted-Products-and-Technology/)
[![China Euro Vehicle Technology](https://i.imgur.com/8OxYcwv.png)](http://www.cevt.se/)


Build status
------------

### master [![master](https://travis-ci.org/stoyicker/android-check-2.svg?branch=master)](https://travis-ci.org/stoyicker/android-check-2)
### dev [![dev](https://travis-ci.org/stoyicker/android-check-2.svg?branch=dev)](https://travis-ci.org/stoyicker/android-check-2)

Usage
-----

[ ![Download](https://api.bintray.com/packages/stoyicker-org/android-check-2/org.stoyicker.android-check/images/download.svg) ](https://bintray.com/stoyicker-org/android-check-2/org.stoyicker.android-check/_latestVersion)

This plugin is available in [the Gradle Plugin Portal](https://plugins.gradle.org/plugin/org.stoyicker.android-check) and jCenter. It attaches itself to the `check` task, but you can also execute the corresponding tasks manually when desired: `androidCheckstyle` for CheckStyle, and `androidPmd` for PMD.

```java
classpath("org.stoyicker.android-check:plugin:+") {
    // This is to avoid some conflicts with lint due to how classloading is performed by Gradle
    exclude module: "asm"
    exclude module: "gson"
    exclude module: "guava"
    exclude module: "commons-logging"
}
``` 

Apply the plugin after applying either com.android.application or com.android.library.

```java
apply plugin: "org.stoyicker.android-check"
```

Configuration
-------------

### Recommended

The default one.

### Customized

```java
// Configuration is completely optional, defaults will be used if not present
check {
  // Do absolutely nothing, default: false
  skip true/false
  // Fails build if a violation is found, default: true
  abortOnError true/false. Ignored if all per-tool confs are set to abortOnError false (see below)
  // Checkstyle configuration
  checkstyle {
    // Completely skip CheckStyle, default: false
    skip true/false

    // Fails build if CheckStyle rule violation is found, default: false
    abortOnError true/false

    // Configuration file for CheckStyle, default: <project_path>/config/checkstyle.xml, if non-existent then <project_path>/<module_path>/config/checkstyle.xml, if non-existent then plugin/src/main/resources/checkstyle/conf-default.xml
    config 'path/to/checkstyle.xml'

    // Output file for XML reports, default: new File(project.buildDir, 'outputs/checkstyle/checkstyle.xml')
    reportXML new File(project.buildDir, 'path/where/you/want/checkstyle.xml')

    // Output file for HTML reports, default: new File(project.buildDir, 'outputs/checkstyle/checkstyle.html')
    reportHTML new File(project.buildDir, 'path/where/you/want/checkstyle.html')
  }
  // PMD configuration
  pmd {
    // Same options as Checkstyle, except for a couple of defaults:

    // Configuration file for CheckStyle, default: <project_path>/config/pmd.xml, if non-existent then <project_path>/<module_path>/config/pmd.xml, if non-existent then plugin/src/main/resources/pmd/conf-default.xml
    config 'path/to/pmd.xml'

    // Output file for XML reports, default: new File(project.buildDir, 'outputs/pmd/pmd.xml')
    reportXML new File(project.buildDir, 'path/where/you/want/pmd.xml')
    
    // Output file for HTML reports, default: new File(project.buildDir, 'outputs/pmd/pmd.html')
    reportHTML new File(project.buildDir, 'path/where/you/want/pmd.html')
  }
}
```

Also, if `abortOnError` is `true`, the browser will open the report for the tool that caused the failure (if your system supports it).

Developed By
============

The original version of this plugin was developed by:

  - [Noveo Group][2]
  - [Pavel Stepanov](https://github.com/stefan-nsk) - <pstepanov@noveogroup.com>

This fork is owned and maintained by [Jorge Antonio Diaz-Benito Soriano](https://www.linkedin.com/in/jorgediazbenitosoriano).

License
=======

See [LICENSE.txt](LICENSE.txt).

Original work licensed under [MIT license](https://github.com/noveogroup/android-check/blob/master/LICENSE.txt).

[1]: https://github.com/noveogroup/android-check
[2]: http://noveogroup.com/
