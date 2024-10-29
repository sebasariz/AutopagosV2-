/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iammagis.autopagos.jpa.beans.support;

import com.iammagis.autopagos.jpa.beans.Campo;
import com.iammagis.autopagos.jpa.beans.Estado;
import com.iammagis.autopagos.jpa.beans.Factura;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author sebasariz
 */
public class XlsReader {
    
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    DecimalFormat decimalFormat = new DecimalFormat("###0.##");
    String decimalPattern = "([0-9]*)\\.([0-9]*)";
    
    String error;
    
    public ArrayList<Factura> getFacturas(File file) throws InvalidFormatException, IOException {
        ArrayList<Factura> facturas = new ArrayList<>();

        // Read from original Excel file.
        if (file == null) {
            return null;
        }
        Workbook workbook = WorkbookFactory.create(new FileInputStream(file));
        
        int numeropaginas = workbook.getNumberOfSheets();
//        System.out.println("numero de paginas: " + numeropaginas);
        // Get the first sheet.  
        try {
            Sheet sheet = workbook.getSheetAt(0);
            System.out.println("sheet.getLastRowNum(): "+sheet.getLastRowNum());
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                // Set value of the first cell. 
                Row row = sheet.getRow(i);
                
                if (row.getCell(0) == null) {
                    System.out.println("Termine por null");
                    return facturas;
                } 
                String referencia = "";
                String email = "";
                Calendar calendarEmision = Calendar.getInstance();
                calendarEmision.set(Calendar.HOUR, 0);
                calendarEmision.set(Calendar.MINUTE, 0);
                calendarEmision.set(Calendar.SECOND, 0);
                calendarEmision.set(Calendar.MILLISECOND, 0);
                double valor = 0;
                Calendar calendarVencimiento = Calendar.getInstance();
                calendarVencimiento.set(Calendar.HOUR, 0);
                calendarVencimiento.set(Calendar.MINUTE, 0);
                calendarVencimiento.set(Calendar.SECOND, 0);
                calendarVencimiento.set(Calendar.MILLISECOND, 0);
                
                String retorno = valueReturn(row.getCell(3), workbook);
                if (retorno.equals("error")) {
                    error = "Se ha producido un error en la lectura del archivo.\nEl error se encuentra en la fila " + (i + 1) + ", columna VALOR";
                    return new ArrayList<>();
                } else {
                    valor = Double.parseDouble(retorno);
                    if (valor != 0) {
                        referencia = valueReturn(row.getCell(0), workbook);
                        if (referencia.equals("error")) {
                            error = "Se ha producido un error en la lectura del archivo.\nEl error se encuentra en la fila " + (i + 1) + ", columna REFERENCIA";
                            return new ArrayList<>();
                        }
                        String calendarEmisionString = valueReturn(row.getCell(1), workbook);
                        if (calendarEmisionString.equals("error")) {
                            error = "Se ha producido un error en la lectura del archivo.\nEl error se encuentra en la fila " + (i + 1) + ", columna FECHA DE EMISIÓN";
                            return new ArrayList<>();
                        }
                        Date dateEmision = simpleDateFormat.parse(calendarEmisionString);
                        calendarEmision.setTime(dateEmision);
                        String calendarioVencidoString = valueReturn(row.getCell(2), workbook);
                        if (calendarioVencidoString.equals("error")) {
                            error = "Se ha producido un error en la lectura del archivo.\nEl error se encuentra en la fila " + (i + 1) + ", columna FECHA DE VENCIMIENTO";
                            return new ArrayList<>();
                        }
                        Date dateVencido = simpleDateFormat.parse(calendarioVencidoString);
                        calendarVencimiento.setTime(dateVencido);
                        email = valueReturn(row.getCell(4), workbook);
                        if (email.equals("error")) {
                            error = "Se ha producido un error en la lectura del archivo.\nEl error se encuentra en la fila " + (i + 1) + ", columna EMAIL";
                            return new ArrayList<>();
                        }
                        
                        Factura factura = new Factura();
                        factura.setEmail(email);
                        factura.setEstadoIdestado(new Estado(1));
                        factura.setReferencia(referencia);
                        factura.setFechaEmision(BigInteger.valueOf(calendarEmision.getTimeInMillis()));
                        factura.setFechaVencimiento(BigInteger.valueOf(calendarVencimiento.getTimeInMillis()));
                        factura.setValor(valor);
                        facturas.add(factura);
                    }
                }
                
            }
        } catch (ParseException ex) {
            Logger.getLogger(XlsReader.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("errorrrr:");
        }
        
        return facturas;
        
    }
    
