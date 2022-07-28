package com.sun.source.doctree;

import java.util.List;
import jdk.Exported;

@Exported
public interface DocCommentTree extends DocTree {
  List<? extends DocTree> getFirstSentence();
  
  List<? extends DocTree> getBody();
  
  List<? extends DocTree> getBlockTags();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\doctree\DocCommentTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */