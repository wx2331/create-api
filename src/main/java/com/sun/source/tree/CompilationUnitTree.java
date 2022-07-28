package com.sun.source.tree;

import java.util.List;
import javax.tools.JavaFileObject;
import jdk.Exported;

@Exported
public interface CompilationUnitTree extends Tree {
  List<? extends AnnotationTree> getPackageAnnotations();
  
  ExpressionTree getPackageName();
  
  List<? extends ImportTree> getImports();
  
  List<? extends Tree> getTypeDecls();
  
  JavaFileObject getSourceFile();
  
  LineMap getLineMap();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\tree\CompilationUnitTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */