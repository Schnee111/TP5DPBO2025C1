import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Menu extends JFrame {
    private int selectedIndex = -1;
    private Database database;

    private JLabel titleLabel, nimLabel, namaLabel, jenisKelaminLabel, gradeLabel;
    private JPanel mainPanel;
    private JTextField nimField, namaField;
    private JTable mahasiswaTable;
    private JButton addUpdateButton, cancelButton, deleteButton;
    private JComboBox<String> jenisKelaminComboBox, gradeComboBox;

    public static void main(String[] args) {
        Menu window = new Menu();
        window.setSize(480, 560);
        window.setLocationRelativeTo(null);
        window.setContentPane(window.mainPanel);
        window.getContentPane().setBackground(Color.white);
        window.setVisible(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Menu() {
        database = new Database();
        mahasiswaTable.setModel(setTable());

        String[] jenisKelaminData = {"Laki-laki", "Perempuan"};
        jenisKelaminComboBox.setModel(new DefaultComboBoxModel<>(jenisKelaminData));

        deleteButton.setVisible(false);

        addUpdateButton.addActionListener(e -> {
            if (selectedIndex == -1) {
                insertData();
            } else {
                updateData();
            }
        });

        deleteButton.addActionListener(e -> {
            if (selectedIndex >= 0) {
                deleteData();
            }
        });

        cancelButton.addActionListener(e -> clearForm());

        mahasiswaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedIndex = mahasiswaTable.getSelectedRow();
                nimField.setText(mahasiswaTable.getValueAt(selectedIndex, 1).toString());
                namaField.setText(mahasiswaTable.getValueAt(selectedIndex, 2).toString());
                jenisKelaminComboBox.setSelectedItem(mahasiswaTable.getValueAt(selectedIndex, 3).toString());
                gradeComboBox.setSelectedItem(mahasiswaTable.getValueAt(selectedIndex, 4).toString());
                addUpdateButton.setText("Update");
                deleteButton.setVisible(true);
            }
        });
    }

    private DefaultTableModel setTable() {
        Object[] column = {"No.", "NIM", "Nama", "Jenis Kelamin", "Grade"};
        DefaultTableModel temp = new DefaultTableModel(null, column);
        try {
            ResultSet rs = database.selectQuery("SELECT * FROM mahasiswa");
            int i = 1;
            while (rs.next()) {
                temp.addRow(new Object[]{i++, rs.getString("nim"), rs.getString("nama"), rs.getString("jenis_kelamin"), rs.getString("grade")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return temp;
    }

    private void insertData() {
        String nim = nimField.getText().trim();
        String nama = namaField.getText().trim();
        String jenisKelamin = (String) jenisKelaminComboBox.getSelectedItem();
        String grade = (String) gradeComboBox.getSelectedItem();

        if (nim.isEmpty() || nama.isEmpty() || jenisKelamin == null || grade == null) {
            JOptionPane.showMessageDialog(null, "Semua field harus diisi!");
            return;
        }

        try {
            ResultSet rs = database.selectQuery("SELECT * FROM mahasiswa WHERE nim = '" + nim + "'");
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "NIM sudah terdaftar!");
                return;
            }

            String sql = "INSERT INTO mahasiswa (nim, nama, jenis_kelamin, grade) VALUES ('" + nim + "', '" + nama + "', '" + jenisKelamin + "', '" + grade + "')";
            database.insertUpdateDeleteQuery(sql);
            mahasiswaTable.setModel(setTable());
            clearForm();
            JOptionPane.showMessageDialog(null, "Data berhasil ditambah!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    private void updateData() {
        String nim = nimField.getText().trim();
        String nama = namaField.getText().trim();
        String jenisKelamin = (String) jenisKelaminComboBox.getSelectedItem();
        String grade = (String) gradeComboBox.getSelectedItem();

        if (nim.isEmpty() || nama.isEmpty() || jenisKelamin == null || grade == null) {
            JOptionPane.showMessageDialog(null, "Semua field harus diisi!");
            return;
        }

        String sql = "UPDATE mahasiswa SET nama='" + nama + "', jenis_kelamin='" + jenisKelamin + "', grade='" + grade + "' WHERE nim='" + nim + "'";
        database.insertUpdateDeleteQuery(sql);
        mahasiswaTable.setModel(setTable());
        clearForm();
        JOptionPane.showMessageDialog(null, "Data berhasil diperbarui!");
    }

    private void deleteData() {
        int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String nim = nimField.getText();
            String sql = "DELETE FROM mahasiswa WHERE nim='" + nim + "'";
            database.insertUpdateDeleteQuery(sql);
            mahasiswaTable.setModel(setTable());
            clearForm();
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
        }
    }

    private void clearForm() {
        nimField.setText("");
        namaField.setText("");
        jenisKelaminComboBox.setSelectedIndex(0);
        gradeComboBox.setSelectedIndex(0);
        addUpdateButton.setText("Add");
        deleteButton.setVisible(false);
        selectedIndex = -1;
    }
}