package com.shop.sport.Controller;

import com.shop.sport.DTO.BestSell;
import com.shop.sport.DTO.HoaDon;
import com.shop.sport.DTO.OrderDTO;
import com.shop.sport.Entity.Order1;
import com.shop.sport.Entity.OrderItem;
import com.shop.sport.Entity.Product;
import com.shop.sport.Entity.User;
import com.shop.sport.MailService.EmailDetails;
import com.shop.sport.MailService.EmailService;
import com.shop.sport.Response.Response;
import com.shop.sport.Service.CartService;
import com.shop.sport.Service.OrderService;
import com.shop.sport.Service.ProductService;
import com.shop.sport.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Annotation
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    Response response = Response.getInstance();

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @PostMapping("/buy")
    public ResponseEntity<Object> BuyProducts(@RequestBody Map<String, String> body) {


        try {
            long idUser = Long.parseLong(body.get("id_user"));
            long idShipMethod = Long.parseLong(body.get("id_ship_method"));
             orderService.insertOrder(idUser,
                    body.get("adress"),idShipMethod, body.get("phone"), body.get("ten_ng_nhan"),
                     body.get("idProducts"),body.get("idQuantities"));

            return response.generateResponse("Buy product Successfully", HttpStatus.OK, true);

        }catch (Exception e) {
            return response.generateResponse("Buy product fail"+e.getMessage(), HttpStatus.BAD_REQUEST, null);

        }
    }

    @GetMapping("/quatity-sell")
    public ResponseEntity<Object> getSoLuongSpdaban() {


        try {
            return response.generateResponse("Buy product Successfully", HttpStatus.OK, orderService.getSumSell());

        }catch (Exception e) {
            return response.generateResponse("Buy product fail"+e.getMessage(), HttpStatus.BAD_REQUEST, null);

        }
    }


    @GetMapping ("/by-user/{id}")
    public ResponseEntity<Object> getOrderByIdUser(@PathVariable("id") long iduser) {
        try {

            List<OrderDTO> list = orderService.getOrder_byIdUser(iduser);
            return response.generateResponse("get list order item Successfully", HttpStatus.OK, list);

        }catch (Exception e) {
            return response.generateResponse("get list order item failed"+e.getMessage(), HttpStatus.OK, 0 );

        }
    }

    @GetMapping ("/best-sell/{thang}/{nam}")
    public ResponseEntity<Object> getBestSell(@PathVariable("thang") int thang,
                                                   @PathVariable("nam")int nam) {
        try {

            List<BestSell> list = orderService.best_sell_month(thang, nam);
            return response.generateResponse("get list order item Successfully", HttpStatus.OK, list);

        }catch (Exception e) {
            return response.generateResponse("get list order item failed"+e.getMessage(), HttpStatus.OK, 0 );

        }
    }

    @GetMapping("/delete-order/{id}")
    public ResponseEntity<Object> deleteOrder(
            @PathVariable long id
    ) {

        try {
            Order1 order = orderService.findByID(id);
//            order.setOrderStatus(orderStatusService.findOrderStatusById(5)); // 5 là id của CANCEL trong bảng order_status


            List<OrderItem> list = order.getOrderItems().stream().toList();
            Product product ;

            for (OrderItem orderItem : list) {
                product = orderItem.getProduct();
                int quantityCurrent = product.getStockQuantity();

                product.setStockQuantity((int) (quantityCurrent + orderItem.getProductId()));
                productService.createProduct(product);

            }

            long isDel =orderService.deleteOder(id);
            if (isDel==1) {

                return response.generateResponse("delete product successfully", HttpStatus.OK, isDel);
            }

            return response.generateResponse("delete product successfully", HttpStatus.OK, isDel);
        } catch (Exception e) {
            return response.generateResponse("delete product failed"+e.getMessage(), HttpStatus.OK, null);

        }
    }


    @GetMapping ("/data-chart/{nam}")
    public ResponseEntity<Object> getDataCHart(
                                              @PathVariable("nam")int nam) {
        try {

            String list = orderService.get_data_chart( nam);

            return response.generateResponse("get list order item Successfully", HttpStatus.OK, list);

        }catch (Exception e) {
            return response.generateResponse("get list order item failed"+e.getMessage(), HttpStatus.OK, 0 );

        }
    }

    @GetMapping ("/export-bill/{id}")
    public ResponseEntity<Object> XuatHoaDon(@PathVariable("id") long idOder) {
        try {

            List<HoaDon> hoaDons = orderService.HoaDon(idOder);

            return response.generateResponse("get list order item Successfully", HttpStatus.OK, hoaDons);

        }catch (Exception e) {
            return response.generateResponse("get list order item failed"+e.getMessage(), HttpStatus.BAD_REQUEST, null);

        }
    }


    @GetMapping ("/wait-confirm")
    public ResponseEntity<Object> DonHangChoXacNhan() {
        try {

            List<Order1> list = orderService.listOrderWaitConfirm();

            return response.generateResponse("get list order wait confirm", HttpStatus.OK, list);

        }catch (Exception e) {
            return response.generateResponse("get list order wait confirm failed"+e.getMessage(), HttpStatus.BAD_REQUEST, null);

        }
    }

    @GetMapping ("/done-confirm")
    public ResponseEntity<Object> DonHangDaXacNhan() {
        try {

            List<Order1> list = orderService.listOrderConfirmed();

            return response.generateResponse("get list order wait confirm", HttpStatus.OK, list);

        }catch (Exception e) {
            return response.generateResponse("get list order wait confirm failed"+e.getMessage(), HttpStatus.BAD_REQUEST, null);

        }
    }


    @PostMapping("/confirm")
    public ResponseEntity<Object> confirmByEmployee(@RequestBody Map<String, String> body) {


        try {
            long idSeler = Long.parseLong(body.get("id_seller"));

            long idOder = Long.parseLong(body.get("id_order"));
            long idPaymentMethod = Long.parseLong(body.get("idPaymentMethod"));
            float paymentAmount = orderService.total_order(idOder);
            Boolean confirmOrder= orderService.confirmOrder(idSeler,
                    paymentAmount,idOder ,idPaymentMethod);


           Order1 order1 = orderService.findByID(idOder);
           User Customer = userService.getUserById(order1.getUser().getId());

            try {
                EmailDetails details = new EmailDetails();
                details.setSubject("ĐẶT MUA HÀNG THÀNH CÔNG !!!");
                details.setMsgBody("Bạn đã đặt mua sản phẩm :" +
                        "\n\nĐơn hàng sẽ sớm được gửi cho bạn, cảm ơn quý khách hàng đã tin tưởng và ủng hộ Shop");
                details.setRecipient(Customer.getEmail());
                Boolean status
                        = emailService.sendSimpleMail(details);;

                return response.generateResponse("confirmOrder product Successfully !", HttpStatus.OK, status);
            } catch (Exception e) {
                return response.generateResponse(" confirmOrder sent mail failed Provide accout failed" + e.getMessage(), HttpStatus.BAD_REQUEST, true);
            }


//            return response.generateResponse("confirmOrder product Successfully", HttpStatus.OK, true);

        }catch (Exception e) {
            return response.generateResponse("confirmOrder product fail"+e.getMessage(), HttpStatus.BAD_REQUEST, null);

        }
    }

}
