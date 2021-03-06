package org.jboss.reddeer.swt.impl.button;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.jboss.reddeer.swt.matcher.RegexMatcher;
import org.jboss.reddeer.swt.matcher.StyleMatcher;
import org.jboss.reddeer.swt.reference.ReferencedComposite;

/**
 * Parent for all all prescribed button implementations like OK, Cancel, Next, etc.
 * @author Jiri Peterka
 *
 */
public abstract class PredefinedButton extends AbstractButton {

	protected PredefinedButton(ReferencedComposite refComposite, int index,
			String text, int style) {
		
		super(refComposite, index, null, SWT.PUSH,new RegexMatcher("(?i)" + text),new StyleMatcher(style));
		
		
		if (!getText().equals(text)) {
			throw new SWTException(text + " button is found but case mishmash detected");
		}
	}

}
