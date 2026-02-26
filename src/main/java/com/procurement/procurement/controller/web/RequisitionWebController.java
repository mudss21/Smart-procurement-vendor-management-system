package com.procurement.procurement.controller.web;

import com.procurement.procurement.entity.procurement.Requisition;
import com.procurement.procurement.service.procurement.RequisitionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/requisition")
public class RequisitionWebController {

    private final RequisitionService requisitionService;

    public RequisitionWebController(RequisitionService requisitionService) {
        this.requisitionService = requisitionService;
    }

    // Load page
    @GetMapping
    public String requisitionPage(Model model) {

        List<Requisition> requisitions = requisitionService.getAllRequisitions();
        model.addAttribute("requisitions", requisitions);

        return "requisition";
    }

    // Create requisition
    @PostMapping
    public String createRequisition(@ModelAttribute Requisition requisition) {

        requisition.setStatus("PENDING");
        requisitionService.createRequisition(requisition);

        return "redirect:/requisition";
    }
}