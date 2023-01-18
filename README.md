# test-mail-connection
__*Simple standalone app to test connections to ms mail server using OAuth2 authentication*__
#### execution
java -jar test-mail-connection-1.0-SNAPSHOT-jar-with-dependencies.jar D:\\...some path...\\credentials.properties
#### credentials
>host=outlook.office365.com
><br/>port=993
><br/>username=login@example.com
><br/>client_id=111111111-1111-1111-1111-111111111111
><br/>client_secret=21F8Q~1fz2~1LN156TkrZ4p4AAd9enJdCP_e2c11
><br/>scope=https://outlook.office365.com/.default
><br/>grant_type=client_credentials
><br/>token_uri=https://example.com/token
><br/>mail.debug.auth=true
#### details
[To register an app](https://learn.microsoft.com/en-us/graph/auth-register-app-v2). 
When you register your app you need to get client_id and client_secret.
You can find these parameters [here](https://portal.azure.com/#view/Microsoft_AAD_RegisteredApps/ApplicationsListBlade).
To get token uri you need to find {tenant}. URI - https://login.microsoftonline.com/{tenant}/oauth2/v2.0/token. 
You may find {tenant} in your app settings. To find scopes and permissions [here](https://learn.microsoft.com/en-us/graph/permissions-reference)