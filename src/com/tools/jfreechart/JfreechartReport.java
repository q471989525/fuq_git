package com.tools.jfreechart;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.ui.TextAnchor;

/**
 * 用 JFreeChart 生成柱状图
 * 需要包jcommon-1.0.21.jar;jfreechart-1.0.17.jar;jfreechart-1.0.17-swt.jar
 * @date 2014-2-24
 * @author fuq
 */
public class JfreechartReport {

   
    private String saveFilePath = "C:\\Users\\Administrator\\Desktop\\jfreechart-1.0.17";
//    private IProjManage iProjManage;

    /**
     * 生成柱状图<br/>
     * <b>默认保存路径：sys_config中</b>
     *
     * @param title 标题
     * @param data 二维数组。格式为{{XX1项目上月计划,XX2项目上月计划,...},{本月完成1,...2,...},{本月计划1,....}}
     * @param rowKeys  {"上月计划", "上月完成", "本月计划"}; 
     * @param columnKeys {"项目信息"}
     * @param width 生成图片的宽度 (像素)
     * @param height 生成图片的高度
     * @return String 生成图片的保存路径
     * @throws IOException
     *
     */
    public String mkImage(String title, double[][] data,String[] rowKeys,String[] columnKeys, int width, int height) throws IOException {
        //double[][] data = new double[][]{{720, 750, 860, 6800}, {830, 780, 790, 2702}, {400, 380, 390, 450}};
       // String[] rowKeys = {"上月计划", "上月完成", "本月计划"};
//        List<ProjInfo> allProj = iProjManage.getAllProj();
//        String[] columnKeys = new String[allProj.size()];
//        for (int i = 0; i < allProj.size(); i++) {
//            columnKeys[i] = allProj.get(i).getShort_name();
//        }

        String bottomText = "";
        String leftText = "产值(万元)";
        CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);

        JFreeChart chart = ChartFactory.createBarChart3D(title,
                bottomText,
                leftText,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
        TextTitle textTitle = chart.getTitle();
        textTitle.setFont(new Font("宋体", Font.BOLD, 20));

        CategoryPlot plot = chart.getCategoryPlot();

        //<editor-fold defaultstate="collapsed" desc="设置文字字体">
        CategoryAxis domainAxis = plot.getDomainAxis();//(柱状图的x轴)
        domainAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 16));//设置x轴坐标上的字体
        domainAxis.setLabelFont(new Font("宋体", Font.BOLD, 16));//设置x轴上的标题的字体
        ValueAxis valueAxis = plot.getRangeAxis();//(柱状图的y轴)
        valueAxis.setTickLabelFont(new Font("宋体", Font.BOLD, 16));//设置y轴坐标上的字体
        valueAxis.setLabelFont(new Font("宋体", Font.BOLD, 16));//设置y轴坐标上的标题的字体
        //底部文字乱码
        chart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 12));
        //</editor-fold>
        //设置网格背景颜色
        plot.setBackgroundPaint(Color.white);
        //设置网格竖线颜色
        plot.setDomainGridlinePaint(Color.pink);
        //设置网格横线颜色
        plot.setRangeGridlinePaint(Color.pink);

        //显示每个柱的数值，并修改该数值的字体属性
        BarRenderer3D renderer = new BarRenderer3D();
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);

        //默认的数字显示在柱子中，通过如下两句可调整数字的显示
        //注意：此句很关键，若无此句，那数字的显示会被覆盖，给人数字没有显示出来的问题
        renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_LEFT));
        renderer.setItemLabelAnchorOffset(10D);

        //设置每个地区所包含的平行柱的之间距离
        renderer.setItemMargin(0.2);
        plot.setRenderer(renderer);
        //设置地区、销量的显示位置
        //将下方的 内容 放到上方
        //plot.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
        //将默认放在左边的 单位和数量 放到右方
        //plot.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);
        //直接输出流
        //ChartUtilities.writeChartAsJPEG(response.getOutputStream(), chart, 1000, 350);
        String path = "";
        String fileName = UUID.randomUUID().toString() + ".png";
        path = saveFilePath + File.separator + fileName;
        ChartUtilities.saveChartAsPNG(new File(path), chart, width, height);
        return path;
    }
    
    
    public static void main(String[] args) throws IOException {
        JfreechartReport jfreechartReport = new JfreechartReport();
       double[][] data = new double[][]{{720, 750, 860, 6800}, {830, 780, 790, 2702}, {400, 380, 390, 450}};
       String[] rowKeys = {"上月计划", "上月完成", "本月计划"};
       String[] columnKeys={"项目1","项目2","项目3","项目4"};
       System.out.println(jfreechartReport.mkImage("测试",data,rowKeys,columnKeys, 1000, 300));
    }


}
