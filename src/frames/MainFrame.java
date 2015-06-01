package frames;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import service.BlockService;
import util.Frameutil;

/**
 * 主界面
 * @author skywalker
 *
 */
public class MainFrame extends JFrame {

	private static final long serialVersionUID = -2592111571544578534L;
	/**主背景颜色**/
	private static final Color MAINBACKGROUNDCOLOR = new Color(187,173,160);
	/**窗体宽度**/
	private static final int WINDOWWIDTH = 388;
	/**窗体高度**/
	private static final int WINDOWHEIGHT = 440;
	/**主Panel的尺寸(正方形)**/
	private static final int MAINPANELSIZE = 384;
	/**上方信息条高度**/
	private static final int MESSAGEHEIGHT = 30;
	private JPanel contentPane;
	private BlockService blockService = BlockService.getInstance();

	public MainFrame() {
		try {
			//windows样式	
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}catch(Exception e) {
			e.printStackTrace();
		}
		//图标
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/2048.png")));
		setTitle("2048");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//居中窗体
		Point point = Frameutil.getMiddlePoint(WINDOWWIDTH, WINDOWHEIGHT);
		setBounds(point.x, point.y, WINDOWWIDTH, WINDOWHEIGHT);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//上方信息条
		JPanel messagePanel = new JPanel();
		messagePanel.setBackground(MAINBACKGROUNDCOLOR);
		messagePanel.setBounds(0, 0, MAINPANELSIZE, MESSAGEHEIGHT);
		contentPane.add(messagePanel);
		messagePanel.setLayout(null);
		
		//显示最高分
		JLabel maxPoint = new JLabel();
		Font font = new Font("微软雅黑", Font.BOLD, 20);
		maxPoint.setBounds(BlockService.BLOCKSPACE, 0, MAINPANELSIZE / 2 - 40, MESSAGEHEIGHT);
		Color color = new Color(255, 255, 255);
		maxPoint.setForeground(color);
		maxPoint.setFont(font);
		messagePanel.add(maxPoint);
		
		//显示当前分数
		JLabel currentPoint = new JLabel();
		currentPoint.setFont(font);
		currentPoint.setForeground(color);
		currentPoint.setBounds(BlockService.BLOCKSPACE + MAINPANELSIZE / 2 - 40, 0, MAINPANELSIZE / 2 - 40, MESSAGEHEIGHT);
		messagePanel.add(currentPoint);
		
		//重新开始
		JLabel restart = new JLabel();
		restart.setFont(font);
		restart.setText("重   玩");
		restart.setBounds(BlockService.BLOCKSPACE + MAINPANELSIZE - 80, 0, 80, MESSAGEHEIGHT);
		color = new Color(129,75,33);
		restart.setForeground(color);
		restart.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				blockService.restart();
			}
		});
		messagePanel.add(restart);
		
		//游戏主界面
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(MAINBACKGROUNDCOLOR);
		mainPanel.setBounds(0, MESSAGEHEIGHT, MAINPANELSIZE, MAINPANELSIZE);
		contentPane.add(mainPanel);
		mainPanel.setLayout(null);
		
		//增加键盘事件监听
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				blockService.moveControl(e.getKeyCode());
				//重画
				repaint();
			}
		});
		
		//游戏开始
		blockService.start(this, mainPanel, maxPoint, currentPoint);
		
	}
	
}
