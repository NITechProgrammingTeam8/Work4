import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;


public class ChainGUI extends JFrame {
    ChainTable ctable;

    ChainGUI(String title) {
        setTitle(title);
        int appWidth = 1500;
        int appHeight = 700;
        setBounds(100, 100, appWidth, appHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    class MenuPanel extends JPanel implements ActionListener {
        JTextArea wmTA;
        JTextArea schTA;
        ArrayList<Rule> ruleList;
        DefaultListModel ruleMod;
        JList rulePnl;
        JLabel status;

        MenuPanel() {
            addWindowListener(new WindowAdapter() {
                public void windowOpened(WindowEvent e) {
                    udRuleMod();
                }
            });
            JPanel schPnl = new JPanel();

            wmTA = new JTextArea(10, 35);
            schTA = new JTextArea(3, 30);

            JButton schBtn = new JButton("検索");
            schBtn.addActionListener(this);

            schPnl.add(schTA);
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

            RuleEditor rp = new RuleEditor();

            editPnl.add(btnPnl);
            editPnl.add(re);

            status = new JLabel();
            Container contentPane = getContentPane();
            contentPane.add(schPnl, BorderLayout.NORTH);
            contentPane.add(editPnl, BorderLayout.CENTER);
            contentPane.add(status, BorderLayout.SOUTH);
        }

        void udRuleMod() {
            ruleList = ctable.getRules();
            ruleMod.clear();
            for (Rule r : ruleList) {
                ruleMod.addElement(r);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();

            if (cmd.equals("検索")) {
                ArrayList<String> astList = new ArrayList<>(Arrays.asList(wmTA.getText().split("¥n")));
                ArrayList<String> schAst = new ArrayList<>(Arrays.asList(schTA.getText().split("¥n")));

                ctable.schStep(astList, schAst);
            } else {
                if (cmd.equals("追加")) {
                    Rule r = new Rule(   );
                    ctable.addRule(val);
                }else if (cmd.equals("更新")) {
                    if (!listPanel.isSelectionEmpty()) {
                        int index = listPnl.getSelectedIndex();
                        Rule val = (Rule) listPnl.getSelectedValue();

                        ctable.udRule(val, , , )
                    } else {
                        System.out.println("更新失敗（未選択のため）");
                    }
                } else if (cmd.equals("削除")) {
                    if (!listPanel.isSelectionEmpty()) {
                        int index = listPnl.getSelectedIndex();
                        Rule val = (Rule) listPnl.getSelectedValue();
                        ctable.rmRule(val);
                    } else {
                        System.out.println("削除失敗（未選択のため）");
                    }
                } 
            }
            udRuleMod();
            status.setText(cmd + "の実行");
        }

        class RuleEditor extends JPanel implements ActionListener {
            Rule rule;
            JTextField nameTF;
            JTextArea ifTA;
            JTextField thenTF;

            RuleEditor() {
                add(new JLabel("Name"));
                nameTF= new JTextField(20);
                add(nameTF);
                add(new JLabel("if"));
                ifTA = new JTextArea(10, 20);
                add(ifTA);
                add(new JLabel("then"));
                thenTF = new JTextField(20);
                add(thenTF);

                JButton doneBtn = new JButton("完了");
                doneBtn.addActionListener(this);
                add(doneBtn);
            }

            RuleEditor(Rule rule) {
                this();
                this.rule = rule;

                nameTF.setText(rule.getName());
                StringBuilder sb = new StringBuilder();
                for(String s : rule.getAntecedents()) {
                    sb.append(s);
                    sb.append("\n");
                }
                ifTA.setText(sb.toString());
                thenTF.setText(rule.getConsequent());
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                String nameText = nameTF.getText();
                ArrayList<String> ifList = new ArrayList<>(Arrays.asList(ifTA.getText().split("¥n")));
                String thenText = thenTF.getText();
    
                if(rule == null) {
                    ctable.addRule(nameText, ifList, thenText);
                } else {
                    ctable.udRule(rule, )
                }
            }
        }
    }
}

abstract class ChainTable extends JPanel {

    ChainTable(String fileName) {
        SpringLayout layout = new SpringLayout();  // GridBagLayoutの方がいいかも
        setLayout(layout);
    }

    abstract ArrayList<Rule> getRules();

    abstract void schStep(ArrayList<String> astList, ArrayList<String> schAst);

    abstract void paintPnls(ArrayList<StepResult> srlist);  // パネル（解）の描写

    abstract void addRule(String nameText, ArrayList<String> ifList, String thenText);

    abstract void udRule(Rule rule, String name, ArrayList<String> antecedents, String consequent);

    abstract void rmRule(Rule rule);
}