package com.tools.iText;


import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *  iText 生成简单表格  PDF 生成
 *
 * @author Fuq
 * @date 2013-7-15
 */
public class MeasMinusFineReport  {

    static BaseFont bfChinese = null;
    private final Logger logger = Logger.getLogger(getClass());
    private final String PATH = "C:\\Users\\Administrator\\Desktop\\test.pdf";

    public MeasMinusFineReport() {
        try {
            bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (DocumentException ex) {
            logger.error("", ex);
        } catch (IOException ex) {
            logger.error("", ex);
        }
    }

    public static void main(String[] args) throws Exception {
        MeasMinusFineReport measMinusFineReport = new MeasMinusFineReport();
        measMinusFineReport.createReport(null);

    }

    private void createReport(List<Object> mfsList) throws Exception {
        Rectangle rectangle = PageSize.A4.rotate();//设置为横向
        Document document = new Document(rectangle);
        PdfWriter.getInstance(document, new FileOutputStream(PATH));
        document.addTitle("违约扣款一览表（JL05 表）");
        document.addSubject("违约扣款一览表（JL05 表）");
        document.addKeywords("违约扣款一览表（JL05 表）");
        document.addProducer();
        document.addCreationDate();
        document.open();
        //iText中用文本块(Chunk)、短语(Phrase)和段落(paragraph)处理文本。

        Font font = new Font(bfChinese, 12, Font.NORMAL);
        Phrase prs = new Phrase();
        Chunk chunk1 = new Chunk("XXX高速公路（JL05）", font);
        prs.add(chunk1);
        Paragraph topP = new Paragraph(prs);
        Chunk chunk2 = new Chunk("违约扣款一览表", new Font(bfChinese, 23, Font.BOLD));
        Paragraph topP1 = new Paragraph(chunk2);
        topP.setAlignment(1);//居中
        topP1.setAlignment(1);
        document.add(topP);
        document.add(topP1);


        //defaultLayout.setBorder(0);//设置无边框



        Chunk ckT1 = new Chunk("施工队: 施工队1　　　　　　　　　　　　　　　合同号: XXXXXXXXX　　　　　　　　　　　　　　　填报日期: 2013-07-16", font);
        Paragraph ctp = new Paragraph(ckT1);
        Chunk ckT2 = new Chunk("施工段: 施工段1　　　　　　　　　　　　　　　编　号: XXXXXXXXX　　　　　　　　　　　　　　　截止日期: 2013-07-16", font);
        Paragraph ctp1 = new Paragraph(ckT2);
        document.add(ctp);
        document.add(ctp1);
        Table table = new Table(6);
        Cell defaultLayout = table.getDefaultLayout();
        defaultLayout.setHorizontalAlignment(Element.ALIGN_CENTER);//内容水平居中
      //  defaultLayout.setVerticalAlignment(Element.ALIGN_MIDDLE);  // 设置垂直居中  
        table.setPadding(4);
        table.setWidth(100f);
        float[] widths ={ 0.10f, 0.30f, 0.13f, 0.13f, 0.13f, 0.21f};
        table.setWidths(widths);
        Cell t1A = new Cell(getTh("月份"));
        
        Cell t1B = new Cell(getTh("违约扣款名称"));
        Cell t1C = new Cell(getTh("本期末扣款金额(元)"));
        Cell t1D = new Cell(getTh("上期末扣款金额(元)"));
        Cell t1E = new Cell(getTh("本期扣款金额(元)"));
        Cell t1F = new Cell(getTh("备注"));
        table.addCell(t1A, 0, 0);
        table.addCell(t1B, 0, 1);
        table.addCell(t1C, 0, 2);
        table.addCell(t1D, 0, 3);
        table.addCell(t1E, 0, 4);
        table.addCell(t1F, 0, 5);
        int column = 0;
        int columnCount = 12;
        while (column++ < columnCount) {
            Cell tIA = new Cell(getTd("月份" + column));
            Cell tIB = new Cell(getTd("违约扣款名称" + column));
            Cell tIC = new Cell(getTd("本期末扣款金额" + column));
            Cell tID = new Cell(getTd("上期末扣款金额" + column));
            Cell tIE = new Cell(getTd("本期扣款金额" + column));
            Cell tIF = new Cell(getTd("备注" + column));
            table.addCell(tIA, column, 0);
            table.addCell(tIB, column, 1);
            table.addCell(tIC, column, 2);
            table.addCell(tID, column, 3);
            table.addCell(tIE, column, 4);
            table.addCell(tIF, column, 5);
        }

        Cell tIA = new Cell(getTh("合计"));
        tIA.setColspan(2);

        Cell tIC = new Cell(getTd(" 2" + column));
        Cell tID = new Cell(getTd(" 3" + column));
        Cell tIE = new Cell(getTd(" 4" + column));
        Cell tIF = new Cell(getTd("5" + column));
        table.addCell(tIA, column, 0);

        table.addCell(tIC, column, 2);
        table.addCell(tID, column, 3);
        table.addCell(tIE, column, 4);
        table.addCell(tIF, column, 5);
        document.add(table);

        Chunk bT1 = new Chunk("施工队: 施工段1　　　　　　　　　　　　计划经理: XXXX　　　　　　　　　　　　总工: XXXX　　　　　　　　　　　　项目经理: XXX", font);
        Paragraph bp = new Paragraph(bT1);
        document.add(bp);
        document.close();
    }

    private Paragraph getTh(String text) {
        Font tableFont = new Font(bfChinese, 10, Font.BOLD);
        Paragraph paragraph = new Paragraph(text, tableFont);
        // paragraph.setAlignment(2);
        return paragraph;
    }

    private Paragraph getTd(String text) {
        Font tableFont = new Font(bfChinese, 10, Font.NORMAL);
        Paragraph paragraph = new Paragraph(text, tableFont);
        // paragraph.setAlignment(2);
        return paragraph;
    }
}
