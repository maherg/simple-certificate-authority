class LogEntryController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    // def allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        if(!params.max) params.max = 20
        params.sort = 'dateCreated'
        params.order = 'desc'
        [ logEntryInstanceList: LogEntry.list( params ) ]
    }

    def empty = {
        LogEntry.executeUpdate( "delete LogEntry le" )
        flash.message = "The log has been emptied"
        redirect( action : 'list' )
    }
}
