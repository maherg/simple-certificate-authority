class AccountTests extends GroovyTestCase {

    def accountService
    
    void testAddNewAccountAndGenerate() {
        def acc = new Account( username : 'maher',
                               password : 'maher'
                             ).save()
        accountService.generate( acc )
    }
}
