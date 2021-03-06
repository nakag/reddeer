package org.jboss.reddeer.swt.impl.menu;

import org.jboss.reddeer.junit.logging.Logger;
import org.hamcrest.Matcher;
import org.jboss.reddeer.swt.api.Menu;

/**
 * Abstract class for all Menu implementations
 * 
 * @author Jiri Peterka
 * 
 */
public abstract class AbstractMenu implements Menu {

	protected final Logger log = Logger.getLogger(this.getClass());

	protected String[] path;
	protected Matcher<String>[] matchers;

	@Override
	public abstract void select();
	
	@Override
	public String getText() {
		throw new UnsupportedOperationException("not yet implemented");
	}
}
