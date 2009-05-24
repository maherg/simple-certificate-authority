<div class="nav">
  <span class="menuButton"><g:link class="home" controller="account" action="login">Home</g:link></span>
  <span class="menuButton"><g:link class="verify" controller="account" action="verify">Verify</g:link></span>
  <g:if test="${session.account}">
    <span class="menuButton"><g:link class="show" controller="account" action="show">My Account</g:link></span>
    <span class="menuButton"><g:link class="encrypt" controller="account" action="encrypt">Encrypt/Decrypt</g:link></span>
    <g:if test="${session.account.id == 1}">
      <span class="menuButton"><g:link class="list" controller="account" action="list">Account List</g:link></span>      
      <span class="menuButton"><g:link class="logs" controller="logEntry" action="list">Logs</g:link></span>
    </g:if>
    <span class="menuButton"><g:link class="logout" controller="account" action="logout">Logout</g:link></span>
  </g:if>
  <g:else>
    <span class="menuButton"><g:link class="create" controller="account" action="create">New Account</g:link></span>
  </g:else>
</div>
