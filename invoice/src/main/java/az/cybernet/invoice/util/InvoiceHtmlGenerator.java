package az.cybernet.invoice.util;

import az.cybernet.invoice.dto.response.ProductDetailResponse;
import az.cybernet.invoice.entity.InvoiceDetailed;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Base64;
import java.util.List;

@Component
public class InvoiceHtmlGenerator {

    private String getLogoBase64() {
        try (InputStream in = getClass().getResourceAsStream("/logo/Dövlət_Vergi_Xidmətinin_loqosu.png")) {
            if (in == null) {
                System.err.println("Logo tapılmadı!");
                return "";
            }
            byte[] bytes = in.readAllBytes();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String generate(InvoiceDetailed invoice) {
        StringBuilder productRows = new StringBuilder();
        int index = 1;

        List<ProductDetailResponse> products = invoice.getProducts();

        for (ProductDetailResponse ip : products) {
            String productName = ip.getProductName();
            String unit = ip.getMeasurementName();
            double quantity = ip.getQuantity();
            double price = ip.getPrice();

            productRows.append("""
                        <tr>
                            <td>%d</td>
                            <td>%s</td>
                            <td>%s</td>
                            <td>%.2f</td>
                            <td>%.2f</td>
                            <td>%.2f</td>
                    
                        </tr>
                    """.formatted(index++, productName, unit, quantity, price, price * quantity));
        }

        String productTable = productRows.length() > 0 ? productRows.toString()
                : "<tr><td colspan='6' class='center'>Məhsul əlavə edilməyib</td></tr>";

        String logoHtml = "<div class='logo' style='text-align: center; margin-bottom: 20px;'>" +
                "<img src='data:image/png;base64," + getLogoBase64() + "' alt='Şirkət Logosu' " +
                "style='max-width: 200px; height: auto;' />" +
                "</div>";

        return String.format("""
                                    <!DOCTYPE html>
                                           <html lang="az">
                                           <head>
                                               <meta charset="UTF-8"/>
                                               <title>Elektron Qaimə-Faktura</title>
                                               <style>
                                                   @page {
                                                       size: A4;
                                                       margin: 1cm;
                                                   }
                                                   body {
                                                       font-family: "DejaVu Sans", sans-serif;
                                                       padding: 30px;
                                                   }
                                                   table {
                                                       width: 100%%;
                                                       border-collapse: collapse;
                                                   }
                                                   th, td {
                                                       border: 1px solid black;
                                                       padding: 8px;
                                                       text-align: left;
                                                       vertical-align: middle;
                                                   }
                                                   .center {
                                                       text-align: center;
                                                   }
                                                   .no-border {
                                                       border: none;
                                                   }
                                                    .block {
                                                          display: inline-block;
                                                          vertical-align: top;
                                                    }
                                                    .signature {
                                                          display: flex;
                                                          justify-content: space-between;
                                                          margin-top: 40px;
                                                          align-items: flex-start;
                                                          flex-wrap: nowrap;
                                                    }
                                                   .bold {
                                                       font-weight: bold;
                                                       margin-bottom: 15px;
                                                   }
                                                   .header {
                                                       text-align: center;
                                                       font-size: 18px;
                                                       font-weight: bold;
                                                       padding: 12px;
                                                   }
                                                   .summary {
                                                       text-align: right;
                                                       padding: 10px;
                                                       font-weight: bold;
                                                   }
                                               </style>
                                           </head>
                                           <body>
                                           %s
                                           <table>
                                               <tr>
                                                   <td colspan="2" class="header">ELEKTRON QAİMƏ-FAKTURA</td>
                                               </tr>
                                               <tr>
                                                   <td><b>TARİX:</b></td><td>%s</td>
                                               </tr>
                                               <tr>
                                                   <td><b>SERİYA:</b></td><td>%s</td>
                                               </tr>
                                               <tr>
                                                   <td><b>№:</b></td><td>%s</td>
                                               </tr>
                                               <tr>
                                                   <td><b>TƏQDİM EDƏN VÖEN:</b></td><td>%s</td>
                                               </tr>
                                               <tr>
                                                   <td><b>ALICI VÖEN:</b></td><td>%s</td>
                                               </tr>
                                           </table>
                                           <br/>
                                           <table>
                                               <tr>
                                                   <th>No</th>
                                                   <th>Məhsulun adı</th>
                                                   <th>Ölçü vahidi</th>
                                                   <th>Say</th>
                                                   <th>Qiymət</th>
                                                   <th>Məbləğ</th>
                                               </tr>
                                               %s
                                           </table>
                                           <div class="summary">Yekun məbləğ: %.2f</div>
                                           <br/>
                                           <div class="signature">
                                                     <div class="block">
                                                         <div class="bold">İCRAÇI: CYBERNET LLC</div>
                                                         <br/><br/>
                                                         İmza ___________________________
                                                     </div>
                                                     <div class="block">
                                                         <div class="bold">MÜŞTƏRİ:</div>
                                                         <br/><br/>
                                                         İmza ___________________________
                                                     </div>
                                                 </div>
                                           </body>
                                           </html>
                        """,
                logoHtml,
                invoice.getCreatedAt().toLocalDate(),
                invoice.getSeries(),
                invoice.getInvoiceNumber(),
                invoice.getSenderId(),
                invoice.getCustomerId(),
                productTable,
                invoice.getTotal()
        );
    }
}