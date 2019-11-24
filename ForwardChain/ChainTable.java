import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public abstract class ChainTable extends JPanel {
    View view;
    Presenter pres;

    ChainTable(String fileName) {
        view = new View();
        pres = new Presenter(view);
        pres.start(new ArrayList<String>(), "CarShop.data");

        SpringLayout layout = new SpringLayout();  // GridBagLayoutの方がいいかも
        setLayout(layout);
    }

    ArrayList<Rule> getRules() {
        // pres.fetchRules();  // 合わせる
        // return view.showRuleList();
        return null;
    }

    abstract boolean schStep(ArrayList<String> astList, String schAst);

    abstract void genPnls(ArrayList<StepResult> srlist);  // パネル（解）の描写

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

class FwdChainTable extends ChainTable {
    HashMap<StepResult, StepPanel> stepMap;

    FwdChainTable(String fileName) {
        super(fileName);
        stepMap = new HashMap<>();
    }

    @Override
    boolean schStep(ArrayList<String> astList, String schAst) {  // schAst: 前向き推論時はnullも許容したい
        ArrayList<StepResult> stepList = pres.restart(astList);
        genPnls(stepList);
        
        if(schAst.equals("")) {
            System.out.println("WARNING: 検索文の格納に失敗");  // 後向き推論だったらここで止める
        } else {
            pres.searchAssertion(schAst);
            ArrayList<StepResult> resList = view.showSearchAssertion();
            paintPnls(resList);
        }
        return true;
    }

    @Override
    void genPnls(ArrayList<StepResult> srlist) {
        stepMap.clear();
        for(StepResult sr : srlist) {
            StepPanel sp = new StepPanel(sr);
            stepMap.put(sr, sp);

            // gridbagpaneにして，相対位置はStepPanelのフィールドに保持するか
            add(sp);

            // EdgePanel ep = n.getEdges();  // 各ノードは，リンクをどこから来たかだけを所有する？（どこに繋がってゆくかは気にしない）
            // // layout.putConstraint(, ep, , , , );  // SpringLayoutだとこれの定義に条件分岐が必要そう
            // add(ep);
        }
    }
    
    void repaintPnls(ArrayList<StepResult> reslist) {  // 前向き推論においては，PaintPnlsと別に描写の更新が先にある
        for (StepResult res : reslist) {
            stepMap.get(res).passing();
            
        }
    }
}

class BwdChainTable extends ChainTable {

    BwdChainTable(String fileName) {
        super(fileName);
    }
    
    @Override
    boolean schStep(ArrayList<String> astList, String schAst) {  // schAst: 後向き推論時nullだとfalse
        if(schAst != null) {
            // // ArrayList<StepResult> stepList = pres.restart(astList, schAst);
            // ArrayList<StepResult> stepList = pres.restart(astList);
            // paintPnls(stepList);
            return true;
        } else {
            return false;
        }
    }

    @Override
    void genPnls(ArrayList<StepResult> slist) {
        // for (StepResult s : slist) {
        //     StepPanel sp = new StepPanel(s);
            
        //     layout.putConstraint(, , , , );
        //     add(sp);

        //     EdgePanel ep = s.getEdges();  // 各ノードは，リンクをどこから来たかだけを所有する？（どこに繋がってゆくかは気にしない）
        //     // layout.putConstraint(, ep, , , , );  // SpringLayoutだとこれの定義に条件分岐が必要そう
        //     add(ep);
        // }
    }
}