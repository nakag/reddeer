package org.jboss.reddeer.swt.impl.shell;

import org.eclipse.swt.SWT;
import org.jboss.reddeer.junit.logging.Logger;
import org.jboss.reddeer.swt.api.Shell;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.condition.ShellWithTextIsAvailable;
import org.jboss.reddeer.swt.handler.ShellHandler;
import org.jboss.reddeer.swt.handler.WidgetHandler;
import org.jboss.reddeer.swt.lookup.WidgetLookup;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;

/**
 * Abstract class for all Shells
 * 
 * @author Jiri Peterka
 * 
 */
public abstract class AbstractShell implements Shell {

	protected final Logger log = Logger.getLogger(this.getClass());

	protected org.eclipse.swt.widgets.Shell swtShell;

	@Override
	public String getText() {
		String text = WidgetHandler.getInstance().getText(swtShell);
		return text;
	}

	@Override
	public void setFocus() {
		String text = getText();
		log.info("Setting focus to Shell " + text);
		WidgetHandler.getInstance().setFocus(swtShell);
		new WaitUntil(new ShellWithTextIsActive(text));
	}

	@Override
	public void close() {
		String text = getText();
		log.info("Closing shell " + text);
		WidgetLookup.getInstance().notify(SWT.Close, swtShell);
		ShellHandler.getInstance().closeShell(swtShell);
		new WaitWhile(new ShellWithTextIsAvailable(text));
	}

}
