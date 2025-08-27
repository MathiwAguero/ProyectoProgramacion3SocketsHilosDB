package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {
    private JPanel Login;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JTextField textField1;
    private JTextField textField2;

    public JPanel getPanel() {return Login;}

    public void setPanel(JPanel Login) {this.Login = Login;}

    public JPanel getLogin() {return Login;}

    public JButton getButton1() {return button1;}

    public JButton getButton2() {return button2;}

    public void setButton2(JButton button2) {this.button2 = button2;}

    public JButton getButton3() {return button3;}

    public void setButton3(JButton button3) {this.button3 = button3;}

    public JTextField getTextField1() {return textField1;}

    public void setTextField1(JTextField textField1) {this.textField1 = textField1;}

    public JTextField getTextField2() {return textField2;}

    public void setTextField2(JTextField textField2) {this.textField2 = textField2;}

    public void setButton1(JButton button1) {this.button1 = button1;}

    public void setLogin(JPanel login) {Login = login;}

}
