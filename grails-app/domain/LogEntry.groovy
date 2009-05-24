class LogEntry {
    
    long id
    long version
    
    Date dateCreated
    
    String message
    
    static mapping = {
        message type : 'text'
    }
}
