package src.main.java.org.example;

import java.util.Map;

import org.example.web.Request;
import org.example.workflow.flow.BasicCode;
import org.example.workflow.WorkflowReport;
import org.example.workflow.work.Context;
import org.example.workflow.work.WorkReport;

public class WonderfulCode extends BasicCode {
    
    private Map<String, String> options;
    private Request request;
    private Context context;
    private WorkReport workReport;

    @Autowire
    SearchQuery query;

    public MediocreCode(Logger logger) {
        super(logger);
        this.name = "Not Yet Ready For Prime";
        this.description = "Execute the basic retrieval of data from the downstream service making it available for processing";
    }

    @Override
    public WorkflowReport setup(WorkflowReport report) {
        workReport = new WorkReport();
        context = workContext.get(Context.class);
        if(null == context) {
            workReport.failed();
            report.updateReport(workReport);
            return report;
        }

        options = context.getOptions();
        search = new Search();
        if(options.containsKey("lastName")) {
            search.setLastName(options.get("lastName"));
        }
        if(options.containsKey("middleName")) {
            search.setLastName(options.get("middleName"));
        }
        if(options.containsKey("firstName")) {
            search.setLastName(options.get("firstName"));
        }

        context.addSearch(search);

        workReport = search.isValid() ? workReport.success() : workReport.failed();
        report.updateReport(workReport);

        return report;
    }

    @Override
    public WorkflowReport flow(WorkflowReport report) {

        query.addContext(context);
        workReport = query.search(search) ? workReport.success() : workReport.failed();

        return report.updateReport(workReport);
    }

    @Override
    public WorkflowReport exit(WorkflowReport report) {
        if(context.getSearchResponse().isValid()) {
            context.setResponse(context.getSearchResponse());
            context.setResponse(context.getDocResponse());

            report.update(workReport.success());
        } else report.update(workReport.failed());

        return report;
    }
}