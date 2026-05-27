package com.sakila.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(int staffId, String staffName) {

        setTitle("Sakila Distributed System - Módulo 4 | Staff: " + staffName);
        setSize(1150, 780);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        UIManager.put("TabbedPane.selected", new Color(255, 228, 235));
        UIManager.put("TabbedPane.font", new Font("Dialog", Font.BOLD, 14));

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Alquileres y Devoluciones", new RentalPanel(staffId, staffName));
        tabs.addTab("Caja y Pagos", new PaymentPanel(staffId, staffName));

        add(tabs);

        setVisible(true);
    }
}