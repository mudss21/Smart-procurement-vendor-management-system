package com.procurement.procurement.controller.web;

import com.procurement.procurement.entity.procurement.PurchaseOrder;
import com.procurement.procurement.service.procurement.PurchaseOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/purchase-order")
public class PurchaseOrderWebController {

    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderWebController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @GetMapping
    public String viewPurchaseOrders(Model model) {
        model.addAttribute("orders",
                purchaseOrderService.getAllPurchaseOrders());
        return "purchase-order";
    }

    @PostMapping
    public String createPO(@RequestParam String poNumber) {

        PurchaseOrder po = new PurchaseOrder();
        po.setPoNumber(poNumber);
        po.setStatus("PENDING");

        purchaseOrderService.createPurchaseOrder(po);

        return "redirect:/purchase-order";
    }
}