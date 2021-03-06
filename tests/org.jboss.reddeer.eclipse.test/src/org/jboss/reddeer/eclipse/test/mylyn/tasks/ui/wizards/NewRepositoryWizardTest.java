package org.jboss.reddeer.eclipse.test.mylyn.tasks.ui.wizards;

import org.jboss.reddeer.eclipse.mylyn.tasks.ui.wizards.NewRepositoryWizard;
import org.jboss.reddeer.swt.test.RedDeerTest;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * 
 * @author ldimaggi
 * 
 */
public class NewRepositoryWizardTest extends RedDeerTest {

	@Test
	public void getWizardTest() {
		
		NewRepositoryWizard theWizard = new NewRepositoryWizard();
		theWizard.open();
		assertTrue ("the index is 1", new Integer (theWizard.getPageIndex()).equals(1));
		theWizard.cancel();
	
	}
}
