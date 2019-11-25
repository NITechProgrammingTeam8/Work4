import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;


public class FwdChainGUI extends ChainGUI {
    public static void main(String args[]) {
        FwdChainGUI frame = new FwdChainGUI("前向き推論", "CarShop.data");
        frame.setVisible(true);
    }

    FwdChainGUI(String title, String ruleFileName) {
        super(title);
        ctable = new FwdChainTable(ruleFileName);
        JPanel mainPnl = ctable;
        JPanel menuPnl = new MenuPanel();

        Container contentPane = getContentPane();
        contentPane.add(mainPnl, BorderLayout.CENTER);
        contentPane.add(menuPnl, BorderLayout.WEST);
    }
}

class FwdChainTable extends ChainTable {
    View view;
    Presenter pres;
    HashMap<StepResult, StepPanel> stepMap;

    FwdChainTable(String fileName) {
        super(fileName);
        view = new View();
        pres = new Presenter(view);
        pres.start(new ArrayList<String>(), fileName);
        stepMap = new HashMap<>();
    }

    ArrayList<Rule> getRules() {
        return pres.fetchRules();
    }

    @Override
    void schStep(ArrayList<String> astList,  ArrayList<String> schAst) {  // schAst: 前向き推論時はnullも許容したい
        pres.restart(astList);
        ArrayList<StepResult> stepList = pres.stepResult();
        paintPnls(stepList);
        
        if(schAst.equals("")) {
            System.out.println("WARNING: 検索文の格納に失敗");
        } else {
            pres.searchAssertion(schAst);
            ArrayList<StepResult> resList = view.showSearchAssertion();
            rePaintPnls(resList);
        }
    }

    @Override
    void paintPnls(ArrayList<StepResult> srlist) {
        stepMap.clear();
        for(StepResult sr : srlist) {
            StepPanel sp = new StepPanel(sr);
            for(Assertion ast : sr.getAdd()) {
                // 自分へのedgeを生やす．ここでやるか一通りspを作った後にするか
            }
            stepMap.put(sr, sp);

            // gridbagpaneにして，相対位置はStepPanelのフィールドに保持するか
            add(sp);

            // EdgePanel ep = n.getEdges();  // 各ノードは，リンクをどこから来たかだけを所有する？（どこに繋がってゆくかは気にしない）
            // // layout.putConstraint(, ep, , , , );  // SpringLayoutだとこれの定義に条件分岐が必要そう
            // add(ep);
        }
    }
    
    void repaintPnls(ArrayList<StepResult> reslist) {  // 前向き推論においては，PaintPnlsと別に描写の更新が後にある
        for (StepResult res : reslist) {
            stepMap.get(res).passing();
            
        }
    }

    void addRule(String nameText, ArrayList<String> ifList, String thenText) {
        pres.addRule(nameText, ifList, thenText);
    }

    void udRule(Rule rule, String name, ArrayList<String> antecedents, String consequent) {
        return false;
    }

    void rmRule(Rule rule) {
        return false;
    }
}