package com.svo.library.widget.file_choose.atree;

public interface TreeListener {
    void onTreeNodesChanged(TreeEvent e);
    void onTreeNodesInserted(TreeEvent e);
    void onTreeNodesRemoved(TreeEvent e);
    void onTreeStructureChanged(TreeEvent e);
}
