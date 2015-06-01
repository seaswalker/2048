package frames;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *	�Լ���JPanel
 * @author skywalker
 *
 */
public class Panel extends JPanel {

	private static final long serialVersionUID = 2639038723215135435L;
	
	/**
	 * ��ǰpanel�ϵ�����
	 */
	private int num = 0;
	/**
	 * ��ʾ���ֵ�Label
	 */
	private JLabel label;
	
	public Panel() {
		//��ʼ��label
		this.label = new JLabel();
		//���������ʾLabel
		Font font = new Font("΢���ź�", Font.BOLD, 52);
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
