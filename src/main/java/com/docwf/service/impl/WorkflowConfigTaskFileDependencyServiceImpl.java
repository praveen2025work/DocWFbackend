package com.docwf.service.impl;

import com.docwf.dto.WorkflowConfigTaskFileDependencyDto;
import com.docwf.entity.WorkflowConfigTaskFile;
import com.docwf.entity.WorkflowConfigTaskFileDependency;
import com.docwf.repository.WorkflowConfigTaskFileDependencyRepository;
import com.docwf.repository.WorkflowConfigTaskFileRepository;
import com.docwf.service.WorkflowConfigTaskFileDependencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkflowConfigTaskFileDependencyServiceImpl implements WorkflowConfigTaskFileDependencyService {
    
    @Autowired
    private WorkflowConfigTaskFileDependencyRepository dependencyRepository;
    
    @Autowired
    private WorkflowConfigTaskFileRepository fileRepository;
    
    @Override
    public WorkflowConfigTaskFileDependencyDto createDependency(WorkflowConfigTaskFileDependencyDto dependencyDto) {
        // Validate that both files exist
        if (!fileRepository.existsById(dependencyDto.getFileId())) {
            throw new RuntimeException("File not found: " + dependencyDto.getFileId());
        }
        
        WorkflowConfigTaskFile parentFile = fileRepository.findById(dependencyDto.getParentFileId())
                .orElseThrow(() -> new RuntimeException("Parent file not found: " + dependencyDto.getParentFileId()));
        
        // Validate no circular dependency
        if (!validateDependencyChain(dependencyDto.getFileId(), dependencyDto.getParentFileId())) {
            throw new RuntimeException("Circular dependency detected");
        }
        
        WorkflowConfigTaskFileDependency dependency = new WorkflowConfigTaskFileDependency();
        dependency.setFileId(dependencyDto.getFileId());
        dependency.setParentFile(parentFile);
        dependency.setCreatedBy(dependencyDto.getCreatedBy() != null ? dependencyDto.getCreatedBy() : "system");
        
        WorkflowConfigTaskFileDependency savedDependency = dependencyRepository.save(dependency);
        return convertToDto(savedDependency);
    }
    
    @Override
    public List<WorkflowConfigTaskFileDependencyDto> createDependenciesWithSequenceMapping(
            List<WorkflowConfigTaskFileDependencyDto> dependencyDtos, 
            List<Long> fileIds) {
        
        List<WorkflowConfigTaskFileDependencyDto> createdDependencies = new ArrayList<>();
        
        for (WorkflowConfigTaskFileDependencyDto dependencyDto : dependencyDtos) {
            // Map sequence numbers to actual file IDs
            if (dependencyDto.getFileSequence() != null && dependencyDto.getParentFileSequence() != null) {
                int fileIndex = dependencyDto.getFileSequence() - 1;
                int parentFileIndex = dependencyDto.getParentFileSequence() - 1;
                
                if (fileIndex >= 0 && fileIndex < fileIds.size() && 
                    parentFileIndex >= 0 && parentFileIndex < fileIds.size()) {
                    
                    dependencyDto.setFileId(fileIds.get(fileIndex));
                    dependencyDto.setParentFileId(fileIds.get(parentFileIndex));
                    
                    WorkflowConfigTaskFileDependencyDto created = createDependency(dependencyDto);
                    createdDependencies.add(created);
                }
            }
        }
        
        return createdDependencies;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowConfigTaskFileDependencyDto> getDependenciesByFileId(Long fileId) {
        return dependencyRepository.findByFileId(fileId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowConfigTaskFileDependencyDto> getDependenciesByParentFileId(Long parentFileId) {
        return dependencyRepository.findByParentFileFileId(parentFileId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowConfigTaskFileDependencyDto> getDependenciesByTaskId(Long taskId) {
        return dependencyRepository.findByTaskId(taskId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<WorkflowConfigTaskFileDependencyDto> getDependenciesByWorkflowId(Long workflowId) {
        return dependencyRepository.findByWorkflowId(workflowId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public WorkflowConfigTaskFileDependencyDto updateDependency(Long dependencyId, WorkflowConfigTaskFileDependencyDto dependencyDto) {
        // Find by fileId since it's now the primary key
        WorkflowConfigTaskFileDependency dependency = dependencyRepository.findById(dependencyDto.getFileId())
                .orElseThrow(() -> new RuntimeException("Dependency not found"));
        
        if (dependencyDto.getUpdatedBy() != null) {
            dependency.setUpdatedBy(dependencyDto.getUpdatedBy());
        }
        
        WorkflowConfigTaskFileDependency updatedDependency = dependencyRepository.save(dependency);
        return convertToDto(updatedDependency);
    }
    
    @Override
    public void deleteDependency(Long dependencyId) {
        // Delete by fileId since it's now the primary key
        dependencyRepository.deleteById(dependencyId);
    }
    
    public void deleteDependency(Long fileId, Long parentFileId) {
        // Find and delete by fileId and parentFileId
        List<WorkflowConfigTaskFileDependency> dependencies = dependencyRepository.findByFileId(fileId);
        for (WorkflowConfigTaskFileDependency dep : dependencies) {
            if (dep.getParentFile() != null && dep.getParentFile().getFileId().equals(parentFileId)) {
                dependencyRepository.delete(dep);
                return;
            }
        }
        throw new RuntimeException("Dependency not found");
    }
    
    @Override
    public void deleteDependenciesByFileId(Long fileId) {
        dependencyRepository.deleteByFileId(fileId);
    }
    
    @Override
    public void deleteDependenciesByParentFileId(Long parentFileId) {
        dependencyRepository.deleteByParentFileFileId(parentFileId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean validateDependencyChain(Long fileId, Long parentFileId) {
        // Check if adding this dependency would create a circular reference
        List<Long> parentChain = getDependencyChain(parentFileId);
        return !parentChain.contains(fileId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Long> getDependencyChain(Long fileId) {
        List<Long> chain = new ArrayList<>();
        List<WorkflowConfigTaskFileDependency> dependencies = dependencyRepository.findByFileId(fileId);
        
        for (WorkflowConfigTaskFileDependency dependency : dependencies) {
            Long parentFileId = dependency.getParentFile().getFileId();
            chain.add(parentFileId);
            chain.addAll(getDependencyChain(parentFileId));
        }
        
        return chain;
    }
    
    private WorkflowConfigTaskFileDependencyDto convertToDto(WorkflowConfigTaskFileDependency dependency) {
        WorkflowConfigTaskFileDependencyDto dto = new WorkflowConfigTaskFileDependencyDto();
        dto.setFileId(dependency.getFileId());
        dto.setParentFileId(dependency.getParentFile() != null ? dependency.getParentFile().getFileId() : null);
        dto.setCreatedBy(dependency.getCreatedBy());
        dto.setUpdatedBy(dependency.getUpdatedBy());
        return dto;
    }
}
