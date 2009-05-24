

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Create Account</title>         
    </head>
    <body>
      <g:render template="header" />
        <div class="body">
            <h1>Create Account</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${accountInstance}">
            <div class="errors">
                <g:renderErrors bean="${accountInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="username">Username:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:accountInstance,field:'username','errors')}">
                                    <input type="text" id="username" name="username" value="${fieldValue(bean:accountInstance,field:'username')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="password">Password:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:accountInstance,field:'password','errors')}">
                                    <input type="text" id="password" name="password" value="${fieldValue(bean:accountInstance,field:'password')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="city">City:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:accountInstance,field:'city','errors')}">
                                    <input type="text" id="city" name="city" value="${fieldValue(bean:accountInstance,field:'city')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="countryCode">Country Code:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:accountInstance,field:'countryCode','errors')}">
                                    <input type="text" id="countryCode" name="countryCode" value="${fieldValue(bean:accountInstance,field:'countryCode')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="firstName">First Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:accountInstance,field:'firstName','errors')}">
                                    <input type="text" id="firstName" name="firstName" value="${fieldValue(bean:accountInstance,field:'firstName')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="lastName">Last Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:accountInstance,field:'lastName','errors')}">
                                    <input type="text" id="lastName" name="lastName" value="${fieldValue(bean:accountInstance,field:'lastName')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="organization">Organization:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:accountInstance,field:'organization','errors')}">
                                    <input type="text" id="organization" name="organization" value="${fieldValue(bean:accountInstance,field:'organization')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="organizationUnit">Organization Unit:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:accountInstance,field:'organizationUnit','errors')}">
                                    <input type="text" id="organizationUnit" name="organizationUnit" value="${fieldValue(bean:accountInstance,field:'organizationUnit')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="state">State:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:accountInstance,field:'state','errors')}">
                                    <input type="text" id="state" name="state" value="${fieldValue(bean:accountInstance,field:'state')}"/>
                                </td>
                            </tr> 
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><input class="save" type="submit" value="Create" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
