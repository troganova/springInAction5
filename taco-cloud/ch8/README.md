
-----------------------------
Адрес для проверки jwt токенов
-----------------------------
https://jwt.io/

-----------------------------
Запрос кода авторизации с подтверждением пользователем
-----------------------------
.scope(OidcScopes.OPENID) убрать если запускать напрямую, если через админ, то вернуть
http://localhost:9000/oauth2/authorize?response_type=code&client_id=taco-admin-client&redirect_uri=http://127.0.0.1:9090/login/oauth2/code/taco-admin-client&scope=writeIngredients+deleteIngredients
http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=taco-admin-client&scope=writeIngredients%20deleteIngredients&state=YfgmZNtM9hKloiptLiIlSeaZUiOVcpcIbJsUIFV33jU%3D&redirect_uri=http://127.0.0.1:9090/login/oauth2/code/taco-admin-client
http://127.0.0.1:9000/oauth2/authorize?response_type=code&client_id=taco-admin-client&scope=writeIngredients%20deleteIngredients%20openid&state=ildziAeDVYPjsO1AqpRANDIk6zpQ2dZ4TMQQBWydv9w%3D&redirect_uri=http://127.0.0.1:9090/login/oauth2/code/taco-admin-client&nonce=NjiqhRo3SQjqlsG9L_yNDtPF-eUAQHXjFHoeKWogm5c
http://127.0.0.1:9090/login/oauth2/code/taco-admin-client?code=8VNqHZiL7ApsntdC-04UGcmPHORnx8eaPxO9JJdTi0bWGmfHBcMkF26DoRyu5cuC66nDopQnmHtUf3hFQ1ZspJ-qR4hxnZUWqMVCeRlHSBup4ZlI5uLTFDgzRl3tKR4E

-----------------------------
Запрос токена по коду авторизации
-----------------------------
curl localhost:9000/oauth2/token -H"Content-type: application/x-www-form-urlencoded" -d"grant_type=authorization_code" -d"redirect_uri=http://127.0.0.1:9090/login/oauth2/code/taco-admin-client" -d"code=tP8TMbLOFS4Sz5vYZzI-j6-nqT6EnBbQWVEf9DgEwjpMQTMki_X4_iIeB0R5d2I0Tq7ewTtH8jK0Mla7MoM3yb4qtscQwIhE2VGvFSVWurQ82SC796ZAmVy-ibIb_cU3" -u taco-admin-client:secret

{"access_token":"eyJraWQiOiI0MjgxNTU2ZS02NmYzLTRkYTctYTJjMS0yMjUwZDkxMDM1MmEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0YWNvY2hlZiIsImF1ZCI6InRhY28tYWRtaW4tY2xpZW50IiwibmJmIjoxNzUxODg3MjI5LCJzY29wZSI6WyJkZWxldGVJbmdyZWRpZW50cyIsIndyaXRlSW5ncmVkaWVudHMiXSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDAwIiwiZXhwIjoxNzUxODg3NTI5LCJpYXQiOjE3NTE4ODcyMjksImp0aSI6IjUxZGRiMDM3LWIxYjEtNDAwYS1iMDI0LWI2MGNkNTJmZWYyMSJ9.R-_myRRUexjQHcg0biJDZkCE4HBWwIQBgfyjr5WaQfcTQ3N5xiYB1MeFzAUVEZrv6t7tttGY09-dIYOGQEINsQskoMM3eeMoJiQqrWrBDh9bMinFYN4WS7ZiCecl_zlVKdnwJ0XlV4Ds98do2RJPDmt8qDUxiDmqIiV6UnrOEwkE24LDZt7aPFSsmGfJBUs7ealgque1NUIIhcdC-_SKzYXoCnnP9oxcMiK8dz-3OCH0gwZd0i1RRjsmswn5CYES4tu_0T3Z44EKdo9l136TRfRU2oQtX4wmKBGJzNOZdq1aybGVU3d_megQDEqotZrVXK5vn0vJjxx9Y7DMp7J05A",
"refresh_token":"-I_z1OiSzKZR00fIh6mMekK8Hry7_GgvOp9g2WBTVRAdiLJOMJJmN2zf9fbEV85wkXI5OxRFrvCNZ41oTahMg8wl2FZ9kgANq8Z0KHpG2SRY1teqv1Q85UY5pDPp1KPs",
"scope":"deleteIngredients writeIngredients",
"token_type":"Bearer",
"expires_in":299}

