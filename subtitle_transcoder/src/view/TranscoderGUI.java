package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import control.SubtitleTranscoding;

public class TranscoderGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8782570688602668568L;

	private JButton select = new JButton("SELECT");
	private JButton toSAMI = new JButton("TO SAMI FILE");;
	private JButton toJSON = new JButton("TO JSON FILE");
	private JButton clear = new JButton("CLEAR");
	private JFileChooser fileChooser = new JFileChooser(".");
	private JPanel selectState = new JPanel();
	private TextArea processLabel = new TextArea();
	private SubtitleTranscoding st;
	public TranscoderGUI (){
		st= new SubtitleTranscoding();
		this.setTitle("Subtitle Transcoder");
		this.setLayout(new GridLayout(1,2));
		this.setLocation(300,300);
		setChooser();
		addActions();
		selectState.setLayout(new GridLayout(9,1));
		selectState.add(new JLabel("Select A Subtitle XML File"));
		selectState.add(select);
		selectState.add(toSAMI);
		selectState.add(toJSON);
		selectState.add(clear);
		this.add(selectState);
		processLabel.setEditable(false);
		processLabel.setForeground(Color.WHITE);
		processLabel.setBackground(Color.black);
		processLabel.setColumns(10);
		processLabel.setSize(450, 600);
		this.add(processLabel);
		this.setSize(900, 600);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public void addActions(){
		select.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(e.getSource() == select)
			{
					int returnal = fileChooser.showOpenDialog(TranscoderGUI.this);
					if(returnal == fileChooser.APPROVE_OPTION){
						String filePath = fileChooser.getSelectedFile().getAbsolutePath();
						log(st.parseSubtitleFile(filePath));
					}
			}
				
				
			}});
		 toSAMI.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(e.getSource() == toSAMI){
				     st.toSMAI();
				}
			}
			 
		 });
		 
		 toJSON.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					if(e.getSource() == toJSON){
					     st.toJSON();
					}
				}
				 
			 });
		 
		 clear.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				processLabel.setText("");
			}});
	}
	private void setChooser(){
		FileFilter txtFilter = new FileFilter() {

			@Override
			public boolean accept(File arg0) {
				// TODO Auto-generated method stub
				return arg0.getName().toLowerCase().endsWith(".xml")
						|| arg0.isDirectory();
			}

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "XML Files";
			}

		};
		fileChooser.setFileFilter(txtFilter);
	}
	public void log(String state){
		processLabel.setText(processLabel.getText()+"\n\r"+state);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new TranscoderGUI();
	}

}
