CREATE TABLE [M_OperManualDetails] (
[Job_stepid] bigint NOT NULL,
[Manual_id] bigint NOT NULL,
[Job_step_seq] varchar(20) NOT NULL,
[Job_step_name] varchar(50) NOT NULL,
[Job_step_gmt] varchar(50) NOT NULL,
[Job_step_manual] bit NOT NULL,
[Manual_image] varchar(200) NULL,
[Job_step_waittime] varchar(20) NULL,
[Automatic_judglogic] varchar(50) NULL,
[Processes_tag] varchar(20) NOT NULL,
[Imgsave_address] varchar(200) NULL,
[Remarks] varchar(200) NULL,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([Job_stepid]) 
)
GO

CREATE TABLE [M_OperManual] (
[Manual_id] bigint NOT NULL IDENTITY(1,1),
[Manual_no] varchar(20) NOT NULL,
[Manual_desc] varchar(200) NULL,
[Manual_version] varchar(20) NOT NULL,
[Product_id] bigint NOT NULL,
[Product_version] varchar(20) NOT NULL,
[Product_name] varchar(50) NOT NULL,
[Bom_id] bigint NOT NULL,
[Bom_name] varchar(50) NOT NULL,
[Bom_version] varchar(20) NOT NULL,
[Sub_lotid] bigint NOT NULL,
[Step_count] bigint NOT NULL,
[Remarks] varchar(200) NULL,
[Using_flg] smallint NOT NULL DEFAULT 0,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([Manual_id]) 
)
GO

CREATE TABLE [M_OperManualTool] (
[Manual_toolid] bigint NOT NULL,
[Matter_toolcode] varchar(50) NOT NULL,
[HardwareMST_id] int NOT NULL,
[Job_stepid] bigint NOT NULL,
[Scan_check] bit NOT NULL,
[Count] bigint NOT NULL,
[Tool_param1_l] real NULL,
[Tool_param1_u] real NULL,
[Tool_param2_l] real NULL,
[Tool_param2_u] real NULL,
[Tool_param3_l] real NULL,
[Tool_param3_u] real NULL,
[Tool_judylogic] varchar(50) NULL,
[Program_numbre] varchar(50) NOT NULL,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([Manual_toolid]) 
)
GO

CREATE TABLE [N_OperManualMaterials] (
[Manual_matterid] bigint NOT NULL,
[Manual_typecode] varchar(50) NOT NULL,
[Material_id] bigint NOT NULL,
[Matter_qty] bigint NOT NULL,
[Reclaim_count] bit NOT NULL,
[Matter_judylogic] varchar(50) NOT NULL,
[Scanning_check] bit NOT NULL,
[Job_stepid] bigint NOT NULL,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([Manual_matterid]) 
)
GO

CREATE TABLE [M_OperManualCheckMap] (
[Mes_id] bigint NOT NULL,
[Job_stepid] bigint NOT NULL,
[Matter_code_traceability] varchar(50) NULL,
[Position_no] bigint NULL,
[Bar_code_read] bit NULL,
[Start_no] bigint NULL,
[Length] bigint NULL,
[Check_map_no] bit NOT NULL,
[Img_type] varchar(50) NULL,
[Code] varchar(50) NULL,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([Mes_id]) 
)
GO

CREATE TABLE [M_JobAssignment] (
[Job_signment_id] bigint NOT NULL,
[Manual_id] bigint NOT NULL,
[Product_code] varchar(20) NOT NULL,
[Job_startup_procedure] bigint NOT NULL,
[Job_end_step] bigint NOT NULL,
[Job_all_step] bit NULL,
[Job_gmt] varchar(50) NOT NULL,
[Remarks] varchar(50) NULL,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([Job_signment_id]) 
)
GO

CREATE TABLE [M_JobLogicSetting] (
[Logic_id] bigint NOT NULL,
[Job_stepid] bigint NOT NULL,
[Logic_character] varchar(50) NOT NULL,
[Type] bigint NOT NULL,
[Process_obj] bigint NOT NULL,
[Judge_sign] varchar(50) NOT NULL,
[Judge_value] varchar(50) NOT NULL,
[Process_order] varchar(50) NOT NULL,
[Remarks] varchar(50) NULL,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([Logic_id]) 
)
GO

