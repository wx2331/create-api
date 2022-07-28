/*   */ package com.sun.tools.jdeps.resources;
/*   */ 
/*   */ public final class jdeps_zh_CN extends ListResourceBundle {
/*   */   protected final Object[][] getContents() {
/* 5 */     return new Object[][] { { "artifact.not.found", "找不到" }, { "err.invalid.arg.for.option", "选项的参数无效: {0}" }, { "err.invalid.path", "无效路径: {0}" }, { "err.missing.arg", "没有为{0}指定值" }, { "err.option.after.class", "必须在类之前指定选项: {0}" }, { "err.option.unsupported", "不支持{0}: {1}" }, { "err.profiles.msg", "没有配置文件信息" }, { "err.unknown.option", "未知选项: {0}" }, { "error.prefix", "错误:" }, { "jdeps.wiki.url", "https://wiki.openjdk.java.net/display/JDK8/Java+Dependency+Analysis+Tool" }, { "main.opt.P", "  -P           -profile              显示配置文件或包含程序包的文件" }, { "main.opt.R", "  -R           -recursive            递归遍历所有被依赖对象。\n                                     -R 选项表示 -filter:none。如果指定了 -p, -e, -f\n                                     选项, 则只分析匹配的\n                                     被依赖对象。" }, { "main.opt.apionly", "  -apionly                           通过公共类 (包括字段类型, 方法参数\n                                     类型, 返回类型, 受控异常错误类型\n                                     等) 的公共和受保护成员的签名\n                                     限制对 API (即被依赖对象)\n                                     进行分析" }, { "main.opt.cp", "  -cp <path>   -classpath <path>     指定查找类文件的位置" }, { "main.opt.depth", "  -depth=<depth>                     指定过渡被依赖对象分析\n                                     的深度" }, { "main.opt.dotoutput", "  -dotoutput <dir>                   DOT 文件输出的目标目录" }, { "main.opt.e", "  -e <regex>   -regex <regex>        查找与指定模式匹配的被依赖对象\n                                     (-p 和 -e 互相排斥)" }, { "main.opt.f", "  -f <regex>   -filter <regex>       筛选与指定模式匹配的被依赖对象\n                                     如果多次指定, 则将使用最后一个被依赖对象。\n  -filter:package                    筛选位于同一程序包内的被依赖对象 (默认)\n  -filter:archive                    筛选位于同一档案内的被依赖对象\n  -filter:none                       不使用 -filter:package 和 -filter:archive 筛选\n                                     通过 -filter 选项指定的筛选仍旧适用。" }, { "main.opt.h", "  -h -?        -help                 输出此用法消息" }, { "main.opt.include", "  -include <regex>                   将分析限制为与模式匹配的类\n                                     此选项筛选要分析的类的列表。\n                                     它可以与向被依赖对象应用模式的\n                                     -p 和 -e 结合使用" }, { "main.opt.jdkinternals", "  -jdkinternals                      在 JDK 内部 API 上查找类级别的被依赖对象。\n                                     默认情况下, 它分析 -classpath 上的所有类\n                                     和输入文件, 除非指定了 -include 选项。\n                                     此选项不能与 -p, -e 和 -s 选项一起使用。\n                                     警告: 在下一个发行版中可能无法访问\n                                     JDK 内部 API。" }, { "main.opt.p", "  -p <pkgname> -package <pkgname>    查找与给定程序包名称匹配的被依赖对象\n                                     (可多次指定)" }, { "main.opt.s", "  -s           -summary              仅输出被依赖对象概要" }, { "main.opt.v", "  -v           -verbose              输出所有类级别被依赖对象\n                                     等同于 -verbose:class -filter:none。\n  -verbose:package                   默认情况下输出程序包级别被依赖对象, \n                                     不包括同一程序包中的被依赖对象\n  -verbose:class                     默认情况下输出类级别被依赖对象, \n                                     不包括同一程序包中的被依赖对象" }, { "main.opt.version", "  -version                           版本信息" }, { "main.usage", "用法: {0} <options> <classes...>\n其中 <classes> 可以是 .class 文件, 目录, JAR 文件的路径名,\n也可以是全限定类名。可能的选项包括:" }, { "main.usage.summary", "用法: {0} <options> <classes...>\n使用 -h, -? 或 -help 列出可能的选项" }, { "warn.invalid.arg", "类名无效或路径名不存在: {0}" }, { "warn.mrjar.usejdk9", "{0} 是多发行版 jar 文件。\n已分析所有版本化条目。要分析某个特定版本的条目,\n请使用更新版本的 jdeps (JDK 9 或更高版本) \"--multi-release\" 选项。" }, { "warn.prefix", "警告:" }, { "warn.replace.useJDKInternals", "不支持 JDK 内部 API, 它们专用于通过不兼容方式来删除\n或更改的 JDK 实现, 可能会损坏您的应用程序。\n请修改您的代码, 消除与任何 JDK 内部 API 的相关性。\n有关 JDK 内部 API 替换的最新更新, 请查看:\n{0}" }, { "warn.split.package", "已在{1} {2}中定义程序包{0}" } };
/*   */   }
/*   */ }


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\jdeps\resources\jdeps_zh_CN.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */