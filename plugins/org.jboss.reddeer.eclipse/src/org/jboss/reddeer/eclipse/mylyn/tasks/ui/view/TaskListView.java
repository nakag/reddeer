package org.jboss.reddeer.eclipse.mylyn.tasks.ui.view;

import java.util.ArrayList;
import java.util.List;

import org.jboss.reddeer.eclipse.exception.EclipseLayerException;
import org.jboss.reddeer.swt.api.Tree;
import org.jboss.reddeer.swt.api.TreeItem;
import org.jboss.reddeer.swt.exception.SWTLayerException;
import org.jboss.reddeer.swt.impl.button.PushButton;
import org.jboss.reddeer.swt.impl.menu.ShellMenu;
import org.jboss.reddeer.swt.impl.tree.DefaultTree;
import org.jboss.reddeer.swt.impl.tree.DefaultTreeItem;
import org.jboss.reddeer.swt.wait.TimePeriod;
import org.jboss.reddeer.swt.wait.WaitUntil;
import org.jboss.reddeer.workbench.view.View;
import org.jboss.reddeer.swt.condition.TreeItemHasMinChildren;

/**
 * Represents the Task List view - to support Mylyn automated tests. 
 *  
 * @author ldimaggi
 *
 */
public class TaskListView extends View {
	
	public static final String TITLE = "Task List";
	
	public TaskListView() {
		super(TITLE);
	}

	public List<TaskList> getTaskLists(){
		List<TaskList> theTaskLists = new ArrayList<TaskList>();

		Tree tree;
		try {
			tree = new DefaultTree();
		} catch (SWTLayerException e){
			return new ArrayList<TaskList>();
		}
		for (TreeItem item : tree.getItems()){
			theTaskLists.add(new TaskList(item));
		}
		return theTaskLists;
	}

	public TaskList getTaskList(String name){
		for (TaskList repository : getTaskLists()){
			if (repository.getName().equals(name)){
				return repository;
			}
		}
		throw new EclipseLayerException("There is no repository with name " + name);
	}

	protected Tree getRepositoriesTree(){
		open();
		return new DefaultTree();
	}
	
	/* Method to locate and select a task in the task list view  */
	public TreeItem getTask (String taskCategory, String taskName) {
		new DefaultTree();
		
		DefaultTreeItem theCategory = new DefaultTreeItem (taskCategory);		
		new WaitUntil(new TreeItemHasMinChildren(theCategory, 1), TimePeriod.getCustom(60l)); 
		
		DefaultTreeItem theTask = new DefaultTreeItem (taskCategory, taskName);
		theTask.select();
		return theTask;
	}
	
	/* For use in the Task List View */
	public void createLocalTaskTest () {
				
		new ShellMenu("File", "New", "Other...").select();  
		new DefaultTree();
		DefaultTreeItem theNewTask = new DefaultTreeItem ("Tasks", "Task");
		theNewTask.select();	
		new PushButton("Next >").click();
		
		/* Specify that the new task will be created in the Local repo */
		new DefaultTree();
		DefaultTreeItem theLocalRepo = new DefaultTreeItem ("Local");
		theLocalRepo.select();	
		new PushButton("Finish").click();	
	}
	
}
