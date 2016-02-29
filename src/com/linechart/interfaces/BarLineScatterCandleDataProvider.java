//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.linechart.interfaces;

import com.linechart.components.YAxis.AxisDependency;
import com.linechart.interfaces.ChartInterface;
import com.linechart.utils.Transformer;

public interface BarLineScatterCandleDataProvider extends ChartInterface {
    Transformer getTransformer(AxisDependency var1);

    boolean isInverted(AxisDependency var1);
}
