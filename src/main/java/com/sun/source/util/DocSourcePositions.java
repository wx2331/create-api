package com.sun.source.util;

import com.sun.source.doctree.DocCommentTree;
import com.sun.source.doctree.DocTree;
import com.sun.source.tree.CompilationUnitTree;
import jdk.Exported;

@Exported
public interface DocSourcePositions extends SourcePositions {
  long getStartPosition(CompilationUnitTree paramCompilationUnitTree, DocCommentTree paramDocCommentTree, DocTree paramDocTree);
  
  long getEndPosition(CompilationUnitTree paramCompilationUnitTree, DocCommentTree paramDocCommentTree, DocTree paramDocTree);
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\sourc\\util\DocSourcePositions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */