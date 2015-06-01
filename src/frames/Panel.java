package frames;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *	自己的JPanel
 * @author skywalker
 *
 */
public class Panel extends JPanel {

	private static final long serialVersionUID = 2639038723215135435L;
	
	/**
	 * 当前panel上的数字
	 */
	private int num = 0;
	/**
	 * 显示数字的Label
	 */
	private JLabel label;
	
	public Panel() {
		//初始化label
		this.label = new JLabel();
		//添加数字显示Label
		Font font = new Font("微软雅黑", Font.BOLD, 52);
		this.label.setFont(font);
		this.add(label);
	}
	
	public Panel(int num) {
		this();
		this.num = num;
	}

	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public JLabel getLabel() {
		return label;
	}
	public void setLabel(JLabel label) {
		this.label = label;
	}
	
}
