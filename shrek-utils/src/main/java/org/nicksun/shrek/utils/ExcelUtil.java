package org.nicksun.shrek.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author nicksun
 *
 */
public class ExcelUtil {

	private static final int MAX_IMPORT_COUNT = 1000;

	private static final int MAX_CAPACITY_SHEET = 5000;
	
	private static final String FILETYPE_XLSX = "xlsx";

	private static final String FILETYPE_XLS = "xls";

	private ExcelUtil() {

	}

	/**
	 * 解析excel文件,最大支持导入1000行
	 * 
	 * @param input
	 *            excel流
	 * @param ext
	 *            excel文件类型
	 * @param startRow
	 *            读取起始行
	 * @param func
	 *            处理cells的函数
	 * @return
	 * @throws Exception
	 */
	public static <T, R> List<R> readExcel(InputStream input, String ext, int startRow, Function<List<String>, R> func)
			throws Exception {
		return readExcel(input, ext, startRow, MAX_IMPORT_COUNT, func);
	}

	/**
	 * 解析excel文件
	 * 
	 * @param input
	 *            excel流
	 * @param ext
	 *            excel文件类型
	 * @param startRow
	 *            读取起始行
	 * @param maxImportCount
	 *            最大导入行数
	 * @param func
	 *            处理cells的函数
	 * @return
	 * @throws Exception
	 */
	public static <T, R> List<R> readExcel(InputStream input, String ext, int startRow, int maxImportCount,
			Function<List<String>, R> func) throws Exception {
		try {
			if (FILETYPE_XLSX.equalsIgnoreCase(ext)) {
				return readExcel2007(input, startRow, maxImportCount, func);
			} else if (FILETYPE_XLS.equalsIgnoreCase(ext)) {
				return readExcel2003(input, startRow, maxImportCount, func);
			} else {
				throw new Exception("不支持的文件格式");
			}
		} catch (Exception ex) {
			throw ex;
		}
	}

	private static <R> List<R> readExcel2003(InputStream input, int startRow, int maxImportCount,
			Function<List<String>, R> func) throws Exception {
		List<R> result = new ArrayList<>();
		try (POIFSFileSystem fs = new POIFSFileSystem(input); HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fs);) {
			HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
			int totalRows = sheet.getPhysicalNumberOfRows();
			if (totalRows > maxImportCount) {
				throw new Exception("单次导入不能超过" + MAX_IMPORT_COUNT + "条");
			}

			// 循环输出表格中的内容(忽略表头)
			for (int i = sheet.getFirstRowNum() + startRow; i < totalRows; i++) {
				List<String> temp = new ArrayList<>();
				HSSFRow row = sheet.getRow(i);
				int totalCells = row.getLastCellNum();
				for (int j = row.getFirstCellNum(); j < totalCells; j++) {
					HSSFCell cell = row.getCell(j);
					String cellStr = cell == null ? StringUtils.EMPTY : row.getCell(j).toString().trim();
					temp.add(cellStr);
				}
				if (temp.size() > 0) {
					result.add(func.apply(temp));
				}
			}
		} catch (IOException e) {
			throw e;
		}
		return result;
	}

	private static <R> List<R> readExcel2007(InputStream input, int startRow, int maxImportCount,
			Function<List<String>, R> func) throws Exception {
		List<R> result = new ArrayList<>();
		try (XSSFWorkbook xssfWorkbook = new XSSFWorkbook(input);) {
			// 构造 XSSFWorkbook 对象，strPath 传入文件路径
			XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
			int totalRows = sheet.getPhysicalNumberOfRows();
			if (totalRows > maxImportCount) {
				throw new Exception("单次导入不能超过" + MAX_IMPORT_COUNT + "条");
			}

			// 循环输出表格中的内容(忽略表头)
			for (int i = sheet.getFirstRowNum() + startRow; i < totalRows; i++) {
				List<String> temp = new ArrayList<>();
				XSSFRow row = sheet.getRow(i);
				int totalCells = row.getLastCellNum();
				for (int j = row.getFirstCellNum(); j < totalCells; j++) {
					XSSFCell cell = row.getCell(j);
					String cellStr = cell == null ? StringUtils.EMPTY : row.getCell(j).toString().trim();
					temp.add(cellStr);
				}
				if (temp.size() > 0) {
					result.add(func.apply(temp));
				}
			}
		} catch (IOException e) {
			throw e;
		}
		return result;
	}

	/**
	 * 导出Excel，默认单个sheet页容量是5000
	 * 
	 * @param headers
	 * @param data
	 * @param outputStream
	 * @throws IOException
	 */
	public static void exportExcel(String[] headers, List<Object[]> data, OutputStream outputStream)
			throws IOException {
		exportExcel(headers, data, MAX_CAPACITY_SHEET, outputStream);
	}

	/**
	 * 导出Excel
	 * 
	 * @param headers
	 * @param data
	 * @param maxCapacity
	 *            单个sheet页的最大容量，大于最大容量在另启一个sheet页
	 * @param outputStream
	 * @throws IOException
	 */
	public static void exportExcel(String[] headers, List<Object[]> data, int maxCapacity, OutputStream outputStream)
			throws IOException {
		// 声明一个工作薄
		try (HSSFWorkbook workbook = new HSSFWorkbook();) {
			// 生成一个表格
			HSSFSheet sheet = workbook.createSheet("Sheet1");
			// 头列表
			HSSFRow row1 = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				HSSFCell cell = row1.createCell(i);
				cell.setCellValue(headers[i]);
			}
			int j = 1;
			int k = 1;
			int count = 2;
			HSSFRow row;
			HSSFCell cell;
			if (data != null && data.size() > 0) {
				for (Object[] rowArr : data) {
					row = sheet.createRow(j);
					for (int m = 0; m < headers.length; m++) {
						cell = row.createCell(m);
						cell.setCellValue(getString(rowArr[m]));
					}
					j = j + 1;
					k++;
					// 如果数据量大于5000,则分多个sheet导出
					if (k > maxCapacity) {
						sheet = workbook.createSheet("Sheet" + count);
						HSSFRow row2 = sheet.createRow(0);
						for (int i = 0; i < headers.length; i++) {
							HSSFCell cell1 = row2.createCell(i);
							cell1.setCellValue(headers[i]);
						}
						count++;
						k = 1;
						j = 1;
					}
				}
			}
			workbook.write(outputStream);
		}
	}

	private static String getString(Object o) {
		if (o == null) {
			return StringUtils.EMPTY;
		}
		return o.toString();
	}

}
