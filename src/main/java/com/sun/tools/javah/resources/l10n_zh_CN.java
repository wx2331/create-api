/*   */ package com.sun.tools.javah.resources;
/*   */ 
/*   */ public final class l10n_zh_CN extends ListResourceBundle {
/*   */   protected final Object[][] getContents() {
/* 5 */     return new Object[][] { { "at.args.cant.read", "无法从文件{1}中读取命令行参数。" }, { "at.args.file.not.found", "找不到文件{0}。" }, { "at.args.io.exception", "处理命令行中的 @ 参数时, 遇到以下 I/O 问题: {0}。" }, { "cant.create.dir", "无法为输出创建目录 {0}。" }, { "class.not.found", "找不到类{0}。" }, { "dir.file.mixed", "不能混用选项 -d 和 -o。请尝试使用 -help。" }, { "encoding.iso8859_1.not.found", "找不到用于输出的 ISO8859_1 转换器。这可能是因为安装过程中出现了错误。" }, { "err.cant.use.option.for.fm", "不能将{0}选项与给定的文件管理器一起使用" }, { "err.internal.error", "内部错误: {0}" }, { "err.ioerror", "IO 错误: {0}" }, { "err.missing.arg", "{0}缺少值" }, { "err.no.classes.specified", "未指定类" }, { "err.prefix", "错误:" }, { "err.unknown.option", "未知选项: {0}" }, { "invalid.method.signature", "无效的方法签名: {0}" }, { "io.exception", "无法从 I/O 错误中恢复, 消息为: {0}。" }, { "javah.fullVersion", "{0}完整版本 \"{1}\"" }, { "javah.version", "{0}版本 \"{1}\"" }, { "jni.llni.mixed", "不能混用选项 -jni 和 -llni。请尝试使用 -help。" }, { "jni.no.stubs", "JNI 不需要存根, 请参阅 JNI 文档。" }, { "jni.sigerror", "无法确定{0}的签名" }, { "jni.unknown.type", "遇到未知类型 (JNI)。" }, { "main.opt.bootclasspath", "  -bootclasspath <path>    从中加载引导类的路径" }, { "main.opt.classpath", "  -classpath <path>        从中加载类的路径" }, { "main.opt.cp", "  -cp <path>               从中加载类的路径" }, { "main.opt.d", "  -d <dir>                 输出目录" }, { "main.opt.force", "  -force                   始终写入输出文件" }, { "main.opt.h", "  -h  --help  -?           输出此消息" }, { "main.opt.jni", "  -jni                     生成 JNI 样式的标头文件 (默认值)" }, { "main.opt.o", "  -o <file>                输出文件 (只能使用 -d 或 -o 之一)" }, { "main.opt.v", "  -v  -verbose             启用详细输出" }, { "main.opt.version", "  -version                 输出版本信息" }, { "main.usage", "用法: \n  javah [options] <classes>\n其中, [options] 包括:" }, { "main.usage.foot", "<classes> 是使用其全限定名称指定的\n(例如, java.lang.Object)。" }, { "no.bootclasspath.specified", "未在命令行中指定任何引导类路径。请尝试使用 -help。" }, { "no.classes.specified", "未在命令行中指定任何类。请尝试使用 -help。" }, { "no.classpath.specified", "未在命令行中指定任何类路径。请尝试使用 -help。" }, { "no.outputdir.specified", "未在命令行中指定任何输出目录。请尝试使用 -help。" }, { "no.outputfile.specified", "未在命令行中指定任何输出文件。请尝试使用 -help。" }, { "old.jni.mixed", "不能混用选项 -jni 和 -old。请尝试使用 -help。" }, { "old.llni.mixed", "不能混用选项 -old 和 -llni。请尝试使用 -help。" }, { "old.not.supported", "此版本的 javah 不支持选项 -old。" }, { "super.class.not.found", "找不到所需的超类{0}。" }, { "tracing.not.supported", "警告: 不再支持跟踪。请使用虚拟机的 -verbose:jni 选项。" }, { "tried.to.define.non.static", "尝试为非静态字段生成 #define。" }, { "unknown.array.type", "生成旧样式的标头时遇到未知的数组类型。" }, { "unknown.option", "{0}是非法参数\n" }, { "unknown.type.for.field", "生成旧样式的标头时遇到未知的类型。" }, { "unknown.type.in.method.signature", "生成旧样式的存根时遇到未知的类型。" }, { "usage", "用法: javah [options] <classes>\n\n其中, [options] 包括:\n\n\t-help                 输出此帮助消息并退出\n\t-classpath <path>     从中加载类的路径\n\t-cp <path>            从中加载类的路径\n\t-bootclasspath <path> 从中加载引导类的路径\n\t-d <dir>              输出目录\n\t-o <file>             输出文件 (只能使用 -d 或 -o 之一)\n\t-jni                  生成 JNI 样式的标头文件 (默认值)\n\t-version              输出版本信息\n\t-verbose              启用详细输出\n\t-force                始终写入输出文件\n\n<classes> 是使用其全限定名称指定的,\n(例如, java.lang.Object)。\n" } };
/*   */   }
/*   */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javah\resources\l10n_zh_CN.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */