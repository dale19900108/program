package com.dabaicong.program;

import javax.swing.*;  
import javax.swing.text.DefaultHighlighter;  
import javax.swing.text.BadLocationException;  
import java.awt.BorderLayout;  
import java.awt.Font;  
import java.awt.HeadlessException;  
import java.awt.Color;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.util.regex.Pattern;  
import java.util.regex.Matcher;  
   
/** 
 * 测试 Java 正则表达式 
 */  
public class RegTest {  
   
    public static void main(String[] args) {  
        try {  
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
   
        new RegTesterFrame().setVisible(true);  
    }  
}  
   
class RegTesterFrame extends JFrame {  
   
    private JTextField textbox = new JTextField();  // 包含测试文本的文本域  
       
    private JTextArea textarea = new JTextArea();   // 包含正则表达式的文本框  
   
    private RegTesterFrame thisFrame = this;  
       
    private DefaultHighlighter.DefaultHighlightPainter highlightPainter =  
            new DefaultHighlighter.DefaultHighlightPainter(Color.green);  
   
    // 构造方法  
    RegTesterFrame() throws HeadlessException {  
        super("测试 Java 正则表达式");  
        setSize(500, 300);  
        setLocation(300, 100);  
        setDefaultCloseOperation(EXIT_ON_CLOSE);  
   
        setupComponents();  
    }  
   
    // 初始化控件  
    private void setupComponents() {  
        setupBorder();  
   
        JPanel topPanel = new JPanel(new BorderLayout(5, 0));  
        this.add(topPanel, BorderLayout.NORTH);  
        setupTopPanel(topPanel);  
   
        JPanel centerPanel = new JPanel(new BorderLayout());  
        this.add(centerPanel, BorderLayout.CENTER);  
        setupCenterPanel(centerPanel);  
    }  
   
    // 给窗体面板加上边框  
    private void setupBorder() {  
        JPanel panel = new JPanel(new BorderLayout(5, 5));  
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));  
        this.setContentPane(panel);  
    }  
   
    // 文本域面板  
    private void setupCenterPanel(JPanel panel) {  
        textarea.setFont(new Font(Font.DIALOG_INPUT, 0, 12));  
        panel.add(new JScrollPane(textarea));  
    }  
   
    // 上方面板  
    private void setupTopPanel(JPanel panel) {  
        textbox.setFont(new Font(Font.DIALOG_INPUT, 0, 12));  
        panel.add(textbox, BorderLayout.CENTER);  
   
        JButton button = new JButton("测试");  
        button.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                checkRegularExpression();  
            }  
        });  
        panel.add(button, BorderLayout.EAST);  
        panel.add(new JLabel("正则表达式："), BorderLayout.WEST);  
    }  
   
    // 检查正则表达式  
    private void checkRegularExpression() {  
        textarea.getHighlighter().removeAllHighlights();  
   
        String reg = textbox.getText();  
        String text = textarea.getText();  
   
        Pattern p = Pattern.compile(reg);  
        Matcher m = p.matcher(text);  
        if (m.find()) {  
            thisFrame.setTitle("文本符合表达式。");  
            highlightMatches(m);  
        } else {  
            thisFrame.setTitle("文本不符合表达式。");  
        }  
    }  
   
    // 将符合正则表达式的部分高亮显示  
    private void highlightMatches(Matcher m) {  
        highlight(m);  
        int start = m.end();  
        while (m.find(start)) {  
            highlight(m);  
            start = m.end();  
        }  
    }  
   
    private void highlight(Matcher m) {  
        try {  
            textarea.getHighlighter().addHighlight(m.start(), m.end(), highlightPainter);  
        } catch (BadLocationException e) {  
            e.printStackTrace();  
        }  
    }  
} 
