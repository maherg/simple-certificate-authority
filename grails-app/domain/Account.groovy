import java.security.*
import java.security.spec.*
import java.security.cert.*
import org.bouncycastle.x509.*
import org.bouncycastle.jce.*
import javax.security.auth.x500.*

import sun.misc.BASE64Decoder
import sun.misc.BASE64Encoder

class Account {

    long id
    long version

    String username
    String password

    String firstName = "Unknown"
    String lastName = "Unknown"
    String organizationUnit = "Unknown"
    String organization = "Unknown"
    String city = "Unknown"
    String state = "Unknown"
    String countryCode = "Unknown"

    String privateKeyStr
    String publicKeyStr
    String certificateStr
    
    static constraints = {
        username( blank : false , unique : true )
        password( blank : false )
        privateKeyStr( nullable : true )
        publicKeyStr( nullable : true )
        certificateStr( nullable : true )
    }

    String toString()
    {
        return "C=$countryCode, CN=$firstName $lastName, O=$organization, OU=$organizationUnit, L=$city, ST=$state"
    }
}
