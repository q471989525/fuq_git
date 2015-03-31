/*
 * 读取excel
 */
package com.tools.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 读取excel
 *
 * @author Fuq
 */
public class ExcelReader {

    /**
     *
     * @param fs
     * @throws Exception
     */
    public void readXlsx(InputStream fs) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(fs);
        Iterator<XSSFSheet> iterator = workbook.iterator();//工作表集合
        while (true) {
            XSSFSheet st = null;
            try {
                st = iterator.next();
            } catch (Exception e) {
                //坑爹 抛出NoSuchElementException 表示没有下一个
                break;
            }
            st.getSheetName();
            for (int rowNumOfSheet = 0; rowNumOfSheet <= st.getLastRowNum(); rowNumOfSheet++) {
                if (null != st.getRow(rowNumOfSheet)) {
                    XSSFRow aRow = st.getRow(rowNumOfSheet); // 获得一个行

                    Iterator<Cell> cellIterator = aRow.cellIterator();

                    Cell next = null;
                    System.out.println("Row:" + aRow.getRowNum());
                    while (true) {
                        try {
                            next = cellIterator.next();
                        } catch (Exception e) {
                            //抛出NoSuchElementException 表示没有下一个
                            break;
                        }
                        next.setCellType(Cell.CELL_TYPE_STRING);//设置类型为String
                        System.out.print(next.getStringCellValue() + "\t");
                    }
                    System.out.print("\n");
                }
            }
        }



    }

    public void readXls(InputStream fs) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook(fs);
        int i = 0;
        HSSFSheet st = null;
        while (true) {
            try {
                st = workbook.getSheetAt(i);
                i++;
            } catch (Exception ex) {
                break;
            }
            for (int rowNumOfSheet = 0; rowNumOfSheet <= st.getLastRowNum(); rowNumOfSheet++) {
                if (null != st.getRow(rowNumOfSheet)) {
                    HSSFRow aRow = st.getRow(rowNumOfSheet); // 获得一个行

                    Iterator<Cell> cellIterator = aRow.cellIterator();

                    Cell next = null;
                    System.out.println("Row:" + aRow.getRowNum());
                    while (true) {
                        try {
                            next = cellIterator.next();
                        } catch (Exception e) {
                            break;
                        }
                        next.setCellType(Cell.CELL_TYPE_STRING);
                        System.out.print(next.getStringCellValue() + "\t");
                    }
                    System.out.print("\n");
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ExcelReader excelReader = new ExcelReader();
        excelReader.readXlsx (new FileInputStream("C:\\Users\\Administrator\\Desktop\\大同湖主线桥下部结构.xlsx "));
    }
}