CREATE TABLE [M_OperType] (
[Type_id] bigint NOT NULL,
[Type_no] varchar(10) NOT NULL,
[Type_name] varchar(50) NOT NULL,
[Using_flg] smallint NOT NULL DEFAULT 0,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([Type_id]) 
)
GO

CREATE TABLE [M_HardwareMST] (
[HardwareMST_id] bigint NOT NULL,
[HardwareMST_no] varchar(10) NOT NULL,
[HardwareMST_name] varchar(50) NOT NULL,
[HardwareMST_spec] varchar(50) NOT NULL,
[HardwareMST_type] varchar(50) NOT NULL,
[Using_flg] smallint NOT NULL DEFAULT 0,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([HardwareMST_id]) 
)
GO

CREATE TABLE [M_communication] (
[Communication_id] bigint NOT NULL,
[Communication_name] varchar(50) NOT NULL,
[Using_flg] smallint NOT NULL DEFAULT 0,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([Communication_id]) 
)
GO

CREATE TABLE [M_HardwareManagement] (
[Hw_id] bigint NOT NULL,
[HardwareMST_id] bigint NOT NULL,
[Hw_model] varchar(50) NOT NULL,
[Hw_state] bigint NOT NULL,
[Using_flg] smallint NOT NULL DEFAULT 0,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([Hw_id]) 
)
GO

CREATE TABLE [M_ProductsMST] (
[Product_id] bigint NOT NULL,
[Product_code] varchar(50) NOT NULL,
[Product_name] varchar(50) NOT NULL,
[Product_info] varchar(200) NULL,
[Using_flg] smallint NOT NULL,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([Product_id]) 
)
GO

CREATE TABLE [M_MatterMST] (
[Material_id] bigint NOT NULL,
[Material_code] varchar(50) NOT NULL,
[Material_name] varchar(50) NOT NULL,
[Material_info] varchar(200) NULL,
[Material_image] varchar(200) NULL,
[Using_flg] smallint NOT NULL DEFAULT 0,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([Material_id]) 
)
GO

CREATE TABLE [M_BomMST] (
[Bom_id] bigint NOT NULL,
[Bom_no] varchar(10) NOT NULL,
[Bom_name] varchar(50) NOT NULL,
[Bom_version] varchar(50) NOT NULL,
[Product_id] bigint NOT NULL,
[Remarks] varchar(200) NULL,
[Using_flg] smallint NOT NULL DEFAULT 0,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([Bom_id]) 
)
GO

CREATE TABLE [M_BomDetails] (
[BomDetails_id] bigint NOT NULL,
[Bom_id] bigint NOT NULL,
[OperModel_id] bigint NOT NULL,
[Using_flg] smallint NOT NULL DEFAULT 0,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([BomDetails_id]) 
)
GO

CREATE TABLE [M_AreaMST] (
[Area_id] bigint NOT NULL,
[Area_code] varchar(50) NOT NULL,
[Area_name] varchar(50) NOT NULL,
[Remarks] varchar(200) NULL,
[Using_flg] smallint NOT NULL,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([Area_id]) 
)
GO

CREATE TABLE [M_Matter] (
[Matter_id] bigint NOT NULL,
[Material_id] bigint NOT NULL,
[Bom_id] bigint NOT NULL,
[Using_flg] smallint NOT NULL DEFAULT 0,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([Matter_id]) 
)
GO

CREATE TABLE [M_Workbench] (
[Workbench_id] bigint NOT NULL,
[Area_id] bigint NOT NULL,
[Workbench_no] varchar(20) NOT NULL,
[Workbench_name] varchar(50) NOT NULL,
[Workbench_desc] varchar(200) NOT NULL,
[Using_flg] smallint NOT NULL DEFAULT 0,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([Workbench_id]) 
)
GO

