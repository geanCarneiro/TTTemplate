/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gean.tttemplate.view.telaPrincipal;

import com.gean.tttemplate.utils.TweetPresetNode;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 *
 * @author GeanCarneiro
 */
public class PresetsTreeModel extends DefaultTreeModel{
    
    public PresetsTreeModel(List<TweetPresetNode> presets){
        super(new DefaultMutableTreeNode());
        presets.forEach(preset -> ((MutableTreeNode)this.getRoot()).insert(preset.toTreeNode(), 0));
    }
    
    
    
}
