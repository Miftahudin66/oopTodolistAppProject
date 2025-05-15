
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class TodoApp extends JFrame {

    private DefaultListModel<String> todoListModel;
    private JList<String> todoList;
    private JTextField inputField;
    private final File saveFile = new File("todo_data.txt");

    public TodoApp() {
        setTitle("Todo List App");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        todoListModel = new DefaultListModel<>();
        todoList = new JList<>(todoListModel);
        inputField = new JTextField();

        loadTodosFromFile();

        JScrollPane scrollPane = new JScrollPane(todoList);

        JButton addButton = new JButton("Tambah");
        JButton deleteButton = new JButton("Hapus");
        JButton editButton = new JButton("Edit");

        addButton.addActionListener(e -> addTodo());
        deleteButton.addActionListener(e -> deleteTodo());
        editButton.addActionListener(e -> editTodo());

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addTodo() {
        String text = inputField.getText().trim();
        if (!text.isEmpty()) {
            todoListModel.addElement(text);
            inputField.setText("");
            saveTodosToFile();
        }
    }

    private void deleteTodo() {
        int selected = todoList.getSelectedIndex();
        if (selected != -1) {
            todoListModel.remove(selected);
            saveTodosToFile();
        }
    }

    private void editTodo() {
        int selected = todoList.getSelectedIndex();
        if (selected != -1) {
            String currentText = todoListModel.get(selected);
            String newText = JOptionPane.showInputDialog(this, "Edit Todo:", currentText);
            if (newText != null && !newText.trim().isEmpty()) {
                todoListModel.set(selected, newText.trim());
                saveTodosToFile();
            }
        }
    }

    private void loadTodosFromFile() {
        if (saveFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    todoListModel.addElement(line);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Gagal memuat data", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveTodosToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
            for (int i = 0; i < todoListModel.size(); i++) {
                writer.write(todoListModel.get(i));
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TodoApp().setVisible(true));
    }
}
