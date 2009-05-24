import java.security.*
import java.security.cert.*
import org.bouncycastle.x509.*
import org.bouncycastle.jce.provider.BouncyCastleProvider
import javax.security.auth.x500.*

import sun.misc.BASE64Decoder
import sun.misc.BASE64Encoder
import sun.security.x509.X500Name

class BootStrap {

    def accountService
    
    def init = { servletContext ->

                 Security.addProvider( new BouncyCastleProvider() )

                 def ca = Account.get( 1 )
                 if( ! ca )
                 {
                     log.debug "Account 'ca' doesn't exist, creating a new one"
                     ca = new Account( username : "ca" ,
                                  password : "ca" ,
                                  firstName : "Certificate" ,
                                  lastName : "Authority" ,
                                  organizationUnit : "CA" ,
                                  organization : "CA" ,
                                  city : "Cairo" ,
                                  state : "Cairo" ,
                                  countryCode : "EG" ,
                                ).save()
                     accountService.generateAccount( ca )
                 }
                 log.debug "Bootstrap done !"
               }
    def destroy = {
    }
} 