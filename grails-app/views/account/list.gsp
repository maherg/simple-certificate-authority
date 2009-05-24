

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Account List</title>
    </head>
    <body>
      <g:render template="header" />
        <div class="body">
            <h1>Account List</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                   	        <g:sortableColumn property="id" title="Id" />
                        
                   	        <g:sortableColumn property="username" title="Username" />
                        
                   	        <g:sortableColumn property="password" title="Password" />
                        
                   	        <g:sortableColumn property="city" title="City" />
                        
                   	        <g:sortableColumn property="countryCode" title="Country Code" />

                                <th>Private Key</th>
                                <th>Public Key</th>
                                <th>Certificate</th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${accountInstanceList}" status="i" var="accountInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${accountInstance.id}">${fieldValue(bean:accountInstance, field:'id')}</g:link></td>
                        
                            <td>${fieldValue(bean:accountInstance, field:'username')}</td>
                        
                            <td>${fieldValue(bean:accountInstance, field:'password')}</td>
                        
                            <td>${fieldValue(bean:accountInstance, field:'city')}</td>
                        
                            <td>${fieldValue(bean:accountInstance, field:'countryCode')}</td>

                            <td>
                              <g:if test="${accountInstance.privateKeyStr}">
                                <b>Available</b>
                              </g:if>
                              <g:else>
                                <b>N/A</b>
                              </g:else>
                            </td>

                            <td>
                              <g:if test="${accountInstance.publicKeyStr}">
                                <b>Available</b>
                              </g:if>
                              <g:else>
                                <b>N/A</b>
                              </g:else>
                            </td>

                            <td>
                              <g:if test="${accountInstance.certificateStr}">
                                <b>Available</b>
                              </g:if>
                              <g:else>
                                <b>N/A</b>
                              </g:else>
                            </td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${Account.count()}" />
            </div>
        </div>
    </body>
</html>
