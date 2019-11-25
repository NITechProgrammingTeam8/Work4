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
        int appHeight = 800;
        setBounds(100, 100, appWidth, appHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    class MenuPanel extends JPanel implements ActionListener {
        JTextArea wmTA;
        JTextField schTF;
        JPanel editPnl;
        ArrayList<Rule> ruleList;
        DefaultListModel ruleMod;
        JList rulePnl;
        RuleEditor re;
        JLabel status;

        MenuPanel() {
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

            addWindowListener(new WindowAdapter() {
                public void windowOpened(WindowEvent e) {
                    udRuleMod();
                }
            });

            JPanel schPnl = new JPanel();
            // GridLayout gl = new GridLayout();
            // gl.setRows(1);
            // schPnl.setLayout(gl);

            wmTA = new JTextArea(10, 25);
            schTF = new JTextField(20);

            JButton schBtn = new JButton("検索");
            schBtn.addActionListener(this);

            schPnl.add(wmTA);
            schPnl.add(schTF);
            schPnl.add(schBtn);

            editPnl = new JPanel();
            // editPnl.setLayout(gl);
            // gl.setRows(1);

            ruleMod = new DefaultListModel();
            rulePnl = new JList(ruleMod);
            JScrollPane ruleSp = new JScrollPane();
            ruleSp.getViewport().setView(rulePnl);
            ruleSp.setPreferredSize(new Dimension(500, 300));

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

            re = new RuleEditor();

            editPnl.add(ruleSp);
            editPnl.add(btnPnl);
            editPnl.add(re);

            status = new JLabel();
            add(schPnl);
            add(editPnl);
            add(status);
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
                ArrayList<String> astList = new ArrayList<>(Arrays.asList(wmTA.getText().split("\n")));
                // ArrayList<String> schAst = new ArrayList<>(Arrays.asList(schTA.getText().split("\n")));  //  複数の質問文のとき？
                ArrayList<String> schAst = new ArrayList<>();
                schAst.add(schTF.getText());

                ctable.schStep(astList, schAst);
            } else {
                if (cmd.equals("追加")) {
                    re.addRule();
                }else if (cmd.equals("更新")) {
                    if (!rulePnl.isSelectionEmpty()) {
                        int index = rulePnl.getSelectedIndex();
                        Rule val = (Rule) rulePnl.getSelectedValue();
                        re.udRule(val);
                    } else {
                        System.out.println("更新失敗（未選択のため）");
                    }
                } else if (cmd.equals("削除")) {
                    if (!rulePnl.isSelectionEmpty()) {
                        int index = rulePnl.getSelectedIndex();
                        Rule val = (Rule) rulePnl.getSelectedValue();
                        ctable.rmRule(val);
                    } else {
                        System.out.println("削除失敗（未選択のため）");
                    }
                }
            }
            status.setText(cmd + "の実行");
        }

        class RuleEditor extends JPanel implements ActionListener {
            Rule rule;
            JTextField nameTF;
            JTextArea ifTA;
            JTextField thenTF;

            RuleEditor() {
                setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

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

            @Override
            public void actionPerformed(ActionEvent e) {
                String nameText = nameTF.getText();
                ArrayList<String> ifList = new ArrayList<>(Arrays.asList(ifTA.getText().split("\n")));
                String thenText = thenTF.getText();
    
                if(rule == null) {
                    ctable.addRule(nameText, ifList, thenText);
                } else {
                    ctable.udRule(new Rule(nameText, ifList, thenText));
                }
                udRuleMod();
            }

            void addRule() {
                this.rule = null;

                nameTF.setText("");
                nameTF.setEditable(true);
                ifTA.setText("");
                thenTF.setText("");
            }

            void udRule(Rule rule) {
                this.rule = rule;

                nameTF.setText(rule.getName());
                nameTF.setEditable(false);
                StringBuilder sb = new StringBuilder();
                for(String s : rule.getAntecedents()) {
                    sb.append(s);
                    sb.append("\n");
                }
                ifTA.setText(sb.toString());
                thenTF.setText(rule.getConsequent());
            }
        }
    }
}

abstract class ChainTable extends JPanel {

    ChainTable(String fileName) {
        setLayout(null);
    }

    abstract ArrayList<Rule> getRules();

    abstract void schStep(ArrayList<String> astList, ArrayList<String> schAst);

    abstract void paintPnls(ArrayList<StepResult> srlist); // パネル（解）の描写

    abstract void addRule(String nameText, ArrayList<String> ifList, String thenText);

    abstract void udRule(Rule rule);

    abstract void rmRule(Rule rule);
}