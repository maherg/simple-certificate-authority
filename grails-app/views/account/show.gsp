

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Certificate Authority : Viewing Account : ${account.username}</title>
  </head>
  <body>
    <g:render template="header" />
    <div class="body">
      <h1>Viewing Account : ${account.username}</h1>
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
      <div class="dialog">
        <table>
          <tbody>

            
            <tr class="prop">
              <td valign="top" class="name">Username:</td>
              
              <td valign="top" class="value">${fieldValue(bean:account, field:'username')}</td>
              
            </tr>
            
            <tr class="prop">
              <td valign="top" class="name">Password:</td>
              
              <td valign="top" class="value">${fieldValue(bean:account, field:'password')}</td>
              
            </tr>

            <tr class="prop">
              <td valign="top" class="name">First Name:</td>
              
              <td valign="top" class="value">${fieldValue(bean:account, field:'firstName')}</td>
              
            </tr>
            
            <tr class="prop">
              <td valign="top" class="name">Last Name:</td>
              
              <td valign="top" class="value">${fieldValue(bean:account, field:'lastName')}</td>
              
            </tr>

            <tr class="prop">
              <td valign="top" class="name">Country Code:</td>
              
              <td valign="top" class="value">${fieldValue(bean:account, field:'countryCode')}</td>
              
            </tr>

            <tr class="prop">
              <td valign="top" class="name">City:</td>
              
              <td valign="top" class="value">${fieldValue(bean:account, field:'city')}</td>
              
            </tr>

            <tr class="prop">
              <td valign="top" class="name">State:</td>
              
              <td valign="top" class="value">${fieldValue(bean:account, field:'state')}</td>
              
            </tr>
            
            <tr class="prop">
              <td valign="top" class="name">Organization:</td>
              
              <td valign="top" class="value">${fieldValue(bean:account, field:'organization')}</td>
              
            </tr>
            
            <tr class="prop">
              <td valign="top" class="name">Organization Unit:</td>
              
              <td valign="top" class="value">${fieldValue(bean:account, field:'organizationUnit')}</td>
              
            </tr>

            <tr class="prop">
              <td valign="top" class="name">Private Key:</td>
              
              <td valign="top" class="value">
                <g:if test="${account.privateKeyStr}">
                  <a href="${createLink( action : 'download' , params : [ q : 'privateKey' ] )}">Download</a>
                </g:if>
                <g:else>
                  <b>N/A</b>
                </g:else>
              </td>
              
            </tr>

            <tr class="prop">
              <td valign="top" class="name">Public Key:</td>
              
              <td valign="top" class="value">
                <g:if test="${account.publicKeyStr}">
                  <a href="${createLink( action : 'download' , params : [ q : 'publicKey' ] )}">Download</a>
                </g:if>
                <g:else>
                  <b>N/A</b>
                </g:else>
              </td>
              
            </tr>

            <tr class="prop">
              <td valign="top" class="name">Certificate:</td>
              
              <td valign="top" class="value">
                <g:if test="${account.certificateStr}">
                  <a href="${createLink( action : 'download' , params : [ q : 'certificate' ] )}">Download</a>
                </g:if>
                <g:else>
                  <b>N/A</b>
                </g:else>
              </td>
              
            </tr>
            
          </tbody>
        </table>
      </div>
      <div class="buttons">
        <g:form>
          <input type="hidden" name="id" value="${account?.id}" />
          <g:if test="${!account.certificateStr}">
            <span class="button"><g:actionSubmit  value="Generate" /></span>
          </g:if>
          <span class="button"><g:actionSubmit class="edit" value="Edit" /></span>
          <g:if test="${account.id != 1}">
            <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
          </g:if>
        </g:form>
      </div>
    </div>
  </body>
</html>
