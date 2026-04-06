package com.exemplo;

import com.exemplo.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class Main extends JFrame {
    private static SessionFactory factory;
    private JTable table;
    private DefaultTableModel tableModel;

    public Main() {
        // 1. Setup Window (dwl/Wayland friendly)
        setTitle("Sistema MoR - Protótipo");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 2. Table to show Data
        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Preço", "Estoque"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 3. Form Panel
        JPanel panel = new JPanel(new GridLayout(5, 2));
        JTextField txtNome = new JTextField();
        JTextField txtPreco = new JTextField();
        JTextField txtEstoque = new JTextField();
        JTextField txtVoltagem = new JTextField();
        JButton btnSave = new JButton("Salvar Eletrônico");

        panel.add(new JLabel("Nome:")); panel.add(txtNome);
        panel.add(new JLabel("Preço:")); panel.add(txtPreco);
        panel.add(new JLabel("Estoque:")); panel.add(txtEstoque);
        panel.add(new JLabel("Voltagem:")); panel.add(txtVoltagem);
        panel.add(btnSave);

        add(panel, BorderLayout.SOUTH);

        // 4. Save Action
        btnSave.addActionListener(e -> {
            saveProduct(txtNome.getText(), txtPreco.getText(), txtEstoque.getText(), txtVoltagem.getText());
            refreshTable();
        });

        refreshTable();
    }

    private void saveProduct(String nome, String preco, String estoque, String voltagem) {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            ProdutoEletronico p = new ProdutoEletronico();
            p.setNome(nome);
            p.setPreco(Double.parseDouble(preco));
            p.setEstoque(Integer.parseInt(estoque));
            p.setVoltagem(voltagem);
            session.persist(p);
            session.getTransaction().commit();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        try (Session session = factory.openSession()) {
            List<Produto> list = session.createQuery("from Produto", Produto.class).list();
            for (Produto p : list) {
                tableModel.addRow(new Object[]{p.getId(), p.getNome(), p.getPreco(), p.getEstoque()});
            }
        }
    }

    public static void main(String[] args) {
        factory = new Configuration().configure().buildSessionFactory();
        // Run the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
