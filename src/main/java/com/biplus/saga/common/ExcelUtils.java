package com.biplus.saga.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelUtils {

    public static Row createRow(Sheet sheet, int line) {
        Row xRow = sheet.getRow(line);
        if (xRow == null) {
            xRow = sheet.createRow(line);
        }
        return xRow;
    }

    public static void createCell(Row row, int column, String value, CellStyle style) {
        Cell xCell = row.getCell(column);
        if (xCell == null) {
            xCell = row.createCell(column);
        }
        xCell.setCellValue(value);
        if (style != null) {
            xCell.setCellStyle(style);
        }
    }

    public static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<>();

        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setFontName("Times New Roman");
        headerFont.setFontHeight((short) 260);

        Font subHeaderFont = wb.createFont();
        subHeaderFont.setBold(true);
        subHeaderFont.setFontName("Times New Roman");
        subHeaderFont.setFontHeight((short) 260);

        Font cellTitleFont = wb.createFont();
        cellTitleFont.setBold(true);
        cellTitleFont.setFontName("Times New Roman");
        cellTitleFont.setFontHeight((short) 260);

        Font cellDataFont = wb.createFont();
        cellDataFont.setBold(false);
        cellDataFont.setFontName("Times New Roman");
        cellDataFont.setFontHeight((short) 240);

        Font cellDataErrorFont = wb.createFont();
        cellDataErrorFont.setBold(true);
        cellDataErrorFont.setColor(Font.COLOR_RED);
        cellDataErrorFont.setFontName("Times New Roman");
        cellDataErrorFont.setFontHeight((short) 240);

        CellStyle styleHeader = wb.createCellStyle();
        styleHeader.setAlignment(HorizontalAlignment.CENTER);
        styleHeader.setVerticalAlignment(VerticalAlignment.TOP);
        styleHeader.setFont(headerFont);
        styleHeader.setWrapText(true);
        styles.put("header", styleHeader);

        CellStyle subHeaderLeft = wb.createCellStyle();
        subHeaderLeft.setFont(subHeaderFont);
        subHeaderLeft.setWrapText(true);
        styles.put("subHeaderLeft", subHeaderLeft);

        CellStyle subHeaderCenter = wb.createCellStyle();
        subHeaderCenter.setFont(subHeaderFont);
        subHeaderCenter.setAlignment(HorizontalAlignment.CENTER);
        subHeaderCenter.setWrapText(true);
        styles.put("subHeaderCenter", subHeaderCenter);

        CellStyle cellBoldCenter = wb.createCellStyle();
        cellBoldCenter.setFont(cellTitleFont);
        cellBoldCenter.setAlignment(HorizontalAlignment.CENTER);
        styles.put("cellBoldCenter", cellBoldCenter);

        CellStyle cellBoldLeft = wb.createCellStyle();
        cellBoldLeft.setFont(cellTitleFont);
        cellBoldLeft.setAlignment(HorizontalAlignment.LEFT);
        styles.put("cellBoldLeft", cellBoldLeft);

        CellStyle cellBoldRight = wb.createCellStyle();
        cellBoldRight.setFont(cellTitleFont);
        cellBoldRight.setAlignment(HorizontalAlignment.RIGHT);
        styles.put("cellBoldRight", cellBoldRight);

        CellStyle cellColTitle = createBorderedStyle(wb);
        cellColTitle.setFont(cellTitleFont);
        cellColTitle.setAlignment(HorizontalAlignment.LEFT);
        cellColTitle.setVerticalAlignment(VerticalAlignment.TOP);
        cellColTitle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        cellColTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellColTitle.setWrapText(true);
        styles.put("cellColumnTitle", cellColTitle);

        CellStyle cellDataLeft = createBorderedStyle(wb);
        cellDataLeft.setFont(cellDataFont);
        cellDataLeft.setAlignment(HorizontalAlignment.LEFT);
        cellDataLeft.setWrapText(false);
//        cellDataLeft.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styles.put("cellDataLeft", cellDataLeft);

        CellStyle cellDataRight = createBorderedStyle(wb);
        cellDataRight.setFont(cellDataFont);
        cellDataRight.setWrapText(false);
        cellDataRight.setAlignment(HorizontalAlignment.RIGHT);
//        cellDataRight.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styles.put("cellDataRight", cellDataRight);

        CellStyle cellDataCenter = createBorderedStyle(wb);
        cellDataCenter.setFont(cellDataFont);
        cellDataCenter.setAlignment(HorizontalAlignment.CENTER);
        cellDataCenter.setVerticalAlignment(VerticalAlignment.CENTER);
        cellDataCenter.setWrapText(false);
//        cellDataCenter.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styles.put("cellDataCenter", cellDataCenter);

        CellStyle hlink_style = createBorderedStyle(wb);
        Font hlink_font = wb.createFont();
        hlink_font.setUnderline(Font.U_SINGLE);
        hlink_font.setColor(IndexedColors.BLUE.getIndex());
        hlink_style.setWrapText(false);
        hlink_style.setFont(hlink_font);

        styles.put("cellHyperLinkLeft", hlink_style);

        CellStyle cellDataError = createBorderedStyle(wb);
        cellDataError.setFont(cellDataErrorFont);
        cellDataError.setAlignment( HorizontalAlignment.LEFT);
        cellDataError.setWrapText(false);
//        cellDataLeft.setFillPattern(CellStyle.SOLID_FOREGROUND);
        styles.put("cellDataError", cellDataError);
//        CellStyle numStyle = wb.createCellStyle();
//        XSSFDataFormat format = wb.createDataFormat();
//        numStyle.setDataFormat(format.getFormat(javafx.scene.input.DataFormat.));

        return styles;
    }

    public static CellStyle createBorderedStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return style;
    }

    public static CellRangeAddress createCellReangeAddress(int col1, int row1, int col2, int row2, Sheet sheet, CellStyle cellStyle) {
        Row row = null;
        Cell cell = null;
        if (cellStyle != null) {
            for (int i = row1; i <= row2; i++) {
                for (int j = col1; j <= col2; j++) {
                    row = sheet.getRow(i);
                    if (row == null) {
                        row = sheet.createRow(i);
                    }
                    cell = row.getCell(j);
                    if (cell == null) {
                        cell = row.createCell(j);
                    }
                    cell.setCellStyle(cellStyle);
                }
            }
        }
        return new CellRangeAddress(row1, row2, col1, col2);
    }

    public static Cell createHyperLinkCell(Sheet sheet, int col, int iRowIndex, String value, String linkStr, CellStyle cellStyle) {
        Row row = null;
        Cell cell = null;
        try {
            row = sheet.getRow(iRowIndex);
            if (row == null) {
                row = sheet.createRow(iRowIndex);
            }
            cell = row.getCell(col);
            if (cell == null) {
                cell = row.createCell(col);
            }
            Hyperlink link = sheet.getWorkbook().getCreationHelper().createHyperlink(HyperlinkType.URL);
            link.setAddress(linkStr == null ? "" : linkStr);
            cell.setHyperlink(link);
            cell.setCellValue(value);
            cell.setCellStyle(cellStyle);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return cell;
    }

    public static void createHeader(Row row, int line, List<String> headers, CellStyle cellStyle){
        for (int i = 0; i < headers.size(); i++){
            createCell(row, i, headers.get(i), cellStyle);
        }
    }

    public static Cell createCell(Row row, int col, Object value, CellStyle cellStyle) {
        Cell cell = null;
        try {

            cell = row.getCell(col);
            if (cell == null) {
                cell = row.createCell(col);
            }
            if (value instanceof BigDecimal) {
                cell.setCellValue(((BigDecimal) value).doubleValue());
            } else if (value instanceof Float) {
                cell.setCellValue((Float) value);
            } else if (value instanceof Double) {
                cell.setCellValue((Double) value);
            } else if (value instanceof Integer) {
                cell.setCellValue((Integer) value);
            } else if (value instanceof Long) {
                cell.setCellValue((Long) value);
            } else {
                cell.setCellValue(value == null ? "" : value.toString());
            }

            cell.setCellStyle(cellStyle);

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return cell;
    }

    public static void autoFitColumn(Sheet sheet){
        if (sheet.getPhysicalNumberOfRows() > 0) {
            Row row = sheet.getRow(sheet.getFirstRowNum());
            Iterator<Cell> cellIterator = row.cellIterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                int columnIndex = cell.getColumnIndex();
                sheet.autoSizeColumn(columnIndex);
            }
        }
    }

    public static XSSFFont createBoldStyle(Workbook wb){
        XSSFFont font= (XSSFFont) wb.createFont();
        font.setBold(true);
        return font;
    }
}
