// https://youtu.be/kQfCWkZKK8k
// https://youtu.be/aUWsT37YEd4

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;

public class QuestionPanel extends JPanel
{
	private CardLayout listOfCards;
	private PageManager pm;
	private GameData data;
	private BattlePanel battle;

	public QuestionPanel (PageManager pageMngr, BattlePanel bp)
	{
		pm = pageMngr;
		data = pm.getGameData();
		data.grabQuestionFromFile();
		battle = bp;
        battle.getQuestionPanel(this);

		setBackground(Color.BLACK);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		listOfCards = new CardLayout();
		setLayout(listOfCards);

		QuestionsPanel second = new QuestionsPanel(pm, listOfCards, this);
		add(second, "2");

		HighScoresPanel third = new HighScoresPanel(data, listOfCards, this);
		add(third, "3");
	}

	public void reloadQuestion()
	{
		data.grabQuestionFromFile();
		QuestionsPanel qp = new QuestionsPanel(pm, listOfCards, this);
		add(qp, "2");
		listOfCards.show(this, "2");
	}

	public class QuestionsPanel extends JPanel implements ActionListener
	{
		private CardLayout listOfCards;
		private QuestionPanel primaryPanel;
		private ButtonGroup group;
		private JTextArea questionArea;
		private JRadioButton [] answer;
		public PageManager pm;
		private boolean lastAnswerCorrect;
        private Timer autoAdvance;

		public QuestionsPanel(PageManager pageMngr, CardLayout c, QuestionPanel p)
		{
			pm = pageMngr;
			data = pm.getGameData();
			listOfCards = c;
			primaryPanel = p;

			setBackground(Color.BLACK);
			setLayout(new BorderLayout(10, 10));
			Font myFont = new Font("Tahoma", Font.BOLD, 22);

			answer = new JRadioButton[4];

			JPanel question = new JPanel();
			question.setBackground(Color.WHITE);
			question.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
			question.setLayout(new BorderLayout());
			add(question, BorderLayout.NORTH);

			questionArea = new JTextArea(data.getQuestion(), 3, 30);
			questionArea.setFont(myFont);
			questionArea.setLineWrap(true);
			questionArea.setWrapStyleWord(true);
			questionArea.setOpaque(false);
			questionArea.setEditable(false);

			question.add(questionArea, BorderLayout.CENTER);

			JPanel answers = new JPanel();
			answers.setBackground(Color.GRAY);
			answers.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
			answers.setLayout(new GridLayout(2, 2, 20, 20));
			add(answers, BorderLayout.CENTER);

			group = new ButtonGroup();

			for(int i = 0; i < answer.length; i++)
			{
				answer[i] = new JRadioButton("" + (char)(65 + i) + ". " + data.getAnswer(i));
				group.add(answer[i]);
				answer[i].setBackground(new Color(230,230,230));
				answer[i].setFont(myFont);
				answer[i].addActionListener(this);
				answers.add(answer[i]);
			}
		}

		public void actionPerformed(ActionEvent evt) //check if answer choice is correct -- called when a radio button is clicked
		{
			lastAnswerCorrect = false;
			int correctIdx = data.getCorrectAnswer();
			if (correctIdx >= 0 && correctIdx < answer.length)
				answer[correctIdx].setBackground(Color.GREEN);

			for (int i = 0; i < answer.length; i++)
			{
				if (answer[i].isSelected())
				{
					if (i != correctIdx)
					{
						answer[i].setBackground(Color.RED);
					}
					else
					{
						lastAnswerCorrect = true;
						data.addOneToCorrectCount();
					}
				}
				answer[i].setEnabled(false); //locks all other answer choices immediately
			}

			//Short pause so user sees green/red feedback, then moves on to battle panel
			autoAdvance = new Timer(1200, new AutoAdvanceListener());
			autoAdvance.setRepeats(false);
			autoAdvance.start();
		}

