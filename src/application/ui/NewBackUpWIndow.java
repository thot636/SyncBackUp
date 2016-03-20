package application.ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.CheckBoxTreeItem.TreeModificationEvent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author thot
 */
public class NewBackUpWIndow {

	private final Node rootIcon = new ImageView(new Image(getClass().getResourceAsStream("computer.png")));
	private final Node folderIcon = new ImageView(new Image(getClass().getResourceAsStream("folder.png")));
	private final Node fileIcon = new ImageView(new Image(getClass().getResourceAsStream("file.png")));
	private final Node HardDrive = new ImageView(new Image(getClass().getResourceAsStream("HardDrive.png")));
	

	public void start() {

		Stage stage = new Stage();
		stage.setTitle("Perform new back-up");
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setPadding(new Insets(25, 25, 25, 25));
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(300);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(700);
		grid.getColumnConstraints().addAll(col1, col2);
		Scene scene = new Scene(grid, 1000, 800);
		stage.setScene(scene);
		// if u wanna get Dektop location
		// String temp = System.getProperty("user.home") + "\\Desktop";
		// List<Path> listOfDrivers = detectDrives();
		//
		// CheckBoxTreeItem<Path> rootItem = new
		// CheckBoxTreeItem<Path>(Paths.get("My Computer"), rootIcon);
		//
		// rootItem.setExpanded(true);
		// for (Path path : listOfDrivers) {
		// CheckBoxTreeItem<Path> item = new CheckBoxTreeItem<Path>(path);
		// rootItem.getChildren().add(item);
		// }
		// TreeView<Path> tree = new TreeView<>(rootItem);
		TreeView<Path> tree = buildFileSystemBrowser();

		tree.setCellFactory((TreeView<Path> p) -> new TextFieldTreeCellImpl());

		tree.setMinHeight(700);
		tree.setMaxHeight(700);
		StackPane root = new StackPane();

		root.getChildren().add(tree);
		root.setAlignment(Pos.TOP_CENTER);

		grid.add(root, 0, 0);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(SyncBackUp.primaryStage);
		stage.show();

	}
	// nie pamietam poco ta metode stworzylem xD
	
//	public static List<String> fileList(String directory) {
//		List<String> fileNames = new ArrayList<>();
//		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directory))) {
//			for (Path path : directoryStream) {
//				fileNames.add(path.toString());
//				System.out.println(path.toString());
//			}
//		} catch (IOException ex) {
//		}
//		return fileNames;
//	}

	public static List<Path> detectDrives() {

		List<File> temp = new ArrayList<File>(Arrays.asList(File.listRoots()));
		List<Path> list = new ArrayList<>();
		for (File file : temp) {
			list.add(file.toPath());
		}
		return list;
	}

	private TreeView buildFileSystemBrowser() {
		CheckBoxTreeItem<Path> rootItem = new CheckBoxTreeItem<Path>(Paths.get("My Computer"));
		rootItem.setGraphic(rootIcon);
		rootItem.addEventHandler(CheckBoxTreeItem.<Path> checkBoxSelectionChangedEvent(),
				new EventHandler<TreeModificationEvent<Path>>() {
					@Override
					public void handle(TreeModificationEvent<Path> event) {
						CheckBoxTreeItem checkBoxTreeItem = event.getTreeItem();

						if (checkBoxTreeItem.isSelected()) {
							ObservableList<CheckBoxTreeItem<Path>> children = FXCollections.observableArrayList();
							children = checkBoxTreeItem.getChildren();
							for (CheckBoxTreeItem<Path> item : children) {
								item.setSelected(true);
							}
						}
						if (!checkBoxTreeItem.isSelected()) {
							ObservableList<CheckBoxTreeItem<Path>> children = FXCollections.observableArrayList();
							children = checkBoxTreeItem.getChildren();
							for (CheckBoxTreeItem item : children) {
								item.setSelected(false);
							}
						}
					}
				});

		rootItem.setExpanded(true);
		List<Path> listOfDrivers = detectDrives();
		Path path = listOfDrivers.get(0);
		CheckBoxTreeItem<Path> item = createNode(path);
		item.setGraphic(HardDrive);
		rootItem.getChildren().add(item);
//		for (Path path : listOfDrivers) {
//			CheckBoxTreeItem<Path> item = createNode(path);
//			item.setGraphic(HardDrive);
//			//item.setIndependent(true);
//			rootItem.getChildren().add(item);
//		}

		return new TreeView<Path>(rootItem);
	}

	private CheckBoxTreeItem<Path> createNode(final Path f) {
		return new CheckBoxTreeItem<Path>(f) {

			private boolean isLeaf;

			private boolean isFirstTimeChildren = true;
			private boolean isFirstTimeLeaf = true;

			@Override
			public ObservableList<TreeItem<Path>> getChildren() {
				if (isFirstTimeChildren) {
					isFirstTimeChildren = false;

					super.getChildren().setAll(buildChildren(this));
				}
				return super.getChildren();
			}

			@Override
			public boolean isLeaf() {
				if (isFirstTimeLeaf) {
					isFirstTimeLeaf = false;
					Path f = (Path) getValue();
					isLeaf = Files.isRegularFile(f);
				}

				return isLeaf;
			}

			private ObservableList<CheckBoxTreeItem<Path>> buildChildren(CheckBoxTreeItem<Path> CheckBoxTreeItem) {
				Path f = CheckBoxTreeItem.getValue();
				if (f != null && Files.isDirectory(f)) {

					List<Path> fileNames = new ArrayList<>();
					try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(f)) {
						for (Path path : directoryStream) {
							fileNames.add(path);
						}
					} catch (IOException ex) {
					}

					if (fileNames != null) {
						ObservableList<CheckBoxTreeItem<Path>> children = FXCollections.observableArrayList();

						for (Path childFile : fileNames) {
							CheckBoxTreeItem<Path> checkBoxTreeItem;
							checkBoxTreeItem = createNode(childFile);

							checkBoxTreeItem.setIndependent(true);

							children.add(checkBoxTreeItem);

						}

						return children;
					}
				}

				return FXCollections.emptyObservableList();
			}
		};
	}

	private final class TextFieldTreeCellImpl extends CheckBoxTreeCell<Path> {
		int i;

		@Override
		public void updateItem(Path item, boolean empty) {
			super.updateItem(item, empty);
			setText(getString());
			if (getTreeItem() != null) {

			}

		}

		private String getString() {
			if (getItem() == null) {
				return "";
			} else if (getItem() instanceof Path && getItem().toString().length() > 3) {

				return getItem().getFileName().toString();

			} else {
				return getItem().toString();
			}
		}

	}

}
/*
 * how to clear TreeView (delete all items to re-build new ones (refresh
 * obsList.removeAll(obsList) // obsList -> ObservableList
 */
