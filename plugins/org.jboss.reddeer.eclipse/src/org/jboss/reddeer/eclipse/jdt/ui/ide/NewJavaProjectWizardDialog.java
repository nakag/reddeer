package org.jboss.reddeer.eclipse.jdt.ui.ide;

import org.jboss.reddeer.eclipse.jface.wizard.NewWizardDialog;
import org.jboss.reddeer.swt.condition.JobIsRunning;
import org.jboss.reddeer.swt.condition.ShellWithTextIsActive;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.exception.WaitTimeoutExpiredException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.shell.DefaultShell;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.swt.wait.WaitWhile;

public class NewJavaProjectWizardDialog extends NewWizardDialog{
	
	public NewJavaProjectWizardDialog() {
		super("Java", "Java Project");
	}
	
	@Override
	public NewJavaProjectWizardPage getFirstPage() {
		return new NewJavaProjectWizardPage(this);
	}
	
	@Override
	public void finish(){
		finish(false);
	}
	@SuppressWarnings("unused")
	public void finish(boolean openAssociatedPerspective) {
		log.debug("Finish wizard dialog");
		new PushButton("Finish").click();
		final String openAssociatedPerspectiveShellText = "Open Associated Perspective?";
		try {
			new WaitUntil(new ShellWithTextIsActive(openAssociatedPerspectiveShellText),
				TimePeriod.getCustom(20),false);
			// Try to find open perspective test
			DefaultShell shell = new DefaultShell(openAssociatedPerspectiveShellText);
			if (openAssociatedPerspective) {
				new PushButton("Yes").click();
			} else {
				new PushButton("No").click();
			}
			new WaitWhile(new ShellWithTextIsActive(openAssociatedPerspectiveShellText),
				TimePeriod.LONG);
		} catch (WaitTimeoutExpiredException wtee) {
			log.info("Shell 'Open Associated Perspective' wasn't shown");
		} catch (SWTLayerException sle) {
			log.info("Shell 'Open Associated Perspective' wasn't shown");
		}
		new WaitWhile(new ShellWithTextIsActive("New Java Project"));
		new WaitWhile(new JobIsRunning(), TimePeriod.LONG);
	}

}
