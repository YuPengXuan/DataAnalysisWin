package com.xn.alex.data.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ProgressBar extends JFrame implements ChangeListener,
		ActionListener {

	JButton start, stop;
	JProgressBar bar;
	JLabel show;
	JPanel pane, pane1;
	Timer timer;

	public ProgressBar(String title) {
		super(title);
		init();
		pack();
	}

	public void init() {
		setBounds(300, 100, 300, 100);
		pane = new JPanel();
		pane.setLayout(new BorderLayout());
		getContentPane().add(pane);

		start = new JButton("start");
		start.addActionListener(this);
		stop = new JButton("stop");
		stop.addActionListener(this);
		pane1 = new JPanel();
		pane1.add(start);
		pane1.add(stop);

		bar = new JProgressBar();
		bar.setMinimum(0);
		bar.setMaximum(100);
		bar.setValue(100);
		bar.setStringPainted(true);
		bar.addChangeListener(this);
		bar.setPreferredSize(new Dimension(200, 30));
		int leave = 100 - bar.getValue();
		show = new JLabel("已完成：" + leave, JLabel.CENTER);

		pane.add(pane1, BorderLayout.NORTH);
		pane.add(bar, BorderLayout.CENTER);
		pane.add(show, BorderLayout.SOUTH);

		timer = new Timer(50, this);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		int value = bar.getValue();
		int leave = 100 - value;
		if (e.getSource() == bar) {
			show.setText("已完成：" + leave + "%");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == start) {
			timer.start();
		} else if (e.getSource() == stop) {
			timer.stop();
			bar.setValue(0);
		} else if (e.getSource() == timer) {
			int value = bar.getValue();
			if (value > 0) {
				value--;
				bar.setValue(value);
			}
		}
	}

	public static void main(String[] args) {
		new ProgressBar("uninstall").setVisible(true);
	}

}
