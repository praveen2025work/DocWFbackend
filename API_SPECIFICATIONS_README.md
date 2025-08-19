# DocWF API Specifications

This directory contains the complete OpenAPI 3.0.1 specifications for the DocWF Workflow Management System, organized into separate entity-specific files for better maintainability and easier navigation.

## 📁 File Structure

### Main Files
- **`main-api-specification.json`** - Main API specification with overview and module references
- **`user-management-api.json`** - Complete user management API specification
- **`workflow-config-api.json`** - Complete workflow configuration API specification  
- **`workflow-execution-api.json`** - Complete workflow execution and task management API specification
- **`file-management-api.json`** - Complete file management API specification

## 🎯 Why Separate Files?

The original `api-specification.json` was causing Cursor to crash due to its large size (~1,500+ lines). By splitting it into logical modules:

- ✅ **Better Performance** - Each file loads quickly without crashing editors
- ✅ **Easier Maintenance** - Developers can focus on specific API areas
- ✅ **Better Organization** - Logical grouping by business domain
- ✅ **Team Collaboration** - Different teams can work on different API modules
- ✅ **Version Control** - Smaller, focused changes are easier to track

## 🔍 API Module Overview

### 1. User Management API (`user-management-api.json`)
**Endpoints:** 15 total
- User CRUD operations (Create, Read, Update, Delete)
- User search by username, email, role, workflow
- Escalation hierarchy management
- User status and availability checks
- Role-based user queries

**Key DTOs:**
- `WorkflowUserDto`

### 2. Workflow Configuration API (`workflow-config-api.json`)
**Endpoints:** 20+ total
- Workflow CRUD operations
- Workflow role management (assign, update, remove)
- Workflow task configuration (add, update, remove)
- Workflow parameter management
- Workflow status management

**Key DTOs:**
- `WorkflowConfigDto`
- `WorkflowConfigRoleDto`
- `WorkflowConfigTaskDto`
- `WorkflowConfigParamDto`

### 3. Workflow Execution API (`workflow-execution-api.json`)
**Endpoints:** 25+ total
- Workflow instance management (start, complete, cancel, escalate)
- Task assignment and execution (assign, start, complete, escalate)
- Workflow progress monitoring
- Task status management
- Workflow statistics and reporting
- User workload monitoring

**Key DTOs:**
- `WorkflowInstanceDto`
- `WorkflowInstanceTaskDto`
- `WorkflowProgressDto`
- `WorkflowInstanceStatsDto`
- `UserWorkloadDto`

### 4. File Management API (`file-management-api.json`)
**Endpoints:** 6 total
- File upload and download
- File updates and consolidation
- File metadata management
- Task instance file associations

**Key DTOs:**
- `WorkflowInstanceTaskFileDto`

## 🚀 How to Use

### For Development
1. **Start with `main-api-specification.json`** to understand the overall API structure
2. **Navigate to specific modules** based on your development needs
3. **Use individual files** for focused development work

### For Testing
- Each specification file can be imported into tools like:
  - Swagger UI
  - Postman
  - Insomnia
  - Any OpenAPI 3.0.1 compatible tool

### For Documentation
- Each file contains complete endpoint documentation
- All DTOs are fully defined with examples
- Response codes and error handling are documented

## 🔧 Technical Details

### OpenAPI Version
- All files use OpenAPI 3.0.1 specification
- Compatible with modern API development tools
- Follows REST API best practices

### Server Configuration
- Local development: `http://localhost:8080`
- Production: `https://api.docwf.com`

### Authentication
- Currently not implemented (to be added based on your security requirements)
- All endpoints are publicly accessible for now

## 📊 Coverage Statistics

| Module | Endpoints | DTOs | Coverage |
|--------|-----------|------|----------|
| User Management | 15 | 1 | 100% |
| Workflow Configuration | 20+ | 4 | 100% |
| Workflow Execution | 25+ | 5 | 100% |
| File Management | 6 | 1 | 100% |
| **Total** | **66+** | **11** | **100%** |

## 🆕 What's New

Compared to the original single file, these separate specifications now include:

- ✅ **All missing endpoints** that were implemented in controllers but not documented
- ✅ **Complete DTO definitions** with proper examples and validation
- ✅ **Better organization** by business domain
- ✅ **Improved maintainability** for future updates
- ✅ **Performance optimization** for development tools

## 🔄 Maintenance

### Adding New Endpoints
1. Identify the appropriate module file
2. Add the endpoint definition following the existing pattern
3. Update the corresponding DTO if needed
4. Update this README with new endpoint counts

### Updating DTOs
1. Modify the DTO in the relevant module file
2. Ensure all references are updated
3. Test with OpenAPI tools

### Version Management
- All files share the same version number (1.0.0)
- Update version numbers consistently across all files when making breaking changes

## 📞 Support

For questions about these API specifications:
- Check the individual specification files for detailed endpoint documentation
- Refer to the controller implementations in the source code
- Contact the development team for clarification

---

**Note:** These specifications represent the complete API surface of your DocWF system. All implemented endpoints from your controllers are now properly documented and organized for easy access and maintenance.
