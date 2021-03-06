package org.jboss.reddeer.swt.impl.tree;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jboss.reddeer.junit.logging.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.condition.TreeItemHasMinChildren;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.tree.internal.BasicTree;
import org.jboss.reddeer.swt.impl.tree.internal.BasicTreeItem;
import org.jboss.reddeer.swt.util.Display;
import org.jboss.reddeer.swt.util.OS;
import org.jboss.reddeer.swt.util.ResultRunnable;
import org.jboss.reddeer.swt.util.Utils;
import org.jboss.reddeer.swt.wait.AbstractWait;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;

/**
 * Basic TreeItem class is abstract class for all Tree Item implementations
 * 
 * @author jjankovi
 * 
 */
public abstract class AbstractTreeItem implements TreeItem {

	protected final Logger logger = Logger.getLogger(this.getClass());

	protected org.eclipse.swt.widgets.TreeItem swtTreeItem;

	protected AbstractTreeItem(org.eclipse.swt.widgets.TreeItem swtTreeItem) {
		if (swtTreeItem != null) {
			this.swtTreeItem = swtTreeItem;
		} else {
			throw new SWTLayerException(
					"SWT Tree Item passed to constructor is null");
		}

	}

	/**
	 * See {@link TreeItem}
	 */
	@Override
	public void select() {
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				logger.debug("Selecting tree item: " + swtTreeItem.getText());
				swtTreeItem.getParent().setFocus();
				swtTreeItem.getParent().setSelection(swtTreeItem);
			}
		});
		logger.debug("Notify tree item " + getText() + " about selection");
		notifyTree(createEventForTree(SWT.Selection));
		logger.info("Selected: " + this);
	}

	/**
	 * See {@link TreeItem}
	 */
	@Override
	public String getText() {
		return Display.syncExec(new ResultRunnable<String>() {
			@Override
			public String run() {
				return swtTreeItem.getText();
			}
		});
	}

	/**
	 * See {@link TreeItem}
	 */
	@Override
	public String getToolTipText() {
		return Display.syncExec(new ResultRunnable<String>() {
			@Override
			public String run() {
				return swtTreeItem.getParent().getToolTipText();
			}
		});
	}

	/**
	 * See {@link TreeItem}
	 */
	@Override
	public String getCell(final int index) {
		return Display.syncExec(new ResultRunnable<String>() {
			@Override
			public String run() {
				return swtTreeItem.getText(index);
			}
		});
	}

	/**
	 * See {@link TreeItem}
	 */
	@Override
	public String[] getPath() {
		return Display.syncExec(new ResultRunnable<String[]>() {
			@Override
			public String[] run() {
				org.eclipse.swt.widgets.TreeItem swttiDummy = swtTreeItem;
				LinkedList<String> items = new LinkedList<String>();
				while (swttiDummy != null) {
					items.addFirst(swttiDummy.getText());
					swttiDummy = swttiDummy.getParentItem();
				}
				return items.toArray(new String[0]);
			}
		});
	}

	/**
	 * See {@link TreeItem}
	 */
	@Override
	public void expand() {
		expand(TimePeriod.SHORT);
	}
	/**
	 * See {@link TreeItem}
	 */
	@Override
	public void expand(TimePeriod timePeriod) {
		logger.debug("Expanding Tree Item " + getText());
		if (!isExpanded()) {
			if (!isExpanded()) {
				if (!Utils.isRunningOS(OS.WINDOWS)){
					notifyTree(createEventForTree(SWT.Expand));
				}
				Display.syncExec(new Runnable() {
					@Override
					public void run() {
						swtTreeItem.setExpanded(true);
					}
				});
				if (Utils.isRunningOS(OS.WINDOWS)){
					notifyTree(createEventForTree(SWT.Expand));
				}
				AbstractWait.sleep(timePeriod.getSeconds()*1000);
				logger.info("Expanded: " + this);
			} else {
				logger.debug("Tree Item " + getText()
						+ " is already expanded. No action performed");
			}
		}
	}
	/**
	 * See {@link TreeItem}
	 */
	@Override
	public void collapse() {
		logger.debug("Collapsing Tree Item " + getText());
		if (isExpanded()) {
			Display.syncExec(new Runnable() {
				@Override
				public void run() {
					logger.debug("Setting tree item " + swtTreeItem.getText() + " collapsed");
					swtTreeItem.setExpanded(false);
				}
			});
			logger.debug("Notify tree about collapse event");
			notifyTree(createEventForTree(SWT.Collapse));
		} else {
			logger.debug("Tree Item " + getText()
					+ " is already collapsed. No action performed");
		}
		logger.info("Collapsed: " + this);
	}

	/**
	 * Return direct descendant specified with parameters
	 * 
	 * @param text
	 *            text of tree item which should be returned
	 * @return direct descendant specified with parameters
	 */
	public TreeItem getItem(final String text) {
		logger.debug("Getting child tree item " + text + " of tree item " + getText());
		expand();
		TreeItem result = Display.syncExec(new ResultRunnable<TreeItem>() {
			@Override
			public TreeItem run() {
				org.eclipse.swt.widgets.TreeItem[] items = swtTreeItem
						.getItems();
				boolean isFound = false;
				int index = 0;
				while (!isFound && index < items.length) {
					if (items[index].getText().equals(text)) {
						isFound = true;
					} else {
						index++;
					}
				}
				if (!isFound) {
					return null;
				} else {
					return new BasicTreeItem(items[index]);
				}
			}
		});
		if (result != null) {
			return result;
		} else {
			SWTLayerException exception = new SWTLayerException("Tree Item " + this 
				+ " has no Tree Item with text " + text);
			exception.addMessageDetail("Tree Item " + this + " has these direct children:");
			for (TreeItem treeItem : this.getItems()){
				exception.addMessageDetail("  " + treeItem.getText());
			}
			throw exception;
		}
	}

	/**
	 * See {@link TreeItem}
	 */
	@Override
	public void doubleClick() {
		logger.debug("Double Click Tree Item " + getText());
		select();
		logger.debug("Notify tree about mouse double click event");
		notifyTree(createEventForTree(SWT.MouseDoubleClick));
		logger.debug("Notify tree about default selection event");
		notifyTree(createEventForTree(SWT.DefaultSelection));
		logger.info("Double Clicked on: " + this);
	}

	/**
	 * See {@link TreeItem}
	 */
	@Override
	public boolean isSelected() {
		return Display.syncExec(new ResultRunnable<Boolean>() {
			@Override
			public Boolean run() {
				return Arrays.asList(swtTreeItem.getParent().getSelection())
						.contains(swtTreeItem);
			}
		});
	}

	/**
	 * See {@link TreeItem}
	 */
	@Override
	public boolean isDisposed() {
		return swtTreeItem.isDisposed();
	}

	/**
	 * See {@link TreeItem}
	 */
	@Override
	public void setChecked(final boolean check) {
		logger.debug((check ? "Check" : "Uncheck") + "Tree Item " + getText()
				+ ":");
		Display.syncExec(new Runnable() {
			@Override
			public void run() {
				swtTreeItem.setChecked(check);
			}
		});
		logger.debug("Notify tree about check event");
		notifyTree(createEventForTree(SWT.Selection, SWT.CHECK));
		logger.info((check ? "Checked: " : "Unchecked: ") + this);
	}

	/**
	 * See {@link TreeItem}
	 */
	@Override
	public boolean isChecked() {
		return Display.syncExec(new ResultRunnable<Boolean>() {
			@Override
			public Boolean run() {
				return swtTreeItem.getChecked();
			}
		});
	}

	/**
	 * Return swt widget of Tree Item
	 */
	public org.eclipse.swt.widgets.TreeItem getSWTWidget() {
		return swtTreeItem;
	}

	/**
	 * Returns children tree items
	 * 
	 * @return
	 */
	@Override
	public List<TreeItem> getItems() {
		expand(TimePeriod.SHORT);
		return Display.syncExec(new ResultRunnable<List<TreeItem>>() {
			@Override
			public List<TreeItem> run() {
				LinkedList<TreeItem> result = new LinkedList<TreeItem>();
				org.eclipse.swt.widgets.TreeItem[] items = swtTreeItem
						.getItems();
				for (org.eclipse.swt.widgets.TreeItem swtTreeItem : items) {
					result.addLast(new BasicTreeItem(swtTreeItem));
				}
				return result;
			}
		});
	}

	/**
	 * See {@link TreeItem}
	 */
	@Override
	public Tree getParent() {
		return Display.syncExec(new ResultRunnable<Tree>() {
			@Override
			public Tree run() {
				return new BasicTree(swtTreeItem.getParent());
			}
		});
	}

	/**
	 * Notifies tree listeners about event event.type field has too be properly
	 * set
	 * 
	 * @param event
	 */
	private void notifyTree(final Event event) {
		Display.syncExec(new Runnable() {
			public void run() {
				swtTreeItem.getParent().notifyListeners(event.type, event);
			}
		});
	}

	/**
	 * Creates event for tree with specified type and empty detail
	 * 
	 * @param type
	 * @return
	 */
	private Event createEventForTree(int type) {
		return createEventForTree(type, SWT.NONE);
	}

	/**
	 * Creates event for tree with specified type and detail
	 * 
	 * @param type
	 * @return
	 */
	private Event createEventForTree(int type, int detail) {
		Event event = new Event();
		event.type = type;
		event.display = Display.getDisplay();
		event.time = (int) System.currentTimeMillis();
		event.item = swtTreeItem;
		event.widget = this.getParent().getSWTWidget();
		event.detail = detail;
		return event;
	}

	/**
	 * See {@link TreeItem}
	 */
	@Override
	public boolean isExpanded() {
		return Display.syncExec(new ResultRunnable<Boolean>() {
			@Override
			public Boolean run() {
				return swtTreeItem.getExpanded();
			}
		});
	}
	/**
	 * See {@link TreeItem}
	 */
	@Override
	public void expand(int minItemsCount) {
		expand(minItemsCount, TimePeriod.SHORT);
	}
	/**
	 * See {@link TreeItem}
	 */
	@Override
	public void expand(int minItemsCount , TimePeriod timePeriod) {
		expand();
		new WaitUntil(new TreeItemHasMinChildren(this, minItemsCount), timePeriod);
	}
	@Override
	public String toString(){
		StringBuffer result = new StringBuffer("TreeItem: ");
		boolean isFirst = true;
		for (String pathItem : this.getPath()){
			if (isFirst){
				isFirst = false;
			} else{
				result.append(" > ");	
			}
			result.append(pathItem);
			
		}
		return result.toString();
	}
}