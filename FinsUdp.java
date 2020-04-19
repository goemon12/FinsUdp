
import java.awt.event.*;
import java.net.*;

public class FinsUdp implements ActionListener, Runnable {
    MyFrame frm;
    InetAddress host;
    int port;
    int chName;
    int chAddr;
    int chLeng;
    DatagramSocket socket;
    DatagramPacket sndPkt;
    DatagramPacket rcvPkt;
    byte[] sbuf = new byte[1000];
    byte[] rbuf = new byte[1000];

    FinsUdp() {
        frm = new MyFrame("FINS/UDPデモ");
        frm.btnOpen.addActionListener(this);
    }

    public static void main(String[] args) {
        new FinsUdp();
    }

    public void actionPerformed(ActionEvent e) {
        if (frm.btnOpen.isSelected()) {
            Thread t = new Thread(this);
            t.start();
        }
    }

    public void run() {
        if (openFins() < 0) {
            frm.btnOpen.setSelected(false);
        }
        else {
            int cnt = 0;
            while (frm.btnOpen.isSelected()) {
                String txt = String.format("通信してます(%04d)\n\n", cnt);
                if (sendFins() < 0) {
                    frm.btnOpen.setSelected(false);
                }
                else {
                    txt += "送信:";
                    for (int i = 0; i < sndPkt.getLength(); i++) {
                        txt += String.format(" %02x", sbuf[i]);
                    }
                    txt += "\n";
                    if (recvFins() < 0) {
                        frm.btnOpen.setSelected(false);
                    }
                    else {
                        txt += "受信:";
                        for (int i = 0; i < rcvPkt.getLength(); i++) {
                            txt += String.format(" %02x", rbuf[i]);
                        }
                        txt += "\n\n";
                        for (int i = 0; i < 10; i++) {
                            txt += frm.cmbChName.getSelectedItem();
                            txt += String.format(" %04d", chAddr + i);
                            txt += String.format(" %02x%02x\n", rbuf[i * 2 + 14], rbuf[i * 2 + 15]);
                        }
                        txt += "\n\n";
                    }
                }
                frm.txtArea.setText(txt);
                System.out.print(txt);
                cnt = (cnt + 1) % 10000;
            }
            socket.close();
            frm.txtArea.append("切断しました\n");
        }
    }

    int openFins() {
        try {
            host = InetAddress.getByName(frm.txtHost.getText());
        } catch (Exception e) {
            return -1;
        }

        port = Integer.parseInt(frm.txtPort.getText());

        try {
            socket = new DatagramSocket(port);
        } catch (Exception e) {
            return -2;
        }

        try {
            socket.setSoTimeout(3000);
        } catch (Exception e) {
            return -3;
        }

        int[] tmp = {0xb0, 0x82};
        chName = tmp[frm.cmbChName.getSelectedIndex()];
        chAddr = Integer.parseInt(frm.txtChAddr.getText());
        chLeng = Integer.parseInt(frm.txtChLeng.getText());

        return 0;
    }

    int sendFins() {
        sbuf[ 0] = (byte)0x80;
        sbuf[ 1] = (byte)0x00;
        sbuf[ 2] = (byte)0x02;
        sbuf[ 3] = (byte)0x00;//DNA
        sbuf[ 4] = (byte)0x01;//DA1
        sbuf[ 5] = (byte)0x00;//DA2
        sbuf[ 6] = (byte)0x00;//SNA
        sbuf[ 7] = (byte)0x02;//SA1
        sbuf[ 8] = (byte)0x00;//SA2
        sbuf[ 9] = (byte)0x12;//SID
        sbuf[10] = (byte)0x01;//MRC
        sbuf[11] = (byte)0x01;//SRC
        sbuf[12] = (byte)(chName % 0x100);
        sbuf[13] = (byte)(chAddr / 0x100);
        sbuf[14] = (byte)(chAddr % 0x100);
        sbuf[15] = (byte)0x00;
        sbuf[16] = (byte)(chLeng / 0x100);
        sbuf[17] = (byte)(chLeng % 0x100);

        sndPkt = new DatagramPacket(sbuf, 18, host, port);

        try {
            socket.send(sndPkt);
        } catch (Exception e) {
            return -1;
        }

        return 0;
    }

    int recvFins() {
        rcvPkt = new DatagramPacket(rbuf, 1000);

        try {
            socket.receive(rcvPkt);
        } catch (Exception e) {
            return -1;
        }

        return 0;
    }

}
