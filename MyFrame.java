
import java.awt.*;
import javax.swing.*;

public class MyFrame extends JFrame {
    JTextField txtHost;
    JTextField txtPort;
    JTextArea txtArea;
    JComboBox cmbChName;
    JTextField txtChAddr;
    JTextField txtChLeng;
    JScrollPane sclPane;
    JToggleButton btnOpen;
    
    MyFrame(String title) {
        super(title);
        
        JPanel pnl1 = new JPanel();
        JPanel pnl2 = new JPanel();
        pnl1.setLayout(new GridLayout(5, 2));
        pnl2.setLayout(new FlowLayout());
        
        txtHost = new JTextField("192.168.250.1");
        txtPort = new JTextField("9600");
        String strChName[] = {"CIO", "DM"};
        cmbChName = new JComboBox(strChName);
        txtChAddr = new JTextField("100");
        txtChLeng = new JTextField("10");
        txtArea = new JTextArea();
        sclPane = new JScrollPane(txtArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        btnOpen = new JToggleButton("接続");
        
        txtArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        txtArea.setLineWrap(true);
        txtArea.setWrapStyleWord(true);
        
        pnl1.add(new JLabel("IP アドレス"));
        pnl1.add(txtHost);
        pnl1.add(new JLabel("ポート NO"));
        pnl1.add(txtPort);
        pnl1.add(new JLabel("CH 名前"));
        pnl1.add(cmbChName);
        pnl1.add(new JLabel("CH 番号"));
        pnl1.add(txtChAddr);
        pnl1.add(new JLabel("CH 数"));
        pnl1.add(txtChLeng);        
        pnl2.add(btnOpen);        

        this.add(pnl1, BorderLayout.NORTH);
        this.add(sclPane, BorderLayout.CENTER);
        this.add(pnl2, BorderLayout.SOUTH);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(320, 480);
        this.setVisible(true);
        this.setLocation(100, 100);
    }
    
}
