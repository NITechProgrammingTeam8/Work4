import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;
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

        fct = new FwdChainTable();
        JPanel mainPanel = fct;
        JPanel menuPanel = new MenuPanel();
        status = new JLabel();

        Container contentPane = getContentPane();
        contentPane.add(mainPanel, BorderLayout.CENTER);
        contentPane.add(menuPanel, BorderLayout.NORTH);
        contentPane.add(status, BorderLayout.SOUTH);
    }

    class MenuPanel extends JPanel implements ActionListener {
        JTextField text;

        MenuPanel() {
            text = new JTextField(30);
            JButton[] btns = new JButton[3];
            btns[0] = new JButton("検索");
            btns[1] = new JButton("追加");
            btns[2] = new JButton("削除");

            add(text);
            for (JButton b : btns) {
                add(b);
                b.addActionListener(this);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            String arg = text.getText();

            String param = "実行";
            if (cmd.equals("検索")) {
                String result = rMap.searchNode(arg).toString();
                status.setText(cmd + "の" + param + ": " + arg + " => " + result);
            } else {
                if (cmd.equals("追加")) {
                    if (rMap.addLink(arg)) {
                        param = "成功";
                    } else {
                        param = "失敗";
                    }
                } else if (cmd.equals("削除")) {
                    if (rMap.removeLink(arg)) {
                        param = "成功";
                    } else {
                        param = "失敗";
                    }
                }
                status.setText(cmd + "の" + param + ": " + arg);
            }
        }
    }
}

class FwdChainTable extends JPanel {
    View view;
    Presenter presenter;
    ArrayList<Assertion> astList;

    FwdChainTable(String fileName) {
        view = new View();
        pst = new Presenter(view);
        astList = new ArrayList<>();  // 検索時はこれに入れる．前向き推論では初期値は要素なし．
        pst.start(astList, "CarShop.data");

        SpringLayout layout = new SpringLayout();  // GridBagLayoutの方がいいかも
        setLayout(layout);

        ArrayList<Node> nodeList = new ArrayList<>();
        for (entry:  pst.getResults()) {  // どう渡される？
            Node n = new Node(rule, ast);  // presenterに引数を合わせる
            
            // layout.putConstraint(, , , , );
            add(np);

            Edge e = n.getEdges();  // 各ノードは，リンクをどこから来たかだけを所有する？（どこに繋がってゆくかは気にしない）
            // layout.putConstraint(, e, , , , );  // SpringLayoutだとこれの定義に条件分岐が必要そう
            add(e);
        }
    }

    ArrayList searchNode(String text) {
        return ad.searchNaturalData(text);
    }
}

class Node extends JPanel {
    private int id;
    private Rule rule;
    private Assertion ast;

    private ArrayList<Edge> fromEdges; // 自分に入ってくるリンク
    private boolean focus;

    Node(Rule rule, Assertion ast) {
        this.rule = rule;
        this.ast = ast;
        fromEdges = new ArrayList<>();

        focus = false;

        setLayout();
        setBackground(Color.ORANGE);
        setBorder(new BevelBorder(BevelBorder.RAISED));

        JLabel rlabel = new JLabel(rule);
        JLabel alabel = new JLabel(assertion);

        add(rlabel);
        add(alabel);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintArrow(g);
    }

    void paintArrow(Graphics g) {
        int fromX = start.x;
        int fromY = start.y;
        int toX = end.x;
        int toY = end.y;

        int constX = getLeft();
        int constY = getTop();

        g.setColor(Color.BLUE);
        g.drawLine(fromX - constX, fromY - constY, toX - constX, toY - constY);
    }
}

class Edge extends JPanel {
    private Link link;
    private NodePanel tail;
    private NodePanel head;
    private Point start;
    private Point end;
    private int margin; // パネルの幅の猶予(パネルが端折れないようにするため)
    private JPanel mainPanel;

