# test-storeWrapper-connection
Simple standalone app to test connections to the storeWrapper server using OAuth2 authentication
#### to execute: java -jar appname.jar D:\\...some path...\\credentials.properties
## credentials.properties:
##### host=example.com
##### port=123
##### username=login@example.com
##### client_id=111111111-1111-1111-1111-111111111111
##### client_secret=abcdf...
##### scope=...
##### grant_type=client_credentials
##### token_uri=https://example.com/token
## details:
### MS:
##### to register an app - https://learn.microsoft.com/en-us/graph/auth-register-app-v2
##### when you register an app you need to get client_id and cleint_secret
##### you can find these parameters here https://portal.azure.com/#view/Microsoft_AAD_RegisteredApps/ApplicationsListBlade
##### to get token uri you need to find {tenant} (POST https://login.microsoftonline.com/{tenant}/oauth2/v2.0/token)
##### you may find {tenant} in your app settings
##### you may find scopes and permisions here https://learn.microsoft.com/en-us/graph/permissions-reference and your app
### Google:
##### to find credentials or create new credentials here https://console.cloud.google.com/apis/credentials
##### there are other settings