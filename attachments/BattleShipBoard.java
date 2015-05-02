import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Stack;

import javax.swing.*;


public class BattleShipBoard extends JFrame {
	private Player player = new Player();
	private Player enemy = new Player();
	private BattleButton[][] myBoard = new BattleButton[11][11];
	private BattleButton[][] enemyBoard = new BattleButton[11][11];
	private Stack<Ship> myFleet = new Stack<>();
	private Stack<Ship> enemyFleet = new Stack<>();
	private int click = 0;
	private int[] lastButton = new int[2];
	private BattleButton center;
	private BattleButton next;
	private boolean hasCenter;
	private int direction;
	private JTextArea text = new JTextArea();

	

	public BattleShipBoard(String title) throws HeadlessException {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		hasCenter = false;
		direction = 1;
		makeFrame();
		setShips();
		introduction();
	//	setMyTestFleet();
	}

	private void introduction() {
		text.append("Welcome to Battleships!\n");
		text.append("Please place your pieces by first clicking the location\n");
		text.append("you want your piece to start at then selecting the location you want to end at.\n");
		text.append("Place your Patrol Boat with length 2.\n");
		text.append("Then place your Destroyer with length 3.\n");
		text.append("Then place your Submarine with length 3.\n");
		text.append("Then place your Battleship with length 4.\n");
		text.append("Finally place your Aircraft Carrier with length 5.\n");
	}

	private void makeFrame() {
		JPanel panel = new JPanel(new GridLayout(1, 2));
		JLabel label1 = new JLabel("Enemy Board");
		JLabel label2 = new JLabel("Your Board");
		align(label1);
		align(label2);
		label1.setFont(new Font(label1.getFont().getFontName(), Font.BOLD, 20));
		label2.setFont(new Font(label2.getFont().getFontName(), Font.BOLD, 20));
		panel.add(label1);
		panel.add(label2);
		add(panel, BorderLayout.NORTH);
		makeBoard();
		
		text.setLineWrap(true);
		text.setEditable(false);
		text.setVisible(true);
		
		JScrollPane scroll = new JScrollPane(text);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setPreferredSize(new Dimension(100, 100));
		
		add(scroll, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}

	private void makeBoard() {
		GridLayout layout = new GridLayout(11, 11);
		JPanel eBoard = new JPanel(layout);
		JPanel mBoard = new JPanel(layout);
		eBoard.setPreferredSize(new Dimension(440, 440));
		mBoard.setPreferredSize(new Dimension(440, 440));
		char[] x = {' ', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
		eBoard.add(new JLabel());
		mBoard.add(new JLabel());
		for (int i = 1; i < layout.getColumns(); i++) {
			JLabel label1 = new JLabel(Integer.toString(i));
			JLabel label2 = new JLabel(Integer.toString(i));
			align(label1);
			align(label2);
			eBoard.add(label1);
			mBoard.add(label2);
		}
		for (int i = 1; i < layout.getRows(); i++) {
			JLabel label1 = new JLabel(Character.toString(x[i]));
			JLabel label2 = new JLabel(Character.toString(x[i]));
			align(label1);
			align(label2);
			eBoard.add(label1);
			mBoard.add(label2);
			for (int j = 1; j < layout.getColumns(); j++) {
				BattleButton btn1 = new BattleButton(i, j);
				BattleButton btn2 = new BattleButton(i, j);
				btn1.setBackground(Color.BLUE);
				btn2.setBackground(Color.BLUE);
				btn1.setIsEnemy(true);
				btn2.setIsEnemy(false);
				btn1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (!((BattleButton) e.getSource()).getIsHit()) {
							if (!player.isDefeated() && !enemy.isDefeated() && myFleet.isEmpty()) {
								play((BattleButton) e.getSource(), false);
								if (!enemy.isDefeated()) {
									if (hasCenter && !center.getShip().isDestroyed()) {
										strike();
									} else {
										if (search()) {
											strike();
										} else {
											attack();
										}
									}
								}
								text.setCaretPosition(text.getDocument().getLength());
							}
						} else {
							if (!player.isDefeated() && !enemy.isDefeated()) {
								text.append("That location has already been hit.\n");
							}
						}
					}
				});
				btn2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						placeFleet((BattleButton) e.getSource(), true, myFleet, myBoard);
					}
				});
				enemyBoard[i][j] = btn1;
				myBoard[i][j] = btn2;
				eBoard.add(btn1);
				mBoard.add(btn2);
			}
		}
		this.add(eBoard, BorderLayout.WEST);
		this.add(mBoard, BorderLayout.EAST);
	}
	
	protected boolean search() {
		for (int i = 1; i < 11; i++) {
			for (int j = 1; j < 11; j++) {
				if (myBoard[i][j].getIsHit() && myBoard[i][j].getHasShip()) {
					if (!myBoard[i][j].getShip().isDestroyed()) {
						this.center = myBoard[i][j];
						this.next = center;
						this.hasCenter = true;
						this.direction = 2;
						return true;
					}
				}
			}
		}
		return false;
	}

	protected void strike() {
		if (direction > 4) {
			direction -= 4;
		}
		switch (direction) {
		case 1:	if (next.getThisX() > 1) {
					doNext(next.getThisX() - 1, next.getThisY());
					break;
				} else {
					if (!center.getShip().isDestroyed()) {
						direction += 2;
						next = center;
						hasCenter = true;
					} else {
						direction++;
					}
					strike();
					break;
				} 
		case 2:	if (next.getThisY() < 10) {
					doNext(next.getThisX(), next.getThisY() + 1);
					break;
				} else {
					if (!center.getShip().isDestroyed()) {
						next = center;
						hasCenter = true;
						direction += 2;
					} else {
						direction++;
					}
					strike();
					break;
				}
		case 3: if (next.getThisX() < 10) {
					doNext(next.getThisX() + 1, next.getThisY());
					break;
				}  else {
					if (!center.getShip().isDestroyed()) {
						next = center;
						hasCenter = true;
						direction += 2;
					} else {
						direction++;
					}
					strike();
					break;
				}
		case 4: if (next.getThisY() > 1) {
					doNext(next.getThisX(), next.getThisY() - 1);
					break;
				}  else {
					if (!center.getShip().isDestroyed()) {
						next = center;
						hasCenter = true;
						direction += 2;
					} else {
						direction++;
					}
					strike();
					break;
				}
		
		}
	}
	
	private void doNext(int x, int y) {
		this.next = myBoard[x][y];
		if (next.getIsHit()) {
			this.next = center;
			this.direction++;
			strike();
		} else {
			play(next, false);
			if (!next.getHasShip() || (isEdge(next) && !canContinue())) {
				next = center;
				hasCenter = true;
				direction += 2;
			}
		}
	}
	
	private boolean canContinue() {
		if (isEdge(next)) {
			int nx = next.getThisX();
			int ny = next.getThisY();
			int cx = center.getThisX();
			int cy = center.getThisY();
			if (ny == cy) {
				return nx != 10 || nx != 1;
			} else if (nx == cx) {
				return ny != 10 || ny != 1;
			}
		}
		return false;
	}

	private boolean isEdge(BattleButton btn) {
		int x = btn.getThisX();
		int y = btn.getThisY();
		return x == 1 || y == 1 || x == 10 || y == 10;
	}

	private void attack() {
		Random r = new Random();
		int x, y;
		if (r.nextBoolean()) {
			x = r.nextInt(5) * 2 + 1;
			y = r.nextInt(5) * 2 + 1;
		} else {
			x = r.nextInt(5) * 2 + 2;
			y = r.nextInt(5) * 2 + 2;
		}
		if (!myBoard[x][y].getIsHit()) {
			play(myBoard[x][y], true);
		} else {
			attack();
		}
	}
	
	private void play(BattleButton btn, boolean toggle) {
		if (btn.getHasShip()) {
			btn.setBackground(Color.RED);
			btn.setIsHit(true);
			btn.getShip().damage(btn.getPlace());
			if (toggle) {
				this.center = btn;
				this.next = center;
				this.hasCenter = true;
			}
			if (btn.getShip().isDestroyed()) {
				text.append(btn.getShip().toString(btn.getIsEnemy()) + "\n");
			} else {
				text.append(btn.toString() + "\n");
			}
			if (player.isDefeated()) {
				text.append("You lost.\n");
			} else if (enemy.isDefeated()){
				text.append("YOU WON!\n");
			}
		} else {
			btn.setBackground(Color.WHITE);
			btn.setIsHit(true);
			if (toggle) {
				this.hasCenter = false;
			}
			text.append(btn.toString() + "\n");
		}
	}
	
	
	private void align(JLabel label) {
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
	}
	
	private void setShips() {
		createFleet(player.getFleet(), enemy.getFleet());
		Random r = new Random();
		while(!enemyFleet.isEmpty()) {
			int x = r.nextInt(10) + 1;
			int y = r.nextInt(10) + 1;
			int z = enemyFleet.peek().length() - 1;
			if (r.nextBoolean()) {
				if (y + z > 10) {
					placeFleet(enemyBoard[x][y], false, enemyFleet, enemyBoard);
					placeFleet(enemyBoard[x][y - z], false, enemyFleet, enemyBoard);
				} else if (y + z > 0){
					placeFleet(enemyBoard[x][y], false, enemyFleet, enemyBoard);
					placeFleet(enemyBoard[x][y + z], false, enemyFleet, enemyBoard);
				}
			} else {
				if (x + z > 10) {
					placeFleet(enemyBoard[x][y], false, enemyFleet, enemyBoard);
					placeFleet(enemyBoard[x - z][y], false, enemyFleet, enemyBoard);
				} else if (x + z > 0) {
					placeFleet(enemyBoard[x][y], false, enemyFleet, enemyBoard);
					placeFleet(enemyBoard[x + z][y], false, enemyFleet, enemyBoard);
				}
			}
		}
	}
	
	private void setMyTestFleet() {
		myBoard[1][1].doClick();
		myBoard[1][2].doClick();
		myBoard[2][1].doClick();
		myBoard[2][3].doClick();
		myBoard[3][1].doClick();
		myBoard[3][3].doClick();
		myBoard[4][1].doClick();
		myBoard[4][4].doClick();
		myBoard[5][1].doClick();
		myBoard[5][5].doClick();
	}
	
	public void createFleet(Stack<Ship> myFleet, Stack<Ship> enemyFleet) {
		this.myFleet = myFleet;
		this.enemyFleet = enemyFleet;
	}

	public void placeFleet(BattleButton start, boolean showShips, Stack<Ship> fleet, BattleButton[][] board) {
		if (!fleet.isEmpty()) {
			click++;
			if (click % 2 == 0) {
				Ship ship = fleet.pop();
				BattleButton end = board[lastButton[0]][lastButton[1]];
				if (!start.equals(end)) {
					if (start.getThisX() == end.getThisX() && start.getThisY() != end.getThisY()) {
						int s = Math.min(start.getThisY(), end.getThisY());
						int b = Math.max(start.getThisY(), end.getThisY());
						if (ship.length() == b - s + 1) {
							checkPath(start.getThisX(), s, b, true, start, end, ship, fleet, board, showShips);
						} else {
							end.setBackground(Color.BLUE);
							fleet.push(ship);
						}
					} else if (start.getThisX() != end.getThisX() && start.getThisY() == end.getThisY()) {
						int s = Math.min(start.getThisX(), end.getThisX());
						int b = Math.max(start.getThisX(), end.getThisX());
						if (ship.length() == b - s + 1) {
							checkPath(start.getThisY(), s, b, false, start, end, ship, fleet, board, showShips);
						} else {
							end.setBackground(Color.BLUE);
							fleet.push(ship);
						}
					} else {
						end.setBackground(Color.BLUE);
						fleet.push(ship);
					}
				} else {
					start.setBackground(Color.BLUE);
					fleet.push(ship);
				}
			} else {
				if (showShips) {
					start.setBackground(Color.GRAY);
				}
				lastButton[0] = start.getThisX();
				lastButton[1] = start.getThisY();
			}
		}
	}
	
	private void checkPath(int m, int s, int b, boolean isHorizontal, BattleButton start, BattleButton end, Ship ship, Stack<Ship> fleet, BattleButton[][] board, boolean showShips) {
		boolean flag = true;
		Stack<BattleButton> place = new Stack<>();
		for (int i = s; i <= b; i++) {
			if (isHorizontal) {
				place.push(board[start.getThisX()][i]);
				if (board[start.getThisX()][i].getHasShip()) {
					flag = false;
				}
			} else {
				place.push(board[i][start.getThisY()]);
				if (board[i][start.getThisY()].getHasShip()) {
					flag = false;
				}
			}
		}
		if (flag) {
			int j = 0;
			while (!place.isEmpty()) {
				BattleButton btn = place.pop();
				if (showShips) {
					btn.setBackground(Color.GRAY);
				}
				btn.addShip(ship, j);
				j++;
			}
		} else {
			if (!end.getHasShip()) {
				end.setBackground(Color.BLUE);
			}
			fleet.push(ship);
		}
	}
}