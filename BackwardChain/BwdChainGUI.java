import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class BwdChainGUI extends ChainGUI {
    public static void main(String args[]) {
        BwdChainGUI frame = new BwdChainGUI("後向き推論", "CarShop2.data");
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
    HashMap<StepResult, StepPanel> stepMap;
    HashMap<StepResult, EdgePanel> edgeMap;
    HashMap<Integer, Integer> locMap;
    int next;

    BwdChainTable(String fileName) {
        super(fileName);
        view = new View();
        pres = new Presenter(view);
        pres.start(fileName);
        stepMap = new HashMap<>();
        edgeMap = new HashMap<>();
        locMap = new HashMap<>();
        next = 200;
    }

    ArrayList<Rule> getRules() {
        return pres.fetchRules();
    }
    
    @Override
    void schStep(ArrayList<String> astList, ArrayList<String> schAst) {  // schAst: 後向き推論時nullだとfalse
        removeAll();
        
        if (schAst.get(0).equals("")) { // 複数の質問文は非対応
            System.out.println("ERROR: 検索文の格納に失敗");
        } else {
            ArrayList<SearchStep> resList = pres.stepResults("CarShopWm.data", schAst.get(0));  // そもそもPresenter自体が複数の質問文は非対応
            answerPnls(resList);
        }
    }

    @Override
    void answerPnls(ArrayList<SearchStep> srList) {
        stepMap.clear();
        edgeMap.clear();
        locMap.clear();

        for (StepResult sr : srList.get(0).getKeiro()) {  // 複数解は１つ目の解のみ表示
            tracePar(sr);
        }
    }
    
    void tracePar(StepResult sr) {
        if (sr.getAddSR() != null) {
            for (StepResult from : sr.getAddSR()) { // 各StepPanelは，リンク(EdgePanel)をどこから来たかだけを所有する？（どこに繋がってゆくかは気にしない）
                tracePar(from); // 再帰
            }
            StepPanel sp = stepMap.get(sr);
            if (sp == null) {
                sp = new StepPanel(sr); // ここで作ったり
                stepMap.put(sr, sp);
                add(sp);

                Integer locY = stepMap.get(sr.getAddSR().get(0)).getY() + next;
                Integer preX = locMap.get(locY);
                Integer locX = 10;
                if (preX != null) {
                    locX = preX + next;
                }
                locMap.put(locY, locX);
                sp.setLocation(locX, locY);
            }

            for (StepResult from : sr.getAddSR()) {
                StepPanel fromPnl = stepMap.get(from);
                EdgePanel ep = new EdgePanel(fromPnl, sp);
                edgeMap.put(sr, ep);
                add(ep);
            }
        } else {
            if (!stepMap.containsKey(sr)) {
                StepPanel sp = new StepPanel(sr.getAnswer()); // ここで作ったり
                stepMap.put(sr, sp);
                add(sp);

                Integer locY = 10;
                Integer preX = locMap.get(locY);
                Integer locX = 10;
                if (preX != null) {
                    locX = preX + next;
                }
                locMap.put(locY, locX);
                sp.setLocation(locX, locY);
            }
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