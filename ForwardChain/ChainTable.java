public abstract class ChainTable extends JPanel {
    View view;
    Presenter pres;
    ArrayList<Assertion> astList;
    Assertion schAst;  // astListの中に入れていい？

    ChainTable(String fileName) {
        view = new View();
        pres = new Presenter(view);
        pres.start("CarShop.data");

        SpringLayout layout = new SpringLayout();  // GridBagLayoutの方がいいかも
        setLayout(layout);
    }

    ArrayList<Rule> getRules() {
        pres.getRules();  // 合わせる
        return view.showRuleList();
    }

    abstract boolean schNode(ArrayList<Assertion> astList, Assertion schAst);

    abstract void paintPnls(ArrayList<Node> nlist);  // パネル（解）の描写

    boolean addRule(Rule rule) {
        return false;
    }

    boolean udRule(Rule rule, String , ArrayList<String> , String ) {
        return false;
    }

    boolean rmRule(Rule rule) {
        return false;
    }
}

class FwdChainTable extends ChainTable {

    FwdChainTable(String fileName) {
        super(fileName);
    }

    @Override
    boolean schNode(ArrayList<Assertion> astList, Assertion schAst) {
        this.astList = astList;
        this.schAst = schAst;  // 前向き推論時はnullも許容したい
        ArrayList<Node> nodeList = pres.restart(astList);
        genPnls(nodeList);

        if(schAst != null) {
            ArrayList<Node> resList = pres.getResults(schAst);
            paintPnls(resList);
        }
        return true;
    }

    void genPnls(ArrayList<Node> nlist) {  // 前向き推論においては，PaintPnlsと別に全体の描写が先にある
        
    }
    
    @Override
    void paintPnls(ArrayList<Node> nlist) {
        for (Node n : nlist) {
            NodePanel np = new NodePanel(n);
            
            // layout.putConstraint(, , , , );
            add(np);

            EdgePanel ep = n.getEdges();  // 各ノードは，リンクをどこから来たかだけを所有する？（どこに繋がってゆくかは気にしない）
            // layout.putConstraint(, ep, , , , );  // SpringLayoutだとこれの定義に条件分岐が必要そう
            add(ep);
        }
    }
}

class BwdChainTable extends ChainTable {

    BwdChainTable(String fileName) {
        super(fileName);
    }
    
    @Override
    boolean schNode(ArrayList<Assertion> astList, Assertion schAst) {
        this.astList = astList;
        this.schAst = schAst;  // 後向き推論時nullだとfalse
        if(schAst != null) {
            ArrayList<Node> nodeList = pres.restart(astList, schAst);
            paintPnls(nodeList);
            return true;
        } else {
            return false;
        }
    }

    @Override
    void paintPnls(ArrayList<Node> nlist) {
        for (Node n : nlist) {
            NodePanel np = new NodePanel(n);
            
            // layout.putConstraint(, , , , );
            add(np);

            EdgePanel ep = n.getEdges();  // 各ノードは，リンクをどこから来たかだけを所有する？（どこに繋がってゆくかは気にしない）
            // layout.putConstraint(, ep, , , , );  // SpringLayoutだとこれの定義に条件分岐が必要そう
            add(ep);
        }
    }
}