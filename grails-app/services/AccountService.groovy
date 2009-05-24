// 
import java.security.*
import java.security.spec.*
import java.security.cert.*
import org.bouncycastle.x509.*
import org.bouncycastle.jce.*
import javax.security.auth.x500.*
import javax.crypto.*

import sun.misc.BASE64Decoder
import sun.misc.BASE64Encoder
import org.springframework.web.context.request.RequestContextHolder as RCH 


class AccountService
{
    static expose = [ 'xfire' ]
    
    // static scope = 'session'
    
    boolean transactional = true

    public Account generateAccount( Account account )
    {
        log.debug "====== AccountService.generateAccount() Started ======"
        if( account && account.username && account.password )
        {
            new LogEntry( message : "${caller().remoteHost} --> CA : Requested to generate an account for ${account.username}" ).save()
            // Check if the account already exists
            def existingAccount = Account.findByUsername( account.username )
            if( existingAccount && existingAccount.certificateStr )
            {
                log.debug "Attempted to generate for an existing generated account, returning gracefully..."
                new LogEntry( message : "CA -->${caller().serverName} : Account already exists !" ).save()
                return account
            }
            def username = account.username

            log.debug "Going to generate a new account for $username"
            
            log.debug "Generating new keys..."

            def privKey = null
            def pubKey = null
            
            // Generate new keys
            def keyPair = this.generateKeyPair()
            privKey = keyPair.getPrivate()
            pubKey = keyPair.getPublic()
            
            log.debug "Generated private key in ${privKey.getFormat()} format using ${privKey.getAlgorithm()} algorithm ( ${privKey.getEncoded().size()} bytes )"
            log.debug "Generated public key in ${pubKey.getFormat()} format using ${privKey.getAlgorithm()} algorithm ( ${pubKey.getEncoded().size()} bytes )"
                
            // Save them in the account
            account.privateKeyStr = new BASE64Encoder().encode( privKey.getEncoded() )
            account.publicKeyStr = new BASE64Encoder().encode( pubKey.getEncoded() )

            // Generate an appropriate certificate
            def cert = null

            log.debug "Generating a new certificate..."

            if( account.id == 1 )
            {
                log.debug "Account '$username' is the certificate authority, generating certificate type V1"
                // Generate a root certificate
                def certGen = new X509V1CertificateGenerator()
                cert = this.generateCertificate( account , pubKey , certGen , account , privKey ) // Generate with own private key
            }
            else
            {
                log.debug "Account '$username' is a client, generating certificate type V3"
                // Get root's private key
                def ca = Account.get( 1 )
                def caPrivKey = generateKeyFromString( ca.privateKeyStr , 'private' )

                log.debug "Acquired the CA's private key, generating the client's certificate..."

                // Generate a client's certificate
                def certGen = new X509V3CertificateGenerator()
                cert = this.generateCertificate( account , pubKey , certGen , ca , caPrivKey )
            }

            account.certificateStr = new BASE64Encoder().encode( cert.getEncoded() )

            log.debug "Storing the certificate as text ${account.certificateStr.size()} chars"

            log.debug "Account '$username' has a new certificate now"

            // Save the account to DB with the updated changes

            account.save( flush : true )
            
            new LogEntry( message : "CA --> ${caller().serverName} : Account generated with keys & certificate successfully" ).save()
            return account
        }
        else
        {
            return null
        }
    }

    public Account obtainAccount( String username , String password )
    {
        log.debug "====== AccountService.obtainAccount() Called By  ======"
        if( username && password )
        {
            new LogEntry( message : "${caller().remoteHost} --> CA : Requested to obtain the account of ${username}" ).save()
            def account = Account.findByUsernameAndPassword( username , password )
            if( account )
            {
                log.debug "Obtained account $username, sending it to WS client"
                new LogEntry( message : "CA --> ${caller().serverName} : Account retrieved, sending to client" ).save()
                return account
            }
            else
            {
                log.debug "Failed to find an account $username , $password"
                new LogEntry( message : "CA --> ${caller().serverName} : Account doesn't exist" ).save()
            }
        }
        return null
    }

