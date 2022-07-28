package com.sun.source.util;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.Tree;
import jdk.Exported;

@Exported
public interface SourcePositions {
  long getStartPosition(CompilationUnitTree paramCompilationUnitTree, Tree paramTree);
  
  long getEndPosition(CompilationUnitTree paramCompilationUnitTree, Tree paramTree);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\sourc\\util\SourcePositions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */