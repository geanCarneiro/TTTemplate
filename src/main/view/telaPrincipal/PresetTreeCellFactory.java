package main.view.telaPrincipal;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;
import main.utils.TweetPreset;
import main.view.createTweet.CreateTweetFactory;

public class PresetTreeCellFactory
		implements Callback<TreeView<TweetPreset>, TreeCell<TweetPreset>> {

	@Override
	public TreeCell<TweetPreset> call(TreeView<TweetPreset> param) {
		// TODO Auto-generated method stub
		return new DoubleClickCellImpl();
	}

}

class DoubleClickCellImpl extends TreeCell<TweetPreset> {
	
    @Override
    protected void updateItem(TweetPreset item, boolean empty) {
        super.updateItem(item, empty);

        if(empty) {
			setText(null);
		} else if(item == null) {
			setText("[NAN]");
		} else {
			setText(item.getNome());
		}
    }

    public DoubleClickCellImpl() {
        super();

        setOnMouseClicked(event -> {
            TreeItem<TweetPreset> ti = getTreeItem();
            if (ti == null || !ti.isLeaf() || event.getClickCount() < 2)
                return;

			CreateTweetFactory.createView(getItem());
        });
    }
}
