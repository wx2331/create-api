package com.sun.tools.javap;

import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.Callable;
import javax.tools.DiagnosticListener;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.OptionChecker;
import javax.tools.StandardJavaFileManager;
import javax.tools.Tool;

public interface DisassemblerTool extends Tool, OptionChecker {
  DisassemblerTask getTask(Writer paramWriter, JavaFileManager paramJavaFileManager, DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Iterable<String> paramIterable1, Iterable<String> paramIterable2);
  
  StandardJavaFileManager getStandardFileManager(DiagnosticListener<? super JavaFileObject> paramDiagnosticListener, Locale paramLocale, Charset paramCharset);
  
  public static interface DisassemblerTask extends Callable<Boolean> {
    void setLocale(Locale param1Locale);
    
    Boolean call();
  }
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\tools\javap\DisassemblerTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */