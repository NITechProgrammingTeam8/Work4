import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

class StepPanel extends JPanel {
    private int id;
    private Rule rule;
    private Assertion ast;

    // private ArrayList<EdgePanel> fromEdges; // 自分に入ってくるリンク

    StepPanel(StepResult step) {
        // rule = step.getRule();
        // ast = step.getAssertion();
        // fromEdges = new ArrayList<>();

        // setLayout();
        // setBackground(Color.ORANGE);
        // setBorder(new BevelBorder(BevelBorder.RAISED));

        // JLabel rlabel = new JLabel(rule);
        // JLabel alabel = new JLabel(assertion);

        // add(rlabel);
        // add(alabel);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // paintArrow(g);
    }

    // void paintArrow(Graphics g) {
    //     int fromX = start.x;
    //     int fromY = start.y;
    //     int toX = end.x;
    //     int toY = end.y;

    //     int constX = getLeft();
    //     int constY = getTop();

    //     g.setColor(Color.BLUE);
    //     g.drawLine(fromX - constX, fromY - constY, toX - constX, toY - constY);
    // }
}


class EdgePanel extends JPanel {
    private StepPanel tail;
    private StepPanel head;
    private Point start;
    private Point end;
    private int margin; // パネルの幅の猶予(パネルが端折れないようにするため)
    private JPanel mainPanel;
    private boolean pass;

    EdgePanel(Link link, StepPanel tail, StepPanel head) { // tail =label=> head
        this.link = link;
        this.tail = tail;
        this.head = head;
        margin = 100;
        pass = false;

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

    void passing() {
        pass = true;
    }
}