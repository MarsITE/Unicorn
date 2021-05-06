<!DOCTYPE html>
<html>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>

<body>
    <div align="center">
        <h3>Hello, ${email}!</h3>
        <p>You are registered on <a href="${client_url}">WorkSearch</a>. Thanks:)</p>
        <h6> Here you <i> <a href="${client_url}verify-email?token=${token}">confirmation link</a> </i> </h6>
    </div>

    <p>You need to improve your account by ${time_to_improve}.</p>

    <div align="left">
        <p><b>Administrator</b></p>
        <p>WorkSearch</p>
    </div>
</body>
<html>