package com.shop.sport.Service;

import com.shop.sport.DTO.BestSell;
import com.shop.sport.DTO.HoaDon;
import com.shop.sport.DTO.OrderDTO;
import com.shop.sport.Entity.Order1;
import com.shop.sport.Entity.OrderItem;
import com.shop.sport.Repositories.IOrderItem;
import com.shop.sport.Repositories.OrderReopository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderReopository orderReopository;

    @Autowired
    private IOrderItem orderItemRepo;


    public Boolean insertOrder(long idUser, String adress, long idShipMethod, String phone, String name_reciver,
                            String productIds, String quantities ) {
            try {
                orderReopository.insert_to_order_order_item(adress,idUser,idShipMethod,phone,name_reciver, productIds, quantities);
                return true;
            }catch (Exception e) {
                return false;
            }

    }

    public Order1 findByID(long id) {
        return  orderReopository.findById(id).orElse(null);
    }

    public List<OrderDTO> getOrder_byIdUser(long idUser) {
            return orderReopository.getOrder_byIdUser(idUser);

    }

    public List<BestSell> best_sell_month(int thang, int nam) {
        return orderReopository.best_sell_month(thang, nam);

    }

    public Long deleteOder(long id) {
        Long result = orderReopository.delete_order(id);
        return result;
    }

    public String get_data_chart( int nam) {
        return orderReopository.get_data_chart( nam);

    }

    public long getSumSell() {
        List<OrderItem> list = (List<OrderItem>) orderItemRepo.findAll();
//        long sum=0;
//        for (int i = 0; i < list.size(); i++) {
//            sum+=1;
//        }

        return list.toArray().length;

    }

    public List<HoaDon> HoaDon(long idOrder) {
        return orderReopository.hoadon(idOrder);

    }

    public List<Order1> listOrderWaitConfirm() {
        return orderReopository.findOrdersWithoutPayment();

    }

    public List<Order1> listOrderConfirmed() {
        return orderReopository.findOrdersPayment();

    }

    public Long total_order(long idOrder) {
        return orderReopository.total_order(idOrder);

    }

    public Boolean confirmOrder(long idSeller, float paymentAmount, long idOder, long idPaymentMethod) {
        try {
            orderReopository.comfirm_order(idSeller,paymentAmount,idOder,idPaymentMethod);

            return true;
        }catch (Exception e){
            return false;
        }
    }
}
