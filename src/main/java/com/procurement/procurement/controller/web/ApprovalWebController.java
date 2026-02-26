package com.procurement.procurement.controller.web;

import com.procurement.procurement.entity.procurement.Approval;
import com.procurement.procurement.service.procurement.ApprovalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/approval")
public class ApprovalWebController {

    private final ApprovalService approvalService;

    public ApprovalWebController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    // ================= VIEW PAGE =================
    @GetMapping
    public String viewApprovals(Model model) {
        model.addAttribute("approvals",
                approvalService.getAllApprovals());
        return "approval";
    }

    // ================= UPDATE STATUS =================
    @PostMapping("/update")
    public String updateApprovalStatus(
            @RequestParam Long id,
            @RequestParam String status) {

        approvalService.updateApprovalStatus(id, status);

        return "redirect:/approval";
    }
}