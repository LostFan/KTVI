package org.lostfan.ktv.view;

import java.awt.*;
import java.util.List;
import javax.swing.*;

import org.lostfan.ktv.domain.*;

public class TariffPriceView {

    public static final int HEIGHT = 400;
    public static final int WIDTH = 300;

    private JFrame frame;

    public TariffPriceView(List<TariffPrice> prices) {
        this.frame = new JFrame("TarifPrice");
        this.frame.setSize(new Dimension(WIDTH, HEIGHT));
        this.frame.setVisible(true);
        this.frame.setLocationRelativeTo(null);
    }
}
