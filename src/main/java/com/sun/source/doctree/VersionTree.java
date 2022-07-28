package com.sun.source.doctree;

import java.util.List;
import jdk.Exported;

@Exported
public interface VersionTree extends BlockTagTree {
  List<? extends DocTree> getBody();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\doctree\VersionTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */