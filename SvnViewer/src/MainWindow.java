import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

public class MainWindow {
	private JTextField fldSvnUrl;
	final JButton btnRefresh;
	final JButton btnInsert;
	final JButton btnSave;
	private JPanel p1, p3;
	JTable tblFiles;
	JTable tblSaveFiles;

	public MainWindow() {
		String[] petStrings = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };
		Engine engine = new Engine(this);

		JPanel pnlChooseSvnFiles = new JPanel();

		// Set the layout manager for this panel
		BorderLayout bl = new BorderLayout();
		pnlChooseSvnFiles.setLayout(bl);

		fldSvnUrl = new JTextField("http://svn.svnkit.com/repos/svnkit/trunk/");
		fldSvnUrl.setHorizontalAlignment(JTextField.LEFT);

		btnRefresh = new JButton("Обновить");
		btnRefresh.addActionListener(engine);

		p3 = new JPanel();
		BoxLayout gl = new BoxLayout(p3, BoxLayout.X_AXIS);
		p3.setLayout(gl);

		p3.add(fldSvnUrl);
		p3.add(btnRefresh);

		p1 = new JPanel();
		BoxLayout gl1 = new BoxLayout(p1, BoxLayout.Y_AXIS);
		p1.setLayout(gl1);

		p1.add(p3);

		JComboBox petList = new JComboBox(petStrings);
		p1.add(petList);

		pnlChooseSvnFiles.add(p1, BorderLayout.NORTH);

		// tblFiles = new JTable(data, columnNames);
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("File name");
		tblFiles = new JTable(model);
		tblFiles.setAutoCreateRowSorter(true);
		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(tblFiles);
		// Add the scroll pane to this panel.
		pnlChooseSvnFiles.add(scrollPane, BorderLayout.CENTER);

		JPanel p3 = new JPanel();
		BoxLayout gl3 = new BoxLayout(p3, BoxLayout.X_AXIS);
		p3.setLayout(gl3);
		pnlChooseSvnFiles.add(p3, BorderLayout.SOUTH);

		btnInsert = new JButton("Выбрать");
		btnInsert.addActionListener(engine);
		p3.add(btnInsert);
		
		JPanel pnlSaveFiles = new JPanel();
		BoxLayout gl2 = new BoxLayout(pnlSaveFiles, BoxLayout.Y_AXIS);
		pnlSaveFiles.setLayout(gl2);
		DefaultTableModel model2 = new DefaultTableModel();
		model2.addColumn("File name");
		tblSaveFiles = new JTable(model2);
		tblSaveFiles.setAutoCreateRowSorter(true);
		JScrollPane scrollPane2 = new JScrollPane(tblSaveFiles);
		pnlSaveFiles.add(scrollPane2);

		btnSave = new JButton("Сохранить файлы");
		btnSave.addActionListener(engine);
		pnlSaveFiles.add(btnSave);
		
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Выбор файлов", pnlChooseSvnFiles);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        tabbedPane.addTab("Сохранение", pnlSaveFiles);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
        
		// Create the frame and set its content pane
		JFrame frame = new JFrame("Svn Viewer");
		frame.setContentPane(tabbedPane);
		// Set the size of the window to be big enough to accommodate all
		// controls
		frame.pack();

		// Display the window
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		MainWindow window = new MainWindow();
	}
}
