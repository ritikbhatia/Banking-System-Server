from clientBank.EnumPython import Status

class Response:
    def __init__(self, status, message) -> None:
         self.status = status
         self.message = message
    
    def __repr__(self) -> str:
        print(self.message)
        