import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator extends JFrame implements ActionListener {
    private JTextField display;
    private String operator;
    private double num1, num2, result;
    private boolean startNewNumber;

    public Calculator() {
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLayout(new BorderLayout(10, 10));
        
        // Display field
        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 32));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.WHITE);
        add(display, BorderLayout.NORTH);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        String[] buttons = {
            "C", "CE", "←", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "±", "0", ".", "="
        };
        
        for (String text : buttons) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("Arial", Font.BOLD, 20));
            btn.addActionListener(this);
            
            if (text.equals("=")) {
                btn.setBackground(new Color(0, 120, 215));
                btn.setForeground(Color.WHITE);
            } else if (text.matches("[+\\-*/]")) {
                btn.setBackground(new Color(240, 240, 240));
            } else if (text.equals("C") || text.equals("CE") || text.equals("←")) {
                btn.setBackground(new Color(255, 200, 200));
            }
            
            buttonPanel.add(btn);
        }
        
        add(buttonPanel, BorderLayout.CENTER);
        
        operator = "";
        num1 = 0;
        num2 = 0;
        startNewNumber = true;
        
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        
        try {
            if (cmd.matches("[0-9]")) {
                handleNumber(cmd);
            } else if (cmd.equals(".")) {
                handleDecimal();
            } else if (cmd.matches("[+\\-*/]")) {
                handleOperator(cmd);
            } else if (cmd.equals("=")) {
                calculate();
            } else if (cmd.equals("C")) {
                clear();
            } else if (cmd.equals("CE")) {
                clearEntry();
            } else if (cmd.equals("←")) {
                backspace();
            } else if (cmd.equals("±")) {
                toggleSign();
            }
        } catch (Exception ex) {
            display.setText("Error");
            startNewNumber = true;
        }
    }
    
    private void handleNumber(String num) {
        if (startNewNumber) {
            display.setText(num);
            startNewNumber = false;
        } else {
            if (display.getText().equals("0")) {
                display.setText(num);
            } else {
                display.setText(display.getText() + num);
            }
        }
    }
    
    private void handleDecimal() {
        if (startNewNumber) {
            display.setText("0.");
            startNewNumber = false;
        } else if (!display.getText().contains(".")) {
            display.setText(display.getText() + ".");
        }
    }
    
    private void handleOperator(String op) {
        if (!operator.isEmpty() && !startNewNumber) {
            calculate();
        }
        num1 = Double.parseDouble(display.getText());
        operator = op;
        startNewNumber = true;
    }
    
    private void calculate() {
        if (operator.isEmpty()) return;
        
        num2 = Double.parseDouble(display.getText());
        
        switch (operator) {
            case "+":
                result = num1 + num2;
                break;
            case "-":
                result = num1 - num2;
                break;
            case "*":
                result = num1 * num2;
                break;
            case "/":
                if (num2 == 0) {
                    display.setText("Cannot divide by 0");
                    startNewNumber = true;
                    operator = "";
                    return;
                }
                result = num1 / num2;
                break;
        }
        
        display.setText(formatResult(result));
        num1 = result;
        operator = "";
        startNewNumber = true;
    }
    
    private String formatResult(double val) {
        if (val == (long) val) {
            return String.valueOf((long) val);
        }
        return String.valueOf(val);
    }
    
    private void clear() {
        display.setText("0");
        num1 = 0;
        num2 = 0;
        operator = "";
        startNewNumber = true;
    }
    
    private void clearEntry() {
        display.setText("0");
        startNewNumber = true;
    }
    
    private void backspace() {
        String text = display.getText();
        if (text.length() > 1) {
            display.setText(text.substring(0, text.length() - 1));
        } else {
            display.setText("0");
            startNewNumber = true;
        }
    }
    
    private void toggleSign() {
        double val = Double.parseDouble(display.getText());
        val = -val;
        display.setText(formatResult(val));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Calculator());
    }
}