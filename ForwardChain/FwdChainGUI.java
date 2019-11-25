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
    HashMap<StepResult, EdgePanel> edgeMap;

    FwdChainTable(String fileName) {
        super(fileName);
        view = new View();
        pres = new Presenter(view);
        pres.start(new ArrayList<String>(), fileName);
        stepMap = new HashMap<>();
        edgeMap = new HashMap<>();
    }

    ArrayList<Rule> getRules() {
        return pres.fetchRules();
    }

    @Override
    void schStep(ArrayList<String> astList, ArrayList<String> schAst) { // schAst: 前向き推論時はnullも許容したい
        pres.restart(astList);
        ArrayList<StepResult> stepList = pres.stepResult();
        paintPnls(stepList);

        if (schAst.get(0).equals("")) {  // 複数の質問文は非対応
            System.out.println("WARNING: 検索文の格納に失敗");
        } else {
            ArrayList<ArrayList<SearchStep>> resList = pres.searchAssertion(schAst); // 型は[質問のリスト]<[質問に対する複数の回答<[回答の導出]>]>，Whatで問われる質問は回答が複数ある場合があるから
            repaintPnls(resList.get(0)); // 複数の質問文は非対応
        }
    }

    @Override
    void paintPnls(ArrayList<StepResult> srList) {
        stepMap.clear();
        edgeMap.clear();
        for (StepResult sr : srList) {
            StepPanel sp = new StepPanel(sr);
            stepMap.put(sr, sp);
            // gridbagpaneにして，相対位置はStepPanelのフィールドに保持するか
            add(sp);

            for (StepResult from : sr.getAddSR()) {
                StepPanel fromPnl = stepMap.get(from);
                EdgePanel ep = new EdgePanel(fromPnl, sp);
                edgeMap.put(sr, ep);  // 各StepPanelは，リンク(EdgePanel)をどこから来たかだけを所有する？（どこに繋がってゆくかは気にしない）
                // layout.putConstraint(, ep, , , , ); // SpringLayoutだとこれの定義に条件分岐が必要そう
                add(ep);
            }

        }
    }

    void repaintPnls(ArrayList<SearchStep> resList) { // 前向き推論においては，PaintPnlsと別に描写の更新が後にある
        // for (SearchStep solve : resList) { // solveを切り替えて表示する必要がある．すなわちCardLayout
        // for(StepResult sp : solve.getKeiro()) {
        // stepMap.get(sp).passing();
        // }
        // }

        SearchStep solve = resList.get(0);
        for (StepResult sp : solve.getKeiro()) {
            edgeMap.get(sp).passing();
        }
    }

    void addRule(String nameText, ArrayList<String> ifList, String thenText) {
        pres.addRule(nameText, ifList, thenText);
    }

    void udRule(Rule rule) {
        pres.updateRule(rule);
    }

    void rmRule(Rule rule) {
        pres.deleteRule(rule);
    }
}