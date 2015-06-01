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
 * 方块工具类
 * @author skywalker
 *
 */
public class BlockService {
	
	/**无数字方块背景**/
	private static Map<Integer, Color> blockColors = new HashMap<Integer, Color>(11);
	/**方块间距**/
	public static final int BLOCKSPACE = 8;
	/**方块大小**/
	private static final int BLOCKSIZE = 86;
	private static BlockService blockService;
	/**二维数组存放方块**/
	private Panel[][] blocks = new Panel[4][4];;
	/**随机数生成器**/
	private Random random = new Random();
	/**保留主panel，供重新开始游戏**/
	private JPanel mainPanel;
	/**保留主窗体的引用**/
	private JFrame frame;
	/**显示最高分和当前分的组件**/
	private JLabel maxPointLabel;
	private JLabel currentPointLabel;
	/**最高得分**/
	private int maxPoint = 0;
	/**当前分数**/
	private int currentPoint = 0;
	/**标记是否最高分已经改变**/
	private boolean isMaxPointChanged = false;
	
	public synchronized static BlockService getInstance() {
		if(blockService == null) {
			blockService = new BlockService();
		}
		return blockService;
	}
	
	private BlockService(){}
	
	static {
		//设置颜色块--0
		blockColors.put(0, new Color(204,192,179));
		//设置颜色块--2
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
	 * 开始游戏
	 */
	public void start(JFrame frame, JPanel mainPanel, JLabel maxPoint, JLabel currentPoint) {
		if(this.mainPanel == null) {
			this.mainPanel = mainPanel;
			this.frame = frame;
			this.maxPointLabel = maxPoint;
			this.currentPointLabel = currentPoint;
		}
		//生成16个方块
		for(int i = 0;i < 4;i ++) {
			for(int j = 0;j < 4;j ++) {
				blocks[i][j] = generateBlock(mainPanel, i, j);
			}
		}
		
		//随机生成两个2方块
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
	 * 设置分数
	 * @param isInit 如果为true从文件读取最大值，并且当前分数设0
	 * @param isSetMaxPoint true刷新最大值
	 * @param isSetCurrentPoint true刷新当前值
	 * @param isSaveMaxPoint true把最高分写入文件
	 */
	private void setPoints(boolean isInit, boolean isSetMaxPoint, boolean isSetCurrentPoint, boolean isSaveMaxPoint) {
		if(isInit) {
			//初始化
			maxPoint = PointUtil.readMaxPoint();
			maxPointLabel.setText("最高得分:" + maxPoint);
			currentPointLabel.setText("当前得分:" + currentPoint);
		}else {
			if(isSetMaxPoint) {
				maxPointLabel.setText("最高得分:" + maxPoint);
			}
			if(isSaveMaxPoint && isMaxPointChanged) {
				PointUtil.writeMaxPoint(maxPoint);
			}
			if(isSetCurrentPoint) {
				currentPointLabel.setText("当前得分:" + currentPoint);
			}
			//重画
			frame.repaint();
		}
	}

	/**
	 * 生成16个方块
	 * @param noX横向第几个，noY纵向第几个
	 * 从零开始计
	 */
	public Panel generateBlock(JPanel parent, int noX, int noY) {
		Panel panel = new Panel();
		//计算坐标
		int x = (noY + 1) * BLOCKSPACE + noY * BLOCKSIZE;
		int y = (noX + 1) * BLOCKSPACE + noX * BLOCKSIZE;
		panel.setBounds(x, y, BLOCKSIZE, BLOCKSIZE);
		panel.setBackground(blockColors.get(0));
		parent.add(panel);
		return panel;
	}
	
	/**
	 * 改变一个panel，并且显示数字
	 */
	public void changeBlock(Panel panel, int num) {
		panel.setBackground(blockColors.get(num));
		panel.setNum(num);
		//零不现实任何内容
		String result = (num == 0) ? "" : num + "";
		Font font = null;
		//如果数字已经达到3位数或者四位数改变字体大小
		if(num > 100) {
			int fontSize = (num > 1000) ? 35 : 43;
			font = new Font("微软雅黑", Font.BOLD, fontSize);
		}else {
			font = new Font("微软雅黑", Font.BOLD, 52);
		}
		panel.getLabel().setFont(font);
		panel.getLabel().setText(result);
	}

	/**
	 * 移动控制
	 * @param keyCode 键位码
	 * 下 40
	 * 上38
	 * 左37
	 * 右39
	 * @param blocks 
	 */
	public void moveControl(int keyCode) {
		Direction direction = null;
		//如果是向下
		if(keyCode == 40) {
			direction = Direction.Down;
			moveUpOrDown(direction);
		}else if(keyCode == 38) {
			//向上移
			direction = Direction.Up;
			moveUpOrDown(direction);
		}else if(keyCode == 37) {
			//向左移
			direction = Direction.Left;
			moveLeftOrRight(direction);
		}else if(keyCode == 39) {
			//向右移
			direction = Direction.Right;
			moveLeftOrRight(direction);
		}
	}
	
	/**
	 * 新增一个
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
			//检测游戏失败
			gameOver();
		}else {
			changeBlock(nulls.get(random.nextInt(size)), 2);
		}
	}
	
	/**
	 * 判断游戏是否结束
	 * 当没有空格并且无法做出有效(合并)移动时
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
	 * 游戏结束
	 */
	public void gameOver() {
		//重玩0 退出1 直接关闭对话框-1
		int option = JOptionPane.showOptionDialog(null, "游戏结束，下次好运!", "提示", JOptionPane.DEFAULT_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, new Object[]{"重玩", "退出"}, null);
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
	 * 游戏成功，达到2048
	 */
	private void succeed() {
		//重玩0 退出1 直接关闭对话框-1
		int option = JOptionPane.showOptionDialog(null, "恭喜达到2048!", "success", JOptionPane.DEFAULT_OPTION, 
				JOptionPane.QUESTION_MESSAGE, null, new Object[]{"重玩", "退出"}, null);
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
	 * 重新开始
	 */
	public void restart() {
		//清空主panel容器
		this.mainPanel.removeAll();
		this.currentPoint = 0;
		//清除当前分数并且保存最高分
		setPoints(false, false, true, true);
		start(this.frame, this.mainPanel, this.maxPointLabel, this.currentPointLabel);
		this.frame.repaint();
	}
	
	/**
	 * 左右移
	 */
	private void moveLeftOrRight(Direction direction) {
		int result = 0;
		int jBegin = 1;
		int jEnd = 4;
		if(direction == Direction.Right) {
			jBegin = 0;
			jEnd = 3;
		}
		//先按列扫描
		for(int j = jBegin;j < jEnd;j ++) {
			for(int i = 0;i < 4;i ++) {
				int vAxis = (direction == Direction.Right) ? 2 - j : j;
				Panel panel = blocks[i][vAxis];
				//当前块有数字
				if(panel.getNum() != 0) {
					Point nextPosition = getNextBlock(direction, i, vAxis);
					Panel next = blocks[nextPosition.x][nextPosition.y];
					//如果左面或右面没有方块，直接交换
					if(next.getNum() == 0) {
						changeBlock(next, panel.getNum());
						changeBlock(panel, 0);
					}else if(next.getNum() == panel.getNum()) {
						//如果下一个位置的数字和当前的相等，那么相加并且当前的清零
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
						//判断是否达到2048
						if(result == 2048) {
							succeed();
						}
					}else {
						//不相等，直接移动到下一个的左方或右方
						int nextPos = (direction == Direction.Right) ? nextPosition.y - 1 : nextPosition.y + 1;
						//如果下一个位置不是当前位置
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
	 * 上下移
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
				//当前块有数字
				if(panel.getNum() != 0) {
					Point nextPosition = getNextBlock(direction, hAxis, j);
					Panel next = blocks[nextPosition.x][nextPosition.y];
					//如果下面没有方块，直接交换
					if(next.getNum() == 0) {
						changeBlock(next, panel.getNum());
						changeBlock(panel, 0);
					}else if(next.getNum() == panel.getNum()) {
						//如果下一个位置的数字和当前的相等，那么相加并且当前的清零
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
						//判断是否达到2048
						if(result == 2048) {
							succeed();
						}
					}else {
						//不相等，直接移动到下一个的上方或下方
						int nextPos = (direction == Direction.Down) ? nextPosition.x - 1 : nextPosition.x + 1;
						//如果下一个位置不是当前位置
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
	 * 获取下一个移动到的位置
	 */
	private Point getNextBlock(Direction direction, int i, int j) {
		switch (direction) {
		case Down:
			for(int m = i + 1;m < 4;m ++) {
				Panel panel = blocks[m][j];
				//找到不为零的立即返回或者是最后一行
				if(panel.getNum() != 0 || m == 3) {
					return new Point(m, j);
				}
			}
			break;
		case Up:
			for(int m = i - 1;m >= 0;m --) {
				Panel panel = blocks[m][j];
				//找到不为零的立即返回或者是第一行
				if(panel.getNum() != 0 || m == 0) {
					return new Point(m, j);
				}
			}
			break;
		case Left:
			for(int m = j - 1;m >= 0;m --) {
				Panel panel = blocks[i][m];
				//找到不为零的立即返回或者是第一列
				if(panel.getNum() != 0 || m == 0) {
					return new Point(i, m);
				}
			}
			break;
		case Right:
			for(int m = j + 1;m < 4;m ++) {
				Panel panel = blocks[i][m];
				//找到不为零的立即返回或者是最后一列
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
