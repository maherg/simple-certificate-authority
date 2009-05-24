

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>CA Logs</title>
        <script type="text/javascript">
            function reloadLogs()
            {
                window.location.reload();
            }
            setInterval( 'reloadLogs()' , 5000 );
        </script>
    </head>
    <body>
        <g:render template="/account/header" />
        <div class="body">
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <br />
            <g:link controller="logEntry" action="empty" id="" params="[]" >Empty Log</g:link>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <g:link controller="logEntry" action="list" id="" params="[]" >Refresh</g:link>
            <br />
            <br />
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="dateCreated" title="Timestamp" />
                        
                   	        <g:sortableColumn property="message" title="Message" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${logEntryInstanceList}" status="i" var="logEntryInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><pre>${logEntryInstance.dateCreated}</pre></td>
                        
                            <td><pre>${logEntryInstance.message}</pre></td>
                        
                        </tr>
                    </g:each>
                    <g:if test="${logEntryInstanceList.size() == 0}">
                        <tr><td colspan="2"><i>The log is empty</i></td></tr>
                    </g:if>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${LogEntry.count()}" />
            </div>
        </div>
    </body>
</html>
