package cn.xiaofangwei.onlinesurvey.utils;

import cn.xiaofangwei.onlinesurvey.entity.Questionnaire;
import cn.xiaofangwei.onlinesurvey.entity.vo.DataAnalysis;
import org.apache.poi.hssf.usermodel.*;

import java.util.List;
import java.util.Map;

public class ExcelUtil {

    public static HSSFWorkbook exportExcel(Questionnaire questionnaire, List<DataAnalysis> dataAnalyses) {
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet("数据统计");
        int i=0, j=0;
        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        HSSFRichTextString title = new HSSFRichTextString(questionnaire.getTitle());
        cell.setCellValue(title);

        i+=2;

        for (int k = 0; k < dataAnalyses.size(); k++) {
            DataAnalysis question = dataAnalyses.get(k);
            //题目
            HSSFRow createRow = sheet.createRow(i);
            HSSFCell cell0 = createRow.createCell(j);
            HSSFRichTextString questionTitle = new HSSFRichTextString(k+1+".["+ getQuestionType(question.getQuestionType()) +"]"+question.getQuestionTitle());
            cell0.setCellValue(questionTitle);

            i += 1;
            if (question.getQuestionType() == 3) {
                HSSFRow createRow1 = sheet.createRow(i);
                HSSFCell cell1 = createRow1.createCell(j);
                HSSFRichTextString text1 = new HSSFRichTextString("问答题请通过下载详情数据获取");
                cell1.setCellValue(text1);
                i+=2;
                continue;
            }

            HSSFRow createRow2 = sheet.createRow(i);

            HSSFCell cell2 = createRow2.createCell(j);
            HSSFRichTextString xuanxiang = new HSSFRichTextString("选项");
            cell2.setCellValue(xuanxiang);

            HSSFCell cell3 = createRow2.createCell(j+1);
            HSSFRichTextString shuliang = new HSSFRichTextString("数量");
            cell3.setCellValue(shuliang);

            HSSFCell cell4 = createRow2.createCell(j+2);
            HSSFRichTextString zhanbi = new HSSFRichTextString("占比");
            cell4.setCellValue(zhanbi);

            i+=1;

            //题目
            List<Map<String, Object>> analysisResult = question.getAnalysisResult();
            for (int a = 0; a < analysisResult.size(); a++) {
                Map<String, Object> result = analysisResult.get(a);

                HSSFRow optionRow = sheet.createRow(i);

                HSSFCell c = optionRow.createCell(j);
                HSSFRichTextString text1 = new HSSFRichTextString((String) result.get("option"));
                c.setCellValue(text1);

                HSSFCell v = optionRow.createCell(j+1);
                HSSFRichTextString text2 = new HSSFRichTextString((int) result.get("count")+"");
                v.setCellValue(text2);

                HSSFCell n = optionRow.createCell(j+2);
                HSSFRichTextString text3 = new HSSFRichTextString((String) result.get("percent"));
                n.setCellValue(text3);

                i+=1;
            }
            HSSFRow totalRow = sheet.createRow(i);
            HSSFCell totalCell = totalRow.createCell(j);
            HSSFRichTextString text1 = new HSSFRichTextString("总计");
            totalCell.setCellValue(text1);

            int count = analysisResult.stream().mapToInt(result -> Integer.valueOf((int)result.get("count"))).sum();

            HSSFCell totalCell1 = totalRow.createCell(j+1);
            HSSFRichTextString text2 = new HSSFRichTextString(count + "");
            totalCell1.setCellValue(text2);

            HSSFCell totalCell2 = totalRow.createCell(j+2);
            HSSFRichTextString text3 = new HSSFRichTextString("100%");
            totalCell2.setCellValue(text3);

            i+=2;
        }
        return workbook;
    }

    public static String getQuestionType(int type) {
        if (type == 1) return "单选题";
        else if (type == 2) return "多选题";
        else if (type ==3) return "问答题";
        else return "";
    }
}