CREATE TABLE [M_WorkbenchTools] (
[WorkbenchTools_id] bigint NOT NULL,
[Workbench_id] bigint NOT NULL,
[Hw_id] bigint NOT NULL,
[Communication_id] bigint NOT NULL,
[Ip_address] varchar(20) NULL,
[Ip_port] varchar(20) NULL,
[Ok_address] varchar(20) NULL,
[Ng_address] varchar(20) NULL,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([WorkbenchTools_id]) 
)
GO

CREATE TABLE [M_MaterialBox] (
[MaterialBox_id] bigint NOT NULL,
[MaterialBox_no] varchar(20) NOT NULL,
[OperModel_id] bigint NOT NULL,
[Matterinput] varchar(20) NOT NULL,
[Matteroutput] varchar(20) NOT NULL,
[Feedinput] varchar(20) NOT NULL,
[Feedoutput] varchar(20) NOT NULL,
[Using_flg] smallint NOT NULL DEFAULT 0,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([MaterialBox_id]) 
)
GO

CREATE TABLE [M_OperModel] (
[OperModel_id] bigint NOT NULL,
[OperModel_name] varchar(50) NOT NULL,
[Remarks] varchar(200) NULL,
[Using_flg] smallint NOT NULL DEFAULT 0,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([OperModel_id]) 
)
GO

CREATE TABLE [P_ProductOrder] (
[Order_id] bigint NOT NULL,
[Order_no] varchar(30) NOT NULL,
[Order_dat] date NOT NULL,
[Customer_info] varchar(100) NOT NULL,
[Order_qty] bigint NOT NULL,
[Completed_qty] bigint NOT NULL DEFAULT 0,
[Order_status] smallint NOT NULL DEFAULT 0,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([Order_id]) 
)
GO

CREATE TABLE [P_Plan] (
[Plan_id] bigint NOT NULL,
[Plan_type] smallint NOT NULL,
[Order_id] bigint NOT NULL,
[Manual_id] bigint NOT NULL,
[Lot_no] varchar(50) NOT NULL,
[Plan_qty] bigint NOT NULL,
[Completed_qty] bigint NOT NULL DEFAULT 0,
[Plan_status] smallint NOT NULL DEFAULT 0,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([Plan_id]) 
)
GO

CREATE TABLE [P_PlanAllocation] (
[PlanAllocation_id] bigint NOT NULL,
[Workbench_id] bigint NOT NULL,
[Plan_id] bigint NOT NULL,
[Workorder] varchar(20) NOT NULL,
[Plan_qty] bigint NOT NULL,
[Completed_qty] bigint NOT NULL DEFAULT 0,
[Inst_dat] datetime NULL,
[Upd_dat] datetime NULL,
PRIMARY KEY ([PlanAllocation_id]) 
)
GO

CREATE TABLE [R_OperPerformance] (
[OperPerformance_id] bigint NOT NULL,
[Collect_time] datetime NOT NULL,
[Workbench_id] bigint NOT NULL,
[Oper_id] bigint NOT NULL,
[Plan_id] bigint NOT NULL,
[Sub_lot_id] varchar(50) NOT NULL,
[Step_seq] bigint NOT NULL,
[process_seq] bigint NOT NULL,
[Result] smallint NOT NULL,
[Collect_content1] varchar(20) NULL,
[Collect_content2] varchar(20) NULL,
[Collect_content3] varchar(20) NULL,
PRIMARY KEY ([OperPerformance_id]) 
)
GO

CREATE TABLE [R_JobPerformance] (
[JobPerformance_id] bigint NOT NULL,
[Workbench_id] bigint NOT NULL,
[Oper_id] bigint NOT NULL,
[Plan_id] bigint NOT NULL,
[Sub_lot_id] varchar(50) NOT NULL,
[Start_time] datetime NOT NULL,
[End_time] datetime NULL,
[Ng_count] bigint NOT NULL DEFAULT 0,
PRIMARY KEY ([JobPerformance_id]) 
)
GO

