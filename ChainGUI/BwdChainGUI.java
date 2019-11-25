import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class BwdChainGUI extends ChainGUI {
    public static void main(String args[]) {
        BwdChainGUI frame = new BwdChainGUI("後向き推論", "CarShop.data");
        frame.setVisible(true);
    }

    BwdChainGUI(String title, String ruleFileName) {
        super(title);
        ctable = new BwdChainTable(ruleFileName);
        JPanel mainPnl = ctable;
        JPanel menuPnl = new MenuPanel();

        Container contentPane = getContentPane();
        contentPane.add(mainPnl, BorderLayout.CENTER);
        contentPane.add(menuPnl, BorderLayout.WEST);
    }
}

class BwdChainTable extends ChainTable {
    View view;
    Presenter pres;

    BwdChainTable(String fileName) {
        super(fileName);
        view = new View();
        pres = new Presenter(view);
        pres.start(fileName);
    }

    ArrayList<Rule> getRules() {
        return pres.fetchRules();
    }
    
    @Override
    boolean schStep(ArrayList<String> astList, String schAst) {  // schAst: 後向き推論時nullだとfalse
        if(schAst != null) {
            ArrayList<StepResult> stepList = pres.stepResults(astList, schAst);
            paintPnls(stepList);
            return true;
        } else {
            return false;
        }
    }

    @Override
    void paintPnls(ArrayList<StepResult> slist) {
        // for (StepResult s : slist) {
        //     StepPanel sp = new StepPanel(s);
            
        //     layout.putConstraint(, , , , );
        //     add(sp);

        //     EdgePanel ep = s.getEdges();  // 各ノードは，リンクをどこから来たかだけを所有する？（どこに繋がってゆくかは気にしない）
        //     // layout.putConstraint(, ep, , , , );  // SpringLayoutだとこれの定義に条件分岐が必要そう
        //     add(ep);
        // }
    }

    boolean addRule(Rule rule) {
        return false;
    }

    boolean udRule(Rule rule, String name, ArrayList<String> antecedents, String consequent) {
        return false;
    }

    boolean rmRule(Rule rule) {
        return false;
    }
}