    public String obtainCertificate( String username )
    {
        log.debug "====== AccountService.obtainCertificate() Started ======"
        if( username )
        {
            def account = Account.findByUsername( username )
            if( account && account.certificateStr )
            {
                new LogEntry( message : "${caller().remoteHost} --> CA : Requested to obtain the certificate of ${username}" ).save()
                log.debug "Obtained certificate for $username, sending it to WS client"
                return account.certificateStr
            }
            else
            {
                new LogEntry( message : "CA --> ${caller().serverName} : Account doesn't exist" ).save()
                log.debug "Failed to find an account $username or a pre-generated certificate for $username"
            }
        }
        return null
    }

    public boolean accountExists( String username , String password )
    {
        log.debug "====== AccountService.accountExists() Started ======"
        if( username && password )
        {
            new LogEntry( message : "${caller().remoteHost} --> CA : Requested to check if acount${username} exists" ).save()
            def exists = Account.findByUsernameAndPassword( username , password ) != null
            if( exists )
            {
                log.debug "Found an account for $username"
                new LogEntry( message : "CA --> ${caller().serverName} : Account exists" ).save()
                return true
            }
            else
            {
                log.debug "Failed to find account for $username"
                new LogEntry( message : "CA --> ${caller().serverName} : Account doesn't exist" ).save()
                return false
            }
        }
        return false
    }

    public String canYouHearMe()
    {
        return "Loud and Clear ;-)"
    }

    boolean verifyCertificate( String certStr )
    {
        log.debug "====== AccountService.verify() Started ======"
        try
        {
            new LogEntry( message : "${caller().serverName} --> CA : Verify certificate supplied..." ).save()
            log.debug "Going to verify certificate ${certStr.size()} chars..."
            def certBytes = new BASE64Decoder().decodeBuffer( certStr )
            log.debug "Parsing certificate..."

            def cf = CertificateFactory.getInstance( "X.509" , "BC" )
            def cert = cf.generateCertificate( new BufferedInputStream( new ByteArrayInputStream( certBytes ) ) )

            // CA Cert
            def ca = Account.get( 1 )
            def caCertBytes = new BASE64Decoder().decodeBuffer( ca.certificateStr )
            def caCert = cf.generateCertificate( new BufferedInputStream( new ByteArrayInputStream( caCertBytes ) ) )
            
            def issuerPrincipal = cert.getIssuerX500Principal()
            def caPrincipal = caCert.getSubjectX500Principal()
            
            log.info "Received certificate's issuer is '${issuerPrincipal.getName()}'"
            
            // Get the ca account
            
            if( issuerPrincipal.equals( caPrincipal ) )
            {
                log.debug "Certificate verified and has been generated by the 'ca' account"
                new LogEntry( message : "CA --> ${caller().serverName} : Certificate verified & generated by the CA" ).save()
                return true
            }
            else
            {
                log.debug "Certificate has NOT been verified as generated by the 'ca' account"
                new LogEntry( message : "CA --> ${caller().serverName} : Certificate NOT verified" ).save()
                return false
            }
            
        }
        catch( e )
        {
            log.error( "An error occurred while parsing the certificate ${e.message}" )
            new LogEntry( message : "CA --> ${caller().serverName} : Error parsing the supplied certificate" ).save()
            return false
        }
    }

    public String encrypt( String plainText , Account account , String keyType )
    {
        // Get the Key from the supplied args
        def key = null
        switch( keyType )
        {
        case 'private':
            key = generateKeyFromString( account.privateKeyStr , 'private' )
            break;
        case 'public':
            key = generateKeyFromString( account.publicKeyStr , 'public' )
            break;
        }

        // Initialize the Cipher
        def cipher = Cipher.getInstance( "RSA" , "BC" )
        cipher.init( Cipher.ENCRYPT_MODE , key )

        log.debug "Retrieved an initialized cipher instance, encrypting..."

        // Encrypt
        def plainBytes = plainText.getBytes()

        def cipherBytes = cipher.doFinal( plainBytes )
        def cipherText = new BASE64Encoder().encode( cipherBytes )

        log.debug "Encrypted the plaintext ${plainBytes.size()} bytes to ciphertext ${cipherBytes.size()} bytes"

        return cipherText
    }

