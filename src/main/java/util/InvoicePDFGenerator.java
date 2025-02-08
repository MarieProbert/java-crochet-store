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
     * Generates the invoice in PDF format, allowing the user to choose where to save it.
     * @param owner The parent window for the FileChooser (can be passed as null if unavailable).
     * @param client The concerned client.
     * @param order The corresponding order.
     * @param invoice The invoice to be generated.
     * @throws IOException
     * @throws DocumentException
     */
    public static void generateInvoicePDF(Window owner, User client, Order order, Invoice invoice)
            throws IOException, DocumentException {

        // Open a dialog window to choose the file save path
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Invoice");
        // Filter to show only PDF files
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        // Suggest a default file name
        fileChooser.setInitialFileName("invoice" + invoice.getInvoiceID() + ".pdf");

        // Show the dialog (if owner is null, the system's default window will be used)
        File file = fileChooser.showSaveDialog(owner);

        // If the user cancels, stop the generation
        if (file == null) {
            return;
        }

        // Create the PDF document
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        // Invoice title
        Paragraph title = new Paragraph("Invoice",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.BOLD, BaseColor.BLACK));
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Client information
        document.add(new Paragraph("Customer: " + client.getFirstName() + " " + client.getLastName()));
        document.add(new Paragraph("Address: " + client.getAddress().getStreet() + ", " 
                + client.getAddress().getCity() + " " + client.getAddress().getPostCode() + ", " 
                + client.getAddress().getCountry()));
        document.add(new Paragraph("Email: " + client.getEmail()));
        document.add(new Paragraph("Invoice Date: " 
                + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(invoice.getInvoiceDate())));

        // Space before the table
        document.add(new Paragraph("\n"));

        // Product table (3 columns: Product, Quantity, Total Price)
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

        // Total of the order
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Total: " + String.format("%.2f", totalAmount),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Font.BOLD)));

        // Additional invoice information
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Invoice ID: " + invoice.getInvoiceID()));
        document.add(new Paragraph("Order ID: " + invoice.getOrderID()));

        // Close the document
        document.close();
    }
}
