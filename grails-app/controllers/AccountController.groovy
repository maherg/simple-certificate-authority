import merchantservice.util.security.hash.MD5

class AccountController
{
    
    def index = { redirect(action:login,params:params) }

    def accountService

    // the delete, save and update actions only accept POST requests

    def allowedMethods = [ delete: [ 'POST' ] ,
                           save:   [ 'POST' ] ,
                           update: [ 'POST' ] ,
                           encrypt: [ 'POST' , 'GET' ] ,
                           decrypt: [ 'POST' , 'GET' ] ,
                         ]

    def beforeInterceptor = [ action : this.&auth , except : [ 'login' , 'verify' , 'create' , 'save' ] ]

    def auth = {
        if( ! session.account )
        {
            flash.message = "Please login first !"
            redirect( action : "login" )
            return false
        }
    }

    def list = {
        if(!params.max) params.max = 10
        [ accountInstanceList: Account.list( params ) ]
    }

    def login = {
        if( request.post )
        {
            def account = Account.findByUsernameAndPassword( params.username , MD5.encode( params.password ) )
            if( account )
            {
                session.account = account
                log.debug "Login successful , ${account.username}"
                redirect( action : show )
            }
            else
            {
                flash.message = "Failed to login with your username and password !"
                redirect( action : login , params : params )
            }
        }
        else
        {
            render( view : 'login' )
        }
    }

    def logout = {
        if( session.account )
        {
            session.account = null
            flash.message = "You have logged out successfully !"
            redirect( action : login , params : [:] )
        }
    }

    def show = {
        
        if( session.account.id == 1 && params.id && Account.exists( params.id ) )
        {
            return [ account : Account.get( params.id ) ]
        }
        return [ account : session.account ]
    }

    def encrypt = {
        if( request.post )
        {
            def plaintext = params.plaintext
            if( plaintext?.size() > 0 )
            {
                def ciphertext = accountService.encrypt( plaintext , session.account , params.key )
                render( view : 'crypt' , model : [ ciphertext : ciphertext , plaintext : "" ] )
            }
            else
            {
                flash.message = "Please enter the plain text to encrypt"
                redirect( action : 'encrypt' )
            }
        }
        else
        {
            render( view : 'crypt' )
        }
    }

    def decrypt = {
        if( request.post )
        {
            def ciphertext = params.ciphertext
            if( ciphertext?.size() > 0 )
            {
                def plaintext = accountService.decrypt( ciphertext , session.account , params.key )
                render( view : 'crypt' , model : [ ciphertext : "" , plaintext : plaintext ] )
            }
            else
            {
                flash.message = "Please enter the cipher text to decrypt"
                redirect( action : 'decrypt' )
            }
        }
        else
        {
            render( view : 'crypt' )
        }
    }

    def generate = {

        // Re-merge the previous account instance
        def account = session.account.merge()

        // Assign it again to the current session
        session.account = account

        try
        {
            accountService.generateAccount( account )
            flash.message = "Generated necessary keys and the certificate for your account"
        }
        catch( e )
        {
            log.error( e )
            flash.message = "An error occurred during the generation process"
        }
        finally
        {
            redirect( action : show )
        }
        
    }

    def download = {
        def q = params.q

        def account = session.account

        switch( q )
        {
        case 'privateKey':
            render account.privateKeyStr
            break;
        case 'publicKey':
            render account.publicKeyStr
            break;
        case 'certificate':
            render account.certificateStr
            break;
        default:
            break;
        }
    }

    def verify = {
        if( request.get )
        {
            render( view : 'verify' )
        }
        else
        {
            def certificate = params.certificate
            render( view : 'verify-results' ,
                    model : [ certificate : certificate ,
                              result : accountService.verifyCertificate( certificate ) ? "Verified" : "Not Verified"
                            ]
                  )
        }
    }

    def delete = {
        def accountInstance = Account.get( params.id )
        if(accountInstance) {
            accountInstance.delete()
            flash.message = "Account ${params.id} deleted"
            redirect(action:list)
        }
        else {
            flash.message = "Account not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def accountInstance = Account.get( params.id )

        if(!accountInstance) {
            flash.message = "Account not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ accountInstance : accountInstance ]
        }
    }

    def update = {
        def accountInstance = Account.get( params.id )
        if(accountInstance) {
            accountInstance.properties = params
            accountInstance.password = MD5.encode( accountInstance.password )
            if(!accountInstance.hasErrors() && accountInstance.save()) {
                flash.message = "Account ${accountInstance.username} updated"
                session.account = accountInstance
                redirect(action:show,id:accountInstance.id)
            }
            else {
                render(view:'edit',model:[accountInstance:accountInstance])
            }
        }
        else {
            flash.message = "Account not found with id ${params.id}"
            redirect(action:edit,id:params.id)
        }
    }

    def create = {
        def accountInstance = new Account()
        accountInstance.properties = params
        return ['accountInstance':accountInstance]
    }

    def save = {
        def accountInstance = new Account(params)
        accountInstance.password = MD5.encode( accountInstance.password )
        if(!accountInstance.hasErrors() && accountInstance.save()) {
            flash.message = "Account ${accountInstance.id} created"
            session.account = accountInstance
            redirect(action:show,id:accountInstance.id)
        }
        else {
            render(view:'create',model:[accountInstance:accountInstance])
        }
    }
}