    public String decrypt( String cipherText , Account account , String keyType )
    {
        // Get the Key from the supplied args
        def key = null
        switch( keyType )
        {
        case 'private':
            key = generateKeyFromString( account.privateKeyStr , 'private' )
            break;
        case 'public':
            key = generateKeyFromString( account.publicKeyStr , 'public' )
            break;
        }

        // Initialize the Cipher
        def cipher = Cipher.getInstance( "RSA" , "BC" )

        cipher.init( Cipher.DECRYPT_MODE , key )

        log.debug "Retrieved an initialized cipher instance, decrypting..."

        // Decrypt
        def cipherBytes = new BASE64Decoder().decodeBuffer( cipherText )
        cipher.update( cipherBytes )

        def plainBytes = cipher.doFinal()
        def plainText = new String( plainBytes )

        log.debug "Decrypted the ciphertext ${cipherBytes.size()} bytes to plaintext ${plainBytes.size()} bytes"
        
        return plainText
    }

    private def generateCertificate( account , subjectPubKey , certgen  , caAccount , issuerPrivKey )
    {
        log.debug "Preparing the certificate generator : $certgen"
        // Prepare the generator
        certgen.setSerialNumber( BigInteger.valueOf( System.currentTimeMillis() ) )
        certgen.setIssuerDN( new X509Principal( caAccount.toString() ) )
        certgen.setSubjectDN( new X509Principal( account.toString() ) )
        certgen.setNotBefore( new Date( System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30 ) )
        certgen.setNotAfter( new Date( System.currentTimeMillis() + ( 1000L * 60 * 60 * 24 * 30 ) ) )
        certgen.setPublicKey( subjectPubKey )
        certgen.setSignatureAlgorithm( "SHA1withRSA" )

        log.debug "Going to generate the certificate..."
        
        // Generate the certificate
        def cert = certgen.generate( issuerPrivKey , "BC" )

        // Validity checks

        def now = new Date()
        log.debug "Validating certificate : $now"
        cert.checkValidity( now )

        log.debug "Certificate Valid !"

        log.debug "Verifying certificate with public key : $subjectPubKey"

//        cert.verify( subjectPubKey , "BC" )

//        log.debug "Certificate Verified !"

        log.debug "Certificate OK, length ( ${cert.getEncoded().size()} ) returning it..."
        
        return cert
    }

    // TODO : requestCertificate( ... )

    private def generateKeyPair()
    {
        // Get the key generator
        def keyGen = KeyPairGenerator.getInstance( "RSA" , "BC" )
        keyGen.initialize( 1024 )
        
        // Generate the keypair
        def keyPair = keyGen.generateKeyPair()

        return keyPair
    }

    private def generateKeyFromString( keyStr , type )
    {
        // Convert the String to a byte array
        def keyBytes = new BASE64Decoder().decodeBuffer( keyStr )
        // Convert the byte array to an appropriate {Private|Public}Key
        def keyFactory = KeyFactory.getInstance( "RSA" , "BC" )

        def key = null
        type = type.toLowerCase()

        switch( type )
        {
        case 'private':
            def keySpec = new PKCS8EncodedKeySpec( keyBytes )
            key  = keyFactory.generatePrivate( keySpec )
            break;
        case 'public':
            def keySpec = new X509EncodedKeySpec( keyBytes )
            key  = keyFactory.generatePublic( keySpec )
            break;
        }
        return key
    }
    
    private def caller()
    {
        def requestAttrs = RCH.currentRequestAttributes().request
        return [ remoteHost : requestAttrs.remoteHost ,
                 serverName : requestAttrs.serverName ,
               ]
    }
}
