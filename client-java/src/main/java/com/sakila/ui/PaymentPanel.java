package com.sakila.ui;

import com.sakila.api.ApiClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PaymentPanel extends JPanel {

    private final ApiClient api;
    private final int staffId;
    private final String staffName;

    private final JTextField paymentCustomerField;
    private final JTextField paymentRentalField;
    private final JTextField paymentAmountField;

    private final JSpinner minPaymentsSpinner;
    private final JSpinner minAmountSpinner;

    private final JTextArea resultArea;

    private final Color background = new Color(255, 244, 248);
    private final Color card = Color.WHITE;
    private final Color darkPink = new Color(173, 20, 87);
    private final Color text = new Color(60, 45, 55);
    private final Color borderPink = new Color(255, 202, 212);

    public PaymentPanel(int staffId, String staffName) {
        this.staffId = staffId;
        this.staffName = staffName;

        api = new ApiClient();

        setLayout(new BorderLayout());
        setBackground(background);

        JPanel mainPanel = new JPanel(new BorderLayout(22, 22));
        mainPanel.setBackground(background);
        mainPanel.setBorder(new EmptyBorder(24, 28, 24, 28));

        mainPanel.add(createTopHeader(), BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 22, 0));
        contentPanel.setBackground(background);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(background);

        paymentCustomerField = createTextField();
        paymentRentalField = createTextField();
        paymentAmountField = createTextField();

        minPaymentsSpinner = new JSpinner(
                new SpinnerNumberModel(1, 1, 100, 1)
        );

        minAmountSpinner = new JSpinner(
                new SpinnerNumberModel(1, 1, 10000, 1)
        );

        leftPanel.add(createPaymentCard());
        leftPanel.add(Box.createVerticalStrut(14));


        leftPanel.add(createFiltersCard());

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Dialog", Font.PLAIN, 15));
        resultArea.setForeground(text);
        resultArea.setBackground(new Color(255, 250, 252));
        resultArea.setBorder(new EmptyBorder(18, 18, 18, 18));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setText("""
                PANEL DE RESULTADOS

                Aquí se mostrarán:
                - comprobantes de pago
                - ingresos diarios
                - reportes de clientes destacados
                """);

        JScrollPane resultScroll = new JScrollPane(resultArea);
        resultScroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderPink, 2),
                new EmptyBorder(0, 0, 0, 0)
        ));

        JPanel resultCard = new JPanel(new BorderLayout(10, 10));
        resultCard.setBackground(card);
        resultCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderPink, 2),
                new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel resultTitle = new JLabel("Resultados de Caja");
        resultTitle.setFont(new Font("Dialog", Font.BOLD, 18));
        resultTitle.setForeground(darkPink);

        resultCard.add(resultTitle, BorderLayout.NORTH);
        resultCard.add(resultScroll, BorderLayout.CENTER);

        contentPanel.add(leftPanel);
        contentPanel.add(resultCard);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createTopHeader() {

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(background);

        JPanel textPanel = createHeader();

        JButton incomeButton = createButton("Ingresos del día");

        incomeButton.addActionListener(
                e -> getDailyIncome()
        );

        topPanel.add(textPanel, BorderLayout.WEST);
        topPanel.add(incomeButton, BorderLayout.EAST);

        return topPanel;
    }

    private JPanel createHeader() {

        JPanel header = new JPanel(new GridLayout(2, 1, 4, 4));
        header.setBackground(background);

        JLabel title = new JLabel("Módulo de Caja, Pagos y Reportes");
        title.setFont(new Font("Dialog", Font.BOLD, 26));
        title.setForeground(darkPink);

        JLabel subtitle = new JLabel("Dashboard financiero para cobros, ingresos diarios y clientes destacados");
        subtitle.setFont(new Font("Dialog", Font.PLAIN, 14));
        subtitle.setForeground(new Color(120, 80, 100));

        header.add(title);
        header.add(subtitle);

        return header;
    }

    private JPanel createPaymentCard() {

        JPanel cardPanel = createCard("Registrar Pago");

        cardPanel.add(createLabel("Customer ID"));
        cardPanel.add(paymentCustomerField);

        cardPanel.add(Box.createVerticalStrut(6));

        cardPanel.add(createLabel("Rental ID"));
        cardPanel.add(paymentRentalField);

        cardPanel.add(Box.createVerticalStrut(6));

        cardPanel.add(createLabel("Monto"));
        cardPanel.add(paymentAmountField);

        JButton button = createButton("Registrar pago");

        button.addActionListener(
                e -> createPayment()
        );

        JPanel buttonsPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 10, 0)
        );

        buttonsPanel.setBackground(card);
        buttonsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.setPreferredSize(
                new Dimension(160, 42)
        );

        JButton debtButton = createButton(
                "Ver deuda"
        );

        debtButton.setPreferredSize(
                new Dimension(160, 42)
        );

        debtButton.addActionListener(
                e -> getCustomerDebt()
        );

        buttonsPanel.add(button);
        buttonsPanel.add(debtButton);

        cardPanel.add(Box.createVerticalStrut(12));
        cardPanel.add(buttonsPanel);

        cardPanel.setPreferredSize(
        new Dimension(500, 320)
        );

        cardPanel.setMaximumSize(
        new Dimension(Integer.MAX_VALUE, 320)
        );

        return cardPanel;
    }


    private JPanel createFiltersCard() {

        JPanel cardPanel = createCard("Filtros de Clientes Destacados");

        cardPanel.add(createLabel("Pagos mínimos"));

        minPaymentsSpinner.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 34)
        );
        minPaymentsSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);

        cardPanel.add(minPaymentsSpinner);

        cardPanel.add(minPaymentsSpinner);

        cardPanel.add(Box.createVerticalStrut(8));

        cardPanel.add(createLabel("Monto mínimo"));

        minAmountSpinner.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 34)
        );

        minAmountSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);

        cardPanel.add(minAmountSpinner);

        cardPanel.add(minAmountSpinner);

        JButton rewardsButton = createButton("Clientes destacados");

        rewardsButton.addActionListener(
                e -> getRewardsReport()
        );

        cardPanel.add(Box.createVerticalStrut(12));
        cardPanel.add(rewardsButton);

        cardPanel.setPreferredSize(
        new Dimension(500, 320)
        );

        cardPanel.setMaximumSize(
        new Dimension(Integer.MAX_VALUE, 320)
        );

        return cardPanel;
    }

    private JPanel createCard(String title) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(card);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderPink, 2),
                new EmptyBorder(14, 16, 14, 16)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 17));
        titleLabel.setForeground(darkPink);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(8));


        return panel;
    }

    private JLabel createLabel(String value) {

        JLabel label = new JLabel(value);
        label.setFont(new Font("Dialog", Font.BOLD, 13));
        label.setForeground(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        return label;
    }

    private JTextField createTextField() {

        JTextField field = new JTextField();

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        field.setFont(new Font("Dialog", Font.PLAIN, 14));
        field.setForeground(text);
        field.setBackground(new Color(255, 250, 252));

        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderPink),
                new EmptyBorder(7, 10, 7, 10)
        ));

        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        return field;
    }

    private JButton createButton(String value) {

        JButton button = new JButton(value);

        button.setBackground(darkPink);
        button.setForeground(Color.WHITE);

        button.setFocusPainted(false);

        button.setFont(
                new Font("Dialog", Font.BOLD, 13)
        );

        button.setBorder(
                new EmptyBorder(10, 16, 10, 16)
        );

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        return button;
    }

    private void createPayment() {

        try {
            int customerId = Integer.parseInt(
                    paymentCustomerField.getText()
            );

            int rentalId = Integer.parseInt(
                    paymentRentalField.getText()
            );

            double amount = Double.parseDouble(
                    paymentAmountField.getText()
            );

            String json = String.format("""
                    {
                        "customer_id": %d,
                        "staff_id": %d,
                        "rental_id": %d,
                        "amount": %.2f
                    }
                    """,
                    customerId,
                    staffId,
                    rentalId,
                    amount
            );

            api.post("/payments", json);

            resultArea.setText("""
                    PAGO REGISTRADO

                    Estado: Cobro procesado correctamente

                    Staff ID: %d
                    Operador: %s

                    Customer ID: %d
                    Rental ID: %d
                    Monto: S/ %.2f

                    El pago fue almacenado correctamente en Supabase PostgreSQL.
                    """.formatted(
                    staffId,
                    staffName,
                    customerId,
                    rentalId,
                    amount
            ));

        } catch (Exception ex) {
            resultArea.setText("""
                    ERROR AL REGISTRAR PAGO

                    No se pudo procesar el cobro.
                    Revisa que el cliente y alquiler existan.

                    Detalle: %s
                    """.formatted(ex.getMessage()));
        }
    }

    private void getDailyIncome() {

        try {
            String response = api.get(
                    "/payments/daily-income"
            );

            String fecha = api.extractString(
                    response,
                    "fecha"
            );

            String total = api.extractNumber(
                    response,
                    "ingresos_totales"
            );

            resultArea.setText("""
                    INGRESOS DEL DÍA

                    Fecha: %s
                    Total recaudado: S/ %s

                    Este monto corresponde a los pagos registrados hoy.
                    """.formatted(
                    fecha,
                    total
            ));

        } catch (Exception ex) {
            resultArea.setText("""
                    ERROR AL CONSULTAR INGRESOS

                    No se pudieron obtener los ingresos del día.

                    Detalle: %s
                    """.formatted(ex.getMessage()));
        }
    }

    private void getRewardsReport() {

        try {
            int minPayments = (Integer)
                    minPaymentsSpinner.getValue();

            int minAmount = (Integer)
                    minAmountSpinner.getValue();

            String json = String.format("""
                    {
                        "min_payments": %d,
                        "min_amount": %d
                    }
                    """,
                    minPayments,
                    minAmount
            );

            String response = api.post(
                    "/reports/rewards",
                    json
            );

            response = response
                    .replace("[", "")
                    .replace("]", "");

            String[] clientes = response.split("\\},\\{");

            StringBuilder builder = new StringBuilder();

            builder.append("REPORTE DE CLIENTES DESTACADOS\n");
            builder.append("Filtros aplicados:\n");
            builder.append("Pagos mínimos: ").append(minPayments).append("\n");
            builder.append("Monto mínimo: S/ ").append(minAmount).append("\n");
            builder.append("================================\n\n");

            int contador = 1;

            for (String cliente : clientes) {

                cliente = cliente
                        .replace("{", "")
                        .replace("}", "")
                        .replace("\"", "");

                String id = extraerValor(
                        cliente,
                        "customer_id"
                );

                String nombre = extraerValor(
                        cliente,
                        "first_name"
                );

                String apellido = extraerValor(
                        cliente,
                        "last_name"
                );

                String pagos = extraerValor(
                        cliente,
                        "total_pagos"
                );

                String total = extraerValor(
                        cliente,
                        "total_gastado"
                );

                builder.append("Cliente destacado #")
                        .append(contador)
                        .append("\n");

                builder.append("--------------------------------\n");
                builder.append("ID Cliente: ").append(id).append("\n");
                builder.append("Nombre: ").append(nombre).append(" ").append(apellido).append("\n");
                builder.append("Pagos realizados: ").append(pagos).append("\n");
                builder.append("Total gastado: S/ ").append(total).append("\n\n");

                contador++;
            }

            resultArea.setText(
                    builder.toString()
            );

        } catch (Exception ex) {
            resultArea.setText("""
                    ERROR AL GENERAR REPORTE

                    No se pudo obtener el reporte de clientes destacados.

                    Detalle: %s
                    """.formatted(ex.getMessage()));
        }
    }

    private String extraerValor(
            String texto,
            String campo
    ) {

        String buscar = campo + ":";

        int inicio = texto.indexOf(buscar);

        if (inicio == -1) {
            return "";
        }

        inicio += buscar.length();

        int fin = texto.indexOf(
                ",",
                inicio
        );

        if (fin == -1) {
            fin = texto.length();
        }

        return texto.substring(
                inicio,
                fin
        ).trim();
    }
    private void getCustomerDebt() {

    try {
        int customerId = Integer.parseInt(
                paymentCustomerField.getText()
        );

        String response = api.get(
                "/customers/" + customerId + "/debt"
        );

        String nombre = api.extractString(
                response,
                "customer_name"
        );

        String alquileres = api.extractNumber(
                response,
                "alquileres_activos"
        );

        String dias = api.extractNumber(
        response,
        "dias_alquilado"
        );

        String monto = api.extractNumber(
                response,
                "monto_estimado"
        );

        resultArea.setText("""
                DESGLOSE DE DEUDA

                Cliente: %s
                Customer ID: %d

                Alquileres activos: %s
                Días de alquiler acumulados: %s
                Monto estimado pendiente: S/ %s

                Cálculo aplicado:
                S/ 2.99 por día de alquiler activo.
                """.formatted(
                nombre,
                customerId,
                alquileres,
                dias,
                monto
        ));

    } catch (Exception ex) {
        resultArea.setText("""
                ERROR AL CONSULTAR DEUDA

                No se pudo obtener el desglose del cliente.

                Detalle: %s
                """.formatted(ex.getMessage()));
    }
}
}