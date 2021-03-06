package gameClient;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import Server.game_service;
import utils.StdDraw;


public class Management {
	private MyGameGUI mgg;
	private game_service game;

	public Management() {
		
		mgg = new MyGameGUI();
		game = mgg.game;
		
		ManagementGame();
	}
	
	/**
	 * responsible of the player option, to play manual or automaticly
	 */
	public void ManagementGame() {
		int w = manualOrAuto(); //0 for manual, 1 for auto
		if(w == 0) {
			//mgg.InstructionForManual();
		}else if(w == -1) {
			return;
		}
		int id = pickID();
		StdDraw.id = id;
		
		int topLvl = SimpleDB.getTopLevel(id);
		
		int s = -1;
		while(s == -1) {
			s = pickScenario();
			if(s == -1)
				JOptionPane.showMessageDialog(null, "choose a valid scenario");
			else if (s == -2) {
				System.out.println("Exiting game");
				System.exit(-2);
			}else if(topLvl < s) {
				s = -1;
				JOptionPane.showMessageDialog(null, "You need to pass level " + topLvl + " first");
			}
		}
		if(w == 0) {
			manual(s);
			
		}else {
			auto(s, id);
		}
	}
	
	/**
	 * run manual game
	 * @param s
	 */
	private void manual(int s) {
		game = mgg.gameManualScenario(s);
		mgg.initFruits(game);
		mgg.initRobots(game);
		mgg.runManualScenario(game);
	}
	
	/**
	 * run auto game
	 * @param s
	 */
	private void auto(int s, int id) {
		new Automat(s,game,mgg, id);
	}
	
	/**
	 * jframe of the option on the screen
	 * @return
	 */
	private int manualOrAuto() {
		Object[] options = {"Manual",
		"Auto"};
		int n = JOptionPane.showOptionDialog(null,
				"How would you like to play?",
				"Choose way of game",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,     //do not use a custom Icon
				options,  //the titles of buttons
				options[0]); //default button title
		return n;
	}
	
	/**
	 * let the user pick scenario
	 * @return
	 */
	private int pickScenario() {
		JTextField SPDestField = new JTextField(5);
		JPanel SPEdgePanel = new JPanel();

		SPEdgePanel.add(new JLabel("scenario:"));
		SPEdgePanel.add(SPDestField);

		int SPEdgeRes = JOptionPane.showConfirmDialog(null, SPEdgePanel, 
				"Pick scenario (0 - 23)", JOptionPane.OK_CANCEL_OPTION);
		if (SPEdgeRes == JOptionPane.OK_OPTION) {
			try {

				int sce = Integer.parseInt(SPDestField.getText());
				if((sce <= 23 && sce >= 0) || sce == -1 || sce == -31) {
					return sce;
				}
				else return -1;

			}catch(Exception err) {
				return -1;
			}
		}
		return -2;	 //error happened or choose to cancel the game
	}
	
	private int pickID() {
		JTextField SPDestField = new JTextField(5);
		JPanel SPEdgePanel = new JPanel();

		SPEdgePanel.add(new JLabel("ID:"));
		SPEdgePanel.add(SPDestField);

		int SPEdgeRes = JOptionPane.showConfirmDialog(null, SPEdgePanel, 
				"What is your ID?", JOptionPane.OK_CANCEL_OPTION);
		if (SPEdgeRes == JOptionPane.OK_OPTION) {
			try {

				int id = Integer.parseInt(SPDestField.getText());
				if(SimpleDB.isContainsID(id)) {
					return id;
				}
				else return -1;

			}catch(Exception err) {
				return -1;
			}
		}
		return -2;	 //error happened or choose to cancel the game
	}

	
}
