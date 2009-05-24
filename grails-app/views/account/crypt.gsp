

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main" />
    <title>Encrypt/Decrypt</title>         
  </head>
  <body>
    <g:render template="header" />
    <div class="body">
      <h1>Encrypt/Decrypt</h1>
      <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
      </g:if>
      <g:form method="post" >
        <div class="dialog">
          <table>
            <tbody>
              
              <tr class="prop">
                <td valign="top" class="name">
                  <label for="plaintext">Plain Text :</label>
                </td>
                <td valign="top" class="value">
                  <textarea id="plaintext" class="plaintext" name="plaintext">${plaintext ? plaintext : ''}</textarea>
                </td>
              </tr>
              <tr class="prop">
                <td valign="top" class="name">
                  <label for="ciphertext">Cipher Text ( In Base64 ) :</label>
                </td>
                <td valign="top" class="value">
                  <textarea id="ciphertext" class="ciphertext" name="ciphertext">${ciphertext ? ciphertext : ''}</textarea>
                </td>
              </tr> 
              <tr class="prop">
                <td valign="top" class="name">
                  <label for="key">With Key :</label>
                </td>
                <td valign="top" class="value">
                  <select name="key" id="key" class="key">
                    <option value="private">Private Key</option>
                    <option value="public">Public Key</option>
                  </select>
                </td>
              </tr> 
            </tbody>
          </table>
        </div>
        <div class="buttons">
          <span class="button">
            <span class="button"><g:actionSubmit class="encrypt" value="Encrypt" /></span>
            <span class="button"><g:actionSubmit class="decrypt" value="Decrypt" /></span>
          </span>
        </div>
      </g:form>
    </div>
  </body>
</html>
