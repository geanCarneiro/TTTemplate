/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gean.tttemplate.view.telaPrincipal;

import com.gean.tttemplate.utils.TweetPreset;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author GeanCarneiro
 */
public class PresetsTreeCellRenderer extends DefaultTreeCellRenderer{
    
    public PresetsTreeCellRenderer(){
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        TweetPreset tweetPreset = (TweetPreset) ((DefaultMutableTreeNode)value).getUserObject();
        label.setText(tweetPreset == null ? "" : tweetPreset.getNome());
        return label;
    }
    
}