-----------------------------
Запрос ингредиентов с токеном
-----------------------------
curl localhost:8080/api/ingredients -H"Content-type: application/json" -H"Authorization: Bearer eyJraWQiOiI0MjgxNTU2ZS02NmYzLTRkYTctYTJjMS0yMjUwZDkxMDM1MmEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0YWNvY2hlZiIsImF1ZCI6InRhY28tYWRtaW4tY2xpZW50IiwibmJmIjoxNzUxODg3MjI5LCJzY29wZSI6WyJkZWxldGVJbmdyZWRpZW50cyIsIndyaXRlSW5ncmVkaWVudHMiXSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDAwIiwiZXhwIjoxNzUxODg3NTI5LCJpYXQiOjE3NTE4ODcyMjksImp0aSI6IjUxZGRiMDM3LWIxYjEtNDAwYS1iMDI0LWI2MGNkNTJmZWYyMSJ9.R-_myRRUexjQHcg0biJDZkCE4HBWwIQBgfyjr5WaQfcTQ3N5xiYB1MeFzAUVEZrv6t7tttGY09-dIYOGQEINsQskoMM3eeMoJiQqrWrBDh9bMinFYN4WS7ZiCecl_zlVKdnwJ0XlV4Ds98do2RJPDmt8qDUxiDmqIiV6UnrOEwkE24LDZt7aPFSsmGfJBUs7ealgque1NUIIhcdC-_SKzYXoCnnP9oxcMiK8dz-3OCH0gwZd0i1RRjsmswn5CYES4tu_0T3Z44EKdo9l136TRfRU2oQtX4wmKBGJzNOZdq1aybGVU3d_megQDEqotZrVXK5vn0vJjxx9Y7DMp7J05A" -d'{"id":"FISH","name":"Stinky Fish", "type":"PROTEIN"}'

-----------------------------
Обновление токена по refresh_token
-----------------------------
curl localhost:9000/oauth2/token -H"Content-type: application/x-www-form-urlencoded" -d"grant_type=refresh_token&refresh_token=-I_z1OiSzKZR00fIh6mMekK8Hry7_GgvOp9g2WBTVRAdiLJOMJJmN2zf9fbEV85wkXI5OxRFrvCNZ41oTahMg8wl2FZ9kgANq8Z0KHpG2SRY1teqv1Q85UY5pDPp1KPs" -u taco-admin-client:secret

