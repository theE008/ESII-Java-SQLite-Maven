package com.exemplo;

import com.exemplo.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Main extends JFrame {
    private static SessionFactory factory;
    private JTable table;
    private DefaultTableModel tableModel;
    
    // Form Fields
    private JTextField txtId, txtNome, txtPreco, txtEstoque, txtVoltagem;

    public Main() {
        setTitle("Gerenciador de Produtos - MoR Prototype");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen

        // Root Panel with padding
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(root);

        // --- TABLE SECTION ---
        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Preço", "Estoque", "Tipo"}, 0);
        table = new JTable(tableModel);
        root.add(new JScrollPane(table), BorderLayout.CENTER);

        // --- FORM SECTION ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        txtId = new JTextField(); txtId.setEditable(false); // ID is auto-gen
        txtNome = new JTextField(15);
        txtPreco = new JTextField();
        txtEstoque = new JTextField();
        txtVoltagem = new JTextField();

        addFormField(formPanel, "ID (Auto):", txtId, gbc, 0);
        addFormField(formPanel, "Nome:", txtNome, gbc, 1);
        addFormField(formPanel, "Preço:", txtPreco, gbc, 2);
        addFormField(formPanel, "Estoque:", txtEstoque, gbc, 3);
        addFormField(formPanel, "Voltagem:", txtVoltagem, gbc, 4);

        // --- BUTTONS ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Novo/Salvar");
        JButton btnUpdate = new JButton("Atualizar");
        JButton btnDelete = new JButton("Excluir");
        btnDelete.setForeground(Color.RED);

        btnPanel.add(btnSave);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(formPanel, BorderLayout.NORTH);
        rightPanel.add(btnPanel, BorderLayout.SOUTH);
        root.add(rightPanel, BorderLayout.EAST);

        // --- EVENTS ---
        
        // Fill form when selecting a row
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtId.setText(tableModel.getValueAt(row, 0).toString());
                txtNome.setText(tableModel.getValueAt(row, 1).toString());
                txtPreco.setText(tableModel.getValueAt(row, 2).toString());
                txtEstoque.setText(tableModel.getValueAt(row, 3).toString());
            }
        });

        btnSave.addActionListener(e -> { saveOrUpdate(false); refreshTable(); });
        btnUpdate.addActionListener(e -> { saveOrUpdate(true); refreshTable(); });
        btnDelete.addActionListener(e -> { deleteProduct(); refreshTable(); });

        refreshTable();
    }

    private void addFormField(JPanel p, String label, JTextField field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row; p.add(new JLabel(label), gbc);
        gbc.gridx = 1; p.add(field, gbc);
    }

    private void saveOrUpdate(boolean isUpdate) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            ProdutoEletronico p = new ProdutoEletronico();
            
            if (isUpdate) p.setId(Long.parseLong(txtId.getText()));
            
            p.setNome(txtNome.getText());
            p.setPreco(Double.parseDouble(txtPreco.getText()));
            p.setEstoque(Integer.parseInt(txtEstoque.getText()));
            p.setVoltagem(txtVoltagem.getText());
            
            session.merge(p); // Merge works for both insert and update
            session.getTransaction().commit();
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void deleteProduct() {
        String idStr = txtId.getText();
        if (idStr.isEmpty()) return;
        
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            Produto p = session.get(Produto.class, Long.parseLong(idStr));
            if (p != null) session.remove(p);
            session.getTransaction().commit();
            clearForm();
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        try (Session session = factory.openSession()) {
            List<Produto> list = session.createQuery("from Produto", Produto.class).list();
            for (Produto p : list) {
                tableModel.addRow(new Object[]{p.getId(), p.getNome(), p.getPreco(), p.getEstoque(), p.getClass().getSimpleName()});
            }
        }
    }

    private void clearForm() {
        txtId.setText(""); txtNome.setText(""); txtPreco.setText(""); 
        txtEstoque.setText(""); txtVoltagem.setText("");
    }

    public static void main(String[] args) {
        factory = new Configuration().configure().buildSessionFactory();
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
