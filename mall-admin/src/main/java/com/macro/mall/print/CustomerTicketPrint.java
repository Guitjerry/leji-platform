package com.macro.mall.print;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.macro.mall.dto.OmsOrderPrintDto;
import com.macro.mall.model.OmsOrderItem;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.List;

/**
 * @Date 2021/2/6 20:06
 * @Version 1.0
 * 打印
 * @Author Admin
 * @Copyright (c) 2020, 乐记
 */
@org.springframework.stereotype.Component
public class CustomerTicketPrint implements Printable {
    private OmsOrderPrintDto omsOrderPrintDto;
    // 构造函数
    public CustomerTicketPrint(OmsOrderPrintDto omsOrderPrintDto) {
        super();
        this.omsOrderPrintDto = omsOrderPrintDto;
    }

    public CustomerTicketPrint() {
    }


    /**
     * 打印方法
     * graphics - 用来绘制页面的上下文，即打印的图形
     * pageFormat - 将绘制页面的大小和方向，即设置打印格式，如页面大小一点为计量单位（以1/72 英寸为单位，1英寸为25.4毫米。A4纸大致为595 × 842点）
     * 小票纸宽度一般为58mm，大概为165点
     * pageIndex - 要绘制的页面从 0 开始的索引 ，即页号
     **/
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
            throws PrinterException {
        Component c = null;
        //此 Graphics2D 类扩展 Graphics 类，以提供对几何形状、坐标转换、颜色管理和文本布局更为复杂的控制。
        //它是用于在 Java(tm) 平台上呈现二维形状、文本和图像的基础类。
        //拿到画笔
        Graphics2D g2 = (Graphics2D) graphics;
        // 设置打印颜色为黑色
        g2.setColor(Color.black);

        // 打印起点坐标
        double x = pageFormat.getImageableX(); //返回与此 PageFormat相关的 Paper对象的可成像区域左上方点的 x坐标。
        double y = pageFormat.getImageableY(); //返回与此 PageFormat相关的 Paper对象的可成像区域左上方点的 y坐标。

        // 虚线
        float[] dash1 = { 4.0f };
        // width - 此 BasicStroke 的宽度。此宽度必须大于或等于 0.0f。如果将宽度设置为
        // 0.0f，则将笔划呈现为可用于目标设备和抗锯齿提示设置的最细线条。
        // cap - BasicStroke 端点的装饰
        // join - 应用在路径线段交汇处的装饰
        // miterlimit - 斜接处的剪裁限制。miterlimit 必须大于或等于 1.0f。
        // dash - 表示虚线模式的数组
        // dash_phase - 开始虚线模式的偏移量

        // 设置画虚线
        g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 4.0f, dash1, 0.0f));

        // 设置打印字体（字体名称、样式和磅值大小）（字体名称可以是物理或者逻辑名称）
        //Font.PLAIN:普通样式常量   Font.ITALIC:斜体样式常量   Font.BOLD:粗体样式常量。
        Font font = new Font("宋体", Font.BOLD, 16);
        g2.setFont(font);               //设置字体
        float heigth = font.getSize2D();//字体高度
        float line = heigth;//下一行开始打印的高度
        //设置小票标题
        g2.drawString("【点餐分单】", (float)x+40, (float)y+line);

        font = new Font("宋体",Font.PLAIN,16);
        g2.setFont(font);
        line =line+heigth*2;

//        //  桌号和排号都为空，此行不打印
//        if (deskNumber == null && descNumber == null) {
//            g2.drawString("", (float)x, (float)y + line);
//            //  桌号为空，则显示排号
//        } else if (deskNumber == null) {
//            g2.drawString("排号: "+descNumber+"号", (float)x, (float)y + line);
//            //  排号为空，则显示桌号
//        } else if(descNumber == null) {
//            g2.drawString("桌号: "+deskNumber+"号", (float)x, (float)y + line);
//        }else{
//            g2.drawString("桌号: "+deskNumber+"号", (float)x, (float)y + line);
//        }
        font = new Font("宋体", Font.PLAIN, 10);
        g2.setFont(font);         // 设置字体
        heigth = font.getSize2D();// 字体高度
        line +=heigth;
        // 显示订单号
        g2.drawString("订单编号:" + omsOrderPrintDto.getOrderSn(), (float) x , (float) y + line);

        line+=heigth;
        // 显示收银员
        g2.drawString("下单时间:" + DateUtil.formatDateTime(omsOrderPrintDto.getCreateTime()), (float) x, (float) y + line);

        line += heigth;
        //绘制虚线: (在此图形上下文的坐标系中使用当前颜色在点 (x1, y1) 和 (x2, y2) 之间画一条线。)
        g2.drawLine((int) x, (int) (y + line), (int) x + 180, (int) (y + line));

        line += heigth;
        //显示标题
        g2.drawString("名称", (float) x , (float) y + line+5);
        g2.drawString("数量", (float) x + 100, (float) y + line+5);
        g2.drawString("价格", (float) x + 150, (float) y + line+5);

        line = line+heigth*2;
        if(CollectionUtil.isNotEmpty(omsOrderPrintDto.getOmsOrderItems())) {
            List<OmsOrderItem> omsOrderItems = omsOrderPrintDto.getOmsOrderItems();
            for(OmsOrderItem omsOrderItem: omsOrderItems) {
                g2.drawString(omsOrderItem.getProductName(), (float) x, (float) y + line);
                g2.drawString(String.valueOf(omsOrderItem.getProductQuantity()), (float) x + 100, (float) y + line);
                g2.drawString(String.valueOf(omsOrderItem.getProductPrice()),(float) x + 150, (float) y + line);
            }
        }
        line += heigth;
        //绘制虚线
        g2.drawLine((int) x, (int) (y + line), (int) x + 180, (int) (y + line));

        switch (pageIndex) {
            case 0:
                return PAGE_EXISTS;//0
            default:
                return NO_SUCH_PAGE;//1

        }
    }

}
