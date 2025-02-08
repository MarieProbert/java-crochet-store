package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import tables.CartItem;
import tables.Invoice;
import tables.Order;
import tables.Product;
import tables.User;

public class InvoicePDFGenerator {

    /**
     * Génère la facture en PDF en laissant l'utilisateur choisir où l'enregistrer.
     * @param owner La fenêtre parente pour le FileChooser (peut être passé à null si non disponible).
     * @param client Le client concerné.
     * @param order La commande correspondante.
     * @param invoice La facture à générer.
     * @throws IOException
     * @throws DocumentException
     */
    public static void generateInvoicePDF(Window owner, User client, Order order, Invoice invoice)
            throws IOException, DocumentException {

        // Ouvrir une fenêtre de dialogue pour choisir le chemin d'enregistrement du fichier
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer la facture");
        // Filtre pour n'afficher que les fichiers PDF
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf"));
        // Proposition d'un nom de fichier par défaut
        fileChooser.setInitialFileName("invoice" + invoice.getInvoiceID() + ".pdf");

        // Affiche la boîte de dialogue (si owner est null, la fenêtre par défaut du système sera utilisée)
        File file = fileChooser.showSaveDialog(owner);

        // Si l'utilisateur annule, on arrête la génération
        if (file == null) {
            return;
        }

        // Création du document PDF
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        // Titre de la facture
        Paragraph title = new Paragraph("Invoice",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.BOLD, BaseColor.BLACK));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Informations du client
        document.add(new Paragraph("Customer: " + client.getFirstName() + " " + client.getLastName()));
        document.add(new Paragraph("Address: " + client.getAddress().getStreet() + ", " 
                + client.getAddress().getCity() + " " + client.getAddress().getPostCode() + ", " 
                + client.getAddress().getCountry()));
        document.add(new Paragraph("Email: " + client.getEmail()));
        document.add(new Paragraph("Invoice Date: " 
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(invoice.getInvoiceDate())));

        // Espace avant le tableau
        document.add(new Paragraph("\n"));

        // Tableau des produits (3 colonnes : Produit, Quantité, Prix total)
        PdfPTable table = new PdfPTable(3);
        table.addCell(new PdfPCell(new Phrase("Product")));
        table.addCell(new PdfPCell(new Phrase("Quantity")));
        table.addCell(new PdfPCell(new Phrase("Total Price")));

        double totalAmount = 0;
        for (Map.Entry<Product, CartItem> entry : order.getCart().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue().getQuantity();
            double price = entry.getValue().getPriceAtPurchase();
            double lineTotal = price * quantity;
            totalAmount += lineTotal;

            table.addCell(new PdfPCell(new Phrase(product.getName())));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(quantity))));
            table.addCell(new PdfPCell(new Phrase(String.format("%.2f", lineTotal))));
        }
        document.add(table);

        // Total de la commande
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Total: " + String.format("%.2f", totalAmount),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Font.BOLD)));

        // Informations supplémentaires de la facture
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Invoice ID: " + invoice.getInvoiceID()));
        document.add(new Paragraph("Order ID: " + invoice.getOrderID()));

        // Fermeture du document
        document.close();
    }
}
