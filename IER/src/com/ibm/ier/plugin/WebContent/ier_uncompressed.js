define([
	"dojo/_base/declare",
	"ier/constants",
	"ier/messages",
	"ier/util/util",
	"ier/util/dialog",
	"ier/util/menu",
	"ier/util/property",
	
	"ier/model/DispositionSchedule",
	"ier/model/FilePlan",
	"ier/model/FilePlanRepositoryMixin",
	"ier/model/FilePlansTreeModel",
	"ier/model/IERRepositoryMixin",
	"ier/model/Hold",
	"ier/model/Location",
	"ier/model/NamingPattern",
	"ier/model/RecordCategory",
	"ier/model/RecordVolume",
	"ier/model/RecordFolder",
	"ier/model/Record",
	"ier/model/RecordCategoryContainerMixin",
	"ier/model/RecordFolderContainerMixin",
	"ier/model/RecordContainerMixin",
	"ier/model/RecordEntryTemplate",
	"ier/model/ReportDefinition",
	"ier/model/IERDesktopMixin",
	"ier/model/actions/DeclareAction",
	"ier/model/actions/PlaceOnHoldAction",
	"ier/model/actions/RemoveHoldAction",
	"ier/model/actions/RecordPropertiesAction",
	
	"ier/widget/layout/CommonActionsHandler",
	"ier/widget/layout/FilePlanPane",
	"ier/widget/layout/FilePlanFlyoutPane",
	"ier/widget/layout/JobsFlyoutPane",
	"ier/widget/layout/TaskPane",
	"ier/widget/layout/ConfigureFlyoutPane",
	"ier/widget/layout/ConfigurePane",
	"ier/widget/layout/ReportsFlyoutPane",
	"ier/widget/layout/ReportLayout",
	"ier/widget/layout/ReportPane",
	"ier/widget/layout/SearchPane",
	"ier/widget/layout/DashboardPane",
	"ier/widget/layout/IERMainLayout",
	"ier/widget/layout/FavoritePane",
	"ier/widget/layout/AdminiPane",
	"ier/widget/listView/modules/TaskFilter",
	"ier/widget/tasks/MultiStatusTasksListingPane",
	"ier/widget/tasks/RecurringTasksListingPane",
	"ier/widget/tasks/TasksListingPane",
	"ier/widget/tasks/ReportTasksListingPane",
	"ier/widget/tasks/ReportTaskInformationPane",
	"ier/widget/tasks/DDSweepTaskInformationPane",
	"ier/widget/tasks/DefensibleDisposalTasksListingPane",
	"ier/widget/tasks/CompletedTasksListingPane",
	
	"ier/widget/admin/PluginConfigurationPane",
	"ier/widget/admin/config",
	"ier/widget/admin/AdminGrid",
	"ier/widget/admin/AdminCheckBox",
	"ier/widget/admin/GeneralSettingsPane",
	"ier/widget/admin/RepositorySettingsPane",
	"ier/widget/admin/SystemConfigPane",
	"ier/widget/admin/SystemPropertyPane",
	
	"ier/widget/CognosViewer",
	"ier/widget/BaseReportViewer"
	
], function(){

});