package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
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

/**
 * Utility class for generating PDF invoices.
 */
public class InvoicePDFGenerator {

    /**
     * Generates a PDF invoice, allowing the user to choose the save location.
     *
     * @param owner   the parent window for the file chooser (can be null)
     * @param client  the client associated with the invoice
     * @param order   the order associated with the invoice
     * @param invoice the invoice to generate
     * @throws IOException       if an I/O error occurs
     * @throws DocumentException if a PDF document error occurs
     */
    public static void generateInvoicePDF(Window owner, User client, Order order, Invoice invoice)
            throws IOException, DocumentException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Invoice");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("invoice" + invoice.getInvoiceID() + ".pdf");
        File file = fileChooser.showSaveDialog(owner);
        if (file == null) {
            return;
        }

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        // Invoice title (centered)
        Paragraph invoiceTitle = new Paragraph("Invoice No. " + invoice.getInvoiceID(),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, com.itextpdf.text.BaseColor.BLACK));
        invoiceTitle.setAlignment(Element.ALIGN_CENTER);
        document.add(invoiceTitle);
        document.add(Chunk.NEWLINE);

        // Header table with company and client details
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setSpacingBefore(10);
        headerTable.setSpacingAfter(10);
        headerTable.setWidths(new float[]{1f, 1f});

        PdfPCell companyCell = new PdfPCell();
        companyCell.setBorder(Rectangle.NO_BORDER);
        companyCell.addElement(new Paragraph("Crochet Store",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        headerTable.addCell(companyCell);

        PdfPCell clientCell = new PdfPCell();
        clientCell.setBorder(Rectangle.NO_BORDER);
        clientCell.addElement(new Paragraph(client.getFirstName() + " " + client.getLastName(),
                FontFactory.getFont(FontFactory.HELVETICA, 12)));
        clientCell.addElement(new Paragraph(client.getAddress().getStreet(),
                FontFactory.getFont(FontFactory.HELVETICA, 12)));
        clientCell.addElement(new Paragraph(client.getAddress().getPostCode() + " " + client.getAddress().getCity(),
                FontFactory.getFont(FontFactory.HELVETICA, 12)));
        clientCell.addElement(new Paragraph(client.getAddress().getCountry(),
                FontFactory.getFont(FontFactory.HELVETICA, 12)));
        headerTable.addCell(clientCell);

        document.add(headerTable);

        // Order information
        Paragraph orderInfo = new Paragraph();
        orderInfo.setAlignment(Element.ALIGN_LEFT);
        orderInfo.setFont(FontFactory.getFont(FontFactory.HELVETICA, 12));
        orderInfo.add("Order No.: " + order.getOrderID() + "\n");
        orderInfo.add("Client No.: " + order.getClientID() + "\n");
        orderInfo.add("Invoice Date: " + new SimpleDateFormat("yyyy-MM-dd").format(invoice.getInvoiceDate()) + "\n");
        document.add(orderInfo);
        document.add(Chunk.NEWLINE);

        // Product table
        PdfPTable productTable = new PdfPTable(4);
        productTable.setWidthPercentage(100);
        productTable.setSpacingBefore(10f);
        productTable.setSpacingAfter(10f);
        productTable.setWidths(new float[]{3f, 1f, 2f, 2f});

        productTable.addCell(new PdfPCell(new Phrase("Product",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12))));
        productTable.addCell(new PdfPCell(new Phrase("Quantity",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12))));
        productTable.addCell(new PdfPCell(new Phrase("Unit Price",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12))));
        productTable.addCell(new PdfPCell(new Phrase("Total Price",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12))));

        double totalAmount = 0;
        for (Map.Entry<Product, CartItem> entry : order.getCart().entrySet()) {
            Product product = entry.getKey();
            CartItem item = entry.getValue();
            int quantity = item.getQuantity();
            double unitPrice = item.getPriceAtPurchase();
            double lineTotal = unitPrice * quantity;
            totalAmount += lineTotal;

            productTable.addCell(new PdfPCell(new Phrase(product.getName(),
                    FontFactory.getFont(FontFactory.HELVETICA, 12))));
            productTable.addCell(new PdfPCell(new Phrase(String.valueOf(quantity),
                    FontFactory.getFont(FontFactory.HELVETICA, 12))));
            productTable.addCell(new PdfPCell(new Phrase(String.format("%.2f €", unitPrice),
                    FontFactory.getFont(FontFactory.HELVETICA, 12))));
            productTable.addCell(new PdfPCell(new Phrase(String.format("%.2f €", lineTotal),
                    FontFactory.getFont(FontFactory.HELVETICA, 12))));
        }
        document.add(productTable);

        Paragraph totalParagraph = new Paragraph("Order Total: " + String.format("%.2f €", totalAmount),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12));
        totalParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalParagraph);

        document.close();
    }
}
