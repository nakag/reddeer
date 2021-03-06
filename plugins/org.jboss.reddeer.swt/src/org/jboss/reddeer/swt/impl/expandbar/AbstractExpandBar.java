package org.jboss.reddeer.swt.impl.expandbar;

import java.util.List;

import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.api.ExpandBar;
import org.jboss.reddeer.swt.api.ExpandBarItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.handler.ExpandBarHandler;
import org.jboss.reddeer.swt.handler.WidgetHandler;
/**
 * Abstract class for all Expand Bar implementations
 * 
 * @author Vlado Pakan
 * 
 */
public class AbstractExpandBar implements ExpandBar {

	protected final Logger logger = Logger.getLogger(this.getClass());

	protected org.eclipse.swt.widgets.ExpandBar swtExpandBar;

	protected AbstractExpandBar(final org.eclipse.swt.widgets.ExpandBar swtExpandBar) {
		if (swtExpandBar != null) {
			this.swtExpandBar = swtExpandBar;
		} else {
			throw new SWTLayerException(
					"SWT Expand Bar passed to constructor is null");
		}
	}
	/**
	 * See {@link ExpandBar}
	 */
	@Override
	public int getItemsCount() {
		return ExpandBarHandler.getItemsCount(this);
	}
	/**
	 * See {@link ExpandBar}
	 */
	@Override
	public List<ExpandBarItem> getItems() {
		return ExpandBarHandler.getItems(this);
	}
	/**
	 * See {@link ExpandBar}
	 */
	@Override
	public void setFocus() {
		WidgetHandler.getInstance().setFocus(swtExpandBar);

	}
	/**
	 * See {@link ExpandBar}
	 */
	@Override
	public void expandAll() {
		ExpandBarHandler.expandAll(this);
	}
	/**
	 * See {@link ExpandBar}
	 */
	@Override
	public void collapseAll() {
		ExpandBarHandler.collapseAll(this);
	}
	/**
	 * See {@link ExpandBar}
	 */
	@Override
	public org.eclipse.swt.widgets.ExpandBar getSWTWidget() {
		return swtExpandBar;
	}
}
