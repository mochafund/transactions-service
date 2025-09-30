package com.mochafund.transactionsservice.reference.service;

import com.mochafund.transactionsservice.common.exception.BadRequestException;
import com.mochafund.transactionsservice.common.exception.ResourceNotFoundException;
import com.mochafund.transactionsservice.reference.consumers.payloads.AccountEventPayload;
import com.mochafund.transactionsservice.reference.consumers.payloads.CategoryEventPayload;
import com.mochafund.transactionsservice.reference.consumers.payloads.TagEventPayload;
import com.mochafund.transactionsservice.reference.consumers.payloads.WorkspaceEventPayload;
import com.mochafund.transactionsservice.reference.entity.WorkspaceAccountId;
import com.mochafund.transactionsservice.reference.entity.WorkspaceAccountReference;
import com.mochafund.transactionsservice.reference.entity.WorkspaceCategoryId;
import com.mochafund.transactionsservice.reference.entity.WorkspaceCategoryReference;
import com.mochafund.transactionsservice.reference.entity.WorkspaceReference;
import com.mochafund.transactionsservice.reference.entity.WorkspaceTagId;
import com.mochafund.transactionsservice.reference.entity.WorkspaceTagReference;
import com.mochafund.transactionsservice.reference.repository.WorkspaceAccountRepository;
import com.mochafund.transactionsservice.reference.repository.WorkspaceCategoryRepository;
import com.mochafund.transactionsservice.reference.repository.WorkspaceReferenceRepository;
import com.mochafund.transactionsservice.reference.repository.WorkspaceTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class WorkspaceMetadataService {

    private final WorkspaceReferenceRepository workspaceRepository;
    private final WorkspaceAccountRepository accountRepository;
    private final WorkspaceCategoryRepository categoryRepository;
    private final WorkspaceTagRepository tagRepository;

    @Transactional
    public void upsertWorkspace(WorkspaceEventPayload payload) {
        WorkspaceReference reference = workspaceRepository.findById(payload.getWorkspaceId())
                .orElse(WorkspaceReference.builder()
                        .id(payload.getWorkspaceId())
                        .build());

        workspaceRepository.save(reference);
    }

    @Transactional
    public void deleteWorkspace(UUID workspaceId) {
        if (!workspaceRepository.existsById(workspaceId)) {
            return;
        }
        workspaceRepository.deleteById(workspaceId);
    }

    @Transactional
    public void upsertAccount(AccountEventPayload payload) {
        ensureWorkspaceExists(payload.getWorkspaceId());
        WorkspaceAccountReference reference = WorkspaceAccountReference.builder()
                .id(new WorkspaceAccountId(payload.getWorkspaceId(), payload.getId()))
                .build();

        accountRepository.save(reference);
    }

    @Transactional
    public void removeAccount(AccountEventPayload payload) {
        accountRepository.deleteById(new WorkspaceAccountId(payload.getWorkspaceId(), payload.getId()));
    }

    @Transactional
    public void upsertCategory(CategoryEventPayload payload) {
        ensureWorkspaceExists(payload.getWorkspaceId());
        WorkspaceCategoryReference reference = WorkspaceCategoryReference.builder()
                .id(new WorkspaceCategoryId(payload.getWorkspaceId(), payload.getId()))
                .build();

        categoryRepository.save(reference);
    }

    @Transactional
    public void removeCategory(CategoryEventPayload payload) {
        categoryRepository.deleteById(new WorkspaceCategoryId(payload.getWorkspaceId(), payload.getId()));
    }

    @Transactional
    public void upsertTag(TagEventPayload payload) {
        ensureWorkspaceExists(payload.getWorkspaceId());
        WorkspaceTagReference reference = WorkspaceTagReference.builder()
                .id(new WorkspaceTagId(payload.getWorkspaceId(), payload.getId()))
                .build();

        tagRepository.save(reference);
    }

    @Transactional
    public void removeTag(TagEventPayload payload) {
        tagRepository.deleteById(new WorkspaceTagId(payload.getWorkspaceId(), payload.getId()));
    }

    @Transactional(readOnly = true)
    public void assertWorkspaceExists(UUID workspaceId) {
        if (!workspaceRepository.existsById(workspaceId)) {
            throw new ResourceNotFoundException("Workspace not found");
        }
    }

    @Transactional(readOnly = true)
    public void assertAccountBelongsToWorkspace(UUID workspaceId, UUID accountId) {
        if (!accountRepository.existsById(new WorkspaceAccountId(workspaceId, accountId))) {
            throw new BadRequestException("Account does not belong to workspace");
        }
    }

    @Transactional(readOnly = true)
    public void assertCategoryBelongsToWorkspace(UUID workspaceId, UUID categoryId) {
        if (categoryId == null) {
            return;
        }
        if (!categoryRepository.existsById(new WorkspaceCategoryId(workspaceId, categoryId))) {
            throw new BadRequestException("Category does not belong to workspace");
        }
    }

    private void ensureWorkspaceExists(UUID workspaceId) {
        workspaceRepository.findById(workspaceId)
                .orElseGet(() -> workspaceRepository.save(WorkspaceReference.builder()
                        .id(workspaceId)
                        .build()));
    }
}
