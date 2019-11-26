import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

class StepPanel extends JPanel {
    JLabel alabel;

    StepPanel(String ast) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.ORANGE);
        setBorder(new BevelBorder(BevelBorder.RAISED));
        setSize(200, 30);
        setLocation(10, 10);

        alabel = new JLabel(ast);
        add(alabel);
    }

    StepPanel(StepResult step) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBackground(Color.ORANGE);
        setBorder(new BevelBorder(BevelBorder.RAISED));
        setSize(200, 150);
        setLocation(10, 10);

        Rule af = step.getAnswerField();
        if (af != null) {
            JPanel rpanel = new RulePanel(af);
            add(rpanel);
        }

        JLabel alabel = new JLabel(" => " + step.getAnswer());
        add(alabel);
    }

    // @Override
    // public void paintComponent(Graphics g) {
    //     super.paintComponent(g);
    //     paintArrow(g);
    // }

    // void paintArrow(Graphics g) {
    //     int fromX = rpanel.getX() + (rpanel.getWidth() / 2);
    //     int fromY = rpanel.getY() + rpanel.getHeight();
    //     int toX = alabel.getX() + (alabel.getWidth() / 2);
    //     int toY = alabel.getY();

    //     g.setColor(Color.BLUE);
    //     g.drawLine(fromX, fromY, toX, toY);
    // }

    class RulePanel extends JPanel {
        RulePanel(Rule rule) {
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            setBackground(Color.ORANGE);
            setBorder(new EtchedBorder(EtchedBorder.LOWERED));

            add(new JLabel("rule: " + rule.getName()));
            add(new JLabel("if: "));
            for (String s : rule.getAntecedents()) {
                add(new JLabel("      " + s));
            }
            add(new JLabel("then: " + rule.getConsequent()));
        }
    }
}

class EdgePanel extends JPanel {
    private StepPanel topPnl;
    private StepPanel btmPnl;
    private Point topPoint;
    private Point btmPoint;
    private int grace; // パネルの幅の猶予(パネルのサイズが小さすぎたときのため)
    private boolean pass;

    EdgePanel(StepPanel topPnl, StepPanel btmPnl) {
        this.topPnl = topPnl;
        this.btmPnl = btmPnl;
        grace = 10;
        pass = false;
        topPoint = new Point(topPnl.getX() + (topPnl.getWidth() / 2), topPnl.getY() + topPnl.getHeight());
        btmPoint = new Point(btmPnl.getX() + (btmPnl.getWidth() / 2), btmPnl.getY());

        int lpX = getLeft();
        int lpY = getTop();
        int lpWidth = getRight() - lpX;
        int lpHeight = getBtm() - lpY;
        setBounds(lpX, lpY, lpWidth, lpHeight);

        // setBackground(Color.BLACK);
        setOpaque(false); // パネルの透過

        // setLayout(null);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        paintArrows(g2d);
    }

    void paintArrows(Graphics2D g) {
        int fromX = topPoint.x;
        int fromY = topPoint.y;
        int toX = btmPoint.x;
        int toY = btmPoint.y;

        // 相対座標の考慮
        int relX = getLeft();
        int relY = getTop();

        if (pass) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLUE);
        }
        BasicStroke stroke = new BasicStroke(2.0f);
        g.setStroke(stroke);
        g.drawLine(fromX - relX, fromY - relY, toX - relX, toY - relY);
    }

    int getLeft() {
        return execHor(true) - grace;
    }

    int getRight() {
        return execHor(false) + grace;
    }

    int getTop() {
        return execVer(true);
    }

    int getBtm() {
        return execVer(false);
    }

    int execHor(boolean b) {
        int left = topPoint.x;
        int right = btmPoint.x;
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
        int top = topPoint.y;
        int btm = btmPoint.y;
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