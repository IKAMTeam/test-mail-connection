# test-mail-connection
Simple standalone app to test connections to the mail server using OAuth2 authentication
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