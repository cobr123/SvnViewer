import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;

public class Engine implements ActionListener {
	final MainWindow parent;
	final DisplayRepositoryTree svnRep = new DisplayRepositoryTree(
			"http://svn.svnkit.com/repos/svnkit/trunk/", "anonymous",
			"anonymous");

	public Engine(final MainWindow window) {
		this.parent = window;
	}
	private HashSet<String> getFileNames(){
		DefaultTableModel modelForSave = (DefaultTableModel) parent.tblSaveFiles
				.getModel();
		int cnt = modelForSave.getRowCount();
		HashSet<String> set = new HashSet<String>();
		for (int i = 0; i < cnt; ++i) {
			if (!set.contains((String) modelForSave.getValueAt(i, 0))) {
				set.add((String) modelForSave.getValueAt(i, 0));
			}
		}
		return set;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// Get the source of this action
		if (e.getSource() instanceof JButton) {
			JButton clickedButton = (JButton) e.getSource();
			if (clickedButton == parent.btnRefresh) {
				parent.btnRefresh.setEnabled(false);
				(new FileListRefresher()).execute();
			} else if (clickedButton == parent.btnSave) {
				int cnt = parent.tblSaveFiles.getModel().getRowCount();
				if (cnt > 0) {
					String dir = chooseDir();
					//System.out.println(dir);
					HashSet<String> set = getFileNames();
					if(dir != null){
						svnRep.SaveFilesTo(dir, set);
					}
				}
			} else if (clickedButton == parent.btnInsert) {
				int rows[] = parent.tblFiles.getSelectedRows();
				for (int i = rows.length - 1; i >= 0; --i) {
					if (rows[i] >= 0) {
						DefaultTableModel model = (DefaultTableModel) parent.tblFiles
								.getModel();
						String rowVal = (String) model.getValueAt(rows[i], 0);
						model.removeRow(rows[i]);

						DefaultTableModel modelForSave = (DefaultTableModel) parent.tblSaveFiles
								.getModel();
						modelForSave.insertRow(0, new Object[] { rowVal });
					}
				}
			}
		}
	}

	private String chooseDir() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("choosertitle");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			// System.out.println("getCurrentDirectory(): " +
			// chooser.getCurrentDirectory());
			// System.out.println("getSelectedFile() : " +
			// chooser.getSelectedFile());
			return chooser.getSelectedFile().getAbsolutePath();
		} else {
			return null;
			// System.out.println("No Selection ");
		}
	}

	class FileListRefresher extends SwingWorker<Object, Object> {
		private Collection entries;

		@Override
		public String doInBackground() {
			// System.out.println("doInBackground");
			try {
				entries = svnRep.GetEntries();
			} catch (SVNException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void done() {
			try {
				parent.btnRefresh.setEnabled(true);
				DefaultTableModel model = new DefaultTableModel();
				Iterator iterator = entries.iterator();
				model.addColumn("File name");

				DefaultTableModel modelForSave = (DefaultTableModel) parent.tblSaveFiles
						.getModel();
				int cnt = modelForSave.getRowCount();
				HashSet<String> set = new HashSet<String>();
				for (int i = 0; i < cnt; ++i) {
					if (!set.contains((String) modelForSave.getValueAt(i, 0))) {
						set.add((String) modelForSave.getValueAt(i, 0));
					}
				}

				while (iterator.hasNext()) {
					SVNDirEntry entry = (SVNDirEntry) iterator.next();
					// System.out.println(entry.getName());
					if (!set.contains(entry.getName())) {
						model.insertRow(0, new Object[] { entry.getName() });
					}
				}
				parent.tblFiles.setModel(model);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// System.out.println("done");
		}
	}
}
