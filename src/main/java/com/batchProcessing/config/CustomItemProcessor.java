package com.batchProcessing.config;

import com.batchProcessing.model.Product;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Product, Product> {

    @Override
    public Product process(Product item) throws Exception {
        // data transformation logic
       try {
           double discountPercentage = Double.parseDouble(item.getDiscount());
           double originalPrice = Double.parseDouble(item.getPrice());
           double discount = (discountPercentage/100)*originalPrice;
           double finalPrice = originalPrice - discount;
           item.setDiscountedPrice(String.valueOf(finalPrice));
       }
       catch (NumberFormatException ex){
           ex.printStackTrace();
       }
        return item;
    }
}
