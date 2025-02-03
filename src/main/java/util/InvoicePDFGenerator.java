package util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import tables.Client;
import tables.Invoice;
import tables.Order;
import tables.Product;

public class InvoicePDFGenerator {

    // Méthode pour générer une facture en PDF
    public static void generateInvoicePDF(Client client, Order order, Invoice invoice, String dirPath) throws IOException, DocumentException {
    	String fileName = "invoice" + invoice.getInvoiceID() + ".pdf";
        String filePath = dirPath + "/" + fileName;
    	
    	// Créer un document PDF
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Ajouter un titre à la facture
        Paragraph title = new Paragraph("Invoice", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.BOLD, BaseColor.BLACK));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Ajouter les informations du client
        document.add(new Paragraph("Customer: " + client.getFirstName() + " " + client.getLastName()));
        document.add(new Paragraph("Address: " + client.getStreet() + ", " + client.getCity() + " " + client.getPostCode() + ", " + client.getCountry()));
        document.add(new Paragraph("Email: " + client.getEmail()));
        document.add(new Paragraph("Invoice Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(invoice.getInvoiceDate())));

        // Ajouter un espace entre les informations du client et le tableau des produits
        document.add(new Paragraph("\n"));

        // Ajouter un tableau avec les détails de la commande (produits et quantités)
        PdfPTable table = new PdfPTable(3); // 3 colonnes : Product, Quantity, Total Price

        table.addCell(new PdfPCell(new Phrase("Product")));
        table.addCell(new PdfPCell(new Phrase("Quantity")));
        table.addCell(new PdfPCell(new Phrase("Total Price")));

        // Ajouter les lignes de la commande
        double totalAmount = 0; // pour calculer le total de la commande

        for (Map.Entry<Product, Integer> entry : order.getCart().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double price = product.getPrice();
            double lineTotal = price * quantity;
            totalAmount += lineTotal;

            table.addCell(new PdfPCell(new Phrase(product.getName())));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(quantity))));
            table.addCell(new PdfPCell(new Phrase(String.format("%.2f", lineTotal))));
        }

        document.add(table);

        // Ajouter le total de la commande
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Total: " + String.format("%.2f", totalAmount), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Font.BOLD)));

        // Ajouter les informations supplémentaires de la facture
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Invoice ID: " + invoice.getInvoiceID()));
        document.add(new Paragraph("Order ID: " + invoice.getOrderID()));

        // Fermer le document PDF
        document.close();
    }

}