    public ArrayList<Factura> getFacturasCustom(File file, ArrayList<Campo> campos) throws IOException, InvalidFormatException {
        ArrayList<Factura> facturas = new ArrayList<Factura>();
        ArrayList<Campo> camposNuevos = new ArrayList<>();
        // Read from original Excel file.
        if (file == null) {
            return null;
        }
        Workbook workbook = WorkbookFactory.create(new FileInputStream(file));
        try {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                // Set value of the first cell. 
                Row row = sheet.getRow(i);
                
                if (row == null || row.getCell(0) == null) {
                    System.out.println("Termine por null");
                    return facturas;
                }
//                System.out.println("=========================celda: " + i);
                String referencia = "";
                String email = "";
                Calendar calendarEmision = Calendar.getInstance();
                calendarEmision.set(Calendar.HOUR, 0);
                calendarEmision.set(Calendar.MINUTE, 0);
                calendarEmision.set(Calendar.SECOND, 0);
                calendarEmision.set(Calendar.MILLISECOND, 0);
                double valor = 0;
                Calendar calendarVencimiento = Calendar.getInstance();
                calendarVencimiento.set(Calendar.HOUR, 0);
                calendarVencimiento.set(Calendar.MINUTE, 0);
                calendarVencimiento.set(Calendar.SECOND, 0);
                calendarVencimiento.set(Calendar.MILLISECOND, 0);
                
                String retorno = valueReturn(row.getCell(3), workbook);
                if (retorno.equals("error")) {
                    error = "Se ha producido un error en la lectura del archivo.\nEl error se encuentra en la fila " + (i + 1) + ", columna VALOR";
                    return new ArrayList<Factura>();
                } else {
                    valor = Double.parseDouble(retorno);
                    if (valor != 0) {
                        
                        referencia = valueReturn(row.getCell(0), workbook);
                        if (referencia.equals("error")) {
                            error = "Se ha producido un error en la lectura del archivo.\nEl error se encuentra en la fila " + (i + 1) + ", columna REFERENCIA";
                            return new ArrayList<Factura>();
                        }
                        String calendarEmisionString = valueReturn(row.getCell(1), workbook);
                        if (calendarEmisionString.equals("error")) {
                            error = "Se ha producido un error en la lectura del archivo.\nEl error se encuentra en la fila " + (i + 1) + ", columna FECHA DE EMISIÓN";
                            return new ArrayList<Factura>();
                        }
                        Date dateEmision = new Date();
                        
                        try {
                            double dateDouble = Double.parseDouble(calendarEmisionString);
                            dateEmision = DateUtil.getJavaDate(dateDouble);
                        } catch (Exception e) {
                            dateEmision = simpleDateFormat.parse(calendarEmisionString);
                        }
                        calendarEmision.setTime(dateEmision);
                        String calendarioVencidoString = valueReturn(row.getCell(2), workbook);
                        if (calendarioVencidoString.equals("error")) {
                            error = "Se ha producido un error en la lectura del archivo.\nEl error se encuentra en la fila " + (i + 1) + ", columna FECHA DE VENCIMIENTO";
                            return new ArrayList<Factura>();
                        }
                        Date dateVencido = new Date();
                        try {
                            double dateDouble = Double.parseDouble(calendarioVencidoString);
                            dateVencido = DateUtil.getJavaDate(dateDouble);
                        } catch (Exception e) {
                            dateVencido = simpleDateFormat.parse(calendarioVencidoString);
                        }
                        calendarVencimiento.setTime(dateVencido);
                        email = valueReturn(row.getCell(4), workbook);
                        if (email.equals("error")) {
                            error = "Se ha producido un error en la lectura del archivo.\nEl error se encuentra en la fila " + (i + 1) + ", columna EMAIL";
                            return new ArrayList<Factura>();
                        }

                        //obtenemos los valores de los campos
                        camposNuevos = new ArrayList<>();
                        for (Campo campo : campos) {
                            Campo campoNuevo = new Campo();
                            campoNuevo.setReferencia(campo.getReferencia());
                            campoNuevo.setXls(campo.getXls());
                            int number = Integer.parseInt(campo.getXls());
                            String valorCampo = valueReturn(row.getCell(number), workbook);
                            campoNuevo.setValor(valorCampo);
                            camposNuevos.add(campoNuevo);
                        }

//                        System.out.println("referencia: " + referencia);
//                        System.out.println("Fecha : " + calendarEmision.getTime());
//                        System.out.println("Valor : " + valor);
//                        System.out.println("Fecha vencimiento: " + calendarVencimiento.getTime());
//                        System.out.println("Email: " + email);
//                        System.out.println("campos: " + campos.size());
                        Factura factura = new Factura();
                        factura.setCampos(camposNuevos);
                        factura.setEmail(email);
                        factura.setEstadoIdestado(new Estado(1));
                        factura.setReferencia(referencia);
                        factura.setFechaEmision(BigInteger.valueOf(calendarEmision.getTimeInMillis()));
                        factura.setFechaVencimiento(BigInteger.valueOf(calendarVencimiento.getTimeInMillis()));
                        factura.setFechaCreacion(BigInteger.valueOf(System.currentTimeMillis()));
                        factura.setValor(valor);
                        
                        facturas.add(factura);
                    }
                }
                
            }
        } catch (ParseException ex) {
            Logger.getLogger(XlsReader.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("errorrrr:");
        }
        
        return facturas;
    }
    
