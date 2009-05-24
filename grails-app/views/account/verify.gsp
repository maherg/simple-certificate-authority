

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Verify Certificate</title>         
    </head>
    <body>
      <g:render template="header" />
        <div class="body">
            <h1>Verify Certificate</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:form action="verify" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="username">Certificate ( Base64 Encoded ):</label>
                                </td>
                                <td valign="top" class="value">
                                  <textarea id="certificate" class="certificate" name="certificate">${certificate ? certificate : ''}</textarea>
                                </td>
                            </tr> 
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button">
                      <span class="button"><g:actionSubmit class="verify" value="Verify" /></span>
                    </span>
                </div>
            </g:form>
        </div>
    </body>
</html>
