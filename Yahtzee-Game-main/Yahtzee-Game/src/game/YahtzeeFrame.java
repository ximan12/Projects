package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class YahtzeeFrame extends JFrame {
	
	private JPanel textfield;
	private JPanel Section;
	private JPanel dicePanel;
	private JPanel Upper;
	private JPanel Lower;
	private ArrayList<ImagePanel> Images;
	private Object[] DPobjects;
	private Object[] LowerObjects;
	private Object[] UpperObjects;
	private ArrayList<JCheckBox> CheckBoxes;
	private int[] Points;
	private int[] Points2;
	private Map<Integer, Integer> Counter;
	private ArrayList<JTextField> TextFieldList;
	private static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#");
	private  ArrayList<JTextField> TextFieldList2;
	private ArrayList<Boolean> LowerButtonSelected;
	private ArrayList<Boolean> UpperButtonSelected;
	private ArrayList<Boolean> LowerIfEmpty;
	private ArrayList<Boolean> UpperIfEmpty;
	private ArrayList<Integer> SubtotalScore;
	private ArrayList<Integer> SubtotalScore2;
	private ArrayList<JButton> IfRepeat;
	private int RollTimes;
	private int Turns;
	private int total = 0;
	private int Subtotal2 = 0;

	
	public YahtzeeFrame() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,1000);
		textfield = new JPanel();
		Section = new JPanel();
		dicePanel = new JPanel();
		
		this.add(textfield, BorderLayout.NORTH);
		this.add(Section, BorderLayout.CENTER);
		this.add(dicePanel, BorderLayout.EAST);
		
		createTextfield();
		createSection();
		createDicePanel();
		
		
		
	}
	
	public void createTextfield() {
		JLabel label = new JLabel("Player Name");
		JTextField text = new JTextField(10);
		textfield.add(label);
		textfield.add(text);
	}
	
	public void createSection() {
		
		Section.setLayout(new GridLayout(2, 1));
		
		Upper = new JPanel();
		createUpper();
	    Upper.setBorder(new TitledBorder(new EtchedBorder(), "Upper Section"));
		Lower = new JPanel();
		createLower();
	    Lower.setBorder(new TitledBorder(new EtchedBorder(), "Lower Section"));

	    IfRepeat = new ArrayList<>();

		Section.add(Upper);
		Section.add(Lower);
		
	}
	
	
	
	public void createDicePanel() {
		
		dicePanel.setLayout(new BoxLayout(dicePanel, BoxLayout.Y_AXIS));
		
		Images = new ArrayList<>();
		CheckBoxes = new ArrayList<>();
		ArrayList<JPanel> panels = new ArrayList<>();
		Points = new int[5];
		Points2 = new int[5];
		Counter = new HashMap<>();
		RollTimes = 0;
		Turns = 0;
		DPobjects = new Object[] {
				
				new JLabel("Turn : 0"),
				new JLabel("Roll : 0"),
				new JButton("Roll")
				
		};
		
		JButton Roll = (JButton) DPobjects[2];
		Roll.addActionListener(new RollListener());
		
		for (int i = 0; i < 5; i++) {
			
			Images.add(new ImagePanel("die" + (i + 1) + ".png"));
			Images.get(i).scaleImage(0.6);
			Points[i] = i + 1;
			Points2[i] = i + 1;
			CheckBoxes.add(new JCheckBox("Keep"));
			panels.add(new JPanel());
			panels.get(i).add(CheckBoxes.get(i));
			dicePanel.add(Images.get(i));
			
			dicePanel.add(panels.get(i));
		}
		
		for (Object object : DPobjects) {
			panels.add(new JPanel());
			panels.get(panels.size() - 1).add((Component) object);
			dicePanel.add(panels.get(panels.size() - 1));
		}
		
		
		
	}
	
	public void createUpper() {
		
		Upper.setLayout(new GridLayout(1, 2));
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		Upper.add(leftPanel);
		Upper.add(rightPanel);
		
		leftPanel.setLayout(new GridLayout(9, 1));
		rightPanel.setLayout(new GridLayout(9, 1));
		
		UpperButtonSelected = new ArrayList<>();
	    UpperIfEmpty = new ArrayList<>();
		SubtotalScore = new ArrayList<>();

		
		UpperObjects = new Object[] {
				new JButton("Aces"), 
				new JButton("Twos"), 
				new JButton("Threes"),
				new JButton("Fours"),
				new JButton("Fives"),
				new JButton("Sixs"),
				new JLabel("Score Subtotal"),
				new JLabel("Bonus"),
				new JLabel("Grand Total")
				};
		
		TextFieldList = new ArrayList<>();
		ArrayList<JPanel> rowPaneList = new ArrayList<>();
		
		for (int i = 0; i < 9; i++) {
			
			TextFieldList.add(new JTextField(10));
			TextFieldList.get(i).setEditable(false);
			rowPaneList.add(new JPanel());
		}
		
		int index = 0;
		for (Object object : UpperObjects) {
			
			
			JPanel rowPane = rowPaneList.get(index);
			
			if (index <= 5) {
				((AbstractButton) object).addActionListener(new UpperButtonListener());
				UpperButtonSelected.add(false);
				UpperIfEmpty.add(true);
				SubtotalScore.add(0);

			}
			rowPane.add((Component) object);
			rightPanel.add(TextFieldList.get(index++));
			leftPanel.add(rowPane);
			
		}
	}
	
	public void createLower() {
		
		Lower.setLayout(new GridLayout(1, 2));
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		Lower.add(leftPanel);
		Lower.add(rightPanel);
		
		leftPanel.setLayout(new GridLayout(10, 1));
		rightPanel.setLayout(new GridLayout(10, 1));
		
		SubtotalScore2 = new ArrayList<>();
		LowerButtonSelected = new ArrayList<>();
	    LowerIfEmpty = new ArrayList<>();

		
		LowerObjects = new Object[] {
				new JButton("3 of a kind"), 
				new JButton("4 of a kind"), 
				new JButton("Full House"),
				new JButton("Small Straight"),
				new JButton("Large Straight"),
				new JButton("Yahtzee"),
				new JButton("Chance"),
				new JLabel("Yahtzee Bonus"),
				new JLabel("Total of lower section"),
				new JLabel("Grand Total")
				};

		TextFieldList2 = new ArrayList<>();
		ArrayList<JPanel> rowPaneList = new ArrayList<>();
		
		for (int i = 0; i < 10; i++) {
			
			TextFieldList2.add(new JTextField(10));
			TextFieldList2.get(i).setEditable(false);
			rowPaneList.add(new JPanel());
		}
		
		int index = 0;
		for (Object object : LowerObjects) {
			
			JPanel rowPane = rowPaneList.get(index);
			if (index <= 6) {
				((AbstractButton) object).addActionListener(new LowerButtonListener());
				LowerButtonSelected.add(false);
				LowerIfEmpty.add(true);
				SubtotalScore2.add(0);

			}
			
			rowPane.add((Component) object);
			rightPanel.add(TextFieldList2.get(index++));
			leftPanel.add(rowPane);
			
		}
		
		
	}
	
	class UpperButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (Turns < 13 && (!IfRepeat.contains(e.getSource()))) {
				
				
				for (int i = 0; i <=5; i++) {
					
					if (e.getSource().equals(UpperObjects[i])) {
						
						UpperButtonSelected.set(i, true);
					}
					

				}
				
				RollTimes = 0;
				((JLabel) DPobjects[1]).setText("Roll : " + RollTimes);

				Turns++;
				((JLabel) DPobjects[0]).setText("Turn : " + Turns);
				
				IfRepeat.add((JButton) e.getSource());
				
				CheckBoxes.forEach(c -> {c.setSelected(false);});


			}
			
		}
		
	}
	
	class LowerButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (Turns < 13 && (!IfRepeat.contains(e.getSource()))) {
				
				RollTimes = 0;
				((JLabel) DPobjects[1]).setText("Roll : " + RollTimes);

				Turns++;
				((JLabel) DPobjects[0]).setText("Turn : " + Turns);
				
				CheckBoxes.forEach(c -> {c.setSelected(false);});
				
				IfRepeat.add((JButton) e.getSource());

				
				for (int i = 0; i <=6; i++) {
					

					if (e.getSource().equals(LowerObjects[i])) {
						
						LowerButtonSelected.set(i, true);
					}
				}
			}
			
		}
		
		
		
	}
	
	
	class RollListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			
			if (RollTimes < 3 && Turns < 13) {
				
				RollTimes ++;
				((JLabel) DPobjects[1]).setText("Roll : " + RollTimes);
				
				Counter.clear();
				
				int index = 0;
				for (ImagePanel die : Images) {
					
					if (!CheckBoxes.get(index).isSelected()) {
						
						int temp = (int)(Math.random() * 6) + 1;
						
						die.setImage("die" + temp + ".png");
						die.scaleImage(0.6);
						
						Points[index] = temp;
						
						Counter.put(temp, Counter.getOrDefault(temp, 0) + 1);
						
					} else {
						
						Counter.put(Points[index], 
								Counter.getOrDefault(Points[index], 0) + 1);
					}
					
					index++;	
					
				}
				
			
				Points2 = Points.clone();
				
				Arrays.sort(Points2);
				
				UpperSection();
				LowerSection();

				
			
			
			} else if (RollTimes == 3) {
				
				int UBS = 0;
				int UIE = 0;
				for (int i = 0; i <= 5; i++) {
					
					if (UpperButtonSelected.get(i) == false) {
						
						UBS++;
						
						if (UpperIfEmpty.get(i) == true) {
							
							UIE++;
						}
					}
				}
				
				int LBS = 0;
				int LIE = 0;
				for (int i = 0; i <= 6; i++) {
					
					if (LowerButtonSelected.get(i) == false) {
						
						UBS++;
						
						if (LowerIfEmpty.get(i) == true) {
							
							UIE++;
						}
					}
				}
				
				if (UBS == UIE && LBS == LIE) {
					
					RollTimes = 0;
					((JLabel) DPobjects[1]).setText("Roll : " + RollTimes);

					Turns++;
					((JLabel) DPobjects[0]).setText("Turn : " + Turns);
					
					CheckBoxes.forEach(c -> {c.setSelected(false);});


				}
				
				
				
				
			} else if (Turns == 13) {
				
				TextFieldList2.get(8).setText(DECIMAL_FORMAT.format(Subtotal2));
				TextFieldList2.get(9).setText(DECIMAL_FORMAT.format(total + Subtotal2));
				


			} 
		}
		
		public void UpperSection() {
			
			for (int i = 1; i <= 6; i++) {
				
				if (!UpperButtonSelected.get(i - 1)) {
					
					if (Counter.containsKey(i)) {
						
						int temp = Counter.get(i) * i;
						
						TextFieldList.get(i - 1).setText(DECIMAL_FORMAT.format(temp));
						
						UpperIfEmpty.set(i - 1, false);
						
						SubtotalScore.set(i - 1, temp);
					} else {
						
						TextFieldList.get(i - 1).setText("");
						UpperIfEmpty.set(i - 1, true);

					}
				}
			}
				
				int Subtotal = 0;
				for (int i = 0; i <= 5; i++) {
					
					if (UpperButtonSelected.get(i)) {
						
						Subtotal += SubtotalScore.get(i);
						
					}
					
				}
				
				if (Subtotal > 0)
					TextFieldList.get(6).setText(DECIMAL_FORMAT.format(Subtotal));
				if (Subtotal >= 63)
					TextFieldList.get(7).setText(DECIMAL_FORMAT.format(35));
				
				total = Subtotal;
				if (Subtotal >= 63)
					total += 35;
				if(!UpperButtonSelected.contains(false)) {
					TextFieldList.get(8).setText(DECIMAL_FORMAT.format(total));
				}

				
			
		}
		
		public void LowerSection() {
			
			int straightLine = 1;
			
			
			if (Counter.size() >= 4 && Points2[0] <= 3) {
				
				for (int i = 1; i < 5; i++) {
					
					if (Points2[i] - Points2[i - 1] == 0) {
						
						continue;
						
					} else if (Points2[i] - Points2[i - 1] == 1) {
						
						straightLine++;
						
						
					} else {
						if (straightLine < 4) {
						straightLine = 0;
						
					}
					
				}
			}
			}
			
			if (!LowerButtonSelected.get(3)) {
			if (straightLine == 4) {
				TextFieldList2.get(3).setText(DECIMAL_FORMAT.format(30));
				LowerIfEmpty.set(3, false);
				SubtotalScore2.set(3, 30);

			}
			if (straightLine < 4) {
				TextFieldList2.get(3).setText("");
				LowerIfEmpty.set(3, true);

			}

			}
			
			if (!LowerButtonSelected.get(4)) {
			if (straightLine == 5) {
				TextFieldList2.get(4).setText(DECIMAL_FORMAT.format(40));
				LowerIfEmpty.set(4, false);
				SubtotalScore2.set(4, 40);


			}

			if (straightLine < 5) {
				TextFieldList2.get(4).setText("");
				LowerIfEmpty.set(4, true);

			}
			}
			
			

			int sum = 0;
			
			for (int p : Points2) {
				
				sum += p;
			}
			
			if (!LowerButtonSelected.get(6)) {
				
				TextFieldList2.get(6).setText(DECIMAL_FORMAT.format(sum));
				SubtotalScore2.set(6, sum);

			}

			if (!LowerButtonSelected.get(1)) {
				
				if (Counter.containsValue(4)) {
					TextFieldList2.get(1).setText(DECIMAL_FORMAT.format(sum));
					LowerIfEmpty.set(1, false);
					SubtotalScore2.set(1, sum);


				}
				if (!Counter.containsValue(4)) {
					TextFieldList2.get(1).setText("");
					LowerIfEmpty.set(1, true);

				}
				
			}
			
			if (!LowerButtonSelected.get(0)) {	
				
				if (Counter.containsValue(3)) {
					TextFieldList2.get(0).setText(DECIMAL_FORMAT.format(sum));
					LowerIfEmpty.set(0, false);
					SubtotalScore2.set(0, sum);


				}
				if (!Counter.containsValue(3)) {
					TextFieldList2.get(0).setText("");
					LowerIfEmpty.set(0, true);

				}
				
			}
			
			if (!LowerButtonSelected.get(2)) {
				
			if (Counter.containsValue(2) && Counter.containsValue(3)) {
				
				TextFieldList2.get(2).setText(DECIMAL_FORMAT.format(25));
				LowerIfEmpty.set(2, false);
				SubtotalScore2.set(2, 25);



			} else {
				
				TextFieldList2.get(2).setText("");
				LowerIfEmpty.set(2, true);

			}
			
			}	
			
			if (!LowerButtonSelected.get(5)) {
			if (Counter.size() == 1) {
				
				TextFieldList2.get(5).setText(DECIMAL_FORMAT.format(50));
				LowerIfEmpty.set(5, false);
				SubtotalScore2.set(5, 50);



			} else {
					
					TextFieldList2.get(5).setText("");
					LowerIfEmpty.set(5, true);

			}	

			}
			
			if (LowerButtonSelected.get(5) && Counter.size() == 1) {
				
				TextFieldList2.get(7).setText(DECIMAL_FORMAT.format(100));
				SubtotalScore2.set(7, 100);


			}
			
			
			Subtotal2 = 0;
			for (int i = 0; i <= 6; i++) {
				
				if (LowerButtonSelected.get(i)) {
					
					Subtotal2 += SubtotalScore2.get(i);
					
				}
				
			}
			
			if (SubtotalScore2.size() == 8)
				Subtotal2 += 100;

		
		
	}
	}

	
	
	
	
	public static void main(String args[]) {
		YahtzeeFrame yahtzee = new YahtzeeFrame();
		yahtzee.setVisible(true);
	}
}
