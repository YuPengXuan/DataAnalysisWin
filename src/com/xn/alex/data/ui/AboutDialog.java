/*
 * Copyright (c) 2012 Ericsson, Inc. All Rights Reserved.
 */
package com.xn.alex.data.ui;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

@SuppressWarnings("serial")
public class AboutDialog extends JDialog {
    private String strName="";
    private String strVersion="";

    public AboutDialog() {

        loadReleaseInfo();
        
        getContentPane().setLayout(new BorderLayout(5, 5));

        this.setTitle("关于移动数据分析系统");

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.white);
        northPanel.setBorder(BorderFactory.createLineBorder(Color.gray));

        ImageIcon icon = new ImageIcon("resource/das.png");
        SplashImage image = new SplashImage();
        image.setIcon(icon);
        northPanel.add(image,BorderLayout.WEST);
        
        JLabel lblVersion = new JLabel();
        String verStr = "<html><table>"
                + "<tr><td><b>" + strName + "</b></td></tr>"
                + "<tr><td>Version: " + strVersion+"</td></tr>" 
                + "<tr><td>(c) AlexSoft Copywrite, Sweden. All rights reserved.</td></tr>"
                + "<tr><td>移动数据可视化分析系统.</td></tr>"
                + "</table></html>";
        lblVersion.setText(verStr);
        northPanel.add(lblVersion,BorderLayout.CENTER);
        
        getContentPane().add(northPanel, BorderLayout.CENTER);
               
        JButton btnClose = new JButton("OK");
        btnClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dispose();
            }
        });
        
        JPanel southPanel = new JPanel();
        FlowLayout fl_southPanel = new FlowLayout();
        fl_southPanel.setAlignment(FlowLayout.RIGHT);
        southPanel.setLayout(fl_southPanel);
        southPanel.add(btnClose);
        getContentPane().add(southPanel, BorderLayout.SOUTH);

        setModal(true);
        setSize(new Dimension(580, icon.getIconHeight() + 50));
        setLocationRelativeTo(this.getParent());
        setResizable(false);
    }

	public void loadReleaseInfo() {
		strName = "移动数据分析系统";
		strVersion = "1.0.0"; 
	}
}
