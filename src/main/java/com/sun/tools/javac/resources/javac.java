/*   */ package com.sun.tools.javac.resources;
/*   */ 
/*   */ public final class javac extends ListResourceBundle {
/*   */   protected final Object[][] getContents() {
/* 5 */     return new Object[][] { { "javac.err.dir.not.found", "directory not found: {0}" }, { "javac.err.empty.A.argument", "-A requires an argument; use ''-Akey'' or ''-Akey=value''" }, { "javac.err.error.writing.file", "error writing {0}; {1}" }, { "javac.err.file.not.directory", "not a directory: {0}" }, { "javac.err.file.not.file", "not a file: {0}" }, { "javac.err.file.not.found", "file not found: {0}" }, { "javac.err.invalid.A.key", "key in annotation processor option ''{0}'' is not a dot-separated sequence of identifiers" }, { "javac.err.invalid.arg", "invalid argument: {0}" }, { "javac.err.invalid.flag", "invalid flag: {0}" }, { "javac.err.invalid.profile", "invalid profile: {0}" }, { "javac.err.invalid.source", "invalid source release: {0}" }, { "javac.err.invalid.target", "invalid target release: {0}" }, { "javac.err.no.source.files", "no source files" }, { "javac.err.no.source.files.classes", "no source files or class names" }, { "javac.err.profile.bootclasspath.conflict", "profile and bootclasspath options cannot be used together" }, { "javac.err.req.arg", "{0} requires an argument" }, { "javac.fullVersion", "{0} full version \"{1}\"" }, { "javac.msg.bug", "An exception has occurred in the compiler ({0}). Please file a bug against the Java compiler via the Java bug reporting page (http://bugreport.java.com) after checking the Bug Database (http://bugs.java.com) for duplicates. Include your program and the following diagnostic in your report. Thank you." }, { "javac.msg.io", "\n\nAn input/output error occurred.\nConsult the following stack trace for details.\n" }, { "javac.msg.plugin.not.found", "plug-in not found: {0}" }, { "javac.msg.plugin.uncaught.exception", "\n\nA plugin threw an uncaught exception.\nConsult the following stack trace for details.\n" }, { "javac.msg.proc.annotation.uncaught.exception", "\n\nAn annotation processor threw an uncaught exception.\nConsult the following stack trace for details.\n" }, { "javac.msg.resource", "\n\nThe system is out of resources.\nConsult the following stack trace for details.\n" }, { "javac.msg.usage", "Usage: {0} <options> <source files>\nuse -help for a list of possible options" }, { "javac.msg.usage.header", "Usage: {0} <options> <source files>\nwhere possible options include:" }, { "javac.msg.usage.nonstandard.footer", "These options are non-standard and subject to change without notice." }, { "javac.opt.A", "Options to pass to annotation processors" }, { "javac.opt.AT", "Read options and filenames from file" }, { "javac.opt.J", "Pass <flag> directly to the runtime system" }, { "javac.opt.Werror", "Terminate compilation if warnings occur" }, { "javac.opt.X", "Print a synopsis of nonstandard options" }, { "javac.opt.Xbootclasspath.a", "Append to the bootstrap class path" }, { "javac.opt.Xbootclasspath.p", "Prepend to the bootstrap class path" }, { "javac.opt.Xdoclint", "Enable recommended checks for problems in javadoc comments" }, { "javac.opt.Xdoclint.custom", "\n        Enable or disable specific checks for problems in javadoc comments,\n        where <group> is one of accessibility, html, missing, reference, or syntax,\n        and <access> is one of public, protected, package, or private." }, { "javac.opt.Xdoclint.subopts", "(all|none|[-]<group>)[/<access>]" }, { "javac.opt.Xlint", "Enable recommended warnings" }, { "javac.opt.Xlint.suboptlist", "Enable or disable specific warnings" }, { "javac.opt.Xstdout", "Redirect standard output" }, { "javac.opt.arg.class", "<class>" }, { "javac.opt.arg.class.list", "<class1>[,<class2>,<class3>...]" }, { "javac.opt.arg.directory", "<directory>" }, { "javac.opt.arg.dirs", "<dirs>" }, { "javac.opt.arg.encoding", "<encoding>" }, { "javac.opt.arg.file", "<filename>" }, { "javac.opt.arg.flag", "<flag>" }, { "javac.opt.arg.key.equals.value", "key[=value]" }, { "javac.opt.arg.number", "<number>" }, { "javac.opt.arg.path", "<path>" }, { "javac.opt.arg.pathname", "<pathname>" }, { "javac.opt.arg.plugin", "\"name args\"" }, { "javac.opt.arg.profile", "<profile>" }, { "javac.opt.arg.release", "<release>" }, { "javac.opt.bootclasspath", "Override location of bootstrap class files" }, { "javac.opt.classpath", "Specify where to find user class files and annotation processors" }, { "javac.opt.d", "Specify where to place generated class files" }, { "javac.opt.deprecation", "Output source locations where deprecated APIs are used" }, { "javac.opt.diags", "Select a diagnostic mode" }, { "javac.opt.encoding", "Specify character encoding used by source files" }, { "javac.opt.endorseddirs", "Override location of endorsed standards path" }, { "javac.opt.extdirs", "Override location of installed extensions" }, { "javac.opt.g", "Generate all debugging info" }, { "javac.opt.g.lines.vars.source", "Generate only some debugging info" }, { "javac.opt.g.none", "Generate no debugging info" }, { "javac.opt.headerDest", "Specify where to place generated native header files" }, { "javac.opt.help", "Print a synopsis of standard options" }, { "javac.opt.implicit", "Specify whether or not to generate class files for implicitly referenced files" }, { "javac.opt.maxerrs", "Set the maximum number of errors to print" }, { "javac.opt.maxwarns", "Set the maximum number of warnings to print" }, { "javac.opt.moreinfo", "Print extended information for type variables" }, { "javac.opt.nogj", "Don't accept generics in the language" }, { "javac.opt.nowarn", "Generate no warnings" }, { "javac.opt.parameters", "Generate metadata for reflection on method parameters" }, { "javac.opt.pkginfo", "Specify handling of package-info files" }, { "javac.opt.plugin", "Name and optional arguments for a plug-in to be run" }, { "javac.opt.prefer", "Specify which file to read when both a source file and class file are found for an implicitly compiled class" }, { "javac.opt.print", "Print out a textual representation of specified types" }, { "javac.opt.printProcessorInfo", "Print information about which annotations a processor is asked to process" }, { "javac.opt.printRounds", "Print information about rounds of annotation processing" }, { "javac.opt.printflat", "Print abstract syntax tree after inner class conversion" }, { "javac.opt.printsearch", "Print information where classfiles are searched" }, { "javac.opt.proc.none.only", "Control whether annotation processing and/or compilation is done." }, { "javac.opt.processor", "Names of the annotation processors to run; bypasses default discovery process" }, { "javac.opt.processorpath", "Specify where to find annotation processors" }, { "javac.opt.profile", "Check that API used is available in the specified profile" }, { "javac.opt.prompt", "Stop after each error" }, { "javac.opt.retrofit", "Retrofit existing classfiles with generic types" }, { "javac.opt.s", "Emit java sources instead of classfiles" }, { "javac.opt.scramble", "Scramble private identifiers in bytecode" }, { "javac.opt.scrambleall", "Scramble package visible identifiers in bytecode" }, { "javac.opt.source", "Provide source compatibility with specified release" }, { "javac.opt.sourceDest", "Specify where to place generated source files" }, { "javac.opt.sourcepath", "Specify where to find input source files" }, { "javac.opt.target", "Generate class files for specific VM version" }, { "javac.opt.verbose", "Output messages about what the compiler is doing" }, { "javac.opt.version", "Version information" }, { "javac.version", "{0} {1}" }, { "javac.warn.profile.target.conflict", "profile {0} is not valid for target release {1}" }, { "javac.warn.source.target.conflict", "source release {0} requires target release {1}" }, { "javac.warn.target.default.source.conflict", "target release {0} conflicts with default source release {1}" } };
/*   */   }
/*   */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javac\resources\javac.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */