from clientBank.EnumPython import Status

'''
This class is used to define the structure of the Response. It containes status which is used to 
indicate the status of the execution of the requested service and message which contains additional
information about the execution of the service.
'''


class Response:
    def __init__(self, status, message):
        self.status = status
        self.message = message

    def __repr__(self) -> str:
        print(self.message)

    def __str__(self) -> str:
        return(self.message)