    private String valueReturn(Cell cell, Workbook workbook) {
        String valorCampo = "";
        if (cell != null) {
            if (cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                valorCampo += decimalFormat.format(cell.getNumericCellValue()).replace(",", ".");
            } else if (cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                valorCampo += cell.getStringCellValue();
            } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
//                System.out.println("Formula is " + cell.getCellFormula());
                FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
//                System.out.println("evaluator.evaluateInCell(cell).getCellType(): " + evaluator.evaluateInCell(cell).getCellType());
                switch (evaluator.evaluateInCell(cell).getCellType()) {
                    case Cell.CELL_TYPE_BOOLEAN:
                        valorCampo = Boolean.toString(cell.getBooleanCellValue());
                        System.out.println(cell.getBooleanCellValue());
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            valorCampo = simpleDateFormat.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                        } else {
                            valorCampo = decimalFormat.format(cell.getNumericCellValue());
                        } 
                        break;
                    case Cell.CELL_TYPE_STRING:
                        valorCampo = cell.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_BLANK:
                        break;
                    case Cell.CELL_TYPE_ERROR:
                        valorCampo = "error";
                        System.out.println("entre por tipo error");
                        break;
                    // CELL_TYPE_FORMULA will never occur
                    case Cell.CELL_TYPE_FORMULA:
                        break;
                }
            } else {
                //error en la fila: i columna referencia
                valorCampo = "error";
                System.out.println("entrando por error dos porque no es formula: " + cell.getCellType());
            }
        } else {
            valorCampo = "error";
        }
        return valorCampo;
    }
    
    public String getError() {
        return error;
    }
    
}
