class AccountPython:
    DEFAULT_ACCOUNT_NUMBER = -99

    def __init__(self, accountNumber, accountHolderName, password, currency, balance) -> None:
        self.accountNumber = accountNumber
        self.accountHolderName = accountHolderName
        self.password = password
        self.currency = currency
        self.balance = balance


    
