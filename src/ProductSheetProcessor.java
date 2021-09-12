import java.io.*;
import java.math.BigInteger;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ProductSheetProcessor {

    private final String inputFileName;
    private final String outputHeader = "PID^Product Id^Mfr Name^Mfr P/N^Price^COO^Short Description^UPC^UOM";

    private int fileNumber = 0;
    private int errorCount=0;

    ProductSheetProcessor(String fileName){
        this.inputFileName = fileName;
    }

    public PrintWriter createNewOutputFile() throws IOException {
        fileNumber++;
        File outFile = new File("output"+fileNumber+".csv");
        if(!outFile.exists()){
            outFile.createNewFile();
        }

        FileWriter outFW= new FileWriter(outFile);
        BufferedWriter outBW=new BufferedWriter(outFW);
        PrintWriter outPW=new PrintWriter(outBW);

        return outPW;
    }

    public void writeBadProduct(BadProduct badProduct,Sheet sheet) throws IOException {
        Row row = sheet.createRow(++errorCount);
        Cell cell = row.createCell(0);
        try {
            cell.setCellValue(Double.parseDouble(badProduct.getPid()));
        }catch (Exception e){
            cell.setCellValue(badProduct.getPid());
        }
        cell = row.createCell(1);
        cell.setCellValue(badProduct.getProductId());
        cell = row.createCell(2);
        cell.setCellValue(badProduct.getMfrName());
        cell = row.createCell(3);
        cell.setCellValue(badProduct.getMfrPn());
        cell = row.createCell(4);
        try {
            cell.setCellValue(Double.parseDouble(badProduct.getCost()));
        }catch (Exception e){
            cell.setCellValue(badProduct.getCost());
        }
        cell = row.createCell(5);
        cell.setCellValue(badProduct.getCoo());
        cell = row.createCell(6);
        cell.setCellValue(badProduct.getShortDesc());
        cell = row.createCell(7);
        cell.setCellValue(badProduct.getUpc());
        cell = row.createCell(8);
        cell.setCellValue(badProduct.getUom());
    }

    public void startProcessing() {
        try{
            System.out.print("Processing..");
            List<Product> products = readProductsFromFile();

            PrintWriter outPW = createNewOutputFile();
            outPW.println(outputHeader); // Add Header in First Output File

            Workbook workbook = new XSSFWorkbook();
            Sheet errorSheet = workbook.createSheet();
            createErrorHeader(errorSheet);

            int outputCounter=0; //
            for (Product product : products) {
                System.out.print(".");
                if(product instanceof GoodProduct){
                    outputCounter++;
                    outPW.println(product.toString());
                    if(outputCounter==10000){
                        outputCounter=0;
                        outPW.close();
                        outPW= createNewOutputFile(); // Get New File
                        outPW.println(outputHeader); // Header in New File
                    }
                }
                else{
                    writeBadProduct((BadProduct) product,errorSheet);
                }
            }

            // Create a Error.xlsx from errorSheet
            try (FileOutputStream outputStream = new FileOutputStream("Error.xlsx")) {
                workbook.write(outputStream);
            }

            workbook.close(); // Close Error Sheet
            outPW.close(); // Close Last Output File

        }catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    private void createErrorHeader(Sheet errorSheet) {
        Row row = errorSheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("PID");
        cell = row.createCell(1);
        cell.setCellValue("Product Id");
        cell = row.createCell(2);
        cell.setCellValue("Mfr Name");
        cell = row.createCell(3);
        cell.setCellValue("MfrPN");
        cell = row.createCell(4);
        cell.setCellValue("Cost");
        cell = row.createCell(5);
        cell.setCellValue("Coo");
        cell = row.createCell(6);
        cell.setCellValue("Short Description");
        cell = row.createCell(7);
        cell.setCellValue("UPC");
        cell = row.createCell(8);
        cell.setCellValue("UOM");

    }

    private Product createProduct(Row row)
    {
        DataFormatter formatter = new DataFormatter();
        String p_id = formatter.formatCellValue(row.getCell(0));
        String productId = formatter.formatCellValue(row.getCell(1));
        String mfrName = formatter.formatCellValue(row.getCell(2));
        String mfrPn= formatter.formatCellValue(row.getCell(3));
        String cost = formatter.formatCellValue(row.getCell(4));
        String coo = formatter.formatCellValue(row.getCell(5));
        String shortDesc = formatter.formatCellValue(row.getCell(6));
        String upc = formatter.formatCellValue(row.getCell(7));
        String uom = formatter.formatCellValue(row.getCell(8));

        try{
            if(productId.isEmpty() || productId.length() > 50  ||
                    mfrName.isEmpty() || mfrName.length() > 50 ||
                    mfrPn.isEmpty() || mfrPn.length() > 50 ||
                    shortDesc.isEmpty() || shortDesc.length() > 300 ||
                    coo.length() > 2 || uom.length() > 2 || upc.length() > 12 ||
                    p_id.isEmpty() || p_id.length()>20 ){

                throw new Exception("Required Fields are Empty or Not in Specified Length");
            }

            BigInteger pid=new BigInteger(p_id);
            double price=Double.parseDouble(cost);
            price = price + (price*0.20);

            if(coo.isEmpty()){
                coo="TW";
            }
            if(uom.isEmpty()){
                uom="EA";
            }

            return new GoodProduct(pid,productId,mfrName,mfrPn,price,coo,shortDesc,upc,uom);
        }
        catch (NumberFormatException e){
//            e.printStackTrace();
//            System.out.println("Number Format Exception");
        }
        catch(NullPointerException e){
//            e.printStackTrace();
//            System.out.println("Null pointer Exception");
        }
        catch (Exception e){
//            e.printStackTrace();
//            System.out.println("Validation Exception");
        }
        return new BadProduct(p_id,productId,mfrName,mfrPn,cost,coo,shortDesc,upc,uom);
    }

    private List<Product> readProductsFromFile(){
        
        List<Product> products=new ArrayList<>();

        try{
            File file=new File(inputFileName);
            FileInputStream fis=new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);

            for (Row row : sheet) {
                if(row.getRowNum()==0){ // Skip Header
                    continue;
                }
                Product product = createProduct(row);
                products.add(product);
            }
            
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return products;
    }

}
