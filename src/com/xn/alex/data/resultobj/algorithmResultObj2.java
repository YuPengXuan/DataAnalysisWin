package com.xn.alex.data.resultobj;

import java.util.Comparator;

public class algorithmResultObj2 implements Comparator<Object> {

    public Number x = 0;

    public Number y = 0;

    public Number yDivX = 0;

    public String type = null;

    public int nodeNum = -1;

    @Override
    public int compare(final Object paramT1, final Object paramT2) {
        // TODO Auto-generated method stub
        final algorithmResultObj2 obj1 = (algorithmResultObj2) paramT1;

        final algorithmResultObj2 obj2 = (algorithmResultObj2) paramT2;

        if (obj1.yDivX.floatValue() > obj2.yDivX.floatValue()) {
            return -1;
        }

        return 1;
    }

}
