import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class FwdChainGUI extends JFrame {
    FwdChainTable fct;

    public static void main(String args[]) {
        FwdChainGUI frame = new FwdChainGUI("前向き推論");
        frame.setVisible(true);
    }

    FwdChainGUI(String title) {

        setTitle(title);
        int appWidth = 1500;
        int appHeight = 700;
        setBounds(100, 100, appWidth, appHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        fct = new FwdChainTable("CarShop.data");
        JPanel mainPnl = fct;
        JPanel menuPnl = new MenuPanel();

        Container contentPane = getContentPane();
        contentPane.add(mainPnl, BorderLayout.CENTER);
        contentPane.add(menuPnl, BorderLayout.WEST);
    }

    class MenuPanel extends JPanel implements ActionListener {
        JTextArea wmTexts;
        JTextField schText;
        List<Rule> ruleList;
        DefaultListModel ruleMod;
        JList rulePnl;
        JLabel status;

        MenuPanel() {
            addWindowListener(new WindowAdapter() {
                public void windowOpened(WindowEvent e) {
                    // ruleList = fct.getRules();
                    // for (Rule r : ruleList) {
                    //     ruleMod.addElement(r);
                    // }
                }
            });
            JPanel schPnl = new JPanel();

            wmTexts = new JTextArea(10, 35);
            schText = new JTextField(20);

            JButton schBtn = new JButton("検索");
            schBtn.addActionListener(this);

            schPnl.add(wmTexts);
            schPnl.add(schText);
            schPnl.add(schBtn);

            JPanel editPnl = new JPanel();
            
            ruleMod = new DefaultListModel();
            rulePnl = new JList(ruleMod);
            JScrollPane ruleSp = new JScrollPane();
            ruleSp.getViewport().setView(rulePnl);
            ruleSp.setPreferredSize(new Dimension(200, 300));

            JPanel btnPnl = new JPanel();
            JButton addBtn = new JButton("追加");
            addBtn.addActionListener(this);
            btnPnl.add(addBtn);
            JButton udBtn = new JButton("更新");
            udBtn.addActionListener(this);
            btnPnl.add(udBtn);
            JButton rmBtn = new JButton("削除");
            rmBtn.addActionListener(this);
            btnPnl.add(rmBtn);

            editPnl.add(btnPnl);

            status = new JLabel();
            Container contentPane = getContentPane();
            contentPane.add(schPnl, BorderLayout.NORTH);
            contentPane.add(editPnl, BorderLayout.CENTER);
            contentPane.add(status, BorderLayout.SOUTH);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();

            String param = "失敗";
            if (cmd.equals("検索")) {
                String[] arg = wmTexts.getText().split("¥n");
                ArrayList<String> astList = new ArrayList<>(Arrays.asList(arg));

                String schAst = schText.getText();

                fct.schStep(astList, schAst);
                param = "実行";
            } else {
                // if (cmd.equals("追加")) {
                //     Rule r = new Rule(   );
                //     if (fct.addRule(val)) {
                //         ruleMod.addElement(r);
                //         param = "成功";
                //     }
                // }else if (cmd.equals("更新")) {
                //     if (!listPanel.isSelectionEmpty()) {
                //         int index = listPnl.getSelectedIndex();
                //         Rule val = (Rule) listPnl.getSelectedValue();

                //         // 更新処理

                //         if (fct.udRule(val, , , )) {
                //             model.set(index, r);  // Ruleインスタンス自体が書き換わっているが，これは必要？
                //             param = "成功";
                //         }
                //     } else {
                //         System.out.println("更新失敗（未選択のため）");
                //     }
                // } else if (cmd.equals("削除")) {
                //     if (!listPanel.isSelectionEmpty()) {
                //         int index = listPnl.getSelectedIndex();
                //         Rule val = (Rule) listPnl.getSelectedValue();
                //         if (fct.rmRule(val)) {
                //             ruleMod.remove(index);
                //             param = "成功";
                //         }
                //     } else {
                //         System.out.println("削除失敗（未選択のため）");
                //     }
                // } 
            }
            status.setText(cmd + "の" + param);
        }
    }
}