{"access_token":"eyJraWQiOiI0MjgxNTU2ZS02NmYzLTRkYTctYTJjMS0yMjUwZDkxMDM1MmEiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0YWNvY2hlZiIsImF1ZCI6InRhY28tYWRtaW4tY2xpZW50IiwibmJmIjoxNzUxODg3NzE1LCJzY29wZSI6WyJkZWxldGVJbmdyZWRpZW50cyIsIndyaXRlSW5ncmVkaWVudHMiXSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDAwIiwiZXhwIjoxNzUxODg4MDE1LCJpYXQiOjE3NTE4ODc3MTUsImp0aSI6IjZmYWZhZmYxLTY0ZTktNDMwZS05ZTEwLTAzOTBiMzJkYjliZSJ9.ZdsLAoNfoGdKGXYd_7d-QuMOO9oteMtG7hMl-EaD1dXXVwwedNr1TT8dUN-cEAMDhavu3I4snrXGajzxk7WrZjlDpi6_wZkrJh9OAas7GOU1M7Tuh7i0MAItXPd4EhkLdx1-BP5kZKlVVNZ9rtzzVe0PE9MPC1KtaJMGBr62id7CxFuJZDQPT-ONRbWqc9yxfyi-m7YBLJudXXkIgzb_pukmFzroQyaxVsNBeFJNgr70q44m4GMFQVyjm_y4i1vBRJPJzzJmGL_qWgk20iYJ337mjv-McXonAtC6uLq5IcQeQpnl0lFPlq6iW7oQLOIW56I0Hp9VAt0nuGWeADOwxg",
"refresh_token":"-I_z1OiSzKZR00fIh6mMekK8Hry7_GgvOp9g2WBTVRAdiLJOMJJmN2zf9fbEV85wkXI5OxRFrvCNZ41oTahMg8wl2FZ9kgANq8Z0KHpG2SRY1teqv1Q85UY5pDPp1KPs","scope":"deleteIngredients writeIngredients",
"token_type":"Bearer",
"expires_in":299}



curl localhost:8080/api/ingredients -H"Content-type: application/json" -d'{"id":"CRKT", "name":"Legless Crickets", "type":"PROTEIN"}'

curl localhost:8080/api/ingredients -H"Content-type: application/json" -d'{"id":"SHMP", "name":"Coconut Shrimp", "type":"PROTEIN"}' -H"Authorization: Bearer eyJraWQiOiI4NTEwMTU3MC04ODRhLTQyZWYtYWZkZC1kZmUzNTgzNzk1MzIiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0YWNvY2hlZiIsImF1ZCI6InRhY28tYWRtaW4tY2xpZW50IiwibmJmIjoxNzUxODkxODI2LCJzY29wZSI6WyJkZWxldGVJbmdyZWRpZW50cyIsIndyaXRlSW5ncmVkaWVudHMiXSwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDAwIiwiZXhwIjoxNzUxODkyMTI2LCJpYXQiOjE3NTE4OTE4MjYsImp0aSI6IjFhN2VhYzcwLTE4ZjYtNGMwMC04NzQ4LTdiMTRlZDE0NGE3YiJ9.ZEm6FrdEmmxzThHLx34ldcwkNpqWBPUUcQxJU28h3WLi-PV4XTobRCsrwHBDpEx31kJ1GEIpHS4DSiMqhlGoPT2EXFsTSFzLD1RqMomfK9E6y58dmyoPKI2YPM53DAFno2FX1bhz5uYYayMTzpXi32aqA_jVOCRrb_tjYVZXrp7UvPy9X8rT8KnYm3G__42EYk6rM3DrlzfOpgGFDR4XLxaMGP_nvMz1T2aQIJHsbdoS48dP4va57w0IKgQuTT0IsGSumpIRqhegeGKMpCazZ1wZ2geYqrKzbud6HD2TU0_xHBlo6IkGQU6B_i3PQ4MzqbSgNxVgeRfczUCxIZlKfg"


curl -X DELETE http://localhost:8080/api/ingredients/SHMP -H "Authorization: Bearer YOUR_ACCESS_TOKEN"


curl -u taco-admin-client:secret -d "grant_type=authorization_code&code=XXX&redirect_uri=http://127.0.0.1:9090/login/oauth2/code/taco-admin-client" http://127.0.0.1:9000/oauth2/token



curl -v -u taco-admin-client:secret -X POST http://localhost:9000/oauth2/token -d grant_type=authorization_code -d code=5ZMc8IUkL_xQtycHSffzC_a60lLNqqTTfm6qMq1-dlBhmkszjsB7RFbgJ3oFJn_zeGD0rlGIIZ7rRlMhBIpQJsuQPAL1P5urEijWapYqKb3GxaLP3OtvIcta56u-BaoZ -d redirect_uri=http://127.0.0.1:9090/login/oauth2/code/taco-admin-client
