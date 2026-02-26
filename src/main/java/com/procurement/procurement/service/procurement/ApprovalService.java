package com.procurement.procurement.service.procurement;

import com.procurement.procurement.entity.procurement.Approval;
import com.procurement.procurement.repository.procurement.ApprovalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApprovalService {

    private final ApprovalRepository approvalRepository;

    public ApprovalService(ApprovalRepository approvalRepository) {
        this.approvalRepository = approvalRepository;
    }

    public Approval createApproval(Approval approval) {
        approval.setStatus("PENDING");
        approval.setApprovedAt(null);
        return approvalRepository.save(approval);
    }

    public Approval updateApprovalStatus(Long approvalId, String status) {

        Approval approval = approvalRepository.findById(approvalId)
                .orElseThrow(() ->
                        new RuntimeException("Approval not found with id: " + approvalId));

        approval.setStatus(status);

        if ("APPROVED".equalsIgnoreCase(status)) {
            approval.setApprovedAt(LocalDateTime.now());
        }

        if ("REJECTED".equalsIgnoreCase(status)) {
            approval.setApprovedAt(null);
        }

        return approvalRepository.save(approval);
    }

    public List<Approval> getAllApprovals() {
        return approvalRepository.findAll();
    }

    public List<Approval> getApprovalsByPurchaseOrderId(Long purchaseOrderId) {
        return approvalRepository.findByPurchaseOrderId(purchaseOrderId);
    }

    public List<Approval> getApprovalsByRequisitionId(Long requisitionId) {
        return approvalRepository.findByRequisitionId(requisitionId);
    }
}