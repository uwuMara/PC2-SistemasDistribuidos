package com.sakila.ui;

import com.sakila.api.ApiClient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RentalPanel extends JPanel {

    private final ApiClient api;
    private final int staffId;
    private final String staffName;

    private final JTextField stockInventoryField;
    private final JTextField rentalCustomerField;
    private final JTextField rentalInventoryField;
    private final JTextField returnRentalField;

    private final JTextArea resultArea;

    private final Color background = new Color(255, 244, 248);
    private final Color card = Color.WHITE;
    private final Color darkPink = new Color(173, 20, 87);
    private final Color text = new Color(60, 45, 55);
    private final Color borderPink = new Color(255, 202, 212);

    public RentalPanel(int staffId, String staffName) {

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

        stockInventoryField = createTextField();
        rentalCustomerField = createTextField();
        rentalInventoryField = createTextField();
        returnRentalField = createTextField();

        leftPanel.add(createStockCard());
        leftPanel.add(Box.createVerticalStrut(14));

        leftPanel.add(createRentalCard());
        leftPanel.add(Box.createVerticalStrut(14));

        leftPanel.add(createReturnCard());
        leftPanel.add(Box.createVerticalStrut(14));



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

                Aquí se mostrarán las respuestas de las operaciones:
                - verificación de stock
                - registro de alquileres
                - devoluciones
                - alquileres activos
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

        JPanel resultHeader = new JPanel(new BorderLayout());
        resultHeader.setBackground(card);

        JLabel resultTitle = new JLabel("Resultados de Operación");
        resultTitle.setFont(new Font("Dialog", Font.BOLD, 18));
        resultTitle.setForeground(darkPink);

        resultHeader.add(resultTitle, BorderLayout.WEST);

        resultCard.add(resultHeader, BorderLayout.NORTH);
        resultCard.add(resultScroll, BorderLayout.CENTER);

        contentPanel.add(leftPanel);
        contentPanel.add(resultCard);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new GridLayout(2, 1, 4, 4));
        header.setBackground(background);

        JLabel title = new JLabel("Terminal de Alquileres y Devoluciones");
        title.setFont(new Font("Dialog", Font.BOLD, 26));
        title.setForeground(darkPink);

        JLabel subtitle = new JLabel("Dashboard transaccional para stock, rentas, retornos y alquileres activos");
        subtitle.setFont(new Font("Dialog", Font.PLAIN, 14));
        subtitle.setForeground(new Color(120, 80, 100));

        header.add(title);
        header.add(subtitle);

        return header;
    }

    private JPanel createTopHeader() {

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(background);

        JPanel textPanel = createHeader();

        JButton activeButton = createButton("Alquileres Activos");

        activeButton.addActionListener(
                e -> getActiveRentals()
        );

        topPanel.add(textPanel, BorderLayout.WEST);
        topPanel.add(activeButton, BorderLayout.EAST);

        return topPanel;
    }

    private JPanel createStockCard() {
        JPanel cardPanel = createCard("Verificar Stock");

        cardPanel.add(createLabel("Inventory ID"));
        cardPanel.add(stockInventoryField);

        JButton button = createButton("Verificar stock");
        button.addActionListener(e -> checkStock());

        cardPanel.add(Box.createVerticalStrut(6));
        cardPanel.add(button);

        return cardPanel;
    }

    private JPanel createRentalCard() {
        JPanel cardPanel = createCard("Registrar Alquiler");

        cardPanel.add(createLabel("Customer ID"));
        cardPanel.add(rentalCustomerField);

        cardPanel.add(Box.createVerticalStrut(6));

        cardPanel.add(createLabel("Inventory ID"));
        cardPanel.add(rentalInventoryField);

        JButton button = createButton("Procesar alquiler");
        button.addActionListener(e -> createRental());

        cardPanel.add(Box.createVerticalStrut(8));
        cardPanel.add(button);

        return cardPanel;
    }

    private JPanel createReturnCard() {
        JPanel cardPanel = createCard("Registrar Devolución");

        cardPanel.add(createLabel("Rental ID"));
        cardPanel.add(returnRentalField);

        JButton button = createButton("Registrar devolución");
        button.addActionListener(e -> returnRental());

        cardPanel.add(Box.createVerticalStrut(8));
        cardPanel.add(button);

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

    private void checkStock() {

        try {
            int inventoryId = Integer.parseInt(stockInventoryField.getText());

            String response = api.get("/inventory/" + inventoryId + "/stock");

            String title = api.extractString(response, "title");
            boolean available = api.extractBoolean(response, "available");

            if (available) {
                resultArea.setText("""
                        ESTADO DE INVENTARIO

                        Resultado: Disponible para alquiler
                        Película: %s
                        Inventory ID: %d

                        Puedes procesar el alquiler usando este inventario.
                        """.formatted(title, inventoryId));
            } else {
                resultArea.setText("""
                        ESTADO DE INVENTARIO

                        Resultado: No disponible
                        Película: %s
                        Inventory ID: %d

                        Esta copia ya se encuentra alquilada.
                        Prueba con otro Inventory ID.
                        """.formatted(title, inventoryId));

                JOptionPane.showMessageDialog(
                        this,
                        "Inventario no disponible.\nLa película ya se encuentra alquilada.",
                        "Bloqueo de alquiler",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        } catch (Exception ex) {
            resultArea.setText("""
                    ERROR EN VERIFICACIÓN

                    No se pudo verificar el inventario.
                    Revisa que el Inventory ID sea válido.

                    Detalle: %s
                    """.formatted(ex.getMessage()));
        }
    }

    private void createRental() {

        try {
            int customerId = Integer.parseInt(rentalCustomerField.getText());
            int inventoryId = Integer.parseInt(rentalInventoryField.getText());

            String stockResponse = api.get("/inventory/" + inventoryId + "/stock");
            boolean available = api.extractBoolean(stockResponse, "available");
            String filmTitle = api.extractString(stockResponse, "title");

            if (!available) {
                resultArea.setText("""
                        ALQUILER BLOQUEADO

                        Resultado: Inventario no disponible
                        Película: %s
                        Inventory ID: %d

                        La operación fue detenida antes de registrar el alquiler.
                        """.formatted(filmTitle, inventoryId));

                JOptionPane.showMessageDialog(
                        this,
                        "No se puede procesar el alquiler.\nEl inventario no está disponible.",
                        "Alquiler bloqueado",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            String json = String.format("""
                    {
                        "customer_id": %d,
                        "inventory_id": %d,
                        "staff_id": %d
                    }
                    """, customerId, inventoryId, staffId);

            String response = api.post("/rentals", json);

            String rentalId = api.extractNumber(response, "rental_id");

            resultArea.setText("""
                    ALQUILER REGISTRADO

                    Estado: Operación realizada correctamente

                    Staff ID: %d
                    Operador: %s

                    Cliente ID: %d
                    Película: %s
                    Inventory ID: %d
                    Rental ID: %s

                    Usa el Rental ID para registrar pagos o devoluciones.
                    """.formatted(
                    staffId,
                    staffName,
                    customerId,
                    filmTitle,
                    inventoryId,
                    rentalId
            ));

        } catch (Exception ex) {
            resultArea.setText("""
                    ERROR AL PROCESAR ALQUILER

                    No se pudo registrar el alquiler.
                    Verifica que el cliente exista y que el inventario esté disponible.

                    Detalle: %s
                    """.formatted(ex.getMessage()));
        }
    }

    private void returnRental() {

        try {
            int rentalId = Integer.parseInt(returnRentalField.getText());

            String json = String.format("""
                    {
                        "rental_id": %d
                    }
                    """, rentalId);

            api.put("/rentals/return", json);

            resultArea.setText("""
                    DEVOLUCIÓN REGISTRADA

                    Estado: Alquiler cerrado correctamente

                    Rental ID: %d

                    La fecha de devolución fue registrada en la base de datos.
                    """.formatted(rentalId));

        } catch (Exception ex) {
            resultArea.setText("""
                    ERROR AL REGISTRAR DEVOLUCIÓN

                    No se pudo cerrar el alquiler.
                    Revisa que el Rental ID exista.

                    Detalle: %s
                    """.formatted(ex.getMessage()));
        }
    }

    private void getActiveRentals() {

        try {
            String response = api.get("/rentals/active");

            response = response
                    .replace("[", "")
                    .replace("]", "");

            String[] rentals = response.split("\\},\\{");

            StringBuilder builder = new StringBuilder();

            builder.append("ALQUILERES ACTIVOS\n");
            builder.append("================================\n\n");

            int contador = 1;

            for (String rental : rentals) {

                rental = rental
                        .replace("{", "")
                        .replace("}", "")
                        .replace("\"", "");

                String rentalId = extraerValor(rental, "rental_id");
                String cliente = extraerValor(rental, "customer_name");
                String pelicula = extraerValor(rental, "film_title");
                String inventoryId = extraerValor(rental, "inventory_id");
                String fecha = extraerValor(rental, "rental_date");
                String dias = extraerValor(rental, "dias");
                String horas = extraerValor(rental, "horas");
                String minutos = extraerValor(rental, "minutos");

                builder.append("Alquiler activo #").append(contador).append("\n");
                builder.append("--------------------------------\n");
                builder.append("Rental ID: ").append(rentalId).append("\n");
                builder.append("Cliente: ").append(cliente).append("\n");
                builder.append("Película: ").append(pelicula).append("\n");
                builder.append("Inventory ID: ").append(inventoryId).append("\n");
                builder.append("Fecha de alquiler: ").append(fecha).append("\n");
                builder.append("Tiempo transcurrido: ")
                        .append(dias).append(" días, ")
                        .append(horas).append(" horas, ")
                        .append(minutos).append(" minutos\n\n");

                contador++;
            }

            resultArea.setText(builder.toString());

        } catch (Exception ex) {
            resultArea.setText("""
                    ERROR AL CONSULTAR ALQUILERES

                    No se pudieron obtener los alquileres activos.

                    Detalle: %s
                    """.formatted(ex.getMessage()));
        }
    }

    private String extraerValor(String texto, String campo) {

        String buscar = campo + ":";

        int inicio = texto.indexOf(buscar);

        if (inicio == -1) {
            return "";
        }

        inicio += buscar.length();

        int fin = texto.indexOf(",", inicio);

        if (fin == -1) {
            fin = texto.length();
        }

        return texto.substring(inicio, fin).trim();
    }
}