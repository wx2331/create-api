package com.sun.source.doctree;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import jdk.Exported;

@Exported
public interface ErroneousTree extends TextTree {
  Diagnostic<JavaFileObject> getDiagnostic();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\doctree\ErroneousTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */