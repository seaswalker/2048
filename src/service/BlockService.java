package service;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import util.PointUtil;
import model.Direction;
import frames.Panel;

/**
 * ���鹤����
 * @author skywalker
 *
 */
public class BlockService {
	
	/**�����ַ��鱳��**/
	private static Map<Integer, Color> blockColors = new HashMap<Integer, Color>(11);
	/**������**/
	public static final int BLOCKSPACE = 8;
	/**�����С**/
	private static final int BLOCKSIZE = 86;
	private static BlockService blockService;
	/**��ά�����ŷ���**/
	private Panel[][] blocks = new Panel[4][4];;
	/**�����������**/
	private Random random = new Random();
	/**������panel�������¿�ʼ��Ϸ**/
	private JPanel mainPanel;
	/**���������������**/
	private JFrame frame;
	/**��ʾ��߷ֺ͵�ǰ�ֵ����**/
	private JLabel maxPointLabel;
	private JLabel currentPointLabel;
	/**��ߵ÷�**/
	private int maxPoint = 0;
	/**��ǰ����**/
	private int currentPoint = 0;
	/**����Ƿ���߷��Ѿ��ı�**/
	private boolean isMaxPointChanged = false;
	
	public synchronized static BlockService getInstance() {
		if(blockService == null) {
			blockService = new BlockService();
		}
		return blockService;
	}
	
	private BlockService(){}
	
	static {
		//������ɫ��--0
		blockColors.put(0, new Color(204,192,179));
		//������ɫ��--2
		blockColors.put(2, new Color(238,228,218));
		blockColors.put(4, new Color(237,224,200));
		blockColors.put(8, new Color(242,177,121));
		blockColors.put(16, new Color(245,149,99));
		blockColors.put(32, new Color(246,124,95));
		blockColors.put(64, new Color(246,94,59));
		blockColors.put(128, new Color(237,207,114));
		blockColors.put(256, new Color(237,204,97));
		blockColors.put(512, new Color(237,200,80));
		blockColors.put(1024, new Color(237,197,63));
		blockColors.put(2048, new Color(237,194,46));
	}
	
	/**
	 * ��ʼ��Ϸ
	 */
	public void start(JFrame frame, JPanel mainPanel, JLabel maxPoint, JLabel currentPoint) {
		if(this.mainPanel == null) {
			this.mainPanel = mainPanel;
			this.frame = frame;
			this.maxPointLabel = maxPoint;
			this.currentPointLabel = currentPoint;
		}
		//����16������
		for(int i = 0;i < 4;i ++) {
			for(int j = 0;j < 4;j ++) {
				blocks[i][j] = generateBlock(mainPanel, i, j);
			}
		}
		
		//�����������2����
		int preX = 0;
		int preY = 0;
		for(int i = 0;i < 2;i ++) {
			int x = random.nextInt(4);
			int y = random.nextInt(4);
			if(i == 1) {
				while(preX == x && preY == y) {
					x = random.nextInt(4);
				}
			}else {
				preX = x;
				preY = y;
			}
			changeBlock(blocks[x][y], 2);
			setPoints(true, false, true, false);
		}
	}
	
	/**
	 * ���÷���
	 * @param isInit ���Ϊtrue���ļ���ȡ���ֵ�����ҵ�ǰ������0
	 * @param isSetMaxPoint trueˢ�����ֵ
	 * @param isSetCurrentPoint trueˢ�µ�ǰֵ
	 * @param isSaveMaxPoint true����߷�д���ļ�
	 */
	private void setPoints(boolean isInit, boolean isSetMaxPoint, boolean isSetCurrentPoint, boolean isSaveMaxPoint) {
		if(isInit) {
			//��ʼ��
			maxPoint = PointUtil.readMaxPoint();
			maxPointLabel.setText("��ߵ÷�:" + maxPoint);
			currentPointLabel.setText("��ǰ�÷�:" + currentPoint);
		}else {
			if(isSetMaxPoint) {
				maxPointLabel.setText("��ߵ÷�:" + maxPoint);
			}
			if(isSaveMaxPoint && isMaxPointChanged) {
				PointUtil.writeMaxPoint(maxPoint);
			}
			if(isSetCurrentPoint) {
				currentPointLabel.setText("��ǰ�÷�:" + currentPoint);
			}
			//�ػ�
			frame.repaint();
		}
	}

	/**
	 * ����16������
	 * @param noX����ڼ�����noY����ڼ���
	 * ���㿪ʼ��
	 */
	public Panel generateBlock(JPanel parent, int noX, int noY) {
		Panel panel = new Panel();
		//��������
		int x = (noY + 1) * BLOCKSPACE + noY * BLOCKSIZE;
		int y = (noX + 1) * BLOCKSPACE + noX * BLOCKSIZE;
		panel.setBounds(x, y, BLOCKSIZE, BLOCKSIZE);
		panel.setBackground(blockColors.get(0));
		parent.add(panel);
		return panel;
	}
	
	/**
	 * �ı�һ��panel��������ʾ����
	 */
	public void changeBlock(Panel panel, int num) {
		panel.setBackground(blockColors.get(num));
		panel.setNum(num);
		//�㲻��ʵ�κ�����
		String result = (num == 0) ? "" : num + "";
		Font font = null;
		//��������Ѿ��ﵽ3λ��������λ���ı������С
		if(num > 100) {
			int fontSize = (num > 1000) ? 35 : 43;
			font = new Font("΢���ź�", Font.BOLD, fontSize);
		}else {
			font = new Font("΢���ź�", Font.BOLD, 52);
		}
		panel.getLabel().setFont(font);
		panel.getLabel().setText(result);
	}

	/**
	 * �ƶ�����
	 * @param keyCode ��λ��
	 * �� 40
	 * ��38
	 * ��37
	 * ��39
	 * @param blocks 
	 */
	public void moveControl(int keyCode) {
		Direction direction = null;
		//���������
		if(keyCode == 40) {
			direction = Direction.Down;
			moveUpOrDown(direction);
		}else if(keyCode == 38) {
			//������
			direction = Direction.Up;
			moveUpOrDown(direction);
		}else if(keyCode == 37) {
			//������
			direction = Direction.Left;
			moveLeftOrRight(direction);
		}else if(keyCode == 39) {
			//������
			direction = Direction.Right;
			moveLeftOrRight(direction);
		}
	}
	
	/**
	 * ����һ��
	 */
	private void addBlock() {
		Random random = new Random();
		List<Panel> nulls = new ArrayList<Panel>();
		for(int i = 0;i < 4;i ++) {
			for(int j = 0;j < 4;j ++) {
				Panel panel = blocks[i][j];
				if(panel.getNum() == 0) {
					nulls.add(panel);
				}
			}
		}
		int size = nulls.size();
		if(size == 0 && isOver()) {
			//�����Ϸʧ��
			gameOver();
		}else {
			changeBlock(nulls.get(random.nextInt(size)), 2);
		}
	}
	
	/**
	 * �ж���Ϸ�Ƿ����
	 * ��û�пո����޷�������Ч(�ϲ�)�ƶ�ʱ
	 */
	public boolean isOver() {
		Panel panel = null;
		Panel next = null;
		for(int i = 0;i < 4;i ++) {
			for(int j = 0;j < 4;j ++) {
				for(Direction direction : Direction.values()) {
					if(direction == Direction.Down && i < 3) {
						next = blocks[i + 1][j];
						panel = blocks[i][j];
						if(panel.getNum() == next.getNum()) {
							return false;
						}
					}else if(direction == Direction.Up && i > 0) {
						next = blocks[i - 1][j];
						panel = blocks[i][j];
						if(panel.getNum() == next.getNum()) {
							return false;
						}
					}else if(direction == Direction.Left && j > 0) {
						next = blocks[i][j - 1];
						panel = blocks[i][j];
						if(panel.getNum() == next.getNum()) {
							return false;
						}
					}else if(direction == Direction.Right && j < 3) {
						next = blocks[i][j + 1];
						panel = blocks[i][j];
						if(panel.getNum() == next.getNum()) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * ��Ϸ����
	 */
	public void gameOver() {
		//����0 �˳�1 ֱ�ӹرնԻ���-1
		int option = JOptionPane.showOptionDialog(null, "��Ϸ�������´κ���!", "��ʾ", JOptionPane.DEFAULT_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, new Object[]{"����", "�˳�"}, null);
		switch (option) {
		case 1:
			setPoints(false, false, false, true);
			System.exit(0);
			break;
		default:
			restart();
			break;
		}
	}
	
	/**
	 * ��Ϸ�ɹ����ﵽ2048
	 */
	private void succeed() {
		//����0 �˳�1 ֱ�ӹرնԻ���-1
		int option = JOptionPane.showOptionDialog(null, "��ϲ�ﵽ2048!", "success", JOptionPane.DEFAULT_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, new Object[]{"����", "�˳�"}, null);
		switch (option) {
		case 1:
			setPoints(false, false, false, true);
			System.exit(0);
			break;
		default:
			restart();
			break;
		}
	}
	
	/**
	 * ���¿�ʼ
	 */
	public void restart() {
		//�����panel����
		this.mainPanel.removeAll();
		this.currentPoint = 0;
		//�����ǰ�������ұ�����߷�
		setPoints(false, false, true, true);
		start(this.frame, this.mainPanel, this.maxPointLabel, this.currentPointLabel);
		this.frame.repaint();
	}
	
	/**
	 * ������
	 */
	private void moveLeftOrRight(Direction direction) {
		int result = 0;
		int jBegin = 1;
		int jEnd = 4;
		if(direction == Direction.Right) {
			jBegin = 0;
			jEnd = 3;
		}
		//�Ȱ���ɨ��
		for(int j = jBegin;j < jEnd;j ++) {
			for(int i = 0;i < 4;i ++) {
				int vAxis = (direction == Direction.Right) ? 2 - j : j;
				Panel panel = blocks[i][vAxis];
				//��ǰ��������
				if(panel.getNum() != 0) {
					Point nextPosition = getNextBlock(direction, i, vAxis);
					Panel next = blocks[nextPosition.x][nextPosition.y];
					//������������û�з��飬ֱ�ӽ���
					if(next.getNum() == 0) {
						changeBlock(next, panel.getNum());
						changeBlock(panel, 0);
					}else if(next.getNum() == panel.getNum()) {
						//�����һ��λ�õ����ֺ͵�ǰ����ȣ���ô��Ӳ��ҵ�ǰ������
						result = next.getNum() << 1;
						currentPoint += result;
						if(currentPoint > maxPoint) {
							maxPoint = currentPoint;
							isMaxPointChanged = true;
							setPoints(false, true, true, false);
						}else {
							setPoints(false, false, true, false);
						}
						changeBlock(next, result);
						changeBlock(panel, 0);
						//�ж��Ƿ�ﵽ2048
						if(result == 2048) {
							succeed();
						}
					}else {
						//����ȣ�ֱ���ƶ�����һ�����󷽻��ҷ�
						int nextPos = (direction == Direction.Right) ? nextPosition.y - 1 : nextPosition.y + 1;
						//�����һ��λ�ò��ǵ�ǰλ��
						if(i != nextPosition.x || vAxis != nextPos) {
							changeBlock(blocks[nextPosition.x][nextPos], panel.getNum());
							changeBlock(panel, 0);
						}
					}
				}
			}
		}
		if(result < 2048) {
			addBlock();
		}
	}

	/**
	 * ������
	 */
	private void moveUpOrDown(Direction direction) {
		int result = 0;
		int iBegin = 0;
		int iEnd = 3;
		if(direction == Direction.Up) {
			iBegin = 1;
			iEnd = 4;
		}
		for(int i = iBegin;i < iEnd;i ++) {
			for(int j = 0;j < 4;j ++) {
				int hAxis = (direction == Direction.Down) ? 2 - i : i;
				Panel panel = blocks[hAxis][j];
				//��ǰ��������
				if(panel.getNum() != 0) {
					Point nextPosition = getNextBlock(direction, hAxis, j);
					Panel next = blocks[nextPosition.x][nextPosition.y];
					//�������û�з��飬ֱ�ӽ���
					if(next.getNum() == 0) {
						changeBlock(next, panel.getNum());
						changeBlock(panel, 0);
					}else if(next.getNum() == panel.getNum()) {
						//�����һ��λ�õ����ֺ͵�ǰ����ȣ���ô��Ӳ��ҵ�ǰ������
						result = next.getNum() << 1;
						currentPoint += result;
						if(currentPoint > maxPoint) {
							maxPoint = currentPoint;
							isMaxPointChanged = true;
							setPoints(false, true, true, false);
						}else {
							setPoints(false, false, true, false);
						}
						changeBlock(next, result);
						changeBlock(panel, 0);
						//�ж��Ƿ�ﵽ2048
						if(result == 2048) {
							succeed();
						}
					}else {
						//����ȣ�ֱ���ƶ�����һ�����Ϸ����·�
						int nextPos = (direction == Direction.Down) ? nextPosition.x - 1 : nextPosition.x + 1;
						//�����һ��λ�ò��ǵ�ǰλ��
						if(hAxis != nextPos || j != nextPosition.y) {
							changeBlock(blocks[nextPos][nextPosition.y], panel.getNum());
							changeBlock(panel, 0);
						}
					}
				}
			}
		}
		if(result < 2048) {
			addBlock();
		}
	}

	/**
	 * ��ȡ��һ���ƶ�����λ��
	 */
	private Point getNextBlock(Direction direction, int i, int j) {
		switch (direction) {
		case Down:
			for(int m = i + 1;m < 4;m ++) {
				Panel panel = blocks[m][j];
				//�ҵ���Ϊ����������ػ��������һ��
				if(panel.getNum() != 0 || m == 3) {
					return new Point(m, j);
				}
			}
			break;
		case Up:
			for(int m = i - 1;m >= 0;m --) {
				Panel panel = blocks[m][j];
				//�ҵ���Ϊ����������ػ����ǵ�һ��
				if(panel.getNum() != 0 || m == 0) {
					return new Point(m, j);
				}
			}
			break;
		case Left:
			for(int m = j - 1;m >= 0;m --) {
				Panel panel = blocks[i][m];
				//�ҵ���Ϊ����������ػ����ǵ�һ��
				if(panel.getNum() != 0 || m == 0) {
					return new Point(i, m);
				}
			}
			break;
		case Right:
			for(int m = j + 1;m < 4;m ++) {
				Panel panel = blocks[i][m];
				//�ҵ���Ϊ����������ػ��������һ��
				if(panel.getNum() != 0 || m == 3) {
					return new Point(i, m);
				}
			}
		default:
			break;
		}
		return null;
	}
	
}