    LinkPanel(Link link, NodePanel tail, NodePanel head) { // tail =label=> head
        this.link = link;
        this.tail = tail;
        this.head = head;
        margin = 100;

        // setBackground(Color.BLACK);
        setOpaque(false); // パネルの透過
        Rectangle source = tail.getBounds();
        Rectangle distance = head.getBounds();
        setShortestDistance(source, distance);

        setSize();

        setLayout(null);

        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(0, 128, 128));
        mainPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JLabel label = new JLabel(tail.getId() + " = " + link.getLabel() + " => " + head.getId());

        mainPanel.add(label);

        int mainWidth = 120;
        int mainHeight = 30;
        int fitX = -(mainWidth / 2);
        int fitY = -(mainHeight / 2);
        mainPanel.setBounds((getRight() - getLeft()) / 2 + fitX, (getBtm() - getTop()) / 2 + fitY, mainWidth,
                mainHeight);
        add(mainPanel);

        tail.addDepartFromMeLinks(this);
        head.addArriveAtMeLinks(this);
    }

    void setShortestDistance(Rectangle source, Rectangle distance) {
        Point[] fromMidPoints = getMidPoints(source);
        Point[] toMidPoints = getMidPoints(distance);

        double min = Double.MAX_VALUE;
        for (int i = 0; i < 4; i++) {
            Point from = fromMidPoints[i].getLocation();
            for (int j = 0; j < 4; j++) {
                Point to = toMidPoints[j].getLocation();
                double value = (from.getX() - to.getX()) * (from.getX() - to.getX())
                        + (from.getY() - to.getY()) * (from.getY() - to.getY());
                if (value < min) {
                    min = value;
                    start = from;
                    end = to;
                }
            }
        }
    }

    Point[] getMidPoints(Rectangle r) {
        Point[] midPoints = new Point[4];
        for (int i = 0; i < midPoints.length; i++) {
            midPoints[i] = new Point();
        }
        midPoints[0].setLocation(r.x + r.width / 2.0, r.y); // 上の中点
        midPoints[1].setLocation(r.x + r.width, r.y + r.height / 2.0); // 右の中点
        midPoints[2].setLocation(r.x + r.width / 2.0, r.y + r.height); // 下の中点
        midPoints[3].setLocation(r.x, r.y + r.height / 2.0); // 左の中点
        return midPoints;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintArrows(g);
    }

    void paintArrows(Graphics g) {
        int fromX = start.x;
        int fromY = start.y;
        int toX = end.x;
        int toY = end.y;

        int constX = getLeft();
        int constY = getTop();

        g.setColor(Color.BLUE);
        g.drawLine(fromX - constX, fromY - constY, toX - constX, toY - constY);
    }

    Point getStart() {
        return start;
    }

    Point getEnd() {
        return end;
    }

    int getLeft() {
        return execHor(true) - margin;
    }

    int getRight() {
        return execHor(false) + margin;
    }

    int getTop() {
        return execVer(true) - margin;
    }

    int getBtm() {
        return execVer(false) + margin;
    }

    int execHor(boolean b) {
        int left = start.x;
        int right = end.x;
        if (left > right) {
            int tmp = left;
            left = right;
            right = tmp;
        }
        if (b) {
            return left;
        } else {
            return right;
        }
    }

    int execVer(boolean b) {
        int top = start.y;
        int btm = end.y;
        if (top > btm) {
            int tmp = top;
            top = btm;
            btm = tmp;
        }
        if (b) {
            return top;
        } else {
            return btm;
        }
    }

    void setSize() {
        int lpX = getLeft();
        int lpY = getTop();
        int lpWidth = getRight() - lpX;
        int lpHeight = getBtm() - lpY;
        setBounds(lpX, lpY, lpWidth, lpHeight);
        // System.out.println(lp);
        // System.out.println(nodes.get(l.getTail()));
        // System.out.println(nodes.get(l.getHead()));
        // System.out.println();
    }

    void update() {
        Rectangle source = tail.getBounds();
        Rectangle distance = head.getBounds();
        setShortestDistance(source, distance);
        setSize();

        int fitX = -(mainPanel.getWidth() / 2);
        int fitY = -(mainPanel.getHeight() / 2);
        mainPanel.setLocation((getRight() - getLeft()) / 2 + fitX, (getBtm() - getTop()) / 2 + fitY);
    }
}