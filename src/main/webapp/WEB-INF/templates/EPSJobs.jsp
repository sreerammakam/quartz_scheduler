<%@page import="org.quartz.Trigger.TriggerState" %>
<%@page import="org.quartz.impl.matchers.GroupMatcher" %>
<%@page import="org.quartz.CronTrigger" %>
<%@page import="org.quartz.JobDataMap" %>
<%@page import="org.springframework.context.ApplicationContext" %>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils" %>
<%@ page import="org.quartz.*" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Collections" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="com.google.common.base.Throwables" %>

<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="en">

<%
    Logger log = LoggerFactory.getLogger(this.getClass().getName());
    try {
        ApplicationContext context = RequestContextUtils.findWebApplicationContext(request);
        Scheduler scheduler = (Scheduler) context.getBean("epsQuartzScheduler");

        String action = request.getParameter("action");
        String groupParam = request.getParameter("group");
        String jobParam = request.getParameter("job");
        if (groupParam != null && jobParam != null) {
            for (String group : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group))) {
                    if (jobParam.contains(jobKey.getName()) && groupParam.equalsIgnoreCase(group)) {
                        if ("exec".equals(action)) {
                            if (scheduler.getTriggerState(scheduler.getTriggersOfJob(jobKey).get(0).getKey()) == TriggerState.COMPLETE ||
                                scheduler.getTriggerState(scheduler.getTriggersOfJob(jobKey).get(0).getKey()) == TriggerState.NORMAL) {
                                scheduler.triggerJob(jobKey);
                            }
                        } else if ("pause".equals(action)) {
                            if (scheduler.getTriggerState(scheduler.getTriggersOfJob(jobKey).get(0).getKey()) == TriggerState.PAUSED) {
                                scheduler.resumeTrigger(scheduler.getTriggersOfJob(jobKey).get(0).getKey());
                            } else {
                                scheduler.pauseTrigger(scheduler.getTriggersOfJob(jobKey).get(0).getKey());
                            }
                        }
                    }
                }
            }
        }
%>
<head>
    <title>EPS Jobs Information</title>
    <style>
        <%@include file="./EPSJobs.css"%>
    </style>
</head>
<body>
<h1>
    <a href=<%=request.getContextPath() %>/eps-scheduler/reference/jsp> EPS Jobs Information</a>
</h1>
<h1 class="error">
    Please do not reload the page after executing a job. To view the updated table, click the 'EPS Jobs Information'
    link at the top of the page!
</h1>
<p>Current Date Time:
    <b>
        <%=Calendar.getInstance().getTime()%>
    </b>
</p>
<p>Scheduler State:
    <b>
        <%=scheduler.isStarted() ? "Running" : "Not Running"%>
    </b>
</p>
<br/>
<table class="tableBorder">
    <caption>EPS Jobs</caption>
    <tr>
        <th class="tdBorder bold center">Group</th>
        <th class="tdBorder bold center">Job</th>
        <th class="tdBorder bold center">Cron Expression</th>
        <th class="tdBorder bold center">Previous Fire Time</th>
        <th class="tdBorder bold center">Next Fire Time</th>
        <th class="tdBorder bold center">Running</th>
        <th class="tdBorder bold center">Actions</th>
    </tr>
    <%
        List<String> groups = scheduler.getJobGroupNames();
        log.info("Job Groups=" + groups);
        Collections.sort(groups);

        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        log.info("Executing Jobs=" + executingJobs);

        for (String group : groups) {
            Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.jobGroupEquals(group));
            jobKeys.stream().sorted().collect(Collectors.toList());
            int i = 0;
            for (JobKey jobkey : jobKeys) {
                int j = 0;
                if (!scheduler.getTriggersOfJob(jobkey).isEmpty()) {
                    Trigger trigger = scheduler.getTrigger(scheduler.getTriggersOfJob(jobkey).get(j).getKey());
                    JobDetail jobDetail = scheduler.getJobDetail(jobkey);

                    int numInstances = 0;
                    for (JobExecutionContext jobExecutionContext : executingJobs) {
                        JobDetail execJobDetail = jobExecutionContext.getJobDetail();
                        if (execJobDetail.getKey().equals(jobDetail.getKey())) {
                            numInstances++;
                        }
                    }
                    boolean isPaused = scheduler.getTriggerState(scheduler.getTriggersOfJob(jobkey).get(0).getKey()) == TriggerState.PAUSED;
    %>
    <tr>
        <%
            if (i == 0) {
        %>
        <td class="tdBorder" rowspan="<%=jobKeys.size()%>"><%=group%>
        </td>
        <%
            }
        %>
        <td class="tdBorder <%=isPaused ? "error bold" : ""%>"><%=jobkey%>
        </td>
        <td class="tdBorder"><%=trigger != null && trigger instanceof CronTrigger ? ((CronTrigger) trigger).getCronExpression() : "N/A"%>
        </td>
        <td class="tdBorder"><%=trigger != null ? (trigger.getPreviousFireTime() != null ? trigger.getPreviousFireTime() : "N/A") : "N/A"%>
        </td>
        <td class="tdBorder"><%=trigger != null ? trigger.getNextFireTime() : "&nbsp;"%>
        </td>
        <td class="tdBorder center"><%=numInstances > 0 ? "<b>YES (" + numInstances + "Instance)</b>" : "NO"%>
        </td>
        <td class="tdBorder center"><a
                href="jsp?action=exec&group=<%=group%>&job=<%=jobkey%>">Execute</a>&nbsp;|&nbsp;<a
                href="jsp?action=pause&group=<%=group%>&job=<%=jobkey%>"><%=isPaused ? "Resume" : "Pause"%>
        </a>
        </td>
    </tr>
    <%
                    i++;
                }
                j++;
            }
        }
    %>
</table>
</body>
<%
    } catch (SchedulerException se) {
        log.error("SchedulerException: {}" + Throwables.getStackTraceAsString(se));
    }
%>
<br>
<br>
<br>
<p>&copy; 2025 Laboratory Corporation of America® Holdings.
<p>
</html>
