import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public abstract class ChainTable extends JPanel {

    ChainTable(String fileName) {
        SpringLayout layout = new SpringLayout();  // GridBagLayoutの方がいいかも
        setLayout(layout);
    }

    abstract ArrayList<Rule> getRules();

    abstract boolean schStep(ArrayList<String> astList, String schAst);

    abstract void paintPnls(ArrayList<StepResult> srlist);  // パネル（解）の描写

    abstract boolean addRule(Rule rule);

    abstract boolean udRule(Rule rule, String name, ArrayList<String> antecedents, String consequent);

    abstract boolean rmRule(Rule rule);
}