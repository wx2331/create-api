package com.sun.source.doctree;

import java.util.List;
import jdk.Exported;

@Exported
public interface UnknownInlineTagTree extends InlineTagTree {
  List<? extends DocTree> getContent();
}


/* Location:              C:\Program Files\Java\jdk1.8.0_211\lib\tools.jar!\com\sun\source\doctree\UnknownInlineTagTree.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */