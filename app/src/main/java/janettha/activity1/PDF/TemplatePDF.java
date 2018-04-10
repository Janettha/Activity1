package janettha.activity1.PDF;

import android.content.Context;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.annotation.Documented;

public class TemplatePDF {
    private Context context;
    private File filePDF;
    private Document document;
    private PdfWriter pdfWriter;

    public TemplatePDF(Context context) {
        this.context = context;
    }

    public void openPDF(){
        createFile();
        try{
            document = new Document(PageSize.A4);
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(filePDF));
            document.open();
        }catch(Exception e){
            Log.e("openDocument", e.toString());
        }
    }

    private void createFile(){
        File folder = new File(Environment.getExternalStorageDirectory().toString(), "PDF_emociones");
        if(!folder.exists()){
            folder.mkdirs();
        }
        filePDF=new File(folder, "TemplatePDF.pdf");
    }

    public void closeDocument(){
        document.close();
    }

    public void addMetaData(String user){
        document.addTitle(user);
        document.addCreationDate();
        document.addAuthor(user);
    }


}
