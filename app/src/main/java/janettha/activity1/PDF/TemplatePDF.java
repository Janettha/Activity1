package janettha.activity1.PDF;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.annotation.Documented;
import java.util.ArrayList;
import java.util.List;

import janettha.activity1.Models.Emocion;
import janettha.activity1.Models.Emociones;
import janettha.activity1.Models.Respuesta;
import janettha.activity1.Models.Tutores;

public class TemplatePDF {
    private Context context;
    private File filePDF;
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private Font fTitle = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
    private Font fSubTitle = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private Font fText = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);
    private Font fHighText = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD, BaseColor.YELLOW);

    private String headers[] = {"Ejercicio", "INICIO", "FIN", "Respuesta", "Calificacion"};
    Emociones em = new Emociones();
    List<Emocion> emociones;

    String tutor, email;

    ArrayList<Respuesta>respuestasActB = new ArrayList<>();

    public TemplatePDF(Context context) {
        this.context = context;
        emociones = em.Emociones(context, "m");
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

    public void addHeader(String user, String iniS, String finS, String date){
        getDatosTutor();
        paragraph = new Paragraph();
        addChilCenter(new Paragraph("USER: "+user, fTitle));
        addChilCenter(new Paragraph("Fecha de creacion de archivo PDF: "+date, fText));
        paragraph.setSpacingAfter(20);
        addChilP(new Paragraph("Tutor: "+ tutor, fText));
        addChilP(new Paragraph("Correo de tutor: "+email, fText));
        addChilP(new Paragraph("Inicio de sesion: "+iniS));
        addChilP(new Paragraph("Fin de sesion: "+finS));
        paragraph.setSpacingAfter(30);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("addTitles", e.toString());
        }
    }

    private void addChilP(Paragraph childP){
        childP.setAlignment(Element.ALIGN_LEFT);
        paragraph.add(childP);
    }

    private void addChilCenter(Paragraph childP){
        childP.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childP);
    }

    public void addParrafo(int nActividad){
        paragraph = new Paragraph("Actividad No. "+String.valueOf(nActividad), fSubTitle);
        paragraph.setSpacingAfter(20);
        paragraph.setSpacingBefore(5);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("addParagraph", e.toString());
        }
    }

    public void createTable(ArrayList<Respuesta>Respuesta){
        int indexC = 0;
        paragraph = new Paragraph();
        paragraph.setFont(fText);
        PdfPTable pdfPTable = new PdfPTable(headers.length);
        pdfPTable.setWidthPercentage(100);
        //Se crean celdas
        PdfPCell pdfPCell;
        while (indexC < headers.length){
            pdfPCell = new PdfPCell(new Phrase(headers[indexC++], fSubTitle));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setBackgroundColor(BaseColor.ORANGE);
            pdfPTable.addCell(pdfPCell);
        }

        for(int indexR = 0; indexR < Respuesta.size(); indexR++){
            String[]row = new String[headers.length];
            row[0] = em.getEmocion(Respuesta.get(indexR).getId()).getName();
            row[1] = Respuesta.get(indexR).getInicioS();
            row[2] = Respuesta.get(indexR).getFinS();
            row[3] = em.getEmocion(Respuesta.get(indexR).getRespuesta()).getName();
            if(Respuesta.get(indexR).getCalif()) {
                row[4] = "Correcto";
            }else{
                row[4] = "Incorrecto";
            }
            for (indexC = 0; indexC < headers.length ; indexC++) {
                pdfPCell = new PdfPCell(new Phrase(row[indexC]));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setFixedHeight(40);
                pdfPTable.addCell(pdfPCell);
            }
        }
        paragraph.add(pdfPTable);
        try {
            document.add(paragraph);
        } catch (DocumentException e) {
            Log.e("createTable", e.toString());
        }
    }

    public void viewPDF(){
        Intent  intent = new Intent(context, ViewPDFActivity.class);
        intent.putExtra("path", filePDF.getAbsolutePath());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void getDatosTutor(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        email = mAuth.getCurrentUser().getEmail();
        tutor = mAuth.getCurrentUser().getDisplayName();
    }

    public void addRespuesta(Respuesta r){
        respuestasActB.add(r);
    }

    public ArrayList<Respuesta> getRespuestasActB(){
        return respuestasActB;
    }

}