        class AutoAdvanceListener implements ActionListener
        {
                public void actionPerformed(ActionEvent e)
				{
					int questionNum = data.getCurrentQuestionNumber();
					resetQuestion();
					battle.resolveRound(lastAnswerCorrect);
					if (!lastAnswerCorrect)
					{
						pm.explanationPanel.loadExplanation(questionNum);
						pm.changePanelCard("Explanation");
					}
					else
					{
						pm.changePanelCard("Battle");
						battle.startTimer = true;
					}
				}
        }
		public void resetQuestion ( )
		{
            autoAdvance.stop();
			group.clearSelection();
			data.grabQuestionFromFile();
			questionArea.setText(data.getQuestion());
			answer[0].setText("A. " + data.getAnswer(0));
			answer[1].setText("B. " + data.getAnswer(1));
			answer[2].setText("C. " + data.getAnswer(2));
			answer[3].setText("D. " + data.getAnswer(3));
			for(int i = 0; i < answer.length; i++)
			{
				answer[i].setEnabled(true);
				answer[i].setBackground(new Color(230, 230, 230));
			}
		}
	}

	class HighScoresPanel extends JPanel implements ActionListener
	{
		private GameData data;
		private CardLayout listOfCards;
		private QuestionPanel primaryPanel;
		private JTextArea scoreInfo;
		private JTextArea highScoresArea;

		public HighScoresPanel(GameData d, CardLayout c, QuestionPanel p)
		{
			data = d;
			listOfCards = c;
			primaryPanel = p;

			Font myFont = new Font("Tahoma", Font.BOLD, 22);

			setLayout(new BorderLayout(20, 20));
			setBackground(Color.GRAY);
			setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
			setFont(myFont);

			JPanel centerPanel = new JPanel();
			centerPanel.setLayout(new GridLayout(1, 2, 10, 10));
			add(centerPanel, BorderLayout.CENTER);

			JPanel leftSidePanel = new JPanel();
			leftSidePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
			leftSidePanel.setLayout(new BorderLayout());
			centerPanel.add(leftSidePanel, BorderLayout.CENTER);

			scoreInfo = new JTextArea("" + data.getCorrectCount(), 10, 20);
			scoreInfo.setFont(myFont);
			scoreInfo.setLineWrap(true);
			scoreInfo.setWrapStyleWord(true);
			scoreInfo.setOpaque(false);
			scoreInfo.setEditable(false);
			leftSidePanel.add(scoreInfo);

			JPanel rightSidePanel = new JPanel();
			rightSidePanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
			rightSidePanel.setLayout(new BorderLayout());
			centerPanel.add(rightSidePanel, BorderLayout.CENTER);

			/*highScoresArea = new JTextArea("" + data.getHighScores(), 10, 20);
			highScoresArea.setFont(myFont);
			highScoresArea.setLineWrap(true);
			highScoresArea.setWrapStyleWord(true);
			highScoresArea.setOpaque(false);
			highScoresArea.setEditable(false);
			highScoresArea.setMargin(new Insets(10,10,10,10));
			JScrollPane scroller = new JScrollPane(highScoresArea);
			rightSidePanel.add(scroller);*/

			JPanel buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
			add(buttonPanel, BorderLayout.SOUTH);

			JButton playAgain = new JButton("PLAY AGAIN");
			playAgain.setFont(myFont);
			playAgain.addActionListener(this);
			buttonPanel.add(playAgain);

			JButton exit = new JButton("EXIT");
			exit.setFont(myFont);
			exit.addActionListener(this);
			buttonPanel.add(exit);
		}

		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			scoreInfo.setText("" + data.toString());
		}

		public void actionPerformed(ActionEvent evt)
		{
			String command = evt.getActionCommand();

			if(command.equals("PLAY AGAIN"))
			{
				data.saveToHighScores();
				listOfCards.previous(primaryPanel);
			}
			else if(command.equals("EXIT"))
			{
				data.saveToHighScores();
				System.exit(0);
			}
		}
	}
}