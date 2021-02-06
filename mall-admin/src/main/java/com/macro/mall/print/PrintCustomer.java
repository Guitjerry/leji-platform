package com.macro.mall.print;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.standard.PrinterName;
import java.awt.print.*;

/**
 * @Date 2021/2/6 20:25
 * @Version 1.0
 * @Author Admin
 * @Copyright (c) 2020, 乐记
 */
@Component
public class PrintCustomer {
    //用于创建打印内容
    private CustomerTicketPrint customerTicketPrint;

    public PrintCustomer(CustomerTicketPrint customerTicketPrint) {
        this.customerTicketPrint = customerTicketPrint;
    }

    public PrintCustomer() {
    }

    public CustomerTicketPrint getCustomerTicketPrint() {
        return customerTicketPrint;
    }

    public void setCustomerTicketPrint(CustomerTicketPrint customerTicketPrint) {
        this.customerTicketPrint = customerTicketPrint;
    }

    public void PrintCustomer() throws Exception{
            //Book 类提供文档的表示形式，该文档的页面可以使用不同的页面格式和页面 painter
            Book book = new Book();  //要打印的文档

            //PageFormat类描述要打印的页面大小和方向
            PageFormat pf = new PageFormat();       //初始化一个页面打印对象
            pf.setOrientation(PageFormat.PORTRAIT); //设置页面打印方向，从上往下


            // 通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
            Paper paper = new Paper();
            paper.setSize(180, 30000);// 纸张大小
            paper.setImageableArea(10, 0, 180, 30000);// A4(595x842)设置打印区域，其实0，0应该是72，72，因为A4纸的默认X,Y边距是72
            pf.setPaper(paper);

            book.append(customerTicketPrint, pf);

            //指定XP-80C打印机打印分单
            HashAttributeSet hs = new HashAttributeSet();
            String printerName="XP-80C";
            hs.add(new PrinterName(printerName,null));
            PrintService[] pss = PrintServiceLookup.lookupPrintServices(null, hs);
            Assert.isTrue(pss.length>0, "无法找到打印机" + printerName);
            // 获取打印服务对象
            PrinterJob job = PrinterJob.getPrinterJob();
            // 设置打印类
            job.setPageable(book);
            //添加指定的打印机
            job.setPrintService(pss[0]);
            //开始打印
            job.print();
    }
}
